package com.fy.engineserver.activity.xianling;

public class TimedTask {
	/** 限时任务id */
	private int taskId;
	/** 所属大类 */
	private byte type;
	/** 目标id */
	private int monsterCategoryId;
	/** 怪物头像 */
	private String monsterIcon;
	/** 任务描述 */
	private String des;

	// /** 奖励道具 */
	// private ActivityProp[] prize;
	// /** 奖励临时物品id,读表时创建出来 */
	// private long[] tempIds;
	// /** 奖励数量,读表时创建出来 */
	// private int[] nums;
	// /** 刷到的概率 */
	// private int rate;

	public int getTaskId() {
		return taskId;
	}

	public TimedTask(int taskId, byte type, int monsterCategoryId, String monsterIcon, String des) {
		super();
		this.taskId = taskId;
		this.type = type;
		this.monsterCategoryId = monsterCategoryId;
		this.monsterIcon = monsterIcon;
		this.des = des;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getMonsterCategoryId() {
		return monsterCategoryId;
	}

	public String getMonsterIcon() {
		return monsterIcon;
	}

	public void setMonsterIcon(String monsterIcon) {
		this.monsterIcon = monsterIcon;
	}

	public void setMonsterCategoryId(int monsterCategoryId) {
		this.monsterCategoryId = monsterCategoryId;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

}
