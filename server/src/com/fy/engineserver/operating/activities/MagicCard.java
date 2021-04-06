package com.fy.engineserver.operating.activities;

import java.io.Serializable;

public class MagicCard implements Serializable{
	private static final long serialVersionUID = 3702571188099740373L;
	
	String cardNo;
	String password;
	int price;
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
}
