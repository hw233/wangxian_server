package com.fy.boss.platform.liantong;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class LianTongValidateOrderId extends HttpServlet {
	
	public static Map<String,String> payTypeMap = new HashMap<String, String>();
	
	static
	{
		payTypeMap.put("0", "沃支付");
		payTypeMap.put("1", "支付宝");
		payTypeMap.put("2", "VAC支付");
		payTypeMap.put("3", "神州付");
	}
	
	
	public final String appId = "9000664520130618192340829600";
	public final String key = "52c670999cdef4b09eb6";
	
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		try
		{
			
			
			long startTime = System.currentTimeMillis(); 
			String cpId = "86000305";

			
			String charsetName = req.getCharacterEncoding();
			if(StringUtils.isEmpty(charsetName))
			{
				charsetName = "utf-8";
			}
			
			req.setCharacterEncoding(charsetName);
			StringBuffer returnMessage = new StringBuffer();
			returnMessage.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			returnMessage.append("<checkOrderIdRsp>");
			
			
			
			String str = null;
		//	FileUtils.getContent(req.getInputStream());
			
			
			if(str == null)
			{

		        StringBuilder out = new StringBuilder();
		        InputStreamReader reader = new InputStreamReader(req.getInputStream(), charsetName);
		        char[] buffer = new char[4096];
		        int bytesRead = -1;
		        while ((bytesRead = reader.read(buffer)) != -1) {
		            out.append(buffer, 0, bytesRead);
		            
		        }
		        
		        
				try
				{
					str = out.toString();
					reader.close();
					
				}
				catch (Exception e) {
					PlatformSavingCenter.logger.error("[联通订单验证] [充值平台：联通] [关闭流时出现异常] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					returnMessage.append("1");
					returnMessage.append("</checkOrderIdRsp>");
					res.getOutputStream().print(returnMessage.toString());
					return;
				}
			}
			
			
			Document dom = null;
			try {
				dom = XmlUtil.loadString(str);
				
			} catch (Exception e) {
				returnMessage.append("1");
				returnMessage.append("</checkOrderIdRsp>");
				res.getOutputStream().print(returnMessage.toString());
				PlatformSavingCenter.logger.error("[联通充值接口回调] [失败] [解析响应内容信息] [失败] [待解析的字符串:"+str+"] [返回的字符串:"+returnMessage.toString()+"] [reqcontenttype:"+req.getContentType()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return;
			}
			Element root = dom.getDocumentElement();

			
			String orderid = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "orderid"), "", null);
			String signMsg = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "signMsg"), "", null);
				
			PassportManager passportManager = PassportManager.getInstance();
			
			
			String orginStr = "orderid="+orderid+"&Key="+key;
			
			String mysign = StringUtil.hash(orginStr);
			
			if(!signMsg.equalsIgnoreCase(mysign))
			{
				returnMessage.append("1");
				returnMessage.append("</checkOrderIdRsp>");
				res.getOutputStream().print(returnMessage.toString());
				
				PlatformSavingCenter.logger.error("[联通订单验证] [充值平台：联通] [签名不匹配] [我方签名:"+mysign+"] [传入签名:"+signMsg+"] [str:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return;
			}
			
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
			OrderForm orderForm = orderFormManager.getOrderForm(orderid);
			
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
				 
				 
				returnMessage.append("0");
				returnMessage.append("</checkOrderIdRsp>");
				res.getWriter().write(returnMessage.toString());
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[联通订单验证] [充值平台：联通] [成功] [orderId:"+orderid+"] [my order id:--] [my orderid:--] [充值类型:--] [充值金额:--] [充值结果:--] [充值内码:--] [订单中充值金额:--] [返回值:"+returnMessage+"] [联通返回:"+str+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return;
			}
			else
			{
				returnMessage.append("1");
				returnMessage.append("</checkOrderIdRsp>");
				res.getWriter().write(returnMessage.toString());
				PlatformSavingCenter.logger.error("[联通订单验证] [充值平台：联通] [失败:未找到匹配订单] [orderId:"+orderid+"] [my order id:--] [my orderid:--] [充值类型:--] [充值金额:--] [充值结果:--] [充值内码:--] [订单中充值金额:--] [返回值:"+returnMessage+"] [联通返回:"+str+"] [username:--] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return;
			}
		}
		catch(Exception e)
		{
			StringBuffer returnMessage = new StringBuffer();
			returnMessage.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			returnMessage.append("<checkOrderIdRsp>");
			returnMessage.append("1");
			returnMessage.append("</checkOrderIdRsp>");
			res.getOutputStream().print(returnMessage.toString());
			PlatformSavingCenter.logger.error("[联通订单验证] [充值平台：联通] [失败:出现未知异常] [联通 orderId:--] [my order id:--] [my orderid:--] [充值类型:--] [transIDO:--] [充值金额:--] [充值结果:--] [充值内码:--] [订单中充值金额:--] [返回值:"+returnMessage+"] [联通返回:--] [costs:--ms]",e);
			return;
		}
	}
	
}
