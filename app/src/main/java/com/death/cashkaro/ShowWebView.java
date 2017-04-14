package com.death.cashkaro;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.singh.daman.proprogressviews.CircleArcProgress;

public class ShowWebView extends AppCompatActivity {

    WebView webView;
    CircleArcProgress circleArcProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_web_view);
        webView = (WebView) findViewById(R.id.urlLoader);
        circleArcProgress = (CircleArcProgress) findViewById(R.id.progressInd);
        circleArcProgress.setVisibility(View.INVISIBLE);
        String url = getIntent().getStringExtra("URL");

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                circleArcProgress.setVisibility(View.VISIBLE);
                view.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                circleArcProgress.setVisibility(View.INVISIBLE);
                int cx = (circleArcProgress.getLeft() + webView.getRight()) / 2;
                int cy = (circleArcProgress.getTop() + webView.getBottom()) / 2;

                int dx = Math.max(cx, webView.getWidth() - cx);
                int dy = Math.max(cy, webView.getHeight() - cy);
                float finalRadius = (float) Math.hypot(dx, dy);

                Animator animator;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    animator = ViewAnimationUtils.createCircularReveal(webView, cx, cy, 0, finalRadius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(1000);
                    animator.start();
                }
                view.setVisibility(View.VISIBLE);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
        {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}
