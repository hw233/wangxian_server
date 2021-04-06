/**
 * 
 */
package com.fy.gamegateway.thirdpartner.oppo;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.fy.gamegateway.tools.JacksonManager;
import com.qqv3.open.ErrorCode;
import com.qqv3.open.OpensnsException;
import com.xuanzhi.tools.servlet.HttpUtils;
/**
 * @author Administrator
 *
 */
public class OppoClientService {
	
	
	static Logger logger = NewLoginHandler.logger;
	
	public static final String OAUTH_CONSUMER_KEY = "oauthConsumerKey";
	public static final String OAUTH_TOKEN = "oauthToken";
	public static final String OAUTH_SIGNATURE_METHOD = "oauthSignatureMethod";
	public static final String OAUTH_SIGNATURE = "oauthSignature";
	public static final String OAUTH_TIMESTAMP = "oauthTimestamp";
	public static final String OAUTH_NONCE = "oauthNonce";
	public static final String OAUTH_VERSION = "oauthVersion";
	public static final String CONST_SIGNATURE_METHOD = "HMAC-SHA1";
	public static final String CONST_OAUTH_VERSION = "1.0";
	
	
	public static final String appid = "3657362";
	public static final String AppKey = "drxCTmwpTS008Ooogckos8004";
	public static final String AppSecret = "B96f2DC550A0E0D49D63F2AC9A3379c3";
	public static final String url = "http://i.open.game.oppomobile.com/gameopen/user/fileIdInfo";
	public static Random random = new Random();
	
	private static OppoClientService self;
	public static OppoClientService getInstance(){
		if(self == null){
			self = new OppoClientService();
		}
		return self;
	}
	
	public String getVivoLogin(String username,String token){
		long startTime = System.currentTimeMillis();
		String returnContent = "";
		try {
			String url = "https://usrsys.vivo.com.cn/sdk/user/auth.do?authtoken="+token;
			HashMap headers = new HashMap();
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url),  headers, 60000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			returnContent = new String(bytes,encoding);
            JacksonManager jm = JacksonManager.getInstance();
			try {
				Map map =(Map)jm.jsonDecodeObject(returnContent);
				int resultCode = (Integer)map.get("retcode");
				HashMap data = (HashMap)map.get("data");
				if(resultCode == 0 && data != null && data.size() > 0){
					String openid = (String)data.get("openid");
					if(openid != null){
						logger.info("[调用VIVO登录接口] [验证数据] [成功] [openid:"+openid+"] [token:"+token+"] [url:"+url+"] [返回数据:"+returnContent+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
						return openid;
					}
				}
		        logger.info("[调用VIVO登录接口] [验证数据] [失败] [url:"+url+"] [返回数据:"+returnContent+"] [code:"+code+"] [message:"+message+"] [token:"+token+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}catch (Exception e) {
				e.printStackTrace();
				logger.info("[解析VIVO接口信息] [异常] [userName:"+username+"] [url:"+url+"]  [返回内容:"+returnContent+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
			}
		} catch (Exception e) {
			logger.error("[调用VIVO登录接口] [异常] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}
	
	public boolean checkLogin(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String returnContent = "";
		try {
			String token = pwd;
			String ssoid = username.split("OPPOUSER_")[1];
			String baseStr = generateBaseString((startTime/1000)+"",random.nextInt(1000000)+"",token);
			String sign = generateSign(baseStr);
			String query_url = url+"?fileId="+URLEncoder.encode(ssoid ,"UTF-8")+"&token="+ URLEncoder.encode(token,"UTF-8"); //url+"?fileId=URLEncoder.encode(ssoid,"UTF-8")&token= URLEncoder.encode(token,"UTF-8")";
			
			HashMap headers = new HashMap();
			headers.put("param",baseStr);
			headers.put("oauthSignature",sign);
			
			byte bytes[] = HttpUtils.webGet(new java.net.URL(query_url),  headers, 60000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			returnContent = new String(bytes,encoding);
			//logger.info("[调用OPPO登录接口] [返回数据] [成功] [query_url:"+query_url+"] ["+returnContent+"] [baseStr:"+baseStr+"] [sign:"+sign+"] [code:"+code+"] ["+message+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
            try {
            	JSONObject jo = new JSONObject(returnContent);
            	 int resultCode = jo.optInt("resultCode", 0);
                 if(resultCode == 200 ){
                	logger.info("[调用OPPO登录接口] [验证数据] [成功] [query_url:"+query_url+"] [headers:"+headers+"] [返回数据:"+returnContent+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
                	return true;
                 } else{
                	 logger.info("[调用OPPO登录接口] [验证数据] [失败] [query_url:"+query_url+"] [返回数据:"+returnContent+"] [token:"+token+"] [ssoid:"+ssoid+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
                 }
            } catch (JSONException e) {
                throw new OpensnsException(ErrorCode.RESPONSE_DATA_INVALID, e); 
            } 
    	} catch (Exception e) {
    		e.printStackTrace();
			logger.error("[调用OPPO登录接口] [异常] [username:"+username+"] [token:"+pwd+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
	}
	
	public static String generateSign(String baseStr) throws UnsupportedEncodingException{
		byte[] byteHMAC = null;
		try {
		Mac mac = Mac.getInstance("HmacSHA1");
		SecretKeySpec spec = null;
		String oauthSignatureKey =AppSecret + "&";
		spec = new SecretKeySpec(oauthSignatureKey.getBytes(),"HmacSHA1");
		mac.init(spec);
		byteHMAC = mac.doFinal(baseStr.getBytes());
		} catch (InvalidKeyException e) {
		e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		}
		return URLEncoder.encode(String.valueOf(base64Encode(byteHMAC)) ,"UTF-8");
	}
	
	public static char[] base64Encode(byte[] data) {
		final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
		char[] out = new char[((data.length + 2) / 3) * 4];
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
		boolean quad = false;
		boolean trip = false;
		int val = (0xFF & (int) data[i]);
		val <<= 8;
		if ((i + 1) < data.length) {
		val |= (0xFF & (int) data[i + 1]);
		trip = true;
		}
		val <<= 8;
		if ((i + 2) < data.length) {
		val |= (0xFF & (int) data[i + 2]);
		quad = true;
		}
		out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
		val >>= 6;
		out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
		val >>= 6;
		out[index + 1] = alphabet[val & 0x3F];
		val >>= 6;
		out[index + 0] = alphabet[val & 0x3F];
		}
		return out;
	}
	
	public static String generateBaseString(String timestamp,String nonce,String token){
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(OAUTH_CONSUMER_KEY).
			append("=").
			append(URLEncoder.encode(AppKey,"UTF-8")).
			append("&").
			append(OAUTH_TOKEN).
			append("=").
			append(URLEncoder.encode(token,"UTF-8")).
			append("&").
			append(OAUTH_SIGNATURE_METHOD).
			append("=").
			append(URLEncoder.encode(CONST_SIGNATURE_METHOD,"UTF-8")).
			append("&").
			append(OAUTH_TIMESTAMP).
			append("=").
			append(URLEncoder.encode(timestamp,"UTF-8")).
			append("&").
			append(OAUTH_NONCE).
			append("=").
			append(URLEncoder.encode(nonce,"UTF-8")).
			append("&").
			append(OAUTH_VERSION).
			append("=").
			append(URLEncoder.encode(CONST_OAUTH_VERSION,"UTF-8")).
			append("&");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			logger.warn("[oppo获取前面原串] [异常] ",e1);
		}
		return sb.toString();
	}
}
