package com.fy.boss.platform.xy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.fy.boss.platform.lenovo.com.test.util.CpTransSyncSignValid;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.xy.XYSavingCallBackServlet;
import com.xuanzhi.tools.text.StringUtil;

public class XYSavingCallBackServlet extends HttpServlet {

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		try
		{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
			
			/**
			 * 
			 */
			String orderid = req.getParameter("orderid");
			String uid = req.getParameter("uid");
			String serverid = req.getParameter("serverid");
			String amount = req.getParameter("amount");
			String extra = req.getParameter("extra");
			String ts = req.getParameter("ts");
			String sign = req.getParameter("sign");
	
			String miyao = "sTnFzuQb6iP6W7JjGMsAh7JBwhkwe5WS";
			String my_sign = XYSavingCallBackServlet.genSign(req.getParameterMap(), miyao);
			
			//去掉sign标记 得到所有参数
			//验证sign
			long money = (long)(Double.valueOf(amount)*100);
			if(my_sign.equals(sign))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(extra);
				
					//通过orderId查订单
				if(orderForm != null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：XYSDK] [失败：订单已经回调过了] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [交易时间:"+ts+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							return;
						}
					}
					
					if(Long.valueOf(money) != orderForm.getPayMoney())
					{
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：XYSDK] [警告：传回的充值金额和订单存储的金额不一致] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [交易时间:"+ts+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					
				try {
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setResponseDesp("XYSDK回调充值接口成功");
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					orderForm.setPayMoney(Long.valueOf(money));
					orderForm.setChannelOrderId(orderid);
					orderFormManager.update(orderForm);
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：XYSDK] [成功] [用户充值] [成功] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [交易时间:"+ts+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：XYSDK] [失败：更新订单出错] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [交易时间:"+ts+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("FAILURE");
						return;
					}
					
					res.getWriter().write("success");
					return;
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：XYSDK] [失败：无参数指定订单] [交易流水号:"+orderid+"] [交易金额:"+money+"] [交易时间:"+ts+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("fail");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：XYSDK] [失败：签名验证失败] [交易流水号:"+orderid+"] [交易金额:"+money+"] [交易时间:"+ts+"] [sign:"+sign+"] [my sign:"+my_sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("fail");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：XYSDK] [失败：出现异常] [交易流水号:--] [充值类型:--] [计费点编号:--] [交易金额:--] [购买数量:--] [交易时间:--] [传入字符串:"+req.getParameterMap()+"] [加密前:--] [sign:--] [my sign:--] [costs:--]",e);
			res.getWriter().write("fails");
			return;
		}
		
	}
	
	public static String genSign(Map<String,String[]> params,String secretkey)
	{
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String[] v = params.get(keys[i]);
			if( StringUtils.isEmpty(v[0]) || "sign".equals(keys[i]) || "sig".equals(keys[i]))continue;
			sb.append(keys[i]+"="+v[0]+"&");
		}
		String md5Str = secretkey+sb.substring(0,sb.length()-1);
		
		
		
		String sign = StringUtil.hash(md5Str);
		return sign;
	}
	
	public static  String genReturnResult(String ret,String msg)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("ret:"+ret+","+"msg:"+"\""+msg+"\""+"}");
		return buffer.toString();
	}
	
}
