<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
											com.xuanzhi.boss.authorize.model.*,com.xuanzhi.tools.text.*,
											com.xuanzhi.boss.client.*"%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="JavaScript">
<!-- 
-->
</script>
</head>
<body>
<% 
String stype = request.getParameter("stype"); 
String message = null; 
Passport pass = null; 
if(stype != null) { 
	BossClientService boss = BossClientService.getInstance(); 
	if(stype.equals("sub1")) { 
		String username = request.getParameter("username"); 
		try { 
			pass = boss.getPassport(username); 
		} catch(Exception e) { 
			e.printStackTrace(); 
			message = e.getMessage(); 
		} 
	} 
	if(stype.equals("sub2")) {
		long id = Long.parseLong(request.getParameter("id"));
		Passport passport = boss.getPassport(id);
		String username = request.getParameter("username"); 
		String passwd = request.getParameter("passwd"); 
		String email = request.getParameter("email"); 
		String idcard = request.getParameter("idcard"); 
		String realname = request.getParameter("realname"); 
		long sex = Long.parseLong(request.getParameter("sex")); 
		String birthday = request.getParameter("birthday"); 
		String mobile = request.getParameter("mobile"); 
		String qq = request.getParameter("qq"); 
		String phone = request.getParameter("phone"); 
		String address = request.getParameter("address"); 
		String postcode = request.getParameter("postcode"); 
		long points = Long.parseLong(request.getParameter("points")); 
		long status = Long.parseLong(request.getParameter("status")); 
		String activegames = request.getParameter("activegames"); 
		String authquestions = request.getParameter("authquestions"); 
		String authanswers = request.getParameter("authanswers"); 
		passport.setUsername(username);
		passport.setPasswd(passwd);
		passport.setEmail(email);
		passport.setIdcard(idcard);
		passport.setRealname(realname);
		passport.setSex(sex);
		passport.setBirthday(DateUtil.parseDate(birthday,"yyyy-MM-dd"));
		passport.setMobile(mobile);
		passport.setQq(qq);
		passport.setAddress(address);
		passport.setPostcode(postcode);
		passport.setPoints(points);
		passport.setStatus(status);
		passport.setActivegames(activegames);
		passport.setAuthquestions(authquestions);
		passport.setAuthanswers(authanswers);
		passport.setPhone(phone);
		try {
			boss.updatePassport(passport);
			message = "更新成功";
		} catch(Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
	}
} 
if(message != null) { 
	out.println(message); 
} 
if(pass != null) { 
%>
<form action="" name=f2>
	账号名:<input type=text name=username size=20 value="<%=pass.getUsername() %>"><br>
	密码:<input type=password name=passwd size=20 value=""><br>
	邮件:<input type=text name=email size=20 value="<%=pass.getEmail() %>"><br>
	身份证:<input type=text name=idcard size=20 value="<%=pass.getIdcard() %>"><br>
	真名:<input type=text name=realname size=20 value="<%=pass.getRealname() %>"><br>
	性别:<select name="sex">
			<option value="<%=Passport.MALE %>" <%if(pass.getSex() == Passport.MALE) out.print("selected");%>>男</option>
			<option value="<%=Passport.FEMALE %>" <%if(pass.getSex() == Passport.FEMALE) out.print("selected");%>>女</option>
		</select><br>
	生日:<input type=text name=birthday size=20 value="<%=DateUtil.formatDate(pass.getBirthday(),"yyyy-MM-dd") %>"><br>
	手机:<input type=text name=mobile size=20 value="<%=pass.getMobile() %>"><br>
	qq:<input type=text name=qq size=20 value="<%=pass.getQq() %>"><br>
	电话:<input type=text name=phone size=20 value="<%=pass.getPhone() %>"><br>
	地址:<input type=text name=address size=20 value="<%=pass.getAddress() %>"><br>
	邮编:<input type=text name=postcode size=20 value="<%=pass.getPostcode() %>"><br>
	积分:<input type=text name=points size=20 value="<%=pass.getPoints() %>"><br>
	状态:<select name="status">
			<option value="<%=Passport.STATUS_NORMAL %>" <%if(pass.getStatus() == Passport.STATUS_NORMAL) out.print("selected");%>>正常</option>
			<option value="<%=Passport.STATUS_TRY %>" <%if(pass.getStatus() == Passport.STATUS_TRY) out.print("selected");%>>试玩</option>
			<option value="<%=Passport.STATUS_PAUSED %>" <%if(pass.getStatus() == Passport.STATUS_PAUSED) out.print("selected");%>">暂停</option>
		</select><br>
	激活的游戏:<input type=text name=activegames size=20 value="<%=pass.getActivegames() %>"><br>
	验证问题:<textarea name=authquestions cols=40 rows=4><%=pass.getAuthquestions() %></textarea><br>
	验证答案:<textarea name=authanswers cols=40 rows=4><%=pass.getAuthanswers() %></textarea><br>
	<input type=hidden name=stype value="sub2"><br>
	<input type=hidden name=id value="<%=pass.getId() %>"><br>
	<input type=submit name=sub1 value=" 提 交 ">
</form>
<%
} else {
 %>
<form action="" name=f1>
	账号名:<input type=text name=username size=20><br>
	<input type=hidden name=stype value="sub1"><br>
	<input type=submit name=sub1 value=" 提 交 ">
</form>
<%} %>
</body>
</html>
