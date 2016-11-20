package com.example.pitech09.krishii;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private Pro_Adapter mAdapter;
    FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    final Context context = this;
    int loader = R.drawable.plainicon;
    String myJSON,  email,user_id, usernamee= null, image_url;
    SharedPreferences.Editor editor;
    Dialog dialog;
    EditText editTextEmail;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        email=pref.getString("email", null);
        dialog = new Dialog(context);
      //  Toast.makeText(MainActivity.this, email, Toast.LENGTH_SHORT).show();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        log();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, Main_Fragment.class);
                startActivity(i);
            }
        });


       new AsyncFetch().execute();
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            Bundle extras = getIntent().getExtras();
            if (extras== null) {

                try {
                    url = new URL("http://krishiland.com/krishi/krishi_detail.php");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "");
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setDoOutput(true);
                    conn.connect();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return e1.toString();
                }


            } else{


                String actionn = extras.getString("action");
                String type = extras.getString("type");
                String ciid = extras.getString("cid");

                try {
                    url = new URL("http://krishiland.com/krishi/krishi_detail.php");

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("cid", ciid);
                    postDataParams.put("action", actionn);
                    postDataParams.put("type", type);

                    Log.i("params", postDataParams.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "");
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return e1.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            List<Pro_Cons> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    Pro_Cons fishData = new Pro_Cons();
                    fishData.property_idd = json_data.getString("id");
                    fishData.property_title = json_data.getString("title");
                    fishData.property_action = json_data.getString("listed_name");
                    fishData.property_type = json_data.getString("category_name");
                    fishData.property_price = json_data.getString("price");
                    fishData.property_city_name = json_data.getString("city_name");
                    fishData.property_state_name = json_data.getString("state_name");
                    fishData.property_image = json_data.getString("image");
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new Pro_Adapter(MainActivity.this, data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            } catch (JSONException e) {
                SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent o = new Intent(MainActivity.this,MainActivity.class);
                startActivity(o);
                finish();
               // Toast.makeText(MainActivity.this, "Login Error!!Try Again", Toast.LENGTH_LONG).show();
            }

        }

    }


    private void log(){


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        String name = pref.getString("name", null);
        String id = pref.getString("user_id",null);
        String  image = pref.getString("image", null);
        email=pref.getString("email", null);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        assert navigationView != null;

        if(id == null) {

            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.profile).setVisible(false);
            nav_Menu.findItem(R.id.password).setVisible(false);
            nav_Menu.findItem(R.id.logout).setVisible(false);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    int id = menuItem.getItemId();

                    switch (id) {
                        case R.id.home:
                            Intent home = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(home);
                            finish();
                            // Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.login:
                            login();
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.register:
                            Intent in = new Intent(MainActivity.this, Register.class);
                            startActivity(in);
                            finish();
                            drawerLayout.closeDrawers();



                           /* break;
                        case R.id.logout:
                            SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            Intent s = new Intent(MainActivity.this,MainActivity.class );
                            s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(s);
                            finish();
                            drawerLayout.closeDrawers();*/
                    }
                    return true;
                }
            });

            //String ii = "http://krishiland.com/user_image/"+image;
            View header = navigationView.getHeaderView(0);
            ImageView mm = (ImageView) header.findViewById(R.id.imgg);
            TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
            ImageLoader imgLoader = new ImageLoader(getApplicationContext());
            imgLoader.DisplayImage(image, loader, mm);

            if(name == null){
                tv_email.setText("User Name");
            }else{
                tv_email.setText(name);
            }

            //Toast.makeText(getApplicationContext(), username,Toast.LENGTH_LONG).show();
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

                @Override
                public void onDrawerClosed(View v) {
                    super.onDrawerClosed(v);
                }

                @Override
                public void onDrawerOpened(View v) {
                    super.onDrawerOpened(v);
                }
            };
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();


        } else {

            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.login).setVisible(false);
            nav_Menu.findItem(R.id.register).setVisible(false);
              navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    int id = menuItem.getItemId();

                    switch (id) {
                        case R.id.home:
                            Intent home = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(home);
                            finish();
                            // Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.profile:
                            Intent profile = new Intent(MainActivity.this, Profile_Update.class);
                            startActivity(profile);
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.password:
                            Intent password = new Intent(MainActivity.this, Password_Update.class);
                            startActivity(password);

                            drawerLayout.closeDrawers();

                            break;
                        case R.id.logout:
                            SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent s = new Intent(MainActivity.this,MainActivity.class );
                            s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(s);
                            finish();
                            drawerLayout.closeDrawers();
                    }
                    return true;
                }
            });

            //String ii = "http://krishiland.com/user_image/"+image;
            View header = navigationView.getHeaderView(0);
            ImageView mm = (ImageView) header.findViewById(R.id.imgg);
            TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
            ImageLoader imgLoader = new ImageLoader(getApplicationContext());
            imgLoader.DisplayImage(image, loader, mm);

            if(name == null){
                tv_email.setText("User Name");
            }else{
                tv_email.setText(name);
            }

            //Toast.makeText(getApplicationContext(), username,Toast.LENGTH_LONG).show();
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

                @Override
                public void onDrawerClosed(View v) {
                    super.onDrawerClosed(v);
                }

                @Override
                public void onDrawerOpened(View v) {
                    super.onDrawerOpened(v);
                }
            };
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();




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
    private void hideItem()
    {
        NavigationView  navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.home).setTitle("New");
    }

    public void login(){


        dialog.setContentView(R.layout.user_login);

        editTextEmail = (EditText) dialog.findViewById(R.id.editTextEmail);
        //   editTextEmail.setHintTextColor(ContextCompat.getColor(this, R.color.White));
        editTextPassword = (EditText) dialog.findViewById(R.id.editTextPassword);
        //   editTextPassword.setHintTextColor(ContextCompat.getColor(this, R.color.White));
        TextView rg = (TextView) dialog.findViewById(R.id.linkSignup);
        rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gr = new Intent(MainActivity.this,Register.class);
                startActivity(gr);
            }
        });

        AppCompatButton buttonLogin = (AppCompatButton) dialog.findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = editTextEmail.getText().toString().trim();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("email",email);
                editor.apply();
                getProp();

                login_check();

            }
        });


        dialog.show();

    }

    private void login_check(){

        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();





        final ProgressDialog loading = ProgressDialog.show(MainActivity.this, "Authenticating", "Please wait", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://krishiland.com/krishi/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.trim().equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                            loading.dismiss();


                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();

                        }else{
                            loading.dismiss();

                            Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_EMAIL, email);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    editor = pref.edit();
                    editor.apply();
                    email=pref.getString("email", null);

                    URL url = new URL("http://krishiland.com/krishi/user_detail.php");

                    JSONObject postDataParams = new JSONObject();
                    Log.e("Value>>>>>", String.valueOf(postDataParams));
                    postDataParams.put("email", email);
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

                myJSON = result;
                prop();

            }


        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    private void prop() {

        try {

            JSONArray property_images = new JSONArray(myJSON);
            JSONObject c = property_images.getJSONObject(0);

            user_id = c.getString("u_id");
            usernamee = c.getString("username");
            image_url = c.getString("user_image");



            String ii = "http://krishiland.com/user_image/"+image_url;
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("user_id",user_id);
            editor.putString("name", usernamee);
            editor.putString("image", ii);
            editor.putString("email",email);
            editor.apply();

            Toast.makeText(MainActivity.this, user_id+" "+usernamee+" "+ii, Toast.LENGTH_SHORT).show();

        }

        //System.out.println(aa);

        catch (Exception e) {
            e.printStackTrace();
        }

    }




}
