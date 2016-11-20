package com.example.pitech09.krishii;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Pitech09 on 10/4/2016.
 */
public class Profile_Update extends AppCompatActivity {

    private ProgressDialog progressDialog;
    SharedPreferences.Editor editor;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private ImageView imageView;
    EditText name, number,email,state,address, img;
    String imagea,user_name, user_contact, user_email, myyJSONN, state_id , city_id,city_name, user_addresss, myyJSON, email_name,myJSONM;
    public static final String UPLOAD_URL = "http://krishiland.com/krishi/user_update.php";
    ArrayList<String> listItems=new ArrayList<>();
    ArrayList<String> listItems2=new ArrayList<>();
    ArrayList<String> countryList = new ArrayList<String>();
    ArrayList<String> countryList2 = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    Spinner sp,sp2;
    ImageView i1;

    String name1, email1,phone1,city1,state1,address1;
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.profile_update);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        email_name=pref.getString("email", null);
        Toast.makeText(Profile_Update.this, email_name, Toast.LENGTH_SHORT).show();


        name = (EditText) findViewById(R.id.editTextName);
        number = (EditText) findViewById(R.id.editTextNumber);
        email = (EditText) findViewById(R.id.editTextEmail);
        //state = (EditText) findViewById(R.id.editTextState);
        //city = (EditText) findViewById(R.id.editTextCity);
        address = (EditText) findViewById(R.id.editTextaddress);
        imageView = (ImageView) findViewById(R.id.imageView);
        Button update = (Button) findViewById(R.id.buttonUpdate);

        sp=(Spinner)findViewById(R.id.statespinner);
        sp2=(Spinner)findViewById(R.id.cityspinner);
        adapter=new ArrayAdapter<String>(this,R.layout.spinner_layouta,R.id.state,listItems);
        adapter2=new ArrayAdapter<String>(this,R.layout.spinner_layoutb,R.id.city,listItems2);
        getState();
        sp.setAdapter(adapter);
        sp2.setAdapter(adapter2);
        sp2.setEnabled(false);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                state_id = countryList.get(arg2);
                listItems2.clear();
                getCity();
                Toast.makeText(Profile_Update.this, state_id, Toast.LENGTH_SHORT).show();
                sp2.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                city_id = countryList2.get(arg2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        assert update != null;
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProp();
            }
        });

        /*Button User_image  = (Button) findViewById(R.id.imagebutton);

        User_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });*/



        getProp();


      }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void prop() {

        try {

            JSONArray property_images = new JSONArray(myyJSON);
            Log.e("--->>>>>>>",String.valueOf(property_images));
            for(int i=0;i<property_images.length();i++){
                JSONObject c = property_images.getJSONObject(0);
                user_name = c.getString("username");
                user_contact = c.getString("contact");
                user_email = c.getString("email");
                imagea = c.getString("user_image");
                //user_cityy = c.getString("user_city");
                //user_statee = c.getString("user_state");
                user_addresss = c.getString("user_address");

                name.setText(user_name);
                number.setText(user_contact);
                number.setEnabled(false);
                email.setText(user_email);
                email.setEnabled(false);
                email1 = email.getText().toString();
                address.setText(user_addresss);
              //  state.setText(user_statee);
               // city.setText(user_cityy);

              ImageView im = (ImageView) findViewById(R.id.image);
                Glide.with(this).load("http://krishiland.com/user_image/" + imagea)
                        .error(R.drawable.plainicon)
                        .into(im);
            }

        }
        //System.out.println(aa);

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void City(){

        try {
            ArrayList<String> listc =  new ArrayList<>(); ;
            JSONArray  property_images = new JSONArray(myyJSONN);
            Log.e("--->>>>>>>",String.valueOf(property_images));
            for(int i=0;i<property_images.length();i++){

                JSONObject c = property_images.getJSONObject(i);

                 city_id = c.getString("city_id");
                 city_name = c.getString("city_name");
                listc.add(city_name);
                countryList2.add(city_id);

            }


            listItems2.addAll(listc);
            adapter2.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void getCity(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            public void onPreExecute() {

                progressDialog = new ProgressDialog(Profile_Update.this);
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.show();

            }
            @Override
            protected String doInBackground(String... params) {

                InputStream inputStream = null;
                String result = null;
                try {

                    URL url = new URL("http://krishiland.com/krishi/city.php");

                    JSONObject postDataParams = new JSONObject();
                    Log.e("Value>>>>>", String.valueOf(postDataParams));
                    postDataParams.put("state_id", state_id);
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

                        BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder("");
                        String line="";
                        while ((line = in.readLine()) != null)
                        {
                            sb.append(line).append("\n");
                        }
                        result = sb.toString();
                    }

                    assert inputStream != null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line).append("\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    Log.i("tagconvertstr", "["+result+"]");
                    System.out.println(e);
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){

                myyJSONN = result;
                City();
                progressDialog.dismiss();



            }


        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }



    public void getState(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ArrayList<String> list;
            public void onPreExecute() {
                super.onPreExecute();
                list=new ArrayList<>();
            }
            @Override
            protected String doInBackground(String... params) {

                InputStream inputStream = null;
                String result = null;
                try {

                    URL url = new URL("http://krishiland.com/krishi/state.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    int responseCode=conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder("");
                        String line="";
                        while ((line = in.readLine()) != null)
                        {
                            sb.append(line).append("\n");
                        }
                        result = sb.toString();
                    }


                try{
                    JSONArray jArray =new JSONArray(result);
                    for(int i=0;i<jArray.length();i++){
                        JSONObject jsonObject=jArray.getJSONObject(i);
                        String id = jsonObject.getString("state_id");
                        String name = jsonObject.getString("state_name");
                        list.add(name);
                        countryList.add(id);
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                    assert inputStream != null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line).append("\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    Log.i("tagconvertstr", "["+result+"]");
                    System.out.println(e);
                }

                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }

                return null;
            }



            @Override
            protected void onPostExecute(String result){

                listItems.addAll(list);
                adapter.notifyDataSetChanged();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }



    public void getProp(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            public void onPreExecute() {
                // Pbar.setVisibility(View.VISIBLE);
            }
            @Override
            protected String doInBackground(String... params) {

                InputStream inputStream = null;
                String result = null;
                try {

                    URL url = new URL("http://krishiland.com/krishi/user_detail.php");

                    JSONObject postDataParams = new JSONObject();
                    Log.e("Value>>>>>", String.valueOf(postDataParams));
                    postDataParams.put("email", email_name);
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

                        BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder("");
                        String line="";
                        while ((line = in.readLine()) != null)
                        {
                            sb.append(line).append("\n");
                        }
                        result = sb.toString();
                    }

                    assert inputStream != null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line).append("\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    Log.i("tagconvertstr", "["+result+"]");
                    System.out.println(e);
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){

                myyJSON = result;
                prop();

            }


        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    public void setProp(){

       // final String image = getStringImage(bitmap);
        name1 = name.getText().toString();
        address1 = address.getText().toString();
        class UploadImage extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Profile_Update.this,"Please wait...","uploading",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<String,String>();
                param.put("name",name1);
                param.put("email",email1);
                param.put("city",city_id);
                param.put("state",state_id);
                param.put("address",address1);
                //param.put("image",image);
                Log.i("<<<ID>>>", String.valueOf(param));
                String result = rh.sendPostRequest(UPLOAD_URL, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
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

    }

    @Override
    public void onBackPressed()
    {

        super.onBackPressed();
        finish();
    }
}
