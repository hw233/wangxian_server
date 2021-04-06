package com.fy.engineserver.image;


import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.engineserver.core.res.ResourceManager;

public class ImageServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idStr = request.getParameter("id");
		byte[] data = null;
		ResourceManager rm = ResourceManager.getInstance();
		data = rm.getIconPngBytes(ResourceManager.物品图标, idStr);
		OutputStream os = response.getOutputStream();
		response.setHeader("Content-Type", "image/png");
		if(data != null){
			os.write(data);
		}else{
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
