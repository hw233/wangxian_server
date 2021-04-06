package com.fy.engineserver.economic.thirdpart.migu.entity;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity(name="SaleRecord")
@SimpleIndices({
	@SimpleIndex(members={"saleId"}),
	@SimpleIndex(members={"buyPlayerId"}),
	@SimpleIndex(members={"buyPassportId"}),
	@SimpleIndex(members={"sellPlayerId"}),
	@SimpleIndex(members={"sellPassportId"}),
	@SimpleIndex(members={"articleId"}),
	@SimpleIndex(members={"createTime"}),
	@SimpleIndex(members={"notified"}),
	@SimpleIndex(members={"notifySucc"}),
	@SimpleIndex(members={"handleResult","responseResult","notified"}),
	@SimpleIndex(members={"deviceCode"}),
	@SimpleIndex(members={"channelOrderId"})
})
public class SaleRecord implements java.io.Serializable {


	//调用失败
	public static final int HANDLE_RESULT_NOBACK = -1;
	//调用失败
	public static final int HANDLE_RESULT_FAILED = 0;
	//调用成功
	public static final int HANDLE_RESULT_SUCC = 1;
	
	
	//发布
	public static final int RESPONSE_NOBACK = -1;
	//上架
	public static final int RESPONSE_SHIFT = 3;
	//充值失败
	public static final int RESPONSE_FAILED = 0;
	//充值成功
	public static final int RESPONSE_SUCC = 1;
	//退款成功
	public static final int BACK_MONEY_SUCC = 2;
		
	//交易类型 道具类型 银锭类型 角色
	public static final int ARTICLE_TRADE = 1;
	public static final int SILVER_TRADE = 2;
	public static final int ROLE_TRADE = 3;
	
	
	// Fields
	@SimpleId
	private long id = 0l;
	
	@SimpleVersion
	private int version;
	
	/**
	 * 订单号
	 */

	private String saleId;
	
	/**
	 * 交易平台
	 */
	private String savingPlatform;
	
	/**
	 * 充值介质id
	 */
	private String savingMedium;
	
	/**
	 * 介质详情（充值卡或银行卡为卡号信息，短信为手机号...）
	 */
	private String mediumInfo;
	
	/**
	 * 用户交易时的渠道
	 */
	private String userChannel;
	
	/**
	 * 购买商品数量
	 *
	 */
	private long goodsCount;
	public long getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(long goodsCount) {
		this.goodsCount = goodsCount;
	}
	/**
	 * 出售物品的id
	 * id 为 200000 代表银子
	 */
	private long articleId;
	
	/**
	 * 物品的格子号
	 */
	private int cellIndex;

	/**
	 * 发布物品时的价格，单位:(分)
	 */
	private long payMoney = 0l;
	/**
	 * 扣完手续费的金额 单位分
	 */
	private long realPayMoney = 0l;
	
	/**
	 *手续费
	 */
	private long tradeMoney = 0l;
	
	/**
	 * 买物品账户id
	 */
	private long buyPassportId = 0l;
	
	/**
	 * 买物品角色Id
	 */
	private long buyPlayerId = 0l;
	
	/**
	 * 卖物品账户id
	 */
	private long sellPassportId = 0l;
	
	/**
	 * 卖物品角色Id
	 */
	private long sellPlayerId = 0l;
	
	/**
	 * 创建时间
	 */
	private long createTime;
	
	/**
	 * 交易类型
	 */
	
	private int tradeType;
	
	/**
	 * 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
	 */
	private int handleResult = -1;
	
	/**
	 * 调用充值结果的描述
	 */
	@SimpleColumn(length=1000)
	private String handleResultDesp;
	
	/**
	 * 本订单兑换状态, -1表示未返回, 0-返回充值成功, 1-返回充值失败 代表对方回调或给出充值是否成功的确认状态
	 */
	private int responseResult = -1;
	
	/**
	 * 充值结果返回描述
	 */
	private String responseDesp;
	
	/**
	 * 充值结果返回时间 (记录交易成功时间)
	 */
	private long responseTime;
	
	/**
	 * 是否充值通知游戏服务器过了
	 * 线程通知游戏服 是否已经通知
	 */
	private boolean notified; 
	
	/**
	 * 
	 * 通知游戏服务器是否成功
	 * 得到游戏服的响应后更新
	 */
	private boolean notifySucc;
	
	/**
	 * 订单对应服务器
	 */
	private String serverName;
	
	/**
	 * 备注字段1    
	 */
	@SimpleColumn(length=400)
	private String memo1;
	
	/**
	 * 备注字段2   (用以存储卖家username，角色交易使用)
	 */
	@SimpleColumn(length=400)
	private String memo2;
	
	/**
	 * 备注字段3   (用以存储买家username,角色交易使用)
	 */
	@SimpleColumn(length=400)
	private String memo3;
	

	/**
	 * channelOrderId
	 * @return
	 */
	private String channelOrderId;
	
	//交易银两的预付费 单位是文
	private long prepayfee;
	
