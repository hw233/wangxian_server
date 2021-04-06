package com.fy.boss.platform.uc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.uc.UCWapSavingManager;
import com.xuanzhi.tools.text.StringUtil;

public class UCWapSavingCallBackServlet extends HttpServlet {

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		String secure_mode = req.getParameter("secure_mode");
		String notify_data = req.getParameter("notify_data");
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("secure_mode", secure_mode);
		map.put("notify_data", notify_data);
		String sign = sign(map, UCWapSavingManager.publicKey);
		String getSign = req.getParameter("sign");
		if(!sign.equalsIgnoreCase(getSign)) {
			PlatformSavingCenter.logger.warn("[充值回调] [充值平台：UCWap支付] [失败:签名校验失败] [secure_mode:"+secure_mode+"] [notify_data:"+notify_data+"] [getSign:"+getSign+"] [mySign:"+sign+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			res.getWriter().write("success");
			return;
		}
		
		try {
			JacksonManager jm = JacksonManager.getInstance();
			Map dataMap = (Map)jm.jsonDecodeObject(notify_data);
			
			String order_id = (String)dataMap.get("order_id");
			OrderForm order = OrderFormManager.getInstance().getOrderForm(order_id);
			if(order == null) {
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：UCWap支付] [失败:找不到对应的订单] [order_id:"+order_id+"] [notify_data:"+notify_data+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				res.getWriter().write("success");
				return;		
			}
			if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：UCWap支付] [失败:此订单已经回调过了] [order_id:"+order_id+"] [notify_data:"+notify_data+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				res.getWriter().write("success");
				return;					
			}
			long amount = (long)(Double.valueOf((String)dataMap.get("order_amt"))*100);
			String tradeId = (String)dataMap.get("trade_id");
			String trade_status = (String)dataMap.get("trade_status");
			Passport passport = PassportManager.getInstance().getPassport(order.getPassportId());
			if(trade_status.equals("S")) {
				//支付成功，以传入的金额为准
				order.setPayMoney(amount);
				order.setResponseResult(OrderForm.RESPONSE_SUCC);
				order.setResponseDesp("UC方的TradeID：" + tradeId);
				OrderFormManager.getInstance().update(order);
				PlatformSavingCenter.logger.info("[充值回调] [充值平台：UCWap支付] [成功完成充值] [订单:"+order.getOrderId()+"] [充值金额:"+amount+"] [username:"+passport.getUserName()+"] [server:"+order.getServerName()+"] [channel:"+order.getUserChannel()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} else {
				order.setResponseResult(OrderForm.RESPONSE_FAILED);
				order.setResponseDesp("UC方的TradeID：" + tradeId);
				OrderFormManager.getInstance().update(order);
				PlatformSavingCenter.logger.info("[充值回调] [充值平台：UCWap支付] [失败：返回充值失败] [订单:"+order.getOrderId()+"] [充值金额:"+amount+"] [tradeID:"+tradeId+"] [username:"+passport.getUserName()+"] [server:"+order.getServerName()+"] [channel:"+order.getUserChannel()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			res.getWriter().write("success");
		} catch(Exception e) {
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：UCWap支付] [异常] [notify_data:"+notify_data+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		res.getWriter().write("failure");
	}
	
	private String sign(HashMap<String,String> params,String secretkey){
	
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v+  "&");
		}
		String md5Str = sb.toString();
		if(md5Str.length() > 0) {
			md5Str = md5Str.substring(0, md5Str.length()-1);
		}
		md5Str = md5Str + secretkey;
		
		String sign = StringUtil.hash(md5Str);
		
		return sign;
	}
}
