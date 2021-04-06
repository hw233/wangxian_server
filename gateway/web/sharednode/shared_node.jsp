<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%>
<%@page import="java.io.*,com.fy.gamegateway.mieshi.resource.manager.*,"%><%!
%><%

ResourceSharedNodeManager nodeManager = ResourceSharedNodeManager.getInstance();

String action = request.getParameter("action");
if("add_package_channel".equals(action)){
	
	String channel = request.getParameter("channel");
	String node = request.getParameter("url");
	String gpu = request.getParameter("gpuType");
	nodeManager.addPackageNode(channel.trim(),node.trim(), gpu);
	PackageSharedNode packageSharedNode = nodeManager.getPackageSharedNodeByChannel(channel, gpu);
	if (packageSharedNode != null) {
		if (packageSharedNode.getPackageVersion() == null) {
			String aaa = node.trim();
			String bbb = aaa.substring(aaa.indexOf("mieshi_auto_") + "mieshi_auto_".length());
			String ccc = bbb.substring(0, bbb.indexOf("_half"));
			packageSharedNode.setPackageVersion(ccc);
		}
	}
	nodeManager.saveTo(nodeManager.getConfigFile());
	
}else if ("add_package_channel2".equals(action)) {
	String channel = request.getParameter("channel");
	String node = request.getParameter("url");
	String version  = request.getParameter("cVersion");
	String apkName  = request.getParameter("apkName");
	String gpu = request.getParameter("gpuType");
	nodeManager.addPackageNode(channel.trim(),node.trim(), gpu);
	PackageSharedNode packageSharedNode = nodeManager.getPackageSharedNodeByChannel(channel, gpu);
	if (packageSharedNode != null) {
		packageSharedNode.setPackageVersion(version);
		packageSharedNode.setFilename(apkName);
	}
	nodeManager.saveTo(nodeManager.getConfigFile());
}else if("del_package_channel".equals(action)){
	
	String index = request.getParameter("index");
	nodeManager.removePackageNode(Integer.parseInt(index));
	nodeManager.saveTo(nodeManager.getConfigFile());
	
}else if("del_channel".equals(action)){
	String channel = request.getParameter("channel");
	nodeManager.removeChannel(channel.trim());
	nodeManager.saveTo(nodeManager.getConfigFile());
}else if("add_channel".equals(action)){
	String channel = request.getParameter("channel");
	String node = request.getParameter("node");
	nodeManager.addChannel2NodeMap(channel.trim(),node.trim());
	nodeManager.saveTo(nodeManager.getConfigFile());
}else if("add_node".equals(action)){
	String node = request.getParameter("node");
	nodeManager.addNode(node.trim(),true);
	nodeManager.saveTo(nodeManager.getConfigFile());
}else if ("remove_node".equals(action)) {
	String node = request.getParameter("node");
	nodeManager.removeNode(node);
	nodeManager.saveTo(nodeManager.getConfigFile());
}else if ("ref_node".equals(action)) {
	nodeManager.getNodeList().clear();
	nodeManager.getChannle2NodeMap().clear();
	nodeManager.getChannle2PackageNodes().clear();
	nodeManager.load(nodeManager.getConfigFile());
} else if("add_url".equals(action)){
	String node = request.getParameter("node");
	String url = request.getParameter("url");
	ResourceSharedNode n = nodeManager.getResourceSharedNode(node.trim());
	n.addURL(url.trim());
	nodeManager.saveTo(nodeManager.getConfigFile());
} else if("del_url".equals(action)){
	String node = request.getParameter("node");
	int i = Integer.parseInt(request.getParameter("urlindex"));
	ResourceSharedNode n = nodeManager.getResourceSharedNode(node.trim());
	n.removeURL(i);
	nodeManager.saveTo(nodeManager.getConfigFile());
} else if("enable_node".equals(action)){
	String node = request.getParameter("node");
	ResourceSharedNode n = nodeManager.getResourceSharedNode(node.trim());
	n.setEnable(!n.isEnable());
	nodeManager.saveTo(nodeManager.getConfigFile());
} 

