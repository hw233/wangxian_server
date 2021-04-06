package com.fy.engineserver.carbon;

import java.util.List;

public interface CarbonMgIns {
	public void doAct();
	/** 刷新时间规则(目前只写了每天刷新的，如果以后又需求再添加) */
	public byte getActType();
	/** 得到刷新小时 */
	public List<Integer> getActHour();
	/** 多长时间内通知 */
	public int maxNotifyMinits();
	/** 开启前多长时间需要通知(单位分钟，做开启前系统通知等操作用) */
	public int preNotifyMinits();
	/** 开启前执行方法 */
	public void doPreAct();
}
