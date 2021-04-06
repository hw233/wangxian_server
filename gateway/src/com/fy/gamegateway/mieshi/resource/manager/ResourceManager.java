package com.fy.gamegateway.mieshi.resource.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.fy.gamegateway.message.GameMessageFactory;
import com.fy.gamegateway.message.MIESHI_RESOURCE_FILE_REQ;
import com.fy.gamegateway.mieshi.server.MieshiGatewayServer;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.FileUtils;

public class ResourceManager {

	static Logger logger = Logger.getLogger(MieshiGatewayServer.class);
	
	private static ResourceManager self;
	
	private ResourceManager(){
		
	}
	MieshiGatewayServer mgs;

	Hashtable<String,byte[]> zipMap = new Hashtable<String,byte[]>();

	public Hashtable<String,byte[]> getZipMap(){
		return zipMap;
	}
	
	public MieshiGatewayServer getMgs() {
		return mgs;
	}

	public void setMgs(MieshiGatewayServer mgs) {
		this.mgs = mgs;
	}

	public static ResourceManager getInstance(){
		return self;
	}
	
	public void init() throws Exception{
		findRealResourceRandomPath();
		load(true,new File(this.testResourceRootPath+"/"+this.resourceFileForServer));
		load(false,new File(this.realResourceRootPath+"/"+this.randomResourcePath + "/"+this.resourceFileForServer));
		self = this;
	}

	private void findRealResourceRandomPath(){

			File dir = new File(getRealResourceRootPath());
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
				if(num > 0){
					this.randomResourcePath = num + "";
				}
			}
			
