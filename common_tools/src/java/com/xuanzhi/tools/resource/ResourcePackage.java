package com.xuanzhi.tools.resource;

/**
 * 代表着一个资源包
 * 
 *
 */
public class ResourcePackage {
	
	public int masterVersion;
	
	public int slaveVersion;
	
	public boolean fullPackage;
	
	public boolean aPackageFlag;
	
	public int fileNum;
	
	public long fileSize;
	
	public String downloadUrls[];

	public int getMasterVersion() {
		return masterVersion;
	}

	public void setMasterVersion(int masterVersion) {
		this.masterVersion = masterVersion;
	}

	public int getSlaveVersion() {
		return slaveVersion;
	}

	public void setSlaveVersion(int slaveVersion) {
		this.slaveVersion = slaveVersion;
	}

	public boolean isFullPackage() {
		return fullPackage;
	}

	public void setFullPackage(boolean fullPackage) {
		this.fullPackage = fullPackage;
	}

	public boolean isAPackageFlag() {
		return aPackageFlag;
	}

	public void setAPackageFlag(boolean packageFlag) {
		aPackageFlag = packageFlag;
	}

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String[] getDownloadUrls() {
		return downloadUrls;
	}

	public void setDownloadUrls(String[] downloadUrls) {
		this.downloadUrls = downloadUrls;
	}
	
	public String getIdentity(){
		return masterVersion + "." + slaveVersion+","+fullPackage+","+aPackageFlag;
	}
}
