package com.xuanzhi.tools.resource;

import java.io.File;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 读取配置文件，并解析成内存中使用的数据
 * 
 * 配置文件的格式：
 *  * <?xml version="1.0" encoding="UTF-8" ?>
	 * <resoucepackages create-time="2014-05-16 13:35:33">
	 * 
	 * 		<resoucepackage version="1.0" full-type="full|half" package-type="a|b" file-num="6399" file-size="755666777">
	 * 			<url>http://wh1.sqage.com/download/res/3dsword20140516181818_AFR_1.0.7z</url>
	 * 			<url>http://wh2.sqage.com/download/res/3dsword20140516181818_AFR_1.0.7z</url>
	 * 		</resoucepackage>
	 * 
	 * </resoucepackages>
	 * 
 * 此配置文件由工具产生。
 * 
 * 
 *
 */
public class DefaultResourceManager {
	
	Comparator<ResourcePackage> comparator = new Comparator<ResourcePackage>(){
		public int compare(ResourcePackage o1, ResourcePackage o2) {
			if(o1.masterVersion < o2.masterVersion) return -1;
			if(o1.masterVersion > o2.masterVersion) return 1;
			if(o1.slaveVersion < o2.slaveVersion) return -1;
			if(o1.slaveVersion > o2.slaveVersion) return 1;
			if(o1.fullPackage == false && o2.fullPackage == true) return -1;
			if(o1.fullPackage == true && o2.fullPackage == false) return 1;
			return 0;
		}};
		
	
	public ArrayList<ResourcePackage> arpList = new ArrayList<ResourcePackage>();
	
	public ArrayList<ResourcePackage> brpList = new ArrayList<ResourcePackage>();
	
	String configFile;
	
	String diskCacheFile;
	
	DefaultDiskCache diskCache;
	
	public String getConfigFile() {
		return configFile;
	}

	public void setDiskCacheFile(String configFile) {
		this.diskCacheFile = configFile;
	}
	
