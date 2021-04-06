<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ include file="./sendEmail.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
	String getOperateUser(HttpServletRequest request){
		return String.valueOf(request.getSession().getAttribute("authorize.username"));
	}
%>
<%
String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
	out.print("<h2>"+getOperateUser(request)+"</h2>");
	String option = request.getParameter("option");
	String playerIdStr = request.getParameter("playerId");
	String playerName = request.getParameter("playerName");
	String mailTitle = request.getParameter("mailTitle");
	String mailContent = request.getParameter("mailContent");
	StringBuffer sbf = new StringBuffer();
	if ("send".equals(option)){
		if(getOperateUser(request) == null || "".equals(getOperateUser(request))){
			out.print("<h1><font color=red>你还没有登陆,不能操作</font><h1>");
		} else {
			if ((playerName == null || playerName.trim().equals(""))&& (playerIdStr == null || playerIdStr.trim().equals(""))) {
				out.print("<h2><font color=red>请输入有效的角色信息</font></h2>");
			} else {
				Player p = null;
				if (playerIdStr != null && !playerIdStr.trim().equals("")){
					p = GamePlayerManager.getInstance().getPlayer(Long.valueOf(playerIdStr));
				}
				if (p == null) {
					p = GamePlayerManager.getInstance().getPlayer(playerName);
				}
				if (p == null) {
					out.print("<h2><font color=red>角色不存在</font></h2>");
				} else {
					boolean checkSucss = true;
					StringBuffer notice = new StringBuffer();
					List<ArticleEntity> aeList = new ArrayList<ArticleEntity>();
					for (int i = 0 ; i < 6;i++) {
						String articleIdStr = request.getParameter("articleId"+i);
						if (articleIdStr != null && !"".equals(articleIdStr.trim())) {
							Long articleId = Long.valueOf(articleIdStr);
							ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(articleId);
							if (ae == null) {
								checkSucss = false;
								notice.append("<h2><font color=red>物品不存在:"+articleIdStr+"</font></h2>");
							} else {
								aeList.add(ae);
							}
						}
					}
					if (!checkSucss) {
						out.print(notice.toString());
					} else {
						long mailId = MailManager.getInstance().sendMail(-1,p.getId(),aeList.toArray(new ArticleEntity[0]),mailTitle,mailContent,0,0,0,"",MailSendType.后台发送,p.getName(),ip,recorder);
						
						sbf.append("<h3>后台发送已存在的物品</h3>");
						sbf.append("邮件标题:" + mailTitle + "<BR/>");
						sbf.append("邮件内容:" + mailContent + "<BR/>");
						sbf .append("<table>");
						sbf .append("<tr bgcolor=#61ABE9><td>物品ID</td><td>物品名字</td><td>物品颜色</td><td>是否绑定</td></tr>");
						for (ArticleEntity ae : aeList) {
							if (ae != null) {
								sbf .append("<tr><td>"+ae.getId()+"</td><td>"+ae.getArticleName()+"</td><td>"+ae.getColorType()+"</td><td>"+ae.isBinded()+"</td></tr>");
							}
						}
						//数据统计
						/*ArticleEntity  [] articleEntities = (ArticleEntity[])aeList.toArray();
						String articlenames[] = new String[articleEntities.length];
						int[] counts = new int[articleEntities.length];
						for(int i=0;i<articleEntities.length;i++){
							articlenames[i] = articleEntities[i].getArticleName();
							counts[i] = (int)articleEntities[i].getReferenceCount();
						}*/
						
						sbf.append("<BR/>");
						sbf.append("<hr>");
						sbf.append("<hr>");
						sbf.append("接受者信息:");
						sbf.append("<tr bgcolor=#61ABE9><td>角色ID</td><td>角色名字</td><td>账号</td><td>等级</td><td>vip等级</td><td>邮件ID</td></tr>");
						sbf.append("<tr><td>"+p.getId()+"</td><td>"+p.getName()+"</td><td>"+p.getUsername()+"</td><td>"+p.getLevel()+"</td><td>"+p.getVipLevel()+"</td><td>"+mailId+"</td></tr>");
						ArticleManager.logger.error("[通过后台发物品]" + sbf.toString());
						sendMail("后台补物品-",sbf.toString(),request);
						//数据统计
						/*String ip = request.getRemoteAddr();
						Object o = session.getAttribute("authorize.username");
						if(o!=null){
							String recorder = o.toString();
							RecordEvent mailevent =  new MailEvent(mailTitle,mailContent,articlenames,counts,0,p.getUsername(),ip,recorder);
							EventManager.getInstance().eventAdd(mailevent);
						}*/
					}
				}
			}
		}
	}
	if (playerIdStr == null) {
		playerIdStr = "";
	}
	if (playerName == null) {
		playerName = "";
	}
