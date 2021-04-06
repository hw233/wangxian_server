<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.LevelRandomPackage"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.article.concrete.*,com.google.gson.*,com.xuanzhi.tools.cache.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.sprite.*,java.util.*,java.lang.reflect.*,com.google.gson.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改礼包个数</title>
<script type="text/javascript">
function subcheck(sm) {
	document.getElementById("sm").value=sm;
	document.form1.submit();
}
</script>
</head>
<body>
<%
	ArticleManager am = ArticleManager.getInstance();
	Article[] allArticles = am.getAllArticles();
	int num = 0;
	int num2 = 0;
	for(Article a:allArticles){
		if(a.getName().equals("蓝色屠魔贴锦囊")){
			if(a instanceof LevelRandomPackage){
				num++;
				LevelRandomPackage p = (LevelRandomPackage)a;
				out.print("修改前》》》要修改的物品名："+a.getName()+"--开出个数："+p.getOutputNum()+"<br>");	
				p.setOutputNum(1);
				out.print("修改后》》》要修改的物品名："+a.getName()+"--开出个数："+p.getOutputNum()+"<br>");
				out.print("数量："+num);
			}
		}else if(a.getName().equals("贵宾仙囊")){
			if(a instanceof PackageProps){
				num2++;
				PackageProps p = (PackageProps)a;
				out.print("修改前》》》要修改的物品名："+a.getName()+"--是否广播："+p.isNeedSendNotice()+"<br>");	
				p.setNeedSendNotice(false);
				out.print("修改后》》》要修改的物品名："+a.getName()+"--是否广播："+p.isNeedSendNotice()+"<br>");
				out.print("数量："+num);
			}
		}
		
	}

%>
</table>
</div>
</body>
</html>
