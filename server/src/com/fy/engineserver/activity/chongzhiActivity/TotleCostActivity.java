/**
 * 
 */
package com.fy.engineserver.activity.chongzhiActivity;

/**
 * @author Administrator
 *
 */
public class TotleCostActivity {

	private int hasChargeRmb;
	private int needChargeRmb;
	private long [] ids = new long[0];
	private int [] nums = new int[0];
	private int [] colors = new int[0];
	private String showIcon = "";
	private long endTime;
	
	
	public int[] getColors() {
		return colors;
	}
	public void setColors(int[] colors) {
		this.colors = colors;
	}
	public int getHasChargeRmb() {
		return hasChargeRmb;
	}
	public void setHasChargeRmb(int hasChargeRmb) {
		this.hasChargeRmb = hasChargeRmb;
	}
	public int getNeedChargeRmb() {
		return needChargeRmb;
	}
	public void setNeedChargeRmb(int needChargeRmb) {
		this.needChargeRmb = needChargeRmb;
	}
	public long[] getIds() {
		return ids;
	}
	public void setIds(long[] ids) {
		this.ids = ids;
	}
	public int[] getNums() {
		return nums;
	}
	public void setNums(int[] nums) {
		this.nums = nums;
	}
	public String getShowIcon() {
		return showIcon;
	}
	public void setShowIcon(String showIcon) {
		this.showIcon = showIcon;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	
}
