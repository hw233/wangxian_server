package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;
import java.util.List;

public class AddPlan {

	/**
	 * 方案名
	 */
	private String planName;
	
	/**
	 * 装载和卸下要修改的属性及其值
	 * 第一维为级别 一般为4级: 4 8 12 16级
	 * 第二维为某个特定级别，对玩家属性的修改
	 */
	private IntProperty ips[][];

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public IntProperty[][] getIps() {
		return ips;
	}

	public void setIps(IntProperty[][] ips) {
		this.ips = ips;
	}

	public IntProperty[] getIntPropertysByLevel(int level){
		if(this.ips != null){
			List<IntProperty> newIps = new ArrayList<IntProperty>();
			if(level >= 4){
				for(IntProperty ip : ips[0]){
					newIps.add(ip);
				}
			}
			if(level >= 8){
				for(IntProperty ip : ips[1]){
					newIps.add(ip);
				}
			}
			if(level >= 12){
				for(IntProperty ip : ips[2]){
					newIps.add(ip);
				}
			}
			if(level >= 16){
				for(IntProperty ip : ips[3]){
					newIps.add(ip);
				}
			}
			return newIps.toArray(new IntProperty[0]);
		}
		return new IntProperty[0];
	}
}
