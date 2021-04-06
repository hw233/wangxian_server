package com.fy.boss.platform.u8;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class AnJiuSavingCallBackServlet extends HttpServlet {

    String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3hXcNfuM2lc0WwV2DmkLXunqpmopCtsM2EwS6jxjnQ90XORLn4YJqCQJOruNc6wZdCm9+ENyZf9GDpfx4eciMsMYwMIluaZm4S8WO5J5zRUp3KCuHakW1NCvfmgy2hRwUZw1jZisehcZe/WeedUtcxuHOdqKTucWzP2pe53xVuQIDAQAB";
    String appSecret = "d78a9de975a42676ebe5bdc18ede97dc";

    private static String buildHttpQueryNoEncode(Map<String, String> data) throws UnsupportedEncodingException {
        String builder = new String();
        for (Entry<String, String> pair : data.entrySet()) {
            builder += pair.getKey()+ "=" + pair.getValue() + "&";
        }
        return builder.substring(0, builder.length() - 1);
    }
    
	protected void service(HttpServletRequest req, HttpServletResponse res){
		long startTime = System.currentTimeMillis(); 
		try{
//			orderid=15311186875672960&username=d259630776&gameid=4191&roleid=1100000000000370716&serverid=测试服&paytype=zfb&amount=1.0&
//			paytime=1531118687&attach=anjiu20180709-1199000000000206849&sign=3eef29e8a441c3904e0cd4f99046945f
			
			req.setCharacterEncoding("utf-8");
			String str2 = FileUtils.getContent(req.getInputStream());
			String queryString = str2;
	    	String[] queryStringArr=queryString.split("&");
			String[] queryItemArr=new String[2];
//			String[] queryKeyArr= {"orderid","username","gameid","roleid","serverid","paytype","amount","paytime","attach","sign"};
			Map<String, String> map = new TreeMap<String, String>();
			String tempStr="";
			//Arrays.sort(queryKeyArr);
			for(String str : queryStringArr){
				queryItemArr=str.split("=");
//				if(Arrays.binarySearch(queryKeyArr, queryItemArr[0])>=0) {
					tempStr="";
					if(queryItemArr.length==2) {
						tempStr=queryItemArr[1];
					}
					map.put(queryItemArr[0],URLDecoder.decode(tempStr,"utf-8"));
//				}
			}
			String sign = map.get("sign");
			String sourceStr = str2.split("&sign=")[0] + "&appkey=10233e9dbc6985ed6d85e9d590bc4795";
			
			String username = map.get("username");
			String amount = map.get("amount");
			String attach = map.get("attach");
			String roleid = map.get("roleid");
//			String username = ParamUtils.getParameter(req, "username","");
//			String gameid = req.getParameter("gameid");
//			String roleid = req.getParameter("roleid");
//			String serverid = req.getParameter("serverid");
//			String paytype = req.getParameter("paytype");
//			String amount = req.getParameter("amount");
//			String paytime = req.getParameter("paytime");
//			String attach = req.getParameter("attach");
//			String sign = req.getParameter("sign");
//			sign=MD5(“orderid=100000&username=zhangsan&gameid
//					=6&roleid=zhangsanfeng&serverid=1&paytype=1&amount=1&
//					paytime=20130101125612&attach=test&appkey=12312312321 3”)
//			StringBuilder sb = new StringBuilder();
//	        sb.append("orderid=").append(orderid).append("&").append("username=").append(username).append("&").
//	        append("gameid=").append(gameid).append("&").append("roleid=").append(roleid).append("&").
//	        append("serverid=").append(serverid).append("&").append("paytype=").append(paytype).append("&")
//	        .append("amount=").append(amount).append("&").append("paytime=").append(paytime).append("&")
//	        .append("attach=").append(attach).append("&").append("appkey=").append("10233e9dbc6985ed6d85e9d590bc4795");
	        String mysign = StringUtil.hash(sourceStr);
	        if(mysign.equals(sign)){

				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(attach);
				if(orderForm != null)
				{
					PassportManager passportManager = PassportManager.getInstance();
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：安久] [失败:此订单已经回调过] [orderId:"+attach+"] [username:"+username+"] [充值金额:"+amount+"] [传入内容:"+queryString+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("success");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2(sourceStr);
					orderForm.setMediumInfo(attach+"@"+amount+"@"+roleid);
					orderForm.setPayMoney((long)Double.parseDouble(amount) * 100);
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：安久] [失败:更新订单信息时报错] [orderId:"+attach+"]  [充值金额:"+amount+"] [str:"+queryString+"] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("FAIL");
						return;
					}
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：安久] [成功] [orderId:"+attach+"]  [充值金额:"+amount+"] [username:"+username+"] [传入内容:"+queryString+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("success");
					return;
				}
				else
				{
		
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：安久] [失败:查询订单失败,无匹配订单] [orderId:"+attach+"]  [充值金额:"+amount+"] [str:"+queryString+"] [username:--] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("success");
					return;
				}
			
	        }else{
	        	PlatformSavingCenter.logger.error("[充值回调] [充值平台：安久] [失败:签名不正确] [mysign:"+mysign+"] [sign:"+sign+"] [sourceStr:"+sourceStr+"] [str:"+queryString+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
	        }
		}catch(Exception e){
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：安久] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("FAIL");
			} catch (IOException e1) {
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：安久] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
	}
	
}
