package com.fy.boss.platform.qq.msdk;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.game.model.Server;
import com.fy.boss.game.service.ServerManager;
import com.fy.boss.tools.JacksonManager;
import com.qq.open.OpensnsException;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;

public class MSDKSavingManager {

	private static MSDKSavingManager self;
	
	public static String query_url = "http://msdk.qq.com";
	public static String query_url_test = "http://msdktest.qq.com";
	
	public static String query_ysdk_url = "https://ysdk.qq.com";
	public static String query_ysdk_url_test = "http://ysdktest.qq.com";
	
	public static final String MSDK_APPID = "1106884290";//"1106696671";
	public static final String MSDK_APPKEY_TEST = "wiKHDdqOWEeTofJv";
	public static final String MSDK_APPKEY = "dS5UZl7Fb0ZTLxRyWVDRhkp7wMZSVKvq";
//	public static final String MSDK_APPID = "1104521248";
//	public static final String MSDK_APPKEY = "9NovA4Qd01YxSkXn";
	
	public static final String WXSDK_APPID = "wxe110eb5a8ce1a4a2";
	public static final String WXSDK_APPKEY = "a2a4d597698a0f3b5fcd70f67e5b7ea8";
//	public static final String WXSDK_APPID = "wx50817faa35d77569";
//	public static final String WXSDK_APPKEY = "916704cd755a2d93a07d9de4da78feff";
	
	public static int ZOND_ID = 1;
	
	public static MSDKSavingManager getInstance(){
		if(self == null){
			self = new MSDKSavingManager();
		}
		return self;
	}
	
