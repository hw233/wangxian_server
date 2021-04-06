package com.fy.boss.platform.u8;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.u8.util.EncryptUtils;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.FileUtils;

public class V8DKSavingCallBackServlet extends HttpServlet {

    String appSecret = "99db59f8920855fff2e47c4ce96576c1";

	protected void service(HttpServletRequest req, HttpServletResponse res)
	{
		long startTime = System.currentTimeMillis(); 
		try
		{
			req.setCharacterEncoding("utf-8");
			String str = FileUtils.getContent(req.getInputStream());
			JacksonManager jm = JacksonManager.getInstance();
			Map dataMap2 = (Map)jm.jsonDecodeObject(str);
			
			Integer state = (Integer)dataMap2.get("state");
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：v8] [test:"+str+"] [state:"+state+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			if(state != 1){
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：v8] [失败:返回状态不对] [state:"+state+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("FAIL");
				return;
			}
			Map dataMap = (Map) dataMap2.get("data");
			
			String productID = (String)dataMap.get("productID");
			long orderID = (Long)dataMap.get("orderID");
			int userID = (Integer)dataMap.get("userID");
			int channelID = (Integer)dataMap.get("channelID");
			int gameID = (Integer)dataMap.get("gameID");
			String serverID = (String)dataMap.get("serverID");
			int money = (Integer)dataMap.get("money");
			String currency = (String)dataMap.get("currency");
			String extension = (String)dataMap.get("extension");
			String signType = (String)dataMap.get("signType");
			String sign = (String)dataMap.get("sign");
			
			StringBuilder sb = new StringBuilder();
	        sb.append("channelID=").append(channelID).append("&").append("currency=").append(currency).append("&").
	        append("extension=").append(extension).append("&").append("gameID=").append(gameID).append("&").
	        append("money=").append(money).append("&").append("orderID=").append(orderID).append("&")
	        .append("productID=").append(productID).append("&").append("serverID=").append(serverID).append("&")
	        .append("userID=").append(userID).append("&").append(appSecret);
			
	        if("md5".equalsIgnoreCase(signType) == false){
	        	PlatformSavingCenter.logger.error("[充值回调] [充值平台：v8] [失败:签名类型不一致] [v8 orderId:"+extension+"] [productID:"+productID+"] [充值金额:"+money+"] [支付结果:--] [订单ID:"+channelID+"] [u8id:"+userID+"] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
	        	res.getWriter().write("FAIL");
				return;
	        }
	        
	        String mySign = EncryptUtils.md5(sb.toString()).toLowerCase();
	        
	        if(mySign.equals(sign)){

				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(extension);
				if(orderForm != null)
				{
					PassportManager passportManager = PassportManager.getInstance();
					
					Passport passport = passportManager.getPassport(orderForm.getPassportId());
					String username = "";
					
					if(passport != null)
					{
						username = passport.getUserName();
					}
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：v8] [失败:此订单已经回调过] [v8 orderId:"+extension+"] [productID:"+productID+"] [充值金额:"+money+"] [支付结果:--] [订单ID:"+channelID+"] [u8id:"+userID+"] [username:"+username+"] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("SUCCESS");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2(str);
					orderForm.setMediumInfo(currency+"@"+money+"@"+userID);
					orderForm.setPayMoney(money);
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：v8] [失败:更新订单信息时报错] [v8 orderId:"+extension+"] [productID:"+productID+"] [充值金额:"+money+"] [支付结果:--] [订单ID:"+channelID+"] [u8id:"+userID+"] [username:"+username+"] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("FAIL");
						return;
					}
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：v8] [成功] [v8 orderId:"+extension+"] [productID:"+productID+"] [充值金额:"+money+"] [支付结果:--] [订单ID:"+channelID+"] [u8id:"+userID+"] [username:"+username+"] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("SUCCESS");
					return;
				}
				else
				{
		
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：v8] [失败:查询订单失败,无匹配订单] [orderId:"+extension+"] [productID:"+productID+"] [充值金额:"+money+"] [支付结果:--] [订单ID:"+channelID+"] [u8id:"+userID+"] [username:--] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("SUCCESS");
					return;
				}
			
	        }else{
	        	PlatformSavingCenter.logger.error("[充值回调] [充值平台：v8] [失败:签名不正确] [state:"+state+"] [mySign:"+mySign+"] ["+sb.toString()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
	        }
		}catch(Exception e){
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：v8] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("FAIL");
			} catch (IOException e1) {
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：v8] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
	}
	
}
