<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%>
<%@page import="com.xuanzhi.boss.gmuser.model.Authority"%>
<%@page import="com.xuanzhi.boss.gmuser.model.Gmuser"%>
<%@ include file="statjsp/useradmin/header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<META http-equiv=Content-Type content="text/html; charset=utf-8"/>
<link href="css/dtree.css" type="text/css" rel="stylesheet" />
<script src="js/dtree.js" type=text/javascript></script>
<META content="MSHTML 6.00.2900.3157" name=GENERATOR/>
		<style type="text/css">
<!--
body,td,th {
	font-size: 12px;
}

body {
	margin-left: 5px;
	margin-top: 5px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #DDEDFF;
}
-->
</style>
				<script type="text/javascript">
  function change(tag,username,gmid){
  var url = tag.value;
  window.parent.document.getElementById("sesFrame").src=tag.value+"/savemessage.jsp?server="+url+"&gmid="+gmid+"&username="+username;
  window.location.replace("leftframe.jsp?server="+url);
  }
  function exit1(){
  window.location.replace("leftframe.jsp?exit=true");
  }
  function updatePsw(){
  window.location.replace("updatePassword.jsp");
  }
  
</script>
	</head>
	<body>
		<div align="center" class="dtree">
			<a href="javascript:%20d.openAll();" class="node">全部展开</a> |
			<a href="javascript:%20d.closeAll();" class="node">全部折叠</a>
			<br />
			<br />
		</div>
		<div style="display: none;" id="sess"></div>
		<%
    String exit = request.getParameter("exit");
    if(exit!=null&&"true".equals(exit)){
      session.removeAttribute("username");
      out.println("<script>window.top.location.replace('index.jsp');</script>");
      return;
    } 
	GmUserManager gmanager = GmUserManager.getInstance();
	//管理GM用户和权限
	ModuleTypeManager mtmanager = ModuleTypeManager.getInstance();
	//管理所有文件夹
	XmlRoleManager rmanager = XmlRoleManager.getInstance();
	//管理所有角色
	XmlServerManager smanager = XmlServerManager.getInstance();
	//管理所有的服务器
	String username = session.getAttribute("username").toString();
	//gm的用户名
	String gmid = session.getAttribute("gmid").toString();
	//gm的id
	out.print(session.getAttribute("username") + "你好!<br/>");
	List<ModuleType> types = mtmanager.getTypes();
	//所有的文件类型
	Gmuser gu = gmanager.getGmuser(username);
	//获得该GM用户对象
	List<Authority> roleids=new ArrayList<Authority>();
	if(gu!=null)
	 roleids = gu.getAuthos();
	//该用户所用有的所有权限，可以获得该用户所拥有的角色Id
	List<XmlModule> usermodules = new ArrayList<XmlModule>();
	//保存该用户所拥有的模块
	List<XmlModule> totalmodules = mtmanager.getModules();
	List<String>  totalmoduleids = mtmanager.getModuleids();
	List<String> servers = new ArrayList<String>();
	//保存该用户所管理的servers
	List<XmlServer> totalservers = smanager.getServers();
	 //所有的服务器列表
	List<String> totalroleids = rmanager.getRoleids();
	//所有的角色列表
	List<Authority> deleteauths = new ArrayList<Authority>();
	//保存要删除的角色权限列表
	for (Authority r : roleids) { 
	    //表里gm用户的权限列表
		if (totalroleids.contains(r.getRoleid()))//如果该角色ID还存在则添加到用户的servers中
			servers.addAll(rmanager.getRole(r.getRoleid())
							.getServers());
		else if (!totalroleids.contains(r.getRoleid()))//如果不包含，则删除该项
			deleteauths.add(r);
	}
	if (deleteauths.size() > 0) {//如果该用户的某些角色ID已不存在，删除并更新
		roleids.removeAll(deleteauths);
		gmanager.updateGmuser(gu);
	}
	String serverurl="";//保存该当前的服务器（“”标识为空）
	String serverurl1 = request.getParameter("server");
	if(serverurl1!=null)
	  serverurl = serverurl1;
	LinkedHashMap<String, ArrayList<String[]>> directoryMap = new LinkedHashMap<String, ArrayList<String[]>>();
	for (Authority a : roleids) {
		//遍历各个模块，通过角色ID，将用户所拥有的的模块取出来
		List<String> mids = rmanager.getXmoduleIds(a.getRoleid());
		for (String mid : mids) {
		    if(totalmoduleids.contains(mid)&&!usermodules.contains(mtmanager.getModule(mid))&&mtmanager.getModule(mid).getIsShow().equals("true"))
			usermodules.add(mtmanager.getModule(mid));
		}
	}
	for (ModuleType mt : types) {
	//根据文件类型拼接
		ArrayList<String[]> di = new ArrayList<String[]>();
		List<XmlModule> ms = mt.getModules();
		for (XmlModule m : ms) {
			if (usermodules.contains(m)){
			  String url = m.getJspName();
				if(!"true".equals(m.getBase())&&!"".equals(serverurl))
					di.add(new String[] { m.getDescription(),
									serverurl + "/" + url});
				else if("true".equals(m.getBase()))
					di.add(new String[] { m.getDescription(),
					url });
			}  
		}
		if(di.size()>0){
		  directoryMap.put(mt.getName(), di);
		}
	}
	 
	    if("admin".equals(username)){
			ArrayList<String[]> directory = new ArrayList<String[]>();
			directoryMap.put("gm管理",directory);
			directory.add(new String[]{"服务器列表","/statjsp/useradmin/server_manager.jsp"});
			directory.add(new String[]{"功能页面列表","/statjsp/useradmin/types_manager.jsp"});
			directory.add(new String[]{"角色管理","/statjsp/useradmin/trole_list.jsp"});
			directory.add(new String[]{"gm账号管理","/statjsp/useradmin/gmuser_list.jsp"});
		}	
%>
		<script type=text/javascript>
		<!--
                var d = new dTree('d');
                d.add(0,-1,'gm管理后台1.0');
<%String pageUrl = request.getRequestURL().toString();
			if (pageUrl.lastIndexOf("/") > 0) {
				pageUrl = pageUrl.substring(0, pageUrl.lastIndexOf("/"));
			}

			String dirs[] = directoryMap.keySet().toArray(new String[0]);
			int parentIndex = 0;
			int currentIndex = 1;
			for (int i = 0; i < dirs.length; i++) {
				ArrayList<String[]> pageList = directoryMap.get(dirs[i]);
				int count = 0;
				for (int j = 0; j < pageList.size(); j++) {
					String[] ps = pageList.get(j);
					String url = pageUrl + "/" + ps[1];
					count++;
				}

				if (count > 0) {
					out.println("d.add(" + currentIndex + ",0,'" + dirs[i]
							+ "','');");
					parentIndex = currentIndex;
					currentIndex++;

					for (int j = 0; j < pageList.size(); j++) {
						String[] ps = pageList.get(j);
						String url = pageUrl + "/" + ps[1];
						out.println("d.add(" + currentIndex + "," + parentIndex
								+ ",'" + ps[0] + "','" + ps[1] + "');");
						currentIndex++;
					}

				}
			}%>
			document.write(d);
        //-->
	</script>
		<br />
		 <input type="button" onclick="javascript:exit1()" value="退出" />
		  <input type="button" onclick="javascript:updatePsw()" value="修改密码" />
		 <br/><br/>

		<div></div>
	</body>
</html>
