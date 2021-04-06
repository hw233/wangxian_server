package com.xuanzhi.tools.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpUtils {

	public static final String Response_Code = "Response-Code";

	public static final String Response_Message = "Response-Message";

	public static final String ENCODING = "Encoding";

	/**
	 * 获得指定URL的数据
	 * 
	 * @param url
	 * @param headers
	 *            包含HTTP请求的头，所以Map中必须是<String,String>
	 * @param connectTimeout
	 *            设置连接超时时间，单位是ms
	 * @param readTimeout
	 *            设置读超时时间，单位是ms
	 * @return 内容，同时在headers中，包含HTTP响应的头， 其中： <code>Response-Code</code>对应
	 *         Response Code <code>Response-Message</code>对应 Response Message
	 *         <code>Encoding</code>对应响应内容体的Encoding
	 * @throws IOException
	 */
	public static void webHead(URL url, java.util.Map headers, int connectTimeout, int readTimeout) throws IOException {
		if (headers == null)
			headers = new java.util.HashMap();
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("HEAD");
			String jdkV = System.getProperty("java.vm.version");
			if (jdkV != null) {
				Class clazz = conn.getClass();
				try {
					java.lang.reflect.Method method = clazz.getMethod("setConnectTimeout", new Class[] { int.class });
					method.invoke(conn, new Object[] { new Integer(connectTimeout) });
					method = clazz.getMethod("setReadTimeout", new Class[] { int.class });
					method.invoke(conn, new Object[] { new Integer(readTimeout) });
				} catch (Exception ec) {
					ec.printStackTrace();
				}
			} else {
				System.setProperty("sun.net.client.defaultConnectTimeout", "" + connectTimeout);
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
			headers.clear();

			int k = 0;
			String feildValue = null;
			while ((feildValue = conn.getHeaderField(k)) != null) {
				String key = conn.getHeaderFieldKey(k);
				k++;
				if (key != null) {
					headers.put(key, feildValue);
				}
			}
			headers.put("Response-Code", new Integer(conn.getResponseCode()));
			headers.put("Response-Message", conn.getResponseMessage());
		} catch (IOException e) {
			throw e;
		} finally {
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获得指定URL的数据
	 * 
	 * @param url
	 * @param headers
	 *            包含HTTP请求的头，所以Map中必须是<String,String>
	 * @param connectTimeout
	 *            设置连接超时时间，单位是ms
	 * @param readTimeout
	 *            设置读超时时间，单位是ms
	 * @return 内容，同时在headers中，包含HTTP响应的头， 其中： <code>Response-Code</code>对应
	 *         Response Code <code>Response-Message</code>对应 Response Message
	 *         <code>Encoding</code>对应响应内容体的Encoding
	 * @throws IOException
	 */
	public static byte[] webGet(URL url, java.util.Map headers, int connectTimeout, int readTimeout) throws IOException {
		if (headers == null)
			headers = new java.util.HashMap();
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String jdkV = System.getProperty("java.vm.version");
			if (jdkV != null) {
				Class clazz = conn.getClass();
				try {
					java.lang.reflect.Method method = clazz.getMethod("setConnectTimeout", new Class[] { int.class });
					method.invoke(conn, new Object[] { new Integer(connectTimeout) });
					method = clazz.getMethod("setReadTimeout", new Class[] { int.class });
					method.invoke(conn, new Object[] { new Integer(readTimeout) });
				} catch (Exception ec) {
					ec.printStackTrace();
				}
			} else {
				System.setProperty("sun.net.client.defaultConnectTimeout", "" + connectTimeout);
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
			if (contentType != null && contentType.toLowerCase().indexOf("charset") > 0) {
				int k = contentType.toLowerCase().indexOf("charset");
				if (contentType.length() > k + 7) {
					String sss = contentType.substring(k + 7).trim();
					k = sss.indexOf("=");
					if (k >= 0 && sss.length() > k + 1) {
						encoding = sss.substring(k + 1).trim();
						if (encoding.indexOf(";") > 0) {
							encoding = encoding.substring(0, encoding.indexOf(";")).trim();
						}
					}

				}
			}
			headers.clear();

			int k = 0;
			String feildValue = null;
			while ((feildValue = conn.getHeaderField(k)) != null) {
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
			while ((n = bis.read(bytes)) > 0) {
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
										encoding = s.substring(0, s.indexOf("\""));

									}
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (encoding == null) {
				encoding = "UTF-8";
			}
			headers.put("Encoding", encoding);

			return bytes;

		} catch (IOException e) {
			throw e;
		} finally {
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 使用代理
	 * 
	 * @param url
	 * @param headers
	 * @param proxyIP
	 * @param proxyPort
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws IOException
	 */
	public static byte[] webGet(URL url, java.util.Map headers, String proxyIP, int proxyPort, int connectTimeout, int readTimeout) throws IOException {
		if (headers == null)
			headers = new java.util.HashMap();
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String strProxy="10.148.147.226";// 上海代理地址
			String strPort="3300"; // 代理端口
			Properties systemProperties = System.getProperties(); // 关键代码
			systemProperties.setProperty("http.proxySet","true"); 
			systemProperties.setProperty("http.proxyHost",strProxy); 
			systemProperties.setProperty("http.proxyPort",strPort);
			// https
			systemProperties.setProperty("https.proxySet","true"); 
			systemProperties.setProperty("https.proxyHost",strProxy); 
			systemProperties.setProperty("https.proxyPort",strPort); 
			String jdkV = System.getProperty("java.vm.version");
			if (jdkV != null) {
				Class clazz = conn.getClass();
				try {
					java.lang.reflect.Method method = clazz.getMethod("setConnectTimeout", new Class[] { int.class });
					method.invoke(conn, new Object[] { new Integer(connectTimeout) });
					method = clazz.getMethod("setReadTimeout", new Class[] { int.class });
					method.invoke(conn, new Object[] { new Integer(readTimeout) });
				} catch (Exception ec) {
					ec.printStackTrace();
				}
			} else {
				System.setProperty("sun.net.client.defaultConnectTimeout", "" + connectTimeout);
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
			if (contentType != null && contentType.toLowerCase().indexOf("charset") > 0) {
				int k = contentType.toLowerCase().indexOf("charset");
				if (contentType.length() > k + 7) {
					String sss = contentType.substring(k + 7).trim();
					k = sss.indexOf("=");
					if (k >= 0 && sss.length() > k + 1) {
						encoding = sss.substring(k + 1).trim();
						if (encoding.indexOf(";") > 0) {
							encoding = encoding.substring(0, encoding.indexOf(";")).trim();
						}
					}

				}
			}
			headers.clear();

			int k = 0;
			String feildValue = null;
			while ((feildValue = conn.getHeaderField(k)) != null) {
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
			while ((n = bis.read(bytes)) > 0) {
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
										encoding = s.substring(0, s.indexOf("\""));

									}
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (encoding == null) {
				encoding = "UTF-8";
			}
			headers.put("Encoding", encoding);

			return bytes;

		} catch (IOException e) {
			throw e;
		} finally {
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 向指定的URL的发送数据，并获得返回值
	 * 
	 * @param url
	 * @param headers
	 *            包含HTTP请求的头，所以Map中必须是<String,String>
	 * @param content
	 *            内容体
	 * @param connectTimeout
	 *            设置连接超时时间，单位是ms
	 * @param readTimeout
	 *            设置读超时时间，单位是ms
	 * 
	 * @return 内容，同时在headers中，包含HTTP响应的头， 其中： <code>Response-Code</code>对应
	 *         Response Code <code>Response-Message</code>对应 Response Message
	 *         <code>Encoding</code>对应响应内容体的Encoding
	 * @throws IOException
	 */
	public static byte[] webPost(URL url, byte[] content, java.util.Map headers, int connectTimeout, int readTimeout) throws IOException {
		HttpURLConnection urlconnection = null;
		try {
			urlconnection = (HttpURLConnection) url.openConnection();

			// set connectTimeout and readTimeout
			String jdkV = System.getProperty("java.vm.version");
			if (jdkV != null) {
				Class clazz = urlconnection.getClass();
				try {
					java.lang.reflect.Method method = clazz.getMethod("setConnectTimeout", new Class[] { int.class });
					method.invoke(urlconnection, new Object[] { new Integer(connectTimeout) });
					method = clazz.getMethod("setReadTimeout", new Class[] { int.class });
					method.invoke(urlconnection, new Object[] { new Integer(readTimeout) });
				} catch (Exception ec) {
					ec.printStackTrace();
				}
			} else {
				System.setProperty("sun.net.client.defaultConnectTimeout", "" + connectTimeout);
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
			urlconnection.setRequestProperty("Content-length", Integer.toString(content.length));

			urlconnection.setDoInput(true);
			urlconnection.setDoOutput(true);

			OutputStream outputstream = urlconnection.getOutputStream();
			outputstream.write(content);
			outputstream.flush();

			String contentType = urlconnection.getContentType();
			String encoding = null;
			if (contentType != null && contentType.toLowerCase().indexOf("charset") > 0) {
				int k = contentType.toLowerCase().indexOf("charset");
				if (contentType.length() > k + 7) {
					String sss = contentType.substring(k + 7).trim();
					k = sss.indexOf("=");
					if (k >= 0 && sss.length() > k + 1) {
						encoding = sss.substring(k + 1).trim();
						if (encoding.indexOf(";") > 0) {
							encoding = encoding.substring(0, encoding.indexOf(";")).trim();
						}
					}

				}
			}
			headers.clear();

			int k = 0;
			String feildValue = null;
			while ((feildValue = urlconnection.getHeaderField(k)) != null) {
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
			while ((n = bis.read(bytes)) > 0) {
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
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (urlconnection != null) {
				urlconnection.disconnect();
			}
		}
	}

	/**
	 * 
	 * 在多个IP的情况下，绑定在指定的IP上对外发送数据 袁延春 2009-0-17 yuanych@gmail.com
	 * 
	 * @param url
	 * @param content
	 * @param headers
	 * @param connectTimeout
	 * @param readTimeout
	 * @param bindLocalHost
	 *            绑定在什么主机
	 * @param bindLocalPort
	 *            绑定在什么端口
	 * @return
	 * @throws IOException
	 */
	public static byte[] webPost(URL url, byte[] content, java.util.Map headers, int connectTimeout, int readTimeout, String bindLocalHost, int bindLocalPort)
			throws IOException {

		if (url == null) {
			throw new RuntimeException("null url");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("POST " + url.getPath() + " HTTP/1.1" + '\n');
		sb.append("HOST: " + url.getHost() + '\n');
		sb.append("Content-Length: " + content.length + '\n');
		if (headers == null) {
			headers = new HashMap();
		}
		if (headers.get("Content-Type") == null) {
			headers.put("Content-Type", "text/xml; charset=utf-8");
		}

		for (Object key : headers.keySet()) {
			sb.append(key + ": " + headers.get(key) + '\n');
		}
		sb.append('\n');

		byte[] hs = sb.toString().getBytes();
		byte[] contents = new byte[hs.length + content.length];
		System.arraycopy(hs, 0, contents, 0, hs.length);
		System.arraycopy(content, 0, contents, hs.length, content.length);

		java.net.Socket s = null;
		DataInputStream is = null;
		DataOutputStream os = null;
		byte[] rets = new byte[0];
		try {
			s = new java.net.Socket();
			s.setSoTimeout(readTimeout);
			if (bindLocalHost != null && bindLocalHost.trim().length() > 0) {
				s.bind(new InetSocketAddress(bindLocalHost, bindLocalPort));
			}
			s.connect(new java.net.InetSocketAddress(url.getHost(), url.getPort() <= 0 ? 80 : url.getPort()), connectTimeout);
			is = new DataInputStream(new BufferedInputStream(s.getInputStream()));
			os = new DataOutputStream(s.getOutputStream());
			os.write(contents);
			os.flush();

			headers.clear();
			String line = is.readLine();// HTTP/1.1 200 OK
			if (line != null) {
				String[] t = line.split(" ");
				if (t.length > 1) {
					headers.put("Response-Code", Integer.parseInt(t[1]));
				}
				if (t.length > 2) {
					headers.put("Response-Message", t[2]);
				}
			}

			// 处理header
			line = is.readLine();
			while (line != null) {
				if (line.trim().length() == 0) {
					break;// header end
				} else {
					String[] tmp = line.split(":");
					int i = line.indexOf(":");
					if (i > 0) {
						String k = line.substring(0, i);
						String v = line.substring(i + 1);
						headers.put(k, v.trim());
					}
				}
				line = is.readLine();
			}

			// 处理包体
			String cl = (String) headers.get("Content-Length");
			if (cl == null || cl.trim().length() == 0) {
				cl = is.readLine();
			}
			int clength = 0;
			try {
				int radix = 10;
				if (cl != null && cl.startsWith("0")) {
					radix = 16;
				}
				clength = Integer.parseInt(cl, radix);
			} catch (Exception e) {
			}
			rets = new byte[clength];
			is.read(rets);

		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (s != null) {
				s.close();
			}
		}
		return rets;
	}


	public static void main(String args[]) {
		URL url;
		try {
			url = new URL(args[0]);

			Map headers = new HashMap();
			byte[] resContent = HttpUtils.webGet(url, headers, 60000, 60000);
			Integer resCode = (Integer) headers.get("Response-Code");
			String resMsg = (String) headers.get("Response-Message");
			String resEncoding = (String) headers.get("Encoding");
			String content = new String(resContent, resEncoding);
			System.out.println(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
