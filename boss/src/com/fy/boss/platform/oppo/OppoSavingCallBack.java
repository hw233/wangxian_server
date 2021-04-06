package com.fy.boss.platform.oppo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.wandoujia.Base64;
public class OppoSavingCallBack extends HttpServlet {

	public static final Logger logger = PlatformSavingCenter.logger; 
	public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmreYIkPwVovKR8rLHWlFVw7YDfm9uQOJKL89Smt6ypXGVdrAKKl0wNYc3/jecAoPi2ylChfa2iRu5gunJyNmpWZzlCNRIau55fxGW0XEu553IiprOZcaw5OuYGlf60ga8QT6qToP0/dpiL/ZbmNUO9kUhosIjEu22uFgR+5cYyQIDAQAB";
	
	private static String getKebiContentString(String url){
		final String[] strings = url.split("&");
		final Map<String, String> data = new HashMap<String, String>();
		for(String string : strings){
		final String[] keyAndValue = string.split("=");
		data.put(keyAndValue[0], keyAndValue[1]);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("notifyId=").append(data.get("notifyId"));
		sb.append("&partnerOrder=").append(data.get("partnerOrder"));
		sb.append("&productName=").append(data.get("productName"));
		sb.append("&productDesc=").append(data.get("productDesc"));
		sb.append("&price=").append(data.get("price"));
		sb.append("&count=").append(data.get("count"));
		sb.append("&attach=").append(data.get("attach"));
		return sb.toString();
	}
	
	public static boolean doCheck(String content, String sign, String publicKey) {
		try {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decode(publicKey);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		java.security.Signature signature =	java.security.Signature.getInstance("SHA1WithRSA");
		signature.initVerify(pubKey);
		signature.update(content.getBytes("utf-8"));
		boolean bverify = signature.verify(Base64.decode(sign));
		return bverify;
		} catch (Exception e) {
		logger.error("验证签名出错.",e);
		}
		return false;
	}
    
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res){
		
		long startTime = System.currentTimeMillis();
		
		try {
			req.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("notifyId=").append(req.getParameter("notifyId"));
		sb.append("&partnerOrder=").append(req.getParameter("partnerOrder"));
		sb.append("&productName=").append(req.getParameter("productName"));
		sb.append("&productDesc=").append(req.getParameter("productDesc"));
		sb.append("&price=").append(req.getParameter("price"));
		sb.append("&count=").append(req.getParameter("count"));
		sb.append("&attach=").append(req.getParameter("attach"));
		
		String bacicStr = sb.toString();
		String sign = req.getParameter("sign");
		String partnerOrder = req.getParameter("partnerOrder");
		String price = req.getParameter("price");
		String notifyId = req.getParameter("notifyId");
		try {
			if(doCheck(bacicStr, sign, publicKey)){
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(partnerOrder);
				long money = Math.round((Double.valueOf(price)));
				if(orderForm != null)
				{
					Passport p = PassportManager.getInstance().getPassport(orderForm.getPassportId());
					if(p == null)
					{
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：OPPO] [失败：找不到匹配的用户] [交易流水号:"+notifyId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						res.getWriter().write("result=FAIL&resultMsg=用户异常");
						return;
					}
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：OPPO] [失败：订单已经回调过了] [交易流水号:"+notifyId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("result=OK&resultMsg=成功");
							return;
						}
					}
					
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setResponseDesp("vivo回调充值接口成功");
					if(Long.valueOf(money) != orderForm.getPayMoney())
					{
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：OPPO] [警告：传回的充值金额和订单存储的金额不一致] [交易流水号:"+notifyId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					orderForm.setPayMoney(Long.valueOf(money));
					orderForm.setChannelOrderId(notifyId);
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：OPPO] [成功] [用户充值] [成功] [交易流水号:"+notifyId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					try {
						orderFormManager.update(orderForm);
						res.getWriter().write("result=OK&resultMsg=成功");
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：OPPO] [失败：更新订单出错] [交易流水号:"+notifyId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					}
					
					res.getWriter().write("result=FAIL&resultMsg=更新订单异常");
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：OPPO] [失败：无参数指定订单] [交易流水号:"+notifyId+"] [交易金额:"+money+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("result=FAIL&resultMsg=订单异常");
				}
			}else{
				res.getWriter().write("result=FAIL&resultMsg=验签失败");
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：OPPO] [验证签名] [bacicStr:"+bacicStr+"] [url:"+req.getQueryString()+"]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				res.getWriter().write("result=FAIL&resultMsg=处理异常");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：OPPO] [充值异常] [bacicStr:"+bacicStr+"] [url:"+req.getQueryString()+"]",e);
		}
	}
	
}
