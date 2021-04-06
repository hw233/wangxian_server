package com.xuanzhi.tools.objectdb.entity;

import javax.persistence.*;

@Embeddable
public class EquipColumn {

	public static final int EC_SIZE = 10;
	
	public static final int WEAPON = 0;
	public static final int HEAD = 1;
	public static final int SHOUDER = 2;
	public static final int BODY = 3;
	
	private long equipEntityId[];
	
	public EquipColumn(){
		equipEntityId = new long[EC_SIZE];
	}

	public long[] getEquipEntityId() {
		return equipEntityId;
	}

	public void setEquipEntityId(long[] equipEntityId) {
		this.equipEntityId = equipEntityId;
	}
	
	
}
