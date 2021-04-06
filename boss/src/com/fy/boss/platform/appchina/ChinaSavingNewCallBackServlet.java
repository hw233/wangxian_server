package com.fy.boss.platform.appchina;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.appchina.unit.CpTransSyncSignValid;
import com.fy.boss.tools.JacksonManager;

public class ChinaSavingNewCallBackServlet extends HttpServlet {

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		
		String transdata = (String)req.getParameter("transdata");
		String sign = req.getParameter("sign");
		
		JacksonManager jm = JacksonManager.getInstance();
		Map dataMap = (Map)jm.jsonDecodeObject(transdata);
		
		String exorderno = (String)dataMap.get("exorderno");
		String transid = (String)dataMap.get("transid");
		String appid = (String)dataMap.get("appid");
		String waresid = ((Integer)dataMap.get("waresid")).intValue() + "";
		String chargepoint = dataMap.get("chargepoint")+"";
		String feetype = ((Integer)dataMap.get("feetype")).intValue()+"";
		String money = ((Integer)dataMap.get("money")).intValue()+"";
		String count = dataMap.get("count")+"";
		String result = dataMap.get("result")+"";
		String transtype = dataMap.get("transtype")+"";
		String transtime = dataMap.get("transtime")+"";
		String cpprivate = dataMap.get("cpprivate")+"";
		
		String miyao = "RTIwNjY0RjAxODYyNTY5NzgyRjkwOTFFRTBBQTJDRkY0MDc1MjczQU1UWTFOVFkwTkRreU5ERTNOemMyTWpNNU9EY3JNVEUxTXpRek1qVXdOREV5TWpJd05ERTRNRE0yTkRJeE56SXpOemM1T0RnNU1qSTJNVEV4";
		
		//去掉sign标记 得到所有参数
		//验证sign
		boolean isRight = false;
		try{
			isRight = CpTransSyncSignValid.validSign(transdata,sign.trim(),miyao);
			PlatformSavingCenter.logger.warn("[充值回调] [充值平台：应用汇NEWSDK] [test] [isRight:"+isRight+"] [transdata:"+transdata+"] [sign:"+sign+"] [key:"+miyao+"] ");
		}catch(Exception e){
			e.printStackTrace();
			PlatformSavingCenter.logger.warn("[充值回调] [充值平台：应用汇NEWSDK] [isRight:"+isRight+"] [transdata:"+transdata+"] [sign:"+sign+"] [key:"+miyao+"] [异常"+e+"] ");
		}
		
		if(isRight)
		{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
			OrderForm orderForm = orderFormManager.getOrderForm(exorderno);
			
				//通过orderId查订单
			if(orderForm != null)
			{
				synchronized(orderForm) {
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇NEWSDK] [失败：订单已经回调过了] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
						res.getWriter().write("SUCCESS");
						return;
					}
				}
				orderForm.setResponseTime(System.currentTimeMillis());
				orderForm.setMemo2("[外部订单号:"+exorderno+"] [交易流水号:"+transid+"] [商品编号："+waresid+"] [计费点编号:"+chargepoint+"] [计费类型："+feetype+"] [交易金额:"+money+"] [购买数量:"+count+"] [交易结果:"+result+"] [交易类型:"+transtype+"] [交易时间:"+transtime+"] [sign:"+sign+"]");
				orderForm.setResponseDesp("应用汇回调充值接口成功");
				if(Long.valueOf(money) != orderForm.getPayMoney())
				{
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：应用汇NEWSDK] [警告：传回的充值金额和订单存储的金额不一致] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				if(result.trim().equals("0")) {
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					orderForm.setPayMoney(Long.valueOf(money));
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：应用汇NEWSDK] [成功] [用户充值] [成功] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"]  [充值结果:"+result+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				else
				{
					orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
					orderForm.setResponseDesp("应用汇交易失败");
					PlatformSavingCenter.logger.info("[充值回调] [充值平台：应用汇NEWSDK] [成功] [用户充值] [失败] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"]  [充值结果:"+result+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				try {
					orderFormManager.update(orderForm);
				} catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇NEWSDK] [失败：更新订单出错] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					res.getWriter().write("failure");
					return;
				}
				
				res.getWriter().write("SUCCESS");
				return;
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇NEWSDK] [失败：无参数指定订单] [交易流水号:"+transid+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [购买数量:"+count+"] [交易时间:"+transtime+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("failure");
				return;
			}
		}
		else
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇NEWSDK] [失败：签名验证失败] [交易流水号:"+transid+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [购买数量:"+count+"] [交易时间:"+transtime+"] [加密前:"+exorderno + transid + waresid + chargepoint + feetype + money + count + result +
					transtype + transtime+miyao+"] [sign:"+sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			res.getWriter().write("failure");
			return;
		}
		
		
	}
	
}
