
<%@page import="com.fy.gamegateway.mieshi.resource.manager.PackageManager"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.PackageManager.Version"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.LoginEntity"%>
<%@ page contentType="text/html;charset=utf-8" 
import="java.util.*,java.io.*"%>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@page import="com.fy.gamegateway.mieshi.waigua.NewLoginHandler"%>
	<%
	String clientVersion = "2.2.99";
	PackageManager.Version nowVersion = new Version(clientVersion);
	Version needVersion = new Version("3.0.8");
	
	if(nowVersion.compareTo(needVersion) == -1){
		String content = "<f>《飘渺寻仙曲》已更新至V3.0.8版，请您在（链接地址）下载最新客户端进行游戏</f>\n<f onclick='true' url='http://116.213.192.220/NEW91_ETC.jsp' size='30' color='0xffff00'>请点击这里更新</f>";
		out.print(content);
	}else{
		out.print("不逊要更新");
	}
	%>
