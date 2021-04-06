package com.fy.boss.platform.uc;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.finance.service.platform.NonBankcardPaymentResult;
import com.fy.boss.finance.service.platform.YeepayDigestUtil;
import com.fy.boss.platform.uc.UCYeepaySavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;

public class UCYeepaySavingManager {
	protected static UCYeepaySavingManager m_self = null;
    			
	private String yeepayReturnUrl = "http://116.213.192.216:9110/mieshi_game_boss/uc_yeepayresult";
	private String szxUrl = "https://www.yeepay.com/app-merchant-proxy/command.action";
	private String merId = "10011738156";
	private String privateKey = "1G34v976d403yol732fZE1wT0H1esWb15Sp734Pe454x5125IEot902S2l9u";
	
	public static HashMap<String, String> yeepayResultCodeDespMap = new HashMap<String, String>();
	
	static {
		yeepayResultCodeDespMap.put("1", com.xuanzhi.language.translate.text_1605);
		yeepayResultCodeDespMap.put("-1", com.xuanzhi.language.translate.text_1606);
		yeepayResultCodeDespMap.put("2", com.xuanzhi.language.translate.text_1607);
		yeepayResultCodeDespMap.put("5", com.xuanzhi.language.translate.text_1608);
		yeepayResultCodeDespMap.put("11", com.xuanzhi.language.translate.text_1549);
		yeepayResultCodeDespMap.put("66", com.xuanzhi.language.translate.text_1609);
		yeepayResultCodeDespMap.put("95", com.xuanzhi.language.translate.text_1610);
		yeepayResultCodeDespMap.put("112", com.xuanzhi.language.translate.text_1611);
		yeepayResultCodeDespMap.put("8001", com.xuanzhi.language.translate.text_1612);
		yeepayResultCodeDespMap.put("8002", com.xuanzhi.language.translate.text_1613);
	}
	
	public static HashMap<String, String> yeepayResultMsgDespMap = new HashMap<String, String>();
	
	static {
		yeepayResultMsgDespMap.put("23", com.xuanzhi.language.translate.text_1614);
		yeepayResultMsgDespMap.put("32", com.xuanzhi.language.translate.text_1614);
		yeepayResultMsgDespMap.put("1003", com.xuanzhi.language.translate.text_1614);
		yeepayResultMsgDespMap.put("7046", com.xuanzhi.language.translate.text_1614);
		yeepayResultMsgDespMap.put("7049", com.xuanzhi.language.translate.text_1614);
		yeepayResultMsgDespMap.put("7060", com.xuanzhi.language.translate.text_1614);
		yeepayResultMsgDespMap.put("7063", com.xuanzhi.language.translate.text_1614);
		yeepayResultMsgDespMap.put("7065", com.xuanzhi.language.translate.text_1614);
		yeepayResultMsgDespMap.put("1004", com.xuanzhi.language.translate.text_1615);
		yeepayResultMsgDespMap.put("1005", com.xuanzhi.language.translate.text_1615);
		yeepayResultMsgDespMap.put("7051", com.xuanzhi.language.translate.text_1615);
		yeepayResultMsgDespMap.put("8001", com.xuanzhi.language.translate.text_1615);
		yeepayResultMsgDespMap.put("25", com.xuanzhi.language.translate.text_1616);
		yeepayResultMsgDespMap.put("77", com.xuanzhi.language.translate.text_1616);
		yeepayResultMsgDespMap.put("1002", com.xuanzhi.language.translate.text_1616);
		yeepayResultMsgDespMap.put("1006", com.xuanzhi.language.translate.text_1616);
		yeepayResultMsgDespMap.put("7054", com.xuanzhi.language.translate.text_1616);
		yeepayResultMsgDespMap.put("7055", com.xuanzhi.language.translate.text_1616);
		yeepayResultMsgDespMap.put("7056", com.xuanzhi.language.translate.text_1616);
		yeepayResultMsgDespMap.put("7064", com.xuanzhi.language.translate.text_1616);
		yeepayResultMsgDespMap.put("8002", com.xuanzhi.language.translate.text_1616);
		yeepayResultMsgDespMap.put("12", com.xuanzhi.language.translate.text_1617);
		yeepayResultMsgDespMap.put("41", com.xuanzhi.language.translate.text_1617);
		yeepayResultMsgDespMap.put("53", com.xuanzhi.language.translate.text_1617);
		yeepayResultMsgDespMap.put("63", com.xuanzhi.language.translate.text_1617);
		yeepayResultMsgDespMap.put("76", com.xuanzhi.language.translate.text_1617);
		yeepayResultMsgDespMap.put("7034", com.xuanzhi.language.translate.text_1617);
		yeepayResultMsgDespMap.put("1002", com.xuanzhi.language.translate.text_1618);
		yeepayResultMsgDespMap.put("1007", com.xuanzhi.language.translate.text_1618);
		yeepayResultMsgDespMap.put("7000", com.xuanzhi.language.translate.text_1618);
		yeepayResultMsgDespMap.put("3001", com.xuanzhi.language.translate.text_1619);
		yeepayResultMsgDespMap.put("3002", com.xuanzhi.language.translate.text_1620);
		yeepayResultMsgDespMap.put("3003", com.xuanzhi.language.translate.text_1621);
		yeepayResultMsgDespMap.put("3004", com.xuanzhi.language.translate.text_1622);
		yeepayResultMsgDespMap.put("3005", com.xuanzhi.language.translate.text_1623);
		yeepayResultMsgDespMap.put("3006", com.xuanzhi.language.translate.text_1624);
		yeepayResultMsgDespMap.put("3007", com.xuanzhi.language.translate.text_1625);
		yeepayResultMsgDespMap.put("3008", com.xuanzhi.language.translate.text_1626);
	}
	
