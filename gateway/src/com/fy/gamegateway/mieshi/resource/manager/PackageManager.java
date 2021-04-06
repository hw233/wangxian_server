package com.fy.gamegateway.mieshi.resource.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.InnerTesterManager;
import com.fy.gamegateway.mieshi.server.MieshiGatewayServer;
import com.xuanzhi.tools.text.FileUtils;
/**
 * 管理apk包
 * 
 * 包的目录结构如下
 *  版本
 *    渠道
 *      平台
 *         包
 *  包的名称如下： mieshi_pvr_full.apk
 *  
 *         其中 pvr,png,ati,etc标识包的类型，根据客户端的gpu来选择
 *             full,half 是全资源包还是半资源包
 *             
 *  版本的格式：
 *  	  1.2.3_force
 *  	  1.2.4
 *             
 * 
 *
 */
public class PackageManager {
	
	static Logger logger = Logger.getLogger(MieshiGatewayServer.class);
	
	private static PackageManager self;
	public static PackageManager getInstance(){
		return self;
	}
	
	public Map<String,Boolean> 需要用户手动更新客户端的列表 = new Hashtable<String,Boolean>();
	public static String[] 渠道英文名 = new String[]{"DCN_MIESHI","UC_MIESHI","other"};
	public static String[] 渠道中文名 = new String[]{"当乐","UC","其他"};
	public static String[] 手动更新描述 = new String[]{"亲爱的玩家：《缥缈寻仙曲OL》封测客户端已经发布最新版本，版本号为1.3.5 请玩家主动去当乐论坛http://a.d.cn/netgame/104/下载最新版本的客户端，并且完成安装，原有客户端和资源文件不用删除。给您造成不便，敬请谅解。","亲爱的玩家：《缥缈寻仙曲OL》封测客户端已经发布最新版本，版本号为1.3.5 请玩家主动去UC论坛 http://ms.9game.cn/downgm2/1930下载最新版本的客户端，并且完成安装，原有客户端和资源文件不用删除。给您造成不便，敬请谅解。","亲爱的玩家：《缥缈寻仙曲OL》封测客户端已经发布最新版本，版本号为1.3.5 请玩家主动去官方地址http://www.sqage.com/mieshi.apk下载最新版本的客户端，并且完成安装，原有客户端和资源文件不用删除。给您造成不便，敬请谅解。"};
	
	public static class Version implements java.lang.Comparable<Version>{
		public ArrayList<Integer> versionNumList = new ArrayList<Integer>();
		public boolean forceUpdate;
		public String versionString;
		
		public ArrayList<Package> packageList = new ArrayList<Package>();
		
		public Version(String version){
			
			versionString = version;
			
			if(version == null) return;
			int k = version.indexOf("_");
			if(k >= 0){
				String s = version.substring(k+1);
				//这个force是加在版本文件夹上
				if(s.contains("force")){
					forceUpdate = true;
				}else{
					forceUpdate =false;
				}
				
				version = version.substring(0,k);
			}
			
			String ss[] = version.split("\\.");
			for(int i = 0 ; i < ss.length ; i++){
				try{
					Integer v = Integer.parseInt(ss[i].trim());
					versionNumList.add(v);
				}catch(Exception e){
					throw new java.lang.IllegalArgumentException("错误的版本串:"+versionString);
				}
				
			}
		}

		public int compareTo(Version o) {
			if(this == o) return 0;
			
			for(int i = 0 ; i < versionNumList.size() && i < o.versionNumList.size() ; i++){
				Integer x = versionNumList.get(i);
				Integer ox = o.versionNumList.get(i);
				
				if(x.intValue() < ox.intValue()) return -1;
				if(x.intValue() > ox.intValue()) return 1;
			}
			
			if(versionNumList.size() < o.versionNumList.size()){
				return -1;
			}
			
			if(versionNumList.size() > o.versionNumList.size()){
				return 1;
			}
			
			return 0;
		}
		public String toString(){
			return this.versionString;
		}
		
	}
	public static final String FULL_FLAGS[] = new String[]{"full","half"};
	public static final String GPU_NAMES[] = new String[]{"ios_res","android_res","png_res"};
	
	public static class Package{
		public Version version;
		public String channel;
		public String platform;
		public String gpuFlag;
		public String fullFlag;
		
