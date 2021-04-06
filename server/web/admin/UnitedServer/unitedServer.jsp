<%@page import="java.sql.Statement"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.xuanzhi.tools.dbpool.ConnectionPool"%>
<%@page
	import="com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServer"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<UnitedServer> serverList = new ArrayList<UnitedServer>();

	UnitedServer main = new UnitedServer("巍巍昆仑", "/home/game/resin/webapps/game_server/WEB-INF/spring_config/simpleEMF.xml", true);
	UnitedServer s1 = new UnitedServer("无量净土", "/home/game/resin/webapps/game_server/WEB-INF/spring_config/wl_simpleEMF.xml", false);

	serverList.add(main);
	serverList.add(s1);
	long startTime = System.currentTimeMillis();
	out.print("<HR>合服开始<HR>");
	if (false) {
		for (UnitedServer us : serverList) {
			SimpleEntityManager<Player> m = us.getFactory().getSimpleEntityManager(Player.class);

			DefaultSimpleEntityManagerFactory factory = us.getFactory();
			Field f = DefaultSimpleEntityManagerFactory.class.getDeclaredField("pool");
			f.setAccessible(true);
			ConnectionPool pool = (ConnectionPool) f.get(factory);

			Field initialize_successF = DefaultSimpleEntityManagerFactory.class.getDeclaredField("initialize_success");
			initialize_successF.setAccessible(true);
			boolean initialize_success = (Boolean) initialize_successF.get(factory);

			System.out.println(us.toString() + "      initialize_success=" + initialize_success);

			Field urlF = ConnectionPool.class.getDeclaredField("url");
			urlF.setAccessible(true);
			String url = (String) urlF.get(pool);

			Field usernameF = ConnectionPool.class.getDeclaredField("username");
			usernameF.setAccessible(true);
			String username = (String) usernameF.get(pool);

			System.out.println(">>>>>>>>>>>>>>>>>>>>>" + m);
			System.out.println("=====================" + ((GamePlayerManager) GamePlayerManager.getInstance()).em);
			System.out.println("=====================" + pool);
			System.out.println(us.toString() + "    url:" + url + " username:" + username + "<BR/> " + m.nextId());

			Connection connection = pool.getConnection();
			Statement stmt = connection.createStatement();
			for (Iterator<Object> itor = connection.getClientInfo().keySet().iterator(); itor.hasNext();) {
				Object o = itor.next();
				out.print(us.toString() + " " + o + " : " + connection.getClientInfo().getProperty(String.valueOf(o)) + "<BR/>");
			}

			long num = m.count();
			System.out.println("<HR>");
			System.out.println(num);
			System.out.println("<HR>");
		}
	}
	UnitedServerManager.getInstance().uniteServerBegin(serverList.toArray(new UnitedServer[0]));
	out.print("<HR>合服完成,耗时:" + (System.currentTimeMillis() - startTime) + "ms<HR>");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>