	public static HashMap<String, String> yeepayPayStatusDespMap = new HashMap<String, String>();
	
	static {
		yeepayPayStatusDespMap.put("0", com.xuanzhi.language.translate.text_1498);
		yeepayPayStatusDespMap.put("1", "销卡成功,订单失败");
		yeepayPayStatusDespMap.put("7", com.xuanzhi.language.translate.text_1627);
		yeepayPayStatusDespMap.put("1002", com.xuanzhi.language.translate.text_1628);
		yeepayPayStatusDespMap.put("1003", com.xuanzhi.language.translate.text_1629);
		yeepayPayStatusDespMap.put("1004", com.xuanzhi.language.translate.text_1630);
		yeepayPayStatusDespMap.put("1006", com.xuanzhi.language.translate.text_1631);
		yeepayPayStatusDespMap.put("1007", com.xuanzhi.language.translate.text_1618);
		yeepayPayStatusDespMap.put("1008", "余额卡过期（有效期1个月）");
		yeepayPayStatusDespMap.put("1010", com.xuanzhi.language.translate.text_1632);
		yeepayPayStatusDespMap.put("10000", com.xuanzhi.language.translate.text_1178);
		yeepayPayStatusDespMap.put("2005", com.xuanzhi.language.translate.text_1545);
		yeepayPayStatusDespMap.put("2006", com.xuanzhi.language.translate.text_1633);
		yeepayPayStatusDespMap.put("2007", com.xuanzhi.language.translate.text_1634);
		yeepayPayStatusDespMap.put("2008", com.xuanzhi.language.translate.text_1635);
		yeepayPayStatusDespMap.put("2009", com.xuanzhi.language.translate.text_1636);
		yeepayPayStatusDespMap.put("2010", com.xuanzhi.language.translate.text_1637);
		yeepayPayStatusDespMap.put("2011", com.xuanzhi.language.translate.text_1638);
		yeepayPayStatusDespMap.put("2012", com.xuanzhi.language.translate.text_1639);
		yeepayPayStatusDespMap.put("2013", "该卡已被锁定");
		yeepayPayStatusDespMap.put("2014", "系统繁忙，请稍后再试");
	}
            
	public static final int YEEPAY_CARD_SZX = 0;
	public static final int YEEPAY_CARD_LT = 1;
	public static final int YEEPAY_CARD_DX = 2;
	
	public static final String YEEPAY_MEDIUM_NAME[] = new String[]{"移动充值卡","联通一卡付","电信充值卡","盛大卡","征途卡","骏网一卡通","久游卡","网易卡","完美卡","搜狐卡"};
	public static final String YEEPAY_MEDIUM_FRPID[] = new String[]{"SZX","UNICOM","TELECOM","SNDACARD","ZHENGTU","JUNNET","JIUYOU","NETEASE","WANMEI","SOHU"};
	
