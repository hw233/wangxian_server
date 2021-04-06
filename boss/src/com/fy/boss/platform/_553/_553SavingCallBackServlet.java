package com.fy.boss.platform._553;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.MessageDigest;
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
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class _553SavingCallBackServlet extends HttpServlet {
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		try
		{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");

			String uname = ParamUtils.getParameter(req, "uname", "");
			String _package = ParamUtils.getParameter(req, "package", "");
			String count = ParamUtils.getParameter(req, "count", "");
			String rmb = ParamUtils.getParameter(req, "rmb", "");
			String channelorderid = ParamUtils.getParameter(req, "orderid", "");
			String orderId = ParamUtils.getParameter(req, "userdata", "");
			String sign = ParamUtils.getParameter(req, "sign", "");
			String md5pre = (_package+count+rmb+uname+channelorderid+"sfjkwef893209;wer5");
			String my_sign = StringUtil.hash(md5pre);

			if(my_sign.equalsIgnoreCase(sign))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(orderId);
				if(orderForm != null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：553SDK] [失败:此订单已经回调过] [553SDK orderid:"+channelorderid+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+rmb+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [传入账号:"+uname+"] [游戏包容量:"+_package+"] [游戏包个数:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("2");
							return;
						}
						//更新订单
						orderForm.setResponseTime(System.currentTimeMillis());
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						//以渠道返回的充值额度为准
						orderForm.setPayMoney((long)(Double.valueOf(rmb)*100 ));	
						orderForm.setChannelOrderId(channelorderid);
						try {
							orderFormManager.update(orderForm);
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：553SDK] [成功] [553SDK orderid:"+channelorderid+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+rmb+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [传入账号:"+uname+"] [游戏包容量:"+_package+"] [游戏包个数:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("0&"+channelorderid);
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：553SDK] [更新订单信息失败] [553SDK orderid:"+channelorderid+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+rmb+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [传入账号:"+uname+"] [游戏包容量:"+_package+"] [游戏包个数:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
							res.getWriter().write("0&"+channelorderid);
						}
						return;
					}
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：553SDK] [失败：订单不存在] [553SDK orderid:"+channelorderid+"] [my orderid:"+orderId+"] [实际支付金额:"+rmb+"] [传入账号:"+uname+"] [游戏包容量:"+_package+"] [游戏包个数:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("0&"+channelorderid);
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：553SDK] [失败：验签失败] [553SDK orderid:"+channelorderid+"] [my order id:"+orderId+"] [my orderid:--] [实际支付金额:"+rmb+"] [数据库中记录金额:--] [传入账号:"+uname+"] [游戏包容量:"+_package+"] [游戏包个数:"+count+"] [md5pre:"+md5pre+"] [mysign:"+my_sign+"] [sign:"+sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("1");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：553SDK] [失败：出现未知异常]",e);
			res.getWriter().write("3");
			return;

		}
		
	}
}
