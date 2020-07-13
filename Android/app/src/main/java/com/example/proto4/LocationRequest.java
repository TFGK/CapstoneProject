package com.example.proto4;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LocationRequest extends StringRequest{
    final static private String url2 = "http://192.168.123.101/userLocation.php";
    final static private String url = "http://54.180.103.20:8000/userLocation.php";
    private Map<String,String> map;

    public LocationRequest(String id, String lat, String lng, String place){
        super(Method.POST, url, null, null);

        map = new HashMap<>();
//        map.put("user_id", id);
//        map.put("place_lat", lat);
//        map.put("place_lng", lng);
//        map.put("place_name", place);

        InsertData task = new InsertData();
        task.execute(url,id,lat,lng,place);
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
            String lat = (String)params[2];
            String lng = (String)params[3];
            String place = (String)params[4];
            String serverURL = (String)params[0];
            String postParameters = "user_id=" + id + "&place_lat=" + lat+ "&place_lng=" + lng + "&place_name=" + place;
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
