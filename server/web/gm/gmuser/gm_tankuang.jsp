<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.chat.ChatMessage"%>
<%@page import="com.fy.engineserver.message.QUERY_WINDOW_RES"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.menu.Option_Cancel"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@include file="../header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>潜龙GM私聊 </title>
		<link rel="stylesheet" href="../style.css" />
		<script type='text/javascript'
			src='/game_server/dwr/interface/chatborder.js'></script>
		<script type='text/javascript' src='/game_server/dwr/engine.js'></script>
		<script type="text/javascript"
			src="/game_server/dwr/interface/DWRManager.js"></script>
		<script type="text/javascript" src="/game_server/dwr/util.js"></script>
		<script type="text/javascript">
 
	</head>
	<body>
		<%
			try {
			String gmname = session.getAttribute("username").toString();
				String username = session.getAttribute("username").toString();
				ActionManager amanager = ActionManager.getInstance();
				ChatMessageService cms = ChatMessageService.getInstance();
				PlayerManager pmanager = PlayerManager.getInstance();
				out.print("gmname:" + gmname);
				String action = request.getParameter("action");
				String playname = null;
				Player p = null;
				int playid = -1;
				try {
					if (request.getParameter("playername") != null
							|| request.getParameter("playerid") != null) {
						//改变用户角色时
						if (request.getParameter("playerid") != null) {
							playid = Integer.parseInt(request
									.getParameter("playerid"));
							p = pmanager.getPlayer(playid);
							amanager.save(username,"与"+p.getName()+"私聊 在"+DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd HH:mm:ss"));
							playname = p.getName();
						}
					}
				} catch (Exception e) {
					out.print("请输入正确的角色名或者ID号");
				}
				if(action!=null&&"addkuang".equals(action)){
				  String message = request.getParameter("message");
				  	MenuWindow mw = new MenuWindow();
					mw.setTitle("GM提示");
					mw.setDescriptionInUUB(message);
					mw.setWidth(1000);
					mw.setHeight(1000);
					
					Option_Cancel o = new Option_Cancel();
					o.setText("关闭窗口");
					
					mw.setOptions(new Option[]{o});
					
					QUERY_WINDOW_RES re = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),
							mw,mw.getOptions());
					p.addMessageToRightBag(re);
					amanager.save(gmname,"[发送弹框提示]["+message+"]");
				}  
			} catch (Exception e) {
			  out.print(StringUtil.getStackTrace(e));
				//out.print(e.getMessage());
				//RequestDispatcher rdp = request.getRequestDispatcher("visitfobiden.jsp");
			   // rdp.forward(request,response);

			}
		%>
	</body>
