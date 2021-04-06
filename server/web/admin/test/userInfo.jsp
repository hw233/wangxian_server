<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.message.USER_CLIENT_INFO_REQ"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String playerName = request.getParameter("playerName");
	if (playerName == null || "".equals(playerName.trim())) {
		playerName = "";
	}
	if (!"".equals(playerName)) {
		Player p = GamePlayerManager.getInstance().getPlayer(playerName);
		
		if (p == null) {
			out.print("角色不存在<BR>");
		} else {
			p.initClientInfo();
			out.print("clientId：" +p.getClientInfo("clientId") + "<HR>");
			Map<String,String> map = p.getUSER_CLIENT_INFO_map();
			for (Iterator<String> itor = map.keySet().iterator();itor.hasNext();) {
				String key = itor.next();
				String value = map.get(key);
				out.print(key + "------>" + value + "<BR>");
			}
			map = initClientInfo(p);
			for (Iterator<String> itor = map.keySet().iterator();itor.hasNext();) {
				String key = itor.next();
				String value = map.get(key);
				out.print(key + "---JSP--->" + value + "<BR>");
			}
		}
	}
%>
<%!
public HashMap<String,String> initClientInfo(Player p){
	HashMap<String,String> USER_CLIENT_INFO_map = new HashMap<String,String>();
	if (p.getConn() != null) {
		Object o = p.getConn().getAttachmentData("USER_CLIENT_INFO_REQ");
		if (o instanceof USER_CLIENT_INFO_REQ) {
			USER_CLIENT_INFO_REQ req = (USER_CLIENT_INFO_REQ) o;
			Field [] fields = USER_CLIENT_INFO_REQ.class.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				try {
					Object object = f.get(req);
					USER_CLIENT_INFO_map.put(f.getName(), String.valueOf(object));
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	} else {
		USER_CLIENT_INFO_map = null;
	}
	return USER_CLIENT_INFO_map;
}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="./userInfo.jsp" method="post">
	角色名:<input name="playerName" type="text" value="<%=playerName%>">
	<input type="submit">
</form>
</body>
</html>