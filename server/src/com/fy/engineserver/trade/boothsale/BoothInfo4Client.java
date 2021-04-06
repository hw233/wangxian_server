package com.fy.engineserver.trade.boothsale;

public class BoothInfo4Client {

	long[] entityId = new long[0];// 各个单元格放置的物体唯一标识，长度就是背包格个数，内容-1标识空格子
	int[] counts = new int[0];// 各个单元格放置的物体个数，0标识没有物品
	long[] perPrice = new long[0];// 单价
	int[] knapType = new int[0];			//对应物品在原包裹类型
	int[] knapIndex = new int[0];			//对应物品在原包裹位置
	public BoothInfo4Client() {
		
	}

	public long[] getPerPrice() {
		return perPrice;
	}

	public void setPerPrice(long[] perPrice) {
		this.perPrice = perPrice;
	}

	public long[] getEntityId() {
		return entityId;
	}

	public void setEntityId(long[] entityId) {
		this.entityId = entityId;
	}

	public int[] getCounts() {
		return counts;
	}

	public void setCounts(int[] counts) {
		this.counts = counts;
	}

	public void setKnapType(int[] knapType) {
		this.knapType = knapType;
	}

	public int[] getKnapType() {
		return knapType;
	}

	public void setKnapIndex(int[] knapIndex) {
		this.knapIndex = knapIndex;
	}

	public int[] getKnapIndex() {
		return knapIndex;
	}
}
