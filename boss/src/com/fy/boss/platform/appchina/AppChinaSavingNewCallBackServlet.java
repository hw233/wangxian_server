package com.fy.boss.platform.appchina;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.appchina.Base641;
/**
 * 应用汇Android 单机SDK v7.0.0
 * @author wtx
 * @create 2016-6-3 下午04:58:38
 */
public class AppChinaSavingNewCallBackServlet extends HttpServlet {

	/**
	 * 应用私钥：
	 * 用于对商户应用发送到平台的数据进行加密
	 */
	public final static String privateKey = "MIICXgIBAAKBgQDL78QUXXonCp11p+9QKEVQrSYuFko3z83u7i7Eo/ticPlDl2SkIGu17pt55gy2GNUCMGlVLX5w7qpwTK9CNaukZ3wp3PnuOvY9VGQ7KviHgSgjjGeK4VIJxwChwMYsb3Gclp7xXhih0bTaybyvt3uQEzay7oP3DzHzEodcdu597QIDAQABAoGACal9b72SCQF+vlFLjE+sIQtjELHrqENHLXfJbXWbdmmF1cb5dLE4iTEZ2qekmIgKp4TlqKx0HiOgnZt0fj2OuTCqyslk+YysR3BB+ExMayGPVw+FMzR+f8XRWBRkCIOJTyUI4BGe8NbQ/1WVhEu7JUwew97falUz3ZOivEWPVgECQQDnmPTaB4jLRx/9F/idx8O4l2/J2ateRa6oulOX219bn8Wfw2qHgFmdkMOet4fB1hsxL++LkkYWauVOk1/g1eiBAkEA4WyxPqicNecBQ7lf9pT8WCZ1W82LmvuDvSRDTHFdEQfzoLK04ra/8A18xzA0JDFeMBllrGGjZkBvV5dSuEL/bQJBAKZ5z3+uLMmaTcc7ZO3du7XDvYqF2eZBBqSokxA0k54kAgWujbPrMf/OIJ/FY2OVSSSZRYK5WYIhwsUxsJjIioECQQDbo5rjMUfa4TSDNyiASlPA09//Tbubizql3KJR5hbG1FeVmOnUCDnlfE66iYYUSaG6/dPp7MxXgaq6zm9Dp8vVAkEA01q+jSeZ1fDDtIWpwuTZ1ldkMS4lC4phCiB8/6vnZnMFFhln+EbXHnNKRELRN5e+zDXWQsW2+whD61Qj+lC0vg==";

	/**
	 * 平台公钥：
	 * 用于商户应用对接收平台的数据进行解密
	 */
	public final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBfS8ba+cihX8feB3HGLS5lJxDFRz3eyz2ZmrjIaP2IIodbcEcGeRFrG+Aa/ilvM7s3pkT7BB2NYCTSnQ3bdBrulhcdkvtDmTtrXiVr8UT+suLU/rz0dTW8Bcj7SaINsbMfIvXInQB3VzSb2kzbjRnZH4CPedM4ufL7kbjuLIVmwIDAQAB";

	public static String miyao = publicKey;
	
	
	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param ali_public_key  爱贝公钥
	* @param input_charset 编码格式
	* @return 布尔值
	*/
	
