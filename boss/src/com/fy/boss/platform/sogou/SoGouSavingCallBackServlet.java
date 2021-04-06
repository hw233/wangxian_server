package com.fy.boss.platform.sogou;

import java.io.IOException;
import java.util.LinkedHashMap;
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
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class SoGouSavingCallBackServlet extends HttpServlet {
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		String paykey = "{69223808-38B4-45F5-BAA9-9D27421EBA7F}";
		
		try
		{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
			
			int gid = ParamUtils.getIntParameter(req, "gid", 0);
			int sid = ParamUtils.getIntParameter(req, "sid", 0);
			String uid = ParamUtils.getParameter(req, "uid");
			String role = ParamUtils.getParameter(req, "role", "");
			String orderId = ParamUtils.getParameter(req, "appdata", "");
			String date = ParamUtils.getParameter(req, "date", "");
			String channelorderid = ParamUtils.getParameter(req, "oid", "");
			String rmb = ParamUtils.getParameter(req, "amount1", "");
			String youxibi = ParamUtils.getParameter(req, "amount2", "");
			long time = ParamUtils.getLongParameter(req, "time", 0l);
			String sign = ParamUtils.getParameter(req, "auth", "");
			String realAmount = ParamUtils.getParameter(req, "realAmount", "");
		
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
			params.put("gid", req.getParameter("gid"));
			params.put("sid", req.getParameter("sid"));
			params.put("uid", req.getParameter("uid"));
			params.put("role", req.getParameter("role"));
			params.put("appdata", req.getParameter("appdata"));
			params.put("date", req.getParameter("date"));
			params.put("oid", req.getParameter("oid"));
			params.put("amount1", req.getParameter("amount1"));
			params.put("amount2", req.getParameter("amount2"));
			params.put("time", req.getParameter("time"));
			if(realAmount != null && !realAmount.isEmpty()){
				params.put("realAmount", req.getParameter("realAmount"));
			}
			
			
			String my_sign = sign(params,paykey);
			
			

			if(my_sign.equalsIgnoreCase(sign))
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm(orderId);
				if(orderForm != null)
				{
					Passport pp = PassportManager.getInstance().getPassport(orderForm.getPassportId());
					String username = "";
					if(pp != null)
					{
						username = pp.getUserName();
					}
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：搜狗SDK] [失败:此订单已经回调过] [搜狗SDK orderid:"+channelorderid+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+rmb+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [传入账号:"+username+"] [sign:"+sign+"] [mysign:"+my_sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("OK");
							return;
						}
						//更新订单
						orderForm.setResponseTime(System.currentTimeMillis());
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						//以渠道返回的充值额度为准
						orderForm.setPayMoney((long)(Double.valueOf(rmb)*100 ));	
						orderForm.setChannelOrderId(channelorderid);
						try {
							orderFormManager.update(orderForm);
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：搜狗SDK] [搜狗SDK orderid:"+channelorderid+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+rmb+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [传入账号:"+username+"] [sign:"+sign+"] [mysign:"+my_sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：搜狗SDK] [搜狗SDK orderid:"+channelorderid+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [实际支付金额:"+rmb+"] [数据库中记录金额:"+orderForm.getPayMoney()+"] [传入账号:"+username+"] [sign:"+sign+"] [mysign:"+my_sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						}
						res.getWriter().write("OK");
						return;
					}
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：搜狗SDK] [失败:订单不存在] [搜狗SDK orderid:"+channelorderid+"] [my order id:--] [my orderid:"+orderId+"] [实际支付金额:"+rmb+"] [数据库中记录金额:--] [传入账号:--] [sign:"+sign+"] [mysign:"+my_sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("ERR_500");
					return;
				}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：搜狗SDK] [失败:验签失败] [搜狗SDK orderid:"+channelorderid+"] [my order id:--] [my orderid:"+orderId+"] [实际支付金额:"+rmb+"] [数据库中记录金额:--] [传入账号:--] [sign:"+sign+"] [mysign:"+my_sign+"] [params:"+params+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				res.getWriter().write("ERR_200");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：搜狗SDK] [失败：出现未知异常]",e);
			res.getWriter().write("ERR_500");
			return;

		}
		
	}
	
	public static String sign(Map<String,String> params,String secretkey){
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			if(v == null) continue;
			if("sign".equalsIgnoreCase(keys[i])) continue;
			sb.append(keys[i]+"="+v + "&");
		}
		String md5Str = sb.toString();
		if(md5Str.length() > 0) {
			md5Str = md5Str.substring(0, md5Str.length()-1);
		}
		md5Str = md5Str + "&"+secretkey;
		
		String sign = StringUtil.hash(md5Str);
		
		return sign;
	}
}
