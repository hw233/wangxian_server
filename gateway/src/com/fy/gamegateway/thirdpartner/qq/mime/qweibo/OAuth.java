package com.fy.gamegateway.thirdpartner.qq.mime.qweibo;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.thirdpartner.qq.mime.qweibo.utils.Base64Encoder;

public class OAuth implements Serializable {
	static final long serialVersionUID = -8736851024315996L;
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);
	private static final String OAuthVersion = "1.0";
	private static final String OAuthQParameterPrefix = "oauth_";
	private static final String OAuthConsumerKeyKey = "oauth_consumer_key";
	private static final String OAuthCallbackKey = "oauth_callback";
	private static final String OAuthVersionKey = "oauth_version";
	private static final String OAuthSignatureMethodKey = "oauth_signature_method";
	private static final String OAuthSignatureKey = "oauth_signature";
	private static final String OAuthTimestampKey = "oauth_timestamp";
	private static final String OAuthNonceKey = "oauth_nonce";
	private static final String OAuthTokenKey = "oauth_token";
	private static final String oAauthVerifier = "oauth_verifier";
	private static final String OAuthTokenSecretKey = "oauth_token_secret";
	private static final String HMACSHA1SignatureType = "HmacSHA1";
	private static final String HMACSHA1SignatureType_TEXT = "HMAC-SHA1";
	private Random random = new Random();
	private String unreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~";
	
	private static final Pattern AUTHORIZATION = Pattern.compile("\\s*(\\w*)\\s+(.*)");
    private static final Pattern NVP 		   = Pattern.compile("(\\S*)\\s*\\=\\s*\"([^\"]*)\"");
    public static final String AUTH_SCHEME     = "OAuth";
    public static final String ENCODING 	   = "UTF-8";


	/**
	 * Get the URL based on the specified key.
	 * 
	 * @param url
	 *            The full url that needs to be signed including its non OAuth
	 *            url parameters
	 * @param httpMethod
	 *            The http method used. Must be a valid HTTP method verb
	 *            (POST,GET,PUT, etc)
	 * @param customKey
	 *            The consumer key
	 * @param customSecrect
	 *            The consumer seceret
	 * @param tokenKey
	 *            The token, if available. If not available pass null or an
	 *            empty string
	 * @param tokenSecrect
	 *            The token secret, if available. If not available pass null or
	 *            an empty string
	 * @param verify
	 *            The oAauth Verifier.
	 * @param callbackUrl
	 *            The OAuth Callback URL(You should encode this url if it
	 *            contains some unreserved characters).
	 * @param parameters
	 * @param queryString
	 * @return
	 */
	public String getOauthUrl(String url, String httpMethod, String customKey,
			String customSecrect, String tokenKey, String tokenSecrect,
			String verify, String callbackUrl, List<QParameter> parameters,
			StringBuffer queryString) {
		long startTime = System.currentTimeMillis();
		String parameterString = normalizeRequestParameters(parameters);
		decodeRequestParameters(parameters);

		String urlWithQParameter = url;
		if (parameterString != null && !parameterString.equals("")) {
			urlWithQParameter += "?" + parameterString;
		}
		
		URL aUrl = null;
		try {
			aUrl = new URL(urlWithQParameter);
		} catch (MalformedURLException e) {
			System.err.println("URL parse error:" + e.getLocalizedMessage());
		}
		String nonce = generateNonce();
		String timeStamp = generateTimeStamp();

		parameters.add(new QParameter(OAuthVersionKey, OAuthVersion));
		parameters.add(new QParameter(OAuthNonceKey, nonce));
		parameters.add(new QParameter(OAuthTimestampKey, timeStamp));
		parameters.add(new QParameter(OAuthSignatureMethodKey,
				HMACSHA1SignatureType_TEXT));
		parameters.add(new QParameter(OAuthConsumerKeyKey, customKey));

		if (tokenKey != null && !tokenKey.equals("")) {
			parameters.add(new QParameter(OAuthTokenKey, tokenKey));
		}

		if (verify != null && !verify.equals("")) {
			parameters.add(new QParameter(oAauthVerifier, verify));
		}

		if (callbackUrl != null && !callbackUrl.equals("")) {
			parameters.add(new QParameter(OAuthCallbackKey, callbackUrl));
		}

		StringBuffer normalizedUrl = new StringBuffer();
		String signature = generateSignature(aUrl, customSecrect, tokenSecrect,
				httpMethod, parameters, normalizedUrl, queryString);

		queryString.append("&oauth_signature=");
		queryString.append(encode(signature));

		parameters.add(new QParameter(OAuthSignatureKey, signature));
		
		return normalizedUrl.toString();
	}

	/**
	 * Normalizes the request parameters according to the spec.
	 * 
	 * @param parameters
	 *            The list of parameters already sorted
	 * @return a string representing the normalized parameters
	 */
	public String normalizeRequestParameters(List<QParameter> parameters) {
		StringBuffer sb = new StringBuffer();
		QParameter p = null;
		for (int i = 0, size = parameters.size(); i < size; i++) {
			p = parameters.get(i);
			sb.append(p.mName);
			sb.append("=");
			sb.append(p.mValue);

			if (i < size - 1) {
				sb.append("&");
			}
		}

		return sb.toString();
	}
	
	void decodeRequestParameters(List<QParameter> parameters) {		
		QParameter p = null;
		for (int i = 0, size = parameters.size(); i < size; i++) {
			p = parameters.get(i);						
			p.mValue = decode(p.mValue);
			}
	}

	/**
	 * Generate the timestamp for the signature.
	 * 
	 * @return
	 */
	private String generateTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

    private static Random RAND = new Random();

	/**
	 * Just a simple implementation of a random number between 123400 and
	 * 9999999
	 * 
	 * @return
	 */
	public synchronized String generateNonce() {
		//return String.valueOf(random.nextInt(9876599) + 123400);
    	long nonce 	   = 0;    
    	long timestamp = System.currentTimeMillis() / 1000;
    	nonce		   = timestamp + RAND.nextLong();
    	        
        return String.valueOf(nonce);
	}


	/**
	 * @param value
	 *            string to be encoded
	 * @return encoded string
	 * @see <a href="http://wiki.oauth.net/TestCases">OAuth / TestCases</a>
	 * @see <a
	 *      href="http://groups.google.com/group/oauth/browse_thread/thread/a8398d0521f4ae3d/9d79b698ab217df2?hl=en&lnk=gst&q=space+encoding#9d79b698ab217df2">Space
	 *      encoding - OAuth | Google Groups</a>
	 * @see <a href="http://tools.ietf.org/html/rfc3986#section-2.1">RFC 3986 -
	 *      Uniform Resource Identifier (URI): Generic Syntax - 2.1.
	 *      Percent-Encoding</a>
	 */
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
					&& encoded.charAt(i + 1) == '7'
					&& encoded.charAt(i + 2) == 'E') {
				buf.append('~');
				i += 2;
			} else {
				buf.append(focus);
			}
		}
		return buf.toString();
	}
	
    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, ENCODING);
            // This implements http://oauth.pbwiki.com/FlexibleDecoding
        } catch (java.io.UnsupportedEncodingException wow) {
            throw new RuntimeException(wow.getMessage(), wow);
        }
    }

	/**
	 * Generates a signature using the HMAC-SHA1 algorithm
	 * 
	 * @param url
	 *            The full url that needs to be signed including its non OAuth
	 *            url parameters
	 * @param consumerSecret
	 *            The consumer seceret
	 * @param tokenSecret
	 *            The token secret, if available. If not available pass null or
	 *            an empty string
	 * @param httpMethod
	 *            The http method used. Must be a valid HTTP method verb
	 *            (POST,GET,PUT, etc)
	 * @param parameters
	 * @param normalizedUrl
	 * @param normalizedRequestParameters
	 * @return A base64 string of the hash value
	 */
	private String generateSignature(URL url, String consumerSecret,
			String tokenSecret, String httpMethod, List<QParameter> parameters,
			StringBuffer normalizedUrl, StringBuffer normalizedRequestParameters) {

		String signatureBase = generateSignatureBase(url, httpMethod,
				parameters, normalizedUrl, normalizedRequestParameters);
		byte[] oauthSignature = null;
		try {
			Mac mac = Mac.getInstance(HMACSHA1SignatureType);
			String oauthKey = encode(consumerSecret)
					+ "&"
					+ ((tokenSecret == null || tokenSecret.equals("")) ? ""
							: encode(tokenSecret));
			SecretKeySpec spec = new SecretKeySpec(
					oauthKey.getBytes("US-ASCII"), HMACSHA1SignatureType);
			mac.init(spec);
			
			//System.out.println("When getSignature:key:" + oauthKey + " bstring:" + signatureBase);
			oauthSignature = mac.doFinal(signatureBase.getBytes("US-ASCII"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return Base64Encoder.encode(oauthSignature);
	}
	
	/**
	 * ���key����ݣ�����һ��ǩ��
	 * @param secret
	 * @param data
	 * @return
	 */
	public String generateSignatureByKey(String secret , String data , boolean bHex) {

		byte[] oauthSignature = null;
		try {
			Mac mac = Mac.getInstance(HMACSHA1SignatureType);
			SecretKeySpec spec = new SecretKeySpec(
					secret.getBytes("US-ASCII"), HMACSHA1SignatureType);
			mac.init(spec);			
			oauthSignature = mac.doFinal(data.getBytes("US-ASCII"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (bHex)
			return bytes2HexStr(oauthSignature);
		else
			return Base64Encoder.encode(oauthSignature);
	}

	/**
	 * Generate the signature base that is used to produce the signature
	 * 
	 * @param url
	 *            The full url that needs to be signed including its non OAuth
	 *            url parameters
	 * @param httpMethod
	 *            The http method used. Must be a valid HTTP method verb
	 *            (POST,GET,PUT, etc)
	 * @param parameters
	 * @param normalizedUrl
	 * @param normalizedRequestParameters
	 * @return
	 */
	private String generateSignatureBase(URL url, String httpMethod,
			List<QParameter> parameters, StringBuffer normalizedUrl,
			StringBuffer normalizedRequestParameters) {

		Collections.sort(parameters);

		normalizedUrl.append(url.getProtocol());
		normalizedUrl.append("://");
		normalizedUrl.append(url.getHost());
		if ((url.getProtocol().equals("http") || url.getProtocol().equals(
				"https"))
				&& url.getPort() != -1) {
			normalizedUrl.append(":");
			normalizedUrl.append(url.getPort());
		}
		normalizedUrl.append(url.getPath());		
		normalizedRequestParameters.append(formEncodeParameters(parameters));

		StringBuffer signatureBase = new StringBuffer();
		signatureBase.append(httpMethod.toUpperCase());
		signatureBase.append("&");
		signatureBase.append(encode(normalizedUrl.toString()));
		signatureBase.append("&");
		signatureBase.append(encode(normalizedRequestParameters.toString()));
		
		String qry = url.getQuery();//��ɴ��ѯ�����url
		if (qry != null && !qry.isEmpty())
		{
			normalizedUrl.append("?");
			normalizedUrl.append(qry);
		}

		return signatureBase.toString();
	}

	/**
	 * Encode each parameters in list.
	 * 
	 * @param parameters
	 *            List of parameters
	 * @return Encoded parameters
	 */
	private String formEncodeParameters(List<QParameter> parameters) {
		List<QParameter> encodeParams = new ArrayList<QParameter>();
		for (QParameter a : parameters) {
			encodeParams.add(new QParameter(a.mName, encode(a.mValue)));
		}

		return normalizeRequestParameters(encodeParams);
	}	
	
	
	/**
	 * ��http�����л�ȡOAuth����
	 * @param request
	 * @param bNotByHeader		OAuth������Httpͷ����
	 * @return
	 */
	public static Map<String, String> getOAuthPararmMap(HttpServletRequest request , boolean bNotByHeader)
	{
		Map<String, String> m = new HashMap<String, String>();
		
		for (Enumeration<String> headers = request
				.getHeaders("Authorization"); headers != null
				&& headers.hasMoreElements();) {
			String header = headers.nextElement();
			for (QParameter parameter : decodeAuthorization(header)) {
				if (!"realm".equalsIgnoreCase(parameter.mName)) {
					m.put(parameter.mName, parameter.mValue);
				}
			}
		}
	
		if (bNotByHeader)//����в�����header���棬���д�header�����ȡ
		{
	        for (Object e : request.getParameterMap().entrySet()) 
	        {
	            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;
	            String name = entry.getKey();
	            for (String value : entry.getValue()) {
	                m.put(name, decode(value));
	            }
	        }
			
		}
                
        return m;		
	}
	
	public boolean verifySignature(Map<String, String> m , String url , String qryString ,String httpMethod ,
										  String customSecrect , String tokenSecrect)
	{
		String temp = m.get(OAuthSignatureKey);		
		if (temp == null || temp.isEmpty())
			return false;
		
		String signature = new String(temp);
		m.remove(OAuthSignatureKey);
		
		List<QParameter> listParamater = new ArrayList<QParameter>();
		for (Map.Entry<String, String> e : m.entrySet())
		{
			QParameter p = new QParameter(e.getKey() , e.getValue());
			listParamater.add(p);			
		}		
		
		String urlWithQParameter = url;
		if (qryString != null && !qryString.isEmpty()) {
			urlWithQParameter += "?" + qryString;
		}
		
		URL aUrl = null;
		try {
			aUrl = new URL(urlWithQParameter);
		} catch (MalformedURLException e) {
			System.err.println("URL parse error:" + e.getLocalizedMessage());
			return false;
		}
		
		StringBuffer normalizedUrl = new StringBuffer();
		StringBuffer queryString   = new StringBuffer();
		String genSignature           = generateSignature(aUrl, customSecrect, tokenSecrect,
									    httpMethod, listParamater, normalizedUrl, queryString);
		
		return genSignature.equals(signature);
	}			
	
    public static List<QParameter> decodeAuthorization(String authorization) {
        List<QParameter> into = new ArrayList<QParameter>();
        if (authorization != null) {
            Matcher m = AUTHORIZATION.matcher(authorization);
            if (m.matches()) {
                if (AUTH_SCHEME.equalsIgnoreCase(m.group(1))) {
                    for (String nvp : m.group(2).split("\\s*,\\s*")) {
                        m = NVP.matcher(nvp);
                        if (m.matches()) {
                            String name  = OAuth.decodePercent(m.group(1));
                            String value = OAuth.decodePercent(m.group(2));
                            into.add(new QParameter(name, value));
                        }
                    }
                }
            }
        }
        
        return into;
    }
    
    public static String decodePercent(String s) {
        try {
            return URLDecoder.decode(s, ENCODING);
            // This implements http://oauth.pbwiki.com/FlexibleDecoding
        } catch (java.io.UnsupportedEncodingException wow) {
            throw new RuntimeException(wow.getMessage(), wow);
        }
    }
    
    /**
     * ��������post����
     * @param request http����
     * @return		  �����б�
     */
    public Map<String,String> getPostParameter(HttpServletRequest request)
    {
		try {
			String body = getBody(request.getInputStream(), request
					.getContentLength(), OAuth.ENCODING);
			if (body == null || body.isEmpty()) {
				Map<String, String> m = new HashMap<String, String>();
				for (Object e : request.getParameterMap().entrySet()) {
					Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;
					String name = entry.getKey();
					for (String value : entry.getValue()) {
						m.put(name, value);
					}
				}
                return m;

			} else
				return decodeForm(body);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
   	}
    
    private static String getBody(InputStream from, int len , String encoding) throws IOException
    {    							
    	 if (from == null) {
             return null;
         }
         try {
             StringBuilder into = new StringBuilder();
             Reader r = new InputStreamReader(from, encoding);
             char[] s = new char[1024];
             for (int n; 0 < (n = r.read(s));) {
                 into.append(s, 0, n);
             }
             return into.toString();
         }catch(Exception e)
         {
        	 e.printStackTrace();
        	 return null;
         } finally {
             from.close();
         }    	
    }
    
    private static Map<String,String> decodeForm(String form) {
    	Map<String,String> m = new HashMap<String,String>();
        if (!form.isEmpty() && form != null) {
            for (String nvp : form.split("\\&")) {
                int equals = nvp.indexOf('=');
                String name;
                String value;
                if (equals < 0) {
                    name = decodePercent(nvp);
                    value = null;
                } else {
                    name = decodePercent(nvp.substring(0, equals));
                    value = decodePercent(nvp.substring(equals + 1));
                }
                m.put(name, value);                
            }
        }
        return m;
    }  
    
    private static final char[] digits = new char[] { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String bytes2HexStr(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}

		char[] buf = new char[2 * bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			buf[2 * i + 1] = digits[b & 0xF];
			b = (byte) (b >>> 4);
			buf[2 * i + 0] = digits[b & 0xF];
		}
		return new String(buf);
	}
}
