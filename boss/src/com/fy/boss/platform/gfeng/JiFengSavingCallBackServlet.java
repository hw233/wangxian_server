package com.fy.boss.platform.gfeng;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
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
import org.w3c.dom.Node;

import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class JiFengSavingCallBackServlet extends HttpServlet {

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		String appkey = "1839238243";
		String uid = "4857133";
		try
		{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
			String str = FileUtils.getContent(req.getInputStream());
			if(str == null)
			{
				str = "";
				BufferedInputStream bf = new BufferedInputStream(req.getInputStream());
				BufferedReader bfr = new BufferedReader(new InputStreamReader(bf));
				String line = null;
				while(( line = bfr.readLine() )!= null)
				{
					str += line;
				}

				try
				{
					bfr.close();
					bf.close();
				}
				catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：机锋] [关闭流时出现异常] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
			}

			String sign = req.getParameter("sign");
			String time = req.getParameter("time");
			
			StringBuffer returnMessage = new StringBuffer();
			//returnMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			String errorResult = "<response><ErrorCode>0</ErrorCode><ErrorDesc>Success</ErrorDesc></response>";
			String successResult = "<response><ErrorCode>1</ErrorCode><ErrorDesc>Success</ErrorDesc></response>";
			
			
			
			Document dom = null;
			try {
				dom = XmlUtil.loadString(str);
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[机锋充值接口回调] [失败] [解析响应内容信息] [失败] [待解析的字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				returnMessage.append(0);
				res.getWriter().write(returnMessage.toString());
				return;
			}
			Element root = dom.getDocumentElement();
			
			/**
			 * < response > <order>
< order_id >00002010090922</order_id > < cost >123456</ cost >
< appkey >123456</ appkey >
< create_time >123456</ create_time >
</order>
￼</response>
			 */
			Element orderNode = (Element)dom.getFirstChild();
			String order_id = XmlUtil.getValueAsString(XmlUtil.getChildByName(orderNode, "order_id"), "", null);
			String my_sign = StringUtil.hash((uid+time).getBytes());
			String cost = XmlUtil.getValueAsString(XmlUtil.getChildByName(orderNode, "cost"), "", null);
			long money = Long.valueOf(cost) * 10;

			//去掉sign标记 得到所有参数
			//验证sign
			if(my_sign.equalsIgnoreCase(sign))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(order_id);

				//通过orderId查订单
				if(orderForm != null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：机锋] [失败：订单已经回调过了] [交易流水号:--] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:--] [计费点编号:--] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+cost+"] [交易时间:"+time+"] [返回字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write(successResult);
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setResponseDesp("机锋回调充值接口成功");
					if(Long.valueOf(money) != orderForm.getPayMoney())
					{
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：机锋] [警告：传回的充值金额和订单存储的金额不一致] [交易流水号:--] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:--] [计费点编号: --] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+cost+"] [交易时间:"+time+"] [返回字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					orderForm.setPayMoney(Long.valueOf(money));
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：机锋] [成功] [用户充值] [成功] [交易流水号:--] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:--] [计费点编号:--] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+cost+"] [交易时间:"+time+"] [返回字符串:"+str+"] [充值结果:成功] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：机锋] [失败：更新订单出错] [交易流水号:--] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:--] [计费点编号:--] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+cost+"] [交易时间:"+time+"] [返回字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write(successResult);
						return;
					}

					res.getWriter().write(successResult);
					return;
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：机锋] [失败：无参数指定订单] [交易流水号:--] [充值类型:--] [计费点编号:--] [交易金额:"+money+"] [购买数量:"+cost+"] [交易时间:"+time+"] [返回字符串:"+str+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write(successResult);
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：机锋] [失败：签名验证失败] [交易流水号:--] [充值类型:--] [计费点编号:--] [交易金额:"+money+"] [购买数量:"+cost+"] [交易时间:"+time+"] [传入字符串:"+str+"] [uid:"+uid+"] [加密前:"+(uid+time)+"] [sign:"+sign+"] [my sign:"+my_sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write(errorResult);
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：机锋] [失败：出现未知错误] [交易流水号:--] [充值类型:--] [计费点编号:--] [交易金额:--] [购买数量:--] [交易时间:--] [传入字符串:--] [uid:"+uid+"]",e);
			return;
		}

	}

}
