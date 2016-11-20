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

public class FragmentTwo extends ListFragment implements OnItemClickListener {

    onNameSetListenerr onNameSetListenerr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_two, container, false);
    }


    public interface onNameSetListenerr {
        void setNamee(String namee);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{

            onNameSetListenerr = (onNameSetListenerr) activity;

        }catch (Exception e){

        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.type, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Object o = this.getListAdapter().getItem(position);
        String pen = o.toString();
        onNameSetListenerr.setNamee(pen);
        // Toast.makeText(getActivity(), pen, Toast.LENGTH_SHORT).show();
    }

}