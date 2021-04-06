package com.fy.engineserver.gm.custom;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {  
	
	protected static HttpUtil m_self = null;
    
//    protected static Log cat = LogFactory.getLog(HttpUtil.class.getName());
public	static Logger cat = LoggerFactory.getLogger(HttpUtil.class.getName());
    
    public static HttpUtil getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		m_self = this;
		System.out.println("[HttpUtil] [initalized...]");
	}
	
	public ResponseMessage doRequest(HttpClient client, String url, String method, HashMap form_data, String encoding) {
		try {
			HttpConnectionManagerParams params= client.getHttpConnectionManager().getParams(); 
			params.setSoTimeout(10*1000); 
			params.setConnectionTimeout(10*1000);
			if (method.toLowerCase().equals("get")) {
				GetMethod get = new GetMethod(url);
				get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
			    		new DefaultHttpMethodRetryHandler(3, false));
				//get.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
				get.setRequestHeader("User-Agent", "Nokia7610");
		        //设定超时的时间
		        get.getParams().setSoTimeout(1000*10);
				try {
			        // Execute the method.
			        int statusCode = client.executeMethod(get);

		        	String redirectLocation = null;
			        while (statusCode >= 300) {
		              Header locationHeader = get.getResponseHeader("location");
		              if (locationHeader != null) {
		                  redirectLocation = locationHeader.getValue();
				          if(cat.isInfoEnabled())
					          cat.info("**************************************************************");
//				          cat.info("Method Redirecting, location:"+redirectLocation);
				          if(cat.isInfoEnabled())
					          cat.info("Method Redirecting, location:{}", new Object[]{redirectLocation});
				          if(cat.isInfoEnabled())
					          cat.info("**************************************************************");
				          get.setURI(new URI(redirectLocation, false));
				          statusCode = client.executeMethod(get);
		              } else {
				          if(cat.isInfoEnabled())
					          cat.info("**************************************************************");
//				          cat.info("Method failed: " + get.getStatusLine() + "Red-location:"+redirectLocation);
				          if(cat.isInfoEnabled())
					          cat.info("Method failed: {}Red-location:{}", new Object[]{get.getStatusLine(),redirectLocation});
				          if(cat.isInfoEnabled())
					          cat.info("**************************************************************");
				          break;
		              }
			        }
			        // Read the response body.
			        byte[] responseBody = get.getResponseBody();
			        ResponseMessage mes = new ResponseMessage();
			        mes.setContent(new String(responseBody,encoding));
			        mes.setResponseHeaders(get.getResponseHeaders());
			        return mes;
			        // Deal with the response.
			        // Use caution: ensure correct character encoding and is not binary data

			      } catch (HttpException e) {
			        System.err.println("Fatal protocol violation: " + e.getMessage());
			        e.printStackTrace();
			      } catch (IOException e) {
			        System.err.println("Fatal transport error: " + e.getMessage());
			        e.printStackTrace();
			      } finally {
			        // Release the connection.
			        get.releaseConnection();
			      }  
			}  else { 
				PostMethod post = new PostMethod(url);
				post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
			    		new DefaultHttpMethodRetryHandler(3, false));
				//post.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
				//post.setRequestHeader("User-Agent", "Nokia7610");
				post.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
				//post.setFollowRedirects(true);
		    	//设定连接的超时的时间，为20秒.
                post.getParams().setSoTimeout(1000*10);
			    try {
			    	if(form_data != null && form_data.size() > 0) {
				    	NameValuePair[] data = new NameValuePair[form_data.size()];
				    	Iterator keys = form_data.keySet().iterator();
				    	int i=0;
				    	while(keys.hasNext()) {
				    		String key = (String)keys.next();
				    		String value = (String)form_data.get(key);
				    		data[i] = new NameValuePair(key, value);
				    		++i;
		
				    	}
				    	
				        post.setRequestBody(data);
			    	}
			        // Execute the method.
			        int statusCode = client.executeMethod(post);

		        	String redirectLocation = null;
			        while (statusCode >= 300) {
		              Header locationHeader = post.getResponseHeader("location");
		              if (locationHeader != null) {
		                  redirectLocation = locationHeader.getValue();
				          if(cat.isInfoEnabled())
					          cat.info("**************************************************************");
//				          cat.info("Method Redirecting, location:"+redirectLocation);
				          if(cat.isInfoEnabled())
					          cat.info("Method Redirecting, location:{}", new Object[]{redirectLocation});
				          if(cat.isInfoEnabled())
					          cat.info("**************************************************************");
				          post.setURI(new URI(redirectLocation, false));
				          statusCode = client.executeMethod(post);
		              } else {
				          if(cat.isInfoEnabled())
					          cat.info("**************************************************************");
//				          cat.info("Method failed: " + post.getStatusLine() + "Red-location:"+redirectLocation);
				          if(cat.isInfoEnabled())
					          cat.info("Method failed: {}Red-location:{}", new Object[]{post.getStatusLine(),redirectLocation});
				          if(cat.isInfoEnabled())
					          cat.info("**************************************************************");
				          break;
		              }
			        }
                 
			        // Read the response body.
			       
			     //   System.out.println("[trace1] ["+statusCode+"]");
			        byte[] responseBody = post.getResponseBody();
			        ResponseMessage mes = new ResponseMessage();
			        mes.setContent(new String(responseBody,encoding));
			        mes.setResponseHeaders(post.getResponseHeaders());
			        return mes;
			     //   System.out.println(responseBody.length);
			        // Deal with the response.
			        // Use caution: ensure correct character encoding and is not binary data

			      } catch (HttpException e) {
			        System.err.println("Fatal protocol violation: " + e.getMessage());
			        e.printStackTrace();
			      } catch (IOException e) {
			        System.err.println("Fatal transport error: " + e.getMessage());
			        e.printStackTrace();
			      } finally {
			        // Release the connection.
			    	  post.releaseConnection();
			      }  
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String args[]) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String url = "http://37.duote.com/nettransport.exe";
		HttpClient client = new HttpClient();
		//System.out.print(utils.isFile(url) + "==" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now));
	}

}

