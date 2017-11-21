package com.ias.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ias.handler.HttpHandler;
import com.ias.iaslogin.R;
import com.ias.model.ListMember;
import com.ias.model.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class NewsFragment extends Fragment {

    String urlRefer = "https://oto.detik.com/";
    WebView viewDoge;
    ProgressBar progressBar;
    View view;

    public NewsFragment(){

    }

    //  run first
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_fragment, container, false);
        loadViewByUrl(view, urlRefer);
        TextView fragment_title = (TextView) getActivity().findViewById(R.id.fragment_title);
        fragment_title.setText("News");
        return view;
    }

    public void loadViewByUrl(View view, String url){
        viewDoge = (WebView) view.findViewById(R.id.webNews);
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
//                if (url.contains("facebook")){
//                    Intent intent = new Intent(getActivity(), HomeActivity.class);
//                    startActivity(intent);
//                }
//                else {
                view.loadUrl(url);
//            }
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