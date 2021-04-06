package com.fy.boss.platform.meizu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.JsonUtil;

/**
 * 37魅族迁移整合魅族sdk
 * @author wtx
 * @date 2015-10-29 下午03:42:30
 */
public class MeiZuSavingCallBackServlet2 extends HttpServlet {
	
	public static String NEW_APPID = "3198169";
	public static String NEW_APPKEY = "21a19e0d38d44cdf888bef8259305187";
	public static String NEW_appSecre = "gKqv28YXHjGk5ip8FZQuEdCc7ZgEBrXc";

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		
		Map<Object, Object> map = new LinkedHashMap<Object, Object>(req.getParameterMap());
		List<Map.Entry<Object,Object>> mapList = new ArrayList<Map.Entry<Object,Object>>(map.entrySet()); 

		Collections.sort(mapList, new Comparator<Map.Entry<Object,Object>>(){ 
			public int compare(Map.Entry<Object,Object> mapping1,Map.Entry<Object,Object> mapping2){ 
				String key1 = (String)mapping1.getKey();
				String key2 = (String)mapping2.getKey();
				if(key2==null){
					key2 = "";
				}
				return key1.compareTo(key2); 
			} 
		}); 
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		String value = "";
		for(Map.Entry<Object,Object> entry : mapList){
			if(entry != null && entry.getKey() != null){
				String key = (String)entry.getKey();
				Object valueO = entry.getValue();
				if(key != null){
					if(valueO != null){
						for(String v : (String[])valueO){
							value = v;
						}
					}
					params.put(key, value);
				}
			}
		}
		
		String mysign = "";
		try {
			mysign = getSignCode(params, NEW_appSecre);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String sign = req.getParameter("sign");
		params.put("sign_type","md5");
		params.put("sign",sign);
		
		String uid = req.getParameter("uid");
		String orderid = req.getParameter("order_id");
		String cp_order_id = req.getParameter("cp_order_id");
		String productPrice = req.getParameter("total_price");
		String tradeStatus = req.getParameter("trade_status");
		
		
		//去掉sign标记 得到所有参数
		//验证sign
		if(sign.equalsIgnoreCase(mysign) && "3".equals(tradeStatus))
		{
			
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
			OrderForm orderForm = orderFormManager.getOrderForm(cp_order_id);
			long money = Math.round((Double.valueOf(productPrice)) * 100);
			
				//通过orderId查订单
			if(orderForm != null)
			{
				Passport p = PassportManager.getInstance().getPassport(orderForm.getPassportId());
				
				if(p == null)
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：魅族2] [失败：找不到匹配的用户] [uid:"+uid+"] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("1");
					return;
				}
				
				synchronized(orderForm) {
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：魅族2] [失败：订单已经回调过了] [uid:"+uid+"] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						res.getWriter().write("1");
						return;
					}
				}
				
				orderForm.setResponseTime(System.currentTimeMillis());
				orderForm.setResponseDesp("魅族回调充值接口成功");
				if(Long.valueOf(money) != orderForm.getPayMoney())
				{
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：魅族2] [警告：传回的充值金额和订单存储的金额不一致] [uid:"+uid+"] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
				orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
				orderForm.setPayMoney(Long.valueOf(money));
				orderForm.setChannelOrderId(orderid);
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[充值回调] [充值平台：魅族2] [成功] [用户充值] [成功] [uid:"+uid+"] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				try {
					orderFormManager.update(orderForm);
				} catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：魅族2] [失败：更新订单出错] [uid:"+uid+"] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				}
				
				Map responseJson = new HashMap();
				responseJson.put("code", 200);
				try {
					res.getWriter().write(JsonUtil.jsonFromObject(responseJson));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：魅族2] [失败：无参数指定订单] [uid:"+uid+"] [交易流水号:"+orderid+"] [交易金额:"+money+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");

				Map responseJson = new HashMap();
				responseJson.put("code", 1);
				try {
					res.getWriter().write(JsonUtil.jsonFromObject(responseJson));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：魅族2] [失败：签名验证失败] [uid:"+uid+"] [交易流水号:"+orderid+"] [tradeStatus:"+tradeStatus+"] [sign:"+sign+"] [mysign:"+mysign+"] [params:"+params+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			Map responseJson = new HashMap();
			responseJson.put("code", 1);
			try {
				res.getWriter().write(JsonUtil.jsonFromObject(responseJson));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static final String getSignCode(Map map, String appSecret) throws Exception {
		String queryString = concatMap(map);
		String signText = queryString + ":" + appSecret;
		String serverSignCode = getMD5Str(signText);
		return serverSignCode;
	}
	public static final String concatMap(Map<String, String> params) throws UnsupportedEncodingException {
		List<String> keysSet = new ArrayList<String>();
		keysSet.addAll(params.keySet());
		Collections.sort(keysSet);
		StringBuilder sb = new StringBuilder();
		for (String key : keysSet) {
			if (key.equals("sign") || key.equals("sign_type")) {
				continue;
			}
			Object value = params.get(key);
			sb.append("&").append(key).append("=").append(value.toString());
		}

		if (sb.length() > 0 && sb.toString().startsWith("&")) {
			sb.delete(0, 1);
		}
		return sb.toString();
	}
	public static String getMD5Str(String ...str)throws Exception {
		MessageDigest messageDigest = null;
		StringBuffer strB = new StringBuffer();
		for(int i = 0; i< str.length; i++){
			strB.append(str[i]);
		}
		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(strB.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			throw e;
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}
	
}
