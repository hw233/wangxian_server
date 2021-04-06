package com.fy.boss.platform.website;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.website.AlipayWebSavingManager;
import com.xuanzhi.tools.text.StringUtil;

public class AlipayWebSavingBackServlet  extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		long now = System.currentTimeMillis();
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = req.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
			params.put(name, valueStr);
		}
		String trade_no = req.getParameter("trade_no");				//支付宝交易号
		String order_no = req.getParameter("out_trade_no");	        //获取订单号
		String total_fee = req.getParameter("total_fee");	        //获取总金额
		String buyer_email = req.getParameter("buyer_email");		//买家支付宝账号
		String trade_status = req.getParameter("trade_status");		//交易状态
		if(PlatformSavingCenter.logger.isDebugEnabled())
			PlatformSavingCenter.logger.debug("[充值返回] [支付平台：网站充值] [返回结果] [请求map:"+requestParams+"] [内容:" + params + "] [交易状态:" +trade_status+ "] [支付宝交易号:" +trade_no+ "] [支付宝交易号:" +trade_no+ "] [订单号:" +order_no+ "] [获取总金额:" +total_fee+ "] [买家支付宝账号" + buyer_email + "] [cost:"+(System.currentTimeMillis()-now)+"]");
		
		if(validateMd5(params)){//验证成功
			OrderForm order = OrderFormManager.getInstance().getOrderForm(order_no);
			if (order != null) {
				synchronized(order) {
					if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.info("[充值回调] [网站充值] [安全支付] [回调充值失败：此订单已经回调过了] ["+order.getOrderId()+"] ["+(System.currentTimeMillis()-now)+"ms]");
					}
					else {
						order.setResponseTime(System.currentTimeMillis());
						if(trade_status.equals("TRADE_SUCCESS")){
							long payMoney = order.getPayMoney();
							// 必须在完成更新订单后才能进行兑换操作，否则可能造成重复的兑换
							long rmbFen = 0;
							try {
								//回调金额单位 rmb元
								Double money = Double.valueOf(total_fee) * 100;
								rmbFen = money.intValue();
							} catch (NumberFormatException e) {
								PlatformSavingCenter.logger.error("[充值返回] [支付结果:成功] [支付平台：网站充值] [订单金额非法] [订单:" + order.getOrderId() + "] [订单金额:" + payMoney + "分] [实际充值额:" + total_fee + "分] [支付宝交易号:" + trade_no
									+ "] [cost:" + (System.currentTimeMillis() - now) + "ms]",e);
							}
							order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							order.setResponseResult(OrderForm.RESPONSE_SUCC);
							order.setPayMoney(rmbFen);
							order.setMemo2("支付宝订单号:" + trade_no);
							order.setChannelOrderId(trade_no);
							try {
								OrderFormManager.getInstance().update(order);
								PlatformSavingCenter.logger.info("[充值回调] [网站充值] [回调充值成功] [订单号:"+order.getOrderId()+"] [user:"+PassportManager.getInstance().getPassport(order.getPassportId()).getUserName()+"] [充值额度:"+rmbFen+"分] [支付宝订单:"+trade_no+"] ["+(payMoney!=rmbFen?"返回充值额("+rmbFen+")与订单额("+payMoney+")不符，以返回为准":"")+"] ["+(System.currentTimeMillis()-now)+"ms]");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								PlatformSavingCenter.logger.info("[充值回调] [网站充值] [回调充值失败：返回成功但是更新订单失败] [订单号:"+order.getOrderId()+"] [user:"+PassportManager.getInstance().getPassport(order.getPassportId()).getUserName()+"] [充值额度:"+rmbFen+"分] [支付宝订单:"+trade_no+"] ["+(payMoney!=rmbFen?"返回充值额("+rmbFen+")与订单额("+payMoney+")不符，以返回为准":"")+"] ["+(System.currentTimeMillis()-now)+"ms]");
							}
						} else {
							order.setResponseResult(OrderForm.RESPONSE_FAILED);
							order.setResponseDesp("支付宝返回充值失败");
							order.setMemo2("支付宝订单号:" + trade_no);
							order.setChannelOrderId(trade_no);
							try {
								OrderFormManager.getInstance().update(order);
								PlatformSavingCenter.logger.info("[充值回调] [网站充值] [回调充值失败:返回充值失败] [订单号:"+order.getOrderId()+"] [user:"+PassportManager.getInstance().getPassport(order.getPassportId()).getUserName()+"] [充值额度:"+((Double)(Double.valueOf(total_fee) * 100)).intValue()+"分] [支付宝订单:"+trade_no+"] ["+(System.currentTimeMillis()-now)+"ms]");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								PlatformSavingCenter.logger.info("[充值回调] [网站充值] [回调充值失败:返回充值失败但是更新订单失败] [订单号:"+order.getOrderId()+"] [user:"+PassportManager.getInstance().getPassport(order.getPassportId()).getUserName()+"] [充值额度:"+((Double)(Double.valueOf(total_fee) * 100)).intValue()+"分] [支付宝订单:"+trade_no+"] ["+(System.currentTimeMillis()-now)+"ms]");
							}
						}
					}
				}
			}
			else {
				PlatformSavingCenter.logger.info("[充值返回] [订单找不到] [支付平台：网站充值] [用户:----] [支付结果:成功] [订单:" + order_no + "] [充值额:" + total_fee + "分] [支付宝交易号:"
						+ trade_no + "]");
			}
			res.getWriter().write("success");
		} else {
			PlatformSavingCenter.logger.info("[充值返回] [验证出错] [支付平台：网站充值] [用户:----] [支付结果:----] [订单:" + order_no + "] [充值额:" + total_fee + "分] [支付宝交易号:"
					+ trade_no + "]");
			res.getWriter().write("fail");
		}
	}
	
    /****
     * 验证md5
     */
	public static boolean validateMd5(Map<String, String> paramMap) throws UnsupportedEncodingException {
	    Map param = new HashMap();
		param.putAll(paramMap);
		StringBuffer request_str = new StringBuffer();
		Object[] paramKeys = param.keySet().toArray();
		Object[] paramKeys2 = new Object[paramKeys.length + 1];
		Arrays.sort(paramKeys);
		for (int i = 0; i < paramKeys.length; i++) {
			if(param.get(paramKeys[i]) == null || (paramKeys[i] != null && paramKeys[i].equals("sign")) || (paramKeys[i] != null && paramKeys[i].equals("sign_type"))) {
				continue;
			}
			request_str.append(paramKeys[i] + "").append("=").append(URLDecoder.decode(param.get(paramKeys[i]) + "","UTF-8")).append("&");
			paramKeys2[i] = paramKeys[i];
		}
		if (request_str.length() > 0) {
			request_str.deleteCharAt(request_str.length() - 1);
		}
		String sig = request_str.append(AlipayWebSavingManager.key).toString();
		String md5 = StringUtil.hash(sig);
		if(md5.equals(paramMap.get("sign"))) {
			return true;
		}
		PlatformSavingCenter.logger.warn("[充值返回] [md5验证未通过] [支付平台：网站充值] [加密前:" + sig + "] [加密后:" + md5 + "] [sign:" +paramMap.get("sign")+ "]");
		return false;
	}
}
