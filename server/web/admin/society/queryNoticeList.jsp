<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerId = request.getParameter("playerId");
	String addPlayerId = request.getParameter("addPlayerId");
	SocialManager sm = SocialManager.getInstance();
	List<Long> list = null;
	if (playerId != null && !"".equals(playerId)) {
		Relation relation = sm.getRelationById(Long.parseLong(playerId));
		list = relation.getNoticePlayerId();
		if (list == null || list.size() == 0) {
			out.print("没有关注人");
			return;
		} else {
			if (addPlayerId != null && !"".equals(addPlayerId)) {
				if (sm.getRelationA2B(Long.parseLong(playerId), Long.parseLong(addPlayerId)) < 0) {
					out.print("添加失败,两人不是好友关系<br>");
					//if (list.contains(Long.parseLong(addPlayerId))) {
						//list.remove(Long.parseLong(addPlayerId));
						//out.print("不是好友,移除<br>");
					//}
				} else {
					list.add(Long.parseLong(addPlayerId));
					relation.setNoticePlayerId(list);
					sm.cache.put(Long.parseLong(playerId), relation);
					out.print("添加成功<br>");
				}
			}
			for (long id : list) {
				out.print(id + "<br>");
			}
		}
	}
%>


<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>

<%@page import="java.util.List"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询玩家好友上下线通知列表</title>
</head>
<body>
<form action="">查询的玩家id:<input type="text" name="playerId"
	value="<%=playerId == null ? "" : playerId%>" /><br>
要添加的玩家id:<input type="text" name="addPlayerId"
	value="<%=addPlayerId == null ? "" : addPlayerId%>" />(仅查询时不需填)<br>
<input type="submit" value="提交"></form>


</body>
</html>