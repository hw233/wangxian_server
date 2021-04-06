<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.xuanzhi.tools.text.StringUtil"%><%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.MonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager.MonsterTempalte"%>
<%@page import="com.fy.engineserver.activity.flushmonster.FlushMonsterInGuoQingManager"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.sprite.monster.FlopSet"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人物所持物品</title>
<%
MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
int[] a = new int[]{10010056,10010376,210007,210008,240003,240004,10010372,10010375,10010373,10010374};
for(int i = 0; i < a.length; i++){
	MonsterTempalte mt = mmm.getMonsterTempalteByCategoryId(a[i]);
	mt.monster.setFsList(new FlopSet[0]);
	mt.monster.setFsProbabilitis(new Integer[0]);
	out.println(mt.monster.getName()+"掉落刷新成功");
}

 %>
<body>

</body>
</html>
