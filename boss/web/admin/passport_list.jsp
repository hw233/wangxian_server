<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="header.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>通行证列表</title>
<link rel="stylesheet" href="../css/style.css"/>
<script language="JavaScript">
<!--
function subcheck() {
	f1.submit();
}
function del(id, name) {
	if(window.confirm("你是否确定要删除此游戏:"+name)) {
		window.location.replace('passport_delete.jsp?id=' + id);
	}
}
function change(id,count){
   window.location.replace('passport_update.jsp?id='+id+'&count='+count);
}
function jump(count){
   window.location.replace('passport_list.jsp?count='+count);
}

function overTag(tag){
	tag.style.color = "red";	
	tag.style.backgroundColor = "gray";
}
			
function outTag(tag){
	tag.style.color = "black";
	tag.style.backgroundColor = "white";
}
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>通行证列表</h1>
	<%
	int pagesize=20; 
	int count=0;
	PassportManager pmanager =PassportManager.getInstance();
	String countstr=request.getParameter("count");
	if(countstr!=null){
	    count=Integer.parseInt(countstr);
	}
// 	List<Passport> passports =pmanager.getPassports(count,pagesize) ;
    
//     int    totalcount=pmanager.getCount();
	  
%> 
<%-- 	<h3 align=left>总的数量是 <%=totalcount %></h3> --%>
<!-- 	<form action="" method=post name=f1> -->
<!-- 	<table id="test1" align="center" width="80%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow"> -->
<%-- 	 <caption><% out.println((count/pagesize+1)+"/"+((totalcount-1)/pagesize+1)); %></caption> --%>
<!-- 	 <tr > -->
<!-- 	  <th align=center width=20%><b>用户名</b></th> -->
<!-- 	  <th align=center width=20%><b>身份证号</b></th> -->
<!-- 	  <th align=center width=20%><b>真实姓名</b></th> -->
<!-- 	  <th align=center width=20%><b>电子邮件</b></th> -->
<!-- 	  <th align=center width=20%><b>操作</b></th> -->
<!-- 	 </tr> -->
<%-- 	 <% for(Passport passport:passports){%> --%>
<!-- 	 <tr onmouseover = "overTag(this);" onmouseout = "outTag(this);"> -->
<!-- 	  <td align="center" width=20%> -->
<%-- 	  	<%=passport.getUsername() %> --%>
<!-- 	  </td> -->
<!-- 	  <td align="left" width=20%> -->
<%-- 	  	<%=passport.getIdcard()%> --%>
<!-- 	  </td> -->
<!-- 	  <td align="left" width=20%> -->
<%-- 	  	<%=passport.getRealname()%> --%>
<!-- 	  </td> -->
<!-- 	  <td align="left" width=20%> -->
<%-- 	  	<%=passport.getEmail()%></td> --%>
<!-- 	  <td align="left" width=20%> -->
<%-- 	  	<a href="#" onclick="del(<%=passport.getId()%>, '<%=passport.getUsername()%>')">删除</a> --%>
<%-- 	  	<a href="#" onclick="change('<%=passport.getId()%>',<%=count %>);">更新</a> --%>
<!-- 	  </td> -->
<!-- 	 </tr> -->
<%-- 	<%}%> --%>
    </table>
    <input type=button name=bt2 value="注册通行证" onclick="location.replace('passport_register.jsp')"/>

	</form>
</body>
</html>
