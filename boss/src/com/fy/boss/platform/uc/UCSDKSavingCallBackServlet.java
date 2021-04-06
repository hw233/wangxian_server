package com.fy.boss.platform.uc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.dao.impl.ExceptionOrderFormDAOImpl;
import com.fy.boss.finance.model.ExceptionOrderForm;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.uc.UcSavingCallBackServlet;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class UCSDKSavingCallBackServlet extends HttpServlet {

	public static final Logger logger = Logger.getLogger(UcSavingCallBackServlet.class); 
	public static Map<String,String> cardTypeMap = new HashMap<String, String>();
/*	public static int CPID = 149;
	public static int ANDROID_GAMEID = 63371;
	public static int ANDROID_ServerID = 907;
	public static int ANDDROID_CHANNELID = 2;
	public static int ANDDROID_APPID = 10024;
	//public static String ANDDROID_Secretkey = "2b6f740373c5893c8b798798fbdda077";
	public static String ANDDROID_Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";
	
	public static int IOS_GAMEID = 63586;
	public static int IOS_ServerID = 908 ;
	public static int IOS_CHANNELID = 2;
	public static int IOS_APPID = 10025;
	public static String IOS_Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";*/

	
	/**
	 * 测试用
	 */
	public static int CPID = 149;
	public static int ANDROID_GAMEID = 43201;
	public static int ANDROID_ServerID = 845;
	public static int ANDDROID_CHANNELID = 2;
	public static int ANDDROID_APPID = 10024;
	//public static String ANDDROID_Secretkey = "2b6f740373c5893c8b798798fbdda077";
	public static String ANDDROID_Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";
	
	
	/**
	 * ios(越狱)	测试	149/43202/846/2	1b31d9c43c8d8d7ebf655b6ab96f5c09	isDebug=1	sdk.test2.g.uc.cn
	 */
	public static int IOS_GAMEID = 43202;
	public static int IOS_ServerID = 846 ;
	public static int IOS_CHANNELID = 2;
	public static int IOS_APPID = 10025;
	public static String IOS_Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";
	
	
	/**
	 * 1 SZX 神州行 充值卡
2 UNICOM 联通卡 充值卡
3 TELECOM 电信卡 充值卡
4 JUNNET 骏网一卡通 充值卡
5 SNDACARD 盛大卡 充值卡
6 ZHENGTU 征途卡 充值卡
7 WANMEI 完美卡 充值卡
1021 CMCC 移动话费支付 移动话费支付
1022 UP U 点余额支付 U 点余额支付
	 */
	
	static {
		cardTypeMap.put("1","神州行");
		cardTypeMap.put("2","联通卡 充值卡");
		cardTypeMap.put("3","电信卡 充值卡");
		cardTypeMap.put("4","骏网一卡通 充值卡");
		cardTypeMap.put("5","盛大卡 充值卡");
		cardTypeMap.put("6","征途卡 充值卡");
		cardTypeMap.put("7","完美卡 充值卡" );
		cardTypeMap.put("1021","移动话费支付 移动话费支付");
		cardTypeMap.put("1022","U点余额支付 U点余额支付");
	}
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			
	{
		long startTime = System.currentTimeMillis(); 
		try
		{
			req.setCharacterEncoding("utf-8");
			String str = FileUtils.getContent(req.getInputStream());
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
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：UCSDK] [关闭流时出现异常] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
			}
			JacksonManager jm = JacksonManager.getInstance();
			Map dataMap = (Map)jm.jsonDecodeObject(str);
			HashMap<String,String> params = new HashMap<String,String>();
			params.putAll(dataMap);
			params.remove("sign");
			
			Map<String,String> data = (Map)dataMap.get("data");
			String sign = (String)dataMap.get("sign");
			
			String orderId = data.get("orderId");
			String gameId = data.get("gameId");
			String serverId = data.get("serverId");
			String uid = String.valueOf(data.get("ucid"));
			String payType = data.get("payWay");
			String cardType = cardTypeMap.get(payType);
			long payAmount = Math.round(Double.valueOf((String)data.get("amount")) * 100);
			String callbackInfo = data.get("callbackInfo");
			String orderStatus = data.get("orderStatus");
			String failedDesc = data.get("failedDesc");
			String mobileOs = "ios";
			
			if(gameId.equalsIgnoreCase(IOS_GAMEID+""))
			{
				mobileOs = "ios";
			}
			else
			{
				mobileOs = "android";
			}
			
			//去掉sign标记 得到所有参数
			//验证sign
			String my_sign = "";
			if(mobileOs.equalsIgnoreCase("ios"))
			{
				my_sign = uc_sign(data,IOS_Secretkey);
			}
			else
			{
				my_sign = uc_sign(data,ANDDROID_Secretkey);
			}
			if(my_sign.equalsIgnoreCase(sign))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
					//通过渠道orderId查订单
				if(StringUtils.isEmpty(orderId))
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：UCSDK] [失败:传入的订单ID为空] [uc orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [渠道:"+callbackInfo+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("FAILURE");
					return; 
				}
				
				
				
				OrderForm orderForm = orderFormManager.getByChannelOrderid(orderId,callbackInfo);
				if(orderForm != null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：UCSDK] [失败:此订单已经回调过] [uc orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [渠道:"+callbackInfo+"] [ucId:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("SUCCESS");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2(str);
					orderForm.setSavingMedium("UCSDK充值");
					orderForm.setMediumInfo(cardType+"@"+payAmount+"@"+uid);
					orderForm.setPayMoney(payAmount);
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
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：UCSDK] [失败:更新订单信息时报错] [uc orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [渠道:"+callbackInfo+"] [ucId:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("FAILURE");
						return;
					}
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：UCSDK] [充值"+(orderStatus.equals("S")?"成功":"失败")+"] [uc orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [渠道:"+callbackInfo+"] [ucId:"+uid+"] [支付结果:"+orderStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("SUCCESS");
					return;
				}
				else
				{
					//如果没有对应渠道订单号的订单 则插入到ExceptionOrderForm供后面同步状态使用
					ExceptionOrderForm eorderForm = new ExceptionOrderForm();
					eorderForm.setChannelOrderId(orderId);
					eorderForm.setChannel(callbackInfo);
					eorderForm.setUserChannel(callbackInfo);
					//设置金额
					eorderForm.setPayMoney(payAmount);
					//设置充值时间
					eorderForm.setResponseTime(System.currentTimeMillis());
					//设置充值结果
					if(orderStatus.equals("S"))
					{
						eorderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					}
					else
					{
						eorderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						eorderForm.setResponseDesp(failedDesc);
					}
					//设置充值媒介
					eorderForm.setSavingMedium(cardType);
					eorderForm.setMediumInfo(cardType+"@"+payAmount+"@"+uid);
					//设置充值平台
					eorderForm.setSavingPlatform("UCSDK");
					ExceptionOrderFormDAOImpl daoImpl = ExceptionOrderFormDAOImpl.getInstance();
					daoImpl.saveNew(eorderForm);
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：UCSDK] [失败:查询订单失败,无匹配订单] [将失配订单插入ExcepitonOrder中] [成功] [uc orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [渠道:"+callbackInfo+"] [ucId:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("SUCCESS");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：UCSDK] [失败:签名验证失败] [UC传入内容:"+str+"] [uc sign:"+sign+"] [my sign:"+my_sign+"] [uc orderId:"+orderId+"] [充值类型:"+cardTypeMap.get(payType)+"] [充值金额:"+payAmount+"] [支付结果:"+orderStatus+"] [渠道:"+callbackInfo+"] [ucId:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("FAILURE");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：UCSDK] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("FAILURE");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：UCSDK] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
		
	}
	
	private String uc_sign(Map<String,String> params,String secretkey){
	
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v);
		}
		String md5Str = CPID+sb.toString()+secretkey;
		
		String sign = StringUtil.hash(md5Str);
		
		return sign;
	}
}
