package com.fy.boss.platform.wandoujia;

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
import com.fy.boss.platform.wandoujia.WandouRsa;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class WandoujiaDKSavingCallBackServlet extends HttpServlet {

	public static Map<String,String> cardTypeMap = new HashMap<String, String>();

	
	
	
	protected void service(HttpServletRequest req, HttpServletResponse res)
	{
		long startTime = System.currentTimeMillis(); 
		try
		{
			req.setCharacterEncoding("utf-8");
			String signType = ParamUtils.getParameter(req, "signType");
			
			if(StringUtils.isEmpty(signType))
			{
				signType = ParamUtils.getParameter(req, "signtype");
			}
			
			String sign = ParamUtils.getParameter(req, "Sign");
			
			if(StringUtils.isEmpty(sign))
			{
				sign = ParamUtils.getParameter(req, "sign");
			}
			
			
			
			String str = ParamUtils.getParameter(req, "content");
			JacksonManager jm = JacksonManager.getInstance();
			Map dataMap = (Map)jm.jsonDecodeObject(str);
			
			//String timeStamp = (String)dataMap.get("timeStamp");
			String orderId =( (Integer)dataMap.get("orderId"))+"";
			long payAmount = (Integer)dataMap.get("money")+0l;
			String payType = (String)dataMap.get("chargeType");
			//String appkeyId = (String)dataMap.get("appKeyId");
			String uid = String.valueOf(dataMap.get("buyerId"));
			String callbackInfo = (String)dataMap.get("out_trade_no");
			//String cardNo = (String)dataMap.get("cardNo");
			
			/*if(gameId.equalsIgnoreCase(IOS_GAMEID+""))
			{
				mobileOs = "ios";
			}
			else
			{
				mobileOs = "android";
			}*/
			
			//去掉sign标记 得到所有参数
			//验证sign
			if(WandouRsa.doCheck(str, sign))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
					//通过渠道orderId查订单
//				if(StringUtils.isEmpty(orderId))
//				{
//					PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:传入的订单ID为空] [uc orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [订单ID:"+callbackInfo+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
//					res.getWriter().write("FAILURE");
//					return; 
//				}
				
				
				
				OrderForm orderForm = orderFormManager.getOrderForm(callbackInfo);
				if(orderForm != null)
				{
					PassportManager passportManager = PassportManager.getInstance();
					
					Passport passport = passportManager.getPassport(orderForm.getPassportId());
					String username = "";
					
					if(passport != null)
					{
						username = passport.getUserName();
					}
					
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：wandoujia] [失败:此订单已经回调过] [wandoujia orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:--] [订单ID:"+callbackInfo+"] [wandoujiaid:"+uid+"] [username:"+username+"] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("SUCCESS");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2(str);
					orderForm.setMediumInfo(payType+"@"+payAmount+"@"+uid);
					orderForm.setPayMoney(payAmount);
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：wandoujia] [失败:更新订单信息时报错] [wandoujia orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:--] [订单ID:"+callbackInfo+"] [wandoujiaid:"+uid+"] [username:"+username+"] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("FAILURE");
						return;
					}
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：wandoujia] [成功] [wandoujia orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:--] [订单ID:"+callbackInfo+"] [wandoujiaid:"+uid+"] [username:"+username+"] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("SUCCESS");
					return;
				}
				else
				{
		
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：wandoujia] [失败:查询订单失败,无匹配订单] [wandoujia orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:--] [订单ID:"+callbackInfo+"] [wandoujiaid:"+uid+"] [username:--] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("SUCCESS");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：wandoujia] [失败:签名验证失败] [传入内容:"+str+"] [sign:"+sign+"] [my sign:--] [wandoujia orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:--] [订单ID:"+callbackInfo+"] [wandoujiaid:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("FAILURE");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：wandoujia] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("FAILURE");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：wandoujia] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
		
	}
	
}
