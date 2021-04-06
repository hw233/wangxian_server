<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User"
	%><%!
	long lastRequestTime = 0;
	//保存当天的用户列表
	ArrayList<String> todayList = null;
	
%><%

            ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
            UserDao userDao=(UserDao)ctx.getBean("UserDao");
    
    
         User user= userDao.getById(52L);
         out.print(user.getName());
    
    
	String resinHome = System.getProperty("resin.home");

	DefaultDiskCache cache = null;
	if(cache == null){
		cache = new DefaultDiskCache(new File(""+resinHome+"/webapps/webgame/admin/stat/nation_stat.ddc"),"nation_stat",10L*365L*24L*3600L*100L);
	}
	
	String day = request.getParameter("day");
	if(day == null) day = DateUtil.formatDate(new Date(),"yyyy-MM-dd");

	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	
		
	String command = null;
	command = ""+resinHome+"/webgame_server/bin/stat_nations.sh "+resinHome+" " + day;
	
		
	boolean needScanFile = false;
	if(cache.get(day) == null && day.equals(today) == false) needScanFile = true;
	if(needScanFile == false && day.equals(today) && todayList == null) needScanFile = true;
	if(needScanFile == false && day.equals(today) && System.currentTimeMillis() - lastRequestTime > 5 * 60 * 1000) needScanFile = true;
	if(needScanFile){
		Process p = Runtime.getRuntime().exec(command);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		ArrayList<String> al = new ArrayList<String>();
		
		while( (line = reader.readLine()) != null){
			al.add(line);
		}
		if(day.equals(today) == false)
			cache.put(day,al);
		else{
			lastRequestTime = System.currentTimeMillis();
			todayList = al;
		}
	}
	
	ArrayList<String> startDayAl = (ArrayList<String>)cache.get(day);
	if(startDayAl == null) startDayAl =new ArrayList<String>();
	if(day.equals(today)) startDayAl = todayList;
	
	
%>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
		<script language="javascript" type="text/javascript" src="../js/flotr/lib/prototype-1.6.0.2.js"></script>
		<script language="javascript" type="text/javascript" src="../js/flotr/flotr-0.2.0-alpha.js"></script>
	</head>

	<body>
		
		<h2 style="font-weight:bold;">
			<%=day %>国家分布统计分布
		</h2>
		<form>输入日期：<input type='text' name='day' value='<%=day %>'>(格式：2011-01-01) 
		<input type='submit' value='提交'> </form>
		<br/>
		
		<center>
			<table id="test1" align="center" width="90%" cellpadding="1" bgcolor='#000000'
				cellspacing="1" border="0">
				<tr bgcolor='#EEEEBB'><td>国家</td><td>玩家数量</td></tr>
			<%
					for(int j = 0 ; j < startDayAl.size() ; j++){
						String ss[] = startDayAl.get(j).split(" ");
						String nation=ss[0];
						String count=ss[1];
					%><tr bgcolor='#FFFFFF'><td> <% if("0".equals(nation)){ out.print("魏国");}  
					                                if("1".equals(nation)){ out.print("蜀国");}    
					                                if("2".equals(nation)){ out.print("吴国");}  
					                               %></td><td><%= count %></td></tr><% 
						
					}
			%>
			</table>
			<div id="container1" style="width:100%;height:400px;display:block;"></div>
			<script>
				<!--drawFlotr1();-->
			</script>
			
		<center>
		<br>
		<i>此页面5分钟执行扫描文件一次</i>
		<br>
	</body>
</html>
