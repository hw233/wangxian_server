<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
	String errorMessage = null;
	String action = request.getParameter("action");
	boolean submitted = "true".equals(request.getParameter("submitted"));
	
	String name = request.getParameter("name");
	String realname = request.getParameter("realname");
	String email = request.getParameter("email");
	
	UserManager um = am.getUserManager();
	if(submitted == false && name != null){
		User user = um.getUser(name);
		if(realname == null && user != null)
			realname = user.getRealName();
		if(email == null && user != null){
			email = user.getEmail();
		}
	}
	if(submitted && action != null){
		if(action.equals("add")){
			if(um.isUserExists(name)){
				errorMessage = "用户已经存在，请用其他名称！";
			}else{
				um.addUser(name,realname,email);
				am.saveTo(am.getConfigFile());
				errorMessage = "添加成功！";
			}
		}else if(action.equals("modify")){
			User u = um.getUser(name);
			if(u == null){
				errorMessage = "要修改的用户不存在，请用其他名称！";
			}else{
				String subaction = request.getParameter("subaction");
				if(subaction == null){
					u.setRealName(realname);
					u.setEmail(email);
					
				}else if(subaction.equals("addrole")){
					String rid = request.getParameter("rid");
					Role role = am.getRoleManager().getRole(rid);
					if(role != null){
						u.addRole(role);
					}
				}else if(subaction.equals("removerole")){
					String rid = request.getParameter("rid");
					Role role = am.getRoleManager().getRole(rid);
					if(role != null){
						u.removeRole(role);
					}
				}else if(subaction.equals("addproperty")){
					String key = request.getParameter("key");
					String value = request.getParameter("value");
					if(key != null && value != null){
						u.getProperties().setProperty(key,value);
					}
				}else if(subaction.equals("removeproperty")){
					String key = request.getParameter("key");
					if(key != null){
						u.getProperties().remove(key);
					}
				}
				am.saveTo(am.getConfigFile());
				errorMessage = "修改成功！";
			}
		}else if(action.equals("remove")){
			User u = um.getUser(name);
			if(u == null){
				errorMessage = "要删除的用户不存在，请用其他名称！";
			}else{
				um.removeUser(name);
				am.saveTo(am.getConfigFile());
				errorMessage = "删除成功！";
			}
		}
	}
%><html>
<head>
</head>
<body>
<%
	String submitName = "未知操作";
	if(action.equals("add")){
		out.println("<h3>添加新的用户</h3>");
		submitName="添加用户";
	}else if(action.equals("modify")){
		out.println("<h3>修改已有用户</h3>");
		submitName="修改用户";
	}else if(action.equals("remove")){
		out.println("<h3>删除用户</h3>");
		submitName="删除用户";
	}else{
		out.println("<h3>不能识别的action["+action+"]</h3>");
	}
	if(errorMessage != null){
		%>
		<table border='0' cellpadding='0' cellspacing='3' width='100%' bgcolor='#FF0000' align='center'>
		<tr bgcolor='#FFFFFF' align='center'><td><font color='red'><%=errorMessage %></font></td></tr>
		</table><br/><br/>
		<%
	}
%><form id='first' name='first'><input type='hidden' name='submitted' value='true'>
<input type='hidden' name='action' value='<%=action %>'>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#00FFFF' align='center'><td>属性名称</td><td>属性值</td><td>属性说明</td></tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>用户名</td>
<td><input type='text' id='name' name='name' size='40'  value='<%=(name==null?"":name)%>'></td>
<td>请确保标识的正确性，唯一性，标识必须是英文字符</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>真实姓名</td>
<td><input type='text' id='realname' name='realname' size='40'  value='<%=(realname==null?"":realname)%>'></td>
<td>用户真实姓名</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>用户邮箱</td>
<td><input type='text' id='email' name='email' size='40'  value='<%=(email==null?"":email)%>'></td>
<td>用于取回密码</td>
</tr>

<tr bgcolor='#00FFFF' align='center'><td colspan=3><input type='submit' value='<%=submitName %>'></td></tr>
</table>
</form>

<% 
	if(action.equals("modify")){
%>

<h4>为用户<%=name %>添加新的角色</h4>
<form id='tow' name='two'><input type='hidden' name='submitted' value='true'>
<input type='hidden' name='action' value='<%=action %>'>
<input type='hidden' name='subaction' value='addrole'>
<input type='hidden' name='name' value='<%=name %>'>
请选择要添加的角色：<select id='rid' name='rid'><% 
	User user = um.getUser(name);
	ArrayList<Role> al = new ArrayList<Role>();
	Role roles[] = am.getRoleManager().getRoles();
	for(int i = 0 ; i < roles.length ; i++){
		if(user.isRoleExists(roles[i]) ==false){
			al.add(roles[i]);
		}	
	}
	for(int i = 0 ; i < al.size() ; i++){
		Role r = al.get(i);
		out.println("<option value='"+r.getId()+"'>"+r.getName()+"</option>");
	}
%></select><input type='submit' value='添加新的角色'></form>


<h4>为用户<%=name %>删除已有的角色</h4>
<form id='three' name='three'><input type='hidden' name='submitted' value='true'>
<input type='hidden' name='action' value='<%=action %>'>
<input type='hidden' name='subaction' value='removerole'>
<input type='hidden' name='name' value='<%=name %>'>
请选择要删除的角色：<select id='rid' name='rid'><% 
	roles = user.getRoles();
	for(int i = 0 ; i < roles.length ; i++){
		Role r = roles[i];
		out.println("<option value='"+r.getId()+"'>"+r.getName()+"</option>");
	}
%></select><input type='submit' value='删除已有的角色'></form>

<h4>用户<%=name %>属性列表</h4>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#00FFFF' align='center'><td>属性名称</td><td>属性值</td></tr>
<% 
	String propKeys[] = user.getProperties().keySet().toArray(new String[0]);
	for(int i = 0 ; i < propKeys.length ; i++){
		String value = user.getProperties().getProperty(propKeys[i]);
		%><tr bgcolor='#FFFFFF' align='center'>
<td><%=propKeys[i] %></td>
<td><%=value %></td>
</tr>
		<%
	}
%>
</table>

<h4>为用户<%=name %>添加新的属性</h4>
<form id='four' name='four'><input type='hidden' name='submitted' value='true'>
<input type='hidden' name='action' value='<%=action %>'>
<input type='hidden' name='subaction' value='addproperty'>
<input type='hidden' name='name' value='<%=name %>'>
请输入属性Key：<input type='text' id='key' name='key' size='40'  value=''><br/>
请输入属性Value：<input type='text' id='value' name='value' size='40'  value=''><br/>
<input type='submit' value='添加新的属性'></form>



<h4>为用户<%=name %>删除已有的属性</h4>
<form id='five' name='five'><input type='hidden' name='submitted' value='true'>
<input type='hidden' name='action' value='<%=action %>'>
<input type='hidden' name='subaction' value='removeproperty'>
<input type='hidden' name='name' value='<%=name %>'>
请选择要删除的Key：<select id='key' name='key'><% 
	String keys[] = user.getProperties().keySet().toArray(new String[0]);
	for(int i = 0 ; i < keys.length ; i++){
		out.println("<option value='"+keys[i]+"'>"+keys[i]+" = "+user.getProperties().getProperty(keys[i])+"</option>");
	}
%></select><input type='submit' value='删除已有的属性'></form>
<%
	}
%>

<center><a href='./users.jsp'>返回用户列表页面</a></center>
</body>
</html> 
