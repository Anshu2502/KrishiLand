package com.example.pitech09.krishii;

/**
 * Created by Pitech09 on 9/9/2016.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Login extends Activity implements View.OnClickListener {

    //Defining views
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //Initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextEmail.setHintTextColor(ContextCompat.getColor(this, R.color.White));
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword.setHintTextColor(ContextCompat.getColor(this, R.color.White));
        TextView rg = (TextView) findViewById(R.id.linkSignup);

        rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gr = new Intent(Login.this,Register.class);
                startActivity(gr);
            }
        });

        AppCompatButton buttonLogin = (AppCompatButton) findViewById(R.id.buttonLogin);

        //Adding click listener
        assert buttonLogin != null;
        buttonLogin.setOnClickListener(this);
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        boolean loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            //Intent    intent = new Intent(LoginActivity.this, ProfileActivity.class);
            //startActivity(intent);
        }
    }

    private void login(){
        //Getting values from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final ProgressDialog loading = ProgressDialog.show(Login.this, "Authenticating", "Please wait", false, false);

        //Toast.makeText(getApplicationContext(),password,Toast.LENGTH_LONG).show();
        //Creating a string request
        //Config.LOGIN_URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://krishiland.com/krishi/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.trim().equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                            loading.dismiss();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email",email);
                            editor.apply();

                            //Starting profile activity
                            //Toast.makeText(getApplicationContext(),"User Found",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            loading.dismiss();
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View v) {
        //Calling the login function
        login();

    }

    @Override
    public void onBackPressed()
    {

        super.onBackPressed();
        finish();
    }

}