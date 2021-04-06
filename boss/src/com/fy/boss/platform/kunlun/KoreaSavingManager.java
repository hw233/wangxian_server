package com.fy.boss.platform.kunlun;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.xiaomi.XiaomiSdkSavingManager;
import com.fy.boss.platform.kunlun.KoreaSavingManager;
import com.xuanzhi.tools.text.DateUtil;

public class KoreaSavingManager {

	protected static KoreaSavingManager m_self = null;
	
	private static Map<String,String> serverMap = new HashMap<String, String>();
	private static Map<String,String> descMap = new HashMap<String, String>();
	
	public void initialize() throws Exception {
		m_self = this;
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
	}
	
	
	static
	{
		
		/*
		 *  원시천존
			태상노군
			영보천존
			옥황상제
			서왕모
			현천상제
			삼관대제
			북두성군
			천선낭랑
			구천현녀

		 */
		serverMap.put("ST", "557001");
		serverMap.put("S1-원시천존", "557002");
		serverMap.put("S2-태상노군", "557003");
		serverMap.put("S3-영보천존", "557004");
		serverMap.put("S4-옥황상제", "557005");
		serverMap.put("S5-서왕모", "557006");
		serverMap.put("S6-현천상제", "557007");
		serverMap.put("S7-삼관대제", "557008");
		serverMap.put("S8-북두성군", "557009");
		serverMap.put("S9-천선낭랑", "557010");
		serverMap.put("S10-구천현녀", "557011");
		serverMap.put("S11-풍도대제", "557012");
		serverMap.put("S12-관성제군", "557013");
		serverMap.put("S13-문창제군", "557014");
		serverMap.put("S14-탁탑천왕", "557015");
		serverMap.put("S15-제천대성", "557016");
		serverMap.put("S16-천상대전", "557017");
		
		
		descMap.put("S1-원시천존", "S1-元始天尊");
		descMap.put("S2-태상노군", "S2-太上老君");
		descMap.put("S3-영보천존", "S3-灵宝天尊");
		descMap.put("S4-옥황상제", "S4-玉皇大帝");
		descMap.put("S5-서왕모", "S5-西王母");
		descMap.put("S6-현천상제", "S6-玄天上帝");
		descMap.put("S7-삼관대제", "S7-三官大帝");
		descMap.put("S8-북두성군", "S8-北斗星君");
		descMap.put("S9-천선낭랑", "S9-天仙娘娘");
		descMap.put("S10-구천현녀", "S10-九天玄女");
		descMap.put("S11-풍도대제", "S11-酆都大帝");
		descMap.put("S12-관성제군", "S12-关圣帝君");
		descMap.put("S13-문창제군", "S13-文昌帝君");
		descMap.put("S14-탁탑천왕", "S14-托塔天王");
		descMap.put("S15-제천대성", "S15-齐天大圣");
		descMap.put("S16-천상대전", "S16-天界大战");
		
	}

	/**
	 * 支付宝充值
	 * 
	 * @param passport
	 * @param channel
	 * @param servername
	 * @param addAmount 分
	 * @param addType
	 *            充值方式： 1-支付宝， 5-页面支付， 2-wap支付
	 * @param callbackUrl
	 *            页面支付或wap支付的回调页面
	 * @return
	 */
	public String kunlunSaving(Passport passport, String channel, String servername, int addAmount, String os) {
		long startTime = System.currentTimeMillis();
		String chineseServerName = descMap.get(servername);
		String savingMediumName = "KoreaAndroid";
		if(channel.toLowerCase().contains("google"))
		{
			savingMediumName = "KoreaGoogleplay";
		}
		if(channel.toLowerCase().contains("koreakt") || channel.toLowerCase().contains("koreasdkkt"))
		{
			savingMediumName = "KoreaKT";
		}
		if(channel.toLowerCase().contains("tstore"))
		{
			savingMediumName = "KoreaT-STORE";
		}
			
		
		if(channel.toLowerCase().contains("appstore"))
		{
			savingMediumName = "KoreaAppstore";
		}
		
		if(channel.toLowerCase().contains("naver"))
		{
			savingMediumName = "KoreaNA";
		}
		
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("昆仑");
			order.setSavingMedium(savingMediumName);
			//设置充值人
			order.setPassportId(passport.getId());
			//设置消费金额
			order.setPayMoney( new Long(addAmount));
			//设置serverid
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setHandleResultDesp("新生成订单");
			//设置订单responseResult
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			order.setResponseDesp("新生成订单");
			//设置是否通知游戏服状态 设为false
			order.setNotified(false);
			//设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(os);
			//先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			//设置订单号
			order.setOrderId("korea"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[昆仑充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+(chineseServerName == null ? servername : chineseServerName)+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId()+"@"+serverMap.get(servername);
			}
			else
			{
				PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+(chineseServerName == null ? servername : chineseServerName)+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+(chineseServerName == null ? servername : chineseServerName)+"] [手机平台:"+os+"] [充值平台:昆仑] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	
	public String kunlunSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String chineseServerName = descMap.get(servername);
		String savingMediumName = "KoreaAndroid";
		if(channel.toLowerCase().contains("google"))
		{
			savingMediumName = "KoreaGoogleplay";
		}
		if(channel.toLowerCase().contains("koreakt") || channel.toLowerCase().contains("koreasdkkt"))
		{
			savingMediumName = "KoreaKT";
		}
		if(channel.toLowerCase().contains("koreatstore") || channel.toLowerCase().contains("tstore"))
		{
			savingMediumName = "KoreaT-STORE";
		}
			
		
		if(channel.toLowerCase().contains("appstore"))
		{
			savingMediumName = "KoreaAppstore";
		}
		
		if(channel.toLowerCase().contains("naver"))
		{
			savingMediumName = "KoreaNA";
		}
		
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("昆仑");
			order.setSavingMedium(savingMediumName);
			//设置充值人
			order.setPassportId(passport.getId());
			//设置消费金额
			order.setPayMoney( new Long(addAmount));
			//设置serverid
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setHandleResultDesp("新生成订单");
			//设置订单responseResult
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			order.setResponseDesp("新生成订单");
			//设置是否通知游戏服状态 设为false
			order.setNotified(false);
			//设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			//先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			//设置订单号
			order.setOrderId("korea"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[昆仑充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+(chineseServerName == null ? servername : chineseServerName)+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [渠道:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId()+"@"+serverMap.get(servername);
			}
			else
			{
				PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+(chineseServerName == null ? servername : chineseServerName)+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [渠道:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+(chineseServerName == null ? servername : chineseServerName)+"] [手机平台:"+os+"] [充值平台:昆仑] [渠道:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}

	public static KoreaSavingManager getInstance() {
	  	if(m_self == null)
    	{
    		m_self = new KoreaSavingManager();
    	}
		return m_self;
	}
}
