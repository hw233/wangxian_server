<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.dbpool.*,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.*,java.lang.reflect.*"%><%
	
%><html>
<head>
</head>
<body>
<br>
<br/>
<%
SimpleEntityManagerOracleImpl manager = (SimpleEntityManagerOracleImpl)SimpleEntityManagerFactory.getSimpleEntityManager(com.mieshi.engineserver.sprite.Player.class);
Class clazz = Class.forName("com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl");
Field f = clazz.getDeclaredField("pool");
f.setAccessible(true);
ConnectionPool pool = (ConnectionPool)f.get(manager);
if(pool != null) {
	pool.reapIdleConnections();
}
Class clazz2 = Class.forName("com.xuanzhi.tools.dbpool.ConnectionPool");
Field f2 = clazz2.getDeclaredField("m_oIdleConnList");
f2.setAccessible(true);
List list = (List)f2.get(pool);
Field f3 = clazz2.getDeclaredField("timeoutSeconds");
f3.setAccessible(true);
int timeout = (Integer)f3.get(pool);
%>
空闲:<%=list.size() %>/<%=pool.getIdleSize() %><br>
<%
long now = System.currentTimeMillis();
for(int i=0; i<list.size(); i++) {
	PooledConnection conn = (PooledConnection)list.get(i);
	long lastCheck = conn.getLastCheckin();
	out.println("["+conn.dumpInfo()+"] [time:"+(now-lastCheck)/1000+"s] [lastCheck:"+lastCheck+"] ["+timeout+"]<br>");
}
%>
}
</body>
</html> 
