<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.props.BookProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.AvataProps"%>
<%@page import="com.fy.engineserver.sprite.monster.FlopSet"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Props"%>
<%@page import="com.fy.engineserver.newtask.prizes.TaskPrizeOfArticle"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.prizes.TaskPrize"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<title>比较怪物等级掉落不存在</title>
</head>
<body>
<form method="post" action="?isstart=true"> 
	<%!
		public int getIndex(int level1){
			return (level1-1)/20;
		}
	%>

	<%
		MemoryMonsterManager mm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();	
		LinkedHashMap<int[], FlopSet[]> map = mm.getLevel2FlopListMap();
		Iterator<int[]> it = map.keySet().iterator();
		int count = 0;
		int articleLeveluseLimit = 0;
		int propscount = 0;
		int propssucccount = 0;
		int equcount = 0;
		int equsucccount = 0;
		int 最低级别 = 0;
		int jiu = 0;
		int playerskill = 0;
		int horse = 0;
		while(it.hasNext()){
			int[] levels = (int[])it.next();
			FlopSet[] fss = null;
			try{
				fss = (FlopSet[])map.get(levels);
			}catch(Exception e){
				e.printStackTrace();
				out.print("异常："+levels+"=============================="+e);
			}
			
			最低级别 = levels[0];
			if(fss!=null && fss.length>0){
				for(FlopSet fs : fss){
					String names [] = fs.getArticles();
					if(names!=null && names.length>0){
						for(String name:names){
							Article a = ArticleManager.getInstance().getArticle(name);
							if(a!=null){
								count++;
								articleLeveluseLimit = a.getArticleLevel();
								if(a.get物品二级分类().equals(Translate.酒)){
									jiu++;
									continue;
								}
								if(a instanceof BookProps){
									playerskill++;
									continue;
								}
								
								if(a instanceof Props){
									//玩家等级限制
// 									articleLeveluseLimit = p.getLevelLimit();
									
									if(getIndex(articleLeveluseLimit)==getIndex(最低级别)){
										propssucccount++;
									}else{
										propscount++;
										out.print("<font color='red'>《有错误道具-除了技能书》</font>--掉落物品："+name+"--物品最低级别："+最低级别+"--(道具)物品本身等级："+articleLeveluseLimit+"<br>");
									}
									
								}else if(a instanceof Equipment){
									Equipment e = (Equipment)a;
// 									articleLeveluseLimit = e.getPlayerLevelLimit();
									if (e.getEquipmentType() > EquipmentColumn.EQUIPMENT_TYPE_FOR_PLAYER) {
										horse++;
										continue;
									}
									if(getIndex(articleLeveluseLimit)==getIndex(最低级别)){
										equsucccount++;
// 										out.print("<font color='green'>《么错误装备》</font>--掉落物品："+name+"--物品最低级别："+最低级别+"--玩家等级限制："+articleLeveluseLimit+"<br>");
									}else{
										out.print("<font color='red'>《有错误装备-除了坐骑装备》</font>--掉落物品："+name+"--物品最低级别："+最低级别+"--(装备)装备本身等级"+articleLeveluseLimit+"<br>");
										equcount++;
									}
								}	
								
							}else{
								out.print("请注意，物品不存在！物品名：------<font color = 'red'>"+name+"</font><br>");
							}
						}
					}
				}	
			}
		}
		out.print("*****************************************************<br>");
		out.print("总数："+count+"<br>");
		out.print("酒的数量："+jiu+"<br>");
		out.print("坐骑装备的数量："+horse+"<br>");
		out.print("技能书类的数量："+jiu+"<br>");
		out.print("道具错误总数："+propscount+"<br>");
		out.print("道具正确总数："+propssucccount+"<br>");
		out.print("装备错误总数："+equcount+"<br>");
		out.print("装备正确总数："+equsucccount+"<br>");
		out.print("*****************************************************");
	%>
</form>
</body>
</html>
