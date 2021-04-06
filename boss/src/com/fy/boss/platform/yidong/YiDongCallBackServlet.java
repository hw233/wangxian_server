package com.fy.boss.platform.yidong;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class YiDongCallBackServlet extends HttpServlet {
	
	
	public static Map<String,String> feeMap = new HashMap<String, String>();
	
	static
	{
		/*
		1	测试	000061313001	银子125000	250
		2	测试	000061313002	银子50000	100
		3	测试	000061313003	银子5000	10
		4	测试	000061313004	银子10000	20
		5	测试	000061313005	银子40000	80
		6	测试	000061313006	银子25000	50
		7	测试	000061313007	银子60000	120
		8	测试	000061313008	银子250000	500
		9	测试	000061313009	银子100000	200
		10	测试	000061313010	银子500000	1000*/
		
		/*feeMap.put("000061313001", "250");
		feeMap.put("000061313002", "100");
		feeMap.put("000061313003", "10");
		feeMap.put("000061313004", "20");
		feeMap.put("000061313005", "80");
		feeMap.put("000061313006", "50");
		feeMap.put("000061313007", "120");
		feeMap.put("000061313008", "500");
		feeMap.put("000061313009", "200");
		feeMap.put("000061313010", "1000");*/
		
		feeMap.put("000066785001", "100" );
		feeMap.put("000066785002", "200" );
		feeMap.put("000066785003", "300" );
		feeMap.put("000066785004", "500" );
		feeMap.put("000066785005", "800" );
		feeMap.put("000066785006", "1000");
		feeMap.put("000066785007", "1500");
		feeMap.put("000066785008", "2000");
		feeMap.put("000066785009", "2500");
		feeMap.put("000066785010", "3000");
//		/**
//		 * 006016033001银子50两100 审批通过 ..
//006016033002银子100两200 审批通过 ..
//006016033003银子150两300 审批通过 ..
//006016033004银子250两500 审批通过 ..
//006016033005银子400两800 审批通过 ..
//006016033006银子500两1000 审批通过 ..
//006016033007银子750两1500 审批通过 ..
//006016033008银子1锭2000 审批通过 ..
//006016033009银子1锭250两2500 审批通过 ..
//006016033010银子1锭500两3000 审批通过 ..
//
//		 */
		feeMap.put("006016033001", "100" );
		feeMap.put("006016033002", "200" );
		feeMap.put("006016033003", "300" );
		feeMap.put("006016033004", "500" );
		feeMap.put("006016033005", "800" );
		feeMap.put("006016033006", "1000");
		feeMap.put("006016033007", "1500");
		feeMap.put("006016033008", "2000");
		feeMap.put("006016033009", "2500");
		feeMap.put("006016033010", "3000");

		
	}
	
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		try
		{
			//限制ip
			if(!"112.4.3.36".equals(getIpAddr(req)))
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台:移动] [失败] [充值ip非法] [回调ip:"+getIpAddr(req)+"] [正确ip:112.4.3.36]");
				return;
			}
			
			
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
			StringBuffer returnMessage = new StringBuffer();
			returnMessage.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			returnMessage.append("<response>");
			
			String str = FileUtils.getContent(req.getInputStream());
			
			if(str == null)
			{
				str = "";
				BufferedInputStream bf = new BufferedInputStream(req.getInputStream());
				BufferedReader bfr = new BufferedReader(new InputStreamReader(bf));
				String line = null;
				while(( line = bfr.readLine() )!= null)
				{
					str += line;
				}
				
				try
				{
					bfr.close();
					bf.close();
				}
				catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：移动] [关闭流时出现异常] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
			}
			
			
			Document dom = null;
			try {
				dom = XmlUtil.loadString(str);
			} catch (Exception e) {
//				returnMessage.append("<transIDO>");
//				returnMessage.append(0);
//				returnMessage.append("</transIDO>");
				returnMessage.append("<hRet>");
				returnMessage.append(-1);
				returnMessage.append("</hRet>");
				returnMessage.append("<message>");
				returnMessage.append("parse content error");
				returnMessage.append("</message>");
				returnMessage.append("</response>");
				res.getWriter().write(returnMessage.toString());
				PlatformSavingCenter.logger.error("[移动充值接口回调] [失败] [解析响应内容信息] [失败] [待解析的字符串:"+str+"] [返回的字符串:"+returnMessage.toString()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return;
			}
			Element root = dom.getDocumentElement();

			/**
			 *  
userId	用户伪码	String	HTTP	游戏业务平台	userId	   
cpServiceId	业务代码	String	HTTP	游戏业务平台	计费业务代码	   
consumeCode	消费代码	String	HTTP	游戏业务平台	道具计费代码	   
cpParam	CP参数	String	HTTP	游戏业务平台	合作方透传参数	   
hRet	状态外码	String	HTTP	游戏业务平台	平台计费结果（状态码外码）0-成功 1-失败	   
status	状态内码	String	HTTP	游戏业务平台	状态信息（状态码内码），请参见附录C	   
versionId	版本号	String	HTTP	游戏业务平台	版本号100	   
transIDO	交易号	String	HTTP	游戏业务平台	事务流水号，长度17。	 

			 */
			
			String hRet = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "hRet"), "", null);
			String status = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "status"), "", null);
			String transIDO = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "transIDO"), "", null);
			String versionId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "versionId"), "", null);
			String userId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "userId"), "", null);
			String cpServiceId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "cpServiceId"), "", null);
			String consumeCode = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "consumeCode"), "", null);
			String cpParam = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "cpParam"), "", null);
			
			if(StringUtils.isEmpty(cpParam))
			{
				cpParam = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "cpparam"), "", null);
			}
			String payAmount = feeMap.get(consumeCode);
			

			
			PassportManager passportManager = PassportManager.getInstance();
			
			
			
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
			//如果cpOrderId没有传过来，则通过cardNo和充值类型查询订单
			OrderForm orderForm = orderFormManager.getOrderForm(cpParam);
			if(orderForm!= null)
			{
				
				 Passport passport  = passportManager.getPassport(orderForm.getPassportId());
				 
				 String username = "";
				 if(passport != null)
				 {
					 username = passport.getUserName();
				 }
				 
				 if(username == null)
				 {
					 username = "";
				 }
				 
				 
				synchronized(orderForm) {
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
//						returnMessage.append("<transIDO>");
//						returnMessage.append(transIDO);
//						returnMessage.append("</transIDO>");
						returnMessage.append("<hRet>");
						returnMessage.append(0);
						returnMessage.append("</hRet>");
						returnMessage.append("<message>");
						returnMessage.append("Successful");
						returnMessage.append("</message>");
						returnMessage.append("</response>");
						res.getWriter().write(returnMessage.toString());
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：移动] [失败:此订单已经回调过了] [移动 orderId:"+transIDO+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [transIDO:"+transIDO+"] [充值金额:"+payAmount+"] [充值结果:"+hRet+"] [充值内码:"+status+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [返回值:"+returnMessage+"] [移动返回:"+str+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						return;
					}
				}
				//更新订单
				try {
					orderForm.setResponseTime(System.currentTimeMillis());
					if(hRet.trim().equals("0")) {
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						//以返回的充值额度为准
						orderForm.setPayMoney(Long.valueOf(payAmount));
						orderForm.setChannelOrderId(transIDO);
						orderForm.setResponseTime(System.currentTimeMillis());
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：移动] [成功] [ok] [移动 orderId:"+transIDO+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [transIDO:"+transIDO+"] [充值金额:"+payAmount+"] [充值结果:"+hRet+"] [充值内码:"+status+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [返回值:"+returnMessage+"] [移动返回:"+str+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");	
					} else {
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						orderForm.setResponseDesp("移动充值平台返回充值失败");
						orderForm.setPayMoney(Long.valueOf(payAmount));
						orderForm.setChannelOrderId(transIDO);
						orderForm.setResponseTime(System.currentTimeMillis());
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：移动] [失败:返回充值失败] [移动 orderId:"+transIDO+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [transIDO:"+transIDO+"] [充值金额:"+payAmount+"] [充值结果:"+hRet+"] [充值内码:"+status+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [返回值:"+returnMessage+"] [移动返回:"+str+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					}
					orderFormManager.update(orderForm);
				} catch (Exception e) {
//					returnMessage.append("<transIDO>");
//					returnMessage.append(transIDO);
//					returnMessage.append("</transIDO>");
					returnMessage.append("<hRet>");
					returnMessage.append(0);
					returnMessage.append("</hRet>");
					returnMessage.append("<message>");
					returnMessage.append("Successful");
					returnMessage.append("</message>");
					returnMessage.append("</response>");
					res.getWriter().write(returnMessage.toString());
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：移动] [失败:更新订单出现异常] [移动 orderId:"+transIDO+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [transIDO:"+transIDO+"] [充值金额:"+payAmount+"] [充值结果:"+hRet+"] [充值内码:"+status+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [返回值:"+returnMessage+"] [移动返回:"+str+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
					return;
				}
//				returnMessage.append("<transIDO>");
//				returnMessage.append(transIDO);
//				returnMessage.append("</transIDO>");
				returnMessage.append("<hRet>");
				returnMessage.append(0);
				returnMessage.append("</hRet>");
				returnMessage.append("<message>");
				returnMessage.append("Successful");
				returnMessage.append("</message>");
				returnMessage.append("</response>");
				res.getWriter().write(returnMessage.toString());
				return;
			}
			else
			{
//				returnMessage.append("<transIDO>");
//				returnMessage.append(0);
//				returnMessage.append("</transIDO>");
				returnMessage.append("<hRet>");
				returnMessage.append(0);
				returnMessage.append("</hRet>");
				returnMessage.append("<message>");
				returnMessage.append("Successful");
				returnMessage.append("</message>");
				returnMessage.append("</response>");
				res.getWriter().write(returnMessage.toString());
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：移动] [失败:未找到匹配订单] [移动 orderId:"+transIDO+"] [my order id:--] [my orderid:--] [充值类型:--] [transIDO:"+transIDO+"] [充值金额:"+payAmount+"] [充值结果:"+hRet+"] [充值内码:"+status+"] [订单中充值金额:--] [返回值:"+returnMessage+"] [移动返回:"+str+"] [username:--] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return;
			}
		}
		catch(Exception e)
		{
			StringBuffer returnMessage = new StringBuffer();
			returnMessage.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			returnMessage.append("<response>");
			returnMessage.append("<transIDO>");
			returnMessage.append(0);
			returnMessage.append("</transIDO>");
			returnMessage.append("<hRet>");
			returnMessage.append(0);
			returnMessage.append("</hRet>");
			returnMessage.append("<message>");
			returnMessage.append("has Exception");
			returnMessage.append("</message>");
			returnMessage.append("</response>");
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：移动] [失败:出现未知异常] [移动 orderId:--] [my order id:--] [my orderid:--] [充值类型:--] [transIDO:--] [充值金额:--] [充值结果:--] [充值内码:--] [订单中充值金额:--] [返回值:"+returnMessage+"] [移动返回:--] [costs:--ms]",e);
			return;
		}
	}
	
}
