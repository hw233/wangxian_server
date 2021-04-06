package com.fy.gamegateway.mieshi.resource.manager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.fy.gamegateway.mieshi.server.MieshiGatewayServer;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.text.StringUtil;


public class ResourceMD5 {
	
	static Logger logger = Logger.getLogger(MieshiGatewayServer.class);

	public static final int FILETYPE_OTHER = 0;
	
	public static final int FILETYPE_SOUND = 2;
	public static final int FILETYPE_PICTURE = 1;

	
	public static String[] sound_postfix = new String[]{".wav", ".ogg", ".wav"};
//	private static String[] picture_postfix = new String[]{".png", ".DDS", ".pvr"};
	public static String[] picture_postfix = new String[]{".png", ".pvr", ".ges"};
	

	
	public static String OTHER = "/other";
	public static String PICTURE = "/picture";
	public static String SOUND = "/sound";


//	public static final String GPU_NAMES[] = new String[]{"pvr_res","attic_res","png_res"};
	public static final String GPU_NAMES[] = new String[]{"ios_res","png_res", "android_res"};
	
	public static final String PLATFORM_NAMES[] = new String[]{"IOS", "Android"};

	
	public static boolean isValid_GPU(String gpu){
		for(int i = 0 ; i < GPU_NAMES.length ; i++){
			if(GPU_NAMES[i].equals(gpu)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValid_Platform(String pla){
		for(int i = 0 ; i < PLATFORM_NAMES.length ; i++){
			if(PLATFORM_NAMES[i].equals(pla)){
				return true;
			}
		}
		return false;
	}

	static String getMD5(File file) throws Exception{
		FileInputStream input = new FileInputStream(file);
		byte bytes[] = new byte[(int)file.length()];
		input.read(bytes);
		input.close();
		
		return StringUtil.hash(bytes);
	}

	public static ArrayList<File> traverseFile(File file){
		ArrayList<File> list = new ArrayList<File>();
		if (file.isDirectory()) {
			File[] dirF = file.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					if (name.endsWith(".orig")) {
						return false;
					}
					return true;
				}
			});
			for (int i = 0;dirF != null && i<dirF.length; i++) {
				list.addAll(traverseFile(dirF[i]));
			}
		} else if(file.isFile()){
			if (!file.isHidden()) {
				list.add(file);
			}
		}
		return list;
	}
	
	public static void createResourcePackage(File resDir,String saveDir,String programVersion,String resourceVersion) throws Exception{
		
		//other 全部存入zip中
		
		//picture 分几个gpu类型
		
		//sound 分IOS和Android
		
		//数据包的名称  mieshi_resource_1.3.0_pvr_res_Android.zip
		
		for(int i = 0 ; i < GPU_NAMES.length ; i++){
			for(int j = 0 ; j < PLATFORM_NAMES.length ; j++){
				long zipUncompressTotalSize = 0L;
				String gpu = GPU_NAMES[i];
				String platform = PLATFORM_NAMES[j];
				
				ZipOutputStream zoutput = new ZipOutputStream(new FileOutputStream(saveDir+"/pmxx_res_" + programVersion+"_" + resourceVersion+"_"+gpu+"_"+platform+".zip"));
				
				File other = new File(resDir,OTHER);
				ArrayList<File> files = traverseFile(other);
				for (int k = 0; k < files.size(); k++) {
					File f = files.get(k);
					FileInputStream input = new FileInputStream(f);
					String s = f.getAbsolutePath().substring(other.getAbsolutePath().length());
					while(s.charAt(0) == '/'){
						s = s.substring(1);
					}
					ZipEntry ze = new ZipEntry("pmxx_res/"+s);
					ze.setSize(input.available());
					zipUncompressTotalSize+= ze.getSize();
					zoutput.putNextEntry(ze);
					byte bytes[] = new byte[4096];
					int n = 0;
					while( (n = input.read(bytes)) != -1 ){
						zoutput.write(bytes, 0, n);
					}
					input.close();
					zoutput.closeEntry();
				}
				
				other = new File(resDir,PICTURE+"/"+gpu);
				files = traverseFile(other);
				for (int k = 0; k < files.size(); k++) {
					File f = files.get(k);
					FileInputStream input = new FileInputStream(f);
					String s = f.getAbsolutePath().substring(other.getAbsolutePath().length());
					while(s.charAt(0) == '/'){
						s = s.substring(1);
					}
					ZipEntry ze = new ZipEntry("pmxx_res/"+s);
					ze.setSize(input.available());
					zipUncompressTotalSize+= ze.getSize();
					zoutput.putNextEntry(ze);
					byte bytes[] = new byte[4096];
					int n = 0;
					while( (n = input.read(bytes)) != -1 ){
						zoutput.write(bytes, 0, n);
					}
					input.close();
					zoutput.closeEntry();
				}
				
				other = new File(resDir,SOUND+"/"+platform);
				files = traverseFile(other);
				for (int k = 0; k < files.size(); k++) {
					File f = files.get(k);
					FileInputStream input = new FileInputStream(f);
					String s = f.getAbsolutePath().substring(other.getAbsolutePath().length());
					while(s.charAt(0) == '/'){
						s = s.substring(1);
					}
					ZipEntry ze = new ZipEntry("pmxx_res/"+s);
					ze.setSize(input.available());
					zipUncompressTotalSize+= ze.getSize();
					zoutput.putNextEntry(ze);
					byte bytes[] = new byte[4096];
					int n = 0;
					while( (n = input.read(bytes)) != -1 ){
						zoutput.write(bytes, 0, n);
					}
					input.close();
					zoutput.closeEntry();
				}
				ResourceManager rm =ResourceManager.getInstance(); 
				File f = new File(resDir+"/"+rm.getResourceFileForClient());
				FileInputStream input = new FileInputStream(f);
				ZipEntry ze = new ZipEntry("pmxx_res/"+rm.getResourceFileForClient()+"2");
				ze.setSize(input.available());
				zipUncompressTotalSize+= ze.getSize();
				zoutput.putNextEntry(ze);
				byte bytes[] = new byte[4096];
				int n = 0;
				while( (n = input.read(bytes)) != -1 ){
					zoutput.write(bytes, 0, n);
				}
				input.close();
				zoutput.closeEntry();
				
				zoutput.setComment("total:"+zipUncompressTotalSize);
				zoutput.close();
			}
		}
	}
	
