package com.fy.boss.finance.service.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.internal.util.AlipaySignature;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;

public class AlipayAppSavingBackServlet  extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
    public static String getSignCheckContentV2(Map<String, String> params) {
        if (params == null) {
            return null;
        }

       // params.remove("sign");
       // params.remove("sign_type");

        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }

        return content.toString();
    }
    
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		long now = System.currentTimeMillis();
		String charset = req.getParameter("charset");
		String trade_no = req.getParameter("trade_no");
		String out_trade_no = req.getParameter("out_trade_no");
		String trade_status = req.getParameter("trade_status");
		String total_amount = req.getParameter("total_amount");
		
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = req.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) requestParams.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]
		                    : valueStr + values[i] + ",";
		  	}
		    //乱码解决，这段代码在出现乱码时使用。
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		//切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
		boolean verified = false;
		
		String content = "";
		
        //使用支付宝公钥验签名
        try {
        	content = getSignCheckContentV2(params);
            verified = AlipaySignature.rsaCheckV1(params, AlipaySavingManager.aliPublicKey, charset,"RSA2");
        } catch (Exception e) {
            PlatformSavingCenter.logger.error("[支付宝安全支付通知] [发生异常] ["+params+"]", e);
        }
        
        if (verified) {
			if(trade_status.equals("WAIT_BUYER_PAY")) {
				PlatformSavingCenter.logger.info("[充值回调] [支付宝] [安全支付] [正在等待支付：不做任何修改] [trade_status:"+trade_status+"] [订单金额:"+total_amount+"元] [out_trade_no:"+out_trade_no+"] [trade_no:"+out_trade_no+"]");
				return;
			}
			
			OrderForm order = OrderFormManager.getInstance().getOrderForm(out_trade_no);
			if(order != null) {
				synchronized(order) {
					if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.info("[充值回调] [支付宝] [安全支付] [回调充值失败：此订单已经回调过了] ["+order.getOrderId()+"] ["+(System.currentTimeMillis()-now)+"ms]");
						res.getWriter().write("success");
					} else {
						float addmoney = Float.valueOf(total_amount);
						long addamount = (long)(addmoney*100);
						long oldPay = order.getPayMoney();
//						if(addamount != oldPay){
//							PlatformSavingCenter.logger.info("[充值回调] [支付宝] [失败：支付金额与订单金额不符] [trade_status:"+trade_status+"] [订单金额:"+oldPay+"分] [支付金额:"+addamount+"] [orderid:"+out_trade_no+"] ["+params+"]");
//							return;
//						}
						
						//if(trade_status.equals("TRADE_FINISHED")) {
						if(trade_status.equals("TRADE_SUCCESS")) {
							order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							order.setResponseResult(OrderForm.RESPONSE_SUCC);
							order.setPayMoney(addamount);
							order.setMemo2("支付宝订单号:" + trade_no);
							try {
								OrderFormManager.getInstance().update(order);
								PlatformSavingCenter.logger.info("[充值回调] [支付宝] [安全支付] [回调充值成功] [订单号:"+order.getOrderId()+"] [user:"+PassportManager.getInstance().getPassport(order.getPassportId()).getUserName()+"] [充值额度:"+addamount+"分] [支付宝订单:"+trade_no+"] ["+(oldPay!=addamount?"返回充值额("+addamount+")与订单额("+oldPay+")不符，以返回为准":"")+"] ["+(System.currentTimeMillis()-now)+"ms]");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								PlatformSavingCenter.logger.info("[充值回调] [支付宝] [安全支付] [回调充值失败：返回成功但是更新订单失败] [订单号:"+order.getOrderId()+"] [user:"+PassportManager.getInstance().getPassport(order.getPassportId()).getUserName()+"] [充值额度:"+addamount+"分] [支付宝订单:"+trade_no+"] ["+(oldPay!=addamount?"返回充值额("+addamount+")与订单额("+oldPay+")不符，以返回为准":"")+"] ["+(System.currentTimeMillis()-now)+"ms]");
							}
						} else {
							order.setResponseResult(OrderForm.RESPONSE_FAILED);
							order.setResponseDesp("支付宝返回充值失败");
							order.setMemo2("支付宝订单号:" + trade_no);
							try {
								OrderFormManager.getInstance().update(order);
								PlatformSavingCenter.logger.info("[充值回调] [支付宝] [安全支付] [回调充值失败:返回充值失败] [订单号:"+order.getOrderId()+"] [user:"+PassportManager.getInstance().getPassport(order.getPassportId()).getUserName()+"] [充值额度:"+addamount+"分] [支付宝订单:"+trade_no+"] ["+(System.currentTimeMillis()-now)+"ms]");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								PlatformSavingCenter.logger.info("[充值回调] [支付宝] [安全支付] [回调充值失败:返回充值失败但是更新订单失败] [订单号:"+order.getOrderId()+"] [user:"+PassportManager.getInstance().getPassport(order.getPassportId()).getUserName()+"] [充值额度:"+addamount+"分] [支付宝订单:"+trade_no+"] ["+(System.currentTimeMillis()-now)+"ms]");
							}
						}
						res.getWriter().write("success");
						return;
					}
				}
			} else {
				PlatformSavingCenter.logger.info("[充值回调] [支付宝] [安全支付] [回调充值失败:订单不存在] [订单号:"+out_trade_no+"] [user:---] [充值额度:"+total_amount+"元] [支付宝订单:"+trade_no+"] ["+(System.currentTimeMillis()-now)+"ms]");
			}
        } else {
			PlatformSavingCenter.logger.info("[充值回调] [支付宝] [安全支付] [回调充值失败:返回验证错误] [content:"+content+"] [dataMap:"+params+"] ["+(System.currentTimeMillis()-now)+"ms]");
        }
	}
}
