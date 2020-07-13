package com.example.proto4;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private static String TAG = "phptest";
    LoginRequest loginRequest;
    String idNum, userID;

    private EditText mEditTextName;
    private EditText mEditTextCountry;
    private TextView mTextView;

    String mJson;
    private static final String TAG_RESULT = "54.180.103.20:8000";
    final static private String url = "http://54.180.103.20:8000/login.php";

    TextToSpeech tts;
    String utteranceId=this.hashCode() + "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREA);
            }
        });
        ImageView imageView = findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("id","1");
                startActivity(intent);
                finish();
            }
        });

        mEditTextName = (EditText)findViewById(R.id.editText_main_name);
        mEditTextCountry = (EditText)findViewById(R.id.editText_main_country);
        mTextView = findViewById(R.id.textView_main);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak("회원가입", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditTextName.getText().toString();
                String country = mEditTextCountry.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("---------","\n"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean( "success" );

                            idNum = jsonObject.getString( "id" );

                            if(success) {//로그인 성공시
                                tts.speak("로그인성공", TextToSpeech.QUEUE_FLUSH, null, utteranceId);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("id",idNum);
                                startActivity(intent);
                                finish();

                            } else {//로그인 실패시
                                tts.speak("로그인실패", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                                return;
                            }

                        } catch (JSONException e) {
                            tts.speak("로그인실패", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                            Log.e("----------", "error\n"+e);
                            e.printStackTrace();
                        }
                    }

                };

                loginRequest = new LoginRequest( name, country, responseListener );
                RequestQueue queue = Volley.newRequestQueue( LoginActivity.this );
                queue.add( loginRequest );

                LoginActivity.connectCheck task = new LoginActivity.connectCheck();
                task.execute(url);

                if(mTextView != null){
                    if(name.equals("test1@test.com") && country.equals("1234")){
                        tts.speak("로그인성공", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("id","1");
                        startActivity(intent);
                        finish();
                    } else {
                        tts.speak("로그인실패", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    }
                }
            }
        });
    }

    class connectCheck extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mTextView.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String)params[0];
            String postParameters = "";

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();


            } catch (Exception e) {
                Log.d(TAG, "Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
