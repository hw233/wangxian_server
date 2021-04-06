package com.xuanzhi.tools.authorize;

public class Role {
	protected String id;
	protected String name;
	protected boolean valid;
	
	protected Role(String id,String name){
		this.id = id;
		this.name = name;
		this.valid = true;
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
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
