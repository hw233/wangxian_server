package com.fy.boss.platform.heepay;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class HeePayManager {

	private static HeePayManager self;
	
	public String key = "05A26B272D374DA98BD09193";//"4B05A95416DB4184ACEE4313";
	
	public String queryTokenIdUrl = "https://pay.heepay.com/Phone/SDK/PayInit.aspx";
	
	public String chargeBackUrl = "http://124.248.40.21:9110/mieshi_game_boss/heePayResult";
	
	public static HeePayManager getInstance(){
		if(self == null){
			self = new HeePayManager();
		}
		return self;
	}
	
	
	public String cardSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "微信支付";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			order.setSavingPlatform("汇付宝");
			order.setSavingMedium(savingMediumName);
			order.setPassportId(passport.getId());
			order.setPayMoney(new Long(addAmount));
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
			order.setHandleResultDesp("新生成订单");
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			order.setResponseDesp("新生成订单");
			order.setNotified(false);
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			String meta_option = "";
//			String functionName = "";
//			if(others.length > 1){
//				meta_option = others[1];
//			}else if(others.length > 2){
//				functionName = others[2];
//			}
//			
//			if(functionName.contains("购买月卡")){
//				order.setChargeType("购买月卡");
//				order.setChargeValue(meta_option);
//			}else if(functionName.contains("每日礼包")){
//				order.setChargeType("每日礼包");
//				order.setChargeValue(meta_option);
//			}
			
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			
			//先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			//设置订单号
			String orderid =  "heepay" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId();
			order.setOrderId(orderid);
			orderFormManager.updateOrderForm(order, "orderId");
		
			String returnContent = orderid;
			
			if (order.getId() > 0 && order.getOrderId() != null) {
				String returnValue = returnContent;
				int money = addAmount / 100;
				String tokenId = queryTokenId(orderid, money, passport.getLastLoginIp(), meta_option);
				if(tokenId == null || tokenId.isEmpty()){
					PlatformSavingCenter.logger.info("[汇付宝充值订单生成] [失败:获取tokenid失败] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [返回值:"+returnValue+"] [channel:"+channel+"] [others:"+(others!=null ?Arrays.toString(others):"")+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					return null;
				}
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[汇付宝充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [channel:"+channel+"] [角色id:"+others[0]+"] [tokenId:"+tokenId+"] [others:"+(others!=null ?Arrays.toString(others):"")+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return returnValue+"####"+tokenId;
			}else {
				PlatformSavingCenter.logger.error("[汇付宝充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [返回内容:"+returnContent+"] [channel:"+channel+"] [角色id:"+others[0]+"] [others:"+(others!=null ?Arrays.toString(others):"")+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		}catch(Exception e) {
			PlatformSavingCenter.logger.error("[汇付宝充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [others:"+(others!=null ?Arrays.toString(others):"")+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [fid:--] [充值平台:移动] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	public String queryTokenId(String orderId,int money,String ip,String option){
		long now = System.currentTimeMillis();
		String version = "1";
		String pay_type = "30";
		String agent_id = "2083444";//"1664502";
		String agent_bill_id = orderId;
		String pay_amt = money+"";
		String return_url = queryTokenIdUrl;
		String notify_url = chargeBackUrl;
		String user_ip = ip;
		String agent_bill_time = DateUtil.formatDate(new Date(now),"yyyyMMddHHmmss");
		String goods_name = "success";
//		try {
//			goods_name = new String("飘渺寻仙曲银两".getBytes("gb2312"),"gb2312");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
		String goods_num = "1";
		String remark = orderId;
		String goods_note = "heepay";
		String sign_type = "MD5";
		
		
		String keyStr = "version="+version+"&"+
						"agent_id="+agent_id+"&"+
						"agent_bill_id="+agent_bill_id+"&"+
						"agent_bill_time="+agent_bill_time+"&"+
						"pay_type="+pay_type+"&"+
						"pay_amt="+pay_amt+"&"+
						"notify_url="+notify_url+"&"+
						"user_ip="+user_ip+"&"+
						"key="+key;
		
		String sign = StringUtil.hash(keyStr).toLowerCase();
		
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("version",version);
		params.put("pay_type",pay_type);
		params.put("agent_id",agent_id);
		params.put("agent_bill_id",agent_bill_id);
		params.put("pay_amt",pay_amt);
		params.put("return_url",return_url);
		params.put("notify_url",notify_url);
		params.put("user_ip",user_ip);
		params.put("agent_bill_time",agent_bill_time);
		params.put("goods_name",goods_name);
		params.put("goods_num",goods_num);
		params.put("remark",remark);
		params.put("goods_note",goods_note);
		params.put("sign_type",sign_type);
		params.put("sign",sign);
		params.put("meta_option",option);
		
		HashMap headers = new HashMap();
		String content = "";
		String result = "";
		String encoding = "";
		Integer code = -1;
		String message= "";
		byte bytes[] = null;
		try {
			content = buildParams(params);
			bytes = HttpUtils.webPost(new java.net.URL(queryTokenIdUrl),content.getBytes(), headers, 5000, 10000);
			encoding = (String)headers.get(HttpUtils.ENCODING);
			code = (Integer)headers.get(HttpUtils.Response_Code);
			message = (String)headers.get(HttpUtils.Response_Message);
		} catch (Exception e) {
			e.printStackTrace();
			encoding = (String)headers.get(HttpUtils.ENCODING);
			code = (Integer)headers.get(HttpUtils.Response_Code);
			message = (String)headers.get(HttpUtils.Response_Message);
			PlatformSavingCenter.logger.warn("[请求汇付宝tokenId] [请求异常] ["+(bytes==null?"null":bytes.length)+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [result:"+result+"] [content:"+content+"] [sign:"+sign+"] ["+e+"]");
			return null;
		}
		try {
			result = new String(bytes,encoding);
			
			Document dom = XmlUtil.loadString(result);
			Element root = dom.getDocumentElement();
			String token_id = XmlUtil.getValueAsString(root,"token_id",null);
			String error = XmlUtil.getValueAsString(root,"error",null);
			
			if(PlatformSavingCenter.logger.isInfoEnabled()){
				PlatformSavingCenter.logger.warn("[请求汇付宝tokenId] [成功] [token_id:"+token_id+"] [orderId:"+orderId+"] [error:"+error+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [result:"+result+"]");
			}
			return token_id;
		} catch (Exception e) {
			e.printStackTrace();
			PlatformSavingCenter.logger.warn("[请求汇付宝tokenId] [异常] [content:"+content+"] [result:"+result+"]",e);
		}
		PlatformSavingCenter.logger.warn("[请求汇付宝tokenId] [错误] [content:"+content+"] [result:"+result+"]");
		return null;
	}
	
	public String buildParams(Map<String,String> params){
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
	
	
}
