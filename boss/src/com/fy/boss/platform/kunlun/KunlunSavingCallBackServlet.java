package com.fy.boss.platform.kunlun;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.StringUtil;

public class KunlunSavingCallBackServlet extends HttpServlet {

	private static final String SECRETKEY = "88eab57d47bb111904ff2aee7668215a";
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		try
		{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
			String orderId = req.getParameter("oid");
			String openid = req.getParameter("uid");
			String amount = req.getParameter("amount");
			String coins = req.getParameter("amount");
			String kunlunbi  = req.getParameter("coins");
			String dtime = req.getParameter("dtime");
			String success = "0";
			String ext = req.getParameter("ext");
			String sign = req.getParameter("sign");
			
			
			//解析 ext
			JacksonManager jm = JacksonManager.getInstance();
			Map map =(Map)jm.jsonDecodeObject(ext);
			String cpOrderId = (String)map.get("partnersorderid");
			String uname = (String)map.get("uname");
			
			//去掉sign标记 得到所有参数  md5(oid+uid+amount+coins+dtime+KEY)
			String signStr = orderId + openid +amount+kunlunbi+dtime+SECRETKEY;
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
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：昆仑] [失败：订单已经回调过了] [昆仑订单id:"+orderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+amount+"] [实际金额:"+coins+"] [订单中原金额:"+orderForm.getPayMoney()+"] [昆仑币:"+kunlunbi+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							String returnMessage = "{\"retcode\":0,\"retmsg\":\"success\"}";
							
							res.getWriter().write(returnMessage);
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2("[外部订单号:"+orderId+"] [交易金额:"+amount+"] [实际交易金额:"+coins+"] [交易结果:"+success+"] [sign:"+sign+"]");
					orderForm.setResponseDesp("昆仑回调充值接口成功");
					if(Math.round(Double.valueOf(coins) * 100) != orderForm.getPayMoney())
					{
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：昆仑] [警告：传回的充值金额和订单存储的金额不一致] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+amount+"] [传入实际金额:"+coins+"] [订单中原金额:"+orderForm.getPayMoney()+"] [昆仑币:"+kunlunbi+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					if(success.trim().equals("0")) {
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						orderForm.setPayMoney(Math.round((Double.valueOf(coins)) * 100));
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：昆仑] [成功] [充值成功] [传入昆仑ORDERID:"+orderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+amount+"] [扣完手续费传入实际金额:"+coins+"] [更新入订单的金额:"+Math.round((Double.valueOf(coins) )* 100)+"] [订单中原金额:"+orderForm.getPayMoney()+"] [昆仑用户:"+openid+"] [昆仑币:"+kunlunbi+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					else
					{
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						orderForm.setResponseDesp("昆仑交易失败");
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：昆仑] [成功] [充值失败] [传入昆仑ORDERID:"+orderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+amount+"] [扣完手续费传入实际金额:"+coins+"] [更新入订单的金额:"+Math.round((Double.valueOf(coins)) * 100)+"] [订单中原金额:"+orderForm.getPayMoney()+"] [昆仑用户:"+openid+"] [返回结果:"+success+"] [错误信息:"+ext+"] [昆仑币:"+kunlunbi+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					try {
						orderForm.setChannelOrderId(orderId);
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：昆仑] [失败：更新订单出错] [传入昆仑ORDERID:"+orderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+amount+"] [扣完手续费传入实际金额:"+coins+"] [更新入订单的金额:"+Math.round((Double.valueOf(coins)) * 100)+"] [订单中原金额:"+orderForm.getPayMoney()+"] [昆仑用户:"+openid+"] [返回结果:"+success+"] [错误信息:"+ext+"] [昆仑币:"+kunlunbi+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						String returnMessage = "{\"retcode\":1,\"retmsg\":\"fail\"}";
						
						res.getWriter().write(returnMessage);
						return;
					}
					
					String returnMessage = "{\"retcode\":0,\"retmsg\":\"success\"}";
					
					res.getWriter().write(returnMessage);
					return;
				}
				else
				{
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：昆仑] [成功] [查找订单失败] [传入昆仑ORDERID:"+orderId+"] [传入orderId:"+cpOrderId+"] [充值金额:"+amount+"] [扣完手续费传入实际金额:"+coins+"] [更新入订单的金额:"+Math.round((Double.valueOf(coins)) * 100)+"] [昆仑用户:"+openid+"] [昆仑币:"+kunlunbi+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					String returnMessage = "{\"retcode\":1,\"retmsg\":\"fail\"}";
					
					res.getWriter().write(returnMessage);
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：昆仑] [失败：签名验证失败] [准备用于签名的字符串:"+signStr+"] [传入的sign:"+sign+"] [my sign:"+my_sign+"] [昆仑币:"+kunlunbi+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				String returnMessage = "{\"retcode\":1,\"retmsg\":\"fail\"}";
				
				res.getWriter().write(returnMessage);
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：昆仑] [出现未知异常]",e);
		}
			
			
	}
	
}
