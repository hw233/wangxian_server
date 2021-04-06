<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
	String errorMessage = null;
	boolean submitted = "true".equals(request.getParameter("submitted"));
	
	String name = request.getParameter("name");
	
	if(name == null && session.getAttribute(AuthorizedServletFilter.USER) != null){
		User user = (User)session.getAttribute(AuthorizedServletFilter.USER);
		name = user.getName();
	}
	
	String p0 = request.getParameter("p0");
	String p1 = request.getParameter("p1");
	String p2 = request.getParameter("p2");
	
	int expire = 30;
	
	if(submitted){
		User user = am.getUserManager().getUser(name);
		if(user == null){
			errorMessage = "指定的用户不存在！请重新输入用户名";
		}else{
			boolean b = am.getUserManager().matchUser(user.getName(),p0);
			if(b == false){
				errorMessage = "当前密码输入不对，请重新输入密码！";
			}
		}
		
		if(p1 == null || p2 == null || p1.equals(p2) == false){
			errorMessage = "新密码输入不完整或者两次输入的密码不匹配，请重新输入！";
		}
		
		if(p1.equals(p0)){
			errorMessage = "输入新密码与老密码一样，请输入不同的密码！";
		}
		
		if(errorMessage == null){
				
			char chars[] = p1.toCharArray();
			boolean includeNum = false;
			boolean includeLittleLetter = false;
			boolean includeBigLetter = false;
			for(int i = 0 ; i < chars.length ; i++){
				if(chars[i] >='0' && chars[i] <='9') includeNum = true;
				if(chars[i] >='a' && chars[i] <='z') includeLittleLetter = true;
				if(chars[i] >='A' && chars[i] <='Z') includeBigLetter = true;
			}
			if(includeNum == false || includeLittleLetter == false || includeBigLetter == false || chars.length < 6){
				errorMessage = "密码长度至少是6位，并且必须同时含有小写字母，大写字母和数字，请重新输入！";
			}
			else{
				am.getUserManager().setUserPassword(user,p1,expire);
				am.saveTo(am.getConfigFile());
				errorMessage = "设置成功！有效期为"+expire+"天";
			}
			
		}
	}
%>
<html>
<head>
</head>
<body>
<%
	if(errorMessage != null){
		%>
		<table border='0' cellpadding='0' cellspacing='3' width='100%' bgcolor='#FF0000' align='center'>
		<tr bgcolor='#FFFFFF' align='center'><td><font color='red'><%=errorMessage %></font></td></tr>
		</table><br/><br/>
		<%
	}
%><form id='two' name='two' method='post'><input type='hidden' name='submitted' value='true'>

<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#00FFFF' align='center'><td>属性名称</td><td>属性值</td><td>属性说明</td></tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>请输入您的用户名</td>
<td><input type='text' id='name' name='name' size='40'  value='<%=name==null?"":name %>'></td>
<td>用户名</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>当前密码</td>
<td><input type='password' id='p0' name='p0' size='40'  value='<%=p0==null?"":p0 %>'></td>
<td>目前使用的密码</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>新的密码</td>
<td><input type='password' id='p1' name='p1' size='40'  value='<%=p1==null?"":p1 %>'></td>
<td>密码长度至少是6位，并且必须同时含有小写字母，大写字母和数字</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>重新输入一次</td>
<td><input type='password' id='p2' name='p2' size='40'  value='<%=p2==null?"":p2 %>'></td>
<td>密码长度至少是6位，并且必须同时含有小写字母，大写字母和数字</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>有效期</td>
<td>30天</td>
<td>单位为天</td>
</tr>

<tr bgcolor='#00FFFF' align='center'><td colspan=3><input type='submit' value='提   交'></td></tr>
</table>
</form>
</body>
</html> 
