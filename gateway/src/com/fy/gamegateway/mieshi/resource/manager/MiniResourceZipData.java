package com.fy.gamegateway.mieshi.resource.manager;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

import com.xuanzhi.tools.servlet.HttpUtils;

public class MiniResourceZipData implements Serializable {
	
	private static final long serialVersionUID = 1166503546093776420L;

	public static String DEFAULT_DOWN_CHANNEL = "YOUAI_XUNXIAN";
	public static String SPACE = ",";

	private String lastResourceVersion;			//对应的上个版本
	private String nowResourceVersion;			//对应的当前版本
	
	private long[][] zipSizes = new long[ResourceMD5.GPU_NAMES.length][ResourceMD5.PLATFORM_NAMES.length];
	
	//这个小包中每个资源所对应的小版本提升
	private HashMap<String, Integer> resourceSingleVersion = new HashMap<String, Integer>();
	
	//下载地址			渠道,如果找不到就用咱们渠道的
	private HashMap<String, String> downloadUrl = new HashMap<String, String>();
	private HashMap<String, Boolean> downloadEnable = new HashMap<String, Boolean>();

	//心跳检查下载链接是否可以正确链接
	public void heartbeat() {
		for (String key : downloadUrl.keySet()) {
			String url = downloadUrl.get(key);
			if (url.length() > 0) {
				try {
					HashMap headers = new HashMap();
					HttpUtils.webHead(new URL(url), headers, 1000, 1000);
					Integer integer = (Integer)headers.get(HttpUtils.Response_Code);
					if(integer == 200){
						downloadEnable.put(key, Boolean.TRUE);
					}else {
						downloadEnable.put(key, Boolean.FALSE);
						MiniResourceZipManager.logger.error("检查Mini资源出现异常 ["+integer+"] ["+key+"] ["+url+"]");
					}
				} catch(Exception e) {
					MiniResourceZipManager.logger.error("检查Mini资源出现异常 ["+key+"] ["+url+"]", e);
				}
			}else {
				downloadEnable.put(key, Boolean.FALSE);
			}
		}
	}
	
	public void setSize(int plat, int gpu, long size) {
		zipSizes[gpu][plat] = size;
	}
	
	public long getSize(String gpu, String platform) {
		int gpuIndex = -1;
		int platformIndex = -1;
		for (int i = 0; i < ResourceMD5.GPU_NAMES.length; i++) {
			if (ResourceMD5.GPU_NAMES[i].equals(gpu)) {
				gpuIndex = i;
				break;
			}
		}
		for (int j = 0; j < ResourceMD5.PLATFORM_NAMES.length; j++) {
			if (ResourceMD5.PLATFORM_NAMES[j].equalsIgnoreCase(platform)) {
				platformIndex = j;
				break;
			}
		}
//		MiniResourceZipManager.logger.warn("[getSize] ["+gpu+"] ["+gpuIndex+"] ["+platform+"] ["+platformIndex+"]");
		if (gpuIndex >= 0 && platformIndex >= 0) {
			return zipSizes[gpuIndex][platformIndex];
		}
		return 0L;
	}
	
	public String createKey (String channel, String platform, String gpu) {
		return channel+SPACE+platform.toLowerCase()+SPACE+gpu;
	}
	
	public String getDownUrl (String channel, String platform, String gpu) {
		String realChannel = MiniResourceZipManager.cTOc.get(channel);
		if (realChannel == null) {
			realChannel = channel;
		}
		Boolean enable = downloadEnable.get(createKey(realChannel, platform, gpu));
		if (enable != null && enable.booleanValue() == true) {
			return downloadUrl.get(createKey(realChannel, platform, gpu));
		}
		if (!realChannel.equals(channel)) {
			enable = null;
			enable = downloadEnable.get(createKey(channel, platform, gpu));
			if (enable != null && enable.booleanValue() == true) {
				return downloadUrl.get(createKey(channel, platform, gpu));
			}
		}
		enable = null;
		enable = downloadEnable.get(createKey(DEFAULT_DOWN_CHANNEL, platform, gpu));
		if (enable != null && enable.booleanValue() == true) {
			return downloadUrl.get(createKey(DEFAULT_DOWN_CHANNEL, platform, gpu));
		}
		return "";
	}
	
	public void addChannel (String channel) {
		for (int i = 0; i < ResourceMD5.GPU_NAMES.length; i++) {
			for (int j = 0; j < ResourceMD5.PLATFORM_NAMES.length; j++) {
				downloadUrl.put(createKey(channel, ResourceMD5.PLATFORM_NAMES[j], ResourceMD5.GPU_NAMES[i]), "");
				downloadEnable.put(createKey(channel, ResourceMD5.PLATFORM_NAMES[j], ResourceMD5.GPU_NAMES[i]), Boolean.FALSE);
			}
		}
	}
	
	public void addOneChannel (String channel, String gpu, String plat, String url) {
		downloadUrl.put(createKey(channel, plat, gpu), url);
		downloadEnable.put(createKey(channel, plat, gpu), Boolean.FALSE);
	}
	
	public void setLastResourceVersion(String lastResourceVersion) {
		this.lastResourceVersion = lastResourceVersion;
	}

	public String getLastResourceVersion() {
		return lastResourceVersion;
	}

	public void setNowResourceVersion(String nowResourceVersion) {
		this.nowResourceVersion = nowResourceVersion;
	}

	public String getNowResourceVersion() {
		return nowResourceVersion;
	}

	public void setResourceSingleVersion(HashMap<String, Integer> resourceSingleVersion) {
		this.resourceSingleVersion = resourceSingleVersion;
	}

	public HashMap<String, Integer> getResourceSingleVersion() {
		return resourceSingleVersion;
	}

	public void setDownloadUrl(HashMap<String, String> downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public HashMap<String, String> getDownloadUrl() {
		return downloadUrl;
	}
	
	public HashMap<String, Boolean> getDownloadEnable() {
		return downloadEnable;
	}

	public void setDownloadEnable(HashMap<String, Boolean> downloadEnable) {
		this.downloadEnable = downloadEnable;
	}

	public void setZipSizes(long[][] zipSizes) {
		this.zipSizes = zipSizes;
	}

	public long[][] getZipSizes() {
		return zipSizes;
	}
}
