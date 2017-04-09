package com.death.cashkaro;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager carouselContainer, dealContaier;
    GridLayoutManager mLayoutManager;
    RecyclerView storeContainer;
    ArrayList<StoreModel> storeModels;
    private AdsRecyclerViewAdapter mAdapter;
    Document document = null;
    String[] deals = new String[9];
    String d[] = new String[]{"http://asset6.ckassets.com/resources/image/slider_images/ck-storepage-v2/1x4/slide1.1491369320.png","http://asset6.ckassets.com/resources/image/slider_images/ck-storepage-v2/1x4/slide2.1491369320.png","http://asset6.ckassets.com/resources/image/slider_images/ck-storepage-v2/1x4/slide3.1491371727.png","http://asset6.ckassets.com/resources/image/slider_images/ck-storepage-v2/1x4/slide4.1491373109.png"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carouselContainer = (ViewPager) findViewById(R.id.carouselContainer);
        dealContaier = (ViewPager) findViewById(R.id.dealsContainer);
        storeContainer = (RecyclerView) findViewById(R.id.storeContainer);
        storeModels = new ArrayList<>();
        mAdapter = new AdsRecyclerViewAdapter(getApplicationContext(), storeModels);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        storeContainer.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        storeContainer.setLayoutManager(mLayoutManager);
        storeContainer.setItemAnimator(new DefaultItemAnimator());
        storeContainer.setAdapter(mAdapter);
        carouselContainer.setAdapter(new MyViewPagerAdapter(this, d));
        new FillModel().execute("http://cashkaro.com");
    }
    class FillModel extends AsyncTask<String,String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            Log.e("URL", params[0]);
            try {
                storeModels.clear();
                document = Jsoup.connect(params[0]).followRedirects(true).get();
                //Log.e("DOC", document.toString());
                for (Element element : document.select("li.fl.pos.col_4.row_2")) {
                    Log.e("ELEMENTS", element.toString());
                    StoreModel model = new StoreModel();
                    Log.e("IMAGE LINK", element.select("img").attr("abs:src"));
                    model.setImageLink(element.select("img").attr("abs:src"));
                    Log.e("ADD PARAGRAPH", element.select("p").first().text());
                    model.setOfferText(element.select("p").first().text());
                    Log.e("VIEW ALL", element.select("p").get(1).text());
                    model.setBottomText(element.select("p").get(1).text());
                    storeModels.add(model);
                }
                int i = 0;
                for(Element element2 : document.select(".box"))
                {
                    Log.e("ELEMENT2", element2.select("img").attr("abs:src"));
                    deals[i] = element2.select("img").attr("abs:src");
                    i+=1;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "EXCEPTION", Toast.LENGTH_LONG).show();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dealContaier.setAdapter(new MyViewPagerAdapter(MainActivity.this, deals));
            mAdapter.notifyDataSetChanged();
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
