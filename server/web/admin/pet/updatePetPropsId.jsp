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
<title>设置宠物的道具id</title>
</head>
<body>
<br><br>

<br>



		<%
	
		String stId = request.getParameter("petId");
		String ppIds = request.getParameter("ppIds");
		String ownerIds = request.getParameter("ownerIds");
		if(stId == null || stId.equals("") || ppIds == null || ownerIds == null ){
			
			%>
			<form action="">
					宠物id:<input type="text" name="petId"></input>	<br/>
					道具id:<input type="text" name="ppIds"></input>	<br/>
					ownerId:<input type="text" name="ownerIds"></input>	<br/>
					<input type="submit" value="sumit"></input>
			</form>
			
			<%
		}else{
			
			long id = Long.parseLong(stId.trim());
			PetManager pm = PetManager.getInstance();
			Pet pet = pm.getPet(id);
			if(pet != null){
				
				PetManager.logger.error("[后台设置宠物道具id] [前] [ownerId:"+pet.getOwnerId()+"] [propsId:"+pet.getPetPropsId()+"]");
				pet.setPetPropsId(Long.parseLong(ppIds.trim()));
				pet.setOwnerId(Long.parseLong(ownerIds.trim()));
				PetManager.logger.error("[后台设置宠物道具id] [后] [ownerId:"+pet.getOwnerId()+"] [propsId:"+pet.getPetPropsId()+"]");
			}else{
				out.print("没有这个宠物"+id);
			}
		}
	
	%>
</body>
</html>
