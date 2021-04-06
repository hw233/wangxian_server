<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../gm/css/style.css" />
<title>邮件排队系统设置</title>
</head>

<body bgcolor="#c8edcc">
	<h2>邮件系统设置和监控</h2>
	<hr>
	<%
		Object obj = session.getAttribute("authorize.username");
		String iscommit = request.getParameter("iscommit");
		NewFeedbackQueueManager manager = NewFeedbackQueueManager.getInstance();
		int 反馈缓存数 = manager.getFbCache().size();
		int 对话缓存数 = manager.getTalkCache().size();
		int 普通队列数 = manager.getQueue().size();
		int VIP队列数 = manager.getVipqueue().size();
		int GM临时反馈缓存数 = manager.getRecordIds().size();
		int GM临时对话缓存数 = manager.getTalks().size();
		int 玩家删除的列表缓存数 = manager.getDels().size();
		int VIP队列最大可排队数 = manager.getMaxVipQueueNum();
		int 普通队列最大可排队数 = manager.getMaxQueueNum();
		String 客服电话 = manager.getGmTelephone();
		int 客户端最大可显示的反馈条数 = manager.getMaxLimmitShow();
		long 系统关闭反馈时间设置 = manager.getSystemColseTime();
		String 是否开启系统关闭反馈 = manager.isStart()==true?"是":"否";
		if(iscommit!=null && iscommit.equals("true")){
			if(obj!=null && obj.toString().equals("wangtianxin")){
				String maxvipqueuenum = request.getParameter("maxvipqueuenum");
				String maxqueuenum = request.getParameter("maxqueuenum");		
				String gmtelephone = request.getParameter("gmtelephone");
				String maxshownum = request.getParameter("maxshownum");
				String systemColseTime = request.getParameter("systemColseTime");
				String isstartsystemColseTime = request.getParameter("isstartsystemColseTime");
				if(maxvipqueuenum!=null&&maxvipqueuenum.trim().length()>0){
					manager.setMaxVipQueueNum(Integer.parseInt(maxvipqueuenum));
					out.print("VIP队列最大可排队数-设置成功:"+maxvipqueuenum);
				}
				if(maxqueuenum!=null&&maxqueuenum.trim().length()>0){
					manager.setMaxQueueNum(Integer.parseInt(maxqueuenum));
					out.print("普通队列最大可排队数-设置成功:"+maxqueuenum);
				}
				if(gmtelephone!=null&&gmtelephone.trim().length()>0){
					manager.setGmTelephone(gmtelephone);
					out.print("客服电话-设置成功:"+gmtelephone);
				}
				if(maxshownum!=null&&maxshownum.trim().length()>0){
					manager.setMaxLimmitShow(Integer.parseInt(maxshownum));
					out.print("客户端最大可显示的反馈条数-设置成功:"+maxshownum);
				}
				if(systemColseTime!=null&&systemColseTime.trim().length()>0){
					manager.setSystemColseTime(Long.parseLong(systemColseTime));
					out.print("系统关闭反馈时间设置-设置成功:"+systemColseTime);
				}
				if(isstartsystemColseTime!=null&&isstartsystemColseTime.trim().length()>0){
					if(isstartsystemColseTime.equals("是")){
						manager.setStart(true);
					}
					if(isstartsystemColseTime.equals("否")){
						manager.setStart(false);
					}
					out.print("是否开启系统关闭反馈-设置成功:"+isstartsystemColseTime);
				}
				NewFeedbackStateManager.logger.warn("[邮件系统设置和监控] [设置人："+obj.toString()+"] [maxvipqueuenum:"+maxvipqueuenum+"] [maxqueuenum:"+maxqueuenum+"] [gmtelephone:"+gmtelephone+"] [maxshownum:"+maxshownum+"]");
			}else{
				out.print("请申请设置权限！");
			}
		}
	%>
	<form method="post" action="?iscommit=true">
		<table>
		<br><br>
			<tr><td>VIP队列最大可排队数：</td><td><input type='text' name='maxvipqueuenum'><%=VIP队列最大可排队数 %></td></tr>	
			<tr><td>普通队列最大可排队数：</td><td><input type='text' name='maxqueuenum'><%=普通队列最大可排队数 %></td></tr>	
			<tr><td>客服电话：</td><td><input type='text' name='gmtelephone'><%=客服电话 %></td></tr>	
			<tr><td>客户端最大可显示的反馈条数：</td><td><input type='text' name='maxshownum'><%=客户端最大可显示的反馈条数 %></td></tr>
			<tr><td>系统关闭反馈时间设置：</td><td><input type='text' name='systemColseTime'><%=系统关闭反馈时间设置 %></td></tr>
			<tr><td>是否开启系统关闭反馈：</td><td><input type='text' name='isstartsystemColseTime'><%=是否开启系统关闭反馈 %></td></tr>		
			<tr><td>反馈缓存数：</td><td><%=反馈缓存数 %></td></tr>	
			<tr><td>对话缓存数：</td><td><%=对话缓存数 %></td></tr>	
			<tr><td>普通队列数：</td><td><%=普通队列数 %></td></tr>		
			<tr><td>VIP队列数：</td><td><%=VIP队列数 %></td></tr>	
			<tr><td>GM临时反馈缓存数：</td><td><%=GM临时反馈缓存数 %></td></tr>
			<tr><td>GM临时对话缓存数：</td><td><%=GM临时对话缓存数 %></td></tr>
			<tr><td>玩家删除的列表缓存数：</td><td><%=玩家删除的列表缓存数 %></td></tr>
			<tr><input type="submit" value='设置'></tr>
	</table>
	</form>
</body>
</html>

