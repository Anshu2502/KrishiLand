package com.example.pitech09.krishii;

/**
 * Created by Pitech09 on 9/7/2016.
 */
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class FragmentOne extends ListFragment implements OnItemClickListener {
    onNameSetListener onNameSetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    public interface onNameSetListener {
        void setName(String name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            onNameSetListener = (onNameSetListener) activity;

        }catch (Exception e){

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.action, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

        Object o = this.getListAdapter().getItem(position);
        String pen = o.toString();
        onNameSetListener.setName(pen);
        //Toast.makeText(getActivity(), pen, Toast.LENGTH_SHORT).show();

    }



}