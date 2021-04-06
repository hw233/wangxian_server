package com.fy.engineserver.datasource.skill2;

/**
 * 
 * create on 2013年9月3日
 */
public class BuffCondition {
	
	public static final int CON_ATK_TIMES = 1;
	public static final int CON_RATE_POST_HURT = 2;
	public static final int CON_RATE_WHEN_ATK= 3;
	
	public int conditionType = 0;
	public int paramInt;
	
	public BuffCondition() {
		super();
	}

	public BuffCondition(int type, int paramInt){
		conditionType = type;
		if(paramInt <= 0 && type == CON_ATK_TIMES){
			paramInt = 1985;//防止为0导致不可预料的后果。
		}
		this.paramInt = paramInt;
	}

}
