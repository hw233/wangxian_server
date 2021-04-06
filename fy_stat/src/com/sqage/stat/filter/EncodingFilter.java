package com.sqage.stat.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class EncodingFilter extends HttpServlet implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		arg2.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
}
