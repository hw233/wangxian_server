<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.core.*,com.xuanzhi.tools.transport.*"%><% 
	
	String beanName ="game_manager";
	GameManager sm = null;
	sm = (GameManager)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
	String gameId = request.getParameter("game");
	Game game = null;
	
	if(request.getParameter("fb") != null && request.getParameter("fb").equals("true")){
		int index = Integer.parseInt(request.getParameter("index"));
		XinShouChunFuBenManager xscfb = XinShouChunFuBenManager.getInstance();
		if(xscfb != null){
			int k = xscfb.indexOf(gameId);
			game = xscfb.getGames()[k][index];
		}
	}else{
		String country = request.getParameter("country");
		game = sm.getGameByName(gameId,Integer.valueOf(country));
	}
	
	GameInfo gi = game.getGameInfo();
	BucketMatrix bm = game.getCurrentBucketMatrix();
	StringBuffer basicInfo = new StringBuffer();
	int avg = 0;
	int c = 0;
	Bucket buckets[][] = bm.getBuckets();
	for(int i = 0 ; i < buckets.length ; i++){
		for(int j = 0 ; j < buckets[i].length ; j++){
			avg += buckets[i][j].size();
			c++;
		}
	}
	avg = avg/c;
	
	basicInfo.append("基本信息：桶的宽度="+sm.getBucketWidth()+",桶的高度="+sm.getBucketHeight()+"<br/>");
	basicInfo.append("阵列数据：横向="+bm.getBuckets().length+",纵向="+bm.getBuckets()[0].length+",平均="+avg+"<br/>");
	
	long totalCosts[] = game.totalCosts;
	long collectEnterCosts[] = game.collectEnterCosts;
	
	long totalCost = totalCosts[0] + totalCosts[1] + totalCosts[2];
	if(totalCost ==0)totalCost=1;
	long avgCost = (totalCosts[0] + totalCosts[1] + totalCosts[2])/game.heartbeatCount;
	basicInfo.append("心跳数据：<br><table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
	
	basicInfo.append("<tr bgcolor='#00FFFF'><td>平均耗时</td><td>消息处理</td><td>对象心跳函数</td><td>收集进入及变化</td><td>收集离开</td><td>创建/发送</td><td>处理观察者</td><td>清空标记</td></tr>");
	basicInfo.append("<tr bgcolor='#FFFFFF'><td>"+(avgCost)+"ms</td><td>"
			+(totalCosts[0]*100/totalCost)+"%</td><td>"+((totalCosts[1]*100/totalCost))
			+"%</td><td>"+(totalCosts[3]*100/totalCost)
			+"%</td><td>"+(totalCosts[4]*100/totalCost)
			+"%</td><td>"+((totalCosts[5]-totalCosts[8])*100/totalCost)+"%/"+(totalCosts[8]*100/totalCost)
			+"%</td><td>"+(totalCosts[6]*100/totalCost)
			+"%</td><td>"+(totalCosts[7]*100/totalCost)
			+"%</td></tr>");
	basicInfo.append("</table>");
	
	basicInfo.append("收集进入及变化数据：<br><table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
	
	basicInfo.append("<tr bgcolor='#00FFFF'><td>平均耗时</td><td>收集属性变化</td><td>收集新进入的人</td><td>收集路径</td><td>不在窗口的人</td></tr>");
	long totalCollectCost = collectEnterCosts[0];
	if(totalCollectCost== 0)totalCollectCost = 1;
	basicInfo.append("<tr bgcolor='#FFFFFF'><td>"+(totalCollectCost/game.heartbeatCount)+"ms</td><td>"
			+(collectEnterCosts[1]*100/totalCollectCost)
			+"%</td><td>"+(collectEnterCosts[2]*100/totalCollectCost)
			+"%</td><td>"+(collectEnterCosts[3]*100/totalCollectCost)
			+"%</td><td>"+(collectEnterCosts[4]*100/totalCollectCost)
			+"%</td></tr>");
	basicInfo.append("</table>");
	
	
	%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY>
<h2><%=gameId %>，桶阵列的情况</h2><br><a href="./bucket.jsp?game=<%=gameId %>">刷新此页面</a><br>
<%= basicInfo.toString() %>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<%
for(int i = 0 ; i < buckets[0].length ; i++){
	out.println("<tr bgcolor='#FFFFFF' align='center'>");
	for(int j = 0 ; j < buckets.length ; j++){
		out.println("<td>"+buckets[j][i].size()+"</td>");
	}
	out.println("</tr>");
}
%>
</table>	
</BODY></html>
