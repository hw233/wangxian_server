package com.fy.gamegateway.mieshi.waigua;

import com.fy.gamegateway.stat.StatManager;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.text.StringUtil;

public class SessionManager {
	
	//在检查Session的时候是否检查服务器是不是正在维护
	public static boolean isSessionServerWeiHu = true;
	
	public static SessionManager instance = new SessionManager();
	
	public static SessionManager getInstance () {
		return instance;
	}
	
	public LruMapCache sessionCache = new LruMapCache(2000000, 30 * 60 * 1000L, false, "sessionCache");
	
	public Session getSession (String sid) {
		return (Session)sessionCache.get(sid);
	}
	
	/**
	 * 校验
	 * 返回为 1 不存在或已经过期
	 *       2 用户名不匹配
	 *       3 clientID不匹配
	 *       0 为正常
	 * @param sid
	 * @param username
	 * @param clientId
	 * @return
	 */
	public int validateSession (String sid, String username, String clientId) {
//		Session s = getSession(sid);
//		if (s == null) {
//			return 1;
//		}
//		if (!s.getUsername().equals(username)) {
//			return 2;
//		}
//		if (!s.getClientID().equals(clientId)) {
//			return 3;
//		}
//		if (System.currentTimeMillis() - s.getCreateTime() > 30 * 60 * 1000L) {
//			return 1;
//		}

		
		return 0;
	}
	
	/**
	 * @param username
	 * @param clientId
	 * @return
	 */
	public Session createSession (String username, String clientId) {
		Session s = new Session();
		s.setUsername(username);
		s.setClientID(clientId);
		s.setCreateTime(System.currentTimeMillis());
		s.setSessionID(StringUtil.randomString(32));
		sessionCache.put(s.getSessionID(), s);
		return s;
	}
	
}
