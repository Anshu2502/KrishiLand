package com.example.pitech09.krishii;

/**
 * Created by Pitech09 on 9/7/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;

public class Pro_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Pro_Cons> data= Collections.emptyList();
    Pro_Cons current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public Pro_Adapter(Context context, List<Pro_Cons> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_pro, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Pro_Cons current=data.get(position);
        myHolder.type.setText(current.property_title);
        myHolder.action.setText("Type: " + current.property_action);
        myHolder.city.setText("City: " + current.property_city_name);
        myHolder.state.setText("State: " + current.property_state_name);
        myHolder.price.setText("Rs. " + current.property_price);
        myHolder.price.setTextColor(ContextCompat.getColor(context, R.color.green));

        // load image into imageview using glide
        Glide.with(context).load("http://krishiland.com/user_image/" + current.property_image)
                .placeholder(R.drawable.loading_image)
                .error(R.mipmap.ic_cancel)
                .into(myHolder.ivFish);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pro_Cons current=data.get(position);
                Intent intent = new Intent(context,Property_Detail.class);
                intent.putExtra("property_title",current.property_title);
                intent.putExtra("property_id",current.property_idd);
                intent.putExtra("property_image",current.property_image);
                context.startActivity(intent);
                ((Activity)context).finish();

                //Toast.makeText(context, String.valueOf(current.property_idd), Toast.LENGTH_LONG).show();
            }
        });

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        ImageView ivFish;
        TextView action, type, price, city, state;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.textFishName);
            ivFish = (ImageView) itemView.findViewById(R.id.ivFish);
            action = (TextView) itemView.findViewById(R.id.textSize);
            city = (TextView) itemView.findViewById(R.id.textType);
            state = (TextView) itemView.findViewById(R.id.textType2);
            price = (TextView) itemView.findViewById(R.id.textPrice);
        }

    }

}