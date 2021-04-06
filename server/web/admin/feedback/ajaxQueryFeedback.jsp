<%@page import="com.xuanzhi.boss.game.GameConstants"%>
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
<%@page import="com.fy.engineserver.gm.feedback.Reply"%>
<%@page import="com.fy.engineserver.country.data.Country"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
				
	<%
	

	
	{}
	
		String idst = request.getParameter("id");
		if(idst != null && !idst.equals("")){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long id = Long.parseLong(idst.trim());
			Feedback fb =  FeedbackManager.getInstance().getFeedBack(id);
			PlayerManager pm = PlayerManager.getInstance();
			if(fb != null){
				
				StringBuffer sb = new StringBuffer();
				long pid = fb.getPlayerId();
				Player  p = pm.getPlayer(pid);
				sb.append("服务器:"+GameConstants.getInstance().getServerName()+"<br/>");
				sb.append("国家:"+CountryManager.得到国家名(p.getCountry())+"<br/>");
				sb.append("账号:"+p.getUsername()+"<br/>");
				sb.append("角色名称:"+p.getName()+"<br/>");
				sb.append("职业:"+CareerManager.careerNameByType(p.getCareer())+"<br/>");
				sb.append("等级:"+p.getLevel()+"<br/>");
				sb.append("||");
				
				List<Reply> list = fb.getList();
				for(Reply r: list){
					if(r.getGmName().equals("")){
						sb.append("<font color = 'blue'>"+p.getName()+"(玩家)&nbsp;&nbsp;"+sdf.format(new Date(r.getSendDate()))+"</font><br/>");
						sb.append("&nbsp;&nbsp;&nbsp;"+r.getFcontent()+"<br/>");
					}else{
						sb.append("<font color = 'red'>"+r.getGmName()+"(GM)&nbsp;&nbsp;"+sdf.format(new Date(r.getSendDate()))+"<br/>");
						sb.append("&nbsp;&nbsp;&nbsp;"+r.getFcontent()+"<br/>");							
					}
				}
				out.print(sb.toString());
			}else{
				out.print("没有指定反馈");
			}
			
			
		}else{
			out.print("输入出错");
		}
	
	
	%>
</body>

</html>