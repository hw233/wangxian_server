package com.fy.engineserver.util.security;

import java.util.*;

import com.fy.engineserver.platform.PlatformManager.Platform;

/**
 * 每个平台限制IP地址
 * 
 * 
 */
public class IpPermit {

	private Platform platform;

	public Set<String> allowIpList = new HashSet<String>();

	public IpPermit(Platform platform, String[] allowIpList) {
		super();
		this.platform = platform;
		for (String ip : allowIpList) {
			this.allowIpList.add(ip);
		}
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public Set<String> getAllowIpList() {
		return allowIpList;
	}

	public void setAllowIpList(Set<String> allowIpList) {
		this.allowIpList = allowIpList;
	}

	@Override
	public String toString() {
		return "IpPermit [platform=" + platform + ", allowIpList=" + allowIpList + "]";
	}

}
