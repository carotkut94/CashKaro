package com.death.cashkaro;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Provides UI for the view with Tiles.
 */
public class HomeFragment extends Fragment {

    ViewPager carouselContainer, dealContaier;
    GridLayoutManager mLayoutManager;
    RecyclerView storeContainer;
    ArrayList<StoreModel> storeModels;
    ArrayList<SliderModel> sliderModelsLower;
    ArrayList<SliderModel> sliderModels;
    private AdsRecyclerViewAdapter mAdapter;
    Document document = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareUI(view);
        if (NetworkUtils.isConnected(getContext())) {
            new FillModel().execute("http://cashkaro.com");
        }

    }

    public void prepareUI(View view) {
        carouselContainer = (ViewPager) view.findViewById(R.id.carouselContainer);
        dealContaier = (ViewPager) view.findViewById(R.id.dealsContainer);
        storeContainer = (RecyclerView) view.findViewById(R.id.storeContainer);
        storeModels = new ArrayList<>();
        sliderModels = new ArrayList<>();
        sliderModelsLower = new ArrayList<>();
        mAdapter = new AdsRecyclerViewAdapter(getContext(), storeModels);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        storeContainer.addItemDecoration(new GridSpacingItemDecoration(2, NetworkUtils.dpToPx(getContext(), 10), true));
        storeContainer.setLayoutManager(mLayoutManager);
        storeContainer.setItemAnimator(new DefaultItemAnimator());
        storeContainer.setAdapter(mAdapter);
    }


    private class FillModel extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("URL", params[0]);
            try {
                storeModels.clear();
                sliderModels.clear();
                document = Jsoup.connect(params[0]).followRedirects(true).get();

                for (Element element : document.select("li.fl.pos.col_4.row_2")) {
                    StoreModel model = new StoreModel();
                    model.setExternalLink(element.select("a").get(1).attr("abs:data-tab"));
                    model.setStoreLink(element.select("a").attr("abs:href"));
                    model.setImageLink(element.select("img").attr("abs:src"));
                    model.setOfferText(element.select("p").first().text());
                    model.setBottomText(element.select("p").get(1).text());
                    storeModels.add(model);
                }
                for (int k = 0; k < 4; k++) {
                    SliderModel sliderModel = new SliderModel();
                    sliderModel.setImageLink(document.select("img#banner" + (k + 1)).attr("abs:src"));
                    sliderModel.setBackendLink(document.select("a#bannerLink" + k).attr("abs:href"));
                    sliderModels.add(sliderModel);
                }
                for (Element element2 : document.select(".box")) {
                    SliderModel model = new SliderModel();
                    model.setImageLink(element2.select("img").attr("abs:src"));
                    model.setBackendLink(element2.select("a").attr("abs:href"));
                    sliderModelsLower.add(model);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            carouselContainer.setAdapter(new MyViewPagerAdapter(getContext(), sliderModels));
            dealContaier.setAdapter(new MyViewPagerAdapter(getContext(), sliderModelsLower));
            mAdapter.notifyDataSetChanged();
        }
    }
}
