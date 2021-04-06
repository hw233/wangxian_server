<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_addDamage"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_ZengShu"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_JiangDiZhiLiao"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_Silence"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	long time = SealManager.getInstance().seal.getSealCanOpenTime();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	String timestr = sdf.format(time);
	out.print("解开封印最早日期:" + timestr);
	//String servername = GameConstants.getInstance().getServerName();
	//if (servername.equals("国内本地测试")) {
	String act = request.getParameter("act");
	if (null != act && !"".equals(act) && act.equals("cancleprotect")) {
		if (!TestServerConfigManager.isTestServer()) {
			String pwd = request.getParameter("pwd");
			if (null == pwd && "".equals(pwd)) {
				out.print("不是测试服,请输入密码");
				return;
			} else if (!pwd.equals("sealmodifypwd")) {
				out.print("密码不正确,请确认清楚");
				return;
			}

		}
		SealManager.testFalg = true;
		out.print("设置成功");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Date"%>
<%@page
	import="com.fy.engineserver.util.server.TestServerConfigManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>取消封印保护机制</title>
</head>
<body>
<form action=""><input type="hidden" name="act"
	value="cancleprotect" /> 非测试服请输入密码:<input name="pwd" type="password"
	value="" /> <input type="submit" value="取消封印保护" /></form>
</body>
</html>