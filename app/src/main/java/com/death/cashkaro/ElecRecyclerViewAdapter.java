package com.death.cashkaro;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ElecRecyclerViewAdapter extends RecyclerView.Adapter<ElecRecyclerViewAdapter.MyViewHolder>  {



    private List<ElecModel> models;
    private Context mContext;

    public ElecRecyclerViewAdapter(Context context, List<ElecModel> models) {
        mContext = context;
        this.models = models;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elecmodel_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        final ElecModel model = models.get(position);
        holder.adText.setText(model.getAdText());
        holder.shopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
                mContext.startActivity(new Intent(mContext, ShowWebView.class).putExtra("URL", model.getBackendLink()));
            }
        });
        Glide.with(mContext).load(model.getImageLink()).diskCacheStrategy(DiskCacheStrategy.NONE).dontTransform().crossFade().into(holder.adImage);
    }

    private void showNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Hurray!!")
                        .setContentText("You just unlocked a deal!");

        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView adImage;
        TextView adText;
        Button shopNow;
        MyViewHolder(View itemView) {
            super(itemView);
            adImage = (ImageView) itemView.findViewById(R.id.card_image);
            adText = (TextView) itemView.findViewById(R.id.ad);
            shopNow = (Button) itemView.findViewById(R.id.showOffer);

        }
    }
}
