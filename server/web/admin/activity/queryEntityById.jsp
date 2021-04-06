<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>根据id查询物品</title>
</head>
<body>


	<%
		String ids = request.getParameter("id");
		if(ids != null && !ids.equals("")){
			long id = Long.parseLong(ids.trim());
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
			if(ae != null){
				Player player = PlayerManager.getInstance().getOnlinePlayers()[0];
				out.print("物品类型:"+ae.getClass()+"<br/>");
				out.print("物品名称"+ae.getArticleName()+"<br/>");
				out.print("物品颜色:"+ae.getColorType()+"<br/>");
				out.print("物品info:"+ae.getInfoShow(player)+"<br/>");
			}else{
				out.print("ae null");
			}
			return;
		}
	
	
	%>


		<form action="">
			物品id:<input type="text" name="id" />
			<input type="submit" value="submit" />
		</form>

</body>
</html>