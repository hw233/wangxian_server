package com.fy.engineserver.sprite;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerUtilServlet extends HttpServlet {
	
//	protected static Logger logger = Logger.getLogger(PlayerUtilServlet.class);
public	static Logger logger = LoggerFactory.getLogger(PlayerUtilServlet.class);

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = req.getParameter("username");
		PlayerManager pmanager = PlayerManager.getInstance();
		int pcount = 0;
		try {
			pcount = pmanager.getAmountOfPlayers(username);
		} catch(Exception e) {
			e.printStackTrace();
//logger.error(e);
			logger.error( " " , e );
		}
		int allCount = 0;
		try {
			allCount = pmanager.getAmountOfPlayers();
		} catch(Exception e) {
			e.printStackTrace();
//logger.error(e);
			logger.error( " " , e );
		}
		String result = pcount + "," + allCount;
		res.getWriter().write(result);
		if(logger.isInfoEnabled()) {
//			logger.info("[getUserPlayerInfo] ["+username+"] ["+pcount+"] ["+allCount+"]");
			if(logger.isInfoEnabled())
				logger.info("[getUserPlayerInfo] [{}] [{}] [{}]", new Object[]{username,pcount,allCount});
		}
	}

}
