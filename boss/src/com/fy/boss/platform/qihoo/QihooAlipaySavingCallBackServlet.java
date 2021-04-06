package com.fy.boss.platform.qihoo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.platform.xiaomi.common.HmacSHA1Encryption;
import com.fy.boss.platform.xiaomi.common.ParamEntry;
import com.fy.boss.platform.xiaomi.common.ParamUtil;
import com.fy.boss.platform.xiaomi.vo.NotifyReceiverResponse;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.qihoo.QihooAliPaySavingManager;
import com.xuanzhi.tools.servlet.HttpsUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.JsonUtil;
import com.xuanzhi.tools.text.StringUtil;

public class QihooAlipaySavingCallBackServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4050449812012426946L;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try
		{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
			String app_key = req.getParameter("app_key");//应用App key
			String inner_trade_code = req.getParameter("inner_trade_code");//支付网关送给银行的流水号
			String amount = req.getParameter("rec_amount");// 必选。支付金额 单位分 大于0的整数
			String actualAmount = req.getParameter("pay_amount");// 必选。支付金额 单位分 大于0的整数
			long payAmount = Math.round(Double.valueOf(amount) * 100);
			if(!StringUtils.isEmpty(actualAmount))
			{
				payAmount = Math.round(Double.valueOf(actualAmount) * 100);
			}
			String app_uid = req.getParameter("mer_code");//用户在应用内的用户id (pay_mode=21)
			String orderid = req.getParameter("mer_trade_code");// 应用扩展信息1，应用可以利用此字段传递应用内部订单号
			String app_ext2 = req.getParameter("app_ext2");// 应用扩展信息2
			String user_id = req.getParameter("user_id");// 360账号id
			String order_id = req.getParameter("gateway_trade_code");//应用平台接口生成的订单号，唯一，排重
			String gateway_flag = req.getParameter("bank_pay_flag");//如果支付返回成功，返回success，如果支付过程断路，返回空，如果支付失败，返回fail
			String bank_trade_code = req.getParameter("bank_trade_code");//第三方流水号
			
			String sign_type = req.getParameter("sign_type");//签名算法	目前仅支持sign_type=md5
			String sign_return = req.getParameter("sign_return");//应用回传给订单核实接口的参数，不加入签名校验计算
			String sign = req.getParameter("sign");//提取的签名（签名方法如下），不加入签名校验计算
			
			Map<String,String[]> map = new HashMap<String,String[]>();
			map = req.getParameterMap();
			if(PlatformSavingCenter.logger.isDebugEnabled())
				PlatformSavingCenter.logger.debug("[充值回调] [充值平台:360接口支付宝] [参数:"+map+"]");
	//		map.put("sign", sign);
			OrderFormManager fmanager = OrderFormManager.getInstance();
			OrderForm orderForm = fmanager.getOrderForm(orderid);
			if (orderForm != null) {
				synchronized(orderForm) {
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：360接口支付宝] [失败：此订单已经回调过了] [360接口支付宝 orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderid+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [实际充值金额:"+payAmount+"] [appKey:"+app_key+"] [银行流水号:"+inner_trade_code+"] [支付宝流水号:"+bank_trade_code+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+payAmount+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+QihooAliPaySavingManager.MD5_KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
							 res.getWriter().write("success");
							return;
					}
				}
				
				if (signedUrl(map, QihooAliPaySavingManager.MD5_KEY).equals(sign)) {
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2("[外部订单号:"+order_id+"] [交易金额:"+amount+"] [交易结果:"+gateway_flag+"] [sign:"+sign+"]");
					orderForm.setResponseDesp("360接口支付宝游戏回调充值接口成功");
					
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					orderForm.setPayMoney(payAmount);
					orderForm.setChannelOrderId(order_id);
					try {
						fmanager.update(orderForm);
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：360接口支付宝] ["+(gateway_flag.equalsIgnoreCase("success") ? "成功" : "失败")+"] [360接口支付宝 orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderid+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [实际充值金额:"+payAmount+"] [appKey:"+app_key+"] [银行流水号:"+inner_trade_code+"] [支付宝流水号:"+bank_trade_code+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [自用appKey:"+QihooAliPaySavingManager.MD5_KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						res.getWriter().write("success");
						return;
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：360接口支付宝] [失败:出现异常] [360接口支付宝 orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderid+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [银行流水号:"+inner_trade_code+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [支付宝流水号:"+bank_trade_code+"] [sign_type:"+sign_type+"] [自用appKey:"+QihooAliPaySavingManager.MD5_KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
						res.getWriter().write("success");
						return;
					}
				} else {
					
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2("[外部订单号:"+order_id+"] [交易金额:"+amount+"] [交易结果:"+gateway_flag+"] [sign:"+sign+"]");
					orderForm.setResponseDesp("360接口支付宝游戏回调充值接口成功");
					
					orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
					orderForm.setPayMoney(payAmount);
					orderForm.setChannelOrderId(order_id);
					
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：360接口支付宝] [失败:验签失败] [360接口支付宝 orderId:"+order_id+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderid+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [银行流水号:"+inner_trade_code+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [支付宝流水号:"+bank_trade_code+"] [sign_type:"+sign_type+"] [自用appKey:"+QihooAliPaySavingManager.MD5_KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					res.getWriter().write("success");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：360接口支付宝] [失败:数据库中未找到对应订单号的订单] [360接口支付宝 orderId:"+order_id+"] [my order id:--] [my orderid:"+orderid+"] [充值类型:--] [账号:"+user_id+"] [充值金额:"+amount+"] [appKey:"+app_key+"] [银行流水号:"+inner_trade_code+"] [app_uid:"+app_uid+"] [gateway_flag:"+gateway_flag+"] [sign_type:"+sign_type+"] [myOrderid:"+orderid+"] [sign_return:"+sign_return+"] [订单中充值金额:"+amount+"] [sign:"+sign+"] [sign_type:"+sign_type+"] [支付宝流水号:"+bank_trade_code+"] [自用appKey:"+QihooAliPaySavingManager.MD5_KEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				res.getWriter().write("success");
				return;
			}
		}
		catch(Throwable t)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：360接口支付宝] [失败:出现未知异常]",t);
			res.getWriter().write("success");
			return;
		}
	}
	
	
public static String signedUrl(Map<String,String[]> params,String secretkey){
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i])[0];
			if(v == null) continue;
			if("sign".equalsIgnoreCase(keys[i])) continue;
			sb.append(keys[i]+"="+v + "&");
		}
		String md5Str = sb.toString();
		if(md5Str.length() > 0) {
			md5Str = md5Str.substring(0, md5Str.length()-1);
		}
		md5Str = md5Str + secretkey;
		
		String sign = StringUtil.hash(md5Str);
		
		return sign;
	}
	
}
