package com.fy.boss.platform.baidusdk;

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
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.baidusdk.Base64;
import com.xuanzhi.tools.text.StringUtil;

public class BAIDUNEWSDKSavingCallBackServlet extends HttpServlet {

	public static String appid = "5179344";
	public static String appkey = "sL7MReEdeBWl498aoM3cXkWF";
	public static String client_secret = "rw1j0wjI9hWKoKACyRhGdF6BPI6Sg3wb";
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try{
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
			String channelOrderId = req.getParameter("CooperatorOrderSerial");
			String OrderSerial = req.getParameter("OrderSerial");
			params.put("AppID", req.getParameter("AppID"));
			params.put("OrderSerial", req.getParameter("OrderSerial"));
			params.put("CooperatorOrderSerial", req.getParameter("CooperatorOrderSerial"));
			params.put("Content", req.getParameter("Content"));
			String mySign = sign(params,client_secret);
			String handleContent2 = Base64.decode(req.getParameter("Content"));
			params.put("handleContent", handleContent2);
			params.put("Sign", req.getParameter("Sign"));
			params.put("mySign",mySign);
			
			if(PlatformSavingCenter.logger.isInfoEnabled()){
				PlatformSavingCenter.logger.info("[充值回调] [充值平台：NEW百度SDK] [SDK内部订单号:"+OrderSerial+"] [orderid:"+channelOrderId+"] [参数:"+params+"]");
			}
			
			try {
				if(req.getParameter("Sign") != null && req.getParameter("Sign").equalsIgnoreCase(mySign)){
					OrderFormManager orderFormManager = OrderFormManager.getInstance();
						//查询订单
						//如果cpOrderId没有传过来，则通过cardNo和充值类型查询订单
						OrderForm orderForm = orderFormManager.getOrderForm(channelOrderId.trim());
						if(orderForm!= null){
							Passport  p = PassportManager.getInstance().getPassport(orderForm.getPassportId());
							String username = p.getUserName();
							synchronized(orderForm) {
								String handleContent = Base64.decode(req.getParameter("Content"));
								JacksonManager jm = JacksonManager.getInstance();
								try {
									Map map =(Map)jm.jsonDecodeObject(handleContent);
									String MerchandiseName = (String)map.get("MerchandiseName");
									String OrderMoney = (String)map.get("OrderMoney");
									Integer OrderStatus = (Integer)map.get("OrderStatus");
									String StatusMsg = (String)map.get("StatusMsg");
									
									if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
										res.getWriter().write("{\"AppID\":\"5179344\",\"ResultCode\":\"1\",\"ResultMsg\":\"success\"}");
										PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEW百度SDK] [失败:此订单已经回调过了] [username:"+username+"] [uid:"+map.get("UID")+"] [商品:"+MerchandiseName+"] [百度SDKorderId:"+OrderSerial+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值金额:"+OrderMoney+"] [充值结果:"+OrderStatus+"] [结果描述:"+StatusMsg+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
										return;
									}
									
									try {
										orderForm.setResponseTime(System.currentTimeMillis());
										if(OrderStatus != null && OrderStatus.intValue() == 1) {
											orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
											orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
											orderForm.setPayMoney((long)((Double.valueOf(OrderMoney) * 100)));
											orderForm.setChannelOrderId(channelOrderId);
											//MD5(AppID+ResultCode+SecretKey)返回签名
											String returnSign = StringUtil.hash("51793441"+client_secret).toLowerCase();
											String jsonStr = "{\"AppID\":\"5179344\",\"ResultCode\":\"1\",\"ResultMsg\":\"success\",\"Sign\":\""+returnSign+"\"} ";
											res.getWriter().write(jsonStr);
											orderFormManager.update(orderForm);
											if(PlatformSavingCenter.logger.isInfoEnabled())
												PlatformSavingCenter.logger.info("[充值回调] [充值平台：NEW百度SDK] [成功充值] [username:"+username+"] [uid:"+map.get("UID")+"] [商品:"+MerchandiseName+"] [状态码:"+OrderStatus+"] [百度SDK订单号:"+OrderSerial+"] [自有订单号:"+orderForm.getId()+"] [充值金额:"+OrderMoney+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [充值结果:"+StatusMsg+"] [jsonStr:"+jsonStr+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
										} else {
											orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
											orderForm.setChannelOrderId(channelOrderId);
											if(PlatformSavingCenter.logger.isInfoEnabled())
												PlatformSavingCenter.logger.info("[充值回调] [充值平台：NEW百度SDK] [充值失败] [username:"+username+"] [uid:"+map.get("UID")+"] [商品:"+MerchandiseName+"] [状态码:"+OrderStatus+"] [百度SDK订单号:"+OrderSerial+"] [自有订单号:"+orderForm.getId()+"] [充值金额:"+OrderMoney+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [充值结果:"+StatusMsg+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
											orderFormManager.update(orderForm);
											res.getWriter().write("充值失败");
										}
									} catch (Exception e) {
										res.getWriter().write("ERROR_FAIL");
										PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEW百度SDK] [失败:更新订单信息出错] [username:"+username+"] [uid:"+map.get("UID")+"] [商品:"+MerchandiseName+"] [回传订单id:"+params.get("orderid")+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值金额:"+OrderMoney+"] [充值结果:"+params.get("result").trim()+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
										return;
									}
								}catch (Exception e) {
									res.getWriter().write("ERROR_FAIL_0");
									PlatformSavingCenter.logger.info("[解析充值回调返回数据失败] [NEW百度SDK] [username:"+username+"] ["+req.getParameter("Content")+"] [handleContent:"+handleContent+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
								}
							}
						}else{
							res.getWriter().write("ERROR_FAIL_1");
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEW百度SDK] [失败] [数据库中无此订单] [回传订单id:"+channelOrderId+"] [回传内容："+req.getParameter("Content")+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							return;
						}
				}else{
					res.getWriter().write("ERROR_FAIL_2");
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEW百度SDK] [失败] [失败：请求校验加密串不一致] [回传订单id:"+channelOrderId+"] [回传的sign:"+req.getParameter("Sign")+"] [生成的sign:"+mySign+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					return;
				}
			} catch (Exception e1) {
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEW百度SDK] [失败:出现异常] [回传订单id:"+channelOrderId+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
				res.getWriter().write("ERROR_FAIL_3");
				return;
			}
		}catch(Exception e){
			res.getWriter().write("ERROR_FAIL_4");
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：NEW百度SDK] [失败:出现异常]",e);
			return;
		}
	}
	
	public static String sign(LinkedHashMap<String,String> params,String appkey){
		String values[] = params.values().toArray(new String[0]);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < values.length ; i++){
			sb.append(values[i]);
		}
		String md5Str = sb.toString()+appkey;
		String sign = StringUtil.hash(md5Str);
		return sign.toLowerCase();
	}
	
	public static void main(String[] args) {
		String content = "eyJVSUQiOjU5ODI3NTQxOSwiTWVyY2hhbmRpc2VOYW1lIjoi6ZO25LikIiwiT3JkZXJNb25leSI6IjEwLjAwIiwiU3RhcnREYXRlVGltZSI6IjIwMTUtMDUtMTkgMTE6MjQ6NDciLCJCYW5rRGF0ZVRpbWUiOiIyMDE1LTA1LTE5IDExOjI1OjQxIiwiT3JkZXJTdGF0dXMiOjEsIlN0YXR1c01zZyI6IuaIkOWKnyIsIkV4dEluZm8iOiIiLCJWb3VjaGVyTW9uZXkiOjB9";
		String rightSign = "46674312b5cfc9077b29bee555d038fe";
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("AppID", "5179344");
		params.put("OrderSerial", "d4e14c9c4aabbf75_01001_2015051911_000000");
		params.put("CooperatorOrderSerial", "wx-2015051911-1100000000000144402");
		params.put("Content", content);
		String mySign = sign(params,client_secret);
		System.out.println("正确签名:"+rightSign);
		System.out.println("我的签名:"+mySign);
	}
	
}
	