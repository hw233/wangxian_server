package com.fy.gamegateway.mieshi.server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.XmlUtil;



/**
 * 需要去渠道页面下载的版本的管理器
 * @liuxun
 *
 */
public class NeedToHttpVersionManager {
	
	static Logger logger = Logger.getLogger(MieshiGatewayServer.class);
	
	static NeedToHttpVersionManager m_self;
	
	public static String[] needChannel = new String[]{"360SDK_MIESHI", "360SDK01_MIESHI", "KOREASDKKT_MIESHI", "KOREASDKNAVER_MIESHI", "KOREASDKTSTORE_MIESHI", "KOREAKT_MIESHI", "KOREATSTORE_MIESHI", "WIN8STORE_MIESHI", "WIN8SONY_MIESHI"};
	public static boolean isNeedChannel (String channel) {
		for (String c : needChannel) {
			if (channel.equalsIgnoreCase(c)) {
				return true;
			}
		}
		return false;
	}
	
	public static NeedToHttpVersionManager getInstance(){
		return m_self;
	}
	
	public static class Version implements java.lang.Comparable<Version>{
		public ArrayList<Integer> versionNumList = new ArrayList<Integer>();
		public String versionString;
		public String versionChannel;
		public String versionUrl;
		public String versionGpu;
		
		public Version(String version, String vChannel, String vUrl, String versionGpu){
			
			versionString = version;
			this.versionChannel = vChannel;
			this.versionUrl = vUrl;
			this.versionGpu = versionGpu;
			
			if(version == null) return;
//			int k = version.indexOf("_");
//			if(k >= 0){
//				String s = version.substring(k+1);
//				//这个force是加在版本文件夹上
//				if(s.contains("force")){
//					forceUpdate = true;
//				}else{
//					forceUpdate =false;
//				}
//				
//				version = version.substring(0,k);
//			}
			
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
			if (this.versionGpu != null && o.versionGpu != null && !this.versionGpu.equals(o.versionGpu)) {
				return 0;
			}
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
			return "[" + this.versionString + "] [" + this.versionChannel + "] [" + this.versionUrl + "]";
		}
		
	}
	
	private String configFile;
	
	private ConcurrentHashMap<String, Version> versions;
	
	private ConcurrentHashMap<String, Version> testVersions;
	
	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public ConcurrentHashMap<String, Version> getVersions() {
		return versions;
	}

	public void setVersions(ConcurrentHashMap<String, Version> versions) {
		this.versions = versions;
	}

	public void setTestVersions(ConcurrentHashMap<String, Version> testVersions) {
		this.testVersions = testVersions;
	}

	public ConcurrentHashMap<String, Version> getTestVersions() {
		return testVersions;
	}
	
	public void init() throws Exception{
		versions = new ConcurrentHashMap<String, Version>();
		testVersions = new ConcurrentHashMap<String, Version>();
		File f = new File(this.configFile);
		if(f.exists()){
			load(this.configFile);
		}
		
		m_self = this;
	}
	
	public void load(String file) throws Exception{
		
		Document dom = XmlUtil.load(file);
		
		Element root = dom.getDocumentElement();
		
		{
			ConcurrentHashMap<String , Version> vs = new ConcurrentHashMap<String, Version>();
			Element eles[] = XmlUtil.getChildrenByName(root, "version");
			for(int i = 0 ; i < eles.length ; i++){
				Element e = eles[i];
				String name = XmlUtil.getAttributeAsString(e, "name", null);
				String channel = XmlUtil.getAttributeAsString(e, "channel", null);
				String url = XmlUtil.getAttributeAsString(e, "vurl", null);
				String gpu = XmlUtil.getAttributeAsString(e, "gpu", null);
				Version v = new Version(name, channel, url, gpu);
				vs.put(channel+gpu, v);
			}
			this.versions = vs;
		}
		{
			ConcurrentHashMap<String , Version> vs = new ConcurrentHashMap<String, Version>();
			Element testEles[] = XmlUtil.getChildrenByName(root, "testVersion");
			for(int i = 0 ; i < testEles.length ; i++){
				Element e = testEles[i];
				String name = XmlUtil.getAttributeAsString(e, "name", null);
				String channel = XmlUtil.getAttributeAsString(e, "channel", null);
				String url = XmlUtil.getAttributeAsString(e, "vurl", null);
				String gpu = XmlUtil.getAttributeAsString(e, "gpu", null);
				Version v = new Version(name, channel, url, gpu);
				vs.put(channel+gpu, v);
			}
			this.testVersions = vs;
		}
		
	}
	
