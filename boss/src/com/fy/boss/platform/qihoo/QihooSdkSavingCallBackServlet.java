package com.fy.boss.platform.qihoo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpsUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class QihooSdkSavingCallBackServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4050449812012426946L;
	public static  String KEY = "b45e94711e0232d7f66c1f916a0c755b";
	public static  String appKey = "455ff7448d782ec75a5ada99b2296cdc";
	//https://openapi.360.cn/pay/verify_mobile_notification.json?
	public static final String VERIFY_NOTIFICATION_URL = "https://openapi.360.cn/pay/verify_mobile_notification.json?";//"https://testol.openapi.360.cn/pay/verify_mobile_notification.json?";
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		String app_key = req.getParameter("app_key");//应用App key
		String product_id = req.getParameter("product_id");//支付产品id
		String amount = req.getParameter("amount");// 必选。支付金额 单位分 大于0的整数
		String app_uid = req.getParameter("app_uid");//用户在应用内的用户id (pay_mode=21)
		String orderid = req.getParameter("app_ext1");// 应用扩展信息1，应用可以利用此字段传递应用内部订单号
		String app_ext2 = req.getParameter("app_ext2");// 应用扩展信息2
		String user_id = req.getParameter("user_id");// 360账号id
		String order_id = req.getParameter("order_id");//应用平台接口生成的订单号，唯一，排重
		String gateway_flag = req.getParameter("gateway_flag");//如果支付返回成功，返回success，如果支付过程断路，返回空，如果支付失败，返回fail
		String sign_type = req.getParameter("sign_type");//签名算法	目前仅支持sign_type=md5
		String sign_return = req.getParameter("sign_return");//应用回传给订单核实接口的参数，不加入签名校验计算
		String sign = req.getParameter("sign");//提取的签名（签名方法如下），不加入签名校验计算
		if(StringUtils.isEmpty(orderid))
		{
			orderid = req.getParameter("app_order_id");
		}
		
		boolean isNewSdk = false;
		if(orderid.contains("qinew"))
		{
			isNewSdk = true;
		}
		
		if(isNewSdk)
		{
			KEY = "e13fc0b7eade2270747973c5248fb24c";
			appKey = "27d0f6f656017c38ae8df11b3b66a2b4";
		}
		else
		{
			KEY = "b45e94711e0232d7f66c1f916a0c755b";
			appKey = "455ff7448d782ec75a5ada99b2296cdc";
		}
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("status", "ok");
		params.put("delivery", "success");
		params.put("msg", "成功");
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("app_key", appKey);
		map.put("product_id", product_id);
		map.put("amount", amount);
		map.put("app_uid", app_uid);
		if(orderid != null) {
			if(req.getParameter("app_ext1") != null )map.put("app_ext1", orderid);
			if(req.getParameter("app_order_id") != null )map.put("app_order_id", orderid);
		}
		if(app_ext2 != null) {
			map.put("app_ext2", app_ext2);
		}
		if(user_id != null) {
			map.put("user_id", user_id);
		}
		
		map.put("order_id", order_id);
		if(gateway_flag != null) {
			map.put("gateway_flag", gateway_flag);
		}
		map.put("sign_type", sign_type);
		map.put("sign_return", sign_return);
