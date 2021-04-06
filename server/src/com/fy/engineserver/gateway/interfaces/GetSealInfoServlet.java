package com.fy.engineserver.gateway.interfaces;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.seal.SealManager;
import com.xuanzhi.tools.text.JsonUtil;

public class GetSealInfoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		//获取SealManager中的map 转成json 输出
		SealManager sealManager  = SealManager.getInstance();
		try {
			String json = JsonUtil.jsonFromObject(sealManager.sealMap);
			if(Game.logger.isInfoEnabled())
			{
				Game.logger.info("["+Thread.currentThread()+"] [获取封印相关信息] ["+req.getRemoteAddr()+"] ["+json+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			res.getWriter().write(json);
			return;
		} catch (Exception e) {
			Game.logger.error("["+Thread.currentThread()+"] [获取封印相关信息] ["+req.getRemoteAddr()+"] [--] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	
	}
	
}
