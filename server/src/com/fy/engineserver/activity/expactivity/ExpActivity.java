package com.fy.engineserver.activity.expactivity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 经验活动配置.
 * 
 */
public abstract class ExpActivity extends BaseActivityInstance{

	private String name;// 活动名字-刷页面删除的依据

	private double expRate;// 经验倍数
	private Set<String> limitmaps = new HashSet<String>();
	
	public ExpActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getExpRate() {
		return expRate;
	}

	public void setExpRate(double expRate) {
		this.expRate = expRate;
	}

	public Set<String> getLimitmaps() {
		return limitmaps;
	}

	public void setLimitmaps(Set<String> limitmaps) {
		this.limitmaps = limitmaps;
	}

	/**
	 * boolean 表示是否有活动
	 * doubleValue表示经验倍数,直接乘,不是增加
	 * @param time
	 * @return
	 */
	public abstract CompoundReturn getExpActivityMultiple(Calendar calendar);

}
