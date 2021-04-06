<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.fy.engineserver.core.*,com.fy.engineserver.downcity.*,java.util.*"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@include file="../authority.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<link href="css/dtree.css" type="text/css" rel="stylesheet" />
<SCRIPT src="js/dtree.js" type=text/javascript></SCRIPT>

<meta content="MSHTML 6.00.2900.3157" name=GENERATOR><style type="text/css">
<!--
body,td,th {
	font-size: 12px;
}
body {
	margin-left: 5px;
	margin-top: 5px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #DDEDFF;
}
-->
</style>
</head>
<body>
        
		<%
		try{
		String gmname = session.getAttribute("username").toString();
		//所有区域地图
		out.print("<input type='button' value='刷新' onclick='window.location.replace(\"map_list.jsp\")' />");
		String beanName = "game_manager";
		GameManager gm = (GameManager)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
		Game games[] = gm.getGames();
		//int index = 10001;
		long quyu =0l;
		out.print("<table width=80% ><caption>所有区域地图(唯一)</caption>");
		for(int i = 0 ; i < games.length ; i++){
		    String name = games[i].getGameInfo().getName();
		    if(i==0)
		    out.print("<tr>");
			out.print("<td class='top'><a href='gameinfo.jsp?game="+name+"' >"+name+"("+games[i].getNumOfPlayer()+")</a></td>");
			if(i!=0&&i%8==0)
			out.print("</tr>");
			quyu = quyu +games[i].getNumOfPlayer();
		}
		out.print("</table>");
		out.print("<br/>区域总人数为： "+quyu);
		out.print("<hr color='green' size='10' /> ");	
		XinShouChunFuBenManager xscfb = XinShouChunFuBenManager.getInstance();
		
		long zixin = 0l;
		long rixin = 0l;
		if(xscfb != null){
		    out.print("<table width+80% ><caption>新手村</caption>");
			Game gamess[][] = xscfb.getGames();
			int count =0;
			for(int j = 0 ; j < gamess.length ; j++){
				for(int i = 0 ; i < gamess[j].length ; i++){
				if(count==0)
				 out.print("<tr>");
				 out.print("<td class='top' ><a href='gameinfo.jsp?fb=true&game="+gamess[j][i].getGameInfo().getName()+"&index="+i+"' >"+gamess[j][i].getGameInfo().getName()+"-"+(i+1)+"("+gamess[j][i].getNumOfPlayer()+")</a></td>");
				 if(gamess[j][i].getGameInfo().getName().contains("紫")){
				  zixin += gamess[j][i].getNumOfPlayer();
				  }
				 if(gamess[j][i].getGameInfo().getName().contains("日月"))
				  rixin += gamess[j][i].getNumOfPlayer();
				 count++;
				 if(count!=0&&count%8==0)
				 out.print("</tr>");
				}
			}
			out.print("</table><br/>紫微宫新手总数为:"+zixin+"<br/>日月盟新手总数为："+rixin+"<br/>总人数为:"+(zixin+rixin));
			out.print("<hr size=10 color='green' />");
		}
		out.print("<p align='center'>副本</p>");
		DownCityManager dcm = DownCityManager.getInstance();
		DownCity[] dcs = dcm.getAllDownCity();
		if(dcm != null){
			ArrayList<DownCityInfo> dciList = dcm.getDciList();//获得所有的副本
			if(dciList != null){
			    long totalcount=0l;
				for(int i = 0; i < dciList.size(); i++){
					DownCityInfo dci = dciList.get(i);
					if(dci != null){
						int count = 0;
						ArrayList<DownCity> dcList = new ArrayList<DownCity>();
						if(dcs != null){
							for(DownCity dc : dcs){
								if(dc != null && dc.getGame() != null && dc.getGame().getGameInfo() != null && dc.getGame().getGameInfo().getName() != null && dc.getGame().getGameInfo().getName().equals(dci.getMapName())){
									count++;
									dcList.add(dc);
								}
							}
						}
						out.print("<a href='downcity_templatebyname.jsp?downCityName="+dci.getName()+"'> "+dci.getMapName()+dci.getName()+"("+count+")</a><br>");
						if(count != 0 && dcList.size() != 0){
							for(int j = 0; j < dcList.size(); j++){
								DownCity dc = dcList.get(j);
								if(dc != null){
								    if(j%10==0)
								      out.print("<br/>");
									out.print("<a href='downcitybyid.jsp?downcityid="+dc.getId()+"' >"+dc.getId()+"</a>("+(dc.getGame()!= null ? dc.getGame().getNumOfPlayer():"无副本地图") +")    ");
								}
							}
							out.print("<hr size=3 color='#40c0a0'/>");
						}
						totalcount = totalcount +count;
					}
				}
				out.print("<br/>副本总数为："+totalcount);
			}
		}
		}catch(Exception e){
	//	RequestDispatcher rdp = request
	//						.getRequestDispatcher("../gmuser/visitfobiden.jsp");
	//				rdp.forward(request, response);
	  out.print(StringUtil.getStackTrace(e));
		}
	%>
</body>
