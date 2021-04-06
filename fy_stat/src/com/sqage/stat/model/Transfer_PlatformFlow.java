package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class Transfer_PlatformFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;
	public static Logger logger = Logger.getLogger(Transfer_PlatformFlow.class);

	private String		id=""	;	        //	订单id	
	private String		articleId=""	;	//	道具id	
	private String		articleName=""	;	//	道具名称	
	private String		articleColor="";	//	道具颜色	
	private String		cellIndex=""	;	//	背包中的格子号	
	private String		goodsCount=""	;	//	此次购买的道具数量	
	private long		createTime	;	//	订单创建时间	
	private long		payMoney	;	//	订单金额	
	private String		sellPassportId="";	//	卖家的账号id	
	private String		sellPassportName="";	//	卖家的账号	
	private String		sellPlayerId=""	;	//	卖家的角色id	
	private String		sellPlayerName=""	;	//	卖家的角色名	
	private String		sellPassportChannel="";	//	卖家的渠道	
	private String		sellPlayerLevel=""	;	//	卖家的等级	
	private String		sellVipLevel=""	;	//	卖家的vip等级	
	private String		tradeType=""	;	    //	交易类型	
	private String		responseResult=""	;	//	订单交易结果	
	private long		tradeMoney	;	    //    	手续费
	private String		buyPlayerId=""	;	    //	购买人的角色id
	private String		buyPlayerName=""	;	//	购买人的角色名	
	private String		buyPassportId=""	;	//	购买人的账号id	
	private String		buyPassportName=""	;	//	购买人的账号	
	private String		buyPlayerLevel=""	;	//	购买人的等级	
	private String		buyPlayerVipLevel="";	//	购买人的vip等级	
	private String		buyPassportChannel="";	//	购买人的渠道	
	private String		serverName=""	;	    //	游戏服名称	

	private String   articleSalePaySilver="";   //挂单手续费——扣除银锭(成功发布道具类寄售时收取)：	a)	同时挂单的前5笔免手续费	b)	同时挂单的第6-30笔每笔寄售订单收取10两银锭的手续费

	private String   cancelSaleSilver="";       //银锭交易提前撤单扣除银两
	  
	  private String column1="";//银两挂单提前扣掉的手续费用
	  private String column2="";//成功完成交易时间
	  private String column3="";//备用项
	  private String column4="";//备用项
	
	@Override
	public String toString() {
		return "Transfer_PlatformFlow [articleColor:" + articleColor + "] [articleId:" + articleId + "] [articleName:" + articleName
				+ "] [articleSalePaySilver:" + articleSalePaySilver + "] [buyPassportChannel:" + buyPassportChannel + "] [buyPassportId:"
				+ buyPassportId + "] [buyPassportName:" + buyPassportName + "] [buyPlayerId:" + buyPlayerId + "] [buyPlayerLevel:" + buyPlayerLevel
				+ "] [buyPlayerName:" + buyPlayerName + "] [buyPlayerVipLevel:" + buyPlayerVipLevel + "] [cancelSaleSilver:" + cancelSaleSilver
				+ "] [cellIndex:" + cellIndex + "] [column1:" + column1 + "] [column2:" + column2 + "] [column3:" + column3 + "] [column4:" + column4
				+ "] [createTime:" + createTime + "] [goodsCount:" + goodsCount + "] [id:" + id + "] [payMoney:" + payMoney + "] [responseResult:"
				+ responseResult + "] [sellPassportChannel:" + sellPassportChannel + "] [sellPassportId:" + sellPassportId + "] [sellPassportName:"
				+ sellPassportName + "] [sellPlayerId:" + sellPlayerId + "] [sellPlayerLevel:" + sellPlayerLevel + "] [sellPlayerName:"
				+ sellPlayerName + "] [sellVipLevel:" + sellVipLevel + "] [serverName:" + serverName + "] [tradeMoney:" + tradeMoney
				+ "] [tradeType:" + tradeType + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public String getArticleColor() {
		return articleColor;
	}
	public void setArticleColor(String articleColor) {
		this.articleColor = articleColor;
	}
	public String getCellIndex() {
		return cellIndex;
	}
	public void setCellIndex(String cellIndex) {
		this.cellIndex = cellIndex;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(long payMoney) {
		this.payMoney = payMoney;
	}
	public String getSellPassportId() {
		return sellPassportId;
	}
	public void setSellPassportId(String sellPassportId) {
		this.sellPassportId = sellPassportId;
	}
	public String getSellPassportName() {
		return sellPassportName;
	}
	public void setSellPassportName(String sellPassportName) {
		this.sellPassportName = sellPassportName;
	}
	public String getSellPlayerId() {
		return sellPlayerId;
	}
	public void setSellPlayerId(String sellPlayerId) {
		this.sellPlayerId = sellPlayerId;
	}
	public String getSellPlayerName() {
		return sellPlayerName;
	}
	public void setSellPlayerName(String sellPlayerName) {
		this.sellPlayerName = sellPlayerName;
	}
	public String getSellPassportChannel() {
		return sellPassportChannel;
	}
	public void setSellPassportChannel(String sellPassportChannel) {
		this.sellPassportChannel = sellPassportChannel;
	}
	public String getSellPlayerLevel() {
		return sellPlayerLevel;
	}
	public void setSellPlayerLevel(String sellPlayerLevel) {
		this.sellPlayerLevel = sellPlayerLevel;
	}
	public String getSellVipLevel() {
		return sellVipLevel;
	}
	public void setSellVipLevel(String sellVipLevel) {
		this.sellVipLevel = sellVipLevel;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getResponseResult() {
		return responseResult;
	}
	public void setResponseResult(String responseResult) {
		this.responseResult = responseResult;
	}
	public long getTradeMoney() {
		return tradeMoney;
	}
	public void setTradeMoney(long tradeMoney) {
		this.tradeMoney = tradeMoney;
	}
	public String getBuyPlayerId() {
		return buyPlayerId;
	}
	public void setBuyPlayerId(String buyPlayerId) {
		this.buyPlayerId = buyPlayerId;
	}
	public String getBuyPlayerName() {
		return buyPlayerName;
	}
	public void setBuyPlayerName(String buyPlayerName) {
		this.buyPlayerName = buyPlayerName;
	}
	public String getBuyPassportId() {
		return buyPassportId;
	}
	public void setBuyPassportId(String buyPassportId) {
		this.buyPassportId = buyPassportId;
	}
	public String getBuyPassportName() {
		return buyPassportName;
	}
	public void setBuyPassportName(String buyPassportName) {
		this.buyPassportName = buyPassportName;
	}
	public String getBuyPlayerLevel() {
		return buyPlayerLevel;
	}
	public void setBuyPlayerLevel(String buyPlayerLevel) {
		this.buyPlayerLevel = buyPlayerLevel;
	}
	public String getBuyPlayerVipLevel() {
		return buyPlayerVipLevel;
	}
	public void setBuyPlayerVipLevel(String buyPlayerVipLevel) {
		this.buyPlayerVipLevel = buyPlayerVipLevel;
	}
	public String getBuyPassportChannel() {
		return buyPassportChannel;
	}
	public void setBuyPassportChannel(String buyPassportChannel) {
		this.buyPassportChannel = buyPassportChannel;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}



	public String getColumn1() {
		return column1;
	}



	public void setColumn1(String column1) {
		this.column1 = column1;
	}



	public String getColumn2() {
		return column2;
	}



	public void setColumn2(String column2) {
		this.column2 = column2;
	}



	public String getColumn3() {
		return column3;
	}



	public void setColumn3(String column3) {
		this.column3 = column3;
	}



	public String getColumn4() {
		return column4;
	}



	public void setColumn4(String column4) {
		this.column4 = column4;
	}
	public String getArticleSalePaySilver() {
		return articleSalePaySilver;
	}
	public void setArticleSalePaySilver(String articleSalePaySilver) {
		this.articleSalePaySilver = articleSalePaySilver;
	}
	public String getCancelSaleSilver() {
		return cancelSaleSilver;
	}
	public void setCancelSaleSilver(String cancelSaleSilver) {
		this.cancelSaleSilver = cancelSaleSilver;
	}
    
	
}
