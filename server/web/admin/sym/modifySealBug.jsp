<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.seal.data.Seal"%>
<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_addDamage"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_ZengShu"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_JiangDiZhiLiao"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_Silence"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	String servers[] = {"千娇百媚","海纳百川","琼楼金阙","众仙归来","蓬莱仙境","北斗剑圣","三界奇缘","夺魂战神","群龙聚首","器定乾坤"};
	String servername = GameConstants.getInstance().getServerName();	
	boolean istrue = false;
	for(String name : servers){
		if(name.equals(servername)){
			istrue = true;
		}
	}
	if(istrue==false){
		out.print("无权操作！！");
		return;
	}
	Seal seal = SealManager.getInstance().getSeal();
	long oldtime = seal.getSealCanOpenTime();
	seal.setSealCanOpenTime(seal.getSealCanOpenTime() - 30*24*60*60*1000L);
	SealManager.getInstance().saveSeal();
	SealManager.getInstance().closeTimeLength = 180*24*60*60*1000L;
	out.print("[设置成功] ["+servername+"] ["+SealManager.getInstance().isCloseCurrSeal()+"] [老的时间："+TimeTool.formatter.varChar23.format(oldtime)+"] [新的时间："+TimeTool.formatter.varChar23.format(seal.getSealCanOpenTime())+"]");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改封印bug</title>
</head>
<body>

</body>
</html>