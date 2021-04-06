package com.fy.engineserver.seal.data;

import com.fy.engineserver.seal.SealManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 封印信息
 * 
 *
 */
@SimpleEntity
@SimpleIndices({
})
public class SealTaskInfo {

	//封印级别，封印阶段，该阶段boss拥有的buff，材料捐献的点数
	
	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	private int sealLevel;
	private int sealStep;
	private int buffLevel;
	private long points;	//190级的第1阶段单纯的收集点数
	private int bossid;	
	private int sealType;	//同等级，同阶段的区分.220级第一阶段,1,2
	private long sealTaskStartTime;		//破封任务开启时间
	private long bossRefreshTime;		//boss刷新时间
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
	public int getSealLevel() {
		return sealLevel;
	}
	public void setSealLevel(int sealLevel) {
		this.sealLevel = sealLevel;
		SealManager.getInstance().em_info.notifyFieldChange(this, "sealLevel");
	}
	public int getSealStep() {
		return sealStep;
	}
	public void setSealStep(int sealStep) {
		this.sealStep = sealStep;
		SealManager.getInstance().em_info.notifyFieldChange(this, "sealStep");
	}
	public long getPoints() {
		return points;
	}
	public void setPoints(long points) {
		this.points = points;
		SealManager.getInstance().em_info.notifyFieldChange(this, "points");
	}
	public int getBuffLevel() {
		return buffLevel;
	}
	public void setBuffLevel(int buffLevel) {
		this.buffLevel = buffLevel;
		SealManager.getInstance().em_info.notifyFieldChange(this, "buffLevel");
	}
	public int getBossid() {
		return bossid;
	}
	public void setBossid(int bossid) {
		this.bossid = bossid;
		SealManager.getInstance().em_info.notifyFieldChange(this, "bossid");
	}
	public int getSealType() {
		return this.sealType;
	}
	public void setSealType(int sealType) {
		this.sealType = sealType;
		SealManager.getInstance().em_info.notifyFieldChange(this, "sealType");
	}
	public long getSealTaskStartTime() {
		return this.sealTaskStartTime;
	}
	public void setSealTaskStartTime(long sealTaskStartTime) {
		this.sealTaskStartTime = sealTaskStartTime;
		SealManager.getInstance().em_info.notifyFieldChange(this, "sealTaskStartTime");
	}
	public long getBossRefreshTime() {
		return this.bossRefreshTime;
	}
	public void setBossRefreshTime(long bossRefreshTime) {
		this.bossRefreshTime = bossRefreshTime;
		SealManager.getInstance().em_info.notifyFieldChange(this, "bossRefreshTime");
	}
	@Override
	public String toString() {
		return "SealTaskInfo [id=" + this.id + ", sealLevel=" + this.sealLevel + ", sealStep=" + this.sealStep + ", buffLevel=" + this.buffLevel + ", points=" + this.points + ", bossid=" + this.bossid + ", sealType=" + this.sealType + ", sealTaskStartTime=" + this.sealTaskStartTime + ", bossRefreshTime=" + this.bossRefreshTime + "]";
	}
	
}
