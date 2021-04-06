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

</style>
<script>
</script>		
</head>
<body>
<div style="text-align:center;font-size:28px;"><b>飘渺寻仙曲安装包下载</b></div><br/>
<%
	
	PackageManager pm = PackageManager.getInstance();
	PackageManager.Version v = pm.getRealVersions()[pm.getRealVersions().length-1];
	ArrayList<PackageManager.Package> al = v.packageList;
	out.println("<div style='text-align:left;font-size:25px;'>最新的版本号：<b>"+v.versionString+"</b></div>");
	out.println("<div style='text-align:left;font-size:25px;'>如果您已经安装过飘渺寻仙曲(灭世)，无需卸载，会自动更新。如果您是第一次安装，安装后会自动下载资源，有70M左右，建议在WIFI环境下下载。</div>");
	out.println("<br/>");
	for(int j = 0 ; j < al.size() ; j++){
		PackageManager.Package pp = al.get(j);
		String link = pp.httpDownloadURL;
		if(pp.platform.equalsIgnoreCase("IOS") && pp.channel.equalsIgnoreCase("SQAGE_MIESHI")){
			link = "itms-services://?action=download-manifest&url="+pp.httpDownloadURL;
			//.ipa
			int kk = link.lastIndexOf(".");
			link = link.substring(0,kk) + ".plist";
			if(pp.fullFlag.equalsIgnoreCase("FULL")){
				out.println("<div style='text-align:left;font-size:25px;'><b>iPhone/iPad/iPod</b>，<a href='"+link+"'>请点击这里安装全资源包</a></div><br/>");
			}else{
				out.println("<div style='text-align:left;font-size:25px;'><b>iPhone/iPad/iPod</b>，<a href='"+link+"'>请点击这里更新</a></div><br/>");
			}
		}else if(pp.platform.equalsIgnoreCase("Android") && pp.channel.equalsIgnoreCase("SQAGE_MIESHI") &&
				pp.gpuFlag.equalsIgnoreCase("auto") && pp.fullFlag.equalsIgnoreCase("half")){
			out.println("<div style='text-align:left;font-size:25px;'><b>Android手机/平板</b>，<a href='"+link+"'>请点击这里</a></div><br/>");
		}
	}
	out.println("<br/>");
	
	out.println("<div style='text-align:center;font-size:20px;'>如果您下载遇到问题，请联系我们。官方QQ群:164885580/213757249，邮件：msgame@sqage.com</div>");
%>
<br/>
<br/>
</body>
</html>
		
