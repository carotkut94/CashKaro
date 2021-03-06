package com.death.cashkaro;
/**
 * created by Sidhant rajora
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


public class MyViewPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private  ArrayList<SliderModel> photos;
    public MyViewPagerAdapter(Context context, ArrayList<SliderModel> ids) {
        photos = ids;
        this.context =context;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.add_container, container, false);
        ImageView imageViewPreview = (ImageView) view.findViewById(R.id.imageView);
        TextView textView = (TextView) view.findViewById(R.id.counter) ;
        textView.setText(" "+(position+1)+"/"+getCount()+" ");
        Glide.with(context).load(photos.get(position).getImageLink()).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageViewPreview);
        imageViewPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,ShowWebView.class).putExtra("URL", photos.get(position).getBackendLink()));
            }
        });
        container.addView(view);
        return view;
    }
    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((View) obj);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}