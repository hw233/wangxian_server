package com.fy.boss.platform.heepay;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;

public class HeePaySavingBackServlet extends HttpServlet{

	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		long now = System.currentTimeMillis();
		req.setCharacterEncoding("utf-8");
		String result = req.getParameter("result");
		String pay_message = req.getParameter("pay_message");
		String agent_id = req.getParameter("agent_id");
		String jnet_bill_no = req.getParameter("jnet_bill_no");
		String agent_bill_id = req.getParameter("agent_bill_id");
		String pay_type = req.getParameter("pay_type");
		String pay_amt = req.getParameter("pay_amt");
		String remark = req.getParameter("remark");
		String sign = req.getParameter("sign");
		
		//签名验证
		LinkedHashMap<String, String> qianMingMap2 = new LinkedHashMap<String, String>();
		qianMingMap2.put("result",result);
		qianMingMap2.put("agent_id", agent_id);
		qianMingMap2.put("jnet_bill_no",jnet_bill_no);
		qianMingMap2.put("agent_bill_id",agent_bill_id);
		qianMingMap2.put("pay_type",pay_type);
		qianMingMap2.put("pay_amt",pay_amt);
		qianMingMap2.put("remark",remark);
		qianMingMap2.put("key",HeePayManager.getInstance().key);
		
		String qingMingb2 = HeePayManager.getInstance().buildParams(qianMingMap2);
		String my_sign = StringUtil.hash(qingMingb2).toLowerCase();
		
		if(my_sign.equals(sign)){
			
			HashMap headers = new HashMap();
			String content = "";
			String encoding = "";
			Integer code = -1;
			String message= "";
			byte bytes[] = null;
			try {
				if(result.equals("1")){
					//TODO
					OrderForm order = OrderFormManager.getInstance().getOrderForm(agent_bill_id);
					if(order == null){
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：汇付宝] [核验订单状态结果:没找到订单] [orderId:"+agent_bill_id+"] [result:"+result+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-now)+"]");
						res.getWriter().write("fail");
						return;
					}
					
					synchronized(order) {
						if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							res.getWriter().write("success");
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：汇付宝] [失败:此订单已经回调过了] [orderId:"+agent_bill_id+"] [result:"+result+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-now)+"]");
							return;
						}
						//更新订单
						order.setResponseTime(System.currentTimeMillis());
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setResponseResult(OrderForm.RESPONSE_SUCC);
						//以渠道返回的充值额度为准
						order.setPayMoney((long)(Double.valueOf(pay_amt)*100 ));					
						try {
							OrderFormManager.getInstance().update(order);
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：汇付宝] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] [实际支付金额:"+pay_amt+"] [支付类型:"+pay_amt+"] [数据库中记录金额:"+order.getPayMoney()+"] [result:"+result+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-now)+"]");
							res.getWriter().write("success");
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：汇付宝] [更新订单信息失败] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] [实际支付金额:"+pay_amt+"] [数据库中记录金额:"+order.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-now)+"]",e);
							res.getWriter().write("fail");
						}
					}
				}else{
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：汇付宝] [订单处理结果：异常] [支付结果：0=正在处理，1=成功，-1=失败] [result:"+result+"] [result:"+result+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-now)+"]");
				}
			} catch (Exception e) {
				e.printStackTrace();
				encoding = (String)headers.get(HttpUtils.ENCODING);
				code = (Integer)headers.get(HttpUtils.Response_Code);
				message = (String)headers.get(HttpUtils.Response_Message);
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：汇付宝] [核验订单状态结果：异常] [result:"+result+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-now)+"]",e);
				res.getWriter().write("fail");
				return;
			}
		}else{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：汇付宝] [失败:校验字符串失败] [qingMingb2:"+qingMingb2+"] [传回校验字符串:"+sign+"] [生成校验字符串:"+my_sign+"] [costs:"+(System.currentTimeMillis()-now)+"]");
			res.getWriter().write("fail");
			return;
		}
		
	}
	
}
