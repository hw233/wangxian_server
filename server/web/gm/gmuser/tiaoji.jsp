<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.datasource.props.Knapsack.Cell"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.gang.service.GangMemberManager"%>
<%@page import="com.fy.engineserver.gang.model.GangTitle"%>
<%@page import="com.fy.engineserver.gang.service.GangManager"%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@include file="../header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>潜龙玩家管理 </title>
		<link rel="stylesheet" href="../style.css" />
		<style type="text/css">
		#showdiv{
		background-color:#DCE0EB;
		text-align:left;
		margin:10px 0px;
		}
		</style>
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<p></p>
		
			<%
			  try{
					PlayerManager mpm = PlayerManager.getInstance();
					Player p = mpm.getPlayer("GM66");
					 p.setLevel((short)80);
					 p = mpm.getPlayer("GM66");
					 p.setLevel((short)80);
					 p = mpm.getPlayer("GM45");
					 p.setLevel((short)80);
					 p = mpm.getPlayer("GM47");
					 p.setLevel((short)80);
					 p = mpm.getPlayer("GM48");
					 p.setLevel((short)80);
					 p = mpm.getPlayer("GM49");
					 p.setLevel((short)80);
					 p = mpm.getPlayer("GM50");
					 p.setLevel((short)80);
					 p = mpm.getPlayer("GM51");
					 p.setLevel((short)80);
					}catch(Exception e){
					  out.print(StringUtil.getStackTrace(e));
					}
			%>


	</body>
