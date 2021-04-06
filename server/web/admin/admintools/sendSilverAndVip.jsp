<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.economic.SavingReasonType"%>
<%@page import="com.fy.engineserver.economic.CurrencyType"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerNameStr = request.getParameter("players");
	String send = request.getParameter("send");
	String silverStr = request.getParameter("siver");
	String sendType = request.getParameter("sendType");
	
	if (sendType == null || "".equals(sendType)) {
		sendType = "0";
	}
	if ("send".equals(send)) {
		boolean realSend = true;
		String[] playerNameArr = playerNameStr.split("\r\n");
		String[] silverStrArr = silverStr.split("\r\n");
		
		boolean sendVipOnly = "1".equals(sendType);
		
		out.print(Arrays.toString(playerNameArr) + "<BR>");
		out.print(Arrays.toString(silverStrArr) + "<BR>");
		if (silverStrArr.length != playerNameArr.length) {
			out.print("<H2>输入的角色数量和要发的银锭数量不符合</H2>");
		} else {
			long[] siverArr = new long[silverStrArr.length];
			int [] rmb = new int [silverStrArr.length];//相当于多少RMB,增加VIP用
			for (int i = 0; i < silverStrArr.length; i++) {
				double ding = Double.parseDouble(silverStrArr[i]);
				siverArr[i] = (long)(ding * 1000 * 1000);//文
				rmb[i] = (int)(ding * 20 * 100);
			}
			Player [] players = new Player[silverStrArr.length];
			for (int i = 0; i < playerNameArr.length; i++) {
				Player player = null;
				try {
					player = GamePlayerManager.getInstance().getPlayer(playerNameArr[i]);
				} catch (Exception e) {}
				if (player != null) {
					players[i] = player;
				} else {
					realSend = false;
					out.print("<H2 style='color:red;'>角色不存在:"+playerNameArr[i]+",所有人都没有发放,请确定角色名称后再发放</H2></BR>");
				}
			}
			if (realSend) {
				for (int i = 0; i < playerNameArr.length; i++) {
					Player player = players[i];
					int oldVip = player.getVipLevel();
					int rbmAdd = rmb[i];
					long silver = siverArr[i];
					long oldSilver = player.getSilver();
					long oldRmb = player.getRMB();
					if (!sendVipOnly) {
						BillingCenter.getInstance().playerSaving(player,silver,CurrencyType.YINZI,SavingReasonType.通知奖励,"后台发银子");
					}
					player.setRMB(player.getRMB() + rmb[i]);
					String notice = "发放银子成功:["+player.getName()+"] [银子变化:" + BillingCenter.得到带单位的银两(oldSilver) +"-->"+BillingCenter.得到带单位的银两(player.getSilver())+"] [rmb变化:"+oldRmb+ "-->" + player.getRMB()+"] [vip变化:"+oldVip + "--->"+ player.getVipLevel()+"]";
					out.print(notice + "</BR>");
					System.out.println(notice );
				}
			}
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="font-size: 12px;">
	<form action="" method="post">
		<h1 color=red><%=GameConstants.getInstance().getServerName() %></h1>
		<H3>一下2个列表中的数据要去除空格,数量严格要求一致</H3>
		<table border="1">
			<tr>
				<td colspan="3">
					增加银子和VIP:<input type="radio" name="sendType" value="0" <%=("0".equals(sendType)) ? "checked" :"" %>>
					只增加vip:<input type="radio" name="sendType" value="1" <%="1".equals(sendType) ? "checked" :"" %>>
				</td>
			</tr>
			<tr>
				<td>要发银锭的玩家列表:<BR/><textarea rows="15" cols="15" name="players"></textarea></td>
				<td>要发银锭的数量(锭):<BR/><textarea rows="15" cols="15" name="siver"></textarea></td>
				<td><input type="hidden" name="send" value="send"> 
				<input type="submit" value="发银子" style="background-color: red;"></td>
			</tr>
		</table>
	</form>
</body>
</html>