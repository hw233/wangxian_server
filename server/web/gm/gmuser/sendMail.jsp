<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@include file="../header.jsp"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>邮件发送 </title>
		<link rel="stylesheet" href="../style.css" />
 	<script>
	 	function $(tag) {
			return document.getElementById(tag);
		}
 		function send(){
 			var username = $("username").value;
 			var mailtitle = $("title").value;
 			var mailtext = $("content").value;
			if(username&&mailtitle&&mailtext){
				var str = "?action=send&username="+username+"&mailtitle="+mailtitle+"&mailtext="+mailtext;
				window.location.replace(str);
			}else{
				alert("信息不完整");
			}
 		}
 	</script>
	</head>
	<body>
	<%
	String ip = request.getRemoteAddr();
	String recorder = "";
	Object o = session.getAttribute("authorize.username");
	if(o!=null){
		recorder = o.toString();
	}
		String username = request.getParameter("username");
		String mailtitle = request.getParameter("mailtitle");
		String mailtext = request.getParameter("mailtext");
		System.out.println("mailtitme=" + mailtitle + ", mailtext=" + mailtext);
		MailManager mm = MailManager.getInstance();	
		PlayerManager pm = PlayerManager.getInstance();
		if(username!=null&&username.trim().length()>0&&mailtitle!=null&&mailtitle.trim().length()>0&&mailtext!=null&mailtext.trim().length()>0){
			String names[] = username.split(",");	
			Player [] ps = null;	
			boolean issend = true;
			List<Long> pid = new ArrayList<Long>();
			if(names!=null&&names.length<=10){
				for(String name:names){
					try{
						ps = pm.getPlayerByUser(name);
						if(ps.length>0){
							pid.add(ps[0].getId());
						}else{
							issend = false;
							out.print("<font color = 'red'>发送失败，没有用户名： "+name+"，或该用户没有角色,请检查！---");
						}
					}catch(Exception e){
						out.println("异常"+e);
					}
				}
				if(issend!=false&&pid.size()>0){
					for(long id : pid){
						mm.sendMail(-1,id, new ArticleEntity[]{}, mailtitle, mailtext, 0, 0, 0,"GM后台发送",MailSendType.后台发送,"",ip,recorder);	
					}
					out.print("发送成功!");
				}
			}else{
				issend = false;
				out.print("<font color = 'red'>发送失败，一次发送不能超过10个！！");
			}
			
			
		}
		
		
		
	%>
	
		<table width='60%'>
		<h1>邮件发送</h1>
			<tr><th>用户名</th><td class='top'><input type='text' id='username' name='username' value='<%=username!=null?username:"" %>'/>支持群发：多个用户名之间用英文的, 分割，一次最多发10封</td></tr>
			<tr><th>邮件标题</th><td><input type='text' id='title' name='title' value=''/></td></tr>
			<tr><th>邮件内容</th><td><textarea style='width:50%' rows='5' name ='content' id='content' value=''></textarea></td></tr>
			<tr><td colspan=2 ><input type='button' value='发送' onclick='send()' /></td></tr>
		</table>
		
		
	</body>
</html>