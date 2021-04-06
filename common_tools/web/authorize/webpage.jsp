<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
	String errorMessage = null;
	String action = request.getParameter("action");
	boolean submitted = "true".equals(request.getParameter("submitted"));
	
	String pid = request.getParameter("pid");
	String mid = request.getParameter("mid");
	String gid = request.getParameter("gid");
	String pname = request.getParameter("pname");
	String purl = request.getParameter("purl");
	
	PlatformManager pm = am.getPlatformManager();
	Platform platform = pm.getPlatform(pid);
	Module module = platform.getModule(mid);
	
	if(submitted == false && gid != null){
		WebPage webpage = module.getWebPage(gid);
		if(pname == null && webpage != null){
			pname = webpage.getName();
		}
		if(purl == null && webpage != null){
			purl = webpage.getPagePattern();
		}
	}
	if(submitted && action != null){
		if(action.equals("add")){
			if(module.isWebPageExists(gid)){
				errorMessage = "页面已经存在，请用其他ID！";
			}else{
				module.addWebPage(gid,pname,purl);
				am.saveTo(am.getConfigFile());
				errorMessage = "页面添加成功！";
			}
		}else if(action.equals("modify")){
			WebPage webpage = module.getWebPage(gid);
			if(webpage == null){
				errorMessage = "要修改的页面不存在，请用其他ID！";
			}else{
				webpage.setName(pname);
				webpage.setPagePattern(purl);
				am.saveTo(am.getConfigFile());
				errorMessage = "修改成功！";
			}
		}else if(action.equals("remove")){
			WebPage webpage = module.getWebPage(gid);
			if(webpage == null){
				errorMessage = "要删除的页面不存在，请用其他ID！";
			}else{
				module.removeWebPage(gid);
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
		out.println("<h3>为"+platform.getName()+"平台的"+module.getName()+"模块添加新的页面</h3>");
		submitName="添加页面";
	}else if(action.equals("modify")){
		out.println("<h3>为"+platform.getName()+"平台的"+module.getName()+"模块修改已有页面</h3>");
		submitName="修改页面";
	}else if(action.equals("remove")){
		out.println("<h3>为"+platform.getName()+"平台的"+module.getName()+"模块删除页面</h3>");
		submitName="删除页面";
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
<input type='hidden' name='mid' value='<%=mid %>'>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#00FFFF' align='center'><td>属性名称</td><td>属性值</td><td>属性说明</td></tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>唯一标识</td>
<td><input type='text' id='gid' name='gid' size='40'  value='<%=(gid==null?"":gid)%>'></td>
<td>请确保标识的正确性，唯一性，标识必须是英文字符</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>名称</td>
<td><input type='text' id='pname' name='pname' size='40'  value='<%=(pname==null?"":pname)%>'></td>
<td>页面的名词，可以是中文名</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>页面的模式匹配表达式</td>
<td><input type='text' id='purl' name='purl' size='40'  value='<%=(purl==null?"":purl)%>'></td>
<td>页面的模式匹配表达式，比如.*/admin/.*</td>
</tr>

<tr bgcolor='#00FFFF' align='center'><td colspan=3><input type='submit' value='<%=submitName %>'></td></tr>
</table>
</form>
<a href='./platforms.jsp'>返回平台列表页面</a><br/>
<a href='./platforms.jsp?platform=<%=platform.getId() %>'>返回『<%=platform.getName() %>』平台列表页面</a>
</body>
</html> 
