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
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class LeHaiHaiSavingCallBackServlet extends HttpServlet {

	protected void service(HttpServletRequest req, HttpServletResponse res)
	{
		long startTime = System.currentTimeMillis(); 
		String str =  "";
		try
		{
			req.setCharacterEncoding("utf-8");
			//str = FileUtils.getContent(req.getInputStream());
			str = ParamUtils.getParameter(req, "data");
			
			JacksonManager jm = JacksonManager.getInstance();
			Map dataMap = (Map)jm.jsonDecodeObject(str);
			
			String orderid = (String)dataMap.get("orderid");
			String extendsInfo = (String)dataMap.get("extendsInfo");
			String sign = (String)dataMap.get("sign");
			String gameid = (String)dataMap.get("gameid");
			String serverid = (String)dataMap.get("serverid");
			String uid = (String)dataMap.get("uid");
			String amount = (String)dataMap.get("amount");
			Integer time = (Integer)dataMap.get("time");
			
			extendsInfo = java.net.URLEncoder.encode(extendsInfo);
			
//			sign = md5(“amount=xxx&extendsInfo=xxx&gameid=xxx&orderid=xxx&serverid=xxx&time=xxx&uid=xxx”+key)
			StringBuilder sb = new StringBuilder();
	        sb.append("amount=").append(amount).append("&").append("extendsInfo=").append(extendsInfo).append("&").
	        append("gameid=").append(gameid).append("&").append("orderid=").append(orderid).append("&").
	        append("serverid=").append(serverid).append("&").append("time=").append(time).append("&")
	        .append("uid=").append(uid).append("83580300BEB790E69AEC915B330395BE");
			
	        String mysign = StringUtil.hash(sb.toString());
	        if(mysign.equals(sign)){

				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(extendsInfo);
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
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：乐嗨嗨] [失败:此订单已经回调过] [乐嗨嗨 orderId:"+orderid+"] [充值金额:"+amount+"] [支付结果:--] [订单ID:"+extendsInfo+"] [乐嗨嗨id:"+uid+"] [username:"+username+"] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("succ");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2(str);
					orderForm.setMediumInfo(orderid+"@"+amount+"@"+uid);
					orderForm.setPayMoney((long)(Float.parseFloat(amount) * 100));
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：乐嗨嗨] [失败:更新订单信息时报错] [乐嗨嗨 orderId:"+orderid+"] [充值金额:"+amount+"] [支付结果:--] [订单ID:"+extendsInfo+"] [乐嗨嗨id:"+uid+"] [username:"+username+"] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("fail");
						return;
					}
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：乐嗨嗨] [成功] [乐嗨嗨 orderId:"+orderid+"] [充值金额:"+amount+"] [支付结果:--] [订单ID:"+extendsInfo+"] [乐嗨嗨id:"+uid+"] [username:"+username+"] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("succ");
					return;
				}
				else
				{
		
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：乐嗨嗨] [失败:查询订单失败,无匹配订单] [乐嗨嗨 orderId:"+orderid+"] [充值金额:"+amount+"] [支付结果:--] [订单ID:"+extendsInfo+"] [乐嗨嗨id:"+uid+"] [username:--] [传入内容:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("succ");
					return;
				}
			
	        }else{
	        	PlatformSavingCenter.logger.error("[充值回调] [充值平台：乐嗨嗨] [失败:签名不正确] [sign:"+sign+"] [mysign:"+mysign+"] ["+sb.toString()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
	        }
		}catch(Exception e){
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：乐嗨嗨] [失败:出现异常] [str:"+str+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("fail");
			} catch (IOException e1) {
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：乐嗨嗨] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
	}
	
}
