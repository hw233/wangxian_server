package com.xuanzhi.tools.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 */
public class HttpsUtils {


	public static final String Response_Code = "Response-Code";

	public static final String Response_Message = "Response-Message";

	public static final String ENCODING = "Encoding";

	public static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			// 直接Pass
			return true;
		}
	}
/**
 	 * 通过https获得指定URL的数据
     * @param url
     * @param headers 包含HTTPs请求的头，所以Map中必须是<String,String>
     * @param connectTimeout 设置连接超时时间，单位是ms
     * @param readTimeout 设置读超时时间，单位是ms
     * @return 内容，同时在headers中，包含HTTP响应的头，
     * 			其中：
     * 				<code>Response-Code</code>对应 Response Code
     * 				<code>Response-Message</code>对应 Response Message
     * 				<code>Encoding</code>对应响应内容体的Encoding
     * @throws IOException
 */
    public  static byte[] webGet(URL url, java.util.Map headers,int connectTimeout,int readTimeout) throws Exception
    {
        if(headers == null)
        	headers = new java.util.HashMap();
        HttpsURLConnection conn = null;
	    try {
	    	// 创建SSLContext对象，并使用我们指定的信任管理器初始化
	    	TrustManager[] tm = { new MyX509TrustManager() }; 

	        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
	        sslContext.init(null, tm, new java.security.SecureRandom());

	        // 从上述SSLContext对象中得到SSLSocketFactory对象
	        SSLSocketFactory ssf = sslContext.getSocketFactory();
	        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
	        conn = (HttpsURLConnection) url.openConnection();
	        conn.setSSLSocketFactory(ssf);
	        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());

	        String jdkV = System.getProperty("java.vm.version");
	        if(jdkV!=null && jdkV.indexOf("1.5")!=-1){
	          Class clazz = conn.getClass();
	          try{
	            java.lang.reflect.Method method = clazz.getMethod("setConnectTimeout",new Class[]{int.class});
	            method.invoke(conn,new Object[]{new Integer(connectTimeout)});
	            method = clazz.getMethod("setReadTimeout",new Class[]{int.class});
	            method.invoke(conn,new Object[]{new Integer(readTimeout)});
	          }catch(Exception ec){
	            ec.printStackTrace();
	          }
	        }else{
	          System.setProperty("sun.net.client.defaultConnectTimeout","" + connectTimeout);
	          System.setProperty("sun.net.client.defaultReadTimeout", "" + readTimeout);
	        }

	        Iterator it = headers.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry me = (Map.Entry) it.next();
	            String key = (String) me.getKey();
	            String value = (String) me.getValue();
	            if (key != null && value != null) {
	                conn.setRequestProperty(key, value);
	            }
	        }

	        String contentType = conn.getContentType();
	        String encoding = null;
	        if (contentType != null &&
	            contentType.toLowerCase().indexOf("charset") > 0) {
	            int k = contentType.toLowerCase().indexOf("charset");
	            if (contentType.length() > k + 7) {
	                String sss = contentType.substring(k + 7).trim();
	                k = sss.indexOf("=");
	                if (k >= 0 && sss.length() > k + 1) {
	                    encoding = sss.substring(k + 1).trim();
	                    if (encoding.indexOf(";") > 0) {
	                        encoding = encoding.substring(0,
	                            encoding.indexOf(";")).trim();
	                    }
	                }

	            }
	        }
	        headers.clear();

	        int k = 0;
	        String feildValue = null;
	        while ( (feildValue = conn.getHeaderField(k)) != null) {
	            String key = conn.getHeaderFieldKey(k);
	            k++;
	            if (key != null) {
	                headers.put(key, feildValue);
	            }
	        }
	        headers.put("Response-Code", new Integer(conn.getResponseCode()));
	        headers.put("Response-Message", conn.getResponseMessage());

	        java.io.InputStream bis = conn.getInputStream();
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        byte bytes[] = new byte[1024];
	        int n = 0;
	        while ( (n = bis.read(bytes)) > 0) {
	            out.write(bytes, 0, n);
	        }
	        bis.close();

	        bytes = out.toByteArray();
	        if (encoding == null) {
	            try {
	                for (int i = 0; i < 64 && i < bytes.length - 2; i++) {
	                    if (bytes[i] == '?' && bytes[i + 1] == '>') {
	                        String s = new String(bytes, 0, i);
	                        if (s.indexOf("encoding") > 0) {
	                            s = s.substring(s.indexOf("encoding") + 8);
	                            if (s.indexOf("=") >= 0) {
	                                s = s.substring(s.indexOf("=") + 1).trim();
	                                if (s.charAt(0) == '"') {
	                                    s = s.substring(1);
	                                }
	                                if (s.indexOf("\"") > 0) {
	                                    encoding = s.substring(0,
	                                        s.indexOf("\""));

	                                }
	                            }
	                        }
	                    }
	                }
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        if (encoding == null) {
	            encoding = "UTF-8";
	        }
	        headers.put("Encoding", encoding);

	        return bytes;

	    }
	    catch (Exception e) {
	        throw e;
	    }
	    finally {
	        if (conn != null) {
	            try {
	                conn.disconnect();
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
    }

    /**
     * 向指定的URL的发送数据，并获得返回值
     * @param url
     * @param headers 包含HTTP请求的头，所以Map中必须是<String,String>
     * @param content 内容体
     * @param connectTimeout 设置连接超时时间，单位是ms
     * @param readTimeout 设置读超时时间，单位是ms

     * @return 内容，同时在headers中，包含HTTP响应的头，
     * 			其中：
     * 				<code>Response-Code</code>对应 Response Code
     * 				<code>Response-Message</code>对应 Response Message
     * 				<code>Encoding</code>对应响应内容体的Encoding
     * @throws Exception
     */
    public static byte[] webPost(URL url, byte[] content,java.util.Map headers,int connectTimeout,int readTimeout) throws
        Exception {
        HttpsURLConnection urlconnection = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
	    	TrustManager[] tm = { new MyX509TrustManager() };

	        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
	        sslContext.init(null, tm, new java.security.SecureRandom());

	        // 从上述SSLContext对象中得到SSLSocketFactory对象
	        SSLSocketFactory ssf = sslContext.getSocketFactory();
	        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
	        urlconnection = (HttpsURLConnection) url.openConnection();
	        urlconnection.setSSLSocketFactory(ssf);
	        urlconnection.setHostnameVerifier(new TrustAnyHostnameVerifier());

            //set connectTimeout and readTimeout
            String jdkV = System.getProperty("java.vm.version");
            if(jdkV!=null && jdkV.indexOf("1.5")!=-1){
              Class clazz = urlconnection.getClass();
              try{
                java.lang.reflect.Method method = clazz.getMethod("setConnectTimeout",new Class[]{int.class});
                method.invoke(urlconnection,new Object[]{new Integer(connectTimeout)});
                method = clazz.getMethod("setReadTimeout",new Class[]{int.class});
                method.invoke(urlconnection,new Object[]{new Integer(readTimeout)});
              }catch(Exception ec){
                ec.printStackTrace();
              }
            }else{
              System.setProperty("sun.net.client.defaultConnectTimeout","" + connectTimeout);
              System.setProperty("sun.net.client.defaultReadTimeout", "" + readTimeout);
            }

            Iterator it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry me = (Map.Entry) it.next();
                String key = (String) me.getKey();
                String value = (String) me.getValue();
                if (key != null && value != null) {
                	urlconnection.setRequestProperty(key, value);
                }
            }
            urlconnection.setRequestProperty("Request-method", "post");
            urlconnection.setRequestProperty("Content-length",
                                             Integer.toString(content.length));

            urlconnection.setDoInput(true);
            urlconnection.setDoOutput(true);


            OutputStream outputstream = urlconnection.getOutputStream();
            outputstream.write(content);
            outputstream.flush();


            String contentType = urlconnection.getContentType();
            String encoding = null;
            if (contentType != null &&
                contentType.toLowerCase().indexOf("charset") > 0) {
                int k = contentType.toLowerCase().indexOf("charset");
                if (contentType.length() > k + 7) {
                    String sss = contentType.substring(k + 7).trim();
                    k = sss.indexOf("=");
                    if (k >= 0 && sss.length() > k + 1) {
                        encoding = sss.substring(k + 1).trim();
                        if (encoding.indexOf(";") > 0) {
                            encoding = encoding.substring(0,
                                encoding.indexOf(";")).trim();
                        }
                    }

                }
            }
            headers.clear();

            int k = 0;
            String feildValue = null;
            while ( (feildValue = urlconnection.getHeaderField(k)) != null) {
                String key = urlconnection.getHeaderFieldKey(k);
                k++;
                if (key != null) {
                    headers.put(key, feildValue);
                }
            }
            headers.put("Response-Code", new Integer(urlconnection.getResponseCode()));
            headers.put("Response-Message", urlconnection.getResponseMessage());

            java.io.InputStream bis = urlconnection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte bytes[] = new byte[1024];
            int n = 0;

            int count = 0;
            while( (n = bis.read(bytes)) > 0){
                out.write(bytes, 0, n);
            }
            bis.close();
            bytes = out.toByteArray();
            out.close();
            outputstream.close();

            if (encoding == null) {
                encoding = "UTF-8";
            }
            headers.put("Encoding", encoding);

            return bytes;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if (urlconnection != null){
            	urlconnection.disconnect();
            }
        }
    }

    public  static void main(String args[]){
    	URL url;
    	try {
    		url = new URL(args[0]);

    		Map headers = new HashMap();
    		byte[] resContent =HttpsUtils.webGet(url, headers, 60000, 60000);
    		Integer resCode = (Integer)headers.get("Response-Code");
    		String resMsg = (String)headers.get("Response-Message");
    		String resEncoding = (String)headers.get("Encoding");
    		String content = new String(resContent,resEncoding);
    		System.out.println(content);
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
}
