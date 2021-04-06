package com.fy.boss.platform.aiwan;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.StringUtil;

public class AiWanSavingCallBackServlet extends HttpServlet{
//剑缘情侠
	public static final String app_key = "ef00253243f359c47b940d8a924bd170";
	protected void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		long now = System.currentTimeMillis();
		
		try {
			req.setCharacterEncoding("utf-8");
			String app_id = req.getParameter("app_id");
			String cp_order_id = java.net.URLEncoder.encode(req.getParameter("cp_order_id"));
			String mem_id = req.getParameter("mem_id");
			String order_id = req.getParameter("order_id");
			String order_status = req.getParameter("order_status");
			String pay_time = req.getParameter("pay_time");
			String product_id = java.net.URLEncoder.encode(req.getParameter("product_id"));
			String product_name = java.net.URLEncoder.encode(req.getParameter("product_name"));
			String product_price = java.net.URLEncoder.encode(req.getParameter("product_price"));
			String sign = req.getParameter("sign");
//			String ext = java.net.URLEncoder.encode(req.getParameter("ext"));
//		app_id=1&cp_order_id=20161028111&ext=kjsdkjdskjdsdkjs&mem_id=12325&order_id=14794504894304304120001&order_status=2&pay_time=1479450489&product_id=1&product_name=%E5%85%83%E5%AE%9D&product_price=1&app_key=f875364690581668449d4cf0aeb60560
			String signStr = "app_id="+app_id+"&cp_order_id="+cp_order_id+"&mem_id="+mem_id+"&order_id="+order_id+"&order_status="+order_status+"&pay_time="+pay_time+"&product_id="+product_id+"&product_name="+product_name+"&product_price="+product_price+"&app_key="+app_key;
			String mysign = StringUtil.hash(signStr);
			if(!order_status.equals("2")){
				res.getWriter().write("FAILURE");
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：爱玩2SDK] [失败:充值失败] [orderid:"+cp_order_id+"] [amount:"+product_price+"] [order_id:"+order_id+"] [order_status:"+order_status+"] [costs:"+(System.currentTimeMillis()-now)+"]");
				return;
			}
			if(sign.equals(mysign)){
				OrderForm order = OrderFormManager.getInstance().getOrderForm(cp_order_id);
				if(order == null){
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：爱玩2SDK] [核验订单状态结果:没找到订单] [orderid:"+cp_order_id+"] [amount:"+product_price+"] [order_id:"+order_id+"] [order_status:"+order_status+"] [costs:"+(System.currentTimeMillis()-now)+"]");
					res.getWriter().write("FAILURE");
					return;
				}
				
				long oldPayMoney = order.getPayMoney();
				synchronized(order) {
					if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：爱玩2SDK] [失败:此订单已经回调过了] [orderid:"+cp_order_id+"] [order_id:"+order_id+"] [amount:"+product_price+"] [gamename:"+order.getServerName()+"] [costs:"+(System.currentTimeMillis()-now)+"]");
						res.getWriter().write("FAILURE");
						return;
					}
					order.setResponseTime(System.currentTimeMillis());
					order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					order.setResponseResult(OrderForm.RESPONSE_SUCC);
					order.setPayMoney((long)(Double.valueOf(product_price) * 100));					
					try {
						OrderFormManager.getInstance().update(order);
						res.getWriter().write("SUCCESS");
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：爱玩2SDK] [更新订单信息失败] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] gamename:"+order.getServerName()+"] [数据库中记录金额:"+order.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-now)+"]",e);
						res.getWriter().write("FAILURE");
						return;
					}
				}
				PlatformSavingCenter.logger.error("[充值回调] [成功] [充值平台：爱玩2SDK] [订单:"+cp_order_id+"] [order_id:"+order_id+"] [amount:"+product_price+"] [oldPayMoney:"+oldPayMoney+"] [costs:"+(System.currentTimeMillis()-now)+"]");
			}else{
				PlatformSavingCenter.logger.error("[充值回调] [失败:签名失败]  [充值平台：爱玩2SDK] [sign:"+sign+"] [mysign:"+mysign+"] [signStr:"+signStr+"] [costs:"+(System.currentTimeMillis()-now)+"]");
				res.getWriter().write("FAILURE");
				return;
			}
		}catch (Exception e) {
			e.printStackTrace();
			PlatformSavingCenter.logger.info("[充值回调] [解析登录返回数据失败] [充值平台：爱玩2SDK] ["+(System.currentTimeMillis()-now)+"ms]", e);
			res.getWriter().write("FAILURE");
			return;
		}
	}
	
}
