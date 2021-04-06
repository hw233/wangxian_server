<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="com.xuanzhi.stat.model.CurrencyType"%><html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
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
	myCal1 = new Calendar({ starttime: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
	myCal2 = new Calendar({ endtime: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
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
	<h1>分渠道当日基本数据统计</h1>
	<%
	String message = null;
	String starttime = ParamUtils.getParameter(request, "starttime", DateUtil.formatDate(new Date(), "yyyy-MM-dd") );
	String endtime = ParamUtils.getParameter(request, "endtime", DateUtil.formatDate(new Date(), "yyyy-MM-dd") );
	java.util.Date starttimedate = DateUtil.parseDate(starttime + " 00:00:01","yyyy-MM-dd HH:mm:ss");
	java.util.Date endtimedate = DateUtil.parseDate(endtime + " 00:00:01","yyyy-MM-dd HH:mm:ss");
	if(starttimedate.getTime() > endtimedate.getTime()) {
		out.println("<script>window.alert('起始日期不能晚于结束日期');location.replace('channel_day_stat.jsp');</script>");
		return;
	}
	ChannelManager cmanager = ChannelManager.getInstance();
	ChannelItemManager cimanager = ChannelItemManager.getInstance();
	List<Channel> clist = cmanager.getChannels();
	long channelid = Long.parseLong(ParamUtils.getParameter(request, "channel", "-1"));
	long channelitemid = Long.parseLong(ParamUtils.getParameter(request, "channelitem", "-1"));
	UserStatManager umanager = UserStatManager.getInstance();
	PlayerStatManager pmanager = PlayerStatManager.getInstance();
	SavingYuanbaoManager smanager = SavingYuanbaoManager.getInstance();
	PlayerExpenseManager pemanager = PlayerExpenseManager.getInstance();
	PlayerLoginManager plm = PlayerLoginManager.getInstance();
	PlayerLogoutManager plmanager = PlayerLogoutManager.getInstance();
	OnlinePlayerManager opmanager = OnlinePlayerManager.getInstance();
	String subed = request.getParameter("subed");
	%>
		<form action="#" method="post" name=f1>
		<table align="left" cellpadding="0" cellspacing="0" border="0" width="98%">
		 <tr>
		 	<td align="left" style="border-top:1px #ccc solid;">
		 	<p style="clear:both;text-align:left;">
			<label for="starttime">开始日期:</label><input type="text" id="starttime" name="starttime" size="10" value="<%=starttime %>"/>
			<label for="endtime">结束日期:</label><input type="text" id="endtime" name="endtime" size="10" value="<%=endtime %>"/>
			<label for="channel">渠道:</label><select name="channel" id="channel" onchange="selectChannel()">
				<option value="-1">所有</option>
				<%for(Channel c : clist) {%>
				<option value="<%=c.getId() %>" <%if(c.getId()==channelid) out.print("selected");%>><%=c.getName() %></option>
				<%} %>
			</select>
			<label for="channelitem">子渠道:</label><select name="channelitem" id="channelitem">
				<option value="-1">所有</option>
			</select>
			<input type=hidden name=subed value="true"/>
			<input type=button name=bt1 value="查询" onclick="stat()"/>
			</p>
			</td>
		 </tr>
	    </table><br/>
	   	<table id="test1" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	    	<tr>
		      <th width="60"><b>渠道</b></th>
		      <th width="60"><b>子渠道</b></th>
		      <th width="60"><b>统计日期</b></th>
		      <th width="60"><b>注册用户</b></th>
		      <th width="60"><b>创建角色</b></th>
		      <th width="60"><b>充值账号</b></th>
		      <th width="60"><b>充值元宝</b></th>
		      <th width="90"><b>充值人名币(元)</b></th>
		      <th width="60"><b>充值ARPU(元)</b></th>
		      <th width="60"><b>消费账号</b></th>
		      <th width="60"><b>消费总额</b></th>
		      <th width="90"><b>消费ARPU(元宝)</b></th>
		      <th width="90"><b>平均在线用户数</b></th>
		      <th width="90"><b>在线贡献比</b></th>
		      <th width="90"><b>当日平均在线时长(分)</b></th>
		      <th width="90"><b>历史人均创建角色数</b></th>
		      <th width="90"><b>历史平均等级</b></th>
		    </tr>
		    <%
		    if(subed != null) {
			    Channel c = null;
			    if(channelid != -1) {
			    	c = cmanager.getChannel(channelid);
			    }
			    ChannelItem ci = null;
			    if(channelitemid != -1) {
			    	ci = cimanager.getChannelItem(channelitemid);
			    }
			    List<Date> dates = new ArrayList<Date>();
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(starttimedate);
			    cal.set(Calendar.HOUR_OF_DAY, 0);
			    cal.set(Calendar.MINUTE, 0);
			    cal.set(Calendar.SECOND, 0);
			    while(!DateUtil.formatDate(cal.getTime(),"yyMMdd").equals(DateUtil.formatDate(endtimedate,"yyMMdd"))) {
			    	dates.add(cal.getTime());
			    	cal.add(Calendar.DAY_OF_YEAR, 1);
			    }
			    dates.add(cal.getTime());
			    for(Date rdate : dates) {
			    	//获取当天这个渠道注册的用户数
			    	cal.setTime(rdate);
					Date start = cal.getTime();
					cal.add(Calendar.DAY_OF_YEAR, 1);
					Date end = cal.getTime();
			    	int regnum = 0;
			    	int playernum = 0;
			    	int savingUserNum = 0;
			    	long savingYuanbao = 0;
			    	long savingRMB = 0;
			    	float savingARPU = 0;
			    	long expenseUserNum = 0;
			    	long expenseYuanbao = 0;
			    	float expenseARPU = 0;
			    	float avgOnlinetime = 0;
			    	float avgPlayerNum = 0;
			    	float avgLevel = 0;
			    	float onlinenum = opmanager.getAvgOnlineNum(start, end);
			    	float channelonlinenum = 0;
			    	float onlineperc = 0f;
			    	if(c != null && ci != null) {
			    		regnum = umanager.getNewUserNum(start, end, channelid, channelitemid);
			    		playernum = pmanager.getChannelNewPlayerNum(start, end, channelid, channelitemid);
			    		savingUserNum = smanager.getSavingYuanbaoUsernum(start, end, channelid, channelitemid);
			    		savingYuanbao = smanager.getSavingYuanbaoAmount(start, end, channelid, channelitemid);
			    		savingRMB = smanager.getSavingRmbAmount(start, end, channelid, channelitemid);
			    		expenseUserNum = pemanager.getExpenseUserNum(start, end, CurrencyType.RMB_YUANBAO, channelid, channelitemid);
			    		expenseYuanbao = pemanager.getExpenseAmount(start, end, CurrencyType.RMB_YUANBAO, channelid, channelitemid);
			    		long onlineTime = plmanager.getChannelPlayerOnlineTime(start, end, channelid, channelitemid);
			    		int loginUserNum = plm.getChannelLoginUserNum(start, end, channelid, channelitemid);
			    		if(loginUserNum != 0)
			    			avgOnlinetime = NumberUtils.round(new Float(onlineTime)/(60*new Float(loginUserNum)), 1);
			    		long levelSum = pmanager.getChannelPlayerMaxLevelSum(channelid, channelitemid);
			    		int userNum = umanager.getChannelUserNum(channelid, channelitemid);
			    		int playerNum = pmanager.getChannelPlayerNum(channelid, channelitemid);
			    		if(userNum != 0) {
			    			avgLevel = NumberUtils.round(new Float(levelSum)/new Float(userNum), 1);
			    			avgPlayerNum = NumberUtils.round(new Float(playerNum)/new Float(userNum), 1);
			    		}
			    		channelonlinenum = opmanager.getChannelAvgOnlineNum(start, end, channelid, channelitemid);
			    	} else if(c != null) {
			    		regnum = umanager.getNewUserNum(start, end, channelid);
			    		playernum = pmanager.getChannelNewPlayerNum(start, end, channelid);
			    		savingUserNum = smanager.getSavingYuanbaoUsernum(start, end, channelid);
			    		savingYuanbao = smanager.getSavingYuanbaoAmount(start, end, channelid);
			    		savingRMB = smanager.getSavingRmbAmount(start, end, channelid);
			    		expenseUserNum = pemanager.getExpenseUserNum(start, end, CurrencyType.RMB_YUANBAO, channelid);
			    		expenseYuanbao = pemanager.getExpenseAmount(start, end, CurrencyType.RMB_YUANBAO, channelid);
			    		long onlineTime = plmanager.getChannelPlayerOnlineTime(start, end, channelid);
			    		int loginUserNum = plm.getChannelLoginUserNum(start, end, channelid);
			    		if(loginUserNum != 0)
			    			avgOnlinetime = NumberUtils.round(new Float(onlineTime)/(60*new Float(loginUserNum)), 1);
			    		long levelSum = pmanager.getChannelPlayerMaxLevelSum(channelid);
			    		int userNum = umanager.getChannelUserNum(channelid);
			    		int playerNum = pmanager.getChannelPlayerNum(channelid);
			    		if(userNum != 0) {
			    			avgPlayerNum = NumberUtils.round(new Float(playerNum)/new Float(userNum), 1);
			    			avgLevel = NumberUtils.round(new Float(levelSum)/new Float(userNum), 1);
			    		}
			    		channelonlinenum = opmanager.getChannelAvgOnlineNum(start, end, channelid);
			    		
			    	} else if(c == null && ci == null) {
			    		regnum = umanager.getNewUserNum(start, end);
			    		playernum = pmanager.getNewPlayerNum(start, end);
			    		savingUserNum = smanager.getSavingYuanbaoUsernum(start, end);
			    		savingYuanbao = smanager.getSavingYuanbaoAmount(start, end);
			    		savingRMB = smanager.getSavingRmbAmount(start, end);
			    		expenseUserNum = pemanager.getExpenseUserNum(start, end, CurrencyType.RMB_YUANBAO);
			    		expenseYuanbao = pemanager.getExpenseAmount(start, end, CurrencyType.RMB_YUANBAO);
			    		long onlineTime = plmanager.getPlayerOnlineTime(start, end);
			    		int loginUserNum = plm.getLoginUserNum(start, end);
			    		if(loginUserNum != 0)
			    			avgOnlinetime = NumberUtils.round(new Float(onlineTime)/(60*new Float(loginUserNum)), 1);
			    		long levelSum = pmanager.getPlayerMaxLevelSum();
			    		int userNum = umanager.getUserNum();
			    		int playerNum = pmanager.getPlayerNum();
			    		if(userNum != 0) {
			    			avgPlayerNum = NumberUtils.round(new Float(playerNum)/new Float(userNum), 1);
			    			avgLevel = NumberUtils.round(new Float(levelSum)/new Float(userNum), 1);
			    		}
			    		channelonlinenum = opmanager.getAvgOnlineNum(start, end);
			    	}
		    		if(savingUserNum != 0) {
		    			savingARPU = NumberUtils.round(new Float(savingRMB)/(100*new Float(savingUserNum)), 1);
		    		}
		    		if(expenseUserNum != 0) {
		    			expenseARPU = NumberUtils.round(new Float(expenseYuanbao)/(new Float(expenseUserNum)), 1);
		    		}
		    		if(onlinenum != 0) {
		    			onlineperc = NumberUtils.round(channelonlinenum*100/onlinenum, 1);
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
		    		<%=playernum %>
		    	</td>	
		    	<td>
		    		<%=savingUserNum %>
		    	</td>	
		    	<td>
		    		<%=savingYuanbao %>
		    	</td>	
		    	<td>
		    		<%=savingRMB/100 %>
		    	</td>	
		    	<td>
		    		<%=savingARPU %>
		    	</td>	
		    	<td>
		    		<%=expenseUserNum %>
		    	</td>	
		    	<td>
		    		<%=expenseYuanbao %>
		    	</td>	
		    	<td>
		    		<%=expenseARPU %>
		    	</td>	
		    	<td>
		    		<%=channelonlinenum %>
		    	</td>	
		    	<td>
		    		<%=onlineperc %>%
		    	</td>	
		    	<td>
		    		<%=avgOnlinetime %>
		    	</td>	
		    	<td>
		    		<%=avgPlayerNum %>
		    	</td>	
		    	<td>
		    		<%=avgLevel %>
		    	</td>	
		    </tr>
		    <%}} %>
	    </table>
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
