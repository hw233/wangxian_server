package com.fy.boss.platform.sina;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
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
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class SinaWYXSavingCallBackServlet extends HttpServlet {

	private static final String APPKEY = "3467194209";
	private static final String SECRETKEY = "084b6fbb10729ed4da8c3d3f5a3ae7c9e80d62e09a6638f818e0767091389babd41d8cd98f00b204e9800998ecf8427ef5c24371ae2aed82ec2f23c8bfb72e36";	
	private static final String MSGFROM = "6056";	
	
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			
	{
		long startTime = System.currentTimeMillis(); 
		try
		{
			req.setCharacterEncoding("utf-8");
			HashMap<String,String> params = new HashMap<String,String>();
			
			params.put("AppKey", ParamUtils.getParameter(req, "AppKey",""));
			params.put("msgfrom", ParamUtils.getParameter(req, "msgfrom",""));
			params.put("orderNo", ParamUtils.getParameter(req, "orderNo",""));
			params.put("pay_type", ParamUtils.getParameter(req, "pay_type",""));
			params.put("fee", ParamUtils.getParameter(req, "fee",""));
			params.put("pay_status", ParamUtils.getParameter(req, "pay_status",""));
			
			//生成自己的签名
			String my_sign = sign(params);
			
			params.put("sign", ParamUtils.getParameter(req, "sign",""));
			//
			
			//验证sign
			if(my_sign.equalsIgnoreCase(params.get("sign")))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
					//通过渠道orderId查订单
				if(StringUtils.isEmpty(params.get("orderNo")))
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:传入的订单ID为空] [充值类型:"+params.get("pay_type")+"] [充值金额:"+params.get("fee")+"] [支付结果:"+params.get("pay_status")+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("ERROR");
					return; 
				}
				
				
				
				OrderForm orderForm = orderFormManager.getOrderForm(params.get("orderNo"));
				if(orderForm != null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [订单已经回调过了] [充值类型:"+params.get("pay_type")+"] [充值金额:"+params.get("fee")+"] [支付结果:"+params.get("pay_status")+"] [订单的id:"+orderForm.getId()+"] [订单的orderId:"+orderForm.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("SUCC");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setPayMoney(Long.valueOf(params.get("fee")));
					if(params.get("pay_status").equalsIgnoreCase("S"))
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					else
					{
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						orderForm.setResponseDesp("状态:" + params.get("pay_status")  );
					}
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:更新订单信息时报错] [充值类型:"+params.get("pay_type")+"] [充值金额:"+params.get("fee")+"] [支付结果:"+params.get("pay_status")+"] [订单的id:"+orderForm.getId()+"] [订单的orderId:"+orderForm.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("ERROR");
						return;
					}
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：新浪微游戏] [充值"+(params.get("pay_status").equals("S")?"成功":"失败")+"] [充值类型:"+params.get("pay_type")+"] [充值金额:"+params.get("fee")+"] [支付结果:"+params.get("pay_status")+"] [订单的id:"+orderForm.getId()+"] [订单的orderId:"+orderForm.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("SUCC");
					return;
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:无对应订单信息] [充值类型:"+params.get("pay_type")+"] [充值金额:"+params.get("fee")+"] [支付结果:"+params.get("pay_status")+"] [传入的订单id:"+params.get("orderNo")+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("ERROR");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:签名验证失败] [充值类型:"+params.get("pay_type")+"] [充值金额:"+params.get("fee")+"] [支付结果:"+params.get("pay_status")+"] [传入的订单id:"+params.get("orderNo")+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("ERROR");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("ERROR");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
		
	}
	
	//签名
	private String sign(Map<String,String> paramMap)
	{
		String keys[] = paramMap.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0 ; i < keys.length ; i++){
			String v = paramMap.get(keys[i]);
			sb.append(keys[i]+"="+v+"&");
		}
		sb.append("key="+SECRETKEY);
		String sign = StringUtil.hash(sb.toString());

		return sign;
	}
}