	public static void createResourceFile(boolean realResource, 
			ArrayList<String> errorMessage, String randomTimePath) throws Exception{
		long startTime = System.currentTimeMillis();
		boolean hasError = false;
		ResourceManager rm = ResourceManager.getInstance();
		String paths = "";
		String randomResourcePath = rm.randomResourcePath;
		if(realResource){
			File dir = new File(rm.getRealResourceRootPath());
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
				try{
					//如果传进来的参数比系统存在的最大时间路径大，那么按照参数生成新路径
					if(Long.parseLong(randomTimePath) > num){
						num = Long.parseLong(randomTimePath);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				randomResourcePath = num+"";
			}
			paths = rm.getRealResourceRootPath()+"/"+randomResourcePath;
			
		}else{
			paths = rm.getTestResourceRootPath();
		}
		File newXml = new File(paths+"/"+rm.getResourceFileForServer());

		File oldXml = null;
		int resourceVersion = 1;		//资源版本
		boolean isNew = true;			//是否是第一次打包
		boolean isUpdate = false;		//是否有更新
		Hashtable<String, ResourceData> resOtherTab = new Hashtable<String, ResourceData>();
		Hashtable<String, ResourceData> resSoundTab = new Hashtable<String, ResourceData>();
		Hashtable<String, ResourceData> resPictureTab = new Hashtable<String, ResourceData>();
		Hashtable<String, ResourceData> oldTab = new Hashtable<String, ResourceData>();
		
		LinkedHashMap<String, ResourceData> allResourceData = new LinkedHashMap<String, ResourceData>();
		if (newXml.isFile()) {
			//xml是否存在
			isNew = false;
			//oldXml = new File(paths+"/"+rm.getResourceFileForServer()+"_bak_" +System.currentTimeMillis());
			//newXml.renameTo(oldXml);
			
			SAXReader reader = new SAXReader();
			Document document = null;
			try {
				document = reader.read(newXml);
			} catch (DocumentException e) {
				logger.warn("[创建资源文件列表] [失败] [出现异常] [解析XML失败] ["+oldXml.getAbsolutePath()+"] [realResource:"+realResource+"] [path"+paths+"]",e);
				return;
			}
			
			Element resources = document.getRootElement();
			resourceVersion = Integer.parseInt(resources.attributeValue("version"));
			for (Iterator<Element> i = resources.elementIterator(); i.hasNext();) {
				Element element = i.next();
				ResourceData data = new ResourceData();
				data.setPath(element.attributeValue("filepath"));
				oldTab.put(data.getPath(), data);
				data.setVersion(Integer.parseInt(element.attributeValue("version")));
				data.setFileType(Integer.parseInt(element.attributeValue("filetype")));
				data.setTypeMD5(new Hashtable<String, String>());
				for (Iterator<Attribute> att = element.attributeIterator(); att.hasNext();) {
					Attribute a = att.next();
					if (a.getName().endsWith("MD5")) {
						data.addTypeMD5(a.getName().substring(0, a.getName().indexOf("MD5")), a.getValue());
					}
				}
			}
		}
		
		StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding='utf-8'?>\n");
		
		
		//得到other
		File other = new File(paths + OTHER);
		try{
			logger.warn("创建资源文件列表other开始");
			ArrayList<File> allOthers = traverseFile(other);
			for (int i = 0; i < allOthers.size(); i++) {
				ResourceData re = new ResourceData();
				//路径要去掉入口路径只保留到other下为根目录
				
				String path = allOthers.get(i).getPath();
				path = path.substring(other.getPath().length()+1, path.length());
				re.setPath(path);
				re.setFileType(0);
				re.setTypeMD5(new Hashtable<String, String>());
				
				re.fileSize = (int)allOthers.get(i).length();
				
				byte zipData[] = ResourceManager.zip(allOthers.get(i));
				re.fileSize = zipData.length;
			
				
				re.addTypeMD5("other", getMD5(allOthers.get(i)));
				resOtherTab.put(re.getPath(), re);
				//other有后缀
				allResourceData.put(re.getPath(), re);
				if (isNew) {
					re.setVersion(1);
				} else {
					ResourceData old = oldTab.get(path);
					if (old == null) {
						re.setVersion(1);
						//System.out.println("新增other资源" + re.getPath());
					} else {
						if (old.getTypeMD5().get("other").equals(re.getTypeMD5().get("other"))){
							re.setVersion(old.getVersion());
						} else {
							//System.out.println("other有更新" + re.getPath() + "更新后版本" + (old.getVersion() + 1));
							re.setVersion(old.getVersion()+1);
							isUpdate = true;
						}
					}
				}
				logger.warn("创建资源文件列表other " + i + "~~" + allOthers.size());
			}
			logger.warn("创建资源文件列表other完成");
		}catch (Exception e) {
			logger.warn("[创建资源文件列表] [失败] [出现异常] [扫描OTHER] ["+other.getAbsolutePath()+"] [realResource:"+realResource+"] [path"+paths+"]",e);
			return;
		}
		
		//得到sound
		File sound = new File(paths + SOUND);
		try{
			logger.warn("创建资源文件列表sound开始");
			
			File[] sound_pingtai = sound.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					for (int i = 0; i<PLATFORM_NAMES.length; i++) {
						if (PLATFORM_NAMES[i].equals(name)) {
							return true;
						}
					}
					return false;
				}
			});
			if(sound_pingtai == null) sound_pingtai = new File[0];
			
