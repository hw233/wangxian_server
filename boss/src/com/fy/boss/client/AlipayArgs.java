package com.fy.boss.client;

public class AlipayArgs {
	private String partnerID;
	
	private String sellerID;
	
	private String aliPublicKey;
	
	private String ownPublicKey;
	
	private String ownPrivateKey;
	
	private String notifyUrl;

	public String getPartnerID() {
		return partnerID;
	}

	public void setPartnerID(String partnerID) {
		this.partnerID = partnerID;
	}

	public String getSellerID() {
		return sellerID;
	}

	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}

	public String getAliPublicKey() {
		return aliPublicKey;
	}

	public void setAliPublicKey(String aliPublicKey) {
		this.aliPublicKey = aliPublicKey;
	}

	public String getOwnPublicKey() {
		return ownPublicKey;
	}

	public void setOwnPublicKey(String ownPublicKey) {
		this.ownPublicKey = ownPublicKey;
	}

	public String getOwnPrivateKey() {
		return ownPrivateKey;
	}

	public void setOwnPrivateKey(String ownPrivateKey) {
		this.ownPrivateKey = ownPrivateKey;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	
}
