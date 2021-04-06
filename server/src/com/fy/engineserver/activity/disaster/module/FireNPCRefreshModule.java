package com.fy.engineserver.activity.disaster.module;
/**
 * 火圈npc刷新模板
 */
public class FireNPCRefreshModule {
	/** npc模板id */
	private int categoryId;
	/** 刷新坐标 */
	private int[] point;
	
	public FireNPCRefreshModule(int categoryId, int[] point) {
		super();
		this.categoryId = categoryId;
		this.point = point;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int[] getPoint() {
		return point;
	}
	public void setPoint(int[] point) {
		this.point = point;
	}
}