		public int packageSize;
		public long lastModifiedTime;
		
		public String httpDownloadURL;
		
		public File packageFile;
		
		public Package(String version,String channel, String platform, File file,String httpDownloadURL){
			this.version = new Version(version);
			this.channel = channel;
			this.platform = platform;
			this.lastModifiedTime = file.lastModified();
			this.packageSize = (int)file.length();
			packageFile = file;
			String name = file.getName();
			for(int i = 0 ; i < GPU_NAMES.length ; i++){
				if(name.toLowerCase().indexOf(GPU_NAMES[i]) >= 0){
					gpuFlag = GPU_NAMES[i];
				}
			}
			
			if(gpuFlag == null){
				throw new java.lang.IllegalArgumentException("错误的包文件名，没有对应的GPU:"+file);
			}
			

			for(int i = 0 ; i < FULL_FLAGS.length ; i++){
				if(name.toLowerCase().indexOf(FULL_FLAGS[i]) >= 0){
					fullFlag = FULL_FLAGS[i];
				}
			}
			
			if(fullFlag == null){
				throw new java.lang.IllegalArgumentException("错误的包文件名，没有对应的FULLFlag:"+file);
			}
			
			this.httpDownloadURL = httpDownloadURL;
		}

	}
	
	//资源的根目录
	String testPackageRoot;
	String testHttpRoot;
	
	String realPackageRoot;
	String realHttpRoot;
	
	Version testVersions[];
	
	Version realVersions[];

	public Version[] getTestVersions() {
		return testVersions;
	}

	public void setTestVersions(Version[] testVersions) {
		this.testVersions = testVersions;
	}

	public Version[] getRealVersions() {
		return realVersions;
	}

	public void setRealVersions(Version[] realVersions) {
		this.realVersions = realVersions;
	}

	public String getRandomPackagePath() {
		return randomPackagePath;
	}

	public void setRandomPackagePath(String randomPackagePath) {
		this.randomPackagePath = randomPackagePath;
	}

	public String getTestPackageRoot() {
		return testPackageRoot;
	}

	public void setTestPackageRoot(String testPackageRoot) {
		this.testPackageRoot = testPackageRoot;
	}

	public String getTestHttpRoot() {
		return testHttpRoot;
	}

	public void setTestHttpRoot(String testHttpRoot) {
		this.testHttpRoot = testHttpRoot;
	}

	public String getRealPackageRoot() {
		return realPackageRoot;
	}

	public void setRealPackageRoot(String realPackageRoot) {
		this.realPackageRoot = realPackageRoot;
	}

	public String getRealHttpRoot() {
		return realHttpRoot;
	}

	public void setRealHttpRoot(String realHttpRoot) {
		this.realHttpRoot = realHttpRoot;
	}

