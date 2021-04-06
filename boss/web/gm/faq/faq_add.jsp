<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.faq.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/style.css"/>
</style>
<title>添加FAQ文章</title>
<script type="text/javascript">
	function $(tag){
	    return document.getElementById(tag);
	}
	
	function sub(){
		$("faq").action="?action=add"
		var servername = $("servername").value;
		var modle = $("modle").value;
		var recorder = $("recorder").value;
		var createtime = $("createtime").value;
		var title = $("title").value;
		var content = $("content").value;
		if(servername&&modle&&recorder&&createtime&&title&&content){
			$("faq").submit();	
		}else{
			alert("信息填写不完整");
		}
	}
	
	function onchange1(value){
		var str = "?gamename="+value;
		window.location.replace(str);
	}
	
</script>
</head>

<body bgcolor="#c8edcc">
	<h1>添加FAQ文章</h1>
	<form action="" method='post' id="faq">
		<table width='60%'>
		<%
			FaqManager fm = FaqManager.getInstance();		
			List<FaqGameModule> games = fm.getGames();
			String gamename = "";
			//浏览器会根据name来设定发送到服务器的request
			String action = request.getParameter("action");
			String servername = request.getParameter("servername");
			String modle = request.getParameter("modle");
			String recorder = request.getParameter("recorder");
			String createtime = request.getParameter("createtime");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			if(action!=null&&action.trim().equals("add")){
// 				out.print("servername:"+servername+"--modle:"+modle+"--recorder:"+recorder+"--createtime:"+createtime+"--title:"+title+"--content:"+content);
				if(servername!=null&&modle!=null&&recorder!=null&&createtime!=null&&title!=null&&content!=null){
					FaqRecord record = new FaqRecord();
					record.setGamename(servername);
					record.setModulename(modle);
					record.setRecorder(recorder);
					record.setRecordtime(createtime);
					record.setTitle(title);
					record.setContent(content);
					if(fm.addNewRecord(record)){
						out.print("<font color='red'>添加成功！");
					}else{
						out.print("<font color='red'>添加失败！");
					}
				}
			}else{
				out.print("请填写正确的信息！");
			}
			
			
		%>
			<tr><th>游戏名：</th><td><select id='servername' name='servername' onchange='onchange1(this.value)'>
			<%
				if(games.size()>0){
					Map<String,String> names = new HashMap<String,String>();
					gamename = request.getParameter("gamename");
					for(FaqGameModule game:games){
						names.put(game.getGamename(), game.getGamename());
					}
					if(gamename!=null&&gamename.trim().length()>0){
						out.print("<option value='"+gamename+"'>"+gamename+"</option>");
						for(String ss:names.values()){
							if(!ss.equals(gamename)){
								out.print("<option value='"+ss+"'>"+ss+"</option>");
							}
						}
					}else{
						for(String ss:names.values()){
							out.print("<option value='"+ss+"'>"+ss+"</option>");
						}
					}
				}else{
					out.print("<option value='无'>无</option>");
				}
			%>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="game_add.jsp"><img src="../../images/add.png" style="border-style:none;"/></a> </td></tr>
			<tr><th>基础模块：</th><td><select id='modle' name='modle'>
			<%
				if(gamename!=null&&gamename.trim().length()>0){
					for(FaqGameModule gg : games){
						if(gg.getGamename().equals(gamename)){
							out.print("<option value='"+gg.getModule()+"'>"+gg.getModule()+"</option>");	
						}
					}
				}else{
					out.print("<option value='无'>无</option>");
				}
				
			%>			
			</select></td></tr>
			<tr><th>录入人：</th><td><input type="text" name="recorder" id="recorder"></td></tr>
			<%
				String now = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
			%>
			<tr><th>录入时间：</th><td><%=now %><input type='hidden' id='createtime' name='createtime' value='<%=now %>' /></td></tr>
			<tr><th>录入标题：</th><td><input type="text" name="title" id="title"></td></tr>
			<tr><th>录入内容：</th><td><textarea id='content' name='content' style='width:80%;height:50px;'></textarea></td></tr>
			<tr><td><input type="button" value="提交" onclick='sub();'><input type='reset' value='重置' /></td></tr>
		</table>
	</form>
</body>
</html>