package com.fy.engineserver.levelExpTag;

public enum ActivityType {

	
	/**
	 * 家族建设任务	神农	   平定四方	国内寻宝	国内仙缘	国内论道	国外寻宝	国外仙缘	国外论道	
	 * 国外刺探	国外宝藏	经验挂机副本	家族经验副本	装备挂机副本	宠物捕捉副本
	 */
	家族建设任务((byte)0),
	神农((byte)1),
	平定四方((byte)2),
	国内寻宝((byte)3),
	国内仙缘((byte)4),
	国内论道((byte)5),
	国外寻宝((byte)6),
	国外仙缘((byte)7),
	国外论道((byte)8),
	国外刺探((byte)9),
	国外宝藏((byte)10),
	经验挂机副本((byte)11),
	家族经验副本((byte)12),
	装备挂机副本((byte)13),
	宠物捕捉副本((byte)14);
	
	public byte type;
	ActivityType(byte type){
		this.type = type;
	}
	
}
