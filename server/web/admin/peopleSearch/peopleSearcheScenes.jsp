<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page
	import="com.fy.engineserver.activity.peoplesearch.PeopleSearch"%>
<%@page
	import="com.fy.engineserver.activity.peoplesearch.PeopleSearchManager"%>
<%@page
	import="com.fy.engineserver.activity.peoplesearch.PeopleSearchScene"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	PeopleSearchScene[] allScenes = PeopleSearchManager.getInstance().getPeopleSearchScenes();
%>
<%!SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table style="font-size: 12px;">
		<tr style="background-color: #8FA7ED;">
			<td>序号</td>
			<td>主人</td>
			<td>创建时间</td>
			<td>主人最后存在时间</td>
			<td>boss是否刷出来了</td>
			<td>主人是否进入过</td>
			<td>所有生物</td>
		</tr>
		<%
			for (int i = 0; i < allScenes.length; i++) {
				PeopleSearchScene scene = allScenes[i];
		%>
		<tr style="background-color: #CEA8E6;">
			<td><%=i%></td>
			<td><%=scene.getOwner().getName()%></td>
			<td><%=sdf.format(scene.getCreateTime())%></td>
			<td><%=sdf.format(scene.getPlayerLastExistTime())%></td>
			<td><%=scene.isBossBourn()%></td>
			<td><%=scene.isOwnerEnter()%></td>
			<td><% for (LivingObject lo : scene.getGame().getLivingObjects()) {
				StringBuffer sbf = new StringBuffer(lo.getClass().getSimpleName());
				if (lo instanceof Player) {
					Player p = (Player) lo;
					sbf.append("|名字").append(p.getName()).append("|等级:").append(p.getLevel()).append("|血量:").append(p.getHp()+"/"+p.getMaxHP());
				} else if (lo instanceof Monster) {
					Monster p = (Monster) lo;
					sbf.append("|名字").append(p.getName()).append("|等级:").append(p.getLevel()).append("|血量:").append(p.getHp()+"/"+p.getMaxHP());
				} else if (lo instanceof Pet) {
					Pet p = (Pet) lo;
					sbf.append("|名字").append(p.getName()).append("|等级:").append(p.getLevel()).append("|血量:").append(p.getHp()+"/"+p.getMaxHP());
				}
				sbf.append("<BR/>");
				out.print(sbf.toString());
			}%></td>
		</tr>
		<%
			}
		%>
	</table>

</body>
</html>