package com.xuanzhi.tools.authorize;

public class WebPage {
	
	public static final int NO_PERMISSION = 0;
	public static final int WHOLE_PERMISSION = 1;
	
	public static final String PERMISSION_NAMES[] = new String[]{"没有权限","访问权限"};
	
	
	protected Module module;
	protected String id;
	protected String name;
	protected String pagePattern;
	
	protected WebPage(Module module,String id,String name,String pagePattern){
		this.module = module;
		this.id = id;
		this.name = name;
		this.pagePattern = pagePattern;
	}
	
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
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
	public String getPagePattern() {
		return pagePattern;
	}
	public void setPagePattern(String pagePattern) {
		this.pagePattern = pagePattern;
	}
}
