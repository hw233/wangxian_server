package com.fy.gamegateway.mieshi.resource.manager;

import java.io.File;
import java.util.ArrayList;

/**
 * 资源共享节点
 *
 */
public class ResourceSharedNode {

	public static class ResourcePackage{
		public String url;
	
		public String gpu;
		public String platform;
		public String resourceVersion;
		public String packageVersion;
		
		public long fileSize;
		public String filename;
		
		public boolean checkEnable = false;
		public long lastCheckTime = 0;
	}
	
	protected String name;
	
	//是否有效
	protected boolean enable;
	
	protected boolean initialized = false;
	
	//各个包的url地址
	//格式必须是 pmxx_res_1.3.5_force_43_pvr_res_Android.zip
	protected ArrayList<String> packageURLs = new  ArrayList<String>();
	
	protected ArrayList<ResourcePackage> rps = new  ArrayList<ResourcePackage>();
	
	/**
	 * 查找资源节点
	 * @param gpu
	 * @param platform
	 * @return
	 */
	public ResourcePackage getURLForDownLoad(String gpu,String platform){
		if(!initialized) init();
		
		for(int i = rps.size() -1 ; i >= 0 ; i--){
			ResourcePackage p = rps.get(i);
			if(p.gpu.equals(gpu) && p.platform.equals(platform) && p.checkEnable == true){
				return p;
			}
		}
		return null;
	}
	
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public boolean isEnable() {
		return enable;
	}



	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void addURL(String url){
		packageURLs.add(url);
		init();
	}
	
	public void removeURL(int index){
		if(index >= 0 && index < packageURLs.size()){
			packageURLs.remove(index);
		}
		init();
	}


	public ArrayList<String> getPackageURLs() {
		return packageURLs;
	}

	public ArrayList<ResourcePackage> getRps() {
		if(!initialized) init();
		return rps;
	}



	public synchronized void init(){
		ArrayList<ResourcePackage> tempRPS = new  ArrayList<ResourcePackage>();
		
		for(int i = 0  ; i < packageURLs.size()  ;i++){
			String s = packageURLs.get(i);
			
			int k = s.indexOf('?');
			if(k >= 0){
				s = s.substring(0,k);
			}
			k = s.lastIndexOf('/');
			if(k >= 0){
				s = s.substring(k+1);
			}
			ResourcePackage p = new ResourcePackage();
			p.filename = s;
			p.url = packageURLs.get(i);
			
			ResourceManager rm = ResourceManager.getInstance();
			if(rm != null){
				File file = new File(rm.getRealResourceRootPath() + "/" + p.filename);
				if(file.exists() == false){
					System.out.println("[解析资源包节点] [非法的资源包节点配置] [本地文件不存在] ["+name+"] ["+p.url+"]");
				}
				p.fileSize = file.length();
			}
			
			if(p.fileSize > 0 && s.startsWith("pmxx_res_") && s.endsWith(".zip")){
				
				
				for(int j = 0 ; j < ResourceMD5.PLATFORM_NAMES.length ; j++){
					if(s.endsWith("_"+ResourceMD5.PLATFORM_NAMES[j]+".zip")){
						p.platform = ResourceMD5.PLATFORM_NAMES[j];
						
						s = s.substring(0,s.length()- p.platform.length()- 5);
					}
				}
				
				for(int j = 0 ; j < ResourceMD5.GPU_NAMES.length ; j++){
					if(s.endsWith("_"+ResourceMD5.GPU_NAMES[j])){
						p.gpu = ResourceMD5.GPU_NAMES[j];
						
						s = s.substring(0,s.length()- p.gpu.length()- 1);
					}
				}
				
				k = s.lastIndexOf("_");
				if(k > 0){
					p.resourceVersion = s.substring(k+1);
					s = s.substring(0,k);
				}
				
				if(s.length() > "pmxx_res_".length()){
					p.packageVersion = s.substring("pmxx_res_".length());
				}
				
				if(p.gpu != null && p.packageVersion != null && p.platform != null && p.resourceVersion != null){
					tempRPS.add(p);
					
					System.out.println("[解析资源包节点] [成功] ["+name+"] ["+p.url+"]");

				}else{
					System.out.println("[解析资源包节点] [非法的资源包节点配置] [格式不对] ["+name+"] ["+p.url+"]");
				}
				
				
			}
		}
		rps = tempRPS;
		initialized = true;
	}
}
