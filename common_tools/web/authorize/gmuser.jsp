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
	//0-忘仙GM, //1-忘仙GM管理员, //2-三国GM
	int type = 0;
	
	UserManager um = am.getUserManager();
	if(submitted == false && name != null){
		User user = um.getUser(name);
		if(realname == null && user != null)
			realname = user.getRealName();
		if(email == null && user != null){
			email = user.getEmail();
		}
		Role role = am.getRoleManager().getRole("wangxian_gm_manager");
		if(user.isRoleExists(role)) {
			type = 0;
		} else {
			role = am.getRoleManager().getRole("wangxian_gm_captain");
			if(user.isRoleExists(role)) {
				type = 1;
			} else {
				type = 2;
			}
		}
	}
	if(submitted && action != null){
		if(action.equals("add")){
			if(um.isUserExists(name)){
				errorMessage = "用户已经存在，请用其他名称！";
			}else{
				User u = um.addUser(name,realname,email);
				String usertype = request.getParameter("usertype");
				if(usertype.equals("0")) {
					Role role = am.getRoleManager().getRole("wangxian_gm_captain");
					Role role2 = am.getRoleManager().getRole("role_gm_common");
					if(role != null){
						u.addRole(role);
					}
					if(role2 != null) {
						u.addRole(role2);
					}
				} else if(usertype.equals("1")) {
					Role role = am.getRoleManager().getRole("wangxian_gm_manager");
					Role role2 = am.getRoleManager().getRole("role_gm_captain");
					if(role != null){
						u.addRole(role);
					}
					if(role2 != null) {
						u.addRole(role2);
					}
				} else if(usertype.equals("2")) {
					Role role = am.getRoleManager().getRole("gm_tools");
					if(role != null){
						u.addRole(role);
					}
					role = am.getRoleManager().getRole("game_monitor");
					if(role != null){
						u.addRole(role);
					}
				}
				Role role = am.getRoleManager().getRole("modify_password");
				if(role != null){
					u.addRole(role);
				}
				am.saveTo(am.getConfigFile());
				errorMessage = "添加成功！";
			}
		}else if(action.equals("remove")){
			User u = um.getUser(name);
			if(u == null){
				errorMessage = "要删除的用户不存在，请用其他名称！";
			}else{
				Role roles[] = u.getRoles();
				boolean hasOther = false;
				for(int j = 0; j < roles.length ; j++){
					if(!roles[j].getId().equals("modify_password") && !roles[j].getId().equals("wangxian_gm_captain") && !roles[j].getId().equals("wangxian_gm_manager") && !roles[j].equals("gm_tools") && !roles[j].equals("game_monitor")) {
						hasOther = true;
					}
				}
				if(hasOther) {
					errorMessage = "没有权限删除其他角色用户！";
				} else {
					um.removeUser(name);
					am.saveTo(am.getConfigFile());
					errorMessage = "删除成功！";
				}
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
<tr bgcolor='#FFFFFF' align='center'>
<td>用户类型</td>
<td>
 <input type=radio name=usertype value='0'<%if(type==0) out.print(" checked");%>>忘仙普通GM
 <input type=radio name=usertype value='1'<%if(type==1) out.print(" checked");%>>忘仙GM组长
 <input type=radio name=usertype value='2'<%if(type==2) out.print(" checked");%>>三国GM
</td>
<td>决定用户的权限</td>
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
<%
	}
%>

<center><a href='./gmusers.jsp'>返回用户列表页面</a></center>
</body>
</html> 
