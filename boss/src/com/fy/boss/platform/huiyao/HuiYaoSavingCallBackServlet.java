package com.fy.boss.platform.huiyao;

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

public class HuiYaoSavingCallBackServlet extends HttpServlet{

	public static final String app_key = "8F53295A73878494E9BC8DD6C3C7104F";
	
	protected void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		long now = System.currentTimeMillis();
		req.setCharacterEncoding("utf-8");
		String transdata = req.getParameter("transdata");
		String sign = req.getParameter("sign");
		
		//sign =md5(transdata + &key=app_key)
		String mysign = StringUtil.hash(transdata+"&key="+app_key);
		
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(transdata);
			Integer result = (Integer)map.get("result");
			if(result == 0){
				Integer amount = (Integer)map.get("amount");
				String app_order_id = (String)map.get("app_order_id");
				String hy_order_id = (String)map.get("hy_order_id");
				
				if(sign.equalsIgnoreCase(mysign)){
					OrderForm order = OrderFormManager.getInstance().getOrderForm(app_order_id);
					if(order == null){
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：辉耀SDK] [核验订单状态结果:没找到订单] [orderid:"+app_order_id+"] [amount:"+amount+"] [hy_order_id:"+hy_order_id+"] [transdata:"+transdata+"] [costs:"+(System.currentTimeMillis()-now)+"]");
						res.getWriter().write("FAIL");
						return;
					}
					
					long oldPayMoney = order.getPayMoney();
					synchronized(order) {
						if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：辉耀SDK] [失败:此订单已经回调过了] [orderid:"+app_order_id+"] [hy_order_id:"+hy_order_id+"] [amount:"+amount+"] [gamename:"+order.getServerName()+"] [transdata:"+transdata+"] [costs:"+(System.currentTimeMillis()-now)+"]");
							res.getWriter().write("FAIL");
							return;
						}
						//更新订单
						order.setResponseTime(System.currentTimeMillis());
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setResponseResult(OrderForm.RESPONSE_SUCC);
						//以渠道返回的充值额度为准
						order.setPayMoney((long)(Long.valueOf(amount)));					
						try {
							OrderFormManager.getInstance().update(order);
							res.getWriter().write("SUCCESS");
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：辉耀SDK] [更新订单信息失败] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] [[hy_order_id:"+hy_order_id+"] gamename:"+order.getServerName()+"] [数据库中记录金额:"+order.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-now)+"]",e);
							res.getWriter().write("FAIL");
							return;
						}
					}
					PlatformSavingCenter.logger.error("[充值回调] [成功] [充值平台：辉耀SDK] [订单:"+app_order_id+"] [hy_order_id:"+hy_order_id+"] [amount:"+amount+"] [oldPayMoney:"+oldPayMoney+"] [transdata:"+transdata+"] [costs:"+(System.currentTimeMillis()-now)+"]");
				}else{
					PlatformSavingCenter.logger.error("[充值回调] [失败:签名失败]  [充值平台：辉耀SDK] [sign:"+sign+"] [mysign:"+mysign+"] [transdata:"+transdata+"] [costs:"+(System.currentTimeMillis()-now)+"]");
					res.getWriter().write("FAIL");
					return;
				}
			}else{
				PlatformSavingCenter.logger.info("[充值回调] [解析登录返回数据失败] [充值平台：辉耀SDK] [transdata:"+transdata+"] [sign:"+sign+"] [mysign:"+mysign+"] ["+(System.currentTimeMillis()-now)+"ms]");
				res.getWriter().write("FAIL");
				return;
			}
		}catch (Exception e) {
			PlatformSavingCenter.logger.info("[充值回调] [解析登录返回数据失败] [充值平台：辉耀SDK] [transdata:"+transdata+"] [sign:"+sign+"] [mysign:"+mysign+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
			res.getWriter().write("FAIL");
			return;
		}
	}
	
}
