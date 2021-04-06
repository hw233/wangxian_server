package com.fy.engineserver.shop;

import java.text.SimpleDateFormat;
import java.util.Hashtable;

/**
 * 
 * 有购买数量限制的商品数据
 * 
 * 购买时间及数量数据
 * 数据记录的是玩家购买商品的总数量，时间，以及每人购买的数量时间
 * 
 *
 */

public class SelledGoodsData implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3828573443074940970L;

	/**
	 * 商品所在商店的id与商品id以及商品名字组合成key如1-1-馒头,商品商店id为1，商品id为1，商品名字馒头
	 */
	
	private String selledGoodsDataKey;
	
	/**
	 * 卖出商品的数量，有时间限制情况下，在下个周期开始时清零
	 */
	private long selledGoodsCount;
	
	/**
	 * 商品最近卖出时间
	 */
	private long selledLastFlushTime;
	
	/**
	 * 策划的配置时间
	 */
	private long configStartTime;
	private long configEndTime;
	
	/**
	 * 每个玩家购买数据，购买数量，以及最后刷新的时间
	 * key为玩家id，value为数组，数组第一个值为购买数量，有时间限制情况下，在下个周期开始时清零，第二个值为最后刷新的时间
	 * long[0]  数量
	 * long[1]  刷新时间
	 * long[2]  最后购买时间
	 * long[3]  策划配表开始时间
	 * long[4]  策划配表结束时间
	 */

	public Hashtable<Long,long[]> selledPersonGoodsData = new Hashtable<Long,long[]>();

	private long lastCheckTime = 0;
	
	private void checkPersonGoodsData(){
		
		System.out.println("=============== 检查某个物品记录的历史数据 ===================");
		long now = System.currentTimeMillis();
		
		if(now - lastCheckTime > 3600*1000L){
			lastCheckTime = now;
			Long ids[] = selledPersonGoodsData.keySet().toArray(new Long[0]);
			for(int i = 0 ; i < ids.length ; i++){
				long[]  r = selledPersonGoodsData.get(ids[i]);
				if(now - r[2] > 90L * 24 * 3600000 ){
					selledPersonGoodsData.remove(ids[i]);
					System.out.println("=============== 检查某个物品记录的历史数据，清除。。。。");
				}
			}
		}
		System.out.println("=============== 检查某个物品记录的历史数据 ===================");
	}
	
	public void initNewData(long playerId){
		long[] r = selledPersonGoodsData.get(playerId);
		if(r == null){
			r = new long[5];
			selledPersonGoodsData.put(playerId,r);
		}else{
			if(r.length<5){
				long oldNum = r[0];
				long oldFlashTime = r[1];
				long oldLastBuyTime = r[2];
				r = new long[5];
				r[0] = oldNum;
				r[1] = oldFlashTime;
				r[2] = oldLastBuyTime;
				r[3] = System.currentTimeMillis();
				r[4] = System.currentTimeMillis();
				selledPersonGoodsData.put(playerId,r);
			}
		}
	}
	
	public void addSelledNum(long playerId,int n,long configStartTime,long configEndTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(selledPersonGoodsData.size() > 1000000){
			checkPersonGoodsData();
		}
		long[] r = selledPersonGoodsData.get(playerId);
		boolean isnewcreate = false;
		if(r == null){
			isnewcreate = true;
			r = new long[5];
			selledPersonGoodsData.put(playerId,r);
		}
		//initNewData(playerId);
		long oldValue = r[0];
		r[0] = oldValue + n;
		r[1] = System.currentTimeMillis();
		r[2] = System.currentTimeMillis();
		r[3] = configStartTime;
		this.configStartTime = configStartTime;
		this.configEndTime = configEndTime;
		r[4] = configEndTime;
		selledPersonGoodsData.put(playerId,r);
		ShopManager.logger.warn("[用户购买数据更新] [isnewcreate:"+isnewcreate+"] [玩家id:"+playerId+"] [dataSize:"+selledPersonGoodsData.size()+"] [商店id,商品id,商品名:"+selledGoodsDataKey+"] [玩家总购买数量:"+oldValue+"-->"+r[0]+"-->"+getSelledNum(playerId)+"] [表开始时间:"+sdf.format(configStartTime)+"] [表结束时间:"+sdf.format(configEndTime)+"]");
	}
	
	public long getSelledNum(long playerId){
		long[] r = selledPersonGoodsData.get(playerId);
		if(r != null){
			return r[0];
		}
		return 0;
	}
	
	public long getLastFlushTime(long playerId){
		long[] r = selledPersonGoodsData.get(playerId);
		if(r != null){
			return r[1];
		}
		return System.currentTimeMillis();
	}
	
	public long getLastBuyTime(long playerId){
		long[] r = selledPersonGoodsData.get(playerId);
		if(r != null){
			return r[2];
		}
		return System.currentTimeMillis();
	}
	
	public void clearSellData(long playerId){
//		long[] r = selledPersonGoodsData.get(playerId);
//		if(r != null){
//			if(System.currentTimeMillis() - r[2] < 24 * 3600000L){
//			r[0] = 0;
//			r[1] = System.currentTimeMillis();
//			}else{
//				selledPersonGoodsData.remove(key)
//			}
//		}
		selledPersonGoodsData.remove(playerId);
	}
	
	public String getSelledGoodsDataKey() {
		return selledGoodsDataKey;
	}

	public void setSelledGoodsDataKey(String selledGoodsDataKey) {
		this.selledGoodsDataKey = selledGoodsDataKey;
	}

	public long getSelledGoodsCount() {
		return selledGoodsCount;
	}

	public void setSelledGoodsCount(long selledGoodsCount) {
		this.selledGoodsCount = selledGoodsCount;
	}

	public long getSelledLastFlushTime() {
		return selledLastFlushTime;
	}

	public void setSelledLastFlushTime(long selledLastFlushTime) {
		this.selledLastFlushTime = selledLastFlushTime;
	}

	public Hashtable<Long, long[]> getSelledPersonGoodsData() {
		return selledPersonGoodsData;
	}

	public void setSelledPersonGoodsData(Hashtable<Long, long[]> selledPersonGoodsData) {
		this.selledPersonGoodsData = selledPersonGoodsData;
	}

	public long getConfigStartTime(long playerId) {
		initNewData(playerId);
		long[] r = selledPersonGoodsData.get(playerId);
		if(r != null){
			return r[3];
		}
		return System.currentTimeMillis();
	}

	public long getConfigEndTime(long playerId) {
		initNewData(playerId);
		long[] r = selledPersonGoodsData.get(playerId);
		if(r != null){
			return r[4];
		}
		return System.currentTimeMillis();
	}

	public long getConfigStartTime() {
		return this.configStartTime;
	}

	public void setConfigStartTime(long configStartTime) {
		this.configStartTime = configStartTime;
	}

	public long getConfigEndTime() {
		return this.configEndTime;
	}

	public void setConfigEndTime(long configEndTime) {
		this.configEndTime = configEndTime;
	}

}
