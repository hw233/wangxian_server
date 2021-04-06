package com.fy.gamegateway.mieshi.resource.manager;

/**
 * 只对Android的包
 *
 */
public class PackageSharedNode {

	String channel;
	
	String url;
	
	String packageVersion;
	
	String gupType;

	long fileSize;
	String filename;
	
	boolean checkEnable = false;
	long lastCheckTime = 0;
	
	boolean enable; 
	
	protected boolean initialized = false;
	
	
	public PackageSharedNode(String channel,String url){
		this.channel = channel;
		this.url = url;
		init();
	}
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public boolean isValid(){
		return enable && checkEnable && packageVersion != null;
	}
	

	public String getChannel() {
		return channel;
	}


	public String getUrl() {
		return url;
	}


	public String getPackageVersion() {
		return packageVersion;
	}

	public void setPackageVersion(String packageVersion) {
		this.packageVersion = packageVersion;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean isCheckEnable() {
		return checkEnable;
	}

	public void setCheckEnable(boolean checkEnable) {
		this.checkEnable = checkEnable;
	}

	public long getLastCheckTime() {
		return lastCheckTime;
	}

	public void setLastCheckTime(long lastCheckTime) {
		this.lastCheckTime = lastCheckTime;
	}
	
	public String getGupType() {
		return gupType;
	}

	public void setGupType(String gupType) {
		this.gupType = gupType;
	}

	public synchronized void init(){
			String s = url;
			
			int k = s.indexOf('?');
			if(k >= 0){
				s = s.substring(0,k);
			}
			k = s.lastIndexOf('/');
			if(k >= 0){
				s = s.substring(k+1);
			}
			filename = s;
			//mieshi_auto_1.5.2_half.apk
			if(s.startsWith("xunxian_android_") && s.indexOf("_half") >= 0){
				s = s.substring("xunxian_android_".length());
				
				k = s.lastIndexOf("_half");
				s = s.substring(0,k);
				
				packageVersion = s;

				System.out.println("[解析程序包节点] [成功] ["+channel+"] ["+url+"] [version:"+packageVersion+"]");
				
			}else{
				System.out.println("[解析程序包节点] [非法的程序包节点配置] [格式不对] ["+channel+"] ["+url+"]");
			}


		initialized = true;
	}
}
