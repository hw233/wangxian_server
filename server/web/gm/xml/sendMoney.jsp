<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.xuanzhi.boss.game.model.GamePlayer"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>潜龙GM邮箱 发送游戏币</title>
		<link rel="stylesheet" href="../style.css" />
		
		<%
		String playerId=request.getParameter("playerId");
		String title=request.getParameter("title");
		if(title==null|| title.length()==0){
			title="补偿游戏币。";
		}
		String content=request.getParameter("content");
		if(content==null|| content.length()==0){
			content="补偿游戏币。";
		}
		String money=request.getParameter("money");
		 %>
	</head>
	<body>
		<form id='f'>
			玩家ID：<input type='text' id='playerId' name='playerId' value=''>
			<br>
			TITLE：<input type='text' id='title' name='title' value='补偿游戏币'>
			<br>
			金额：<input type='text' id='money' name='money' value=''>
			<br>
			<textarea rows="30" cols="50" id='content' name='content'></textarea>
			<br>
			<input type="submit" value="提交">
		</form>
		<%
			try {
			    if(playerId!=null&&money!=null){
			    	Player p=PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			    	long num=Long.parseLong(money);
			    	
			    	if(num>0){
			    		MailManager.getInstance().sendMail(p.getId(),new ArticleEntity[0],title,content,num,0,0);
			    		
			    		out.println(p.getName()+"的邮件发送成功，附加游戏币"+num+"。");
			    	}
			    }
			} catch (Exception e) {
				out.println(StringUtil.getStackTrace(e));
				//e.printStackTrace();
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("visitfobiden.jsp");
				//rdp.forward(request, response);

			}
		%>
	</body>