	public void initialize() throws Exception{
		m_self = this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");
	}
	
	public static String getYeepayFrpId(String cardname) {
		for(int i=0; i<YEEPAY_MEDIUM_NAME.length; i++) {
			if(YEEPAY_MEDIUM_NAME[i].equals(cardname)) {
				return YEEPAY_MEDIUM_FRPID[i];
			}
		}
		return "";
	}

	/**
	 * 神州付充值接口
	 * @param passport 通行证
	 * @param game 冲在哪个游戏上
	 * @param cardtype 卡类型, 移动充值卡,联通一卡付,电信充值卡 , 盛大卡,征途卡,骏网一卡通,久游卡,网易卡,完美卡,搜狐卡
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
    	String cardinfo = mianzhi + "@" + cardno + "@" + cardpass;
    	if(badsaving) {
    		PlatformSavingCenter.logger.error("[充值调用] [失败:账号频繁提交坏的订单] [支付平台:易宝] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [用户:"+passport.getUserName()+"] ["+(System.currentTimeMillis()-now)+"ms]");
    		return null;
    	}
    	//先生成一个订单
    	java.util.Date cdate = new java.util.Date();
    	OrderForm order = new OrderForm();
    	order.setUserChannel(channel);
    	order.setServerName(server);
    	order.setCreateTime(now);
    	order.setPassportId(passport.getId());
    	order.setMemo1(otherDesp);
    	order.setSavingPlatform("UC易宝");
    	order.setSavingMedium(cardtype);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
    	String medinfo = com.xuanzhi.language.translate.text_1521 + mianzhi + com.xuanzhi.language.translate.text_1522 + cardno + com.xuanzhi.language.translate.text_1523 + cardpass;
    	order.setMediumInfo(medinfo);
    	
    	orderFormManager.createOrderForm(order);
    	long id = order.getId();
    	String orderId = DateUtil.formatDate(cdate,"yyyyMMdd") + "-" + merId + "-" + id;
    	order.setOrderId(orderId);
    	try {
			orderFormManager.update(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.warn("[UC充值调用] [生成订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+channel+"] [server:"+server+"] ["+(System.currentTimeMillis()-now)+"ms]");
		}
    	int payMoney = mianzhi;
    	String yeepayUrl = szxUrl;
    
    	boolean succ = false;
    	List<String> willUpdateFieldNames = new ArrayList<String>();
    	//调用扣费接口
		
		order.setPayMoney(new Long(payMoney));
		willUpdateFieldNames.add("payMoney");
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("p0_Cmd", "ChargeCardDirect");
		reqParams.put("p1_MerId", merId);
		reqParams.put("p2_Order", orderId);
		reqParams.put("p3_Amt", String.valueOf(mianzhi/100));
		reqParams.put("p4_verifyAmt", "true");
		reqParams.put("p5_Pid", "");
		reqParams.put("p6_Pcat", "");
		reqParams.put("p7_Pdesc", "");
		reqParams.put("p8_Url", yeepayReturnUrl);
		reqParams.put("pa_MP", "");
		reqParams.put("pa7_cardAmt", String.valueOf(mianzhi/100));
		reqParams.put("pa8_cardNo", cardno);
		reqParams.put("pa9_cardPwd", cardpass);
		reqParams.put("pd_FrpId", getYeepayFrpId(cardtype));
		reqParams.put("pr_NeedResponse", "1");
		reqParams.put("pz_userId", "");
		reqParams.put("pz1_userRegTime", "");
		
		String hmac = YeepayDigestUtil.getHmac(new String[] { reqParams.get("p0_Cmd"), 
																							reqParams.get("p1_MerId"),
																							reqParams.get("p2_Order"),
																							reqParams.get("p3_Amt"),
																							reqParams.get("p4_verifyAmt"),
																							reqParams.get("p5_Pid"),
																							reqParams.get("p6_Pcat"),
																							reqParams.get("p7_Pdesc"),
																							reqParams.get("p8_Url"),
																							reqParams.get("pa_MP"),
																							reqParams.get("pa7_cardAmt"),
																							reqParams.get("pa8_cardNo"),
																							reqParams.get("pa9_cardPwd"),
																							reqParams.get("pd_FrpId"),
																							reqParams.get("pr_NeedResponse"),
																							reqParams.get("pz_userId"),
																							reqParams.get("pz1_userRegTime")}, privateKey);
		reqParams.put("hmac", hmac);
		
		StringBuffer sb = new StringBuffer();
        Iterator it = reqParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            String key = (String) me.getKey();
            String value = (String) me.getValue();
            sb.append(key + "=" +  value + "&");
        }
        String params = sb.toString();
        if(params.length() > 0) {
        	params = params.substring(0, params.length()-1);
        }
        yeepayUrl = yeepayUrl + "?" + params;
		
    	try {
        	URL url = new URL(yeepayUrl);
    		byte resbyte[] = HttpUtils.webGet(url, new HashMap(), 60000, 60000);
    		String result = new String(resbyte, "gbk").trim();
    		String responseStr[] = result.split("\n");
    		// 创建非银行卡专业版消费请求结果
    		NonBankcardPaymentResult rs = new NonBankcardPaymentResult();
    		// 解析易宝支付返回的消费请求结果,关于返回结果数据详见《易宝支付非银行卡支付专业版接口文档 v3.0》
    		for (int t = 0; t < responseStr.length; t++) {
    			String currentResult = (String) responseStr[t];
    			if (currentResult == null || currentResult.equals("")) {
    				continue;
    			}
    			int i = currentResult.indexOf("=");
    			if (i >= 0) {
    				String sKey = currentResult.substring(0, i);
    				String sValue = currentResult.substring(i + 1);
    				if (sKey.equals("r0_Cmd")) {
    					rs.setR0_Cmd(sValue);
    				} else if (sKey.equals("r1_Code")) {
    					rs.setR1_Code(sValue);
    				} else if (sKey.equals("r6_Order")) {
    					rs.setR6_Order(sValue);
    				} else if (sKey.equals("rq_ReturnMsg")) {
    					rs.setRq_ReturnMsg(sValue);
    				} else if (sKey.equals("hmac")) {
    					rs.setHmac(sValue);
    				} else if (sKey.equals("errorMsg")) {
    					rs.setRq_ReturnMsg(rs.getRq_ReturnMsg() + "|error:" + sValue);
    				}
    				else {
    					PlatformSavingCenter.logger.error("[UC充值调用] [错误:调用返回的结果解析错误("+currentResult+")] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [url:"+yeepayUrl+"] \n" + result);
    				}
    			} else {
					PlatformSavingCenter.logger.error("[UC充值调用] [错误:调用返回的结果解析错误("+currentResult+")] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [url:"+yeepayUrl+"]\n" + result);
    			}
    		}
    		// 不成功则抛出异常
    		//r1code调用结果,1成功，其他失败
			int r1code = Integer.parseInt(rs.getR1_Code());
    		if (r1code != 1) {
				PlatformSavingCenter.logger.error("[UC充值调用] [错误:调用失败{errorCode:" + rs.getR1_Code() + ";errorMessage:"
    					+ rs.getRq_ReturnMsg()+com.xuanzhi.language.translate.text_1640+"易宝"+com.xuanzhi.language.translate.text_1641+cardtype+com.xuanzhi.language.translate.text_1642+cardinfo+com.xuanzhi.language.translate.text_1643+mianzhi+com.xuanzhi.language.translate.text_1644+order.getOrderId()+com.xuanzhi.language.translate.text_1645+passport.getUserName()+com.xuanzhi.language.translate.text_1646+"--"+com.xuanzhi.language.translate.text_1647+yeepayUrl+"]");
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				order.setHandleResultDesp(rs.getRq_ReturnMsg());
				willUpdateFieldNames.add("handleResult");
				willUpdateFieldNames.add("handleResultDesp");
    			//增加坏充值的个数
    			PlatformSavingCenter.getInstance().addBadSaving(passport.getId());
    		} else {
	    		String newHmac = "";
	    		newHmac = YeepayDigestUtil.getHmac(new String[] { rs.getR0_Cmd(),
	    				rs.getR1_Code(),rs.getR6_Order(),
	    				rs.getRq_ReturnMsg() }, privateKey);
	    		// hmac不一致则抛出异常
	    		if (!newHmac.equals(rs.getHmac())) {
					PlatformSavingCenter.logger.error("[UC充值调用] [错误:调用返回验证不合法] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [url:"+yeepayUrl+"]");
					order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);	
					willUpdateFieldNames.add("handleResult");
	    		} else {
	    			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
	    			willUpdateFieldNames.add("handleResult");
	    			PlatformSavingCenter.logger.info("[UC充值调用] [成功] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:"+rs.getR1_Code()+"/"+yeepayResultCodeDespMap.get(rs.getR1_Code())+"] [url:"+yeepayUrl+"]");
	    		}
    		}
    		order.setHandleResultDesp(r1code + ":" + yeepayResultCodeDespMap.get(String.valueOf(r1code)));
    		willUpdateFieldNames.add("handleResultDesp");
    	} catch(Exception e) {
    		e.printStackTrace();
    		PlatformSavingCenter.logger.error("[UC充值调用] [异常] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [url:"+yeepayUrl+"]", e);
    		order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
    		willUpdateFieldNames.add("handleResult");
    		order.setHandleResultDesp(com.xuanzhi.language.translate.text_1576 + e.getMessage());
    		willUpdateFieldNames.add("handleResultDesp");
    	}
    	try {
    		orderFormManager.batch_updateField(order, willUpdateFieldNames);
    	} catch(Exception e) {
    		e.printStackTrace();
    		PlatformSavingCenter.logger.error("[UC充值调用] [异常:更新订单异常] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [-----]", e);
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
   	String cardinfo = mianzhi + "@" + cardno + "@" + cardpass;
   	if(badsaving) {
   		PlatformSavingCenter.logger.error("[充值调用] [失败:账号频繁提交坏的订单] [支付平台:易宝] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [用户:"+passport.getUserName()+"] ["+(System.currentTimeMillis()-now)+"ms]");
   		return null;
   	}
   	//先生成一个订单
   	java.util.Date cdate = new java.util.Date();
   	OrderForm order = new OrderForm();
   	order.setUserChannel(channel);
   	order.setServerName(server);
   	order.setCreateTime(now);
   	order.setPassportId(passport.getId());
   	order.setMemo1(others[0]);
   	order.setSavingPlatform("UC易宝");
   	order.setSavingMedium(cardtype);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
   	String medinfo = com.xuanzhi.language.translate.text_1521 + mianzhi + com.xuanzhi.language.translate.text_1522 + cardno + com.xuanzhi.language.translate.text_1523 + cardpass;
   	order.setMediumInfo(medinfo);
	if(others.length > 1){
		order.setChargeValue(others[1]);
	}
	if(others.length > 2){
		order.setChargeType(others[2]);
	}
   	orderFormManager.createOrderForm(order);
   	long id = order.getId();
   	String orderId = DateUtil.formatDate(cdate,"yyyyMMdd") + "-" + merId + "-" + id;
   	order.setOrderId(orderId);
   	try {
			orderFormManager.update(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.warn("[UC充值调用] [生成订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+channel+"] [server:"+server+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] ["+(System.currentTimeMillis()-now)+"ms]");
		}
   	int payMoney = mianzhi;
   	String yeepayUrl = szxUrl;
   
   	boolean succ = false;
   	List<String> willUpdateFieldNames = new ArrayList<String>();
   	//调用扣费接口
		
		order.setPayMoney(new Long(payMoney));
		willUpdateFieldNames.add("payMoney");
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("p0_Cmd", "ChargeCardDirect");
		reqParams.put("p1_MerId", merId);
		reqParams.put("p2_Order", orderId);
		reqParams.put("p3_Amt", String.valueOf(mianzhi/100));
		reqParams.put("p4_verifyAmt", "true");
		reqParams.put("p5_Pid", "");
		reqParams.put("p6_Pcat", "");
		reqParams.put("p7_Pdesc", "");
		reqParams.put("p8_Url", yeepayReturnUrl);
		reqParams.put("pa_MP", "");
		reqParams.put("pa7_cardAmt", String.valueOf(mianzhi/100));
		reqParams.put("pa8_cardNo", cardno);
		reqParams.put("pa9_cardPwd", cardpass);
		reqParams.put("pd_FrpId", getYeepayFrpId(cardtype));
		reqParams.put("pr_NeedResponse", "1");
		reqParams.put("pz_userId", "");
		reqParams.put("pz1_userRegTime", "");
		
		String hmac = YeepayDigestUtil.getHmac(new String[] { reqParams.get("p0_Cmd"), 
																							reqParams.get("p1_MerId"),
																							reqParams.get("p2_Order"),
																							reqParams.get("p3_Amt"),
																							reqParams.get("p4_verifyAmt"),
																							reqParams.get("p5_Pid"),
																							reqParams.get("p6_Pcat"),
																							reqParams.get("p7_Pdesc"),
																							reqParams.get("p8_Url"),
																							reqParams.get("pa_MP"),
																							reqParams.get("pa7_cardAmt"),
																							reqParams.get("pa8_cardNo"),
																							reqParams.get("pa9_cardPwd"),
																							reqParams.get("pd_FrpId"),
																							reqParams.get("pr_NeedResponse"),
																							reqParams.get("pz_userId"),
																							reqParams.get("pz1_userRegTime")}, privateKey);
		reqParams.put("hmac", hmac);
		
		StringBuffer sb = new StringBuffer();
       Iterator it = reqParams.entrySet().iterator();
       while (it.hasNext()) {
           Map.Entry me = (Map.Entry) it.next();
           String key = (String) me.getKey();
           String value = (String) me.getValue();
           sb.append(key + "=" +  value + "&");
       }
       String params = sb.toString();
       if(params.length() > 0) {
       	params = params.substring(0, params.length()-1);
       }
       yeepayUrl = yeepayUrl + "?" + params;
		
   	try {
       	URL url = new URL(yeepayUrl);
   		byte resbyte[] = HttpUtils.webGet(url, new HashMap(), 60000, 60000);
   		String result = new String(resbyte, "gbk").trim();
   		String responseStr[] = result.split("\n");
   		// 创建非银行卡专业版消费请求结果
   		NonBankcardPaymentResult rs = new NonBankcardPaymentResult();
   		// 解析易宝支付返回的消费请求结果,关于返回结果数据详见《易宝支付非银行卡支付专业版接口文档 v3.0》
   		for (int t = 0; t < responseStr.length; t++) {
   			String currentResult = (String) responseStr[t];
   			if (currentResult == null || currentResult.equals("")) {
   				continue;
   			}
   			int i = currentResult.indexOf("=");
   			if (i >= 0) {
   				String sKey = currentResult.substring(0, i);
   				String sValue = currentResult.substring(i + 1);
   				if (sKey.equals("r0_Cmd")) {
   					rs.setR0_Cmd(sValue);
   				} else if (sKey.equals("r1_Code")) {
   					rs.setR1_Code(sValue);
   				} else if (sKey.equals("r6_Order")) {
   					rs.setR6_Order(sValue);
   				} else if (sKey.equals("rq_ReturnMsg")) {
   					rs.setRq_ReturnMsg(sValue);
   				} else if (sKey.equals("hmac")) {
   					rs.setHmac(sValue);
   				} else if (sKey.equals("errorMsg")) {
   					rs.setRq_ReturnMsg(rs.getRq_ReturnMsg() + "|error:" + sValue);
   				}
   				else {
   					PlatformSavingCenter.logger.error("[UC充值调用] [错误:调用返回的结果解析错误("+currentResult+")] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [角色id:"+others[0]+"] [url:"+yeepayUrl+"] \n" + result);
   				}
   			} else {
					PlatformSavingCenter.logger.error("[UC充值调用] [错误:调用返回的结果解析错误("+currentResult+")] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [角色id:"+others[0]+"] [url:"+yeepayUrl+"]\n" + result);
   			}
   		}
   		// 不成功则抛出异常
   		//r1code调用结果,1成功，其他失败
			int r1code = Integer.parseInt(rs.getR1_Code());
   		if (r1code != 1) {
				PlatformSavingCenter.logger.error("[UC充值调用] [错误:调用失败{errorCode:" + rs.getR1_Code() + ";errorMessage:"
   					+ rs.getRq_ReturnMsg()+com.xuanzhi.language.translate.text_1640+"易宝"+com.xuanzhi.language.translate.text_1641+cardtype+com.xuanzhi.language.translate.text_1642+cardinfo+com.xuanzhi.language.translate.text_1643+mianzhi+com.xuanzhi.language.translate.text_1644+order.getOrderId()+com.xuanzhi.language.translate.text_1645+passport.getUserName()+com.xuanzhi.language.translate.text_1646+"--"+com.xuanzhi.language.translate.text_1647+yeepayUrl+"] [角色id:"+others[0]+"]");
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				order.setHandleResultDesp(rs.getRq_ReturnMsg());
				willUpdateFieldNames.add("handleResult");
				willUpdateFieldNames.add("handleResultDesp");
   			//增加坏充值的个数
   			PlatformSavingCenter.getInstance().addBadSaving(passport.getId());
   		} else {
	    		String newHmac = "";
	    		newHmac = YeepayDigestUtil.getHmac(new String[] { rs.getR0_Cmd(),
	    				rs.getR1_Code(),rs.getR6_Order(),
	    				rs.getRq_ReturnMsg() }, privateKey);
	    		// hmac不一致则抛出异常
	    		if (!newHmac.equals(rs.getHmac())) {
					PlatformSavingCenter.logger.error("[UC充值调用] [错误:调用返回验证不合法] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [url:"+yeepayUrl+"] [角色id:"+others[0]+"]");
					order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);	
					willUpdateFieldNames.add("handleResult");
	    		} else {
	    			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
	    			willUpdateFieldNames.add("handleResult");
	    			PlatformSavingCenter.logger.info("[UC充值调用] [成功] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:"+rs.getR1_Code()+"/"+yeepayResultCodeDespMap.get(rs.getR1_Code())+"] [url:"+yeepayUrl+"] [角色id:"+others[0]+"]");
	    		}
   		}
   		order.setHandleResultDesp(r1code + ":" + yeepayResultCodeDespMap.get(String.valueOf(r1code)));
   		willUpdateFieldNames.add("handleResultDesp");
   	} catch(Exception e) {
   		e.printStackTrace();
   		PlatformSavingCenter.logger.error("[UC充值调用] [异常] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [url:"+yeepayUrl+"] [角色id:"+others[0]+"]", e);
   		order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
   		willUpdateFieldNames.add("handleResult");
   		order.setHandleResultDesp(com.xuanzhi.language.translate.text_1576 + e.getMessage());
   		willUpdateFieldNames.add("handleResultDesp");
   	}
   	try {
   		orderFormManager.batch_updateField(order, willUpdateFieldNames);
   	} catch(Exception e) {
   		e.printStackTrace();
   		PlatformSavingCenter.logger.error("[UC充值调用] [异常:更新订单异常] [支付平台:易宝] [介质:"+cardtype+"] [介质信息:"+cardinfo+"] [面值:"+mianzhi+"] [订单:"+order.getOrderId()+"] [用户:"+passport.getUserName()+"] [结果:-----] [-----] [角色id:"+others[0]+"]", e);
   	}
   	return orderId;
   }
	
	
	
    public static UCYeepaySavingManager getInstance() {
		return m_self;
	}
    
	public void setYeepayReturnUrl(String yeepayReturnUrl) {
		this.yeepayReturnUrl = yeepayReturnUrl;
	}

	public void setSzxUrl(String szxUrl) {
		this.szxUrl = szxUrl;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getYeepayReturnUrl() {
		return yeepayReturnUrl;
	}

	public String getSzxUrl() {
		return szxUrl;
	}

	public String getMerId() {
		return merId;
	}

	public String getPrivateKey() {
		return privateKey;
	}
//	JUNNET 骏网一卡通 
//	SNDACARD 盛大卡 
//	SZX 神州行 
//	ZHENGTU 征途卡 
//	QQCARD Q币卡 
//	UNICOM 联通卡 
//	JIUYOU 久游卡 
//	YPCARD 易宝e卡通 
//	NETEASE 网易卡 
//	WANMEI 完美卡 
//	SOHU 搜狐卡 
//	TELECOM 电信卡 
//	ZONGYOU 纵游一卡通 
//	TIANXIA 天下一卡通 
//	TIANHONG 天宏一卡通 

}
