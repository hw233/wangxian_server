package com.fy.boss.finance.model;

import com.fy.boss.finance.model.ExceptionOrderForm;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;



/**
 * ExceptionOrderForm entity.
 * 充值订单，支持多种充值方式，充值卡、银行卡、短信计费等等
 * 
 * @author MyEclipse Persistence Tools
 */
@SimpleEntity(name="ExceptionOrderForm")
@SimpleIndices({
	@SimpleIndex(members={"orderId"}),
	@SimpleIndex(members={"passportId"},name="EORDER_PASSPORT_INX"),
	@SimpleIndex(members={"createTime"},name="EORDER_TIME_INX"),
	@SimpleIndex(members={"notified"},name="EORDER_NOTIFIED_INX"),
	@SimpleIndex(members={"notifySucc"},name="EORDER_NOTIFYSUCC_INX"),
	@SimpleIndex(members={"handleResult","responseResult","notified"},name="EORDER_UNIONRESULT_INX"),
	@SimpleIndex(members={"channelOrderId"},name="EORDER_CORDERID_INX"),
	@SimpleIndex(members={"channel"})
})
public class ExceptionOrderForm implements java.io.Serializable {


	//调用失败
	public static final int HANDLE_RESULT_NOBACK = -1;
	//调用失败
	public static final int HANDLE_RESULT_FAILED = 0;
	//调用成功
	public static final int HANDLE_RESULT_SUCC = 1;
	
	//未返回
	public static final int RESPONSE_NOBACK = -1;
	//充值失败
	public static final int RESPONSE_FAILED = 0;
	//充值成功
	public static final int RESPONSE_SUCC = 1;


	
	
	
	// Fields
	@SimpleId
	private long id = 0l;
	
	@SimpleVersion
	private int version;
	
	/**
	 * 订单号
	 */

	private String orderId;
	/**
	 * 渠道订单号
	 */
	private String channelOrderId;
	

	/**
	 * 充值平台
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
	 * 用户充值时所属的渠道
	 */
	private String userChannel;
	
	/**
	 * 购买商品数量
	 *
	 */
	private int goodsCount;
	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	/**
	 * 支付人名币，单位:(分)
	 */
	private long payMoney = 0l;
	
	/**
	 * 充值人
	 */
	private long passportId = 0l;
	
	/**
	 * 创建时间
	 */
	private long createTime;
	
	/**
	 * 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
	 */
	private int handleResult = -1;
	
	/**
	 * 调用充值结果的描述
	 */
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
	 * 充值结果返回时间
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
	@SimpleColumn(length=4000)
	private String memo1;
	
	/**
	 * 备注字段2
	 */
	@SimpleColumn(length=4000)
	private String memo2;
	
	/**
	 * 备注字段3 
	 */
	@SimpleColumn(length=4000)
	private String memo3;
	
	private String channel;
	

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public long getPassportId() {
		return passportId;
	}

	public void setPassportId(long passportId) {
		this.passportId = passportId;
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

	
	public String getStatusDesp() {
		if(this.isNotifySucc()) {
			return "充值成功完成";
		} else if(this.isNotified()) {
			return "充值成功，同步游戏服失败";
		} else if(this.getResponseResult() == ExceptionOrderForm.RESPONSE_SUCC) {
			return "充值成功，还未同步游戏服";
		} else if(this.getResponseResult() == ExceptionOrderForm.RESPONSE_FAILED) {
			return "充值失败，原因:" + this.getResponseDesp();
		} else if(this.getHandleResult() == ExceptionOrderForm.HANDLE_RESULT_SUCC) {
			return "充值已提交，未支付或还未返回";
		} else if(this.getHandleResult() == ExceptionOrderForm.HANDLE_RESULT_NOBACK) {
			return "订单已生成，等待支付";
		} else {
			return "充值失败，原因:" + this.getHandleResultDesp();
		}
	}
	
	public String getLogStr() {
		return "[id:"+id+"] [userChannel:"+(userChannel!=null?userChannel:"")+"] [orderId:"+orderId+"] [passport:"+passportId+"] [money:"+payMoney+"] [userChannel:"+userChannel+"] [platform:"+savingPlatform+"] " +
				"[medium:"+savingMedium+"] [mediumInfo:"+mediumInfo+"] [handleResult:"+handleResult+"] " +
				"[responseResult:"+responseResult+"] [notified:"+notified+"] [notifySucc:"+notifySucc+"]";
	}
	
	
	
}
