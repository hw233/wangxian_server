package com.fy.boss.finance.service.platform;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.alipay.api.internal.util.AlipaySignature;
import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.DateUtil;

public class AlipaySavingManager {

	protected static AlipaySavingManager m_self = null;
	
	public static final int PAY_APP = 1;
	public static final int PAY_WEB = 5;
	public static final int PAY_WAP = 2;

	
	public static final String APPID = "2018050760136049";//"2018020702158254";
	
	public static final String notifyUrl = "http://119.254.154.201:12111/alipayResult";
	
	public static final String partnerID = "2088701002800853";
	public static final String sellerID = "2088701002800853";
//	public static final String aliPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCgG3oLMgTCsYsD+24Y0ejakmMUTjRSylqzoxb jpdEFk26RXD1JYnRXOA9tYKPh9cnR3+epsvgh8RcFwhGqbQ/FK6ageYWNM70Q+X37R1bGE2BrN8o jyDnwDcqa8YW8QCB63bgMAJdDLgII1WvQf3ds53H1UjzhZpc+0lKaX+QZQIDAQAB";
	public static final String aliPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmIsYz8IHcB7zfwo1tu59Vjptr270x3GFn/cg4lUxIM/bdctt6q2vU9qkjmu7nJCS/xAZp87Adn5MiT1QV+C5ioYeIjeGgtJTZaJtJ0PDymhLX7tf79N3FF6KrpBeurcK1csd4j1aVIztkfux7gd5gUHLlX05oLiMaQn4ov3pl6TBXcFcJ1DH2TET0bBBaV2EDorvUNL8HEoz3EaJ3csrKzmxqi2jsr/36bLahiddSz8UtodRjCkAFCTUxLjK+jcnh6ZdLDnFPdkdVPHvfTmLPsMJTP5J5lTXo2fnqwkbW4bfZLRpxGFGKNXkRYUc3ZMaQjtq2pnm1Hl9ACzhoTVtdwIDAQAB";

	
	public static final String ownPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3thLDG2dfDgYXFdRnfv+zvsbrZGrGc8xfWNSDccjEhEHPXAHJCgNWS1CLh7+SDPDdBpH+Toaq6hdXZrTxhdoFbuXNXKqiRRU5FYQZVush4stcb8Hfq8Nnz521Afu5JumZSgb/rxP/fCxKeTMsGfOHPYj7NAMlyYqGXa4/EnNS4xOL2+Ten71kv/2cphbqkacIHoCJC/uungbv9uP1ueAAvBEDLq37fAai0FMHMJkNlsvk2KzzFWNxOM/asJzMjg1s5KBUKZQHA9511V3IrNYdUs2nzs6TTeGh0We2hwtAmw3sSbi3HtcKAnBxv4g0op5MgT2hdtjaBh1ALQZWiBGaQIDAQAB";
	public static final String ownPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDe2EsMbZ18OBhcV1Gd+/7O+xutkasZzzF9Y1INxyMSEQc9cAckKA1ZLUIuHv5IM8N0Gkf5OhqrqF1dmtPGF2gVu5c1cqqJFFTkVhBlW6yHiy1xvwd+rw2fPnbUB+7km6ZlKBv+vE/98LEp5MywZ84c9iPs0AyXJioZdrj8Sc1LjE4vb5N6fvWS//ZymFuqRpwgegIkL+66eBu/24/W54AC8EQMurft8BqLQUwcwmQ2Wy+TYrPMVY3E4z9qwnMyODWzkoFQplAcD3nXVXcis1h1SzafOzpNN4aHRZ7aHC0CbDexJuLce1woCcHG/iDSinkyBPaF22NoGHUAtBlaIEZpAgMBAAECggEAB+b7olEwyHrkwkGGb2fEPWrCLIGB48RpKN0gx8RMBhP8gb07Q5j4ADWOpzwKIF1VjQRxkPDyiSvLQ8p9wASMeFjSn96VFY9XA1HZ+v4pPBU/Y0024tMqVH9Oj25Ilmq+l8+4Tmw3GfYzEh0+9PJ5aKWB2J/4ElQUIhsHrEqhsRATs1DyDCFYPmLdiHBD1pDtrNrvUAus5GOFt+dXZ1gUC3h83Zjyc2IvUTV2nomLL9QWBA/ziLCk8hYgygVuM/ttbCighwWf9NqpxrDnYHf5WXrsj976YBnAnEIYEXEa/qdexu5l1QPmvd9F9gnlj/sSzWVgeB897GeUvIaFCzePQQKBgQD11R8f2U1xqchvZoqRpEqZDn41yeadMth3VHBjU9Fc+KppvXZo79xVcPtpSc/pt8BXx6l4siKjbD+fn3Ia0yFu5oC24XWooK7iROJLe9jCFPqjF5+5QS/3YMhilE3+/0DQpeR7ytO7lYGH+nEy6uJZWL8PE3aK546rxBBkmc58jQKBgQDoD8dEzjQeKFLa12wNIFZP+3Hdpi83937N5qznCfkfkJpqKZ1O9METmYatEX9Jb0NlqLKqgipdDBLIwWAuzC7bHXokXAGe1kwIqYHsffKfB0eJ+AQfxYQtHH9nEFxzqCQx6oYsh/2TKI+8E/JQ1mcL+BjBY3HWx0/wcLbroywQTQKBgHh8yXF5twxjk7qN/pKdfHaB9Pf2ChK4DgKGn0L/nNi9hACpfLS4AzzZUE/tfnrltd8QHA7s+C8y6ggZRPrCylspnKKF5H4Oq0h5TJSEkdumHqX6+Sz6N0E+7VfrqkK15pWdCVxnL9l70G3OINc86k2yQopoFqzEC1hCk2pLPDotAoGBAMx6UriTBaNpHpDMbt4mF0fEpLGMpGCpSOd/Ee1GgWhJz3YqEv2mdpahmeTX3RPE4VsrSbsqlM+GOusAUZMKwWK7V4vAMoFWDIs5aU0MMYPlMykkC+Y+8Veam7wh04TprHL7VpzYexx3CifMX6DROt12D8IfsQhyeULA3r1E+yelAoGALB2ixV/kk7LpeLc0vZNchSUUJntCyCpF+KX1n9OGWsp9xSL/pLvzytHKjHTeZBcKMKDYj54YpPvJlDw6qSVhfy6VQ2d6DdQHg9Yog0+o2szlND1dElMLwHtWjoAnzI4O2P+wDZkQrqHk6VXKqXPMKoQupWD/lFOt8WpVFxa5wZA=";
//	public static final String notifyUrl = "http://119.254.154.201:12111/alipayResult";

