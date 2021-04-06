package com.fy.boss.platform.baidusdk;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.downjoy.common.crypto.MessageDigest;
import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.StringUtil;

public class BAIDUSDKSavingCallBackServlet extends HttpServlet {

	public static String appid = "1670";
	public static String appkey = "f8fa7b2a60d2236b119ca003bbc12e5c";
	public static String client_secret = "2f68ed90d5cd79851858fbe83f723c61";
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		try
		{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
	/**
	 * amount	String
	cardtype	String
	orderid	String
	Aid	String
	result	String
	timetamp	Strring
	client_secret	String
	
	 */
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
			params.put("amount", req.getParameter("amount"));
			params.put("cardtype", req.getParameter("cardtype"));
			params.put("orderid", req.getParameter("orderid"));
			params.put("result", req.getParameter("result"));
			params.put("timetamp", req.getParameter("timetamp"));
			String mySign = sign(params,req.getParameter("aid"));
			params.put("aid", req.getParameter("aid"));
			params.put("client_secret", req.getParameter("client_secret"));
			
			String verstring = params.get("client_secret");
			
			if(PlatformSavingCenter.logger.isDebugEnabled())
			{
				PlatformSavingCenter.logger.debug("[充值回调] [充值平台：百度SDK] [参数:"+params+"]");
			}
			
			double factor = 1.0d;
			
			if(Long.parseLong(params.get("cardtype")) > 3l &&  Long.parseLong(params.get("cardtype")) != 101l)
			{
				factor = 1.0d;
			}
			
			String channelOrderId = req.getParameter("orderid");
			
	//		//Md5验证
	//		MessageDigest md;
			try {
				//md = MessageDigest.getInstance(com.downjoy.common.crypto.MessageDigest.ALGORITHM_MD5);
				if(verstring.equalsIgnoreCase(mySign))
				{
					OrderFormManager orderFormManager = OrderFormManager.getInstance();
					//查询订单
						//如果cpOrderId没有传过来，则通过cardNo和充值类型查询订单
						OrderForm orderForm = orderFormManager.getOrderForm(params.get("orderid").trim());
						if(orderForm!= null)
						{
							Passport  p = PassportManager.getInstance().getPassport(orderForm.getPassportId());
							String username = p.getUserName();
							
							synchronized(orderForm) {
								if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
									res.getWriter().write("SUCCESS");
									PlatformSavingCenter.logger.error("[充值回调] [充值平台：百度SDK] [失败:此订单已经回调过了] [百度SDK  orderId:"+params.get("orderid")+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值金额:"+params.get("amount")+"] [充值结果:"+params.get("result").trim()+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [cardtype:"+params.get("cardtype")+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
									return;
								}
								//更新订单
								try {
									orderForm.setResponseTime(System.currentTimeMillis());
									if(params.get("result").trim().equals("1")) {
										orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
										orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
										//以百度SDK返回的充值额度为准
//										orderForm.setPayMoney((long)(((long)(Double.valueOf(params.get("amount")) * 100))/0.8));
										orderForm.setPayMoney((long)((Double.valueOf(params.get("amount")) * 100)/factor));
										orderForm.setChannelOrderId(channelOrderId);
										if(PlatformSavingCenter.logger.isInfoEnabled())
											PlatformSavingCenter.logger.info("[充值回调] [充值平台：百度SDK] [成功充值] [状态码:"+params.get("result")+"] [百度SDK订单号:"+params.get("orderid")+"] [自有订单号:"+orderForm.getId()+"] [cardtype:"+params.get("cardtype")+"] [充值卡号:"+params.get("cardno")+"] [充值金额:"+params.get("amount")+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [充值结果:"+params.get("result").trim()+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
									} else {
										orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
										orderForm.setChannelOrderId(channelOrderId);
//										orderForm.setResponseDesp(req.getParameter("remark"));
										if(PlatformSavingCenter.logger.isInfoEnabled())
											PlatformSavingCenter.logger.info("[充值回调] [充值平台：百度SDK] [充值失败] [状态码:"+params.get("result")+":"+req.getParameter("remark")+"] [百度SDK订单号:"+params.get("orderid")+"] [自有订单号:"+orderForm.getId()+"] [cardtype:"+params.get("cardtype")+"] [充值卡号:"+params.get("cardno")+"] [充值金额:"+params.get("amount")+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [充值结果:"+params.get("result").trim()+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
									}
									res.getWriter().write("SUCCESS");
									orderFormManager.update(orderForm);
								} catch (Exception e) {
									PlatformSavingCenter.logger.error("[充值回调] [充值平台：百度SDK] [失败:更新订单信息出错] [百度SDK  orderId:"+params.get("orderid")+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [cardtype:"+params.get("cardtype")+"] [充值卡号:"+params.get("cardno")+"] [充值金额:"+params.get("amount")+"] [充值结果:"+params.get("result").trim()+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
								}
								return;
							}
						}
						else
						{
							res.getWriter().write("ERROR_FAIL");
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：百度SDK] [失败] [数据库中无此订单] [百度SDK  orderId:"+params.get("orderid")+"] [回传orderId："+params.get("aid")+"] [cardtype:"+params.get("cardtype")+"] [充值卡号:"+params.get("cardno")+"] [充值金额:"+params.get("amount")+"] [充值金额:"+params.get("amount")+"] [充值结果:"+params.get("result").trim()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							return;
						}
				}
				else
				{
					res.getWriter().write("ERROR_FAIL");
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：百度SDK] [失败] [失败：校验加密串不一致] [百度SDK  orderId:"+params.get("orderid")+"] [cardtype:"+params.get("cardtype")+"] [充值卡号:"+params.get("cardno")+"] [充值金额:"+params.get("amount")+"] [充值金额:"+params.get("amount")+"] [回传的sign:"+verstring+"] [生成的sign:"+mySign+"] [充值结果:"+params.get("result").trim()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					return;
				}
			} catch (Exception e1) {
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：百度SDK] [失败:出现异常] [百度SDK  orderId:"+params.get("orderid")+"] [cardtype:"+params.get("cardtype")+"] [充值卡号:"+params.get("cardno")+"] [充值金额:"+params.get("amount")+"] [充值金额:"+params.get("amount")+"] [充值结果:"+params.get("result").trim()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
				res.getWriter().write("ERROR_FAIL");
				return;
			}
		}
		catch(Exception e)
		{
			res.getWriter().write("ERROR_FAIL");
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：百度SDK] [失败:出现异常]",e);
			return;
		}
	}
	
	public static String sign(LinkedHashMap<String,String> params,String str){
		String values[] = params.values().toArray(new String[0]);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < values.length ; i++){
			sb.append(values[i]);
		}
		
		
		
		String md5Str = sb.toString()+client_secret+(str == null ? "":str);
		String sign = StringUtil.hash(md5Str).toLowerCase();
		
		if(PlatformSavingCenter.logger.isDebugEnabled())
		{
			PlatformSavingCenter.logger.debug("[百度SDK充值] [生成签名串] [签名前:"+md5Str+"] [签名后:"+sign+"] [session_secret:"+client_secret+"]");
		}
		
		return sign;
	}
	
}
	