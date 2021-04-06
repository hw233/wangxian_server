package com.xuanzhi.tools.authorize;

public class ModulePermission {
	protected Module module;
	protected Role role;
	protected int permission;
	protected boolean accessOutOfficeEnable = false; 
	
	public boolean isAccessOutOfficeEnable() {
		return accessOutOfficeEnable;
	}

	public void setAccessOutOfficeEnable(boolean accessOutOfficeEnable) {
		this.accessOutOfficeEnable = accessOutOfficeEnable;
	}
	protected ModulePermission(Module module,Role role,int p){
		this.module = module;
		this.role =role;
		this.permission = p;
	}
	
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
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
