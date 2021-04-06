<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.message.HINT_REQ"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>系统邮件</title>
<%
String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
MailManager mm = MailManager.getInstance();
PlayerManager pm = PlayerManager.getInstance();
String title = "遗失物品找回";
String content = "您好，丢失物品已为您找回。在此向您诚挚致歉！今后我们将更为努力，严格保障您的财产安全，感谢您的支持！";
String name = request.getParameter("name");
int sendMailCount = 0;
int notSendMailCount = 0;
if(true){
	return;
}
if(mm != null && pm != null && title != null && content != null && name != null && !title.trim().equals("") && !content.trim().equals("") && !name.trim().equals("")){
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		
		/*long[] ids = pm.getPlayerIdsByLastingRequestTime(0);
		ArrayList<Player> players = new ArrayList<Player>();
		for(long id : ids){
			players.add(pm.getPlayer(id));
		}
		Player[] ps = players.toArray(new Player[0]);
		*/
		Player p = pm.getPlayer(name);
			if(p != null){
				if(am != null){
					ArrayList<ArticleEntity> aeList = new ArrayList<ArticleEntity>();
					ArrayList<Integer> countList = new ArrayList<Integer>();
					{
						String count1 = request.getParameter("count1");
						String article1 = request.getParameter("article1");
						String colorStr = request.getParameter("color1");
						int color = 0;
						if(colorStr != null){
							try{
								color = Integer.parseInt(colorStr);
							}catch(Exception e){
								
							}
						}
						if(article1 != null){
							Article a1 = am.getArticle(article1);
							if(a1 != null){
								int count = Integer.parseInt(count1);
								if(count > 0){
									ArticleEntity ae1 = aem.createEntity(a1, false, ArticleEntityManager.CREATE_REASON_huodong_libao, p, color,count,true);
									if(ae1 != null){
										aeList.add(ae1);
										countList.add(count);
									}
								}
							}
						}
					}

					{
						String article2 = request.getParameter("article2");
						Article a2 = am.getArticle(article2);
						String count2 = request.getParameter("count2");
						String colorStr = request.getParameter("color2");
						int color = 0;
						if(colorStr != null){
							try{
								color = Integer.parseInt(colorStr);
							}catch(Exception e){
								
							}
						}
						if(article2 != null){
							if(a2 != null){
								int count = Integer.parseInt(count2);
								if(count > 0){
									ArticleEntity ae1 = aem.createEntity(a2, false, ArticleEntityManager.CREATE_REASON_huodong_libao, p, color,count,true);
									if(ae1 != null){
										aeList.add(ae1);
										countList.add(count);
									}
								}
							}
						}
					}
					
					{
						String article3 = request.getParameter("article3");
						Article a3 = am.getArticle(article3);
						String count3 = request.getParameter("count3");
						String colorStr = request.getParameter("color3");
						int color = 0;
						if(colorStr != null){
							try{
								color = Integer.parseInt(colorStr);
							}catch(Exception e){
								
							}
						}
						if(article3 != null){
							if(a3 != null){
								int count = Integer.parseInt(count3);
								if(count > 0){
									ArticleEntity ae1 = aem.createEntity(a3, false, ArticleEntityManager.CREATE_REASON_huodong_libao, p, color,count,true);
									if(ae1 != null){
										aeList.add(ae1);
										countList.add(count);
									}
								}
							}
						}
					}
					
					{
						String article4 = request.getParameter("article4");
						Article a4 = am.getArticle(article4);
						String count4 = request.getParameter("count4");
						String colorStr = request.getParameter("color4");
						int color = 0;
						if(colorStr != null){
							try{
								color = Integer.parseInt(colorStr);
							}catch(Exception e){
								
							}
						}
						if(article4 != null){
							if(a4 != null){
								int count = Integer.parseInt(count4);
								if(count > 0){
									ArticleEntity ae1 = aem.createEntity(a4, false, ArticleEntityManager.CREATE_REASON_huodong_libao, p, color,count,true);
									if(ae1 != null){
										aeList.add(ae1);
										countList.add(count);
									}
								}
							}
						}
					}
					
					{
						String article5 = request.getParameter("article5");
						Article a5 = am.getArticle(article5);
						String count5 = request.getParameter("count5");
						String colorStr = request.getParameter("color5");
						int color = 0;
						if(colorStr != null){
							try{
								color = Integer.parseInt(colorStr);
							}catch(Exception e){
								
							}
						}
						if(article5 != null){
							if(a5 != null){
								int count = Integer.parseInt(count5);
								if(count > 0){
									ArticleEntity ae1 = aem.createEntity(a5, false, ArticleEntityManager.CREATE_REASON_huodong_libao, p, color,count,true);
									if(ae1 != null){
										aeList.add(ae1);
										countList.add(count);
									}
								}
							}
						}
					}

					if(aeList != null && !aeList.isEmpty() && countList != null && !countList.isEmpty() && aeList.size() == countList.size()){
						try {
							int[] counts = new int[countList.size()];
							for(int j = 0; j < counts.length; j++){
								counts[j] = countList.get(j);
							}
							mm.sendMail(p.getId(), aeList.toArray(new ArticleEntity[0]), counts, title.trim(), content.trim(), 0, 0, 0, "后台系统邮件",MailSendType.后台发送,p.getName(),ip,recorder);
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, "您收到一封系统邮件，请到邮件npc处领取。");
							p.addMessageToRightBag(hreq);
							ArticleManager.logger.warn("[gm发送系统邮件] [成功] [{}]", new Object[]{p.getLogString()});
							out.println("[gm发送系统邮件] [成功] [发送给:"+p.getLogString()+"]<br/>");
							sendMailCount++;
						} catch (Exception ex) {
							ArticleManager.logger.warn("[gm发送系统邮件] [失败] [{}]", new Object[]{p.getLogString()},ex);
							out.println("[gm发送系统邮件] [失败] [想发送给:"+p.getLogString()+"]");
							notSendMailCount++;
						}
					}else{
						mm.sendMail(p.getId(), new ArticleEntity[0], new int[0], title.trim(), content.trim(), 0, 0, 0, "后台系统邮件",MailSendType.后台发送,p.getName(),ip,recorder);
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, "您收到一封系统邮件，请到邮件npc处领取。");
						p.addMessageToRightBag(hreq);
						ArticleManager.logger.warn("[gm发送系统邮件,邮件不包括任何物品] [成功] [{}]", new Object[]{p.getLogString()});
						out.println("[gm发送系统邮件,邮件不包括任何物品] [成功] [发送给:"+p.getLogString()+"]<br/>");
						sendMailCount++;
					}
				}
			}
}else{
	ArticleManager.logger.warn("[gm发送系统邮件] [失败，title或content为空]");
	out.println("[gm发送系统邮件] [失败，title或content为空]");
}
out.println("成功发送邮件"+sendMailCount+" 没有成功发送邮件"+notSendMailCount);
%>
<script language="JavaScript">
<!--
-->
</script>
</head>
<body>
<form name="f1">
收件人<input name="name"><br/>
邮件title<input name="title"><br/>
邮件内容<input name="content"><br/>
发送物品1<input name="article1">颜色:<input name="color1">数量:<input name="count1"><br/>
发送物品2<input name="article2">颜色:<input name="color2">数量:<input name="count2"><br/>
发送物品3<input name="article3">颜色:<input name="color3">数量:<input name="count3"><br/>
发送物品4<input name="article4">颜色:<input name="color4">数量:<input name="count4"><br/>
发送物品5<input name="article5">颜色:<input name="color5">数量:<input name="count5"><br/>
<input type="submit" value="给人发邮件">
</form>
</body>
</html>
