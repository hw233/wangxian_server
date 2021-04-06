package com.fy.gamegateway.thirdpartner.qq.mime.qweibo;

import java.io.File;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.thirdpartner.qq.mime.qweibo.utils.QHttpUtil;

public class QHttpClient {

	private static final int CONNECTION_TIMEOUT = 2000;
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);

	public QHttpClient() {

	}

	/**
	 * Using GET method.
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @return Response string.
	 * @throws Exception
	 */
	public HttpRspInfo httpGet(String url, String queryString , Header h) throws Exception {
		HttpRspInfo responseData = null;

		if (queryString != null && !queryString.equals("")) {   //url+querystring
			url += "?" + queryString;
		}

		HttpClient httpClient = new HttpClient();
		
		if (QTianjinProxy.isTianjinProxy)
			httpClient.getHostConfiguration().setProxy("10.172.48.92", 3300);
		/**/
		
		GetMethod httpGet = new GetMethod(url);
		if (h != null)
		{
			httpGet.addRequestHeader(h);
			httpGet.setDoAuthentication(true);			
		}
		
		httpGet.setFollowRedirects(false);	
		httpGet.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));

		try {
			int statusCode = httpClient.executeMethod(httpGet);
			if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_MOVED_TEMPORARILY 
					&& statusCode != HttpStatus.SC_MOVED_PERMANENTLY) {
				System.err.println("HttpGet Method failed: "
						+ httpGet.getStatusLine());
			}
			// Read the response body.
			responseData              = new HttpRspInfo();
			responseData.httpCode     = statusCode;
			responseData.responseData = new String(httpGet.getResponseBody() , OAuth.ENCODING);
			
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpGet.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}

	/**
	 * Using POST method.
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @return Response string.
	 * @throws Exception
	 */
	public HttpRspInfo httpPost(String url, Header h , List<QParameter> postParam) throws Exception {
		HttpRspInfo rsp  =  null;
		HttpClient httpClient = new HttpClient();
		
		PostMethod httpPost = new PostMethod(url);
		httpPost.addParameter("Content-Type",
				"application/x-www-form-urlencoded");
		httpPost.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));		
		//if (queryString != null && !queryString.equals("")) {
		//	httpPost.setRequestEntity(new ByteArrayRequestEntity(queryString
		//			.getBytes()));
		//}
		
		if (h != null)
		{
			httpPost.addRequestHeader(h);
			httpPost.setDoAuthentication(true);
		}
		if (postParam != null)
		{
			for (QParameter p : postParam){
				if (p.mName != null && !p.mName.isEmpty())
					httpPost.addParameter(p.mName , p.mValue);
				else if (p.mName != null && p.mName.isEmpty())
				{	
					httpPost.setRequestEntity(new ByteArrayRequestEntity(p.mValue
							.getBytes(OAuth.ENCODING)));
				}
			}
		}

		try {			
			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("HttpPost Method failed: "
						+ httpPost.getStatusLine());
			}		
			
			rsp              = new HttpRspInfo();					
			rsp.httpCode     = statusCode;
			rsp.responseData = new String(httpPost.getResponseBody() , OAuth.ENCODING);//ʹ��utf8	
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return rsp;
	}

	/**
	 * Using POST method with multiParts.
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @param files
	 *            The list of image files
	 * @return Response string.
	 * @throws Exception
	 */
	public String httpPostWithFile(String url, String queryString,
			List<QParameter> files) throws Exception {

		String responseData = null;
		url += '?' + queryString;
		HttpClient httpClient = new HttpClient();
		
		if (QTianjinProxy.isTianjinProxy)
			httpClient.getHostConfiguration().setProxy("10.172.48.92", 3300);
		/**/
		
		PostMethod httpPost = new PostMethod(url);
		try {
			List<QParameter> listParams = QHttpUtil
					.getQueryParameters(queryString);
			int length = listParams.size() + (files == null ? 0 : files.size());
			Part[] parts = new Part[length];
			int i = 0;
			for (QParameter param : listParams) {
				parts[i++] = new StringPart(param.mName,
						QHttpUtil.formParamDecode(param.mValue), "UTF-8");
			}
			for (QParameter param : files) {
				File file = new File(param.mValue);
				parts[i++] = new FilePart(param.mName, file.getName(), file,
						QHttpUtil.getContentType(file), "UTF-8");
			}

			httpPost.setRequestEntity(new MultipartRequestEntity(parts,
					httpPost.getParams()));

			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("HttpPost Method failed: "
						+ httpPost.getStatusLine());
			}
			responseData = httpPost.getResponseBodyAsString();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}

}
