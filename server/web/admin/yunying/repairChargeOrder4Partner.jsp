<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.util.gmstat.RecordEvent"%>
<%@page import="com.fy.engineserver.util.gmstat.EventManager"%>
<%@page import="com.fy.engineserver.util.gmstat.event.MailEvent"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
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
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	Platform platform = PlatformManager.getInstance().getPlatform();
	
	String mailTitle = "";
   	String mailContent = "";
	if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
		mailTitle = "系統郵件";
		mailContent = "這是您未到賬的銀子,請注意查收,祝您遊戲愉快!";
	} else if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
		mailTitle = "충전 복구 메일";
		mailContent = "안녕하세요 수행자님,누락된 충전에 대하여 복구를 해드렸습니다.첨부파일 확인 부탁드립니다.(사기 메일에 주의해 주세요)";
	}
	String send = request.getParameter("send");
	String playerNames = "";
	String silvers = "";
	String rmbs = "";
	String pwd = request.getParameter("pwd");
	
	if ("true".equals(send)) {
		mailTitle = request.getParameter("mailTitle");
		mailContent = request.getParameter("mailContent");
		playerNames = request.getParameter("playerNames");
		silvers = request.getParameter("silvers");
		rmbs = request.getParameter("rmbs");
		String[] playerNameArr = StringTool.string2Array(playerNames, "\r\n", String.class);
		Double[] silverArr = StringTool.string2Array(silvers, "\r\n", Double.class);
		Double[] rmbArr = StringTool.string2Array(rmbs, "\r\n", Double.class);
		boolean validate = validate(playerNameArr, silverArr, rmbArr, out);
		if ("repairChargeOrder4Partner".equals(pwd)) {
			if (validate) {
				if (moneyValidate(silverArr)) {//判断金额
					//数据统计
					String ip = request.getRemoteAddr();
					String recorder  = "";
					Object o = session.getAttribute("authorize.username");
					//开始发邮件
					for (int i = 0; i < playerNameArr.length ; i++) {
						String playerName =  playerNameArr[i];
						Player player = GamePlayerManager.getInstance().getPlayer(playerName);
						int silver = (int)(silverArr[i] * 1000);
						int rmb = (int)(rmbArr[i] * 100);
						
						int oldVip = player.getVipLevel();
						long oldSilver = player.getSilver();
						if(o!=null){
							 recorder = o.toString();
						}
						long mailId = MailManager.getInstance().sendMail(player.getId(),new ArticleEntity[0],new int[0],mailTitle,mailContent,silver,0L,0L,"系统补单",MailSendType.后台发送,player.getName(),ip,recorder);
						player.setRMB(player.getRMB() + rmb);
						String notice = "[系统补单完成] ["+player.getLogString()+"] [发送银子:"+BillingCenter.得到带单位的银两(silver)+"] [vip变化:"+oldVip + ">"+ player.getVipLevel() +"]";
						out.print(notice + "<BR/>");
						ChargeManager.logger.warn(notice);
						
					}
				} else {
					out.print("<H1>发送银子金额过量,请重新输入</H1>");
				}
			}
		} else {
			out.print("<H1>密码错误，不能补单</H1>");
		}
	}
%>
<%!boolean validate(String[] playerNameArr, Double[] silverArr, Double[] rmbArr, JspWriter out) throws Exception {
		if (playerNameArr == null || silverArr == null || rmbArr == null) {
			out.print("<h2><font color='red'>输入错误</font></h2>");
			return false;
		}
		if (playerNameArr.length != silverArr.length || silverArr.length != rmbArr.length) {
			out.print("<h2><font color='red'>输入错误,列表长度不同</font></h2>");
			return false;
		}
		StringBuffer sbf = new StringBuffer("<BR/>");
		boolean error = false;
		for (String name : playerNameArr) {
			try {
				Player p = GamePlayerManager.getInstance().getPlayer(name);
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
	<%!
		boolean moneyValidate(Double[] silverArr){
			for (Double silver : silverArr) {
				if (PlatformManager.getInstance().getPlatform().equals(Platform.韩国)) {
					if (silver > 1250) {//韩国限制发送1250两银子,超过此数量,不允许发送
						return false;
					}
				}
			}
			return true;
		}
	%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合作方补单</title>
</head>
<body>
	<hr>
	<h1><%=GameConstants.getInstance().getServerName() %></h1>
	<hr>
	<form action="" method="post">
	输入密码:<input type="password" name="pwd" ><br/>
		<table style="font-size: 12px;text-align: center;">
			<tr style="color: red;font-weight: bold;">
				<td>角色名称</td>
				<td>银子(<B>两</B>)</td>
				<td>增加的VIP经验<%=platform.equals(Platform.韩国) ? "(销售币-元)" : "(昆仑币-元)"  %></td>
			</tr>
			<tr>
				<td><textarea name="playerNames" rows="15" cols="20"><%=playerNames%></textarea>
				</td>
				<td><textarea name="silvers" rows="15" cols="20"><%=silvers%></textarea>
				</td>
				<td><textarea name="rmbs" rows="15" cols="20"><%=rmbs%></textarea>
				</td>
			</tr>
			<tr>
				<td>邮件标题<input type="text" name="mailTitle" value="<%=mailTitle%>"></td>
				<td colspan="2">邮件内容 <textarea name="mailContent" rows="3" cols="40"><%=mailContent %></textarea></td>
			</tr>
			<tr>
				<td colspan="3"><input type="hidden" name="send" value="true">
					<input type="submit"
					style="background-color: red; font-weight: bold;" value="提交">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>