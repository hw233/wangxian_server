package com.xuanzhi.tools.authorize;

public class PlatformPermission {

	protected Platform platform;
	protected Role role;
	protected int permission;
	protected boolean accessOutOfficeEnable = false; 
	
	public boolean isAccessOutOfficeEnable() {
		return accessOutOfficeEnable;
	}

	public void setAccessOutOfficeEnable(boolean accessOutOfficeEnable) {
		this.accessOutOfficeEnable = accessOutOfficeEnable;
	}

	protected PlatformPermission(Platform p,Role r,int permission){
		this.permission = permission;
		this.platform = p;
		this.role = r;
	}
	
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
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
