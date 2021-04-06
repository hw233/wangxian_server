<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,java.io.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物品一览</title>

<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
td{
text-align:center;
}
.titlecolor{
background-color:#C2CAF5; 
}
#showdiv{
background-color:#DCE0EB;
text-align:left;
margin:0px 0px;
}

#Weapondiv a{text-decoration:none;color:#D59916}
#Weapondiv a:visited{color:#D59916}
#Weapondiv a:hover{color:red}

#Equipmentdiv a{text-decoration:none;color:#D59916}
#Equipmentdiv a:visited{color:#D59916}
#Equipmentdiv a:hover{color:red}

#Expendablediv a{text-decoration:none;color:#D59916}
#Expendablediv a:visited{color:#D59916}
#Expendablediv a:hover{color:red}

#Stonediv a{text-decoration:none;color:#D59916}
#Stonediv a:visited{color:#D59916}
#Stonediv a:hover{color:red}

</style>
<%
String[] colors = new String[]{"白","绿","蓝","紫","橙"};
String[] colorss = new String[]{"white","green","blue","purple","orange"};
String articleName = request.getParameter("articlesName");
StringBuffer sb = new StringBuffer();
ArticleManager am = ArticleManager.getInstance();
if(am != null){
	Equipment[] es = am.getAllEquipments();
	if(es != null){
		for(Equipment e : es){
			if(e != null){
				if(e.getColorType() == 0){
					sb.append(e.getName()+" 白 "+e.getPlayerLevelLimit()+"\n");
				}else if(e.getColorType() == 1){
					sb.append(e.getName()+" 绿 "+e.getPlayerLevelLimit()+"\n");
				}else if(e.getColorType() == 2){
					sb.append(e.getName()+" 蓝 "+e.getPlayerLevelLimit()+"\n");
				}else if(e.getColorType() == 3){
					sb.append(e.getName()+" 紫 "+e.getPlayerLevelLimit()+"\n");
				}else if(e.getColorType() == 4){
					sb.append(e.getName()+" 橙 "+e.getPlayerLevelLimit()+"\n");
				}
			}
		}
	}
	Weapon[] weapons = am.getAllWeapons();
	if(weapons != null){
		for(Weapon e : weapons){
			if(e != null){
				if(e.getColorType() == 0){
					sb.append(e.getName()+" 白 "+e.getPlayerLevelLimit()+"\n");
				}else if(e.getColorType() == 1){
					sb.append(e.getName()+" 绿 "+e.getPlayerLevelLimit()+"\n");
				}else if(e.getColorType() == 2){
					sb.append(e.getName()+" 蓝 "+e.getPlayerLevelLimit()+"\n");
				}else if(e.getColorType() == 3){
					sb.append(e.getName()+" 紫 "+e.getPlayerLevelLimit()+"\n");
				}else if(e.getColorType() == 4){
					sb.append(e.getName()+" 橙 "+e.getPlayerLevelLimit()+"\n");
				}
			}
		}
	}
}
File equipmentColor = new File("/home/game/装备颜色级别.txt");
OutputStream os = new FileOutputStream(equipmentColor);
os.write(sb.toString().getBytes());
os.close();
%>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>
<body>

</body>
<script type="text/javascript" src="../js/title.js"></script>
</html>
