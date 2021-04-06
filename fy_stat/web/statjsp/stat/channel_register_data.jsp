<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>管理后台</title>
<link rel="stylesheet" href="../css/style.css"/>
<link rel="stylesheet" type="text/css" href="../calcss/iframe.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../calcss/calendar.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../calcss/dashboard.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../calcss/i-heart-ny.css" media="screen" />
<script type="text/javascript" src="../js/mootools.js"></script>
<script type="text/javascript" src="../js/calendar.rc4.js"></script>
<script language="JavaScript">
<!--
window.addEvent('domready', function() { 
	myCal1 = new Calendar({ start: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
	myCal2 = new Calendar({ end: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
});

function subcheck() {
	document.getElementById("subed").value = "true";
	document.f1.submit();
}
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>渠道新用户实时数据(按手机计算独立用户)</h1>
	<%
	ChannelManager cmanager = ChannelManager.getInstance();
	ChannelItemManager cimanager = ChannelItemManager.getInstance();
	UserStatManager usmanager = UserStatManager.getInstance();
	
	String start = ParamUtils.getParameter(request, "start", DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
	String end = ParamUtils.getParameter(request, "end", DateUtil.formatDate(new Date(),"yyyy-MM-dd"));

	java.util.Date startdate = DateUtil.parseDate(start + " 00:00:00","yyyy-MM-dd HH:mm:ss");
	java.util.Date enddate = DateUtil.parseDate(end + " 00:00:00","yyyy-MM-dd HH:mm:ss");
	if(startdate.getTime() > enddate.getTime()) {
		out.println("<script>window.alert('开始日期不能大于结束日期');</script>");
		return;
	}
	List<java.util.Date> dlist = new ArrayList<java.util.Date>();
	Calendar cal = Calendar.getInstance();
	cal.setTime(startdate);
	String dstr = DateUtil.formatDate(cal.getTime(), "yyyy-MM-dd");
	String estr = DateUtil.formatDate(enddate, "yyyy-MM-dd");
	while( !dstr.equals(estr) ) {
		dlist.add(cal.getTime());
		cal.add(Calendar.DAY_OF_YEAR, 1);
		dstr = DateUtil.formatDate(cal.getTime(), "yyyy-MM-dd");
	}
	dlist.add(cal.getTime());
	List<Channel> channels = cmanager.getChannels();
	
	long channelid = -1;
	String ch = request.getParameter("channel");
	if(ch != null) {
		channelid = Long.parseLong(ch);
	}
	String showChannel = user.getProperty("channel");
	String subed = request.getParameter("subed");
	long channelitemid = Long.parseLong(ParamUtils.getParameter(request,"channelitem","-1"));
	%>
	<form action="#" method="post" name=f1>
	
	<table align="center" cellpadding="0" cellspacing="0" border="0" width="80%">
	 <tr>
	 	<td colspan="20" align="left" style="border-top:1px #ccc solid;">
	 	<p style="clear:both;text-align:left;">
		<label for="start">开始日期:</label><input type="text" id="start" name="start" size="10" value="<%=start %>"/>
		<label for="end">结束日期:</label><input type="text" id="end" name="end" size="10" value="<%=end %>"/>
		<label for="channel">渠道:</label><select name=channel onchange="f1.submit()">
		<option value="-2">所有渠道</option>	
			<%
			for(Channel channel : channels) {
				if(showChannel != null && showChannel.length() > 0 && showChannel.indexOf(channel.getName()) == -1) {
					continue;
				}
			%>
			<option value="<%=channel.getId() %>" <%if(channel.getId() == channelid) out.print("selected");%>><%=channel.getName() %></option>	
			<%} %>
		</select>
		<%
		if(channelid != -1) {
			Channel channel = cmanager.getChannel(channelid);
			if(channel!=null) {
			List<ChannelItem> items = cimanager.getChannelItemsByChannel(channel);
			if(items.size() > 1) {
		 %>
		<label for="channelitem">子渠道:</label><select name=channelitem>
			<option value="-1">所有</option>
			<%for(ChannelItem item : items) {%>
			<option value="<%=item.getId() %>" <%if(item.getId() == channelitemid) out.print("selected");%>><%=item.getName() %></option>
			<%} %>
		</select>
		<%}}} %>
		<input type=hidden id="subed" name="subed" value="false"/>
		<input type=button name=bt1 value="查询" onclick="subcheck()"/>
		</p>
		</td>
	 </tr>
	</table><br/>
	<%if(channelid != -2) {%>
	<table id="test1" align="center" width="80%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
     <tr>
      <th width=30%><b>日期</b></th>
      <th width=70%><b>新用户数</b></th>
     </tr>  
     <%
     int total = 0;
     if(subed != null && subed.equals("true")) {
     for(int j=0; j<dlist.size(); j++) {
     	String datestr = DateUtil.formatDate(dlist.get(j),"yyyy-MM-dd");
     	Date starttime = DateUtil.parseDate(datestr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
     	Date endtime = DateUtil.parseDate(datestr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
  		int num = 0;
  		if(channelitemid == -1) { 
  			num = usmanager.getChannelUniqMobileNormalUserNum(channelid, starttime, endtime, 1);
  		} else {
  			num = usmanager.getChannelUniqMobileNormalUserNum(channelid, channelitemid, starttime, endtime, 1);
  		}
  		total += num;
      %>
	 <tr>
		<td><%=datestr %></td>
		<td><%=num %></td>
	 </tr> 
	<%} %>
	 <tr>
		<td>总 计 : </td>
		<td><%=total %></td>
	 </tr>
	 <%}%>
    </table>
    <%}else { 
    List<Channel> cs = cmanager.getChannels();
    for(Channel channel : cs) {
    %>
    <table id="test1" align="center" width="80%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
     <tr>
      <th width=40%><b>渠道</b></th>
      <th width=30%><b>日期</b></th>
      <th width=30%><b>新用户数</b></th>
     </tr>  
     <%
     int total = 0;
     if(subed != null && subed.equals("true")) {
     for(int j=0; j<dlist.size(); j++) {
     	String datestr = DateUtil.formatDate(dlist.get(j),"yyyy-MM-dd");
     	Date starttime = DateUtil.parseDate(datestr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
     	Date endtime = DateUtil.parseDate(datestr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
  		int num = usmanager.getChannelUniqMobileNormalUserNum(channel.getId(), starttime, endtime, 1);
  		total += num;
      %>
	 <tr>
		<td><%=channel.getName() %></td>
		<td><%=datestr %></td>
		<td><%=num %></td>
	 </tr> 
	<%} %>
	 <tr>
		<td colspan="2">总 计 : </td>
		<td><%=total %></td>
	 </tr>
	 <%}%>
    </table><br>
    <%}} %>
    <br/><br/>
	</form>
	<font color=red>*查询实时数据，按手机号计算独立用户，且必须已创建角色</font>
</body>
</html>