			String[] name = new String[sound_pingtai.length];
			for (int i = 0; i < name.length; i++) {
				name[i] = sound_pingtai[i].getName();
			}
			
			for (int i = 0; i < sound_pingtai.length; i++) {
				ArrayList<File> lists = traverseFile(sound_pingtai[i]);
				for (int j = 0; j < lists.size(); j++) {
					String path = lists.get(j).getPath();
					path = path.substring(sound_pingtai[i].getPath().length() + 1, path.length());
					String pathWithSuffix = path;
					for (int k = 0; k < sound_postfix.length; k++) {
						int index = path.indexOf(sound_postfix[k]);
						if (index >=0) {
							path = path.substring(0, index);
						}
					}
					
					ResourceData re = null;
					if (i == 0) {
						re = new ResourceData();
						
						re.fileSize = (int)lists.get(j).length();
						
						byte zipData[] = ResourceManager.zip(lists.get(j));
						re.fileSize = zipData.length;
						
						if (isNew) {
							re.setVersion(1);
						}
						re.setPath(path);
						re.setFileType(2);
						re.setTypeMD5(new Hashtable<String, String>());
						resSoundTab.put(re.getPath(), re);
						
						
					} else {
						re = resSoundTab.get(path);
						if(re == null){
							hasError = true;
							errorMessage.add("错误：path="+lists.get(j).getPath()+", 在第一个平台["+sound_pingtai[0].getName()+"]上没有");
							
							//throw new RuntimeException("错误：path="+lists.get(j).getPath()+", 在第一个平台["+sound_pingtai[0].getName()+"]上没有");
						}
	
					}
					if(re != null){
						re.addTypeMD5(name[i], getMD5(lists.get(j)));
						allResourceData.put(pathWithSuffix, re);
					}
					logger.warn("创建资源文件列表sound "+name[i]+"  的"+(j) +"~~" +lists.size()+"完成");
				}
				logger.warn("创建资源文件列表sound "+name[i]+"完成");
			}
			
