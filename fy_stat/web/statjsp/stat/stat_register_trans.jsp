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
<script language="JavaScript">
<!-- 

window.addEvent('domready', function() { 
	myCal1 = new Calendar({ regstart: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
	myCal2 = new Calendar({ regend: 'Y-m-d' }, { direction: 0, tweak: {x: 6, y: 0} });
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

function stat() {
	over(2);
	document.f1.submit();
}
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>注册转化率统计</h1>
	<%
	String regstart = ParamUtils.getParameter(request, "regstart", "");
	String regend = ParamUtils.getParameter(request, "regend", "");
	int statdaynum = ParamUtils.getIntParameter(request, "statdaynum", 1);
	int showtype = ParamUtils.getIntParameter(request, "showtype", 0);
		java.util.Date regstartdate = DateUtil.parseDate(regstart + " 00:00:00","yyyy-MM-dd HH:mm:ss");
		java.util.Date regenddate = DateUtil.parseDate(regend + " 00:00:00","yyyy-MM-dd HH:mm:ss");
		RegisterTransStat regstat = new RegisterTransStat(regstartdate,
														regenddate,
														statdaynum); 
	if(regstart.length() > 0) {													
		regstat.statData();
	}
	HashMap<Long, HashMap<Long, Integer>> reg1Map = regstat.getReg1Map();
	HashMap<Long, HashMap<Long, Integer>> reg2Map = regstat.getReg2Map();
	HashMap<Long, HashMap<Long, Integer>> reg3Map = regstat.getReg3Map();
	HashMap<Long, HashMap<Long, Integer>> reg4Map = regstat.getReg4Map();
	HashMap<Long, HashMap<Long, Integer>> whiteMap = regstat.getWhiteMap();
	HashMap<Long, HashMap<Long, Integer>> level1Map = regstat.getLevel1Map();
	HashMap<Long, HashMap<Long, Integer>> level2Map = regstat.getLevel2Map();
	HashMap<Long, HashMap<Long, Integer>> level3Map = regstat.getLevel3Map();
	HashMap<Long, HashMap<Long, Integer>> level49Map = regstat.getLevel49Map();
	HashMap<Long, HashMap<Long, Integer>> level10Map = regstat.getLevel10Map();
	HashMap<Long, HashMap<Long, Integer>> level15Map = regstat.getLevel15Map();
	HashMap<Long, HashMap<Long, Integer>> level20Map = regstat.getLevel20Map();
	HashMap<Long, HashMap<Long, Integer>> level25Map = regstat.getLevel25Map();
	HashMap<Long, HashMap<Long, Integer>> level30Map = regstat.getLevel30Map();
	HashMap<Long, HashMap<Long, Integer>> level35Map = regstat.getLevel35Map();
	HashMap<Long, HashMap<Long, Integer>> level40Map = regstat.getLevel40Map();
	HashMap<Long, HashMap<Long, Integer>> level45Map = regstat.getLevel45Map();
	HashMap<Long, HashMap<Long, Integer>> level50Map = regstat.getLevel50Map();
	%>
	<form action="#" method="post" name="f1">
	<table align="left" cellpadding="0" cellspacing="0" border="0">
	 <tr>
	 	<td colspan="15" align="left" style="border-top:1px #ccc solid;">
	 	<p style="clear:both;text-align:left;">
		<label for="regstart">开始日期:</label><input type="text" id="regstart" name="regstart" size="10" value="<%=regstart %>"/>
		<label for="regend">结束日期:</label><input type="text" id="regend" name="regend" size="10" value="<%=regend %>"/>
		<label for="regstart">统计天数:</label><input type="text" id="statdaynum" name="statdaynum" size="5" value="<%=statdaynum %>"/>
		<label for="regstart">展开方式:</label>
		<select name=showtype>
			<option value="0">按渠道展开</option>
			<option value="1">按子渠道展开</option>
		</select>
		<input type=button name=bt1 value="查询" onclick="stat()"/>
		</p>
		</td>
	 </tr>
	</table>	<br/>
	<table id="test1" align="center" width="99%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	 <%
	 if(showtype == 0) {
	  %>
     <tr>
      <th width=40><b>渠道</b></th>
      <th width=50><b>直接注册</b></th>
	  <th width=50><b>游客进入</b></th>
	  <th width=40><b>转注册</b></th>
	  <th width=40><b>白名单</b></th>
	  <th width=40><b>总注册</b></th>
	  <th width=40><b>总注册(算游客)</b></th>
	  <th width=40><b>升级用户</b></th>
	  <th width=30><b>1级</b></th>
	  <th width=30><b>2级</b></th>
	  <th width=30><b>3级</b></th>
	  <th width=30><b>4-9级</b></th>
	  <th width=60><b>10-14级</b></th>
	  <th width=60><b>15-19级</b></th>
	  <th width=60><b>20-24级</b></th>
	  <th width=60><b>25-29级</b></th>
	  <th width=60><b>30-34级</b></th>
	  <th width=60><b>35-39级</b></th>
	  <th width=60><b>40-44级</b></th>
	  <th width=60><b>45-49级</b></th>
	  <th width=60><b>50级</b></th>
	  <th width=50><b>白名单率</b></th>
	  <th width=40><b>1级率</b></th>
	  <th width=40><b>2级率</b></th>
	  <th width=40><b>3级率</b></th>
	  <th width=40><b>4-9级率</b></th>
	  <th width=60><b>10-14级率</b></th>
	  <th width=60><b>15-19级率</b></th>
	  <th width=60><b>20-24级率</b></th>
	  <th width=60><b>25-29级率</b></th>
	  <th width=60><b>30-34级率</b></th>
	  <th width=60><b>35-39级率</b></th>
	  <th width=60><b>40-44级率</b></th>
	  <th width=60><b>45-49级率</b></th>
	  <th width=60><b>50级率</b></th>
     </tr>  
     <%} else {%>
     <tr>
      <th width=40><b>渠道</b></th>
      <th width=40><b>子渠道</b></th>
      <th width=60><b>直接注册</b></th>
	  <th width=60><b>游客进入</b></th>
	  <th width=40><b>转注册</b></th>
	  <th width=40><b>白名单</b></th>
	  <th width=40><b>总注册</b></th>
	  <th width=40><b>总注册(算游客)</b></th>
	  <th width=40><b>升级用户</b></th>
	  <th width=30><b>1级</b></th>
	  <th width=30><b>2级</b></th>
	  <th width=30><b>3级</b></th>
	  <th width=30><b>4-9级</b></th>
	  <th width=60><b>10-14级</b></th>
	  <th width=60><b>15-19级</b></th>
	  <th width=60><b>20-24级</b></th>
	  <th width=60><b>25-29级</b></th>
	  <th width=60><b>30-34级</b></th>
	  <th width=60><b>35-39级</b></th>
	  <th width=60><b>40-44级</b></th>
	  <th width=60><b>45-49级</b></th>
	  <th width=60><b>50级</b></th>
	  <th width=60><b>白名单率</b></th>
	  <th width=40><b>1级率</b></th>
	  <th width=40><b>2级率</b></th>
	  <th width=40><b>3级率</b></th>
	  <th width=40><b>4-9级率</b></th>
	  <th width=60><b>10-14级率</b></th>
	  <th width=60><b>15-19级率</b></th>
	  <th width=60><b>20-24级率</b></th>
	  <th width=60><b>25-29级率</b></th>
	  <th width=60><b>30-34级率</b></th>
	  <th width=60><b>35-39级率</b></th>
	  <th width=60><b>40-44级率</b></th>
	  <th width=60><b>45-49级率</b></th>
	  <th width=60><b>50级率</b></th>
     </tr>  
     <%} %>
    <tbody>
	 <%
	 ChannelManager cmanager = ChannelManager.getInstance();
	 ChannelItemManager cimanager = ChannelItemManager.getInstance();
	 HashMap<Long, Integer> channelUserMap = regstat.getChannelUserMap();
	 if(showtype == 0) {
	 	Long channels[] = reg1Map.keySet().toArray(new Long[0]);
	 	for(Long channelid : channels) {
	 		//直接注册
	 		HashMap<Long, Integer> itemMap = reg1Map.get(channelid);
	 		int normalReg = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			normalReg += value;
		 		}
		 	}
	 		//游客
	 		itemMap = reg2Map.get(channelid);
	 		int quickReg = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			quickReg += value;
		 		}
		 	}
	 		//转注册
	 		itemMap = reg3Map.get(channelid);
	 		int transReg = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			transReg += value;
		 		}
		 	}
	 		//总注册
	 		itemMap = reg4Map.get(channelid);
	 		int totalReg = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			totalReg += value;
		 		}
		 	}
	 		//白名单
	 		itemMap = whiteMap.get(channelid);
	 		int whiteuser = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			whiteuser += value;
		 		}
		 	}
		 	//白名单率
		 	float whiteuserRate = NumberUtils.cutFloat(new Float(whiteuser)*100/new Float(totalReg),1);
	 		//1级
	 		itemMap = level1Map.get(channelid);
	 		int level1 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level1 += value;
		 		}
		 	}
		 	int channelusernum = channelUserMap.get(channelid);
		 	//1级率
		 	float level1Rate = NumberUtils.cutFloat(new Float(level1)*100/new Float(channelusernum),1);
	 		//2级
	 		itemMap = level2Map.get(channelid);
	 		int level2 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level2 += value;
		 		}
		 	}
		 	//2级率
		 	float level2Rate = NumberUtils.cutFloat(new Float(level2)*100/new Float(channelusernum),1);
	 		//3级
	 		itemMap = level3Map.get(channelid);
	 		int level3 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level3 += value;
		 		}
		 	}
		 	//3级率
		 	float level3Rate = NumberUtils.cutFloat(new Float(level3)*100/new Float(channelusernum),1);
		 	//49级
	 		itemMap = level49Map.get(channelid);
	 		int level49 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level49 += value;
		 		}
		 	}
		 	//49级率
		 	float level49Rate = NumberUtils.cutFloat(new Float(level49)*100/new Float(channelusernum),1);
	 		//10-14级
	 		itemMap = level10Map.get(channelid);
	 		int level10 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level10 += value;
		 		}
		 	}
		 	//10级率
		 	float level10Rate = NumberUtils.cutFloat(new Float(level10)*100/new Float(channelusernum),1);
		 	
		 	//15-19级
	 		itemMap = level15Map.get(channelid);
	 		int level15 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level15 += value;
		 		}
		 	}
		 	//15级率
		 	float level15Rate = NumberUtils.cutFloat(new Float(level15)*100/new Float(channelusernum),1);
		 	
		 	//20-24级
	 		itemMap = level20Map.get(channelid);
	 		int level20 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level20 += value;
		 		}
		 	}
		 	//20级率
		 	float level20Rate = NumberUtils.cutFloat(new Float(level20)*100/new Float(channelusernum),1);
		 	
		 	//25-29级
	 		itemMap = level25Map.get(channelid);
	 		int level25 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level25 += value;
		 		}
		 	}
		 	//25级率
		 	float level25Rate = NumberUtils.cutFloat(new Float(level25)*100/new Float(channelusernum),1);
		 	
		 	//30-34级
	 		itemMap = level30Map.get(channelid);
	 		int level30 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level30 += value;
		 		}
		 	}
		 	//30级率
		 	float level30Rate = NumberUtils.cutFloat(new Float(level30)*100/new Float(channelusernum),1);
		 	
		 	//35-39级
	 		itemMap = level35Map.get(channelid);
	 		int level35 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level35 += value;
		 		}
		 	}
		 	//35级率
		 	float level35Rate = NumberUtils.cutFloat(new Float(level35)*100/new Float(channelusernum),1);
		 	
		 	//40-44级
	 		itemMap = level40Map.get(channelid);
	 		int level40 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level40 += value;
		 		}
		 	}
		 	//40级率
		 	float level40Rate = NumberUtils.cutFloat(new Float(level40)*100/new Float(channelusernum),1);
		 	
		 	//45-49级
	 		itemMap = level45Map.get(channelid);
	 		int level45 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level45 += value;
		 		}
		 	}
		 	//45级率
		 	float level45Rate = NumberUtils.cutFloat(new Float(level45)*100/new Float(channelusernum),1);
		 	
		 	//50级
	 		itemMap = level50Map.get(channelid);
	 		int level50 = 0;
	 		if(itemMap != null) {
		 		Integer values[] = itemMap.values().toArray(new Integer[0]);
		 		for(Integer value : values) {
		 			level50 += value;
		 		}
		 	}
		 	//50级率
		 	float level50Rate = NumberUtils.cutFloat(new Float(level50)*100/new Float(channelusernum),1);
		 	
		 	Channel channel = cmanager.getChannel(channelid);
	 %>
	 <tr>
		<td><%=channel.getName() %></td>
		<td><%=normalReg %></td>
		<td><%=quickReg %></td>
		<td><%=transReg %></td>
		<td><%=whiteuser %></td>
		<td><%=totalReg %></td>
		<td><%=totalReg+quickReg %></td>
		<td><%=channelusernum %></td>
		<td><%=level1 %></td>
		<td><%=level2 %></td>
		<td><%=level3 %></td>
		<td><%=level49 %></td>
		<td><%=level10 %></td>
		<td><%=level15 %></td>
		<td><%=level20 %></td>
		<td><%=level25 %></td>
		<td><%=level30 %></td>
		<td><%=level35 %></td>
		<td><%=level40 %></td>
		<td><%=level45 %></td>
		<td><%=level50 %></td>
		<td><%=whiteuserRate %>%</td>
		<td><%=level1Rate %>%</td>
		<td><%=level2Rate %>%</td>
		<td><%=level3Rate %>%</td>
		<td><%=level49Rate %>%</td>
		<td><%=level15Rate %>%</td>
		<td><%=level20Rate %>%</td>
		<td><%=level25Rate %>%</td>
		<td><%=level30Rate %>%</td>
		<td><%=level35Rate %>%</td>
		<td><%=level40Rate %>%</td>
		<td><%=level45Rate %>%</td>
		<td><%=level50Rate %>%</td>
	 </tr> 
	 <%}} else {	
	 	Long channels[] = reg1Map.keySet().toArray(new Long[0]);
	 	for(Long channelid : channels) {
	 		HashMap<Long, Integer> itemMap = reg1Map.get(channelid);
	 		Long channelitems[] = itemMap.keySet().toArray(new Long[0]);
	 		int channelusernum = channelUserMap.get(channelid);
	 		for(Long channelitemid : channelitems) {
	 			//正常注册
	 			int normalReg = 0;
	 			try {
	 				normalReg = reg1Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
	 			//游客
	 			int quickReg = 0;
	 			try {
	 				quickReg = reg2Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
	 			//转注册
	 			int transReg = 0;
	 			try {
	 				transReg = reg3Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
	 			//总注册
	 			int totalReg = 0;
	 			try {
	 				totalReg = reg4Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
	 			//白名单
	 			int whiteuser = 0;
	 			try {
	 				whiteuser = whiteMap.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//白名单率
			 	float whiteuserRate = NumberUtils.cutFloat(new Float(whiteuser)*100/new Float(totalReg),1);
	 			//1级
	 			int level1 = 0;
	 			try {
	 				level1 = level1Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//1级率
			 	float level1Rate = NumberUtils.cutFloat(new Float(level1)*100/new Float(channelusernum),1);
	 			//2级
	 			int level2 = 0;
	 			try {
	 				level2 = level2Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//2级率
			 	float level2Rate = NumberUtils.cutFloat(new Float(level2)*100/new Float(channelusernum),1);
		 		//3级
	 			int level3 = 0;
	 			try {
	 				level3 = level3Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//3级率
			 	float level3Rate = NumberUtils.cutFloat(new Float(level3)*100/new Float(channelusernum),1);
			 	//49级
	 			int level49 = 0;
	 			try {
	 				level49 = level49Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//49级率
			 	float level49Rate = NumberUtils.cutFloat(new Float(level49)*100/new Float(channelusernum),1);
		 		//10-14级以上
	 			int level10 = 0;
	 			try {
	 				level10 = level10Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//10级率
			 	float level10Rate = NumberUtils.cutFloat(new Float(level10)*100/new Float(channelusernum),1);
			 	
			 	//15-19级以上
	 			int level15 = 0;
	 			try {
	 				level15 = level15Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//15级率
			 	float level15Rate = NumberUtils.cutFloat(new Float(level15)*100/new Float(channelusernum),1);
			 	
			 	//20-24级以上
	 			int level20 = 0;
	 			try {
	 				level20 = level20Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//20级率
			 	float level20Rate = NumberUtils.cutFloat(new Float(level20)*100/new Float(channelusernum),1);
			 	
			 	//25-29级以上
	 			int level25 = 0;
	 			try {
	 				level25 = level25Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//25级率
			 	float level25Rate = NumberUtils.cutFloat(new Float(level25)*100/new Float(channelusernum),1);
			 	
			 	//30-34级以上
	 			int level30 = 0;
	 			try {
	 				level30 = level30Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//30级率
			 	float level30Rate = NumberUtils.cutFloat(new Float(level30)*100/new Float(channelusernum),1);
			 	
			 	//35-39级以上
	 			int level35 = 0;
	 			try {
	 				level35 = level35Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//35级率
			 	float level35Rate = NumberUtils.cutFloat(new Float(level35)*100/new Float(channelusernum),1);
			 	
			 	//40-44级以上
	 			int level40 = 0;
	 			try {
	 				level40 = level40Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//40级率
			 	float level40Rate = NumberUtils.cutFloat(new Float(level40)*100/new Float(channelusernum),1);
			 	
			 	//45-49级以上
	 			int level45 = 0;
	 			try {
	 				level45 = level45Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//45级率
			 	float level45Rate = NumberUtils.cutFloat(new Float(level45)*100/new Float(channelusernum),1);
			 	
			 	//50级以上
	 			int level50 = 0;
	 			try {
	 				level50 = level50Map.get(channelid).get(channelitemid);
	 			} catch(Exception e) {
	 				//
	 			} 
			 	//50级率
			 	float level50Rate = NumberUtils.cutFloat(new Float(level50)*100/new Float(channelusernum),1);
			 	
			 	Channel channel = cmanager.getChannel(channelid);
			 	ChannelItem channelitem = cimanager.getChannelItem(channelitemid);
			 %>
			 <tr>
				<td><%=channel.getName() %></td>
				<td><%=channelitem.getName() %></td>
				<td><%=normalReg %></td>
				<td><%=quickReg %></td>
				<td><%=transReg %></td>
				<td><%=whiteuser %></td>
				<td><%=totalReg %></td>
				<td><%=totalReg+quickReg %></td>
				<td><%=channelusernum %></td>
				<td><%=level1 %></td>
				<td><%=level2 %></td>
				<td><%=level3 %></td>
				<td><%=level49 %></td>
				<td><%=level10 %></td>
				<td><%=level15 %></td>
				<td><%=level20 %></td>
				<td><%=level25 %></td>
				<td><%=level30 %></td>
				<td><%=level35 %></td>
				<td><%=level40 %></td>
				<td><%=level45 %></td>
				<td><%=level50 %></td>
				<td><%=whiteuserRate %>%</td>
				<td><%=level1Rate %>%</td>
				<td><%=level2Rate %>%</td>
				<td><%=level3Rate %>%</td>
				<td><%=level49Rate %>%</td>
				<td><%=level15Rate %>%</td>
				<td><%=level20Rate %>%</td>
				<td><%=level25Rate %>%</td>
				<td><%=level30Rate %>%</td>
				<td><%=level35Rate %>%</td>
				<td><%=level40Rate %>%</td>
				<td><%=level45Rate %>%</td>
				<td><%=level50Rate %>%</td>
			 </tr> 
			 <%}
			}
		} 
		%>
	 </tbody>
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
