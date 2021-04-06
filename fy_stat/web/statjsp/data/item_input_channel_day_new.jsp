<%@ page contentType="text/html;charset=utf-8"%><%@ include file="../inc/header.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>...</title>
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
		channelitem.options[0] = new Option("请选择...", "-1");
	} else {
		ChannelBoundry.getChannelItems(cid, afterSelectChannel);
	}
}

function afterSelectChannel(citems) {
	var channelitem = document.getElementById("channelitem");
	channelitem.length == 1;
	channelitem.options[0] = new Option("请选择...", "-1");
	for(var i=0; i<citems.length; i++) {
		var str = citems[i].split("#");
		channelitem.options[i+1] = new Option(str[0], str[1]);
	}
}

function sub() {
	document.f1.submit();
}

window.addEvent('domready', function() { 
	myCal1 = new Calendar({ yyyymm: 'Ym' }, { direction: 0, tweak: {x: 6, y: 0} });
});
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>录入单价预估</h1>
	<%
	ChannelManager cmanager = ChannelManager.getInstance();
	ChannelItemManager cimanager = ChannelItemManager.getInstance();
	List<Channel> clist = cmanager.getChannels();
	ItemInputChannelDayManager imanager = ItemInputChannelDayManager.getInstance();
	String ymonth = ParamUtils.getParameter(request, "yyyymm", null);
	long channelid = Long.parseLong(ParamUtils.getParameter(request, "channel", "-1"));
	long channelitemid = Long.parseLong(ParamUtils.getParameter(request, "channelitem", "-1"));
	String message = null;
	if(ymonth != null) {
		long amount = Long.parseLong(ParamUtils.getParameter(request, "amount", "0"));
		long realamount = Long.parseLong(ParamUtils.getParameter(request, "realamount", "0"));
		if(channelid != -1 && channelitemid != -1) {
			long num = imanager.getChannelInputs(Long.parseLong(ymonth), channelid, channelitemid).size();
			if(num > 0) {
				message = "当日此渠道的单价预估已经录入，如要修改请到列表页修改";
			} else {
				ItemInputChannelDay input = new ItemInputChannelDay();
				input.setInputmonth(Long.parseLong(ymonth));
				input.setChannelid(channelid);
				input.setChannelitemid(channelitemid);
				input.setAmount(amount);
				input.setRealamount(realamount);
				imanager.createItemInputChannelDay(input);
				message = "完成录入";
			}
		} else {
			message = "您必须选择渠道和子渠道";
		}
	}
	%>
	<%if(message != null) {%>
	<span style="padding-left:50px;font-size:14px;color:red"><%=message%></span>
	<%}%>
	<form action="#" method=post name=f1>                                                   
	<table id="test1" align="center" width="50%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	<tr><th>月份</th><td class="top"><input type="text" id="yyyymm" name ="yyyymm"/>(yyyyMM)</td></tr>
	<tr><th>渠道</th><td class="top">
	<select name="channel" id="channel" onchange="selectChannel()">
			<option value="-1">请选择...</option>
			<%for(Channel c : clist) {%>
			<option value="<%=c.getId() %>" <%if(c.getId()==channelid) out.print("selected");%>><%=c.getName() %></option>
			<%} %>
		</select>
	</td></tr>
	<tr><th>子渠道</th><td>
	<select name="channelitem" id="channelitem">
			<option value="-1">请选择...</option>
		</select>
	</td></tr>
	<tr><th>预估单价</th><td><input type="text" id="amount" name ="amount"/>(分)</td></tr>
	<tr><th>结算单价</th><td><input type="text" id="amount" name ="realamount"/>(分)</td></tr>
    <tr><td colspan="2" align="center"><input type="button" value="提交" onclick="sub();"/><input type="reset" value="重置"/></td></tr>
    </table>
     
	</form>
	<input type=button name=bt1 value=" 返 回 " onclick="return location.replace('item_input_channel_day_list.jsp');">
    
</body>
</html>