	/**
	 * 充值的设备号
	 */
	@SimpleColumn(length=100)
	private String deviceCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public String getSavingPlatform() {
		return savingPlatform;
	}

	public void setSavingPlatform(String savingPlatform) {
		this.savingPlatform = savingPlatform;
	}

	public String getSavingMedium() {
		return savingMedium;
	}

	public void setSavingMedium(String savingMedium) {
		this.savingMedium = savingMedium;
	}

	public String getMediumInfo() {
		return mediumInfo;
	}

	public void setMediumInfo(String mediumInfo) {
		this.mediumInfo = mediumInfo;
	}

	public long getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(long payMoney) {
		this.payMoney = payMoney;
	}

	
	
	

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public long getBuyPassportId() {
		return buyPassportId;
	}

	public void setBuyPassportId(long buyPassportId) {
		this.buyPassportId = buyPassportId;
	}

	public long getBuyPlayerId() {
		return buyPlayerId;
	}

	public void setBuyPlayerId(long buyPlayerId) {
		this.buyPlayerId = buyPlayerId;
	}

	public long getSellPassportId() {
		return sellPassportId;
	}

	public void setSellPassportId(long sellPassportId) {
		this.sellPassportId = sellPassportId;
	}

	public long getSellPlayerId() {
		return sellPlayerId;
	}

	public void setSellPlayerId(long sellPlayerId) {
		this.sellPlayerId = sellPlayerId;
	}
	
	public int getCellIndex() {
		return cellIndex;
	}

	public void setCellIndex(int cellIndex) {
		this.cellIndex = cellIndex;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(int handleResult) {
		this.handleResult = handleResult;
	}

	public String getHandleResultDesp() {
		return handleResultDesp;
	}

	public void setHandleResultDesp(String handleResultDesp) {
		this.handleResultDesp = handleResultDesp;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponseDesp() {
		return responseDesp;
	}

	public void setResponseDesp(String responseDesp) {
		this.responseDesp = responseDesp;
	}

	public int getResponseResult() {
		return responseResult;
	}

	public void setResponseResult(int responseResult) {
		this.responseResult = responseResult;
	}

	public boolean isNotified() {
		return notified;
	}

	public void setNotified(boolean notified) {
		this.notified = notified;
	}

	public boolean isNotifySucc() {
		return notifySucc;
	}

	public void setNotifySucc(boolean notifySucc) {
		this.notifySucc = notifySucc;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getMemo1() {
		return memo1;
	}

	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}

	public String getMemo2() {
		return memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}

	public String getMemo3() {
		return memo3;
	}

	public void setMemo3(String memo3) {
		this.memo3 = memo3;
	}
	
	public String getUserChannel() {
		return userChannel;
	}

	public void setUserChannel(String userChannel) {
		this.userChannel = userChannel;
	}
	
	public String getChannelOrderId() {
		return channelOrderId;
	}

	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}
	
	
	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	
	

	public long getRealPayMoney() {
		return realPayMoney;
	}

	public void setRealPayMoney(long realPayMoney) {
		this.realPayMoney = realPayMoney;
	}
	
	

	public long getTradeMoney() {
		return tradeMoney;
	}

	public void setTradeMoney(long tradeMoney) {
		this.tradeMoney = tradeMoney;
	}
	
	

	public long getPrepayfee() {
		return prepayfee;
	}

	public void setPrepayfee(long prepayfee) {
		this.prepayfee = prepayfee;
	}

	
	public int getTradeType() {
		return tradeType;
	}

	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}

	public String getStatusDesp() {
		if(this.isNotifySucc()) {
			return "充值成功完成";
		} else if(this.isNotified()) {
			return "充值成功，同步游戏服失败";
		} else if(this.getResponseResult() == SaleRecord.RESPONSE_SUCC) {
			return "充值成功，还未同步游戏服";
		} else if(this.getResponseResult() == SaleRecord.RESPONSE_FAILED) {
			return "充值失败，原因:" + this.getResponseDesp();
		} else if(this.getHandleResult() == SaleRecord.HANDLE_RESULT_SUCC) {
			return "充值已提交，未支付或还未返回";
		} else if(this.getHandleResult() == SaleRecord.HANDLE_RESULT_NOBACK) {
			return "订单已生成，等待支付";
		} else {
			return "充值失败，原因:" + this.getHandleResultDesp();
		}
	}
	
	public String getLogStr() {
		return "[id:"+id+"] [userChannel:"+(userChannel!=null?userChannel:"")+"] [saleId:"+saleId+"] [buypassport:"+buyPassportId+"] [buyplayerId:"+buyPlayerId+"] [sellPassportId:"+sellPassportId+"] [sellPlayerId:"+sellPlayerId+"] [money:"+payMoney+"] [userChannel:"+userChannel+"] [platform:"+savingPlatform+"] " +
				"[medium:"+savingMedium+"] [mediumInfo:"+mediumInfo+"] [handleResult:"+handleResult+"] " +
				"[responseResult:"+responseResult+"] [notified:"+notified+"] [notifySucc:"+notifySucc+"] [prepayfee:"+prepayfee+"] [trademoney:"+tradeMoney+"]";
	}
}