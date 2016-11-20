package com.example.pitech09.krishii;

/**
 * Created by Pitech09 on 9/8/2016.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Pitech09 on 8/12/2016.
 */
public class QueryForm extends Activity {
    EditText query;
    Button s_query;
    InputStream is=null;
    int code;
    SharedPreferences.Editor editor;
    String result=null, line=null, u_id, p_id;
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.query_form);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        u_id = pref.getString("user_id",null);
        p_id=pref.getString("property_id", null);

        Toast.makeText(QueryForm.this, u_id+" - "+p_id, Toast.LENGTH_SHORT).show();

        Log.e("<<<<-->>>>",u_id+" "+p_id);


        query = (EditText)findViewById(R.id.query);
        s_query = (Button) findViewById(R.id.buttonEnquery);

        s_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inserts();
           }
        });
    }

    public void inserts()
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateandTime = sdf.format(new Date());
            String message = query.getText().toString();
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            URL url = new URL("http://krishiland.com/krishi/query_detail.php");

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("req_user_id", u_id);
            postDataParams.put("proty_id", p_id);
            postDataParams.put("req_msg", message);
            postDataParams.put("req_date", currentDateandTime);
            Log.e("params", postDataParams.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Your Query Has Been Send Successfully");

                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent ii = new Intent (QueryForm.this,MainActivity.class);
                        startActivity(ii);
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                //Toast.makeText(getApplicationContext(),"Data Inserted Please Check",Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {
            JSONObject json_data = new JSONObject(result);
            code=(json_data.getInt("code"));

            if(code==1)
            {
                // Toast.makeText(getBaseContext(), "Inserted Successfully",
                //       Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Sorry, Try Again",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Log.e("Fail 3", e.toString());
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    @Override
    public void onBackPressed() {


        Intent in = new Intent(QueryForm.this, Property_Detail.class);
        in.putExtra("property_id",p_id);
        startActivity(in);
        finish();

    }


}