//		map.put("sign", sign);
		OrderFormManager fmanager = OrderFormManager.getInstance();
		OrderForm orderForm = fmanager.getOrderForm(orderid);

		//{"status":"ok","delivery":"success","msg":""}
		
		if (orderForm != null) {
			synchronized(orderForm) {
				if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
					if((!StringUtils.isEmpty(orderForm.getChannelOrderId())) &&(orderid.equals(orderForm.getOrderId()))&& (!orderForm.getChannelOrderId().equals(order_id+"")) )
					{
						//生成订单
						OrderForm order = new OrderForm();
						order.setCreateTime(new Date().getTime());
						//设置充值平台
						order.setSavingPlatform(orderForm.getSavingPlatform());
						//设置充值介质
						order.setSavingMedium(orderForm.getSavingMedium());
						//设置充值人
						order.setPassportId(orderForm.getPassportId());
						//设置商品数量
						order.setGoodsCount(0);
						//设置消费金额
						order.setPayMoney(Long.valueOf(amount));
						order.setServerName(orderForm.getServerName());
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setHandleResultDesp("新生成订单");
						//设置订单responseResult
						order.setResponseResult(OrderForm.RESPONSE_SUCC);
						order.setResponseTime(System.currentTimeMillis());
						//设置是否通知游戏服状态 设为false
						order.setNotified(false);
						//设置通知游戏服是否成功 设为false
						order.setNotifySucc(false);
						order.setUserChannel(orderForm.getUserChannel());
						orderForm.setChannelOrderId(order_id);
						//先存入到数据库中
						order = fmanager.createOrderForm(order);
						if(!isNewSdk)
							order.setOrderId("qihoo"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
						else
						{
							order.setOrderId("qinew"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
						}
						try {
							fmanager.update(order);
						} catch (Exception e) {
							params.put("msg", "拷贝订单时 更新订单出现异常");
							String result = toJsonStr(params); 
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：360SDK] [失败:拷贝订单时 更新订单出现异常] [result:"+result+"] [360SDK orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+order.getOrderId()+"] [充值类型:"+order.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [product_id:"+product_id+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+order.getPayMoney()+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+appKey+"] [appSecret:"+KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
							res.getWriter().write(result);
							 return;
						}
						String result = toJsonStr(params); 
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：360SDK] [成功:用户进行了连续充值，为用户新生成订单] [result:"+result+"] [360SDK orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+order.getOrderId()+"] [充值类型:"+order.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [product_id:"+product_id+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+order.getPayMoney()+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+appKey+"] [appSecret:"+KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						res.setContentType("text/json; charset=UTF-8");
						res.getWriter().print(result);
						 return;
					}
					else
					{
						params.put("msg", "此订单已经回调过了");
						String result = toJsonStr(params); 
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：360SDK] [失败：此订单已经回调过了] [result:"+result+"] [360SDK orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderid+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [product_id:"+product_id+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+appKey+"] [appSecret:"+KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						res.setContentType("text/json; charset=UTF-8");
						res.getWriter().print(result);
						return;
					}
				}
			}
			
		
			
			
			boolean bo = false;
			if (validateMd5(map,sign)) {
				boolean bobo = false;
				try {
					bobo = verifyNotification(map);
				} catch (Exception e) {
					params.put("msg", "二次验证出现异常");
					String result = toJsonStr(params); 
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：360SDK] [失败：二次验证出现异常] [360SDK orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderid+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [product_id:"+product_id+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+appKey+"] [appSecret:"+KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
					res.getWriter().write(result);
					return;
				}
				if(bobo) {
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2("[外部订单号:"+order_id+"] [交易金额:"+amount+"] [交易结果:"+gateway_flag+"] [sign:"+sign+"]");
					orderForm.setResponseDesp("360SDK游戏回调充值接口成功");
					
					if(gateway_flag != null && gateway_flag.equalsIgnoreCase("success") )
					{
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						orderForm.setPayMoney(Long.parseLong(amount));
						orderForm.setChannelOrderId(order_id);
					}
					else
					{
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						orderForm.setResponseDesp("360SDK游戏交易失败");
						orderForm.setChannelOrderId(order_id);
					}
					try {
						fmanager.update(orderForm);
						String result = toJsonStr(params); 
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：360SDK] [result:"+result+"] ["+(gateway_flag.equalsIgnoreCase("success") ? "成功" : "失败")+"] [360SDK orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderid+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [product_id:"+product_id+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+appKey+"] [appSecret:"+KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						res.getWriter().print(result);
						return;
					} catch (Exception e) {
						String result = toJsonStr(params); 
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：360SDK] [失败:出现异常] [result:"+result+"] [360SDK orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderid+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [product_id:"+product_id+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+appKey+"] [appSecret:"+KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
						res.getWriter().write(result);
						return;
					}
				} else {
					String result = toJsonStr(params); 
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：360SDK] [失败:二次校验失败] [result:"+result+"] [360SDK orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderid+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [product_id:"+product_id+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+appKey+"] [appSecret:"+KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					res.getWriter().write(result);
					return;
				}
			} else {
				String result = toJsonStr(params); 
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：360SDK] [失败:验签失败] [result:"+result+"] [360SDK orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderid+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [product_id:"+product_id+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+appKey+"] [appSecret:"+KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				res.setContentType("text/json; charset=UTF-8");
				res.getWriter().write(result);
				return;
				
			}
		}
		else
		{
			String result = toJsonStr(params); 
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：360SDK] [失败:数据库中未找到对应订单号的订单] [result:"+result+"] [360SDK orderId:"+order_id+"] [my order id:--] [my orderid:"+orderid+"] [充值类型:--] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [product_id:"+product_id+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+appKey+"] [appSecret:"+KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			res.setContentType("text/json; charset=UTF-8");
			res.getWriter().write(result);
			return;
		}
	}
	
	public static String toJsonStr(HashMap<String,String> params){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		String keys[] = params.keySet().toArray(new String[0]);
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append("\""+keys[i]+"\":\""+v+"\"");
			if(i < keys.length -1){
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	public static boolean verifyNotification(Map<String, String> map) throws Exception {
		Map<String, String> vmap = new HashMap<String, String>();
		vmap.put("app_key", map.get("app_key"));
		vmap.put("product_id", map.get("product_id"));
		vmap.put("amount", map.get("amount"));
		vmap.put("app_uid", map.get("app_uid"));
		vmap.put("order_id", map.get("order_id"));
		vmap.put("app_order_id", map.get("app_order_id"));
		vmap.put("sign_type", map.get("sign_type"));
		vmap.put("sign_return", map.get("sign_return"));
		vmap.put("client_id", appKey);
		vmap.put("client_secret", KEY);
		StringBuffer request_str = new StringBuffer();
		Object[] paramKeys = vmap.keySet().toArray();
		Arrays.sort(paramKeys);
		for (int i = 0; i < paramKeys.length; i++) {
			request_str.append(paramKeys[i] + "").append("=").append(vmap.get(paramKeys[i])).append("&");
		}
		if(request_str.length() > 0) {
			request_str.deleteCharAt(request_str.length() - 1);
		}
		URL url = new URL(VERIFY_NOTIFICATION_URL + request_str);
		Map headers = new HashMap();
		byte[] resContent =HttpsUtils.webGet(url, headers, 30000, 30000);
		String resEncoding = (String)headers.get("Encoding");
		String content = new String(resContent,resEncoding);
		Object obj = JacksonManager.getInstance().jsonDecodeObject(content);
		System.out.println("verifyNotification content:" + content);
		if(obj != null && obj instanceof Map) {
			Map rmap = (Map) obj;
			if(rmap.get("ret") != null && "verified".equals(rmap.get("ret"))) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
    /****
     * 验证md5
     */
	public static boolean validateMd5(Map<String, String> paramMap,String sign) throws UnsupportedEncodingException {
	    Map param = new HashMap();
		param.putAll(paramMap);
		StringBuffer request_str = new StringBuffer();
		Object[] paramKeys = param.keySet().toArray();
		Arrays.sort(paramKeys);
		for (int i = 0; i < paramKeys.length; i++) {
			if("sign_return".equals(paramKeys[i])) {
				continue;
			}
			request_str.append(param.get(paramKeys[i])).append("#");
		}
		String sig = request_str.append(KEY).toString();
		System.out.println("[奇虎360] sign:" + sig);
		sig = StringUtil.hash(sig).toLowerCase();
//		System.out.println("[奇虎360] sign:" + sig);
		if(sig.equals(sign)) {
			return true;
		}
		return false;
	}
	
}
