package com.ias.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ias.activity.HomeActivity;
import com.ias.iaslogin.R;

/**
 * Created by mdm on 8/9/2017.
 */

public class DogeFragment extends Fragment {
    String urlRefer = "https://www.google.co.id/";
    WebView viewDoge;
    ProgressBar progressBar;
    View view;

    public DogeFragment(){

    }

//  run first
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doge, container, false);
        loadViewByUrl(view, getArguments().getString("url"));

        return view;
    }

    public void loadViewByUrl(View view, String url){
        viewDoge = (WebView) view.findViewById(R.id.webDoge);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        viewDoge.getSettings().setJavaScriptEnabled(true);

        viewDoge.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.VISIBLE);
                if (url.contains("facebook")){
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
                else {
                view.loadUrl(url);}
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);


//           for login
            }
        });

        viewDoge.loadUrl(url);
    }
}
