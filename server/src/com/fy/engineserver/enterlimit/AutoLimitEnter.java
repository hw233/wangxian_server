package com.fy.engineserver.enterlimit;

import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData;

/**
 * 自动封号
 * 
 * 
 */
public abstract class AutoLimitEnter {

	/**
	 * 获得自动封号的描述
	 * @return
	 */
	public abstract String getDes();

	/**
	 * 获得自动封号的名字
	 * @return
	 */
	public abstract String getName();

	public abstract boolean needLimitEnter(PlayerRecordData playerRecordData);

}
