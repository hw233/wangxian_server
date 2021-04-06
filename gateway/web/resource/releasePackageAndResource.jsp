<%@page import="com.fy.gamegateway.language.Translate"%>
<%@page import="com.xuanzhi.language.translate"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.util.zip.*,
java.io.*,com.fy.gamegateway.mieshi.resource.manager.*,com.fy.gamegateway.message.*,
com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.transport.*"%><%
	MieshiGatewaySubSystem gateway = MieshiGatewaySubSystem.getInstance();

	PackageManager pm = PackageManager.getInstance();
	ResourceManager rm = ResourceManager.getInstance();
	String action = request.getParameter("action");
	if("enterpublish".equals(action)){
		
		gateway.setPublishingPackageAndResource(true);
		
		String descriptionInUUB = Translate.系统正在发布新的资源持续;
		int width = 0;
		int height = 0;
		String[] btns = new String[]{Translate.继续等待,Translate.退出游戏};
		byte[] oType = new byte[]{(byte)0,(byte)1};
		
		OPEN_WINDOW_REQ owq = new OPEN_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), 0, "", descriptionInUUB, width, height, btns, oType);

		
		MieshiGatewayServer server = MieshiGatewayServer.getInstance();
		DefaultConnectionSelector dcs = null;
		dcs = (DefaultConnectionSelector)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean("gateway_mieshi_user_selector");
		
		ConnectionSelectorHelper helper = new ConnectionSelectorHelper(dcs);
		Connection conns [] = helper.getAllConnections();
		
		for(int i = 0 ; i < conns.length ; i++){
			server.sendMessageToClient(conns[i],owq);
		}
		
		Thread.sleep(10000);
		
		for(int i = 0 ; i < conns.length ; i++){
			conns[i].close();
		}
		
		out.println("所有玩家都将收到弹矿提示，并且连接已经中断！");
		
	}else if("exitpublish".equals(action)){
		
		gateway.setPublishingPackageAndResource(false);
		gateway.handleWaitingList();
		
	}else if("package".equals(action)){
		if(gateway.isPublishingPackageAndResource()){
			pm.refreshRealPackage(false);
			out.println("从测试程序包复制到正式程序包，请查看正式程序包页面");
		}else{
			out.println("不在发布正式的程序包和资源包过程中，不能发布！");
		}
	}else if("resource".equals(action)){
		if(gateway.isPublishingPackageAndResource()){
			rm.copyTestResourceToRealResource();
			out.println("从测试资源包复制到正式资源包，请查看正式资源包页面");
		}else{
			out.println("不在发布正式的程序包和资源包过程中，不能发布！");
		}
	}else if("zipresource".equals(action)){
		if(gateway.isPublishingPackageAndResource()){
			rm.createRealResourcePackageForCurrentVersion();
			out.println("已经将资源打成zip供下载");
		}else{
			out.println("不在发布正式的程序包和资源包过程中，不能发布！");
		}
	}else if ("miniZip".equals(action)) {
		String re = MiniResourceZipManager.instance.createMiniResourceZip();
		out.println(re);
	}
	
%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.ResourceManager"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.PackageManager"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.PackageManager.Version"%><html>
<head>
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../css/table.css"/>

</head>
<body>
<center>
<h2>程序包和资源的发布页面</h2><br><a href="./releasePackageAndResource.jsp">刷新此页面</a><br>

<h2><font color='red'>此发布会导致用户更新版本和更新资源，所以要特别特别的小心，如果你不能确认要发布，请不要发布，出错后果非常严重！！！</font></h2>

<h2> <%
	if(gateway.isPublishingPackageAndResource()){
		out.println("正处于发布正式的程序包和资源包过程中，所有的玩家请求都被挂起！");
	}else{
		out.println("不在发布正式的程序包和资源包过程中，不能发布！");
	}
%></h2>

<br>
<% if(gateway.isPublishingPackageAndResource() == false){ %>
<form name="f0">
<input type='hidden' name='action' value='enterpublish'>
<input type="submit" value="设置进入发布流程，所有玩家将弹出提示框，并在10秒钟后断线！并阻塞玩家请求！">
</form>
<br/>
<br/>
<% }else{ %>
<form name="f0">
<input type='hidden' name='action' value='exitpublish'>
<input type="submit" value="设置发布完成，所有玩家请求开始处理！">
</form>
<br/>
<br/>
<% } %>
<form name="f0">
<input type='hidden' name='action' value='miniZip'>
<input type="submit" value="根据测试资源包和正式资源包生成miniZip包">
</form>
<br/>
<br/>
<form name="f1">
<input type='hidden' name='action' value='package'>
<input type="submit" value="更新到正式程序包,从测试程序包复制到正式程序包">
</form>
<br/>
<br/>
<form name="f2">
<input type='hidden' name='action' value='resource'>
<input type="submit" value="更新到正式资源包,从测试资源包复制到正式资源包">
</form>
<br/>
<br/>
<form name="f3">
<input type='hidden' name='action' value='zipresource'>
<input type="submit" value="将资源包打成zip包">
</form>

</center>
</body>
</html> 
