/**
 * 
 */
package com.fy.boss.platform.qq;

import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.fy.boss.platform.qq.QQUser;
import com.fy.boss.platform.qq.QQUserCenter;

/**
 * @author Administrator
 *
 */
public class QQUserCenter {

	private HashMap<String, QQUser> users=new HashMap<String, QQUser>();
	
	private Logger logger = Logger.getLogger("com.fy.boss.platform.qq.QQUserCenter");
	
	private static QQUserCenter self;
	
	public static QQUserCenter getInstance() {
		if(self != null){
			return self;
		}
		synchronized(QQUserCenter.class){
			if(self != null) return self;
			self = new QQUserCenter();
		}
		return self;
	}
	
	public synchronized void addUser(QQUser user){
		this.users.put(user.getUid(), user);
		if(logger.isInfoEnabled()){
			logger.info("[添加腾讯用户] [UID："+user.getUid()+"] [SID："+user.getSid()+"] [登录时间："+new Date(user.getLoginTime())+"]");
		}
	}
	
	public QQUser getUser(String uid){
		return this.users.get(uid);
	}

}
