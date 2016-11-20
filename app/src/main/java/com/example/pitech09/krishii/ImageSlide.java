package com.example.pitech09.krishii;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * Created by Pitech09 on 9/2/2016.
 */
public class ImageSlide extends FragmentActivity {

    public ArrayList<Integer> fetchList= new ArrayList<Integer>();
    public String[] stockArr;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public void onCreate(Bundle SavedInsatnceState){
        super.onCreate(SavedInsatnceState);
        setContentView(R.layout.image_slide);

        if(Build.VERSION.SDK_INT >=23){
           if (checkPermission()){
               fetchList = getIntent().getIntegerArrayListExtra("list");
               stockArr = new String[fetchList.size()];
               stockArr = fetchList.toArray(stockArr);

               ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
               ImageAdapter adapter = new ImageAdapter(this);
               if (viewPager != null) {
                   viewPager.setAdapter(adapter);
               }else

               {
                   Toast.makeText(ImageSlide.this, "Internal System Error", Toast.LENGTH_SHORT).show();
               }
           }else{
               requestPermission();
           }
        }else{
            fetchList = getIntent().getIntegerArrayListExtra("list");
            stockArr = new String[fetchList.size()];
            stockArr = fetchList.toArray(stockArr);

            ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
            ImageAdapter adapter = new ImageAdapter(this);
            if (viewPager != null) {
                viewPager.setAdapter(adapter);
            }else

            {
                Toast.makeText(ImageSlide.this, "Internal System Error", Toast.LENGTH_SHORT).show();
            }
        }




    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ImageSlide.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE );
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(ImageSlide.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(ImageSlide.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(ImageSlide.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchList = getIntent().getIntegerArrayListExtra("list");
                    stockArr = new String[fetchList.size()];
                    stockArr = fetchList.toArray(stockArr);

                    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
                    ImageAdapter adapter = new ImageAdapter(this);
                    if (viewPager != null) {
                        viewPager.setAdapter(adapter);
                    }else

                    {
                        Toast.makeText(ImageSlide.this, "Internal System Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public class ImageAdapter extends PagerAdapter {
        Context context;

       /* String[] imagUrl={
                "http://s3.amazonaws.com/images.hitfix.com/assets/8674/20120919151318.jpg",
                "http://s3.amazonaws.com/images.hitfix.com/assets/8674/20120919151318.jpg",
                "http://s3.amazonaws.com/images.hitfix.com/assets/8674/20120919151318.jpg" };*/


        ImageAdapter(Context context){
            this.context=context;
        }
        @Override
        public int getCount() {
            return stockArr.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }





        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader imgLoader = new ImageLoader(context);
            int loader = R.drawable.loader;

            imgLoader.DisplayImage(stockArr[position], loader, imageView );
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }
}

