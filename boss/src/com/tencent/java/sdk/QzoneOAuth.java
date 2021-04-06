package com.tencent.java.sdk;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class QzoneOAuth {

	private static final String MAC_NAME = "HmacSHA1";
	private static QzoneOAuth instance;

	public static QzoneOAuth getInstance() {
		if (instance == null) {
			instance = new QzoneOAuth();
		}
		return instance;
	}

	private QzoneOAuth() {
	}
	
	/**
	 * 通过参数列表生成QzoneSDK协议的string
	 * 
	 * @param scriptName
	 *            OpenApi CGI名字
	 * @param params
	 *            OpenApi的参数列表
	 * @param protocol
	 *            HTTP请求协议 "http" / "https"
	 * @return 返回服务器响应内容
	 */
	public static String generateQZoneQueryString(String appKey, String actionName, String method, Map<String, String> params) {

		if (params == null) {
			params = new HashMap<String, String>();
		}

		// 计算签名
		String sig;
		try {
			sig = QzoneOAuth.getInstance().makeSig(method, actionName, params, appKey);
			params.put("sig", sig);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return generateQueryString(params);
	}
	
	/**
	 * change Map to QueryString
	 */
	public static String generateQueryString(Map<String, String> params) {
		if (params == null)
			return "";
		String aQueryParam = "";
		if (params.size() > 0) {
			Iterator<Map.Entry<String, String>> aKeyIterator = params.entrySet().iterator();
			while (aKeyIterator.hasNext()) {
				Map.Entry<String, String> entry = aKeyIterator.next();
				String aParamName = entry.getKey();
				String aParamValue = encode(entry.getValue());
				aQueryParam += aParamName + "=" + aParamValue + "&";
			}
			aQueryParam = aQueryParam.substring(0, aQueryParam.length() - 1);
		}
		return aQueryParam;
	}
	
	/*
	 * 生成签名所需源串
	 * 
	 * @param method HTTP请求方法 "get" / "post"
	 * 
	 * @param url_path CGI名字, eg: /v3/user/get_info
	 * 
	 * @param params URL请求参数
	 * 
	 * @return 签名所需源串
	 */
	@SuppressWarnings("deprecation")
	public String makeSource(String method, String url_path,
			Map<String, String> params) {
		Object[] keys = params.keySet().toArray();

		Arrays.sort(keys);

		StringBuilder buffer = new StringBuilder(256);
		buffer.append(method.toUpperCase()).append("&")
				.append(URLEncoder.encode(url_path)).append("&");

		StringBuilder buffer2 = new StringBuilder();

		for (int i = 0; i < keys.length; i++) {
			buffer2.append(keys[i]).append("=").append(params.get(keys[i]));

			if (i != keys.length - 1) {
				buffer2.append("&");
			}
		}

		buffer.append(encode(buffer2.toString()));

		return buffer.toString();
	}

	/*
	 * 生成签名
	 * 
	 * @param method HTTP请求方法 "get" / "post"
	 * 
	 * @param url_path CGI名字, eg: /v3/user/get_info
	 * 
	 * @param params URL请求参数
	 * 
	 * @param secret 密钥
	 * 
	 * @return 签名值
	 * 
	 * @throws Exception 不支持指定编码以及不支持指定的加密方法时抛出异常。
	 */
	public String makeSig(String method, String url_path,
			Map<String, String> params, String secret) throws Exception {
		String sig = null;
		try {
			String keyString = secret + '&';
			byte[] keyBytes = keyString.getBytes("UTF-8");
			SecretKey key = new SecretKeySpec(keyBytes, MAC_NAME);
			Mac mac = Mac.getInstance(MAC_NAME);
			mac.init(key);

			String sbs = makeSource(method, url_path, params);;
			byte[] text = sbs.getBytes("UTF-8");
			sig = base64Encode(mac.doFinal(text)).trim();

		} catch (Exception e) {
			throw new Exception(e);
		}
		return sig;
	}

	public String base64Encode(byte[] b) {
		return new String(Base64Encoder.encode(b));
	}

	/**
	 * 对value进行编码
	 * 
	 * @param value
	 * @return
	 */
	public static String encode(String value) {
		if (value == null) {
			return "";
		}
		try {
			return URLEncoder.encode(value, "UTF-8")
					// OAuth encodes some characters differently:
					.replace("+", "%20").replace("*", "%2A")
					.replace("%7E", "~");
			// This could be done faster with more hand-crafted code.
		} catch (UnsupportedEncodingException wow) {
			throw new RuntimeException(wow.getMessage(), wow);

		}

	}
}
