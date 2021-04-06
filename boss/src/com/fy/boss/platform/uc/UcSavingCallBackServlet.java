package com.fy.boss.platform.uc;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.metal.MetalBorders.PaletteBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.uc.UCWebSavingManager;
import com.fy.boss.platform.uc.UcSavingCallBackServlet;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class UcSavingCallBackServlet extends HttpServlet {
	public static final Logger logger = Logger.getLogger(UcSavingCallBackServlet.class); 
	public static Map<Integer,String> cardTypeMap = new HashMap<Integer, String>();
	
	
	static {
		cardTypeMap.put(1001,"神州行");
		cardTypeMap.put(1002,"联通卡");
		cardTypeMap.put(1003,"电信卡");
		cardTypeMap.put(1004,"骏网一卡通");
		cardTypeMap.put(1005,"盛大卡");
		cardTypeMap.put( 1006,"征途卡");
		cardTypeMap.put(1007,"Q币卡");
		cardTypeMap.put(1008,"久游卡");
		cardTypeMap.put( 1009,"易宝e卡通");
		cardTypeMap.put(1010,"网易卡");
		cardTypeMap.put(1011,"完美卡" );
		cardTypeMap.put(1012,"搜狐卡");
		cardTypeMap.put(1013 ,"纵游一卡通");
		cardTypeMap.put(1014, "天下一卡通");
		cardTypeMap.put(1015 ,"天宏一卡通");
	}
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		String str = FileUtils.getContent(req.getInputStream());
		JacksonManager jm = JacksonManager.getInstance();
		Map dataMap = (Map)jm.jsonDecodeObject(str);
		HashMap<String,String> params = new HashMap<String,String>();
		params.putAll(dataMap);
		params.remove("sign");
		
		int status = ((Number)dataMap.get("status")).intValue();
		String message = (String)dataMap.get("message");
		message = URLDecoder.decode(message, "utf-8");
		long time = (((Number)dataMap.get("time"))).longValue();
		String dataStr = (String)dataMap.get("data");
		dataStr = URLDecoder.decode(dataStr, "utf-8");
		Map data  = (Map)jm.jsonDecodeObject(dataStr);
		String sign = (String)dataMap.get("sign");
		
		String orderId = (String)data.get("orderId");
		String cpId = (String)data.get("cpId");
		String mobileOs = "ios";
		
		String cpOrderId = (String)data.get("cpOrderId");
		String uid = String.valueOf(data.get("uid"));
		String gameId = String.valueOf(data.get("gameId"));
		String serverId = (String)data.get("serverId");
		int channelId = ((Number)data.get("channelId")).intValue();
		
		if(channelId == UCWebSavingManager.UC_IOS_CHANNELID)
		{
			mobileOs = "ios";
		}
		else
		{
			mobileOs = "android";
		}
		
		int payType = ((Number)data.get("payType")).intValue();
		String cardNo = (String)data.get("cardNo");
		long payAmount = Math.round(((Number)data.get("payAmount")).doubleValue() * 100);
		long trameTime =((Number)data.get("trameTime")).longValue();
		
		//去掉sign标记 得到所有参数
		//验证sign
		String my_sign = "";
		if(mobileOs.equalsIgnoreCase("ios"))
		{
			my_sign = uc_sign(params,UCWebSavingManager.UC_IOS_Secretkey);
		}
		else
		{
			my_sign = uc_sign(params,UCWebSavingManager.UC_ANDDROID_Secretkey);
		}
		if(my_sign.equalsIgnoreCase(sign))
		{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
				//通过orderId查订单
			if(StringUtils.isEmpty(orderId))
			{
				logger.error("[充值回调] [充值平台：UC] [失败:传入的订单ID为空] [uc orderId:"+orderId+"] [my orderId:"+cpOrderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值卡号:"+cardNo+"] [充值金额:"+payAmount+"] [支付结果:"+status+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("failure");
				return; 
			}
			OrderForm orderForm = orderFormManager.getOrderForm(cpOrderId);
			if(orderForm != null)
			{
				synchronized(orderForm) {
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：UC] [失败:此订单已经回调过] [uc orderId:"+orderId+"] [my orderId:"+cpOrderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值卡号:"+cardNo+"] [充值金额:"+payAmount+"] [支付结果:"+status+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						res.getWriter().write("{\"ErrorCode\":\"1\",\"ErrorDesc\":\"接收成功\"} ");
						return;
					}
				}
				orderForm.setResponseTime(System.currentTimeMillis());
				orderForm.setMemo2(dataStr);
				orderForm.setResponseDesp("状态:" + status + " message:" + message + " time:"+time );
				if(payAmount != orderForm.getPayMoney())
				{
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：UC] [传回的充值金额和订单存储的金额不一致] [uc orderId:"+orderId+"] [my orderId:"+cpOrderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值卡号:"+cardNo+"] [充值金额:"+payAmount+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [支付结果:"+status+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				orderForm.setPayMoney(payAmount);
				if(status == 20100200)
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
				else
					orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
				try {
					orderFormManager.update(orderForm);
				} catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：UC] [失败:更新订单信息时报错] [uc orderId:"+orderId+"] [my orderId:"+cpOrderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值卡号:"+cardNo+"] [充值金额:"+payAmount+"] [支付结果:"+status+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					res.getWriter().write("failure");
					return;
				}
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[充值回调] [充值平台：UC] [充值"+(status == 20100200?"成功":"失败")+"] [uc orderId:"+orderId+"] [my orderId:"+cpOrderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值卡号:"+cardNo+"] [充值金额:"+payAmount+"] [支付结果:"+status+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("success");
				return;
			}
			else
			{
				logger.error("[充值回调] [充值平台：UC] [失败:查询订单失败,无匹配订单] [uc orderId:"+orderId+"] [my orderId:"+cpOrderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值卡号:"+cardNo+"] [充值金额:"+payAmount+"] [支付结果:"+status+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("failure");
				return;
			}
		}
		else
		{
			logger.error("[充值回调] [充值平台：UC] [失败:签名验证失败] [UC传入内容:"+str+"] [uc sign:"+sign+"] [my sign:"+my_sign+"] [uc orderId:"+orderId+"] [my orderId:"+cpOrderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值卡号:"+cardNo+"] [充值金额:"+payAmount+"] [支付结果:"+status+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			res.getWriter().write("failure");
			return;
		}
		
		
	}
	
	private String uc_sign(HashMap<String,String> params,String secretkey){
		String appId = params.get("appId");
		String requestId = params.get("requestId");
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v);
		}
		String md5Str = appId+secretkey+requestId+sb.toString();
		
		String sign = StringUtil.hash(md5Str);
		
		sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v+"&");
		}
		sb.append("sign="+sign);
		
		return sb.toString();
	}
}
