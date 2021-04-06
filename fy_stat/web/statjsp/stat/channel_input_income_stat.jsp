<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Portal管理后台</title>
<link rel="stylesheet" href="../css/style.css"/>
<link rel="stylesheet" type="text/css" href="../calcss/iframe.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../calcss/calendar.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../calcss/dashboard.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../calcss/i-heart-ny.css" media="screen" />
<script type="text/javascript" src="../js/mootools.js"></script>
<script type="text/javascript" src="../js/calendar.rc4.js"></script>
<link rel="stylesheet" href="../css/atalk.css" />
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
	<h1>分渠道投入产出统计</h1>
	<%
	String message = null;
	String regstart = ParamUtils.getParameter(request, "regstart", DateUtil.formatDate(new Date(), "yyyy-MM-dd") );
	String regend = ParamUtils.getParameter(request, "regend", DateUtil.formatDate(new Date(), "yyyy-MM-dd") );
	java.util.Date regstartdate = DateUtil.parseDate(regstart + " 00:00:01","yyyy-MM-dd HH:mm:ss");
	java.util.Date regenddate = DateUtil.parseDate(regend + " 23:59:59","yyyy-MM-dd HH:mm:ss");
	String instart = ParamUtils.getParameter(request, "instart", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
	String inend = ParamUtils.getParameter(request, "inend", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
	java.util.Date instartdate = DateUtil.parseDate(instart + " 00:00:01","yyyy-MM-dd HH:mm:ss");
	java.util.Date inenddate = DateUtil.parseDate(inend + " 23:59:59","yyyy-MM-dd HH:mm:ss");
	if(regstartdate.getTime() > regenddate.getTime() || instartdate.getTime() > inenddate.getTime()) {
		out.println("<script>window.alert('起始日期不能晚于结束日期');location.replace('channel_input_income_stat.jsp');</script>");
		return;
	}
	ChannelManager cmanager = ChannelManager.getInstance();
	ChannelItemManager cimanager = ChannelItemManager.getInstance();
	List<Channel> clist = cmanager.getChannels();
	long channelid = Long.parseLong(ParamUtils.getParameter(request, "channel", "-1"));
	long channelitemid = Long.parseLong(ParamUtils.getParameter(request, "channelitem", "-1"));
	UserStatManager umanager = UserStatManager.getInstance();
	SavingYuanbaoManager smanager = SavingYuanbaoManager.getInstance();
	ItemInputChannelDayManager imanager = ItemInputChannelDayManager.getInstance();
	int stype = ParamUtils.getIntParameter(request, "stype", 0);
	String sqagesaving = ParamUtils.getParameter(request,"sqagesaving","");
	String smssaving = ParamUtils.getParameter(request,"smssaving","");
	%>
	
		<form action="#" method="post" name=f1>
		<table align="left" cellpadding="0" cellspacing="0" border="0" width="98%">
		 <tr>
		 	<td align="left" style="border-top:1px #ccc solid;">
		 	<p style="clear:both;text-align:left;">
			<label for="regstart">注册开始:</label><input type="text" id="regstart" name="regstart" size="6" value="<%=regstart %>"/>
			<label for="regend">注册结束:</label><input type="text" id="regend" name="regend" size="6" value="<%=regend %>"/>
			<label for="instart">收入开始:</label><input type="text" id="instart" name="instart" size="6" value="<%=instart %>"/>
			<label for="inend">收入结束:</label><input type="text" id="inend" name="inend" size="6" value="<%=inend %>"/>
			</p>
			<p style="clear:both;text-align:left;"><label for="channel">渠道:</label><select name="channel" id="channel" onchange="selectChannel()">
				<option value="-1">所有</option>
				<%for(Channel c : clist) {%>
				<option value="<%=c.getId() %>" <%if(c.getId()==channelid) out.print("selected");%>><%=c.getName() %></option>
				<%} %>
			</select>
			<label for="channelitem">子渠道:</label><select name="channelitem" id="channelitem">
				<option value="-1">所有</option>
			</select>
			<label for="stype">统计方式:</label><select id=stype name="stype" id="stype">
				<option value="0" <%if(stype == 0) out.print("selected");%>>按日</option>
				<option value="1" <%if(stype == 1) out.print("selected");%>>按周</option>
				<option value="2" <%if(stype == 2) out.print("selected");%>>按月</option>
			</select>
			<input type=checkbox name=sqagesaving value="false" <%if(sqagesaving != null && sqagesaving.equals("false")) out.print("checked"); %>/><label for="sqagesaving" style="width:80px;">不统计神奇卡</label>
			<input type=checkbox name=smssaving value="false" <%if(smssaving != null && smssaving.equals("false")) out.print("checked"); %>/><label for="smssaving" style="width:80px;">不统计短信</label>
			<input type=hidden name=subed value="true"/>
			<input type=button name=bt1 value="查询" onclick="stat()"/></p>
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
	   	<table id="test1" width="99%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	    	<tr>
		      <th rowspan=2 width="60"><b>渠道</b></th>
		      <th rowspan=2 width="60"><b>子渠道</b></th>
		      <th rowspan=2 width="60"><b>注册日期</b></th>
		      <th rowspan=2 width="60"><b>注册数</b></th>
		      <th rowspan=2 width="120" nowrap><b>渠道成本预估(元)</b></th>
		      <th colspan="<%=indays.size() %>" width="50"><b>收入</b></th>
		      <th rowspan=2 width="90"><b>支付成本(元)</b></th>
		      <th rowspan=2 width="40"><b>毛利</b></th>
		      <th rowspan=2 width="50"><b>毛利率</b></th>
		    </tr>
		    <tr>
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
		    	List<ItemInputChannelDay> ilist = null;
		    	long month = Long.parseLong(DateUtil.formatDate(rdate,"yyyyMM"));
		    	if(c != null && ci != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid, channelitemid);
		    		ilist = imanager.getChannelInputs(month, channelid, channelitemid);
		    	} else if(c != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid);
		    		ilist = imanager.getChannelInputs(month, channelid);
		    	} else if(c == null && ci == null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime);
		    		ilist = imanager.getChannelInputs(month);
		    	}
		    	long input = 0;
		    	if(ilist != null && ilist.size() > 0) {
		    		for(ItemInputChannelDay in : ilist) {
		    			long inputper = in.getAmount();
		    			long cid = in.getChannelid();
		    			long ciid = in.getChannelitemid();
		    			int cregnum = umanager.getNewUserNum(starttime, endtime, cid, ciid);
		    			input += cregnum*inputper;
		    			//System.out.println("[ind:"+in.getId()+"] [amount:"+inputper+"] [cregnum:"+cregnum+"]");
		    		}
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
		    	<td>
		    		<%=NumberUtils.round(new Float(input)/100, 1) %>
		    	</td>	
		    	<%
		    	long cost = 0;
		    	long stotal = 0;
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
		    		if(Integer.parseInt(DateUtil.formatDate(idate,"yyyyMMdd")) >= 
		    			Integer.parseInt(DateUtil.formatDate(rdate, "yyyyMMdd"))) {
		    			long saving = 0;
		    			if(c != null && ci != null) {
		    				if(sqagesaving.equals("false") && smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountPure(starttime, endtime, istart, iend, channelid, channelitemid);
		    				} else if(sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSqage(starttime, endtime, istart, iend, channelid, channelitemid);
		    				} else if(smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSms(starttime, endtime, istart, iend, channelid, channelitemid);
		    				} else {
		    					saving = smanager.getSavingRmbAmount(starttime, endtime, istart, iend, channelid, channelitemid);
		    				}
		    				cost += smanager.getSavingRmbCost(starttime, endtime, istart, iend, channelid, channelitemid);
		    			} else if(c != null) {
		    				if(sqagesaving.equals("false") && smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountPure(starttime, endtime, istart, iend, channelid);
		    				} else if(smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSms(starttime, endtime, istart, iend, channelid);
		    				} else if(sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSqage(starttime, endtime, istart, iend, channelid);
		    				} else {
		    					saving = smanager.getSavingRmbAmount(starttime, endtime, istart, iend, channelid);
		    				}
		    				cost += smanager.getSavingRmbCost(starttime, endtime, istart, iend, channelid);
		    			} else if(c == null && ci == null) {
		    				if(smssaving.equals("false") && sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountPure(starttime, endtime, istart, iend);
		    				} else if(smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSms(starttime, endtime, istart, iend);
		    				} else if(sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSqage(starttime, endtime, istart, iend);
		    				} else {
		    					saving = smanager.getSavingRmbAmount(starttime, endtime, istart, iend);
		    				}
		    				cost += smanager.getSavingRmbCost(starttime, endtime, istart, iend);
		    			}
		    			stotal += saving;
		    			out.print(saving/100);
		    		} else {
		    			out.print("N/A");
		    		}
		    		%>
		    	</td>
		    	<%} %>
		    	<td>
		    		<%=cost/100 %>
		    	</td>
		    	<td>
		    		<%=(stotal-cost-input)/100 %>
		    	</td>
		    	<td>
		    		<%=stotal != 0?NumberUtils.round(new Float(stotal-cost-input)*100/new Float(stotal), 1):0 %>%
		    	</td>
		    </tr>
		    <%} %>
	    </table>
		<%}
		if(stype == 1) {	
			List<Date[]> regweeks = DateUtil.splitTime(regstartdate, regenddate, 1);
			List<Date[]> inweeks = DateUtil.splitTime(instartdate, inenddate, 1);
			%>
	   	<table id="test1" width="99%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	    	<tr>
		      <th rowspan=2 width="60"><b>渠道</b></th>
		      <th rowspan=2 width="60"><b>子渠道</b></th>
		      <th rowspan=2 width="60"><b>注册日期</b></th>
		      <th rowspan=2 width="60"><b>注册数</b></th>
		      <th rowspan=2 width="60"><b>渠道成本预估(元)</b></th>
		      <th colspan="<%=inweeks.size() %>" width="120"><b>收入</b></th>
		      <th rowspan=2 width="90"><b>支付成本(元)</b></th>
		      <th rowspan=2 width="40"><b>毛利</b></th>
		      <th rowspan=2 width="50"><b>毛利率</b></th>
		    </tr>
		    <tr>
		      <%for(Date[] inday : inweeks) {%>
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
		    	int regnum = 0;
		    	//获取当周这个渠道的成本预估
		    	List<ItemInputChannelDay> ilist = null;
		    	long month = Long.parseLong(DateUtil.formatDate(rdate[0],"yyyyMM"));
		    	if(c != null && ci != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid, channelitemid);
		    		ilist = imanager.getChannelInputs(month, channelid, channelitemid);
		    	} else if(c != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid);
		    		ilist = imanager.getChannelInputs(month, channelid);
		    	} else if(c == null && ci == null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime);
		    		ilist = imanager.getChannelInputs(month);
		    	}
		    	long input = 0;
		    	if(ilist != null && ilist.size() > 0) {
		    		for(ItemInputChannelDay in : ilist) {
		    			long inputper = in.getAmount();
		    			long cid = in.getChannelid();
		    			long ciid = in.getChannelitemid();
		    			int cregnum = umanager.getNewUserNum(starttime, endtime, cid, ciid);
		    			input += cregnum*inputper;
		    			System.out.println("[ind:"+in.getId()+"] [amount:"+inputper+"] [cregnum:"+cregnum+"]");
		    		}
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
		    	<td>
		    		<%=NumberUtils.round(new Float(input)/100, 1) %>
		    	</td>	
		    	<%
		    	long cost = 0;
		    	long stotal = 0;
		    	for(Date[] idate : inweeks) {
					Date istart = idate[0];
					Date iend = idate[1];
		    	 %>
		    	<td>
		    		<%
		    		if(idate[1].getTime() >= rdate[0].getTime()) {
		    			long saving = 0;
		    			if(c != null && ci != null) {
		    				if(sqagesaving.equals("false") && smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountPure(starttime, endtime, istart, iend, channelid, channelitemid);
		    				} else if(sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSqage(starttime, endtime, istart, iend, channelid, channelitemid);
		    				} else if(smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSms(starttime, endtime, istart, iend, channelid, channelitemid);
		    				} else {
		    					saving = smanager.getSavingRmbAmount(starttime, endtime, istart, iend, channelid, channelitemid);
		    				}
		    				cost += smanager.getSavingRmbCost(starttime, endtime, istart, iend, channelid, channelitemid);
		    			} else if(c != null) {
		    				if(sqagesaving.equals("false") && smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountPure(starttime, endtime, istart, iend, channelid);
		    				} else if(smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSms(starttime, endtime, istart, iend, channelid);
		    				} else if(sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSqage(starttime, endtime, istart, iend, channelid);
		    				} else {
		    					saving = smanager.getSavingRmbAmount(starttime, endtime, istart, iend, channelid);
		    				}
		    				cost += smanager.getSavingRmbCost(starttime, endtime, istart, iend, channelid);
		    			} else if(c == null && ci == null) {
		    				if(smssaving.equals("false") && sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountPure(starttime, endtime, istart, iend);
		    				} else if(smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSms(starttime, endtime, istart, iend);
		    				} else if(sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSqage(starttime, endtime, istart, iend);
		    				} else {
		    					saving = smanager.getSavingRmbAmount(starttime, endtime, istart, iend);
		    				}
		    				cost += smanager.getSavingRmbCost(starttime, endtime, istart, iend);
		    			}
		    			stotal += saving;
		    			out.print(saving/100);
		    		} else {
		    			out.print("N/A");
		    		}
		    		%>
		    	</td>
		    	<%} %>
		    	<td>
		    		<%=cost/100 %>
		    	</td>
		    	<td>
		    		<%=(stotal-cost-input)/100 %>
		    	</td>
		    	<td>
		    		<%=stotal != 0?NumberUtils.round(new Float(stotal-cost-input)*100/new Float(stotal), 1):0 %>%
		    	</td>
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
		      <th rowspan=2 width="120"><b>渠道成本预估(元)</b></th>
		      <th colspan="<%=inmonths.size() %>" width="60"><b>收入</b></th>
		      <th rowspan=2 width="90"><b>支付成本(元)</b></th>
		      <th rowspan=2 width="40"><b>毛利</b></th>
		      <th rowspan=2 width="50"><b>毛利率</b></th>
		    </tr>
		    <tr>
		      <%for(Date[] inday : inmonths) {%>
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
		    	int regnum = 0;
		    	//获取当周这个渠道的成本预估
		    	List<ItemInputChannelDay> ilist = null;
		    	long month = Long.parseLong(DateUtil.formatDate(rdate[0],"yyyyMM"));
		    	if(c != null && ci != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid, channelitemid);
		    		ilist = imanager.getChannelInputs(month, channelid, channelitemid);
		    	} else if(c != null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime, channelid);
		    		ilist = imanager.getChannelInputs(month, channelid);
		    	} else if(c == null && ci == null) {
		    		regnum = umanager.getNewUserNum(starttime, endtime);
		    		ilist = imanager.getChannelInputs(month);
		    	}
		    	long input = 0;
		    	if(ilist != null && ilist.size() > 0) {
		    		for(ItemInputChannelDay in : ilist) {
		    			long inputper = in.getAmount();
		    			long cid = in.getChannelid();
		    			long ciid = in.getChannelitemid();
		    			int cregnum = umanager.getNewUserNum(starttime, endtime, cid, ciid);
		    			input += cregnum*inputper;
		    			System.out.println("[ind:"+in.getId()+"] [amount:"+inputper+"] [cregnum:"+cregnum+"]");
		    		}
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
		    	<td>
		    		<%=NumberUtils.round(new Float(input)/100, 1) %>
		    	</td>	
		    	<%
		    	long cost = 0;
		    	long stotal = 0;
		    	for(Date[] idate : inmonths) {
					Date istart = idate[0];
					Date iend = idate[1];
		    	 %>
		    	<td>
		    		<%
		    		if(idate[1].getTime() >= rdate[0].getTime()) {
		    			long saving = 0;
		    			if(c != null && ci != null) {
		    				if(sqagesaving.equals("false") && smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountPure(starttime, endtime, istart, iend, channelid, channelitemid);
		    				} else if(sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSqage(starttime, endtime, istart, iend, channelid, channelitemid);
		    				} else if(smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSms(starttime, endtime, istart, iend, channelid, channelitemid);
		    				} else {
		    					saving = smanager.getSavingRmbAmount(starttime, endtime, istart, iend, channelid, channelitemid);
		    				}
		    				cost += smanager.getSavingRmbCost(starttime, endtime, istart, iend, channelid, channelitemid);
		    			} else if(c != null) {
		    				if(sqagesaving.equals("false") && smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountPure(starttime, endtime, istart, iend, channelid);
		    				} else if(smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSms(starttime, endtime, istart, iend, channelid);
		    				} else if(sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSqage(starttime, endtime, istart, iend, channelid);
		    				} else {
		    					saving = smanager.getSavingRmbAmount(starttime, endtime, istart, iend, channelid);
		    				}
		    				cost += smanager.getSavingRmbCost(starttime, endtime, istart, iend, channelid);
		    			} else if(c == null && ci == null) {
		    				if(smssaving.equals("false") && sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountPure(starttime, endtime, istart, iend);
		    				} else if(smssaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSms(starttime, endtime, istart, iend);
		    				} else if(sqagesaving.equals("false")) {
		    					saving = smanager.getSavingRmbAmountNoSqage(starttime, endtime, istart, iend);
		    				} else {
		    					saving = smanager.getSavingRmbAmount(starttime, endtime, istart, iend);
		    				}
		    				cost += smanager.getSavingRmbCost(starttime, endtime, istart, iend);
		    			}
		    			stotal += saving;
		    			out.print(saving/100);
		    		} else {
		    			out.print("N/A");
		    		}
		    		%>
		    	</td>
		    	<%} %>
		    	<td>
		    		<%=cost/100 %>
		    	</td>
		    	<td>
		    		<%=(stotal-cost-input)/100 %>
		    	</td>
		    	<td>
		    		<%=stotal != 0?NumberUtils.round(new Float(stotal-cost-input)*100/new Float(stotal), 1):0 %>%
		    	</td>
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
