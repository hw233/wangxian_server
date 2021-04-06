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
<h3>角色配置情况汇总</h3>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr align='center' bgcolor='#00FFFF'>
<td>角色名词</td>
<td>角色是否可用</td>
<td>拥有此角色的用户</td>
<td>操作</td>
</tr>
<%
	RoleManager rm = am.getRoleManager();
	Role roles[] = rm.getRoles();
	for(int i = 0 ; i < roles.length ; i++){
		StringBuffer sb = new StringBuffer();
		User us[] = am.getUserManager().getUsers();
		for(int j = 0; j < us.length ; j++){
			if(us[j].isRoleExists(roles[i])){
				sb.append(us[j].getRealName()+"，");
			}
		}
		out.print("<tr align='center' bgcolor='"+(roles[i].isValid()?"#FFFFFF":"#DDDDDD")+"'>");
		out.print("<td>"+roles[i].getName()+"</td>");
		out.print("<td>"+roles[i].isValid()+"</td>");
		out.print("<td>"+sb+"</td>");
		out.print("<td><a href='./role.jsp?action=modify&rid="+roles[i].getId()+"'>修改</a>|<a href='./role.jsp?action=remove&rid="+roles[i].getId()+"'>删除</a></td>");
		out.print("</tr>\n");
	}
%></table><br>
<a href='./role.jsp?action=add'>添加新的角色</a>

</body>
</html> 
