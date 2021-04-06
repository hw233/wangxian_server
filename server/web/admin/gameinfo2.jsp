<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.core.*,com.xuanzhi.tools.transport.*"%><% 
	
	String beanName ="game_manager";
	GameManager sm = null;
	sm = (GameManager)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
	String gameId = "古祭坛";
	Game game = null;
	
	if(request.getParameter("fb") != null && request.getParameter("fb").equals("true")){
		int index = Integer.parseInt(request.getParameter("index"));
		XinShouChunFuBenManager xscfb = XinShouChunFuBenManager.getInstance();
		if(xscfb != null){
			int k = xscfb.indexOf(gameId);
			game = xscfb.getGames()[k][index];
		}
	}else{
		game = sm.getGameByName(gameId,0);
		if(game == null){
			game = sm.getGameByName(gameId,1);
		}
	}
	
	GameInfo gi = game.getGameInfo();
	
	boolean limitM = gi.isLimitMOUNT();
	
	gi.setLimitMOUNT(false);
	%>
修改完成:<%=limitM + "-->" + gi.isLimitMOUNT() %>
