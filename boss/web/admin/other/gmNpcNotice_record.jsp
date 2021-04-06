<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="com.fy.boss.gm.XmlServerManager"%>
<%@page import="com.fy.boss.gm.npcnotice.BoardItem"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.boss.gm.npcnotice.NpcNoticeManager"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="style.css"/>
<title>record</title>
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
		NpcNoticeManager gsnm = NpcNoticeManager.getInstance();
		String shanchu = request.getParameter("shanchu");
		String id = request.getParameter("id");
		List<BoardItem> list = gsnm.getNotices(new Date(),new Date());
		int deleteindex = -1;
		if(shanchu!=null && shanchu.equals("true")){
			for(int i=0;i<list.size();i++){
				if(list.get(i).getId()==Long.parseLong(id)){
					BoardItem item = list.get(i);
					Date d = new Date();
					d.setTime(1);
					item.setEndShowTime(d);
					if(gsnm.updateNotice(item)){
							deleteindex = i;
// 							list.remove(i);
					}
				}
			}
			if(deleteindex>=0){
				list.remove(deleteindex);
			}
		}		
	%>
	
	<div id='messid'></div>
	<form method="post">
		<table>
		<tr><th>类型</th><th>名称</th><th>内容</th><th>生效时间</th><th>失效时间</th><th>记录人</th><th>一般评分</th><th>好评分</th><th>很好评分</th><th>操作</th></tr>
		<%
			if(list!=null && list.size()>0){
				for(BoardItem notice:list){
			%>			
				<tr><td><%=notice.getNoticetype()%></td><td><%=notice.getTitle()%></td><td><%=notice.getContent()%></td><td><%=sdf.format(notice.getBeginShowTime()) %></td><td><%=sdf.format(notice.getEndShowTime()) %></td><td><%=notice.getAuthorName()%></td><td><%=notice.getVoteNormal() %></td><td><%=notice.getVoteGood()%></td><td><%=notice.getVoteBetter() %></td><td><input type='button' value='删除' name='delete' id='delete' onclick='deletenotice("<%=notice.getId()%>")'></td></tr>
			<%			
				}
			}
		%>
		<%
		if(deleteindex>=0){
			XmlServerManager xsm = XmlServerManager.getInstance();
			List<XmlServer> xs =  xsm.getServers();
			if(xs!=null && xs.size()>0){
				for(XmlServer server : xs){
					if(!server.getDescription().equals("网关")){
						String serverurl = server.getUri();
						serverurl = serverurl.substring(0,serverurl.indexOf("gm"))+"admin/operation/router.jsp";
						HashMap headers = new HashMap();
						String reqip = request.getLocalAddr();
						String contentP = "class=com.mieshi.korea.BoardDataProc&action=signalNewData&authorize.username=koreaAdmin&authorize.password=KoreaWXadmin252@";
						if(reqip.equals("116.213.192.200")){
							contentP = "class=com.mieshi.korea.BoardDataProc&action=signalNewData&authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p";
						}
						try{
							byte[] b = HttpUtils.webPost(new URL(serverurl), contentP.getBytes(), headers, 15000, 15000);
						}catch(Exception e){
							continue;
						}
					}
				}
			}
		}
		
		%>
		</table>	
	
 	</form>



</body>
</html>

