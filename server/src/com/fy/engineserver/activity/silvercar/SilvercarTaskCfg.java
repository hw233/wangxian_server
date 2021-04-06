package com.fy.engineserver.activity.silvercar;

/**
 * 押镖任务配置
 * 
 */
public class SilvercarTaskCfg {
	/** 任务名 */
	private String taskName;
	/** 要扣除的道具 */
	private String needArticleName;
	/** 要扣除的道具颜色 */
	private int needArticleColor;
	/** 要扣除的银子 */
	private int needMoney;

	public SilvercarTaskCfg(String taskName, String needArticleName, int needArticleColor, int needMoney) {
		this.taskName = taskName;
		this.needArticleName = needArticleName;
		this.needArticleColor = needArticleColor;
		this.needMoney = needMoney;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getNeedArticleName() {
		return needArticleName;
	}

	public void setNeedArticleName(String needArticleName) {
		this.needArticleName = needArticleName;
	}

	public int getNeedArticleColor() {
		return needArticleColor;
	}

	public void setNeedArticleColor(int needArticleColor) {
		this.needArticleColor = needArticleColor;
	}

	public int getNeedMoney() {
		return needMoney;
	}

	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}

	@Override
	public String toString() {
		return "SilvercarTaskCfg [taskName=" + taskName + ", needArticleName=" + needArticleName + ", needArticleColor=" + needArticleColor + ", needMoney=" + needMoney + "]";
	}
}
