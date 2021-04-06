<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,com.fy.engineserver.datasource.buff.*,
com.fy.engineserver.datasource.skill.*,com.google.gson.*,java.lang.reflect.*,com.fy.engineserver.sprite.monster.*,"%><% 
	
	GameManager gm = GameManager.getInstance();

	
	GameInfo g1 = gm.getGameInfo("紫微宫新手村");

	ArrayList<GameInfo> al = new ArrayList<GameInfo>();

	ArrayList<GameInfo> al2 = new ArrayList<GameInfo>();
	al2.add(g1);
	
	while(true){
		GameInfo gis[] = al2.toArray(new GameInfo[0]);
		al2.clear();
		for(int i = 0 ; i < gis.length ; i++){
			if(al.contains(gis[i]) == false){
				al.add(gis[i]);
			}
			TransportData tds[] = gis[i].getTransports();
			for(int j = 0 ; j < tds.length ; j++){
				GameInfo gi2 = gm.getGameInfo(tds[j].getTargetMapName());
				if(gi2 != null && al.contains(gi2) == false){
					al2.add(gi2);
				}
			}
		}
		if(al2.size() == 0) break;
	}
	
	GameInfo gis[] = gm.getGameInfos();
	for(int i = 0 ; i < gis.length ; i++){
		if(al.contains(gis[i]) == false){
			al.add(gis[i]);
		}
	}
	
	gis = al.toArray(new GameInfo[0]);
	
	
	%>
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" href="../css/common.css"/>
<style type="text/css">
table{
border: 1px solid #69c;
border-collapse: collapse;
text-align: center;

}
td{
text-align: center;
border: 1px solid #69c;
}
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<table>
<tr class="titlecolor"><td>地图名</td><td>地图大小</td><td>跳转点</td><td>方向指示</td><td>目的地图</td><td>目标点</td></tr>
<% 
	

	for(int i = 0 ; i < gis.length ; i++){
		GameInfo gi = gis[i];
		
		TransportData tds[] = gi.getTransports();
		if(tds.length > 0){
			for(int j = 0 ; j < tds.length ; j++){
				TransportData td = tds[j];
				out.println("<tr>");
				if(j == 0){
					out.println("<td rowspan='"+tds.length+"'>"+gi.getName()+"</td><td rowspan='"+tds.length+"'> </td>");
				}
				out.println("<td>("+td.getX()+","+td.getY()+")</td>");
				
				
				GameInfo g2= gm.getGameInfo(td.getTargetMapName());
				if(g2 == null){
					out.println("<td bgcolor='#ff0000'> ------> </td>");
					
					out.println("<td bgcolor='#ff0000'>"+td.getTargetMapName()+"</td>");
					out.println("<td bgcolor='#ff0000'>("+td.getTargetMapX()+","+td.getTargetMapY()+")</td>");
				}else{
					boolean hasBackLink = false;
					TransportData tds2[] = g2.getTransports();
					for(int k = 0 ; k < tds2.length ; k++){
						if(tds2[k].getTargetMapName() != null && tds2[k].getTargetMapName().equals(gi.getName())){
							hasBackLink = true;
						}
					}
					
					if(hasBackLink){
						out.println("<td > <------> </td>");
					}else{
						out.println("<td >  ------> </td>");
					}
					
					out.println("<td>"+td.getTargetMapName()+"</td>");
					out.println("<td>("+td.getTargetMapX()+","+td.getTargetMapY()+")</td>");
					
				}
				out.println("</tr>");
			}
		}else{
			out.println("<tr bgcolor='#bbbbbb'><td>"+gi.getName()+"</td><td> </td><td>---</td><td>---</td><td>---</td><td>---</td></tr>");
		}
	
	}
%>
</table>
</BODY></html>
