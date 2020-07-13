package com.example.proto4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEditTextID, mEditTextN, mEditTextPW, mEditTextY;
    private Button regiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView imageView = findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mEditTextN = findViewById(R.id.editText_name);
        mEditTextID = findViewById(R.id.editText_mail);
        mEditTextPW = findViewById(R.id.editText_password);
        mEditTextY = findViewById(R.id.editText_year);

        regiButton = findViewById(R.id.button_insert);
        regiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mEditTextID.getText().toString();
                String pw = mEditTextPW.getText().toString();
                String name = mEditTextN.getText().toString();
                String year = mEditTextY.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("---------","res\n"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean( "success" );

                            if(success) {//성공시
                                String userN = jsonObject.getString( "name" );
                                String userID = jsonObject.getString( "email" );
                                String userPass = jsonObject.getString( "password" );
                                String userY = jsonObject.getString( "year" );

                                Toast.makeText( getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT ).show();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

                            } else {//실패시
                                Toast.makeText( getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT ).show();
                                return;
                            }

                        } catch (JSONException e) {
                            Log.e("----------", "error\n"+e);
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(id, name, pw, year, responseListener );
                RequestQueue queue = Volley.newRequestQueue( RegisterActivity.this );
                queue.add( registerRequest );
            }
        });
    }
}
