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
	myCal1 = new Calendar({ starttime: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
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
	<h1>分渠道当日注册用户所属地查询</h1>
	<%
	String message = null;
	String starttime = ParamUtils.getParameter(request, "starttime", DateUtil.formatDate(new Date(), "yyyy-MM-dd") );
	java.util.Date starttimedate = DateUtil.parseDate(starttime + " 00:00:01","yyyy-MM-dd HH:mm:ss");
	Calendar cal = Calendar.getInstance();
	cal.setTime(starttimedate);
	cal.add(Calendar.DAY_OF_YEAR, 1);
	Date endtimedate = cal.getTime();
	ChannelManager cmanager = ChannelManager.getInstance();
	ChannelItemManager cimanager = ChannelItemManager.getInstance();
	ProvinceManager pmanager = ProvinceManager.getInstance();
	List<Channel> clist = cmanager.getChannels();
	long channelid = Long.parseLong(ParamUtils.getParameter(request, "channel", "-1"));
	long channelitemid = Long.parseLong(ParamUtils.getParameter(request, "channelitem", "-1"));
	UserStatManager umanager = UserStatManager.getInstance();
	String subed = request.getParameter("subed");
	String hour = ParamUtils.getParameter(request, "hour", "-1");
	List<String> hlist = new ArrayList<String>();
	for(int i=0; i<24; i++) {
		hlist.add(i+":00~"+(i+1)+":00");
	}
	HashMap<Long, Integer> amap = null;
    int regnum = 0;
    Channel cc = null;
    if(channelid != -1) {
    	cc = cmanager.getChannel(channelid);
    }
    ChannelItem cci = null;
    if(channelitemid != -1) {
    	cci = cimanager.getChannelItem(channelitemid);
    }
	if(subed != null) {
		System.out.println("trace1:["+hour+"]");
		if(hour.equals("-1")) {
			if(cc!= null && cci != null) {
				amap = umanager.statRegisterArea(starttimedate, endtimedate, channelid, channelitemid);
				regnum = umanager.getNewUserNum(starttimedate, endtimedate, channelid, channelitemid);
			} else if(cc != null) {
				amap = umanager.statRegisterArea(starttimedate, endtimedate, channelid);
				regnum = umanager.getNewUserNum(starttimedate, endtimedate, channelid);
			} else if(cc == null && cci == null) {
				amap = umanager.statRegisterArea(starttimedate, endtimedate);
				regnum = umanager.getNewUserNum(starttimedate, endtimedate);
			}
		} else {
			String h1 = hour.split("~")[0].split(":")[0];
			String h2 = hour.split("~")[1].split(":")[0];
			cal.setTime(starttimedate);
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(h1));
			Date start = cal.getTime();
			cal.add(Calendar.HOUR_OF_DAY, 1);
			Date end = cal.getTime();
			if(cc!= null && cci != null) {
				amap = umanager.statRegisterArea(start, end, channelid, channelitemid);
				regnum = umanager.getNewUserNum(start, end, channelid, channelitemid);
			} else if(cc != null) {
				amap = umanager.statRegisterArea(start, end, channelid);
				regnum = umanager.getNewUserNum(start, end, channelid);
			} else if(cc == null && cci == null) {
				amap = umanager.statRegisterArea(start, end);
				regnum = umanager.getNewUserNum(start, end);
			}
		}
	}
	%>
		<form action="#" method="post" name=f1>
		<table align="left" cellpadding="0" cellspacing="0" border="0" width="98%">
		 <tr>
		 	<td align="left" style="border-top:1px #ccc solid;">
		 	<p style="clear:both;text-align:left;">
			<label for="starttime">查询日期:</label><input type="text" id="starttime" name="starttime" size="10" value="<%=starttime %>"/>
			<label for="hour">时间:</label><select name="hour" id="hour">
				<option value="-1">所有</option>
				<%for(String h : hlist) {%>
				<option value="<%=h %>" <%if(hour.equals(h)) out.print("selected");%>><%=h %></option>
				<%} %>
			</select>
			<label for="channel">渠道:</label><select name="channel" id="channel" onchange="selectChannel()">
				<option value="-1">所有</option>
				<%for(Channel c : clist) {%>
				<option value="<%=c.getId() %>" <%if(c.getId()==channelid) out.print("selected");%>><%=c.getName() %></option>
				<%} %>
			</select>
			<label for="channelitem">子渠道:</label><select name="channelitem" id="channelitem">
				<option value="-1">所有</option>
			</select>
			<input type=hidden name=subed value="true""/>
			<input type=button name=bt1 value="查询" onclick="stat()"/>
			</p>
			</td>
		 </tr>
	    </table><br/>
	    <%
	    if(amap != null) {
	    %>
	   	<table id="test1" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	    	<tr>
		      <th width="60"><b>渠道</b></th>
		      <th width="60"><b>子渠道</b></th>
		      <th width="60"><b>日期</b></th>
		      <th width="60"><b>时间</b></th>
		      <th width="60"><b>注册数</b></th>
		      <%
		      Long ps[] = amap.keySet().toArray(new Long[0]);
		      Arrays.sort(ps, new MapComparator(amap));
		      for(int i=0; i<ps.length; i++) {
		      	Province p = pmanager.getProvince(ps[i]);
		      	if(p != null) {
		       %>
		      <th width="60"><b><%=p.getName() %></b></th>
		      <%}} %> 
		    </tr>
		    <tr>
		    	<td>
		    		<%=cc==null?"所有":cc.getName() %>
		    	</td>	
		    	<td>
		    		<%=cci==null?"所有":cci.getName() %>
		    	</td>	
		    	<td>
		    		<%=starttime %>
		    	</td>	
		    	<td>
		    		<%=hour.equals("-1")?"全天":hour %>
		    	</td>	
		    	<td>
		    		<%=regnum %>
		    	</td>	
		    	<%
		    	for(int i=0; i<ps.length; i++) {
		    		Province p = pmanager.getProvince(ps[i]);
		    		if(p != null) {
		    	 %>
		    	<td>
		    		<%=amap.get(ps[i]) %>
		    	</td>	
		     	<%}} %>
		    </tr>
		    <%} %>
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
