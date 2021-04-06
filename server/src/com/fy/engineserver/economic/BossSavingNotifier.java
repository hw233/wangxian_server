package com.fy.engineserver.economic;

/**
 *
 * 
 * @version 创建时间：May 30, 2012 10:59:16 AM
 * 
 */
public interface BossSavingNotifier {
	
	/**
	 * 通知用户完成了充值
	 * @param username	用户名
	 * @param money	充值额度，单位：分
	 * @param platform	平台
	 * @param medium	介质
	 */
	public void notifySaving(String username, long money, String platform, String medium) throws Exception;

}
