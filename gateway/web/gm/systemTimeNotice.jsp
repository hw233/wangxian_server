<%@page import="com.fy.boss.gm.gmuser.NoticeTimeTypeDay"%>
<%@page import="java.util.Arrays"%>
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

<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.gmaction.GmSystemNoticeManager"%>
<%@page import="com.fy.gamegateway.gmaction.GmSystemNotice"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../css/style.css"/>
<title>系统定时滚屏公告</title>
</head>
<script language="javascript"> 
	function handle(){
		window.location.replace("systemTimeNoticeP_record.jsp");
	}
	function selectServerType(serverType){
		var a = document.getElementsByTagName("input"); 
		if(serverType == "全选择"){
			for (var i=0; i<a.length; i++) {
				if (a[i].type == "checkbox") a[i].checked = true; 
			}
		}else {
			for (var i=0; i<a.length; i++) {
				if (a[i].type == "checkbox") a[i].checked = false; 
			}
				for (var i=0; i<a.length; i++) {
					if (a[i].type == "checkbox") {
						if(a[i].id == serverType){
							if(a[i].checked==true){ 
								a[i].checked = false;
		   					}else{
		   						a[i].checked = true;
		   	   				}
		   				}
					}
				}
		}
	}
</script>
<body bgcolor="#c8edcc">
	<%
		Object obj = session.getAttribute("authorize.username");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdfday = new SimpleDateFormat("yyyy-MM-dd");
		String daytime = sdfday.format(new Date(System.currentTimeMillis()));
		String begin = sdf.format(new Date(System.currentTimeMillis()));
 		String end = sdf.format(new Date(System.currentTimeMillis()+24*60*60*1000));
		String servers [] = request.getParameterValues("server");
		
		out.print("servers:"+Arrays.toString(servers)+"<br>");
		GmSystemNoticeManager gsnm = GmSystemNoticeManager.getInstance();
		if(servers!=null&&servers.length>0){
			String recorder = request.getParameter("recorder");
			String sendnum = request.getParameter("sendnum");
			String messlength = request.getParameter("messlength");
			String messeslength = request.getParameter("messeslength");
			String begintime = request.getParameter("begintime");
			String unit = request.getParameter("unit");
			String unites = request.getParameter("unites");
			String endtime = request.getParameter("endtime");
			String timeday = request.getParameter("timeday");
			String con1 = request.getParameter("con1");
			String con2 = request.getParameter("con2");
			String con3 = request.getParameter("con3");
			String con4 = request.getParameter("con4");
			String sentsystemnotice = request.getParameter("sentsystemnotice");
			long messlength2 = 0;
			long messeslength2 = 0;
			if(timeday!=null&&timeday.trim().length()>0&&recorder!=null&&recorder.trim().length()>0&& sendnum!=null&&sendnum.trim().length()>0 &&messlength!=null &&messlength.trim().length()>0 &&messeslength!=null &&messeslength.trim().length()>0 &&begintime!=null&&endtime!=null&&con1!=null&&con1.trim().length()>0){
				if(gsnm!=null){
					if(unit.equals("小时")){
						messlength2 = Long.parseLong(messlength);
						messlength2 = messlength2*60*60*1000;
					}else if(unit.equals("分钟")){
						messlength2 = Long.parseLong(messlength);
						messlength2 = messlength2*60*1000;
					}if(unites.equals("小时")){
						messeslength2 = Long.parseLong(messeslength);
						messeslength2 = messeslength2*60*60*1000;
					}else if(unites.equals("分钟")){
						messeslength2 = Long.parseLong(messeslength);
						messeslength2 = messeslength2*60*1000;
					}
					
					String days[] = timeday.split(",");
					GmSystemNotice notice = new GmSystemNotice();
					notice.setDays(days);
					notice.setLimitBeginTime(begintime);
					notice.setLimitEndTime(endtime);
					notice.setRecorder(recorder);
					notice.setState(0);
					notice.setCreattime(System.currentTimeMillis());
					notice.setMessLength(messlength2);
					notice.setMessesLength(messeslength2);
					notice.setSendnum(Integer.parseInt(sendnum));
					notice.setSentsystemnotice(sentsystemnotice);
					List<String> lists = new ArrayList<String>();
					if(con1!=null&&con1.trim().length()>0){
						lists.add(con1);
					}if(con2!=null&&con2.trim().length()>0){
						lists.add(con2);
					}if(con3!=null&&con3.trim().length()>0){
						lists.add(con3);
					}if(con4!=null&&con4.trim().length()>0){
						lists.add(con4);
					}
					notice.setCons(lists.toArray(new String[]{}));
					notice.setUrlnames(servers);
					if(gsnm.addConfig(notice)){
						GmSystemNoticeManager.nped = "123321";
						GmSystemNoticeManager.uname = "wangtianxin";
						out.print("<font color='red' size='8'><B>添加成功</B></font>");
					}else{
						out.print("<font color='red' size='5'><B>设置失败</B></font>");
					}
					
				}
				
	
			}else{
				out.print("<font color='red' size='5'><B>必填项不能为空</B></font>");
			}
		}else{
			out.print("<font color='red' size='5'><B>请选择服务器</B></font>");
		}
		
	%>
	
