package com.fy.boss.platform.t6533;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class T6533SavingCallBackServlet extends HttpServlet {

    String appSecret = "8a95c501f17f9e06b0a85ea9d172d439";

	protected void service(HttpServletRequest req, HttpServletResponse res){
		long startTime = System.currentTimeMillis(); 
		try{
			req.setCharacterEncoding("utf-8");
			String str2 = FileUtils.getContent(req.getInputStream());
			String queryString = str2;
	    	String[] queryStringArr=queryString.split("&");
			String[] queryItemArr=new String[2];
			Map<String, String> data = new TreeMap<String, String>();
			String tempStr="";
			for(String str : queryStringArr){
				queryItemArr=str.split("=");
				tempStr="";
				if(queryItemArr.length==2) {
					tempStr=queryItemArr[1];
				}
				data.put(queryItemArr[0],URLDecoder.decode(tempStr,"utf-8"));
			}
			String sign = data.get("sign");
			String orderID = data.get("orderID");
			
			StringBuilder sb = new StringBuilder();
			sb.append("orderID=").append(data.get("orderID")).append("&")
			.append("status=").append(data.get("status")).append("&")
			.append("deviceType=").append(data.get("deviceType")).append("&")
			.append("channelID=").append(data.get("channelID")).append("&")
			.append("userID=").append(data.get("userID")).append("&")
			.append("serverID=").append(data.get("serverID")).append("&")
			.append("roleID=").append(data.get("roleID")).append("&")
			.append("productID=").append(data.get("productID")).append("&")
			.append("currency=").append(data.get("currency")).append("&")
			.append("amount=").append(data.get("amount")).append("&")
			.append("time=").append(data.get("time")).append("&")
			.append("extension=").append(data.get("extension")).append("&")
			.append("key=").append(appSecret); //后台 RH_AppSecret

	        String mysign = StringUtil.hash(sb.toString());
	        if(mysign.equals(sign)){

				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(orderID);
				if(orderForm != null)
				{
					PassportManager passportManager = PassportManager.getInstance();
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：6533] [失败:此订单已经回调过] [orderId:"+data.get("orderID")+"] [username:"+data.get("userID")+"] [充值金额:"+data.get("amount")+"] [传入内容:"+queryString+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("success");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2(sb.toString());
					orderForm.setMediumInfo(data.get("userID")+"@"+data.get("amount")+"@");
					orderForm.setPayMoney((long)Double.parseDouble(data.get("amount")));
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：6533] [失败:更新订单信息时报错] ["+sb.toString()+"] [str:"+queryString+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("FAIL");
						return;
					}
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：6533] [成功] ["+sb.toString()+"] [传入内容:"+queryString+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("success");
					return;
				}
				else
				{
		
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：6533] [失败:查询订单失败,无匹配订单] ["+sb.toString()+"] [str:"+queryString+"] [username:--] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("success");
					return;
				}
			
	        }else{
	        	PlatformSavingCenter.logger.error("[充值回调] [充值平台：6533] [失败:签名不正确] [mysign:"+mysign+"] [sign:"+sign+"] ["+sb.toString()+"] [str:"+queryString+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
	        }
		}catch(Exception e){
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：6533] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("FAIL");
			} catch (IOException e1) {
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：6533] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
	}
	
}
