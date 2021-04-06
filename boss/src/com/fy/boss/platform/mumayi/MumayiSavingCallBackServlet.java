package com.fy.boss.platform.mumayi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.fy.boss.platform.mumayi.Base64;
import com.fy.boss.platform.mumayi.MD5Util;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class MumayiSavingCallBackServlet extends HttpServlet {
	
	public static String miyao = "81a2e83029d33495JkqMve99116IcR0Vjyqh0GpSqAWGEt29nsLyQmcDKxitM0c";

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		
      	int contentTotalBytes = req.getContentLength();
        byte contentBinArray[] = new byte[contentTotalBytes];
        int i=0;
        int j=0;
        
        for(;i < contentTotalBytes;i += j)
        {
            try
            {
                j = req.getInputStream().read(contentBinArray,i,contentTotalBytes - i);
            }
            catch(Exception exception)
            {
               exception.printStackTrace();
            }
        }
		
		String content = new String(contentBinArray,"utf-8");
		
		
		String uid = getValue(content, "uid");
		String orderid = getValue(content, "orderID");
		String productName = getValue(content, "productName");
		String productPrice = getValue(content, "productPrice");
		String myOrderId = getValue(content, "productDesc");
		String orderTime = getValue(content, "orderTime");
		String sign = getValue(content, "tradeSign");
		String result = getValue(content, "tradeState");
		
		//去掉sign标记 得到所有参数
		//验证sign
		if(verify(sign, miyao, orderid))
		{
			
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
			OrderForm orderForm = orderFormManager.getOrderForm(myOrderId);
			long money = Math.round((Double.valueOf(productPrice)) * 100);
			
				//通过orderId查订单
			if(orderForm != null)
			{
				Passport p = PassportManager.getInstance().getPassport(orderForm.getPassportId());
				
				if(p == null)
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：木蚂蚁] [失败：找不到匹配的用户] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [返回字符串:"+content+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("success");
					return;
				}
				
				synchronized(orderForm) {
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：木蚂蚁] [失败：订单已经回调过了] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [返回字符串:"+content+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						res.getWriter().write("success");
						return;
					}
				}
				
				
				orderForm.setResponseTime(System.currentTimeMillis());
				orderForm.setResponseDesp("木蚂蚁回调充值接口成功");
				if(Long.valueOf(money) != orderForm.getPayMoney())
				{
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：木蚂蚁] [警告：传回的充值金额和订单存储的金额不一致] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [返回字符串:"+content+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				if(result.trim().equals("success")) {
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					orderForm.setPayMoney(Long.valueOf(money));
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：木蚂蚁] [成功] [用户充值] [成功] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [返回字符串:"+content+"] [充值结果:"+result+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				else
				{
					orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
					orderForm.setResponseDesp("木蚂蚁交易失败");
					PlatformSavingCenter.logger.info("[充值回调] [充值平台：木蚂蚁] [成功] [用户充值] [失败] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [返回字符串:"+content+"] [充值结果:"+result+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
				try {
					orderFormManager.update(orderForm);
				} catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：木蚂蚁] [失败：更新订单出错] [交易流水号:"+orderid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [返回字符串:"+content+"] [用户名:"+p.getUserName()+"] [昵称:"+p.getNickName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					res.getWriter().write("success");
					return;
				}
				
				res.getWriter().write("success");
				return;
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：木蚂蚁] [失败：无参数指定订单] [交易流水号:"+orderid+"] [交易金额:"+money+"] [返回字符串:"+content+"] [uid:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("success");
				return;
			}
		}
		else
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：木蚂蚁] [失败：签名验证失败] [交易流水号:"+orderid+"] [秘钥:"+miyao+"] [sign:"+sign+"] [uid:"+uid+"] ["+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			res.getWriter().write("success");
			return;
		}
		
		
	}
	
	
	
	private String  getValue(String content,String key)
	{
		if(content == null)
		{
			content = "";
		}
		
		 int startpos = content.indexOf(key);
	   	 int endpos = content.indexOf('\r', startpos+key.length());
	     while(content.charAt(endpos) == '\r' || content.charAt(endpos) == '\n')
	     {
	    	 if(endpos >= content.length())
	    	 {
	    		 if(PlatformSavingCenter.logger.isDebugEnabled())
	    		 {
	    			 PlatformSavingCenter.logger.debug("[木蚂蚁充值回调] [准备查找value] [发现不规范内容] ["+endpos+"] ["+content.length()+"]");
	    		 }
	    		 
	    		 return "";
	    	 }
	    	 endpos++;
	     }
		
	     String value = "";
	     while(content.charAt(endpos) != '\r' && content.charAt(endpos) != '\n')
	     {
	    	 if(endpos >= content.length())
	    	 {
	    		 if(PlatformSavingCenter.logger.isDebugEnabled())
	    		 {
	    			 PlatformSavingCenter.logger.debug("[木蚂蚁充值回调] [正在拼装value] [发现不规范内容] ["+endpos+"] ["+content.length()+"] ["+value+"]");
	    		 }
	    		 
	    		 return "";
	    	 }
	    	 value+=content.charAt(endpos);
	    	 endpos++;
	     }
	     
	     return value;
	}
	
	public boolean verify(String sign , String appKey , String orderId) throws UnsupportedEncodingException{
		if(sign.length()<14){
			return false;
		}
		String verityStr = sign.substring(0,8);   
		sign = sign.substring(8);		
		String temp = MD5Util.toMD5(sign);			
		if(!verityStr.equals(temp.substring(0,8))){
			return false;
		}
		String keyB =  sign.substring(0,6);
			
		String randKey = keyB+appKey;
			
		randKey = MD5Util.toMD5(randKey);
			
		byte[] signB = Base64.decodeFast(sign.substring(6));
		int signLength = signB.length;
		String verfic="";
		for(int i =0 ; i< signLength ; i++){
			char b = (char)(signB[i]^randKey.getBytes()[i%32]);
			verfic +=String.valueOf(b);
		}
		return verfic.equals(orderId);	
	}
	
	
}
