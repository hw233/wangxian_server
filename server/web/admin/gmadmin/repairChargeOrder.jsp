<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@include file="../admintools/inc.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String time = sdf.format(new Date());
	if (!time.startsWith("2017")) {
		out.append("页面已过期,请联系吉雅泰!");
		return ;
	}
	
	String mailTitle = "";
   	String mailContent = "";
	if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
		mailTitle = "系統郵件";
		mailContent = "這是您未到賬的銀子,請注意查收,祝您遊戲愉快!";
	} else if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
		mailTitle = "충전 복구 메일";
		mailContent = "안녕하세요 수행자님,누락된 충전에 대하여 복구를 해드렸습니다.첨부파일 확인 부탁드립니다.(사기 메일에 주의해 주세요)";
	} else if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
		mailTitle = "系统邮件";
		mailContent = "这是您未到账的银子,祝您游戏愉快!";
	}
	String send = request.getParameter("send");
	String playerNames = "";
	String silvers = "";
	String rmbs = "";
	String pwd = request.getParameter("pwd");
	
	boolean usePlayerId = "playerId".equals(request.getParameter("inputType"));
	String ip = request.getRemoteAddr();
	String recorder = "";
	Object o = session.getAttribute("authorize.username");
	if(o!=null){
		recorder = o.toString();
	}
	if ("true".equals(send)) {
		mailTitle = request.getParameter("mailTitle");
		mailContent = request.getParameter("mailContent");
		playerNames = request.getParameter("playerNames");
		silvers = request.getParameter("silvers");
		rmbs = request.getParameter("rmbs");
		String[] playerNameArr = StringTool.string2Array(playerNames, "\r\n", String.class);
		Double[] silverArr = StringTool.string2Array(silvers, "\r\n", Double.class);
		Double[] rmbArr = StringTool.string2Array(rmbs, "\r\n", Double.class);
			boolean validate = validate(playerNameArr, silverArr, rmbArr, out,usePlayerId);
			
			
			if ("%c0901z_#%".equals(pwd)) {
				if (validate) {
					StringBuffer content = new StringBuffer();
					if (playerNameArr.length == 1){
					{
	
						//发送者信息:
						content.append("<table style='font-size: 12px;text-align: center;' border='1'>");
	
						content.append("<tr>");
						content.append("<td style='background-color: red;color: white;font-size: 14px;font-weight: bold;' colspan='2'>发送者信息</td>");
						content.append("</tr>");
	
						content.append("<tr>");
						content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>发送者</td>");
						content.append("<td>").append(request.getSession().getAttribute("authorize.username")).append("</td>");
						content.append("</tr>");
	
						content.append("<tr>");
						content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>IP</td>");
						content.append("<td>").append(request.getRemoteAddr()).append("</td>");
						content.append("</tr>");
	
						content.append("<tr>");
						content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>服务器</td>");
						content.append("<td>").append(GameConstants.getInstance().getServerName()).append("</td>");
						content.append("</tr>");
	
						content.append("</table>");
						content.append("<HR>");
					
					}
	
					content.append("<table style='font-size: 12px;text-align: center;' border='1'>");
					content.append("<tr>");
					content.append("<td style='background-color: red;color: white;font-size: 14px;font-weight: bold;' colspan='7'>接受者信息</td>");
					content.append("</td>");
					content.append("</tr>");
					content.append("<tr>");
					content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>用户名</td>");
					content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>角色ID</td>");
					content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>角色名</td>");
					content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>邮件内银子</td>");
					content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>VIP变化</td>");
					content.append("</td>");
					content.append("</tr>");
					
					
					//开始发邮件
					for (int i = 0; i < playerNameArr.length ; i++) {
						String playerName =  playerNameArr[i];
						Player player = null;
						if (usePlayerId) {
							player = GamePlayerManager.getInstance().getPlayer(Long.valueOf(playerName));
						} else {
							player = GamePlayerManager.getInstance().getPlayer(playerName);
						}
						int silver = (int)(silverArr[i] * 1000);
						int rmb = (int)(rmbArr[i] * 100);
						
						int oldVip = player.getVipLevel();
						long oldRMB = player.getRMB();
						long oldSilver = player.getSilver();
						
						long mailId = MailManager.getInstance().sendMail(player.getId(),new ArticleEntity[0],new int[0],mailTitle,mailContent,silver,0L,0L,"系统补单",MailSendType.后台发送,player.getName(),ip,recorder);
						player.setRMB(player.getRMB() + rmb);
						player.setOne_day_rmb(player.getOne_day_rmb() + rmb);
						String notice = "[系统补单完成] [" + player.getLogString() + "] [发送银子:" + BillingCenter.得到带单位的银两(silver) + "] [vip变化:"+oldVip + ">"+ player.getVipLevel() +"]";
	
						ChargeManager.logger.warn(notice);
						
						NewChongZhiActivityManager.instance.doChongZhiActivity(player, silver);
						notice += "</BR>";
						notice+=("模拟充值成功:" + silver);
						out.print(notice + "<BR/>");
						
	
						content.append("<tr>");
						content.append("<td>" + player.getUsername() + "</td>");
						content.append("<td>" + player.getId() + "</td>");
						content.append("<td>" + player.getName() + "</td>");
						content.append("<td>" + "[" + BillingCenter.得到带单位的银两(silver) +"]" +"</td>");
						content.append("<td>" + "[" + oldVip + "(" + oldRMB +  ")" + " > " + player.getVipLevel() + "(" + player.getRMB() + ")" + "]" + "</td>");
						content.append("</td>");
						content.append("</tr>");
						
					}
					content.append("</table>");
					
					sendMail("[客服直充] [" + session.getAttribute("authorize.username") + "] [" + GameConstants.getInstance().getServerName() + "]", content.toString());
				} else {
					out.print("<H1>单次只能发一条</H1>");
				}
			}
		} else {
			out.print("<H1>密码错误，不能补单</H1>");
		}
	}
