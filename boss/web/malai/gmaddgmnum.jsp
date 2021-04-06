<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%>
<%@page import="com.fy.boss.gm.gmuser.TransferQuestion"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.boss.gm.gmuser.server.TransferQuestionManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" rel="stylesheet" href="../gm/css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加处理编号</title>
</head>
<body>
	<%
		String zuzhangname = request.getParameter("zuzhangname");
		String zuyuanname = request.getParameter("zuyuanname"); 
		String typename = request.getParameter("typename");
		Object obj = session.getAttribute("authorize.username");
			if(zuzhangname!=null && zuyuanname!=null){
				if(obj!=null && obj.toString().equals("wangsiyu") || obj.toString().equals("wangtianxin")){
					String mgss [] = zuyuanname.split(",");
					if(NewFeedbackQueueManager.getInstance().addGmWorkNum(typename, zuzhangname, mgss)){
						out.print("添加成功");
					}else{
						out.print("添加失败，请找相关人员核实！");
					}
				}else{
					out.print("请找王思宇统一设置编号！");
				}
			}else{
				out.print("内容不能为空！");
			}
		
	%>
	<form action="" method="post">
		<table>
			<tr><th>类型<th><select name='typename'><option>--</option><option>VIP</option></select></tr>
			<tr><th>组长名称：</th><td><input type='text' value='' name='zuzhangname'>登录后台的帐号例如:wangsiyu</td></tr>	
			<tr><th>组员编号：</th><td><input type='text' value='' name='zuyuanname'>GM编号,例如:GM01,GM02,GM03</td></tr>	
			<tr><td colspan="2"><input type="submit" value='确定'></td></tr>
		</table>
	</form>
</body>
</html>