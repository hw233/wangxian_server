package com.fy.engineserver.sprite.pet;

import java.util.Arrays;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "flyState" })})
public class PetFlyState {

	@SimpleId
	private long id;//petId			
	@SimpleVersion
	private int version;
	
	private int flyState;				//0:默认，1:已飞升
	private int lingXingPoint;			//灵性会清洗，潜能值不归还
	private int historyLingXingPoint;	//历史
	private int qianNengPoint;
	private int eatNums;				//易筋丹次数
	private long xiaoHuaDate;			//消化完成截至时间
	private int useTimes;				//使用升华露点次数
	private int skillId;				//领悟到的技能id
	private int tempPoint;				//临时加点
	private int animation;				//是否播放过动画,1:播过
	private int[] pointRecord = new int[5];			//加点分布
	private int[] tempPointRecord = new int[5];			//加点分布
	
	public int[] getTempPointRecord() {
		return tempPointRecord;
	}
	public void setTempPointRecord(int[] tempPointRecord) {
		this.tempPointRecord = tempPointRecord;
		PetManager.em_state.notifyFieldChange(this, "tempPointRecord");
	}
	public int[] getPointRecord() {
		return pointRecord;
	}
	public void setPointRecord(int[] pointRecord) {
		this.pointRecord = pointRecord;
		PetManager.em_state.notifyFieldChange(this, "pointRecord");
	}
	public int getAnimation() {
		return animation;
	}
	public void setAnimation(int animation) {
		this.animation = animation;
		PetManager.em_state.notifyFieldChange(this, "animation");
	}
	public int getTempPoint() {
		return tempPoint;
	}
	public void setTempPoint(int tempPoint) {
		this.tempPoint = tempPoint;
		PetManager.em_state.notifyFieldChange(this, "tempPoint");
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getFlyState() {
		return flyState;
	}
	public void setFlyState(int flyState) {
		this.flyState = flyState;
		PetManager.em_state.notifyFieldChange(this, "flyState");
	}
	public int getLingXingPoint() {
		return lingXingPoint;
	}
	public void setLingXingPoint(int lingXingPoint) {
		this.lingXingPoint = lingXingPoint;
		PetManager.em_state.notifyFieldChange(this, "lingXingPoint");
	}
	public int getQianNengPoint() {
		return qianNengPoint;
	}
	public void setQianNengPoint(int qianNengPoint) {
		this.qianNengPoint = qianNengPoint;
		PetManager.em_state.notifyFieldChange(this, "qianNengPoint");
	}
	public long getXiaoHuaDate() {
		return xiaoHuaDate;
	}
	public void setXiaoHuaDate(long xiaoHuaDate) {
		this.xiaoHuaDate = xiaoHuaDate;
		PetManager.em_state.notifyFieldChange(this, "xiaoHuaDate");
	}
	public int getUseTimes() {
		return useTimes;
	}
	public void setUseTimes(int useTimes) {
		this.useTimes = useTimes;
		PetManager.em_state.notifyFieldChange(this, "useTimes");
	}
	public int getEatNums() {
		return eatNums;
	}
	public void setEatNums(int eatNums) {
		this.eatNums = eatNums;
		PetManager.em_state.notifyFieldChange(this, "eatNums");
	}
	
	public int getHistoryLingXingPoint() {
		return historyLingXingPoint;
	}
	public void setHistoryLingXingPoint(int historyLingXingPoint) {
		this.historyLingXingPoint = historyLingXingPoint;
		PetManager.em_state.notifyFieldChange(this, "historyLingXingPoint");
	}
	
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
		PetManager.em_state.notifyFieldChange(this, "skillId");
	}
	@Override
	public String toString() {
		return "[animation=" + animation + ", eatNums=" + eatNums
				+ ", flyState=" + flyState + ", historyLingXingPoint="
				+ historyLingXingPoint + ", id=" + id + ", lingXingPoint="
				+ lingXingPoint + ", pointRecord="
				+ Arrays.toString(pointRecord) + ", qianNengPoint="
				+ qianNengPoint + ", skillId=" + skillId + ", tempPoint="
				+ tempPoint + ", useTimes=" + useTimes + ", xiaoHuaDate=" + xiaoHuaDate + "]";
	}
	
	
}
