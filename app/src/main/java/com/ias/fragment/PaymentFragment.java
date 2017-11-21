package com.ias.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ias.bl.TopUp;
import com.ias.handler.Constant;
import com.ias.iaslogin.R;
import com.ias.model.Item;
import com.ias.model.Merchant;
import com.ias.model.Request;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentFragment extends Fragment {
    String paymentMethod = "";
    Integer sumAmount;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.payment_fragment,container,false);
        final EditText amount = (EditText) v.findViewById(R.id.amount);
        Button buttonTopUp = (Button) v.findViewById(R.id.buttonTopUp);
        final Spinner spinner1 = (Spinner) v.findViewById(R.id.spinner1);

        buttonTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new TopUpTask().execute();
                /*Toast.makeText(v.getContext(),
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()) ,
                        Toast.LENGTH_SHORT).show();*/
                sumAmount=Integer.parseInt(amount.getText().toString());


                String selectedSpinner = String.valueOf(spinner1.getSelectedItem());

                switch (selectedSpinner) {
                    case "Duitku Wallet" : paymentMethod = "WW";
                        break;
                    case "Credit Card VISA/MASTER" : paymentMethod = "VC";
                        break;
                    case "BCA Klik Pay" : paymentMethod = "BK";
                        break;
                    case "Mandiri Click Pay" : paymentMethod = "MY";
                        break;
                    case "Permata Bank Virtual Account" : paymentMethod = "BT";
                        break;
                    case "Cimb Niaga Virtual Account" : paymentMethod = "B1";
                        break;
                    case "ATM Bersama" : paymentMethod = "A1";
                        break;
                    case "BNI Virtual Account" : paymentMethod = "I1";
                        break;
                    case "MayBank Virtual Account" : paymentMethod = "VA";
                        break;
                    case "Cimb Click" : paymentMethod = "CK";
                        break;
                }


                new TopUpTask().execute();

            }
        } );

        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        TextView fragment_title = (TextView) getActivity().findViewById(R.id.fragment_title);
        fragment_title.setText("Payment");
        return v;
    }

    public class TopUpTask extends AsyncTask<String, Void, String> {
        HashMap<String,String> objectMap = new HashMap<String,String> ();
        String resultEncode="";
        @Override
        protected void onPreExecute(){
            TopUp topUp = new TopUp();

            Request request = new Request();

            Merchant merchant = new Merchant();
            request.setMerchant(merchant);
            request.getMerchant().setMerchantCode("D2644");
            request.getMerchant().setMerchantKey("56c735fae3868230f588fd67ec28b1ed");
            request.getMerchant().setPaymentAmount(sumAmount);
            request.getMerchant().setPaymentMethod(paymentMethod);
            request.getMerchant().setMerchantOrderId("12345");
            request.getMerchant().setProductDetail("Payment for A shop");
            request.getMerchant().setEmail("your_customer@email.com");
            request.getMerchant().setPhoneNumber("1234567890");

            List<Item> items = new ArrayList<>();

            Item item1 = new Item();
            item1.setName("Top Up");
            item1.setPrice(sumAmount);
            item1.setQuantity(1);
            items.add(item1);

            request.setItems(items);

            request.getMerchant().setAdditionalParam("");
            request.getMerchant().setMerchantUserInfo("userinfo");
            request.getMerchant().setCallbackUrl(Constant.CALLBACK_URL);
            request.getMerchant().setReturnUrl(Constant.RETURN_URL);

            resultEncode = topUp.encodeObjectToJSON(request);

            System.out.println("result : "+resultEncode);
        }

        @Override
        protected String doInBackground(String... params) {
            TopUp topUp = new TopUp();

            String url = Constant.PAYMENT_URL;

            HttpURLConnection post = topUp.createConnection(url, "POST");

            System.out.println("param : "+resultEncode);

            String respone = topUp.doRequestTransaction(post, resultEncode);
            System.out.println(respone);
            objectMap = topUp.manageCallback(respone);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Bundle args = new Bundle();
            args.putString("url", objectMap.get("paymentUrl").toString());
            DogeFragment dogeFragment= new DogeFragment();
            dogeFragment.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, dogeFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        }
    }

    /*public class TopUpTask extends AsyncTask<String, Void, String>{

        public String responsePayment = "";

        @Override
        protected String doInBackground(String... params){

            TopUp topUp = new TopUp();

            Request request = new Request();

            Merchant merchant = new Merchant();
            request.setMerchant(merchant);
            request.getMerchant().setMerchantCode("D2425");
            request.getMerchant().setMerchantKey("0b3c603e443f1c569ffb308d5c2b3d7c");
            request.getMerchant().setPaymentAmount(15000);
            request.getMerchant().setPaymentMethod("VC");
            request.getMerchant().setMerchantOrderId("12345");
            request.getMerchant().setProductDetail("Payment for A shop");
            request.getMerchant().setEmail("your_customer@email.com");
            request.getMerchant().setPhoneNumber("1234567890");

            List<Item> items = new ArrayList<>();

            Item item1 = new Item();
            item1.setName("A");
            item1.setPrice(10000);
            item1.setQuantity(2);
            items.add(item1);

            Item item2 = new Item();
            item2.setName("B");
            item2.setPrice(5000);
            item2.setQuantity(1);
            items.add(item2);

            request.setItems(items);

            request.getMerchant().setAdditionalParam("");
            request.getMerchant().setMerchantUserInfo("userinfo");
            request.getMerchant().setCallbackUrl("http://www.lisensi.online/index.php");
            request.getMerchant().setReturnUrl("http://www.lisensi.online/");

            System.out.println("json : "+topUp.encodeObjectToJSON(request));

            URL url = null;
            try {
                System.out.println("masuk ke sini");
                url = new URL("http://sandbox.duitku.com/webapi/api/merchant/inquiry");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(topUp.encodeObjectToJSON(request));

                writer.flush();
                writer.close();

                os.close();

                int responseCode = connection.getResponseCode();

                System.out.println("respone code : "+responseCode);

                if(responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer sb = new StringBuffer("");

                    String line = "";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    System.out.println(sb.toString());
                    responsePayment = sb.toString();
                    return sb.toString();
                }
            } catch (Exception e) {
                System.out.println("ERROR");
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String url){
            Bundle args = new Bundle();
            try {
                System.out.println("On Post Execute");
                JSONObject jsonObject = new JSONObject(responsePayment);
                jsonObject.getString("paymentUrl");
                System.out.println("Payment URL : "+jsonObject.getString("paymentUrl"));
                args.putString("url", jsonObject.getString("paymentUrl"));
            } catch (JSONException e) {
                System.out.println(e.toString());
            }

            DogeFragment dogeFragment= new DogeFragment();
            dogeFragment.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, dogeFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        }
    }*/
}