package com.xuanzhi.tools.authorize;

public class WebPagePermission {
	protected WebPage webpage;
	protected Role role;
	protected int permission;
	protected boolean accessOutOfficeEnable = false; 
	
	public boolean isAccessOutOfficeEnable() {
		return accessOutOfficeEnable;
	}

	public void setAccessOutOfficeEnable(boolean accessOutOfficeEnable) {
		this.accessOutOfficeEnable = accessOutOfficeEnable;
	}
	protected WebPagePermission(WebPage webpage,Role role,int p){
		this.permission = p;
		this.webpage = webpage;
		this.role = role;
	}
	public WebPage getWebpage() {
		return webpage;
	}
	public void setWebpage(WebPage webpage) {
		this.webpage = webpage;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public int getPermission() {
		return permission;
	}
	public void setPermission(int permission) {
		this.permission = permission;
	}
}
