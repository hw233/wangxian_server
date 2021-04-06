package com.fy.engineserver.economic.thirdpart.migu.entity.model;

public class TempSaleInfo {
	/** 生成订单后需要返回给米谷的json串 */
	private String saleInfo;
	/** 创建此缓存时间，在清理缓存时使用 */
	private long createTime;
	
	public TempSaleInfo(long createTime, String saleInfo) {
		this.createTime = createTime;
		this.saleInfo = saleInfo;
	}
	

	public String getSaleInfo() {
		return saleInfo;
	}

	public void setSaleInfo(String saleInfo) {
		this.saleInfo = saleInfo;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
}
