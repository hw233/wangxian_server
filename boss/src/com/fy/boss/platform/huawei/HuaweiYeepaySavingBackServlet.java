package com.fy.boss.platform.huawei;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
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

import org.codehaus.jackson.map.ObjectMapper;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.huawei.domain.ResultDomain;
import com.fy.boss.platform.huawei.util.RSA;

public class HuaweiYeepaySavingBackServlet extends HttpServlet {
	
	private static Map<String,String> payMap = new HashMap<String, String>();
	public static final String devPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy6xtpIGb0CBbcGrtYy+HygM/AUujhTCnagAx4a8kWmKw7NukDhQ+L2mC0MfDOrXW1QtucHMdw5TITAZqSx1tqIKCClRciM1km8RLot5z7aAjKgrSZAubFUI3jGFHS+XnEZRw0JQuRR8EywnwiriOBCyBt3fZGyTn5z9hi6RGx2Qi5UGOUZJF17olB8pkhg4xdUxNdRViEQ3VdOeEz+W3lPc4/a6F9/PZ8as76E3kxmrOYETJhXaJsnJYla1r113FyUY7zn402+UA35RrV508hmWxq/JC1s4R7Sner0oUJo523ButBdlmFMlkPmH3Ejvo1hf3i+FUU5Mob4Hf30ydGQIDAQAB";  //正式
//	public static final String devPubKey = "9a28091de454fa39b30bc69ce9ada064e1cd4367813a5d96559c7627f7514801084d974c5a98c00b8b18afd555099952f97911209c124c7bbe2aad6bd65496808d7cae2d05d410c7db70ed7d7a0a472413e8e09daa42da6426f89ba40d5676698e1864ba390455068e1b26690517f6d5545bd5492e457112125ec517c29bfbd8905518079d99bc4bff74d5dba50463e2"; //测试
//	public static final String devPubKey = "9a28091de454fa39b30bc69ce9ada064e1cd4367813a5d96559c7627f7514801084d974c5a98c00b8b18afd555099952f97911209c124c7bbe2aad6bd65496808d7cae2d05d410c7db70ed7d7a0a472413e8e09daa42da6426f89ba40d5676698e1864ba390455068e1b26690517f6d5545bd5492e457112125ec517c29bfbd8905518079d99bc4bff74d5dba50463e2"; //测试
	
	
	static
	{
		payMap.put("0", "华为钱包");
		payMap.put("1", "充值卡");
		payMap.put("2", "游戏点卡");
		payMap.put("3", "信用卡");
		payMap.put("4", "支付宝");
		payMap.put("17", "微信");
		payMap.put("6", "短代");
		payMap.put("12", "天翼");
		payMap.put("13", "PayPal");
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
	
	/**
	 * 将除了sign的所有请求参数按照key的ASCII码进行升序排列，然后拼接成 
	 * key1=value1&key2=value2&key3=value3..... 
	 * 如果参数为null或空，写成key1=&key2=value2&key3=value3 
	 * @param params 上面获取的所有请求参数 
	 * @return 用于签名校验的字符串
	 */
	public static String getSignData(Map<String, Object> params)
	{
	    StringBuffer content = new StringBuffer();
	    // 按照key做排序  
	    List<String> keys = new ArrayList<String>(params.keySet());
	    Collections.sort(keys);
	    for (int i = 0; i < keys.size(); i++)
	    {
	        String key = (String) keys.get(i);
	        if ("sign".equals(key)) // 如果是sign字段，不作为签名校验参数  
	        {
	               
	            continue;
	        }                
	        if ("signType".equals(key)) // 如果是signType字段，不作为签名校验参数  
	        {
	            continue;
	        }                
	        String value = (String) params.get(key);
	        if (value != null)
	        {
	            content.append((i == 0 ? "" : "&") + key + "=" + value);
	        }
	        else
	        // 如果value为null，则写成：key=&key1=value1  
	        {
	            content.append((i == 0 ? "" : "&") + key + "=");
	        }
	    }
	    return content.toString();
	}
	
	/**
	 * @param request
	 * @return 本接口Content-Type是：application/x-www-form-urlencoded，对所有参数，会自动进行编码， 
	 * 接收端收到消息也会自动根据Content-Type进行解码。 同时，接口中参数在发送端并没有进行单独的URLEncode 
	 * (sign和extReserved、sysReserved参数除外)，所以，在接收端根据Content-Type解码后，即为原始的参数信息。 
	 * 但是HttpServletRequest的getParameter 
	 * ()方法会对指定参数执行隐含的URLDecoder.decode(),所以，相应参数中如果包含比如"%"，就会发生错误。 
	 * 因此，我们建议通过如下方法获取原始参数信息。* 
	 * 注：使用如下方法必须在原始ServletRequest未被处理的情况下进行，否则无法获取到信息。比如 
	 * ，在Struts情况，由于struts层已经对参数进行若干处理， 
	 * http中InputStream中其实已经没有信息，因此，本方法不适用。要获取原始信息 
	 * ，必须在原始的，未经处理的ServletRequest中进行。 
	 */
	public Map<String, Object> getValue(HttpServletRequest request)
	{
	    String line = null;
	    StringBuffer sb = new StringBuffer();
	    try
	    {
	        request.setCharacterEncoding("UTF-8");
	        InputStream stream = request.getInputStream();
	        InputStreamReader isr = new InputStreamReader(stream);
	        BufferedReader br = new BufferedReader(isr);
	        while ((line = br.readLine()) != null)
	        {
	            sb.append(line).append("\r\n");
	        }
	       // System.out.println("The original data is : " + sb.toString());
//	        PlatformSavingCenter.logger.warn("[华为SDK充值回调] [接收参数:"+sb.toString()+"]");
	        br.close();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    catch (Throwable e)
	    {
	        e.printStackTrace();
	    }
	    String str = sb.toString();
	    Map<String, Object> valueMap = new HashMap<String, Object>();
	    if (null == str || "".equals(str))
	    {
	        return valueMap;
	    }
	    String[] valueKey = str.split("&");
	    for (String temp : valueKey)
	    {
	        if (temp != null) {
	        int idx = temp.indexOf('=');
	        int len = temp.length();
	        if (idx != -1) {
	            String key = temp.substring(0, idx);
	            String value = idx+1<len ? temp.substring(idx+1) : "";
	            valueMap.put(key, value);
	        }
	           }
	    }
	    // 接口中，如下参数sign和extReserved、sysReserved是URLEncode的，所以需要decode，其他参数直接是原始信息发送，不需要decode  
	    try
	    {
	        String sign = (String) valueMap.get("sign");
	        String extReserved = (String) valueMap.get("extReserved");
	        String sysReserved = (String) valueMap.get("sysReserved");
	        if (null != sign)
	        {
	            sign = URLDecoder.decode(sign, "utf-8");
	            valueMap.put("sign", sign);
	        }
	        if (null != extReserved)
	        {
	            extReserved = URLDecoder.decode(extReserved, "utf-8");
	            valueMap.put("extReserved", extReserved);
	        }
	        if (null != sysReserved)
	        {
	            sysReserved = URLDecoder.decode(sysReserved, "utf-8");
	            valueMap.put("sysReserved", sysReserved);
	        }
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    return valueMap;
	} 
	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
			long now = System.currentTimeMillis();
			Map<String, Object> map = null;
			map = getValue(req);
			if (null == map)
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
				{
					PlatformSavingCenter.logger.info("[华为SDK充值回调] [失败] [没有参数传递] [cost:"+(System.currentTimeMillis()-now)+"ms]");
				}
				return;
			}
	
			String sign = (String) map.get("sign");
			
			ResultDomain result = new ResultDomain();
			result.setResult(1);
			String resultinfo = convertJsonStyle(result);
			//获取待签名字符串
	        String content = RSA.getSignData(map);
			PlatformSavingCenter.logger.info("[华为SDK充值回调] [接收参数] [content："+content+"] [map："+map+"]");
			if (RSA.doCheck(content, sign, devPubKey)) {
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				//查询订单
				OrderForm orderForm = orderFormManager.getOrderForm((String)map.get("requestId"));
				if(orderForm!= null)
				{
					Passport p = PassportManager.getInstance().getPassport(orderForm.getPassportId());
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台:"+orderForm.getSavingMedium()+"] [失败:此订单已经回调过了] [华为SDK orderId:"+(String)map.get("orderId")+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+p.getUserName()+"] [充值金额:"+(long)(Double.valueOf((String)map.get("amount"))*100)+"] [充值结果:"+("0".equals(((String)map.get("result"))) ?"成功":"失败")+"] [开发者社区名称:"+(String)map.get("userName")+"] [产品名称:"+(String)map.get("productName")+"] [支付类型:"+payMap.get((String)map.get("payType"))+"] [通知时间:"+(String)map.get("notifyTime")+"] [requestId:"+(String)map.get("requestId")+"] [sign:"+(String)map.get("sign")+"] [costs:"+(System.currentTimeMillis()-now)+"ms]");
							result.setResult(0);
							resultinfo = convertJsonStyle(result);
							res.setContentType("application/json");
							res.setCharacterEncoding("UTF-8");
							PrintWriter out = res.getWriter();
							out.print(resultinfo);
							out.close();
							return;
						}
					}
						//更新订单
					try {
						orderForm.setResponseTime(System.currentTimeMillis());
						if("0".equals(((String)map.get("result")))) {
							orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
							
							//以返回的充值额度为准
							orderForm.setPayMoney((long)(Double.valueOf((String)map.get("amount"))*100));
							orderForm.setChannelOrderId((String)map.get("orderId"));
							orderFormManager.update(orderForm);
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.error("[充值回调] [充值平台:"+orderForm.getSavingMedium()+"] [成功] [OK] [华为SDK orderId:"+(String)map.get("orderId")+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+p.getUserName()+"] [充值金额:"+(long)(Double.valueOf((String)map.get("amount"))*100)+"] [充值结果:"+("0".equals((String)(map.get("result")))?"成功":"失败")+"] [开发者社区名称:"+(String)map.get("userName")+"] [产品名称:"+(String)map.get("productName")+"] [支付类型:"+payMap.get((String)map.get("payType"))+"] [通知时间:"+(String)map.get("notifyTime")+"] [requestId:"+(String)map.get("requestId")+"] [sign:"+(String)map.get("sign")+"] [costs:"+(System.currentTimeMillis()-now)+"ms]");
						} else {
							orderForm.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
							orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
							
							//以返回的充值额度为准
							orderForm.setPayMoney((long)(Double.valueOf((String)map.get("amount"))*100));
							orderForm.setChannelOrderId((String)map.get("orderId"));
							orderFormManager.update(orderForm);
							PlatformSavingCenter.logger.error("[充值回调] [充值平台:"+orderForm.getSavingMedium()+"] [失败] [充值失败] [华为SDK orderId:"+(String)map.get("orderId")+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+p.getUserName()+"] [充值金额:"+(long)(Double.valueOf((String)map.get("amount"))*100)+"] [充值结果:"+("0".equals((String)(map.get("result")))?"成功":"失败")+"] [开发者社区名称:"+(String)map.get("userName")+"] [产品名称:"+(String)map.get("productName")+"] [支付类型:"+payMap.get((String)map.get("payType"))+"] [通知时间:"+(String)map.get("notifyTime")+"] [requestId:"+(String)map.get("requestId")+"] [sign:"+(String)map.get("sign")+"] [costs:"+(System.currentTimeMillis()-now)+"ms]");
						}
						
						result.setResult(0);
						resultinfo = convertJsonStyle(result);
						res.setContentType("application/json");
						res.setCharacterEncoding("UTF-8");
						PrintWriter out = res.getWriter();
						out.print(resultinfo);
						out.close();
						return;
						
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台:"+orderForm.getSavingMedium()+"] [失败] [出现异常] [华为SDK orderId:"+(String)map.get("orderId")+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+p.getUserName()+"] [充值金额:"+(long)(Double.valueOf((String)map.get("amount"))*100)+"] [充值结果:"+("0".equals((String)(map.get("result")))?"成功":"失败")+"] [开发者社区名称:"+(String)map.get("userName")+"] [产品名称:"+(String)map.get("productName")+"] [支付类型:"+payMap.get((String)map.get("payType"))+"] [通知时间:"+(String)map.get("notifyTime")+"] [requestId:"+(String)map.get("requestId")+"] [sign:"+(String)map.get("sign")+"] [costs:"+(System.currentTimeMillis()-now)+"ms]",e);
						result.setResult(1);
						resultinfo = convertJsonStyle(result);
						res.setContentType("application/json");
						res.setCharacterEncoding("UTF-8");
						PrintWriter out = res.getWriter();
						out.print(resultinfo);
						out.close();
						return;
					}
			} else {
				PlatformSavingCenter.logger.error("[充值回调] [失败] [未在数据库中找到匹配订单] [华为SDK orderId:"+(String)map.get("orderId")+"] [充值金额:"+(long)(Double.valueOf((String)map.get("amount"))*100)+"] [充值结果:"+("0".equals((String)(map.get("result")))?"成功":"失败")+"] [开发者社区名称:"+(String)map.get("userName")+"] [产品名称:"+(String)map.get("productName")+"] [支付类型:"+payMap.get((String)map.get("payType"))+"] [通知时间:"+(String)map.get("notifyTime")+"] [requestId:"+(String)map.get("requestId")+"] [sign:"+(String)map.get("sign")+"] [costs:"+(System.currentTimeMillis()-now)+"ms]");
				result.setResult(1);
				resultinfo = convertJsonStyle(result);
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				PrintWriter out = res.getWriter();
				out.print(resultinfo);
				out.close();
				return;
			}
		}
		else
		{
			PlatformSavingCenter.logger.error("[充值回调] [失败] [签名验证失败] [华为SDK orderId:"+(String)map.get("orderId")+"] [充值金额:"+(long)(Double.valueOf((String)map.get("amount"))*100)+"] [充值结果:"+("0".equals((String)(map.get("result")))?"成功":"失败")+"] [开发者社区名称:"+(String)map.get("userName")+"] [产品名称:"+(String)map.get("productName")+"] [支付类型:"+payMap.get((String)map.get("payType"))+"] [通知时间:"+(String)map.get("notifyTime")+"] [requestId:"+(String)map.get("requestId")+"] [sign:"+(String)map.get("sign")+"] [公钥:"+devPubKey+"] [costs:"+(System.currentTimeMillis()-now)+"ms]");
			result.setResult(1);
			resultinfo = convertJsonStyle(result);
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.print(resultinfo);
			out.close();
			return;
		}
	}
	
	public Map<String, Object> getValue_old(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<String> longcodeArray = new ArrayList<String>();
		List<String> messageArray = new ArrayList<String>();
		try {

			String keyandValue = "";
			String key = "";
			String value = "";
			Iterator<String> it = request.getParameterMap().keySet().iterator();
			while (it.hasNext()) {
				key = it.next();
				value = ((Object[]) (request.getParameterMap().get(key)))[0]
						.toString();
				keyandValue = key + "=" + value;
				map.put(key, value);
			}

		} catch (Exception e) {
			return null;
		}

		return map;
	}

	private String convertJsonStyle(Object resultMessage) 
	{
		ObjectMapper mapper = new ObjectMapper();
		Writer writer = new StringWriter();
		try 
		{
			if ( null != resultMessage)
			{
				mapper.writeValue(writer, resultMessage);
			}
			
		} catch (Exception e) {

		}
		return writer.toString();
	}
}
