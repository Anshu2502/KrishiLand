package com.example.pitech09.krishii;

/**
 * Created by Pitech09 on 9/7/2016.
 */
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentThree extends Fragment {

    onNameSetListenerrr onNameSetListenerrr;
    onNameSetListenerrr_id onNameSetListenerrr_id;
    String id, city;
    ListView list;
    ArrayList<HashMap<String, String>> location = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        list = (ListView)view.findViewById(R.id.list);
        return view;
    }

    public interface onNameSetListenerrr {
        void setNameee(String nameee);
    }

    public interface onNameSetListenerrr_id {
        void setNameee_id(String id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{

            onNameSetListenerrr = (onNameSetListenerrr) activity;
            onNameSetListenerrr_id = (onNameSetListenerrr_id) activity;

        }catch (Exception e){

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String url = "http://krishiland.com/krishi/city_filter.php";
        try {

            JSONArray data = new JSONArray(request(url));

            location = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                id = c.getString("city_id");
                city = c.getString("city_name");

                map = new HashMap<String, String>();
                map.put("idd", id);
                map.put("cityy", city);
                location.add(map);

            }
            ListAdapter adapter = new SimpleAdapter(getActivity(), location,R.layout.list_fragment,new String[] {"cityy" }, new int[] {R.id.texxt});
            list.setAdapter(adapter);
            //getListView().setOnItemClickListener(this);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String naam = location.get(+position).get("cityy");
                    String city_id = location.get(+position).get("idd");
                    onNameSetListenerrr.setNameee(naam);
                    onNameSetListenerrr_id.setNameee_id(city_id);

                }
            });
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private String request(String urlString) {
        // TODO Auto-generated method stub

        StringBuilder chaine = new StringBuilder("");
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }

        return chaine.toString();
    }


}