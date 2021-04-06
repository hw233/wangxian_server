<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@page import="org.apache.log4j.Logger"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>腾讯激活码</title>
		<link rel="stylesheet" href="../gm/style.css" />
	</head>
	<%
	String ip = request.getRemoteAddr();
	String recorder = "";
	Object o = session.getAttribute("authorize.username");
	if(o!=null){
		recorder = o.toString();
	}
		String servername = GameConstants.getInstance().getServerName();
		String pnames = request.getParameter("pnames");
		String jhms = request.getParameter("jhms");
		String numstr = request.getParameter("nums");
		if(pnames!=null && jhms!=null && numstr!=null){
			String ps [] = pnames.split(",");
			String js [] = jhms.split(",");
			int nums = Integer.parseInt(numstr);
			if(nums>0 && ps.length>0 && js.length>0){
				int index = 0;
				Player p = null;
				MailManager mm = MailManager.getInstance();
				ArticleEntity aes [] = new ArticleEntity[0];
				int counts[] = new int[0];
				String errorname = "";
				for(int i=0,len=ps.length;i<len;i++){
					try{
						p = PlayerManager.getInstance().getPlayer(ps[i]);
						if(p!=null){
							for(int j=0;j<nums;j++){
								if(index >= js.length){
									out.print("[后台发送激活码] [失败] [<font color='red'>激活码的数量不足</font>] ["+ps[i]+"] </br>");
									ActivitySubSystem.logger.warn("[后台发送激活码] [失败] [激活码的数量不足] ["+ps[i]+"] ["+index+"]");
									errorname = ps[i];
									continue;
								}
								String jhm = js[index];
								mm.sendMail(p.getId(), aes, counts, "激活码奖励", "恭喜您获得激活码："+jhm, 0, 0, 0, "后台发送激活码",MailSendType.后台发送,p.getName(),ip,recorder);
								index++;
								out.print("[后台发送激活码] [成功] ["+ps[i]+"] ["+jhm+"]</br>");
								ActivitySubSystem.logger.warn("[后台发送激活码] [成功] [nums:"+nums+"] ["+ps[i]+"] ["+jhm+"] ["+index+"]");
							}
						}
					}catch(Exception e){
						out.print("[后台发送激活码] [异常] ["+ps[i]+"] ["+e.toString()+"]</br>");
						ActivitySubSystem.logger.warn("[后台发送激活码] [异常] ["+ps[i]+"] ["+e.toString()+"]");
					}
				}
				out.print("<font color='red'>激活码发放完毕，最后发放的激活码为："+js[index-1]+",如果该批激活码还要使用，请过滤掉已经发送的.</br>如果激活码不足，请制作新的激活码，从玩家："+errorname+" 开始继续发;</font>");
			}else{
				out.print("内容不符");
			}
		}
	%>
	<h2>当前服务器：<font color='red'><%=servername %></font></h2>
	<body>
	<form>
		<table>
			<tr><th>每个玩家获取激活码的数量：</th><td><input type='text' name='nums'></td></tr>
			<tr><th>玩家集：</th><td><textarea name='pnames'></textarea></td></tr>
			<tr><th>激活码：</th><td><textarea name='jhms'></textarea></td></tr>
			<tr><td><input type='submit' value='发送'></td></tr>
		</table>
	</form>	
	</body>
</html>
