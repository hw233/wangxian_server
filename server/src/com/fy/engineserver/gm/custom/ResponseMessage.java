package com.fy.engineserver.gm.custom;

import org.apache.commons.httpclient.Header;

public class ResponseMessage {
	private String content;
	private Header responseHeaders[];
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Header[] getResponseHeaders() {
		return responseHeaders;
	}
	public void setResponseHeaders(Header[] responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
}
