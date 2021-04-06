<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.gm.custom.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.sprite.*,java.util.*,java.lang.reflect.*,com.google.gson.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.gm.record.ActionManager"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%><html>
<%-- <%@include file="../authority.jsp" %> --%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>玩家ID角色页面 </title>
</head>
<body>
<%
try{
Gson gson = new Gson();
String gmname = session.getAttribute("username").toString();
ActionManager amanager = ActionManager.getInstance();
String userName = request.getParameter("text");
PlayerManager mpm = PlayerManager.getInstance();
String playerName = request.getParameter("playerName");  
String message = null;
String username = session.getAttribute("username").toString();
if(playerName != null) {
	int career = Integer.parseInt(request.getParameter("career"));
	int politicalCamp = Integer.parseInt(request.getParameter("politicalCamp"));
	int sex = Integer.parseInt(request.getParameter("sex"));
	short level = Short.parseShort(request.getParameter("level").trim());
	try {
	    if(result!=null&&"true".equals(result)){
// 		Player p = mpm.createPlayer(userName,playerName,(byte)sex,(byte)politicalCamp,career);
		
// 		p.setLevel(level);
		amanager.save(gmname,"为账号"+userName+"添加了一个角色："+playerName);
		out.print("添加成功");
		}
		else
		out.print("非常抱歉您的权限不够");
	} catch(Exception e) {
		e.printStackTrace();
		message = e.getMessage();
	}
}
Player players[] = mpm.getPlayerByUser(userName);
if(players.length>0){
%>
<table cellspace='1' border='0' bgcolor='black'>
<tr bgcolor='#C2CAF5' align="center">
<td colspan="5">用户<%=userName %>在游戏中拥有的能运行的角色</td>
</tr>
<tr bgcolor='#C2CAF5'>
<td>角色ID</td><td>角色名</td><td>等级</td><td>性别</td><td>职业</td>
</tr>
<%for(Player player : players){ 
String career = "";
if(player.getCareer() == 0){
	career ="少林";
}else if(player.getCareer() == 1){
	career ="武当";
}else if(player.getCareer() == 2){
	career ="峨眉";
}else if(player.getCareer() == 3){
	career ="明教";
}else if(player.getCareer() == 4){
	career ="五毒";
}

%>
<tr bgcolor='white'>
<td><a href="user_action.jsp?playerid=<%=player.getId()%>"><%=player.getId() %></a></td>
<td><%=player.getName() %></td>
<td><%=player.getLevel() %></td>
<td><%=player.getSex()==0?"男":"女" %></td>
<td><%=career %></td>
</tr>
<%} }%>
</table>
<form action="resultbyid.jsp">
	角色名：<input type="text" size=15 name=playerName>
	职业:
	<select name="career">
	<option value="0">少林</option>
	<option value="1">武当</option>
	<option value="2">峨眉</option>
	<option value="3">明教</option>
	<option value="4">五毒</option>
	</select>
	性别:
	<select name="sex">
	<option value="0">男</option>
	<option value="1">女</option>
	</select>
	阵营:
	<select name="politicalCamp">
	<option value="0">中立</option>
	<option value="1">紫薇宫</option>
	<option value="2">日月盟</option>
	</select>
	级别：
	<select name='level'>
     <option value='80'>80</option>
     <option value='70'>70</option>
     <option value='60'>60</option>
     <option value='50'>50</option>
     <option value='40'>40</option>
     <option value='30'>30</option>
     <option value='20'>20</option>
	</select>
	<input type="hidden" name="text" value="<%=userName%>">
	<input type=submit name=sub1 value=创建新玩家>
</form>
<%=(message==null?"":message)%>
<%}catch(Exception e){
  //RequestDispatcher rdp = request
	//						.getRequestDispatcher("visitfobiden.jsp");
		//			rdp.forward(request, response);
		  out.print(StringUtil.getStackTrace(e));
} %>
</body>
</html>
