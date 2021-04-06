package com.fy.boss.platform.uc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.uc.g.sdk.cp.model.PayCallback;
import cn.uc.g.sdk.cp.protocol.CpRequestHelper;
import cn.uc.g.sdk.cp.util.BeanToMapUtil;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.StringUtil;

public class UCNewSDKSavingCallBackServlet extends HttpServlet {

	public static final Logger logger = PlatformSavingCenter.logger; 

	
	public static int CPID = 149;
	public static String ANDDROID_Secretkey_uc = "1b31d9c43c8d8d7ebf655b6ab96f5c09";
//	public static String ANDDROID_Secretkey_wdj = "e88a91af99ec8548f0b8544de3bf16ce";
	
	private static Set<String> promAmountSet=new HashSet<String>();
	static{
		promAmountSet.add("500");
		promAmountSet.add("1000");
		promAmountSet.add("2000");
		promAmountSet.add("3000");
		promAmountSet.add("5000");
		promAmountSet.add("10000");
		promAmountSet.add("20000");
		promAmountSet.add("30000");
		promAmountSet.add("50000");
		promAmountSet.add("100000");
		promAmountSet.add("150000");
		promAmountSet.add("200000");
		

	}
    
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			
	{
//		long startTime = System.currentTimeMillis(); 
//		try
//		{
//			req.setCharacterEncoding("utf-8");
//			String str = FileUtils.getContent(req.getInputStream());
//			String oldvalue = str;
//			if(str == null)
//			{
//				str = "";
//				BufferedInputStream bf = new BufferedInputStream(req.getInputStream());
//				BufferedReader bfr = new BufferedReader(new InputStreamReader(bf));
//				String line = null;
//				while(( line = bfr.readLine() )!= null)
//				{
//					str += line;
//				}
//				
//				try
//				{
//					bfr.close();
//					bf.close();
//				}
//				catch (Exception e) {
//					PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [关闭流时出现异常] [costs:"+(System.currentTimeMillis()-startTime)+"]");
//				}
//			}
//			if(PlatformSavingCenter.logger.isDebugEnabled()){
//				PlatformSavingCenter.logger.debug("[充值回调] [oldvalue:"+oldvalue+"] [str:"+str+"] [oldvalueSize:"+(oldvalue != null ? oldvalue.length() : "-1")+"]");
//			}
//			JacksonManager jm = JacksonManager.getInstance();
//			Map dataMap = (Map)jm.jsonDecodeObject(str);
//			
//			Map<String,String> data = (Map)dataMap.get("data");
//			String sign = (String)dataMap.get("sign");
//			String version = (String)dataMap.get("ver");
//			
//			String orderId = data.get("orderId");
//			String gameId = data.get("gameId");
//			String accountId = data.get("accountId");
//			String creator = data.get("creator");
//			String payType = data.get("payWay");
//			long payAmount = Math.round(Double.valueOf((String)data.get("amount")) * 100);
//			String callbackInfo = data.get("callbackInfo");
//			String orderStatus = data.get("orderStatus");
//			String failedDesc = data.get("failedDesc");
//			
//			HashMap<String,String> params = new HashMap<String,String>();
//			params.put("accountId", accountId);
//			params.put("amount", payAmount+"");
//			params.put("callbackInfo", callbackInfo);
//			String cpOrderId = "";
//			if(data.get("cpOrderId") != null){
//				cpOrderId = data.get("cpOrderId");
//				params.put("cpOrderId", callbackInfo);
//			}
//			params.put("creator", creator);
//			if(failedDesc != null && failedDesc.trim().length() > 0){
//				params.put("failedDesc", failedDesc);
//			}
//			params.put("gameId", gameId);
//			params.put("orderId", orderId);
//			params.put("orderStatus", orderStatus);
//			params.put("payWay", payType);
//			
//			String my_sign = sign(data,ANDDROID_Secretkey_uc);
//			
//			if(my_sign.equalsIgnoreCase(sign)){
//				OrderFormManager orderFormManager = OrderFormManager.getInstance();
//				OrderForm orderForm = orderFormManager.getOrderForm(callbackInfo);
//				if(orderForm != null)
//				{
//					synchronized(orderForm) {
//						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
//							PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:此订单已经回调过] [uc orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [订单ID:"+callbackInfo+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
//							res.getWriter().write("SUCCESS");
//							return;
//						}
//					}
//					
//					orderForm.setResponseTime(System.currentTimeMillis());
//					orderForm.setMemo2(str);
//					orderForm.setSavingMedium("UCSDK充值");
//					orderForm.setMediumInfo(payType+"@"+payAmount);
//					orderForm.setPayMoney(payAmount);
//					orderForm.setChannelOrderId(orderId);
//					if(orderStatus.equals("S"))
//						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
//					else
//					{
//						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
//						orderForm.setResponseDesp("状态:" + orderStatus + "@" + failedDesc );
//					}
//					try {
//						orderFormManager.update(orderForm);
//					} catch (Exception e) {
//						PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:更新订单信息时报错] [uc orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [订单ID:"+callbackInfo+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
//						res.getWriter().write("FAILURE");
//						return;
//					}
//					if(PlatformSavingCenter.logger.isInfoEnabled())
//						PlatformSavingCenter.logger.info("[充值回调] [充值平台：NEWUCSDK] [充值"+(orderStatus.equals("S")?"成功":"失败")+"] [uc orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [订单ID:"+callbackInfo+"]  [支付结果:"+orderStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
//					res.getWriter().write("SUCCESS");
//					return;
//				}
//				else
//				{
//		
//					if(PlatformSavingCenter.logger.isInfoEnabled())
//						PlatformSavingCenter.logger.info("[充值回调] [充值平台：NEWUCSDK] [失败:查询订单失败,无匹配订单] [uc orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [订单ID:"+callbackInfo+"] [data:"+data+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
//					res.getWriter().write("SUCCESS");
//					return;
//				}
//			}
//			else
//			{
//				PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:签名验证失败] [UC传入内容:"+str+"] [uc sign:"+sign+"] [my sign:"+my_sign+"] [uc orderId:"+orderId+"] [充值类型:"+payType+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [订单ID:"+callbackInfo+"] [data:"+data+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
//				res.getWriter().write("FAILURE");
//				return;
//			}
//		}
//		catch(Exception e)
//		{
//			PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
//			try {
//				res.getWriter().write("FAILURE");
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
//			}
//			return;
//		}
		
	long startTime = System.currentTimeMillis(); 	
	BufferedReader in = null;
	try {
		req.setCharacterEncoding("utf-8");
		in = new BufferedReader(new InputStreamReader(req.getInputStream(),"utf-8"));
		String ln;
		StringBuilder stringBuilder = new StringBuilder();
		while ((ln = in.readLine()) != null) {
		    stringBuilder.append(ln);
		    stringBuilder.append("\r\n");
		}
		
		//测试数据
		//stringBuilder.append("{\"sign\":\"57da4acf4fd3289debf6f54be5591c6b\",\"data\":{\"failedDesc\":\"\",\"amount\":0.10,\"callbackInfo\":\"\",\"ucid\":\"56920\",\"gameId\":119474,\"payWay\":1022,\"orderStatus\":\"S\",\"orderId\":\"20130507145444656713\",\"accountId\":\"123456789\"}}");
		
		logger.info("[接收到的参数]"+stringBuilder.toString());
		//参数解析为Map对象
		JacksonManager jm = JacksonManager.getInstance();
		Map resp = (Map)jm.jsonDecodeObject(stringBuilder.toString());
//		Map<String, Object> resp = (Map<String, Object>) JacksonUtil.decode(stringBuilder.toString(), new TypeReference<Map<String, Object>>() {});
		if(resp !=null){
		    String respSign = (String) resp.get("sign");
		    Map<String, Object> data = (Map<String, Object>) resp.get("data");
		    //apiKey=202cb962234w4ers2aaa
		    String sign = CpRequestHelper.createMD5Sign(data, "", "1b7bbe8bff2e825c03f2cc42301dec92");
			if(sign != null && respSign != null && respSign.equals(sign)){
			    PayCallback payModel = BeanToMapUtil.convertMap(PayCallback.class, data);
			    //输出获取到的参数
				logger.debug("[sign]"+respSign);
				logger.debug("[orderId]"+payModel.getOrderId());
				logger.debug("[gameId]"+payModel.getGameId());
				logger.debug("[accountId]"+payModel.getAccountId());
				logger.debug("[creator]"+payModel.getCreator());
				logger.debug("[payWay]"+payModel.getPayWay());
				logger.debug("[amount]"+payModel.getAmount());
				logger.debug("[callbackInfo]"+payModel.getCallbackInfo());
				logger.debug("[orderStatus]"+payModel.getOrderStatus());
				logger.debug("[failedDesc]"+payModel.getFailedDesc());
				logger.debug("[cpOrderId]"+payModel.getCpOrderId());
			
				//游戏需根据orderStatus参数的值判断是否给玩家过账虚拟货币。（S为充值成功、F为充值失败，避免假卡、无效卡充值成功）
				if("S".equals(payModel.getOrderStatus())){
					//定额充值的游戏需要校验amount值避免玩家窜改了客户端下单时的金额，影响游戏收支平衡
//					if(promAmountSet.contains(payModel.getAmount())){
						OrderFormManager orderFormManager = OrderFormManager.getInstance();
						OrderForm orderForm = orderFormManager.getOrderForm(payModel.getCallbackInfo());
						if(orderForm != null){
							synchronized(orderForm) {
								if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
									PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:此订单已经回调过] [uc orderId:"+payModel.getOrderId()+"] [充值类型:"+payModel.getPayWay()+"] " +
											"[充值金额:"+payModel.getAmount()+"] [支付结果:"+payModel.getOrderStatus()+"] [订单ID:"+payModel.getCallbackInfo()+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
									res.getWriter().write("SUCCESS");
									return;
								}
							}
							orderForm.setResponseTime(System.currentTimeMillis());
							orderForm.setMemo2(stringBuilder.toString());
							orderForm.setSavingMedium("UCSDK充值");
							orderForm.setMediumInfo(payModel.getPayWay()+"@"+payModel.getAmount());
							orderForm.setPayMoney((long)(Double.valueOf(payModel.getAmount())*100 ));
							orderForm.setChannelOrderId(payModel.getOrderId());
							if(payModel.getOrderStatus().equals("S")){
								orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
							}else{
								orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
								orderForm.setResponseDesp("状态:" + payModel.getOrderStatus() + "@" + payModel.getFailedDesc() );
							}
							try {
								orderFormManager.update(orderForm);
							} catch (Exception e) {
								PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEWUCSDK] [失败:更新订单信息时报错] [uc orderId:"+payModel.getOrderId()+"] [充值类型:"+payModel.getPayWay()+"] " +
										"[充值金额:"+payModel.getAmount()+"] [支付结果:"+payModel.getOrderStatus()+"] [订单ID:"+payModel.getCallbackInfo()+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
								res.getWriter().write("FAILURE");
								return;
							}
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：NEWUCSDK] [充值成功] [uc orderId:"+payModel.getOrderId()+"] [充值类型:"+payModel.getPayWay()+"] " +
									"[充值金额:"+payModel.getAmount()+"] [订单ID:"+payModel.getCallbackInfo()+"]  [支付结果:"+payModel.getOrderStatus()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("SUCCESS");
							return;
						}else{
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：NEWUCSDK] [失败:查询订单失败,无匹配订单] [uc orderId:"+payModel.getOrderId()+"] [充值类型:"+payModel.getPayWay()+"] " +
									"[充值金额:"+payModel.getAmount()+"] [支付结果:"+payModel.getOrderStatus()+"] [订单ID:"+payModel.getCallbackInfo()+"] [data:"+data+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("SUCCESS");
							return;
						}
//			        }else{
//			        	logger.warn("充值失败，金额不符");
//			        }                       
			    }else{
			    	logger.warn("充值失败，S为充值成功、F为充值失败，避免假卡、无效卡充值成功:"+payModel.getOrderStatus());
			    }            
			}else{
				logger.warn("充值失败，签名错误，[自己签名:"+sign+"] [uc签名:"+respSign+"]");
			}
			res.getWriter().println("FAILURE");//返回给sdk server的响应内容 ,对于重复多次通知失败的订单,请参考文档中通知机制。
		}else{
		    logger.debug("接口返回异常");
		}
	}catch(Exception e){
	    logger.error("接收支付回调通知的参数失败", e);
	}finally{
	    try{
	        if(null != in)in.close();
	        in = null;
	    }catch(Exception e1){
	    }
	}
	try {
		res.getWriter().write("FAILURE");
	} catch (IOException e) {
		e.printStackTrace();
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
