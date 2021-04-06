package com.fy.boss.platform.tmall;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.tmall.TmallOrderQueryServlet;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class TmallOrderQueryServlet extends HttpServlet {
	public static TmallOrderQueryServlet self;

	//正式版
	//public static String PRIVATE_KEY = "b54d4b3e13cb682842cee60675bdc123";
	public static String PRIVATE_KEY = "f89b9f36338df851ba76502174b138a9";
	
	//public static String PRIVATE_KEY = "ecardintegrationtestecardintegra";
	
	//正式版
	//public static String COOPID = "1035598051";
	//新版
	//public static String COOPID = "3593650262";
	public static String COOPID = "1695470576";
	
	//public static String COOPID = "182589235";
	
	public static String SANGUO_URL = "http://b.sqgames.net/game_boss/tmOrderQuery";
				
	public static Map<String, Long> map = new HashMap<String,Long>();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		TmallOrderQueryServlet.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		res.setCharacterEncoding("utf-8");
		String coopId = ParamUtils.getParameter(req,"coopId", "");
		String tbOrderNo = ParamUtils.getParameter(req,"tbOrderNo", "");
		String sign = ParamUtils.getParameter(req,"sign", "");
		boolean isErr = false;
		
		//先验证合法性
		HashMap<String,String> pmap = new HashMap<String,String>();
		pmap.put("coopId", coopId);
		pmap.put("tbOrderNo", tbOrderNo);
		
		String mySign = sign(pmap, PRIVATE_KEY);
		
		String result = "REQUEST_FAILED";
		String failCode = "";
		String failReason = "";
		
		//淘宝要求 coopId不合法 则返回0103
		if(!COOPID.equals(coopId))
		{
			isErr = true;
			failCode = "0103";
			failReason = "CoopId not match.";
		}
		
		
		
		if(mySign.toLowerCase().equals(sign.toLowerCase()) && !isErr) {
			//查询订单
			String myOrderId = "TB-" + tbOrderNo;
			OrderForm order = OrderFormManager.getInstance().getOrderForm(myOrderId);
			if(order == null) {
				//查询三国
				String content = queryOrder(tbOrderNo);
				if(content.equals("NOT_EXIST")) {
					HashMap<String,String> returnMap = new HashMap<String,String>();
					returnMap.put("tbOrderNo", tbOrderNo);
					returnMap.put("coopOrderStatus", "REQUEST_FAILED");
					returnMap.put("failedCode", "0104");
					returnMap.put("failedReason", "No order found.");
					String xmlContent = xmlContent(returnMap);
					res.getWriter().write(xmlContent);
					PlatformSavingCenter.logger.warn("[天猫查询订单] [订单不存在] [自有订单:"+myOrderId+"] [淘宝订单:"+tbOrderNo+"] ["+req.getQueryString()+"] [return:"+xmlContent+"] ["+(System.currentTimeMillis()-start)+"ms]");
					return;
				} else {
					myOrderId = content;
				}
			} 
			HashMap<String,String> returnMap = new HashMap<String,String>();
			returnMap.put("tbOrderNo", tbOrderNo);
			returnMap.put("coopOrderNo", myOrderId);
			returnMap.put("coopOrderStatus", "SUCCESS");
			returnMap.put("coopOrderSnap", "Tmall saving success.");
			returnMap.put("coopOrderSuccessTime", DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
			String xmlContent = xmlContent(returnMap);
			res.getWriter().write(xmlContent);
			PlatformSavingCenter.logger.warn("[天猫查询订单] [成功] [自有订单:"+myOrderId+"] [淘宝订单:"+tbOrderNo+"] ["+req.getQueryString()+"] [return:"+xmlContent+"] ["+(System.currentTimeMillis()-start)+"ms]");
			return;
		} else if(!isErr) {
			//不合法
			result = "REQUEST_FAILED";
			failCode = "0102";
			failReason = "Sign code invalid";
			isErr = true;
		}
		HashMap<String,String> returnMap = new HashMap<String,String>();
		returnMap.put("tbOrderNo", tbOrderNo);
		returnMap.put("coopOrderStatus", result);
		returnMap.put("failedCode", failCode);
		returnMap.put("failedReason", failReason);
		String xmlContent = xmlContent(returnMap);
		res.getWriter().write(xmlContent);
		PlatformSavingCenter.logger.warn("[天猫查询订单] [失败:"+failReason+"] [sign:"+sign+"] [mySign:"+mySign+"] ["+req.getQueryString()+"] [return:"+xmlContent+"] ["+(System.currentTimeMillis()-start)+"ms]");
	}
	
	public static String sign(HashMap<String,String> params,String secretkey){
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+v);
		}
		String md5Str = sb.toString() + secretkey;
		
		String sign = StringUtil.hash(md5Str);
		
		return sign;
	}
	
	public static String xmlContent(HashMap<String,String> cmap) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><response>");
		String keys[] = cmap.keySet().toArray(new String[0]);
		for(String s : keys) {
			String v = cmap.get(s);
			sb.append("<" + s + ">" + v + "</" + s + ">");
		}
		sb.append("</response>");
		return sb.toString();
	}
	
	public String queryOrder(String tmOrderNo) {
		String url = SANGUO_URL + "?orderId=" + tmOrderNo;
		try {
			byte result[] = HttpUtils.webGet(new URL(url), new HashMap(), 30000, 30000);
			String content = new String(result, "utf-8").trim();
			return content;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOT_EXIST";
	}
}
