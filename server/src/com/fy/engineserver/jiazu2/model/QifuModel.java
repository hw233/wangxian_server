package com.fy.engineserver.jiazu2.model;

import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.util.CompoundReturn;

public class QifuModel {
	/** 祈福等级 */
	private int qifuLevel;
	/** 消耗银子类型  （0工资   1银子 2绑银） */
	private byte costType;
	/** 消耗银子数量 */
	private long costNum;
	/** 增加家族修炼值 */
	private int addNum;
	/**增加家族资金*/
	private int addJiazuZiyuan;
	
	@Override
	public String toString() {
		return "QifuModel [qifuLevel=" + qifuLevel + ", costType=" + costType + ", costNum=" + costNum + ", addNum=" + addNum + "]";
	}
	public int getQifuLevel() {
		return qifuLevel;
	}
	public void setQifuLevel(int qifuLevel) {
		this.qifuLevel = qifuLevel;
	}
	public byte getCostType() {
		return costType;
	}
	public void setCostType(byte costType) {
		this.costType = costType;
	}
	public long getCostNum() {
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.shangxiangActivity, this.qifuLevel);
		if (cr != null && cr.getBooleanValue() && cr.getLongValue() > 0) {
			return cr.getLongValue();
		}
		return costNum;
	}
	public void setCostNum(long costNum) {
		this.costNum = costNum;
	}
	public int getAddNum() {
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.shangxiangActivity, this.qifuLevel);
		if (cr != null && cr.getBooleanValue() && cr.getIntValue() > 0) {
			return cr.getIntValue();
		}
		return addNum;
	}
	public void setAddNum(int addNum) {
		this.addNum = addNum;
	}
	public int getAddJiazuZiyuan() {
		return addJiazuZiyuan;
	}
	public void setAddJiazuZiyuan(int addJiazuZiyuan) {
		this.addJiazuZiyuan = addJiazuZiyuan;
	}
	
	
}
