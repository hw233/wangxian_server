package com.fy.boss.platform.u8.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.u8.sdk.PayParams;
import com.u8.sdk.U8SDK;
import com.u8.sdk.utils.RSAUtils;
import com.u8.sdk.utils.U8HttpUtils;
import com.u8.sdk.verify.UToken;

public class U8OrderUtils{

	private static final String ORDER_URL = "http://192.168.1.106:8080/pay/getOrderID";
	
	public static final String ORDER_PRIKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAO0m9rBaOFCEj4ncScPeC+6H63XMHhs4xb08lR2TbthAPKIZV3jZB0cuh91M3XJcpdhlHUGbLhbWlmG5xKgN1Lt8Z+QoebfNEyyKM06I9YeDSykwRyEjhhOUgLjeIVV3NI8T/awhl+tb/0yyld+5aoXJKxOx/pzqolzoDRs0omEzAgMBAAECgYBGzwt5PHb0E6CIGS4tPW9ymULEuV2D4z+ncR9U5WCDUSrJe6eSfbqellYazYiRTPh31DkYDa2FRC1CoKUHSJnrjeNR2TMw0WUBFvNcqYe2qOJZg3iOhyUDhIChhQiWWC9VrzAvqSU6tuyKGMy5rAWbfTneEnL7NHsTgRRDC+0JAQJBAPlRGW6T4TnRBtbOpRcMU+jdCyJAK3zwuRO13alhexDLq105D1osg2uP1d3+XvTQudwCGo1qRfBSp/W72fynz5kCQQDzgmLyxGzO1rugtJNMLQTqsRGg8ZUoUPmsEVGbmnHwRzd2OGHWbT1JuIEEb+ivrZV3PfeEObv7fDAT6qIhyiarAkAcd4ka2iG+U0KfpkqtXgf6r7qEt6T/iBDp0js0CuBdY5P2efxpxGlhD7RQu6ml9Gs0Vr0nZnoD3bw1z7QtKBAJAkBiqBjesqZCxs0NtxtWaYbsbwDta/M6elQtWnbtzA0NhEz8IKvC7E9AZvgejBiB1JoRzZFSiPGYWiBAcXduqTAxAkEAqG24ePhjesKoF1Us2ViqgJC7zDd96v+LI5eausw3TfKjO4jj5oMoQiyc+hZFxHYlkyZRfA6XEraF1Rdgngf65w==";
	public static final String ORDER_PUBKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDtJvawWjhQhI+J3EnD3gvuh+t1zB4bOMW9PJUdk27YQDyiGVd42QdHLofdTN1yXKXYZR1Bmy4W1pZhucSoDdS7fGfkKHm3zRMsijNOiPWHg0spMEchI4YTlIC43iFVdzSPE/2sIZfrW/9MspXfuWqFySsTsf6c6qJc6A0bNKJhMwIDAQAB";
	
	/***
	 * 访问U8Server验证sid的合法性，同时获取U8Server返回的token，userID,sdkUserID信息
	 * 这里仅仅是测试，正式环境下，请通过游戏服务器来获取订单号，不要放在客户端操作
	 * @param result
	 * @return
	 */
	public static U8Order getOrder(String url, PayParams data){
		
		try{
			
			UToken tokenInfo = U8SDK.getInstance().getUToken();
			if(tokenInfo == null){
//				Log.e("U8SDK", "The user now not logined. the token is null");
				return null;
			}
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("userID", ""+tokenInfo.getUserID());
			params.put("productID", data.getProductId());
			params.put("productName", data.getProductName());
			params.put("productDesc", data.getProductDesc());
			params.put("money", ""+data.getPrice() * 100);
			params.put("roleID", ""+data.getRoleId());
			params.put("roleName", data.getRoleName());
			params.put("serverID", data.getServerId());
			params.put("serverName", data.getServerName());
			params.put("extension", data.getExtension());
			
			String sign = generateSign(tokenInfo, data);
			params.put("sign", sign);
			
			String orderResult = U8HttpUtils.httpPost(url, params);
			
//			Log.d("U8SDK", "The order result is "+orderResult);
			
			return parseOrderResult(orderResult);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		
	}
	
    private static String generateSign(UToken token, PayParams data) throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder();
        sb.append("userID=").append(token.getUserID()).append("&")
        		.append("productID=").append(data.getProductId()).append("&")
                .append("productName=").append(data.getProductName()).append("&")
                .append("productDesc=").append(data.getProductDesc()).append("&")
                .append("money=").append(data.getPrice() * 100).append("&")
                .append("roleID=").append(data.getRoleId()).append("&")
                .append("roleName=").append(data.getRoleName()).append("&")
                .append("serverID=").append(data.getServerId()).append("&")
                .append("serverName=").append(data.getServerName()).append("&")
                .append("extension=").append(data.getExtension())
                .append(U8SDK.getInstance().getAppKey());

        String encoded = URLEncoder.encode(sb.toString(), "UTF-8");

//        Log.d("U8SDK", "The encoded getOrderID sign is "+encoded);

        String sign = RSAUtils.sign(encoded, ORDER_PRIKEY, "UTF-8");
        
//        Log.d("U8SDK", "The getOrderID sign is "+sign);
        
        return sign;

    }
	
	private static U8Order parseOrderResult(String orderResult){
		
		try {
			JSONObject jsonObj = new JSONObject(orderResult);
			int state = jsonObj.getInt("state");
			
			if(state != 1){
//				Log.d("U8SDK", "get order failed. the state is "+ state);
				return null;
			}
			
			JSONObject jsonData = jsonObj.getJSONObject("data");
			
			return new U8Order(jsonData.getString("orderID"), jsonData.getString("extension"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
