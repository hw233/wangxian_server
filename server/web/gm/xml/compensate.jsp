<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>任务管理</title>
		<link rel="stylesheet" href="../style.css" />
		<style type="text/css">
#a {
	font-size: 0px;
}

#a li {
	font-size: 16px;
	text-align: center;
	width: 110px;
	list-style-type: none;
	display: inline;
	padding: 5px 10px;
}
</style>
		<script type="text/javascript">
	function $(tag) {
		return document.getElementById(tag);
	}
	
	function sub() {
		var maxtime = $("maxtime").value;
		if (!maxtime) {
			alert("时间不能为空");
			return;
		}
		var title = $("title").value;
		var content = $("content").value;
		if(!title||!content){
		   alert("标题或者邮件内容不能为空！！");
		   return;
		}
		var item = $("item1").value;
		if(item.length==0){
		   alert("礼品不能为空！");
		   return;
		}
		   
		$("f1").submit();
	}
	function start() {
		if (window.confirm("确认开始！！！"))
			window.location.replace("compensate.jsp?action=start");
	}
	function stop(){
	   	if (window.confirm("确认结束！！！"))
			window.location.replace("compensate.jsp?action=stop");
	
	}
</script>
	</head>
	<body>
		<%
			try {
				String gmusername = session.getAttribute("username").toString();
				out.print("<input type='button' value='刷新' onclick='window.location.replace(\"compensate.jsp\");' />");
				GMAutoSendMail gmsendmail = GMAutoSendMail.getInstance();
				GmItemManager gimanager = GmItemManager.getInstance();
				List<String> gmitems = gimanager.getItemNames();
				out.print(gmsendmail.isRunning()?"正在发送":"停止发送");
				String action = request.getParameter("action");
				ActionManager amanager = ActionManager.getInstance();
				if (action != null && action.length() > 0) {  
					if ("add".equals(action.trim())) {
						String[] ars = request.getParameterValues("item");
						if (ars != null && ars.length>0 && gmsendmail.setArslist(ars)) {
						    StringBuffer sb = new StringBuffer();
						    for(String s :ars)
						      sb.append(s+"|");
							String maxtime = request.getParameter("maxtime");
							String title = request.getParameter("title");
							String content = request.getParameter("content");
							try {
								gmsendmail.setStop(Integer.parseInt(maxtime
										.trim())*60000);
								gmsendmail.setTitle(title);
								gmsendmail.setContent(content);
								out.print("添加成功["+title+"]["+content+"]["+maxtime+"]["+sb.toString()+"]");
								amanager.save(gmusername,"添加补偿邮件成功["+title+"]["+content+"]["+maxtime+"]["+sb.toString()+"]");
							} catch (Exception e) {
								out.print("请输入正确格式的时间!!");
							}
						}
					}
					else if("start".equals(action.trim())){
						//out.print(" gmsendmail 文件地址   2" +gmsendmail.getCompensateFile());
					  if(gmsendmail.getStop()!=-1&&gmsendmail.getArslist().length>0){
					    gmsendmail.start();
					    out.print("<br/>开启补偿邮件成功！");
					    amanager.save(gmusername,"开启补偿邮件成功！");
					    }
					}
					else if("stop".equals(action.trim())){
						//out.print(" gmsendmail 文件地址  3" +gmsendmail.getCompensateFile());
					  if(gmsendmail.isRunning()){
					     gmsendmail.stop();
					     out.print("<br/>邮件补偿关闭！！");
					     amanager.save(gmusername,"关闭邮件补偿！");
					     }
					     
					}
				}
               
				out
						.print("<form action='?action=add' method='post' id='f1'> <table width = '60%' align='center' ><caption>补偿设置 </caption>");
				//out
				//		.print("<tr><th>补偿礼品名</th><td class='top'><div id='d1' >礼品1：<input type='text' name='item' id='item0' value=''/> </div><input type='button' value='新增礼品' onclick='add();' /></td></tr>");
				out.print("<tr><th>补偿礼品名</th><td class='top'><select name='item' id='item1' multiple=true size='5' >");
				for (String item : gmitems)
					out.print("<option value='" + item + "' >" + item
							+ "</option>");
				out.print("</select></td></tr>");
				out
						.print("<tr><th>补偿时间</th><td><input type='text' name='maxtime' id='maxtime' value='' />分钟</td></tr>");
				out.print("<tr><th>邮件标题</th><td><input type='text' name='title' id='title' value='玩家重启补偿' /></td></tr>");
				out.print("<tr><th>邮件内容</th><td><input type='text' name='content' id='content' value='感谢您的参与！祝您游戏愉快！' /></td></tr>");
				out
						.print("<tr><td colspan='2' ><input type='button' value='确认' onclick='sub();' /><input type='button' value='开始' onclick='start();' /><input type='button' value='结束' onclick='stop();' /></td></tr></table></form>");

			} catch (Exception e) {
				//e.printStackTrace();
				//out.print(e.getMessage());
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				//rdp.forward(request, response);
				out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
