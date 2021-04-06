package com.fy.boss.platform.website;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.website.AlipayWebSavingManager;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class AlipayWebSavingManager {

	protected static AlipayWebSavingManager m_self = null;
	
	public static final int PAY_APP = 1;
	public static final int PAY_WEB = 5;
	public static final int PAY_WAP = 2;

	public static final String partnerID = "2088701023337233";
	public static final String sellerID = "sanguopc@sqage.com";
	public static String key = "drd9te0348vpcy348qnkle069w8pj0uv";
	public static String return_url = "http://w.sqage.com/website/sy.jsp";
	public static String input_charset = "UTF-8";
	public static String sign_type = "MD5";
	public static String transport = "http";
	public static final String notifyUrl = "http://116.213.192.216/mieshi_game_boss/alipayWebResult";
	
	//调试
	//public static final String notifyUrl = "http://116.213.142.183/mieshi_game_boss/debuga.jsp";
//	public static final String notifyUrl = "http://112.25.14.13:9110/mieshi_game_boss/alipayWebResult";
//	public static final String notifyUrl = "http://112.25.14.13/mieshi_game_boss/alipayWebResult";
	 
	/**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
	private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

	public void initialize() throws Exception {
		m_self = this;
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
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
	public String aliSaving(Passport passport, String channel, String servername, int addAmount, int addType, String callbackUrl, String mobileOs) {
		long t = System.currentTimeMillis();

		String weburl = "http://w.sqage.com/win8alipay.jsp";
		
		String medinfo = "充值额:" + addAmount;
		String mediumName = "未知";
		String ip = "";
		try
		{
			ip = servername.split("@")[1];
			servername = servername.split("@")[0];
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值调用] [分解字符获取ip] [支付平台:网页支付宝] [方式:" + mediumName + "] [用户:"
					+ passport.getUserName() + "] [amount:" + addAmount+ "分] [订单号:--] [手机平台:"+mobileOs+"] [servername:"+servername+"]",e);
		}
		if (addType == 1) {
			mediumName = "天猫直充";
		} else if (addType == 5) {
			mediumName = "天猫直充";
		} else if (addType == 2) {
			mediumName = "天猫直充";
		}
		
		boolean isWin8 = false;
		
		if(channel.toLowerCase().contains("win8"))
		{
			mediumName = "网页支付宝";
			isWin8 = true;
		}

		try {
			// 先生成一个订单
			OrderForm order = new OrderForm();

			java.util.Date cdate = new java.util.Date();

			order.setCreateTime(t);
			order.setPassportId(passport.getId());
			order.setServerName(servername);

			order.setSavingPlatform("网站充值");

			order.setSavingMedium(mediumName);
			order.setMediumInfo(medinfo);
			order.setMemo1(callbackUrl);
			order.setPayMoney(new Long(addAmount));
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setUserChannel(channel);
			order.setMemo1(mobileOs);
			order = OrderFormManager.getInstance().createOrderForm(order);

			long id = order.getId();
			String orderID = "wx-"  + DateUtil.formatDate(cdate, "yyyyMMddHH") + "-" + id;
			order.setOrderId(orderID);
			OrderFormManager.getInstance().update(order);

			if (order.getId() > 0 && order.getOrderId() != null) {
				String orderId = order.getOrderId();
				String body = "银两"; //描述
				String subject = "飘渺寻仙曲银两"; //显示在商品描述中的
				//String ip = "116.213.192.200";
				
				String chargeurl = create_direct_pay_by_user(orderID,subject,body,addAmount+"",passport.getUserName(),"",channel,ip);
				PlatformSavingCenter.logger.info("[充值调用] [成功] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [手机平台:"+mobileOs+"] [充值链接:"+chargeurl+"]");
				if(isWin8)
				{
					chargeurl =  weburl +"?u="+URLEncoder.encode(chargeurl,"utf-8");
				}
				
				
				return chargeurl;
				
				//return orderId + "@@" + orderInfo + "@@"  + "";
			} else {
				PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [手机平台:"+mobileOs+"]");
				return null;
			}
		} catch (Exception e) {
			PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:网页支付宝] [方式:" + mediumName + "] [用户:" + passport.getUserName() + "] [amount:" + addAmount
					+ "分] [订单号:-----] [手机平台:"+mobileOs+"]", e);
		}
		return null;
	}
	
    
	public String aliSaving(Passport passport, String channel, String servername, int addAmount, int addType, String callbackUrl, String mobileOs,String[]others) {
		long t = System.currentTimeMillis();

		String weburl = "http://w.sqage.com/win8alipay.jsp";
		
		String medinfo = "充值额:" + addAmount;
		String mediumName = "未知";
		String ip = "";
		try
		{
			ip = servername.split("@")[1];
			servername = servername.split("@")[0];
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值调用] [分解字符获取ip] [支付平台:网页支付宝] [方式:" + mediumName + "] [用户:"
					+ passport.getUserName() + "] [amount:" + addAmount+ "分] [订单号:--] [手机平台:"+mobileOs+"] [servername:"+servername+"] [角色id:"+others[0]+"]",e);
		}
		if (addType == 1) {
			mediumName = "天猫直充";
		} else if (addType == 5) {
			mediumName = "天猫直充";
		} else if (addType == 2) {
			mediumName = "天猫直充";
		}

		boolean isWin8 = false;
		
		if(channel.toLowerCase().contains("win8"))
		{
			mediumName = "网页支付宝";
			isWin8 = true;
		}
		
		try {
			// 先生成一个订单
			OrderForm order = new OrderForm();

			java.util.Date cdate = new java.util.Date();

			order.setCreateTime(t);
			order.setPassportId(passport.getId());
			order.setServerName(servername);

			order.setSavingPlatform("网站充值");

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
			String orderID = "wx-"  + DateUtil.formatDate(cdate, "yyyyMMddHH") + "-" + id;
			order.setOrderId(orderID);
			OrderFormManager.getInstance().update(order);

			if (order.getId() > 0 && order.getOrderId() != null) {
				String orderId = order.getOrderId();
				String body = "银两"; //描述
				String subject = "飘渺寻仙曲银两"; //显示在商品描述中的
				//String ip = "116.213.192.200";
				
				String chargeurl = create_direct_pay_by_user(orderID,subject,body,addAmount+"",passport.getUserName(),"",channel,ip);
				PlatformSavingCenter.logger.info("[充值调用] [成功] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [手机平台:"+mobileOs+"] [充值链接:"+chargeurl+"] [角色id:"+others[0]+"]");
				
				if(isWin8)
				{
					chargeurl =  weburl +"?u="+URLEncoder.encode(chargeurl,"utf-8");
				}
				
				return chargeurl;
				
				//return orderId + "@@" + orderInfo + "@@"  + "";
			} else {
				PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [手机平台:"+mobileOs+"] [角色id:"+others[0]+"]");
				return null;
			}
		} catch (Exception e) {
			PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:网页支付宝] [方式:" + mediumName + "] [用户:" + passport.getUserName() + "] [amount:" + addAmount
					+ "分] [订单号:-----] [手机平台:"+mobileOs+"] [角色id:"+others[0]+"]", e);
		}
		return null;
	}
	
	
	
    /**
     * 构造即时到帐接口
     * @param sParaTemp 请求参数集合
     * @return 表单提交HTML信息
     */
    public static String create_direct_pay_by_user(String orderId, String subject, String body, String totalFee, String username,String pwd,String channel, String ip) {
    	Map<String, String> sParaTemp = new HashMap<String,String>();
		//必填参数//
    	sParaTemp.put("payment_type", "1");//支付类型 1商品购买
		//请与贵网站订单系统中的唯一订单号匹配
		sParaTemp.put("out_trade_no", orderId);
		//订单名称，显示在支付宝收银台里的“商品名称”里，显示在支付宝的交易管理的“商品名称”的列表里。
		sParaTemp.put("subject", subject);
		//订单总金额，显示在支付宝收银台里的“应付总额”里 支付宝是元 需要转一下
		sParaTemp.put("total_fee", Double.valueOf(Long.valueOf(totalFee)/100.0d+"")+"");
		//扩展参数//
		//订单描述、订单详细、订单备注，显示在支付宝收银台里的“商品描述”里
		sParaTemp.put("body", body);//格式 多少个银两
//		sParaTemp.put("show_url", show_url);
//		sParaTemp.put("paymethod", paymethod);
//		sParaTemp.put("defaultbank", defaultbank);
		try {
			sParaTemp.put("exter_invoke_ip", ip);
			sParaTemp.put("anti_phishing_key", query_timestamp());
		} catch (Exception e1) {
			sParaTemp.remove("exter_invoke_ip");
			if(sParaTemp.get("anti_phishing_key") != null) {
				sParaTemp.remove("anti_phishing_key");
			}
			e1.printStackTrace();
		}
//		sParaTemp.put("extra_common_param", extra_common_param);
//		sParaTemp.put("buyer_email", buyer_email);
//		sParaTemp.put("royalty_type", royalty_type);
//		sParaTemp.put("royalty_parameters", royalty_parameters);
		
		StringBuffer sbuffer = new StringBuffer();
		/*sbuffer.append("login.do?userName=").append(username).append("&pwd=")
		.append(pwd).append("&channel=").append(channel);*/
	//	String returnurl = return_url + sbuffer.toString();
		String returnurl = return_url + sbuffer.toString();
         
        sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", partnerID);
        sParaTemp.put("return_url", returnurl);
        sParaTemp.put("notify_url", notifyUrl);
        sParaTemp.put("seller_email", sellerID);
        sParaTemp.put("_input_charset", input_charset);
        String params = "";
        try {
        	params = buildQuery(sParaTemp);
        	String url = ALIPAY_GATEWAY_NEW + params;
			return url;
		} catch (UnsupportedEncodingException e) {
			PlatformSavingCenter.logger.error("[充值调用] [生成充值url] [失败:发生异常] [支付平台:网页支付宝] [支付url:"+ALIPAY_GATEWAY_NEW+"] [查询参数:"+params+"] [返回链接:"+returnurl+"] [notifyUrl:"+notifyUrl+"]", e);
			return "";
		}
    }
    
    /**
     * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数
     * 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
     * @return 时间戳字符串
     * @throws IOException
     * @throws DocumentException
     * @throws MalformedURLException
     */
    public static String query_timestamp() throws MalformedURLException,
                                                        DocumentException, IOException {
        //构造访问query_timestamp接口的URL串
        String strUrl = ALIPAY_GATEWAY_NEW + "service=query_timestamp&partner=" + partnerID;
        StringBuffer result = new StringBuffer();

        SAXReader reader = new SAXReader();
        Document doc = reader.read(new URL(strUrl).openStream());
        List<Node> nodeList = doc.selectNodes("//alipay/*");
        for (Node node : nodeList) {
            // 截取部分不需要解析的信息
            if (node.getName().equals("is_success") && node.getText().equals("T")) {
                // 判断是否有成功标示
                List<Node> nodeList1 = doc.selectNodes("//response/timestamp/*");
                for (Node node1 : nodeList1) {
                    result.append(node1.getText());
                }
            }
        }
        return result.toString();
    }
    
    /****
     * 生成请求ＡＰＩ的ＵＲＬ参数
     * @param paramMap
     * @return String
     * @throws UnsupportedEncodingException 
     */
	public static String buildQuery(Map<String, String> paramMap) throws UnsupportedEncodingException {
	    Map param = new HashMap();
		param.putAll(paramMap);
		StringBuffer request_str = new StringBuffer();
		Object[] paramKeys = param.keySet().toArray();
		Object[] paramKeys2 = new Object[paramKeys.length + 2];
		Arrays.sort(paramKeys);
		for (int i = 0; i < paramKeys.length; i++) {
			request_str.append(paramKeys[i] + "").append("=").append(param.get(paramKeys[i]) + "").append("&");
			paramKeys2[i] = paramKeys[i];
		}
		if (request_str.length() > 0) {
			request_str.deleteCharAt(request_str.length() - 1);
		}
		String sig = request_str.append(key).toString();
		sig = StringUtil.hash(sig);
		paramKeys2[paramKeys.length] = "sign";
		param.put("sign", sig);
		paramKeys2[paramKeys.length + 1] = "sign_type";
		param.put("sign_type", sign_type);
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < paramKeys2.length; i++) {
			strbuf.append((String)paramKeys2[i]).append("=");
			strbuf.append(URLEncoder.encode((String)param.get(paramKeys2[i]),"UTF-8")).append("&");
		}
		if (strbuf.length() > 0) {
			strbuf.deleteCharAt(strbuf.length() - 1);
		}
		return strbuf.toString();
	}
	
	

	public static AlipayWebSavingManager getInstance() {
    	if(m_self == null)
    	{
    		m_self = new AlipayWebSavingManager();
    	}
		return m_self;
	}
}
