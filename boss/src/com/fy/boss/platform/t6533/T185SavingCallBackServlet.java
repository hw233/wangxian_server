package com.fy.boss.platform.t6533;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.StringUtil;

public class T185SavingCallBackServlet extends HttpServlet {

    String appSecret = "613614ebaaa869d571a0b653c50a2ed1";

	protected void service(HttpServletRequest req, HttpServletResponse res){
		long startTime = System.currentTimeMillis(); 
		try{
			req.setCharacterEncoding("utf-8");
//			String str2 = FileUtils.getContent(req.getInputStream());
//			String queryString = str2;
//	    	String[] queryStringArr=queryString.split("&");
//			String[] queryItemArr=new String[2];
//			Map<String, String> data = new TreeMap<String, String>();
//			String tempStr="";
//			for(String str : queryStringArr){
//				queryItemArr=str.split("=");
//				tempStr="";
//				if(queryItemArr.length==2) {
//					tempStr=queryItemArr[1];
//				}
//				data.put(queryItemArr[0],URLDecoder.decode(tempStr,"utf-8"));
//			}
//			String sign = data.get("sign");
//			String orderID = data.get("orderID");
			
			String sign = req.getParameter("sign");
			String orderID = req.getParameter("orderID");
			String status = req.getParameter("status");
			String deviceType = req.getParameter("deviceType");
			String channelID = req.getParameter("channelID");
			String userID = req.getParameter("userID");
			String serverID = req.getParameter("serverID");
			String roleID = req.getParameter("roleID");
			String productID = req.getParameter("productID");
			String currency = req.getParameter("currency");
			String amount = req.getParameter("amount");
			String time = req.getParameter("time");
			String extension = req.getParameter("extension");
			
			StringBuilder sb = new StringBuilder();
			sb.append("orderID=").append(orderID).append("&")
			.append("status=").append(status).append("&")
			.append("deviceType=").append(deviceType).append("&")
			.append("channelID=").append(channelID).append("&")
			.append("userID=").append(userID).append("&")
			.append("serverID=").append(serverID).append("&")
			.append("roleID=").append(roleID).append("&")
			.append("productID=").append(productID).append("&")
			.append("currency=").append(currency).append("&")
			.append("amount=").append(amount).append("&")
			.append("time=").append(time).append("&")
			.append("extension=").append(extension).append("&")
			.append("key=").append(appSecret); //后台 RH_AppSecret

	        String mysign = StringUtil.hash(sb.toString());
	        if(mysign.equals(sign)){

				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(extension);
				if(orderForm != null)
				{
					PassportManager passportManager = PassportManager.getInstance();
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：185] [失败:此订单已经回调过] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("success");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2(sb.toString());
					orderForm.setMediumInfo(userID+"@"+amount+"@");
					orderForm.setPayMoney((long)Double.parseDouble(amount));
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：185] [失败:更新订单信息时报错] ["+sb.toString()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("FAIL");
						return;
					}
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：185] [成功] ["+sb.toString()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("success");
					return;
				}
				else
				{
		
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：185] [失败:查询订单失败,无匹配订单] ["+sb.toString()+"] [username:--] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("SUCCESS");
					return;
				}
			
	        }else{
	        	PlatformSavingCenter.logger.error("[充值回调] [充值平台：185] [失败:签名不正确] [mysign:"+mysign+"] [sign:"+sign+"] ["+sb.toString()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
	        }
		}catch(Exception e){
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：185] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("FAIL");
			} catch (IOException e1) {
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：185] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
	}
	
}
