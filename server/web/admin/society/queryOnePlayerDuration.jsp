<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>

<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.gm.feedback.Feedback"%>
<%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.text.*"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<title>查询一个人的在线时间</title>
</head>
<body>

	<%
	
		String name = request.getParameter("name");
		if(name == null || name.equals("")){
			%>
			<form action="">
				name:<input type="text" name="name" />
				<br/>
				<input type="submit" value="submit">
			</form>
			<%
		}else{
			Player player = PlayerManager.getInstance().getPlayer(name.trim());
			if(player != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd : HH:mm:ss");
				long time = player.getDurationOnline();
				int minute = (int)time/(60*1000);
				
				out.print("在线时间:"+(minute/60)+"小时"+(minute%60)+"分钟<br/>");
			}
		}
	
	
	%>

</body>

</html>
