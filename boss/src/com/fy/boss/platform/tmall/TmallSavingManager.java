package com.fy.boss.platform.tmall;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.game.model.Server;
import com.fy.boss.game.service.ServerManager;
import com.fy.boss.platform.tmall.TmallSavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class TmallSavingManager extends HttpServlet implements Runnable {
	public static TmallSavingManager self;

	//正式版
	//public static String PRIVATE_KEY = "b54d4b3e13cb682842cee60675bdc123";
	public static String PRIVATE_KEY = "f89b9f36338df851ba76502174b138a9";

//	public static String PRIVATE_KEY = "ecardintegrationtestecardintegra";
	
	public static String SANGUO_KEY = "548fj9304jgogj98u503djfg932";
	
	public static String SANGUO_URL = "http://b.sqgames.net/game_boss/tmResult";

	//正式版
//	public static String COOPID = "1035598051";
	
	//新版
	
	//public static String COOPID = "3593650262";
	public static String COOPID = "1695470576";
	
//	public static String COOPID = "182589235";

	public static String ASYNC_NOTIFY_URL = "http://cpgw.taobao.com/ecard/ecard_result.do";

	public static Map<String, Long> cardMap = new HashMap<String, Long>();

	public static Map<String, String> serverMap = new HashMap<String, String>();

	public static Map<String, String> wxMap = new HashMap<String, String>();

	public static Map<String, String> sgMap = new HashMap<String, String>();
	

//	public static String GAME_WANGXIAN_ID = "1016";
//	public static String GAME_SANGUO_ID = "1015";
	
	static {
		cardMap.put("T9YHUHSF5",200L);  
		cardMap.put("FFRDY4E3G",3000L); 
		cardMap.put("DP5VWLKSU",6000L); 
		cardMap.put("9VFRCQXNX",30000L);
		cardMap.put("7XQLM5U4V",1000L); 
		cardMap.put("E5Q7PF6L9",5000L); 
		cardMap.put("733HFEWQD",10000L);
		cardMap.put("9L399XMNX",20000L);
		cardMap.put("69QJCV94J",50000L);
		cardMap.put("KL6MMK7YA",100000L);
		cardMap.put("UP7K9GA43",200000L);
		cardMap.put("746TR9CLS",500000L);
		
		cardMap.put("5WLMML3V3",600L);  
		cardMap.put("UPR3XAQ9N",1200L); 
		cardMap.put("YM5VMLYKH",3000L); 
		cardMap.put("HKYVE9QQY",6000L); 
		cardMap.put("P7V4A3MAE",30000L);
		cardMap.put("YU5URWKS9",500000L);
		cardMap.put("FJAAEA43G",1000L); 
		cardMap.put("AJQASANPS",5000L); 
		cardMap.put("4KN63UJ97",10000L);
		cardMap.put("4XC6WMXXY",20000L);
		cardMap.put("4GLSWQWV6",50000L);
		cardMap.put("SCNEJGPUY",100000L);
		cardMap.put("9FRUHTCML",200000L);

		/**
		 * 
		 * 问天灵台
玉幡宝刹
飞瀑龙池
西方灵山
雪域冰城
白露横江
左岸花海
裂风峡谷
右道长亭
永安仙城
霹雳霞光
对酒当歌
独霸一方
独步天下
飞龙在天
九霄龙吟
万象更新
春风得意
天下无双
幻灵仙境
仙子乱尘
梦倾天下
再续前缘
兰若古刹
火烧连营
风林火山
		 */
		
		
		
		serverMap.put("29515","问天灵台");   
		serverMap.put("29516","玉幡宝刹");
		serverMap.put("29518","飞瀑龙池");
		serverMap.put("29517","西方灵山");
		serverMap.put("29519","雪域冰城");
		
		serverMap.put("29520","白露横江");   
		serverMap.put("29530","左岸花海");
		serverMap.put("29723","裂风峡谷");
		serverMap.put("32200","右道长亭");
		serverMap.put("32201","永安仙城");
		serverMap.put("32202","霹雳霞光");
		serverMap.put("32203","对酒当歌");
		serverMap.put("32204","独霸一方");
		serverMap.put("32205","独步天下");
		serverMap.put("32206","飞龙在天");
		serverMap.put("32207","九霄龙吟");
		serverMap.put("32208","万象更新");
		serverMap.put("32209","春风得意");
		serverMap.put("32210","天下无双");
		serverMap.put("32211","幻灵仙境");
		serverMap.put("32212","仙子乱尘");
		serverMap.put("32213","梦倾天下");
		serverMap.put("32214","再续前缘");
		serverMap.put("32215","兰若古刹");
		serverMap.put("32569","权倾皇朝");
		serverMap.put("32622","诸神梦境");
		serverMap.put("32786","倾世情缘");
		serverMap.put("32877","傲啸封仙");
		serverMap.put("32937","一统江湖");
		serverMap.put("33003","龙隐幽谷");
		//三国
		serverMap.put("29521","火烧连营");
		serverMap.put("32216","风林火山");

		
		wxMap.put("问天灵台","");   
		wxMap.put("玉幡宝刹","");
		wxMap.put("飞瀑龙池","");
		wxMap.put("西方灵山","");
		wxMap.put("雪域冰城","");
		
		wxMap.put("白露横江","");
		wxMap.put("左岸花海","");
		wxMap.put("裂风峡谷","");
		wxMap.put("右道长亭","");
		wxMap.put("永安仙城","");
		wxMap.put("霹雳霞光","");
		wxMap.put("对酒当歌","");
		wxMap.put("独霸一方","");
		wxMap.put("独步天下","");
		wxMap.put("飞龙在天","");
		wxMap.put("九霄龙吟","");
		wxMap.put("万象更新","");
		wxMap.put("春风得意","");
		wxMap.put("天下无双","");
		wxMap.put("幻灵仙境","");
		wxMap.put("仙子乱尘","");
		wxMap.put("梦倾天下","");
		wxMap.put("再续前缘","");
		wxMap.put("兰若古刹","");
		wxMap.put("权倾皇朝","");
		wxMap.put("诸神梦境","");
		wxMap.put("倾世情缘","");
		wxMap.put("傲啸封仙","");
		wxMap.put("一统江湖", "");
		wxMap.put("龙隐幽谷", "");
		
		sgMap.put("火烧连营", "");
		sgMap.put("风林火山", "");
	}

	public static List<String> orderList = new LinkedList<String>();
	public static Map<String,String> orderNotifyUrlMap= new ConcurrentHashMap<String,String>();

	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);
				if (orderList.size() > 0) {
					synchronized (this) {
						while (orderList.size() > 0) {
							String order = orderList.remove(0);
							String notifyUrl = orderNotifyUrlMap.remove(order);
							asyncToTmall(order,notifyUrl);
						}
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public void asyncToTmall(String order,String notifyUrl) {
		long start = System.currentTimeMillis();
		
		
		String url = "";
		if(notifyUrl == null || notifyUrl.length()<=0)
		{
			url = new String(ASYNC_NOTIFY_URL);
		}
		else
		{
			url = notifyUrl;
		}
				
		HashMap<String, String> pmap = new HashMap<String, String>();
		pmap.put("coopId", COOPID);
		pmap.put("tbOrderNo", order.split(",")[0]);
		pmap.put("coopOrderNo", order.split(",")[1]);
		pmap.put("coopOrderStatus", "SUCCESS");
		pmap.put("coopOrderSnap", "TmallSavingSuccess");
		pmap.put("coopOrderSuccessTime", DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
		url += "?" + getSignedUrl(pmap, PRIVATE_KEY);
		try {
			byte result[] = HttpUtils.webGet(new URL(url), new HashMap(), 15000, 15000);
			PlatformSavingCenter.logger.info("[异步通知TMALL订单] [成功] [order:" + order + "] [url:" + url + "] [return:" + new String(result) + "] ["
					+ (System.currentTimeMillis() - start) + "ms]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			PlatformSavingCenter.logger.error("[异步通知TMALL订单] [异常] [OrderNo:" + order + "] [url:" + url + "] [return:] ["
					+ (System.currentTimeMillis() - start) + "ms]", e);
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		TmallSavingManager.self = this;
		Thread t = new Thread(this, "TmallOrderQueryServlet");
		t.start();
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
		PlatformSavingCenter.logger.info("[" + this.getClass().getName() + "] [initialized]");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();

		req.setCharacterEncoding("gbk");

		res.setCharacterEncoding("utf-8");

		String coopId = ParamUtils.getParameter(req, "coopId", "").trim();
		String tbOrderNo = ParamUtils.getParameter(req, "tbOrderNo", "").trim();
		String cardId = ParamUtils.getParameter(req, "cardId", "").trim();
		String cardNum = ParamUtils.getParameter(req, "cardNum", "").trim();
		String customer = ParamUtils.getParameter(req, "customer", "").trim();
		String sum = ParamUtils.getParameter(req, "sum", "").trim();
		String gameId = ParamUtils.getParameter(req, "gameId", "").trim();
		String section1 = ParamUtils.getParameter(req, "section1", "").trim();
		String section2 = ParamUtils.getParameter(req, "section2", "").trim();
		String tbOrderSnap = ParamUtils.getParameter(req, "tbOrderSnap", "").trim();
		String notifyUrl = ParamUtils.getParameter(req, "notifyUrl", "").trim();
		String sign = ParamUtils.getParameter(req, "sign", "").trim();

		// 先验证合法性
		HashMap<String, String> pmap = new HashMap<String, String>();
		pmap.put("coopId", coopId);
		pmap.put("tbOrderNo", tbOrderNo);
		if (cardId.length() > 0) {
			pmap.put("cardId", cardId);
		}
		if (cardId.length() > 0) {
			pmap.put("cardNum", cardNum);
		}
		pmap.put("customer", customer);
		pmap.put("sum", sum);
		if (gameId.length() > 0) {
			pmap.put("gameId", gameId);
		}
		if (section1.length() > 0) {
			pmap.put("section1", section1);
		}
		if (section2.length() > 0) {
			pmap.put("section2", section2);
		}
		if (notifyUrl.length() > 0) {
			pmap.put("notifyUrl", notifyUrl);
		}
		if (tbOrderSnap.length() > 0) {
			pmap.put("tbOrderSnap", tbOrderSnap);
		}

		String mySign = sign(pmap, PRIVATE_KEY);

		String result = "ORDER_FAILED";
		String failCode = "";
		String failReason = "";
		String game = "";
		String serverName = serverMap.get(section1);
		
		
//		if(gameId.equals(GAME_WANGXIAN_ID)) {
//			game = "飘渺寻仙曲";
//		} else if(gameId.equals(GAME_SANGUO_ID)) {
//			game = "三国时代";
//		}
		
		if(wxMap.get(serverName) != null) {
			game = "飘渺寻仙曲";
		} else if(sgMap.get(serverName) != null) {
			game = "三国时代";
		}
		else
		{
			game = "飘渺寻仙曲";
		}

		if (coopId.equals(COOPID)) {
			// 创建订单，返回成功
			Long amount = cardMap.get(cardId);
			if (amount != null && amount > 0) {
				int num = 0;
				try
				{
					num = Integer.valueOf(cardNum);
					
				}
				catch(Exception e)
				{
					PlatformSavingCenter.logger.warn("[天猫直充订单] [失败] [出现异常]",e);
				}
				finally
				{
					if(num <= 0)
					{
						result = "ORDER_FAILED";
						failCode = "0103";
						failReason = "invalid cardnum";
						HashMap<String, String> returnMap = new HashMap<String, String>();
						returnMap.put("tbOrderNo", tbOrderNo);
						returnMap.put("coopOrderStatus", result);
						returnMap.put("failedCode", failCode);
						returnMap.put("failedReason", failReason);
						String xmlContent = xmlContent(returnMap);
						res.getWriter().write(xmlContent);
						PlatformSavingCenter.logger.warn("[天猫直充订单] [失败:" + failReason + "] [游戏:"+game+"] [sign:" + sign + "] [mySign:" + mySign + "] [sighString:"
								+ getSighString(pmap, PRIVATE_KEY) + "] [" + req.getQueryString() + "] [return:" + xmlContent + "] [" + (System.currentTimeMillis() - start)
								+ "ms]");
						return;
					}

				}
				
				amount = amount * num;
				if (mySign.toLowerCase().equals(sign.toLowerCase())) {
					// 如果是飘渺寻仙曲，这边处理，否则交给三国处理
					if (game.equals("飘渺寻仙曲")) {
						// 合法的
						// 1.如果存在帐号，立即生成订单，返回成功
						Passport passport = PassportManager.getInstance().getPassport(customer);
						if (passport != null) {
							// 检查是否服务器正确并且有这个用户的角色
							Server server = ServerManager.getInstance().getServer(serverName);
							if (server != null && serverHasPlayer(passport.getUserName(), serverName)) {
								synchronized (this) {
									// 看看有没有淘宝的这个订单存在
									String myOrderId = "TB-" + tbOrderNo;
									OrderForm order = OrderFormManager.getInstance().getOrderForm(myOrderId);
									if (order != null) {
										result = "ORDER_FAILED";
										failCode = "0301";
										failReason = "Duplicate order";
									} else {
										try {
											myOrderId = tmallSaving(passport, tbOrderNo, serverName, amount);
//											order = OrderFormManager.getInstance().getOrderForm(myOrderId);
											HashMap<String, String> returnMap = new HashMap<String, String>();
											returnMap.put("tbOrderNo", tbOrderNo);
											returnMap.put("coopOrderNo", myOrderId);
											//returnMap.put("coopOrderStatus", "SUCCESS");
											returnMap.put("coopOrderStatus", "UNDERWAY");
										//	returnMap.put("coopOrderSnap", "Tmall saving " + amount + " feng.");
									//		returnMap.put("coopOrderSuccessTime", DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
											String xmlContent = xmlContent(returnMap);
											res.getWriter().write(xmlContent);
											String orderIdentify = tbOrderNo+"," + myOrderId;
											orderList.add(orderIdentify);
											orderNotifyUrlMap.put(tbOrderNo+"," + myOrderId,notifyUrl);
											PlatformSavingCenter.logger.warn("[天猫直充订单] [成功] [游戏:"+game+"] [自有订单:" + myOrderId + "] [淘宝订单:" + tbOrderNo + "] [充值额:" + amount
													+ "] [" + req.getQueryString() + "] [return:" + xmlContent + "] [" + (System.currentTimeMillis() - start)
													+ "ms]");

											return;
										} catch (Exception e) {
											result = "ORDER_FAILED";
											failCode = "0306";
											failReason = "Exception occured";
											PlatformSavingCenter.logger.error("[天猫直充订单] [失败:发生异常] [游戏:"+game+"] [淘宝订单:" + tbOrderNo + "] [充值额:" + amount + "] ["
													+ req.getQueryString() + "] [" + (System.currentTimeMillis() - start) + "ms]", e);
										}
									}
								}
							} else {
								// 服务器名错误
								result = "ORDER_FAILED";
								failCode = "0101";
								failReason = "Server not found.";
							}
						} else {
							result = "ORDER_FAILED";
							failCode = "0306";
							failReason = "Username not exists.";
						}
					} else {
						//三国
						String content = sanguoSaving(tbOrderNo,customer,serverMap.get(section1),amount);
						if(content.equals("FAILED")) {
							result = "ORDER_FAILED";
							failCode = "0306";
							failReason = "Saving failed.";
						} else {
							HashMap<String, String> returnMap = new HashMap<String, String>();
							returnMap.put("tbOrderNo", tbOrderNo);
							returnMap.put("coopOrderNo", content);
							returnMap.put("coopOrderStatus", "SUCCESS");
							returnMap.put("coopOrderSnap", "Tmall saving " + amount + " feng.");
							returnMap.put("coopOrderSuccessTime", DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
							String xmlContent = xmlContent(returnMap);
							res.getWriter().write(xmlContent);
							orderList.add(tbOrderNo + "," + content);
							PlatformSavingCenter.logger.warn("[天猫直充订单] [成功] [游戏:"+game+"] [自有订单:" + content + "] [淘宝订单:" + tbOrderNo + "] [充值额:" + amount
									+ "] [" + req.getQueryString() + "] [return:" + xmlContent + "] [" + (System.currentTimeMillis() - start)
									+ "ms]");
							return;
						}
					}
				} else {
					// 不合法
					result = "ORDER_FAILED";
					failCode = "0102";
					failReason = "Sign code invalid";
				}
			} else {
				// 不合法的计费代码
				result = "ORDER_FAILED";
				failCode = "0305";
				failReason = "Illigal cardId";
			}
		} else {
			// 不合法
			result = "ORDER_FAILED";
			failCode = "0306";
			failReason = "CoopId not match.";
		}
		HashMap<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("tbOrderNo", tbOrderNo);
		returnMap.put("coopOrderStatus", result);
		returnMap.put("failedCode", failCode);
		returnMap.put("failedReason", failReason);
		String xmlContent = xmlContent(returnMap);
		res.getWriter().write(xmlContent);
		PlatformSavingCenter.logger.warn("[天猫直充订单] [失败:" + failReason + "] [游戏:"+game+"] [sign:" + sign + "] [mySign:" + mySign + "] [sighString:"
				+ getSighString(pmap, PRIVATE_KEY) + "] [" + req.getQueryString() + "] [return:" + xmlContent + "] [coopId:"+coopId+"] [my coopId:"+COOPID+"] [" + (System.currentTimeMillis() - start)
				+ "ms]");
	}

	public String tmallSaving(Passport passport, String tmOrderNo, String servername, long amount) throws Exception {
		long t = System.currentTimeMillis();
		OrderForm order = new OrderForm();
		order.setCreateTime(t);
		order.setOrderId("TB-" + tmOrderNo);
		order.setPassportId(passport.getId());
		order.setServerName(servername);
		order.setSavingPlatform("天猫商城");
		order.setSavingMedium("天猫直充");
		order.setMemo1("IOS");
		order.setPayMoney(amount);
		order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
		order.setResponseResult(OrderForm.RESPONSE_SUCC);
		order.setUserChannel(passport.getLastLoginChannel());
		order.setMemo2("淘宝订单:" + tmOrderNo);
		order = OrderFormManager.getInstance().createOrderForm(order);
		return order.getOrderId();
	}
	
	public String sanguoSaving(String tmOrderNo, String username,  String serverName, long paymoney) {
		String sign = StringUtil.hash(tmOrderNo+username+serverName+paymoney+SANGUO_KEY);
		String url = SANGUO_URL + "?tmOrderNo=" + tmOrderNo + "&username=" + java.net.URLEncoder.encode(username) + "&serverName=" + java.net.URLEncoder.encode(serverName) + "&paymoney=" + paymoney + "&sign=" + sign;
		try {
			byte result[] = HttpUtils.webGet(new URL(url), new HashMap(), 30000, 30000);
			String content = new String(result, "utf-8").trim();
			System.err.println("[天猫充值trace] [调用三国充值] ["+content+"] ["+url+"] [mysign:"+sign+"] [signString:"+(tmOrderNo+username+serverName+paymoney+SANGUO_KEY)+"]");
			return content;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "FAILED";
	}

	public static String sign(HashMap<String, String> params, String secretkey) {

		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			String v = params.get(keys[i]);
			sb.append(keys[i] + v);
		}
		String md5Str = sb.toString() + secretkey;

		String sign = md5(md5Str);

		return sign;
	}

	public static String getSighString(HashMap<String, String> params, String secretkey) {
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			String v = params.get(keys[i]);
			sb.append(keys[i] + v);
		}
		String md5Str = sb.toString() + secretkey;
		return md5Str;
	}

	public static String getSignedUrl(HashMap<String, String> params, String secretkey) {
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			String v = params.get(keys[i]);
			sb.append(keys[i] + v);
		}
		String sign = md5(sb.toString() + secretkey);
		sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			String v = params.get(keys[i]);
			try {
				v = java.net.URLEncoder.encode(v, "gbk");
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb.append(keys[i] + "=" + v + "&");
		}
		return sb.toString() + "sign=" + sign;
	}

	public static String xmlContent(HashMap<String, String> cmap) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><response>");
		String keys[] = cmap.keySet().toArray(new String[0]);
		for (String s : keys) {
			String v = cmap.get(s);
			sb.append("<" + s + ">" + v + "</" + s + ">");
		}
		sb.append("</response>");
		return sb.toString();
	}

	public boolean serverHasPlayer(String username, String serverName) {
		Server server = ServerManager.getInstance().getServer(serverName);
		if (server != null) {
			String url = server.getSavingNotifyUrl();
			String ss[] = url.split("saving_notifier");
			String checkUrl = ss[0] + "playercheck.jsp" + ss[1] + "&username=" + username;
			System.err.println("[天猫直充] [DEBUG_checkurl] [" + serverName + "] [" + username + "] [" + checkUrl + "]");
			try {
				byte[] result = HttpUtils.webGet(new URL(checkUrl), new HashMap(), 15000, 15000);
				String content = new String(result, "utf-8");
				if (content.indexOf("OK") != -1) {
					return true;
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public static String md5(String s) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(s.getBytes("GBK"));
			return StringUtil.toHex(md5.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	public static void main(String args[]) {
		String s = "cardId67JURYCQSM9Q4cardNum1coopId182589235customertestgameId1015notifyUrlhttp://cpgw.daily.taobao.net/ecard/ecard_result.dosection15278section25279sum29.88tbOrderNo92140084647185tbOrderSnap29.88|传奇世界30元|传奇世界|封闭测试区|测试1ecardintegrationtestecardintegra";
		System.out.println(md5(s));
	}
}