%>

<%@page import="com.fy.engineserver.util.gmstat.RecordEvent"%>
<%@page import="com.fy.engineserver.util.gmstat.event.MailEvent"%>
<%@page import="com.fy.engineserver.util.gmstat.EventManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="cssOfSendArticle.css" />
<script type="text/javascript" src="jquery-1.8.2.js"></script>
<script type="text/javascript" src="jsOfSendArticle.js"></script>
<title>通过ID给玩家发送已有物品</title>
</head>
<body style="font-size: 12px;">
<div style="float: left;">
	<form name="f1" action="./sendOldArticle.jsp" method="post">
	<input type="hidden" value="send" name="option">
		<div>角色id: <input type="text" name="playerId" value="<%=playerIdStr%>"></div>
		<div>角色名字:<input type="text" name="playerName" value="<%=playerName%>"></div>
		<div>邮件标题:<input type="text" id="mailTitle" name="mailTitle" value="<%=mailTitle==null?"系统邮件":mailTitle%>"></div>
		<div>邮件内容:<textarea rows="3" cols="40"id="mailContent"name="mailContent" ><%=mailContent==null?"您好,这是您意外丢失的物品,祝您游戏愉快!":"" %></textarea></div>
		<div ><input type="submit" value="发送邮件" style="background-color: red;"></div>
		<div>
		<table>
			<tr>
				<td width="18px;">物品id1</td>
				<td width="18px;">物品id2</td>
				<td width="18px;">物品id3</td>
				<td width="18px;">物品id4</td>
				<td width="18px;">物品id5</td>
			</tr>
			<tr>
				<td><input name="articleId1" type="text" size="18px;" onblur="checkArticle(this,0)"></td>
				<td><input name="articleId2" type="text" size="18px;" onblur="checkArticle(this,0)"></td>
				<td><input name="articleId3" type="text" size="18px;" onblur="checkArticle(this,0)"></td>
				<td><input name="articleId4" type="text" size="18px;" onblur="checkArticle(this,0)"></td>
				<td><input name="articleId5" type="text" size="18px;" onblur="checkArticle(this,0)"></td>
			</tr>
			<tr>
				<td width="18px;"><span id="articleId1_show" style="width:18px;"></span></td>
				<td width="18px;"><span id="articleId2_show" style="width:18px;"></span></td>
				<td width="18px;"><span id="articleId3_show" style="width:18px;"></span></td>
				<td width="18px;"><span id="articleId4_show" style="width:18px;"></span></td>
				<td width="18px;"><span id="articleId5_show" style="width:18px;"></span></td>
			</tr>
		</table>
		</div>
	</form>
</div>

<div style="float: left;margin-left:40px;">
<hr>
		<h2>标题预设</h2>
	<hr>
	<span class="mailTitle" ondblclick="changeTitle(this)">
		系统邮件
	</span>
	<span class="mailTitle" ondblclick="changeTitle(this)">
		系统补偿
	</span>
	<span class="mailTitle" ondblclick="changeTitle(this)">
		系统奖励
	</span>
	<br/>
	<hr>
		<h2>内容预设</h2>
	<hr>
	<span class="mailContent" ondblclick="changeContent(this)">
		您好,这是您被盗号找回的物品,请保护好自己的账号,祝您游戏愉快!
	</span>
	<hr>
	<span class="mailContent" ondblclick="changeContent(this)">
		您好,这是您意外丢失的物品,祝您游戏愉快!
	</span>
	<hr>
	<span class="mailContent" ondblclick="changeContent(this)">
		您好,这是您参与活动的奖励,祝您游戏愉快!
	</span>
	<hr>
</div>
</body>
<script type="text/javascript">
function checkArticle(element,type) {
	var input = element.value;
	var name = element.name;
	if (input === "") {
		document.getElementById(name+"_show").innerText="";
		return;
	}
	var message = getArticle(input,type);
	//$("#"+name+"_show").css("background-color","red");
	document.getElementById(name+"_show").innerHTML=message;
}
</script>
</html>