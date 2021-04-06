<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.fy.engineserver.wing.WingManager"%>
<%@page
	import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.wing.WingPanel"%>
<%@page import="com.fy.engineserver.wing.Wing"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.core.res.ResourceManager"%>
<%@page import="com.fy.engineserver.wing.BrightInlay"%>
<%@page import="com.fy.engineserver.wing.BrightInlayTypeProp"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.wing.WingAddProp"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>翅膀信息显示</title>
<style type="text/css">
body {
	font-size: 12px;
}

table,th,td {
	border: 1px solid blue;
	border-spacing: 0px;
	border-collapse: collapse;
}
</style>
<%
	WingManager wm = WingManager.getInstance();
	String playerName = request.getParameter("playerName");
	if (!"".equals(playerName) && null != playerName) {
		Player p = null;
		try{
			p = PlayerManager.getInstance().getPlayer(playerName);
		}catch (Exception e){
			p = PlayerManager.getInstance().getPlayer(Long.valueOf(playerName));
		}
		if (p != null) {
			WingPanel wp = p.getWingPanel();
			if (wp != null) {
				out.print("翅膀面板id:" + wp.getId() + "<br>");
				out.print("星级:" + wp.getStar() + "<br>");
				wm.getPropValue(p);
				int[] wingIds = wm.getWingsOnPlayer(p);
				if (wingIds.length > 0) {
					out.print("<fieldset style='width:200px;'><legend>玩家现有翅膀:</legend>");
					for (int wingId : wingIds) {
						Wing w = wm.getWingbyId(wingId, p);
						out.print("翅膀id:" + wingId + "--个数:" + w.getCurrNum() + "<br>");
					}
					out.print("</fieldset>");
				} else {
					out.print("玩家还没有收藏的翅膀");
				}
				out.print("<fieldset style='width:200px;float:left;'><legend>宝石孔开启状态:</legend>");
				for (boolean inlayOpen : wp.getInlayOpen()) {
					out.print(inlayOpen + "<br>");
				}
				out.print("</fieldset>");
				out.print("<fieldset style='width:200px;'><legend>宝石孔镶嵌状态:</legend>");
				for (long inlayId : wp.getInlayArticleIds()) {
					out.print(inlayId + "<br>");
				}
				out.print("</fieldset>");
				out.print("光效宝石镶嵌状态:" + wp.getBrightInlayId() + "<br>");
				out.print("每种翅膀最大收集数量:" + wm.getMaxKeepNum(p) + "<br>");

				out.print("<hr>");
				out.print("几个属性数组:<br>");
				out.print("<table>");
				out.print("<tr>");
				out.print("<td>属性类型</td>");
				for(int i=0;i<wp.propertyInfo.length;i++){
					out.print("<td>"+wp.propertyInfo[i]+"</td>");
				}
				out.print("</tr>");
				out.print("<tr>");
				out.print("<td>总加成</td>");
				for(int i=0;i<wp.getPropertyValue().length;i++){
					out.print("<td>"+wp.getPropertyValue()[i]+"</td>");
				}
				out.print("</tr>");
				out.print("<tr>");
				out.print("<td>基础加成</td>");
				for(int i=0;i<wp.getBasicPropertyValue().length;i++){
					out.print("<td>"+wp.getBasicPropertyValue()[i]+"</td>");
				}
				out.print("</tr>");
				out.print("<tr>");
				out.print("<td>基础激活</td>");
				for(int i=0;i<wp.getBasicPropertyActive().length;i++){
					out.print("<td>"+wp.getBasicPropertyActive()[i]+"</td>");
				}
				out.print("</tr>");
				out.print("<tr>");
				out.print("<td>奖励加成</td>");
				for(int i=0;i<wp.getPrizePropertyValue().length;i++){
					out.print("<td>"+wp.getPrizePropertyValue()[i]+"</td>");
				}
				out.print("</tr>");
				out.print("<tr>");
				out.print("<td>奖励激活</td>");
				for(int i=0;i<wp.getPrizePropertyActive().length;i++){
					out.print("<td>"+wp.getPrizePropertyActive()[i]+"</td>");
				}
				out.print("</tr>");
				out.print("</table>");
				/* /* out.print("propertyValue:" + Arrays.toString(wp.getPropertyValue()) + "<br>");
				out.print("basicPropertyValue:" + Arrays.toString(wp.getBasicPropertyValue()) + "<br>");
				out.print("basicPropertyActive:" + Arrays.toString(wp.getBasicPropertyActive()) + "<br>");
				out.print("prizePropertyValue:" + Arrays.toString(wp.getPrizePropertyValue()) + "<br>");
				out .print("prizePropertyActive:" + Arrays.toString(wp.getPrizePropertyActive()) + "<br>");
				 */
				 out.print("攻,防,血强化次数:" + Arrays.toString(wm.getStrongMul(wp)) + "<br>");
				out.print("<hr>");
				int[][] strongPropValue=wm.handleStrongPropValue(p);
				int[] addPropValue = strongPropValue[0];
				int[] addPropValueAfterStrong = strongPropValue[1];
				int[] addPropType = strongPropValue[2];
				int[] active = strongPropValue[3];
				out.print("强化前属性值:"+Arrays.toString(addPropValue)+ "<br>");
				out.print("强化后属性值:"+Arrays.toString(addPropValueAfterStrong)+ "<br>");
				out.print("属性值类型:"+Arrays.toString(addPropType)+ "<br>");
				out.print("是否已激活:"+Arrays.toString(active)+ "<br>");

				WingAddProp wap = wm.getWingAddPropMap().get(wp.getStar());
				if (wap != null) {
					Map<Integer, int[]> strongProps = wap.getStrongProps();
					for (int strongMul : strongProps.keySet()) {
						out.print("[强化等级:" + strongMul + "][攻防血加成:" + Arrays.toString(strongProps.get(strongMul)) + "]<br>");
					}
					out.print("<hr>");
				} else {
					out.print("没有找到" + wp.getStar() + "星对应的属性值");
				}

				String act = request.getParameter("act");
				if (null != act && !"".equals(act)) {
					if (!TestServerConfigManager.isTestServer()) {
						String pwd = request.getParameter("pwd");
						if (null == pwd && "".equals(pwd)) {
							out.print("不是测试服,请输入密码");
							return;
						} else if (!pwd.equals("chibangmodifypwd")) {
							out.print("密码不正确,请确认清楚");
							return;
						}

					}
					if ("setStar".equals(act)) {
						String star = request.getParameter("star");
						wp.setStar(Integer.valueOf(star));
						wm.getPropValue(p);
						out.print("玩家" + playerName + "的翅膀星级设置为" + star + "<br>");
					} else if ("removeWingFromPlayer".equals(act)) {
						wp.setWingId(-1);
						out.print("后台清除玩家" + playerName + "佩戴的翅膀");
						WingManager.logger.warn("后台清除玩家" + playerName + "佩戴的翅膀");
					} else if ("clearCollection".equals(act)) {
						Map<Integer, Map<Integer, Integer>> wingMap = new HashMap<Integer, Map<Integer, Integer>>();
						wp.setWingMap(wingMap);
						out.print("后台清除玩家" + playerName + "收藏的翅膀");
						WingManager.logger.warn("后台清除玩家" + playerName + "收藏的翅膀");
					}
				}
			}
		}
	}