	public void initialize() throws Exception {
		m_self = this;
	}

	/**
	 * 支付宝充值
	 * 
	 * @param passport
	 * @param channel
	 * @param servername
	 * @param addAmount 分
	 * @param addType
	 *            充值方式： 1-支付宝， 5-页面支付， 2-wap支付
	 * @param callbackUrl
	 *            页面支付或wap支付的回调页面
	 * @return
	 */
	public String aliSaving(Passport passport, String channel, String servername, int addAmount, int addType, String callbackUrl, String mobileOs,String[]others) {
		long t = System.currentTimeMillis();

		String medinfo = "充值额:" + addAmount;
		String mediumName = "未知";
		if (addType == 1) {
			mediumName = "支付宝";
		} else if (addType == 5) {
			mediumName = "页面支付";
		} else if (addType == 2) {
			mediumName = "wap页面支付";
		}

		try {
			// 先生成一个订单
			OrderForm order = new OrderForm();

			java.util.Date cdate = new java.util.Date();

			order.setCreateTime(t);
			order.setPassportId(passport.getId());
			order.setServerName(servername);

			order.setSavingPlatform("支付宝");

			order.setSavingMedium(mediumName);
			order.setMediumInfo(medinfo);
			order.setMemo1(callbackUrl);
			order.setPayMoney(new Long(addAmount));
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			order = OrderFormManager.getInstance().createOrderForm(order);

			long id = order.getId();
			String orderID = DateUtil.formatDate(cdate, "yyyyMMddHH") + id;
			order.setOrderId(orderID);
			OrderFormManager.getInstance().update(order);

			if (order.getId() > 0 && order.getOrderId() != null) {
				PlatformSavingCenter.logger.info("[充值调用] [成功] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [手机平台:"+mobileOs+"] [角色id:"+others[0]+"]");
				String timestamp = DateUtil.formatDate(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss");
				//biz_content公共参数外所有请求参数集合
				LinkedHashMap<String, String> qianMingMap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, Object> alipayMap = new LinkedHashMap<String, Object>();
				qianMingMap.put("timeout_express","30m");
				qianMingMap.put("product_code", "QUICK_MSECURITY_PAY");
				qianMingMap.put("total_amount",(addAmount/100)+"");
				qianMingMap.put("subject","飘渺充值");
				qianMingMap.put("body","飘渺使用支付宝充值");
				qianMingMap.put("out_trade_no",orderID);
//				String selfContent = buildParams(qianMingMap);
				alipayMap.put("app_id",APPID);
				JacksonManager jm = JacksonManager.getInstance();
				alipayMap.put("biz_content",jm.toJsonString(qianMingMap));
				alipayMap.put("charset","utf-8");
				alipayMap.put("format","json");
				alipayMap.put("method","alipay.trade.app.pay");
				alipayMap.put("notify_url",notifyUrl);
				alipayMap.put("sign_type","RSA2");
				alipayMap.put("timestamp",timestamp);
				alipayMap.put("version","1.0");
				String alipayContent = buildParams(alipayMap);
				String sign = AlipaySignature.rsaSign(alipayContent, ownPrivateKey, "utf-8", "RSA2");  //StringUtil.hash(alipayContent);
				alipayMap.put("sign",sign);
				
				
				String strsign = buildParams2(alipayMap);
				PlatformSavingCenter.logger.info("[充值调用] [产生订单成功] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [手机平台:"+mobileOs+"] [角色id:"+others[0]+"]" +
								"[alipayContent:"+alipayContent+"] [sign:"+sign+"] [strsign:"+strsign+"]");
				return strsign;
			} else {
				PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [手机平台:"+mobileOs+"] [角色id:"+others[0]+"]");
				return null;
			}
		} catch (Exception e) {
			PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:支付宝] [方式:" + mediumName + "] [用户:" + passport.getUserName() + "] [amount:" + addAmount
					+ "分] [订单号:-----] [手机平台:"+mobileOs+"] [角色id:"+others[0]+"]", e);
		}
		return null;
	}
	
//	String getAlipayOrderInfoSqage(String orderId, long addAmount) {
//		String strOrderInfo = "partner=" + "\"" + partnerID + "\"";
//		strOrderInfo += "&";
//		strOrderInfo += "seller=" + "\"" + sellerID + "\"";
//		strOrderInfo += "&";
//		strOrderInfo += "out_trade_no=" + "\"" +orderId + "\"";
//		strOrderInfo += "&";
//		strOrderInfo += "subject=" + "\"飘渺寻仙曲\"";
//		strOrderInfo += "&";
//		strOrderInfo += "body=" + "\"飘渺寻仙曲充值\"";
//		strOrderInfo += "&";
//		strOrderInfo += "total_fee=" + "\""+ addAmount/100 + "\"";
//		strOrderInfo += "&";
//		strOrderInfo += "notify_url=\""+ notifyUrl + "\"";
//		String strsign = Rsa.sign(strOrderInfo, ownPrivateKey);
//		strsign = URLEncoder.encode(strsign);
//		String info = strOrderInfo + "&sign=\"" + strsign + "\"&sign_type=\"RSA\"";
//		return info;
//	}

	private String buildParams2(Map<String,Object> params) throws UnsupportedEncodingException{
		String[] key = params.keySet().toArray(new String[params.size()]);
		StringBuffer sb = new StringBuffer();
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(URLEncoder.encode((String)params.get(k),"utf-8"));
			sb.append("&");
		}
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	
	private String buildParams(Map<String,Object> params){
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
	
	
	public static AlipaySavingManager getInstance() {
		return m_self;
	}
}
