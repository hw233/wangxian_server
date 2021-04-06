package com.fy.boss.platform.feiliu;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
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
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.feiliu.RsaMessage;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class FeiLiuCallBackServlet extends HttpServlet {
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		StringBuffer returnMessage = new StringBuffer();
		returnMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		returnMessage.append("<Response>");
		returnMessage.append("<Ret>");
		String str = FileUtils.getContent(req.getInputStream());
		Document dom = null;
		try {
			dom = XmlUtil.loadString(str);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[飞流充值接口回调] [失败] [解析响应内容信息] [失败] [待解析的字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			returnMessage.append(0);
			returnMessage.append("</Ret>");
			returnMessage.append("</Response>");
			res.getWriter().write(returnMessage.toString());
			return;
		}
		Element root = dom.getDocumentElement();
		
		String flOrderId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "FLOrderId"), "", null);
		String orderId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "OrderId"), "", null);
		String productId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "ProductId"), "", null);
		String cardNO = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "CardNO"), "", null);
		String amount = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "Amount"), "", null);
		String ret = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "Ret"), "", null);
		String cardStatus = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "CardStatus"), "", null);
		String merPriv = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "MerPriv"), "", null);
		String verifyString = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "VerifyString"), "", null);
		

		//去掉sign标记 得到所有参数
		//验证sign
		String mingwen = flOrderId+"|"+orderId+"|"+productId+"|"+cardNO+"|"+amount+"|"+ret+"|"+cardStatus+"|"+merPriv;
		RsaMessage rm = new RsaMessage();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL urlPath = null; 
		if(classLoader != null) {
			urlPath = classLoader.getResource("feiliu_SignKey.pub");
		}
		RSAPublicKey publicKey = RsaMessage.initPublicKey(urlPath.getPath());

		String miwen = null;
		try {
			miwen = rm.decrypt(verifyString, publicKey);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：飞流] [失败 :生成加密信息失败] [签名字符串:"+verifyString+"] [明文:"+mingwen+"] [本地生成密文:"+miwen+"] [飞流 orderId:"+flOrderId+"] [orderId:"+orderId+"] [cardStatus:"+cardStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			returnMessage.append(0);
			returnMessage.append("</Ret>");
			returnMessage.append("</Response>");
			res.getWriter().write(returnMessage.toString());
			return;
		}
		
		//验证签名
		if(miwen.equalsIgnoreCase(mingwen))
		{
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
				//如果cpOrderId没有传过来，则通过cardNo和充值类型查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(orderId);
				if(orderForm!= null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：飞流] [失败:此订单已经回调过了] [飞流 orderId:"+flOrderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [充值卡号:"+cardNO+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [cardStatus:"+cardStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
							returnMessage.append(1);
							returnMessage.append("</Ret>");
							returnMessage.append("</Response>");
							res.getWriter().write(returnMessage.toString());
							return;
						}
					}
						//更新订单
					try {
						orderForm.setResponseTime(System.currentTimeMillis());
						//以返回的充值额度为准
						orderForm.setPayMoney(Long.valueOf(amount));
						if(ret.trim().equals("1")) {
							orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：飞流] [充值成功] [飞流 orderId:"+flOrderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [充值卡号:"+cardNO+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [cardStatus:"+cardStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						} else {
							orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
							orderForm.setResponseDesp("飞流充值平台返回充值失败");
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：飞流] [充值失败：飞流充值平台返回充值失败] [飞流 orderId:"+flOrderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [充值卡号:"+cardNO+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [cardStatus:"+cardStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						}
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：飞流] [失败:更新订单信息出错] [飞流 orderId:"+flOrderId+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [充值卡号:"+cardNO+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [cardStatus:"+cardStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
						returnMessage.append(1);
						returnMessage.append("</Ret>");
						returnMessage.append("</Response>");
						res.getWriter().write(returnMessage.toString());
						return;
					}
					returnMessage.append(1);
					returnMessage.append("</Ret>");
					returnMessage.append("</Response>");
					res.getWriter().write(returnMessage.toString());
					return;
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：飞流] [失败:数据库中无此订单] [飞流 orderId:"+flOrderId+"] [orderId:"+orderId+"] [充值卡号:"+cardNO+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [cardStatus:"+cardStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					returnMessage.append(1);
					returnMessage.append("</Ret>");
					returnMessage.append("</Response>");
					res.getWriter().write(returnMessage.toString());
					return;
				}
		}
		else
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：飞流] [失败:验证字符串失败] [飞流 orderId:"+flOrderId+"] [orderId:"+orderId+"] [充值卡号:"+cardNO+"] [充值金额:"+amount+"] [充值结果:"+ret+"] [cardStatus:"+cardStatus+"] [feiliu verifyString:"+verifyString+"] [my sign:"+miwen+"] [明文:"+mingwen+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			returnMessage.append(1);
			returnMessage.append("</Ret>");
			returnMessage.append("</Response>");
			res.getWriter().write(returnMessage.toString());
			return;
		}
	}
	
}
