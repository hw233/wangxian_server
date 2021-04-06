<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Dao"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ceng"%>
<%@page import="com.fy.engineserver.authority.AuthorityAgent"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ta"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String action = request.getParameter("action");
	if (action != null) {
		if (action.equals("flushBug")) {
			AuthorityAgent.getInstance().init();
			for (Object key : QianCengTaManager.getInstance().cache.keySet()) {
				QianCengTa_Ta ta = (QianCengTa_Ta)QianCengTaManager.getInstance().cache.get(key);
				ta.setAuthority(null);
			}
			out.print(AuthorityAgent.getInstance().getConfigMap().size() + "<br>");
		}else if (action.equals("playerBug")) {
			long pid = Long.parseLong(request.getParameter("pid"));
			Player p = PlayerManager.getInstance().getPlayer(pid);
			if (p != null) {
				QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(pid);
				int size = ta.getDaoList().size();
				out.print("size=="+size+"<br>");
				if (size < 6) {
					QianCengTa_Dao dao = ta.getDaoList().get(size - 1);
					out.print("dao=="+dao.getDaoIndex()+"   "+ta.getMaxDao()+"   "+ta.getMaxCengInDao()+"<br>");
					if (ta.getMaxDao() == dao.getDaoIndex()-1 && ta.getMaxCengInDao() == 23) {
						out.print("开启道成功<br>");
						ta.openDao(1, dao.getDaoIndex());
						ta.setMaxDao(dao.getDaoIndex());
						ta.setMaxCengInDao(-1);
					}
				}
			}
		}
	}
%>
	<br>
	刷新解决千层塔bug
	<form>
		<input type="hidden" name="action" value="flushBug">
		<input type="submit" value="解决千层塔bug">
	</form>
	<br>
	解决玩家bug
	<form>
		<input type="hidden" name="action" value="playerBug">
		<input type="text" name="pid">
		<input type="submit" value="解决千层塔bug">
	</form>
</body>
</html>