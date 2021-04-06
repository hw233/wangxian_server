package com.fy.engineserver.auction;

/**
 * 拍卖品的客户端信息
 *
 */
public class AuctionInfo4Client {

	private long id;						//拍卖ID
	private long entityId;					//物品ID
	private String icon;					//图标
	private String articleName;				//物品名称
	private int count;						//数量
	private String sellName;				//出售者名称
	private String leaveTime;				//剩余时长
	private String buyPlayerName = "";		//竞拍者名称
	private long nowPrice;					//竞拍价
	private long buyPrice;					//一口价
	private int level;						//等级
	private int color;						//品质

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSellName() {
		return sellName;
	}

	public void setSellName(String sellName) {
		this.sellName = sellName;
	}

	public String getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}

	public long getNowPrice() {
		return nowPrice;
	}

	public void setNowPrice(long nowPrice) {
		this.nowPrice = nowPrice;
	}

	public long getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(long buyPrice) {
		this.buyPrice = buyPrice;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setBuyPlayerName(String buyPlayerName) {
		this.buyPlayerName = buyPlayerName;
	}

	public String getBuyPlayerName() {
		return buyPlayerName;
	}

}
