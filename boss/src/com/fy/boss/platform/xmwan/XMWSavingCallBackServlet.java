package com.fy.boss.platform.xmwan;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;

public class XMWSavingCallBackServlet extends HttpServlet{

	
	private String check_url = "http://open.xmwan.com/v2/purchases/verify";
	private String client_id = "0ab7be5209eaaf7c25ed7a52ac78717e";
	private String client_secret = "1844e8d82dcb1a102afc8d6c267d045a";
	
	
	/**
	 * 6. Client服务器校验寻仙决服务器参数签名，并核验回调的订单。
	 * 7. Client服务器处理充值逻辑，为用户发放道具，并返回success。
	 * amount=10&app_order_id=ORDER123456&app_user_id=XMW-USER-
		ID&notify_url=http://demo.xmwan.com/xmwan-sdk-php/
		notify.php&timestamp=12345678&client_secret=游戏服务器保存的密钥
	 */
	protected void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		long now = System.currentTimeMillis();
		req.setCharacterEncoding("utf-8");
		String serial = req.getParameter("serial");
		String amountstr = req.getParameter("amount");
		String status = req.getParameter("status");
		String app_order_id = req.getParameter("app_order_id");
		String app_user_id = req.getParameter("app_user_id");
		String sign = req.getParameter("sign");
		
		//签名验证
		LinkedHashMap<String, String> qianMingMap2 = new LinkedHashMap<String, String>();
		qianMingMap2.put("amount",amountstr);
		qianMingMap2.put("app_order_id", app_order_id);
		qianMingMap2.put("app_user_id",app_user_id);
		qianMingMap2.put("serial",serial);
		qianMingMap2.put("status",status);
		
		String qingMingb2 = buildParams(qianMingMap2);
		qingMingb2 += "&client_secret="+client_secret;
		String my_sign = StringUtil.hash(qingMingb2).toLowerCase();
		
		if(my_sign.equals(sign)){
			//支付结果通知
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
			params.put("serial",serial);
			params.put("client_id",client_id);
			params.put("client_secret",client_secret);
			params.put("amount",amountstr);
			params.put("app_order_id",app_order_id);
			
			//核验订单状态
			LinkedHashMap<String, String> qianMingMap = new LinkedHashMap<String, String>();
			qianMingMap.put("amount",amountstr);
			qianMingMap.put("app_order_id", app_order_id);
			qianMingMap.put("serial",serial);
			String qingMingb = buildParams(qianMingMap);
			qingMingb += "&client_secret="+client_secret;
			String signArg = StringUtil.hash(qingMingb).toLowerCase();
			params.put("sign",signArg);
			
			HashMap headers = new HashMap();
			String content = "";
			String result = "";
			String encoding = "";
			Integer code = -1;
			String message= "";
			byte bytes[] = null;
			try {
				content = buildParams(params);
				bytes = HttpUtils.webPost(new java.net.URL(check_url),content.getBytes(), headers, 5000, 10000);
				encoding = (String)headers.get(HttpUtils.ENCODING);
				code = (Integer)headers.get(HttpUtils.Response_Code);
				message = (String)headers.get(HttpUtils.Response_Message);
				result = new String(bytes,encoding);
				String mess = "";
				if(result.contains("success")){
					mess = "该订单已完成";
				}else if(result.contains("unpaid")){
					mess = "该订单尚未支付";
				}else if(result.contains("nvalid")){
					mess = "没有找到需要查询的订单";
				}
				if(mess.equals("该订单已完成") || mess.equals("success")){
					//TODO
					OrderForm order = OrderFormManager.getInstance().getOrderForm(app_order_id);
					if(order == null){
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：寻仙决SDK] [核验订单状态结果:没找到订单] [serial:"+serial+"] [status:"+status+"] [amount:"+amountstr+"] [app_order_id:"+app_order_id+"] [app_user_id:"+app_user_id+"] [result:"+result+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-now)+"]");
						res.getWriter().write("fail");
						return;
					}
					
					synchronized(order) {
						if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							res.getWriter().write("success");
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：寻仙决SDK] [失败:此订单已经回调过了] [serial:"+serial+"] [status:"+status+"] [amount:"+amountstr+"] [app_order_id:"+app_order_id+"] [app_user_id:"+app_user_id+"] [result:"+result+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-now)+"]");
							return;
						}
						//更新订单
						order.setResponseTime(System.currentTimeMillis());
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setResponseResult(OrderForm.RESPONSE_SUCC);
						//以渠道返回的充值额度为准
						order.setPayMoney((long)(Long.valueOf(amountstr)*100 ));					
						try {
							OrderFormManager.getInstance().update(order);
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：寻仙决SDK] [核验订单状态结果:"+mess+"] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] [实际支付金额:"+amountstr+"] [数据库中记录金额:"+order.getPayMoney()+"] [serial:"+serial+"] [status:"+status+"] [amount:"+amountstr+"] [app_order_id:"+app_order_id+"] [app_user_id:"+app_user_id+"] [result:"+result+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-now)+"]");
							res.getWriter().write("success");
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：寻仙决SDK] [更新订单信息失败] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] [实际支付金额:"+amountstr+"] [数据库中记录金额:"+order.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-now)+"]",e);
							res.getWriter().write("fail");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				encoding = (String)headers.get(HttpUtils.ENCODING);
				code = (Integer)headers.get(HttpUtils.Response_Code);
				message = (String)headers.get(HttpUtils.Response_Message);
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：寻仙决SDK] [核验订单状态结果：异常] [serial:"+serial+"] [status:"+status+"] [amount:"+amountstr+"] [app_order_id:"+app_order_id+"] [app_user_id:"+app_user_id+"] [result:"+result+"] [encoding:"+encoding+"] [code:"+code+"] [message:"+message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-now)+"]",e);
				res.getWriter().write("fail");
				return;
			}
		}else{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：寻仙决SDK] [失败:校验字符串失败] [qingMingb2:"+qingMingb2+"] [传回校验字符串:"+sign+"] [生成校验字符串:"+my_sign+"] [serial:"+serial+"] [status:"+status+"] [amount:"+amountstr+"] [app_order_id:"+app_order_id+"] [app_user_id:"+app_user_id+"] [costs:"+(System.currentTimeMillis()-now)+"]");
			res.getWriter().write("fail");
			return;
		}
	}
	
	private String buildParams(Map<String,String> params){
		String[] key = params.keySet().toArray(new String[params.size()]);

		StringBuffer sb = new StringBuffer();
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(params.get(k));
			sb.append("&");
		}
		
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	public static void main(String[] args) {
		//[传回校验字符串:a6c661c6353bc843a3f6442d33ca9f08] [生成校验字符串:e51210a9484ffd8b3fa15d4c4d90324c]
		String md5pre = "amount=5&app_order_id=xmwan20150403-1100000000000088065&serial=201504031631102694119160";
		String my_sign = StringUtil.hash(md5pre).toLowerCase();
		System.out.println(my_sign);
	}
}
