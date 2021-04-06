package com.fy.gamegateway.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public final class HttpURLUtils {
	public static Logger logger = Logger.getLogger(HttpURLUtils.class);

	public static String httpBuildQuery(Map<String, String> map, Object[] keyset) {
		StringBuffer strbuf = new StringBuffer();
		try {
			if (keyset == null) {
				Set set = map.entrySet();
				Iterator it = set.iterator();
				while (it.hasNext()) {
					Entry entry = (Entry) it.next();
					strbuf.append(
							URLEncoder.encode(entry.getKey().toString(),
									"UTF-8")).append("=");
					strbuf.append(
							URLEncoder.encode(entry.getValue().toString(),
									"UTF-8")).append("&");
				}
			} else {
				for (int i = 0; i < keyset.length; i++) {
					strbuf.append(
							URLEncoder.encode(keyset[i].toString(), "UTF-8"))
							.append("=");
					strbuf.append(
							URLEncoder.encode(map.get(keyset[i]), "UTF-8"))
							.append("&");
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (strbuf.length() > 0) {
			strbuf.deleteCharAt(strbuf.length() - 1);
		}
		return strbuf.toString();
	}
	
	public static String httpBuildQueryNotEncode(Map<String, String> map, Object[] keyset) {
		StringBuffer strbuf = new StringBuffer();
		try {
			if (keyset == null) {
				Set set = map.entrySet();
				Iterator it = set.iterator();
				while (it.hasNext()) {
					Entry entry = (Entry) it.next();
					strbuf.append(entry.getKey().toString()).append("=");
					strbuf.append(entry.getValue().toString()).append("&");
				}
			} else {
				for (int i = 0; i < keyset.length; i++) {
					strbuf.append(keyset[i].toString()).append("=");
					strbuf.append(map.get(keyset[i])).append("&");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (strbuf.length() > 0) {
			strbuf.deleteCharAt(strbuf.length() - 1);
		}
		return strbuf.toString();
	}
	
	public static String postRequest(String purl, String query) {
		String result = "";
		try {
			// String param = KxUtil.httpBuildQuery(query, "UTF-8");
			String param = query;
			URL url = new URL(purl);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setDoOutput(true);
			huc.setRequestMethod("POST");
			huc.connect();
			OutputStream out = huc.getOutputStream();
			out.write(param.getBytes());
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(huc
					.getInputStream()));
			String line = "";
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<String> postRequestStringArray(String purl, String query) {
		List<String> slist = new ArrayList<String>();
		try {
			// String param = KxUtil.httpBuildQuery(query, "UTF-8");
			String param = query;
			URL url = new URL(purl);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setDoOutput(true);
			huc.setRequestMethod("POST");
			huc.connect();
			OutputStream out = huc.getOutputStream();
			out.write(param.getBytes());
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(huc
					.getInputStream()));
			String line = "";
			while ((line = br.readLine()) != null) {
				slist.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return slist;
	}
	
	public static String doPost(String reqUrl, Map<String, String> parameters) {
		HttpURLConnection urlConn = null;
		try {
			urlConn = sendPost(reqUrl, parameters);
			String responseContent = getContent(urlConn);
			return responseContent.trim();
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
		}
	}

	public static String doUploadFile(String reqUrl,
			Map<String, String> parameters, String fileParamName,
			String filename, String contentType, byte[] data) {
		HttpURLConnection urlConn = null;
		try {
			urlConn = sendFormdata(reqUrl, parameters, fileParamName, filename,
					contentType, data);
			String responseContent = new String(getBytes(urlConn));
			return responseContent.trim();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}
	}

	private static HttpURLConnection sendFormdata(String reqUrl,
			Map<String, String> parameters, String fileParamName,
			String filename, String contentType, byte[] data) {
		HttpURLConnection urlConn = null;
		try {
			URL url = new URL(reqUrl);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setConnectTimeout(5000);// （单位：毫秒）jdk
			urlConn.setReadTimeout(5000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
			urlConn.setDoOutput(true);

			urlConn.setRequestProperty("connection", "keep-alive");

			String boundary = "-----------------------------114975832116442893661388290519"; // 分隔符
			urlConn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			boundary = "--" + boundary;
			StringBuffer params = new StringBuffer();
			if (parameters != null) {
				for (Iterator<String> iter = parameters.keySet().iterator(); iter
						.hasNext();) {
					String name = iter.next();
					String value = parameters.get(name);
					params.append(boundary + "\r\n");
					params.append("Content-Disposition: form-data; name=\""
							+ name + "\"\r\n\r\n");
					// params.append(URLEncoder.encode(value, "UTF-8"));
					params.append(value);
					params.append("\r\n");
				}
			}

			StringBuilder sb = new StringBuilder();
			// sb.append("\r\n");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\"" + fileParamName
					+ "\"; filename=\"" + filename + "\"\r\n");
			sb.append("Content-Type: " + contentType + "\r\n\r\n");
			byte[] fileDiv = sb.toString().getBytes();
			byte[] endData = ("\r\n" + boundary + "--\r\n").getBytes();
			byte[] ps = params.toString().getBytes();

			OutputStream os = urlConn.getOutputStream();
			os.write(ps);
			os.write(fileDiv);
			os.write(data);
			os.write(endData);

			os.flush();
			os.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return urlConn;
	}

	private static String getContent(HttpURLConnection urlConn) {
		try {
			String responseContent = null;
			InputStream in = urlConn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
			return responseContent;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static byte[] getBytes(String reqUrl, Map<String, String> parameters) {
		HttpURLConnection conn = sendPost(reqUrl, parameters);
		return getBytes(conn);
	}

	private static byte[] getBytes(HttpURLConnection urlConn) {
		try {
			InputStream in = urlConn.getInputStream();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int i = 0; (i = in.read(buf)) > 0;)
				os.write(buf, 0, i);
			in.close();
			return os.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static HttpURLConnection sendPost(String reqUrl,
			Map<String, String> parameters) {
		HttpURLConnection urlConn = null;
		try {
			String params = generatorParamString(parameters);
			URL url = new URL(reqUrl);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			// urlConn
			// .setRequestProperty(
			// "User-Agent",
			// "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
			urlConn.setConnectTimeout(5000);// （单位：毫秒）jdk
			urlConn.setReadTimeout(5000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
			urlConn.setDoOutput(true);
			byte[] b = params.getBytes();
			urlConn.getOutputStream().write(b, 0, b.length);
			urlConn.getOutputStream().flush();
			urlConn.getOutputStream().close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return urlConn;
	}

	/**
	 * 将parameters中数据转换成用"&"链接的http请求参数形式
	 * 
	 * @param parameters
	 * @return
	 */
	public static String generatorParamString(Map<String, String> parameters) {
		StringBuffer params = new StringBuffer();
		if (parameters != null) {
			for (Iterator<String> iter = parameters.keySet().iterator(); iter
					.hasNext();) {
				String name = iter.next();
				String value = parameters.get(name);
				params.append(name + "=");
				try {
					params.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (Exception e) {
					String message = String.format("'%s'='%s'", name, value);
					throw new RuntimeException(message, e);
				}
				if (iter.hasNext())
					params.append("&");
			}
		}
		return params.toString();
	}

	public static HttpURLConnection sendGet(String link, String charset) {
		HttpURLConnection urlConn = null;
		try {
			URL url = new URL(link);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
		} catch (Exception e) {
			logger.error("发送get请求:", e);
		}
		return urlConn;
	}
	
	public static HashMap<String, String> getRetturnXmlHashMap(HttpURLConnection httpURLCon) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		InputStream xstr = null;
		try {
			xstr = httpURLCon.getInputStream();
			SAXReader reader = new SAXReader();
			Document document = reader.read(xstr);
			Element root = document.getRootElement();
			List<Node> listnodeList = root.elements();
			for(Node n : listnodeList) {
				hashMap.put(n.getName().trim(), n.getText().trim());
			}
		} catch (Exception e) {
			logger.error("解析http请求后返回的XML发生错误:" + xstr.toString(), e);
			return hashMap;
		}
		logger.info("解析http请求后返回的XML:" + xstr.toString());
		return hashMap;
	}

	/**
	 * 
	 * @param link
	 * @param charset
	 * @return
	 */
	public static String doGet(String link, String charset) {
		try {
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			BufferedInputStream in = new BufferedInputStream(conn
					.getInputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int i = 0; (i = in.read(buf)) > 0;) {
				out.write(buf, 0, i);
			}
			out.flush();
			String s = new String(out.toByteArray(), charset);
			return s;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * UTF-8编码
	 * 
	 * @param link
	 * @return
	 */
	public static String doGet(String link) {
		return doGet(link, "UTF-8");
	}

	public static int getIntResponse(String link) {
		String str = doGet(link);
		return Integer.parseInt(str.trim());
	}
	
	/**
	 * 对URL进行编码，编码失败，则返回原来的URL
	 * @param url
	 * @param enc
	 * @return
	 */
	public static String getEncodeURL(String url, String enc, Class subClass) {
		try {
			return java.net.URLEncoder.encode(url, enc);
		} catch (UnsupportedEncodingException e) {
			logger.error(subClass.getName()
					+ "中getEncodeURL(url, enc):" + enc + "为不支持的编码方式。", e);
		}
		return url;
	}
}
