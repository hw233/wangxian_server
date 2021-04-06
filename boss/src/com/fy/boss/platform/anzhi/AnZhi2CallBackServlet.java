package com.fy.boss.platform.anzhi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.finance.service.platform.Base64;
import com.fy.boss.platform.feiliu.RsaMessage;
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class AnZhi2CallBackServlet extends HttpServlet {

	public static Map<String,String> payMap = new HashMap<String, String>();

	static 
	{
		payMap.put("001", "充值卡");
		payMap.put("002", "支付宝");
		payMap.put("003", "银联");
	}

	String appSecret = "ONIrX72Ne531T0fWOksJRWI8";

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
			{
		try
		{
			//只有成功才会回调
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");

			//接参数
			String appidParam = ParamUtils.getParameter(req,"appKey","");
			String liushuihaoParam = ParamUtils.getParameter(req,"orderId","");
			String moneyParam =  ParamUtils.getParameter(req,"amount","");
			String myOrderId = ParamUtils.getParameter(req,"ext","");

			String payResult = ParamUtils.getParameter(req,"payResult","");
			String verifystringParam =  ParamUtils.getParameter(req,"signStr","");
			String msg  = ParamUtils.getParameter(req,"msg","");
			String payType = payMap.get(ParamUtils.getParameter(req,"payType",""));


			String my_sign = "";

			if(msg != null)
			{
				msg = URLDecoder.decode(msg,"utf-8");
			}
			//验证签名
			//￼￼appKey=xxx&amount=xxx&orderId=xxx&payResult=xxx&signStr=xxx&msg=xxx&ext=xxx&payType=xxx
			//appKey+amount+orderId+payResult+ext+msg+appSecret
			my_sign = StringUtil.hash(appidParam+moneyParam+liushuihaoParam+payResult+myOrderId+msg+appSecret);

			boolean isSign = false;

			isSign = my_sign.equalsIgnoreCase(verifystringParam);


			if(isSign)
			{
				myOrderId = new String(Base64.decode(myOrderId));


				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(myOrderId);
				if(orderForm!= null)
				{
					Passport p = PassportManager.getInstance().getPassport(orderForm.getPassportId());
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {

							PlatformSavingCenter.logger.error("[充值回调] [充值平台：安智2] [失败:此订单已经回调过了] [安智2 orderId:"+liushuihaoParam+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+(p == null? "":p.getUserName() )+"] [充值金额:"+moneyParam+"] [appId:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [payResult:"+payResult+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [payType:"+payType+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
							res.getWriter().write("success");
							return;
						}
					}
					//更新订单
					try {
						orderForm.setResponseTime(System.currentTimeMillis());
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						if("200".equalsIgnoreCase(payResult))
							orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						else
							orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						orderForm.setResponseDesp(msg);
						//以返回的充值额度为准
						//宝币确保是1宝币等于1元人民币 并保证不会出现打折的问题 如果是出现了打折问题 则需要找宝币的人
						orderForm.setPayMoney((long)(Double.valueOf(moneyParam)*100));
						orderForm.setChannelOrderId(liushuihaoParam);
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：安智2] [充值"+("200".equalsIgnoreCase(payResult) ? "成功":"失败")+"] [安智2 orderId:"+liushuihaoParam+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+(p == null? "":p.getUserName() )+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [payResult:"+payResult+"] [appId:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [payResult:"+payResult+"] [payType:"+payType+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：安智2] [失败:更新订单信息出错] [安智2 orderId:"+liushuihaoParam+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+(p == null? "":p.getUserName() )+"] [充值金额:"+moneyParam+"] [payResult:"+payResult+"] [appId:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [payResult:"+payResult+"] [ [订单中充值金额:"+orderForm.getPayMoney()+"] [payType:"+payType+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
						res.getWriter().write("success");
						return;
					}

					res.getWriter().write("success");
					return;
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：安智2] [失败:数据库中无此订单] [安智2 orderId:"+liushuihaoParam+"] [orderId:"+myOrderId+"] [充值金额:"+moneyParam+"] [账号:--] [payResult:"+payResult+"] [appId:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [payResult:"+payResult+"] [payType:"+payType+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					res.getWriter().write("success");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：安智2] [失败:签名验证失败] [安智2 orderId:"+liushuihaoParam+"] [payResult:"+payResult+"] [appId:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [payResult:"+payResult+"] [orderId:"+myOrderId+"] [充值金额:"+moneyParam+"] [账号:--] [待解密字符串:"+ParamUtils.getParameter(req, "signStr", "")+"] [签名后字符串:"+my_sign+"] ["+appidParam+moneyParam+liushuihaoParam+payResult+myOrderId+msg+appSecret+"] [payType:"+payType+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				res.getWriter().write("success");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：安智2] [失败:出现异常] [安智2 orderId:--] [cid:--] [payResult:--] [appId:--] [appidParam:--] [money:--] [myOrderid:--] [payResult:--] [orderId:--] [充值金额:--] [账号:--]",e);
			res.getWriter().write("success");
			return;
		}
			}


}
