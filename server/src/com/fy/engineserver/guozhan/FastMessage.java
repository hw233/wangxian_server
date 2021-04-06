package com.fy.engineserver.guozhan;

import java.io.Serializable;

/**
 *
 * 
 * @version 创建时间：May 5, 2012 10:08:32 AM
 * 
 */
public class FastMessage implements Serializable {
	
	public static final long serialVersionUID = 459434839489570L;
		
	private String icon;
	
	private String name;
	
	private String message;
	
	public FastMessage(String icon, String name, String message) {
		this.icon = icon;
		this.name = name;
		this.message = message;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public FastMessage cloneOne() {
		FastMessage m = new FastMessage(icon,name, message);
		return m;
	}
}
