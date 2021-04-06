<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.fy.boss.gm.faq.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
<link rel="stylesheet" href="../css/style.css"/>
<title>添加新游戏的FAQ</title>
<script type="text/javascript">
	function $(a){
		return document.getElementById(a).value;
	}
	
	function sub(){
		var gamename = $("gamename");
		var module = $("module");
		var recorder = $("recorder");
		var createtime = $("createtime");
		var url = $("moduleurl");
		if(gamename&&module&&recorder){
			window.location.replace("game_add.jsp?action=add&gamename="+gamename+"&module="+module+"&recorder="+recorder+"&createtime="+createtime+"&url="+url);
		}else{
			alert("信息填写不完整");
		}
		
	}
	
</script>
</head>
<body bgcolor="#c8edcc">
	<h1>添加游戏FAQ</h1>
	<form action="" id="fadd">
		<table width='60%'>
			<%
				String action = request.getParameter("action");
				String gamename = request.getParameter("gamename");
				String module = request.getParameter("module");
				String recorder = request.getParameter("recorder");
				String createtime = request.getParameter("createtime");
				String url = request.getParameter("url");
				if(action!=null&&gamename!=null&&gamename.trim().length()>0&&module!=null&&module.trim().length()>0
						&&recorder!=null&&recorder.trim().length()>0&&createtime!=null&&createtime.trim().length()>0&&url!=null&&url.trim().length()>0){
					FaqManager fm = FaqManager.getInstance();
// 					List<FaqGameModule> games = fm.getGames();
// 					boolean isadd = true;
// 					for(FaqGameModule game:games){
// 						if(game.getGamename().equals(gamename)&&game.getModule().equals(module)){
// 							isadd = false;
// 						}
// 					}
// 					if(isadd){
						FaqGameModule fgm = new FaqGameModule();
						fgm.setGamename(gamename);
						fgm.setRecorder(recorder);
						fgm.setRecordtime(createtime);
						fgm.setModule(module);
						fgm.setUrl(url);
						
						if(fm.addNewGame(fgm)){
							out.print("<font color='red'>添加成功！！");
						}
// 					}else{
// 						FaqGameModule fgm = new FaqGameModule();
// 						fgm.setGamename(gamename);
// 						fgm.setRecorder(recorder);
// 						fgm.setRecordtime(createtime);
// 						fgm.setModule(module);
// 						fgm.setUrl(url);
						
// 						out.print("<font color='red'>添加成功，"+module+" 已存在 "+gamename+"下！");
// 					}
					
					
				}
				
			%>
			<tr><th>游戏名字：</th><td><input type="text" id="gamename"></td></tr>
			<tr><th>游戏模块：</th><td><input type="text" id="module"></td></tr>
			<tr><th>模块url：</th><td><input type="text" id="moduleurl"></td></tr>
			<tr><th>添加人：</th><td><input type="text" id="recorder"></td></tr>
			<%
				Date date = new	Date();
				String now = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
			%>
			<tr><th>添加时间：</th><td><%=now %><input type='hidden' id='createtime' name='createtime' value='<%=now %>' /></td></tr>
			<tr><td><input type="button" value="提交" onclick="sub();"><input type='reset' value='重置' /></td></tr>
		</table>
	</form>
</body>
</html>