package com.fy.engineserver.sprite;

import java.util.Calendar;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class PropsUseRecord {

	private String propsName;
	private long lastUseTime;
	private byte useNum;

	public PropsUseRecord() {
		
	}

	public PropsUseRecord(String propsName, long lastUseTime, byte useNum) {
		super();
		this.propsName = propsName;
		this.setLastUseTime(lastUseTime);
		this.useNum = useNum;
	}

	public String getPropsName() {
		return propsName;
	}

	public long getLastUseTime() {
		return lastUseTime;
	}


	public byte getUseNum() {
		return useNum;
	}

	/**
	 * 
	 * @param maxNum   这个物品的最大使用次数
	 * @return
	 */
	public boolean canUseProps(int maxNum) {
		
		long now = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(now);
		
		int y1 = cal.get(Calendar.YEAR);
		int d1 = cal.get(Calendar.DAY_OF_YEAR);
		/**
		 * 每周日00点重新计算
		 */
		int week1 = cal.get(Calendar.WEEK_OF_YEAR);
		
		cal.setTimeInMillis(this.getLastUseTime());
		int y2 = cal.get(Calendar.YEAR);
		int d2 = cal.get(Calendar.DAY_OF_YEAR);
		
		if(y1 != y2 || d1 != d2){
			useNum = 0;
		}
		if (getUseNum() >= maxNum) {
			return false;
		} else {
			return true;
		}
	}
	
	
	public int todayUseTime(){
		long now = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(now);
		
		int y1 = cal.get(Calendar.YEAR);
		int d1 = cal.get(Calendar.DAY_OF_YEAR);
		
		cal.setTimeInMillis(this.getLastUseTime());
		int y2 = cal.get(Calendar.YEAR);
		int d2 = cal.get(Calendar.DAY_OF_YEAR);
		
		if(y1 != y2 || d1 != d2){
			return 0;
		}else{
			return this.getUseNum();
		}
	}
	

	public void useProps() {
		useNum++;
		this.setLastUseTime(System.currentTimeMillis());
	}

	public void setLastUseTime(long lastUseTime) {
		this.lastUseTime = lastUseTime;
	}
}
