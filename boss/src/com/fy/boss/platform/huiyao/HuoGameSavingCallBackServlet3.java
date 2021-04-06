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

public class HuoGameSavingCallBackServlet3 extends HttpServlet{

	public static final String appSecret = "jiuzpml";
	protected void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		long now = System.currentTimeMillis();
		
		try {
			req.setCharacterEncoding("utf-8");
			String out_trade_no = req.getParameter("out_trade_no");
			String price = req.getParameter("price");
			String pay_status = req.getParameter("pay_status");
			String extend = req.getParameter("extend");
			String signType = req.getParameter("signType");
			String sign = req.getParameter("sign");
			
//			String product_id = java.net.URLEncoder.encode(req.getParameter("product_id"));
//			String ext = java.net.URLEncoder.encode(req.getParameter("ext"));
//			String signStr = "out_trade_no="+out_trade_no+"&price="+price+"&pay_status="+pay_status+"&extend="+extend+appSecret;
			String signStr = out_trade_no+price+pay_status+extend+appSecret;
			String mysign = StringUtil.hash(signStr);
			
			if(pay_status == null || !pay_status.equals("1")){
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：九州飘渺曲] [支付状态不正确] [orderid:"+out_trade_no+"] [amount:"+price+"] [pay_status:"+pay_status+"] [extend:"+extend+"] [costs:"+(System.currentTimeMillis()-now)+"]");
				res.getWriter().write("FAILURE");
				return;
			}
			if(sign.equals(mysign)){
				OrderForm order = OrderFormManager.getInstance().getOrderForm(extend);
				if(order == null){
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：九州飘渺曲] [核验订单状态结果:没找到订单] [orderid:"+out_trade_no+"] [amount:"+price+"] [pay_status:"+pay_status+"] [extend:"+extend+"] [costs:"+(System.currentTimeMillis()-now)+"]");
					res.getWriter().write("FAILURE");
					return;
				}
				
				long oldPayMoney = order.getPayMoney();
				synchronized(order) {
					if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：九州飘渺曲] [失败:此订单已经回调过了] [orderid:"+order.getId()+"] [amount:"+price+"] [gamename:"+order.getServerName()+"] [pay_status:"+pay_status+"] [extend:"+extend+"] [costs:"+(System.currentTimeMillis()-now)+"]");
						res.getWriter().write("FAILURE");
						return;
					}
					order.setResponseTime(System.currentTimeMillis());
					order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					order.setResponseResult(OrderForm.RESPONSE_SUCC);
					long qian = (long)Double.parseDouble(price) * 100;
					order.setPayMoney(qian);					
					try {
						OrderFormManager.getInstance().update(order);
						res.getWriter().write("success");
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：九州飘渺曲] [更新订单信息失败] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] gamename:"+order.getServerName()+"] [数据库中记录金额:"+order.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-now)+"]",e);
						res.getWriter().write("FAILURE");
						return;
					}
				}
				PlatformSavingCenter.logger.error("[充值回调] [成功] [充值平台：九州飘渺曲] [订单:"+order.getId()+"] [amount:"+price+"元] [oldPayMoney:"+oldPayMoney+"] [costs:"+(System.currentTimeMillis()-now)+"]");
			}else{
				PlatformSavingCenter.logger.error("[充值回调] [失败:签名失败]  [充值平台：九州飘渺曲] [sign:"+sign+"] [mysign:"+mysign+"] [signStr:"+signStr+"] [costs:"+(System.currentTimeMillis()-now)+"]");
				res.getWriter().write("FAILURE");
				return;
			}
		}catch (Exception e) {
			e.printStackTrace();
			PlatformSavingCenter.logger.info("[充值回调] [解析登录返回数据失败] [充值平台：九州飘渺曲] ["+(System.currentTimeMillis()-now)+"ms]", e);
			res.getWriter().write("FAILURE");
			return;
		}
	}
	
}