	public void init() throws Exception{
		refreshTestPackage();
		refreshRealPackage(true);
		self = this;
	}
	
	
	public synchronized String generateIOS84PlistFile(Package p) throws Exception{
		if(p.platform.equalsIgnoreCase("IOS") == false){
			return p.httpDownloadURL;
		}
		HashMap<String,String> plistMap = new HashMap<String,String>();
		File plistmap = new File(testPackageRoot+"/plistmap.cfg");
		if(plistmap.isFile() && plistmap.exists()){
			BufferedReader reader = new BufferedReader(new FileReader(plistmap));
			String s = null;
			while( (s = reader.readLine()) != null ){
				String ss[] = s.split("=");
				if(ss != null && ss.length == 2){
					plistMap.put(ss[0].trim(), ss[1].trim());
				}
			}
			reader.close();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
		sb.append("<plist version=\"1.0\">\n");
		sb.append("<dict>\n");
		sb.append("<key>items</key>\n");

		sb.append("<array>\n");
			sb.append("<dict>\n");
		           
				sb.append("<key>assets</key>\n");
				sb.append("<array>\n");
			               
					sb.append("<dict>\n");
					sb.append("<key>kind</key>\n");
					sb.append("<string>software-package</string>\n");
					sb.append("<key>url</key>\n");
					sb.append("<string>"+p.httpDownloadURL+"</string>\n");
					sb.append("</dict>\n");
				               
					sb.append("<dict>\n");
					sb.append("<key>kind</key>\n");
					sb.append("<string>display-image</string>\n");
				             //      <!-- optional. indicates if icon needs shine effect applied. -->
					sb.append("<key>needs-shine</key>\n");
					sb.append("<true/>\n");
					sb.append("<key>url</key>\n");
					sb.append("<string>"+testHttpRoot+"/mieshi_small.png</string>\n");
					sb.append("</dict>\n");
				            //   <!-- full-size-image: the large 512x512 icon used by iTunes. -->
					sb.append("<dict>\n");
					sb.append("<key>kind</key>\n");
					sb.append("<string>full-size-image</string>\n");
				                  
					sb.append("<key>needs-shine</key>\n");
					sb.append("<true/>\n");
					sb.append("<key>url</key><string>"+testHttpRoot+"/mieshi_big.png</string>\n");
					sb.append(" </dict>\n");
				sb.append("</array>\n");
			
				sb.append("<key>metadata</key>\n");
				sb.append("<dict>\n");
			              // <!-- required -->
					sb.append("<key>bundle-identifier</key>\n");
					
					String bundleIdentifier = "com.sqage.mieshiwx";
					sb.append("<string>"+bundleIdentifier+"</string>\n");
				               //<!-- optional (software only) -->
					sb.append("<key>bundle-version</key>\n");
					sb.append("<string>1000</string>\n");
				               //<!-- required. the download kind. -->
					sb.append("<key>kind</key>\n");
					sb.append("<string>software</string>\n");
				               //<!-- optional. displayed during download; typically company name -->
					sb.append("<key>subtitle</key>\n");
					sb.append("<string>飘渺寻仙曲</string>\n");
				               //<!-- required. the title to display during the download. -->
					sb.append("<key>title</key>\n");
					sb.append("<string>飘渺寻仙曲</string>\n");
				sb.append("</dict>\n");
			
			sb.append("</dict>\n");
		sb.append("</array>\n");
		sb.append("</dict>\n");
		sb.append("</plist>\n");
		
		String path = p.packageFile.getAbsolutePath();
		path = path.replaceAll(".ipa", "84.plist");
		
		FileOutputStream output = new FileOutputStream(path);
		output.write(sb.toString().getBytes("UTF-8"));
		output.close();
		
		String plist = p.httpDownloadURL;
		plist = plist.replaceAll("ipa", "plist");
		
		
		return plist;
	}
	
	/**
	 * 
	 * @param p
	 * @return
	 */
	public synchronized String generateIOSPlistFile(Package p) throws Exception{
		if(p.platform.equalsIgnoreCase("IOS") == false){
			return p.httpDownloadURL;
		}
		HashMap<String,String> plistMap = new HashMap<String,String>();
		File plistmap = new File(testPackageRoot+"/plistmap.cfg");
		if(plistmap.isFile() && plistmap.exists()){
			BufferedReader reader = new BufferedReader(new FileReader(plistmap));
			String s = null;
			while( (s = reader.readLine()) != null ){
				String ss[] = s.split("=");
				if(ss != null && ss.length == 2){
					plistMap.put(ss[0].trim(), ss[1].trim());
				}
			}
			reader.close();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
		sb.append("<plist version=\"1.0\">\n");
		sb.append("<dict>\n");
		sb.append("<key>items</key>\n");

		sb.append("<array>\n");
			sb.append("<dict>\n");
		           
				sb.append("<key>assets</key>\n");
				sb.append("<array>\n");
			               
					sb.append("<dict>\n");
					sb.append("<key>kind</key>\n");
					sb.append("<string>software-package</string>\n");
					sb.append("<key>url</key>\n");
					sb.append("<string>"+p.httpDownloadURL+"</string>\n");
					sb.append("</dict>\n");
				               
					sb.append("<dict>\n");
					sb.append("<key>kind</key>\n");
					sb.append("<string>display-image</string>\n");
				             //      <!-- optional. indicates if icon needs shine effect applied. -->
					sb.append("<key>needs-shine</key>\n");
					sb.append("<true/>\n");
					sb.append("<key>url</key>\n");
					sb.append("<string>"+testHttpRoot+"/mieshi_small.png</string>\n");
					sb.append("</dict>\n");
				            //   <!-- full-size-image: the large 512x512 icon used by iTunes. -->
					sb.append("<dict>\n");
					sb.append("<key>kind</key>\n");
					sb.append("<string>full-size-image</string>\n");
				                  
					sb.append("<key>needs-shine</key>\n");
					sb.append("<true/>\n");
					sb.append("<key>url</key><string>"+testHttpRoot+"/mieshi_big.png</string>\n");
					sb.append(" </dict>\n");
				sb.append("</array>\n");
			
				sb.append("<key>metadata</key>\n");
				sb.append("<dict>\n");
			              // <!-- required -->
					sb.append("<key>bundle-identifier</key>\n");
					
					String bundleIdentifier = plistMap.get(p.channel);
					if(bundleIdentifier == null) bundleIdentifier = "com.sqage.mieshi";
					sb.append("<string>"+bundleIdentifier+"</string>\n");
				               //<!-- optional (software only) -->
					sb.append("<key>bundle-version</key>\n");
					sb.append("<string>1000</string>\n");
				               //<!-- required. the download kind. -->
					sb.append("<key>kind</key>\n");
					sb.append("<string>software</string>\n");
				               //<!-- optional. displayed during download; typically company name -->
					sb.append("<key>subtitle</key>\n");
					sb.append("<string>飘渺寻仙曲</string>\n");
				               //<!-- required. the title to display during the download. -->
					sb.append("<key>title</key>\n");
					sb.append("<string>飘渺寻仙曲</string>\n");
				sb.append("</dict>\n");
			
			sb.append("</dict>\n");
		sb.append("</array>\n");
		sb.append("</dict>\n");
		sb.append("</plist>\n");
		
		String path = p.packageFile.getAbsolutePath();
		path = path.replaceAll("ipa", "plist");
		
		FileOutputStream output = new FileOutputStream(path);
		output.write(sb.toString().getBytes("UTF-8"));
		output.close();
		
		String plist = p.httpDownloadURL;
		plist = plist.replaceAll("ipa", "plist");
		
		
		return plist;
	}
	/**
	 * 刷新测试用的程序包
	 * @throws Exception
	 */
	public void refreshTestPackage() throws Exception{
		
		File dir = new File(testPackageRoot);
		
		ArrayList<Version> al = new ArrayList<Version>();
		
		File versionDirs[] = dir.listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory()) return true;
				return false;
			}});
		
		for(int i = 0 ; versionDirs!=null && i < versionDirs.length ; i++){
			Version version = new Version(versionDirs[i].getName());
			
			File channelDirs[] = versionDirs[i].listFiles(new FileFilter(){
				@Override
				public boolean accept(File pathname) {
					if(pathname.isDirectory()) return true;
					return false;
				}});
			
			for(int j = 0 ; channelDirs != null && j < channelDirs.length ; j++){
				
				File platformDir[] = channelDirs[j].listFiles(new FileFilter(){
					@Override
					public boolean accept(File pathname) {
						if(pathname.isDirectory()) return true;
						return false;
					}});
				
				for(int k = 0 ; platformDir!=null && k < platformDir.length ; k++){
					
					File packageFiles[] = platformDir[k].listFiles(new FileFilter(){
						@Override
						public boolean accept(File pathname) {
							if(pathname.isFile() && (pathname.getName().endsWith(".ipa") ||  pathname.getName().endsWith(".apk")) ) return true;
							return false;
						}});
					
					for(int l = 0 ; packageFiles != null && l < packageFiles.length ; l++){
						Package p = new Package(version.versionString,channelDirs[j].getName(),platformDir[k].getName(),packageFiles[l],
								testHttpRoot+"/" + version.versionString+"/"+channelDirs[j].getName()+"/"+platformDir[k].getName()+"/"+packageFiles[l].getName());
						
						if(p.platform.equalsIgnoreCase("IOS")){
							generateIOSPlistFile(p);
							generateIOS84PlistFile(p);
						}
						
						version.packageList.add(p);
					}
				}
			}
			
			al.add(version);
		}
		
		Version versions[] = al.toArray(new Version[0]);
		
		Arrays.sort(versions);
		
		this.testVersions = versions;
		
	}
	
	/**
	 * 根据时间生成路径名，然后这个路径用于正式程序包
	 */
	protected String randomPackagePath;
	/**
	 * 刷新正式用的程序包
	 * 首先复制测试程序包到正式路径下，然后刷新正式程序包
	 * @throws Exception
	 */
	public void refreshRealPackage(boolean init) throws Exception{
		String realPackageRootTemp = "";
		String realHttpRootTemp = "";
		if(init){
			File dir = new File(realPackageRoot);
			File fileList[] = dir.listFiles(new FileFilter(){
				@Override
				public boolean accept(File pathname) {
					if(pathname.isDirectory()) return true;
					return false;
				}});
			if(fileList != null){
				long num = 0;
				for(File file : fileList){
					if(file != null){
						try{
							long numTemp = Long.parseLong(file.getName());
							if(numTemp > num){
								num = numTemp;
							}
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
				randomPackagePath = num+"";
			}
			realPackageRootTemp = realPackageRoot+"/"+randomPackagePath+"/";
			realHttpRootTemp = realHttpRoot + "/"+randomPackagePath+"/";
		}else{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			randomPackagePath = simpleDateFormat.format(date);
			realPackageRootTemp = realPackageRoot+"/"+randomPackagePath+"/";
			realHttpRootTemp = realHttpRoot + "/"+randomPackagePath+"/";
			FileUtils.copy(new File(testPackageRoot), new File(realPackageRootTemp), true);
		}
		//刷新正式程序包
		File dir = new File(realPackageRootTemp);
		
		ArrayList<Version> al = new ArrayList<Version>();
		
		File versionDirs[] = dir.listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory()) return true;
				return false;
			}});
		
		for(int i = 0 ; versionDirs!=null && i < versionDirs.length ; i++){
			Version version = new Version(versionDirs[i].getName());
			
			File channelDirs[] = versionDirs[i].listFiles(new FileFilter(){
				@Override
				public boolean accept(File pathname) {
					if(pathname.isDirectory()) return true;
					return false;
				}});
			
			for(int j = 0 ; channelDirs != null && j < channelDirs.length ; j++){
				
				File platformDir[] = channelDirs[j].listFiles(new FileFilter(){
					@Override
					public boolean accept(File pathname) {
						if(pathname.isDirectory()) return true;
						return false;
					}});
				
				for(int k = 0 ; platformDir!=null && k < platformDir.length ; k++){
					
					File packageFiles[] = platformDir[k].listFiles(new FileFilter(){
						@Override
						public boolean accept(File pathname) {
							if(pathname.isFile() && (pathname.getName().endsWith(".ipa") ||  pathname.getName().endsWith(".apk")) ) return true;
							return false;
						}});
					
					for(int l = 0 ; packageFiles != null && l < packageFiles.length ; l++){
						Package p = new Package(version.versionString,channelDirs[j].getName(),platformDir[k].getName(),packageFiles[l],
								realHttpRootTemp+"/" + version.versionString+"/"+channelDirs[j].getName()+"/"+platformDir[k].getName()+"/"+packageFiles[l].getName());
						
						if(p.platform.equalsIgnoreCase("IOS")){
							generateIOSPlistFile(p);
							generateIOS84PlistFile(p);
						}
						
						version.packageList.add(p);
					}
				}
			}
			
			al.add(version);
		}
		
		Version versions[] = al.toArray(new Version[0]);
		
		Arrays.sort(versions);
		
		this.realVersions = versions;
		
	}
	
	/**
	 * 客户端检查是否有新的版本
	 * 有新的版本返回true，没有新的版本，返回false
	 * @param clientId
	 * @param clientVersion
	 * @param clientChannel
	 * @param clientPlatform
	 * @param gpuFlag
	 * @return
	 */
	public boolean hasNewVersion(String clientId,String clientVersion,String clientChannel,String clientPlatform,String gpuFlag){
		InnerTesterManager itm = InnerTesterManager.getInstance();
		boolean isTester = false;
		if(itm != null){
			isTester = itm.isInnerTester(clientId);
		}
		try{
			Version v = new Version(clientVersion);
		}catch(Exception e){
			logger.warn("[客户端检查最新版本] [失败] [版本串非法] [--] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"] [是否内部测试人员:"+isTester+"]",e);
		}
		Version[] versions = null;
		if(isTester){
			versions = testVersions;
		}else{
			versions = realVersions;
		}
		if(versions == null || versions.length == 0) return false;
		
		Version v = new Version(clientVersion);
		
		//////////////
		
		Version c = new Version("0.0.1");
		
		int rr = c.compareTo(v);
		if(rr >= 0 && "android".equals(clientPlatform.toLowerCase())){
			if(需要用户手动更新客户端的列表.get(clientId) == null){
				需要用户手动更新客户端的列表.put(clientId, true);
				if(logger.isInfoEnabled())
					logger.info("[客户端检查最新版本] [新加入一个用户到需要用户手动更新客户端的列表] [clientId:"+clientId+"] [列表用户个数:"+(需要用户手动更新客户端的列表.keySet()!= null?需要用户手动更新客户端的列表.keySet().size():0)+"]");
			}
			if(logger.isInfoEnabled())
				logger.info("[客户端检查最新版本] [成功] ["+(rr>0?"有新版本":"无新版本")+"] ["+c.versionString+"] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"] [小于等于1.3.4的人员:"+isTester+"]");
			return false;
		}else{
			if(需要用户手动更新客户端的列表.get(clientId) != null && 需要用户手动更新客户端的列表.get(clientId) == true){
				需要用户手动更新客户端的列表.remove(clientId);
				if(logger.isInfoEnabled())
					logger.info("[客户端检查最新版本] [从需要用户手动更新客户端的列表中去除一个用户] [clientId:"+clientId+"] [列表用户个数:"+(需要用户手动更新客户端的列表.keySet()!= null?需要用户手动更新客户端的列表.keySet().size():0)+"]");
			}
		}
		
		//////////////
		
		Version newV = versions[versions.length-1];
		
		int r = newV.compareTo(v);
		if(logger.isInfoEnabled())
			logger.info("[客户端检查最新版本] [成功] ["+(r>0?"有新版本":"无新版本")+"] ["+newV.versionString+"] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"] [是否内部测试人员:"+isTester+"]");
		
		if(r > 0){
			return true;
		}
		return false;
		
	}
	
	
	/**
	 * 客户端检查是否有新的版本
	 * 有新的版本返回true，没有新的版本，返回false
	 * @param clientId
	 * @param clientVersion
	 * @param clientChannel
	 * @param clientPlatform
	 * @param gpuFlag
	 * @return
	 */
	public Package getNewPackage(String clientId,String clientVersion,String clientChannel,String clientPlatform,String gpuFlag){
		boolean b = hasNewVersion(clientId,clientVersion,clientChannel,clientPlatform,gpuFlag);
		if(b == false) return null;
		Version[] versions = null;
		InnerTesterManager itm = InnerTesterManager.getInstance();
		boolean isTester = false;
		if(itm != null){
			isTester = itm.isInnerTester(clientId);
		}
		if(isTester){
			versions = testVersions;
		}else{
			versions = realVersions;
		}
		if(versions == null || versions.length == 0) return null;
		
		Version v = new Version(clientVersion);
		
		for(int k = versions.length-1 ; k >= 0 ; k--){
			Version newV = versions[k];
			if(newV.compareTo(v) > 0){
				for(int i = 0 ; i < newV.packageList.size() ; i++){
					Package p = newV.packageList.get(i);
					
					if(p.channel.equalsIgnoreCase(clientChannel) && p.platform.equalsIgnoreCase(clientPlatform) && p.gpuFlag.equalsIgnoreCase(gpuFlag)
						&& p.fullFlag != null && p.fullFlag.equals("half")){
						if(logger.isInfoEnabled())
							logger.info("[客户端获取最新的包] [成功] [OK] ["+newV.versionString+"] ["+p.httpDownloadURL+"] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"] [是否内部测试人员:"+isTester+"]");
					
						return p;
					}
				}
	
				for(int i = 0 ; i < newV.packageList.size() ; i++){
					Package p = newV.packageList.get(i);
					
					if(p.channel.equalsIgnoreCase(clientChannel) && p.platform.equalsIgnoreCase(clientPlatform) && p.gpuFlag.equalsIgnoreCase(gpuFlag)
						){
						if(logger.isInfoEnabled())
							logger.info("[客户端获取最新的包] [成功] [OK] ["+newV.versionString+"] ["+p.httpDownloadURL+"] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"] [是否内部测试人员:"+isTester+"]");
					
						return p;
					}
				}
			}
		}
		
		logger.warn("[客户端获取最新的包] [失败] [有新版本但是找不到匹配的包] ["+versions[versions.length-1].versionString+"] [--] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"] [是否内部测试人员:"+isTester+"]");
		return null;
	}
}
