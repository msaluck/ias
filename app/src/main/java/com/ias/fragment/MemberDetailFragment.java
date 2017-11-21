package com.ias.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ias.iaslogin.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class MemberDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.member_detail_fragment,container,false);
        ((ImageView) v.findViewById(R.id.imageProfileDetail)).setImageBitmap(getImageBitmap(getArguments().getString("urlImage")));
        ((TextView) v.findViewById(R.id.textViewDetailName)).setText(getArguments().getString("fullName"));
        ((TextView) v.findViewById(R.id.textViewDetailUserID)).setText(getArguments().getString("userID"));
        ((TextView) v.findViewById(R.id.textViewDetailStnkNumber)).setText(getArguments().getString("stnkNumber"));
        ((TextView) v.findViewById(R.id.textViewDetailChapterName)).setText(getArguments().getString("chapterName"));
        ((TextView) v.findViewById(R.id.textViewDetailBranchName)).setText(getArguments().getString("branchName"));
        ((TextView) v.findViewById(R.id.textViewDetailBrandName)).setText(getArguments().getString("brandName"));
        ((TextView) v.findViewById(R.id.textViewDetailCarTypeName)).setText(getArguments().getString("carTypeName"));
        ((TextView) v.findViewById(R.id.textViewDetailCommunityAdminName)).setText(getArguments().getString("communityAdminName"));
        ((TextView) v.findViewById(R.id.textViewDetailIasAdminName)).setText(getArguments().getString("iasAdminName"));
        ((TextView) v.findViewById(R.id.textViewDetailSimNumber)).setText(getArguments().getString("noSim"));
        ((TextView) v.findViewById(R.id.textViewDetailRegionName)).setText(getArguments().getString("regionName"));
        ((TextView) v.findViewById(R.id.textViewDetailYear)).setText(getArguments().getString("year"));
        ((TextView) v.findViewById(R.id.textViewDetailHp)).setText(getArguments().getString("hp"));
        TextView fragment_title = (TextView) getActivity().findViewById(R.id.fragment_title);
        fragment_title.setText(getArguments().getString("fullName"));
        return v;
    }
    public Bitmap getImageBitmap(String image){
        byte[] decodedBytes = Base64.decode(image, 0);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bmp;
    }




}