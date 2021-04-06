package com.xuanzhi.tools.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.text.JsonUtil;

/**
 * 
 */
public class JsonMessageServer extends HttpServlet {
	
	public static final long serialVersionUID = 34594058968455343L;
	
	public Logger logger = LoggerFactory.getLogger(JsonMessageServer.class);

	private static JsonMessageHandler handler;
		
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		if(handler == null) {
			String queueMaxSize = config.getInitParameter("queue-max-size");
			String handlerName = config.getInitParameter("handler");
			try {
				Class c = Class.forName(handlerName);
				Method m = c.getMethod("getInstance", null);
				this.handler = (JsonMessageHandler)m.invoke(c, null);
				handler.setQueueMaxSize(Integer.valueOf(queueMaxSize));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String content = readContent(req.getInputStream(), "UTF-8");
			JsonObject obj = JsonUtil.objectFromJson(content, JsonObject.class);
			String className = obj.getClassName();
			JsonObject original = (JsonObject)JsonUtil.objectFromJson(content, Class.forName(className));
			handler.putMessage(original);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String readContent(InputStream stream, String charset) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte buf[] = new byte[512];
		int n=0;
		while((n = stream.read(buf)) != -1) {
			out.write(buf,0,n);
		}
		return out.toString(charset);
	}
}
