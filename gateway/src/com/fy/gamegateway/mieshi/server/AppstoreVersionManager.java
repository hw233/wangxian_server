package com.fy.gamegateway.mieshi.server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.XmlUtil;



/**
 * 苹果版本的管理器
 *
 */
public class AppstoreVersionManager {
	
	static Logger logger = Logger.getLogger(MieshiGatewayServer.class);
	
	static AppstoreVersionManager m_self;
	
	public static AppstoreVersionManager getInstance(){
		return m_self;
	}
	
	public static class Version implements java.lang.Comparable<Version>{
		public ArrayList<Integer> versionNumList = new ArrayList<Integer>();
		public String versionString;
		public String downUrl;
		public String appChannel;
		
		public Version(String version, String downUrl, String channel){
			
			versionString = version;
			this.downUrl = downUrl;
			appChannel = channel;
			
			if(version == null) return;
			
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
			
			if (!o.appChannel.equals(this.appChannel)) {
				return -1;
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
			return this.versionString;
		}
		
	}
	
	String configFile;
	
	Version versions[];
	
	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public Version[] getVersions() {
		return versions;
	}

	public void setVersions(Version[] versions) {
		this.versions = versions;
	}

	public void init() throws Exception{
	
		File f = new File(this.configFile);
		if(f.exists()){
			load(this.configFile);
		}
		
		m_self = this;
	}
	
	public void addVersion(String version, String downUrl, String channel){
		if(!downUrl.contains("&amp;") && downUrl.contains("&"))
		{
			downUrl = downUrl.replaceAll("&", "&amp;");
		}
		
		ArrayList<Version> al = new ArrayList<Version>();
		for(int i = 0 ; versions != null && i < versions.length ; i++){
			Version v = versions[i];
			al.add(v);
		}
		Version v = new Version(version, downUrl, channel);
		al.add(v);
		Version vs[] = al.toArray(new Version[0]);
		Arrays.sort(vs);
		
		this.versions = vs;
	}
	
	public void removeVersion(int index){
		ArrayList<Version> al = new ArrayList<Version>();
		for(int i = 0 ; versions != null && i < versions.length ; i++){
			Version v = versions[i];
			al.add(v);
		}
		al.remove(index);
		Version vs[] = al.toArray(new Version[0]);
		Arrays.sort(vs);
		
		this.versions = vs;
	}
	
	
	
	public void load(String file) throws Exception{
		
		Document dom = XmlUtil.load(file);
		
		Element root = dom.getDocumentElement();
		
		ArrayList<Version> al = new ArrayList<Version>();
		
		Element eles[] = XmlUtil.getChildrenByName(root, "version");
		for(int i = 0 ; i < eles.length ; i++){
			Element e = eles[i];
			String name = XmlUtil.getAttributeAsString(e, "name", null);
			String downUrl = XmlUtil.getAttributeAsString(e, "downUrl", null, null);
			String channel  = XmlUtil.getAttributeAsString(e, "channel", null, null);
			Version v = new Version(name, downUrl, channel);
			al.add(v);
		}
		Version vs[] = al.toArray(new Version[0]);
		Arrays.sort(vs);
		
		this.versions = vs;
	}
	
	public void saveTo(String file) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='utf-8' ?>\n");
		sb.append("<appstore-versions>\n");
		
		for(int i = 0 ; versions != null && i < versions.length ; i++){
			Version v = versions[i];
			sb.append("<version name='"+v.versionString+"' downUrl='"+v.downUrl+"' channel='"+v.appChannel+"'/>\n");
		}
		
		sb.append("</appstore-versions>");
		
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
		if(versions == null || versions.length == 0) return false;
		
		Version v = new Version(clientVersion, "", clientChannel);
		Version newV = null;
		for (Version vv : versions) {
			if (vv.appChannel.equals(clientChannel)) {
				newV = vv;
				break;
			}
		}
		if (newV == null) {
			return false;
		}
		int r = newV.compareTo(v);
		
		if(logger.isInfoEnabled())
			logger.info("[客户端检查最新版本] [成功] ["+(r>0?"有新版本":"无新版本")+"] ["+newV.versionString+"] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"]");
		
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
	public Version getNewPackageVersion(String clientId,String clientVersion,String clientChannel,String clientPlatform,String gpuFlag){
		boolean b = hasNewVersion(clientId,clientVersion,clientChannel,clientPlatform,gpuFlag);
		if(b == false) return null;
	
		if(versions == null || versions.length == 0) return null;
		
		Version v = new Version(clientVersion, "", clientChannel);
		
		for(int k = versions.length-1 ; k >= 0 ; k--){
			Version newV = versions[k];
			if(newV.compareTo(v) > 0){
				if(newV.downUrl != null && newV.downUrl.length() > 0)
					return newV;
			}
		}
		
		logger.warn("[客户端获取最新的包] [失败] [有新版本但是找不到匹配的包] ["+versions[versions.length-1].versionString+"] [--] ["+clientId+"] ["+clientVersion+"] ["+clientVersion+"] ["+clientPlatform+"] ["+gpuFlag+"]");
		return null;
	}
}
