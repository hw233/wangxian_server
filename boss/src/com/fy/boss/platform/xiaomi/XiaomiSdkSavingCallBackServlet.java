package com.fy.boss.platform.xiaomi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.xiaomi.common.HmacSHA1Encryption;
import com.fy.boss.platform.xiaomi.common.ParamEntry;
import com.fy.boss.platform.xiaomi.common.ParamUtil;
import com.fy.boss.platform.xiaomi.vo.NotifyReceiverResponse;
import com.xuanzhi.tools.text.JsonUtil;

public class XiaomiSdkSavingCallBackServlet extends HttpServlet {

	//更新相关的订单状态
	
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		try{
//			String appKey = "512e79c0-a81e-c2af-38b7-505af006f12f";//正式
			String appKey = "5581773990782";//正式
			String AppSecret = "f6JGesLqklrDfsoZPKUn5A==";//正式
			//String appKey = "eb070b57-9b91-c2ee-224e-5087550f6c0e"; //测试
			req.setCharacterEncoding("utf-8");
			
			String appId = req.getParameter("appId");
			String orderId = req.getParameter("cpOrderId");
			String cpUserInfo = req.getParameter("cpUserInfo");
			String uid = req.getParameter("uid");
			String channelOrderId = req.getParameter("orderId");
			String orderStatus = req.getParameter("orderStatus");
			String payFee = req.getParameter("payFee");
		
			String productCode = req.getParameter("productCode");
			String productCount = req.getParameter("productCount");
			String productName = req.getParameter("productName");
			String payTime = req.getParameter("payTime");
			String sign = req.getParameter("signature");
	
			List<ParamEntry> paras = new ArrayList<ParamEntry>();
			paras.add(new ParamEntry("appId",appId));
			paras.add(new ParamEntry("cpOrderId",orderId));
			paras.add(new ParamEntry("orderId",channelOrderId));
			paras.add(new ParamEntry("uid",uid));
			paras.add(new ParamEntry("productCode",productCode));
			paras.add(new ParamEntry("productName",productName));
			paras.add(new ParamEntry("productCount",productCount));
			paras.add(new ParamEntry("payFee",payFee));
			paras.add(new ParamEntry("payTime",payTime));
			paras.add(new ParamEntry("orderStatus",orderStatus));
			paras.add(new ParamEntry("cpUserInfo",cpUserInfo));
			
			
			String returnString = ParamUtil.getSortQueryString(paras);
			String my_sign = HmacSHA1Encryption.hmacSHA1Encrypt(returnString,AppSecret);
			int errcode = 200;
			//去掉sign标记 得到所有参数
			//验证sign
			if(my_sign.equalsIgnoreCase(sign))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(orderId);
				
					//通过orderId查订单
				if(orderForm != null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：小米游戏] [失败：订单已经回调过了] [XiaomiSDK订单id:"+channelOrderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [交易金额:"+payFee+"] [实际金额:"+payFee+"] [订单中原金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							errcode = 1005;
							NotifyReceiverResponse result = new NotifyReceiverResponse();
							result.setErrcode(errcode);
							result.setErrMsg("此订单已经回调过了");
							String json = JsonUtil.jsonFromObject(result);
							res.getWriter().write(json);
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2("[外部订单号:"+channelOrderId+"] [交易金额:"+payFee+"] [交易结果:"+orderStatus+"] [sign:"+sign+"]");
					orderForm.setResponseDesp("小米游戏回调充值接口成功");
					if(Long.parseLong(payFee) != orderForm.getPayMoney())
					{
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：小米游戏] [警告：传回的充值金额和订单存储的金额不一致] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+payFee+"] [传入实际金额:"+payFee+"] [订单中原金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					if("TRADE_SUCCESS".equals(orderStatus)) {
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						orderForm.setPayMoney(Long.parseLong(payFee));
						orderForm.setChannelOrderId(channelOrderId);
					}
					else
					{
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						orderForm.setResponseDesp("小米游戏交易失败");
						orderForm.setChannelOrderId(channelOrderId);
					}
					try {
						orderFormManager.update(orderForm);
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：小米游戏] [成功] [充值"+("TRADE_SUCCESS".equals(orderStatus) ? "成功" : "失败")+"] [传入小米游戏ORDERID:"+channelOrderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+payFee+"] [扣完手续费传入实际金额:"+payFee+"] [更新入订单的金额:"+Math.round(Double.valueOf(payFee))+"] [订单中原金额:"+orderForm.getPayMoney()+"] [小米游戏用户:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					} catch (Exception e) {
						errcode = 1001;
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：小米游戏] [失败：更新订单出错] [传入小米游戏ORDERID:"+channelOrderId+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值金额:"+payFee+"] [扣完手续费传入实际金额:"+payFee+"] [更新入订单的金额:"+Math.round(Double.valueOf(payFee))+"] [订单中原金额:"+orderForm.getPayMoney()+"] [小米游戏用户:"+uid+"] [返回结果:"+orderStatus+"] [错误信息:"+orderStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					}
				}
				else
				{
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：小米游戏] [失败] [查找订单失败] [传入小米游戏ORDERID:"+channelOrderId+"] [传入orderId:"+orderId+"] [充值金额:"+payFee+"] [扣完手续费传入实际金额:"+payFee+"] [更新入订单的金额:"+payFee+"] [小米游戏用户:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：小米游戏] [失败：签名验证失败] [appkey:"+appKey+"] [准备用于签名的字符串:"+returnString+"] [传入的sign:"+sign+"] [my sign:"+my_sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			}
			
			NotifyReceiverResponse result = new NotifyReceiverResponse();
			result.setErrcode(errcode);
			result.setErrMsg("");
			String json = JsonUtil.jsonFromObject(result);
			res.getWriter().write(json);
			return;
		}catch(Exception e){
			
			NotifyReceiverResponse result = new NotifyReceiverResponse();
			result.setErrcode(1001);
			result.setErrMsg("");
			String json = "";
			try {
				json = JsonUtil.jsonFromObject(result);
			} catch (Exception e1) {
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：小米游戏] [失败：出现异常] [准备用于签名的字符串:--] [传入的sign:--] [my sign:--] [异常:"+e+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
			}
			res.getWriter().write(json);
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：小米游戏] [失败：出现异常] [准备用于签名的字符串:--] [传入的sign:--] [my sign:--] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return;
		}
	}
	
}