			for (Enumeration<String> e = resSoundTab.keys(); e.hasMoreElements();) {
				ResourceData newData = resSoundTab.get(e.nextElement());
				for (int i = 0; i < PLATFORM_NAMES.length; i++) {
					if (newData.getTypeMD5().get(PLATFORM_NAMES[i]) == null) {
						hasError = true;
						errorMessage.add("错误：声音 path="+newData.getPath()+", 在平台["+PLATFORM_NAMES[i]+"]上没有");
					}
				}
			}
			if (!isNew) {
				for (Enumeration<String> e = resSoundTab.keys(); e.hasMoreElements();) {
					ResourceData newData = resSoundTab.get(e.nextElement());
					ResourceData oldData = oldTab.get(newData.getPath());
					if (oldData == null) {
						newData.setVersion(1);
						isUpdate = true;
					} else {
						boolean isChanage = false;
						for (Enumeration<String> md5E = newData.getTypeMD5().keys(); md5E.hasMoreElements();) {
							String key = md5E.nextElement();
							if (oldData.getTypeMD5().get(key) != null && newData.getTypeMD5().get(key).equals(oldData.getTypeMD5().get(key))) {
								if (isChanage) {
									hasError = true;
									errorMessage.add("出错了，只有一个平台下的声音修改了" + newData.getPath());
								}
							} else if (oldData.getTypeMD5().get(key) == null) {
								
							} else {
								isChanage = true;
							}
						}
						if (isChanage) {
							//System.out.println("声音有更新" + newData.getPath() + "更新后版本" + (oldData.getVersion() + 1));
							newData.setVersion(oldData.getVersion() + 1);
							isUpdate = true;
						} else {
							newData.setVersion(oldData.getVersion());
						}
					}
				}
			}
			logger.warn("创建资源文件列表sound结束");
		}catch (Exception e) {
			logger.warn("[创建资源文件列表] [失败] [出现异常] [扫描SOUND] ["+sound.getAbsolutePath()+"] [realResource:"+realResource+"] [path"+paths+"]",e);
			return;
		}
		//得到picture
		File picture = new File(paths + PICTURE);
		try{
			logger.warn("创建资源文件列表picture开始");
			File[] picture_pingtai = picture.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					for(int j = 0 ; j < GPU_NAMES.length ; j++){
						if(GPU_NAMES[j].equals(name)){
							return true;
						}
					}
					return false;
				}
			});
			
			for (int i = 0; picture_pingtai != null && i < picture_pingtai.length; i++) {
				int gpuNameIndex = -1;
				for(int j = 0 ; j < GPU_NAMES.length ; j++){
					if(GPU_NAMES[j].equals(picture_pingtai[i].getName())){
						gpuNameIndex = j;
						break;
					}
				}
				if (gpuNameIndex < 0) {
					continue;
				}
				ArrayList<File> lists = traverseFile(picture_pingtai[i]);
				for (int j = 0; j < lists.size(); j++) {
					
					String path = lists.get(j).getPath();
					path = path.substring(picture_pingtai[i].getPath().length() + 1, path.length());
					String pathWithSuffix = path;
					
					//为了ETC格式的alpha通道
//					if(path.endsWith(ETC1_ALPHA_SUFFIX)){
//						continue;
//					}
					
					for (int k = 0; k < picture_postfix.length; k++) {
						int index = path.indexOf(picture_postfix[k]);
						if (index >=0) {
							path = path.substring(0, index);
						}
					}
					ResourceData re = null;
					if (i == 0) {
						re = new ResourceData();
						
						re.fileSize = (int)lists.get(j).length();
						
						
						byte zipData[] = ResourceManager.zip(lists.get(j));
						re.fileSize = zipData.length;
						
						
						if (isNew) {
							re.setVersion(1);
						}
						re.setPath(path);
						re.setFileType(1);
						re.setTypeMD5(new Hashtable<String, String>());
						resPictureTab.put(re.getPath(), re);
						
					} else {
						re = resPictureTab.get(path);
						if(re == null){
							hasError = true;
							errorMessage.add("错误：path="+lists.get(j).getPath()+", 在第一个平台["+picture_pingtai[0].getName()+"]上没有");
						}
					}
					if(re != null){
						re.addTypeMD5(GPU_NAMES[gpuNameIndex], getMD5(lists.get(j)));
						allResourceData.put(pathWithSuffix, re);
					
					}
					logger.warn("创建资源文件列表picture "+picture_pingtai[i].getName()+"  的"+(j) +"~~" +lists.size()+"完成");
				}
				logger.warn("创建资源文件列表picture "+picture_pingtai[i].getName()+"完成");
			}
			for (Enumeration<String> e = resPictureTab.keys(); e.hasMoreElements();) {
				ResourceData newData = resPictureTab.get(e.nextElement());
				for (int i = 0; i < GPU_NAMES.length; i++) {
					if (newData.getTypeMD5().get(GPU_NAMES[i]) == null) {
						hasError = true;
						errorMessage.add("错误：图片 path="+newData.getPath()+", 在GPU["+GPU_NAMES[i]+"]上没有");
					}
				}
			}
			if (!isNew) {
				for (Enumeration<String> e = resPictureTab.keys(); e.hasMoreElements();) {
					ResourceData newData = resPictureTab.get(e.nextElement());
					ResourceData oldData = oldTab.get(newData.getPath());
					if (oldData == null) {
						newData.setVersion(1);
						isUpdate = true;
						//System.out.println("新增picture资源" + newData.getPath());
					} else {
						boolean isChanage = false;
						for (Enumeration<String> md5E = newData.getTypeMD5().keys(); md5E.hasMoreElements();) {
							String key = md5E.nextElement();
							if (oldData.getTypeMD5().get(key) != null && newData.getTypeMD5().get(key).equals(oldData.getTypeMD5().get(key))) {
								if (isChanage) {
									//System.out.println("出错了，只有一个GPU下的图片修改了" + newData.getPath());
									hasError = true;
									errorMessage.add("出错了，只有一个GPU"+key+"下的图片修改了" + newData.getPath());
								}
							}else if (oldData.getTypeMD5().get(key) == null) {
								//如果么有旧的，代表新加了一种资源
							}else {
								isChanage = true;
							}
						}
						if (isChanage) {
							//System.out.println("图片有更新" + newData.getPath() + "更新后版本" + (oldData.getVersion() + 1));
							newData.setVersion(oldData.getVersion() + 1);
							isUpdate = true;
						} else {
							newData.setVersion(oldData.getVersion());
						}
					}
				}
			} 
			logger.warn("创建资源文件列表picture结束");
		}catch (Exception e) {
			
			
			logger.warn("[创建资源文件列表] [失败] [出现异常] [扫描picture] ["+picture.getAbsolutePath()+"] [realResource:"+realResource+"] [path"+paths+"]",e);
		
			e.printStackTrace();
			return;
		}
		
		
		if(hasError){
			for(String s : errorMessage){
				logger.warn("[创建资源文件列表] [失败] [realResource:"+realResource+"] [path"+paths+"]" + s);
			}
			return;
		}
		
		for(int i = 8 ; i >= 0  ; i--){
			File oldXml1 = new File(paths+"/"+rm.getResourceFileForServer()+"_bak_" + (i+1));
			File oldXml2 = new File(paths+"/"+rm.getResourceFileForServer()+"_bak_" + i);
			
			if(oldXml1.isFile() && oldXml1.exists()){
				oldXml1.delete();
			}
			
			if(oldXml2.isFile() && oldXml2.exists()){
				oldXml2.renameTo(oldXml1);
			}
		}
		
		oldXml = new File(paths+"/"+rm.getResourceFileForServer()+"_bak_0");
		newXml.renameTo(oldXml);
		
		if (isUpdate) {
			resourceVersion = resourceVersion + 1;
		}
		
		if(realResource){
			rm.setRealResourceVersion(resourceVersion);
		}else{
			rm.setTestResourceVersion(resourceVersion);
		}
		
		//写入xml
		try{
			//写最外面版本
			buffer.append("<resources version=\"").append(resourceVersion).append("\" num=\"")
			.append(resOtherTab.size() + resSoundTab.size() + resPictureTab.size()).append("\"").append(">\n");
			
			//写资源内容
			for (Enumeration<String> keys = resOtherTab.keys(); keys.hasMoreElements();) {
				String key = keys.nextElement();
				ResourceData data = resOtherTab.get(key);
				buffer.append(data.toXmlString());
			}
			for (Enumeration<String> keys = resSoundTab.keys(); keys.hasMoreElements();) {
				String key = keys.nextElement();
				ResourceData data = resSoundTab.get(key);
				buffer.append(data.toXmlString());
			}
			for (Enumeration<String> keys = resPictureTab.keys(); keys.hasMoreElements();) {
				String key = keys.nextElement();
				ResourceData data = resPictureTab.get(key);
				buffer.append(data.toXmlString());
			}
			//写结束

			buffer.append("</resources>");

			newXml = new File(paths+"/"+rm.getResourceFileForServer());
			FileOutputStream out = new FileOutputStream(newXml);
			out.write(buffer.toString().getBytes("utf-8"));
			out.close();

		}catch(Exception e){
			logger.warn("[创建资源文件列表] [失败] [出现异常] [写最外面版本] ["+newXml.getAbsolutePath()+"] [realResource:"+realResource+"] [path"+paths+"]",e);
			throw e;
		}
		
		//写入客户端用资源版本目录 2进制文件    string 版本  int 资源个数 [资源path， 资源type, 资源版本]
		File client = new File(paths + "/"+rm.getResourceFileForClient());
		try {
			
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(client));
			String resourceVersionS = ""+resourceVersion;
			byte[] rvBs = null;
			try{
				rvBs = resourceVersionS.getBytes("UTF-8");
			}catch(java.io.UnsupportedEncodingException e){
				logger.warn("[创建资源文件列表] [失败] [出现异常] [转utf]", e);
				return;
			}
			dos.writeShort(rvBs.length);
			dos.write(rvBs);
			dos.writeInt(resOtherTab.size() + resSoundTab.size() + resPictureTab.size());
			for (Enumeration<String> keys = resOtherTab.keys(); keys.hasMoreElements();) {
				String key = keys.nextElement();
				ResourceData data = resOtherTab.get(key);
				rvBs = null;
				try{
					rvBs = data.getPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
					logger.warn("[创建资源文件列表] [失败] [出现异常] [转utf] [" + data.getPath() + "]", e);
					return;
				}
				dos.writeShort(rvBs.length);
				dos.write(rvBs);
				dos.writeByte(data.getFileType());
				dos.writeInt(data.getVersion());
				dos.writeInt(data.fileSize);
			}
			for (Enumeration<String> keys = resSoundTab.keys(); keys.hasMoreElements();) {
				String key = keys.nextElement();
				ResourceData data = resSoundTab.get(key);
				rvBs = null;
				try{
					rvBs = data.getPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
					logger.warn("[创建资源文件列表] [失败] [出现异常] [转utf] [" + data.getPath() + "]", e);
					return;
				}
				dos.writeShort(rvBs.length);
				dos.write(rvBs);
				dos.writeByte(data.getFileType());
				dos.writeInt(data.getVersion());
				dos.writeInt(data.fileSize);
			}
			for (Enumeration<String> keys = resPictureTab.keys(); keys.hasMoreElements();) {
				String key = keys.nextElement();
				ResourceData data = resPictureTab.get(key);
				rvBs = null;
				try{
					rvBs = data.getPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
					logger.warn("[创建资源文件列表] [失败] [出现异常] [转utf] [" + data.getPath() + "]", e);
					return;
				}
				dos.writeShort(rvBs.length);
				dos.write(rvBs);
				dos.writeByte(data.getFileType());
				dos.writeInt(data.getVersion());
				dos.writeInt(data.fileSize);
			}
			dos.close();
		} catch (Exception e){
			logger.warn("[创建资源文件列表] [失败] [出现异常] [写入客户端用资源版本目录] ["+client.getAbsolutePath()+"] [realResource:"+realResource+"] [path"+paths+"]",e);
			throw e;
		}
		if(realResource){
			rm.allRealResourceData = allResourceData;
			rm.randomResourcePath = randomResourcePath;
		}else{
			rm.allTestResourceData = allResourceData;
		}
		
		
		logger.warn("[创建资源文件列表] [成功] [OK] [OK] [更新:"+isUpdate+"] [realResource:"+realResource+"] [version:"+resourceVersion+"] [path"+paths+"] [耗时:" + (System.currentTimeMillis() - startTime) + "]");

	}
	
}
