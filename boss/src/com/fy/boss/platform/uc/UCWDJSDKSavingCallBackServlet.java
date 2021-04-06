package com.fy.boss.platform.uc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.uc.UcSavingCallBackServlet;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class UCWDJSDKSavingCallBackServlet extends HttpServlet {

	public static final Logger logger = Logger.getLogger(UcSavingCallBackServlet.class); 

	
	public static int CPID = 149;
	public static String ANDDROID_Secretkey_uc = "1b31d9c43c8d8d7ebf655b6ab96f5c09";
//	public static String ANDDROID_Secretkey_wdj = "e88a91af99ec8548f0b8544de3bf16ce";
	
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			
	{
		long startTime = System.currentTimeMillis(); 
		try
		{
			req.setCharacterEncoding("utf-8");
			String str = FileUtils.getContent(req.getInputStream());
			String oldvalue = str;
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
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [关闭流时出现异常] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
			}
			if(PlatformSavingCenter.logger.isDebugEnabled()){
				PlatformSavingCenter.logger.debug("[充值回调] [oldvalue:"+oldvalue+"] [str:"+str+"] [oldvalueSize:"+(oldvalue != null ? oldvalue.length() : "-1")+"]");
			}
			JacksonManager jm = JacksonManager.getInstance();
			Map dataMap = (Map)jm.jsonDecodeObject(str);
			
			Map<String,String> data = (Map)dataMap.get("data");
			String sign = (String)dataMap.get("sign");
			String version = (String)dataMap.get("ver");
			
			String orderId = data.get("orderId");
			String gameId = data.get("gameId");
			String accountId = data.get("accountId");
			String creator = data.get("creator");
			String payType = data.get("payWay");
			long payAmount = Math.round(Double.valueOf((String)data.get("amount")) * 100);
			String callbackInfo = data.get("callbackInfo");
			String orderStatus = data.get("orderStatus");
			String failedDesc = data.get("failedDesc");
			
			HashMap<String,String> params = new HashMap<String,String>();
			params.put("accountId", accountId);
			params.put("amount", payAmount+"");
			params.put("callbackInfo", callbackInfo);
			String cpOrderId = "";
			if(data.get("cpOrderId") != null){
				cpOrderId = data.get("cpOrderId");
				params.put("cpOrderId", callbackInfo);
			}
			params.put("creator", creator);
			if(failedDesc != null && failedDesc.trim().length() > 0){
				params.put("failedDesc", failedDesc);
			}
			params.put("gameId", gameId);
			params.put("orderId", orderId);
			params.put("orderStatus", orderStatus);
			params.put("payWay", payType);
			
			String my_sign = sign(data,ANDDROID_Secretkey_uc);
			
			if(my_sign.equalsIgnoreCase(sign)){
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(callbackInfo);
				if(orderForm != null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:此订单已经回调过] [uc orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [订单ID:"+callbackInfo+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("SUCCESS");
							return;
						}
					}
					
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2(str);
					orderForm.setSavingMedium("UCSDK充值");
					orderForm.setMediumInfo(payType+"@"+payAmount);
					orderForm.setPayMoney(payAmount);
					orderForm.setChannelOrderId(orderId);
					if(orderStatus.equals("S"))
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					else
					{
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						orderForm.setResponseDesp("状态:" + orderStatus + "@" + failedDesc );
					}
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:更新订单信息时报错] [uc orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [订单ID:"+callbackInfo+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("FAILURE");
						return;
					}
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：NEWUCSDK] [充值"+(orderStatus.equals("S")?"成功":"失败")+"] [uc orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [订单ID:"+callbackInfo+"]  [支付结果:"+orderStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("SUCCESS");
					return;
				}
				else
				{
		
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：NEWUCSDK] [失败:查询订单失败,无匹配订单] [uc orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [订单ID:"+callbackInfo+"] [data:"+data+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("SUCCESS");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:签名验证失败] [UC传入内容:"+str+"] [uc sign:"+sign+"] [my sign:"+my_sign+"] [uc orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [订单ID:"+callbackInfo+"] [data:"+data+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("FAILURE");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("FAILURE");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
		
	}
	
	private String sign(Map<String, String> reqMap, String signKey){
		    //将所有key按照字典顺序排序
		    TreeMap<String, String> signMap = new TreeMap<String, String>(reqMap);
		    StringBuilder stringBuilder = new StringBuilder(1024);
		    for (Map.Entry<String, String> entry : signMap.entrySet()) {
		        // roleName 、sign和signType不参与签名
		        if ("sign".equals(entry.getKey()) || "signType".equals(entry.getKey()) || "roleName".equals(entry.getKey())) {
		            continue;
		        }
		        //值为null的参数不参与签名
		        if (entry.getValue() != null) {
		                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
		        }
		    }
		    //拼接签名秘钥
		    stringBuilder.append(signKey);
		    //剔除参数中含有的'&'符号
		    String signSrc = stringBuilder.toString().replaceAll("&", "");
			String sign =  StringUtil.hash(signSrc).toLowerCase();
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [signStr:"+signSrc+"] [sign:"+sign+"]");
		    return StringUtil.hash(signSrc).toLowerCase();
	}
	
	private String uc_sign(Map<String,String> params,String secretkey){
	
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v);
		}
		String md5Str = sb.toString()+secretkey;
		String sign = StringUtil.hash(md5Str);
		PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [signStr:"+md5Str+"] [sign:"+sign+"]");
		
		return sign;
	}
}
