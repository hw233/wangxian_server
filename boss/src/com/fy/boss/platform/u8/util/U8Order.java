package com.fy.boss.platform.u8.util;

public class U8Order {

	private String order;
	private String extension;
	
	public U8Order(String order, String ext){
		this.order = order;
		this.extension = ext;
	}
	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
}
