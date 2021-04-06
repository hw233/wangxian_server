<%@page import="com.fy.boss.gm.gmuser.NoticeTimeTypeDay"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.boss.gm.gmuser.GmSystemNotice"%>
<%@page import="com.fy.boss.gm.gmuser.server.GmSystemNoticeManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.boss.gm.record.TelRecord"%>
<%@page import="com.fy.boss.gm.record.TelRecordManager"%>
<%@page import="com.fy.boss.gm.record.ActionManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="style.css"/>
<title>设定公告记录</title>
</head>
<script language="javascript"> 
	function allcheaked(){ 
		var a = document.getElementsByTagName("input");
		if(a[0].checked==true){ 
			for (var i=0; i<a.length; i++) {
				if (a[i].type == "checkbox") a[i].checked = false; 
			}
			document.getElementById('allche').value = "全选"; 
		}else { 
			for (var i=0; i<a.length; i++) {
				if (a[i].type == "checkbox"){
					a[i].checked = true;
				} 
			}
			document.getElementById('allche').value = "取消全选"; 
		} 
	} 
	
	function quxiao(){
		window.open("?quxiao=true");
	}
	function deletenotice(id){
		if(window.confirm("确定要删除该公告吗？")){
			window.location.replace("?shanchu=true&id="+id);
		}
	}
</script>
<body bgcolor="#c8edcc">
	<%
		Object obj = session.getAttribute("authorize.username");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfday = new SimpleDateFormat("yyyy-MM-dd");
		String daytime = sdfday.format(new Date(System.currentTimeMillis()));
		String begin = sdf.format(new Date(System.currentTimeMillis()));
// 		String end = sdf.format(new Date(System.currentTimeMillis()+24*60*60*1000));
		String servers [] = request.getParameterValues("server");
		GmSystemNoticeManager gsnm = GmSystemNoticeManager.getInstance();
		String shanchu = request.getParameter("shanchu");
		String id = request.getParameter("id");
		List<GmSystemNotice> list = gsnm.getNoticesConfig();
		if(shanchu!=null && shanchu.equals("true")){
			for(int i=0;i<list.size();i++){
				if(list.get(i).getId()==Long.parseLong(id)){
// 					out.print("第"+i+"条："+list.get(i).getRecorder()+"--list记录id："+list.get(i).getId()+"--id:"+id+"--state:"+list.get(i).getState()+"--gsnm.size():"+gsnm.getNotices().size() +"list.size"+list.size()+"<br>");
					list.get(i).setState(1);
					gsnm.getNotices().get(i).setState(1);
					gsnm.getNotices().remove(list.get(i));
// 					out.print("第"+i+"条："+list.get(i).getRecorder()+"--list记录id："+list.get(i).getId()+"--id:"+id+"--state:"+list.get(i).getState()+"--gsnm.size():"+gsnm.getNotices().size() +"list.size"+list.size()+"<br>");
				}
			}
		}		
		
		
	%>
	
	<div id='messid'></div>
	<hr>
	历史设定公告记录
	<form method="post">
	 <hr>
		<table>
		<tr><th>设定人</th><th>创建时间</th><th>有限时间段</th><th>上次发送时间</th><th>下次发送时间</th><th>轮播间隔</th><th>消息间隔</th><th>滚动次数</th><th>是否有效</th><th>服务器数</th><th>内容</th><th>操作</th></tr>
		<%
			if(list!=null && list.size()>0){
				for(GmSystemNotice notice:list){
					if(notice.getRecorder()!=null){
						String lastsenttime = "还没开始发送";
						if(notice.getLastSendTime()>0){
							lastsenttime = sdf.format(notice.getLastSendTime());
						}
		%>			
			<tr><td><%=notice.getRecorder() %></td><td><%=sdf.format(notice.getCreattime()) %></td><td><%=Arrays.toString(notice.getDays()) %></td><td><%=lastsenttime %></td><td><%=sdf.format(notice.getLastSendTime()+notice.getMessLength())  %></td><td><%=(notice.getMessLength()/60000)%>分钟</td><td><%=(notice.getMessesLength()/60000) %>分钟</td><td><%=notice.getSendnum() %></td><td><%=notice.isvalid(System.currentTimeMillis())==false?"否":"是" %></td><td><%=notice.getUrlnames().length %></td><td><%=Arrays.toString(notice.getCons()) %></td><td><input type='button' value='删除' name='delete' id='delete' onclick='deletenotice("<%=notice.getId()%>")'></td></tr>
		<%			
				}}
			}
		%>
		</table>	
	
 	</form>



</body>
</html>

