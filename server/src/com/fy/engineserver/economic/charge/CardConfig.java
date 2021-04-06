package com.fy.engineserver.economic.charge;

import java.util.Arrays;

public class CardConfig {

	private int cardId;
	private String cardName;
	private int chargeMoney;
	private String icon;
	private int ids[] = new int[0];
	private String cardInfo;
	
	public CardConfig(int cardId, String cardName, int chargeMoney,
			String icon, int[] ids, String cardInfo) {
		super();
		this.cardId = cardId;
		this.cardName = cardName;
		this.chargeMoney = chargeMoney;
		this.icon = icon;
		this.ids = ids;
		this.cardInfo = cardInfo;
	}
	
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public int getChargeMoney() {
		return chargeMoney;
	}
	public void setChargeMoney(int chargeMoney) {
		this.chargeMoney = chargeMoney;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int[] getIds() {
		return ids;
	}
	public void setIds(int[] ids) {
		this.ids = ids;
	}
	public String getCardInfo() {
		return cardInfo;
	}
	public void setCardInfo(String cardInfo) {
		this.cardInfo = cardInfo;
	}
	@Override
	public String toString() {
		return "[cardId=" + cardId + ", cardInfo=" + cardInfo
				+ ", cardName=" + cardName + ", chargeMoney=" + chargeMoney
				+ ", icon=" + icon + ", ids=" + Arrays.toString(ids) + "]";
	}
	
}
