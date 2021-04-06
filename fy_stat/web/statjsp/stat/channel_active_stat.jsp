<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Portal管理后台</title>
<script type="text/javascript" src="../js/tablesort.js"></script>
<link rel="stylesheet" href="../css/style.css"/>
<link rel="stylesheet" href="../css/atalk.css" />
<link rel="stylesheet" type="text/css" href="../calcss/iframe.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../calcss/calendar.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../calcss/dashboard.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../calcss/i-heart-ny.css" media="screen" />
<script type="text/javascript" src="../js/mootools.js"></script>
<script type="text/javascript" src="../js/calendar.rc4.js"></script>
<script type="text/javascript" src="/stat_common/dwr/engine.js"></script>
<script type="text/javascript" src="/stat_common/dwr/interface/ChannelBoundry.js"></script>
<script type="text/javascript" src="/stat_common/dwr/interface/DWRManager.js"></script>
<script type="text/javascript" src="/stat_common/dwr/util.js"></script>
<script language="JavaScript">
<!-- 

window.addEvent('domready', function() { 
	myCal1 = new Calendar({ regstart: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
	myCal2 = new Calendar({ regend: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
	myCal3 = new Calendar({ instart: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
	myCal4 = new Calendar({ inend: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
});

function operate() {
	over(2);
}

function unover(index)
{
  	document.getElementById("overlay").style.display="none";
  	document.getElementById("lightbox"+index).style.display="none";
  	document.getElementById("lbody"+index).innerHTML = lbodyOrig;
}

function over(index)
{
	var obj = $('overlay');
 	obj.style.display="block";
	
	if(document.body.offsetHeight>=document.documentElement.clientHeight){
		obj.style.height = document.body.offsetHeight + 'px'; 
	}else{
		obj.style.height = document.documentElement.clientHeight + 'px';
	}
	var currentOpacity = 0;
	//设置定时器timer1
	var timer1 = setInterval(
		function(){
			if(currentOpacity<=50){
				setOpacity(obj,currentOpacity);
				currentOpacity+=1;	
			}
			else{
				clearInterval(timer1);
			}
		}
	,10);
	
	document.getElementById("lightbox"+index).style.display="block";
}

//设置透明度的函数
function setOpacity(elem,current){
	//如果是ie浏览器
	if(elem.filters){ 
		elem.style.filter = 'alpha(opacity=' + current + ')';
	}else{ //否则w3c浏览器
		elem.style.opacity = current/100;
	}
}

function selectChannel() {
	var channel = document.getElementById("channel");
	var channelitem = document.getElementById("channelitem");
	var cid = channel.options[channel.selectedIndex].value;
	if(cid == "-1") {
		channelitem.length == 1;
		channelitem.options[0] = new Option("所有", "-1");
	} else {
		ChannelBoundry.getChannelItems(cid, afterSelectChannel);
	}
}

function afterSelectChannel(citems) {
	var channelitem = document.getElementById("channelitem");
	channelitem.length == 1;
	channelitem.options[0] = new Option("所有", "-1");
	for(var i=0; i<citems.length; i++) {
		var str = citems[i].split("#");
		channelitem.options[i+1] = new Option(str[0], str[1]);
	}
}

function stat() {
	over(2);
	document.f1.submit();
}
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>分渠道活跃率统计</h1>
	<%
	String message = null;
	String regstart = ParamUtils.getParameter(request, "regstart", DateUtil.formatDate(new Date(), "yyyy-MM-dd") );
	String regend = ParamUtils.getParameter(request, "regend", DateUtil.formatDate(new Date(), "yyyy-MM-dd") );
	java.util.Date regstartdate = DateUtil.parseDate(regstart + " 00:00:01","yyyy-MM-dd HH:mm:ss");
	java.util.Date regenddate = DateUtil.parseDate(regend + " 00:00:01","yyyy-MM-dd HH:mm:ss");
	String instart = ParamUtils.getParameter(request, "instart", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
	String inend = ParamUtils.getParameter(request, "inend", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
	java.util.Date instartdate = DateUtil.parseDate(instart + " 00:00:01","yyyy-MM-dd HH:mm:ss");
	java.util.Date inenddate = DateUtil.parseDate(inend + " 00:00:01","yyyy-MM-dd HH:mm:ss");
	if(regstartdate.getTime() > regenddate.getTime() || instartdate.getTime() > inenddate.getTime()) {
		out.println("<script>window.alert('起始日期不能晚于结束日期');location.replace('channel_active_stat.jsp');</script>");
		return;
	}
	ChannelManager cmanager = ChannelManager.getInstance();
	ChannelItemManager cimanager = ChannelItemManager.getInstance();
	List<Channel> clist = cmanager.getChannels();
	long channelid = Long.parseLong(ParamUtils.getParameter(request, "channel", "-1"));
	long channelitemid = Long.parseLong(ParamUtils.getParameter(request, "channelitem", "-1"));
	UserStatManager umanager = UserStatManager.getInstance();
	PlayerLoginManager pmanager = PlayerLoginManager.getInstance();
	int stype = ParamUtils.getIntParameter(request, "stype", 0);
	%>
		<form action="#" method="post" name=f1>
		<table align="left" cellpadding="0" cellspacing="0" border="0" width="98%">
		 <tr>
		 	<td align="left" style="border-top:1px #ccc solid;">
		 	<p style="clear:both;text-align:left;">
			<label for="regstart">注册开始:</label><input type="text" id="regstart" name="regstart" size="10" value="<%=regstart %>"/>
			<label for="regend">注册结束:</label><input type="text" id="regend" name="regend" size="10" value="<%=regend %>"/>
			<label for="instart">登陆开始:</label><input type="text" id="instart" name="instart" size="10" value="<%=instart %>"/>
			<label for="inend">登陆结束:</label><input type="text" id="inend" name="inend" size="10" value="<%=inend %>"/>
			</p>
			<p style="clear:both;text-align:left;">
			<label for="channel">渠道:</label><select name="channel" id="channel" onchange="selectChannel()">
				<option value="-1">所有</option>
				<%for(Channel c : clist) {%>
				<option value="<%=c.getId() %>" <%if(c.getId()==channelid) out.print("selected");%>><%=c.getName() %></option>
				<%} %>
			</select>
			<label for="channelitem">子渠道:</label><select name="channelitem" id="channelitem">
				<option value="-1">所有</option>
			</select>
			<label for="stype">统计方式:</label><select name="stype" id="stype">
				<option value="0" <%if(stype == 0) out.print("selected");%>>按日</option>
				<option value="1" <%if(stype == 1) out.print("selected");%>>按周</option>
				<option value="2" <%if(stype == 2) out.print("selected");%>>按月</option>
			</select>
			<input type=hidden name=subed value="true"/>
			<input type=button name=bt1 value="查询" onclick="stat()"/>
			</p>
			</td>
		 </tr>
	    </table><br/>
		<%
		String subed = request.getParameter("subed");
		if(subed != null) {
		if(stype == 0) {
			Calendar cal = Calendar.getInstance();
			List<Date> regdays = new ArrayList<Date>();
			cal.setTime(regstartdate);
			while(!DateUtil.formatDate(cal.getTime(),"yyyyMMdd").equals(DateUtil.formatDate(regenddate,"yyyyMMdd"))) {
				regdays.add(cal.getTime());
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			regdays.add(regenddate);
			List<Date> indays = new ArrayList<Date>();
			cal.setTime(instartdate);
			while(!DateUtil.formatDate(cal.getTime(),"yyyyMMdd").equals(DateUtil.formatDate(inenddate,"yyyyMMdd"))) {
				indays.add(cal.getTime());
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			indays.add(inenddate);
				
			%>
	   	<table id="test1" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	    	<tr>
		      <th rowspan=2 width="60"><b>渠道</b></th>
		      <th rowspan=2 width="60"><b>子渠道</b></th>
		      <th rowspan=2 width="60"><b>注册日期</b></th>
		      <th rowspan=2 width="60"><b>注册数</b></th>
		      <th colspan="<%=indays.size() %>"><b>活跃用户数</b></th>
		      <th colspan="<%=indays.size() %>"><b>活跃率</b></th>
		    </tr>
		    <tr>
		      <%for(Date inday : indays) {%>
		      <th width="60"><b><%=DateUtil.formatDate(inday,"yyyy-MM-dd") %></b></th>
		      <%} %>
		      <%for(Date inday : indays) {%>
		      <th width="60"><b><%=DateUtil.formatDate(inday,"yyyy-MM-dd") %></b></th>
		      <%} %>
		    </tr>
		    <%
		    Channel c = null;
		    if(channelid != -1) {
		    	c = cmanager.getChannel(channelid);
		    }
		    ChannelItem ci = null;
		    if(channelitemid != -1) {
		    	ci = cimanager.getChannelItem(channelitemid);
		    }
		    
		    for(Date rdate : regdays) {
		    	//获取当天这个渠道注册的用户数
		    	cal.setTime(rdate);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 1);
				Date starttime = cal.getTime();
				cal.add(Calendar.DAY_OF_YEAR, 1);
				cal.set(Calendar.SECOND, 0);
				Date endtime = cal.getTime();
		    	int regnum = 0;
		    	//获取当天这个渠道的成本预估
		    	if(c != null && ci != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid, channelitemid);
		    	} else if(c != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid);
		    	} else if(c == null && ci == null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime);
		    	}
		    %>
		    <tr>
		    	<td>
		    		<%=c==null?"所有":c.getName() %>
		    	</td>	
		    	<td>
		    		<%=ci==null?"所有":ci.getName() %>
		    	</td>	
		    	<td>
		    		<%=DateUtil.formatDate(rdate, "yyyy-MM-dd") %>
		    	</td>	
		    	<td>
		    		<%=regnum %>
		    	</td>	
		    	<%
		    	float fs[] = new float[indays.size()];
		    	int index = 0;
		    	for(Date idate : indays) {
			    	cal.setTime(idate);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 1);
					Date istart = cal.getTime();
					cal.add(Calendar.DAY_OF_YEAR, 1);
					cal.set(Calendar.SECOND, 0);
					Date iend = cal.getTime();
		    	 %>
		    	<td>
		    		<%
		    		if(idate.getTime() >= rdate.getTime()) {
		    			int actnum = 0;
		    			if(c != null && ci != null) {
		    				actnum = pmanager.getNewUserLoginNumByChannel(c.getId(), ci.getId(), starttime, endtime, istart, iend); 
		    			} else if(c != null) {
		    				actnum = pmanager.getNewUserLoginNumByChannel(c.getId(),starttime, endtime, istart, iend); 
		    			} else if(c == null && ci == null) {
		    				actnum = pmanager.getNewUserLoginNum(starttime, endtime, istart, iend); 
		    			}
		    			out.print(actnum);		  	    				
		    			fs[index] = 0;
		    			if(regnum != 0) {
		    				fs[index] = NumberUtils.round(new Float(actnum)*100/new Float(regnum), 1);
		    			}
		    		} else {
		    			out.print("N/A");
		    			fs[index] = 0;
		    		}
		    		index++;
		    		%>
		    	</td>
		    	<%} for(float f : fs) {%>
		    	<td>
		    		<%=f %>%
		    	</td>
		    	<%} %>
		    </tr>
		    <%} %>
	    </table>
		<%}
		if(stype == 1) {	
			List<Date[]> regweeks = DateUtil.splitTime(regstartdate, regenddate, 1);
			List<Date[]> inweeks = DateUtil.splitTime(instartdate, inenddate, 1);
			%>
	   	<table id="test1" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	    	<tr>
		      <th rowspan=2 width="60"><b>渠道</b></th>
		      <th rowspan=2 width="60"><b>子渠道</b></th>
		      <th rowspan=2 width="60"><b>注册日期</b></th>
		      <th rowspan=2 width="60"><b>注册数</b></th>
		      <th colspan="<%=inweeks.size() %>"><b>活跃用户数</b></th>
		      <th colspan="<%=inweeks.size() %>"><b>活跃率</b></th>
		     </tr>
		     <tr>
		      <%for(Date[] inday : inweeks) {%>
		      <th width="60">
		      	<b>
		      		<%=DateUtil.formatDate(inday[0],"第w周") %>
		      		<br/>(<%=DateUtil.formatDate(inday[0],"MM-dd") %>~<%=DateUtil.formatDate(inday[1],"MM-dd") %>)
		      	</b>
		      </th>
		      <%} for(Date[] inday : inweeks) {%>
		      <th width="60">
		      	<b>
		      		<%=DateUtil.formatDate(inday[0],"第w周") %>
		      		<br/>(<%=DateUtil.formatDate(inday[0],"MM-dd") %>~<%=DateUtil.formatDate(inday[1],"MM-dd") %>)
		      	</b>
		      </th>
		      <%} %>
		    </tr>
		    <%
		    Channel c = null;
		    if(channelid != -1) {
		    	c = cmanager.getChannel(channelid);
		    }
		    ChannelItem ci = null;
		    if(channelitemid != -1) {
		    	ci = cimanager.getChannelItem(channelitemid);
		    }
		    for(Date[] rdate : regweeks) {
		    	//获取当周这个渠道注册的用户数
				Date starttime = rdate[0];
				Date endtime = rdate[1];
				Calendar cal = Calendar.getInstance();
				cal.setTime(starttime);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				starttime = cal.getTime();
				cal.setTime(endtime);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				endtime = cal.getTime();
		    	int regnum = 0;
		    	//获取当周这个渠道的成本预估
		    	if(c != null && ci != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid, channelitemid);
		    	} else if(c != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid);
		    	} else if(c == null && ci == null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime);
		    	}
		    %>
		    <tr>
		    	<td>
		    		<%=c==null?"所有":c.getName() %>
		    	</td>	
		    	<td>
		    		<%=ci==null?"所有":ci.getName() %>
		    	</td>	
		    	<td>
		    		<%=DateUtil.formatDate(rdate[0], "第w周") %><br/>
		    		<%=DateUtil.formatDate(rdate[0],"MM-dd") %>~<%=DateUtil.formatDate(rdate[1],"MM-dd") %>
		    	</td>	
		    	<td>
		    		<%=regnum %>
		    	</td>	
		    	<%
		    	float fs[] = new float[inweeks.size()];
		    	int index = 0;
		    	for(Date[] idate : inweeks) {
					Date istart = idate[0];
					Date iend = idate[1];
					cal = Calendar.getInstance();
					cal.setTime(istart);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					istart = cal.getTime();
					cal.setTime(iend);
					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 59);
					cal.set(Calendar.SECOND, 59);
					iend = cal.getTime();
		    	 %>
		    	<td>
		    		<%
		    		if(iend.getTime() >= starttime.getTime()) {
		    			int actnum = 0;
		    			if(c != null && ci != null) {
		    				actnum = pmanager.getNewUserLoginNumByChannel(c.getId(), ci.getId(), starttime, endtime, istart, iend); 
		    			} else if(c != null) {
		    				actnum = pmanager.getNewUserLoginNumByChannel(c.getId(),starttime, endtime, istart, iend); 
		    			} else if(c == null && ci == null) {
		    				actnum = pmanager.getNewUserLoginNum(starttime, endtime, istart, iend); 
		    			}
		    			out.print(actnum);		    				
		    			fs[index] = 0;
		    			if(regnum != 0) {
		    				fs[index] = NumberUtils.round(new Float(actnum)*100/new Float(regnum), 1);
		    			}
		    		} else {
		    			out.print("N/A");
		    			fs[index] = 0;
		    		}
		    		index++;
		    		%>
		    	</td>
		    	<%} for(float f : fs) {%>
		    	<td>
		    		<%=f %>%
		    	</td>
		    	<%} %>
		    </tr>
		    <%} %>
	    </table>
		<%}
		if(stype == 2) {	
			List<Date[]> regmonths = DateUtil.splitTime(regstartdate, regenddate, 2);
			List<Date[]> inmonths = DateUtil.splitTime(instartdate, inenddate, 2);
			%>
	   	<table id="test1" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	    	<tr>
		      <th rowspan=2 width="60"><b>渠道</b></th>
		      <th rowspan=2 width="60"><b>子渠道</b></th>
		      <th rowspan=2 width="60"><b>注册日期</b></th>
		      <th rowspan=2 width="60"><b>注册数</b></th>
		      <th colspan="<%=inmonths.size() %>"><b>活跃用户数</b></th>
		      <th colspan="<%=inmonths.size() %>"><b>活跃率</b></th>
		     </tr>
		     <tr>
		      <%for(Date[] inday : inmonths) {%>
		      <th width="60">
		      	<b>
		      		<%=DateUtil.formatDate(inday[0],"M月") %>
		      		<br/>(<%=DateUtil.formatDate(inday[0],"MM-dd") %>~<%=DateUtil.formatDate(inday[1],"MM-dd") %>)
		      	</b>
		      </th>
		      <%} for(Date[] inday : inmonths) {%>
		      <th width="60">
		      	<b>
		      		<%=DateUtil.formatDate(inday[0],"M月") %>
		      		<br/>(<%=DateUtil.formatDate(inday[0],"MM-dd") %>~<%=DateUtil.formatDate(inday[1],"MM-dd") %>)
		      	</b>
		      </th>
		      <%} %>
		    </tr>
		    <%
		    Channel c = null;
		    if(channelid != -1) {
		    	c = cmanager.getChannel(channelid);
		    }
		    ChannelItem ci = null;
		    if(channelitemid != -1) {
		    	ci = cimanager.getChannelItem(channelitemid);
		    }
		    for(Date[] rdate : regmonths) {
		    	//获取当周这个渠道注册的用户数
				Date starttime = rdate[0];
				Date endtime = rdate[1];
				Calendar cal = Calendar.getInstance();
				cal.setTime(starttime);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				starttime = cal.getTime();
				cal.setTime(endtime);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				endtime = cal.getTime();
		    	int regnum = 0;
		    	//获取当周这个渠道的成本预估
		    	List<ItemInputChannelDay> ilist = null;
		    	long month = Long.parseLong(DateUtil.formatDate(rdate[0],"yyyyMM"));
		    	if(c != null && ci != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid, channelitemid);
		    	} else if(c != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid);
		    	} else if(c == null && ci == null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime);
		    	}
		    %>
		    <tr>
		    	<td>
		    		<%=c==null?"所有":c.getName() %>
		    	</td>	
		    	<td>
		    		<%=ci==null?"所有":ci.getName() %>
		    	</td>	
		    	<td>
		    		<%=DateUtil.formatDate(rdate[0], "M月") %><br/>
		    		<%=DateUtil.formatDate(rdate[0],"MM-dd") %>~<%=DateUtil.formatDate(rdate[1],"MM-dd") %>
		    	</td>	
		    	<td>
		    		<%=regnum %>
		    	</td>	
		    	<%
		    	float fs[] = new float[inmonths.size()];
		    	int index = 0;
		    	for(Date[] idate : inmonths) {
					Date istart = idate[0];
					Date iend = idate[1];
		    	 %>
		    	<td>
		    		<%
		    		if(iend.getTime() >= starttime.getTime()) {
		    			int actnum = 0;
		    			if(c != null && ci != null) {
		    				actnum = pmanager.getNewUserLoginNumByChannel(c.getId(), ci.getId(), starttime, endtime, istart, iend); 
		    			} else if(c != null) {
		    				actnum = pmanager.getNewUserLoginNumByChannel(c.getId(),starttime, endtime, istart, iend); 
		    			} else if(c == null && ci == null) {
		    				actnum = pmanager.getNewUserLoginNum(starttime, endtime, istart, iend); 
		    			}
		    			out.print(actnum);		    				
		    			fs[index] = 0;
		    			if(regnum != 0) {
		    				fs[index] = NumberUtils.round(new Float(actnum)*100/new Float(regnum), 1);
		    			}
		    		} else {
		    			out.print("N/A");
		    			fs[index] = 0;
		    		}
		    		index++;
		    		%>
		    	</td>
		    	<%} for(float f : fs) {%>
		    	<td>
		    		<%=f %>%
		    	</td>
		    	<%} %>
		    </tr>
		    <%} %>
	    </table>
	<%}} %>
	</form>
	<div id="overlay"></div>
	<div id="lightbox2">
     <div class="lbody2" id="lbody2">
       		正在查询，请耐心等候...<br/>
       		<img src='../images/process.gif'/> 
     </div>
	</div>
</body>
</html>
