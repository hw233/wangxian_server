<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	PetEggProps[] allPetEggProps = ArticleManager.getInstance().allPetEggProps;		
	for(PetEggProps egg:allPetEggProps){
		if(egg!=null){
			int oldlevel = egg.getRealTrainLevel();
			if(oldlevel>220){
				egg.setRealTrainLevel(220);
				out.print("[设置宠物蛋携带等级] [宠物蛋:"+egg.getName()+"] [old:"+oldlevel+"] [new:"+egg.getRealTrainLevel()+"]<br>");
			}
		}
	}	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改宠物蛋225携带等级</title>
</head>
<body>

</body>
</html>