package com.xuanzhi.tools.text;

import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;

public class Test extends HttpServlet{

	protected void doGet(HttpServletRequest req,
                     HttpServletResponse resp)
              throws ServletException,java.io.IOException{
		doPost(req,resp);
	}
	
	protected void doPost(HttpServletRequest req,
                     HttpServletResponse resp)
              throws ServletException,java.io.IOException{
		StringBuffer sb = new StringBuffer();
		req.getRequestURI();
		sb.append("url:"+req.getRequestURL()+"<br/>");
		sb.append("uri:"+req.getRequestURI()+"\n");
		sb.append("path:"+req.getServletPath()+"\n");
		sb.append("queryString:"+req.getQueryString()+"<br/>");
		Enumeration en = req.getHeaderNames();
		while(en.hasMoreElements()){
			String header = (String)en.nextElement();
			Enumeration en2 = req.getHeaders(header);
			while(en2.hasMoreElements()){
				sb.append(header+":" + en2.nextElement() + "<br/>");
			}
		}
		
		en = req.getParameterNames();
		while(en.hasMoreElements()){
			String header = (String)en.nextElement();
			String value = req.getParameter(header);
			sb.append(header + "=" + value + "<br/>");
		}
		
		resp.getOutputStream().write(sb.toString().getBytes());
		resp.getOutputStream().flush();
	}
}
