package com.tencent.java.sdk;
/*
 * 支付请求信息类
 */

import java.util.ArrayList;
import java.util.List;
import org.json.*;
public class PaymentInfo {

	public static  String url_param_callbackUrl   = "callbackUrl";
	public static  String url_param_finishPageUrl = "finishPageUrl";
	public static  String url_param_message       = "message";
	public static  String url_paymentItems        = "paymentItems";
	
	private String callbackUrl   = new String();
	private String finishPageUrl = new String();
	private String message       = new String();
	
	List<PaymentItem> listPaymentItem = new ArrayList<PaymentItem>();
	/**
	 * 根据http请求的json格式参数生成订单
	 * @param strJson json格式订单字符串
	 */
	public void fromJsonString(String strJson)
	{
		if (strJson == null || strJson.isEmpty())
			return;
		
		listPaymentItem.clear();
		try{
			JSONObject jsonPayment = new JSONObject(strJson);
			setCallbackUrl(jsonPayment
					.getString(PaymentInfo.url_param_callbackUrl));
			setFinishPageUrl(jsonPayment
					.getString(PaymentInfo.url_param_finishPageUrl));
			setMessage(jsonPayment.getString(PaymentInfo.url_param_message));
			
		org.json.JSONArray arry = jsonPayment
					.getJSONArray(PaymentInfo.url_paymentItems);
			for (int i = 0; i < arry.length(); i++)
			{
				org.json.JSONObject o = arry.getJSONObject(i);
				if (o == null)
					continue;

				PaymentItem p = PaymentItem.fromJsonObj(o);
				if (p != null)
					listPaymentItem.add(p);
			}
		} catch (org.json.JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	String toJsonString()
	{
		org.json.JSONArray arry = new org.json.JSONArray();
		
		for (int i = 0; i < listPaymentItem.size(); i++)
		{			
			org.json.JSONObject o = listPaymentItem.get(i).toJsonObj();
			if (o != null)
				arry.put(o);			
		}
		
		try
		{
			org.json.JSONObject result = new org.json.JSONObject();
			result.put(url_paymentItems, arry);
			result.put("callbackUrl" , callbackUrl);
			result.put("finishPageUrl" , finishPageUrl);
			
			if (!this.message.isEmpty())
				result.put("message" , message);
			
			return result.toString();
		} catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return "";
		}		
	}
	
	public int getPaymentCount()
	{
		return listPaymentItem.size();
	}
	
	public boolean valid()
	{
		if (callbackUrl.length() == 0 || finishPageUrl.length() == 0)
			return false;
		
		 for (PaymentItem it : listPaymentItem)
		 {
			 if (!it.valid())
				 return false;
		 }
		 
		 return true;
	}
	
	public void put(PaymentItem item)
	{
		if (item != null)
			listPaymentItem.add(item);
	}
	
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getFinishPageUrl() {
		return finishPageUrl;
	}
	public void setFinishPageUrl(String finishPageUrl) {
		this.finishPageUrl = finishPageUrl;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}












