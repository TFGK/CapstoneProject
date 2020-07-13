package com.example.proto4;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static String IP_ADDRESS = "54.180.103.20:8000";
    final static private String url = "http://54.180.103.20:8000/login.php";
    private Map<String,String> map;

    public LoginRequest(String email, String password, Response.Listener<String> listener){
        super(Method.POST, url, listener, null);

        map = new HashMap<>();
        map.put("email", email);
        map.put("password",password);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
