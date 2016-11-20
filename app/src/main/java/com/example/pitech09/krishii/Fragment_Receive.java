package com.example.pitech09.krishii;

/**
 * Created by Pitech09 on 9/7/2016.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_Receive extends Fragment {
    TextView tt, ttt, tttt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receivee, container, false);
        tt = (TextView)view.findViewById(R.id.text11);
        ttt = (TextView)view.findViewById(R.id.text12);
        tttt = (TextView)view.findViewById(R.id.text13);
        tt.setVisibility(View.GONE);
        ttt.setVisibility(View.GONE);
        tttt.setVisibility(View.GONE);


        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tt.setVisibility(View.GONE);
            }
        });

        ttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttt.setVisibility(View.GONE);
            }
        });

        tttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tttt.setVisibility(View.GONE);
            }
        });


        return view;
    }

    public void updateInfo(String name){
        tt.setText(name);
        tt.setVisibility(View.VISIBLE);
    }
    public void updateInfo2(String name){
        ttt.setText(name);
        ttt.setVisibility(View.VISIBLE);
    }
    public void updateInfo3(String name){
        tttt.setText(name);
        tttt.setVisibility(View.VISIBLE);
    }




}
