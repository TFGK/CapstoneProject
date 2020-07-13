package com.example.proto4;

import android.os.AsyncTask;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static String IP_ADDRESS = "114.199.210.237:2180";
    final static private String url = "http://192.168.123.101/register.php";
    private Map<String,String> map;

    public RegisterRequest(String id, String name, String password, String year, Response.Listener<String> listener){
        super(Request.Method.POST, url, listener, null);
        map = new HashMap<>();
        map.put("name", name);
        map.put("email", id);
        map.put("password", password);
        map.put("year", year);

//        RegisterRequest.InsertData task = new RegisterRequest.InsertData();
//        task.execute(url,id,password);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }

    class InsertData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }


        @Override
        protected String doInBackground(String... params) {
            String id = (String)params[1];
            String pw = (String)params[2];
            String serverURL = (String)params[0];
            String postParameters = "name="+""+"&email=" + id + "&password=" + pw+ "&year=" + pw;
            Log.e("----------", postParameters);

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
                Log.e("------","\n"+e);
                return new String("Error: " + e.getMessage());
            }

        }
    }
}
