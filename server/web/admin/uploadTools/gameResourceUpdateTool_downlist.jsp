<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type='text/javascript' src='jquery-1.6.2.js'></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资源更新</title>
<%@include file="gameResourceUpdateTool_config.jsp" %>
</head>
<script type="text/javascript">
	function loadcommit(){
		var actionname = document.getElementById("actionname").value;
		var fnames="";
		var a = document.getElementsByTagName("input"); 
		for(var i=0;i<a.length;i++){
			if(a[i].checked==true){ 
				window.location.reload();
				window.location.replace("gameResourceUpdateTool_download.jsp?actionname=ok&filename="+a[i].value+"");
// 				$.ajax({
// 					  type: 'POST',
// 					  url: "gameResourceUpdateTool_download.jsp?actionname=ok&filename="+a[i].value+"",
// 					  contentType : "application/x-www-form-urlencoded; charset=utf-8",
// 					  dataType: "xls",
// 					  success: function(result)
// 					  {
// 						  alert("result:"+result);
// // 						 document.getElementById('messid').innerHTML = "<font color='red'>OK</font>";
// 					  }
// 					});
			}
		}
	}
</script>
<body>
	<table bgcolor="#18edcc">
		<tr><input type="button" value='下载文件' onclick="loadcommit()"><input type="hidden" value='ok' name='actionname' id='actionname'></tr><br>
		<%!
			public static String filePath = "";
			public static String getSonFileList(File file, int index) {
				File[] filses = file.listFiles();
				StringBuffer sbf = new StringBuffer();
				for (File f : filses) {
					for (int i = 0; i < index; i++) {
						sbf.append("&nbsp;&nbsp;&nbsp;&nbsp;");
					}
					if(f.getPath().contains("bindata_tencent") ||f.getPath().contains("modify_xianzu_bindata") ||
							f.getPath().contains("minigame") ||f.getPath().contains("game_runtime_data") || 
							f.getPath().contains("septstation") || f.getPath().contains("translate") || 
							f.getPath().contains("bindata_tencent/map") ||f.getPath().contains("bindata") ||
							f.getPath().contains("modify_5yue15newserver_bindata_server/map")){
						continue;
					}
					if (!f.isDirectory()) {
						sbf.append("&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='filename' value='"+(f.getPath())+"'>").append(f.getName()).append("<br>");
					} else {
						int tempIndex = index + 1;
						String oldstr = f.getAbsolutePath().split("game_init_config")[0];
						filePath = f.getAbsolutePath().split("game_init_config")[1];
						String pathname = f.getAbsolutePath().replace(oldstr, "");
						sbf.append("&nbsp;&nbsp;&nbsp;&nbsp;<B><font color=red>" + pathname + "</font></B>").append("<br/>");
						sbf.append(getSonFileList(f, tempIndex));
					}
				}
				return sbf.toString();
			}
		%>
		<%
			String path = request.getRealPath("/");
			File file = new File(path+"WEB-INF/game_init_config");
			String str = getSonFileList(file,0);
			out.print("<B><font color=red>game_init_config/</font></B><br>");
			out.print(str);
		%>
	
	
	</table>
</body>
	
</html>