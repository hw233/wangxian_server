package com.fy.boss.platform.u8;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.u8.util.EncryptUtils;
import com.fy.boss.platform.u8.util.U8Order;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;

public class V8SDKSavingManager {
	public static V8SDKSavingManager self;

	public void initialize(){
		self=this;
	}
	
	private static final String app_key = "3db0c2e953da147d26969a10d0dc2bd6";
	private static final String AppSecret = "99db59f8920855fff2e47c4ce96576c1";
	
	public String sdkSaving5(Passport passport, String server,long addAmount, String userChannel,String[]others) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("小7充值");
		//设置充值介质
		order.setSavingMedium("小7充值");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(addAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("小7充值通知");
		order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(others[0]);
		order.setUserChannel(userChannel);
		if(others.length > 1){
			order.setChargeValue(others[1]);
		}
		if(others.length > 2){
			order.setChargeType(others[2]);
		}
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		order.setOrderId("xiaoqi" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		try {
			orderFormManager.update(order);
			PlatformSavingCenter.logger.info("[生成小7充值订单] [创建订单] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			PlatformSavingCenter.logger.warn("[生成小7充值订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return order.getOrderId();
	}
	
	public String sdkSaving4(Passport passport, String server,long addAmount, String userChannel,String[]others) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("麦游充值");
		//设置充值介质
		order.setSavingMedium("麦游充值");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(addAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("麦游充值通知");
		order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(others[0]);
		order.setUserChannel(userChannel);
		if(others.length > 1){
			order.setChargeValue(others[1]);
		}
		if(others.length > 2){
			order.setChargeType(others[2]);
		}
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		order.setOrderId("maiyou" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		try {
			orderFormManager.update(order);
			PlatformSavingCenter.logger.info("[生成麦游订单] [创建订单] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			PlatformSavingCenter.logger.warn("[生成麦游订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return order.getOrderId();
	}
	
	public String sdkSaving3(Passport passport, String server,long addAmount, String userChannel,String[]others) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("安久充值");
		//设置充值介质
		order.setSavingMedium("安久充值");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(addAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("安久充值通知");
		order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(others[0]);
		order.setUserChannel(userChannel);
		if(others.length > 1){
			order.setChargeValue(others[1]);
		}
		if(others.length > 2){
			order.setChargeType(others[2]);
		}
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		order.setOrderId("anjiu" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		try {
			orderFormManager.update(order);
			PlatformSavingCenter.logger.info("[生成安久订单] [创建订单] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			PlatformSavingCenter.logger.warn("[生成安久订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return order.getOrderId();
	}
	
	public String sdkSaving2(Passport passport, String server,long addAmount, String userChannel,String[]others) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("乐嗨嗨充值");
		//设置充值介质
		order.setSavingMedium("乐嗨嗨充值");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(addAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("乐嗨嗨充值通知");
		order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(others[0]);
		order.setUserChannel(userChannel);
		if(others.length > 1){
			order.setChargeValue(others[1]);
		}
		if(others.length > 2){
			order.setChargeType(others[2]);
		}
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		order.setOrderId("lehai" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		try {
			orderFormManager.update(order);
			PlatformSavingCenter.logger.info("[生成乐嗨嗨订单] [创建订单] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			PlatformSavingCenter.logger.warn("[生成乐嗨嗨订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return order.getOrderId();
	}
	
	public String sdkSaving(Passport passport, String server,long addAmount, String userChannel,String[]others) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("花生充值");
		//设置充值介质
		order.setSavingMedium("花生充值");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(addAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("花生充值通知");
		order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(others[0]);
		order.setUserChannel(userChannel);
		if(others.length > 1){
			order.setChargeValue(others[1]);
		}
		if(others.length > 2){
			order.setChargeType(others[2]);
		}
		String roleName = "";
		String productID = others[1];
		String productName = "";
		String productDese = "";
		if(others.length > 3){
			roleName = others[3];
		}
		if(others.length > 4){
			productName = others[4];
		}
		if(others.length > 5){
			productDese = others[5];
		}
		
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		
		order.setOrderId("v8" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		
		//获取u8id
		U8Order u8order = getU8Order(passport.getUserName(),order.getOrderId(),server,others[0],roleName,(int)order.getPayMoney(),productID,productName,productDese);
		if(u8order == null){
			return null;
		}
		
		order.setChannelOrderId(u8order.getOrder());
		
		try {
			orderFormManager.update(order);
			PlatformSavingCenter.logger.info("[生成v8订单] [创建订单] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [透传："+u8order.getExtension()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			PlatformSavingCenter.logger.warn("[生成v8订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [透传："+u8order.getExtension()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		String ext = u8order.getExtension();
		if(ext == null || ext.isEmpty()){
			ext = ".";
		}
		return order.getOrderId() + "####" + u8order.getOrder() + "####" + ext;
	}
	
	public U8Order getU8Order(String userName,String extension,String serverName,String roleID,String roleName,int money,String productID,String productName,String productDese){
		try {
			long startTime = System.currentTimeMillis(); 
			int userID = Integer.parseInt(userName.split("V8USER_")[1]);
			String serverID = "1";
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userID", ""+userID);
			params.put("productID", "1");
			params.put("productName", productDese);
			params.put("productDesc", productDese);
			params.put("money", ""+money);
			params.put("roleID", ""+roleID);
			params.put("roleName", roleName);
			params.put("serverID", serverID);
			params.put("serverName", serverName);
			params.put("extension", extension);
			
			
	        StringBuilder sb = new StringBuilder();
	        sb.append("userID=").append(userID).append("&")
	        		.append("productID=").append(1).append("&")
	                .append("productName=").append(productDese).append("&")
	                .append("productDesc=").append(productDese).append("&")
	                .append("money=").append(money).append("&")
	                .append("roleID=").append(roleID).append("&")
	                .append("roleName=").append(roleName).append("&")
	                .append("serverID=").append("1").append("&")
	                .append("serverName=").append(serverName).append("&")
	                .append("extension=").append(extension)
	                .append(app_key);

	      //  String encoded = URLEncoder.encode(sb.toString(), "UTF-8");
//	        String mysign = RSAUtils.sign(encoded, AppSecret, "UTF-8");
	        String mysign = EncryptUtils.md5(sb.toString()).toLowerCase();
//	        String mysign = StringUtil.hash(sb.toString()).toLowerCase();
	        
	        params.put("signType", "md5");
			params.put("sign", mysign);
			String queryStr = buildParams(params);
			
			String content = "";
			try {
				Map headers = new HashMap();
				byte bytes[] = HttpUtils.webPost(new java.net.URL("http://userver.huashy.cn:8080/pay/obtainOrder"), queryStr.getBytes("utf-8"), headers, 60000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
			} catch (Exception e) {
				PlatformSavingCenter.logger.info("[获取V8订单号] [失败] [username:"+userName+"] [queryStr:"+queryStr+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return null;
			}
	
			PlatformSavingCenter.logger.error("[获取V8订单号] [成功] [userName:"+userName+"] [角色:"+roleName+"] [id:"+roleID+"] [金额:"+money+"分] [productID:"+productID+"] [productName:"+productName+"] [productDese:"+productDese+"] [orderId:"+extension+"] [serverName:"+serverName+"] [sb:"+sb.toString()+"] [queryStr:"+queryStr+"] [content:"+content+"]");
			return parseOrderResult(content);
		} catch (Exception e) {
			e.printStackTrace();
			PlatformSavingCenter.logger.error("[获取V8订单号] [异常] [userName:"+userName+"] [角色:"+roleName+"] [id:"+roleID+"] [金额:"+money+"分] [productID:"+productID+"] [productName:"+productName+"] [productDese:"+productDese+"] [orderId:"+extension+"] [serverName:"+serverName+"]",e);
		}
		return null;
	}
	
	private static U8Order parseOrderResult(String orderResult) throws JSONException{
		JSONObject jsonObj = new JSONObject(orderResult);
		int state = jsonObj.getInt("state");
		if(state != 1){
			return null;
		}
		JSONObject jsonData = jsonObj.getJSONObject("data");
		return new U8Order(jsonData.getString("orderID"), jsonData.getString("extension"));
	}
	
	public static String buildParams(HashMap<String,String> params){
		String keys[] = params.keySet().toArray(new String[0]);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v);
		}

		
		sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v+"&");
		}
		
		return sb.substring(0,sb.toString().length()-1);
	}
	public static V8SDKSavingManager getInstance(){
		if(V8SDKSavingManager.self == null){
			V8SDKSavingManager.self = new V8SDKSavingManager();
		}
		return V8SDKSavingManager.self;
	}
}
