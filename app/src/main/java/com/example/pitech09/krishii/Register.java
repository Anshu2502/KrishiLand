package com.example.pitech09.krishii;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pitech09 on 8/5/2016.
 */
public class Register extends AppCompatActivity implements View.OnClickListener  {

    EditText name, contact, email, password, confirm_password, editTextConfirmOtp;
    String name1, contact1, email1, password1, confirm_password1;
    private RequestQueue requestQueue;
    LayoutInflater li;
    private BroadcastReceiver mIntentReceiver;


    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.register);

        name = (EditText) findViewById(R.id.editTextName);
        name.setHintTextColor(ContextCompat.getColor(this, R.color.White));
        contact = (EditText) findViewById(R.id.editTextNumber);
        contact.setHintTextColor(ContextCompat.getColor(this, R.color.White));
        email = (EditText) findViewById(R.id.editTextEmail);
        email.setHintTextColor(ContextCompat.getColor(this, R.color.White));
        password = (EditText) findViewById(R.id.editTextPassword);
        password.setHintTextColor(ContextCompat.getColor(this, R.color.White));
        confirm_password = (EditText) findViewById(R.id.editTextConfirmPassword);
        confirm_password.setHintTextColor(ContextCompat.getColor(this, R.color.White));

        AppCompatButton buttonRegister = (AppCompatButton) findViewById(R.id.buttonRegister);
        requestQueue = Volley.newRequestQueue(this);
        assert buttonRegister != null;
        buttonRegister.setOnClickListener(this);
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


    private void confirmOtp() throws JSONException {
        li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);
        Button buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);

        final AlertDialog alertDialog = alert.create();

        alertDialog.show();

        IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mIntentReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("get_msg");
                msg = msg.replace("\n", "");
                String body = msg.substring(msg.lastIndexOf(":")+1, msg.length());
                String pNumber = msg.substring(0,msg.lastIndexOf(":"));
                editTextConfirmOtp.setText(body);
            }
        };

        this.registerReceiver(mIntentReceiver, intentFilter);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                alertDialog.dismiss();


                final ProgressDialog loading = ProgressDialog.show(Register.this, "Authenticating", "Please wait while we check the entered code", false, false);


                final String otp = editTextConfirmOtp.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://krishiland.com/krishi/confirm.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.equalsIgnoreCase("success")){


                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("email",email.getText().toString());
                                    editor.apply();

                                    loading.dismiss();

                                    Toast.makeText(Register.this,"Your Registration is Completed",Toast.LENGTH_LONG).show();

                                    startActivity(new Intent(Register.this, MainActivity.class));
                                    finish();
                                }else{

                                    Toast.makeText(Register.this,"Wrong OTP Please Try Again",Toast.LENGTH_LONG).show();
                                    try {

                                        confirmOtp();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                                Toast.makeText(Register.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();

                        params.put("otp", otp);
                        params.put("contact", contact.getText().toString().trim());
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    public void register(){

        name1 = name.getText().toString().trim();
        contact1 = contact.getText().toString().trim();
        email1 = email.getText().toString().trim();
        password1 = md5(password.getText().toString().trim());
        confirm_password1 = confirm_password.getText().toString().trim();

        final ProgressDialog loading = ProgressDialog.show(Register.this, "Registering", "Please wait", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Register_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.trim().equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                            //Creating a shared preference

                            try {
                                loading.dismiss();
                                confirmOtp();

                                Toast.makeText(getApplicationContext(), "Please Wait For OTP", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(Register.this, "Error", Toast.LENGTH_LONG).show();
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
                params.put("username", name1);
                params.put("contact", contact1);
                params.put("email", email1);
                params.put("password", confirm_password1);
                Log.i("VALUES----->>>>>",name1 +" "+" "+contact1+" "+ email1+ " "+ confirm_password1);
                return params;
            }
        };

        RequestQueue requestQueuee = Volley.newRequestQueue(this);
        requestQueuee.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        register();
    }


}
