package com.example.proto4;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Sensor;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class ObjectRecognitionActivity extends AppCompatActivity {
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

    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_LOCK = 1;
    private static final int STATE_WAITING_PRECAPTURE = 2;
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    private static final int STATE_PICTURE_TAKEN = 4;

    SeekBar zoomSeekBar;
    protected float zoomLevel = 1f;
    protected float maximumZoomLevel;
    protected Rect zoom;
    private String mCameraId;
    private int mState = STATE_PREVIEW;
    static int LONG_PRESS_TIME = 2000;
    TextToSpeech tts;
    MainActivity main;
    ImageButton imageButton;

    final Handler _Handler = new Handler();
    Runnable _longPressed = new Runnable() {
        @Override
        public void run() {
            Log.e("---------","LongPress");
            takePicture();
        }
    };

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

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREA);
                //speakTTS("사물 인식 페이지");
                tts.speak("사물 인식 페이지", TextToSpeech.QUEUE_FLUSH, null, this.hashCode() + "");

            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_object_recognition);

        mSurfaceView = findViewById(R.id.obj_surfaceView);
        mSurfaceView.setOnTouchListener(new OnSwipeTouchListener(ObjectRecognitionActivity.this){
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                mRecognizer.destroy();
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(i);
                finish();
//                main.onResume();
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
                else if(resultStr.equals("색상 인식")){
                    Intent i = new Intent(getApplicationContext(), ColorRecognitionActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(resultStr.equals("문자 인식")){
                    Intent i = new Intent(getApplicationContext(), CharacterRecognitionActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(resultStr.equals("빛 밝기")){
                    Intent i = new Intent(getApplicationContext(), BrightnessActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(resultStr.equals("인식하기")){
                    takePicture();
                }
            }
        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        deviceOrientation = new DeviceOrientation();

        imageButton = findViewById(R.id.obj_take_photo);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        zoomSeekBar = (SeekBar)findViewById(R.id.obj_zoomSeekBar);
        zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e(TAG, "progress:"+progress);

                try{
                    CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
                    float maxzoom = (characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM))*10;

                    Rect m = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                    zoomSeekBar.setMax((int)maxzoom);

                    if (progress >= 1) {
                        zoomLevel = progress;
                        int minW = (int) (m.width() / maxzoom);
                        int minH = (int) (m.height() / maxzoom);
                        int difW = m.width() - minW;
                        int difH = m.height() - minH;
                        int cropW = difW /100 *(int)zoomLevel;
                        int cropH = difH /100 *(int)zoomLevel;
                        cropW -= cropW & 3;
                        cropH -= cropH & 3;
                        Rect zoom = new Rect(cropW, cropH, m.width() - cropW, m.height() - cropH);
                        mPreviewBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoom);
                    }

                    try {
                        mSession.setRepeatingRequest(mPreviewBuilder.build(), mCaptureCallback, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }catch(CameraAccessException e) {
                    throw new RuntimeException("can not access camera.", e);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
        int afasfasf = event.getActionMasked();
        switch (afasfasf){
            case MotionEvent.ACTION_DOWN:
                Log.e("---","DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("---","UP");
//                takePicture();
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

        mSensorManager.registerListener(deviceOrientation.getEventListener(), mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), mMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(tts != null){
            tts.stop();
            tts = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(tts != null){
            tts.shutdown();
            tts = null;
        }
    }

    public void initSurfaceView() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mDSI_height = displayMetrics.heightPixels;
        mDSI_width = displayMetrics.widthPixels;


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
            final Activity activity = ObjectRecognitionActivity.this;
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
            Toast.makeText(ObjectRecognitionActivity.this, "카메라를 열지 못했습니다.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ObjectRecognitionActivity.this, "카메라 구성 실패", Toast.LENGTH_SHORT).show();
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
            speakTTS("사물인식을 실행합니다");
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
        } catch (IllegalStateException e) {
            Log.e(TAG, "Failed to start camera preview.", e);
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

    private class SaveImageTask extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//            Toast.makeText(ObjectRecognitionActivity.this, "사진을 저장하였습니다.", Toast.LENGTH_SHORT).show();
        }

        // 사물인식
        @Override
        protected Void doInBackground(Bitmap... data) {

            Bitmap bitmap = null;
            try {
                bitmap = getRotatedBitmap(data[0], mDeviceRotation);
            } catch (Exception e) {
                e.printStackTrace();
            }
            insertImage(getContentResolver(), bitmap, ""+System.currentTimeMillis(), "");

            String postUrl= "http://3.34.46.23:5000/";
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try{
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                Log.e("----:MagnifyingActivity", "사진사진사진");
            }catch (Exception e){
                Log.e("----:MagnifyingActivity", "요청요청"+e.getMessage());
                speakTTS("에러 발생");
            }
            byte[] byteArray = stream.toByteArray();

            RequestBody postBodyImage = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "androidFlask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                    .build();

            postRequest(postUrl, postBodyImage);
            return null;
        }

    }

    private void setAspectRatioTextureView(int ResolutionWidth , int ResolutionHeight )
    {
        if(ResolutionWidth > ResolutionHeight){     //가로>세로
            int newWidth = mDSI_height;
            int newHeight = ((mDSI_width * ResolutionWidth)/ResolutionHeight);
            updateTextureViewSize(newWidth,newHeight);

        }else { //가로<세로 1080 1440
            int newWidth = mDSI_width;
            int newHeight = mDSI_height;
//            int newHeight = ((mDSI_width * ResolutionHeight)/ResolutionWidth);
            updateTextureViewSize(newWidth,newHeight);
        }

    }

    private void updateTextureViewSize(int viewWidth, int viewHeight) {
        Log.e("@@@", "TextureView Width : " + viewWidth + " TextureView Height : " + viewHeight);
        mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(viewWidth, viewHeight));
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                Log.e("----", "콜백오류:"+e.getMessage());
                call.cancel();
                speakTTS("서버에 연결 실패");

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            // 사물인식 결과
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String text = response.body().string();
                            Log.e("----:return", "return: "+text);
                            String[] resMsg_en = text.split(",");
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
                                speakTTS(TTSMsg);
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
                                    speakTTS(TTSMsg);
                                }
                                else{
                                    TTSMsg += "가 있습니다";
                                    speakTTS(TTSMsg);
                                }
                            }
                        }catch (IOException e){
                            e.getMessage();
                        }
                    }
                });
            }
        });
    }

    public String getPath(Uri uri) {
        Cursor cursor = null;
        String result;
//        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        try {
            cursor = getContentResolver().query(uri, null, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(column_index);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
