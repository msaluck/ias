package com.ias.activity;

/**
 * Created by user on 11/6/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ias.handler.Constant;
import com.ias.handler.RequestHandler;
import com.ias.iaslogin.R;
import com.ias.model.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import me.philio.pinentry.PinEntryView;


public class ActivationActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activation_account);
        final PinEntryView pinEntryView = (PinEntryView) findViewById(R.id.code);

        Button activateButton = (Button) findViewById(R.id.activationButton);
        activateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                activation(pinEntryView.getText().toString());
            }
        });
    }

    public void activation(String code) {
        final String activationCode = code;
        final String ACTIVATION_URL = Constant.URL_STRING + "/ActivationMemberServlet";

        class activationTask extends AsyncTask<String, Void, String> {
            private ProgressDialog dialog = new ProgressDialog(ActivationActivity.this);

            @Override
            protected void onPreExecute() {
                this.dialog.setMessage("Please wait");
                this.dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> param = new HashMap<String, String>();
                param.put(Constant.USER, Constant.USERNAMEAPP);
                param.put(Constant.PWD, Constant.PWDAPP);
                param.put(Constant.ACTCODE, activationCode);
                String result = rh.sendPostRequest(ACTIVATION_URL, param);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                System.out.println(result);
                Member m = new Member();


                try {
                    JSONObject jsonObj = new JSONObject(result);

                    // Getting JSON Array node
                    JSONArray member = jsonObj.getJSONArray("member");

                    // looping through All Contacts
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject c = member.getJSONObject(i);


                        m = new Member();
                        m.setUserName(c.getString("username") == null ? null : c.getString("username"));


                        // adding contact to contact list

                    }
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());


                }

                if (null != m.getUserName()) {

                    Bundle bundle = new Bundle();
                    System.out.println(m.getUserName());
                    bundle.putString("userMember", m.getUserName());
                    Intent i = new Intent(ActivationActivity.this, SetPasswordActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);

                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Code yang anda masukkan salah", Toast.LENGTH_LONG).show();

                    }
                }
            }
        }
            activationTask a = new activationTask();
            a.execute();

    }
}
