package com.fy.engineserver.gm.record;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.xuanzhi.tools.text.StringUtil;


public class Folder {
	private String path;
	
	private String name;
	
	private Folder parentFolder;
	
	private java.util.Date lastModifyDate;
	
	private StorageManager smanager;
	
	public Folder(StorageManager smanager) {
		this.smanager = smanager;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Folder getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
	}
	
	public boolean rootFolder() {
		if(parentFolder == null)
			return true;
		return false;
	}
	
	public Folder getFolder(String name) {
		File file = new File(StringUtil.encode(path+"/"+name,smanager.getVmEncoding(),smanager.getSysEncoding()));
		if(file.isDirectory()) {
			Folder f = new Folder(smanager);
			f.setName(name);
			f.setParentFolder(this);
			f.setPath(path+"/"+name);
			f.setLastModifyDate(new java.util.Date(file.lastModified()));
			return f;
		}
		return null;
	}
	
	public FileItem getFileItem(String name) {
		File file = new File(StringUtil.encode(path+"/"+name,smanager.getVmEncoding(),smanager.getSysEncoding()));
		if(file.isFile()) {
			FileItem item = new FileItem(smanager);
			item.setName(name);
			item.setParentFolder(this);
			item.setPath(path+"/"+name);
			item.setSize(file.length());
			item.setLastModifyDate(new java.util.Date(file.lastModified()));
			String itempath = file.getPath();
			String suffix = itempath.substring(itempath.lastIndexOf(".")+1);
			item.setType(FileType.getType(suffix));
			return item;
		}
		return null;
	}
	
	public Folder[] listFolders() {
		File dir = new File(StringUtil.encode(path,smanager.getVmEncoding(),smanager.getSysEncoding()));
		File files[] = dir.listFiles();
		List<Folder> flist = new ArrayList<Folder>();
		for(int i=0; i<files.length; i++) {
			File _file = files[i];
			if(_file.isDirectory()) {
				Folder f = new Folder(smanager);
				f.setName(StringUtil.encode(_file.getName(),smanager.getSysEncoding(),smanager.getVmEncoding()));
				f.setParentFolder(this);
				f.setPath(StringUtil.encode(_file.getPath(),smanager.getSysEncoding(),smanager.getVmEncoding()));
				f.setLastModifyDate(new java.util.Date(_file.lastModified()));
				flist.add(f);
			}
		}
		return flist.toArray(new Folder[0]);
	}
	
	public FileItem[] listFileItems() {
		File dir = new File(StringUtil.encode(path,smanager.getVmEncoding(),smanager.getSysEncoding()));
		File files[] = dir.listFiles();
		List<FileItem> flist = new ArrayList<FileItem>();
		for(int i=0; i<files.length; i++) {
			File _file = files[i];
			if(_file.isFile()) {
				String _path = _file.getPath();
				FileItem item = new FileItem(smanager);
				item.setName(StringUtil.encode(_file.getName(),smanager.getSysEncoding(),smanager.getVmEncoding()));
				item.setParentFolder(this);
				item.setPath(StringUtil.encode(_path,smanager.getSysEncoding(),smanager.getVmEncoding()));
				item.setSize(_file.length());
				item.setLastModifyDate(new java.util.Date(_file.lastModified()));
				String suffix = _path.substring(_path.lastIndexOf(".")+1);
				int type = FileType.getType(suffix);
				item.setType(type);
				flist.add(item);
			}
		}
		return flist.toArray(new FileItem[0]);
	}
	
	public Folder createFolder(String name) {
		File file = new File(StringUtil.encode(path+"/"+name, smanager.getVmEncoding(), smanager.getSysEncoding()));
		if(!file.isDirectory()) 
			file.mkdir();
		Folder f = new Folder(smanager);
		f.setName(name);
		f.setParentFolder(this);
		f.setPath(file.getPath());
		f.setLastModifyDate(new java.util.Date(file.lastModified()));
		return f;
	}
	
	public void remove() {
		File file = new File(StringUtil.encode(path, smanager.getVmEncoding(), smanager.getSysEncoding()));
		if(file.isDirectory())
			file.delete();
	}
	
	public void removeFolder(String name) {
		File file = new File(StringUtil.encode(path+"/"+name, smanager.getVmEncoding(), smanager.getSysEncoding()));
		if(file.isDirectory())
			file.delete();
	}
	
	public void removeFile(String name) {
		File file = new File(StringUtil.encode(path+"/"+name, smanager.getVmEncoding(), smanager.getSysEncoding()));
		if(file.isFile())
			file.delete();
	}
	
	public void removeChildFolders() {
		File file = new File(StringUtil.encode(path, smanager.getVmEncoding(), smanager.getSysEncoding()));
		File subfiles[] = file.listFiles();
		for(int i=0; i<subfiles.length; i++) {
			File _file = subfiles[i];
			if(_file.isDirectory())
				_file.delete();
		}
	}
	
	public void removeFiles() {
		File file = new File(StringUtil.encode(path, smanager.getVmEncoding(), smanager.getSysEncoding()));
		File subfiles[] = file.listFiles();
		for(int i=0; i<subfiles.length; i++) {
			File _file = subfiles[i];
			if(_file.isFile())
				_file.delete();
		}
	}

	public java.util.Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(java.util.Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
}
