package com.example.proto4;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    SharedPreferences pref;
    static boolean isFirst;
    String desc_str;
    int count = 1;
    private final int REQUEST_BLUETOOTH_ENABLE = 100;
    private BottomSheetBehavior bottomSheetBehavior;
    private InputMethodManager imm;
    private LinearLayout tmap_main;
    private long time = 0;

    ConnectedTask mConnectedTask = null;
    static BluetoothAdapter mBluetoothAdapter;
    private String mConnectedDeviceName = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    static boolean isConnectionError = false;
    private static final String TAG = "BluetoothClient";

    private Button buttonSearch;
    private TextView speechTextView;
    private EditText searchBar;
    RecyclerView rv;

    private ArrayList<Dictionary> poiNameArr;
    private ArrayList<PathPoint> pointArr;
    private Adapter mAdapter;
    Dictionary data;
    PathPoint pPonit;
    private TMapData tData;
    private TMapView tmapView;
    private TMapPoint tPoint, currentpoint, endpoint, testpoint1, testpoint2, nextPoint;
    private TMapMarkerItem tItem;
    private TMapCircle tCircle;

    TextToSpeech tts, ttss, t1;
    String utteranceId=this.hashCode() + "";
    static TimerTask tt, tl, reset;
    Bitmap bit1,bit2,bit3;

    LocationManager lm;
    SpeechRecognizer sRecognizer;
    String userid;

    Intent i;
    String key = "";
    double latitude, longitude;
    float dist;
    float tl_min = 5;
    float bs_min = 5;
    float tl_dist, bs_dist;
    boolean speakedTL = false;
    boolean speakedBS = false;

    DBHelper helper;

    final Timer timer = new Timer();
    final Timer tl_timer = new Timer();
    final Timer rs_timer = new Timer();

    final Handler handler = new Handler(){
        public void handleMessage(Message message){
            Log.d("----", "검색 결과 핸들러");
            mAdapter.notifyDataSetChanged();
        }
    };

    public TimerTask timerTask(){
        TimerTask tempTask = new TimerTask() {
            @Override
            public void run() {
                changeDescript();
            }
        };
        return tempTask;
    }

    public TimerTask tl_timertask(){
        TimerTask tl_tempTask = new TimerTask() {
            @Override
            public void run() {
                distTest();
            }
        };
        return tl_tempTask;
    }

    public TimerTask resetTask(){
        TimerTask rs_tempTask = new TimerTask() {
            @Override
            public void run() {
                ttss.speak("", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                Log.e("---------", "TEST");
            }
        };
        return rs_tempTask;
    }

    final static private String url2 = "http://54.180.103.20:8000/getlocation.php";
    final static private String url = "http://192.168.123.101/php_conn.php";
    String myJSON;
    JSONArray location = null;
    ArrayList<HashMap<String,String>> locationList;
    HashMap<String,String> locations;
    String resultList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREA);
                tts.speak("길 찾기 페이지", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            }
        });
        Log.e("------", "create");
        bit1 = BitmapFactory.decodeResource(getResources(),R.drawable.traffic_black_18dp);
        bit2 = BitmapFactory.decodeResource(getResources(),R.drawable.bus_black_18dp);
        bit3 = BitmapFactory.decodeResource(getResources(),R.drawable.crosswalk);
        locationList = new ArrayList<HashMap<String, String>>();

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.design_bottom_sheet);

        tmap_main = (LinearLayout) findViewById(R.id.Tmap_main_layout);

        Intent loginI = getIntent();
        userid = loginI.getStringExtra("id");
        Log.e("userid", userid+"");

        helper = new DBHelper(this, "MapData.db", null, 1);
        pref = getSharedPreferences("IsFirst" , Activity.MODE_PRIVATE);
        isFirst = pref.getBoolean("isFirst", true);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(isFirst == true){ //최초 실행시 할 일
            Log.e("--------", "최초실행");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst", false);
            editor.commit();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH,
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);
            }

            helper.insert();
        }

        poiNameArr = new ArrayList<>();
        pointArr = new ArrayList<>();

        mAdapter = new Adapter(poiNameArr);
        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectConfirm(position);
            }
        });

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                onStop();
//                finish();
                mRecognizer.stopListening();
            }
            public void onSwipeLeft() {
                Intent i = new Intent(getApplicationContext(), ObjectRecognitionActivity.class);
                startActivity(i);
                onStop();
//                finish();
            }
            public void onSwipeBottom() {
            }

            public void speeched(){
                Log.e("--------","speeched: "+resultStr);
                if(resultStr.equals("메뉴")){
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);
//                    finish();
                    onStop();
                }
                else if(resultStr.equals("사물 인식")){
                    Intent i = new Intent(getApplicationContext(), ObjectRecognitionActivity.class);
                    startActivity(i);
//                    finish();
                    onStop();
                }
                else if(resultStr.equals("문자 인식")){
                    Intent i = new Intent(getApplicationContext(), CharacterRecognitionActivity.class);
                    startActivity(i);
//                    finish();
                    onStop();
                }
                else if(resultStr.equals("색상 인식")){
                    Intent i = new Intent(getApplicationContext(), ColorRecognitionActivity.class);
                    startActivity(i);
//                    finish();
                    onStop();
                }
                else if(resultStr.equals("빛 밝기")){
                    Intent i = new Intent(getApplicationContext(), BrightnessActivity.class);
                    startActivity(i);
//                    finish();
                    onStop();
                }
                else if(resultStr.equals("초기화")){
                    if(poiNameArr.size() >0){
                        poiNameArr.clear();
                        mAdapter.notifyDataSetChanged();
                        speakTTS("검색 목록을 초기화했습니다. 다시 검색해주세요");
                    }
                }
                else {
                    if(poiNameArr.size() == 0) {
                        searchDestination(resultStr);
                    }
                    else if(poiNameArr.size() > 0) {
                        for (int i = 0; i < poiNameArr.size(); i++) {
                            Log.e("------", poiNameArr.get(i).getPOI_name());
                            if(poiNameArr.get(i).getPOI_name().equals(resultStr)) {
                                speechTextView.setText(resultStr);

                                endpoint = poiNameArr.get(i).getPOI_latlng();
                                GetDirections();
                            }
                        }

                    }
                }
            }
        });

        ttss = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttss.setLanguage(Locale.KOREA);
            }
        });

        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                t1.setLanguage(Locale.KOREA);
            }
        });

        //음성 인식 intent 설정
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);   //활동 시작
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");   //음성 번역 언어
        sRecognizer = SpeechRecognizer.createSpeechRecognizer(this);    //새 SpeechRecognizer를 만드는 팩토리 메서드
        sRecognizer.setRecognitionListener(listener);   //리스너 설정

        //TMAP 호출
        final LinearLayout linearLayoutTmap = findViewById(R.id.TMapLayout);
        rv = (RecyclerView) findViewById(R.id.recycler_poi);
        rv.addItemDecoration(new DividerItemDecoration(this, 1));

        tData = new TMapData();

        tmapView = new TMapView(this);
        tmapView.setSKTMapApiKey("l7xxb116f439ac904ca683fb4a533412e093");
        linearLayoutTmap.addView(tmapView);
        tmapView.setIconVisibility(true);
        tmapView.setTrackingMode(true);
        tmapView.setSightVisible(true);
        tmapView.setCompassMode(true);
        tmapView.bringMarkerToFront(tItem);
        tmapView.setZoomLevel(18);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        searchBar = findViewById(R.id.search_bar);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dest = searchBar.getText().toString();
                searchDestination(dest);
                imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
            }
        });
        buttonSearch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        rv.setAdapter(mAdapter);
        speechTextView = findViewById(R.id.speechView);

        ListView mMessageListview = (ListView) findViewById(R.id.message_listview);

        mConversationArrayAdapter = new ArrayAdapter<>( this,android.R.layout.simple_list_item_1 );
        mMessageListview.setAdapter(mConversationArrayAdapter);

        Log.e( TAG, "Initalizing Bluetooth adapter...");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  //디바이스에서 블루투스를 지원하는지 체크합니다
        if (mBluetoothAdapter == null) {
            showErrorDialog("This device is not implement Bluetooth.");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {   //디바이스의 브루투스 기능이 활성화 되어있는지 체크합니다 활성화되어 있지 않다면 사용자에게 켜도록 요청합니다.
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
        }
        else {     // 블루투스가 활성화 되어 있다면 showPairedDevicesListDialog()메소드를 호출합니다.
            Log.d(TAG, "Initialisation successful.");
            showPairedDevicesListDialog();
        }

        getData(url2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("------", "start");
    }

    @Override
    protected void onResume(){
        super.onResume();
        tts.speak("길 찾기 페이지", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        Log.e("------", "resume");
        setGps();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("------", "pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("------", "stop");

        if(tts !=null){
            tts.stop();
        }

        if(ttss != null){
            ttss.stop();
        }

        if(t1 != null){
            t1.stop();
        }

        if(sRecognizer != null) {
            sRecognizer.stopListening();
        }
    }
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("------", "destroy");
        if(tts != null){
            tts.shutdown();
            tts = null;
        }

        if(ttss != null){
            ttss.shutdown();
        }

        if(t1 != null){
            t1.shutdown();
        }

        if(sRecognizer != null) {
            sRecognizer.destroy();
        }
    }

    public void speakTTS(String sentence) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String utteranceId=this.hashCode() + "";
            tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            tts.playSilentUtterance(3000, TextToSpeech.QUEUE_ADD, null);
        } else {
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, hashmap);
            tts.playSilentUtterance(3000, TextToSpeech.QUEUE_ADD, null);
        }
    }

    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {  //AsyncTask

        private BluetoothSocket mBluetoothSocket = null;
        private BluetoothDevice mBluetoothDevice = null;

        ConnectTask(BluetoothDevice bluetoothDevice) {
            // 시리얼 통신(SPP)를 하기 위한 RFCOMM 블루투스 소켓을 생성합니다.(ConnectTask)
            mBluetoothDevice = bluetoothDevice;
            mConnectedDeviceName = bluetoothDevice.getName();

            //SPP
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            try {
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);  //블루투스 소켓 생성
                Log.d( "Bluetooth", "create socket for "+mConnectedDeviceName);

            } catch (IOException e) {
                Log.e( "Bluetooth", "socket create failed " + e.getMessage());
            }

            //Toast.makeText(MainActivity.this, "connecting...", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // 주변 블루투스 디바이스 찾는 것을 중지합니다.
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                mBluetoothSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mBluetoothSocket.close();
                } catch (IOException e2) {
                    Log.e("Bluetooth", "unable to close() " +
                            " socket during connection failure", e2);
                }

                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSucess) {
            if ( isSucess ) {
                connected(mBluetoothSocket);
                //connected함수 실행 --> ConnectedTask(AsyncTask)실행
            }
            else{

                isConnectionError = true;  //error 트루
                Log.d( "Bluetooth",  "Unable to connect device");
                //showErrorDialog("Unable to connect device"); //연결 안됬을때 뜨는 다이로그 ( 기기가 켜지지 않았을때 )
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        showPairedDevicesListDialog();
                    }
                },5000);
            }
        }
    }

    // 블루투스 소켓을 성공적으로 생성했다면  ConnectedTask AsyncTask를 실행합니다.
    public void connected( BluetoothSocket socket ) {
        mConnectedTask = new ConnectedTask(socket);
        mConnectedTask.execute();
    }

    private class ConnectedTask extends AsyncTask<Void, String, Boolean> {

        private InputStream mInputStream = null;
        private OutputStream mOutputStream = null;
        private BluetoothSocket mBluetoothSocket = null;

        ConnectedTask(BluetoothSocket socket){

            mBluetoothSocket = socket;
            try {
                mInputStream = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e("Bluetooth", "socket not created", e );
            }
            Log.d( "Bluetooth", "connected to "+mConnectedDeviceName);
            Toast.makeText(MainActivity.this, "A.eye기기와 연결되었습니다.", Toast.LENGTH_SHORT).show();
            //t1.speak("A.eye기기와 연결되었습니다.", TextToSpeech.QUEUE_FLUSH, null);
            speakTTS("A.eye기기와 연결되었습니다.");
        }

        // 수신되는 문자열이 있으면 받아서 버퍼에 저장합니다
        @Override
        protected Boolean doInBackground(Void... params) {
            //publishProgress()호출하면 onProgressUpdate()를 자동호출하여 UI에 진행 업데이트

            byte [] readBuffer = new byte[1024];
            int readBufferPosition = 0;

            while (true) {

                if ( isCancelled() ) return false;
                try {
                    int bytesAvailable = mInputStream.available();
                    if(bytesAvailable > 0) {
                        byte[] packetBytes = new byte[bytesAvailable];
                        mInputStream.read(packetBytes);
                        for(int i=0;i<bytesAvailable;i++) {
                            byte b = packetBytes[i];
                            if(b == '\n')
                            {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                        encodedBytes.length);
                                String recvMessage = new String(encodedBytes, "UTF-8");
                                String[] resMsg_en = recvMessage.split(",");
                                int size = resMsg_en.length;
                                String conjc_ko="";
                                String[] resMsg_ko = new String[size];
                                String[] jong =
                                        { "", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ",
                                                "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ",
                                                "ㅍ", "ㅎ" };
//                                StringBuilder TTSMsg = new StringBuilder("");
                                String TTSMsg = "";
                                List<String> coco_labels = Arrays.asList("stairs","person", "bicycle","car", "motorcycle", "airplane" ,"bus", "train", "truck", "boat" , "traffic light","fire hydrant", "stop sign", "parking meter","bench","bird","cat","dog","horse","sheep","cow","elephant","bear","zebra","giraffe","backpack","umbrella","handbag","tie","suitcase","frisbee","skis","snowboard","sports ball","kite","baseball bat", "baseball glove","skateboard","surfboard","tennis racket","bottle","wine glass","cup","fork","knife","spoon","bowl","banana","apple","sandwich","orange","broccoli","carrot","hot dog","pizza","donut","cake","chair","couch","potted plant","bed","dining table","toilet","tv","laptop","mouse","remote","keyboard","cell phone","microwave","oven","toaster","sink","refrigerator","book","clock","vase","scissors","teddy bear","hair drier","toothbrush");
                                List<String> coco_korean = Arrays.asList("계단","사람","자전거","자동차","오토바이","비행기","버스","기차","트럭","보트","신호등","소화전","정지 신호","주차장미터기","벤치","새","고양이","강아지","말","양","소","코끼리","곰","얼룩말","기린","가방","우산","핸드백","넥타이","여행가방","원반","스키","스노보드","공","연","야구방망이","야구글로브","스케이트보드","서핑보드","테니스라켓","병","와인병","컵","포크","칼","숟가락","그릇","바나나","사과","샌드위치","오렌지","브로콜리","당근","핫도그","피자","도넛","케이크","의자","소파","화분","침대","식탁","변기","텔레비전","노트북","마우스","리모컨","키보드","휴대폰","전자레인지","오븐","토스트기","싱크대","냉장고","책","시계","꽃병","가위","곰인형","헤어드라이기","칫솔");

                                for (int k=0 ; k<resMsg_en.length; k++){
                                    for (int j=0 ; j<coco_labels.size(); j++){
                                        if ( resMsg_en[k].equals(coco_labels.get(j))){
                                            if(k==resMsg_en.length-1){
                                                resMsg_ko[k] = coco_korean.get(j);
//                                                conjc_ko=conjunctive.get(j);
                                            }
                                            else{
                                                resMsg_ko[k] = coco_korean.get(j);
                                            }
                                        }
                                    }
                                }

                                if(resMsg_ko[0]==null){
                                    TTSMsg = "다시 해주세요";
                                    t1.speak(TTSMsg, TextToSpeech.QUEUE_FLUSH, null);
                                }else{
                                    Map<String, Integer> check = new HashMap<>();
                                    Set<Map.Entry<String, Integer>> entries = check.entrySet();
                                    for (int k = 0; k < resMsg_ko.length; k++) {
                                        check.put(resMsg_ko[k], 0);  //map배열 생성/  중복제거
                                    }

                                    for(Map.Entry<String, Integer> entry : entries) {
                                        int cnt = 0;
                                        for(int j=0; j < resMsg_ko.length; j++) {
                                            if(entry.getKey().equals(resMsg_ko[j])) {
                                                cnt++;
                                            }
                                        }
                                        check.put(entry.getKey(), cnt);
                                        String key = entry.getKey();
                                        if(cnt > 4) TTSMsg+= " 많은"+key;
                                        else if(cnt >1) TTSMsg+= " 여러"+key;
                                        else TTSMsg+= " "+key;
                                    }
                                }

                                String addJong = TTSMsg.substring(TTSMsg.length()-1);
                                int tmp = (addJong.charAt(0) - 0xAC00) % 28;
                                if(!TTSMsg.equals("") && !TTSMsg.equals("다시 해주세요")) {
                                    if(!jong[tmp].equals("")){
                                        TTSMsg += "이 있습니다";
                                        t1.speak(TTSMsg, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                    else{
                                        TTSMsg += "가 있습니다";
                                        t1.speak(TTSMsg, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                }
                                readBufferPosition = 0;
                            }
                            else
                            {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (IOException e) {

                    Log.e("Bluetooth", "disconnected", e);
                    return false;  // doInBackground()메소드에서 작업이 끝나면 onPostExcuted()로 파라미터 리턴
                }
            }
        }

        @Override
        protected void onProgressUpdate(String... recvMessage) {

            mConversationArrayAdapter.insert(mConnectedDeviceName + ": " + recvMessage[0], 0);
        }

        @Override
        protected void onPostExecute(Boolean isSucess) { //AsyncTask가 끝날시 호출
            super.onPostExecute(isSucess);

            if ( !isSucess ) {
                closeSocket();
                Log.d(TAG, "Device connection was lost");
                isConnectionError = true;
                showErrorDialog("디바이스와 연결이 끊어졌습니다.");
            }
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);

            closeSocket();
        }

        void closeSocket(){

            try {

                mBluetoothSocket.close();
                Log.d("Bluetooth", "close socket()");

            } catch (IOException e2) {

                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }
        }
        // 문자열을 전송할 때 호출되어 집니다.
        void write(String msg){

            msg += "\n";

            try {
                mOutputStream.write(msg.getBytes());
                mOutputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Exception during send", e );
            }
        }
    }

    public void showPairedDevicesListDialog(){
        //페어링 되어 있는 블루투스 장치들의 목록을 보여줌
        //목록에서 블루투스 장치를 선택하면 선택한 디바이스를 인자로 하여 ConnectTaskAsyncTask 실행
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        final BluetoothDevice[] pairedDevices = devices.toArray(new BluetoothDevice[0]);

        if ( pairedDevices.length == 0 ){
            showQuitDialog( "No devices have been paired.\n"
                    +"You must pair it with another device.");
            return;
        }

        String[] items;
        items = new String[pairedDevices.length];
        for (int i=0;i<pairedDevices.length;i++) {
            items[i] = pairedDevices[i].getName();
            Log.d("Bluetooth", "item: "+pairedDevices[i].getName());
            if(pairedDevices[i].getName().equals("raspberrypi")){
                Log.d("Bluetooth", "connect?");
                ConnectTask task = new ConnectTask(pairedDevices[i]);
                task.execute();
            }

        }
    }

    public void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if ( isConnectionError  ) {
                    isConnectionError = false;
                    //finish();
                }
            }
        });
        builder.create().show();
        //에러가 뜨면 엑티비티 없어지는걸 대신 다시 연결하도록 만듬
    }

    public void showQuitDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    void sendMessage(String msg){
        if ( mConnectedTask != null ) {
            mConnectedTask.write(msg);
            Log.d(TAG, "send message: " + msg);
            mConversationArrayAdapter.insert("Me:  " + msg, 0);
        }
    }

    //목적지 선택 확인창
    public void selectConfirm(int pos){
        final int index = pos;
        speakTTS(poiNameArr.get(index).getPOI_name()+"을 선택했습니다.");

        new AlertDialog.Builder(this).setTitle("목적지 선택").setMessage(poiNameArr.get(index).getPOI_name()+" 선택")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        endpoint = poiNameArr.get(index).getPOI_latlng();
                        GetDirections();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                speakTTS("목적지 선택을 취소했습니다.");
            }
        }).show();
    }

    //현재 위치 리스너
    private LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                tmapView.setLocationPoint(longitude, latitude);
                tmapView.setCenterPoint(longitude, latitude);
                currentpoint = new TMapPoint(latitude, longitude);
                Log.e("-------",latitude+"/"+longitude);
            }
        }

        public void onProviderDisabled(String provider) {
            Log.e("-----", "사용 불가능");
        }

        public void onProviderEnabled(String provider) {
            Log.e("-----", "사용 가능");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("-----", "provider 상태 변경");
        }
    };

    //현재위치 받기
    @TargetApi(19)
    public void setGps() {
        Log.e("-----", "set gps");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
        }
    }

    //장소 검색
    protected void searchDestination(String dest) {
//        String dest = searchBar.getText().toString();
        Log.d("----debug----", "검색어: "+dest);
        speakTTS(dest+"을 검색했습니다.");
        tts.speak("검색 결과입니다", TextToSpeech.QUEUE_ADD, null, utteranceId);
        if(poiNameArr.size() > 0) {
            poiNameArr.clear();
        }
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        tData.findAroundKeywordPOI(currentpoint, dest, 2, 10, new TMapData.FindAroundKeywordPOIListenerCallback() {
            @Override
            public void onFindAroundKeywordPOI(ArrayList<TMapPOIItem> arrayList) {
                try {
                    for (int i = 0; i < arrayList.size(); i++) {
                        TMapPOIItem item = arrayList.get(i);
                        String str_addr = item.getPOIAddress();
                        String str_name = item.getPOIName();
                        TMapPoint poi_point = item.getPOIPoint();
                        data = new Dictionary(str_name, poi_point);
                        poiNameArr.add(data);
                        Log.d("----debug----", "\n이름: " + str_name + "\n주소: " + str_addr + "\n좌표: " + poi_point.toString());
                        tts.speak(str_name, TextToSpeech.QUEUE_ADD, null, utteranceId);

                    }

                    new Thread() {
                        public void run() {
                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);
                        }
                    }.start();

                } catch (NullPointerException e){
                    speechTextView.setText("다시 검색해주세요.");
                }
            }
        });
    }

    //경로 데이터 얻기
    public void GetDirections() {
//        final ArrayList<String> descArr = new ArrayList<String>();
        String uri = "https://api2.sktelecom.com/tmap/routes/pedestrian?appKey=l7xxb116f439ac904ca683fb4a533412e093&startX="+currentpoint.getLongitude()+"&startY="+currentpoint.getLatitude()+
                "&endX="+endpoint.getLongitude()+"&endY="+endpoint.getLatitude() +"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO&format=json&startName=start&endName=end";
        Log.d("----debug----", "URI: "+uri);
        pointArr.clear();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        tData.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH, currentpoint, endpoint, new TMapData.FindPathDataAllListenerCallback() {
            RequestQueue queue = Volley.newRequestQueue( MainActivity.this );
            @Override
            public void onFindPathDataAll(Document document) {
                Element root = document.getDocumentElement();
                NodeList nodeListPlacemark = root.getElementsByTagName("Placemark");

                String plc = "";
                double lat, lng;

                for(int i = 0; i < nodeListPlacemark.getLength(); i++) {
                    NodeList nodeListPlacemarkItem = nodeListPlacemark.item(i).getChildNodes();
                    for(int j = 0; j < nodeListPlacemarkItem.getLength(); j++) {
                        if(nodeListPlacemarkItem.item(j).getNodeName().equals("tmap:nodeType")) {
                            if(nodeListPlacemarkItem.item(j).getTextContent().equals("POINT")) {    //tmap:nodeType의 값이 POINT인지 판별
//                                descArr.add(nodeListPlacemarkItem.item(7).getTextContent());    //길 안내 정보 배열로 만듦
                                desc_str = nodeListPlacemarkItem.item(7).getTextContent();
                                Log.d("----", nodeListPlacemarkItem.item(7).getTextContent()+" / "+nodeListPlacemarkItem.item(5).getTextContent());
                            }
                        }

                        if(nodeListPlacemarkItem.item(j).getNodeName().equals("Point")) {
                            String pointLatLng = nodeListPlacemarkItem.item(j).getTextContent().trim();
                            String[] latlng = pointLatLng.split(",");
                            TMapPoint pathPoint = new TMapPoint(Double.valueOf(latlng[1]),Double.valueOf(latlng[0]));
                            pPonit = new PathPoint(pathPoint,desc_str);
                            pointArr.add(pPonit);
                            plc = nodeListPlacemarkItem.item(5).getTextContent();

                            Log.d("path", "----point----: "+pathPoint + plc);

                            LocationRequest locationRequest = new LocationRequest( userid, String.valueOf(pathPoint.getLatitude()), String.valueOf(pathPoint.getLongitude()), plc);
                            queue.add(locationRequest);
                        }
                    }
                }


                speakTTS(pointArr.get(0).getDescript());
                speechTextView.setText(pointArr.get(0).getDescript());

                tt = timerTask();
                timer.schedule(tt,0,3000);

                tl = tl_timertask();
                tl_timer.schedule(tl, 5000, 8000);

                reset = resetTask();
                rs_timer.schedule(reset,8000,20000);


                Log.d("----","타이머 실행");
            }

        });

        tData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, currentpoint, endpoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                polyLine.setLineColor(R.color.textcolor);
                polyLine.setLineWidth(10);
                tmapView.addTMapPath(polyLine);
            }
        });
    }

    //음성 인식 실행
    public void speakDestination(){
        sRecognizer.startListening(i);
        speechTextView.setText("음성인식을 시작합니다.");
        speakTTS("음성인식을 시작합니다.");
    }

    //음성 인식 리스너
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            //사용자가 말하기 시작할 준비가 되면 호출된다
            speechTextView.setTextColor(Color.BLUE);
        }

        @Override
        public void onBeginningOfSpeech() {
            //사용자가 말하기 시작했을 때 호출된다
            speechTextView.setTextColor(Color.RED);
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            //입력받는 소리의 크기를 알려준다
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            //사용자가 말을 시작하고 인식이 된 단어를 buffer에 담는다
        }

        @Override
        public void onEndOfSpeech() {
            //사용자가 말하기를 중지하면 호출된다
            speechTextView.setTextColor(Color.BLACK);
        }

        @Override
        public void onError(int error) {
            //네트워크 또는 인식 오류 발생 시 호출된다
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    speechTextView.setText("오디오 에러");
                    speakTTS("오디오 에러가 발생했습니다.");
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    speechTextView.setText("클라이언트 에러");
                    speakTTS("클라이언트 에러가 발생했습니다.");
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    speechTextView.setText("퍼미션 없음");
                    speakTTS("권한이 없습니다.");
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    speechTextView.setText("네트워크 에러");
                    speakTTS("네트워크 에러가 발생했습니다.");
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    speechTextView.setText("네트워크 시간초과");
                    speakTTS("네트워크 시간이 초과되었습니다.");
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    speechTextView.setText("찾을 수 없음");
                    speakTTS("찾을 수 없습니다");
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    speechTextView.setText("recognizer 바쁨");
                    speakTTS("recognizer 바쁨");
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    speechTextView.setText("서버 에러");
                    speakTTS("서버 에러가 발생했습니다.");
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    speechTextView.setText("말하기 시간초과");
                    speakTTS("말하기 시간을 초과했습니다.");
                    break;
                default:
                    speechTextView.setText("알 수 없는 에러 발생");
                    speakTTS("알 수 없는 에러가 발생했습니다.");
                    break;
            }

        }

        @Override
        public void onResults(Bundle results) {
            //인식 결과가 준비되면 호출된다.
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for(int i = 0; i < matches.size() ; i++){
                String specked = matches.get(i);
                speechTextView.setText(specked);

                //검색 연결
                searchDestination(specked);
            }
        }


        @Override
        public void onPartialResults(Bundle partialResults) {
            //부분 인식 결과를 사용할 수 있을 때 호출된다
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            //향후 이벤트 추가하기 위해 예약된다
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        key = SpeechRecognizer.RESULTS_RECOGNITION;
        speechTextView.setText(key);
        speakTTS(key);

        if (requestCode == REQUEST_BLUETOOTH_ENABLE) {
            if (resultCode == RESULT_OK) {
                //BlueTooth is now Enabled
                //showPairedDevicesListDialog();
                // 엑티비티가 다시 돌아오면 블루투스 다시 연결
            }
            if (resultCode == RESULT_CANCELED) {
                showQuitDialog("You need to enable bluetooth");
            }
        }
    }

    protected void showList(){
        try{
            JSONObject object = new JSONObject(myJSON);
            location = object.getJSONArray("result");

            for(int i = 0; i<location.length();i++){
                //DB쪽
                JSONObject c = location.getJSONObject(i);
                String id = c.getString("id");
//                String type = c.getString("location_type");
                String lat = c.getString("location_lat");
                String lng = c.getString("location_lng");

                //해시맵
                locations = new HashMap<String,String>();
                locations.put("id",id);
//                locations.put("type",type);
                locations.put("lat",lat);
                locations.put("lng",lng);

                resultList += lat+","+lng+",";

                //배열리스트
                locationList.add(locations);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (NullPointerException n) {
            n.printStackTrace();
            Log.e("@@@@@", "NullPointException");
        }
    }

    public void getData(String url){
        class  GetDataJSON extends AsyncTask<String,Void,String>{

                @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!=null){
                        sb.append(json+"/n");
                    }
                    return sb.toString().trim();
                } catch(Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                myJSON = s;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    //거리계산
    public float DistnaceDgree(double lat1, double lng1, double lat2, double lng2) {
        Location point1 = new Location("Point A");
        Location point2 = new Location("Point B");
        point1.setLatitude(lat1);
        point1.setLongitude(lng1);
        point2.setLatitude(lat2);
        point2.setLongitude(lng2);
        float distance = point1.distanceTo(point2);

        return distance;
    }

    //범위 테스트
    public void distTest(){
        /*String tl_str = helper.getResultTL();
        String bs_str = helper.getResultBS();
        String[] tl_arr = tl_str.split(",");
        String[] bs_arr = bs_str.split(",");*/

        String[] tl_arr = resultList.split(",");

        //신호등 버스정류장 횡단보도
        for(int i = 0;i<tl_arr.length;i+=2 ){
            try {
                testpoint1 = new TMapPoint(Double.valueOf(tl_arr[i]), Double.valueOf(tl_arr[i + 1]));
                TMapMarkerItem markerItem2 = new TMapMarkerItem();
                markerItem2.setIcon(bit3);
            /*if(tl_arr[i].equals("신호등")){
                markerItem2.setIcon(bit1);
            }else if(tl_arr[i].equals("횡단보도")){
                markerItem2.setIcon(bit3);
            } else {
                markerItem2.setIcon(bit2);
            }*/
                markerItem2.setTMapPoint(testpoint1);
                tmapView.addMarkerItem("markerItem" + i, markerItem2);

                tl_dist = DistnaceDgree(testpoint1.getLatitude(), testpoint1.getLongitude(), currentpoint.getLatitude(), currentpoint.getLongitude());

                if (tl_dist < tl_min && !speakedTL) {
                    speakedTL = true;
                    break;
                }

                if (tl_min < tl_dist && speakedTL) {
                    speakedTL = false;
                    Log.e("--------", "신호등거리: " + tl_dist + "/" + speakedTL);
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("@@@@@", ""+e);
                tl.cancel();
            }
        }

        /*for(int i = 0;i<tl_arr.length;i+=2 ){
            testpoint1 = new TMapPoint(Double.valueOf(tl_arr[i]), Double.valueOf(tl_arr[i+1]));

//            TMapCircle tlCircle = new TMapCircle();
//            tlCircle.setCenterPoint(testpoint1);
//            tlCircle.setRadius(5);
//            tlCircle.setAreaColor(Color.RED);
//            tlCircle.setAreaAlpha(100);
//            tmapView.addTMapCircle("mcircle"+i, tlCircle);

            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            markerItem1.setIcon(bit1);
            markerItem1.setTMapPoint(testpoint1);
            tmapView.addMarkerItem("markerItem"+i, markerItem1);

            tl_dist =  DistnaceDgree(testpoint1.getLatitude(), testpoint1.getLongitude(), currentpoint.getLatitude(), currentpoint.getLongitude());

            if(tl_dist<tl_min  && !speakedTL){
                speakedTL = true;
//                tl_min = tl_dist;

//                tts.speak("근처에 신호등이 있습니다.", TextToSpeech.QUEUE_ADD, null, utteranceId);
//                tts.playSilentUtterance(10000, TextToSpeech.QUEUE_ADD, null);

//                Log.e("--------","근처에 신호등이 있습니다: "+tl_dist+speakedTL);
                break;
            }

            if(tl_min<tl_dist && speakedTL){
                speakedTL = false;
                Log.e("--------","신호등거리: "+tl_dist+speakedTL);
            }
        }

        for(int j = 0;j<bs_arr.length;j+=2 ){
            testpoint2 = new TMapPoint(Double.valueOf(bs_arr[j]), Double.valueOf(bs_arr[j+1]));

//            TMapCircle tlCircle = new TMapCircle();
//            tlCircle.setCenterPoint(testpoint2);
//            tlCircle.setRadius(5);
//            tlCircle.setAreaColor(Color.YELLOW);
//            tlCircle.setAreaAlpha(100);
//            tmapView.addTMapCircle("bscircle"+j, tlCircle);

            TMapMarkerItem markerItem2 = new TMapMarkerItem();
            markerItem2.setIcon(bit2);
            markerItem2.setTMapPoint(testpoint2);
            tmapView.addMarkerItem("markerItem"+j, markerItem2);

            bs_dist =  DistnaceDgree(testpoint2.getLatitude(), testpoint2.getLongitude(), currentpoint.getLatitude(), currentpoint.getLongitude());

            if(bs_min>bs_dist && !speakedBS){
                speakedBS = true;
//                bs_min = bs_dist;
//                speakTTS("근처에 버스정류장이 있습니다.");

//                tts.speak("근처에 버스정류장이 있습니다.", TextToSpeech.QUEUE_ADD, null, utteranceId);
//                tts.playSilentUtterance(30000, TextToSpeech.QUEUE_ADD, null);
//
//                Log.e("--------","근처에 버스정류장이 있습니다");
                break;
            }

            if(bs_min<bs_dist && speakedBS){
                speakedBS = false;
                Log.e("--------","버정거리: "+bs_dist);
            }
        }*/

    }

    final Handler handler2 = new Handler(){
        public void handleMessage(Message message){
            distTest();
            nextPoint = new TMapPoint(pointArr.get(count).getpath_latlng().getLatitude(),pointArr.get(count).getpath_latlng().getLongitude());
            tCircle = new TMapCircle();
            tCircle.setCenterPoint(nextPoint);
            tCircle.setRadius(8);
            tCircle.setAreaColor(Color.GRAY);
            tCircle.setAreaAlpha(100);
            tmapView.addTMapCircle("circle", tCircle);
            dist = DistnaceDgree(nextPoint.getLatitude(), nextPoint.getLongitude(), currentpoint.getLatitude(), currentpoint.getLongitude());

            if(dist<=8){
                speechTextView.setText(pointArr.get(count).getDescript());
                speakTTS(pointArr.get(count).getDescript());
                count++;
            }

            if(tl_dist<5){
                ttss.speak("근처에 횡단보도가 있습니다.", TextToSpeech.QUEUE_ADD, null, utteranceId);
                ttss.playSilentUtterance(300000, TextToSpeech.QUEUE_ADD, null);

                Log.e("--------","근처에 횡단보도가 있습니다: "+tl_dist+speakedTL);
            }

            /*if(bs_dist<5){
                ttss.speak("근처에 버스정류장이 있습니다.", TextToSpeech.QUEUE_ADD, null, utteranceId);
                ttss.playSilentUtterance(60000, TextToSpeech.QUEUE_ADD, null);

                Log.e("--------","근처에 버스정류장이 있습니다: "+tl_dist+speakedTL);
            }*/

            if(count == pointArr.size()){
                Log.d("----","타이머 종료");
                speakTTS("목적지에 도착했습니다.");
                tmapView.removeAllTMapCircle();
                tmapView.removeAllTMapPolyLine();

                tt.cancel();
                tl.cancel();

                count = 1;
            }
        }
    };

    //안내문 바뀌는거
    public void changeDescript(){
        new Thread(){
            public void run(){
                Message msg = handler2.obtainMessage();
                handler2.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onLocationChange(Location location) {
        tmapView.setLocationPoint(location.getLongitude(), location.getLatitude());
    }
}
