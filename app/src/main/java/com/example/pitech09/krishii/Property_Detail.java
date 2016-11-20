package com.example.pitech09.krishii;

/**
 * Created by Pitech09 on 9/7/2016.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Property_Detail extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        OnMapReadyCallback, View.OnClickListener {

    TextView count, title, describtiona, addressa, citya, statea,pricea, before_pricea, after_pricea,security_price;
    String idd, myJSON, myyJSON, property_id, property_image, property_titlee, description, price, after_price, before_price,
            address,state, city, zip, neighborhood, size, lot_acre, property_status, time,security,monthly_amount,notice,
            type, category,tractor_modela, hours_pricea, day_pricea, branda, modela, name, image, totala, u_id,  email, image_url, user_id, usernamee;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    Intent ii;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker mCurrLocation;
    ImageView img_a, a1, a2, a3, a4;
    int i, q = 0;
    double lat, lon;
    String[] stringArray;
    Button more, query;
    SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    int loader = R.drawable.plainicon;
    Typeface font1;
    final Context context = this;
    TextView t1, t2, t3, t4, t5, t6 ,t7, t8, t9;
    EditText editTextEmail;
    EditText editTextPassword;
    Dialog dialog;
    Double myLatitude = null, myLongitude = null;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;


    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.detail);
        dialog = new Dialog(context);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        name = pref.getString("name", null);
        image = pref.getString("image", null);
        u_id = pref.getString("user_id",null);
        email=pref.getString("email", null);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ii = new Intent(Property_Detail.this, QueryForm.class);


        Bundle extras = getIntent().getExtras();
        TextView textView = (TextView)findViewById(R.id.txt);

        property_titlee = extras.getString("property_title");
        property_id = extras.getString("property_id");
        property_image = extras.getString("property_image");


        SharedPreferences preff = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preff.edit();
        editor.putString("property_id",property_id);
        editor.apply();

        log();

        font1= Typeface.createFromAsset(getAssets(), "Roboto-Italic.ttf");
        describtiona = (TextView) findViewById(R.id.describe);


        addressa = (TextView) findViewById(R.id.address);

        citya = (TextView) findViewById(R.id.city);
        statea = (TextView) findViewById(R.id.state);
        count = (TextView) findViewById(R.id.count_img);
        pricea = (TextView) findViewById(R.id.price);
        before_pricea = (TextView) findViewById(R.id.beforeprice);
        after_pricea = (TextView) findViewById(R.id.afterprice);
        security_price = (TextView) findViewById(R.id.secutity);
        describtiona.setTypeface(font1);
        addressa.setTypeface(font1);
        citya.setTypeface(font1);
        statea.setTypeface(font1);
        before_pricea.setTypeface(font1);
        after_pricea.setTypeface(font1);
        security_price.setTypeface(font1);

        t1 = (TextView)findViewById(R.id.addressheading);
        t2 = (TextView)findViewById(R.id.cityheading);
        t3 = (TextView)findViewById(R.id.stateheading);
        t4 = (TextView)findViewById(R.id.countryheading);
        t5 = (TextView)findViewById(R.id.indiaheading);
        t6 = (TextView)findViewById(R.id.priceheading);
        t7 = (TextView)findViewById(R.id.firstheading);
        t8 = (TextView)findViewById(R.id.secondheading);
        t9 = (TextView)findViewById(R.id.securityheading);
        t1.setTypeface(font1);
        t2.setTypeface(font1);
        t3.setTypeface(font1);
        t4.setTypeface(font1);
        t5.setTypeface(font1);
        t6.setTypeface(font1);
        t7.setTypeface(font1);
        t8.setTypeface(font1);
        t9.setTypeface(font1);
        pricea.setTypeface(font1);

        assert count != null;
        count.setVisibility(View.INVISIBLE);

        more = (Button) findViewById(R.id.more_now);
        query = (Button) findViewById(R.id.query_now);
        title = (TextView) findViewById(R.id.myImageViewText);
        img_a = (ImageView) findViewById(R.id.img1);

        a3 = (ImageView) findViewById(R.id.area3);
        a4 = (ImageView) findViewById(R.id.area4);
        title.setText(property_titlee);
        Glide.with(this).load("http://krishiland.com/user_image/" + property_image)
                .error(R.drawable.loader)
                .into(img_a);
        getStatus();

        getProperty();

        more.setOnClickListener(this);
        query.setOnClickListener(this);

        //logout();
        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


    }

    protected void status() {

        try {
            JSONArray property_images = new JSONArray(myJSON);
            Log.e("Value>>>>>", String.valueOf(property_images));
            stringArray = new String[property_images.length()];
            final ArrayList<String> a = new ArrayList<String>();
            for (i = 1; i < property_images.length(); i++) {
                JSONObject c = property_images.getJSONObject(i);
                stringArray[i] = c.getString("images");
                a.add("http://krishiland.com/user_image/" + stringArray[i]);
                q = q + 1;
            }

            final String aa = stringArray[1];
            String bb = stringArray[2];
            String cc = stringArray[3];

            a1 = (ImageView) findViewById(R.id.area1);
            a2 = (ImageView) findViewById(R.id.area2);
            a3 = (ImageView) findViewById(R.id.area3);
            a4 = (ImageView) findViewById(R.id.area4);

            if (aa != null) {
                Glide.with(this).load("http://krishiland.com/user_image/" + aa)
                        .error(R.drawable.loader)
                        .into(a1);
            } else {
                a1.setImageResource(R.drawable.loader);
            }

            Glide.with(this).load("http://krishiland.com/user_image/" + bb)
                    .error(R.drawable.loader)
                    .into(a2);

            Glide.with(this).load("http://krishiland.com/user_image/" + cc)
                    .error(R.drawable.loader)
                    .into(a3);


            if (q > 3) {
                count.setVisibility(View.VISIBLE);
                String dd = stringArray[4];
                count.setText("+" + q);
                count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent p = new Intent(Property_Detail.this, ImageSlide.class);
                        p.putStringArrayListExtra("list", a);
                        startActivity(p);
                        //Toast.makeText(After_Login_Detail.this, String.valueOf(q), Toast.LENGTH_SHORT).show();
                    }
                });
                Glide.with(this).load("http://krishiland.com/user_image/" + dd) .error(R.drawable.loader).into(a4);
            }
            //System.out.println(aa);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getStatus() {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            public void onPreExecute() {
                // Pbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {

                InputStream inputStream = null;
                String result = null;
                try {

                    URL url = new URL("http://krishiland.com/krishi/particular_detail.php");

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id", property_id);
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

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder("");
                        String line = "";
                        while ((line = in.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        result = sb.toString();
                    }

                    assert inputStream != null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    Log.i("tagconvertstr", "[" + result + "]");
                    System.out.println(e);
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                // Pbar.setVisibility(View.GONE);
                myJSON = result;
                status();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.more_now:
                more();
                break;
            case R.id.query_now:



                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                editor = pref.edit();
                editor.apply();
               String id=pref.getString("user_id", null);
                if(id==null){

                    login();
                }else{
                    startActivity(ii);
                }
                break;
            //handle multiple view click events
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        // Add a marker in Sydney and move the camera

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mFragment.getMapAsync(this);
                mGoogleMap = googleMap;
                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(lat, lon);
                mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Location"));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mGoogleMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) );

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFragment.getMapAsync(this);
                        latLng = new LatLng(lat, lon);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("onRequestPermissionsResult");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(5000); //5 seconds
                        mLocationRequest.setFastestInterval(3000); //3 seconds
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    }
                } else {
                    Toast.makeText(Property_Detail.this, "App Require Permission", Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;
        }

    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        requestLocationUpdates();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    private void requestLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {



          // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(lat, lon);
            mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Location"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mGoogleMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) );  // mCurrLocation = mGoogleMap.addMarker(markerOptions);
        }

    }


    @Override
    public void onLocationChanged(Location location) {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
             mFragment.getMapAsync(this);
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();

        }

    }

    private void propertyy() {

        try {

            JSONArray property_images = new JSONArray(myyJSON);
            JSONObject c = property_images.getJSONObject(0);

           type = c.getString("category_name");



            if (type.equals("Agriculture Equipments")){
                category = c.getString("listed_name");
                if(category.equals("Contract Farming")){
                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    description = c.getString("description");
                    price = c.getString("price");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    property_status = c.getString("property_status");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    zip = c.getString("zip");
                    security = c.getString("security_amt");
                    branda = c.getString("brand");
                    modela = c.getString("agriculture_model");
                    neighborhood = c.getString("neighborhood");
                    totala = c.getString("land_no");
                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);
                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }

                } else  if(category.equals("Lease")){
                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    description = c.getString("description");
                    price = c.getString("price");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    property_status = c.getString("property_status");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    zip = c.getString("zip");
                    security = c.getString("security_amt");
                    branda = c.getString("brand");
                    modela = c.getString("agriculture_model");
                    neighborhood = c.getString("neighborhood");
                    totala = c.getString("land_no");
                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);

                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }
                } else  if(category.equals("Rent")){
                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    description = c.getString("description");
                    price = c.getString("price");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    property_status = c.getString("property_status");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    zip = c.getString("zip");
                    security = c.getString("security_amt");
                    branda = c.getString("brand");
                    modela = c.getString("agriculture_model");
                    neighborhood = c.getString("neighborhood");
                    totala = c.getString("land_no");
                    monthly_amount = c.getString("month_price");
                    notice = c.getString("notice_period");
                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);


                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }
                }else  if(category.equals("Sales")){
                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    description = c.getString("description");
                    price = c.getString("price");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    property_status = c.getString("property_status");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    zip = c.getString("zip");
                    security = c.getString("security_amt");
                    branda = c.getString("brand");
                    modela = c.getString("agriculture_model");
                    neighborhood = c.getString("neighborhood");
                    totala = c.getString("land_no");
                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);
                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }
                }

            }else if(type.equals("Land")){

                category = c.getString("listed_name");


                if(category.equals("Rent")){

                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    description = c.getString("description");
                    neighborhood = c.getString("neighborhood");
                    size = c.getString("size_acre");
                    monthly_amount = c.getString("month_price");
                    price = c.getString("price");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    property_status = c.getString("status");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    security = c.getString("security_amt");
                    zip = c.getString("zip");
                    notice = c.getString("notice_period");
                    time = c.getString("time_pro");

                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);
                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }

                } else if(category.equals("Sales")){

                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    description = c.getString("description");
                    neighborhood = c.getString("neighborhood");
                    size = c.getString("size_acre");
                    price = c.getString("price");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    property_status = c.getString("status");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    security = c.getString("security_amt");
                    zip = c.getString("zip");
                    time = c.getString("time_pro");
                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);

                   // Toast.makeText(Property_Detail.this,before_price+"-"+after_price+"-"+security_price, Toast.LENGTH_SHORT).show();

                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }


                } else if(category.equals("Lease")){

                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    description = c.getString("description");
                    neighborhood = c.getString("neighborhood");
                    size = c.getString("size_acre");
                    price = c.getString("price");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    property_status = c.getString("status");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    security = c.getString("security_amt");
                    zip = c.getString("zip");
                    time = c.getString("time_pro");
                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);
                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{
                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{
                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        after_pricea.setText("Not Described");
                    }else{
                        after_pricea.setText(after_price);
                    }

                    if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }
                }

                else if(category.equals("Contract Farming")){


                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    description = c.getString("description");
                    neighborhood = c.getString("neighborhood");
                    size = c.getString("size_acre");
                    price = c.getString("price");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    property_status = c.getString("status");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    security = c.getString("security_amt");
                    zip = c.getString("zip");
                    time = c.getString("time_pro");


                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);
                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }


                }



            }else if(type.equals("Tractor")){
                category = c.getString("listed_name");

                if(category.equals("Sales")){
                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    neighborhood = c.getString("neighborhood");
                    description = c.getString("description");
                    price = c.getString("price");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");

                    //Toast.makeText(Property_Detail.this, before_price, Toast.LENGTH_SHORT).show();
                    security = c.getString("security_amt");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    zip = c.getString("zip");
                    tractor_modela = c.getString("tractor_model");
                    totala = c.getString("land_no");

                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);
                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }

                } else  if (category.equals("Rent")){
                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    neighborhood = c.getString("neighborhood");
                    description = c.getString("description");
                    price = c.getString("price");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    security = c.getString("security_amt");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    zip = c.getString("zip");
                    tractor_modela = c.getString("tractor_model");
                    totala = c.getString("land_no");
                    hours_pricea = c.getString("hours_price");
                    day_pricea = c.getString("day_price");
                    notice = c.getString("notice_period");

                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    Log.e("Sate>>>>>____---", String.valueOf(statea));
                    statea.setText(state);
                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }

                }
                else  if (category.equals("Lease")){
                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    neighborhood = c.getString("neighborhood");
                    description = c.getString("description");
                    price = c.getString("price");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    zip = c.getString("zip");
                    tractor_modela = c.getString("tractor_model");
                    totala = c.getString("land_no");
                    hours_pricea = c.getString("hours_price");
                    day_pricea = c.getString("day_price");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    security = c.getString("security_amt");
                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);
                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }

                }
                else  if (category.equals("Contract Farming")){
                    idd = c.getString("user_id");
                    ii.putExtra("userid",u_id);
                    neighborhood = c.getString("neighborhood");
                    description = c.getString("description");
                    price = c.getString("price");
                    lat = c.getDouble("latitude");
                    lon = c.getDouble("longitude");
                    address = c.getString("address");
                    state = c.getString("state_name");
                    city = c.getString("city_name");
                    zip = c.getString("zip");
                    tractor_modela = c.getString("tractor_model");
                    totala = c.getString("land_no");
                    hours_pricea = c.getString("hours_price");
                    day_pricea = c.getString("day_price");
                    after_price = c.getString("after_price");
                    before_price = c.getString("before_price");
                    security = c.getString("security_amt");
                    describtiona.setText(description);
                    addressa.setText(address);
                    citya.setText(city);
                    statea.setText(state);
                    if(price.equals("")){
                        pricea.setText("Not Described");
                    }else{

                        pricea.setText(price);
                    }
                    if(before_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        before_pricea.setText(before_price);
                    }
                    if(after_price.equals("")){
                        before_pricea.setText("Not Described");
                    }else{

                        after_pricea.setText(after_price);
                    } if(security.equals("")){
                        security_price.setText("Not Described");
                    }else{
                        security_price.setText(security);
                    }
                }

            }

/*
             address = c.getString("address");
             state = c.getString("state_name");
             city = c.getString("city_name");
             zip = c.getString("zip");

            size_acre = c.getString("size_acre");
            type = c.getString("listed_name");
            idd = c.getString("user_id");
            tractor_modela = c.getString("tractor_model");
            hours_pricea = c.getString("hours_price");
            day_pricea = c.getString("day_price");
            branda = c.getString("brand");
            modela = c.getString("model");


*/
        }
        //System.out.println(aa);

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void more() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

         if(type.equals("Land") && category.equals("Rent")){

            dialog.setContentView(R.layout.land_rent);
            TextView pstatus,neigha,sizea,ntime,time_duration;

            pstatus = (TextView) dialog.findViewById(R.id.statusb);
            neigha = (TextView) dialog.findViewById(R.id.neighboutb);
            sizea = (TextView) dialog.findViewById(R.id.sizeb);
            ntime = (TextView) dialog.findViewById(R.id.noticeb);
            time_duration = (TextView) dialog.findViewById(R.id.timeb);

            pstatus.setText(property_status);
            neigha.setText(neighborhood);
            sizea.setText(size);
            ntime.setText(notice);
            time_duration.setText(time);

        } else if(type.equals("Land") && category.equals("Sales")){
            dialog.setContentView(R.layout.land_sales);
            TextView pstatus,neigha,sizea,time_duration;

            pstatus = (TextView) dialog.findViewById(R.id.statusb);
            neigha = (TextView) dialog.findViewById(R.id.neighboutb);
            sizea = (TextView) dialog.findViewById(R.id.sizeb);
            time_duration = (TextView) dialog.findViewById(R.id.timeb);

            pstatus.setText(property_status);
            neigha.setText(neighborhood);
            sizea.setText(size);
            time_duration.setText(time);
        }  else if(type.equals("Land") && category.equals("Lease")){
            dialog.setContentView(R.layout.land_lease);
            TextView pstatus,neigha,sizea,time_duration;

            pstatus = (TextView) dialog.findViewById(R.id.statusb);
            neigha = (TextView) dialog.findViewById(R.id.neighboutb);
            sizea = (TextView) dialog.findViewById(R.id.sizeb);
            time_duration = (TextView) dialog.findViewById(R.id.timeb);

            pstatus.setText(property_status);
            neigha.setText(neighborhood);
            sizea.setText(size);
            time_duration.setText(time);
        }  else if(type.equals("Land") && category.equals("Contract Farming")){
            dialog.setContentView(R.layout.land_contractfarming);
             TextView pstatus,neigha,sizea,time_duration;

             pstatus = (TextView) dialog.findViewById(R.id.statusb);
             neigha = (TextView) dialog.findViewById(R.id.neighboutb);
             sizea = (TextView) dialog.findViewById(R.id.sizeb);
             time_duration = (TextView) dialog.findViewById(R.id.timeb);

             pstatus.setText(property_status);
             neigha.setText(neighborhood);
             sizea.setText(size);
             time_duration.setText(time);
        } else if (type.equals("Tractor") && category.equals("Contract Farming")){
             dialog.setContentView(R.layout.tractor_contractfarming);
             TextView model,neigha,total,time_duration;

             model = (TextView) dialog.findViewById(R.id.modelb);
             neigha = (TextView) dialog.findViewById(R.id.neighboutb);
             total = (TextView) dialog.findViewById(R.id.totalb);
             time_duration = (TextView) dialog.findViewById(R.id.timeb);

             model.setText(tractor_modela);
             neigha.setText(neighborhood);
             total.setText(size);
             time_duration.setText(time);
         } else if (type.equals("Tractor") && category.equals("Lease")) {
             dialog.setContentView(R.layout.tractor_lease);
             TextView model, neigha, total, time_duration;

             model = (TextView) dialog.findViewById(R.id.modelb);
             neigha = (TextView) dialog.findViewById(R.id.neighboutb);
             total = (TextView) dialog.findViewById(R.id.totalb);
             time_duration = (TextView) dialog.findViewById(R.id.timeb);

             model.setText(tractor_modela);
             neigha.setText(neighborhood);
             total.setText(totala);
             time_duration.setText(time);
         }
         else if (type.equals("Tractor") && category.equals("Rent")) {
             dialog.setContentView(R.layout.tractor_lease);
             TextView model, neigha, total, time_duration,day,hour,noticea;

             model = (TextView) dialog.findViewById(R.id.modelb);
             neigha = (TextView) dialog.findViewById(R.id.neighboutb);
             total = (TextView) dialog.findViewById(R.id.totalb);
             time_duration = (TextView) dialog.findViewById(R.id.timeb);
             day = (TextView) dialog.findViewById(R.id.dayb);
             hour  = (TextView) dialog.findViewById(R.id.hourb);
             noticea = (TextView) dialog.findViewById(R.id.noticeb);

             model.setText(tractor_modela);
             neigha.setText(neighborhood);
             total.setText(totala);
             day.setText(day_pricea);
             hour.setText(hours_pricea);
             noticea.setText(notice);
             time_duration.setText(time);
         }
         else if (type.equals("Tractor") && category.equals("Sales")) {
             dialog.setContentView(R.layout.tractor_sales);
             TextView model, neigha, total;

             model = (TextView) dialog.findViewById(R.id.modelb);
             neigha = (TextView) dialog.findViewById(R.id.neighboutb);
             total = (TextView) dialog.findViewById(R.id.totalb);

             model.setText(tractor_modela);
             neigha.setText(neighborhood);
             total.setText(totala);
         }  else if(type.equals("Agriculture Equipments") && category.equals("Contract Farming")) {
            // Toast.makeText(Property_Detail.this, type+" "+category, Toast.LENGTH_SHORT).show();
             dialog.setContentView(R.layout.equipment_contractfarming);
             TextView model, neigha, total, time_duration, brand;

             model = (TextView) dialog.findViewById(R.id.brandnumb);
             neigha = (TextView) dialog.findViewById(R.id.neighboutb);
             total = (TextView) dialog.findViewById(R.id.totalb);
             time_duration = (TextView) dialog.findViewById(R.id.timeb);
             brand = (TextView) dialog.findViewById(R.id.brandnameb);

             brand.setText(branda);
             model.setText(modela);
             neigha.setText(neighborhood);
             total.setText(totala);
             time_duration.setText(time);

         } else if (type.equals("Agriculture Equipments") && category.equals("Lease")) {
             dialog.setContentView(R.layout.equipment_contractfarming);
             TextView model, neigha, total, time_duration, brand;

             model = (TextView) dialog.findViewById(R.id.modelb);
             neigha = (TextView) dialog.findViewById(R.id.neighboutb);
             total = (TextView) dialog.findViewById(R.id.totalb);
             time_duration = (TextView) dialog.findViewById(R.id.timeb);
             brand = (TextView) dialog.findViewById(R.id.brandnameb);

             brand.setText(branda);
             model.setText(modela);
             neigha.setText(neighborhood);
             total.setText(totala);
             time_duration.setText(time);
         } else if (type.equals("Agriculture Equipments") && category.equals("Rent")) {
             dialog.setContentView(R.layout.equipment_contractfarming);
             TextView model, neigha, total, time_duration, brand, mont, noticeaa;

             model = (TextView) dialog.findViewById(R.id.modelb);
             neigha = (TextView) dialog.findViewById(R.id.neighboutb);
             total = (TextView) dialog.findViewById(R.id.totalb);
             time_duration = (TextView) dialog.findViewById(R.id.timeb);
             brand = (TextView) dialog.findViewById(R.id.brandnameb);
             mont = (TextView) dialog.findViewById(R.id.monthb);
             noticeaa = (TextView) dialog.findViewById(R.id.noticeb);

             brand.setText(branda);
             model.setText(modela);
             neigha.setText(neighborhood);
             total.setText(totala);
             time_duration.setText(time);
             mont.setText(monthly_amount);
             noticeaa.setText(notice);

         } else if (type.equals("Agriculture Equipments") && category.equals("Sales")) {
             dialog.setContentView(R.layout.equipment_contractfarming);
             TextView model, neigha, total, time_duration, brand;

             model = (TextView) dialog.findViewById(R.id.modelb);
             neigha = (TextView) dialog.findViewById(R.id.neighboutb);
             total = (TextView) dialog.findViewById(R.id.totalb);
             time_duration = (TextView) dialog.findViewById(R.id.timeb);
             brand = (TextView) dialog.findViewById(R.id.brandnameb);

             brand.setText(branda);
             model.setText(modela);
             neigha.setText(neighborhood);
             total.setText(totala);
             time_duration.setText(time);

         }

        Button dialogButton = (Button) dialog.findViewById(R.id.declineButton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

/*        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Property_Detail.this);
        LayoutInflater inflater = this.getLayoutInflater();
        //alertDialog.setView(inflater.inflate(R.layout.activity_maps, null));
        View content =  inflater.inflate(R.layout.activity_maps, null);
        alertDialog.setView(content);

        TextView landa = (TextView) findViewById(R.id.land);

          if(type.equals("Land") && category.equals("Rent")){

              Toast.makeText(Property_Detail.this, property_status+"-"+neighborhood+"-"+size+"-"+notice+"-"+time, Toast.LENGTH_SHORT).show();

              landa.setText("Propety Status: "+property_status);

              //landa.setText("Propety Status: "+property_status+"\n"+"Near By Properties: "+neighborhood+"\n"+"Size: "+size+ "\n"+ "Notice Period: "+notice+"\n"+"Time Duration: "+time);


        } else if(type.equals("Land") && category.equals("Sales")){
              landa.setText("Propety Status: "+property_status+"\n"+"Near By Properties: "+neighborhood+"\n"+"Size: "+size+ "\n"+"Time Duration: "+time);

          }     else if(type.equals("Land") && category.equals("Lease")){
              landa.setText("Propety Status: "+property_status+"\n"+"Near By Properties: "+neighborhood+"\n"+"Size: "+size+ "\n"+"Time Duration: "+time);

          }else if(type.equals("Land") && category.equals("Contract Farming")){
              landa.setText("Propety Status: "+property_status+"\n"+"Near By Properties: "+neighborhood+"\n"+"Size: "+size+ "\n"+"Time Duration: "+time);

          }

          else if(type.equals("Agriculture Equipments")){
            landa.setText("Near By Properties: "+neighborhood);
        } else if(type.equals("Tractor")){
            landa.setText("Near By Properties: "+neighborhood);
        }


        alertDialog.show();*/


/*
        // Create custom dialog object
        final Dialog dialog = new Dialog(Property_Detail.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("More Detail");
        this.getWindow().setBackgroundDrawableResource(R.color.green);


        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        if(neighborhood!=null && size_acre != null ){
            text.setText("Neighbour: "+neighborhood+"\n"+"\n"+"Land Size: "+size_acre+"\n"+"\n"+"Type: "+type);

        }

        else if(neighborhood!=null && tractor_modela!=null && hours_pricea!=null && day_pricea!=null&& branda!=null){
            text.setText("Neighbour: "+neighborhood+"\n"+"\n"+"Model "+tractor_modela+"\n"+"\n"+"Brand: "+branda+"\n"+"\n"+"Type: "+type+"\n"+"\n"+"Hour Price: "+hours_pricea+"\n"+"\n"+"Day Price: "+day_pricea);
        } else if (neighborhood!=null && branda!=null && modela!=null && hours_pricea!=null && day_pricea!=null) {

            text.setText("Neighbour: "+neighborhood+"\n"+"\n"+"Model "+modela+"\n"+"\n"+"Brand: "+branda+"\n"+"\n"+"Type: "+type+"\n"+"\n"+"Hour Price: "+hours_pricea+"\n"+"\n"+"Day Price: "+day_pricea);
        }

        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });
*/

    }


    public void getProperty(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            public void onPreExecute() {
                // Pbar.setVisibility(View.VISIBLE);
            }
            @Override
            protected String doInBackground(String... params) {

                InputStream inputStream = null;
                String result = null;
                try {

                    URL url = new URL("http://krishiland.com/krishi/particular_detail.php");

                    JSONObject postDataParams = new JSONObject();
                    Log.e("Value>>>>>", String.valueOf(postDataParams));
                    postDataParams.put("id", property_id);
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
                propertyy();

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

            Toast.makeText(Property_Detail.this, user_id+" "+usernamee+" "+ii, Toast.LENGTH_SHORT).show();

         }

        //System.out.println(aa);

        catch (Exception e) {
            e.printStackTrace();
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
                            Intent home = new Intent(Property_Detail.this, MainActivity.class);
                            startActivity(home);
                            finish();
                            // Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.login:
                            Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                            login();
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.register:
                            Intent in = new Intent(Property_Detail.this, Register.class);
                            startActivity(in);
                            finish();


                            drawerLayout.closeDrawers();

                    }
                    return true;
                }
            });

            String ii = "http://krishiland.com/user_image/"+image;
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
                            Intent home = new Intent(Property_Detail.this, MainActivity.class);
                            startActivity(home);
                            finish();
                            // Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.profile:
                            Intent profile = new Intent(Property_Detail.this, Profile_Update.class);
                            startActivity(profile);
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.password:
                            Intent password = new Intent(Property_Detail.this, Password_Update.class);
                            startActivity(password);

                            drawerLayout.closeDrawers();

                            break;
                        case R.id.logout:
                            SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            Intent s = new Intent(Property_Detail.this,MainActivity.class );
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

        dialog.dismiss();
        Intent in = new Intent(Property_Detail.this, MainActivity.class);
        startActivity(in);
        finish();


    }


    public void login(){




        dialog.setContentView(R.layout.user_login);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        editTextEmail = (EditText) dialog.findViewById(R.id.editTextEmail);
     //   editTextEmail.setHintTextColor(ContextCompat.getColor(this, R.color.White));
        editTextPassword = (EditText) dialog.findViewById(R.id.editTextPassword);
     //   editTextPassword.setHintTextColor(ContextCompat.getColor(this, R.color.White));
        TextView rg = (TextView) dialog.findViewById(R.id.linkSignup);
        rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gr = new Intent(Property_Detail.this,Register.class);
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





        final ProgressDialog loading = ProgressDialog.show(Property_Detail.this, "Authenticating", "Please wait", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://krishiland.com/krishi/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.trim().equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                            loading.dismiss();


                            Intent intent = new Intent(Property_Detail.this, QueryForm.class);
                            startActivity(intent);
                             dialog.dismiss();

                        }else{
                            loading.dismiss();

                            Toast.makeText(Property_Detail.this, "Invalid username or password", Toast.LENGTH_LONG).show();
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


/*

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.connect();
*/
/*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

*/


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

/*
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }
*/



}