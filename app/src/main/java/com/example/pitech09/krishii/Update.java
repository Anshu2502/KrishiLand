package com.example.pitech09.krishii;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Pitech09 on 10/4/2016.
 */
public class Update extends AppCompatActivity {

    Button profile, password;
    SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    int loader = R.drawable.plainicon;
    String name, image;

     public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.update_navigate);
         profile = (Button) findViewById(R.id.buttonProfile);
         password = (Button) findViewById(R.id.buttonPassword);
         SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
         editor = pref.edit();
         String  email_name=pref.getString("email", null);
         name=pref.getString("name", null);
         image=pref.getString("image", null);

         toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         Toast.makeText(Update.this, email_name, Toast.LENGTH_SHORT).show();

         profile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent profile = new Intent(Update.this,Profile_Update.class);
                 startActivity(profile);
             }
         });
         password.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent password = new Intent(Update.this,Password_Update.class);
                 startActivity(password);
             }
         });

         NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
         assert navigationView != null;
         navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(MenuItem menuItem) {

                 int id = menuItem.getItemId();

                 switch (id) {
                     case R.id.home:
                         Intent home = new Intent(Update.this, MainActivity.class);
                         startActivity(home);
                         finish();
                         Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                         drawerLayout.closeDrawers();
                         break;
                     case R.id.profile:

                         Intent profile = new Intent(Update.this, Update.class);
                         startActivity(profile);
                         drawerLayout.closeDrawers();
                         break;
                     case R.id.logout:
                         SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                         SharedPreferences.Editor editor = preferences.edit();
                         editor.clear();
                         editor.commit();
                         Intent s = new Intent(Update.this,Login.class );
                         s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         startActivity(s);
                         finish();
                         drawerLayout.closeDrawers();

                 }
                 return true;
             }
         });


         View header = navigationView.getHeaderView(0);
         ImageView mm = (ImageView) header.findViewById(R.id.imgg);
         TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
         ImageLoader imgLoader = new ImageLoader(getApplicationContext());
         imgLoader.DisplayImage(image, loader, mm);
         tv_email.setText(name);


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

    @Override
    public void onBackPressed()
    {

        super.onBackPressed();
        finish();
    }


}
