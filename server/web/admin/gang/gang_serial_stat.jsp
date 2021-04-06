<%@page contentType="text/html;charset=utf-8" import="com.fy.engineserver.operating.*,com.xuanzhi.gameresource.*,com.fy.engineserver.shop.*,com.fy.engineserver.sprite.*,com.fy.engineserver.gang.model.*"%>

<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page import="com.fy.engineserver.shop.*"%>
<%@page import="com.fy.engineserver.datasource.repute.ReputeManager"%>
<%@page import="com.fy.engineserver.datasource.repute.ReputeDefine"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="com.fy.engineserver.gang.model.*"%>
<%@page import="com.fy.engineserver.gang.service.GangManager"%>
<%@page import="com.fy.engineserver.operating.activities.GangChargeHistoryManager"%>
<%@page import="com.fy.engineserver.operating.activities.DangLeActivityManager"%>
<%@page import="com.fy.engineserver.operating.activities.XinShouCardManager"%>
<%@page import="java.lang.reflect.*"%><%@include file="../IPManager.jsp" %><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		
	</head>
	<body>
		<%
		
			XinShouCardManager xm = XinShouCardManager.getInstance();
			Field f = XinShouCardManager.class.getDeclaredField("serialToArticleMap");
			if(f != null){
				f.setAccessible(true);
				int used = 0;
				Map<String,String> m = (Map<String,String>)f.get(xm);
				String[] ss = m.keySet().toArray(new String[0]);
				for(String s : ss){
					if(xm.isSerialUsed(s)){
						used ++;
					}
				}
				out.println("序列号总数 "+ss.length+" ，已经使用"+used+"，比例"+String.format("%.2f%%",used*1.0/ss.length*100)+"。");
			}
			
		%>
		
	</body>
</html>
