package com.fy.engineserver.datasource.article.entity.client;

public class BagInfo4Client {
	byte bagtype;
	long[] entityId = new long[0];//各个单元格放置的物体唯一标识，长度就是背包格个数，内容-1标识空格子
	short[] counts = new short[0];//各个单元格放置的物体个数，0标识没有物品
	
	short fangbaomax = 20;//防爆包最大扩充个数
	long[] fangbaoEntityId = new long[0];//背包中各个单元格放置的物体唯一标识，长度就是背包格个数，内容-1标识空格子
	short[] fangbaoCounts = new short[0];//背包中各个单元格放置的物体个数，0标识没有物品
	
	public long[] getEntityId() {
		return entityId;
	}
	public void setEntityId(long[] entityId) {
		this.entityId = entityId;
	}
	public short[] getCounts() {
		return counts;
	}
	public void setCounts(short[] counts) {
		this.counts = counts;
	}
	public byte getBagtype() {
		return bagtype;
	}
	public void setBagtype(byte bagtype) {
		this.bagtype = bagtype;
	}
	public short getFangbaomax() {
		return fangbaomax;
	}
	public void setFangbaomax(short fangbaomax) {
		this.fangbaomax = fangbaomax;
	}
	public long[] getFangbaoEntityId() {
		return fangbaoEntityId;
	}
	public void setFangbaoEntityId(long[] fangbaoEntityId) {
		this.fangbaoEntityId = fangbaoEntityId;
	}
	public short[] getFangbaoCounts() {
		return fangbaoCounts;
	}
	public void setFangbaoCounts(short[] fangbaoCounts) {
		this.fangbaoCounts = fangbaoCounts;
	}
	
	
}
