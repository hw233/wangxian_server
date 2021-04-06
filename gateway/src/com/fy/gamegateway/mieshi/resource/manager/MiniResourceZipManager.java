package com.fy.gamegateway.mieshi.resource.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class MiniResourceZipManager implements Runnable {
	public static Logger logger = Logger.getLogger(MiniResourceZipManager.class);

	public static MiniResourceZipManager instance;
	public static ConcurrentHashMap<String, MiniResourceZipData> miniDatas;
	public static DefaultDiskCache diskCache;
	public static String DISK_CACHE_KEY = "MiniResoureZipData";
	private String data_path;
	
	public static String DISK_CACHE_KEY_CTOC = "ctoc";
	public static HashMap<String, String> cTOc = new HashMap<String, String>();
	
	private String miniZipRootPath;
	private String miniZipHttpRootPath;
	
	public void init() {
		diskCache = new DefaultDiskCache(new File(getData_path()), "MiniZip资源", 1000L*60*60*24*365);
		instance = this;
		miniDatas = (ConcurrentHashMap<String, MiniResourceZipData>)diskCache.get(DISK_CACHE_KEY);
		if (miniDatas == null) {
			miniDatas = new ConcurrentHashMap<String, MiniResourceZipData>();
			diskCache.put(DISK_CACHE_KEY, miniDatas);
		}
		cTOc = (HashMap<String, String>)diskCache.get(DISK_CACHE_KEY_CTOC);
		if (cTOc == null) {
			cTOc = new HashMap<String, String>();
			diskCache.put(DISK_CACHE_KEY_CTOC, cTOc);
		}
		new Thread(this).start();
		logger.warn("MiniResoureZipManager初始化成功");
	}
	
	public MiniResourceZipData getMiniResourceUrl (String resourceVersion) {
		if (miniDatas != null) {
			MiniResourceZipData data = miniDatas.get(resourceVersion);
			if (data != null) {
				return data;
			}
		}
		return null;
	}
	
	public String createMiniResourceZip () {
		try {
			MiniResourceZipData data = new MiniResourceZipData();
			data.setLastResourceVersion(""+ResourceManager.getInstance().getRealResourceVersion());
			data.setNowResourceVersion(""+ResourceManager.getInstance().getTestResourceVersion());
			data.addChannel(MiniResourceZipData.DEFAULT_DOWN_CHANNEL);
			logger.warn("[开始检查变化] [正式="+data.getLastResourceVersion()+"] [测试="+data.getNowResourceVersion()+"]");
			
			//检查当前测试资源目录和正式资源目录，查出新增的和变化的
			LinkedHashMap<String, ResourceData> realDatas = ResourceManager.getInstance().getAllRealResourceData();
			LinkedHashMap<String, ResourceData> textDatas = ResourceManager.getInstance().getAllTestResourceData();
			Iterator<String> keyIt = textDatas.keySet().iterator();
			ArrayList<ResourceData> needs = new ArrayList<ResourceData>();
			while(keyIt.hasNext()) {
				String key = keyIt.next();
				ResourceData text = textDatas.get(key);
				ResourceData real = realDatas.get(key);
				if (!needs.contains(text)) {
					if (real == null) {
						needs.add(text);
						logger.warn("[检查到增加] [资源:"+text.getPath()+"] [类型:"+text.getFileType()+"] ["+text.getVersion()+"]");
					}else if (text.getVersion() != real.getVersion()) {
						needs.add(text);
						logger.warn("[检查到变化] [资源:"+text.getPath()+"] [类型:"+text.getFileType()+"] ["+text.getVersion()+"-"+real.getVersion()+"]");
					}
				}
			}
			logger.warn("[检查完成开始打包ZIP] [正式数目:"+realDatas.size()+"] [测试数目:"+textDatas.size()+"] [更新数目:"+needs.size()+"]");
			//打包成ZIP分GPU
			//picture_postfix 修改成和GPU顺序一致
			String[] picture_postfix = {".pvr", ".png", ".ges"};
			String[] sound_postfix = ResourceMD5.sound_postfix;
			String resRootPath = ResourceManager.getInstance().getTestResourceRootPath();
			for (int i = 0; i < ResourceMD5.GPU_NAMES.length; i++) {
				for (int k = 0; k < ResourceMD5.PLATFORM_NAMES.length; k++) {
					String gpu = ResourceMD5.GPU_NAMES[i];
					String picture_end = picture_postfix[i];
					String sound_end = sound_postfix[k];
					String platform = ResourceMD5.PLATFORM_NAMES[k];
					String lastVer = data.getLastResourceVersion();
					String nowVer = data.getNowResourceVersion();
					long zipUncompressTotalSize = 0L;
					String zipPath = getMiniZipRootPath()+"/mieshi_miniResourceZip_"+platform+"_"+gpu+"_"+lastVer+"_"+nowVer+".zip";
					logger.warn("[开始生成ZIP] [gpu:"+gpu+"] [palt:"+platform+"] [版本:"+lastVer+"-"+nowVer+"] ["+zipPath+"]");
					ZipOutputStream zoutput = new ZipOutputStream(new FileOutputStream(zipPath));
					for (int j = 0; j < needs.size(); j++) {
						ResourceData resData = needs.get(j);
						if (resData.getFileType() == 0) {
							//other
							String otherPath = resRootPath + ResourceMD5.OTHER;
							File f = new File(otherPath+"/"+resData.getPath());
							FileInputStream input = new FileInputStream(f);
							ZipEntry ze = new ZipEntry("pmxx_res/"+resData.getPath());
							ze.setSize(input.available());
							long oneSize = ze.getSize();
							zipUncompressTotalSize+= oneSize;
							zoutput.putNextEntry(ze);
							byte bytes[] = new byte[4096];
							int n = 0;
							while( (n = input.read(bytes)) != -1 ){
								zoutput.write(bytes, 0, n);
							}
							input.close();
							zoutput.closeEntry();
							logger.warn("[压缩ZIP] [数目:"+needs.size()+"] [index:"+j+"] [gpu:"+gpu+"] [palt:"+platform+"] [版本:"+lastVer+"-"+nowVer+"] [资源:"+f.getPath()+"] [大小"+oneSize+"] [总大小:"+zipUncompressTotalSize+"]");
						}else if (resData.getFileType() == 1) {
							//picture
//							if (gpu.equals(ResourceMD5.GPU_NAMES[2]) && resData.getPath().indexOf(".") < 0) {
//								String picturePath = resRootPath + ResourceMD5.PICTURE+"/"+gpu;
//								File f = new File(picturePath+"/"+resData.getPath()+ResourceMD5.ETC1_ALPHA_SUFFIX);
//								FileInputStream input = new FileInputStream(f);
//								ZipEntry ze = new ZipEntry("pmxx_res/"+resData.getPath()+ResourceMD5.ETC1_ALPHA_SUFFIX);
//								ze.setSize(input.available());
//								long oneSize = ze.getSize();
//								zipUncompressTotalSize+= oneSize;
//								zoutput.putNextEntry(ze);
//								byte bytes[] = new byte[4096];
//								int n = 0;
//								while( (n = input.read(bytes)) != -1 ){
//									zoutput.write(bytes, 0, n);
//								}
//								input.close();
//								zoutput.closeEntry();
//							}
							String picturePath = resRootPath + ResourceMD5.PICTURE+"/"+gpu;
							File f = null;
							if (resData.getPath().indexOf(".") >= 0) {
								f = new File(picturePath+"/"+resData.getPath());
							}else {
								f = new File(picturePath+"/"+resData.getPath()+picture_end);
							}
							FileInputStream input = new FileInputStream(f);
							ZipEntry ze = null;
							if (resData.getPath().indexOf(".") >= 0) {
								ze = new ZipEntry("pmxx_res/"+resData.getPath());
							}else {
								ze = new ZipEntry("pmxx_res/"+resData.getPath()+picture_end);
							}
							ze.setSize(input.available());
							long oneSize = ze.getSize();
							zipUncompressTotalSize+= oneSize;
							zoutput.putNextEntry(ze);
							byte bytes[] = new byte[4096];
							int n = 0;
							while( (n = input.read(bytes)) != -1 ){
								zoutput.write(bytes, 0, n);
							}
							input.close();
							zoutput.closeEntry();
							logger.warn("[压缩ZIP] [数目:"+needs.size()+"] [index:"+j+"] [gpu:"+gpu+"] [palt:"+platform+"] [版本:"+lastVer+"-"+nowVer+"] [资源:"+f.getPath()+"] [大小"+oneSize+"] [总大小:"+zipUncompressTotalSize+"]");
						}else if (resData.getFileType() == 2) {
							//sound
							String soundPath = resRootPath + ResourceMD5.SOUND+"/"+platform;
							File f = null;
							if (resData.getPath().indexOf(".mp3") >= 0) {
								f = new File(soundPath+"/"+resData.getPath());
							}else {
								f = new File(soundPath+"/"+resData.getPath()+sound_end);
							}
							FileInputStream input = new FileInputStream(f);
							ZipEntry ze = null;
							if (resData.getPath().indexOf(".") >= 0) {
								ze = new ZipEntry("pmxx_res/"+resData.getPath());
							}else {
								ze = new ZipEntry("pmxx_res/"+resData.getPath()+sound_end);
							}
							ze.setSize(input.available());
							long oneSize = ze.getSize();
							zipUncompressTotalSize+= oneSize;
							zoutput.putNextEntry(ze);
							byte bytes[] = new byte[4096];
							int n = 0;
							while( (n = input.read(bytes)) != -1 ){
								zoutput.write(bytes, 0, n);
							}
							input.close();
							zoutput.closeEntry();
							logger.warn("[压缩ZIP] [数目:"+needs.size()+"] [index:"+j+"] [gpu:"+gpu+"] [palt:"+platform+"] [版本:"+lastVer+"-"+nowVer+"] [资源:"+f.getPath()+"] [大小"+oneSize+"] [总大小:"+zipUncompressTotalSize+"]");
						}
					}
					
					//写入测试资源列表
					ResourceManager rm =ResourceManager.getInstance(); 
					File f = new File(resRootPath+"/"+rm.getResourceFileForClient());
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
					
					File zipF = new File(zipPath);
					data.setSize(k, i, zipF.length());
					data.getDownloadUrl().put(data.createKey(MiniResourceZipData.DEFAULT_DOWN_CHANNEL, ResourceMD5.PLATFORM_NAMES[k], ResourceMD5.GPU_NAMES[i]), getMiniZipHttpRootPath()+zipF.getName());
					logger.warn("[ZIP生成成功] [gpu:"+gpu+"] [palt:"+platform+"] [版本:"+lastVer+"-"+nowVer+"] ["+zipPath+"] [url:"+data.getDownloadUrl().get(data.createKey(MiniResourceZipData.DEFAULT_DOWN_CHANNEL, ResourceMD5.PLATFORM_NAMES[k], ResourceMD5.GPU_NAMES[i]))+"]");
				}
			}
			data.heartbeat();
			miniDatas.put(data.getLastResourceVersion(), data);
			diskCache.put(DISK_CACHE_KEY, miniDatas);
			return "ZIP已经全部生成成功，可以去-----[资源包共享节点/MiniZip包共享]";
		} catch(Exception e) {
			logger.error("createMiniResourceZip出错:", e);
		}
		return "出现未知错误，请检查MiniResourceZipManager的log";
	}

	public void setData_path(String data_path) {
		this.data_path = data_path;
	}

	public String getData_path() {
		return data_path;
	}

	public void setMiniZipRootPath(String miniZipRootPath) {
		this.miniZipRootPath = miniZipRootPath;
	}

	public String getMiniZipRootPath() {
		return miniZipRootPath;
	}

	public void setMiniZipHttpRootPath(String miniZipHttpRootPath) {
		this.miniZipHttpRootPath = miniZipHttpRootPath;
	}

	public String getMiniZipHttpRootPath() {
		return miniZipHttpRootPath;
	}

	@Override
	public void run() {
		while(true) {
			try {
				for(String key : miniDatas.keySet()) {
					MiniResourceZipData data = miniDatas.get(key);
					data.heartbeat();
				}
				Thread.sleep(60000);
			} catch(Exception e) {
				logger.error("心跳出错:", e);
			}
		}
	}
}
