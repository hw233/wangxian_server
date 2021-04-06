package com.fy.boss.finance.service.platform;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.finance.service.platform.GameServerSzfSavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DESCryptor;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class GameServerSzfSavingManager {

	protected static GameServerSzfSavingManager m_self = null;
    			
	private String szfReturnUrl = "http://116.213.192.216:9110/mieshi_game_boss/szfresult";//服务器回调地址
	private String szxUrl = "http://pay3.shenzhoufu.com/interface/version3/serverconnszx/entry-noxml.aspx?charset=GBK";
											
	private String merId = "168392";//商户id
	private String desKey = "RpReYjjxkfg=";//des密钥
	private String privateKey = "rterueituritiTewuird2343sd";//接口密钥
	
	private static HashMap<String, String> szfResultDespMap = new HashMap<String, String>();
	
	static {
		szfResultDespMap.put("10", com.xuanzhi.language.translate.text_1547);
		szfResultDespMap.put("101", com.xuanzhi.language.translate.text_1548);
		szfResultDespMap.put("102", com.xuanzhi.language.translate.text_1549);
		szfResultDespMap.put("103", com.xuanzhi.language.translate.text_1550);
		szfResultDespMap.put("104", com.xuanzhi.language.translate.text_1551);
		szfResultDespMap.put("105", com.xuanzhi.language.translate.text_1552);
		szfResultDespMap.put("106", com.xuanzhi.language.translate.text_1553);
		szfResultDespMap.put("107", com.xuanzhi.language.translate.text_1554);
		szfResultDespMap.put("109", com.xuanzhi.language.translate.text_1555);
		szfResultDespMap.put("201", com.xuanzhi.language.translate.text_1556);
		szfResultDespMap.put("501", com.xuanzhi.language.translate.text_1557);
		szfResultDespMap.put("502", com.xuanzhi.language.translate.text_1557);
		szfResultDespMap.put("200", com.xuanzhi.language.translate.text_1498);
		szfResultDespMap.put("902", com.xuanzhi.language.translate.text_1558);
		szfResultDespMap.put("903", com.xuanzhi.language.translate.text_1559);
		szfResultDespMap.put("904", com.xuanzhi.language.translate.text_1560);
		szfResultDespMap.put("905", com.xuanzhi.language.translate.text_1561);
		szfResultDespMap.put("906", com.xuanzhi.language.translate.text_1562);
		szfResultDespMap.put("907", com.xuanzhi.language.translate.text_1563);
		szfResultDespMap.put("908", com.xuanzhi.language.translate.text_1564);
		szfResultDespMap.put("909", com.xuanzhi.language.translate.text_1565);
		szfResultDespMap.put("910", com.xuanzhi.language.translate.text_1566);
		szfResultDespMap.put("911", com.xuanzhi.language.translate.text_1567);
		szfResultDespMap.put("912", com.xuanzhi.language.translate.text_1568);
		szfResultDespMap.put("913", com.xuanzhi.language.translate.text_1569);
		szfResultDespMap.put("914", com.xuanzhi.language.translate.text_1570);
		szfResultDespMap.put("915", com.xuanzhi.language.translate.text_1571);
		szfResultDespMap.put("916", com.xuanzhi.language.translate.text_1572);
		szfResultDespMap.put("917", com.xuanzhi.language.translate.text_1573);
		szfResultDespMap.put("0", com.xuanzhi.language.translate.text_1574);
	}
	
	public static String getSzfResultDesp(String key) {
		return szfResultDespMap.get(key);
	}
            
    public static GameServerSzfSavingManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		m_self = this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");
	}

	/**
	 * 神州付充值接口
	 * @param passport 通行证
	 * @param game 冲在哪个游戏上
	 * @param cardtype 卡类型
	 * @param mianzhi 面值
	 * @param cardno 卡序列号
	 * @param cardpass 卡密
	 * @return
	 */
	public String cardSaving(Passport passport, String cardtype, int mianzhi, String cardno, String cardpass, String otherDesp,
			 String server, String channel, String orderID) {
    	long now = System.currentTimeMillis();
		
		try {
			cardno = cardno.replaceAll(" ", "").trim();
			cardpass = cardpass.replaceAll(" ", "").trim();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
    	OrderFormManager orderFormManager = OrderFormManager.getInstance();
    	boolean badsaving = PlatformSavingCenter.getInstance().isBadSavingPassport(passport.getId());
    	String cardinfo = mianzhi/100 + "@" + cardno + "@" + cardpass;
    	if(badsaving) {
    		PlatformSavingCenter.logger.error("[充值调用] [失败:账号频繁提交坏的订单] [支付平台:神州付] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [用户:"+passport.getUserName()+"] ["+(System.currentTimeMillis()-now)+"ms]");
    		return null;
    	}
    	//先生成一个订单
    	OrderForm order = new OrderForm();
    	order.setUserChannel(channel);
    	order.setServerName(server);
    	order.setCreateTime(now);
    	order.setPassportId(passport.getId());
		order.setPayMoney(mianzhi);
    	order.setMemo1(otherDesp);
    	order.setSavingPlatform("神州付");
    	order.setSavingMedium(cardtype);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
    	String medinfo = "面值:" + mianzhi + ",卡号:" + cardno + ",密码:" + cardpass;
    	order.setMediumInfo(medinfo);
    	orderFormManager.createOrderForm(order);
    	List<String> willUpdateFieldNames = new ArrayList<String>();
    	long id = order.getId();
    	String orderId = DateUtil.formatDate(new Date(),"yyyyMMdd") + "-" + merId + "-" + id;
    	order.setOrderId(orderId);
    	willUpdateFieldNames.add("orderId");
    	String version = "3";
    	int payMoney = 0;
    	String szfUrl = szxUrl;
    	String cardInfo = "";
    	String encCardInfo = "";
    	String returnUrl = szfReturnUrl;
    	String privateField = "";
    	int verifyType = 1;
    	String merUserName = "";
    	String merUserMail = "";
    	String encReturnUrl = "";
    	int cardTypeCombine = 0;
    	//cardtype 卡类型, 0-神州行卡,1-联通一卡付,2-电信卡
    	if(cardtype.equals("移动充值卡")) {
    		cardTypeCombine = 0;
    	} else if(cardtype.equals("联通一卡付")) {
    		cardTypeCombine = 1;
    	} else if(cardtype.equals("电信充值卡")) {
    		cardTypeCombine = 2;
    	}
    	try {
    		cardInfo = DESCryptor.encode(cardinfo,desKey);
    		encCardInfo = java.net.URLEncoder.encode(cardInfo, "UTF-8");
    		encReturnUrl = java.net.URLEncoder.encode(returnUrl, "UTF-8");
    		merUserName = java.net.URLEncoder.encode(merUserName, "UTF-8");
    		merUserMail = java.net.URLEncoder.encode(merUserMail, "UTF-8");
    		szfUrl += "&version="+version+"&merId=" + merId + "&payMoney=" + payMoney + "&orderId=" + orderId;
    		szfUrl += "&returnUrl=" + encReturnUrl + "&cardInfo=" + encCardInfo;
    		szfUrl += "&merUserName="+merUserName+"&merUserMail="+merUserMail+"&privateField=" + privateField;
    		szfUrl += "&verifyType="+verifyType+"&cardTypeCombine=" + cardTypeCombine;
		} catch (Exception e2) {
			e2.printStackTrace();
			PlatformSavingCenter.logger.error(e2);
		}
    	String md5pre = version +merId+ payMoney + orderId + returnUrl + cardInfo + privateField + verifyType+privateKey;
    	String md5String = StringUtil.hash(md5pre);
    	szfUrl += "&md5String=" + md5String;
    	szfUrl += "&signString=";
    	try {
        	URL url = new URL(szfUrl);
    		byte resbyte[] = HttpUtils.webGet(url, new HashMap(), 60000, 60000);
    		String result = new String(resbyte, "gbk").trim();
    		String desp = szfResultDespMap.get(result);
    		order.setHandleResultDesp(result + ":" + desp);
    		willUpdateFieldNames.add("handleResultDesp");
    		if(result.equals("200")) {
    			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
    			willUpdateFieldNames.add("handleResult");
    			order.setHandleResultDesp("调用成功");
    			willUpdateFieldNames.add("handleResultDesp");
    		} else {
    			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
    			willUpdateFieldNames.add("handleResult");
    			order.setHandleResultDesp("调用失败:" + desp);
    			willUpdateFieldNames.add("handleResultDesp");
    			//增加坏充值的个数
    			PlatformSavingCenter.getInstance().addBadSaving(passport.getId());
    		}
    		PlatformSavingCenter.logger.info("[充值调用] [成功] [支付平台:神州付] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:"+result+":"+desp+"] [url:"+szfUrl+"] [md5pre:"+md5pre+"] ["+(System.currentTimeMillis()-now)+"ms]");
    	} catch(Exception e) {
    		e.printStackTrace();
    		PlatformSavingCenter.logger.error("[充值调用] [异常] [支付平台:神州付] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [url:"+szfUrl+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
    		order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
    		willUpdateFieldNames.add("handleResult");
    		order.setHandleResultDesp("调用失败:" + e.getMessage());
    		willUpdateFieldNames.add("handleResultDesp");
    	}
    	try {
    		orderFormManager.batch_updateField(order, willUpdateFieldNames);
    	} catch(Exception ez) {
    		ez.printStackTrace();
    		PlatformSavingCenter.logger.error("[充值调用] [异常:更新订单异常] [支付平台:神州付] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [-----] ["+(System.currentTimeMillis()-now)+"ms]", ez);
    	}
    	return orderId;
    }
	
	
	public String cardSaving(Passport passport, String cardtype, int mianzhi, String cardno, String cardpass, String otherDesp,
			 String server, String channel, String orderID,String[]others) {
   	long now = System.currentTimeMillis();
		
		try {
			cardno = cardno.replaceAll(" ", "").trim();
			cardpass = cardpass.replaceAll(" ", "").trim();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
   	OrderFormManager orderFormManager = OrderFormManager.getInstance();
   	boolean badsaving = PlatformSavingCenter.getInstance().isBadSavingPassport(passport.getId());
   	String cardinfo = mianzhi/100 + "@" + cardno + "@" + cardpass;
   	if(badsaving) {
   		PlatformSavingCenter.logger.error("[充值调用] [失败:账号频繁提交坏的订单] [支付平台:神州付] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [用户:"+passport.getUserName()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-now)+"ms]");
   		return null;
   	}
   	//先生成一个订单
   	OrderForm order = new OrderForm();
   	order.setUserChannel(channel);
   	order.setServerName(server);
   	order.setCreateTime(now);
   	order.setPassportId(passport.getId());
		order.setPayMoney(mianzhi);
   	order.setMemo1(others[0]);
   	order.setSavingPlatform("神州付");
   	order.setSavingMedium(cardtype);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
   	String medinfo = "面值:" + mianzhi + ",卡号:" + cardno + ",密码:" + cardpass;
   	order.setMediumInfo(medinfo);
	if(others.length > 1){
		order.setChargeValue(others[1]);
	}
	if(others.length > 2){
		order.setChargeType(others[2]);
	}
   	orderFormManager.createOrderForm(order);
   	List<String> willUpdateFieldNames = new ArrayList<String>();
   	long id = order.getId();
   	String orderId = DateUtil.formatDate(new Date(),"yyyyMMdd") + "-" + merId + "-" + id;
   	order.setOrderId(orderId);
   	willUpdateFieldNames.add("orderId");
   	String version = "3";
   	int payMoney = 0;
   	String szfUrl = szxUrl;
   	String cardInfo = "";
   	String encCardInfo = "";
   	String returnUrl = szfReturnUrl;
   	String privateField = "";
   	int verifyType = 1;
   	String merUserName = "";
   	String merUserMail = "";
   	String encReturnUrl = "";
   	int cardTypeCombine = 0;
   	//cardtype 卡类型, 0-神州行卡,1-联通一卡付,2-电信卡
   	if(cardtype.equals("移动充值卡")) {
   		cardTypeCombine = 0;
   	} else if(cardtype.equals("联通一卡付")) {
   		cardTypeCombine = 1;
   	} else if(cardtype.equals("电信充值卡")) {
   		cardTypeCombine = 2;
   	}
   	try {
   		cardInfo = DESCryptor.encode(cardinfo,desKey);
   		encCardInfo = java.net.URLEncoder.encode(cardInfo, "UTF-8");
   		encReturnUrl = java.net.URLEncoder.encode(returnUrl, "UTF-8");
   		merUserName = java.net.URLEncoder.encode(merUserName, "UTF-8");
   		merUserMail = java.net.URLEncoder.encode(merUserMail, "UTF-8");
   		szfUrl += "&version="+version+"&merId=" + merId + "&payMoney=" + payMoney + "&orderId=" + orderId;
   		szfUrl += "&returnUrl=" + encReturnUrl + "&cardInfo=" + encCardInfo;
   		szfUrl += "&merUserName="+merUserName+"&merUserMail="+merUserMail+"&privateField=" + privateField;
   		szfUrl += "&verifyType="+verifyType+"&cardTypeCombine=" + cardTypeCombine;
		} catch (Exception e2) {
			e2.printStackTrace();
			PlatformSavingCenter.logger.error(e2);
		}
   	String md5pre = version +merId+ payMoney + orderId + returnUrl + cardInfo + privateField + verifyType+privateKey;
   	String md5String = StringUtil.hash(md5pre);
   	szfUrl += "&md5String=" + md5String;
   	szfUrl += "&signString=";
   	try {
       	URL url = new URL(szfUrl);
   		byte resbyte[] = HttpUtils.webGet(url, new HashMap(), 60000, 60000);
   		String result = new String(resbyte, "gbk").trim();
   		String desp = szfResultDespMap.get(result);
   		order.setHandleResultDesp(result + ":" + desp);
   		willUpdateFieldNames.add("handleResultDesp");
   		if(result.equals("200")) {
   			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
   			willUpdateFieldNames.add("handleResult");
   			order.setHandleResultDesp("调用成功");
   			willUpdateFieldNames.add("handleResultDesp");
   		} else {
   			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
   			willUpdateFieldNames.add("handleResult");
   			order.setHandleResultDesp("调用失败:" + desp);
   			willUpdateFieldNames.add("handleResultDesp");
   			//增加坏充值的个数
   			PlatformSavingCenter.getInstance().addBadSaving(passport.getId());
   		}
   		PlatformSavingCenter.logger.info("[充值调用] [成功] [支付平台:神州付] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:"+result+":"+desp+"] [url:"+szfUrl+"] [md5pre:"+md5pre+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-now)+"ms]");
   	} catch(Exception e) {
   		e.printStackTrace();
   		PlatformSavingCenter.logger.error("[充值调用] [异常] [支付平台:神州付] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [url:"+szfUrl+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
   		order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
   		willUpdateFieldNames.add("handleResult");
   		order.setHandleResultDesp("调用失败:" + e.getMessage());
   		willUpdateFieldNames.add("handleResultDesp");
   	}
   	try {
   		orderFormManager.batch_updateField(order, willUpdateFieldNames);
   	} catch(Exception ez) {
   		ez.printStackTrace();
   		PlatformSavingCenter.logger.error("[充值调用] [异常:更新订单异常] [支付平台:神州付] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [-----] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-now)+"ms]", ez);
   	}
   	return orderId;
   }
	
	public void setSzfReturnUrl(String szfReturnUrl) {
		this.szfReturnUrl = szfReturnUrl;
	}

	public void setSzxUrl(String szxUrl) {
		this.szxUrl = szxUrl;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getSzfReturnUrl() {
		return szfReturnUrl;
	}

	public String getSzxUrl() {
		return szxUrl;
	}

	public String getMerId() {
		return merId;
	}

	public String getDesKey() {
		return desKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}
}
