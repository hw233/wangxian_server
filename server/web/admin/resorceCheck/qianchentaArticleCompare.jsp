<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_FlopSet"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_CengInfo"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_TaInfo"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_DaoInfo"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.homestead.cave.resource.PlantCfg"%>
<%@page import="java.util.*"%>
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
				ArrayList<QianCengTa_DaoInfo> daoList = QianCengTa_TaInfo.instance.daoList;
				ArrayList<QianCengTa_CengInfo> cengList = new ArrayList<QianCengTa_CengInfo>();	
				ArrayList<QianCengTa_FlopSet> flopSets  = new ArrayList<QianCengTa_FlopSet>();
				String[] articleNames = new String[]{};
				ArticleManager am = ArticleManager.getInstance();
				int rightcount = 0;
				int errorcount = 0;
				try{
					for(QianCengTa_DaoInfo dd:daoList){
						cengList = dd.getCengList();
						for(QianCengTa_CengInfo ceng:cengList){
							flopSets = ceng.getFlopSets();
							for(QianCengTa_FlopSet flop:flopSets){
								articleNames = flop.getArticleNames();
								for(String name:articleNames){
									Article a = am.getArticle(name);
									if(a!=null){
// 										out.print("第几道："+dd.getDaoIndex()+"第几层："+ceng.getCengIndex()+"--物品：<font color='green'>"+name+"</font><br>");			
										rightcount++;
									}else{
										out.print("第几道："+dd.getDaoIndex()+"第几层："+ceng.getCengIndex()+"--物品：<font color='red'>"+name+"</font><br>");	
										errorcount++;
									}
								}
							}
						}
					}
					out.print("正确数量："+rightcount+"--错误数量："+errorcount);
				}catch(Exception e){
					e.printStackTrace();
					out.print(e);
				}
			}		
		%>
		<input type='submit' value='开始对比'>
	</form>
</body>
</html>
