<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.xuanzhi.tools.text.StringUtil"%><%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.MonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager.MonsterTempalte"%>
<%@page import="com.fy.engineserver.activity.flushmonster.FlushMonsterInGuoQingManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人物所持物品</title>
<%FlushMonsterInGuoQingManager fmigqm = FlushMonsterInGuoQingManager.getInstance();
fmigqm.开始时间 = 15;
fmigqm.刷新时间 = 10000;
MonsterManager mm = MemoryMonsterManager.getMonsterManager();
//int[] ids = new int[]{20010349,20010350,20010351,20010352,20010353,20010354,20010355,20010356,20010357,20010358,20010359,20010360};
//if(false){
//	for(int id: ids){
//		MonsterTempalte mt = ((MemoryMonsterManager)mm).getMonsterTempalteByCategoryId(id);
//		mt.monster.setMaxHPA(mt.monster.getMaxHPA()*60);
//		if(mt.monster.getMaxHPA() <= 0){
//			mt.monster.setMaxHPA(100000000);
//		}
//		out.println(id+":"+mt.monster.getMaxHPA());
//	}
//}
String[] strs = new String[]{"杏花村仙囊(白)","杏花村仙囊(绿)","屠苏酒仙囊(白)","屠苏酒仙囊(绿)","屠魔帖●降魔仙囊(白)","屠魔帖●逍遥仙囊(白)","屠魔帖●霸者仙囊(白)","屠魔帖●朱雀仙囊(白)","屠魔帖●水晶仙囊(白)","屠魔帖●倚天仙囊(白)","屠魔帖●青虹仙囊(白)","屠魔帖●降魔仙囊(绿)","屠魔帖●逍遥仙囊(绿)","屠魔帖●霸者仙囊(绿)","屠魔帖●朱雀仙囊(绿)","屠魔帖●水晶仙囊(绿)","屠魔帖●倚天仙囊(绿)","屠魔帖●青虹仙囊(绿)"};
String req = request.getParameter("time");
String flush = request.getParameter("flush");
if(req != null){
	fmigqm.开始时间 = Integer.parseInt(req);
}
if(flush != null){
	fmigqm.刷新时间 = Integer.parseInt(flush);
}

ArticleManager am = ArticleManager.getInstance();
for(String str : strs){
	PackageProps pp = (PackageProps)am.getArticle(str);
	pp.setBindStyle((byte)0);
	pp.setOpenBindType((byte)1);
	out.println(pp.getName()+pp.getBindStyle()+pp.getOpenBindType()+"<br/>");
}
 %>
<body>
<form>
怪物攻城开始时间:<input name="time" value="<%=fmigqm.开始时间 %>">
怪物攻城刷新时间:<input name="flush" value="<%=fmigqm.刷新时间 %>">
<input type="submit" value="提交">
</form>
</body>
</html>
