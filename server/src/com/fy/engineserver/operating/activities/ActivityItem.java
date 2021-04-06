/**
 * 
 */
package com.fy.engineserver.operating.activities;

import com.fy.engineserver.core.ExperienceManager;


/**
 * @author Administrator
 *
 */
public class ActivityItem {

	//活动标题
    protected String startMapName = "";
    
    protected int startX;
    
    protected int startY;
    /**
     * 是否需要寻路
     */
    boolean needPathfinding;
    /**
     * 文字颜色
     */
    int textColor;
    
    long activityId;
    
    /**
     * 活动详情
     */
    String detail;
    
    String title;
    
    String time;
    
    /**
     * 这个活动的权重
     */
    int weight;
    
    /**
     * 阵营限制
     */
    int camp;
    
    /**
     * 最低等级限制
     */
    int minimalLevelLimit;
    
    /**
     * 最高等级限制
     */
    int maximalLevelLimit=ExperienceManager.getInstance().maxLevel;
    
    /**
	 * 
	 */
	public ActivityItem() {
		// TODO Auto-generated constructor stub
	}

    public String getStartMapName() {
        return startMapName;
    }

    public void setStartMapName(String startMapName) {
        this.startMapName = startMapName;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public boolean isNeedPathfinding() {
		return needPathfinding;
	}

	public void setNeedPathfinding(boolean needPathfinding) {
		this.needPathfinding = needPathfinding;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getCamp() {
		return camp;
	}

	public void setCamp(int camp) {
		this.camp = camp;
	}

	public int getMinimalLevelLimit() {
		return minimalLevelLimit;
	}

	public void setMinimalLevelLimit(int minimalLevelLimit) {
		this.minimalLevelLimit = minimalLevelLimit;
	}

	public int getMaximalLevelLimit() {
		return maximalLevelLimit;
	}

	public void setMaximalLevelLimit(int maximalLevelLimit) {
		this.maximalLevelLimit = maximalLevelLimit;
	}

	
}
