package com.fy.engineserver.gm.custom;

import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;

public class ModuleCheck {
	private String url;

	public String getUrl() {
		return url;
	}
	private static ModuleCheck self;
	public void setUrl(String url) {
		this.url = url;
	}
    public void init(){
       self = this;	
    }
    public static  ModuleCheck getInstance(){
    	return self;
    }
	public String checkModule(String username, String url1) {
		String checkurl = url;
		HttpClient client = new HttpClient();
		HttpUtil hu = new HttpUtil();
		HashMap<String, String> mps = new HashMap<String, String>();
		mps.put("username", username);
		mps.put("url", url1);
		ResponseMessage mes = hu.doRequest(client, checkurl, "post", mps,
				"utf-8");
		String content = mes.getContent();
		// System.out.println(content);
		return content;

	}
	/*
	 * public static void main(String[] args) { checkModule("huangmin",
	 * "/game_boss/gm/gmmanager/server_manager.jsp"); }
	 */
}
