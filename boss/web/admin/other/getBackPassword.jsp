<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.fy.boss.game.model.Server" %>
<%@page import="com.fy.boss.gm.record.Recover,com.fy.boss.game.*,com.fy.boss.game.service.*"%>
<%@ include file="gm/header.jsp"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>账号密码问题</title>
<script type="text/javascript">
	function sub(){
		alert(11);
	}
</script>


</head>
<body bgcolor="#FFFFFF" >

	<h1 align="center">账号密码问题</h1>
	<input type='button' value='刷新' onclick='window.location.replace("getBackPassword.jsp")' />
	<form action='' method='post' id='f1'>
	<table align='center' width='80%' ><caption>找回帐号、密码记录</caption>

	<tr><th>称呼：</th><td class='top'><input type='text' value='黄先生' name='name' id='name' /></td>
	<th>联系电话：</th><td class='top'><input type='text' value='18611856263' name='phone' id='phone' /></td></tr>

	<tr><th>游戏账号：</th><td><input type='text' value='60982902' name='username' id='username' /></td>
    <th>服务器:</th><td><select>
    <%
    ServerManager gsm = ServerManager.getInstance();
    List<Server> slist = gsm.getServers();
    for(Server s : slist) {
    %>
   	 <option value=<%=s.getName() %> ><%=s.getName() %></option>
    <%} %>
    </select></td></tr>

	<tr><th>角色名：</th><td><input type='text' value='龙丶炫耀' name='player' id='player' /></td>
	<th>问题类型：</th><td><select id='questype' name='questype'>
	<option value='找回账号'>找回帐号</option>
	<option value='找回密码'>找回密码</option></select></td></tr>


	<tr><th>密保信息：</th><td><select id='servername' name='servername'>
	<option>您手机号后四位</option>
	<option value='您父亲的名字'>您父亲的名字</option>
	<option value='您母亲的名字'>您母亲的名字</option>
	<option value='您最爱人的名字'>您最爱人的名字</option>
	<option value='您小学的名字'>您小学的名字</option>
	<option value='您的出生日期'>您的出生日期</option>
	<option value='您的真实姓名'>您的真实姓名</option>
	</select></td>
	<th>密保答案</th><td><input type='text' value='6263' name='username' id='username' /></td></tr>


	<tr><th>手机型号</th><td><input type='text' value='moto_me525+' name='username' id='username' /></td>
	<th>手机设备号</th><td><input type='text' value='353163054700470' name='username' id='username' /></td></tr>

	<tr><th>说明您需要找帐号、密码的原因</th><td colspan='3'><textarea id='question' name='question'  style='width:80%' rows='4' ></textarea></td></tr>
	<tr><th>录入人：</th><td colspan='3'>黄清 </td></tr>

	<tr><th>处理结果</th><td colspan='3'><textarea id='question' name='question' style='width:80%' rows='4' ></textarea></td></tr>
	<tr><th>处理人：</th><td colspan='3'>冰冰 </td> </tr>

	<% String date = DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm"); %>
    <tr><th>填写时间:</th><td colspan='3'><%=date %><input type='hidden' value='2012-09-19 10:52' name='createDate' id='createDate' /></td></tr>

	<tr><th>处理结果：</th><td ><input type='radio' value='已提交'  name='type' />已提交  <input type='radio' value='已处理'  name='type' />已处理  </tr>

	<tr><td colspan='4' ><input type='button' value='提交' onclick='sub();' /><input type='reset' value='重置' /></td></tr>
	</table></form>
 

</body>
</html>