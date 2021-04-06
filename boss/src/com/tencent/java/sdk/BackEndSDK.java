package com.tencent.java.sdk;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;

import com.fy.boss.platform.qq.QQInfoService;



public class BackEndSDK {
	
	private static final int GET_METHOD_INDEX = 0;
	private static final int POST_METHOD_INDEX = 1;

	public static final int GET_SUCCEED_STATUS = 200; // 请求成功
	public static final int POST_SUCCEED_STATUS = 201; // 上传数据成功
	public static final int PROCESSING_SUCCEED_STATUS = 202; // 请求已经接受但是还没处理
	
	public static final String GET_METHOD = "get";
	public static final String POST_METHOD = "post";
	
	/**
	 * 发送协议同步(通用)
	 * 
	 * @param url
	 *            请求的接口url地址
	 * @param httpMethod
	 *            http方法，post或get
	 * @param params
	 *            http请求参数
	 * @param h
	 *            callback
	 * @return
	 */
	public static String httpSynSend(String appkey, String url, String action, String httpMethod,
			Map<String, String> params) {
		String aQueryParam = "";

		String uri = "";

		int method = GET_METHOD_INDEX;
		if (httpMethod.toLowerCase().equals(GET_METHOD)) {
			aQueryParam = QzoneOAuth.generateQZoneQueryString(appkey, action,
					httpMethod.toLowerCase(), params);
			uri = url + action + "?" + aQueryParam;

			method = GET_METHOD_INDEX;

		} else if (httpMethod.toLowerCase().equals(
				POST_METHOD)) {
			aQueryParam = QzoneOAuth.generateQZoneQueryString(appkey, action,
					httpMethod.toLowerCase(), params);

			uri = url + action;

			method = POST_METHOD_INDEX;
		}

		HttpClient httpClient = new DefaultHttpClient();

		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				10000);

