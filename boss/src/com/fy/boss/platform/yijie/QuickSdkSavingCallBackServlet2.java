package com.fy.boss.platform.yijie;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class QuickSdkSavingCallBackServlet2 extends HttpServlet {

	//更新相关的订单状态
	
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		try{
			req.setCharacterEncoding("utf-8");
			
			String nt_data = req.getParameter("nt_data");
			String sign = req.getParameter("sign");
			String md5Sign = req.getParameter("md5Sign");
			
			//Md5_Key：7xx9ofrajcynwoyquayf3dm5fcfycwpj
			String mySign = StringUtil.hash(nt_data+sign+"mcbeqwcmju3yzn1fm6tcfdhup0tkyvwk");
			String orderId = "";
			String order_no = "";
			String amount = "";
			String orderStatus = "";
			String uid = "";
			if(md5Sign.equalsIgnoreCase(mySign)){
				///Callback_Key：11850877107969813561386500413964
				String xmlContent = "";
				try {
					xmlContent = QuickSDKUtil.decode(nt_data,"68544299377226746902817666794961");
					Document dom = XmlUtil.loadString(xmlContent);
//					Element root = dom.getDocumentElement();
					Element root2 = (Element)dom.getFirstChild();
					Element root = (Element)root2.getFirstChild();

					orderId = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "game_order"), "", null);
					order_no = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "order_no"), "", null);
					amount = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "amount"), "", null);
					orderStatus = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "status"), "", null);
					uid = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "channel_uid"), "", null);
//					System.out.println(root2.getTagName()+"/"+root.getTagName());
				} catch (Exception e) {
					res.getWriter().write("decodeXMLerror");
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：quick游戏2] [失败：解析xml文件出错] [xmlContent:"+xmlContent+"] [nt_data:"+nt_data+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					return;
				}
				
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(orderId);
				if(orderForm != null){
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							res.getWriter().write("this is order has callBack");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2("[外部订单号:"+order_no+"] [交易金额:"+amount+"] [sign:"+sign+"]");
					orderForm.setResponseDesp("quick游戏2回调充值接口成功");
					if((long)(Double.valueOf(amount)*100) != orderForm.getPayMoney())
					{
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：quick游戏2] [警告：传回的充值金额和订单存储的金额不一致] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [传入实际金额:"+amount+"元] [订单中原金额:"+orderForm.getPayMoney()+"fen] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					}
					if("0".equals(orderStatus)) {
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						orderForm.setPayMoney((long)(Double.valueOf(amount) * 100));
						orderForm.setChannelOrderId(order_no);
					}else{
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						orderForm.setResponseDesp("quick游戏2交易失败");
						orderForm.setChannelOrderId(order_no);
					}
					try {
						orderFormManager.update(orderForm);
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：quick游戏2] [成功] [充值"+("0".equals(orderStatus) ? "成功" : "失败")+"] [传入quick游戏2ORDERID:"+order_no+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [更新入订单的金额:"+Math.round(Double.valueOf(amount))+"] [订单中原金额:"+orderForm.getPayMoney()+"] [quick游戏2用户:"+uid+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：quick游戏2] [失败：更新订单出错] [传入quick游戏2ORDERID:"+order_no+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [更新入订单的金额:"+Math.round(Double.valueOf(amount))+"] [订单中原金额:"+orderForm.getPayMoney()+"] [quick游戏2用户:"+uid+"] [返回结果:"+orderStatus+"] [错误信息:"+orderStatus+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					}
				}else{
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：quick游戏2] [失败：查找订单失败] [orderId:"+orderId+"] [QuickSDK唯一订单号:"+order_no+"] [xmlContent:"+xmlContent+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
			}else{
				res.getWriter().write("SignError");
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：quick游戏2] [失败：签名验证失败] [nt_data:"+nt_data+"] [sign:"+sign+"] [md5Sign:"+md5Sign+"] [my sign:"+mySign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			}
			return;
		}catch(Exception e){
			res.getWriter().write("callBackError");
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：quick游戏2] [失败：出现异常] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return;
		}
	}
	
}
