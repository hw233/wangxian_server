<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
%><html>
<head>
</head>
<body>
<%
	
	
%>
<h3>用户配置情况汇总</h3>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr align='center' bgcolor='#00FFFF'>
<td>用户名</td>
<td>真实姓名</td>
<td>邮箱</td>
<td>创建时间</td>
<td>密码过期时间</td>
<td>拥有的角色</td>
<td>重置密码</td>
<td>操作</td>
</tr>
<%
	UserManager um = am.getUserManager();
	User users[] = um.getUsers();
	for(int i = 0 ; i < users.length ; i++){
		User u = users[i];
		StringBuffer sb = new StringBuffer();
		Role roles[] = u.getRoles();
		for(int j = 0; j < roles.length ; j++){
			sb.append(roles[j].getName()+",");
		}
		boolean expire = false;
		String expireStr = "";
		if(System.currentTimeMillis() > u.getPasswordExpiredTime().getTime()){
			expire = true;
			expireStr = "已过期";
		}else{
			long l = u.getPasswordExpiredTime().getTime() - System.currentTimeMillis();
			expireStr = "还有"+(l/(24*3600000))+"天"+((l%(24*3600000))/3600000)+"小时过期";
		}
		
		out.print("<tr align='center' bgcolor='"+(expire?"#FFDDEE":"#FFFFFF")+"'>");
		out.print("<td>"+u.getName()+"</td>");
		out.print("<td>"+u.getRealName()+"</td>");
		out.print("<td>"+u.getEmail()+"</td>");
		out.print("<td>"+u.getCreateDate()+"</td>");
		out.print("<td>"+expireStr+"</td>");
		out.print("<td>"+sb+"</td>");
		out.print("<td><a href='./password.jsp?action=modify&name="+u.getName()+"'>重置密码</a></td>");
		out.print("<td><a href='./user.jsp?action=modify&name="+u.getName()+"'>修改</a>|<a href='./user.jsp?action=remove&name="+u.getName()+"'>删除</a></td>");
		out.print("</tr>\n");
	}
%></table><br>
<a href='./user.jsp?action=add'>添加新的用户</a>

</body>
</html> 
