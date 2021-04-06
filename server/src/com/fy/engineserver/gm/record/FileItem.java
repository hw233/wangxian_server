package com.fy.engineserver.gm.record;

import java.io.File;

import com.xuanzhi.tools.text.StringUtil;


public class FileItem {
	private String path;
	
	private String name;
	
	private int type;
	
	private long size;
	
	private Folder parentFolder;
	
	private java.util.Date lastModifyDate;
	
	private StorageManager smanager;
	
	public FileItem(StorageManager smanager) {
		this.smanager = smanager;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Folder getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getWebPath() {
		String webroot = smanager.getWebRoot();
		if(path.startsWith(webroot)) {
			return StringUtil.urlEncode(path.substring(webroot.length()));
		}
		return "#";
	}

	public java.util.Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(java.util.Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	
	public void remove() {
		File file = new File(StringUtil.encode(path, smanager.getVmEncoding(), smanager.getSysEncoding()));
		if(file.isFile())
			file.delete();
	}
}
