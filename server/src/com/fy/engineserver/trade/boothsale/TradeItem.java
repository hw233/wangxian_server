package com.fy.engineserver.trade.boothsale;


import com.fy.engineserver.trade.requestbuy.RequestBuy;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/** 交易的物品 */
@SimpleEmbeddable
public class TradeItem {

	private static SimpleEntityManager<RequestBuy> em;
	
	public static void setEM(SimpleEntityManager<RequestBuy> em){
		TradeItem.em = em;
	}
	
	/** 实体名 */
	@SimpleColumn(length=100)
	private String entityName;
	/** 物品数量 */
	private int entityNum = -1;
	/** 单价 */
	private long perPrice;
	/** 开始时间 */
	private long startTime;
	/** 结束时间 */
	private long endTime;
	
	private int startNum = 0 ;
	
	public TradeItem(){}
	
	public TradeItem(String name, int entityNum, long perPrice, long startTime, long endTime) {
		this.entityName = name;
		this.entityNum = entityNum;
		this.perPrice = perPrice;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public long getPerPrice() {
		return perPrice;
	}

	protected void setPerPrice(long perPrice) {
		this.perPrice = perPrice;
	}

	public int getEntityNum() {
		return entityNum;
	}

	protected void setEntityNum(int entityNum) {
		this.entityNum = entityNum;
	}

	public long getStartTime() {
		return startTime;
	}

	protected void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	protected void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getEntityName() {
		return entityName;
	}

	protected void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public TradeItem init(String name, int entityNum, long perPrice, long startTime, long endTime) {
		this.entityName = name;
		this.entityNum = entityNum;
		this.perPrice = perPrice;
		this.startTime = startTime;
		this.endTime = endTime;
		return this;
	}

	public synchronized void subNum(RequestBuy buy, int num) {
		setEntityNum(getEntityNum() - num);
		em.notifyFieldChange(buy, "item");
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public int getStartNum() {
		if (startNum == 0) {
			setStartNum(entityNum);
		}
		return startNum;
	}
}
