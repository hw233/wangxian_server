<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
	String errorMessage = null;
	String action = request.getParameter("action");
	boolean submitted = "true".equals(request.getParameter("submitted"));
	
	String pid = request.getParameter("pid");
	String pname = request.getParameter("pname");
	String purl = request.getParameter("purl");
	PlatformManager pm = am.getPlatformManager();
	if(submitted == false && pid != null){
		Platform platform = pm.getPlatform(pid);
		if(pname == null && platform != null){
			pname = platform.getName();
		}
		if(purl == null && platform != null){
			purl = platform.getUrl();
		}
	}
	if(submitted && action != null){
		if(action.equals("add")){
			if(pm.isPlatformExists(pid)){
				errorMessage = "平台已经存在，请用其他ID！";
			}else{
				pm.addPlatform(pid,pname,purl);
				am.saveTo(am.getConfigFile());
				errorMessage = "添加成功！";
			}
		}else if(action.equals("modify")){
			Platform platform = pm.getPlatform(pid);
			if(platform == null){
				errorMessage = "要修改的平台不存在，请用其他ID！";
			}else{
				platform.setName(pname);
				platform.setUrl(purl);
				am.saveTo(am.getConfigFile());
				errorMessage = "修改成功！";
			}
		}else if(action.equals("remove")){
			Platform platform = pm.getPlatform(pid);
			if(platform == null){
				errorMessage = "要删除的平台不存在，请用其他ID！";
			}else{
				pm.removePlatform(pid);
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
		out.println("<h3>添加新的平台</h3>");
		submitName="添加平台";
	}else if(action.equals("modify")){
		out.println("<h3>修改已有平台</h3>");
		submitName="修改平台";
	}else if(action.equals("remove")){
		out.println("<h3>删除平台</h3>");
		submitName="删除平台";
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
<td><input type='text' id='pid' name='pid' size='40'  value='<%=(pid==null?"":pid)%>'></td>
<td>请确保标识的正确性，唯一性，标识必须是英文字符</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>名称</td>
<td><input type='text' id='pname' name='pname' size='40'  value='<%=(pname==null?"":pname)%>'></td>
<td>平台的名词，可以是中文名</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>URL</td>
<td><input type='text' id='purl' name='purl' size='40'  value='<%=(purl==null?"":purl)%>'></td>
<td>平台的URL，此URL作为平台所有页面的开始部分，比如http://211.99.200.92:8086/monitor/</td>
</tr>

<tr bgcolor='#00FFFF' align='center'><td colspan=3><input type='submit' value='<%=submitName %>'></td></tr>
</table>
</form>
<a href='./platforms.jsp'>返回平台列表页面</a><br/>
<a href='./platforms.jsp?platform=<%=pid %>'>返回『<%=pname %>』平台列表页面</a>
</body>
</html> 
