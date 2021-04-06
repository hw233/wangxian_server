<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.sqage.stat.client.StatClientService"%>
<%@page import="com.sqage.stat.model.DaoJuFlow"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.gmstat.RecordEvent"%>
<%@page import="com.fy.engineserver.util.gmstat.EventManager"%>
<%@page import="com.fy.engineserver.util.gmstat.event.MailEvent"%><html>
<%@ include file="./sendEmail.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="cssOfSendArticle.css" />
<script type="text/javascript" src="jquery-1.8.2.js"></script>
<script type="text/javascript" src="jsOfSendArticle.js"></script>
<title>批量发送邮件</title>
</head>
<%!String getOperateUser(HttpServletRequest request) {
		return String.valueOf(request.getSession().getAttribute(
				"authorize.username"));
	}%>
<%

String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
	String op = request.getParameter("op");
	//out.print("op:" + op);
	StringBuffer articleMessage = new StringBuffer("<BR/>");
	StringBuffer mailMessage = new StringBuffer("<BR/>");
	StringBuffer playerMessage = new StringBuffer("<BR/>");
	
	boolean recordMail = false;
	
	articleMessage.append("邮件内容:<BR/>");
	articleMessage.append("<table border=1  style='font-size: 12px;'>");
	articleMessage.append("<tr bgcolor=#61ABE9><td>物品</td><td>颜色</td><td>数量</td><td>是否绑定</td></tr>");
	playerMessage.append("<hr/>");
	playerMessage.append("<hr/>");
	playerMessage.append("接受者信息:<BR/>");
	playerMessage.append("<table  border=1 style='font-size: 12px;'>");
	playerMessage.append("<tr bgcolor=#61ABE9><td>角色ID</td><td>角色名字</td><td>账号</td><td>等级</td><td>vip等级</td><td>邮件ID</td></tr>");
	
	String playerIds= null;
	String playerNames= null;
	String usernames = null;
	if(getOperateUser(request) == null || "".equals(getOperateUser(request))){
		out.print("<h1>你还没有登陆,不能操作<h1>");
	} else {
		out.print("<h2>你好,"+getOperateUser(request)+"</h2>");
		if (op != null && "send".equals(op)) {//要发送
			MailManager mailManager = MailManager.getInstance();
			PlayerManager playerManager = PlayerManager.getInstance();
			playerIds = request.getParameter("playerIds");
			usernames = request.getParameter("username");
			String yinzi = "0";// request.getParameter("yinzi");
			playerNames = request.getParameter("playerNames");
			String mailTitle = request.getParameter("mailTitle");
			String mailContent = request.getParameter("mailContent");
			long yinziNum = 0;
			try{
				yinziNum = Long.parseLong(yinzi);
			}catch(Exception ex){
				
			}
			if(usernames != null && usernames.trim().length() != 0){
				String[] unames = StringTool.string2Array(usernames.trim(), ",", String.class);
				Player ps []= null;
				GameConstants gc = GameConstants.getInstance();
				String servername = gc.getServerName();
				List<Long> ids = new ArrayList<Long>();
				for(String uname : unames){
					try{
						ps = playerManager.getPlayerByUser(uname);
						for(Player p:ps){
							if(p!=null){
								ids.add(p.getId());
							}
						}
					}catch(Exception e){
						out.print("<H1>账号不存在,<font color=red>账号:[" + uname + "]</font></H1>");
					}
				}
				
				boolean allplayerExist = true;
				for (long id : ids) {
					Player player = null;
					try {
						player = playerManager.getPlayer(id);
					} catch (Exception e) {
					}
					if (player == null) {
						allplayerExist = false;
						out.println("<H1>角色不存在,<font color=red>id:[" + id + "]</font></H1>");
						break;
					}
				}
				
				List<Article> articles = new ArrayList<Article>();
				List<Integer> colors = new ArrayList<Integer>();
				List<Integer> nums = new ArrayList<Integer>();
				List<Boolean> binds = new ArrayList<Boolean>();
				StringBuffer wrongArticle = new StringBuffer();
				if (allplayerExist) {//玩家都存在
					boolean allArticleExist = true;
					{
						for (int i = 1; i <= 5; i++) {
							String articleName = request.getParameter("articleName" + i);
							Article article = findArticle(articleName);
							if (articleName != null && !"".equals(articleName)) {
								if (article != null) {
									String numStr = request.getParameter("articleNum" + i);
									String colorStr = request.getParameter("articleColor" + i);
									String bindStr = request.getParameter("bind" + i);
									if (numStr != null && colorStr != null && bindStr != null) {
										//binds
										articles.add(article);
										colors.add(Integer.valueOf(colorStr));
										nums.add(Integer.valueOf(numStr));
										binds.add(!"0".equals(bindStr));//0-不绑定false
									} else {
										out.print("道具信息输入不全!请重新输入!");
										allArticleExist = false;
									}
								} else {
									wrongArticle.append("[" + articleName + "]");
									allArticleExist = false;
								}
							}
						}
					}
					if (!allArticleExist) {
						out.print("<H1>有物品输入错误,请重新输入!<font color=red>" + wrongArticle.toString() + "</font></H1>");
					} else {//开始给每个人发奖励
						int[] num = new int[nums.size()];
						for (int i = 0; i < num.length; i++) {
							num[i] = nums.get(i);
						}
					for (long id : ids) {
							long playerId = id;
							Player player = playerManager.getPlayer(playerId);
							List<ArticleEntity> aes = new ArrayList<ArticleEntity>();
							String[] articlenames = new String[articles.size()]; //数据统计
							int[] counts = new int[articles.size()];
							for (int k = 0; k < articles.size(); k++) {
								Article article = articles.get(k);
								articlenames[k] = article.getName();
								counts[k] = nums.get(k);
								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, binds.get(k), ArticleEntityManager.CREATE_REASON_后台, player, colors.get(k), nums.get(k), true);
								aes.add(ae);
								if (!recordMail) {
									articleMessage.append("<tr><td>"+ae.getArticleName()+"</td><td>"+ArticleManager.getColorString(article, ae.getColorType())+"</td><td>"+ nums.get(k) +"</td><td>"+binds.get(k)+"</td></tr>");
								}
							}
							long mailId = -1;
							mailId = mailManager.sendMail(player.getId(), aes.toArray(new ArticleEntity[0]), num, mailTitle, mailContent, yinziNum, 0L, 0L, "后台发送-" + mailTitle,MailSendType.后台发送,player.getName(),ip,recorder);
							if(!recordMail) {
								mailMessage.append("邮件标题:<font color=red>").append(mailTitle).append("</font><br/>");
								mailMessage.append("邮件内容:<font color=red>").append(mailContent).append("</font><br/>");
								recordMail = true;
							}
							playerMessage.append("<tr><td>"+player.getId()+"</td><td>"+player.getName()+"</td><td>"+player.getUsername()+"</td><td>"+player.getLevel()+"</td><td>"+player.getVipLevel()+"</td><td>"+mailId+"</td></tr>");
							{
								//向统计服发送
								for (int i = 0; i < aes.size();i++) {
									ArticleEntity ae = aes.get(i);
									DaoJuFlow daoJuFlow = new DaoJuFlow();
									daoJuFlow.setJixing(request.getRemoteAddr());
									daoJuFlow.setCreateDate(System.currentTimeMillis());
									daoJuFlow.setDanJia(0L);// 购买道具的单价
									daoJuFlow.setDaoJuName(ae.getArticleName());
									daoJuFlow.setDaoJuNum((long)num[i]);// 购买道具的数量
			
									daoJuFlow.setFenQu(GameConstants.getInstance().getServerName());
			
									daoJuFlow.setGetType("后台生成-sendNewArticle");
									daoJuFlow.setHuoBiType("");
			
									daoJuFlow.setGameLevel(String.valueOf(session.getAttribute("authorize.username")));
									daoJuFlow.setUserName(player.getName() + "/" + player.getId());
			
									daoJuFlow.setBindType(String.valueOf(ae.isBinded()));
									daoJuFlow.setDaoJuColor(String.valueOf(ae.getColorType()));
									daoJuFlow.setDaoJuLevel("0");
			
									daoJuFlow.setPosition("后台发送");// 购买道具的位置，比如商店购买
									if (!TestServerConfigManager.isTestServer()) {
										StatClientService statClientService = StatClientService.getInstance();
										statClientService.sendDaoJuFlow("", daoJuFlow);
										ArticleManager.logger.error("发送统计数据:" + daoJuFlow.toString());
									}
								}
							}
						}
						playerMessage.append("</table>");
						articleMessage.append("</table>");
						sendMail("后台补物品-",mailMessage.toString() + articleMessage.toString() + playerMessage.toString(),request);
						ArticleManager.logger.error("[通过后台发物品] [" +getOperateUser(request) +"] [给玩家发奖励] [成功]" + (mailMessage.toString() + articleMessage.toString() + playerMessage.toString()));
					}

				}
				
			}else if (playerIds != null && playerIds.trim().length() != 0) { 
				String[] idArr = StringTool.string2Array(playerIds.trim(), ",", String.class);//所有玩家的ID
				{
					//检查所有玩家都存在
					boolean allplayerExist = true;
					for (String id : idArr) {
						Player player = null;
						try {
							long playerId = Long.parseLong(id);
							player = playerManager.getPlayer(playerId);
						} catch (Exception e) {
						}
						if (player == null) {
							allplayerExist = false;
							out.println("<H1>角色不存在,<font color=red>id:[" + id + "]</font></H1>");
							break;
						}
					}
	
					List<Article> articles = new ArrayList<Article>();
					List<Integer> colors = new ArrayList<Integer>();
					List<Integer> nums = new ArrayList<Integer>();
					List<Boolean> binds = new ArrayList<Boolean>();
	
					StringBuffer wrongArticle = new StringBuffer();
					if (allplayerExist) {//玩家都存在
						boolean allArticleExist = true;
						{
							for (int i = 1; i <= 5; i++) {
								String articleName = request.getParameter("articleName" + i);
								Article article = findArticle(articleName);
								if (articleName != null && !"".equals(articleName)) {
									if (article != null) {
										String numStr = request.getParameter("articleNum" + i);
										String colorStr = request.getParameter("articleColor" + i);
										String bindStr = request.getParameter("bind" + i);
										if (numStr != null && colorStr != null && bindStr != null) {
											//binds
											articles.add(article);
											colors.add(Integer.valueOf(colorStr));
											nums.add(Integer.valueOf(numStr));
											binds.add(!"0".equals(bindStr));//0-不绑定false
										} else {
											out.print("道具信息输入不全!请重新输入!");
											allArticleExist = false;
										}
									} else {
										wrongArticle.append("[" + articleName + "]");
										allArticleExist = false;
									}
								}
							}
						}
						if (!allArticleExist) {
							out.print("<H1>有物品输入错误,请重新输入!<font color=red>" + wrongArticle.toString() + "</font></H1>");
						} else {//开始给每个人发奖励
							int[] num = new int[nums.size()];
							for (int i = 0; i < num.length; i++) {
								num[i] = nums.get(i);
							}
						for (String id : idArr) {
								long playerId = Long.parseLong(id);
								Player player = playerManager.getPlayer(playerId);
								List<ArticleEntity> aes = new ArrayList<ArticleEntity>();
								String[] articlenames = new String[articles.size()]; //数据统计
								int[] counts = new int[articles.size()];
								for (int k = 0; k < articles.size(); k++) {
									Article article = articles.get(k);
									articlenames[k] = article.getName();
									counts[k] = nums.get(k);
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, binds.get(k), ArticleEntityManager.CREATE_REASON_后台, player, colors.get(k), nums.get(k), true);
									aes.add(ae);
									if (!recordMail) {
										articleMessage.append("<tr><td>"+ae.getArticleName()+"</td><td>"+ArticleManager.getColorString(article, ae.getColorType())+"</td><td>"+ nums.get(k) +"</td><td>"+binds.get(k)+"</td></tr>");
									}
								}
								long mailId = -1;
								mailId = mailManager.sendMail(player.getId(), aes.toArray(new ArticleEntity[0]), num, mailTitle, mailContent, yinziNum, 0L, 0L, "后台发送-" + mailTitle,MailSendType.后台发送,player.getName(),ip,recorder);
								if(!recordMail) {
									mailMessage.append("邮件标题:<font color=red>").append(mailTitle).append("</font><br/>");
									mailMessage.append("邮件内容:<font color=red>").append(mailContent).append("</font><br/>");
									recordMail = true;
								}
								playerMessage.append("<tr><td>"+player.getId()+"</td><td>"+player.getName()+"</td><td>"+player.getUsername()+"</td><td>"+player.getLevel()+"</td><td>"+player.getVipLevel()+"</td><td>"+mailId+"</td></tr>");
								
								{
									//向统计服发送
									for (int i = 0; i < aes.size();i++) {
										ArticleEntity ae = aes.get(i);
										DaoJuFlow daoJuFlow = new DaoJuFlow();
										daoJuFlow.setJixing(request.getRemoteAddr());
										daoJuFlow.setCreateDate(System.currentTimeMillis());
										daoJuFlow.setDanJia(0L);// 购买道具的单价
										daoJuFlow.setDaoJuName(ae.getArticleName());
										daoJuFlow.setDaoJuNum((long)num[i]);// 购买道具的数量
				
										daoJuFlow.setFenQu(GameConstants.getInstance().getServerName());
				
										daoJuFlow.setGetType("后台生成-sendNewArticle");
										daoJuFlow.setHuoBiType("");
				
										daoJuFlow.setGameLevel(String.valueOf(session.getAttribute("authorize.username")));
										daoJuFlow.setUserName(player.getName() + "/" + player.getId());
				
										daoJuFlow.setBindType(String.valueOf(ae.isBinded()));
										daoJuFlow.setDaoJuColor(String.valueOf(ae.getColorType()));
										daoJuFlow.setDaoJuLevel("0");
				
										daoJuFlow.setPosition("后台发送");// 购买道具的位置，比如商店购买
										if (!TestServerConfigManager.isTestServer()) {
											StatClientService statClientService = StatClientService.getInstance();
											statClientService.sendDaoJuFlow("", daoJuFlow);
											ArticleManager.logger.error("发送统计数据:" + daoJuFlow.toString());
										}
									}
								}
							}
							playerMessage.append("</table>");
							articleMessage.append("</table>");
							sendMail("后台补物品-",mailMessage.toString() + articleMessage.toString() + playerMessage.toString(),request);
							ArticleManager.logger.error("[通过后台发物品] [" +getOperateUser(request) +"] [给玩家发奖励] [成功]" + (mailMessage.toString() + articleMessage.toString() + playerMessage.toString()));
						}
	
					}
				}
			} else if (playerNames != null && playerNames.trim().length() != 0) {
				String[] nameArr = StringTool.string2Array(playerNames.trim(), ",", String.class);//所有玩家的ID
				{
					//检查所有玩家都存在
					boolean allplayerExist = true;
					for (String playerName : nameArr) {
						Player player = null;
						try {
							player = playerManager.getPlayer(playerName);
						} catch (Exception e) {
						}
						if (player == null) {
							allplayerExist = false;
							out.println("<H1>角色不存在,<font color=red>name:[" + playerName + "]</font></H1>");
							break;
						}
					}
	
					List<Article> articles = new ArrayList<Article>();
					List<Integer> colors = new ArrayList<Integer>();
					List<Integer> nums = new ArrayList<Integer>();
					List<Boolean> binds = new ArrayList<Boolean>();
	
					StringBuffer wrongArticle = new StringBuffer();
					if (allplayerExist) {//玩家都存在
						boolean allArticleExist = true;
						{
							for (int i = 1; i <= 5; i++) {
								String articleName = request.getParameter("articleName" + i);
								Article article = findArticle(articleName);
								//out.print("articleName:" + articleName);
								if (articleName != null && !"".equals(articleName)) {
									if (article != null) {
										String numStr = request.getParameter("articleNum" + i);
										String colorStr = request.getParameter("articleColor" + i);
										String bindStr = request.getParameter("bind" + i);
										if (numStr != null && colorStr != null && bindStr != null) {
											//binds
											articles.add(article);
											colors.add(Integer.valueOf(colorStr));
											nums.add(Integer.valueOf(numStr));
											binds.add(!"0".equals(bindStr));//0-不绑定false
										} else {
											out.print("道具信息输入不全!请重新输入!");
											allArticleExist = false;
										}
									} else {
										wrongArticle.append("[" + articleName + "]");
										allArticleExist = false;
									}
								}
							}
						}
						if (!allArticleExist) {
							out.print("<H1>有物品输入错误,请重新输入!<font color=red>" + wrongArticle.toString() + "</font></H1>");
						} else {//开始给每个人发奖励
							int[] num = new int[nums.size()];
							for (int i = 0; i < num.length; i++) {
								num[i] = nums.get(i);
							}
							for (String playerName : nameArr) {
								Player player = playerManager.getPlayer(playerName);
								List<ArticleEntity> aes = new ArrayList<ArticleEntity>();
								String[] articlenames = new String[articles.size()]; //数据统计
								int[] counts = new int[articles.size()];
								for (int k = 0; k < articles.size(); k++) {
									Article article = articles.get(k);
									articlenames[k] = article.getName();
									counts[k] = nums.get(k);
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, binds.get(k), ArticleEntityManager.CREATE_REASON_后台, player, colors.get(k), nums.get(k), true);
									aes.add(ae);
									if (!recordMail) {
										articleMessage.append("<tr><td>"+ae.getArticleName()+"</td><td>"+ArticleManager.getColorString(article, ae.getColorType())+"</td><td>"+ nums.get(k) +"</td><td>"+binds.get(k)+"</td></tr>");
									}
								}
								long mailId = -1;
								mailId = mailManager.sendMail(player.getId(), aes.toArray(new ArticleEntity[0]), num, mailTitle, mailContent, yinziNum, 0L, 0L, "后台发送-" + mailTitle,MailSendType.后台发送,player.getName(),ip,recorder);
								if(!recordMail) {
									mailMessage.append("邮件标题:<font color=red>").append(mailTitle).append("</font><br/>");
									mailMessage.append("邮件内容:<font color=red>").append(mailContent).append("</font><br/>");
									recordMail = true;
								}
								playerMessage.append("<tr><td>"+player.getId()+"</td><td>"+player.getName()+"</td><td>"+player.getUsername()+"</td><td>"+player.getLevel()+"</td><td>"+player.getVipLevel()+"</td><td>"+mailId+"</td></tr>");
								
								{
									//向统计服发送
									for (int i = 0; i < aes.size();i++) {
										ArticleEntity ae = aes.get(i);
										DaoJuFlow daoJuFlow = new DaoJuFlow();
										daoJuFlow.setJixing(request.getRemoteAddr());
										daoJuFlow.setCreateDate(System.currentTimeMillis());
										daoJuFlow.setDanJia(0L);// 购买道具的单价
										daoJuFlow.setDaoJuName(ae.getArticleName());
										daoJuFlow.setDaoJuNum((long)num[i]);// 购买道具的数量
				
										daoJuFlow.setFenQu(GameConstants.getInstance().getServerName());
				
										daoJuFlow.setGetType("后台生成-sendNewArticle");
										daoJuFlow.setHuoBiType("");
				
										daoJuFlow.setGameLevel(String.valueOf(session.getAttribute("authorize.username")));
										daoJuFlow.setUserName(player.getName() + "/" + player.getId());
				
										daoJuFlow.setBindType(String.valueOf(ae.isBinded()));
										daoJuFlow.setDaoJuColor(String.valueOf(ae.getColorType()));
										daoJuFlow.setDaoJuLevel("0");
				
										daoJuFlow.setPosition("后台发送");// 购买道具的位置，比如商店购买
										if (!TestServerConfigManager.isTestServer()) {
											StatClientService statClientService = StatClientService.getInstance();
											statClientService.sendDaoJuFlow("", daoJuFlow);
											ArticleManager.logger.error("发送统计数据:" + daoJuFlow.toString());
										}
									}
								}
							}
							playerMessage.append("</table>");
							articleMessage.append("</table>");
							sendMail("后台补物品-",mailMessage.toString() + articleMessage.toString() + playerMessage.toString(),request);
							ArticleManager.logger.error("[通过后台发物品] [" +getOperateUser(request) +"] [给玩家发奖励] [成功]" + (mailMessage.toString() + articleMessage.toString() + playerMessage.toString()));
						
						}
					}
				}
			}else{
				out.print("<H1>请输入角色ID</H1>");
			}
		}
	}
