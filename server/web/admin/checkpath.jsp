<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.core.*,com.xuanzhi.tools.transport.*"%><% 
	
	String beanName ="game_manager";
	GameManager sm = null;
	sm = (GameManager)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
	boolean checkPath = "true".equals(request.getParameter("checkpath"));
	boolean usingbucket = "true".equals(request.getParameter("usingbucket"));
	boolean EnableHeartBeatStat = "true".equals(request.getParameter("EnableHeartBeatStat"));
	boolean EnableCollectStat = "true".equals(request.getParameter("EnableCollectStat"));
	boolean submitted = "true".equals(request.getParameter("submitted"));
	
	if(submitted){
		sm.setCheckPath(checkPath);
		sm.setUsingBucket(usingbucket);
		sm.setEnableCollectStat(EnableCollectStat);
		sm.setEnableHeartBeatStat(EnableHeartBeatStat);
	}
	
	%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD> 
<BODY>
<h3>动态改变服务器运行模式</h3>
<form id='f' name='f'>
<input type='hidden' name='submitted' value='true'>
设置检查路径标记：<input type='text' name='checkpath' value='<%=sm.isCheckPath() %>' ><br/>
设置使用桶阵列标记：<input type='text' name='usingbucket' value='<%=sm.isUsingBucket() %>' ><br/>
设置心跳统计标记：<input type='text' name='EnableHeartBeatStat' value='<%=sm.isEnableHeartBeatStat() %>' ><br/>
设置收集标记：<input type='text' name='EnableCollectStat' value='<%=sm.isEnableCollectStat() %>' ><br/>
<input type='submit' value='提交'>
</form>
</BODY></html>