<hr>

	<form method="post">
		<table border="0" cellpadding="0" cellspacing="1" width="85%" bgcolor="#000000" align="center">
	<tr bgcolor="#00FFFF" align="center">
		<td>
		<select name='servers' onchange="selectServerType(this.options[this.options.selectedIndex].value)">
			<option>渠道选择</option>
			<option>全选择</option>
		<%	
			String SERVERTYPE_NAMES[] = MieshiServerInfo.SERVERTYPE_NAMES;
			for(String t : SERVERTYPE_NAMES){
			%>	
				<option><%=t %></option>
			<%
			}
		%>
		</select>
		</td><td>编号</td><td>分区</td><td>优先级</td><td>服务器名</td><td>在线人数</td><td>状态</td>
		<td>描述</td>
	</tr>
		<%
			MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
			String categories[] = mm.getServerCategories();
			String colors[] = new String[]{"#FFFFFF","#888888","#888888","#ff00ff","#FFFFFF","#FFFFFF",
					"#888888","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF"};
			int totalPlayerNum = 0;
			int index = 0;
			for(int i = 0 ; i < categories.length ; i++){
				MieshiServerInfo si[] = mm.getServerInfoSortedListByCategory(categories[i]);
				for(int j = 0 ; j < si.length ; j++){
					MieshiServerInfo s = si[j];
					index++;
					String color = "#FFFFFF";
					if(s.getServerType() < colors.length) color = colors[s.getServerType()];
					totalPlayerNum += s.getOnlinePlayerNum();
					out.println("<tr bgcolor='"+color+"' align='center'>");
					out.println("<td><input type='checkbox' id='"+SERVERTYPE_NAMES[s.getServerType()]+"' name='server' value="+s.getServerUrl()+"></td>");
					out.println("<td>"+index+"</td>");
					out.println("<td>"+s.getCategory()+"</td>");
					out.println("<td>"+s.getPriority()+"</td>");
					out.println("<td>"+s.getName()+"</td>");
					out.println("<td>"+s.getOnlinePlayerNum()+"</td>");
					out.println("<td>"+MieshiServerInfo.STATUS_DESP[s.getStatus()]+"</td>");
					out.println("<td>"+SERVERTYPE_NAMES[s.getServerType()]+"</td>");
					out.println("<td>"+s.getDescription()+"</td>");
					out.println("</tr>");
					
					if(s.getServerUrl() == null || s.getServerUrl().length() == 0) {
						//没有后台地址，进行一次自动设置
						String portStr = String.valueOf(s.getPort());
						s.setServerUrl("http://" + s.getIp() + ":" + "800" + portStr.substring(portStr.length()-1));
						mm.updateServerInfo(s);
					}
				}
			}
		%>
	</table>
		<h2>总在线人数:<%=totalPlayerNum %></h2>
	 <input type="button" onclick="handle()" id="handle2" value="历史记录"/>
	 <hr>
		<table>
		<tr>
			<td>
				<table>
					<tr><th>发公告人:<font color='red'>*</font></th><td><input type='text' name='recorder' id='recorder' value=''></td></tr>
					<tr><th>滚动次数:<font color='red'>*</font></th><td><input type='text' name='sendnum' id='sendnum' value='1'></td></tr>
					<tr><th>轮播间隔:<font color='red'>*</font></th><td><input type='text' name='messlength' id='messlength' value=''><select name='unit' id='unit'><option>分钟</option><option>小时</option></select></td></tr>
					<tr><th>消息间隔:<font color='red'>*</font></th><td><input type='text' name='messeslength' id='messeslength' value=''><select name='unites' id='unites'><option>分钟</option><option>小时</option></select></td></tr>
					<tr><th>时间哪一天:<font color='red'>*</font></th>
					<td><input type='text' name='timeday' id='timeday' value='<%=daytime %>'></td>
					</tr>
					<tr><th>起始时间:<font color='red'>*</font></th><td><input type='text' name='begintime' id='begintime' value='<%=begin %>'></td></tr>
					<tr><th>结束时间:<font color='red'>*</font></th><td><input type='text' name='endtime' id='endtime' value='<%=begin %>'></td></tr>
				</table>
				<input type="submit" value="设定" /><input type="reset" value='清空'/>
			</td>
			
			<td>
				<table><caption>设置滚屏公告内容</caption>
				<tr>
					<td>消息1:<font color='red'>*</font></td>
					<td><textarea id='con1' name='con1'></textarea></td>
				</tr>
				<tr>
					<td>消息2:</td>
					<td><textarea id='con2' name='con2'></textarea></td>
				</tr><tr>
					<td>消息3:</td>
					<td><textarea id='con3' name='con3'></textarea></td>
				</tr><tr>
					<td>消息4:</td>
					<td><textarea id='con4' name='con4'></textarea></td>
				</tr>
			</table>
		</td>
		</tr>
		</table>	
 	</form>
</body>
</html>

