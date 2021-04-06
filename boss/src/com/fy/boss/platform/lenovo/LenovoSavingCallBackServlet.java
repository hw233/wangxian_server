package com.fy.boss.platform.lenovo;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class LenovoSavingCallBackServlet extends HttpServlet {

	private static final String SECRETKEY = "8c55352dd2efe601637a";
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		String orderId = req.getParameter("orderId");
		String openid = req.getParameter("openid");
		String amount = req.getParameter("amount");
		String actualAmount = req.getParameter("actualAmount");
		String cpOrderId = URLDecoder.decode(req.getParameter("extraInfo"),"utf-8");
		String success = req.getParameter("success");
		String msg = req.getParameter("msg");
		String sign = req.getParameter("sign");
		
		//去掉sign标记 得到所有参数
		String signStr = "orderId="+orderId+"&openid=" + openid + "&amount=" +amount + "&actualAmount=" +actualAmount+ "&success=" +success+"&extraInfo="+cpOrderId+"&secret="+SECRETKEY;
		String my_sign = StringUtil.hash(signStr);
		//验证sign
		if(my_sign.equalsIgnoreCase(sign))
		{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
			OrderForm orderForm = orderFormManager.getOrderForm(cpOrderId);
			
				//通过orderId查订单
			if(orderForm != null)
			{
				synchronized(orderForm) {
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：乐逗] [失败：订单已经回调过了] [LENOVO订单id:"+orderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+amount+"] [实际金额:"+actualAmount+"] [订单中原金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						res.getWriter().write("ok");
						return;
					}
				}
				orderForm.setResponseTime(System.currentTimeMillis());
				orderForm.setMemo2("[外部订单号:"+orderId+"] [交易金额:"+amount+"] [实际交易金额:"+actualAmount+"] [交易结果:"+success+"] [sign:"+sign+"]");
				orderForm.setResponseDesp("乐逗回调充值接口成功");
				if(Math.round(Double.valueOf(actualAmount) * 100) != orderForm.getPayMoney())
				{
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：乐逗] [警告：传回的充值金额和订单存储的金额不一致] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+amount+"] [传入实际金额:"+actualAmount+"] [订单中原金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				if(success.trim().equals("0")) {
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					orderForm.setPayMoney(Math.round(Double.valueOf(actualAmount) * 100));
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：乐逗] [成功] [充值成功] [传入乐逗ORDERID:"+orderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+amount+"] [扣完手续费传入实际金额:"+actualAmount+"] [更新入订单的金额:"+Math.round(Double.valueOf(actualAmount) * 100)+"] [订单中原金额:"+orderForm.getPayMoney()+"] [乐逗用户:"+openid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				else
				{
					orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
					orderForm.setResponseDesp("乐逗交易失败");
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：乐逗] [成功] [充值失败] [传入乐逗ORDERID:"+orderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+amount+"] [扣完手续费传入实际金额:"+actualAmount+"] [更新入订单的金额:"+Math.round(Double.valueOf(actualAmount) * 100)+"] [订单中原金额:"+orderForm.getPayMoney()+"] [乐逗用户:"+openid+"] [返回结果:"+success+"] [错误信息:"+msg+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				try {
					orderFormManager.update(orderForm);
				} catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：乐逗] [失败：更新订单出错] [传入乐逗ORDERID:"+orderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+amount+"] [扣完手续费传入实际金额:"+actualAmount+"] [更新入订单的金额:"+Math.round(Double.valueOf(actualAmount) * 100)+"] [订单中原金额:"+orderForm.getPayMoney()+"] [乐逗用户:"+openid+"] [返回结果:"+success+"] [错误信息:"+msg+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					res.getWriter().write("failure");
					return;
				}
				
				res.getWriter().write("ok");
				return;
			}
			else
			{
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：乐逗] [成功] [查找订单失败] [传入乐逗ORDERID:"+orderId+"] [传入orderId:"+cpOrderId+"] [充值金额:"+amount+"] [扣完手续费传入实际金额:"+actualAmount+"] [更新入订单的金额:"+Math.round(Double.valueOf(actualAmount) * 100)+"] [乐逗用户:"+openid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("failure");
				return;
			}
		}
		else
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：乐逗] [失败：签名验证失败] [准备用于签名的字符串:"+signStr+"] [传入的sign:"+sign+"] [my sign:"+my_sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			res.getWriter().write("failure");
			return;
		}
		
		
	}
	
}
