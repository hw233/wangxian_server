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

public class GuoPanSavingCallBackServlet extends HttpServlet{

	public static final String SERVER_KEY = "HGHGS124382H2STGY0TW2M2CQQCUADBET7XSLXTGL8AG91F60PEHLJSNYLFXU15S";
	protected void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		long now = System.currentTimeMillis();
		
		try {
			req.setCharacterEncoding("utf-8");
			String trade_no = req.getParameter("trade_no");
			String serialNumber = req.getParameter("serialNumber");
			String money = req.getParameter("money");
			String status = req.getParameter("status");
			String t = req.getParameter("t");
			String sign = req.getParameter("sign");
			String game_uin = req.getParameter("game_uin");
			
//			sign=md5(serialNumber +money+status+t+SERVER_KEY) 
			String signStr = serialNumber+money+status+t+SERVER_KEY;
			String mysign = StringUtil.hash(signStr);
			
			if(status == null || !status.equals("1")){
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：果盘] [支付状态不正确] [uid:"+game_uin+"] [orderid:"+serialNumber+"] [果盘订单:"+trade_no+"] [money:"+money+"] [status:"+status+"] [costs:"+(System.currentTimeMillis()-now)+"]");
				res.getWriter().write("FAILURE");
				return;
			}
			if(sign.equals(mysign)){
				OrderForm order = OrderFormManager.getInstance().getOrderForm(serialNumber);
				if(order == null){
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：果盘] [核验订单状态结果:没找到订单] [uid:"+game_uin+"] [orderid:"+trade_no+"] [money:"+money+"] [status:"+status+"] [costs:"+(System.currentTimeMillis()-now)+"]");
					res.getWriter().write("FAILURE");
					return;
				}
				
				long oldPayMoney = order.getPayMoney();
				synchronized(order) {
					if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：果盘] [失败:此订单已经回调过了] [uid:"+game_uin+"] [orderid:"+order.getId()+"] [money:"+money+"] [gamename:"+order.getServerName()+"] [status:"+status+"] [costs:"+(System.currentTimeMillis()-now)+"]");
						res.getWriter().write("FAILURE");
						return;
					}
					order.setResponseTime(System.currentTimeMillis());
					order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					order.setResponseResult(OrderForm.RESPONSE_SUCC);
					long qian = (long)Double.parseDouble(money) * 100;
					order.setPayMoney(qian);					
					try {
						OrderFormManager.getInstance().update(order);
						res.getWriter().write("success");
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：果盘] [更新订单信息失败] [uid:"+game_uin+"] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] gamename:"+order.getServerName()+"] [数据库中记录金额:"+order.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-now)+"]",e);
						res.getWriter().write("FAILURE");
						return;
					}
				}
				PlatformSavingCenter.logger.error("[充值回调] [成功] [充值平台：果盘] [uid:"+game_uin+"] [订单:"+order.getId()+"] [money:"+money+"元] [oldPayMoney:"+oldPayMoney+"] [costs:"+(System.currentTimeMillis()-now)+"]");
			}else{
				PlatformSavingCenter.logger.error("[充值回调] [失败:签名失败]  [充值平台：果盘] [uid:"+game_uin+"] [sign:"+sign+"] [mysign:"+mysign+"] [signStr:"+signStr+"] [costs:"+(System.currentTimeMillis()-now)+"]");
				res.getWriter().write("FAILURE");
				return;
			}
		}catch (Exception e) {
			e.printStackTrace();
			PlatformSavingCenter.logger.info("[充值回调] [解析登录返回数据失败] [充值平台：果盘] ["+(System.currentTimeMillis()-now)+"ms]", e);
			res.getWriter().write("FAILURE");
			return;
		}
	}
	
}
