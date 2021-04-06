<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	int count = 0;
	JiazuManager jiazuManager = JiazuManager.getInstance();
	Map<Long, Jiazu> map = jiazuManager.getJiazuLruCacheByID();
	for (Iterator<Long> itor = map.keySet().iterator(); itor.hasNext();) {
		Long jiazuId = itor.next();
		Jiazu jiazu = map.get(jiazuId);
		if (jiazu != null) {
			Set<JiazuMember> set = jiazuManager.getJiazuMember(jiazuId);
			if (set != null) {
				for (JiazuMember jm : set) {
					if (jm != null) {
						try {
							//if (GamePlayerManager.getInstance().isOnline(jm.getPlayerID())) {
							Player p = GamePlayerManager.getInstance().getPlayerInCache(jm.getPlayerID());
							if (p != null) {
								count++;
								p.setDirty(true, "wage");
								out.print("[设置玩家工资] [家族:" + jiazu.getName() + "] [玩家:" + p.getName() + "]" + "<BR/>");
							}
							//}/
						} catch (Exception e) {

						}
					}
				}
			}
		}
	}
	out.print("设置完成[" + count + "]<BR/>");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>