<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%
MieshiServerInfoManager mieshiServerInfoManager = MieshiServerInfoManager.getInstance();
MieshiServerInfo[] newMieshiServerInfos = mieshiServerInfoManager.getNewServerInfoList();

for(MieshiServerInfo msi : newMieshiServerInfos)
{
	out.println(msi.getRealname());
}
%>