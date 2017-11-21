package com.ias.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ias.handler.Constant;
import com.ias.handler.HttpHandler;
import com.ias.iaslogin.R;
import com.ias.model.ListMember;
import com.ias.model.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MemberFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.member_fragment,container,false);
        final ListView listView = (ListView) v.findViewById(R.id.listView);
        final EditText editsearch = (EditText) v.findViewById(R.id.search);
        new GetMemberTask(listView,editsearch).execute();
        TextView fragment_title = (TextView) getActivity().findViewById(R.id.fragment_title);
        fragment_title.setText("Member");
        return v;
    }


    private class GetMemberTask extends AsyncTask<String, Void, ListMember> {
        ArrayList<Member> memberList;
        ListView lv;
        EditText editText;

        private ProgressDialog dialog = new ProgressDialog(getActivity());
        public GetMemberTask( ListView lv,EditText editText){
            this.lv=lv;
            this.editText=editText;
        }
        

        @Override
        protected void onPreExecute(){
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected ListMember doInBackground(String... params) {
            ArrayList<Member> memberList = new ArrayList<>();

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = Constant.URL_STRING+"/GetMemberServlet?user=trci&password=cihuy123!";
            String jsonStr = sh.makeServiceCall(url,"GET");

            Log.e("TAG", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray member = jsonObj.getJSONArray("member");

                    // looping through All Contacts
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject c = member.getJSONObject(i);


                        Member m = new Member();
                        m.setUserName(c.getString("username")==null?null:c.getString("username"));
                        m.setFullName(c.getString("fullname")==null?null:c.getString("fullname"));
                        m.setStnkNumber(c.getString("no_stnk")==null?null:c.getString("no_stnk"));
                        m.setChapterName(c.getString("chapter_name")==null?null:c.getString("chapter_name"));
                        m.setImageProfile(null!=c.getString("url_image")?c.getString("url_image"):null);
                        m.setNoSim(c.getString("no_sim")==null?null:c.getString("no_sim"));
                        m.setBranchName(c.getString("branch_name")==null?null:c.getString("branch_name"));
                        m.setBrandName(c.getString("brand_name")==null?null:c.getString("brand_name"));
                        m.setCarTypeName(c.getString("car_type_name")==null?null:c.getString("car_type_name"));
                        m.setYear(c.getString("year")==null?null:c.getString("year"));
                        m.setCommunityAdminName(c.getString("community_admin_name")==null?null:c.getString("community_admin_name"));
                        m.setIasAdminName(c.getString("ias_admin_name")==null?null:c.getString("ias_admin_name"));
                        m.setRegionName(c.getString("region_name")==null?null:c.getString("region_name"));
                        m.setHp(c.getString("hp")==null?null:c.getString("hp"));
                        // adding contact to contact list
                        memberList.add(m);
                    }
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());


                }

            } else {
                Log.e("TAG", "Couldn't get json from server.");
            }
            final ListMember adapter = new ListMember(getContext(),memberList);

            return adapter;
        }

        protected void onPostExecute(final ListMember result) {
            Log.e("TAG Result",result.toString());

            lv.setAdapter(result);


            editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
//               adapter.getFilter().filter(arg0);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                    result.getFilter().filter(arg0.toString());
                }
            });

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

}