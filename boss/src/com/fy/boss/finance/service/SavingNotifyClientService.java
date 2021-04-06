package com.fy.boss.finance.service;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.fy.boss.finance.model.OrderForm;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;

public class SavingNotifyClientService {

	protected static Logger logger = Logger.getLogger(SavingNotifyClientService.class);
	
	private static final String privateKey = "12345asdfg7y6ydsudkf8HHGsds44loiu";

	/**
	 * 	 * 通知游戏服充值
	 * @param url	通知的url
	 * @param username	充值的帐号
	 * @param paymoney	充值的钱，单位：分
	 * @param savingPlatform 充值平台：例如易宝,UC
	 * @param savingMedium 充值介质：例如移动充值卡，盛大卡
	 */
	public static boolean notifySaving(String url, String username, long paymoney, String userChannel, String savingMedium, String serverName, String platform, OrderForm order) {
		long start = System.currentTimeMillis();
		if(username== null || paymoney < 0 || userChannel == null || savingMedium == null || platform == null) {
			logger.warn("[通知失败] [无效的通知] [username:"+username+"] [pay:"+paymoney+"] [userChannel:"+userChannel+"] [savingMedium:"+savingMedium+"] [orderId:"+order.getId()+"] ["+platform+"]");
			return false;
		}
		String sign = StringUtil.hash(username + paymoney + userChannel + savingMedium + platform + privateKey);
		String content = username+l+paymoney+l+userChannel+l+savingMedium+l+platform+l+sign;
		//String content = username + "@@#@" + paymoney + "@@#@" + userChannel + "@@#@" + savingMedium;
		boolean isLong = false;
		
		try
		{
			Long.parseLong(order.getMemo1());
			isLong = true;
		}
		catch(Exception e)
		{
			isLong = false;
		}
		
		
		if(isLong)
		{
			content += l + order.getMemo1();
		}
		
		if(order.getChargeValue() != null && !order.getChargeValue().isEmpty()){
			content += l+ order.getChargeValue();
		}
		
		if(order.getChargeType() != null && !order.getChargeType().isEmpty()){
			content += l + order.getChargeType();
		}
		
		HashMap headers = new HashMap();
		headers.put("content", java.net.URLEncoder.encode(content));
		try {

			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), new byte[0], headers, 60000, 60000);

			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			String result = new String(bytes,encoding);
			logger.info("[调用充值通知接口] [成功] [用户:"+username+"] [订单ID:"+order.getId()+"] [订单编号:"+order.getOrderId()+"] [充值额度:"+paymoney+"] [充值渠道:"+userChannel+"] [充值方式:"+savingMedium+"] [充值服务器:"+serverName+"] [调用接口:"+url+"] [RspCode:"+code+"] [RspMsg:"+message+"] [content:"+content+"] [result:"+result+"] ["+(System.currentTimeMillis()-start)+"ms]");
			if(result.trim().equals("OK")) {
				return true;
			}
		} catch (Exception e) {
			logger.error("[调用充值通知接口] [失败] [用户:"+username+"] [订单ID:"+order.getId()+"] [订单编号:"+order.getOrderId()+"] [充值额度:"+paymoney+"] [充值渠道:"+userChannel+"] [充值方式:"+savingMedium+"] [充值服务器:"+serverName+"] [调用接口:"+url+"] [RspCode:"+headers.get(HttpUtils.Response_Code)+"] [RspMsg:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] ["+(System.currentTimeMillis()-start)+"ms]", e);
		}
		return false;
	}
	private final static String l = "@@#@";
}
