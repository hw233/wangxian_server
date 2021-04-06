package com.fy.gamegateway.upload;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class StorageManager {
	protected static final Log log = LogFactory.getLog(StorageManager.class.getName());
	
	protected static StorageManager self;
	
	private String webRoot = "/usr/local/soidc/web";
	
	private String storageRoot  = "";
	
	private String sysEncoding;
	
	private String vmEncoding;
	
	public static StorageManager getInstance() {
		return self;
	}
	
	public void initialize() throws Exception{
		log.info("====================================================================================");
		log.info("					StorageManager initialized!");
		log.info("====================================================================================");
		self = this;
		System.out.println("["+StorageManager.class.getName()+"] [initialized]");
	}
	
	public Storage getStorage(String path) {
		String enpath = StringUtil.encode(path,vmEncoding,sysEncoding);
		File dir = new File(enpath);
		if(dir.isDirectory()) {
			Folder folder = new Folder(this);
			folder.setName(StringUtil.encode(dir.getName(),sysEncoding,vmEncoding));
			folder.setParentFolder(null);
			folder.setPath(path);
			folder.setLastModifyDate(new java.util.Date(dir.lastModified()));
			Storage storage = new Storage(this);
			storage.setName(folder.getName());
			storage.setRootFolder(folder);
			return storage;
		}
		return null;
	}
	
	public Storage createStorage(String path) {
		String enpath = StringUtil.encode(path,vmEncoding,sysEncoding);
		if(getStorage(path) == null) {
			FileUtils.chkFolder(enpath);
		}
		return getStorage(path);
	}

	public String getWebRoot() {
		return webRoot;
	}

	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	public String getSysEncoding() {
		return sysEncoding;
	}

	public void setSysEncoding(String sysEncoding) {
		this.sysEncoding = sysEncoding;
	}

	public String getVmEncoding() {
		return vmEncoding;
	}

	public void setVmEncoding(String vmEncoding) {
		this.vmEncoding = vmEncoding;
	}

	public void setStorageRoot(String storageRoot) {
		this.storageRoot = storageRoot;
	}

	public String getStorageRoot() {
		return storageRoot;
	}

}
