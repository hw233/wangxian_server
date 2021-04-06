<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@include file="IPManager.jsp"%>
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
<%@page import="com.fy.engineserver.message.HINT_REQ"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>批量发送邮件</title>
<%
String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
	String op = request.getParameter("op");
	//out.print("op:" + op);
	StringBuffer sbf = new StringBuffer();
	if (op != null && "send".equals(op)) {//要发送
		String pwd = request.getParameter("pwd");
		if (pwd.equals("pwd")) {
			MailManager mailManager = MailManager.getInstance();
			PlayerManager playerManager = PlayerManager.getInstance();
			String playerIds = request.getParameter("playerIds");
			String playerNames = request.getParameter("playerNames");
			String mailTitle = request.getParameter("mailTitle");
			String mailContent = request.getParameter("mailContent");
			String coin = request.getParameter("coin");
			List<Player> players = new ArrayList<Player>();

			//检查所有玩家都存在
			boolean allplayerExist = true;
			if (playerIds != null && playerIds.trim().length() != 0) {
				String[] idArr = StringTool.string2Array(playerIds.trim(), "\n", String.class);//所有玩家的ID
				{
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
						} else {
							players.add(player);
						}
					}
				}
			} else if (playerNames != null && playerNames.trim().length() != 0) {
				String[] nameArr = StringTool.string2Array(playerNames.trim(), "\n", String.class);//所有玩家的名字
				{
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
						} else {
							players.add(player);
						}
					}

				}
			} else {
				out.print("<H1>请输入角色ID</H1>");
			}
			if (players.size() > 0) {
				StringBuffer wrongArticle = new StringBuffer();
				if (allplayerExist) {//玩家都存在
					boolean allArticleExist = true;
					String articleName = request.getParameter("articleName");
					String[] names = articleName.split("\n");
					String numStr = request.getParameter("articleNum");
					String[] numArr = numStr.split("\n");
					String colorStr = request.getParameter("articleColor");
					String[] colorArr = colorStr.split("\n");
					String bindStr = request.getParameter("bind");
					String[] bindArr = bindStr.split("\n");
					for (int i = 0; i < names.length; i++) {
						Article article = findArticle(names[i].trim());
						if (articleName != null && !"".equals(articleName)) {
							if (article == null) {
								wrongArticle.append("[" + names[i] + "]");
								allArticleExist = false;
							}
						}
					}
					if (!allArticleExist) {
						out.print("<H1>有物品输入错误,请重新输入!<font color=red>" + wrongArticle.toString() + "</font></H1>");
					} else if(articleName!=null&&!"".equals(articleName)){//开始给每个人发奖励
						ActivityProp[] activityProps = new ActivityProp[names.length];
						for (int i = 0; i < names.length; i++) {
							ActivityProp ap;
							if (bindArr[i].trim().equals("1")) {//0-不绑定false
								ap = new ActivityProp(names[i].trim(), Integer.valueOf(colorArr[i].trim()), Integer.valueOf(numArr[i].trim()), true);
							} else {
								ap = new ActivityProp(names[i].trim(), Integer.valueOf(colorArr[i].trim()), Integer.valueOf(numArr[i].trim()), false);

							}
							activityProps[i] = ap;
						}
						ActivityManager.sendMailForActivity(players, activityProps, mailTitle, mailContent, "后台发送");
						sbf.append("<H1>发送物品成功</H1>");
					}
				}
			}

			if (coin != null && !"".equals(coin) && Integer.valueOf(coin)!=0) {
				for (Player p : players) {
					MailManager.getInstance().sendMail(p.getId(), null, null, mailTitle, mailContent, Long.valueOf(coin), 0L, 0L, "后台发送",MailSendType.后台发送,p.getName(),ip,recorder);
				}
				sbf.append("<H1>邮件发送银子成功</H1>");
			}

		} else {
			out.print("不知道密码还想发？几个意思？<br>");
		}

	}
%>
<%!private Article findArticle(String articleName) {
		if (articleName != null && !"".equals(articleName)) {
			Article article = ArticleManager.getInstance().getArticle(articleName);
			return article;
		}
		return null;
	}%>
</head>
<body>
<div>
<form name="F1" action="./sendMailForGroupNew.jsp?op=send" method="post">
<div style="float: left">
<table border="1">
	<tr style="background-color: red;">
		<td>物品统计名</td>
		<td>颜色</td>
		<td>数量</td>
		<td>是否绑定(0-不绑定,1-绑定)</td>
	</tr>
	<tr>
		<td><textarea rows="50" name="articleName"></textarea></td>
		<td><textarea rows="50" name="articleColor"></textarea></td>
		<td><textarea rows="50" name="articleNum"></textarea></td>
		<td><textarea rows="50" name="bind"></textarea></td>
	</tr>
</table>
</div>
<div style="float: left;margin-left:20px;"><BR>
玩家ID(",")分隔(id或名字只填其中一项即可):<BR />
<textarea rows="5" cols="40" name="playerIds"></textarea> <BR><BR>
玩家名字(",")分隔(id或名字只填其中一项即可):<BR />
<textarea rows="5" cols="40" name="playerNames"></textarea> <BR><BR>
邮件标题:<BR />
<textarea name="mailTitle" rows="5" cols="40"></textarea> <BR><BR>
邮件内容:<BR />
<textarea name="mailContent" rows="5" cols="40"></textarea><BR><BR>

银子：<input type="text" name="coin"><BR/><BR>
密码：<input type="password" name="pwd"> 
<input type="submit"
	value="提交"> <%=sbf.toString()%></div>

</form>
</div>
</body>
</html>
 