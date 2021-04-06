package com.fy.boss.platform.lenovo.com.test.newsync;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.platform.lenovo.com.test.util.CpTransSyncSignValid;

public class CperSyncServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String pkey = "123456";
	/**
	 * 
	 */
	private static final String SUCCESS = "SUCCESS";
	/**
	 * 
	 */
	private static final String FAILURE = "FAILURE";

	@Override
	public void init(ServletConfig config) throws ServletException {
		pkey = config.getServletContext().getInitParameter("pkey");
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/**
		 * 1/获取信息
		 */
		String transdata = request.getParameter("transdata");
		String sign = request.getParameter("sign");

		/**
		 * 2/验证数据有效性
		 */
		if (transdata == null || "".equalsIgnoreCase(transdata)) {
			sendMessage(FAILURE, response);
			return;
		}
		if (sign == null || "".equalsIgnoreCase(sign)) {
			sendMessage(FAILURE, response);
			return;
		}

		/**
		 * 3/验签操作
		 */
		boolean isvlid = validMessage(transdata, sign);

		if (!isvlid) {
			sendMessage(FAILURE, response);
			return;
		}

		/**
		 * 4/业务处理
		 */
		
		/**
		 * 5/成功返回
		 */
		sendMessage(SUCCESS, response);
		return;

	}

	private boolean validMessage(String transdata, String sign) throws UnsupportedEncodingException {
		return CpTransSyncSignValid.validSign(transdata, sign, pkey);
	}

	/**
	 * 
	 * @param message
	 * @param response
	 * @throws IOException
	 */
	private void sendMessage(String message, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(message);
		out.flush();
		out.close();
	}
}
