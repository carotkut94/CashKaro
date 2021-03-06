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

/**
 * Created by deathcode on 09/04/17.
 */

public class AdsRecyclerViewAdapter extends RecyclerView.Adapter<AdsRecyclerViewAdapter.MyViewHolder>  {



    private List<StoreModel> models;
    private Context mContext;

    public AdsRecyclerViewAdapter(Context context, List<StoreModel> models) {
        mContext = context;
        this.models = models;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final StoreModel model = models.get(position);
        holder.offerText.setText(model.getOfferText());
        holder.viewAllText.setText(model.getBottomText());
        holder.viewAllText.setPaintFlags(holder.viewAllText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Glide.with(mContext).load(model.getImageLink())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.logoImage);
        holder.seeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
                Toast.makeText(mContext, "Clicked", Toast.LENGTH_LONG).show();
                mContext.startActivity(new Intent(mContext, ShowWebView.class).putExtra("URL", model.getExternalLink()));
            }
        });

        holder.viewAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ShowWebView.class).putExtra("URL", model.getStoreLink()));
            }
        });

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
        ImageView logoImage;
        TextView offerText;
        TextView viewAllText;
        Button seeOffer;
        MyViewHolder(View itemView) {
            super(itemView);
            logoImage = (ImageView) itemView.findViewById(R.id.storeImage);
            offerText = (TextView) itemView.findViewById(R.id.offerText);
            seeOffer = (Button) itemView.findViewById(R.id.viewOffer);
            viewAllText = (TextView) itemView.findViewById(R.id.viewAll);


        }
    }
}
