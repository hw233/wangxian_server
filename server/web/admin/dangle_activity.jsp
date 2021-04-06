<%@page contentType="text/html;charset=utf-8" import="com.fy.engineserver.operating.*,com.xuanzhi.gameresource.*,com.fy.engineserver.shop.*,com.fy.engineserver.sprite.*,com.fy.engineserver.gang.model.*"%>

<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page import="com.fy.engineserver.shop.*"%>
<%@page import="com.fy.engineserver.datasource.repute.ReputeManager"%>
<%@page import="com.fy.engineserver.datasource.repute.ReputeDefine"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="com.fy.engineserver.gang.model.*"%>
<%@page import="java.lang.reflect.*"%>
<%@page import="java.io.*"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.*"%>


<%@page import="com.fy.engineserver.gang.service.GangManager"%>
<%@page import="com.fy.engineserver.operating.activities.GangChargeHistoryManager"%>
<%@page import="com.fy.engineserver.operating.activities.DangLeActivityManager"%><%@include file="IPManager.jsp" %><html>
	<head>
		
	</head>
	<body>
		<table border="1">
		<tr>
			<td width="10%">序列号</td>
			<td width="10%">物品名称</td>
			
		</tr>
		<%
			DangLeActivityManager dm = DangLeActivityManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			Player p = pm.getPlayer("11111111");
			
			String k = "6AJfun1zPPwy";
			
			Field f = DangLeActivityManager.class.getDeclaredField("ddc");
			try{
				f.setAccessible(true);
				Object o = f.get(dm);
				
				if(o == null){
					DefaultDiskCache ddc = new DefaultDiskCache(new File("/a.data"), null,
							"当乐活动序列号管理", 100L * 365 * 24 * 3600 * 1000L, false);
					f.set(dm,ddc);
					
					out.println("设置属性成功");
				}
			}catch(Exception e){
				out.println("设置ddc出错" + e.getMessage());
			}
			
			try{
				out.println(dm.getGift(p,k));
				out.println("领取成功");
			}catch(Exception e){
				out.println("领取出错"+e.getMessage());
			}
						
		%>
		</table>
		
	</body>
</html>
