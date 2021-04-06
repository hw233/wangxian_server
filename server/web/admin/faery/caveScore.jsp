<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.homestead.faery.Faery"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String scoreStr = request.getParameter("score");
	List<Cave> caves = new ArrayList<Cave>();
	if (scoreStr == null || "".equals(scoreStr.trim())) {
		out.print("<H1>请输入你要查询的仙府得分下限</H1>");
	} else {
		int score = Integer.valueOf(scoreStr);
		if (score <= 0) {
			out.print("<H1>你输入的分数太少了</H1>");
		} else {
			Hashtable<Long, Faery> faeryMap = FaeryManager.getInstance().getFaeryMap();
			for (Iterator<Long> faeryIds = faeryMap.keySet().iterator();faeryIds.hasNext();) {
				Long faeryId = faeryIds.next();
				Faery faery = faeryMap.get(faeryId);
				if (faery != null) {
					for (Cave cave : faery.getCaves()) {
						if (cave != null) {
							long caveScore= cave.getCaveScore();
							if (caveScore >= score) {
								caves.add(cave);
							}
						}
					}
				}
			}
			Collections.sort(caves,order);
		}
	}
%>
<%!
	Comparator<Cave> order = new Comparator<Cave>(){
		public int compare(Cave o1, Cave o2) {
			return (int)(o2.getCaveScore() - o1.getCaveScore());
		}
	};
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="./caveScore.jsp">
	填写你要查的分数下限,结果>=<input name="score" type="text" value="<%=scoreStr%>">
	<input type="submit" value="谨慎的查询一下">
</form>
<%if(caves.size() > 0) {%>
<hr/>
	一共条数:<%=caves.size() %>
<hr/>
<table style="font-size: 12px;text-align: right;">
	<tr style="background-color: #57ADF4;">
		<td>序号</td>
		<td>角色ID</td>
		<td>分数</td>
		<td>仙府等级</td>
	</tr>
	<%for (int i = 0; i < caves.size(); i++) { 
		Cave cave = caves.get(i);
	%>
	<tr style="background-color: <%=i%2==0?"#29A600":""%>">
		<td><%=i %></td>
		<td><%=cave.getOwnerId() %></td>
		<td><%=cave.getCaveScore() %></td>
		<td><%=cave.getMainBuilding().getGrade() %></td>
	</tr>
	<%} %>
</table>
<%} else {
	
	out.print("<hr/><H1>没有相关数据</H1><hr/>");
}%>
</body>
</html>