%>
<head>
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../css/table.css"/>
</head>
<body>
<center>
<h2>资源包共享节点管理后台</h2>
<br><a href="./shared_node.jsp">刷新此页面</a><br>
<br>

<b>渠道对应的程序包节点</b>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>渠道</td>
	<td>程序包地址</td>
	<td>是否有效</td>
	<td>文件名</td>
	<td>版本</td>
	<td>GPU</td>
	<td>文件大小</td>
	<td>检查结果</td>
	<td>上一次检查时间</td>
	<td>操作</td>
</tr>
<%
PackageSharedNode nodes[] = nodeManager.getAllPackageSharedNodes();
for(int i = 0 ; i < nodes.length ; i++){
	PackageSharedNode node = nodes[i];
	if (!node.isEnable()) {
		node.setEnable(true);
	}
	out.println("<tr bgcolor='#FFFFFF' align='center'>");
	out.println("<td>"+node.getChannel()+"</td>");
	out.println("<td>"+node.getUrl() +"</td>");
	out.println("<td>"+node.isEnable()+"</td>");
	out.println("<td>"+node.getFilename()+"</td>");
	out.println("<td>"+node.getPackageVersion()+"</td>");
	out.println("<td>"+node.getGupType()+"</td>");
	out.println("<td>"+node.getFileSize()+"</td>");
	out.println("<td>"+node.isCheckEnable()+"</td>");
	out.println("<td>"+(System.currentTimeMillis()/1000- node.getLastCheckTime()/1000)+"秒前</td>");
	out.println("<td><a href='./shared_node.jsp?action=del_package_channel&index="+i+"'>删除</a></td>");
	out.println("</tr>");
}
%>	
</table>
<form id="f"><input type='hidden' name='action' value='add_package_channel'>
增加新的渠道程序包：<br>
渠道：<input type='text' name='channel' value=''> 
程序包：<input type='text' name='url' value='' size=50> 
gpu(auto,etc_general)：<input type='text' name='gpuType' value='' size=30> 
	<input type='submit' value='提交'>
</form>
<br>
<br>
<form id="fliu"><input type='hidden' name='action' value='add_package_channel2'>
渠道：<input type='text' name='channel' value=''> 
url：<input type='text' name='url' value='' size=50> 
版本：<input type='text' name='cVersion' value='' size=30> 
apkName：<input type='text' name='apkName' value='' size=20> 
gpu(auto,etc_general)：<input type='text' name='gpuType' value='' size=20> 
	<input type='submit' value='提交'>
</form>



<b>渠道对应的共享节点</b>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>渠道</td>
	<td>共享节点</td>
	<td>是否有效</td>
	<td>操作</td>
</tr>
<%
HashMap<String,ResourceSharedNode> map = nodeManager.getChannle2NodeMap();
Iterator<String> it = map.keySet().iterator();
while(it.hasNext()){
	String channel = it.next();
	ResourceSharedNode node = map.get(channel);
	out.println("<tr bgcolor='#FFFFFF' align='center'>");
	out.println("<td>"+channel+"</td>");
	out.println("<td>"+node.getName() +"</td>");
	out.println("<td>"+node.isEnable()+"</td>");
	out.println("<td><a href='./shared_node.jsp?action=del_channel&channel="+channel+"'>删除</a></td>");
	out.println("</tr>");
}
%>	
</table>
<form id="f"><input type='hidden' name='action' value='add_channel'>
增加新的渠道与节点对应：<br>
渠道：<input type='text' name='channel' value=''> 
节点：<select name='node'><%
	ArrayList<ResourceSharedNode> nodeList = nodeManager.getNodeList();
	for(ResourceSharedNode n : nodeList){
		out.println("<option value='"+n.getName()+"'>"+n.getName()+"</option>");
	}
