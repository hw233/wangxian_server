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
public class VivoSavingCallBack extends HttpServlet {

	public static final Logger logger = PlatformSavingCenter.logger; 

	private String appid = "dc470e100a28ae4b9de7428279f36029";
	private String appkey = "2c645a6dba5a97d79a816eabc330a0e3";
	private String cpid = "dfc4b3a950f56f96f08f";
	
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
		
		Map<String,String> para = new HashMap<String, String>();
		para.put("respCode", req.getParameter("respCode"));
		para.put("respMsg", req.getParameter("respMsg"));
		para.put("signMethod", req.getParameter("signMethod"));
		para.put("signature", req.getParameter("signature"));
		para.put("tradeType", req.getParameter("tradeType"));
		para.put("tradeStatus", req.getParameter("tradeStatus"));
		para.put("cpId", cpid);
		para.put("appId", appid);
		para.put("uid", req.getParameter("uid"));
		para.put("cpOrderNumber", req.getParameter("cpOrderNumber"));
		para.put("orderNumber", req.getParameter("orderNumber"));
		para.put("orderAmount", req.getParameter("orderAmount"));
		para.put("extInfo", req.getParameter("extInfo"));
		para.put("payTime", req.getParameter("payTime"));
		
		String orderId = req.getParameter("cpOrderNumber");
		String notifyId = req.getParameter("orderNumber");
		String respCode = req.getParameter("respCode");
		String tradeStatus = req.getParameter("tradeStatus");
		try {
			if(respCode.equals("200") && tradeStatus.equals("0000")){
				
			}else{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：VIVO] [失败：支付状态不对] [交易流水号:"+notifyId+"] [orderid:"+orderId+"] [respCode:"+respCode+"] [tradeStatus:"+tradeStatus+"] [para:"+para+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("success");
				return;
			}
			
			if(VivoSignUtils.verifySignature(para, appkey)){
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(orderId);
				long money = Math.round((Double.valueOf(req.getParameter("orderAmount"))));
				if(orderForm != null)
				{
					Passport p = PassportManager.getInstance().getPassport(orderForm.getPassportId());
					if(p == null)
					{
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：VIVO] [失败：找不到匹配的用户] [交易流水号:"+notifyId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						res.getWriter().write("fail");
						return;
					}
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：VIVO] [失败：订单已经回调过了] [交易流水号:"+notifyId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("success");
							return;
						}
					}
					
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setResponseDesp("oppo回调充值接口成功");
					if(Long.valueOf(money) != orderForm.getPayMoney())
					{
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：VIVO] [警告：传回的充值金额和订单存储的金额不一致] [交易流水号:"+notifyId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					orderForm.setPayMoney(Long.valueOf(money));
					orderForm.setChannelOrderId(notifyId);
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：VIVO] [成功] [用户充值] [成功] [交易流水号:"+notifyId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					try {
						orderFormManager.update(orderForm);
						res.getWriter().write("success");
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：VIVO] [失败：更新订单出错] [交易流水号:"+notifyId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					}
					
					res.getWriter().write("fail");
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：VIVO] [失败：无参数指定订单] [交易流水号:"+notifyId+"] [交易金额:"+money+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("fail");
				}
			}else{
				res.getWriter().write("fail");
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：VIVO] [验证签名错误] [para:"+para+"] [url:"+req.getQueryString()+"]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				res.getWriter().write("fail");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：VIVO] [充值异常] ["+para+"] [url:"+req.getQueryString()+"]",e);
		}
	}
	
}
