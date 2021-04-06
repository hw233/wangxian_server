package com.fy.engineserver.util.security;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;

/**
 * ip限制
 * 
 * 
 */
public class IpPermitManager {

	private static List<IpPermit> permits = new ArrayList<IpPermit>();

	static {
		addIpPermit(new IpPermit(Platform.官方, new String[] { "106.120.222.214" }));
	}

	public static boolean hasPermition(HttpServletRequest request) {
		Platform currentPlatform = PlatformManager.getInstance().getPlatform();
		IpPermit ipPermit = getIpPermit(currentPlatform);
		if (ipPermit == null) {
			EnterLimitManager.logger.warn("[检查IP访问权限] [平台无任何配置] [" + currentPlatform.toString() + "]");
			return false;
		}
		String requestIp = request.getRemoteAddr();
		if (ipPermit.getAllowIpList().contains(requestIp)) {
			return true;
		}
		return false;
	}

	public static IpPermit getIpPermit(Platform platform) {
		for (IpPermit permit : permits) {
			if (permit.getPlatform().equals(platform)) {
				return permit;
			}
		}
		return null;
	}

	/**
	 * 增加一个新的规则
	 * @param ipPermit
	 */
	public static synchronized void addIpPermit(IpPermit newIpPermit) {
		Platform platform = newIpPermit.getPlatform();
		boolean found = false;
		for (IpPermit permit : permits) {
			if (permit.getPlatform().equals(platform)) {
				found = true;
				permit.getAllowIpList().addAll(newIpPermit.getAllowIpList());
				EnterLimitManager.logger.warn("[新增限制规则] [原来有相同平台] [将Ip加到列表] [" + newIpPermit.toString() + "]");
				break;
			}
		}
		if (!found) {
			permits.add(newIpPermit);
			EnterLimitManager.logger.warn("[新增限制规则] [新增平台] [添加到限制列表] [" + newIpPermit.toString() + "]");
		}
	}

	public static synchronized boolean removeIpPermit(Platform platform) {
		Iterator<IpPermit> iterator = permits.iterator();
		if (iterator.hasNext()) {
			IpPermit ipPermit = iterator.next();
			if (ipPermit.getPlatform().equals(platform)) {
				iterator.remove();
				EnterLimitManager.logger.warn("[删除限制规则] [成功] [" + ipPermit.toString() + "]");
				return true;
			}
		}
		EnterLimitManager.logger.warn("[删除限制规则] [失败] [不存在的平台配置] [" + platform.toString() + "]");
		return false;
	}
}