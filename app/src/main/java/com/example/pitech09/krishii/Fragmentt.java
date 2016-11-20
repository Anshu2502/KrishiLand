package com.example.pitech09.krishii;

/**
 * Created by Pitech09 on 9/7/2016.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragmentt extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(
                R.layout.fragmentt, container, false);
    }
}