%>
</head>
<body>
<!-- <form action=""><input type="hidden" name="act" value="read" />
<input type="submit" value="从ddc读取数据到内存" /></form> -->
<form action="">角色名或playerId:<input type="text" name="playerName" value="" />
<input type="submit" value="查询" /></form>
<hr>
<form action=""><input type="hidden" name="act" value="setStar" />
角色名:<input type="text" name="playerName" value="" />翅膀星级:<input
	type="text" name="star" value="" /> 非测试服请输入密码:<input name="pwd"
	type="password" value="" /> <input type="submit" value="设置翅膀星级" /></form>
<hr>
<form action=""><input type="hidden" name="act"
	value="removeWingFromPlayer" /> 角色名:<input type="text"
	name="playerName" value="" /> 非测试服请输入密码:<input name="pwd"
	type="password" value="" /> <input type="submit" value="清除玩家佩戴的翅膀" /></form>
<hr>
<form action=""><input type="hidden" name="act"
	value="clearCollection" /> 角色名:<input type="text" name="playerName"
	value="" />非测试服请输入密码:<input name="pwd" type="password" value="" /> <input
	type="submit" value="清除玩家已收藏列表" /></form>
<hr>
<!--<form action=""><input type="hidden" name="act" value="putStatue" />
非测试服请输入密码:<input name="pwd" type="password" value="" /> <input
	type="submit" value="摆放天尊" /></form>
<hr>

<form action=""><input type="hidden" name="act"
	value="clearLastElectors" />非测试服请输入密码:<input name="pwd"
	type="password" value="" /> <input type="submit" value="清除上期投票榜数据" /></form>
<hr>
<form action=""><input type="hidden" name="act"
	value="showWorshipAward" /><input type="hidden" name="pwd"
	value="xianzunmodifypwd" /> <input type="submit" value="查看膜拜奖励" /></form>-->

</body>
</html>