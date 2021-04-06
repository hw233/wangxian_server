<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.gm.feedback.Feedback"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%>
<%@page import="com.xuanzhi.stat.model.PlayerMakeDealFlow"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.gm.feedback.FeedBackState"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.gm.feedback.Reply"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试插入反馈数据</title>

</head>
<body>

		<%
		
		String[] source = {"<prototype","req_name=","GET_PLAYER_BY_ID_REQ" ,"req_type=","res_name=","GET_PLAYER_BY_ID_RES" ,"res_type=","req_client_send=",
				"res_client_receive=","comment=","用户查找玩家","用于跳转到玩家所在地"};
		Random random = new Random();
		
		Player player = PlayerManager.getInstance().getPlayer("ttt");
		FeedbackManager fbm = FeedbackManager.getInstance();

		int num = 0;
		String subject = "";
		String content = "";
		int type = 0;
		for(int begin = 0;begin <= 555;begin++){
			for(int i= 0;i<2;i++){
				num = random.nextInt(12);
				subject += source[i];
			}
			for(int i= 0;i<4;i++){
				num = random.nextInt(12);
				content += source[i];
			}
			type = random.nextInt(4);
			fbm.createFeedBack(player,type,subject,content);
		}
		
		out.print("生成完成");
		%>
		
		
				
</body>

</html>