package com.fy.engineserver.septbuilding.service;

import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.sprite.Player;

public interface BuildAble {
	/** 升级 
	 * @throws Exception */
	boolean levelUp(SeptStation station,long NPCId) throws Throwable;

	/** 降级 */
	boolean levelDown(SeptStation station,long NPCId, boolean confirm, Player p) throws Throwable;

	/** 拆除  (保留,暂时无此需求)*/
	boolean destroy(SeptStation station,long NPCId) throws Throwable;
	
	/** 是否可升级[暂未实现] */
	boolean canLvUp(SeptStation station);

	/** 建造 */
	boolean build(SeptStation station, long npcId, int buildType) throws Throwable;
}
