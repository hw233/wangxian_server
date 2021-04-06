package com.fy.boss.platform.huiyao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.StringUtil;

public class HaoDongSavingCallBackServlet2 extends HttpServlet{

	public static final String appSecret = "5826287c518d4cba900174b039fc6eb1";
	protected void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		long now = System.currentTimeMillis();
		
		try {
			req.setCharacterEncoding("utf-8");
			String userID = req.getParameter("userID");
			String orderID = req.getParameter("orderID");
			String gameOrderID = req.getParameter("gameOrderID");
			String channelID = req.getParameter("channelID");
			String gameID = req.getParameter("gameID");
			String serverID = req.getParameter("serverID");
			String roleID = req.getParameter("roleID");
			String productID = req.getParameter("productID");
			String money = req.getParameter("money");//充值金额，单位分
			String currency = req.getParameter("currency");
			String extension = req.getParameter("extension");
			String sign = req.getParameter("sign");
			
//			String product_id = java.net.URLEncoder.encode(req.getParameter("product_id"));
//			String ext = java.net.URLEncoder.encode(req.getParameter("ext"));
//			                  channelID=[channelID]&currency=[currency]&extension=[extension]&gameID=[gameID]&money=[money]&orderID=[orderID]&productID=[productID]&serverID=[serverID]&userID=[userID]&[appSecret]
			String signStr = "channelID="+channelID+"&currency="+currency+"&extension="+extension+"&gameID="+gameID+"&money="+money+"&orderID="+orderID+"&productID="+productID+"&serverID="+serverID+"&userID="+userID+"&"+appSecret;
			String mysign = StringUtil.hash(signStr);
			if(sign.equals(mysign)){
				OrderForm order = OrderFormManager.getInstance().getOrderForm(gameOrderID);
				if(order == null){
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：浩动SDK] [核验订单状态结果:没找到订单] [orderid:"+gameOrderID+"] [amount:"+money+"] [orderID:"+orderID+"] [costs:"+(System.currentTimeMillis()-now)+"]");
					res.getWriter().write("FAILURE");
					return;
				}
				
				long oldPayMoney = order.getPayMoney();
				synchronized(order) {
					if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：浩动SDK] [失败:此订单已经回调过了] [orderid:"+order.getId()+"] [order_id:"+orderID+"] [amount:"+money+"] [gamename:"+order.getServerName()+"] [costs:"+(System.currentTimeMillis()-now)+"]");
						res.getWriter().write("FAILURE");
						return;
					}
					order.setResponseTime(System.currentTimeMillis());
					order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					order.setResponseResult(OrderForm.RESPONSE_SUCC);
					order.setPayMoney(Long.parseLong(money));					
					try {
						OrderFormManager.getInstance().update(order);
						res.getWriter().write("SUCCESS");
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：浩动SDK] [更新订单信息失败] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] gamename:"+order.getServerName()+"] [数据库中记录金额:"+order.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-now)+"]",e);
						res.getWriter().write("FAILURE");
						return;
					}
				}
				PlatformSavingCenter.logger.error("[充值回调] [成功] [充值平台：浩动SDK] [订单:"+gameOrderID+"] [order_id:"+orderID+"] [amount:"+money+"] [oldPayMoney:"+oldPayMoney+"] [costs:"+(System.currentTimeMillis()-now)+"]");
			}else{
				PlatformSavingCenter.logger.error("[充值回调] [失败:签名失败]  [充值平台：浩动SDK] [sign:"+sign+"] [mysign:"+mysign+"] [signStr:"+signStr+"] [costs:"+(System.currentTimeMillis()-now)+"]");
				res.getWriter().write("FAILURE");
				return;
			}
		}catch (Exception e) {
			e.printStackTrace();
			PlatformSavingCenter.logger.info("[充值回调] [解析登录返回数据失败] [充值平台：浩动SDK] ["+(System.currentTimeMillis()-now)+"ms]", e);
			res.getWriter().write("FAILURE");
			return;
		}
	}
	
}