			if(this.randomResourcePath == null){
				randomResourcePath = DateUtil.formatDate(new Date(),"yyyyMMddHHmmss");
			}
		
	}
	
	private void load(boolean test,File f) throws Exception{
		SAXReader reader = new SAXReader();
		Document document = null;
		document = reader.read(f);
		
		Element resources = document.getRootElement();
		if(test){
			this.testResourceVersion = Integer.parseInt(resources.attributeValue("version"));
		}else{
			this.realResourceVersion = Integer.parseInt(resources.attributeValue("version"));
		}
		LinkedHashMap<String, ResourceData> oldTab = new LinkedHashMap<String, ResourceData>();
		for (Iterator<Element> i = resources.elementIterator(); i.hasNext();) {
			Element element = i.next();
			ResourceData data = new ResourceData();
			data.setPath(element.attributeValue("filepath"));

			
			data.setVersion(Integer.parseInt(element.attributeValue("version")));
			data.setFileType(Integer.parseInt(element.attributeValue("filetype")));
			String fileSize = element.attributeValue("filesize");
			if(fileSize == null){
				data.fileSize = 0;
			}else{
				data.fileSize = Integer.parseInt(fileSize);
			}
			data.setTypeMD5(new Hashtable<String, String>());
			
			for (Iterator<Attribute> att = element.attributeIterator(); att.hasNext();) {
				Attribute a = att.next();
				if (a.getName().endsWith("MD5")) {
					data.addTypeMD5(a.getName().substring(0, a.getName().indexOf("MD5")), a.getValue());
				}
			}
			
			if(data.getFileType() == ResourceMD5.FILETYPE_OTHER){
				oldTab.put(data.getPath(), data);
			}else if(data.getFileType() == ResourceMD5.FILETYPE_SOUND){
				String path = data.getPath();
				if(path.indexOf(".") == -1){
					for(int ii = 0 ; ii < ResourceMD5.sound_postfix.length ; ii++){
						oldTab.put(data.getPath()+ ResourceMD5.sound_postfix[ii], data);
					}
				}else{
					oldTab.put(data.getPath(), data);
				}
				
			}else if(data.getFileType() == ResourceMD5.FILETYPE_PICTURE){
				String path = data.getPath();
				if(path.indexOf(".") == -1){
					for(int ii = 0 ; ii < ResourceMD5.picture_postfix.length ; ii++){
						oldTab.put(data.getPath()+ ResourceMD5.picture_postfix[ii], data);
//						if(ResourceMD5.picture_postfix[ii].equals(ResourceMD5.ETC1_SUFFIX)){
//							oldTab.put(data.getPath()+ ResourceMD5.ETC1_ALPHA_SUFFIX, data);
//						}
					}
				}else{
					oldTab.put(data.getPath(), data);
				}
			}
			
		}
		
		if(test){
			this.allTestResourceData = oldTab;
		}else{
			this.allRealResourceData = oldTab;
		}
		
		

	}
	/**
	 * 服务器存放测试资源的路径
	 */
	private String testResourceRootPath;
	
	/**
	 * 服务器存放真实资源的路径
	 */
	private String realResourceRootPath;

	/**
	 * 服务器存放测试资源的路径
	 */
	private String testResourceRootHttpPath;
	
	/**
	 * 服务器存放真实资源的路径
	 */
	private String realResourceRootHttpPath;
	
	/**
	 * 根据时间生成路径名，然后这个路径用于正式程序包
	 */
	protected String randomResourcePath;
	
	/**
	 * 服务器端用的资源文件列表
	 */
	private String resourceFileForServer = "mieshi_resource.xml";
	
	/**
	 * 客户端用的资源文件列表
	 */
	private String resourceFileForClient = "resourceClient.bin";

	private int testResourceVersion = 0;
	
	private int realResourceVersion = 0;
	
	
	public static int LITTLE_PACKAGE_LENGTH = 16*1024;
	
	
	protected LinkedHashMap<String, ResourceData> allTestResourceData = new LinkedHashMap<String, ResourceData>();
	
	protected LinkedHashMap<String, ResourceData> allRealResourceData = new LinkedHashMap<String, ResourceData>();

	
	public void createRealResourcePackageForCurrentVersion() throws Exception{
		PackageManager.Version vs[] = PackageManager.getInstance().getRealVersions();
		if(vs.length == 0) return;
		
		PackageManager.Version newVersion = vs[vs.length-1];
		
		ResourceMD5.createResourcePackage(new File(realResourceRootPath+"/"+randomResourcePath+"/"), realResourceRootPath, newVersion.versionString,""+this.realResourceVersion);
		
	}
	
	public void createTestResourcePackageForCurrentVersion() throws Exception{
		PackageManager.Version vs[] = PackageManager.getInstance().getTestVersions();
		if(vs.length == 0) return;
		
		PackageManager.Version newVersion = vs[vs.length-1];
		
		ResourceMD5.createResourcePackage(new File(testResourceRootPath), testResourceRootPath, newVersion.versionString,""+this.testResourceVersion);
		
	}
	/**
	 * 
	 */
	/**
	 * 产生测试资源文件列表
	 * 产生两个，一个服务器端用，一个客户端用
	 */
	public void createTestResourceFileList(ArrayList<String> errorMessage) throws Exception{
		ResourceMD5.createResourceFile(false,errorMessage,"0");
		zipMap.clear();
	}

	/**
	 * 产生正式资源文件列表
	 * 产生两个，一个服务器端用，一个客户端用
	 */
	private void createRealResourceFileList(ArrayList<String> errorMessage,String randomResourcePath) throws Exception{
		ResourceMD5.createResourceFile(true,errorMessage,randomResourcePath);
		zipMap.clear();
	}

	/**
	 * 复制资源文件到指定位置
	 * 这个方法用于发布测试资源到正式资源文件夹
	 * 生成一个时间串来控制资源新旧
	 */
	public void copyTestResourceToRealResource() throws Exception{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String randomResourcePath = simpleDateFormat.format(date);
		String realPath = realResourceRootPath+"/"+randomResourcePath+"/";
		FileUtils.copy(new File(testResourceRootPath), new File(realPath), true);
		//生成正式资源
		createRealResourceFileList(new ArrayList<String>(),randomResourcePath);
	}
	
	public String getFileVersion(boolean realResource, String fileName, String gpu){
		
		//ETC格式下，alpha通道的文件版本采用对应的颜色文件版本
//		if(fileName.endsWith(ResourceMD5.ETC1_ALPHA_SUFFIX)){
//			fileName = fileName.replace(ResourceMD5.ETC1_ALPHA_SUFFIX, ResourceMD5.ETC1_SUFFIX);
//		}
		
		ResourceData rd = null;
		if(realResource){
			rd = allRealResourceData.get(fileName);
		}else{
			rd = allTestResourceData.get(fileName);
		}
		if(rd != null){
			String fileVersion = rd.getVersion()+"";
			return fileVersion;
		}
		return "0";
	}
	
	
	/**
	 * 发送资源数据给客户端
	 * 第一个发送的包内容为 文件名，文件版本，文件大小，以及文件被拆分成多少个小包发送
	 * 第二批发送的包为资源数据包，分多个小包发送。小包内容为 文件名，偏移量，数据
	 * @param conn
	 * @param fileName
	 * @param clientMachineType
	 *
	 */
	public int constructMemoryDataForClient(String fileRelatedName,byte data[],ArrayList<MIESHI_RESOURCE_FILE_REQ> resultList){
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
		
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		byte bytes[] = new byte[4096];
		int n = 0;
		try{
			java.util.zip.GZIPOutputStream output = new java.util.zip.GZIPOutputStream(bout);
			while( (n = input.read(bytes)) != -1){
				output.write(bytes, 0, n);
			}
			input.close();
			output.finish();
		}catch(Exception e){
			e.printStackTrace();
		}
		int count = 0;
			byte zipData[] = bout.toByteArray();
		
			if(zipData.length <= LITTLE_PACKAGE_LENGTH){
				count = 1;
			}else{
				count = zipData.length/LITTLE_PACKAGE_LENGTH;
				if(zipData.length % LITTLE_PACKAGE_LENGTH != 0){
					count += 1;
				}
			}
			
		
			for(int i = 0; i < count; i++){
				byte[] bs = null;
				if(i == count - 1){
					bs = new byte[zipData.length - i*LITTLE_PACKAGE_LENGTH];
				}else{
					bs = new byte[LITTLE_PACKAGE_LENGTH];
				}
				
				System.arraycopy(zipData, i*LITTLE_PACKAGE_LENGTH, bs, 0, bs.length);
				MIESHI_RESOURCE_FILE_REQ res2 = new MIESHI_RESOURCE_FILE_REQ(GameMessageFactory.nextSequnceNum(), 
						fileRelatedName, i*LITTLE_PACKAGE_LENGTH, bs);
				resultList.add(res2);
			}
			
			return zipData.length;
	
	}
	

	/**
	 * 发送资源数据给客户端
	 * 第一个发送的包内容为 文件名，文件版本，文件大小，以及文件被拆分成多少个小包发送
	 * 第二批发送的包为资源数据包，分多个小包发送。小包内容为 文件名，偏移量，数据
	 * @param conn
	 * @param fileName
	 * @param clientMachineType
	 *
	 */
	public int constructResouceDataForClient(String fileRelatedName, String fileVersion, File file,ArrayList<MIESHI_RESOURCE_FILE_REQ> resultList){
		
		int length = 0;
		long count = 0;
		if(file != null && file.isFile() && file.exists()){
			length = (int)file.length();
		
		}
		if(length > 0){
			
			byte zipData[] = zipMap.get(file.getAbsolutePath());
			if(zipData == null){
				try{
					zipData = zip(file);
					zipMap.put(file.getAbsolutePath(), zipData);
				}catch(Exception e){
					logger.warn("[组装数据包] [失败] [打zip包出现异常] ["+fileRelatedName+"] ["+fileVersion+"] ["+file+"]");
					return -1;
				}
			}
			if(zipData.length <= LITTLE_PACKAGE_LENGTH){
				count = 1;
			}else{
				count = zipData.length/LITTLE_PACKAGE_LENGTH;
				if(zipData.length % LITTLE_PACKAGE_LENGTH != 0){
					count += 1;
				}
			}
			
		
			for(int i = 0; i < count; i++){
				byte[] bs = null;
				if(i == count - 1){
					bs = new byte[zipData.length - i*LITTLE_PACKAGE_LENGTH];
				}else{
					bs = new byte[LITTLE_PACKAGE_LENGTH];
				}
				
				System.arraycopy(zipData, i*LITTLE_PACKAGE_LENGTH, bs, 0, bs.length);
				MIESHI_RESOURCE_FILE_REQ res2 = new MIESHI_RESOURCE_FILE_REQ(GameMessageFactory.nextSequnceNum(), 
						fileRelatedName, i*LITTLE_PACKAGE_LENGTH, bs);
				resultList.add(res2);
			}
			
			return zipData.length;
		}
		logger.warn("[组装数据包] [失败] [文件不存在] ["+fileRelatedName+"] ["+fileVersion+"] ["+file+"]");
		return -1;
	}

	public String 通过gpu得到资源文件夹(String gpu){
		return gpu;
	}

	public String getResourceFileForServer() {
		return resourceFileForServer;
	}

	public void setResourceFileForServer(String resourceFileForServer) {
		this.resourceFileForServer = resourceFileForServer;
	}

	public String getResourceFileForClient() {
		return resourceFileForClient;
	}

	public void setResourceFileForClient(String resourceFileForClient) {
		this.resourceFileForClient = resourceFileForClient;
	}

	public File getResourceFile(boolean realResource, String fileName, String gpu, String pingtais){
		
		String a = fileName;
		//ETC格式下，alpha通道的文件版本采用对应的颜色文件版本
//		if(a.endsWith(ResourceMD5.ETC1_ALPHA_SUFFIX)){
//			a = a.replace(ResourceMD5.ETC1_ALPHA_SUFFIX, ResourceMD5.ETC1_SUFFIX);
//		}
		
		ResourceData data = null;
		if(realResource){
			data = allRealResourceData.get(a);
		}else{
			data = allTestResourceData.get(a);
		}
		if (data == null) {
			logger.warn("[得到资源文件] [失败] [从allResourceData取出为空] [realResource:"+realResource+"] ["+a+"] ["+fileName+"] ["+gpu+"] ["+pingtais+"]");
			return null;
		}
		File file = null;
		switch (data.getFileType()) {
		case 0:
			if(realResource){
				file = new File(realResourceRootPath + "/" +randomResourcePath+"/" + "other/" + fileName);
			}else{
				file = new File(testResourceRootPath + "/" + "other/" + fileName);
			}
			
			break;
		case 1:
			if(realResource){
				file = new File(realResourceRootPath+"/" +randomResourcePath+"/"+"picture/"+通过gpu得到资源文件夹(gpu)+"/"+fileName);
			}else{
				file = new File(testResourceRootPath+"/"+"picture/"+通过gpu得到资源文件夹(gpu)+"/"+fileName);
			}
			
			break;
		case 2:
			if(realResource){
				file = new File(realResourceRootPath+"/" +randomResourcePath+"/"+"sound/"+通过平台得到资源文件夹(pingtais)+"/"+fileName);
			}else{
				file = new File(testResourceRootPath+"/"+"sound/"+通过平台得到资源文件夹(pingtais)+"/"+fileName);
			}
			
			break;
		}
		return file;
	}

	public String 通过平台得到资源文件夹 (String pingtai) {
		return pingtai;
	}

	public String getTestResourceRootPath() {
		return testResourceRootPath;
	}

	public void setTestResourceRootPath(String testResourceRootPath) {
		this.testResourceRootPath = testResourceRootPath;
	}

	public String getRealResourceRootPath() {
		return realResourceRootPath;
	}

	public void setRealResourceRootPath(String realResourceRootPath) {
		this.realResourceRootPath = realResourceRootPath;
	}

	public String getRandomResourcePath() {
		return randomResourcePath;
	}

	public void setRandomResourcePath(String randomResourcePath) {
		this.randomResourcePath = randomResourcePath;
	}

	public int getTestResourceVersion() {
		return testResourceVersion;
	}

	public void setTestResourceVersion(int testResourceVersion) {
		this.testResourceVersion = testResourceVersion;
	}

	public int getRealResourceVersion() {
		return realResourceVersion;
	}

	public void setRealResourceVersion(int realResourceVersion) {
		this.realResourceVersion = realResourceVersion;
	}

	public LinkedHashMap<String, ResourceData> getAllTestResourceData() {
		return allTestResourceData;
	}

	public void setAllTestResourceData(
			LinkedHashMap<String, ResourceData> allTestResourceData) {
		this.allTestResourceData = allTestResourceData;
	}

	public LinkedHashMap<String, ResourceData> getAllRealResourceData() {
		return allRealResourceData;
	}

	public void setAllRealResourceData(
			LinkedHashMap<String, ResourceData> allRealResourceData) {
		this.allRealResourceData = allRealResourceData;
	}

	
	public String getTestResourceRootHttpPath() {
		return testResourceRootHttpPath;
	}

	public void setTestResourceRootHttpPath(String testResourceRootHttpPath) {
		this.testResourceRootHttpPath = testResourceRootHttpPath;
	}

	public String getRealResourceRootHttpPath() {
		return realResourceRootHttpPath;
	}

	public void setRealResourceRootHttpPath(String realResourceRootHttpPath) {
		this.realResourceRootHttpPath = realResourceRootHttpPath;
	}

	public static byte[] zip(File file) throws Exception{
		ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
		java.util.zip.GZIPOutputStream output = new java.util.zip.GZIPOutputStream(bout);
		FileInputStream input = new FileInputStream(file);
		byte bytes[] = new byte[4096];
		int n = 0;
		while( (n = input.read(bytes)) != -1){
			output.write(bytes, 0, n);
		}
		input.close();
		output.finish();
		return bout.toByteArray();
	}
}
