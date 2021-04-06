<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkillManager"%>
<%@page import="com.fy.engineserver.newBillboard.date.pet.PetRankData"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.newBillboard.date.pet.PetALLBillboard"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkill"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.*"%>
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
	String action = request.getParameter("action");
%>
<h2>新版宠物后台</h2>
<%
	Pet2Manager mgr2 = Pet2Manager.getInst();
if("gradeConf".equals(action)){
	out.print("进阶各阶段配置");
out.println("<table border='1'><tr><td>宠物等级</td><td>可进阶</td><td>道具</td><td>中文</td><td>个数</td><td>放大</td><td>力量</td><td>身法</td><td>灵力</td><td>耐力</td><td>定力</td><td>分配点数</td></tr>");
	for(PetGrade pg : PetGrade.levels){
		out.print("<tr>");
		out.print("<td>");	out.print(pg.lvMin);					out.print("</td>");
		out.print("<td>");	out.print(pg.grade);					out.print("</td>");
		out.print("<td>");	out.print(pg.itemName);					out.print("</td>");
		out.print("<td>");	out.print(pg.itemChinese);					out.print("</td>");
		out.print("<td>");	out.print(pg.itemCnt);					out.print("</td>");
		out.print("<td>");	out.print(pg.scale);					out.print("</td>");
		out.print("<td>");	out.print(pg.liLiang);					out.print("</td>");
		out.print("<td>");	out.print(pg.shenFa);					out.print("</td>");
		out.print("<td>");	out.print(pg.linLi);					out.print("</td>");
		out.print("<td>");	out.print(pg.naiLi);					out.print("</td>");
		out.print("<td>");	out.print(pg.dingLi);					out.print("</td>");
		out.print("<td>");	out.print(pg.point);					out.print("</td>");
		out.print("</tr>");
	}
out.print("</table>");
}else if("forceUpdateRank".equals(action)){
	PetALLBillboard.inst.update();
	out.print("ok.");
}else if("rankList".equals(action)){
	out.println("宠物排行:<br/>");
	PetRankData[] arr = PetALLBillboard.data;
	int len = arr == null ? 0 : arr.length;
	for(int i=0; i<len;  i++){
		PetRankData d = arr[i];
		out.println("排名:"+i+" - ");
		out.println("pet id:"+d.petId+" - ");
		out.println("category:"+d.category+" - ");
		out.println("score:"+d.score+"<br/>");
	}
	out.println("完毕:<br/>");
}else if("rankMap".equals(action)){
	out.println("宠物排行MAP:<br/>");
	Iterator<Long> it = PetALLBillboard.petId2rank.keySet().iterator();
	while(it.hasNext()){
		Long key = it.next();
		out.println(key+"->"+PetALLBillboard.petId2rank.get(key)+"<br/>");
	}
	out.println("end");
}else if("gradablePetList".equals(action)){
	out.print("可进阶宠物配置");
	out.println("<table border='1'><tr><td>i</td><td>名称</td><td>统计名称</td>");
	out.println("<td>最大进阶</td>");
	//out.println("<td>基本形象</td><td>4阶形象</td><td>最终形象</td>");
	//out.println("<td>基础技能描述</td><td>天赋技能描述</td><td>获得途径</td>");
	out.println("<td>天生技能ID</td>");
	//out.println("<td>minValues</td><td>maxValues</td>");
	out.println("</tr>");
	int idx = -1;
	for(GradePet gp : PetGrade.petList){
		idx ++;
		String sk = ""+gp.bornSkill[0];
		GenericSkill gs = GenericSkillManager.getInst().maps.get(gp.bornSkill[0]);
		sk += " - " + (gs == null ? "null" : gs.getName())+":"+gp.skDesc[0]+"<br/>";
		
		sk += ""+gp.bornSkill[1];
		gs = GenericSkillManager.getInst().maps.get(gp.bornSkill[1]);
		sk += " - " + (gs == null ? "null" : gs.getName())+":"+gp.skDesc[1];
		
		out.print("<tr>");
		out.print("<td>");	out.print(idx);					out.print("</td>");
		out.print("<td>");	out.print(gp.name+"$");					out.print("</td>");
		out.print("<td>");	out.print(gp.progName+"$");					out.print("</td>");
		out.print("<td>");	out.print(gp.maxGrade);					out.print("</td>");
		//out.print("<td>");	out.print(gp.baseAvatar);					out.print("</td>");
		//out.print("<td>");	out.print(gp.lv4Avatar);					out.print("</td>");
		//out.print("<td>");	out.print(gp.lv7Avatar);					out.print("</td>");
		
		//out.print("<td>");	out.print(gp.jiChuJiNengDesc);					out.print("</td>");
		//out.print("<td>");	out.print(gp.tianFuJiNengDesc);					out.print("</td>");
		//out.print("<td>");	out.print(gp.gainFrom);					out.print("</td>");
		out.print("<td>");	out.print(sk);					out.print("</td>");
		//out.print("<td>");	out.print(Arrays.toString(gp.minValues));					out.print("</td>");
		//out.print("<td>");	out.print(Arrays.toString(gp.maxValues));					out.print("</td>");
		out.print("</tr>");
	}
	out.print("</table>");
}else if("skillBook".equals(action)){
	out.print("所有宠物技能书 --  基础:");
		String names[] = Pet2SkillCalc.jiChuNames;
		int len = names.length;
		out.print(len);	out.println("<br/>");
		for(int i=0; i<len ; i++){
			out.println(names[i]);	out.println("<br/>");
		}
		
		out.println("<br/>");
		
		out.print("所有宠物技能书 --  天赋:");
		names = Pet2SkillCalc.tianFuNames;
		len = names.length;
		out.print(len);	out.println("<br/>");
		for(int i=0; i<len ; i++){
			out.println(names[i]);	out.println("<br/>");
		}

}else if("allPetEgg".equals(action)){
	out.print("allPetEgg");
	out.println("<table border='1'><tr>");
	String[] head = {"i","名字","统计名称"};
	for(String s : head){		out.print("<td>");	out.print(s);		out.print("</td>");	}
	out.print("</tr>");
	PetEggProps[] list = ArticleManager.getInstance().allPetEggProps;
	int len = list.length;
	for(int i=0; i<len; i++){
		PetEggProps pp = list[i];
		out.print("<tr>");
		out.print("<td>");	out.print(i);					out.print("</td>");
		out.print("<td>");	out.print(pp.getName()+"$");					out.print("</td>");
		out.print("<td>");	out.print(pp.getName_stat()+"$");					out.print("</td>");
		out.print("</tr>");
	}
	out.print("</table>");
}else if("allPetProps".equals(action)){
	out.print("allPetProps");
	out.println("<table border='1'><tr>");
	String[] head = {"i","名字","统计名称"};
	for(String s : head){		out.print("<td>");	out.print(s);		out.print("</td>");	}
	out.print("</tr>");
	PetProps[] list = ArticleManager.getInstance().allPetProps;
	int len = list.length;
	out.print("<br/>");
	out.print("PetGrade:"+PetGrade.petList[6].progName+"<br/>");
	out.print("PetProps:"+list[8].getName_stat()+"<br/>");
	out.print("equals:"+PetGrade.petList[6].progName.equals(list[8].getName_stat())+"<br/>");
	for(int i=0; i<len; i++){
		PetProps pp = list[i];
		out.print("<tr>");
		out.print("<td>");	out.print(i);					out.print("</td>");
		out.print("<td>");	out.print(pp.getName()+"$");					out.print("</td>");
		out.print("<td>");	out.print(pp.getName_stat()+"$");					out.print("</td>");
		out.print("</tr>");
	}
	out.print("</table>");
}else if("lianHunConf".equals(action)){
	out.print("炼魂配置");
	out.println("<table border='1'><tr>");
	String[] head = {"携带等级","产出经验","升级经验", "对应道具", "道具中文"};
	for(String s : head){		out.print("<td>");	out.print(s);		out.print("</td>");	}
	out.print("</tr>");
	for(LianHunConf c : PetGrade.lianHunMap.values()){
		out.print("<tr>");
		out.print("<td>");	out.print(c.takeLevel);					out.print("</td>");
		out.print("<td>");	out.print(c.dropExp);					out.print("</td>");
		out.print("<td>");	out.print(c.toNextLvExp);					out.print("</td>");
		out.print("<td>");	out.print(c.articleName);					out.print("</td>");
		out.print("<td>");	out.print(c.progArtName);					out.print("</td>");
		out.print("</tr>");
	}
	out.print("</table>");
}else if("tianFuSkill".equals(action)){
	out.print("宠物天赋技能-按性格-初级");
	String[] head = {"ID","名称","图标",  "BUFF"};
	for(List<GenericSkill> list: Pet2SkillCalc.skByXingGe){
		out.println("<table border='1'><tr>");
		for(String s : head){		out.print("<td>");	out.print(s);		out.print("</td>");	}
		out.print("</tr>");
		for(GenericSkill gp : list){
			out.print("<tr>");
			out.print("<td>");	out.print(gp.getId());					out.print("</td>");
			out.print("<td>");	out.print(gp.getName());					out.print("</td>");
			out.print("<td>");	out.print(gp.getIconId());					out.print("</td>");
			out.print("<td>");	out.print(gp.buff == null ? 
					"null by buff" : 
						(gp.buff.attName));					out.print("</td>");
			out.print("</tr>");
		}
		out.print("</table>");
	}
}else if("skillConf".equals(action)){
	out.print("宠物天生技能配置");
	out.println("<table border='1'><tr>");
	String[] head = {"ID","名称","图标",  "BUFF"};
	for(String s : head){		out.print("<td>");	out.print(s);		out.print("</td>");	}
	out.print("</tr>");
	for(GenericSkill gp : Pet2SkillCalc.bornSkillArr){
		out.print("<tr>");
		out.print("<td>");	out.print(gp.getId());					out.print("</td>");
		out.print("<td>");	out.print(gp.getName());					out.print("</td>");
		out.print("<td>");	out.print(gp.getIconId());					out.print("</td>");
		out.print("<td>");	out.print(gp.buff == null ? 
				"null by buff" : 
					(gp.buff.attName));					out.print("</td>");
		out.print("</tr>");
	}
	out.print("</table>");
	out.print("<br/>");
	
	out.print("宠物天赋技能配置");
	out.println("<table border='1'><tr>");
	head = new String[]{"ID","名称","图标"};
	for(String s : head){		out.print("<td>");	out.print(s);		out.print("</td>");	}
	out.print("</tr>");
	for(GenericSkill gp : Pet2SkillCalc.tianFuSkillArr){
		out.print("<tr>");
		out.print("<td>");	out.print(gp.getId());					out.print("</td>");
		out.print("<td>");	out.print(gp.getName());					out.print("</td>");
		out.print("<td>");	out.print(gp.getIconId());					out.print("</td>");
		out.print("</tr>");
	}
	out.print("</table>");
}else if("takeSkillTaket".equals(action)){
	out.print("技能抽取符配置");
	out.println("<table border='1'><tr><td>名称</td><td>统计名称</td><td>成功率</td><td>可抽取最大技能等级</td></tr>");
	for(TakePetSkillConf c : PetGrade.takePetSkillConf){
		out.print("<tr>");
		out.print("<td>");	out.print(c.name);					out.print("</td>");
		out.print("<td>");	out.print(c.progName);					out.print("</td>");
		out.print("<td>");	out.print(c.succRate);					out.print("</td>");
		out.print("<td>");	out.print(c.maxLvAllow);					out.print("</td>");
		out.print("</tr>");
	}
	out.print("</table>");
}else if("translation".equals(action)){
	for(String key : com.fy.engineserver.sprite.pet2.PetGrade.translation.keySet()){
		out.print(key +" ---> " + PetGrade.translation.get(key));
		out.print("<br/>");
	}
}else if("testChongBai".equals(action)){
	long petId = 1101000000000037889L;
	long playerId = 1101000000000006145L;
	Player player = PlayerManager.getInstance().getPlayer(playerId);
	PET_CHONG_BAI_REQ req = new PET_CHONG_BAI_REQ(GameMessageFactory.nextSequnceNum(),petId);
	Pet2Manager.getInst().sendChongBai(null, player, req);
	out.println("已执行");
}else if("testUpSkill".equals(action)){
	long petId = 1101000000000037889L;
	int bookIndex = 0;
	int skillIndex = 0;
	PET_SKILL_UP_REQ req = new PET_SKILL_UP_REQ(GameMessageFactory.nextSequnceNum(),
			petId, bookIndex, 0, skillIndex);
	long playerId = 1101000000000006145L;
	Player player = PlayerManager.getInstance().getPlayer(playerId);
	Pet2Manager.getInst().skillLvUp(player, req);
	out.println("已执行");
}else if("testTakeSkill".equals(action)){
	//
	/* TakePetSkillConf c = PetGrade.takePetSkillConf[0];
	c.name = c.progName = "1级大礼包";
	out.println("临时使用的抽取符是"+c.name); */
	//
	long petId = 1101000000000037889L;
	long playerId = 1101000000000006145L;
	int fuBagIndex = 0;
	int tgtSkillIndex = 0;
	PET_SKILL_TAKE_REQ req = new PET_SKILL_TAKE_REQ(GameMessageFactory.nextSequnceNum(),
			petId, fuBagIndex, tgtSkillIndex);
	Player player = PlayerManager.getInstance().getPlayer(playerId);
	Pet2Manager.getInst().takeSkill(player, req);
	out.println("已执行");
}else if("openWatch".equals(action)){
	Pet2Manager.watch = true;
	out.println("已执行");
}else if("testJinJie".equals(action)){
	long petId = 1101000000000380636L;
	long playerId = 1101000000000006145L;
	Player player = PlayerManager.getInstance().getPlayer(playerId);
	if(player == null){
		out.println("玩家没有找到，id"+playerId);
	}else{
		//
		PET_JIN_JIE_REQ req = new PET_JIN_JIE_REQ(GameMessageFactory.nextSequnceNum(), petId);
		Pet2Manager.getInst().jinJie(player, req,false);
		out.println("已执行");
	}
}else{
	out.println("未知的action"+action);
}
%>
</BODY></html>
