package com.fy.engineserver.trade.boothsale;

/**
 * 包裹的链接
 * 
 * 
 */
public class SoftLink4Package {

	private int packageType;
	private int indexOfPackage;
	private long entityId = -1;
	private long price;

	public SoftLink4Package(int packageType, int indexOfPackage ,long price) {
		this.packageType = packageType;
		this.indexOfPackage = indexOfPackage;
		this.price = price;
	}
	

	public long getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public int getPackageType() {
		return packageType;
	}

	public void setPackageType(int packageType) {
		this.packageType = packageType;
	}

	public int getIndexOfPackage() {
		return indexOfPackage;
	}

	public void setIndexOfPackage(int indexOfPackage) {
		this.indexOfPackage = indexOfPackage;
	}
	
	public boolean isEmpty(){
		return packageType<0 || indexOfPackage<0;
	}


	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}


	public long getEntityId() {
		return entityId;
	}
}
