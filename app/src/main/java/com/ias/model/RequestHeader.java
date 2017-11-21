package com.ias.model;

public class RequestHeader {

	String method;
	String requestURI;
	String HTTPVersion;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRequestURI() {
		return requestURI;
	}
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}
	public String getHTTPVersion() {
		return HTTPVersion;
	}
	public void setHTTPVersion(String hTTPVersion) {
		HTTPVersion = hTTPVersion;
	}	
}