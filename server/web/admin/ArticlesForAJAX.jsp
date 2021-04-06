<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.manager.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.*"%><%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物品一览</title>
<% 
String articleName = request.getParameter("articleName"); 
if(articleName != null){
 	Gson gson = new Gson();
	ArticleManager am = ArticleManager.getInstance();
	PropsCategory propsCategory[] = am.getAllPropsCategory();
	List<Article> articlesList = new ArrayList<Article>();
	for(Article article : am.getAllArticles()){
	 if(article != null){
		 if(articleName.equals("Weapon")&& article instanceof Weapon){
			 articlesList.add(article);
		 }else if(articleName.equals("Equipment")&& article instanceof Equipment){
			 articlesList.add(article);
		 }else if(articleName.equals("SingleProps")&& article instanceof SingleProps){
			 articlesList.add(article);
		 }else if(articleName.equals("LastingProps")&& article instanceof LastingProps){
			 articlesList.add(article);
		 }else if(articleName.equals("TransferPaper")&& article instanceof TransferPaper){
			 articlesList.add(article);
		 }else if(articleName.equals("InlayArticle")&& article instanceof InlayArticle){
			 articlesList.add(article);
		 }else if(articleName.equals("UpgradeArticle")&& article instanceof UpgradeArticle){
			 articlesList.add(article);
		 }else if(articleName.equals("AiguilleArticle")&& article instanceof AiguilleArticle){
			 articlesList.add(article);
		 }else if(articleName.equals("Articles")&& article instanceof Article){
			 articlesList.add(article);
		 }else if(articleName.equals("Article")&& article instanceof Article && !(article instanceof Weapon)
				 && !(article instanceof Equipment) && !(article instanceof SingleProps) && !(article instanceof LastingProps)
				 && !(article instanceof TransferPaper) && !(article instanceof InlayArticle) && !(article instanceof UpgradeArticle)
				 && !(article instanceof UpgradeArticle) && !(article instanceof AiguilleArticle)){
			 articlesList.add(article);
		 }
	 }
	}
	
%>
</head>
<body>
<%if(articleName.equals("Weapon")){ %>
<table id="weaponList" border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="8">服务器全部武器列表</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>物品类别</td><td>物品名</td><td>物品图片</td><td>物品描述</td><td>最大有效期</td><td>能否重叠</td><td>能否销毁</td><td>绑定方式</td>
</tr>
<%
if(hm != null){
	List<List<Weapon>> weaponLists = new ArrayList<List<Weapon>>();
	List<Weapon> tempWeaponList = new ArrayList<Weapon>();
	for(Article a : articlesList){
		Weapon article = (Weapon)a;
		if(article.getWeaponType() == 1){
			tempWeaponList.add(article);
		}
	}
	weaponLists.add(tempWeaponList);
	tempWeaponList = new ArrayList<Weapon>();
	for(Article a : articlesList){
		Weapon article = (Weapon)a;
		if(article.getWeaponType() == 2){
			tempWeaponList.add(article);
		}
	}
	weaponLists.add(tempWeaponList);
	tempWeaponList = new ArrayList<Weapon>();
	for(Article a : articlesList){
		Weapon article = (Weapon)a;
		if(article.getWeaponType() == 3){
			tempWeaponList.add(article);
		}
	}
	weaponLists.add(tempWeaponList);
	tempWeaponList = new ArrayList<Weapon>();
	for(Article a : articlesList){
		Weapon article = (Weapon)a;
		if(article.getWeaponType() == 4){
			tempWeaponList.add(article);
		}
	}
	weaponLists.add(tempWeaponList);
	tempWeaponList = new ArrayList<Weapon>();
	for(Article a : articlesList){
		Weapon article = (Weapon)a;
		if(article.getWeaponType() == 5){
			tempWeaponList.add(article);
		}
	}
	weaponLists.add(tempWeaponList);
for(List<Weapon> articles : weaponLists){
	for(int j = 0; j < articles.size(); j++){
		Weapon article = articles.get(j);
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String colorStr = "";
		if(article.getColorType()==0){
			colorStr = "白";
		}else if(article.getColorType()==1){
			colorStr = "绿";
		}else if(article.getColorType()==2){
			colorStr = "蓝";
		}else if(article.getColorType()==3){
			colorStr = "紫";
		}else if(article.getColorType()==4){
			colorStr = "橙";
		}
		StringBuffer sb = new StringBuffer();
		String weaponType = "";
		if(article.getWeaponType()==1){
			weaponType = "刀";
		}else if(article.getWeaponType()==2){
			weaponType = "剑";
		}else if(article.getWeaponType()==3){
			weaponType = "棍";
		}else if(article.getWeaponType()==4){
			weaponType = "匕首";
		}else if(article.getWeaponType()==5){
			weaponType = "弓";
		}
		String s = AritcleInfoHelper.generate(article);
		s = s.replaceAll("\\[/color\\]","</font>");
		s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		
		s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
		if(j==0){
		%>
	<tr bgcolor="#FFFFFF" align="center">
	<td rowspan="<%= articles.size()%>"><%=weaponType %></td>
	<td><!-- <input type="button" value="展开" onclick="javascript:expand('<%=article.getName() %>');">&nbsp;&nbsp;--><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');" onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
			
		<%
		}else{
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><!-- <input type="button" value="展开" onclick="javascript:expand('<%=article.getName() %>');">&nbsp;&nbsp;--><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');"  onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
<%
}}}}}
%>
</table>
<%} %>
<%if(articleName.equals("Equipment")){ %>
<table id="equipmentList" border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="8">服务器全部装备列表</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>物品类别</td><td>物品名</td><td>物品图片</td><td>物品描述</td><td>最大有效期</td><td>能否重叠</td><td>能否销毁</td><td>绑定方式</td>
</tr>
<%
if(hm != null){
	List<List<Equipment>> equipmentLists = new ArrayList<List<Equipment>>();
	List<Equipment> tempEquipmentList = new ArrayList<Equipment>();
	for(Article a : articlesList){
		Equipment article = (Equipment)a;
		if(article.getEquipmentType()==1){
			tempEquipmentList.add(article);
		}
	}
	equipmentLists.add(tempEquipmentList);
	tempEquipmentList = new ArrayList<Equipment>();
	for(Article a : articlesList){
		Equipment article = (Equipment)a;
		if(article.getEquipmentType()==2){
			tempEquipmentList.add(article);
		}
	}
	equipmentLists.add(tempEquipmentList);
	tempEquipmentList = new ArrayList<Equipment>();
	for(Article a : articlesList){
		Equipment article = (Equipment)a;
		if(article.getEquipmentType()==3){
			tempEquipmentList.add(article);
		}
	}
	equipmentLists.add(tempEquipmentList);
	tempEquipmentList = new ArrayList<Equipment>();
	for(Article a : articlesList){
		Equipment article = (Equipment)a;
		if(article.getEquipmentType()==4){
			tempEquipmentList.add(article);
		}
	}
	equipmentLists.add(tempEquipmentList);
	tempEquipmentList = new ArrayList<Equipment>();
	for(Article a : articlesList){
		Equipment article = (Equipment)a;
		if(article.getEquipmentType()==5){
			tempEquipmentList.add(article);
		}
	}
	equipmentLists.add(tempEquipmentList);
	tempEquipmentList = new ArrayList<Equipment>();
	for(Article a : articlesList){
		Equipment article = (Equipment)a;
		if(article.getEquipmentType()==6){
			tempEquipmentList.add(article);
		}
	}
	equipmentLists.add(tempEquipmentList);
	tempEquipmentList = new ArrayList<Equipment>();
	for(Article a : articlesList){
		Equipment article = (Equipment)a;
		if(article.getEquipmentType()==7){
			tempEquipmentList.add(article);
		}
	}
	equipmentLists.add(tempEquipmentList);
for(List<Equipment> articles : equipmentLists){
	for(int j= 0; j < articles.size(); j++){
		Equipment article = articles.get(j);
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		
		String colorStr = "";
		if(article.getColorType()==0){
			colorStr = "白";
		}else if(article.getColorType()==1){
			colorStr = "绿";
		}else if(article.getColorType()==2){
			colorStr = "蓝";
		}else if(article.getColorType()==3){
			colorStr = "紫";
		}else if(article.getColorType()==4){
			colorStr = "橙";
		}
		StringBuffer sb = new StringBuffer();
		String equipmentType = "";
		if(article.getEquipmentType()==1){
			equipmentType = "头盔";
		}else if(article.getEquipmentType()==2){
			equipmentType = "护肩";
		}else if(article.getEquipmentType()==3){
			equipmentType = "衣服";
		}else if(article.getEquipmentType()==4){
			equipmentType = "护手";
		}else if(article.getEquipmentType()==5){
			equipmentType = "鞋子";
		}else if(article.getEquipmentType()==6){
			equipmentType = "首饰";
		}else if(article.getEquipmentType()==7){
			equipmentType = "坐骑";
		}
		String s = AritcleInfoHelper.generate(article);
		s = s.replaceAll("\\[/color\\]","</font>");
		s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		
		s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
		if(j==0){
		%>
	<tr bgcolor="#FFFFFF" align="center">
	<td rowspan="<%=articles.size() %>"><%=equipmentType %></td>
	<td><!--<input type="button" value="展开" onclick="javascript:expand('<%=article.getName() %>');">&nbsp;&nbsp;--><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');"  onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
		<%
		}else{
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><!--<input type="button" value="展开" onclick="javascript:expand('<%=article.getName() %>');">&nbsp;&nbsp;--><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');"  onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
<%
}}}}}
%>
</table>
<%} %>
<%if(articleName.equals("SingleProps")){ %>
<table id="singlePropsList" border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="9">服务器简单道具列表</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>物品类别</td><td>物品名</td><td>物品图片</td><td>物品类型</td><td>物品描述</td><td>最大有效期</td><td>能否重叠</td><td>能否销毁</td><td>绑定方式</td>
</tr>
<%
if(hm != null){
	List<List<SingleProps>> singlePropsList = new ArrayList<List<SingleProps>>();
for(Article a : articlesList){
	SingleProps article = (SingleProps)a;
	boolean existFlag = false;
	for(List<SingleProps> iTree : singlePropsList){
		if(article != null && iTree != null && iTree.size()>0 && iTree.get(0) != null && article.getCategoryName().equals(iTree.get(0).getCategoryName())){
			existFlag = true;
			iTree.add(article);
			break;
		}
	}
	if(!existFlag && article != null){
		List<SingleProps> iTree = new ArrayList<SingleProps>();
		iTree.add(article);
		singlePropsList.add(iTree);
	}
}
for(List<SingleProps> singleProps : singlePropsList){
	for(int i = 0; i < singleProps.size(); i++){
		SingleProps article = singleProps.get(i);
		if(article != null){
			String bindStyleStr = "";
			if(article.getBindStyle()==0){
				bindStyleStr = "不绑定";
			}else if(article.getBindStyle()==1){
				bindStyleStr = "装备绑定";
			}else if(article.getBindStyle()==2){
				bindStyleStr = "拾取绑定";
			}
			String propsType = "";
			if(article.getPropsType() == 0){
				propsType = "未知功能";
			}else if(article.getPropsType()==1){
				propsType = "加血";
			}else if(article.getPropsType()==2){
				propsType = "加蓝";
			}else if(article.getPropsType()==3){
				propsType = "加血和蓝";
			}
			String s = AritcleInfoHelper.generate(article);
			s = s.replaceAll("\\[/color\\]","</font>");
			s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
			
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
		if(i==0){
	%>
		<tr bgcolor="#FFFFFF" align="center">
		<td rowspan="<%=singleProps.size() %>"><%=article.getCategoryName() %></td>
		<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
		<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');"  onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
		<td><%= propsType %></td>
		<td><%= article.getDescription() %></td>
		<td><%= article.getMaxValidDays() %></td>
		<td><%= article.isOverlap()?"能":"不能" %></td>
		<td><%= article.isDestroyed()?"能":"不能" %></td>
		<td><%= bindStyleStr %></td>
		</tr>
	<%
	}else{
		%>
		<tr bgcolor="#FFFFFF" align="center">
		<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
		<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');"  onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
		<td><%= propsType %></td>
		<td><%= article.getDescription() %></td>
		<td><%= article.getMaxValidDays() %></td>
		<td><%= article.isOverlap()?"能":"不能" %></td>
		<td><%= article.isDestroyed()?"能":"不能" %></td>
		<td><%= bindStyleStr %></td>
		</tr>
	<%
	}}}}}
%>
</table>
<%} %>
<%if(articleName.equals("LastingProps")){ %>
<table id="lastingPropsList" border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="9">服务器持续性道具列表</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>物品类别</td><td>物品名</td><td>物品图片</td><td>物品类型</td><td>物品描述</td><td>最大有效期</td><td>能否重叠</td><td>能否销毁</td><td>绑定方式</td>
</tr>
<%
if(hm != null){
	List<List<LastingProps>> propsList = new ArrayList<List<LastingProps>>();
	for(Article a : articlesList){
		LastingProps article = (LastingProps)a;
		boolean existFlag = false;
		for(List<LastingProps> iTree : propsList){
			if(article != null && iTree != null && iTree.size()>0 && iTree.get(0) != null && article.getCategoryName().equals(iTree.get(0).getCategoryName())){
				existFlag = true;
				iTree.add(article);
				break;
			}
		}
		if(!existFlag && article != null){
			List<LastingProps> iTree = new ArrayList<LastingProps>();
			iTree.add(article);
			propsList.add(iTree);
		}
	}
	
	for(List<LastingProps> props : propsList){
	for(int i = 0; i < props.size(); i++){
		LastingProps article = props.get(i);
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String propsType = "";
		if(article.getPropsType() == 0){
			propsType = "未知功能";
		}else if(article.getPropsType()==1){
			propsType = "加血";
		}else if(article.getPropsType()==2){
			propsType = "加蓝";
		}else if(article.getPropsType()==3){
			propsType = "加血和蓝";
		}
		String s = AritcleInfoHelper.generate(article);
		s = s.replaceAll("\\[/color\\]","</font>");
		s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		
		s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
		if(i == 0){
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td rowspan="<%=props.size() %>"><%=article.getCategoryName() %></td>
	<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');" onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= propsType %></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
<%}else{
	%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');" onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= propsType %></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
	<%
}
}}}}
%>
</table>
<%} %>
<%if(articleName.equals("TransferPaper")){ %>
<table id="transferPaperList" border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="9">服务器传送符列表</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>物品类别</td><td>物品名</td><td>物品图片</td><td>物品类型</td><td>物品描述</td><td>最大有效期</td><td>能否重叠</td><td>能否销毁</td><td>绑定方式</td>
</tr>
<%
if(hm != null){
	List<List<TransferPaper>> propsList = new ArrayList<List<TransferPaper>>();
	for(Article a : articlesList){
		TransferPaper article = (TransferPaper)a;
		boolean existFlag = false;
		for(List<TransferPaper> iTree : propsList){
			if(article != null && iTree != null && iTree.size()>0 && iTree.get(0) != null && article.getCategoryName().equals(iTree.get(0).getCategoryName())){
				existFlag = true;
				iTree.add(article);
				break;
			}
		}
		if(!existFlag && article != null){
			List<TransferPaper> iTree = new ArrayList<TransferPaper>();
			iTree.add(article);
			propsList.add(iTree);
		}
	}
	for(List<TransferPaper> props : propsList){
	
		for(int i = 0; i < props.size(); i++){
		TransferPaper article = props.get(i);
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String propsType = "";
		if(article.getPropsType() == 0){
			propsType = "未知功能";
		}else if(article.getPropsType()==1){
			propsType = "加血";
		}else if(article.getPropsType()==2){
			propsType = "加蓝";
		}else if(article.getPropsType()==3){
			propsType = "加血和蓝";
		}
		String s = AritcleInfoHelper.generate(article);
		s = s.replaceAll("\\[/color\\]","</font>");
		s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		
		s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
		if(i == 0){
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td rowspan="<%=props.size() %>"><%=article.getCategoryName() %></td>
	<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');" onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= propsType %></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
<%}else{
	%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');" onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= propsType %></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
	<%
}
}}}}
%>
</table>
<%} %>
<%if(articleName.equals("InlayArticle")){ %>
<table id="inlayArticleList" border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="7">服务器镶嵌物品列表</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>物品名</td><td>物品图片</td><td>物品描述</td><td>最大有效期</td><td>能否重叠</td><td>能否销毁</td><td>绑定方式</td>
</tr>
<%
if(hm != null){
	for(Article a : articlesList){
		InlayArticle article = (InlayArticle)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String s = AritcleInfoHelper.generate(article);
		s = s.replaceAll("\\[/color\\]","</font>");
		s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		
		s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');" onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>
<%if(articleName.equals("UpgradeArticle")){ %>
<table id="upgradeArticleList" border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="7">服务器升级物品列表</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>物品名</td><td>物品图片</td><td>物品描述</td><td>最大有效期</td><td>能否重叠</td><td>能否销毁</td><td>绑定方式</td>
</tr>
<%
if(hm != null){
	for(Article a : articlesList){
		UpgradeArticle article = (UpgradeArticle)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String s = AritcleInfoHelper.generate(article);
		s = s.replaceAll("\\[/color\\]","</font>");
		s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		
		s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');" onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>
<%if(articleName.equals("AiguilleArticle")){ %>
<table id="aiguilleArticleList" border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="7">服务器打孔物品列表</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>物品名</td><td>物品图片</td><td>物品描述</td><td>最大有效期</td><td>能否重叠</td><td>能否销毁</td><td>绑定方式</td>
</tr>
<%
if(hm != null){
	for(Article a : articlesList){
		AiguilleArticle article = (AiguilleArticle)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String s = AritcleInfoHelper.generate(article);
		s = s.replaceAll("\\[/color\\]","</font>");
		s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		
		s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');" onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>
<%if(articleName.equals("Article")){ %>

<table id="articleList" border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="7">服务器其他物品列表</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>物品名</td><td>物品图片</td><td>物品描述</td><td>最大有效期</td><td>能否重叠</td><td>能否销毁</td><td>绑定方式</td>
</tr>
<%
if(hm != null){
	for(Article a : articlesList){
		Article article = (Article)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String s = AritcleInfoHelper.generate(article);
		s = s.replaceAll("\\[/color\\]","</font>");
		s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		
		s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');" onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>
<%if(articleName.equals("Articles")){ %>

<table id="articleList" border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="7">服务器全部物品列表</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>物品名</td><td>物品图片</td><td>物品描述</td><td>最大有效期</td><td>能否重叠</td><td>能否销毁</td><td>绑定方式</td>
</tr>
<%
if(hm != null){
	for(Article a : articlesList){
		Article article = (Article)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String s = AritcleInfoHelper.generate(article);
		s = s.replaceAll("\\[/color\\]","</font>");
		s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		
		s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a style="color:#2D20CA" href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" title="<pre><%=s %></pre>" onmouseup="javascript:onclickImage('<%=article.getName() %>');" onmouseover="javascript:document.body.style.cursor='hand';" onmouseout="javascript:document.body.style.cursor='default';"></td>
	<td><%= article.getDescription() %></td>
	<td><%= article.getMaxValidDays() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%= article.isDestroyed()?"能":"不能" %></td>
	<td><%= bindStyleStr %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>
<%} %>

</body>
</html>
