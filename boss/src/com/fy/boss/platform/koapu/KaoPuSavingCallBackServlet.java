package com.fy.boss.platform.koapu;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.JsonUtil;
import com.xuanzhi.tools.text.StringUtil;

public class KaoPuSavingCallBackServlet extends HttpServlet{

	private String KAOPU_SECRETKEY = "BFCF79B9-B1D4-4E95-B9A2-64DD51E15580";
	/** 错误码：
	 *  0  网络连接失败
		1  销卡成功，订单失败
		1000  0 未知错误
		1002  本张卡密您提交过于频繁，请您稍后再试
		1003  不支持的卡类型（比如电信地方卡）
		1004  密码错误或充值卡无效
		1006  充值卡无效
		1007  卡内余额不足
		1008  余额卡过期（有效期 1 个月）
		101  md5 验证失败
		1010  此卡正在处理中
		102  订单号重复
		103  恶意用户
		104  序列号，密码简单验证失败
		105  密码正在处理中
		106  系统繁忙，暂停提交
		107  多次支付时卡内余额不足
		109  des 解密失败
		200  请求成功（非订单支付成功
		2005  此卡已使用
		2006  卡密在系统处理中
		2007  该卡为假卡
		2008  该卡种正在维护
		2009  浙江省移动维护
		201  证书验证失败
		2010  江苏省移动维护
		2011  福建省移动维护
		2012  辽宁省移动维护
		2013  该卡已被锁定
		2014  系统繁忙，请稍后再试
		3001  卡不存在
		3002  卡已使用过
		3003  卡已作废
		3004  卡已冻结
		3005  卡未激活
		3006  密码不正确
		3007  卡正在处理中
		3101  系统错误
		3102  卡已过期
		4000  系统异常
		5001  订单支付失败
	 */
	
	protected void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		long now = System.currentTimeMillis();
		req.setCharacterEncoding("utf-8");
		String username = req.getParameter("username");
		String kpordernum = req.getParameter("kpordernum");
		String ywordernum = req.getParameter("ywordernum");
		String status = req.getParameter("status");
		String paytype = req.getParameter("paytype");
		String amount = req.getParameter("amount");
		String gameserver = req.getParameter("gameserver");
		String errdesc = req.getParameter("errdesc");
		String paytime = req.getParameter("paytime");
		String gamename = req.getParameter("gamename");
		String sign = req.getParameter("sign");
		
		//签名验证
		String transdata = username+"|"+kpordernum+"|"+ywordernum+"|"+status+"|"+paytype+"|"+amount+"|"+gameserver
		+"|"+errdesc+"|"+paytime+"|"+gamename+"|"+KAOPU_SECRETKEY;
		String my_sign = StringUtil.hash(transdata).toLowerCase();
		
		if(my_sign.equals(sign)){
			OrderForm order = OrderFormManager.getInstance().getOrderForm(ywordernum);
			if(order == null){
				PlatformSavingCenter.logger.warn("[充值回调] [充值平台：靠谱SDK] [核验订单状态结果:没找到订单] [orderid:"+ywordernum+"] [username:"+username+"] [status:"+status+"] [amount:"+amount+"] [errdesc:"+errdesc+"] [paytime:"+paytime+"] [gamename:"+gamename+"] [transdata:"+transdata+"] [costs:"+(System.currentTimeMillis()-now)+"]");
				String error_sign = StringUtil.hash("1003|"+KAOPU_SECRETKEY).toLowerCase();
				ResultInfo info = new ResultInfo("1003", "ORDER_NOT_EXIST", error_sign);
				try {
					res.getWriter().write(JsonUtil.jsonFromObject(info));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				return;
			}
			
			synchronized(order) {
				if(order.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：靠谱SDK] [失败:此订单已经回调过了] [orderid:"+ywordernum+"] [username:"+username+"] [status:"+status+"] [amount:"+amount+"] [errdesc:"+errdesc+"] [paytime:"+paytime+"] [gamename:"+gamename+"] [transdata:"+transdata+"] [costs:"+(System.currentTimeMillis()-now)+"]");
					String error_sign = StringUtil.hash("1005|"+KAOPU_SECRETKEY).toLowerCase();
					ResultInfo info = new ResultInfo("1005", "HAS_COMEBACK", error_sign);
					try {
						res.getWriter().write(JsonUtil.jsonFromObject(info));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					return;
				}
				//更新订单
				order.setResponseTime(System.currentTimeMillis());
				order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
				order.setResponseResult(OrderForm.RESPONSE_SUCC);
				//以渠道返回的充值额度为准
				long oldPayMoney = order.getPayMoney();
				order.setPayMoney((long)(Long.valueOf(amount) ));					
				try {
					OrderFormManager.getInstance().update(order);
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[充值回调] [充值平台：靠谱SDK] [orderid:"+ywordernum+"] [username:"+username+"] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] [原始订单金额:"+oldPayMoney+"分] [实际支付金额:"+amount+"分] [数据库中记录金额:"+order.getPayMoney()+"] [errdesc:"+errdesc+"] [paytime:"+paytime+"] [gamename:"+gamename+"] [costs:"+(System.currentTimeMillis()-now)+"]");
					String error_sign = StringUtil.hash("1000|"+KAOPU_SECRETKEY).toLowerCase();
					ResultInfo info = new ResultInfo("1000", "SUCCESS", error_sign);
					try {
						res.getWriter().write(JsonUtil.jsonFromObject(info));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：靠谱SDK] [更新订单信息失败] [id:"+order.getId()+"] [orderid:"+order.getOrderId()+"] [errdesc:"+errdesc+"] [paytime:"+paytime+"] [gamename:"+gamename+"] [数据库中记录金额:"+order.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-now)+"]",e);
					String error_sign = StringUtil.hash("1005|"+KAOPU_SECRETKEY).toLowerCase();
					ResultInfo info = new ResultInfo("1005", "HANDLE_ERROR", error_sign);
					try {
						res.getWriter().write(JsonUtil.jsonFromObject(info));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}else{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：靠谱SDK] [失败:校验字符串失败] [orderid:"+ywordernum+"] [transdata:"+transdata+"] [sdk传回校验字符串:"+sign+"] [my_sign:"+my_sign+"] [errdesc:"+errdesc+"] [paytime:"+paytime+"] [gamename:"+gamename+"] [costs:"+(System.currentTimeMillis()-now)+"]");
			String error_sign = StringUtil.hash("1002|"+KAOPU_SECRETKEY).toLowerCase();
			ResultInfo info = new ResultInfo("1002", "SIGN_ERROR", error_sign);
			try {
				res.getWriter().write(JsonUtil.jsonFromObject(info));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}
	
	class ResultInfo {
		String code = "";
		String msg = "";
		String sign = "";
		public ResultInfo(String code,String msg,String sign){
			this.code = code;
			this.msg = msg;
			this.sign = sign;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		}
	}
	
}
