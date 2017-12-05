package com.ias.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ias.fragment.EventFragment;
import com.ias.fragment.HomeFragment;
import com.ias.fragment.MemberFragment;
import com.ias.fragment.NewsFragment;
import com.ias.fragment.PaymentFragment;
import com.ias.fragment.ProfileFragment;
import com.ias.iaslogin.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);
        ImageView imageViewProfile = (ImageView) header.findViewById(R.id.imageViewProfile);
        imageViewProfile.setImageBitmap(getImageBitmap(sharedpreferences.getString("url_image",null)));


        if (extras != null) {
            String value = sharedpreferences.getString("fullname",null);
            String id = sharedpreferences.getString("id",null);

            Log.d("Home Activity",value);
            Toast.makeText(this,value + id, Toast.LENGTH_LONG).show();
            //The key argument here must match that used in the other activity
        }


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        HomeFragment cameraImportFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame,cameraImportFragment)
                .addToBackStack(null)
                .commit();


    }

    public Bitmap getImageBitmap(String image){
        byte[] decodedBytes = Base64.decode(image, 0);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bmp;
    }


    class ExecuteTask extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... params) {

           return null;
        }

        @Override
        protected void onPostExecute(String result) {

        Toast.makeText(getApplicationContext(),"berhasil", Toast.LENGTH_LONG).show();
        ArrayList<String> friends = new ArrayList<String>();
        friends.add("Budi");
        friends.add("Darman");

        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.content_home, friends); // simple textview for list item
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        }

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment cameraImportFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame,cameraImportFragment)
                    .addToBackStack(null)
                    .commit();

            // Handle the camera action
        } else if (id == R.id.nav_member) {

            MemberFragment galleryFragment= new MemberFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame,galleryFragment)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_event) {

            EventFragment eventFragment= new EventFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame,eventFragment)
                    .addToBackStack(null)
                    .commit();


        } else if (id == R.id.nav_news) {

            NewsFragment newsFragment= new NewsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame,newsFragment)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_profile) {
            ProfileFragment profileFragment= new ProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame,profileFragment)
                    .addToBackStack(null)
                    .commit();

        }
        else if (id == R.id.nav_location) {
            Intent i = new Intent(HomeActivity.this, MapsActivity.class);
            startActivity(i);

        }
        else if (id == R.id.nav_payment) {


            PaymentFragment paymentFragment= new PaymentFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame,paymentFragment)
                    .addToBackStack(null)
                    .commit();

        }
        else if (id == R.id.nav_logout) {
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();

            finish();
            Intent i = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
