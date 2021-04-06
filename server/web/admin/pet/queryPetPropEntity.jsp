<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.*,
	com.xuanzhi.tools.text.*,
	com.fy.engineserver.datasource.article.manager.*,
	com.fy.engineserver.datasource.article.data.props.*,
	com.fy.engineserver.datasource.article.data.entity.*,
	com.fy.engineserver.util.*,
	com.fy.engineserver.sprite.*,
	com.fy.engineserver.sprite.pet.*"

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<style type="text/css">

</style>
<script type="text/javascript">

</script>
</head>
<body>
<br><br>
<h2>根据id查询宠物道具实体</h2>
<br>

	<%
	
		String stId = request.getParameter("entityId");
		if(stId == null || stId.equals("")){
			
			%>
			<form action="">
					输入宠物道具实体id:<input type="text" name="entityId"></input>	<br/>
					<input type="submit" value="sumit"></input>
			</form>
			
			
			<%
		}else{
			
			long id = Long.parseLong(stId.trim());
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleEntity ae =  aem.getEntity(id);
			if(ae != null){
				if(ae instanceof PetPropsEntity){
					PetPropsEntity ppe = (PetPropsEntity)ae;
					out.print("PetPropsEntity 宠物id:"+ppe.getPetId());
				}else if(ae instanceof PetEggPropsEntity){
					PetEggPropsEntity ppp = (PetEggPropsEntity)ae;
					out.print("PetEggPropsEntity 宠物id:"+ppp.getPetId());
				}else{
					out.print("未知类型"+ae.getClass());
				}
			}else{
				out.print("没有这个实体");
			}
			
			
			
			
		}
	
	%>


</body>
</html>
