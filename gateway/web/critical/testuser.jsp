<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager.TestUser"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.thirdpartner.congzhi.* "%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%>
<%@ include file="../server/opdetail.jsp"%>
<%


//String beanName = "outer_gateway_connection_selector";
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	
	String action = "";
	action = request.getParameter("action");
	String testuserpass = request.getParameter("testuserpass");
	
	if(testuserpass != null)
	{
		if(!testuserpass.equals("12345678900"))
		{
			out.println("<script>alert('请输入正确的密码!');history.go(-1);</script>");
			return;
		}
		else
		{
			session.setAttribute("testuserpass", "12345678900");
			response.sendRedirect("testuser.jsp");
		}
	}
	
	if(action != null )
	{
		String pass = (String)session.getAttribute("testuserpass");
		if(pass == null)
		{
			out.println("<script>alert('请输入正确的密码后再操作');history.go(-1);</script>");
			return;
		}
		else if(!pass.equals("12345678900"))
		{
			out.println("<script>alert('请输入正确的密码后再操作');history.go(-1);</script>");
			return;
		}
		
	}
	

	if(action != null && action.equals("add_test_user")){
		
		
		String usernames = request.getParameter("usernames");
		String reasons = request.getParameter("reasons");
		String bumens = request.getParameter("bumens");
		
		String[] usernamearr = usernames.split("\r*\n");
		String[] reasonarr = reasons.split("\r*\n");
		String[] bumenarr = bumens.split("\r*\n");
		
		if(reasonarr.length != usernamearr.length)
		{
			out.println("<script>alert('添加用户与添加原因没有一一对应!');history.go(-1);</script>");
			return;
		}
		
		if(bumenarr.length != usernamearr.length)
		{
			out.println("<script>alert('添加用户与添加部门没有一一对应!');history.go(-1);</script>");
			return;
		}
		
		for(int i=0;i<usernamearr.length;i++)
		{
			String s1 = usernamearr[i];
			String s2 = reasonarr[i];
			String s3 = bumenarr[i];
			if(s2 == null || s2.trim().length() == 0)
			{
				out.println("<script>alert('请填写添加原因!');history.go(-1);</script>");
				return;
			}
			
			if(s3 == null || s3.trim().length() == 0)
			{
				out.println("<script>alert('请填写部门!');history.go(-1);</script>");
				return;
			}
			
			if(s1.trim().length() > 0){
				mm.addTestUser(s1,s2,true,user.getName(),s3);
			}
			out.println("<h2>测试人员"+s1+"设置成功</h2>");
		}
	}else if(action != null && action.equals("del_test_user")){
		String s1 = request.getParameter("username");
		mm.removeTestUser(s1);
		out.println("<h2>测试人员"+s1+"设置成功</h2>");
	}else if(action != null && action.equals("enable_test_user")){
		String s1 = request.getParameter("username");
		MieshiServerInfoManager.TestUser tu = mm.getTestUser(s1);
		if(tu != null){
			mm.addTestUser(s1,tu.reason,!tu.enable);
		}
		out.println("<h2>测试人员"+s1+"设置成功</h2>");
	}
	else if(action != null && action.equals("clear_users"))
	{
		Field f = mm.getClass().getDeclaredField("testUsernameList");
		f.setAccessible(true);
		f.set(mm, new ArrayList<TestUser>());
		mm.clearTestUser();
		out.println("<h2>清空测试用户设置成功</h2>");
	}
%><html>

<head>
<script>
function whiteList(element) {
		var text = element.innerHTML;
		var closeText = "关闭测试账号信息"
		var openText = "显示测试账号信息"
		var whiteList = document.getElementById("whitelist");
		if (openText == text) {
			element.innerHTML=closeText;
			whiteList.style.display = "block";
		} else if (closeText == text){
			element.innerHTML=openText;
			whiteList.style.display = "none";
		}
	}
	
function changeSelect(){
	var chooseAll = document.getElementById("chooseAll");
	var subs = document.getElementsByName("choose");
		for (var i = 0; i < subs.length;i++) {
			if(chooseAll.checked){
				subs[i].checked = true;
			} else {
				subs[i].checked = false;
			}
		}
}

function overTag(tag){
	tag.style.color = "red";	
//		tag.style.fontWeight = "600";
//		tag.style.backgroundColor = "#E9E4E4";
}
			
function outTag(tag){
	tag.style.color = "black";
//		tag.style.fontWeight = "300";
//		tag.style.backgroundColor = "white";
}	
</script>
</head>
<body  style="font-size: 12px; background-color: #c8edcc">
<center>
	<%
		if(session.getAttribute("testuserpass") == null)
		{
	%>
		<form>
			请输入操作密码:<input type = "password" name='testuserpass' value=""  /><br/>
			<input type='submit' value='提交'>
		</form>
	<%
		}
		else{
	%>
	<!-- onclick="whiteList(this)" -->
		<span   style="font-size: 20px;font-weight: bold;text-align: left;">显示测试账号信息</span>
		<div id="whitelist" style="display: block;">
			<h2>测试帐号信息，这些帐号在服务器是内部测试时，可以进入游戏</h2>
			
			
			<table  style="font-size: 12px;text-align: center;width: 100%">
			<tr bgcolor="#00FFFF" align="center">
				<td>测试帐号</td>
				<td>添加的时间</td>
				<td>最后修改时间</td><td>添加的原因</td><td>添加人</td><td>所在部门</td><td>是否生效</td>
				<td>操作</td><td>删除</td>
				</tr>
			<%
				ArrayList<MieshiServerInfoManager.TestUser> tt = mm.getTestUsers();
				for(int i = 0 ; i < tt.size() ; i++){
					MieshiServerInfoManager.TestUser t = tt.get(i);
					
					if(t.addUser == null)
					{
						t.addUser = "无添加人记录";
					}
					
					if(t.bumen == null)
					{
						t.bumen = "无";
					}
					
					out.println("<tr bgcolor='#FFFFFF' align='center' onmouseover = 'overTag(this);' onmouseout = 'outTag(this);'>");
					out.println("<td>"+t.username+"</td>");
					out.println("<td>"+DateUtil.formatDate(t.createTime,"yyyy-MM-dd")+"</td>");
					out.println("<td>"+DateUtil.formatDate(t.lastModifiedTime,"yyyy-MM-dd")+"</td>");
					out.println("<td>"+t.reason+"</td>");
					out.println("<td>"+t.addUser+"</td>");
					out.println("<td>"+t.bumen+"</td>");
					out.println("<td>"+t.enable+"</td>");
					out.println("<td><a href='./testuser.jsp?action=enable_test_user&username="+t.username+"'>"+(t.enable?"失效":"生效")+"</a></td>");
					out.println("<td><a href='./testuser.jsp?action=del_test_user&username="+t.username+"'>删除</a></td>");
					out.println("</tr>");
				}
				%>
			</table>
			<form  method="post" action="testuser.jsp"><input type='hidden'  name='action' value='clear_users' />
				<input type='submit' value='清空数据' />
			</form>
			添加测试帐号：
			<form method="post" action="testuser.jsp" ><input type='hidden'  name='action' value='add_test_user' />
			<br/>
			请输入用户名:<textarea name='usernames' cols="20" rows="10" ></textarea>
			部门:<textarea name='bumens' cols="20" rows="10"  ></textarea>
			添加的原因:<textarea name='reasons' cols="20" rows="10"  ></textarea>
			 	
			<input type='submit' value='提交' />
			</form>
		</div>
		<%} %>
</center>
</body>
<script type="text/javascript">
	
</script>
</html> 


