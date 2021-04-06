package com.fy.boss.platform.appchina;

import java.io.IOException;
import java.util.HashMap;
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

public class AppChinaSavingCallBackServlet extends HttpServlet {

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		try{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
//			String str = FileUtils.getContent(req.getInputStream());
			String str = req.getParameter("transdata");
			
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇] [test] [str:"+str+"]");
			JacksonManager jm = JacksonManager.getInstance();
			Map dataMap = (Map)jm.jsonDecodeObject(str);
			HashMap<String,String> params = new HashMap<String,String>();
			params.putAll(dataMap);
			params.remove("sign");
			
			String exorderno = (String)dataMap.get("exorderno");
			String transid = (String)dataMap.get("transid");
			String waresid = (String)dataMap.get("waresid");
//			String chargepoint = dataMap.get("chargepoint")+"";
			String chargepoint = "";
			
			String feetype = dataMap.get("feetype")+"";
			String money = dataMap.get("money")+"";
			String count = dataMap.get("count")+"";
			String result = dataMap.get("result")+"";
			String transtype = dataMap.get("transtype")+"";
			String transtime = dataMap.get("transtime")+"";
			String sign = dataMap.get("sign")+"";
			String miyao = "tq2bmebccyfak7x7206scs4x";
			String my_sign = StringUtil.hash(exorderno + transid + waresid  + feetype + money + count + result +
					transtype + transtime + miyao);
//			String my_sign = StringUtil.hash(exorderno + transid + waresid + chargepoint + feetype + money + count + result +
//					transtype + transtime + miyao);
			
			//去掉sign标记 得到所有参数
			//验证sign
			if(my_sign.equalsIgnoreCase(sign))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(exorderno);
				
				//通过orderId查订单
				if(orderForm != null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇] [失败：订单已经回调过了] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"] [返回字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("SUCCESS");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2("[外部订单号:"+exorderno+"] [交易流水号:"+transid+"] [商品编号："+waresid+"] [计费点编号:"+chargepoint+"] [计费类型："+feetype+"] [交易金额:"+money+"] [购买数量:"+count+"] [交易结果:"+result+"] [交易类型:"+transtype+"] [交易时间:"+transtime+"] [sign:"+sign+"]");
					orderForm.setResponseDesp("应用汇回调充值接口成功");
					if(Long.valueOf(money) != orderForm.getPayMoney())
					{
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：应用汇] [警告：传回的充值金额和订单存储的金额不一致] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"] [返回字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					if(result.trim().equals("0")) {
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						orderForm.setPayMoney(Long.valueOf(money));
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：应用汇] [成功] [用户充值] [成功] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"] [返回字符串:"+str+"] [充值结果:"+result+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					else
					{
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						orderForm.setResponseDesp("应用汇交易失败");
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：应用汇] [成功] [用户充值] [失败] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"] [返回字符串:"+str+"] [充值结果:"+result+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇] [失败：更新订单出错] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"] [返回字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("failure");
						return;
					}
					
					res.getWriter().write("SUCCESS");
					return;
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇] [失败：无参数指定订单] [交易流水号:"+transid+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [购买数量:"+count+"] [交易时间:"+transtime+"] [返回字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("failure");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇] [失败：签名验证失败] [交易流水号:"+transid+"] [充值类型:"+feetype+"] [计费点编号:"+chargepoint+"] [交易金额:"+money+"] [购买数量:"+count+"] [交易时间:"+transtime+"] [传入字符串:"+str+"] [加密前:"+exorderno + transid + waresid + chargepoint + feetype + money + count + result +
						transtype + transtime+miyao+"] [sign:"+sign+"] [my sign:"+my_sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("failure");
				return;
			}
		}catch(Exception e){
			e.printStackTrace();
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇] [异常]",e);
		}
		
		
	}
	
}
