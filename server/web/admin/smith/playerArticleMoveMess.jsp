<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.smith.*"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
	String downCount = request.getParameter("downCount");	
	String layerCount = request.getParameter("layerCount");	
	ArticleRelationShipManager msm = ArticleRelationShipManager.getInstance();
	List<ArticleRelationShip> ships = null;
	Set<ArticleRelationShip> set = new HashSet<ArticleRelationShip>();
	if(downCount!=null && !"".equals(downCount) && downCount.matches("\\d+")){
		ships = msm.getMinDownCountShips(Integer.valueOf(downCount)); 
		set.addAll(ships);
	}
	if(layerCount!=null && !"".equals(layerCount) && layerCount.matches("\\d+")){
		ships = msm.getMinLayerCountShips(Integer.valueOf(layerCount));
		set.addAll(ships);
	}
	
	StringBuffer sb = new StringBuffer();
	Iterator<ArticleRelationShip> it = set.iterator();
	while(it.hasNext()){
		ArticleRelationShip ship3 = (ArticleRelationShip)it.next();
		if(!msm.isForbid(ship3)){
			sb.append(ship3.getId());
			sb.append("#####");
			sb.append(ship3.getMaxDownCount());
			sb.append("#####");
			sb.append(ship3.getMaxLayerCount());
			sb.append("#####");
			sb.append(ship3.getBottomLevelTotalUp());
			sb.append("#####");
			sb.append(GameConstants.getInstance().getServerName());
			sb.append("#####");
			sb.append(msm.isForbid(ship3)?"<font color='red'><b>封禁</b></font>":"正常");
			sb.append("@@@@@");
		}
	}
	out.print(sb.toString());
%>
