package com.fy.boss.platform.sina;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class SinaWYXNewSavingCallBackServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SECRETKEY = "75aa7ff521e383aa2c2fb2f60e3746d1";	
	
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			
	{
		long startTime = System.currentTimeMillis(); 
		try
		{
			req.setCharacterEncoding("utf-8");
			HashMap<String,String> params = new HashMap<String,String>();
			
			params.put("order_id", ParamUtils.getParameter(req, "order_id",""));
			params.put("source", ParamUtils.getParameter(req, "source",""));
			params.put("orderNo", ParamUtils.getParameter(req, "orderNo",""));
			params.put("actual_amount", ParamUtils.getParameter(req, "actual_amount",""));
			params.put("amount", ParamUtils.getParameter(req, "amount",""));
			params.put("status", ParamUtils.getParameter(req, "status",""));
			params.put("signature", ParamUtils.getParameter(req, "signature",""));
			
			boolean orderSucc = false;
			HashMap<String,String> willSignParams = new HashMap<String,String>();
			
			if(params.get("actual_amount").length() > 0) //成功
			{
				orderSucc = true;
				willSignParams.put("order_id", ParamUtils.getParameter(req, "order_id",""));
				willSignParams.put("amount", ParamUtils.getParameter(req, "amount",""));
				willSignParams.put("order_uid", ParamUtils.getParameter(req, "order_uid",""));
				willSignParams.put("source", ParamUtils.getParameter(req, "source",""));
				willSignParams.put("actual_amount", ParamUtils.getParameter(req, "actual_amount",""));
				
			}
			else //失败
			{
				orderSucc = false;
				willSignParams.put("order_id", ParamUtils.getParameter(req, "order_id",""));
				willSignParams.put("status", ParamUtils.getParameter(req, "status",""));
			}
			//生成自己的签名
			String my_sign = sign(willSignParams);
			
			
			//
			
			//验证sign
			if(my_sign.equalsIgnoreCase(params.get("signature")))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
					//通过渠道orderId查订单
				
				PassportManager passportManager = PassportManager.getInstance();
				
				OrderForm orderForm = orderFormManager.getOrderForm(params.get("order_id"));
				if(orderForm != null)
				{
					 Passport passport  = passportManager.getPassport(orderForm.getPassportId());
					 
					 String username = "";
					 if(passport != null)
					 {
						 username = passport.getUserName();
					 }
					 
					 if(username == null)
					 {
						 username = "";
					 }
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [订单已经回调过了] [充值类型:--] [充值金额:"+params.get("amount")+"] [实际充值金额:"+params.get("actual_amount")+"] [支付是否成功:"+orderSucc+"] [订单的id:"+orderForm.getId()+"] [订单的orderId:"+orderForm.getOrderId()+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("OK");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setPayMoney(Long.valueOf(params.get("actual_amount")));
					if(orderSucc)
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					else
					{
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						orderForm.setResponseDesp("状态:" + (orderSucc == true ? "成功" : "失败")  );
					}
					orderForm.setChannelOrderId(params.get("order_id"));
					try {
						
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:更新订单信息时报错] [充值类型:--] [充值金额:"+params.get("amount")+"] [实际充值金额:"+params.get("actual_amount")+"] [支付是否成功:"+orderSucc+"] [订单的id:"+orderForm.getId()+"] [订单的orderId:"+orderForm.getOrderId()+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("OK");
						return;
					}
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：新浪微游戏] [充值"+(orderSucc == true ? "成功" : "失败")+"] [充值类型:--] [充值金额:"+params.get("amount")+"] [实际充值金额:"+params.get("actual_amount")+"] [支付是否成功:"+orderSucc+"] [订单的id:"+orderForm.getId()+"] [订单的orderId:"+orderForm.getOrderId()+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("OK");
					return;
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:无对应订单信息] [充值金额:"+params.get("amount")+"] [实际充值金额:"+params.get("actual_amount")+"] [支付是否成功:"+orderSucc+"] [传入的订单id:"+params.get("orderNo")+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("OK");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:签名验证失败] [充值金额:"+params.get("amount")+"] [实际充值金额:"+params.get("actual_amount")+"] [支付是否成功:"+orderSucc+"] [传入的订单id:"+params.get("orderNo")+"] ["+params+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("OK");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("OK");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：新浪微游戏] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
		
	}
	
	//签名
	private String sign(Map<String,String> paramMap) throws NoSuchAlgorithmException
	{
		String keys[] = paramMap.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0 ; i < keys.length ; i++){
			String v = paramMap.get(keys[i]);
			sb.append(keys[i]+"|"+v+"|");
		}
		sb.append(SECRETKEY);
		String sign = StringUtil.encrypt(sb.toString(),"SHA-1");
		PlatformSavingCenter.logger.info("[生成待签名串] [待签名串:"+sb.toString()+"] [签名:"+sign+"]");
		return sign;
	}
}
