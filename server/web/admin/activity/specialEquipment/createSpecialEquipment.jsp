<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>

<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><html>


<script type="text/javascript">

	function create1(){

		var obj = document.getElementById("article1");
		var index = obj.selectedIndex;
		var str = obj.options[index].text;
		document.getElementById("articleName").value =str;
		document.create.submit();
		
	}
	
	function create2(){

		var obj = document.getElementById("article2");
		var index = obj.selectedIndex;
		var str = obj.options[index].text;
		document.getElementById("articleName").value =str;
		document.create.submit();
		
	}



</script>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>创建特殊装备</title>
</head>
<body>
	
	
	<%
		String name = request.getParameter("playerName");
		if(name == null ||name.equals("")){
			%>
			
		<form action="" name="">
			用户名：<input type="text" name="playerName" />
			<input type="submit" value="提交"/>
		</form>
		
		<%
		}else{
			
			Equipment[] specials1 = ArticleManager.getInstance().allSpecialEquipments1;
			Equipment[] specials2 = ArticleManager.getInstance().allSpecialEquipments2;
			%>
			
			<h1> 鸿天帝宝 </h1>
	
			<form action="create.jsp" name="create">
				<input type="hidden" name="articleName" id="articleName"/>
					
				<input type="hidden" name="playerName" id="playerName" value="<%=name %>"/>
			</form>
			
			
			<select name="article1" id="article1" style="width:110px" >
				<%for(Article w: specials1){ %>
				<option id="<%=w.getName() %>" ><%=w.getName() %></option>
				<% }%>
			</select>
			<input type="button" value="增加" onclick="create1()">
			
			<h1>伏天古宝 </h1>
			
			<select name="article2" id="article2" style="width:110px" >
				<%for(Article w: specials2){ %>
				<option id="<%=w.getName() %>" ><%=w.getName() %></option>
				<% }%>
			</select>
			<input type="button" value="增加" onclick="create2()">
			
			<%
			
		}
	%>
	
	
</body>
</html>