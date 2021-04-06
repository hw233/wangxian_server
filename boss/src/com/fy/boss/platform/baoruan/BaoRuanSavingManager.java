package com.fy.boss.platform.baoruan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.finance.service.platform.Base64;
import com.fy.boss.platform.baoruan.BaoRuanSavingManager;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class BaoRuanSavingManager {
	static BaoRuanSavingManager self;
	
//原来的	public static final String RECHARGE_URL = "http://game.lewan.cn/merchant/payment/receiveconversion";
	public static final String RECHARGE_URL = "http://www.baoruan.com/nested/account/login/";
	
	private String appid = "135833496863804037";
//	private String cid = "135830493976591001";
	private String cid = "801";
/*	private String uniquekey = "D9iB5BsD8KuYbtuxornbQQuDzkRQDgnh";
	private String financekey = "hXyRXqLR2IoRFtBYFpq4wnQVuFTs6U8m";*/
	private String financekey = "839433b48f56cc5441f60cf46f0c7d07";
	
	//private String callBackUrl = "http://112.25.14.13:9110/mieshi_game_boss/baoruanResult?my_order=";
	private String callBackUrl = "http://116.213.192.216:9110/mieshi_game_boss/baoruanResult?my_order=";
	
	
	public static Map<String, Integer> cardTypeMap = new HashMap<String,Integer>();
	
	
	public void initialize(){
		BaoRuanSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	static {
		cardTypeMap.put("移动充值卡",4);
		cardTypeMap.put("联通一卡付",3);
		cardTypeMap.put("电信充值卡",3);
		cardTypeMap.put("骏网一卡通",3);
		cardTypeMap.put("盛大卡",3);
		cardTypeMap.put( "征途卡",3);
		cardTypeMap.put("久游卡",3);
		cardTypeMap.put( "易宝e卡通",3);
		cardTypeMap.put("网易卡",3);
		cardTypeMap.put("完美卡",3 );
		cardTypeMap.put("搜狐卡",3 );
		cardTypeMap.put("纵游一卡通",3 );
		cardTypeMap.put("天下一卡通",3 );
		cardTypeMap.put("天宏一卡通",3 );
		cardTypeMap.put("支付宝WAP充值",11);
		cardTypeMap.put("财付通WAP充值",12);
		cardTypeMap.put("PP易宝充值卡",3);
		cardTypeMap.put("PP移动充值卡",4);
	}
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel){
		long startTime = System.currentTimeMillis();
		String savingMedium = "宝币";
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("宝软（乐玩）");
		//设置充值介质
		order.setSavingMedium(savingMedium);
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(1);
		//设置消费金额
		order.setPayMoney(payAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
		order.setHandleResultDesp("宝软（乐玩）新生成订单");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("宝软（乐玩）新生成订单");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(mobileOs);
		order.setUserChannel(userChannel);
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		//设置订单号
		order.setOrderId("br-"+DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+ order.getId());
		String  returnCallbackUrl  = callBackUrl;
		returnCallbackUrl += order.getOrderId();
		
		try
		{
			orderFormManager.updateOrderForm(order, "orderId");
			PlatformSavingCenter.logger.info("[宝软（乐玩）充值订单生成] [成功] [更新订单信息] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[宝软（乐玩）充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		
		//拼装url 返给客户端
		LinkedHashMap<String, String> params = new LinkedHashMap<String,String>();
		String uid = passport.getUserName();
		if(!StringUtils.isEmpty(uid))
		{
			if(uid.startsWith("BAORUANUSER_")) {
				uid = uid.split("_")[1];
			}
		}
		params.put("cid", cid);
		params.put("uid", uid);
		params.put("type", "wangxian");
		params.put("token", sign(cid,uid,"wangxian") );
		String utf8CallBack="";
		String base64CallBack="";
		try {
			utf8CallBack = URLEncoder.encode(returnCallbackUrl,"utf-8");
			base64CallBack = Base64.encode(returnCallbackUrl.getBytes());
		} catch (UnsupportedEncodingException e) {
			PlatformSavingCenter.logger.info("[宝软（乐玩）充值订单生成] [成功] [对callback进行utf8加密] [失败] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [用户昵称(宝软uid):"+passport.getNickName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		}
	//	params.put("notify_url", utf8CallBack);
		params.put("notify_url", base64CallBack);
		
		StringBuffer sb = new StringBuffer();
		sb.append(RECHARGE_URL);
		sb.append("?");
		sb.append(buildParams(params));
		
		if(PlatformSavingCenter.logger.isInfoEnabled())
			PlatformSavingCenter.logger.info("[宝软（乐玩）充值订单生成] [成功] [生成URL] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [用户昵称(宝软uid):"+passport.getNickName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [返回值:"+sb.toString()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		return sb.toString();
	}
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel,String[]others){
		long startTime = System.currentTimeMillis();
		String savingMedium = "宝币";
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("宝软（乐玩）");
		//设置充值介质
		order.setSavingMedium(savingMedium);
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(1);
		//设置消费金额
		order.setPayMoney(payAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
		order.setHandleResultDesp("宝软（乐玩）新生成订单");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("宝软（乐玩）新生成订单");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(others[0]);
		order.setUserChannel(userChannel);
		//先存入到数据库中
		if(others.length > 1){
			order.setChargeValue(others[1]);
		}
		if(others.length > 2){
			order.setChargeType(others[2]);
		}
		order = orderFormManager.createOrderForm(order);
		//设置订单号
		order.setOrderId("br-"+DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+ order.getId());
		String  returnCallbackUrl  = callBackUrl;
		returnCallbackUrl += order.getOrderId();
		
		try
		{
			orderFormManager.updateOrderForm(order, "orderId");
			PlatformSavingCenter.logger.info("[宝软（乐玩）充值订单生成] [成功] [更新订单信息] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[宝软（乐玩）充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		
		//拼装url 返给客户端
		LinkedHashMap<String, String> params = new LinkedHashMap<String,String>();
		String uid = passport.getUserName();
		if(!StringUtils.isEmpty(uid))
		{
			if(uid.startsWith("BAORUANUSER_")) {
				uid = uid.split("_")[1];
			}
		}
		params.put("cid", cid);
		params.put("uid", uid);
		params.put("type", "wangxian");
		params.put("token", sign(cid,uid,"wangxian") );
		String utf8CallBack="";
		String base64CallBack="";
		try {
			utf8CallBack = URLEncoder.encode(returnCallbackUrl,"utf-8");
			base64CallBack = Base64.encode(returnCallbackUrl.getBytes());
		} catch (UnsupportedEncodingException e) {
			PlatformSavingCenter.logger.info("[宝软（乐玩）充值订单生成] [成功] [对callback进行utf8加密] [失败] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [用户昵称(宝软uid):"+passport.getNickName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		}
	//	params.put("notify_url", utf8CallBack);
		params.put("notify_url", base64CallBack);
		
		StringBuffer sb = new StringBuffer();
		sb.append(RECHARGE_URL);
		sb.append("?");
		sb.append(buildParams(params));
		
		if(PlatformSavingCenter.logger.isInfoEnabled())
			PlatformSavingCenter.logger.info("[宝软（乐玩）充值订单生成] [成功] [生成URL] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [用户昵称(宝软uid):"+passport.getNickName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [返回值:"+sb.toString()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		return sb.toString();
	}
	
	
	private String buildParams(Map<String,String> params)
	{
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
	
	private  String sign(String cid,String uid,String type)
	{
		String md5Str = StringUtil.hash(cid+uid+type+financekey);
		return md5Str;
	}
	
	
	
	public static BaoRuanSavingManager getInstance(){
		return BaoRuanSavingManager.self;
	}
}
