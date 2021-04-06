<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<%@page import="com.fy.engineserver.sprite.pet2.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.tools.page.PageUtil"%><html>
<head>
</head>
<body>
<%
/*
List<PetsOfPlayer> list = Pet2Manager.getInst().petsOfPlayerBeanEm.query(PetsOfPlayer.class, "pid > ?", new Object[]{1L}, "pid desc", 1, 300);
int idx = -1;
int nullCnt = 0;
for(PetsOfPlayer bean1: list){
	idx++;
	out.println(idx+" -->查询成功 ID="+bean1.pid+"  bean.pets:"+bean1.pets +"$    "+ (bean1.pets==null)+ "$$$<br/>");
	nullCnt += bean1.pets == null ? 1 : 0;
}
out.println("null cnt : "+nullCnt+" sum cnt : "+list.size());
*/
	String action = request.getParameter("action");
	String petId = request.getParameter("petId");
	String playerId = request.getParameter("playerId");
	//
	petId = petId == null ? "" : petId;
	playerId = playerId == null ? "" : playerId;
	
%>
<h2>新版宠物后台</h2>
<form action="pet2.jsp" method="get">
<input type="hidden" name="action" value="queryPetInfo"/>
宠物ID<input type="text" name="petId" value="<%=petId%>"/>
<input type="submit" value="查询进阶信息"/>
</form>
<br/>

<form action="pet2.jsp"  method="get">
<input type="hidden" name="action" value="queryPetsOfPlayer"/>
玩家ID<input type="text" name="playerId" value="<%=playerId%>"/>
<input type="submit" value="查询获得过的宠物"/>
</form>
<br/>

<%
	Pet2Manager mgr2 = Pet2Manager.getInst();
if("queryPetInfo".equals(action)){
	if(petId.isEmpty()){
		%>宠物id是空<%
	}else{
		long id = Long.parseLong(petId);
	}
}else if("queryPetsOfPlayer".equals(action)){
	if(playerId.isEmpty()){
		out.println("玩家id是空");		
	}else{
		long id = Long.parseLong(playerId);
		PetsOfPlayer bean = mgr2.petsOfPlayerBeanEm.find(id);
		if(bean == null){
			out.println("没有找到这个玩家的宠物获得记录");
		}else{
			out.println(""+bean.pets);
		}
	}
}else if("closeWatch".equals(action)){
	Pet2Manager.watch = false;
}else if("openWatch".equals(action)){
	Pet2Manager.watch = true;
}else if("reloadConf".equals(action)){
	PetGrade.levels = PetGrade.load(Pet2Manager.getInst().getFile());
	mgr2.buildAllPetArr();
	Pet2Manager.getInst().loadSkill(Pet2Manager.getInst().getFile());
	out.println("载入成功");
}else if("showBlank".equals(action)){
	
}else{
	out.println("未知的action"+action);
}
%>
<br/>
<a href='pet2info.jsp?action=testJinJie'>测试进阶</a>|
<a href='pet2info.jsp?action=testTakeSkill'>测试抽取技能</a>|
<a href='pet2info.jsp?action=testUpSkill'>测试升级技能</a>|
<a href='pet2info.jsp?action=testChongBai'>测试宠物崇拜</a>|
<a href='pet2info.jsp?action=translation'>翻译及其他配置</a>|
<a href='pet2info.jsp?action=lianHunConf'>炼魂配置</a>|
<%
if(Pet2Manager.watch){
%><a href='pet2info.jsp?action=closeWatch'>关闭详细日志监控</a>|<%}else{ %>
<a href='pet2info.jsp?action=openWatch'>开启详细日志监控</a>
<%} %>
<br/>
配置文件在<%=mgr2.getFile() %><a href="pet2.jsp?action=reloadConf">重新载入</a><br/>
<a href='pet2info.jsp?action=gradeConf'>进阶等级<%
if(com.fy.engineserver.sprite.pet2.PetGrade.levels == null){out.print("null");}
else{out.print(PetGrade.levels.length);}%></a>|
<a href='pet2info.jsp?action=allPetEgg'>allPetEgg</a> |  
<a href='pet2info.jsp?action=allPetProps'>allPetProps</a> |  
<a href='pet2info.jsp?action=gradablePetList'>可进阶宠物个数<%=PetGrade.petList.length%></a> |  
<a href='pet2info.jsp?action=takeSkillTaket'>抽取技能符个数<%=PetGrade.takePetSkillConf.length %></a> | 
<a href='pet2info.jsp?action=rankMap'>排行MAP</a> | 
<a href='pet2info.jsp?action=rankList'>排行列表</a> | 
<a href='pet2info.jsp?action=forceUpdateRank'>重新生成排行</a> | 
<br/>
<a href='pet2info.jsp?action=skillConf'>宠物技能<%=PetGrade.takePetSkillConf.length %></a> | 
<a href='pet2info.jsp?action=skillBook'>技能书</a> | 
<a href='pet2info.jsp?action=tianFuSkill'>天赋技能-按性格</a> | 
所有宠物:<% out.println(Pet2Manager.allPetArr.length);	out.println("<br/>");
for(GradePet gp : Pet2Manager.allPetArr){
	out.print(gp.name); out.println("--");	out.print(gp.progName);  out.println("--"); out.print(gp.icon); out.println("<br/>");
}%>
</BODY></html>
