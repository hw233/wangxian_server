package com.fy.boss.platform._3G;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.feiliu.RsaMessage;
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class _3GSavingCallBackServlet extends HttpServlet {
	public static String md5key = "2324wangxianol";

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
			{
		try
		{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");

			String str = req.getParameter("data");
			if(str == null)
			{
				PlatformSavingCenter.logger.error("[2324充值接口回调] [失败] [未返回回调充值数据]");
				res.getWriter().write("ok");
				return;
			}

			Document dom = null;
			try {
				dom = XmlUtil.loadString(str);
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[2324充值接口回调] [失败] [解析响应内容信息] [失败] [待解析的字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				res.getWriter().write("ok");
				return;
			}
			Element root = dom.getDocumentElement();

			String _2324OrderId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "orderid"), "", null);
			String orderId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "cporderid"), "", null);
			String gameId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "gameid"), "", null);
			String cpId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "cpid"), "", null);
			String userId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "token"), "", null);
			String amount = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "paytotalfee"), "", null);
			String ret = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "access"), "", null);
			String payTypeId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "paytypeid"), "", null);
			String stime = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "stime"), "", null);
			String verifyString = req.getParameter("key");

			//去掉sign标记 得到所有参数
			//验证sign
			String mingwen = md5key + "_"+_2324OrderId+"_"+gameId+"_"+userId;


			String miwen = null;
			miwen = StringUtil.hash(mingwen);

			//验证签名
			if(miwen.equalsIgnoreCase(verifyString))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				//如果cpOrderId没有传过来，则通过cardNo和充值类型查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(orderId);
				if(orderForm!= null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：2324] [失败:此订单已经回调过了] [2324 orderId:"+_2324OrderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [充值用户:"+userId+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [充值类型:"+payTypeId+"] [str:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
							res.getWriter().write("ok");
							return;
						}
					}
					//更新订单
					try {
						orderForm.setResponseTime(System.currentTimeMillis());
						//以返回的充值额度为准
						orderForm.setPayMoney(Math.round(Double.valueOf(amount) * 100));
						if(ret.trim().equals("1")) {
							orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
							orderForm.setChannelOrderId(_2324OrderId);
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：2324] [充值成功] [2324 orderId:"+_2324OrderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [充值用户:"+userId+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [充值类型:"+payTypeId+"] [str:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						} else {
							orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
							orderForm.setResponseDesp("2324充值平台返回充值失败");
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：2324] [充值失败：2324充值平台返回充值失败] [2324 orderId:"+_2324OrderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [充值用户:"+userId+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [充值类型:"+payTypeId+"] [str:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						}
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：2324] [失败:更新订单信息出错] [2324 orderId:"+_2324OrderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [充值用户:"+userId+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [充值类型:"+payTypeId+"] [str:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
						res.getWriter().write("ok");
						return;
					}
					res.getWriter().write("ok");
					return;
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：2324] [失败:数据库中无此订单] [2324 orderId:"+_2324OrderId+"] [orderId:"+orderId+"] [充值用户:"+userId+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [充值类型:"+payTypeId+"] [str:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					res.getWriter().write("ok");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：2324] [失败:验证字符串失败] [2324 orderId:"+_2324OrderId+"] [orderId:"+orderId+"] [充值用户:"+userId+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [充值类型:"+payTypeId+"] [verifyString:"+verifyString+"] [my sign:"+miwen+"] [明文:"+mingwen+"] [str:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				res.getWriter().write("ok");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：2324] [失败] [出现未知异常] [2324 orderId:--] [orderId:--] [充值用户:--] [充值金额:--] [充值结果:--] [充值类型:--] [verifyString:--] [my sign:--] [明文:--] [str:--] [costs:--]",e);
		}
			}
}
