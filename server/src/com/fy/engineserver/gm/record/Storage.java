package com.fy.engineserver.gm.record;

import java.io.File;

import com.xuanzhi.tools.text.StringUtil;


public class Storage {
	private Folder rootFolder;
	
	private String name;
	
	private StorageManager smanager;
	
	public Storage(StorageManager smanager) {
		this.smanager = smanager;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Folder getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(Folder rootFolder) {
		this.rootFolder = rootFolder;
	}
	
	public Folder[] listFolders() {
		return rootFolder.listFolders();
	}
	
	public FileItem[] listFileItems() {
		return rootFolder.listFileItems();
	}
	
	public Folder getFolder(String path) {
		if(path.endsWith("/")) {
			path = path.substring(0, path.length()-1);
		}
		if(path.startsWith(rootFolder.getPath())) {
			File file = new File(StringUtil.encode(path,smanager.getVmEncoding(),smanager.getSysEncoding()));
			if(file.isDirectory())
			{
				Folder nfolder = rootFolder;
				while(!nfolder.getPath().equals(path)) {
					Folder fs[] = nfolder.listFolders();
					for(int i=0; i<fs.length; i++) {
						//如果fs[i]是path的父辈目录
						if(path.startsWith(fs[i].getPath())) {
							String subf = path.substring(fs[i].getPath().length());
							if(subf.startsWith(File.separator) || subf.length() == 0) {
								nfolder = fs[i];
								break;
							}
						}
					}
				}
				return nfolder;
			}
		}
		return null;
	}
	
	public FileItem getFileItem(String path) {
		if(path.startsWith(rootFolder.getPath())) {
			File file = new File(StringUtil.encode(path,smanager.getVmEncoding(),smanager.getSysEncoding()));
			if(file.isFile())
			{
				File parentFolder = file.getParentFile();
				Folder f = getFolder(StringUtil.encode(parentFolder.getPath(),smanager.getSysEncoding(),smanager.getVmEncoding()));
				String name = path.substring(path.lastIndexOf("/")+1);
				return f.getFileItem(name);
			}
		}
		return null;
	}
	
	public void remove(String path) {
		File file = new File(StringUtil.encode(path,smanager.getVmEncoding(),smanager.getSysEncoding()));
		if(file.isDirectory()) {
			Folder f = getFolder(path);
			f.remove();
		}
		if(file.isFile()) {
			FileItem item  = getFileItem(path);
			item.remove();
		}
	}
}
