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

public class YiDongMMCallBackServlet extends HttpServlet {
	
	
	public static Map<String,String> feeMap = new HashMap<String, String>();
	
	static
	{
		feeMap.put("30000825314501", "200" );
		feeMap.put("30000825314502", "1000");
		feeMap.put("30000825314503", "2000");
		feeMap.put("30000825314504", "3000");

		
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
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
			StringBuffer returnMessage = new StringBuffer();
			returnMessage.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			returnMessage.append("<SyncAppOrderResp>");
			
			String Appkey = "59085560F3C7F4F4";
			
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
				returnMessage.append("<MsgType>");
				returnMessage.append("SyncAppOrderResp");
				returnMessage.append("</MsgType>");
				returnMessage.append("<Version>");
				returnMessage.append("1.0.0");
				returnMessage.append("</Version>");
				returnMessage.append("<hRet>");
				returnMessage.append(0);
				returnMessage.append("</hRet>");
				returnMessage.append("</SyncAppOrderResp>");
				res.getWriter().write(returnMessage.toString());
				PlatformSavingCenter.logger.error("[移动充值接口回调] [失败] [解析响应内容信息] [失败] [待解析的字符串:"+str+"] [返回的字符串:"+returnMessage.toString()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return;
			}
			Element root = dom.getDocumentElement();

/**
 * 32 位大写 MD5(OrderID# 
ChannelID#PayCode#AppK
ey)			
 */
			String orderId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "OrderID"), "", null);
			String channelID = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "ChannelID"), "", null);
			String consumeCode = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "PayCode"), "", null);
			String cpParam = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "ExData"), "", null);
			
			String sign = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "MD5Sign"), "", null);
			String mysign = StringUtil.hash(orderId+"#"+channelID+"#"+consumeCode+"#"+Appkey);
			
			
			
			String payAmount = feeMap.get(consumeCode);
			int totalPrice = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(root, "TotalPrice"), 0);
			
			if(sign.equalsIgnoreCase(mysign))
			{
			
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
							returnMessage.append("<MsgType>");
							returnMessage.append("SyncAppOrderResp");
							returnMessage.append("</MsgType>");
							returnMessage.append("<Version>");
							returnMessage.append("1.0.0");
							returnMessage.append("</Version>");
							returnMessage.append("<hRet>");
							returnMessage.append(0);
							returnMessage.append("</hRet>");
							returnMessage.append("</SyncAppOrderResp>");
							res.getWriter().write(returnMessage.toString());
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：移动] [失败:此订单已经回调过了] [移动 orderId:"+orderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [transIDO:"+orderId+"] [充值金额:"+payAmount+"] [充值内码:"+consumeCode+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [返回值:"+returnMessage+"] [移动返回:"+str+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
							return;
						}
					}
					//更新订单
					try {
						orderForm.setResponseTime(System.currentTimeMillis());
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						//以返回的充值额度为准
						
						if(totalPrice < 100)
						{
							orderForm.setPayMoney(Long.valueOf(payAmount));
						}
						else
						{
							orderForm.setPayMoney(totalPrice);
						}
						orderForm.setChannelOrderId(orderId);
						orderForm.setResponseTime(System.currentTimeMillis());
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：移动] [成功] [ok] [移动 orderId:"+orderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [transIDO:"+orderId+"] [充值金额:"+payAmount+"] [充值内码:"+consumeCode+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [返回值:"+returnMessage+"] [移动返回:"+str+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");	
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						returnMessage.append("<MsgType>");
						returnMessage.append("SyncAppOrderResp");
						returnMessage.append("</MsgType>");
						returnMessage.append("<Version>");
						returnMessage.append("1.0.0");
						returnMessage.append("</Version>");
						returnMessage.append("<hRet>");
						returnMessage.append(0);
						returnMessage.append("</hRet>");
						returnMessage.append("</SyncAppOrderResp>");
						res.getWriter().write(returnMessage.toString());
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：移动] [失败:更新订单出现异常] [移动 orderId:"+orderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [transIDO:"+orderId+"] [充值金额:"+payAmount+"] [充值内码:"+consumeCode+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [返回值:"+returnMessage+"] [移动返回:"+str+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
						return;
					}
					returnMessage.append("<MsgType>");
					returnMessage.append("SyncAppOrderResp");
					returnMessage.append("</MsgType>");
					returnMessage.append("<Version>");
					returnMessage.append("1.0.0");
					returnMessage.append("</Version>");
					returnMessage.append("<hRet>");
					returnMessage.append(0);
					returnMessage.append("</hRet>");
					returnMessage.append("</SyncAppOrderResp>");
					res.getWriter().write(returnMessage.toString());
					return;
				}
				else
				{
					returnMessage.append("<MsgType>");
					returnMessage.append("SyncAppOrderResp");
					returnMessage.append("</MsgType>");
					returnMessage.append("<Version>");
					returnMessage.append("1.0.0");
					returnMessage.append("</Version>");
					returnMessage.append("<hRet>");
					returnMessage.append(0);
					returnMessage.append("</hRet>");
					returnMessage.append("</SyncAppOrderResp>");
					res.getWriter().write(returnMessage.toString());
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：移动] [失败:未找到匹配订单] [移动 orderId:"+orderId+"] [my order id:--] [my orderid:--] [充值类型:--] [transIDO:"+orderId+"] [充值金额:"+payAmount+"] [充值内码:"+consumeCode+"] [订单中充值金额:--] [返回值:"+returnMessage+"] [移动返回:"+str+"] [username:--] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					return;
				}
			}
			else
			{
				returnMessage.append("<MsgType>");
				returnMessage.append("SyncAppOrderResp");
				returnMessage.append("</MsgType>");
				returnMessage.append("<Version>");
				returnMessage.append("1.0.0");
				returnMessage.append("</Version>");
				returnMessage.append("<hRet>");
				returnMessage.append(0);
				returnMessage.append("</hRet>");
				returnMessage.append("</SyncAppOrderResp>");
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：移动] [失败:签名验证失败] [移动 orderId:"+orderId+"] [my order id:"+cpParam+"] [my orderid:--] [充值类型:--] [充值金额:"+payAmount+"] [充值结果:--] [充值内码:"+consumeCode+"] [移动返回:"+str+"]");
				return;
			}
		}
		catch(Exception e)
		{
			StringBuffer returnMessage = new StringBuffer();
			returnMessage.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			returnMessage.append("<SyncAppOrderResp>");
			returnMessage.append("<MsgType>");
			returnMessage.append("SyncAppOrderResp");
			returnMessage.append("</MsgType>");
			returnMessage.append("<Version>");
			returnMessage.append("1.0.0");
			returnMessage.append("</Version>");
			returnMessage.append("<hRet>");
			returnMessage.append(0);
			returnMessage.append("</hRet>");
			returnMessage.append("</SyncAppOrderResp>");
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：移动] [失败:出现未知异常] [移动 orderId:--] [my order id:--] [my orderid:--] [充值类型:--] [transIDO:--] [充值金额:--] [充值结果:--] [充值内码:--] [订单中充值金额:--] [返回值:"+returnMessage+"] [移动返回:--] [costs:--ms]",e);
			return;
		}
	}
}
