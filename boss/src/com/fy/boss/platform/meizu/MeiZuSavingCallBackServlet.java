package com.fy.boss.platform.meizu;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.StringUtil;

public class MeiZuSavingCallBackServlet extends HttpServlet {
	
	public static String miyao = "7a875ddc676e4bce0a6abc6e8947e4f8";

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		
		
		String username = req.getParameter("username");
		String orderid = req.getParameter("change_id");
		String productPrice = req.getParameter("money");
		String sign = req.getParameter("hash");
		String myOrderId = req.getParameter("object");
		
		String mysign = StringUtil.hash(username+"|"+orderid+"|"+productPrice+"|"+miyao);
		
		//去掉sign标记 得到所有参数
		//验证sign
		if(sign.equalsIgnoreCase(mysign))
		{
			
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
			OrderForm orderForm = orderFormManager.getOrderForm(myOrderId);
			long money = Math.round((Double.valueOf(productPrice)) * 100);
			
				//通过orderId查订单
			if(orderForm != null)
			{
				Passport p = PassportManager.getInstance().getPassport(orderForm.getPassportId());
				
				if(p == null)
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：魅族] [失败：找不到匹配的用户] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("1");
					return;
				}
				
				synchronized(orderForm) {
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：魅族] [失败：订单已经回调过了] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						res.getWriter().write("1");
						return;
					}
				}
				
				
				
				orderForm.setResponseTime(System.currentTimeMillis());
				orderForm.setResponseDesp("魅族回调充值接口成功");
				if(Long.valueOf(money) != orderForm.getPayMoney())
				{
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：魅族] [警告：传回的充值金额和订单存储的金额不一致] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
				orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
				orderForm.setPayMoney(Long.valueOf(money));
				orderForm.setChannelOrderId(orderid);
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[充值回调] [充值平台：魅族] [成功] [用户充值] [成功] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				try {
					orderFormManager.update(orderForm);
				} catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：魅族] [失败：更新订单出错] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					res.getWriter().write("1");
					return;
				}
				
				res.getWriter().write("1");
				return;
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：魅族] [失败：无参数指定订单] [交易流水号:"+orderid+"] [交易金额:"+money+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("1");
				return;
			}
		}
		else
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：魅族] [失败：签名验证失败] [交易流水号:"+orderid+"] [秘钥:"+miyao+"] [sign:"+sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			res.getWriter().write("1");
			return;
		}
		
		
	}
	
	
}
