package com.example.proto4;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class BrightnessActivity extends AppCompatActivity implements SensorEventListener {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceViewHolder;
    private Handler mHandler;
    private ImageReader mImageReader;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCharacteristics cameraCharacteristics;
    private CameraCaptureSession mSession;
    private int mDeviceRotation;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private SensorManager mSensorManager;
    private DeviceOrientation deviceOrientation;
    int mDSI_height, mDSI_width;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    TextView tvLight;
    SpeechRecognizer sRecognizer;

    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_LOCK = 1;
    private static final int STATE_WAITING_PRECAPTURE = 2;
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    private static final int STATE_PICTURE_TAKEN = 4;

    protected float zoomLevel = 1f;
    protected float maximumZoomLevel;
    protected Rect zoom;
    private String mCameraId;
    private int mState = STATE_PREVIEW;
    TextToSpeech tts;
    boolean speaked = false;
    MainActivity main;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL, 0);
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 90);
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 180);
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 270);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_brightness);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREA);
                //speakTTS("빛 밝기 페이지");
                tts.speak("빛 밝기 페이지", TextToSpeech.QUEUE_FLUSH, null, this.hashCode() + "");
            }
        });

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor == null){
            Toast.makeText(this, "조도 센서를 찾을 수 없습니다!", Toast.LENGTH_SHORT).show();
            speakTTS("조도 센서를 찾을 수 없습니다");
        }
        tvLight = findViewById(R.id.tvLight);

        ImageButton iBtn = findViewById(R.id.take_photo);
        iBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakTTS(String.valueOf(tvLight.getText()));
            }
        });

        mSurfaceView = findViewById(R.id.svLight);
        mSurfaceView.setOnTouchListener(new OnSwipeTouchListener(BrightnessActivity.this){
            public void onSwipeTop() {

            }
            public void onSwipeRight() {
                mRecognizer.destroy();
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(i);
                finish();
            }
            public void onSwipeLeft() {
                mRecognizer.destroy();
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                finish();
            }
            public void onSwipeBottom() {
            }
            public void speeched(){
                Log.e("--------","speeched: "+resultStr);
                if(resultStr.equals("메뉴")){
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(resultStr.equals("길 찾기")){
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(i);
                    finish();
//                    main.onResume();
                }
                else if(resultStr.equals("사물 인식")){
                    Intent i = new Intent(getApplicationContext(), ObjectRecognitionActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(resultStr.equals("문자 인식")){
                    Intent i = new Intent(getApplicationContext(), CharacterRecognitionActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(resultStr.equals("색상 인식")){
                    Intent i = new Intent(getApplicationContext(), ColorRecognitionActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(resultStr.equals("인식하기")){
                    speakTTS(String.valueOf(tvLight.getText()));
                }
            }
        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        deviceOrientation = new DeviceOrientation();

        initSurfaceView();
    }

    public void speakTTS(String sentence) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String utteranceId=this.hashCode() + "";
            tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, hashmap);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int keyAction = event.getAction();
        switch (keyAction){
            case MotionEvent.ACTION_DOWN:
                Log.e("---","DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("---","UP");
//                speakTTS(String.valueOf(tvLight.getText()));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("---","MOVE");
                break;
        }

        return true;
    }

    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };

    @TargetApi(19)
    public void initCameraAndPreview() {
        HandlerThread handlerThread = new HandlerThread("CAMERA2");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        Handler mainHandler = new Handler(getMainLooper());

        try {
            String mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT; // 후면 카메라 사용
            this.mCameraId = mCameraId;

            CameraManager mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            maximumZoomLevel = (characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM));
            Log.e("maxZoomLevel", maximumZoomLevel+"");


            Size largestPreviewSize = map.getOutputSizes(ImageFormat.JPEG)[0];
            Log.e("@@@", largestPreviewSize.getWidth() + " " + largestPreviewSize.getHeight());

            setAspectRatioTextureView(largestPreviewSize.getHeight(),largestPreviewSize.getWidth());

            mImageReader = ImageReader.newInstance(largestPreviewSize.getWidth(), largestPreviewSize.getHeight(), ImageFormat.JPEG,/*maxImages*/7);
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mainHandler);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler);

        } catch (CameraAccessException e) {
            Toast.makeText(this, "카메라를 열지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(deviceOrientation.getEventListener(), mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), mMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
        mSensorManager.unregisterListener(deviceOrientation.getEventListener());

        if (null != mSession) {
            mSession.close();
            mSession = null;
        }
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (null != mImageReader) {
            mImageReader.close();
            mImageReader = null;
        }

        if(tts != null){
            tts.stop();
            tts = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(sRecognizer != null) {
            sRecognizer.stopListening();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(tts != null){
            tts.shutdown();
            tts = null;
        }

        if(sRecognizer != null) {
            sRecognizer.destroy();
        }
    }

    public void initSurfaceView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mDSI_height = displayMetrics.heightPixels;
        mDSI_width = displayMetrics.widthPixels;
        Log.e("@@@","width:"+mDSI_width+"/height:"+mDSI_height);

        mSurfaceViewHolder = mSurfaceView.getHolder();
        mSurfaceViewHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCameraAndPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                if (mCameraDevice != null) {
                    mCameraDevice.close();
                    mCameraDevice = null;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });
    }

    private void captureStillPicture() {
        try {
            final Activity activity = BrightnessActivity.this;
            if (null == activity || null == mCameraDevice) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            if (zoom != null) {
                captureBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoom);
            }

            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,@NonNull CaptureRequest request,@NonNull TotalCaptureResult result) {
                    unlockFocus();
                }
            };

            mSession.stopRepeating();
            mSession.abortCaptures();
            mSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {

            Image image = reader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            new SaveImageTask().execute(bitmap);
        }
    };

    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mSession.capture(mPreviewBuilder.build(), mCaptureCallback,
                    mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            try {
                takePreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Toast.makeText(BrightnessActivity.this, "카메라를 열지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    public void takePreview() throws CameraAccessException {
        mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        mPreviewBuilder.addTarget(mSurfaceViewHolder.getSurface());
        mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceViewHolder.getSurface(), mImageReader.getSurface()), mSessionPreviewStateCallback, mHandler);
    }

    private CameraCaptureSession.StateCallback mSessionPreviewStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mSession = session;

            try {
                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                mSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Toast.makeText(BrightnessActivity.this, "카메라 구성 실패", Toast.LENGTH_SHORT).show();
        }
    };

    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            mSession = session;
            unlockFocus();
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            mSession = session;
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }
    };

    public void takePicture() {

        try {
            CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

            //deviceRotation = getResources().getConfiguration().orientation;
            mDeviceRotation = ORIENTATIONS.get(deviceOrientation.getOrientation());
            Log.e("@@@", mDeviceRotation+"");

            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, mDeviceRotation);
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            mSession.capture(mCaptureRequest, mSessionCaptureCallback, mHandler);
            speakTTS("빛 감지 중");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws Exception {
        if(bitmap == null) return null;
        if (degrees == 0) return bitmap;

        Matrix m = new Matrix();
        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            mSession.capture(mPreviewBuilder.build(), mSessionCaptureCallback, mHandler);

            // After this, the camera will go back to the normal state of preview.
            mSession.setRepeatingRequest(mPreviewBuilder.build(), mSessionCaptureCallback, mHandler);

            if(zoom != null)mPreviewBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoom);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public static final String insertImage(ContentResolver cr, Bitmap source, String title, String description) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String value;
        if(event.sensor.getType() == Sensor.TYPE_LIGHT) {
            tvLight.setText(String.valueOf(event.values[0]));
        }

        if(event.values[0] > 30){
            tvLight.setText("완전 밝음");
            value = "완전 밝음";

        } else if(event.values[0] > 20){
            tvLight.setText("밝음");
            value = "밝음";

        } else if(event.values[0] > 10){
            tvLight.setText("조금 밝음");
            value = "조금 밝음";

        } else if(event.values[0] > 5){
            tvLight.setText("조금 어두움");
            value = "조금 어두움";

        } else if(event.values[0] > 3){
            tvLight.setText("어두움");
            value = "어두움";

        } else{
            tvLight.setText("완전 어두움");
            value = "완전 어두움";
        }

        if(!speaked){
            speaked = true;
//            speakTTS(value);
        }

        Log.e("--------", ""+value+"/"+speaked);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class SaveImageTask extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(BrightnessActivity.this, "사진을 저장하였습니다.", Toast.LENGTH_SHORT).show();
        }


        @Override
        protected Void doInBackground(Bitmap... data) {

            Bitmap bitmap = null;
            try {
                bitmap = getRotatedBitmap(data[0], mDeviceRotation);
            } catch (Exception e) {
                e.printStackTrace();
            }
            insertImage(getContentResolver(), bitmap, ""+System.currentTimeMillis(), "");

            return null;
        }

    }

    private void setAspectRatioTextureView(int ResolutionWidth , int ResolutionHeight )
    {

        if(ResolutionWidth > ResolutionHeight){     //가로>세로
            int newWidth = mDSI_width;
            int newHeight = ((mDSI_width * ResolutionWidth)/ResolutionHeight);
            updateTextureViewSize(newWidth,newHeight);

        }else { //가로<세로
            int newWidth = mDSI_width;
            int newHeight = mDSI_height;
//            int newHeight = ((mDSI_width * ResolutionHeight)/ResolutionWidth);
            updateTextureViewSize(newWidth,newHeight);
        }

    }

    private void updateTextureViewSize(int viewWidth, int viewHeight) {
        Log.e("@@@", "TextureView Width : " + viewWidth + " TextureView Height : " + viewHeight);
        mSurfaceView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
    }

    //음성 인식 리스너
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            //사용자가 말하기 시작할 준비가 되면 호출된다
        }

        @Override
        public void onBeginningOfSpeech() {
            //사용자가 말하기 시작했을 때 호출된다
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
        }

        @Override
        public void onError(int error) {
            //네트워크 또는 인식 오류 발생 시 호출된다
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    speakTTS("오디오 에러가 발생했습니다.");
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    speakTTS("클라이언트 에러가 발생했습니다.");
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    speakTTS("권한이 없습니다.");
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    speakTTS("네트워크 에러가 발생했습니다.");
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    speakTTS("네트워크 시간이 초과되었습니다.");
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    speakTTS("찾을 수 없습니다");
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    speakTTS("recognizer 바쁨");
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    speakTTS("서버 에러가 발생했습니다.");
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    speakTTS("말하기 시간을 초과했습니다.");
                    break;
                default:
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String key = SpeechRecognizer.RESULTS_RECOGNITION;
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
    }
}
