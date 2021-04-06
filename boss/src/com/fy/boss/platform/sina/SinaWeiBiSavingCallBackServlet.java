package com.fy.boss.platform.sina;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.dao.impl.ExceptionOrderFormDAOImpl;
import com.fy.boss.finance.model.ExceptionOrderForm;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class SinaWeiBiSavingCallBackServlet extends HttpServlet {

	private static final String APPKEY = "3467194209";
	public static final String	MSGFROM= "6056";
	private static final String SECRETKEY = "084b6fbb10729ed4da8c3d3f5a3ae7c9e80d62e09a6638f818e0767091389babd41d8cd98f00b204e9800998ecf8427ef5c24371ae2aed82ec2f23c8bfb72e36";
	
	public static Map<String,String> cardTypeMap = new HashMap<String, String>();
	
	/**
	 * 1 SZX 神州行 充值卡
2 UNICOM 联通卡 充值卡
3 TELECOM 电信卡 充值卡
4 JUNNET 骏网一卡通 充值卡
5 SNDACARD 盛大卡 充值卡
6 ZHENGTU 征途卡 充值卡
7 WANMEI 完美卡 充值卡
1021 CMCC 移动话费支付 移动话费支付
1022 UP U 点余额支付 U 点余额支付
	 */
	
	static {
		cardTypeMap.put("1","新浪微币");
	}
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			
	{
		long startTime = System.currentTimeMillis(); 
		try
		{
			req.setCharacterEncoding("utf-8");
			
			
			HashMap<String,String> params = new HashMap<String,String>();
			params.put("order_id", ParamUtils.getParameter(req, "order_id",""));
			params.put("appkey", ParamUtils.getParameter(req, "appkey",""));
			params.put("order_uid", ParamUtils.getParameter(req, "order_uid",""));
			params.put("amount", ParamUtils.getParameter(req, "amount",""));
			
			String orderId = "sinawei_"+params.get("order_id");
			String payType = "1";
			String payAmount = params.get("amount");
			String orderStatus = "成功";//新浪微币 只有成功才会回调我们
			String uid = params.get("order_uid");
			String cardType = cardTypeMap.get(payType);
			
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			
			
			//查询订单
				//通过渠道orderId查订单
			if(StringUtils.isEmpty(orderId))
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微币] [失败:传入的订单ID为空] [新浪微币 orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("FAILURE");
				return; 
			}
			
			
			
			OrderForm orderForm = orderFormManager.getOrderForm(orderId);
			if(orderForm != null)
			{
				synchronized(orderForm) {
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微币] [失败:此订单已经回调过] [新浪微币 orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [渠道:"+orderForm.getUserChannel()+"] [sinauId:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						res.getWriter().write("OK");
						return;
					}
				}
				orderForm.setResponseTime(System.currentTimeMillis());
				orderForm.setSavingMedium("新浪微币");
				orderForm.setMediumInfo(cardType+"@"+payAmount+"@"+uid);
				orderForm.setPayMoney(Long.valueOf(payAmount));
				orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
				try {
					orderFormManager.update(orderForm);
				} catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微币] [失败:更新订单信息时报错] [新浪微币 orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [渠道:"+orderForm.getUserChannel()+"] [sinauid:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					res.getWriter().write("FAILURE");
					return;
				}
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[充值回调] [充值平台：新浪微币] [充值成功] [新浪微币 orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [渠道:"+orderForm.getUserChannel()+"] [sinauid:"+uid+"] [支付结果:"+orderStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("OK");
				return;
			}
			else
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[充值回调] [充值平台：新浪微币] [充值失败] [未在数据库中找到匹配订单] [新浪微币 orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [sinauid:"+uid+"] [支付结果:"+orderStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("OK");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微币] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("FAILURE");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微币] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e1);
			}
			return;
		}
	}
}
