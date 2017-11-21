package com.ias.model;

public class ResponeHeader {
	String HTTPVersion;
	String responeCode;
	String reasonPhrase;
	public String getHTTPVersion() {
		return HTTPVersion;
	}
	public void setHTTPVersion(String hTTPVersion) {
		HTTPVersion = hTTPVersion;
	}
	public String getResponeCode() {
		return responeCode;
	}
	public void setResponeCode(String responeCode) {
		this.responeCode = responeCode;
	}
	public String getReasonPhrase() {
		return reasonPhrase;
	}
	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}
	
}
