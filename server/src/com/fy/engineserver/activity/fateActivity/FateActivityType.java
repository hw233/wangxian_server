package com.fy.engineserver.activity.fateActivity;

import com.fy.engineserver.datasource.language.Translate;

public enum FateActivityType{
	
//	国内仙缘((byte)0,"国内仙缘"),
//	国外仙缘((byte)1,"国外仙缘"),
//	国内论道((byte)2,"国内论道"),
//	国外论道((byte)3,"国外论道");
	国内仙缘((byte)0,Translate.国内仙缘),
	国外仙缘((byte)1,Translate.国外仙缘),
	国内论道((byte)2,Translate.国内论道),
	国外论道((byte)3,Translate.国外论道);
	
	public byte type;
	public String name;
	private FateActivityType(byte type,String name) {
		this.type = type;
		this.name = name;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	
	
}
