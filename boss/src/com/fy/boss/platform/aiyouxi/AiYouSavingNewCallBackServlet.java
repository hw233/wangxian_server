package com.fy.boss.platform.aiyouxi;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class AiYouSavingNewCallBackServlet extends HttpServlet {
	
	public static String validateIp = "202.102.39.*";
	public static String appkey = "3ad5bd3989c643a1129dc2ff224ec30c";
	
	
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
			
			String souceIp = getIpAddr(req);
			if(souceIp != null)
			{
				String[] ipSegs = souceIp.split("[.]");
				String[] vIpSegs = validateIp.split("[.]");
				
				for(int i=0; (i<(ipSegs.length-1) && i<(vIpSegs.length-1));i++)
				{
					if(ipSegs[i] != null && vIpSegs[i] != null)
					{
						if(!(ipSegs[i].equals(vIpSegs[i])))
						{
							if(PlatformSavingCenter.logger.isDebugEnabled())
							{
								PlatformSavingCenter.logger.debug("[爱游戏SDK充值] [失败] [IP非法] ["+souceIp+"] ["+validateIp+"] ["+ipSegs[i]+"] ["+vIpSegs[i]+"] ["+req.getParameterMap()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
							}
							return;
						}
					}
					else
					{
						if(PlatformSavingCenter.logger.isDebugEnabled())
						{
							PlatformSavingCenter.logger.debug("[爱游戏SDK充值] [失败] [IP非法] ["+souceIp+"] ["+validateIp+"] ["+ipSegs[i]+"] ["+vIpSegs[i]+"] ["+req.getParameterMap()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
						}
						return;
					}
					
				}
				
			}
			else
			{
				if(PlatformSavingCenter.logger.isDebugEnabled())
				{
					PlatformSavingCenter.logger.debug("[爱游戏SDK充值] [失败] [IP非法] ["+souceIp+"] ["+validateIp+"] [--] [--] ["+req.getParameterMap()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
				}
				return;
			}
			String orderId = ParamUtils.getParameter(req, "cp_order_id", "");
			String channelorderid = ParamUtils.getParameter(req, "correlator", "");
			String code = ParamUtils.getParameter(req, "result_code", "");
			String rmb = ParamUtils.getParameter(req, "fee", "");
			String payType = ParamUtils.getParameter(req, "pay_type", "");
			String msg = ParamUtils.getParameter(req, "method", "");
			String sign = ParamUtils.getParameter(req, "sign", "");
			String requestTime = ParamUtils.getParameter(req, "version", "");
			String md5pre = (orderId+channelorderid+code+rmb+payType+msg+appkey);
			String my_sign = StringUtil.hash(md5pre);
			
			

			if(my_sign.equalsIgnoreCase(sign))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(orderId);
				if(orderForm != null)
				{
					Passport pp = PassportManager.getInstance().getPassport(orderForm.getPassportId());
					String username = "";
					if(pp != null)
					{
						username = pp.getUserName();
					}
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：爱游戏SDK] [失败:此订单已经回调过] [爱游戏SDK orderid:"+channelorderid+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+rmb+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [传入账号:"+username+"] [状态码:"+code+"] [信息描述:"+msg+"] [支付方式:"+payType+"] [sourceIp:"+souceIp+"] [validateIp:"+validateIp+"] [sign:"+sign+"] [mysign:"+my_sign+"] [requestTime:"+requestTime+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							writeResponse(res, buildResponseString("0", orderId));
							return;
						}
						String mess = "成功";
						//更新订单
						orderForm.setResponseTime(System.currentTimeMillis());
						if("00".equals(code))
						{
							orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						}
						else
						{
							orderForm.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
							orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
							mess = "失败";
						}
						//以渠道返回的充值额度为准
						orderForm.setPayMoney((long)(Double.valueOf(rmb)*100 ));	
						orderForm.setChannelOrderId(channelorderid);
						try {
							orderFormManager.update(orderForm);
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：爱游戏SDK] ["+mess+"] [爱游戏SDK orderid:"+channelorderid+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+rmb+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [传入账号:"+username+"] [状态码:"+code+"] [信息描述:"+msg+"] [支付方式:"+payType+"] [sourceIp:"+souceIp+"] [validateIp:"+validateIp+"] [sign:"+sign+"] [mysign:"+my_sign+"] [requestTime:"+requestTime+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：爱游戏SDK] ["+mess+"] [爱游戏SDK orderid:"+channelorderid+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+rmb+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [传入账号:"+username+"] [状态码:"+code+"] [信息描述:"+msg+"] [支付方式:"+payType+"] [sourceIp:"+souceIp+"] [validateIp:"+validateIp+"] [sign:"+sign+"] [mysign:"+my_sign+"] [requestTime:"+requestTime+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						}
						writeResponse(res, buildResponseString("0", orderId));
						return;
					}
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：爱游戏SDK] [失败:订单不存在] [爱游戏SDK orderid:"+channelorderid+"] [my order id:--] [my orderid:"+orderId+"] [实际支付金额:"+rmb+"] [数据库中记录金额:--] [传入账号:--] [状态码:"+code+"] [信息描述:"+msg+"] [支付方式:"+payType+"] [sourceIp:"+souceIp+"] [validateIp:"+validateIp+"] [sign:"+sign+"] [mysign:"+my_sign+"] [requestTime:"+requestTime+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					writeResponse(res, buildResponseString("0", orderId));
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：爱游戏SDK] [失败:验签失败] [爱游戏SDK orderid:"+channelorderid+"] [my order id:--] [my orderid:"+orderId+"] [实际支付金额:"+rmb+"] [数据库中记录金额:--] [传入账号:--] [状态码:"+code+"] [信息描述:"+msg+"] [支付方式:"+payType+"] [sourceIp:"+souceIp+"] [validateIp:"+validateIp+"] [sign:"+sign+"] [mysign:"+my_sign+"] [requestTime:"+requestTime+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				writeResponse(res, buildResponseString("0", orderId));
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：爱游戏SDK] [失败：出现未知异常]",e);
			writeResponse(res, buildResponseString("0", ""));
			return;

		}
		
	}
	
	public static String buildResponseString(String resCode,String cpOrderId)
	{
		/*
		<cp_notify_resp>

		  <h_ret>0 成功接收 其他 未成功接收</h_ret>

		  <cp_order_id>CP流水号</cp_order_id>

		</cp_notify_resp>
		*/
		
		StringBuffer sb = new StringBuffer("");
		String rootHead = "<cp_notify_resp>";
		String rootTail = "</cp_notify_resp>";
		String hretHead = "<h_ret>";
		String hretTail = "</h_ret>";
		String cp_order_idHead = "<cp_order_id>";
		String cp_order_idTail = "</cp_order_id>";
		sb.append(rootHead);
		sb.append(hretHead);
		sb.append(resCode);
		sb.append(hretTail);
		sb.append(cp_order_idHead);
		sb.append(cpOrderId);
		sb.append(cp_order_idTail);
		sb.append(rootTail);
		
		return sb.toString();
	}
	
	public static void writeResponse(HttpServletResponse res,String resContent)
	{
		try {
			res.getWriter().write(resContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：爱游戏SDK] [失败：发送响应时出现异常]",e);	
		}
	}
}
