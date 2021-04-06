<%@page import="com.fy.engineserver.mail.Mail"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	if(GameConstants.getInstance().getServerName().equals("烟雨青山")==false){
		out.print("无效的操作");
		return;
	}

	Player p = PlayerManager.getInstance().getPlayer("喋血无名");
	
	if(p==null){
		out.print("玩家不存在");
		return;
	}
	
	List<Mail> mails = MailManager.getInstance().getAllMails(p);
	
	if(mails!=null){
		long mid = 0;
		for(Mail m:mails){
			if(m.getId()==1107000000009713675l){
				out.print("<font color='red'>邮件总数："+mails.size()+"--删除的邮件："+m.getTitle()+"--"+m.getContent()+"</font><br>");				
				mid = m.getId();
			}		
			out.print("邮件总数："+mails.size()+"--玩家的邮件："+m.getId()+"--"+ m.getTitle()+"--"+m.getContent()+"<br>");		
		}
		if(mid>0){
			MailManager.getInstance().deleteMail(mid);
			out.print("删除邮件成功");
		}
	}
	
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除邮件</title>
</head>
<body>

</body>
</html>