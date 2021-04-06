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
<script type="text/javascript" src="/game_stat/dwr/engine.js"></script>
<script type="text/javascript" src="/game_stat/dwr/interface/ChannelBoundry.js"></script>
<script type="text/javascript" src="/game_stat/dwr/interface/DWRManager.js"></script>
<script type="text/javascript" src="/game_stat/dwr/util.js"></script>
<script language="JavaScript">
<!-- 
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

window.addEvent('domready', function() { 
	myCal1 = new Calendar({ yyyymm: 'Ym' }, { direction: 0, tweak: {x: 6, y: 0} });
});
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>分渠道单价预估</h1>
	<%
	long yyyymm = Long.parseLong(ParamUtils.getParameter(request,"yyyymm", DateUtil.formatDate(new Date(), "yyyyMM")));

	ChannelManager cmanager = ChannelManager.getInstance();
	ChannelItemManager cimanager = ChannelItemManager.getInstance();
	List<Channel> clist = cmanager.getChannels();
	
	long channelid = Long.parseLong(ParamUtils.getParameter(request, "channel", "-1"));
	long channelitemid = Long.parseLong(ParamUtils.getParameter(request, "channelitem", "-1"));
	
	ItemInputChannelDayManager imanager = ItemInputChannelDayManager.getInstance();
	List<ItemInputChannelDay> ilist = imanager.getChannelInputs(yyyymm, channelid, channelitemid);
	%>
	<form action="#" method="post">
	
	<table align="center" cellpadding="0" cellspacing="0" border="0">
	 <tr>
	 	<td colspan="20" align="left" style="border-top:1px #ccc solid;">
		<label for="yyyymm">月份:</label><input type="text" id="yyyymm" name="yyyymm" size="10" value="<%=yyyymm %>"/>
		<label for="channel">渠道:</label><select name="channel" id="channel" onchange="selectChannel()">
			<option value="-1">所有</option>
			<%for(Channel c : clist) {%>
			<option value="<%=c.getId() %>" <%if(c.getId()==channelid) out.print("selected");%>><%=c.getName() %></option>
			<%} %>
		</select>
		<label for="channelitem">子渠道:</label><select name="channelitem" id="channelitem">
			<option value="-1">所有</option>
		</select>
		<input type=submit name=bt1 value="查询"/>
		</td>
	 </tr>
	</table>
	<table id="test1" align="center" width="70%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	  <tr>
      <th width=20%><b>月份</b></th>
      <th width=15%><b>渠道</b></th>
	  <th width=15%><b>子渠道</b></th>
	  <th width=15%><b>单价预估(分)</b></th>
	  <th width=15%><b>结算单价(分)</b></th>
	  <th width=20%><b>操作</b></th>
     </tr>  
     <%for(ItemInputChannelDay input : ilist) {
     	long ymonth = input.getInputmonth();
     	long cid = input.getChannelid();
     	long citemid = input.getChannelitemid();
     	Channel c = cmanager.getChannel(cid);
     	ChannelItem ci = cimanager.getChannelItem(citemid);
     %>
     <tr>
		<td><%=ymonth %></td>
		<td><%=c.getName() %></td>
		<td><%=ci.getName() %></td>
		<td><%=input.getAmount() %></td>
		<td><%=input.getRealamount() %></td>
		<td>
			<a href="item_input_channel_day_mod.jsp?id=<%=input.getId() %>">修 改</a>
		</td>
	 </tr> 
	 <%} %>
    </table>
	</form>
	    <input type=button name="bt1" value=" 新 增 " onclick="location.replace('/data/item_input_channel_day_new.jsp')"/>
	
</body>
</html>
