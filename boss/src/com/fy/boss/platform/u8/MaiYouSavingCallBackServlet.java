package com.fy.boss.platform.u8;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class MaiYouSavingCallBackServlet extends HttpServlet {

	protected void service(HttpServletRequest req, HttpServletResponse res)
	{
		long startTime = System.currentTimeMillis(); 
		try
		{
			req.setCharacterEncoding("utf-8");
			String str2 = FileUtils.getContent(req.getInputStream());
			String queryString = str2;
	    	String[] queryStringArr=queryString.split("&");
			String[] queryItemArr=new String[2];
//			String[] queryKeyArr= {"orderid","username","gameid","roleid","serverid","paytype","amount","paytime","attach","sign"};
			Map<String, String> map = new TreeMap<String, String>();
			String tempStr="";
			//Arrays.sort(queryKeyArr);
			for(String str : queryStringArr){
				queryItemArr=str.split("=");
//				if(Arrays.binarySearch(queryKeyArr, queryItemArr[0])>=0) {
					tempStr="";
					if(queryItemArr.length==2) {
						tempStr=queryItemArr[1];
					}
					map.put(queryItemArr[0],URLDecoder.decode(tempStr,"utf-8"));
//				}
			}
			String sign = map.get("sign");
			String sourceStr = str2.split("&sign=")[0] + "&appkey=f8607a402949c5780a04003e0d85dcf0";
			String orderid = map.get("orderid");
			String username = map.get("username");
			String amount = map.get("amount");
			String attach = map.get("attach");
			String roleid = map.get("roleid");
	        String mysign = StringUtil.hash(sourceStr);
	        if(mysign.equals(sign)){

				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(attach);
				if(orderForm != null)
				{
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：麦游] [失败:此订单已经回调过] [麦游 orderId:"+orderid+"] [充值金额:"+amount+"] [支付结果:--] [订单ID:"+attach+"] [麦游id:"+orderid+"] [username:"+username+"] [传入内容:"+str2+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("success");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2(str2);
					orderForm.setMediumInfo(orderid+"@"+amount+"@"+roleid);
					orderForm.setPayMoney((long)Double.parseDouble(amount) * 100);
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：麦游] [失败:更新订单信息时报错] [麦游 orderId:"+orderid+"] [充值金额:"+amount+"] [支付结果:--] [订单ID:"+attach+"] [麦游id:"+orderid+"] [username:"+username+"] [传入内容:"+str2+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("error");
						return;
					}
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：麦游] [成功] [麦游 orderId:"+orderid+"] [充值金额:"+amount+"] [支付结果:--] [订单ID:"+attach+"] [麦游id:"+orderid+"] [username:"+username+"] [传入内容:"+str2+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("success");
					return;
				}
				else
				{
		
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：麦游] [失败:查询订单失败,无匹配订单] [充值金额:"+amount+"] [支付结果:--] [订单ID:"+attach+"] [麦游id:"+orderid+"] [username:--] [传入内容:"+str2+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("error");
					return;
				}
			
	        }else{
	        	PlatformSavingCenter.logger.error("[充值回调] [充值平台：麦游] [失败:签名不正确] [mysign:"+mysign+"] [sign:"+sign+"] ["+sourceStr+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
	        	res.getWriter().write("errorSign");
	        }
		}catch(Exception e){
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：麦游] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("error");
			} catch (IOException e1) {
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：麦游] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
	}
	
}