	public String getDiskCacheFile() {
		return diskCacheFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public void init() throws Exception{
		
		diskCache = new DefaultDiskCache(new File(diskCacheFile),"DefaultResourceManager",10L*356*24*3600*1000L);
		
		load();
	}
	
	public void load() throws Exception{
		
		arpList.clear();
		brpList.clear();
		
		Document dom = XmlUtil.load(configFile);
		
		Element rootEle = dom.getDocumentElement();
	
		
		Element eles[] = XmlUtil.getChildrenByName(rootEle, "resoucepackage");
		for(int i = 0 ; i < eles.length ; i++){
			Element e = eles[i];
			String version = XmlUtil.getAttributeAsString(e, "version", (Map<String,String>)null);
			String fullType = XmlUtil.getAttributeAsString(e, "full-type", (Map<String,String>)null);
			String pType = XmlUtil.getAttributeAsString(e, "package-type", (Map<String,String>)null);
			int fileNum = XmlUtil.getAttributeAsInteger(e, "file-num", 0);
			long fileSize = XmlUtil.getAttributeAsLong(e, "file-size", 0L);
			
			String ss[] = version.split("\\.");
			int mv = Integer.parseInt(ss[0]);
			int sv = Integer.parseInt(ss[1]);
			ResourcePackage rp = new ResourcePackage();
			rp.masterVersion = mv;
			rp.slaveVersion = sv;
			rp.fullPackage = fullType.equalsIgnoreCase("full");
			rp.aPackageFlag = pType.equalsIgnoreCase("a");
			rp.fileNum = fileNum;
			rp.fileSize = fileSize;
			
			
			Element urlEles[] = XmlUtil.getChildrenByName(e, "url");
			ArrayList<String> urls = new ArrayList<String>();
			for(int j = 0 ; j < urlEles.length ; j++){
				String url = XmlUtil.getText(urlEles[j], null);
				if(url.trim().length() > 0){
					urls.add(url.trim());
				}
			}
			rp.downloadUrls = urls.toArray(new String[0]);
			
			if(rp.aPackageFlag){
				arpList.add(rp);
			}else{
				brpList.add(rp);
			}
		}
		
		Collections.sort(arpList, comparator);
		Collections.sort(brpList, comparator);
	}
	
	/**
	 * 判断资源是否对某个tag生效
	 * @param rp
	 * @param tag
	 * @return
	 */
	public boolean isValid(ResourcePackage rp,String tag){
		if(rp == null) return false;
		String s = (String)this.diskCache.get(rp.getIdentity());
		if(s == null) return false;
		if(s.equals("*")) return true;
		String ss[] = s.split(",");
		for(int i = 0 ; i < ss.length ; i++){
			if(ss[i].length() > 0 && ss[i].equals(tag)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 当tag为"*"时，表示对全部tag生效。
	 * 否则tag应该为多个字符串的组合，中间以逗号分隔。比如
	 * "whilelist,uc,dan,91,360"
	 * @param rp
	 * @param tag
	 */
	public void setResourcePackageValidFlag(ResourcePackage rp,String tag){
		this.diskCache.put(rp.getIdentity(),tag);
	}

	public String getResourcePackageValidFlag(ResourcePackage rp){
		return (String)this.diskCache.get(rp.getIdentity());
	}
	
	public ResourcePackage getResourcePackageByIdentity(String identity){
		for(int i = 0 ; i < arpList.size() ; i++){
			if(arpList.get(i).getIdentity().equals(identity))
				return arpList.get(i);
		}
		for(int i = 0 ; i < brpList.size() ; i++){
			if(brpList.get(i).getIdentity().equals(identity))
				return brpList.get(i);
		}
		return null;
	}

	/**
	 * 判断是否为最新的资源包
	 * 
	 * @param aPackage
	 * @param masterVersion
	 * @param slaveVersion
	 * @return
	 */
	public boolean isNewestResoucePackage(String tag,boolean aPackage,int masterVersion,int slaveVersion){
		ArrayList<ResourcePackage> al = null;
		if(aPackage){
			al = arpList;
		}else{
			al = brpList;
		}
		if(al.size() == 0) return true;
		ResourcePackage rp = null;
		int i = al.size()-1;
		while(i >= 0){
			rp = al.get(i);
			if(isValid(rp,tag)){
				break;
			}
			i--;
		}
		if(rp == null || !isValid(rp,tag)) return true;
		
		if(rp.masterVersion < masterVersion ) return true;
		if(rp.masterVersion > masterVersion ) return false;
		if(rp.slaveVersion < slaveVersion ) return true;
		if(rp.slaveVersion > slaveVersion ) return false;
		return true;
	}
	
	
	/**
	 * 得到需要下载的包，如果没有需要下载的包，返回长度为0的数组。
	 * 
	 * @param aPackage
	 * @param masterVersion
	 * @param slaveVersion
	 * @return
	 */
	public String[] getNeedUpdateResoucePackage(String tag,boolean aPackage,int masterVersion,int slaveVersion,List<Long> retPackageSize){
		if(isNewestResoucePackage(tag,aPackage,masterVersion,slaveVersion)) return new String[0];
		
		ArrayList<ResourcePackage> al = null;
		if(aPackage){
			al = arpList;
		}else{
			al = brpList;
		}
		if(al.size() == 0) return new String[0];
		ResourcePackage rp2 = null;
		int i = al.size()-1;
		while(i >= 0){
			rp2 = al.get(i);
			if(isValid(rp2,tag)){
				break;
			}
			i--;
		}
		if(rp2 == null || !isValid(rp2,tag))  return new String[0];

		int index = al.indexOf(rp2);
		
		ResourcePackage newRp = new ResourcePackage();
		newRp.masterVersion = masterVersion;
		newRp.slaveVersion = slaveVersion;
		newRp.fullPackage = true;
		
		//玩家的版本太旧，更新最近的一个全包已经之后的所有半包
		if(this.comparator.compare(al.get(0), newRp) > 0){
			int k = 0;
			for(i = index ; i >= 0 ; i--){
				ResourcePackage p = al.get(i);
				if(p.fullPackage){
					k = i;
					break;
				}
			}
			ArrayList<String> ret = new ArrayList<String>();
			for(i = k ; i <= index ; i++){
				ResourcePackage p = al.get(i);
				String ss[] = p.getDownloadUrls();
				if(ss.length > 0){
					ret.add(ss[(int)(Math.random() * ss.length)]);
					if(retPackageSize != null){
						retPackageSize.add(p.getFileSize());
					}
				}
			}
			return ret.toArray(new String[0]);
		}
		
		//只更新半包
		ArrayList<ResourcePackage> al2 = new ArrayList<ResourcePackage>();
		for(i = 0 ; i <= index ; i++){
			if(al.get(i).fullPackage == false || i == 0){
				al2.add(al.get(i));
			}
		}
		int k = 0;
		for(i = 0 ; i < al2.size() ; i++){
			if( comparator.compare(al2.get(i), newRp) > 0){
				k = i;
				break;
			}
		}
		ArrayList<String> ret = new ArrayList<String>();
		for(i = k ; i < al2.size() ; i++){
			ResourcePackage p = al2.get(i);
			String ss[] = p.getDownloadUrls();
			if(ss.length > 0){
				ret.add(ss[(int)(Math.random() * ss.length)]);
				if(retPackageSize != null){
					retPackageSize.add(p.getFileSize());
				}
			}
		}
		return ret.toArray(new String[0]);
		
		//
	}
}
