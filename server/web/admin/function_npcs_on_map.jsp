<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.io.*,com.xuanzhi.tools.text.*,com.fy.engineserver.menu.*,com.fy.engineserver.downcity.*,com.fy.engineserver.core.*,com.fy.engineserver.sprite.npc.*"%><%!

	String format_window(MenuWindow mw,String npcName){
		StringBuffer sb = new StringBuffer();
		sb.append("<a name='"+mw.getId()+"'></a>");
		sb.append("<table>");
		sb.append("<tr><td valign='top'>");
		
		sb.append("<table class='windowMenuTable'>");
		sb.append("<tr class='titlecolor' align='center'><td colspan='2'>"+npcName+"</td></tr>");
		sb.append("<tr class='titlecolor' align='center'><td colspan='2' alt='"+(mw.isShowTask()?"显示任务":"不显示任务")+"'>"+mw.getTitle()+"</td></tr>");
		sb.append("<tr><td width='20%' height='15'></td><td></td></tr>");
		sb.append("<tr><td width='20%'></td><td>"+uub2Html(mw.getDescriptionInUUB())+"</td></tr>");
		sb.append("<tr><td width='20%' height='15'></td><td></td></tr>");

		Option ops[] = mw.getOptions();
		for(int i = 0 ; i < ops.length ; i++){
			sb.append("<tr><td><img src='/game_server/imageServlet?id="+ops[i].getIconId()+"'></td>");
			if(ops[i].getOType() == Option.OPTION_TYPR_WAITTING_NEXT_WINDOW){
				sb.append("<td><a href='#"+ops[i].getOId()+"'><font color='#"+Integer.toHexString(ops[i].getColor())+"'>"+ops[i].getText()+"</font></a></td></tr>");
			}else if(ops[i].getOType() == Option.OPTION_TYPR_CLIENT_FUNCTION){
				sb.append("<td><font color='#"+Integer.toHexString(ops[i].getColor())+"'><input type='text' value='"+ops[i].getText()+"' title='[客户端功能]'></font><td></tr>");
			}else{
				sb.append("<td><font color='#"+Integer.toHexString(ops[i].getColor())+"'><input type='text' value='"+ops[i].getText()+"' title='[服务器功能："+ops[i].toString()+"]'></font></td></tr>");
			}
			
		}
		sb.append("</table>");

		sb.append("</td></tr></table>");
		
		return sb.toString();
	}

	String uub2Html(String uub){
		uub = uub.replaceAll("\\[/color\\]","</font>");
		uub = uub.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		uub = uub.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
		return uub;
	}
%><% 
	WindowManager manager = WindowManager.getInstance();
%>	
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
.bigtable{
width:100%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
table{
margin-top: 0px;
width:100%;
border:0px solid #69c;
}
.windowMenuTable{
width:200px;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
}
td{
border:0px solid #69c;
}
.bigtd{
border:1px solid #69c;
text-align:left;
}
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY >
<h2>地图功能NPC</h2>
<%
GameManager gm = GameManager.getInstance();
if(gm != null){
Game games[] = gm.getGames();
%>
<table class="bigtable" id="bigTable">
<tr class="titlecolor"><td class="bigtd">地图名</td><td class="bigtd">NPC信息</td></tr>
<%
for(int i = 0 ; i < games.length ; i++){
Game game = games[i];
if(game != null){
	%>
	<tr>
	<td class="bigtd"><a href="gameinfo.jsp?game=<%=game.getGameInfo().getName() %>"><%=game.getGameInfo().getName() %></a></td>
	<td class="bigtd">
		<table>
	<tr>
	<%
	LivingObject[] livingObjects = game.getLivingObjects();
	if(livingObjects != null){
		List<MenuWindow> menuList = new ArrayList<MenuWindow>();
		List<String> npcNameList = new ArrayList<String>();
		for(LivingObject lo : livingObjects){
			if(lo instanceof NPC){
				MenuWindow mw = manager.getWindowById(((NPC)lo).getWindowId());
				if(mw != null){
					menuList.add(mw);
					npcNameList.add(((NPC)lo).getName());
				}
			}
		}

		for(int m = 0 ; m < menuList.size() ; m++){
			out.println("<td valign='top'>"+format_window(menuList.get(m),npcNameList.get(m))+"</td>");
			if( ((m+1) % 3) == 0){
				out.println("</tr><tr>");
			}
		}
	}
	%>
	</tr></table>
	</td>
	</tr>
	<%
}
}
%>
</table>
<%
}else{
	out.println("instance error");
}
%>
</BODY></html>