	public static boolean verify(String content, String sign, String iapp_pub_key, String input_charset){
		try{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        byte[] encodedKey = Base641.decode(iapp_pub_key);
	        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature.getInstance("MD5WithRSA");
			signature.initVerify(pubKey);
			signature.update(content.getBytes(input_charset));
			return signature.verify(Base641.decode(sign) );
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		String trdata = req.getParameter("transdata");
		String signstr = req.getParameter("sign");
		String singnTstr = req.getParameter("signtype");
		
		try{
			String transdata = URLDecoder.decode(trdata, "UTF-8");
			String sign = URLDecoder.decode(signstr,"UTF-8");
			String signtype = URLDecoder.decode(singnTstr,"UTF-8");
			
			JacksonManager jm = JacksonManager.getInstance();
			Map dataMap = (Map)jm.jsonDecodeObject(transdata);
			
			String transtype = dataMap.get("transtype")+"";
			String cporderid = (String)dataMap.get("cporderid");
			String transid = (String)dataMap.get("transid");
			String appuserid = (String)dataMap.get("appuserid");
			String appid = (String)dataMap.get("appid");
			String waresid = ((Integer)dataMap.get("waresid")).intValue() + "";
			String feetype = ((Integer)dataMap.get("feetype")).intValue()+"";
			String money = ((Double)dataMap.get("money")).doubleValue()+"";
			String currency = ((String)dataMap.get("currency"));
			String result = dataMap.get("result")+"";
			String transtime = dataMap.get("transtime")+"";
			String count = dataMap.get("count")+"";
			
			try{
				if(verify(trdata, signstr, miyao, "UTF-8"))
				{
					if(!result.trim().equals("0")) {
						PlatformSavingCenter.logger.info("[充值回调] [失败] [充值平台：应用汇NEWSDK] [充值结果:"+result+"] [交易流水号:"+transid+"] [transdata:"+transdata+"] [sign:"+sign+"] [signtype:"+signtype+"]  [充值类型:"+feetype+"]  [交易金额:"+money+"] [购买数量:"+count+"] [交易时间:"+transtime+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
						return;
					}
					
					OrderFormManager orderFormManager = OrderFormManager.getInstance();
					//查询订单
					OrderForm orderForm = orderFormManager.getOrderForm(cporderid);
					
					//通过orderId查订单
					if(orderForm != null)
					{
						synchronized(orderForm) {
							if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
								PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇NEWSDK] [失败：订单已经回调过了] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"] [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
								res.getWriter().write("SUCCESS");
								return;
							}
						}
						orderForm.setResponseTime(System.currentTimeMillis());
						orderForm.setMemo2("[外部订单号:"+cporderid+"] [交易流水号:"+transid+"] [商品编号："+waresid+"] [计费类型："+feetype+"] [交易金额:"+money+"] [购买数量:"+count+"] [交易结果:"+result+"] [交易类型:"+transtype+"] [交易时间:"+transtime+"]");
						orderForm.setResponseDesp("应用汇回调充值接口成功");
						long lMoney = Long.valueOf(money.replace(".0", "")) * 1000;
						if(lMoney != orderForm.getPayMoney())
						{
							PlatformSavingCenter.logger.warn("[充值回调] [充值平台：应用汇NEWSDK] [警告：传回的充值金额和订单存储的金额不一致] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"]  [交易金额:"+money+"] [lMoney:"+lMoney+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]");
						}
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						orderForm.setPayMoney(lMoney);
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.info("[充值回调] [充值平台：应用汇NEWSDK] [成功] [用户充值] [成功] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"]  [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"]  [充值结果:"+result+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						try {
							orderFormManager.update(orderForm);
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇NEWSDK] [失败：更新订单出错] [交易流水号:"+transid+"] [order id:"+orderForm.getId()+"] [my orderId:"+orderForm.getOrderId()+"] [充值类型:"+feetype+"]  [交易金额:"+money+"] [订单中原金额:"+orderForm.getPayMoney()+"] [购买数量:"+count+"] [交易时间:"+transtime+"]  [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
							res.getWriter().write("failure");
							return;
						}
						
						res.getWriter().write("SUCCESS");
						return;
					}
					else
					{
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇NEWSDK] [失败：无参数指定订单] [交易流水号:"+transid+"] [充值类型:"+feetype+"] [交易金额:"+money+"] [购买数量:"+count+"] [交易时间:"+transtime+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						res.getWriter().write("failure");
						return;
					}
				}
				else
				{
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇NEWSDK] [失败：签名验证失败] [sign:"+sign+"] [transdata:"+transdata+"] [signtype:"+signtype+"] [key:"+miyao+"] [signEncode:"+signstr+"] [transdataEncode:"+trdata+"] [signtypeEncode:"+singnTstr+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("failure");
					return;
				}
			}catch(Exception e){
				e.printStackTrace();
				res.getWriter().write("failure");
				PlatformSavingCenter.logger.warn("[充值回调] [异常1] [充值平台：应用汇NEWSDK] [transdataDecode:"+transdata+"] [signDecode:"+sign+"] [signtypeDecode:"+signtype+"] [signEncode:"+signstr+"] [transdataEncode:"+trdata+"] [signtypeEncode:"+singnTstr+"] [key:"+miyao+"] [异常"+e+"] ");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			res.getWriter().write("failure");
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：应用汇NEWSDK] [异常2] [key:"+miyao+"] [signEncode:"+signstr+"] [transdataEncode:"+trdata+"] [signtypeEncode:"+singnTstr+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
		}
	}
	
}
