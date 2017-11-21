package com.ias.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ias.handler.Constant;
import com.ias.handler.RequestHandler;
import com.ias.iaslogin.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView imageView;

    public static final String KEY_IMAGE = "image";
    public static final String ID= "id";
    public static final String FULLNAME= "fullname";
    public static final String USER= "user";
    public static final String PASSWORD= "password";
    private static final int CAMERA_REQUEST = 1888;
    public static final String UPLOAD_URL = Constant.URL_STRING+"/UpdateMemberServlet";

    private int PICK_IMAGE_REQUEST = 1;

    private Bitmap bitmap;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.profile_fragment,container,false);
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        imageView = (CircleImageView) v.findViewById(R.id.imageView);
        imageView.setImageBitmap(getImageBitmap(sharedpreferences.getString("url_image",null)));

        ((TextView) v.findViewById(R.id.textProfilDetailName)).setText(sharedpreferences.getString("fullname",null));
        ((TextView) v.findViewById(R.id.textProfilDetailUserID)).setText(sharedpreferences.getString("username",null));
        ((TextView) v.findViewById(R.id.textProfilDetailStnkNumber)).setText(sharedpreferences.getString("no_stnk",null));
        ((TextView) v.findViewById(R.id.textProfilDetailChapterName)).setText(sharedpreferences.getString("chapter_name",null));
        ((TextView) v.findViewById(R.id.textProfilDetailBranchName)).setText(sharedpreferences.getString("branch_name",null));
        ((TextView) v.findViewById(R.id.textProfilDetailBrandName)).setText(sharedpreferences.getString("brand_name",null));
        ((TextView) v.findViewById(R.id.textProfilDetailCarTypeName)).setText(sharedpreferences.getString("car_type_name",null));
        ((TextView) v.findViewById(R.id.textProfilDetailCommunityAdminName)).setText(sharedpreferences.getString("community_admin_name",null));
        ((TextView) v.findViewById(R.id.textProfilDetailIasAdminName)).setText(sharedpreferences.getString("ias_admin_name",null));
        ((TextView) v.findViewById(R.id.textProfilDetailSimNumber)).setText(sharedpreferences.getString("no_sim",null));
        ((TextView) v.findViewById(R.id.textProfilDetailRegionName)).setText(sharedpreferences.getString("region",null));
        ((TextView) v.findViewById(R.id.textProfilDetailYear)).setText(sharedpreferences.getString("year",null));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });
        TextView fragment_title = (TextView) getActivity().findViewById(R.id.fragment_title);
        fragment_title.setText("Profile");
        return v;
    }
    private void showFileChooser() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });
        myAlertDialog.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            bitmap = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            byte[] decodedBytes = Base64.decode(encodedImage, 0);
            Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            imageView.setImageBitmap(bmp);
            uploadImage();
        }
    }

    public Bitmap getImageBitmap(String image){
        byte[] decodedBytes = Base64.decode(image, 0);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bmp;
    }

    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }


    public void uploadImage(){
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String id = sharedpreferences.getString("id",null);
        final String fullname = sharedpreferences.getString("fullname",null);
        final String user = "trci";
        final String password = "cihuy123!";
        final String image = getStringImage(bitmap);

        class UploadImage extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(),"Please wait...","uploading",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getContext(),"success change image profile",Toast.LENGTH_LONG).show();
                NavigationView navView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                View header = navView.getHeaderView(0);
                CircleImageView imageViewProfile = (CircleImageView) header.findViewById(R.id.imageViewProfile);
                imageViewProfile.setImageBitmap(getImageBitmap(image));
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("url_image", image);
                editor.commit();
            }

            public Bitmap getImageBitmap(String image){
                byte[] decodedBytes = Base64.decode(image, 0);
                Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                return bmp;
            }


            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<String,String>();

                param.put(KEY_IMAGE,image);
                param.put(ID,id);
                param.put(FULLNAME,fullname);
                param.put(USER,user);
                param.put(PASSWORD,password);
                String result = rh.sendPostRequest(UPLOAD_URL, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }
}