<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.data.articles.ExchangeArticle"%>
<%@page import="com.fy.engineserver.deal.ExchangeDeal"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.deal.service.concrete.*"%>
<%@page import="com.fy.engineserver.deal.service.*"%>
<%@page import="com.xuanzhi.tools.text.*"%>


<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交换寻宝物品</title>
</head>
<body>

	<%
	
		PlayerManager pm = PlayerManager.getInstance();
		Player playerA = pm.getPlayer("ff");
		Player playerB = pm.getPlayer("xiaolong1");
		ExchangeDeal edeal = new ExchangeDeal(playerA,playerB);
		edeal.setAgreedA(true);
		edeal.setAgreedB(true);
		
		long [] idA = {1001000000000991035l};
		long [] idB = {1001000000000989825l};
		
		int[] indexA = {0};
		int[] indexB = {1};
		
		int[] countA = {1};
		int[] countB = {1};
		
		byte[] pakTypeA = {Article.KNAP_任务};
		byte[] pakTypeB = {Article.KNAP_任务};
		
		edeal.setEntityIdsA(idA);
		edeal.setEntityIdsB(idB);
		
		edeal.setIndexesA(indexA);
		edeal.setIndexesB(indexB);
		
		edeal.setCountsA(countA);
		edeal.setCountsB(countB);
		
		edeal.setPackageTypeA(pakTypeA);
		edeal.setPackageTypeB(pakTypeB);
		
		String id = StringUtil.randomIntegerString(8);
		edeal.setId(id);
		DealCenter dc = DefaultDealCenter.getInstance();
		dc.getDealMap().put(edeal.getId(),edeal);
		dc.agreeDeal(edeal,playerA);
	%>
</body>
</html>