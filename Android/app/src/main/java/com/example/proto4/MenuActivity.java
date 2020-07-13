package com.example.proto4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MenuActivity extends AppCompatActivity {
    ConstraintLayout menuLayout;
    TextView navBtn, objBtn, colBtn, chrBtn, litBtn, setBtn;
    CardView nav, obj, col, chr, br, set;
    TextToSpeech tts;
    String utteranceId=this.hashCode() + "";
    MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tem);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREA);
                tts.speak("메뉴 페이지", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            }
        });

        menuLayout = findViewById(R.id.menu_layout_tem);
        menuLayout.setOnTouchListener(new OnSwipeTouchListener(MenuActivity.this){
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                mRecognizer.destroy();
                Intent i = new Intent(getApplicationContext(), ObjectRecognitionActivity.class);
                startActivity(i);
                finish();
            }
            public void onSwipeLeft() {
                mRecognizer.destroy();
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(i);
                finish();
//                main.onResume();
            }
            public void onSwipeBottom() {
            }
            public void speeched(){
                Log.e("--------","speeched: "+resultStr);
                if(resultStr.equals("길 찾기")){
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(i);
//                    main.onResume();
                    finish();
                }
                else if(resultStr.equals("사물 인식")){
                    Intent i = new Intent(getApplicationContext(), ObjectRecognitionActivity.class);
                    startActivity(i);
                    finish();
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
            }
        });

        nav = findViewById(R.id.navi);
        navBtn = findViewById(R.id.navigation);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(i);
                finish();
            }
        });

        obj = findViewById(R.id.object);
        objBtn = findViewById(R.id.objectRecog);
        obj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ObjectRecognitionActivity.class);
                startActivity(i);
                finish();
            }
        });

        chr = findViewById(R.id.characterR);
        chrBtn = findViewById(R.id.textRecog);
        chr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CharacterRecognitionActivity.class);
                startActivity(i);
                finish();
            }
        });

        col = findViewById(R.id.colorR);
        colBtn = findViewById(R.id.colorRecog);
        col.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ColorRecognitionActivity.class);
                startActivity(i);
                finish();
            }
        });

        br = findViewById(R.id.brightness);
        br.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BrightnessActivity.class);
                startActivity(i);
                finish();
            }
        });

        litBtn = findViewById(R.id.brightnessBtn);

        set = findViewById(R.id.settingLayout);
        setBtn = findViewById(R.id.setting);
        set.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
