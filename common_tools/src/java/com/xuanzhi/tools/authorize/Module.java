package com.xuanzhi.tools.authorize;

import java.util.ArrayList;

public class Module {
	
	public static final int NO_PERMISSION = 0;
	public static final int PART_PERMISSION = 1;
	public static final int WHOLE_PERMISSION = 2;
	
	public static final String PERMISSION_NAMES[] = new String[]{"没有权限","部分权限","全部权限"};
	
	
	protected Platform platform;
	
	protected String id;
	protected String name;
	protected String uri;
	
	
	protected Module(Platform platform,String id,String name,String uri){
		this.platform = platform;
		this.id = id;
		this.name = name;
		this.uri = uri;
	}
	
	protected ArrayList<WebPage> webPageList = new ArrayList<WebPage>();
	
	public WebPage getWebPage(String id){
		for(int i = 0 ; i < webPageList.size() ; i++){
			WebPage m = webPageList.get(i);
			if(m.id != null && m.id.equals(id)){
				return m;
			}
		}
		return null;
	}
	
	public boolean isWebPageExists(String id){
		return (getWebPage(id) != null);
	}
	
	public WebPage[] getWebPages(){
		return webPageList.toArray(new WebPage[0]);
	}
	
	public WebPage addWebPage(String id,String name,String pagePattern){
		WebPage m = getWebPage(id);
		if(m != null){
			if(name != null && !name.equals(m.name)){
				m.setName(name);
			}
			if(pagePattern != null && !pagePattern.equals(m.pagePattern)){
				m.setPagePattern(pagePattern);
			}
			return m;
		}else{
			m = new WebPage(this,id,name,pagePattern);
			webPageList.add(m);
			return m;
		}
	}
	
	public void removeWebPage(String id){
		WebPage m = getWebPage(id);
		if(m != null){
			webPageList.remove(m);
		}
	}
	
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
