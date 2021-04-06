<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
	String errorMessage = null;
	String action = request.getParameter("action");
	boolean submitted = "true".equals(request.getParameter("submitted"));
	
	String rid = request.getParameter("rid");
	String pname = request.getParameter("pname");
	boolean valid = "true".equals(request.getParameter("valid"));
	RoleManager pm = am.getRoleManager();
	if(submitted == false && rid != null){
		Role platform = pm.getRole(rid);
		if(pname == null && platform != null){
			pname = platform.getName();
		}
		if(platform != null){
			valid = platform.isValid();
		}
	}
	if(submitted && action != null){
		if(action.equals("add")){
			if(pm.isRoleExists(rid)){
				errorMessage = "角色已经存在，请用其他ID！";
			}else{
				Role r = pm.addRole(rid,pname);
				r.setValid(valid);
				am.saveTo(am.getConfigFile());
				errorMessage = "添加成功！";
			}
		}else if(action.equals("modify")){
			Role r = pm.getRole(rid);
			if(r == null){
				errorMessage = "要修改的角色不存在，请用其他ID！";
			}else{
				r.setName(pname);
				r.setValid(valid);
				am.saveTo(am.getConfigFile());
				errorMessage = "修改成功！";
			}
		}else if(action.equals("remove")){
			Role r = pm.getRole(rid);
			if(r == null){
				errorMessage = "要删除的角色不存在，请用其他ID！";
			}else{
				pm.removeRole(rid);
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
		out.println("<h3>添加新的角色</h3>");
		submitName="添加角色";
	}else if(action.equals("modify")){
		out.println("<h3>修改已有角色</h3>");
		submitName="修改角色";
	}else if(action.equals("remove")){
		out.println("<h3>删除角色</h3>");
		submitName="删除角色";
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
%><form id='two' name='two'><input type='hidden' name='submitted' value='true'>
<input type='hidden' name='action' value='<%=action %>'>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#00FFFF' align='center'><td>属性名称</td><td>属性值</td><td>属性说明</td></tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>唯一标识</td>
<td><input type='text' id='rid' name='rid' size='40'  value='<%=(rid==null?"":rid)%>'></td>
<td>请确保标识的正确性，唯一性，标识必须是英文字符</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>名称</td>
<td><input type='text' id='pname' name='pname' size='40'  value='<%=(pname==null?"":pname)%>'></td>
<td>平台的名词，可以是中文名</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>是否有效</td>
<td><input type='text' id='valid' name='valid' size='40'  value='<%=valid%>'></td>
<td>角色是否生效，true表示生效，false表示无效</td>
</tr>

<tr bgcolor='#00FFFF' align='center'><td colspan=3><input type='submit' value='<%=submitName %>'></td></tr>
</table>
</form>
<center><a href='./roles.jsp'>返回角色列表页面</a></center>
</body>
</html> 
