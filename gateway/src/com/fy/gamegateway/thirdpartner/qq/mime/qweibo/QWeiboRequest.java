package com.fy.gamegateway.thirdpartner.qq.mime.qweibo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;

public class QWeiboRequest {

	public static final String HTTP_POST = "POST";
	public static final String HTTP_GET  = "GET";
	
	
	public QWeiboRequest() {

	}

	/**
	 * Do sync request.
	 * 
	 * @param url
	 *            The full url that needs to be signed including its non OAuth
	 *            url parameters
	 * @param httpMethod
	 *            The http method used. Must be a valid HTTP method verb
	 *            (POST,GET,PUT, etc)
	 * @param key
	 *            OAuth key
	 * @param listParam
	 *            Query parameters
	 * @param listPost
	 *            post parameter
	 * @return
	 * @throws Exception
	 */
	public HttpRspInfo syncRequest(String url, String httpMethod, OauthKey key,
			List<QParameter> listParam, List<QParameter> listPost)
			throws Exception {
		long startTime = System.currentTimeMillis();
		if (url == null || url.equals("")) {
			return null;
		}
		OAuth oauth = new OAuth();

		StringBuffer sbQueryString = new StringBuffer();
		String oauthUrl = oauth.getOauthUrl(url, httpMethod, key.customKey,                
				key.customSecrect, key.tokenKey, key.tokenSecrect, key.verify,
				key.callbackUrl, listParam, sbQueryString);
		
		String queryString = sbQueryString.toString();                  
		
		Header h = new Header();            //http header
		h.setName("Authorization");		
		h.setValue("OAuth " + encodeParameters(listParam, ",", true));
		
		QHttpClient http = new QHttpClient();   
		if ("GET".equals(httpMethod)) {
			return http.httpGet(oauthUrl, null , h);
		} else if ((listPost == null) || (listPost.size() == 0)) {
			return http.httpPost(oauthUrl, h, null);
		} else {			
			return http.httpPost(oauthUrl, h, listPost);
		}
	}
	
	  public static String encodeParameters(List<QParameter> postParams, String splitter, boolean quot) {
	        StringBuffer buf = new StringBuffer();
	        for (QParameter param : postParams) {
	            if (buf.length() != 0) {
	                if (quot) {
	                    buf.append("\"");
	                }
	                buf.append(splitter);
	            }
	            buf.append(encode(param.mName)).append("=");
	            if (quot) {
	                buf.append("\"");
	            }
	            buf.append(
	                    encode(param.mValue));
	        }
	        if (buf.length() != 0) {
	            if (quot) {
	                buf.append("\"");
	            }
	        }
	        return buf.toString();
	    }
	  
	    public static String encode(String value) {
	        String encoded = null;
	        try {
	            encoded = URLEncoder.encode(value, "UTF-8");
	        } catch (UnsupportedEncodingException ignore) {
	        }
	        StringBuffer buf = new StringBuffer(encoded.length());
	        char focus;
	        for (int i = 0; i < encoded.length(); i++) {
	            focus = encoded.charAt(i);
	            if (focus == '*') {
	                buf.append("%2A");
	            } else if (focus == '+') {
	                buf.append("%20");
	            } else if (focus == '%' && (i + 1) < encoded.length()
	                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
	                buf.append('~');
	                i += 2;
	            } else {
	                buf.append(focus);
	            }
	        }
	        return buf.toString();
	    }

	/**
	 * Do async request
	 * 
	 * @param url
	 *            The full url that needs to be signed including its non OAuth
	 *            url parameters
	 * @param httpMethod
	 *            The http method used. Must be a valid HTTP method verb
	 *            (POST,GET,PUT, etc)
	 * @param key
	 *            OAuth key
	 * @param listParam
	 *            Query parameters
	 * @param listFile
	 *            Files for post
	 * @param asyncHandler
	 *            The async handler
	 * @param cookie
	 *            Cookie response to handler
	 * @return
	 */
	public boolean asyncRequest(String url, String httpMethod, OauthKey key,
			List<QParameter> listParam, List<QParameter> listFile,
			QAsyncHandler asyncHandler, Object cookie) {
		
		OAuth oauth = new OAuth();

		StringBuffer sbQueryString = new StringBuffer();
		String oauthUrl = oauth.getOauthUrl(url, httpMethod, key.customKey,
				key.customSecrect, key.tokenKey, key.tokenSecrect, key.verify,
				key.callbackUrl, listParam, sbQueryString);
		String queryString = sbQueryString.toString();

		QAsyncHttpClient asyncHttp = new QAsyncHttpClient();
		if ("GET".equals(httpMethod)) {
			return asyncHttp.httpGet(oauthUrl, queryString, asyncHandler,
					cookie);
		} else if ((listFile == null) || (listFile.size() == 0)) {
			return asyncHttp.httpPost(oauthUrl, queryString, asyncHandler,
					cookie);
		} else {
			return asyncHttp.httpPostWithFile(oauthUrl, queryString, listFile,
					asyncHandler, cookie);
		}
	}
}