%>
<%!boolean validate(String[] playerNameArr, Double[] silverArr, Double[] rmbArr, JspWriter out,boolean usePlayerId) throws Exception {
		if (playerNameArr == null || silverArr == null || rmbArr == null) {
			out.print("<h2><font color='red'>输入错误</font></h2>");
			return false;
		}
		if (playerNameArr.length != silverArr.length || silverArr.length != rmbArr.length) {
			out.print("<h2><font color='red'>输入错误,列表长度不同</font></h2>");
			return false;
		}
		int max = 1000000;
		for (Double silverValue : silverArr) {
			if (silverValue > max) {
				out.print("<h2><font color='red'>输入受限.单条金额不能超过"+BillingCenter.得到带单位的银两(max * 1000) +"</font></h2>");
				return false;
			}
		}
		StringBuffer sbf = new StringBuffer("<BR/>");
		boolean error = false;
		for (String name : playerNameArr) {
			try {
				Player p =  null;
				if (usePlayerId) {
					p = GamePlayerManager.getInstance().getPlayer(Long.valueOf(name));
				} else {
					p = GamePlayerManager.getInstance().getPlayer(name);
				}
				if (p == null) {
					error = false;
					sbf.append("角色不存在:").append(name).append("<BR/>");
				}
			} catch (Exception e) {

			}
		}
		if (error) {
			out.print("<font color='red'>输入错误," + sbf.toString() + "</font>");
			return false;
		}
		return true;
	}%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>补单专用.发邮件+vip</title>
</head>
<body>
	<hr>
	<h1><%=GameConstants.getInstance().getServerName() %></h1>
	<hr>
	<form action="" method="post">
	输入密码:<input type="password" name="pwd" ><br/>
		<table style="font-size: 12px;text-align: center;" border="1">
			<tr style="color: red;font-weight: bold;">
				<td>角色名称</td>
				<td>银子(两)</td>
				<td>增加的钱(元)</td>
			</tr>
			<tr>
				<td><input name="playerNames" size="15%" value="<%=playerNames%>">
				</td>
				<td><input name="silvers" size="15%"  value="<%=silvers%>">
				</td>
				<td><input name="rmbs" size="15%" value="<%=rmbs%>">
				</td>
			</tr>
			<tr>
				<td>邮件标题<input type="text" name="mailTitle" value="<%=mailTitle%>"></td>
				<td colspan="2">邮件内容 <textarea name="mailContent" rows="3" cols="40"><%=mailContent %></textarea></td>
			</tr>
			<tr>
				<td>
					角色名称发放:<input name="inputType" type="radio" value="playerName"  <%=!usePlayerId ? "checked" :"" %>>
					角色ID发放:<input name="inputType" type="radio" value="playerId" <%=usePlayerId ? "checked" :"" %>>
				</td>
				<td colspan="2"><input type="hidden" name="send" value="true">
					<input type="submit" style="background-color: red; font-weight: bold;" value="提交">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>