	public String msdkSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others){
		long startTime = System.currentTimeMillis();
		try{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			// 设置充值平台
			order.setSavingPlatform("腾讯MSDK");
			// 设置充值介质
			order.setSavingMedium("MSDK充值");
			// 设置充值人
			order.setPassportId(passport.getId());
			// 设置q币数
			order.setGoodsCount(1);
			// 设置消费金额 转换
			order.setPayMoney(new Long(addAmount));
			// 设置serverid
			order.setServerName(servername);
			//QQ
			order.setHandleResult(-1);
			order.setHandleResultDesp("新生成订单");
			// 设置订单responseResult
			order.setResponseResult(-1);
			order.setResponseDesp("新生成订单");
			// 设置是否通知游戏服状态 设为false
			order.setNotified(false);
			// 设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			// 先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			// 设置订单号
			order.setOrderId("msdk"+DateUtil.formatDate(new Date(), "yyyy-MM-dd") + order.getId());
			// orderFormManager.getEm().notifyFieldChange(order, "orderId");
			// orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[MSDK充值生成订单] [成功] [更新订单信息成功] [用户名:" + passport.getUserName() + "] [channel:"+channel+"] [serverName:" + servername + "] [充值平台："+order.getSavingPlatform()+"] [充值介质:"+order.getSavingMedium()+"] [Q币数量：" + addAmount/100 + "] [充值面值:"+order.getPayMoney()+"分] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [角色id:"+order.getMemo1()+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms]");
			return order.getOrderId()+"####"+ZOND_ID;
		}catch(Exception e){
			PlatformSavingCenter.logger.warn("[MSDK充值生成订单] [异常] [username:"+(passport == null?"null":passport.getUserName())+"] [channel:"+channel+"] [servername:"+servername+"] [Q币数量:"+addAmount/100+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms] ["+e+"]");
			return null;
		}
	} 
	
	public String ysdkCharge(Passport passport, String channel, String servername, int addAmount, String os,String[]others){
		long startTime = System.currentTimeMillis();
		try{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			// 设置充值平台
			order.setSavingPlatform("腾讯YSDK");
			// 设置充值介质
			order.setSavingMedium("米大师充值");
			// 设置充值人
			order.setPassportId(passport.getId());
			// 设置q币数
			order.setGoodsCount(1);
			// 设置消费金额 转换
			order.setPayMoney(new Long(addAmount));
			// 设置serverid
			order.setServerName(servername);
			//QQ
			order.setHandleResult(-1);
			order.setHandleResultDesp("新生成订单");
			// 设置订单responseResult
			order.setResponseResult(-1);
			order.setResponseDesp("新生成订单");
			// 设置是否通知游戏服状态 设为false
			order.setNotified(false);
			// 设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			// 先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			// 设置订单号
			order.setOrderId("ysdk"+DateUtil.formatDate(new Date(), "yyyy-MM-dd") + order.getId());
			// orderFormManager.getEm().notifyFieldChange(order, "orderId");
			// orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[YSDK生成订单] [成功] [更新订单信息成功] [用户名:" + passport.getUserName() + "] [channel:"+channel+"] [serverName:" + servername + "] [充值平台："+order.getSavingPlatform()+"] [充值介质:"+order.getSavingMedium()+"] [Q币数量：" + addAmount/100 + "] [充值面值:"+order.getPayMoney()+"分] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [角色id:"+order.getMemo1()+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms]");
			return order.getOrderId()+"####"+ZOND_ID;
		}catch(Exception e){
			PlatformSavingCenter.logger.warn("[YSDK生成订单] [异常] [username:"+(passport == null?"null":passport.getUserName())+"] [channel:"+channel+"] [servername:"+servername+"] [Q币数量:"+addAmount/100+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms] ["+e+"]");
			return null;
		}
	} 
	
	public String ysdkCharge(Passport passport, String channel, String servername, int addAmount, String os){
		long startTime = System.currentTimeMillis();
		try{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			// 设置充值平台
			order.setSavingPlatform("腾讯YSDK");
			// 设置充值介质
			order.setSavingMedium("米大师充值");
			// 设置充值人
			order.setPassportId(passport.getId());
			// 设置q币数
			order.setGoodsCount(1);
			// 设置消费金额 转换
			order.setPayMoney(new Long(addAmount));
			// 设置serverid
			order.setServerName(servername);
			//QQ
			order.setHandleResult(-1);
			order.setHandleResultDesp("新生成订单");
			// 设置订单responseResult
			order.setResponseResult(-1);
			order.setResponseDesp("新生成订单");
			// 设置是否通知游戏服状态 设为false
			order.setNotified(false);
			// 设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(os);
			// 先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			// 设置订单号
			order.setOrderId("ysdk"+DateUtil.formatDate(new Date(), "yyyy-MM-dd") + order.getId());
			// orderFormManager.getEm().notifyFieldChange(order, "orderId");
			// orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[YSDK生成订单-] [成功] [更新订单信息成功] [用户名:" + passport.getUserName() + "] [channel:"+channel+"] [serverName:" + servername + "] [充值平台："+order.getSavingPlatform()+"] [充值介质:"+order.getSavingMedium()+"] [Q币数量：" + addAmount/100 + "] [充值面值:"+order.getPayMoney()+"分] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [角色id:"+order.getMemo1()+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms]");
			return order.getOrderId();
		}catch(Exception e){
			PlatformSavingCenter.logger.warn("[YSDK生成订单-] [异常] [username:"+(passport == null?"null":passport.getUserName())+"] [channel:"+channel+"] [servername:"+servername+"] [Q币数量:"+addAmount/100+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms] ["+e+"]");
			return null;
		}
	} 
	
	public String msdkExchange(Passport passport, String channel, String servername, int addAmount, String os){
		long startTime = System.currentTimeMillis();
		try{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			// 设置充值平台
			order.setSavingPlatform("腾讯MSDK");
			// 设置充值介质
			order.setSavingMedium("MSDK兑换");
			// 设置充值人
			order.setPassportId(passport.getId());
			// 设置q币数
			order.setGoodsCount(1);
			// 设置消费金额 转换
			order.setPayMoney(new Long(addAmount));
			// 设置serverid
			order.setServerName(servername);
			//QQ
			order.setHandleResult(-1);
			order.setHandleResultDesp("新生成订单");
			// 设置订单responseResult
			order.setResponseResult(-1);
			order.setResponseDesp("新生成订单");
			// 设置是否通知游戏服状态 设为false
			order.setNotified(false);
			// 设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(os);
			// 先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			// 设置订单号
			order.setOrderId("msdk"+DateUtil.formatDate(new Date(), "yyyy-MM-dd") + order.getId());
			// orderFormManager.getEm().notifyFieldChange(order, "orderId");
			// orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[MSDK兑换生成订单-] [成功] [更新订单信息成功] [用户名:" + passport.getUserName() + "] [channel:"+channel+"] [serverName:" + servername + "] [充值平台："+order.getSavingPlatform()+"] [充值介质:"+order.getSavingMedium()+"] [Q币数量：" + addAmount/100 + "] [充值面值:"+order.getPayMoney()+"分] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [角色id:"+order.getMemo1()+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms]");
			return order.getOrderId()+"####"+ZOND_ID;
		}catch(Exception e){
			PlatformSavingCenter.logger.warn("[MSDK兑换生成订单-] [异常] [username:"+(passport == null?"null":passport.getUserName())+"] [channel:"+channel+"] [servername:"+servername+"] [Q币数量:"+addAmount/100+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms] ["+e+"]");
			return null;
		}
	} 
	
	public String msdkExchange(Passport passport, String channel, String servername, int addAmount, String os,String[]others){
		long startTime = System.currentTimeMillis();
		try{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			// 设置充值平台
			order.setSavingPlatform("腾讯MSDK");
			// 设置充值介质
			order.setSavingMedium("MSDK兑换");
			// 设置充值人
			order.setPassportId(passport.getId());
			// 设置q币数
			order.setGoodsCount(1);
			// 设置消费金额 转换
			order.setPayMoney(new Long(addAmount));
			// 设置serverid
			order.setServerName(servername);
			//QQ
			order.setHandleResult(-1);
			order.setHandleResultDesp("新生成订单");
			// 设置订单responseResult
			order.setResponseResult(-1);
			order.setResponseDesp("新生成订单");
			// 设置是否通知游戏服状态 设为false
			order.setNotified(false);
			// 设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			// 先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			// 设置订单号
			order.setOrderId("msdk"+DateUtil.formatDate(new Date(), "yyyy-MM-dd") + order.getId());
			// orderFormManager.getEm().notifyFieldChange(order, "orderId");
			// orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[MSDK兑换生成订单] [成功] [更新订单信息成功] [用户名:" + passport.getUserName() + "] [channel:"+channel+"] [serverName:" + servername + "] [充值平台："+order.getSavingPlatform()+"] [充值介质:"+order.getSavingMedium()+"] [Q币数量：" + addAmount/100 + "] [充值面值:"+order.getPayMoney()+"分] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [角色id:"+order.getMemo1()+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms]");
			return order.getOrderId()+"####"+ZOND_ID;
		}catch(Exception e){
			PlatformSavingCenter.logger.warn("[MSDK兑换生成订单] [异常] [username:"+(passport == null?"null":passport.getUserName())+"] [channel:"+channel+"] [servername:"+servername+"] [Q币数量:"+addAmount/100+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms] ["+e+"]");
			return null;
		}
	} 
	
	
	public String ysdkExchange(Passport passport, String channel, String servername, int addAmount, String os){
		long startTime = System.currentTimeMillis();
		try{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			// 设置充值平台
			order.setSavingPlatform("腾讯YSDK");
			// 设置充值介质
			order.setSavingMedium("米大师兑换");
			// 设置充值人
			order.setPassportId(passport.getId());
			// 设置q币数
			order.setGoodsCount(1);
			// 设置消费金额 转换
			order.setPayMoney(new Long(addAmount));
			// 设置serverid
			order.setServerName(servername);
			//QQ
			order.setHandleResult(-1);
			order.setHandleResultDesp("新生成订单");
			// 设置订单responseResult
			order.setResponseResult(-1);
			order.setResponseDesp("新生成订单");
			// 设置是否通知游戏服状态 设为false
			order.setNotified(false);
			// 设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(os);
			// 先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			// 设置订单号
			order.setOrderId("ysdk"+DateUtil.formatDate(new Date(), "yyyy-MM-dd") + order.getId());
			// orderFormManager.getEm().notifyFieldChange(order, "orderId");
			// orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[YSDK兑换生成订单-] [成功] [更新订单信息成功] [用户名:" + passport.getUserName() + "] [channel:"+channel+"] [serverName:" + servername + "] [充值平台："+order.getSavingPlatform()+"] [充值介质:"+order.getSavingMedium()+"] [Q币数量：" + addAmount/100 + "] [充值面值:"+order.getPayMoney()+"分] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [角色id:"+order.getMemo1()+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms]");
			return order.getOrderId()+"####"+ZOND_ID;
		}catch(Exception e){
			PlatformSavingCenter.logger.warn("[YSDK兑换生成订单-] [异常] [username:"+(passport == null?"null":passport.getUserName())+"] [channel:"+channel+"] [servername:"+servername+"] [Q币数量:"+addAmount/100+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms] ["+e+"]");
			return null;
		}
	} 
	
	public String ysdkExchange(Passport passport, String channel, String servername, int addAmount, String os,String[]others){
		long startTime = System.currentTimeMillis();
		try{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			// 设置充值平台
			order.setSavingPlatform("腾讯YSDK");
			// 设置充值介质
			order.setSavingMedium("米大师兑换");
			// 设置充值人
			order.setPassportId(passport.getId());
			// 设置q币数
			order.setGoodsCount(1);
			// 设置消费金额 转换
			order.setPayMoney(new Long(addAmount));
			// 设置serverid
			order.setServerName(servername);
			//QQ
			order.setHandleResult(-1);
			order.setHandleResultDesp("新生成订单");
			// 设置订单responseResult
			order.setResponseResult(-1);
			order.setResponseDesp("新生成订单");
			// 设置是否通知游戏服状态 设为false
			order.setNotified(false);
			// 设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			
			// 先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			// 设置订单号
			order.setOrderId("ysdk"+DateUtil.formatDate(new Date(), "yyyy-MM-dd") + order.getId());
			// orderFormManager.getEm().notifyFieldChange(order, "orderId");
			// orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[YSDK兑换生成订单] [成功] [更新订单信息成功] [用户名:" + passport.getUserName() + "] [channel:"+channel+"] [serverName:" + servername + "] [充值平台："+order.getSavingPlatform()+"] [充值介质:"+order.getSavingMedium()+"] [Q币数量：" + addAmount/100 + "] [充值面值:"+order.getPayMoney()+"分] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [角色id:"+order.getMemo1()+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms]");
			return order.getOrderId()+"####"+ZOND_ID;
		}catch(Exception e){
			PlatformSavingCenter.logger.warn("[YSDK兑换生成订单] [异常] [username:"+(passport == null?"null":passport.getUserName())+"] [channel:"+channel+"] [servername:"+servername+"] [Q币数量:"+addAmount/100+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms] ["+e+"]");
			return null;
		}
	} 
	
	public String msdkSaving(Passport passport, String channel, String servername, int addAmount, String os){
		long startTime = System.currentTimeMillis();
		try{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			// 设置充值平台
			order.setSavingPlatform("腾讯MSDK");
			// 设置充值介质
			order.setSavingMedium("MSDK充值");
			// 设置充值人
			order.setPassportId(passport.getId());
			// 设置Q币数
			order.setGoodsCount(1);
			// 设置消费金额 转换
			order.setPayMoney(new Long(addAmount));
			// 设置serverid
			order.setServerName(servername);
			//QQ
			order.setHandleResult(-1);
			order.setHandleResultDesp("新生成订单");
			// 设置订单responseResult
			order.setResponseResult(-1);
			order.setResponseDesp("新生成订单");
			// 设置是否通知游戏服状态 设为false
			order.setNotified(false);
			// 设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(os);
			// 先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			// 设置订单号
			order.setOrderId("msdk"+DateUtil.formatDate(new Date(), "yyyy-MM-dd") + order.getId());
			// orderFormManager.getEm().notifyFieldChange(order, "orderId");
			// orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[MSDK充值生成订单-] [成功] [更新订单信息成功] [用户名:" + passport.getUserName() + "] [channel:"+channel+"] [serverName:" + servername + "] [充值平台："+order.getSavingPlatform()+"] [充值介质:"+order.getSavingMedium()+"] [Q币数量：" + addAmount/100 + "] [充值面值:"+order.getPayMoney()+"分] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [角色id:"+order.getMemo1()+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms]");
			return order.getOrderId()+"####"+ZOND_ID;
		}catch(Exception e){
			PlatformSavingCenter.logger.warn("[MSDK充值生成订单-] [异常] [username:"+(passport == null?"null":passport.getUserName())+"] [channel:"+channel+"] [servername:"+servername+"] [Q币数量:"+addAmount/100+"] [cost:"+ (System.currentTimeMillis() - startTime) + "ms] ["+e+"]");
			return null;
		}
	} 
	
	/**
	 * appId,appKey微信，手Q是一样的
	 * @param ip
	 * @param channel
	 * @param userId
	 * @param openkey
	 * @param pf
	 * @param pay_token
	 * @param zoneid
	 * @param pfkey
	 * @return
	 * @throws OpensnsException
	 */
	public int queryYSDKGameGold(String ip,String channel,String userId,String openkey,String pf,String pay_token,String zoneid,String pfkey) throws OpensnsException{
		
		String qUrl = query_ysdk_url;
		String uri = "/mpay/get_balance_m";
		
		String openid = "";
		if(userId.contains("QQUSER_")){
			openid = userId.replace("QQUSER_", "");
		}
		//Cookies
		Map<String, String> cookies = new HashMap<String, String>();
		cookies.put("org_loc", SnsSigCheck.encodeUrl(uri));
		
		
		//请求参数
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		if(userId.contains("@@wx##_")){
			openid = openid.replace("@@wx##_", "");
			params.put("appid",MSDK_APPID);
			cookies.put("session_id", SnsSigCheck.encodeUrl("hy_gameid"));
			cookies.put("session_type", SnsSigCheck.encodeUrl("wc_actoken"));
		}else{
			params.put("appid",MSDK_APPID);
			cookies.put("session_id", SnsSigCheck.encodeUrl("openid"));
			cookies.put("session_type", SnsSigCheck.encodeUrl("kp_actoken"));
		}
		String cookie = buildParam(cookies, ";");
		params.put("format","json");
		params.put("openid",openid);
		params.put("openkey", pay_token);
		params.put("pf",pf);
		params.put("pfkey",pfkey);
		params.put("ts",Long.toString(System.currentTimeMillis() / 1000));
		params.put("userip",ip);
		params.put("zoneid",zoneid);
		//源串是由3部分内容用“&”拼接起来的： HTTP请求方式 & urlencode(uri) & urlencode(a=x&b=y&...)
		//特别注意构造签名里URI需要在实际的URI前加入/v3/r/,比如请求/mpay/get_balance_m,则签名时的uri路径为/v3/r/mpay/get_balance_m
		//String url = buildParam(params,"&");//params不用转码
		//String uriEnc = SnsSigCheck.encodeUrl("/v3/r"+uri);
		//String urlEnc = SnsSigCheck.encodeUrl(url);
		String sig = "-";
		String 源串 = "";//"GET&"+uriEnc+"&"+urlEnc;
		//得到的密钥对源串加密。appkey不同
		try {
			if(userId.contains("@@wx##_")){
//				Mac mac = Mac.getInstance("HmacSHA1");  
//			 	SecretKeySpec signingKey = new SecretKeySpec((WXSDK_APPKEY+"&").getBytes("UTF-8"), mac.getAlgorithm());  
//		        mac.init(signingKey);  
//		        byte[] rawHmac = mac.doFinal(源串.getBytes("UTF-8"));  
//		        sig = new String(Base64Coder.encode(rawHmac));
				sig = SnsSigCheck.makeSig("GET", "/v3/r"+uri, params, MSDK_APPKEY+"&");  //uri,申请的appkey, 参数后需要加&
			}else{
//				Mac mac = Mac.getInstance("HmacSHA1");  
//			 	SecretKeySpec signingKey = new SecretKeySpec((MSDK_APPKEY+"&").getBytes("UTF-8"), mac.getAlgorithm());  
//		        mac.init(signingKey);  
//		        byte[] rawHmac = mac.doFinal(源串.getBytes("UTF-8"));  
//		        sig = new String(Base64Coder.encode(rawHmac));
				sig = SnsSigCheck.makeSig("GET", "/v3/r"+uri, params, MSDK_APPKEY+"&");  //uri,申请的appkey, 参数后需要加&
			}
			params.put("sig",SnsSigCheck.encodeUrl(sig));  //sig需要转码
		} catch (Exception e1) {
			e1.printStackTrace();
			PlatformSavingCenter.logger.warn("[YSDK查询余额] [生成签名失败] [channel:"+channel+"] [sig:"+sig+"] [userId:"+userId+"] [openkey:"+openkey+"] [pay_token:"+pay_token+"] [zoneid:"+zoneid+"] [pfkey:"+pfkey+"] ["+e1+"]");
			return -1;
		}
		String url = buildParam(params,"&");//params不用转码
		
		Map headers = new HashMap();
		String queryUrlStr = "";
		String content = "-";
		try {
			headers.put("Cookie", cookie);
			queryUrlStr = qUrl+uri+"?"+url;
			byte bs[] = HttpUtils.webGet(new URL(queryUrlStr), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bs,encoding);
			JacksonManager jm = JacksonManager.getInstance();
			Map map = (Map)jm.jsonDecodeObject(content);
			
			Integer ret = (Integer)map.get("ret");					//返回码 0：成功；1001：参数错误 ；1018：登陆校验失败。
			Integer balance = (Integer)map.get("balance");			//游戏币个数（包含了赠送游戏币）
			Integer gen_balance = (Integer)map.get("gen_balance");	//赠送游戏币个数
			Integer first_save = (Integer)map.get("first_save");	//是否满足首次充值，1：满足，0：不满足
			Integer save_amt = (Integer)map.get("save_amt");		//累计充值金额
			if(ret != null && ret == 0){
				PlatformSavingCenter.logger.warn("[YSDK查询余额] [成功] [源串:"+源串+"] [channel:"+channel+"] [状态码:"+ret+"] [sig:"+sig+"] [游戏币数:"+balance+"] ["+gen_balance+"] [first_save:"+first_save+"] [累计充值金额:"+save_amt+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
				if(balance != null){
					return balance.intValue();
				}
			}else{
				PlatformSavingCenter.logger.warn("[YSDK查询余额] [失败] [源串:"+源串+"] [channel:"+channel+"] [状态码:"+ret+"] [sig:"+sig+"] [游戏币数:"+balance+"] ["+gen_balance+"] [first_save:"+first_save+"] [累计充值金额:"+save_amt+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
			}
		} catch (Exception e) {
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			e.printStackTrace();
			PlatformSavingCenter.logger.warn("[YSDK查询余额] [异常] [channel:"+channel+"] [encoding:"+encoding+"] [sig:"+sig+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [queryUrlStr:"+queryUrlStr+"] ["+e+"]");
		}
		return -1;
	}
	
	/**
	 * msdk充值成功,查询,兑换游戏币
	 * @throws OpensnsException 
	 */
	public int queryGameGold(String channel,String userId,String openkey,String pf,String pay_token,String zoneid,String pfkey) throws OpensnsException{
		
		String qUrl = query_url;
		String uri = "/mpay/get_balance_m";
		String openid = "";
		if(userId.contains("QQUSER_")){
			openid = userId.replace("QQUSER_", "");
		}
		//Cookies
		Map<String, String> cookies = new HashMap<String, String>();
		cookies.put("org_loc", SnsSigCheck.encodeUrl(uri));
		
		
		//请求参数
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		if(userId.contains("@@wx##_")){
			openid = openid.replace("@@wx##_", "");
			params.put("appid",MSDK_APPID);
			cookies.put("session_id", SnsSigCheck.encodeUrl("hy_gameid"));
			cookies.put("session_type", SnsSigCheck.encodeUrl("wc_actoken"));
			params.put("pay_token","");
		}else{
			params.put("appid",MSDK_APPID);
			cookies.put("session_id", SnsSigCheck.encodeUrl("openid"));
			cookies.put("session_type", SnsSigCheck.encodeUrl("kp_actoken"));
			params.put("pay_token",pay_token);
		}
		String cookie = buildParam(cookies, ";");
		params.put("format","json");
		params.put("openid",openid);
		params.put("openkey", openkey);
		params.put("pf",pf);
		params.put("pfkey",pfkey);
		params.put("ts",Long.toString(System.currentTimeMillis() / 1000));
		params.put("zoneid",zoneid);
		String sig = "-";
		try {
			if(userId.contains("@@wx##_")){
				sig = SnsSigCheck.makeSig("GET", uri, params, MSDK_APPKEY+"&");  //uri,申请的appkey, 参数后需要加&
			}else{
				sig = SnsSigCheck.makeSig("GET", uri, params, MSDK_APPKEY+"&");  //uri,申请的appkey, 参数后需要加&
			}
			params.put("sig",SnsSigCheck.encodeUrl(sig));  //sig需要转码
		} catch (Exception e1) {
			e1.printStackTrace();
			PlatformSavingCenter.logger.warn("[MSDK查询余额] [生成签名失败] [channel:"+channel+"] [sig:"+sig+"] [userId:"+userId+"] [openkey:"+openkey+"] [pay_token:"+pay_token+"] [zoneid:"+zoneid+"] [pfkey:"+pfkey+"] ["+e1+"]");
			return -1;
		}
	    String url = buildParam(params,"&");//params不用转码
		Map headers = new HashMap();
		String queryUrlStr = "";
		String content = "-";
		try {
			headers.put("Cookie", cookie);
			queryUrlStr = qUrl+uri+"?"+url;
			byte bs[] = HttpUtils.webGet(new URL(queryUrlStr), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bs,encoding);
			JacksonManager jm = JacksonManager.getInstance();
			Map map = (Map)jm.jsonDecodeObject(content);
			
			Integer ret = (Integer)map.get("ret");					//返回码 0：成功；1001：参数错误 ；1018：登陆校验失败。
			Integer balance = (Integer)map.get("balance");			//游戏币个数（包含了赠送游戏币）
			Integer gen_balance = (Integer)map.get("gen_balance");	//赠送游戏币个数
			Integer first_save = (Integer)map.get("first_save");	//是否满足首次充值，1：满足，0：不满足
			Integer save_amt = (Integer)map.get("save_amt");		//累计充值金额
			if(ret != null && ret == 0){
				PlatformSavingCenter.logger.warn("[MSDK查询余额] [成功] [channel:"+channel+"] [状态码:"+ret+"] [sig:"+sig+"] [游戏币数:"+balance+"] ["+gen_balance+"] [first_save:"+first_save+"] [累计充值金额:"+save_amt+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
				if(balance != null){
					return balance.intValue();
				}
			}else{
				PlatformSavingCenter.logger.warn("[MSDK查询余额] [失败] [channel:"+channel+"] [状态码:"+ret+"] [sig:"+sig+"] [游戏币数:"+balance+"] ["+gen_balance+"] [first_save:"+first_save+"] [累计充值金额:"+save_amt+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
			}
		} catch (Exception e) {
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			e.printStackTrace();
			PlatformSavingCenter.logger.warn("[MSDK查询余额] [异常] [channel:"+channel+"] [encoding:"+encoding+"] [sig:"+sig+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [queryUrlStr:"+queryUrlStr+"] ["+e+"]");
		}
		return -1;
	}
	
	/**
	 * 通知玩家是否全部兑换
	 */
	public void notifyPlayerToExchangeSilver(OrderForm order,long golds){
		try{
			ServerManager sm = ServerManager.getInstance();
			Server server = sm.getServer(order.getServerName());
			
			HashMap headers = new HashMap();
			headers.put("_notifyPlayerExchange_", "ok");
			headers.put("_notifyPlayerExchange_orderid", order.getOrderId()+"");
			headers.put("_notifyPlayerExchange_golds", golds+"");
			String argstr = "";
			if(order.getMemo1() != null && !order.getMemo1().isEmpty()){
				argstr =  order.getMemo1();
				headers.put("_notifyPlayerExchange_playerid", order.getMemo1());
			}else{
				PassportManager pm = PassportManager.getInstance();
	    		Passport passport = pm.getPassport(order.getPassportId());
	    		argstr = passport.getUserName();
				headers.put("_notifyPlayerExchange_username", passport.getUserName());
			}
			byte bytes[] = HttpUtils.webPost(new java.net.URL(server.getSavingNotifyUrl()), new byte[0], headers, 60000, 60000);

			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			String result = new String(bytes,encoding);
			PlatformSavingCenter.logger.warn("[MSDK通知玩家兑换银两] [成功] [golds:"+golds+"] [argstr:"+argstr+"] [result:"+result+"] [pid:"+order.getMemo1()+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [充值人:"+order.getPassportId()+"] [充值金额:"+order.getPayMoney()+"] ["+order.getLogStr()+"]");
		}catch(Exception e){
			e.printStackTrace();
			PlatformSavingCenter.logger.warn("[MSDK通知玩家兑换银两] [异常] [golds:"+golds+"] [passportid:"+order.getPassportId()+"] [pid:"+order.getMemo1()+"] [充值金额:"+order.getPayMoney()+"] ["+order.getLogStr()+"] ["+e+"] ");
		}
	}
	/**
	 * ysdk扣钱
	 * @param channel
	 * @param userId
	 * @param openkey
	 * @param pf
	 * @param pay_token
	 * @param zoneid
	 * @param pfkey
	 * @param amt
	 * @return
	 * @throws OpensnsException
	 */
	public String costYSDKTencentGlods(String orderId,String channel ,String userId,String openkey,String pf,String pay_token,String zoneid,String pfkey,String amt) throws OpensnsException{
		
		String qUrl = query_ysdk_url;
		String uri = "/mpay/pay_m";
		String openid = "";
		if(userId.contains("QQUSER_")){
			openid = userId.replace("QQUSER_", "");
		}
		//Cookies
		Map<String, String> cookies = new HashMap<String, String>();
		//请求参数
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		cookies.put("org_loc", SnsSigCheck.encodeUrl(uri));
		params.put("amt",amt);
		params.put("billno",orderId);
		if(userId.contains("@@wx##_")){
			openid = openid.replace("@@wx##_", "");
			params.put("appid",MSDK_APPID);
			cookies.put("session_id", SnsSigCheck.encodeUrl("hy_gameid"));
			cookies.put("session_type", SnsSigCheck.encodeUrl("wc_actoken"));
		}else{
			params.put("appid",MSDK_APPID);
			cookies.put("session_id", SnsSigCheck.encodeUrl("openid"));
			cookies.put("session_type", SnsSigCheck.encodeUrl("kp_actoken"));
		}
		String cookie = buildParam(cookies, ";");
		
		params.put("format","json");
		params.put("openid",openid);
		params.put("openkey", pay_token);
		params.put("pf",pf);
		params.put("pfkey",pfkey);
		params.put("ts",Long.toString(System.currentTimeMillis() / 1000));
		params.put("zoneid",zoneid);
		String sig = "-";
		try {
			if(userId.contains("@@wx##_")){
				sig = SnsSigCheck.makeSig("GET", "/v3/r"+uri, params, MSDK_APPKEY+"&");  //uri,申请的appkey, 参数后需要加&
			}else{
				sig = SnsSigCheck.makeSig("GET","/v3/r"+uri, params, MSDK_APPKEY+"&");  //uri,申请的appkey, 参数后需要加&
			}
			params.put("sig",SnsSigCheck.encodeUrl(sig));  //sig需要转码
		} catch (Exception e1) {
			e1.printStackTrace();
			PlatformSavingCenter.logger.warn("[YSDK扣除游戏币] [生成签名失败] [channel:"+channel+"] [sig:"+sig+"] [userId:"+userId+"] [openkey:"+openkey+"] [pay_token:"+pay_token+"] [zoneid:"+zoneid+"] [pfkey:"+pfkey+"] ["+e1+"]");
			return "生成签名失败";
		}
		String url = buildParam(params,"&");//params不用转码
		Map headers = new HashMap();
		String queryUrlStr = "";
		String content = "-";
		try {
			headers.put("Cookie", cookie);
			queryUrlStr = qUrl+uri+"?"+url;
			byte bs[] = HttpUtils.webGet(new URL(queryUrlStr), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bs,encoding);
			JacksonManager jm = JacksonManager.getInstance();
			Map map = (Map)jm.jsonDecodeObject(content);
			
			Integer ret = (Integer)map.get("ret");					//0：表示成功, >=1000 表示失败 1004：余额不足 1018：登陆校验失败 其它：失败
			Integer balance = (Integer)map.get("balance");			//预扣后的余额
			String billno = (String)map.get("billno");			//预扣流水号
			if(ret != null && ret == 0){
				PlatformSavingCenter.logger.warn("[YSDK扣除游戏币] [成功] [channel:"+channel+"] [状态码:"+ret+"] [游戏币数:"+balance+"] [billno:"+billno+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
				if(balance != null){
					return "ok";
				}
			}
			if(ret != null && ret == 1004){
				PlatformSavingCenter.logger.warn("[YSDK扣除游戏币] [失败] [channel:"+channel+"] [状态码:"+ret+"] [游戏币数:"+balance+"] [billno:"+billno+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
				return String.valueOf("余额不足");
			}
			if(ret != null){
				PlatformSavingCenter.logger.warn("[YSDK扣除游戏币] [失败2] [channel:"+channel+"] [状态码:"+ret+"] [游戏币数:"+balance+"] [billno:"+billno+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
				return String.valueOf("扣费失败");
			}
		} catch (Exception e) {
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			e.printStackTrace();
			PlatformSavingCenter.logger.warn("[YSDK扣除游戏币] [异常] [channel:"+channel+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"] ["+e+"]");
		}
		return "失败";
	}
	/**
	 * 扣钱
	 * @return
	 * @throws OpensnsException 
	 */
	public String costTencentGlods(String channel ,String userId,String openkey,String pf,String pay_token,String zoneid,String pfkey,String amt) throws OpensnsException{
	
		String qUrl = query_url;
		if(channel != null && channel.contains("QQYSDK_MIESHI")){
			qUrl = query_ysdk_url;
		}
		String uri = "/mpay/pay_m";
		String openid = "";
		if(userId.contains("QQUSER_")){
			openid = userId.replace("QQUSER_", "");
		}
		//Cookies
		Map<String, String> cookies = new HashMap<String, String>();
		//请求参数
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		cookies.put("org_loc", SnsSigCheck.encodeUrl(uri));
		params.put("amt",amt);
		if(userId.contains("@@wx##_")){
			openid = openid.replace("@@wx##_", "");
			params.put("appid",MSDK_APPID);
			cookies.put("session_id", SnsSigCheck.encodeUrl("hy_gameid"));
			cookies.put("session_type", SnsSigCheck.encodeUrl("wc_actoken"));
			params.put("pay_token","");
		}else{
			params.put("appid",MSDK_APPID);
			cookies.put("session_id", SnsSigCheck.encodeUrl("openid"));
			cookies.put("session_type", SnsSigCheck.encodeUrl("kp_actoken"));
			params.put("pay_token",pay_token);
		}
		String cookie = buildParam(cookies, ";");
		
		params.put("format","json");
		params.put("openid",openid);
		params.put("openkey", openkey);
		params.put("pf",pf);
		params.put("pfkey",pfkey);
		params.put("ts",Long.toString(System.currentTimeMillis() / 1000));
		params.put("zoneid",zoneid);
		String sig = "-";
		try {
			if(userId.contains("@@wx##_")){
				sig = SnsSigCheck.makeSig("GET", uri, params, MSDK_APPKEY+"&");  //uri,申请的appkey, 参数后需要加&
			}else{
				sig = SnsSigCheck.makeSig("GET", uri, params, MSDK_APPKEY+"&");  //uri,申请的appkey, 参数后需要加&
			}
			params.put("sig",SnsSigCheck.encodeUrl(sig));  //sig需要转码
		} catch (Exception e1) {
			e1.printStackTrace();
			PlatformSavingCenter.logger.warn("[MSDK扣除游戏币] [生成签名失败] [channel:"+channel+"] [sig:"+sig+"] [userId:"+userId+"] [openkey:"+openkey+"] [pay_token:"+pay_token+"] [zoneid:"+zoneid+"] [pfkey:"+pfkey+"] ["+e1+"]");
			return "生成签名失败";
		}
		String url = buildParam(params,"&");//params不用转码
		Map headers = new HashMap();
		String queryUrlStr = "";
		String content = "-";
		try {
			headers.put("Cookie", cookie);
			queryUrlStr = qUrl+uri+"?"+url;
			byte bs[] = HttpUtils.webGet(new URL(queryUrlStr), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bs,encoding);
			JacksonManager jm = JacksonManager.getInstance();
			Map map = (Map)jm.jsonDecodeObject(content);
			
			Integer ret = (Integer)map.get("ret");					//0：表示成功, >=1000 表示失败 1004：余额不足 1018：登陆校验失败 其它：失败
			Integer balance = (Integer)map.get("balance");			//预扣后的余额
			String billno = (String)map.get("billno");			//预扣流水号
			if(ret != null && ret == 0){
				PlatformSavingCenter.logger.warn("[MSDK扣除游戏币] [成功] [channel:"+channel+"] [状态码:"+ret+"] [游戏币数:"+balance+"] [billno:"+billno+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
				if(balance != null){
					return "ok";
				}
			}
			if(ret != null && ret == 1004){
				PlatformSavingCenter.logger.warn("[MSDK扣除游戏币] [失败] [channel:"+channel+"] [状态码:"+ret+"] [游戏币数:"+balance+"] [billno:"+billno+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
				return String.valueOf("余额不足");
			}
			if(ret != null){
				PlatformSavingCenter.logger.warn("[MSDK扣除游戏币] [失败] [channel:"+channel+"] [状态码:"+ret+"] [游戏币数:"+balance+"] [billno:"+billno+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
				return String.valueOf("扣费失败");
			}
		} catch (Exception e) {
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			e.printStackTrace();
			PlatformSavingCenter.logger.warn("[MSDK扣除游戏币] [异常] [channel:"+channel+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"] ["+e+"]");
		}
		return "失败";
	}
	
	/**
	 * 取消支付
	 * @throws OpensnsException 
	 */
	public String cancelPayMoney(String userId,String openkey,String pf,String pay_token,String zoneid,String pfkey,String amt,String billno) throws OpensnsException{
		String uri = "/mpay/cancel_pay_m";
		String openid = "";
		if(userId.contains("QQUSER_")){
			openid = userId.replace("QQUSER_", "");
		}
		//Cookies
		Map<String, String> cookies = new HashMap<String, String>();
		cookies.put("org_loc", URLEncoder.encode(uri));
		
		
		//请求参数
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("amt",amt);
		if(userId.contains("@@wx##_")){
			openid = openid.replace("@@wx##_", "");
			params.put("appid",MSDK_APPID);
			cookies.put("session_id", SnsSigCheck.encodeUrl("hy_gameid"));
			cookies.put("session_type", SnsSigCheck.encodeUrl("wc_actoken"));
			params.put("pay_token","");
		}else{
			params.put("appid",MSDK_APPID);
			cookies.put("session_id", SnsSigCheck.encodeUrl("openid"));
			cookies.put("session_type", SnsSigCheck.encodeUrl("kp_actoken"));
			params.put("pay_token",pay_token);
		}
		String cookie = buildParams(cookies);
		params.put("billno",billno);
		params.put("format","json");
		params.put("openid",openid);
		params.put("openkey", openkey);
		params.put("pf",pf);
		params.put("pfkey",pfkey);
		params.put("ts",Long.toString(System.currentTimeMillis() / 1000));
		params.put("zoneid",zoneid);
		String sig = "-";
		try {
			sig = SnsSigCheck.makeSig("GET", uri, params, MSDK_APPKEY+"&");  //uri,申请的appkey, 参数后需要加&
			params.put("sig",URLEncoder.encode(sig));  //sig需要转码
		} catch (Exception e1) {
			e1.printStackTrace();
			PlatformSavingCenter.logger.warn("[MSDK扣除游戏币] [生成签名失败] [sig:"+sig+"] [userId:"+userId+"] [openkey:"+openkey+"] [pay_token:"+pay_token+"] [zoneid:"+zoneid+"] [pfkey:"+pfkey+"] ["+e1+"]");
			return "生成签名失败";
		}
	    String url = buildParams(params);//params不用转码
		Map headers = new HashMap();
		String queryUrlStr = "";
		String content = "-";
		try {
			headers.put("Cookie", cookie);
			queryUrlStr = query_url+uri+"?"+url;
			byte bs[] = HttpUtils.webGet(new URL(queryUrlStr), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bs,encoding);
			JacksonManager jm = JacksonManager.getInstance();
			Map map = (Map)jm.jsonDecodeObject(content);
			
			Integer ret = (Integer)map.get("ret");					//0:成功,1018:登陆校验失败,其它:失败
			if(ret != null && ret == 0){
				PlatformSavingCenter.logger.warn("[MSDK扣除游戏币] [成功] [状态码:"+ret+"] [billno:"+billno+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
				return "ok";
			}else{
				PlatformSavingCenter.logger.warn("[MSDK扣除游戏币] [失败] [状态码:"+ret+"] [billno:"+billno+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"]");
				return String.valueOf(ret);
			}
		} catch (Exception e) {
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			e.printStackTrace();
			PlatformSavingCenter.logger.warn("[MSDK扣除游戏币] [异常] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [cookie:"+cookie+"] [queryUrlStr:"+queryUrlStr+"] ["+e+"]");
		}
		return "失败";
	}
	
	private String buildParams(Map<String,String> params){
		String[] key = params.keySet().toArray(new String[params.size()]);

		StringBuffer sb = new StringBuffer();
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(params.get(k));
			sb.append("&");
		}
		
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	public static String buildParam(Map<String, String> params, String st){
		String[] key = params.keySet().toArray(new String[params.size()]);
		StringBuffer sb = new StringBuffer();
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(params.get(k));
			sb.append(st);
		}
		String content = sb.toString().substring(0, sb.length() - 1);
		return content;
	}
	
}