		try {
			HttpResponse response = null;

			switch (method) {
				case GET_METHOD_INDEX : {
					HttpGet httpGet = new HttpGet(uri);
					response = httpClient.execute(httpGet);
					break;
				}
				case POST_METHOD_INDEX : {
					HttpPost httpPost = new HttpPost(uri);


					httpPost.setHeader("Content-Type",
								"application/x-www-form-urlencoded");
					httpPost.setEntity(new StringEntity(aQueryParam,
								"utf-8"));
					
					// 关闭Expect:100-Continue握手
					httpPost.getParams().setBooleanParameter(
							CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
					response = httpClient.execute(httpPost);

					break;
				}
			}
			if(QQInfoService.logger.isDebugEnabled())
				QQInfoService.logger.debug("[拼装参数发送] ["+uri+"]");
			
			if (response.getStatusLine().getStatusCode() != GET_SUCCEED_STATUS
					&& response.getStatusLine().getStatusCode() != POST_SUCCEED_STATUS) { // 认证出错
				return null;
			} else {
				return processEntity(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(QQInfoService.logger.isDebugEnabled())
				QQInfoService.logger.debug("[拼装参数发送] ["+uri+"]",e);
		}
		return null;
	}
	
	public static String getMagic(String openkey, String openid, String appid, String appkey, String platformid, String tt)
	{
		if(QQInfoService.logger.isDebugEnabled())
			QQInfoService.logger.debug("[魔钻发送请求参数] ["+openkey+"] ["+openid+"] ["+appid+"] ["+appkey+"] ["+platformid+"] ["+tt+"]");
		Map<String, String> params = new HashMap<String, String>();
		// 添加固定参数
		addFixedParam(openkey, openid, appid, appkey, platformid, tt, params);
		
		if(QQInfoService.logger.isDebugEnabled())
			QQInfoService.logger.debug("[魔钻发送请求参数] ["+params+"]");
		
		return httpSynSend(appkey, AppConfig.OpenPlatformURL, AppConfig.magic, GET_METHOD, params);
	}
	
	public static String getVip(String openkey, String openid, String appid, String appkey, String platformid, String tt)
	{
		Map<String, String> params = new HashMap<String, String>();
		// 添加固定参数
		addFixedParam(openkey, openid, appid, appkey, platformid, tt, params);
		
		return httpSynSend(appkey, AppConfig.OpenPlatformURL, AppConfig.vip, GET_METHOD, params);
	}
	
	public static String getUserInfo(String openkey, String openid, String appid, String appkey, String platformid, String tt)
	{
		Map<String, String> params = new HashMap<String, String>();
		// 添加固定参数
		addFixedParam(openkey, openid, appid, appkey, platformid, tt, params);
		
		return httpSynSend(appkey, AppConfig.OpenPlatformURL, AppConfig.userinfo, GET_METHOD, params);
	}
	
	public static String getFriends(String openkey, String openid, String appid, String appkey, String platformid, String tt)
	{
		Map<String, String> params = new HashMap<String, String>();
		// 添加固定参数
		addFixedParam(openkey, openid, appid, appkey, platformid, tt, params);
		
		return httpSynSend(appkey, AppConfig.OpenPlatformURL, AppConfig.friends, GET_METHOD, params);
	}
	
	public static String verifyOpenKey(String openkey, String openid, String appid, String appkey, String platformid, String tt)
	{
		Map<String, String> params = new HashMap<String, String>();
		// 添加固定参数
		addFixedParam(openkey, openid, appid, appkey, platformid, tt, params);
		
		return httpSynSend(appkey, AppConfig.OpenPlatformURL, AppConfig.verify, GET_METHOD, params);
	}
	
	public static String queryBalance(String openkey, String openid, String appid, String appkey, String platformid, String tt)
	{
		Map<String, String> params = new HashMap<String, String>();
		// 添加固定参数
		addFixedParam(openkey, openid, appid, appkey, platformid, tt, params);
		
		return httpSynSend(appkey, AppConfig.OpenPlatformURL, AppConfig.balance, GET_METHOD, params);
	}
	
	public static String charge(String openkey, String openid, String appid, String appkey, String platformid, String tt)
	{
		Map<String, String> params = new HashMap<String, String>();
		// 添加固定参数
		addFixedParam(openkey, openid, appid, appkey, platformid, tt, params);
		
		PaymentInfo chargeInfo = new PaymentInfo();
		
		chargeInfo.setCallbackUrl("##");
		chargeInfo.setFinishPageUrl("##");
		
		PaymentItem item = new PaymentItem();
		item.setItemId("item_001");
		item.setItemName("clothsA");
		item.setType("closths");           //物品的类型。应用自己定义，平台不关注。
		item.setSubType("closths");        //物品的子类型。应用自己定义，平台不关注。
		item.setQuantity(1);            //购买的数量。
		item.setUnitPrice(1);          //购买的物品价格
		
		//goodsId是货币id，用于游戏中有多种货币情况，如无直接赋值-1。该值由腾讯平台产品同学分配。
		int goodsId     = -1; 
		item.setGoodsId(goodsId);
		
		chargeInfo.put(item);
		
		params.put("payInfo", chargeInfo.toJsonString());
		
		return httpSynSend(appkey, AppConfig.OpenPlatformURL, AppConfig.charge, POST_METHOD, params);
	}

	private static void addFixedParam(String openkey, String openid, String appid, String appkey, String platformid, String tt, Map<String, String> params)
	{
		params.put("openid", openid);
		params.put("openkey", openkey);
		params.put("appid", appid);
		params.put("platformid", platformid);
		params.put("tt", tt);
		if(QQInfoService.logger.isDebugEnabled())
			QQInfoService.logger.debug("[添加参数] ["+params+"]");
	}

	/**
	 * 读取返回结果
	 * 
	 * @param entity
	 * @param statusCode
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private static String processEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				entity.getContent()));
		String line, result = "";
		StringBuilder sBuilder = new StringBuilder(result);
		while ((line = br.readLine()) != null) {
			sBuilder.append(line);
		}
		result = sBuilder.toString();

		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//int command = Integer.valueOf(args[0]);
		
		int command = 3;
		
		String sret = "";
		switch (command)
		{
		case 0:
		{
			sret = verifyOpenKey("BDC9FC25520E2FE7F913A687DAC93A21", "96D7EE4393E9C0977E97576E9BE60114", 
					AppConfig.appid, AppConfig.appkey, AppConfig.platformid, AppConfig.terminaltype);
		}
		break;
		case 1:
		{
			sret = queryBalance("BDC9FC25520E2FE7F913A687DAC93A21", "96D7EE4393E9C0977E97576E9BE60114", 
					AppConfig.appid, AppConfig.appkey, AppConfig.platformid, AppConfig.terminaltype);
		}
		break;
		case 2:
		{
			sret = charge("BDC9FC25520E2FE7F913A687DAC93A21", "96D7EE4393E9C0977E97576E9BE60114", 
					AppConfig.appid, AppConfig.appkey, AppConfig.platformid, AppConfig.terminaltype);
		}
		break;
		case 3://魔钻
		{
			sret = getMagic("BBC5BED2FAD20F40F4869EF0096AAAE3", "31FE491BAB57E06CD1078B843FA62694", 
					AppConfig.appid, AppConfig.appkey, AppConfig.platformid, AppConfig.terminaltype);
		}
		break;
		case 4://超Q
		{
			sret = getVip("BDC9FC25520E2FE7F913A687DAC93A21", "96D7EE4393E9C0977E97576E9BE60114", 
					AppConfig.appid, AppConfig.appkey, AppConfig.platformid, AppConfig.terminaltype);
		}
		break;
		case 5://个人信息
		{
			sret = getUserInfo("BDC9FC25520E2FE7F913A687DAC93A21", "96D7EE4393E9C0977E97576E9BE60114", 
					AppConfig.appid, AppConfig.appkey, AppConfig.platformid, AppConfig.terminaltype);
		}
		break;
		case 6://应用好友信息
		{
			sret = getFriends("BDC9FC25520E2FE7F913A687DAC93A21", "96D7EE4393E9C0977E97576E9BE60114", 
					AppConfig.appid, AppConfig.appkey, AppConfig.platformid, AppConfig.terminaltype);
		}
		break;
		}
		System.out.println(sret);
	}

}
