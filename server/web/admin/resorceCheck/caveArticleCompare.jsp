<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.homestead.cave.resource.PlantCfg"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
</script>
</head>
<body>
	<form method="post" action="?isstart=true"> 
		<%
			String act = request.getParameter("isstart");	
			if(act!=null && act.equals("true")){
				FaeryManager fm = FaeryManager.getInstance();
				HashMap<Integer, List<PlantCfg>> plantGradeMap = fm.getPlantGradeMap();
				Iterator it = plantGradeMap.keySet().iterator();
				List<PlantCfg> list = null;
				int count = 0;
				ArticleManager am = ArticleManager.getInstance();
				while(it.hasNext()){
					int level = (Integer)it.next();
					list = plantGradeMap.get(level);
					for(PlantCfg p:list){
						count++;
						try{
							Article a = am.getArticle(p.getOutputName());
							Article a2 = am.getArticle(p.getPlantGroup());
							if(a!=null){
								if(a2!=null){
									out.print("--产出物品名:<font color='green'>"+p.getOutputName()+"</font>--种植物名：<font color='green'>"+p.getPlantGroup()+"</font><br>");
								}else{
									out.print("--产出物品名:<font color='green'>"+p.getOutputName()+"</font>--种植物名：<font color='red'>"+p.getPlantGroup()+"</font><br>");
								}
							}else{
								if(a2!=null){
									out.print("--产出物品名:<font color='red'>"+p.getOutputName()+"</font>--种植物名：<font color='green'>"+p.getPlantGroup()+"</font><br>");
								}else{
									out.print("--产出物品名:<font color='red'>"+p.getOutputName()+"</font>--种植物名：<font color='red'>"+p.getPlantGroup()+"</font><br>");
								}
							}
							
						}catch(Exception e){
							
						}
						
					}
				}
				out.print("读取完毕,数量："+count);
			}		
		%>
		<input type='submit' value='开始对比'>
	</form>
</body>
</html>
