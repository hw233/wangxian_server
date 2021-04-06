<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
	String errorMessage = null;
	String action = request.getParameter("action");
	boolean submitted = "true".equals(request.getParameter("submitted"));
	
	String pid = request.getParameter("pid");
	String mid = request.getParameter("mid");
	String pname = request.getParameter("pname");
	String purl = request.getParameter("purl");
	
	PlatformManager pm = am.getPlatformManager();
	Platform platform = pm.getPlatform(pid);
	
	if(submitted == false && mid != null){
		Module module = platform.getModule(mid);
		if(pname == null && module != null){
			pname = module.getName();
		}
		if(purl == null && module != null){
			purl = module.getUri();
		}
	}
	if(submitted && action != null){
		if(action.equals("add")){
			if(platform.isModuleExists(mid)){
				errorMessage = "模块已经存在，请用其他ID！";
			}else{
				platform.addModule(mid,pname,purl);
				am.saveTo(am.getConfigFile());
				errorMessage = "模块添加成功！";
			}
		}else if(action.equals("modify")){
			Module module = platform.getModule(mid);
			if(module == null){
				errorMessage = "要修改的模块不存在，请用其他ID！";
			}else{
				module.setName(pname);
				module.setUri(purl);
				am.saveTo(am.getConfigFile());
				errorMessage = "修改成功！";
			}
		}else if(action.equals("remove")){
			Module module = platform.getModule(mid);
			if(module == null){
				errorMessage = "要删除的模块不存在，请用其他ID！";
			}else{
				platform.removeModule(mid);
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
		out.println("<h3>为"+platform.getName()+"平台添加新的模块</h3>");
		submitName="添加模块";
	}else if(action.equals("modify")){
		out.println("<h3>为"+platform.getName()+"平台修改已有模块</h3>");
		submitName="修改模块";
	}else if(action.equals("remove")){
		out.println("<h3>为"+platform.getName()+"平台删除模块</h3>");
		submitName="删除模块";
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
<input type='hidden' name='pid' value='<%=pid %>'>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#00FFFF' align='center'><td>属性名称</td><td>属性值</td><td>属性说明</td></tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>唯一标识</td>
<td><input type='text' id='mid' name='mid' size='40'  value='<%=(mid==null?"":mid)%>'></td>
<td>请确保标识的正确性，唯一性，标识必须是英文字符</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>名称</td>
<td><input type='text' id='pname' name='pname' size='40'  value='<%=(pname==null?"":pname)%>'></td>
<td>模块的名词，可以是中文名</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>URI</td>
<td><input type='text' id='purl' name='purl' size='40'  value='<%=(purl==null?"":purl)%>'></td>
<td>模块的URI，此URL作为平台URL后面的部分，支持模式匹配，比如/admin/.*</td>
</tr>

<tr bgcolor='#00FFFF' align='center'><td colspan=3><input type='submit' value='<%=submitName %>'></td></tr>
</table>
</form>
<a href='./platforms.jsp'>返回平台列表页面</a><br/>
<a href='./platforms.jsp?platform=<%=platform.getId() %>'>返回『<%=platform.getName() %>』平台列表页面</a>
</body>
</html> 
