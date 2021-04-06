package com.fy.boss.platform.tbtsdk;

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

public class TbtSavingCallBackServlet extends HttpServlet {
	public static Map<Integer,String> cardTypeMap = new HashMap<Integer, String>();
	
	//public static String APPKEY = "8d7c106d720e2b05855033035933d709";
	public static String APPKEY = "45fd92cfbad4328681603b87d854a173"; //正式
	
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		
		String source = ParamUtils.getParameter(req, "source", "");
		String trade_no = ParamUtils.getParameter(req, "trade_no", "");
		String amount = ParamUtils.getParameter(req, "amount", "");
		String partner = ParamUtils.getParameter(req, "partner", "");
		String sign = ParamUtils.getParameter(req, "sign", "");
		
		String md5pre = ("source="+source+"&trade_no="+trade_no+"&amount="+amount+"&partner="+partner+"&key="+APPKEY);
		String my_sign = StringUtil.hash(md5pre);
		
		if(sign.equalsIgnoreCase(my_sign))
		{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(trade_no);
				if(orderForm != null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：TBTSDK] [失败:此订单已经回调过] [TBTSDK Source:"+source+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+amount+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("{\"status\":\"success\"} ");
							return;
						}
						//更新订单
						orderForm.setResponseTime(System.currentTimeMillis());
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						//以渠道返回的充值额度为准
						orderForm.setPayMoney((long)(Long.valueOf(amount) ));					
						try {
							orderFormManager.update(orderForm);
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：TBTSDK] [成功] [TBTSDK Source:"+source+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+amount+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("{\"status\":\"success\"} ");
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：TBTSDK] [更新订单信息失败] [TBTSDK Source:"+source+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+amount+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
							res.getWriter().write("{\"status\":\"fail\"} ");
						}
						return;
					}
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：TBTSDK] [失败：订单不存在] [TBTSDK Source:"+source+"] [传入订单id:"+trade_no+"] [实际支付金额:"+amount+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("{\"status\":\"fail\"} ");
					return;
				}
		}
		else
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：TBTSDK] [失败:校验字符串失败] [md5pre:"+md5pre+"] [传回校验字符串:"+sign+"] [生成校验字符串:"+my_sign+"] [传入参数 souce:"+source+"] [传入参数 trade_no:"+trade_no+"] [传入参数 amount:"+amount+"] [传入参数 partner:"+partner+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			res.getWriter().write("{\"status\":\"fail\"} ");
			return;
		}
		
		
	}
}
