<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%>
<%@ include file="header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		
			<link href="css/dtree.css" type="text/css" rel="stylesheet" />
			<script src="js/dtree.js" type=text/javascript></script>

			<META content="MSHTML 6.00.2900.3157" name=GENERATOR>
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
	  function change(tag){
		  var url = tag.value;
		  window.parent.document.getElementById("sesFrame").src=tag.value+"/savemessage.jsp?server="+url;
		  window.location.replace("leftframe.jsp?server="+url);
	  }
	  function exit1(){
		  window.location.replace("leftframe.jsp?exit=true");
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
				 session.invalidate();
// 				 session.removeAttribute("username");
// 			      session.removeAttribute("authorize.username");
// 			      session.setAttribute("authorize.username", "");
			      out.print("remove success");
// 			      out.print("username:"+session.getAttribute("authorize.username"));
			     out.print("<script type='text/javascript' >window.top.location.replace(\"/game_boss/gm/index.jsp\");</script>");
// 			   response.sendRedirect("index.jsp");
			      return;
			    }
			ModuleTypeManager mtmanager = ModuleTypeManager.getInstance();
			XmlRoleManager rmanager = XmlRoleManager.getInstance();
			XmlServerManager smanager = XmlServerManager.getInstance();
			
			//获得模块所有类型
			List<ModuleType> types = mtmanager.getTypes();
			
			out.print("你好：<font color='red'><B>"+"d"+"</B></font><br/>选择服务器：");
			
			List<XmlModule> usermodules = new ArrayList<XmlModule>();
			List<String>  totalmoduleids = mtmanager.getModuleids();
			List<String> servers = new ArrayList<String>();
			List<XmlServer> totalservers = smanager.getServers();
			
			//
			ChineseUtil util = new ChineseUtil();
			util.setDicFile("/home/boss/resin_boss_test/webapps/game_boss/gm/manager/chinese4utf8.txt");
			util.initialize();
			Map<String,XmlServer> map = new HashMap<String,XmlServer>();
			String [] quanpin = new String[totalservers.size()];
			for (int i=0; i<totalservers.size();i++) {
				quanpin[i] = util.getFullPin(totalservers.get(i).getDescription());
				map.put(util.getFullPin(totalservers.get(i).getDescription()),totalservers.get(i));
			}
			
			Arrays.sort(quanpin);
			//
			
			String serverurl="";//保存该当前的服务器（“”标识为空）
			String serverurl1 = request.getParameter("server");
			if(serverurl1!=null)
			  serverurl = serverurl1;
			
			out.print("<select id='server' name='server' onchange='change(this);' ><option value='' >---</option>");
			for (String ss : quanpin) {
				//遍历所有的服务器列表,没加权限！！
				if (ss!=null) {
					out.print("<option value='" + map.get(ss).getUri() + "'");
					if (serverurl != null && serverurl.equals(map.get(ss).getUri()))
						out.print("selected");
					out.print(">" + map.get(ss).getDescription() + "</option>");
				}
			}
			out.print("</select>");
			LinkedHashMap<String, ArrayList<String[]>> directoryMap = new LinkedHashMap<String, ArrayList<String[]>>();

			//通过模块类型遍历所拥有的模块
			for (ModuleType mt : types) {
				//根据文件类型拼接
				ArrayList<String[]> di = new ArrayList<String[]>();
				List<XmlModule> ms = mt.getModules();
				for (XmlModule m : ms) {
					String url = m.getJspName();
					if(!"true".equals(m.getBase())&&!"".equals(serverurl))
						di.add(new String[] { m.getDescription(),
										serverurl + "/" + url});
					else if("true".equals(m.getBase()))
						di.add(new String[] { m.getDescription(),
						url });
				}
				if(di.size()>0){
				  directoryMap.put(mt.getName(), di);
				}
			}
				
		%>
		<script type=text/javascript>
		
                var d = new dTree('d');
                d.add(0,-1,'世界游戏管理');
		<%
			String pageUrl = request.getRequestURL().toString();
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
			}
			%>
			document.write(d);
        
	</script>
		<br />
		 <input type="button" onclick="javascript:exit1()" value="清除记录" />
		 <br/><br/>
		<span style="padding: 20px 20px 20px 15px"> 
		 <a href="javascript:window.parent.close()">退出系统</a>
		  </span> 
	</body>
</html>
