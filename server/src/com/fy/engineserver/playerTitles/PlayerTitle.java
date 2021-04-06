package com.fy.engineserver.playerTitles;

import java.util.Calendar;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class PlayerTitle {

	//类型
	private int titleType;
	// 具体的称号名
	private String titleName;
	//获得称号时间 
	private long startTime;

	private transient int color;
	
	private transient String buffName;
	
	private transient int buffLevl;
	
	private transient String titleShowName;
	
	private transient String description;
	
	private transient String icon;
	/** 默认为0代表永久称号   -1代表每天24点清除的称号 */
	private transient long lastTime;
	/** 适用于需要特定时间清除称号的功能  通过获取称号的时间计算出删除称号的时间  比如每天24点清除 */
	private transient long tempLastTime;
	
	public PlayerTitle() {
		
	}
	
	public PlayerTitle(int titleType, String titleName, long startTime, int color, String buffName, int buffLevl, String titleShowName, String description, String icon, long lastTime) {
		super();
		this.titleType = titleType;
		this.titleName = titleName;
		this.startTime = startTime;
		this.color = color;
		this.buffName = buffName;
		this.buffLevl = buffLevl;
		this.titleShowName = titleShowName;
		this.description = description;
		this.icon = icon;
		this.lastTime = lastTime;
	}


	@Override
	public String toString() {
		return "PlayerTitle [titleType=" + titleType + ", titleName=" + titleName + ", startTime=" + startTime + ", color=" + color + ", buffName=" + buffName + ", buffLevl=" + buffLevl + ", titleShowName=" + titleShowName + ", description=" + description + ", icon=" + icon + ", lastTime=" + lastTime + "]";
	}

	public int getTitleType() {
		return titleType;
	}

	public void setTitleType(int titleType) {
		this.titleType = titleType;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public int getBuffLevl() {
		return buffLevl;
	}

	public void setBuffLevl(int buffLevl) {
		this.buffLevl = buffLevl;
	}

	public String getTitleShowName() {
		return titleShowName;
	}

	public void setTitleShowName(String titleShowName) {
		this.titleShowName = titleShowName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getLastTime() {
		if (lastTime == -1) {			
			if (tempLastTime <= 0) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR, 1);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				tempLastTime = c.getTime().getTime() - this.startTime;
			}
			return tempLastTime;
		}
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	
}
