package com.fy.boss.platform.u8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.FileUtils;

public class Xiao7SavingCallBackServlet extends HttpServlet {

    //签名算法 
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";  

    //公钥
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCv6fbt4KLAMHtSo6DHb/bSiHfvFUVxJsrz4JjrF2MwI6AfO3vaexNaJFdqtbl3ybBYvkO865km4wOJTrh7x+VoQ0Vh6nylIzo+q0Xhu6qfcd2VUUDZJO9tXxHyOpbf15gehS5x+EMwA/5iHGflEBTdw0PHCRHKKBXIaWlIGu6KPwIDAQAB";

    //RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;
    
    public static boolean verify(String content, String sign, String publicKey, String input_charset, String algorithm)
    {
      try
      {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = com.fy.boss.platform.huawei.util.Base64.decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        

        Signature signature = 
          Signature.getInstance(algorithm);
        
        signature.initVerify(pubKey);
        signature.update(content.getBytes(input_charset));
        
        return signature.verify(com.fy.boss.platform.huawei.util.Base64.decode(sign));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      return false;
    }
    
    private static String buildHttpQueryNoEncode(Map<String, String> data) throws UnsupportedEncodingException {
        String builder = new String();
        for (Entry<String, String> pair : data.entrySet()) {
            builder += pair.getKey()+ "=" + pair.getValue() + "&";
        }
        return builder.substring(0, builder.length() - 1);
    }
    
    private static Map<String, String> decodeHttpQueryNoDecode(String httpQuery) throws UnsupportedEncodingException {	
    	Map<String, String> map = new TreeMap<String, String>();
    	
    	for(String s: httpQuery.split("&")) {
    		String pair[] = s.split("=");
    		map.put(pair[0], pair[1]);
    	}
    	return map;
    }
    
    //Base64解码
    public static byte[] baseDecode(String str) {  
        return Base64.decodeBase64(str.getBytes());  
    }  
  
	//Base64编码  
    
    //从字符串加载公钥
    public static PublicKey loadPublicKeyByStr() throws Exception {
        try {
            String publicKeyStr = "";
            
            int count = 0;
            for (int i = 0; i < PUBLIC_KEY.length(); ++i)
            {
            	if (count < 64)
            	{
            		publicKeyStr += PUBLIC_KEY.charAt(i);
            		count++;
            	}
            	else
            	{
            		publicKeyStr += PUBLIC_KEY.charAt(i) + "\r\n";
            		count = 0;
            	}
            }
            byte[] buffer = baseDecode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            //System.out.println(publicKey);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }
    
    public static byte[] publicKeyDecrypt(PublicKey publicKey, byte[] cipherData) throws Exception {
        if (publicKey == null) {
            throw new Exception("解密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
 
            int inputLen = cipherData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(cipherData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(cipherData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;                
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }
    
    //RSA验签名检查   
    public static boolean verifySign(String content, String sign, PublicKey publicKey)  
    {  
        try   
        {  
        	java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);  
          
            signature.initVerify(publicKey);
            //System.out.println(content.getBytes());
            signature.update(content.getBytes());
          
            boolean bverify = signature.verify(baseDecode(sign));  
            return bverify;  
              
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
          
        return false;  
    }
    
	protected void service(HttpServletRequest req, HttpServletResponse res)
	{
		long startTime = System.currentTimeMillis(); 
		try{
			req.setCharacterEncoding("utf-8");
			String str2 = FileUtils.getContent(req.getInputStream());
			
			String queryString = str2;
	    	String[] queryStringArr=queryString.split("&");
			String[] queryItemArr=new String[2];
			String[] queryKeyArr= {"encryp_data","extends_info_data","game_area","game_level","game_orderid","game_role_id","game_role_name","sdk_version","subject","xiao7_goid","sign_data"};
				Map<String, String> map = new TreeMap<String, String>();
				String tempStr="";
				Arrays.sort(queryKeyArr);
				for(String str : queryStringArr){
					queryItemArr=str.split("=");
					if(Arrays.binarySearch(queryKeyArr, queryItemArr[0])>=0) {
						tempStr="";
						if(queryItemArr.length==2) {
							tempStr=queryItemArr[1];
						}
						map.put(queryItemArr[0],URLDecoder.decode(tempStr,"utf-8"));
					}
				}
				for(String q_key : queryKeyArr){
					if(map.containsKey(q_key)!=true) {
						System.out.print("failed:order error");
						System.exit(0);
					}
				}
				String sign = map.get("sign_data");
				map.remove("sign_data");
				String sourceStr = buildHttpQueryNoEncode(map);
				//验签
				if (!verifySign(sourceStr, sign, loadPublicKeyByStr()) )
				{
					res.getWriter().write("sign_data_verify_failed");
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：小7] [失败:签名不正确] [str:"+str2+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					return;
				}
				//解密
				String decryptData = new String(publicKeyDecrypt(loadPublicKeyByStr(), baseDecode(map.get("encryp_data"))));
				/*************************************************************
				 * 下面这里将会返回是一个包含game_orderid、guid、pay_price的双列集合
				 * {game_orderid=xxxx, guid=xxxx, pay_price=xxxxx}
				 *************************************************************/
				Map<String, String> decryptMap = decodeHttpQueryNoDecode(decryptData);
				/******************************************************
				 * 这里需要判断是否存在game_orderid、pay_price、guid三个值。
				 ******************************************************/
				if (!decryptMap.containsKey("game_orderid") || !decryptMap.containsKey("pay_price") || !decryptMap.containsKey("guid"))
				{
					res.getWriter().write("encryp_data_decrypt_failed");
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：小7] [失败:解密encryp_data失败] [str:"+str2+"] [decryptMap:"+decryptMap+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					return;
				}
				/*********************************************************************
				 * 对比一下解出来的订单号与传递过来的订单号是否一致。这里同时要比较一下当前订单号是否是属于当前小7渠道。
				 ********************************************************************/
				if(!decryptMap.get("game_orderid").equals(map.get("game_orderid"))){
					res.getWriter().write("failed:game_orderid error");
					PlatformSavingCenter.logger.error("[充值回调] [充值平台：小7] [失败:解密出来的订单和传递过来的不一致] [str:"+str2+"] [解密id:"+decryptMap.get("game_orderid")+"] [传递id:"+map.get("game_orderid")+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					return;
				}
				
//				if(!decryptMap.get("pay_price").equals(map.get("game_price"))){
//					res.getWriter().write("failed:pay_price error");
//					PlatformSavingCenter.logger.error("[充值回调] [充值平台：小7] [失败:解密出来的金额和传递过来的不一致] [str:"+str2+"] [解密金额:"+decryptMap.get("pay_price")+"] [传递金额:"+map.get("game_price")+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
//					return;
//				}
				/********************************************************************
				 * 下面这里是一系列的比较（这里需要游戏方对这一系列的参数进行比较，对于每个参数代表的意思，请查看文档。）
				 * "game_area"		=>"game_area error",
				 * "game_orderid"	=>"game_orderid error",
				 * "game_role_id"	=>"game_role_id error",
				 * "game_role_name"	=>"game_role_name error",
				 * "guid"			=>"guid error",
				 * "xiao7_goid"		=>"xiao7_goid error",
				 * "pay_price"		=>"pay_price error",
				 *********************************************************************/
				/***************************************************************************************************************************
				 * 这里比较重要的是要将解密得到的价格pay_price与CP中指定订单中的商品的价格（这个价格的值必须是游戏方定义的值，不能是从前端接收到的值，因为从前端接收到的值是有可能会被篡改的），
				 * 注意这里游戏方务必是需要比较订单价格是否与当期那订单对应。
				 * 对于价格小7服务器是精确到小数点后面两位返回的。
				 * 对于已经给【小7服务器】返回过正确响应并已经发货的订单但是由于【小7服务器】的一些原因还会给游戏支付回调地址发送支付回调请求，这时候游戏厂商无需重新发货，
				 * 对比支付信息其中比较重要一点是对比一下xiao7_goid是否与第一次收到的xiao7_goid一致，
				 * 如果不一致直接返回failed:xiao7_goid error错误，如果对比所有信息无误后直接返回一个success。
				 * 对于一切都是正确就返回success（注意是小写）字符串
				 ***************************************************************************************************************************/
			
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(map.get("game_orderid"));
				if(orderForm != null)
				{
					PassportManager passportManager = PassportManager.getInstance();
					
					Passport passport = passportManager.getPassport(orderForm.getPassportId());
					String username = "";
					
					if(passport != null)
					{
						username = passport.getUserName();
					}
					
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：小7] [失败:此订单已经回调过] [orderId:"+orderForm.getOrderId()+"] [id:"+orderForm.getId()+"] [充值金额:"+decryptMap.get("pay_price")+"] [支付结果:--]  [小7id:"+decryptMap.containsKey("guid")+"] [username:"+username+"] [传入内容:"+str2+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							res.getWriter().write("success");
							return;
						}
					}
					orderForm.setResponseTime(System.currentTimeMillis());
					orderForm.setMemo2(str2);
					orderForm.setMediumInfo(decryptMap.get("game_orderid")+"@"+decryptMap.get("pay_price")+"@"+decryptMap.containsKey("guid"));
					//以解密出来的金额为准
					orderForm.setPayMoney((long)Double.parseDouble(decryptMap.get("pay_price")) * 100);
					orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					
					try {
						orderFormManager.update(orderForm);
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：小7] [失败:更新订单信息时报错] [orderId:"+orderForm.getOrderId()+"]  [充值金额:"+decryptMap.get("pay_price")+"] [支付结果:--]  [小7id:"+decryptMap.containsKey("guid")+"] [username:"+username+"] [传入内容:"+str2+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						res.getWriter().write("failed:order error ");
						return;
					}
						PlatformSavingCenter.logger.warn("[充值回调] [充值平台：小7] [成功] [orderId:"+orderForm.getOrderId()+"]  [充值金额:"+decryptMap.get("pay_price")+"] [支付结果:--]  [小7id:"+decryptMap.containsKey("guid")+"] [username:"+username+"] [传入内容:"+str2+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("success");
					return;
				}
				else
				{
		
					PlatformSavingCenter.logger.warn("[充值回调] [充值平台：小7] [失败:查询订单失败,无匹配订单]  [充值金额:"+decryptMap.get("pay_price")+"] [支付结果:--]  [小7id:"+decryptMap.containsKey("guid")+"] [username:--] [传入内容:"+str2+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					res.getWriter().write("failed:xiao7_goid error");
					return;
				}
			
		}catch(Exception e){
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：小7] [失败:出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			try {
				res.getWriter().write("failed:order error");
			} catch (IOException e1) {
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：小7] [失败:响应失败信息时出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			return;
		}
		
	}
	
}
