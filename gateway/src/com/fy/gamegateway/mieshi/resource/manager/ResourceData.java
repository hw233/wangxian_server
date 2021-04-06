package com.fy.gamegateway.mieshi.resource.manager;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;


public class ResourceData {

	public ResourceData(){
		
	}
	

	private String path;						//路径
	private int version;						//版本
	private int fileType;						//资源类型，主要是根据后缀区分
	private Hashtable<String, String> typeMD5;					//不同类型资源的MD5
	
	//文件的大小
	public int fileSize;
	
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public int getFileType() {
		return fileType;
	}

	public void setTypeMD5(Hashtable<String, String> typeMD5) {
		this.typeMD5 = typeMD5;
	}

	public Hashtable<String, String> getTypeMD5() {
		return typeMD5;
	}
	
	public void addTypeMD5(String type, String MD5){
		typeMD5.put(type, MD5);
	}

	
	public String toXmlString(){
		StringBuffer buff = new StringBuffer("");
		buff.append("	<files filepath=\"").append(path).append("\" ")
		.append("version=\"").append(version).append("\" ")
		.append("filetype=\"").append(fileType).append("\" ");
		buff.append(" filesize=\""+this.fileSize+"\" ");
		for (Enumeration<String> keys = typeMD5.keys(); keys.hasMoreElements(); ) {
			String key = keys.nextElement();
			String value = typeMD5.get(key);
			buff.append(key).append("MD5=\"").append(value).append("\" ");
		}
		buff.append("/>\n");
		return buff.toString();
	}
}
