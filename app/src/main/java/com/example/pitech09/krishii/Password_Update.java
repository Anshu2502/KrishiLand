package com.example.pitech09.krishii;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Pitech09 on 10/7/2016.
 */
public class Password_Update extends Activity {

    public static final String UPLOAD_URL = "http://krishiland.com/krishi/password_update.php";
    String current_password, confirm_password, email_id;
    EditText cpassword, n_password,c_password;
    SharedPreferences.Editor editor;

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.password_update);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        email_id=pref.getString("email", null);
        Toast.makeText(Password_Update.this, email_id, Toast.LENGTH_SHORT).show();
        cpassword = (EditText)findViewById(R.id.editCurrentPassword);
        n_password = (EditText)findViewById(R.id.editNewPassword);
        c_password = (EditText)findViewById(R.id.editTextConfirmPassword);

        Button update = (Button)findViewById(R.id.buttonUpdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( n_password.getText().toString().equals(c_password.getText().toString())){
                    setProp();
                }else{
                    Toast.makeText(Password_Update.this, "New and Confirm Password are Not Same", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void setProp(){

        // final String image = getStringImage(bitmap);
        current_password = cpassword.getText().toString();
        confirm_password = c_password.getText().toString();
        Toast.makeText(getApplicationContext(),current_password+" "+confirm_password,Toast.LENGTH_LONG).show();
        class UploadImage extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Password_Update.this,"Please wait...","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new Password_Update.RequestHandler();
                HashMap<String,String> param = new HashMap<String,String>();
                param.put("current_password",current_password);
                param.put("email_id",email_id);
                param.put("confirm_password",confirm_password);
                Log.i("<<<ID>>>", String.valueOf(param));
                String result = rh.sendPostRequest(UPLOAD_URL, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

    public class RequestHandler {

        public String sendPostRequest(String requestURL,
           HashMap<String, String> postDataParams) {

            URL url;

            StringBuilder sb = new StringBuilder();
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    sb = new StringBuilder();
                    String response;
                    while ((response = br.readLine()) != null) {
                        sb.append(response);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    @Override
    public void onBackPressed()
    {

        super.onBackPressed();
        finish();
    }
}

