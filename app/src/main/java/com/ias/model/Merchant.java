package com.ias.model;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Merchant {

    private static final String TAG = Merchant.class.getSimpleName();

	private String merchantCode;
	private String merchantKey;
	private Integer paymentAmount;
	private String merchantOrderId;
	private String productDetail;
	private String email;
	private String additionalParam;
	private String paymentMethod;
	private String merchantUserInfo;
	private String phoneNumber;
	private String itemDetails;
	private String returnUrl;
	private String callbackUrl;
	private String signature;
	
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	public Integer getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Integer paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	public String getProductDetail() {
		return productDetail;
	}
	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdditionalParam() {
		return additionalParam;
	}
	public void setAdditionalParam(String additionalParam) {
		this.additionalParam = additionalParam;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getMerchantUserInfo() {
		return merchantUserInfo;
	}
	public void setMerchantUserInfo(String merchantUserInfo) {
		this.merchantUserInfo = merchantUserInfo;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getItemDetails() {
		return itemDetails;
	}
	public void setItemDetails(String itemDetails) {
		this.itemDetails = itemDetails;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getSignature() {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			StringBuffer stringBuffer = new StringBuffer();
			
			stringBuffer.append(merchantCode).append(merchantOrderId).append(paymentAmount).append(merchantKey);
			
			md.update(stringBuffer.toString().getBytes());
			
			byte byteData[] = md.digest();

	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
			
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
            Log.d(TAG,e.getMessage());
            return "";
		}
	}	
}