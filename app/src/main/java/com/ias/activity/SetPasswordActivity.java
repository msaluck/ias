package com.ias.activity;

/**
 * Created by user on 11/6/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ias.handler.Constant;
import com.ias.handler.RequestHandler;
import com.ias.iaslogin.R;

import java.util.HashMap;


public class SetPasswordActivity extends Activity {

    String code;
    String deviceID;
    String userMember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password_activity);
        Bundle bundle = getIntent().getExtras();

        final EditText password = (EditText) findViewById(R.id.password);
        final EditText password2 = (EditText) findViewById(R.id.password2);
        ((TextView) findViewById(R.id.userMember)).setText(bundle.getString("userMember"));
        userMember= bundle.getString("userMember").toString();
        Button activateButton = (Button) findViewById(R.id.activationButton);
        activateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (password.getText().toString().equals(password2.getText().toString())){
              activation(userMember.toString(),password.getText().toString());
                }
                else{
                    Toast.makeText(getApplicationContext(),"password and retype password doesn't match",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void activation(final String userMember, final String password) {

        final String userMember2=userMember;
        final String password2=password;
        System.out.println("User member : "+userMember);
        final String ACTIVATION_URL = Constant.URL_STRING+"/ActivationMemberServlet";

        class activationTask extends AsyncTask<String, Void, String> {
            private ProgressDialog dialog = new ProgressDialog(SetPasswordActivity.this);
            @Override
            protected void onPreExecute(){
                this.dialog.setMessage("Please wait");
                this.dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<String,String>();
                param.put(Constant.USER,Constant.USERNAMEAPP);
                param.put(Constant.PWD,Constant.PWDAPP);
                param.put("userMember",userMember2);
                param.put("passwordUser",password2);
                String result = rh.sendPostRequest(ACTIVATION_URL, param);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                System.out.println("result "+result);
               
                Intent i = new Intent(SetPasswordActivity.this,LoginActivity.class);
                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
//                if (Integer.parseInt(result)!=0){
//
//                    startActivity(i);
//                }
//                else {
//                    Toast toast = Toast.makeText(getApplicationContext(), "Set Password Gagal" ,Toast.LENGTH_LONG);
//                    toast.show();
//                }
            }
        }
        activationTask a = new activationTask();
        a.execute();
    }
}
