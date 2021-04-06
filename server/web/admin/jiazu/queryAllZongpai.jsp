<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		long[] ids = zpm.em.queryIds(ZongPai.class,"");
		List<ZongPai> list = new ArrayList<ZongPai>();
		if(ids != null && ids.length >0){
			for(long id:ids){
				ZongPai zp = zpm.getZongPaiById(id);
				list.add(zp);
			}
		}else{
			out.print("没有宗派");
		}
	%>

	<table border="1" cellpadding="1" cellspacing="1">
		<tr>
			<td>ID</td>
			<td>宗派名</td>
			<td>宗主name</td>
			<td>国家</td>
			<td>家族数</td>
		</tr>
		<%
			for(ZongPai zp : list){
				
				Player player = PlayerManager.getInstance().getPlayer(zp.getMasterId());
				%>
				<tr>
					<td><%=zp.getId() %></td>
					<td><%=zp.getZpname() %></td>
					<td><%=player.getName() %></td>
					<td><%=player.getCountry()%></td>
					<td><%=zp.getJiazuIds().size()%></td>
				</tr>
				<%
			}
		%>
	</table>
</body>
</html>