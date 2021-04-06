package com.fy.engineserver.dajing;
import java.util.*;

public class DajingStudio implements java.io.Serializable{

	private static final long serialVersionUID = 8739905302836469074L;

	public Date createTime = new Date();

	//
	public long[] statData = new long[DajingStudioManager.STATDATANAMES.length]; 
	
	//每天产出限制表
	public long[] canchuData = new long[DajingStudioManager.工作室产出限制名称.length];
	public long lastCanChuTime = System.currentTimeMillis();
	//最后一次激活时间，是指有成员进行了统计内的行为，包括打怪，古董，交易。
	public Date lastActiveTime = new Date();
	public String ip = "";
	public ArrayList<DajingGroup> groupList = new ArrayList<DajingGroup>();
	
	public DajingGroup getDajingGroup(String phoneType){
		for(int i = 0 ; i < groupList.size() ; i++){
			DajingGroup dg = groupList.get(i);
			if(dg.phoneType.equals(phoneType)){
				return dg;
			}
		}
		return null;
	}
}
