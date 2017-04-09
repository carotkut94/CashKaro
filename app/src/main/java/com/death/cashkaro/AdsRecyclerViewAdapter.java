package com.death.cashkaro;

import android.content.Context;
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

        StoreModel model = models.get(position);
        holder.offerText.setText(model.getOfferText());
        holder.viewAllText.setText(model.getBottomText());
        Glide.with(mContext).load(model.getImageLink())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.logoImage);
        holder.seeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView logoImage;
        public TextView offerText;
        public TextView viewAllText;
        public Button seeOffer;
        public MyViewHolder(View itemView) {
            super(itemView);
            logoImage = (ImageView) itemView.findViewById(R.id.storeImage);
            offerText = (TextView) itemView.findViewById(R.id.offerText);
            seeOffer = (Button) itemView.findViewById(R.id.viewOffer);
            viewAllText = (TextView) itemView.findViewById(R.id.viewAll);

        }
    }
}
