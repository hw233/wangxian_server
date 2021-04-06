package com.fy.boss.finance.service.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.finance.service.platform.GameServerSzfSavingManager;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class GameServerSzfExchangeCallBackServlet extends HttpServlet {
	
	public static final int SZF_CARD_SZX = 0;
	public static final int SZF_CARD_LT = 1;
	public static final int SZF_CARD_DX = 2;
	
	public static final String SZF_MEDIUM_NAME[] = new String[]{"移动充值卡", "联通一卡付", "电信充值卡"};
	
	public String getSzfMediumName(int type) {
		try {
			return SZF_MEDIUM_NAME[type];
		} catch(Exception e) {
			return "未知介质";
		}
	}

	private static HashMap<String, String> szfResultDespMap = new HashMap<String, String>();
	
	static {
		szfResultDespMap.put("200", "支付成功");
		szfResultDespMap.put("201", "您输入的充值卡密码错误");
		szfResultDespMap.put("202", "您输入的充值卡已被使用");
		szfResultDespMap.put("203", "您输入的充值卡密码非法");
		szfResultDespMap.put("204", "您输入的卡号或密码错误次数过多");
		szfResultDespMap.put("205", "卡号密码正则不匹配或者被禁止");
		szfResultDespMap.put("206", "本卡之前被提交过，本次订单失败，不再继续处理 ");
		szfResultDespMap.put("207", "暂不支持该充值卡的支付");
		szfResultDespMap.put("208", "您输入的充值卡卡号错误");
		szfResultDespMap.put("209", "您输入的充值卡未激活（生成卡）");
		szfResultDespMap.put("210", "您输入的充值卡已经作废（能查到有该卡，但是没卡 的信息）");
		szfResultDespMap.put("211", "您输入的充值卡已过期");
		szfResultDespMap.put("212", "您选择的卡面额不正确 ");
		szfResultDespMap.put("213", "该卡为特殊本地业务卡，系统不支持");
		szfResultDespMap.put("214", "该卡为增值业务卡，系统不支持");
		szfResultDespMap.put("215", "新生卡");
		szfResultDespMap.put("216", "系统维护");
		szfResultDespMap.put("217", "接口维护");
		szfResultDespMap.put("218", "运营商系统维护");
		szfResultDespMap.put("219", "系统忙，请稍后再试");
		szfResultDespMap.put("220", "未知错误");
		szfResultDespMap.put("221", "本卡之前被处理完毕，本次订单失败，不再继续处理");
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		long now = System.currentTimeMillis();
		
		String content = req.getQueryString();
		String privateKey = GameServerSzfSavingManager.getInstance().getPrivateKey();
		String version = ParamUtils.getParameter(req, "version", "");
		String merId = ParamUtils.getParameter(req, "merId", "");
		int payMoney = ParamUtils.getIntParameter(req, "payMoney", 0);
		int cardMoney = ParamUtils.getIntParameter(req, "cardMoney", 0);
		String orderId = ParamUtils.getParameter(req, "orderId", "");
		//1：成功 0：失败
		int payResult = ParamUtils.getIntParameter(req, "payResult", 0);
		String privateField = ParamUtils.getParameter(req, "privateField", "");
		String payDetails = ParamUtils.getParameter(req, "payDetails", "");
		String md5String = ParamUtils.getParameter(req, "md5String", "");
		
		String errorCode = ParamUtils.getParameter(req, "errcode","");
		
		//全面额支付采用这种拼装方式，否则不用加|间隔!!!!!!!!!!!
		String md5pre = version+"|"+merId+"|"+payMoney+"|"+cardMoney+"|"+orderId+"|"+payResult+"|"+privateField+"|"+payDetails+"|"+privateKey;
		String objMd5 = StringUtil.hash(md5pre);
		
		if(objMd5.equals(md5String)) {
    		PassportManager pmanager = PassportManager.getInstance();
			OrderFormManager fmanager = OrderFormManager.getInstance();
			OrderForm order = fmanager.getOrderForm(orderId);
			if(order != null) {
				Passport passport = pmanager.getPassport(order.getPassportId());
				//只有-1才是等待返回状态
				if(order.getResponseResult() != -1) {
					PlatformSavingCenter.logger.info("[充值回调] [支付平台:神州付] [本订单已经返回过了：返回码："+order.getResponseResult()+", 返回描述:"+order.getResponseDesp()+"] [支付平台:神州付] [介质:] [用户:"+passport.getUserName()+"] [支付结果:"+(payResult==1?"成功":"失败")+"] [订单:"+orderId+"] [充值额:"+payMoney+"分] [实际扣款:"+cardMoney+"分]");
					res.getWriter().write(orderId);
					return;
				}
				//必须在完成更新订单后才能进行兑换操作，否则可能造成重复的兑换
				String resultdesp = szfResultDespMap.get(errorCode);
				try {
					List<String> willUpdateFieldNames = new ArrayList<String>();
					if(payResult == 1) {
						if(cardMoney != order.getPayMoney()) {
							//扣除的是卡实际面额
							order.setResponseDesp("返回充值成功，输入面额：" + order.getPayMoney() + ", 实际面额:" + cardMoney);
							willUpdateFieldNames.add("responseDesp");
							order.setPayMoney((long)cardMoney);
							willUpdateFieldNames.add("payMoney");
						} else {
							order.setResponseDesp("返回充值成功");
							willUpdateFieldNames.add("responseDesp");
						}
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setResponseResult(OrderForm.RESPONSE_SUCC);
						willUpdateFieldNames.add("responseResult");
					} else {
						order.setResponseDesp(resultdesp);
						order.setResponseResult(OrderForm.RESPONSE_FAILED);
						willUpdateFieldNames.add("responseResult");
						willUpdateFieldNames.add("responseDesp");
		    			//增加坏充值的个数
		    			PlatformSavingCenter.getInstance().addBadSaving(passport.getId());
					}
					order.setResponseTime(now);
					willUpdateFieldNames.add("responseTime");
					fmanager.batch_updateField(order, willUpdateFieldNames);
				} catch(Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [支付平台:神州付] [更新订单发生异常,请手动进行兑换并更新订单] [支付平台:神州付] [介质:] [用户:"+passport.getUserName()+"] [支付结果:"+(payResult==1?"成功":"失败")+"] [订单:"+orderId+"] [充值额:"+payMoney+"分] [实际扣款:"+cardMoney+"分]", e);
					return;
				}
				PlatformSavingCenter.logger.info("[充值回调] [支付平台:神州付] [介质:] [用户:"+passport.getUserName()+"] [支付结果:"+(payResult==1?"成功":"失败")+"] ["+resultdesp+"] [返回内容:"+content+"] [订单:"+orderId+"] [充值额:"+payMoney+"分] [实际扣款:"+cardMoney+"分] [errorCode:"+errorCode+"] ["+(System.currentTimeMillis()-now)+"]");
			} else {
				PlatformSavingCenter.logger.info("[充值回调] [支付平台:神州付] [订单找不到] [支付平台:----] [介质:----] [用户:----] [游戏:----] [支付结果:"+(payResult==1?"成功":"失败")+"] [订单:"+orderId+"] [充值额:"+payMoney+"分] [实际扣款:"+cardMoney+"分]");
			}
		} else {
			PlatformSavingCenter.logger.info("[充值返回] [支付平台:神州付] [MD5验证出错] [md5pre:"+md5pre+"] [validMd5:"+objMd5+"] [input:"+md5String+"] [支付平台:神州付] [介质:----] [用户:----] [游戏:----] [支付结果:"+(payResult==1?"成功":"失败")+"] [订单:"+orderId+"] [充值额:"+payMoney+"分] [实际扣款:"+cardMoney+"分]");
		}
		res.getWriter().write(orderId);
	}
}
