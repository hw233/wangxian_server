package com.fy.engineserver.jiazu2.model;

public class JiazuRenwuModel {
	/** 任务id */
	private long taskId;
	/** 增加修炼值数量 */
	private int addXiulian;
	/** 增加家族资金数量 */
	private long addJiazuZiyuan;
	/** 星级 */
	private int star;
	
	@Override
	public String toString() {
		return "JiazuRenwuModel [taskId=" + taskId + ", addXiulian=" + addXiulian + ", addJiazuZiyuan=" + addJiazuZiyuan + "]";
	}
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public int getAddXiulian() {
		return addXiulian;
	}
	public void setAddXiulian(int addXiulian) {
		this.addXiulian = addXiulian;
	}
	public long getAddJiazuZiyuan() {
		return addJiazuZiyuan;
	}
	public void setAddJiazuZiyuan(long addJiazuZiyuan) {
		this.addJiazuZiyuan = addJiazuZiyuan;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	
	
}
