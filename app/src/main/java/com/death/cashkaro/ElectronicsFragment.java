package com.death.cashkaro;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class ElectronicsFragment extends Fragment {
    RecyclerView electContainer;
    ArrayList<ElecModel> elecModels;
    private ElecRecyclerViewAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    Document document = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.electronics_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        electContainer = (RecyclerView) view.findViewById(R.id.elec_container);

        elecModels = new ArrayList<>();
        mAdapter = new ElecRecyclerViewAdapter(getContext(), elecModels);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        electContainer.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
        electContainer.setLayoutManager(mLayoutManager);
        electContainer.setItemAnimator(new DefaultItemAnimator());
        electContainer.setAdapter(mAdapter);

        if(NetworkUtils.isConnected(getContext())) {
            new FillModel().execute("http://cashkaro.com/product/electronics");
        }

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
                elecModels.clear();
                document = Jsoup.connect(params[0]).followRedirects(true).get();
                Log.e("DATA", document.toString());
                for (Element element : document.select("li.fl.col.one_col")) {
                    //Log.e("ELE", element.toString());
                    ElecModel model = new ElecModel();
                    model.setImageLink(element.select("img").attr("abs:src"));
                    model.setBackendLink(element.select("a").attr("abs:href"));
                    model.setAdText(element.select("h3").text());
                    elecModels.add(model);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
