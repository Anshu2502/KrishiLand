package com.example.pitech09.krishii;

/**
 * Created by Pitech09 on 9/7/2016.
 */
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Fragment extends AppCompatActivity implements  FragmentOne.onNameSetListener, FragmentTwo.onNameSetListenerr, FragmentThree.onNameSetListenerrr, FragmentThree.onNameSetListenerrr_id {

    Button b1, b2, b3, b4;

    String a, b, c;
    SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    int loader = R.drawable.plainicon;
    String name, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        b1 = (Button)findViewById(R.id.appl);
        b2 = (Button)findViewById(R.id.button1);
        b3 = (Button)findViewById(R.id.button2);
        b4 = (Button)findViewById(R.id.button3);

        b1.setOnClickListener(mCorkyListener);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment   fr = new FragmentOne();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();
        b2.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgreen));
        b2.setTextColor(Color.WHITE);
        b3.setBackgroundColor(Color.WHITE);
        b3.setTextColor(Color.BLACK);
        b4.setBackgroundColor(Color.WHITE);
        b4.setTextColor(Color.BLACK);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        String  email_name =pref.getString("email", null);
        name=pref.getString("name", null);
        image=pref.getString("image", null);



        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.home:
                        Intent home = new Intent(Main_Fragment.this, MainActivity.class);
                        startActivity(home);
                        finish();
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.profile:
                        Intent profile = new Intent(Main_Fragment.this, Profile_Update.class);
                        startActivity(profile);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.password:
                        Intent password = new Intent(Main_Fragment.this, Password_Update.class);
                        startActivity(password);
                        drawerLayout.closeDrawers();

                        break;
                    case R.id.logout:
                        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent s = new Intent(Main_Fragment.this,Login.class );
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

    public void selectFrag(View view) {
        Fragment fr=null;

        switch(view.getId()){

            case R.id.button1:
                fr = new FragmentOne();
                b2.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgreen));
                b2.setTextColor(Color.WHITE);
                b3.setBackgroundColor(Color.WHITE);
                b3.setTextColor(Color.BLACK);
                b4.setBackgroundColor(Color.WHITE);
                b4.setTextColor(Color.BLACK);

                break;

            case R.id.button2:
                fr = new FragmentTwo();
                b2.setBackgroundColor(Color.WHITE);
                b2.setTextColor(Color.BLACK);
                b3.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgreen));
                b3.setTextColor(Color.WHITE);
                b4.setBackgroundColor(Color.WHITE);
                b4.setTextColor(Color.BLACK);
                break;
            case R.id.button3:
                fr = new FragmentThree();
                b2.setBackgroundColor(Color.WHITE);
                b2.setTextColor(Color.BLACK);
                b3.setBackgroundColor(Color.WHITE);
                b3.setTextColor(Color.BLACK);
                b4.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgreen));
                b4.setTextColor(Color.WHITE);




        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();

    }


    @Override
    public void setName(String name) {
        Fragment_Receive f2 = (Fragment_Receive)getFragmentManager().findFragmentById(R.id.fragment_receiveee);
        f2.updateInfo(name);
        if(name.equals("Contract Farming")){
            a = "1";
        } else if(name.equals("Lease")){

            a = "2";
        }else if(name.equals("Rent")){

            a = "3";
        }if(name.equals("Sales")){
            a = " 4";
        }

    }

    @Override
    public void setNamee(String namee) {
        Fragment_Receive f3 = (Fragment_Receive)getFragmentManager().findFragmentById(R.id.fragment_receiveee);
        f3.updateInfo2(namee);


        if( namee.equals("Land"))
        {
            b = "1";
        }
        else if (namee.equals("Agriculture Equipment")){
            b = "2";
        } else if (namee.equals("Tractor")){
            b = "3";
        }
    }

    @Override
    public void setNameee(String nameee) {
        Fragment_Receive f4 = (Fragment_Receive)getFragmentManager().findFragmentById(R.id.fragment_receiveee);
        f4.updateInfo3(nameee);

    }

    @Override
    public void setNameee_id(String id) {

        c = id.toString();

    }


    private View.OnClickListener mCorkyListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent ii = new Intent(Main_Fragment.this, MainActivity.class);
            ii.putExtra("action",a);
            ii.putExtra("type",b);
            ii.putExtra("cid",c);
            startActivity(ii);
            finish();
                 }
    };


}