%>
<%!private Article findArticle(String articleName) {
		if (articleName != null && !"".equals(articleName)) {
			Article article = ArticleManager.getInstance().getArticle(
					articleName.trim());
			return article;
		}
		return null;
	}%>
<body style="font-size: 12px;">
	<div><%="send".equals(op) ? mailMessage.toString() + articleMessage.toString() + playerMessage.toString() + "</BR>" : ""%></div>
	<div style="float: left;">
		<form name="F1" action="./sendNewArticle.jsp" method="post">
			<BR>玩家账号(",")分隔(账号,id,或者名字只填其中一项即可):<BR />
			<textarea rows="3" cols="40" name="username"><%=usernames==null?"":usernames %></textarea>
			
			<input type="hidden" name="op" value="send"> <BR>玩家ID(",")分隔(账号,id,或者名字只填其中一项即可):<BR />
			<textarea rows="3" cols="40" name="playerIds"><%=playerIds==null?"":playerIds %></textarea>
			
			<BR>玩家名字(",")分隔(账号,id,或者名字只填其中一项即可):<BR />
			<textarea rows="3" cols="40" name="playerNames"><%=playerNames==null?"":playerNames %></textarea>
			<BR>邮件标题:<BR /> <input type="text" id="mailTitle"
				name="mailTitle" value="系统邮件"> <BR>邮件内容:<BR />
			<textarea id="mailContent" name="mailContent" rows="3" cols="40">您好,这是您意外丢弃的物品,祝您游戏愉快!</textarea>
			<table border="1">
				<tr style="color: red; font-weight: bold; font-size: 12px;">
					<td>物品名字</td>
					<td>颜色</td>
					<td>数量</td>
					<td>是否绑定<BR />(0-不绑定,1-绑定)</td>
				</tr>
				<tr>
					<td><input type="text" name="articleName1"
						onblur="checkArticle(this,1)"></td>
					<td><input type="text" name="articleColor1" size="10px">
					</td>
					<td><input type="text" name="articleNum1" size="10px">
					</td>
					<td><input type="text" name="bind1" size="10px"></td>
				</tr>
				<tr>
					<td><input type="text" name="articleName2"
						onblur="checkArticle(this,1)"></td>
					<td><input type="text" name="articleColor2" size="10px">
					</td>
					<td><input type="text" name="articleNum2" size="10px">
					</td>
					<td><input type="text" name="bind2" size="10px"></td>
				</tr>
				<tr>
					<td><input type="text" name="articleName3"
						onblur="checkArticle(this,1)"></td>
					<td><input type="text" name="articleColor3" size="10px">
					</td>
					<td><input type="text" name="articleNum3" size="10px">
					</td>
					<td><input type="text" name="bind3" size="10px"></td>
				</tr>
				<tr>
					<td><input type="text" name="articleName4"
						onblur="checkArticle(this,1)"></td>
					<td><input type="text" name="articleColor4" size="10px">
					</td>
					<td><input type="text" name="articleNum4" size="10px">
					</td>
					<td><input type="text" name="bind4" size="10px"></td>
				</tr>
				<tr>
					<td><input type="text" name="articleName5"
						onblur="checkArticle(this,1)"></td>
					<td><input type="text" name="articleColor5" size="10px">
					</td>
					<td><input type="text" name="articleNum5" size="10px">
					</td>
					<td><input type="text" name="bind5" size="10px"></td>
				</tr>
				<tr>
				</tr>
			</table>
			<input type="submit" value="提交">
		</form>
		<div id="show"></div>
	</div>
	<div style="float: left; margin-left: 80px;">
		<hr>
		<h2>标题预设</h2>
		<hr>
		<span class="mailTitle" ondblclick="changeTitle(this)"> 系统邮件 </span> <span
			class="mailTitle" ondblclick="changeTitle(this)"> 系统补偿 </span> <span
			class="mailTitle" ondblclick="changeTitle(this)"> 系统奖励 </span> <br />
		<hr>
		<h2>内容预设</h2>
		<hr>
		<span class="mailContent" ondblclick="changeContent(this)">
			您好,这是您被盗号找回的物品,请保护好自己的账号,祝您游戏愉快! </span>
		<hr>
		<span class="mailContent" ondblclick="changeContent(this)">
			您好,这是您意外丢弃的物品,祝您游戏愉快! </span>
		<hr>
		<span class="mailContent" ondblclick="changeContent(this)">
			您好,这是您参与活动的奖励,祝您游戏愉快! </span>
		<hr>
	</div>
</body>
<script type="text/javascript">
	function checkArticle(element, type) {
		var input = element.value;
		if (input === "") {
			document.getElementById("show").innerText = "";
			return;
		}
		var message = getArticle(input, type);
		document.getElementById("show").innerHTML = message;
	}
</script>
</html>
