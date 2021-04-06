package com.fy.boss.platform.xmwan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class XMWanSavingManager {

	private static XMWanSavingManager self;
	
	private String client_id = "0ab7be5209eaaf7c25ed7a52ac78717e";
	private String query_wmwan_order_url = "http://open.xmwan.com/v2/purchases";
	private String notify_url = "http://119.254.154.201:12110/xunxian";
	private String client_secret = " 1844e8d82dcb1a102afc8d6c267d045a";
	
	public static XMWanSavingManager getInstance() {
	  	if(self == null)
    	{
    		self = new XMWanSavingManager();
    	}
		return self;
	}
	
	public void init(){
		self = this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");
	}
	
	/**
	 * returnValue:boss订单id+"###"+寻仙决订单id
	 */
	public String xmwanSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "寻仙充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			order.setSavingPlatform("寻仙充值");
			order.setSavingMedium(savingMediumName);
			order.setPassportId(passport.getId());
			order.setPayMoney( new Long(addAmount));
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
			order.setHandleResultDesp("新生成订单");
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			order.setResponseDesp("新生成订单");
			order.setNotified(false);
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			//先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			//设置订单号
			String orderid =  "xunxian" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId();
			order.setOrderId(orderid);
			orderFormManager.updateOrderForm(order, "orderId");
		
			String roleName = "";
			if(others.length > 3){
				roleName = others[3];
			}
			
			if (order.getId() > 0 && order.getOrderId() != null) {
				String serverId = "1";
				if(os != null && os.equals("Android")){
					serverId = "2";
				}
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[寻仙决充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [角色:"+roleName+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				String xmwanOrderId = queryXWWorder(others[0],roleName,serverId,servername,passport.getPassWd(),orderid, passport.getUserName(), addAmount, order.getCreateTime());
				if(xmwanOrderId == null || xmwanOrderId.isEmpty()){
					PlatformSavingCenter.logger.info("[寻仙决充值订单生成] [失败:获取寻仙决订单失败] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [channel:"+channel+"] [xmwanOrderId:"+xmwanOrderId+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					return null;
				}
				return orderid+"####"+xmwanOrderId;
			}else {
				PlatformSavingCenter.logger.error("[寻仙决充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		}catch(Exception e) {
			PlatformSavingCenter.logger.error("[寻仙决充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [fid:--] [充值平台:移动] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	/**
	 * 请求寻仙决创建消费订单
	 */
	public String queryXWWorder(String roleId,String roleName,String serverId,String serverName,String token,String app_order_id,String app_user_id,int amount,long createOrderTime){
		int timestamp = (int) (System.currentTimeMillis()/1000);//DateUtil.formatDate(new Date(createOrderTime),"yyyyMMddHHmmss");
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		JacksonManager jm = JacksonManager.getInstance();
		params.put("access_token",token);
		params.put("client_id",client_id);
		params.put("client_secret",client_secret);
		params.put("app_order_id",app_order_id);
		params.put("app_user_id",app_user_id);
		params.put("notify_url",notify_url);
		params.put("amount",String.valueOf(amount/100));
		params.put("timestamp",String.valueOf(timestamp));
		params.put("app_subject","寻仙充值");
		
		
		LinkedHashMap<String, String> roleMap = new LinkedHashMap<String, String>();
		roleMap.put("serverid",serverId);
		roleMap.put("servername",serverName);
		roleMap.put("accountid",roleId);
		roleMap.put("rolename",roleName);
		params.put("game_detail",jm.toJsonString(roleMap));
		
		
		
		LinkedHashMap<String, String> qianMingMap = new LinkedHashMap<String, String>();
		qianMingMap.put("amount",(int)(amount/100)+"");
		qianMingMap.put("app_order_id", app_order_id);
		qianMingMap.put("app_user_id",app_user_id);
		qianMingMap.put("notify_url",notify_url);
		qianMingMap.put("timestamp",String.valueOf(timestamp));
		
		String qingMingb = buildParams(qianMingMap);
		qingMingb += "&client_secret="+client_secret;
		
		String sign = StringUtil.hash(qingMingb).toLowerCase();
		params.put("sign",sign);
		
		HashMap headers = new HashMap();
		String content = "";
		String result = "";
		String encoding = "";
		Integer code = -1;
		String message= "";
		byte bytes[] = null;
		try {
			content = buildParams(params);
			bytes = HttpUtils.webPost(new java.net.URL(query_wmwan_order_url),content.getBytes(), headers, 5000, 10000);
			encoding = (String)headers.get(HttpUtils.ENCODING);
			code = (Integer)headers.get(HttpUtils.Response_Code);
			message = (String)headers.get(HttpUtils.Response_Message);
		} catch (Exception e) {
			e.printStackTrace();
			encoding = (String)headers.get(HttpUtils.ENCODING);
			code = (Integer)headers.get(HttpUtils.Response_Code);
			message = (String)headers.get(HttpUtils.Response_Message);
			PlatformSavingCenter.logger.warn("[请求寻仙决创建订单] [请求异常] [token:"+token+"] ["+(bytes==null?"null":bytes.length)+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [result:"+result+"] [content:"+content+"] [timestamp:"+timestamp+"] [sign:"+sign+"] ["+e+"]");
			return null;
		}
		try {
			result = new String(bytes,encoding);
			try {
				Map map = (Map)jm.jsonDecodeObject(result);
				String serial = (String)map.get("serial");
				Integer cost = (Integer)map.get("cost");
				if(PlatformSavingCenter.logger.isInfoEnabled()){
					PlatformSavingCenter.logger.warn("[请求寻仙决创建订单] [成功] [serial:"+serial+"] [timestamp:"+timestamp+"] [sign:"+sign+"] [cost:"+(cost==null?-1:cost)+"] [app_order_id:"+app_order_id+"] [amount:"+amount+"] [encoding:"+(encoding==null?"":encoding)+"] [code:"+(code==null?"":code)+"] [message:"+(message==null?"":message)+"] [content:"+(content==null?"":content)+"] [result:"+(result==null?"":result)+"]");
				}
				return serial;
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		PlatformSavingCenter.logger.warn("[请求寻仙决创建订单] [错误] [token:"+token+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [result:"+result+"] [content:"+content+"] [timestamp:"+timestamp+"] [sign:"+sign+"]");
		return null;
	}
	
	private String buildParams(Map<String,String> params){
		String[] key = params.keySet().toArray(new String[params.size()]);

		StringBuffer sb = new StringBuffer();
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(params.get(k));
			sb.append("&");
		}
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	private String getSign(int amount,String app_order_id ,String app_user_id ,int timestamp){
		String str = "amount="+amount+"&app_order_id="+app_order_id+"&app_user_id="+app_user_id+"&notify_url="+notify_url+"&timestamp="+timestamp;
		String sign = StringUtil.hash(str);
		return sign;
	}
	

	
}
