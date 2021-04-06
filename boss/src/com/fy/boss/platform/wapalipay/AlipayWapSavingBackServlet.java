package com.fy.boss.platform.wapalipay;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.wapalipay.AlipayWapSavingManager;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class AlipayWapSavingBackServlet extends HttpServlet {

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		InputStream is =  req.getInputStream();
		String service = req.getParameter("service");
		String v = req.getParameter("v");
		String sec_id = req.getParameter("sec_id");
		
		String notify_data = req.getParameter("notify_data");
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("notify_data", notify_data);
		//service=alipay.wap.trade.create.direct&v=1.0&sec_id=0001&notify_data=<notify >...</notify>
		String str = "service="+service+"&v="+v+"&sec_id="+sec_id+"&notify_data="+notify_data+AlipayWapSavingManager.publicKey;
		str = URLDecoder.decode(str,"utf-8");
		
		String sign = StringUtil.hash(str);
		String getSign = req.getParameter("sign");
		if(!sign.equalsIgnoreCase(getSign)) {
			PlatformSavingCenter.logger.warn("[充值回调] [充值平台：Wap支付宝] [失败:签名校验失败] [secure_mode:MD5] [notify_data:"+notify_data+"] [getSign:"+getSign+"] [mySign:"+sign+"] [str:"+str+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			res.getWriter().write("success");
			return;
		}
		
		try {
			Document dom = null;
			try {
				dom = XmlUtil.loadString(notify_data);
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[充值回调] [失败] [解析响应内容信息] [失败] [待解析的字符串:"+notify_data+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				res.getWriter().write("success");
				return;
			}
			Element root = dom.getDocumentElement();
			
			String paytype = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "payment_type"), "", null);
			String zhifubaoTradeNo = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "trade_no"), "", null);
			String buyer_mail = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "buyer_email"), "", null);
			String orderId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "out_trade_no"), "", null);
			String amount = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "total_fee"), "", null);
			if(StringUtils.isEmpty(amount))
			{
				amount = "0";
			}
			
			String ret = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "trade_status"), "", null);
			
			OrderForm order = OrderFormManager.getInstance().getOrderForm(orderId);
			if(order == null) {
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：Wap支付宝] [失败:找不到对应的订单] [order_id:"+orderId+"] [notify_data:"+notify_data+"] [buyeremail:"+buyer_mail+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				res.getWriter().write("success");
				return;		
			}
			if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：Wap支付宝] [失败:此订单已经回调过了] [order_id:"+orderId+"] [notify_data:"+notify_data+"] [buyeremail:"+buyer_mail+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				res.getWriter().write("success");
				return;					
			}
			long amountl = (long)(Double.valueOf(amount)*100);
			
			Passport passport = PassportManager.getInstance().getPassport(order.getPassportId());
			if(ret.equals("TRADE_SUCCESS")) {
				//支付成功，以传入的金额为准
				order.setPayMoney(amountl);
				order.setResponseResult(OrderForm.RESPONSE_SUCC);
				order.setResponseDesp("TradeID：" + zhifubaoTradeNo);
				order.setChannelOrderId(zhifubaoTradeNo);
				OrderFormManager.getInstance().update(order);
				PlatformSavingCenter.logger.info("[充值回调] [充值平台：Wap支付宝] [成功完成充值] [订单:"+order.getOrderId()+"] [充值金额:"+amount+"] [username:"+passport.getUserName()+"] [buyeremail:"+buyer_mail+"] [server:"+order.getServerName()+"] [channel:"+order.getUserChannel()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} else  if(!ret.equals("WAIT_BUYER_PAY")){
				order.setResponseResult(OrderForm.RESPONSE_FAILED);
				order.setPayMoney(amountl);
				order.setResponseDesp("TradeID：" + zhifubaoTradeNo);
				order.setChannelOrderId(zhifubaoTradeNo);
				OrderFormManager.getInstance().update(order);
				PlatformSavingCenter.logger.info("[充值回调] [充值平台：Wap支付宝] [失败：返回充值失败] [订单:"+order.getOrderId()+"] [充值金额:"+amount+"] [tradeID:"+zhifubaoTradeNo+"] [username:"+passport.getUserName()+"] [buyeremail:"+buyer_mail+"] [server:"+order.getServerName()+"] [channel:"+order.getUserChannel()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.info("[充值回调] [充值平台：Wap支付宝] [等待买家付款]  [充值金额:"+amount+"] [tradeID:"+zhifubaoTradeNo+"] [username:"+passport.getUserName()+"] [buyeremail:"+buyer_mail+"] [server:"+order.getServerName()+"] [channel:"+order.getUserChannel()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			res.getWriter().write("success");
			return;
		} catch(Exception e) {
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：Wap支付宝] [异常] [notify_data:"+notify_data+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		res.getWriter().write("success");
		return;
	}
	
}
