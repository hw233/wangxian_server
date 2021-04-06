<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.fy.gamegateway.mieshi.resource.manager.*,com.fy.gamegateway.mieshi.server.*"%><!DOCTYPE html>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />    
		<meta http-equiv="Cache-Control" content="no-cache"/>    		 	                          
		<meta name="viewport" content="width=440,user-scalable=yes,target-densitydpi=high-dpi">
<style type="text/css">
body {
	border:none 0px;
	margin:0px;
	padding:0px;
	font-color : #fff;
	background-color : rgb(221,223,225);
	font-size : 14px;
}


.link{
	color:#ffffff;
	text-align: center;
	margin-bottom:10px;
}
a {
	font-size : 34px;
	font-color : #fff;
	color : #ff00ff;
	outline: none;
}

.text{
	font-color : #fff;
	color : #fff;
	font-size : 18px;
	padding:10px;
}

.img{
  text-align:center;
}

.text-line{
	height:23px;
	line-height:23px;
}

.start1{
	font-color : #fff;
	color : #fff;
	font-size : 18px;
	text-align:center;
	line-height:40px;
	height:59px;
	width:118px;
	background-image:url(sinaImage/start2.png);
	background-repeat: no-repeat;
	margin-left:-2px;
}


</style>
		
</head>
<body>
<%
	PackageManager pm = PackageManager.getInstance();

	//public Package getNewPackage(String clientId,String clientVersion,String clientChannel,String clientPlatform,String gpuFlag){
	
	String clientId = request.getParameter("ci");
	if(clientId == null) clientId = "";
	
	String clientVersion = request.getParameter("cv");
	if(clientVersion == null) clientVersion = "1.0.0";
	
	String clientChannel = request.getParameter("cc");
	if(clientChannel == null) clientChannel = "SQAGE_MIESHI";
	
	String clientPlatform = request.getParameter("cp");
	if(clientPlatform == null) clientPlatform = "IOS";
	
	String gpuFlag = request.getParameter("gpu");
	if(gpuFlag == null) gpuFlag = "pvr_res";
	
	
	PackageManager.Package p = pm.getNewPackage(clientId,clientVersion,clientChannel,clientPlatform,gpuFlag);
	
%>
<div style="text-align:center;font-size:28px;"><b>飘渺寻仙曲(灭世)版本更新</b></div>
<div style="font-size:28px;">您当前的平台是：<%= clientPlatform %></div>
<div style="font-size:28px;">您当前的GPU型号是：<%= gpuFlag %></div>
<div style="font-size:28px;">您的渠道标识是：<%= clientChannel %></div>
<div style="font-size:28px;">您当前的版本号是：<%= clientVersion %></div>

<%

if(p != null){
		out.println("<div style='font-size:28px;'>有最新的版本："+p.version.versionString+"</div>");
		String link = p.httpDownloadURL;
		if(p.platform.equalsIgnoreCase("IOS")){
			link = "itms-services://?action=download-manifest&url="+p.httpDownloadURL;
			//.ipa
			int kk = link.lastIndexOf(".");
			link = link.substring(0,kk) + ".plist";
		}
		
		out.println("<br/><div style='font-size:28px;'>亲爱的玩家：《飘渺寻仙曲(灭世)》内测客户端已经发布最新版本，版本号为"+p.version.versionString+" 请玩家主动更新安装最新版本客户端，保证顺利进行游戏。</div>");
        out.println("<br/><div style='font-size:28px;'>新客户端安装流程：</div>");
        out.println("<br/><div style='font-size:28px;'>1 请保存好《飘渺寻仙曲(灭世)》登陆用户名和登陆密码。</div>");
        out.println("<br/><div style='font-size:28px;'>2 请<a href='"+link+"'>点击这里更新</a>最新客户端，并完成安装。</div>");
        out.println("<br/><div style='font-size:28px;'>3 更新完后如果出现打不开游戏的情况，请重启您的手机。</div>");
        out.println("<br/><div style='font-size:28px;'>对于本次更新给您造成的不便，敬请谅解，祝游戏愉快。</div>");
        
	}else{
		out.println("<br/>div style='font-size:28px;'>您的版本已经是最新的版本!</div>");
	}
if(request.getParameter("cp") == null){
	out.println("<br/><div style='font-size:28px;'>如果您是Android手机，请<a href='./download.jsp?ci="+clientId+"&cv="+clientVersion+"&cc="+clientChannel+"&cp=Android&gpu=auto'>点击这里</a></div>");
}
%>
<br/>
<br/>
<%
InnerTesterManager itm = InnerTesterManager.getInstance();
boolean isTester = false;
if(itm != null){
	isTester = itm.isInnerTester(clientId);
}
if(isTester){
%>
<div style="text-align:center;font-size:32px;">下载其他版本(内部测试)</div>
<%
	PackageManager.Version[] versions = null;
	if(isTester){
		versions = pm.getTestVersions();
	}else{
		versions = pm.getRealVersions();
	}
	
	for(int i = versions.length -1 ; i >= 0 ; i--){
		PackageManager.Version v = versions[i];
		ArrayList<PackageManager.Package> al = v.packageList;
		for(int j = 0 ; j < al.size() ; j++){
			PackageManager.Package pp = al.get(j);
			String ss = pp.version.versionString+"/"+pp.channel+"/"+pp.platform+"/"+pp.gpuFlag+"/"+pp.fullFlag;
			String link = pp.httpDownloadURL;
			if(pp.platform.equalsIgnoreCase("IOS")){
				link = "itms-services://?action=download-manifest&url="+pp.httpDownloadURL;
				//.ipa
				int kk = link.lastIndexOf(".");
				link = link.substring(0,kk) + ".plist";
			}
			out.println("<div><a href='"+link+"'>"+ss+"</a></div>");
		}
	}
}
%>
</body>
</html>
		
