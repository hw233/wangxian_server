<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><html>
<head>
<title>发送邮件通过物品id</title>
</head>
<body>
	
	
	<%
	String ip = request.getRemoteAddr();
	String recorder = "";
	Object o = session.getAttribute("authorize.username");
	if(o!=null){
		recorder = o.toString();
	}
		String playerIds = request.getParameter("playerId");
		String playerName = request.getParameter("playerName");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String id1s = request.getParameter("id1");
		String id2s = request.getParameter("id2");
		String id3s = request.getParameter("id3");
		String id4s = request.getParameter("id4");
		String id5s = request.getParameter("id5");
	
		if((playerIds != null&& !"".equals(playerIds)) || (playerName != null&& !"".equals(playerName))){
			
			Player sendPlayer = null;
			Player p1 = null;
			if(playerIds != null && !"".equals(playerIds)){
				p1 = PlayerManager.getInstance().getPlayer(Long.parseLong(playerIds));
			}
			
			Player p2 = null;
			if(playerName != null && !"".equals(playerName)){
				p2 = PlayerManager.getInstance().getPlayer(playerName);
			}
			
			if(p1 != null && p2 != null){
				if(p1 != p2){
					out.print("输入的id跟输入的name不是一个人");
					return;
				}
			}
			
			if(p1 != null){
				sendPlayer = p1;
			}else{
				sendPlayer = p2;
			}
			
			if(title == null || title.equals("") || content == null || content.equals("")){
				out.print("输入的标题或内容为空"+"<br/>");
				return;
			}
			
			List<Long> list = new ArrayList<Long>();
			if(id1s != null && !id1s.equals("")){
				list.add(Long.parseLong(id1s));
			}
			if(id2s != null && !id2s.equals("")){
				list.add(Long.parseLong(id2s));
			}
			if(id3s != null && !id3s.equals("")){
				list.add(Long.parseLong(id3s));
			}
			if(id4s != null && !id4s.equals("")){
				list.add(Long.parseLong(id4s));
			}
			if(id5s != null && !id5s.equals("")){
				list.add(Long.parseLong(id5s));
			}
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleEntity ae = null;
			if(list.size() > 0){
				ArticleEntity[] aes = new ArticleEntity[list.size()];
				int[] nums = new int[list.size()];
				StringBuffer sb = new StringBuffer();
				for(int i= 0;i<list.size();i++){
					sb.append("id:"+list.get(i)+" ");
					ae = aem.getEntity(list.get(i));
					if(ae != null){
						aes[i] = ae;
						nums[i] = 1;
					}else{
						out.print("你输入的id物品不存在"+list.get(i));
						return;
					}
				}
				
				
				MailManager.getInstance().sendMail(sendPlayer.getId(), aes, nums, title, content, 0, 0, 0, "后台发送",MailSendType.后台发送,sendPlayer.getName(),ip,recorder);
				MailManager.logger.error("[后台给玩家发东西] ["+sendPlayer.getLogString()+"] ["+sb.toString()+"]");
				out.print("发送完成<br/>");
				out.print("<a href=\"javascript:window.history.back()\">返回</a>");
			}else{
				out.print("没有输入物品Id");
			}
			return;
		}
	
	%>
	
	

	<form action="">
	
		玩家id:<input type="text" name="playerId" /><br/>
		玩家name:<input type="text" name="playerName" /><br/>
		标题:<input type="text" name="title" value="系统邮件"/><br/>
		内容:<input type="text" name="content" value="附件为系统补发的物品，请注意查收，祝您游戏愉快。"/><br/>
		物品id1:<input type="text" name="id1" /><br/>
		物品id2:<input type="text" name="id2" /><br/>
		物品id3:<input type="text" name="id3" /><br/>
		物品id4:<input type="text" name="id4" /><br/>
		物品id5:<input type="text" name="id5" /><br/>
		<input type="submit" value="submit" />
	</form>


</body>
</html>
