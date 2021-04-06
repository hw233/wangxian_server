package com.fy.engineserver.activity.dailyTurnActivity.model;

import java.util.Arrays;

public class TurnModel4Client {
	/** 转盘名 */
	private String turnName;
	/** 转盘id */
	private int turnId;
	/** 转盘包含物品id列表 */
	private long[] entityIds;
	/** 物品获取状态  0未获取  1已经获取到 */
	private int[] entityStatus;
	/** 物品数量 */
	private int[] entityNums;
	/** 获取抽取次数条件 */
	private String[] conditions;
	/** 抽取次数获取状态 0未获取   1获取未用   2获取已用  3未获取可购买 */
	private int[] conditionStatus;
	/** 公告内容 */
	private String desscription;
	/** 按钮文字 */
	private String btnStr;
	
	public TurnModel4Client () {
		
	}
	
	public TurnModel4Client(String turnName, int turnId, long[] entityIds, int[] entityStatus, int[] entityNums, String[] conditions, int[] conditionStatus, String desscription, String btnStr) {
		super();
		this.turnName = turnName;
		this.turnId = turnId;
		this.entityIds = entityIds;
		this.entityStatus = entityStatus;
		this.entityNums = entityNums;
		this.conditions = conditions;
		this.conditionStatus = conditionStatus;
		this.desscription = desscription;
		this.btnStr = btnStr;
	}
	
	@Override
	public String toString() {
		return "TurnModel4Client [turnName=" + turnName + ", turnId=" + turnId + ", entityIds=" + Arrays.toString(entityIds) + ", entityStatus=" + Arrays.toString(entityStatus) + ", entityNums=" + Arrays.toString(entityNums) + ", conditions=" + Arrays.toString(conditions) + ", conditionStatus=" + Arrays.toString(conditionStatus) + ", desscription=" + desscription + ", btnStr=" + btnStr + "]";
	}

	public String getTurnName() {
		return turnName;
	}
	public void setTurnName(String turnName) {
		this.turnName = turnName;
	}
	public int getTurnId() {
		return turnId;
	}
	public void setTurnId(int turnId) {
		this.turnId = turnId;
	}
	public long[] getEntityIds() {
		return entityIds;
	}
	public void setEntityIds(long[] entityIds) {
		this.entityIds = entityIds;
	}
	public int[] getEntityNums() {
		return entityNums;
	}
	public void setEntityNums(int[] entityNums) {
		this.entityNums = entityNums;
	}
	public String[] getConditions() {
		return conditions;
	}
	public void setConditions(String[] conditions) {
		this.conditions = conditions;
	}
	public String getDesscription() {
		return desscription;
	}
	public void setDesscription(String desscription) {
		this.desscription = desscription;
	}
	public String getBtnStr() {
		return btnStr;
	}
	public void setBtnStr(String btnStr) {
		this.btnStr = btnStr;
	}

	public int[] getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(int[] entityStatus) {
		this.entityStatus = entityStatus;
	}

	public int[] getConditionStatus() {
		return conditionStatus;
	}

	public void setConditionStatus(int[] conditionStatus) {
		this.conditionStatus = conditionStatus;
	}
	
	
}