	public void saveTo(String file) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='utf-8' ?>\n");
		sb.append("<needToHttp-versions>\n");
		
		for(String key : versions.keySet()){
			Version v = versions.get(key);
			sb.append("<version name='"+v.versionString+"' gpu='"+v.versionGpu+"' channel='"+v.versionChannel+"' vurl='"+v.versionUrl+"' />");
		}
		
		for(String key : testVersions.keySet()){
			Version v = testVersions.get(key);
			sb.append("<testVersion name='"+v.versionString+"' gpu='"+v.versionGpu+"' channel='"+v.versionChannel+"' vurl='"+v.versionUrl+"' />");
		}
		
		sb.append("</needToHttp-versions>");
		
		File f = new File(file);
		FileOutputStream output = new FileOutputStream(f);
		output.write(sb.toString().getBytes());
		output.close();
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
		String gpu = gpuFlag;
//		if (gpuFlag.equals("etc_general")) {
//			gpu = "etc_general";
//		}else {
//			gpu = "auto";
//		}
		if (isTester) {
			if(testVersions == null || testVersions.size() == 0) {
			}else {
				Version v = new Version(clientVersion, clientChannel, "", gpu);
				Version newV = testVersions.get(clientChannel+gpu); 	
				if (newV != null) {
					int r = newV.compareTo(v);
					
					if(logger.isInfoEnabled())
						logger.info("[needToHttp客户端检查最新版本] [成功] ["+(r>0?"有新版本":"无新版本")+"] ["+newV.versionString+"] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"]");
					
					if(r > 0){
						return true;
					}
				}
			}
		}
		if(versions == null || versions.size() == 0) return false;
		
		Version v = new Version(clientVersion, clientChannel, "", gpu);
		Version newV = versions.get(clientChannel+gpu);
		if (newV != null) {
			int r = newV.compareTo(v);
			
			if(logger.isInfoEnabled())
				logger.info("[needToHttp客户端检查最新版本] [成功] ["+(r>0?"有新版本":"无新版本")+"] ["+newV.versionString+"] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"]");
			
			if(r > 0){
				return true;
			}
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
	public Version getNewPackageVersion(String clientId,String clientVersion,String clientChannel,String clientPlatform,String gpuFlag){
		boolean b = hasNewVersion(clientId,clientVersion,clientChannel,clientPlatform,gpuFlag);
		if(b == false) return null;
	
		String gpu = gpuFlag;
//		if (gpuFlag.equals("etc_general")) {
//			gpu = "etc_general";
//		}else {
//			gpu = "auto";
//		}
		
		InnerTesterManager itm = InnerTesterManager.getInstance();
		boolean isTester = false;
		if(itm != null){
			isTester = itm.isInnerTester(clientId);
		}
		
		if (isTester) {
			if(testVersions == null || testVersions.size() == 0) {
			}else {
				Version newV = testVersions.get(clientChannel+gpu);
				if (newV != null) {
					return newV;
				}
			}
		}
		
		if(versions == null || versions.size() == 0) return null;
		
		Version newV = versions.get(clientChannel+gpu);
		if (newV != null) {
			return newV;
		}
		
		logger.warn("[needToHttp客户端获取最新的包] [失败] [有新版本但是找不到匹配的包] [] [--] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"]");
		return null;
	}

}
