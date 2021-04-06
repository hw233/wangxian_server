<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.sprite.monster.FlopSet"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.LevelRandomPackage"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Props"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.CareerPackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<title>比较物品不存在</title>
</head>
<body>
<form method="post" action="?isstart=true"> 
	<%
		//宝箱类道具,,随机宝箱类道具
		ArticleManager am = ArticleManager.getInstance();
// 		RandomPackageProps[] allRandomPackageProps = am.getAllRandomPackageProps();
// 		int errorCount = 0;
// 		int rightCount = 0;
// 		for(RandomPackageProps props:allRandomPackageProps){
// 			Article a = am.getArticle(props.getName());
// 			if(a==null){
// 				out.print("<font color='red'>《随机宝箱类道具物品不存在》</font>--物品："+props.getName()+"<br>");
// 				errorCount++;
// 			}else{
// 				rightCount++;
// 			}
// 			ArticleProperty[] apps = props.getApps();
// 			if(apps!=null){
// 				if(apps.length>0){
// 					for(ArticleProperty ap :apps){
// 						Article aa = am.getArticle(ap.getArticleName());
// 						if(aa==null){
// 							out.print("随机宝箱里面物品开出来的东西有问题宝箱名："+props.getName()+"---物品名："+ap.getArticleName() + "<BR/>");
// 						}
// 					}
// 				}else{
// 					out.print("随机宝箱里面物品开出来的东西是0--宝箱名："+props.getName());
// 				}
// 			}
// 		}	
// 		out.print("=====随机宝箱类道具,检查完毕。总数："+(rightCount+errorCount)+"--错误的数量："+errorCount+"--正确的数量："+rightCount+"=====<br>");
		
// 		PackageProps[] allPackageProps = am.getAllPackageProps();
// 		errorCount = 0;
// 		rightCount = 0;
// 		for(PackageProps props:allPackageProps){
// 			Article a = am.getArticle(props.getName());
// 			if(a==null){
// 				out.print("<font color='red'>《宝箱类道具物品不存在》</font>--物品："+props.getName()+"<br>");
// 				errorCount++;
// 			}else{
// 				rightCount++;
// 			}
// 			ArticleProperty[] articleNames = props.getArticleNames();
// 			if(articleNames!=null){
// 				if(articleNames.length>0){
// 					for(ArticleProperty ap :articleNames){
// 						Article aa = am.getArticle(ap.getArticleName());
// 						if(aa==null){
// 							out.print("宝箱类道具里面物品开出来的东西有问题宝箱名："+props.getName()+"---物品名："+ap.getArticleName()+"<br>");
// 						}
// 					}
// 				}else{
// 					out.print("宝箱类道具里面物品开出来的东西是0--宝箱名："+props.getName()+"<br>");
// 				}
// 			}
			
// 		}	
		
// 		Article[] allArticles = am.getAllArticles();
// 		for(Article aa:allArticles){
// 			Article a = am.getArticle(aa.getName());
// 			if(a==null){
// 				out.print("全部物品,不存在物品名："+aa.getName()+"<br>");
// 			}
// 		}
		
		
// 		Article[] allArticles = am.getAllArticles();
// 		for(Article aa:allArticles){
// 			Article a = am.getArticle(aa.getName());
// 			if(a==null){
// 				out.print("全部物品,不存在物品名："+aa.getName()+"<br>");
// 			}else{
// 				if(a instanceof Props){
// 					Props p = (Props)a;
// 					if(p instanceof LevelRandomPackage){
// 						out.print(p.getName()+"<br>");
// 					}
// 				}
// 			}
// 		}
// 		out.print("<hr>");
// 		CareerPackageProps[] allCareerPackageProps = am.getAllCareerPackageProps();
// 		for(CareerPackageProps props:allCareerPackageProps){
// 			ArticleProperty[][]articleNames = props.getArticleNames();
// 			int career [] = {0,1,2,3};
// 			//职业装备职业是否符合
// 			for(int i=0;i<career.length;i++){
// 				ArticleProperty[] ap1 = articleNames[i];
// 				for (ArticleProperty ap : ap1) {
// 					Article aa = am.getArticle(ap.getArticleName());
// 					if(aa!=null){
// 						if(aa instanceof Equipment){
// 							Equipment q = (Equipment)aa;
// 							out.print(q.getName()+"--"+q.getCareerLimit()+"--"+(i+1)+"<br>");
// 							if(q.getCareerLimit()-1!=i){
// // 								list.add(new SendHtmlToMail(titles, new String[] { "职业宝箱类", props.getName(), "职业宝箱里装备<font color=red>[" + ap.getArticleName() + "]</font>职业不符" }));
// 							}
// 						}
// 					}
// 				}
// 			}
// 		}
		
// 		out.print("=====宝箱类道具,检查完毕。总数："+(rightCount+errorCount)+"--错误的数量："+errorCount+"--正确的数量："+rightCount+"=====");

		
		MemoryMonsterManager sm = (MemoryMonsterManager) MemoryMonsterManager.getMonsterManager();
		LinkedHashMap<int[], FlopSet[]> level2FlopListMap = sm.getLevel2FlopListMap();
		Iterator<int[]> it = level2FlopListMap.keySet().iterator();
		while(it.hasNext()){
			int[] levels = (int[])it.next();
			int 配置的级别限制 = 0;
			if(levels.length>0){
				配置的级别限制 = levels[0];
			}
			for(int i=0;i<level2FlopListMap.get(levels).length;i++){
				String articles[] = level2FlopListMap.get(levels)[i].getArticles();
				for(String article : articles){
					Article a = ArticleManager.getInstance().getArticle(article);
					
					if(a==null){
						out.print("等级掉落物品不存在！物品名："+article+"<br>");
						continue;
					}
						
					if(a instanceof Equipment){
						Equipment q = (Equipment)a;
						if(q.getEquipmentType()<10){
							if((q.getPlayerLevelLimit()-1)/20!=(配置的级别限制-1)/20){
								out.print("装备名："+article+"--配置的级别限制:"+配置的级别限制+"--装备基本限制："+q.getPlayerLevelLimit()+"<br>");
							}								
						}else{
							if(q.getPlayerLevelLimit()<110){
								out.print("坐骑装备等级小于110："+article);
							}else{
								if((q.getPlayerLevelLimit()-1)/20!=(配置的级别限制-1)/20){
									out.print("坐骑装备名："+article+"--配置的级别限制:"+配置的级别限制+"--坐骑装备基本限制："+q.getPlayerLevelLimit()+"<br>");
								}
							}
						}
					}
					
					if(a instanceof Weapon){
						Weapon w = (Weapon)a;
						if((w.getPlayerLevelLimit()-1)/20!=(配置的级别限制-1)/20){
							out.print("武器名："+article+"--配置的级别限制:"+配置的级别限制+"--武器基本限制："+w.getPlayerLevelLimit()+"<br>");
						}								
					}
					
					if(a.get物品二级分类().equals("酒")){
						if (配置的级别限制 < 81) {
							if(!article.equals("杏花村")){
								out.print("酒杏花村类级别错误：配置的级别限制:"+配置的级别限制+"--article:"+article);
							}
						} else if (配置的级别限制 < 161) {
							if(!article.equals("屠苏酒")){
								out.print("酒屠苏酒类级别错误：配置的级别限制:"+配置的级别限制+"--article:"+article);
							}
						} else {
							if(!article.equals("妙沁药酒")){
								out.print("酒妙沁药酒类级别错误：配置的级别限制:"+配置的级别限制+"--article:"+article);
							}
						}
					}
				}
			}
		}
		
		
		
	
	%>
</form>
</body>
</html>