%></select> <input type='submit' value='提交'>
</form>
<br>
<br>
<br>
<b>渠道对应的共享节点</b>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>共享节点</td>
	<td>是否有效</td>
	
	<td>所有共享的资源包地址</td>
	<td>地址是否可下载</td>
	<td>最近检测时间</td>
	<td>解析出的信息</td>
	<td>文件大小(本地必须存在)</td>
	
	<td>删除</td>
	
	<td>生效开关</td>
	<td>添加</td>
</tr>
<%
for(ResourceSharedNode n : nodeList){
	if(n.getPackageURLs().size() == 0){
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+n.getName()+"</td>");
		out.println("<td>"+n.isEnable()+"</td>");
		
		out.println("<td></td>");
		out.println("<td></td>");
		out.println("<td></td>");
		out.println("<td></td>");
		out.println("<td></td>");
		
		out.println("<td></td>");
		
		out.println("<td></td>");
		
		out.println("<td>");
		out.println("<form>");
		out.println("<input type='hidden' name='action' value='add_url'>");
		out.println("<input type='hidden' name='node' value='"+n.getName()+"'>");
		out.println("<input type='text' name='url' value='' size=50>");
		out.println("<input type='submit' value='添加'>");
		out.println("</form>");
		out.println("</td>");
		
		out.println("</tr>");
	}else{
		String urls[] = n.getPackageURLs().toArray(new String[0]);
		for(int i = 0 ; i < urls.length ; i++){
			
			ArrayList<ResourceSharedNode.ResourcePackage> al = n.getRps();
			ResourceSharedNode.ResourcePackage pr = null;
			for(ResourceSharedNode.ResourcePackage pr2 : al){
				if(pr2.url.equals(urls[i])){
					pr = pr2;
				}
			}
			
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			if(i == 0){
				out.println("<td rowspan='"+urls.length+"'>"+n.getName()+"</td>");
				out.println("<td rowspan='"+urls.length+"'>"+n.isEnable()+"</td>");
			}
			out.println("<td>"+urls[i]+"</td>");
			
			if(pr == null){
				out.println("<td>--</td>");
				out.println("<td>--</td>");
				out.println("<td>--</td>");
				out.println("<td>--</td>");
			}else{			
				out.println("<td>"+pr.checkEnable+"</td>");
				out.println("<td>"+(System.currentTimeMillis()/1000 - pr.lastCheckTime/1000)+"秒前</td>");
				out.println("<td>"+pr.gpu+"|"+pr.platform+"</td>");
				out.println("<td>"+pr.fileSize+"</td>");
			}
			
			out.println("<td><a href='./shared_node.jsp?action=del_url&node="+n.getName()+"&urlindex="+i+"'>删除</a></td>");
			
			if(i == 0){
				
				out.println("<td rowspan='"+urls.length+"'><a href='./shored_node.jsp?action=enable_node&node="+n.getName()+"'>"+(n.isEnable()?"失效":"启用")+"</a></td>");
				
				out.println("<td rowspan='"+urls.length+"'>");
				out.println("<form>");
				out.println("<input type='hidden' name='action' value='add_url'>");
				out.println("<input type='hidden' name='node' value='"+n.getName()+"'>");
				out.println("<input type='text' name='url' value='' size=50>");
				out.println("<input type='submit' value='添加'>");
				out.println("</form>");
				out.println("</td>");
			}
			
			out.println("</tr>");
		}
	}
	
}
%>

</table>
<br>
<form id="f"><input type='hidden' name='action' value='add_node'>
增加节点信息：<br>
节点：<input type='text' name='node' value=''> 
<input type='submit' value='提交'>
</form>
<br>
<form id="f"><input type='hidden' name='action' value='remove_node'>
删除节点信息：<br>
节点：<input type='text' name='node' value=''> 
<input type='submit' value='提交'>
</form>
<br>
<form id="f"><input type='hidden' name='action' value='ref_node'>
重新载入节点信息：<br>
<input type='submit' value='提交'>
</form>
</center>
</body>
</html> 
