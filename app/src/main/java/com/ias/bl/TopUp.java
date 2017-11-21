package com.ias.bl;

import android.util.Log;

import com.ias.handler.HttpHandler;
import com.ias.model.Item;
import com.ias.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TopUp {

    private final static String TAG = TopUp.class.getSimpleName();

    public String encodeObjectToJSON(Request request){
        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("merchantCode", request.getMerchant().getMerchantCode());
            requestJSON.put("paymentAmount", request.getMerchant().getPaymentAmount());
            requestJSON.put("paymentMethod", request.getMerchant().getPaymentMethod());
            requestJSON.put("merchantOrderId", request.getMerchant().getMerchantOrderId());
            requestJSON.put("productDetails", request.getMerchant().getProductDetail());
            requestJSON.put("additionalParam", request.getMerchant().getAdditionalParam());
            requestJSON.put("merchantUserInfo", request.getMerchant().getMerchantUserInfo());
            requestJSON.put("email", request.getMerchant().getEmail());
            requestJSON.put("phoneNumber", request.getMerchant().getPhoneNumber());

            JSONArray itemDetails = new JSONArray();
            for (Item item : request.getItems()) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("name", item.getName());
                jsonObject.put("price", item.getPrice());
                jsonObject.put("quantity", item.getQuantity());

                itemDetails.put(jsonObject.toString());
            }
            requestJSON.put("itemDetails", itemDetails);
            requestJSON.put("callbackUrl", request.getMerchant().getCallbackUrl());
            requestJSON.put("returnUrl", request.getMerchant().getReturnUrl());
            requestJSON.put("signature", request.getMerchant().getSignature());
        }
        catch (JSONException e){
            Log.e(TAG,e.getMessage());
        }
        return requestJSON.toString();
    }

    public HttpURLConnection createConnection(String urlPath, String requestMethod){
        URL url = null;
        try {
            url = new URL(urlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            return httpURLConnection;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public String doRequestTransaction(HttpURLConnection httpURLConnection, String jsonString) {
        OutputStream os = null;
        try {
            os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            System.out.println("write apa sih ? : "+jsonString);

            writer.write(jsonString);

            writer.flush();
            writer.close();

            os.close();

            int responseCode = httpURLConnection.getResponseCode();
            Log.i(TAG,"response transaction "+responseCode);

            if(responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer sb = new StringBuffer("");

                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();

                return sb.toString();
            } else {
                Log.w(TAG,"response is not unsupposed to");
                return "";
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }

    public HashMap<String,String> manageCallback(String respone){
        HashMap<String,String> objectMap =new HashMap<String,String>();
        System.out.println("manageCallback is invoked");
        if (respone != null && !"".equals(respone)) {
            System.out.println("masuk if");
            try {
                JSONObject jsonObject = new JSONObject(respone);
                // untuk sekarang hanya ambil url untuk masuk ke duitku.com
                List<String> keyJSON = new ArrayList<String>();
                Iterator<String> keys = jsonObject.keys();
                String a;
                List<String> b=null;
                while (keys.hasNext()) {
                    a=keys.next();
                    objectMap.put(a,jsonObject.getString(a).toString());
                }

//                for (String a : keyJSON){
//                    objectMap.put(a,jsonObject.getString(a).toString());
//                }
//
//                objectMap.put("paymentUrl",jsonObject.getString("paymentUrl").toString());
//
//                System.out.println("object map : "+jsonObject.getString("statusMessage").toString());
//                System.out.println("object map : "+jsonObject.getString("paymentUrl").toString());
            } catch (JSONException e){
                System.out.println("masuk catch");
                Log.e(TAG, e.getMessage());

            }

        } else {
            Log.e(TAG, "response is not available");

        }

        return objectMap;
    }

    public String redirect(String uri){
        return uri;
    }
}