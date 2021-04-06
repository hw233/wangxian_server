<%@page import="com.fy.engineserver.sprite.monster.FlopSet"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
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
	String [] monsterIds = {"火之新春年兽"};
	MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
	LinkedHashMap<String, Integer[]> monster2FlopListProbabilityMap = mmm.getMonster2FlopListProbabilityMap();
	for(int i=0;i<monsterIds.length;i++){
		String name = monsterIds[i];
		Monster boss = mmm.getMonster(name);
		if(boss==null){
			out.print("怪物不存在:"+name+"<br>");
			continue;
		}
		
		out.print("[修改之前] [怪物："+name+"] [等级"+boss.getLevel()+"]<br>");
		boss.setLevel(85);
		out.print("[修改之后] [怪物："+name+"] [等级"+boss.getLevel()+"]<br>");
		/**
		FlopSet[] fsList = boss.getFlopSets();
		Integer[] fsProbabilitis = monster2FlopListProbabilityMap.get(name);
		Integer newfsProbabilitis [] = new Integer[fsProbabilitis.length]; 
		System.arraycopy(fsProbabilitis,0,newfsProbabilitis,0,fsProbabilitis.length-1);
		newfsProbabilitis[fsProbabilitis.length-1] = 0;
		monster2FlopListProbabilityMap.put(name,newfsProbabilitis);
		fsProbabilitis = monster2FlopListProbabilityMap.get(name);
		boss.setFsProbabilitis(fsProbabilitis);
		out.print("[修改之后] [怪物："+name+"] ["+Arrays.toString(fsProbabilitis)+"]<br>");
		/**if(fsList!=null && fsList.length>0){
			for(FlopSet flop:fsList){
				if(flop!=null && flop.getArticles() != null){
					for(String aname : flop.getArticles()){
						if(aname.equals("疾风刺客蛋") || aname.equals("猪猪侠蛋")){
							out.print("[设置怪物掉落] [怪物："+name+"] [掉落数量:"+fsList.length+"] [type:"+flop.getFlopType()+"] ["+flop.getDropTimeLimitType()+"] [color:"+flop.getColor()+"] [articles:"+(Arrays.toString(flop.getArticles()))+"] ["+Arrays.toString(flop.getFloprates())+"] [flopFormat："+flop.getFlopFormat()+"]<br>");
							out.print("<hr>");
						}
					}
				}
			}
		}else{
			out.print("[怪物掉落集合为空] ["+name+"]");
		}*/
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改怪物等级</title>
</head>
<body>

</body>

<%@page import="java.util.Arrays"%>
<%@page import="java.util.LinkedHashMap"%></html>