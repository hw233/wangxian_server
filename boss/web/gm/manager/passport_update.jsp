<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="../header.jsp"%>
<%@page import="com.fy.boss.authorize.model.*" %>
<%@page import ="com.fy.boss.gm.record.*" %>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<%-- 	<%@include file="../../authority.jsp"%> --%>
	<head>
		<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>更新通行证</title>
		<link rel="stylesheet" href="../css/style.css" />
		<script language="JavaScript">
				<!--
				   function check(){
				      var pwd1 = document.getElementById("pwd1").value;
				      var pwd2 = document.getElementById("pwd2").value;
				      if(pwd1&&pwd2&&pwd1==pwd2){
				        document.getElementById("f1").submit();
				      }else{
				        alert("请输入正确的密码");
				      }
				   
				   }
				-->
		</script>
	</head>
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
		<h1>
			更新通行证
		</h1>
		<%
			try {
				String mid = request.getParameter("mid");
				out
						.print("<input type='button' value='刷新' onclick='window.location.replace(\"passport_update.jsp?b=32");
				if (request.getParameter("id") != null
						&& !"".equals(request.getParameter("id")))
					out.print("&id=" + request.getParameter("id"));

				out.print("\")' />");
				Passport passport = null;
				//ActionManager amanager = ActionManager.getInstance();
				String count = request.getParameter("count");
				if (count == null)
					count = "0";
				PassportManager pmanager = PassportManager.getInstance();
				String idstr = request.getParameter("id");
				passport = pmanager.getPassport(Long.parseLong(idstr));
				String a = request.getParameter("a");
				if (a != null) {
					String pwd1 = request.getParameter("pwd1");
					String pwd2 = request.getParameter("pwd2");
					if (pwd1 != null && pwd2 != null&&pwd1.length()>5 && pwd1.equals(pwd2)) {
						//passport.setPassWd(pwd1);
						pmanager.changeUserPassword(passport.getId(), pwd1);
						//amanager.save(session.getAttribute("username")
						//		.toString(), " 为一玩家更新了游戏密码: "
						//		+ passport.getUserName());
						out.println("修改成功");
					} else {
						out.print("修改失败！");
					}
					return;
				}
		%>
		<form action="passport_update.jsp?a=111&id=<%=passport.getId()%>"
			id='f1' method="post">
			<table id="test1" align="center" width="70%" cellpadding="0"
				cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr>
					<th align=left width=20%>
						<b>用户名</b>
						<input type="hidden" name="count" value="<%=count%>" />
					</th>
					<td align="left" class="top"><%=passport.getUserName()%><input
							type="hidden" name="username" value="<%=passport.getUserName()%>" />
					</td>
				</tr>
				<tr>
					<th align=center width=20%>
						<b>密码</b>
					</th>
					<td align="left">
						<input type='hidden' value='<%=mid%>' name='mid' id='mid' />
						<input type="password" id="pwd1" name="pwd1" value="" />
					</td>
				</tr>
				<tr>
					<th align=center width=20%>
						<b>再输入密码</b>
					</th>
					<td align="left">
						<input type="password" id="pwd2" name="pwd2" value="" />
					</td>
				</tr>
				
				<tr>
					<td colspan="2" align=center>
						<input type="button" onclick="check()" value="确定" />
						|
						<input type="button"
							onclick="window.location.replace('modifyPassport.jsp?mid=<%=mid%><%if (passport != null)
					out.print("&username=" + passport.getUserName());%>')"
							value="返回查询页面" />
					</td>
				</tr>
			</table>
		</form>
		<%
			} catch (Exception e) {
				out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
