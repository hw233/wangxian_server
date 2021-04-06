package com.fy.engineserver.operating.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SerialNumberAndMagicCardRecord implements Serializable{
	private static final long serialVersionUID = -4115813703543577876L;
	
	/**
	 * 用户每天领取的神奇卡
	 * 
	 */
	Map<String , String> dailyMagicCardMap = new LinkedHashMap<String, String>();
	
	/**
	 * 用户使用过的序列号
	 * 
	 */
	Map<String , MagicCard> serialNosMap = new LinkedHashMap<String , MagicCard>();
	
	/**
	 * 用户通过序列号
	 */
	List<MagicCard> magicCards = new ArrayList<MagicCard>();
	
	public void recordRequestMagicCard(String userName , long time , MagicCard card){
		String key = format(userName, time);
		dailyMagicCardMap.put(key , card.cardNo + "=" + card.password + " price=" + card.price);
	}

	public void recordSerialNo(String serialNo , MagicCard card){
		serialNosMap.put(serialNo , card);
	}
	
	public boolean isDailyMagicCardRequested(String userName , long time){
		String key = format(userName, time);
		return dailyMagicCardMap.containsKey(key);
	}
	
	private String format(String userName ,long time){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	
		String key = String.format("%1$tY-%1$tm-%1$td", c);
		return userName + key;
	}
}
