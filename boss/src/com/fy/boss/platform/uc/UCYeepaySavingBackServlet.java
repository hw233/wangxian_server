package com.fy.boss.platform.uc;

import java.io.IOException;
import java.util.ArrayList;
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
import com.fy.boss.finance.service.platform.YeepayDigestUtil;
import com.fy.boss.platform.uc.UCYeepaySavingManager;

public class UCYeepaySavingBackServlet extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		long now = System.currentTimeMillis();
		
		String privateKey = UCYeepaySavingManager.getInstance().getPrivateKey();
		
		// 业务类型
		String r0_Cmd = formatString(req.getParameter("r0_Cmd"));
		// 支付结果
		String r1_Code = formatString(req.getParameter("r1_Code"));
		int payResult = Integer.parseInt(r1_Code);
		// 商户编号
		String p1_MerId = formatString(req.getParameter("p1_MerId"));
		// 商户订单号
		String p2_Order = formatString(req.getParameter("p2_Order"));
		String orderId = p2_Order;
		// 成功金额
		String p3_Amt = formatString(req.getParameter("p3_Amt"));
		int payMoney = new Float(Float.parseFloat(p3_Amt)*100).intValue();
		// 支付方式
		String p4_FrpId = formatString(req.getParameter("p4_FrpId"));
		// 卡序列号组
		String p5_CardNo = formatString(req.getParameter("p5_CardNo"));
		// 确认金额组
		String p6_confirmAmount = formatString(req.getParameter("p6_confirmAmount"));
		// 实际金额组
		String p7_realAmount = formatString(req.getParameter("p7_realAmount"));
		// 卡状态组
		String p8_cardStatus = formatString(req.getParameter("p8_cardStatus"));
		// 扩展信息
		String p9_MP = formatString(req.getParameter("p9_MP"));
		// 支付余额 注：此项仅为订单成功,并且需要订单较验时才会有值。失败订单的余额返部返回原卡密中
		String pb_BalanceAmt = formatString(req.getParameter("pb_BalanceAmt"));
		// 余额卡号  注：此项仅为订单成功,并且需要订单较验时才会有值
		String pc_BalanceAct = formatString(req.getParameter("pc_BalanceAct"));
		// 签名数据
		String hmac	= formatString(req.getParameter("hmac"));
		
		String newHmac = YeepayDigestUtil.getHmac(new String[] {  r0_Cmd, 
			   	r1_Code,
			   	p1_MerId,
			   	p2_Order,
			   	p3_Amt,
			   	p4_FrpId,
			   	p5_CardNo,
			   	p6_confirmAmount,
			   	p7_realAmount,
			   	p8_cardStatus,
			   	p9_MP,
			   	pb_BalanceAmt,
			   	pc_BalanceAct}, privateKey);
		
		if (!hmac.equals(newHmac)) {
			PlatformSavingCenter.logger.info("[充值回调] [充值平台:UC] [MD5验证出错] [支付平台:易宝] [介质:----] [用户:----] [游戏:----] [支付结果:"+(payResult==1?"成功":"失败")+"] [订单:"+orderId+"] [充值额:"+payMoney+"分]");
		} else {
    		PassportManager pmanager = PassportManager.getInstance();
			OrderFormManager fmanager = OrderFormManager.getInstance();
			OrderForm order = fmanager.getOrderForm(p2_Order);
			if(order == null) {
				PlatformSavingCenter.logger.info("[充值回调] [充值平台:UC] [订单不存在] [支付平台:易宝] [介质:----] [用户:----] [游戏:----] [支付结果:"+(payResult==1?"成功":"失败")+"] [订单:"+p2_Order+"] [充值额:"+payMoney+"分]");
				res.getWriter().write("success");
				return;
			}
    		Passport passport = pmanager.getPassport(order.getPassportId());
			if(order != null) {
				//只有-1才是等待返回状态
				if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
					PlatformSavingCenter.logger.info("[充值回调] [充值平台:UC] [错误的返回:此订单已经返回过了] [支付平台:易宝] [介质:] [用户:"+passport.getUserName()+"] [支付结果:"+r1_Code+"] [订单金额:"+order.getPayMoney()+"分] [订单:"+p2_Order+"] [充值额:"+payMoney+"分]");
					res.getWriter().write("success");
					return;
				}
				//必须在完成更新订单后才能进行兑换操作，否则可能造成重复的兑换
				String resultdesp = p8_cardStatus + ":" + UCYeepaySavingManager.yeepayPayStatusDespMap.get(p8_cardStatus);
				try {
					List<String> willUpdateFieldNames = new ArrayList<String>();
					if(payResult == 1) {
						if(order.getPayMoney() != payMoney) {
							//成功金额与充值金额不一致，以成功金额为准
							order.setResponseDesp("返回充值成功，输入面额：" + order.getPayMoney() + ", 实际面额:" + payMoney);
							willUpdateFieldNames.add("responseDesp");
							order.setPayMoney((long)payMoney);
							willUpdateFieldNames.add("payMoney");
						} else {
							order.setResponseDesp("返回充值成功");
							willUpdateFieldNames.add("responseDesp");
						}
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
					PlatformSavingCenter.logger.error("[充值回调] [充值平台:UC] [更新订单发生异常,请手动进行兑换并更新订单] [支付平台:易宝] [介质:] [用户:"+passport.getUserName()+"] [支付结果:"+(payResult==1?"成功":"失败")+"] [订单:"+orderId+"] [订单金额:"+order.getPayMoney()+"分] [充值额:"+payMoney+"分]", e);
					return;
				}
		    	PlatformSavingCenter.logger.info("[充值回调] [充值平台:UC] [支付平台:易宝] [介质:] [用户:"+passport.getUserName()+"] [支付结果:"+(payResult==1?"成功":"失败")+"] ["+resultdesp+"] [订单:"+orderId+"] [订单金额:"+order.getPayMoney()+"分] [充值额:"+payMoney+"分]");
			} else {
				PlatformSavingCenter.logger.info("[充值回调] [充值平台:UC] [订单找不到] [支付平台:易宝] [介质:----] [用户:----] [游戏:----] [支付结果:"+(payResult==1?"成功":"失败")+"] [订单:"+orderId+"] [订单金额:"+order.getPayMoney()+"分] [充值额:"+payMoney+"分]");
			}
		}
		res.getWriter().write("success");
	}
	
	private String formatString(String text){ 
		if(text == null) {
			return ""; 
		}
		return text;
	}
}
