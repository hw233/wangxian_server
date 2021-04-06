<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.core.TransportData"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%-- <%@include file="../header.jsp"%> --%>
<%-- <%@include file="../authority.jsp" %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>GM跟踪玩家</title>
		<link rel="stylesheet" href="../style.css" />
<script type="text/javascript">
	function followTarget(){
		var tarname = document.getElementById('targetName').value;
		var gmname = document.getElementById('gmName').value;
		if(tarname&&gmname){
			window.location.replace("gmFollowPlayer.jsp?tarname="+tarname+"&gmname="+gmname);
		}
	}
</script>
	</head>
	<body>
		<%
			String tarname = request.getParameter("tarname");
			String gmname = request.getParameter("gmname");	
			if(tarname!=null&&tarname.trim().length()>0&&gmname!=null&&gmname.trim().length()>0){
				PlayerManager pmanager = PlayerManager.getInstance();
				try{
					Player tarPlayer = pmanager.getPlayer(tarname);
					Player gmPlayer = pmanager.getPlayer(gmname);
					String istransfer = "yes";
					if(!tarPlayer.isOnline()){istransfer = "目标玩家不在线！！";}
					if(!gmPlayer.isOnline()){istransfer = "被传送的玩家不在线！！";}
					if(istransfer=="yes"){
						gmPlayer.setTransferGameCountry(tarPlayer.getTransferGameCountry());
						gmPlayer.getCurrentGame().transferGame(gmPlayer, new TransportData(0,0,0,0,
								tarPlayer.getGame(), tarPlayer.getX(), tarPlayer.getY()));
						out.print("<font color='red'>移动成功，您成功的把 "+gmname+" 移到了 "+tarname+" 身边！当前地图："+tarPlayer.getGame());
					}else{
						out.print(istransfer);
					}
					
				}catch(Exception e){
					out.print("异常："+StringUtil.getStackTrace(e));
				}
				
			}else{
				out.print("目标玩家或者需要移动的玩家名字不能为空！！");
			}
		
		%>
		<table>
		<h1>会把(移动玩家)移到(目标玩家)身边</h1>
		<tr><th>目标玩家：</th><td><input type='text' id='targetName' name='targetName' value='<%=(tarname!=null?tarname:"") %>'/></td></tr>
		<tr><th>移动玩家：</th><td><input type='text' id='gmName' name='gmName' value='<%=(gmname!=null?gmname:"") %>'/></td></tr>	
		<input type="button" onclick="followTarget()" value="确定"/>
		</table>
	</body>
</html>