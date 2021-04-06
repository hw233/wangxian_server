<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.operating.activitybuff.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.DateFormat"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%><%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>系统定时刷新</title>
<% 
ActivityFlushBossItemManager bossFlush = ActivityFlushBossItemManager.getInstance();
if(bossFlush == null){
	return;
}

String action = request.getParameter("action");
if(action != null && action.equals("create")){
	try{
	int id = 	Integer.parseInt(request.getParameter("itemid"));
	
	int bossId = Integer.parseInt(request.getParameter("bossId"));
	String descp = request.getParameter("descp");
	int timeType = Integer.parseInt(request.getParameter("timeType"));
	int weekDay = Integer.parseInt(request.getParameter("weekDay"));
	String fixDay = request.getParameter("fixDay");
	String betweenTime = request.getParameter("betweenTime");
	String intervalTimeForBetweenTime = request.getParameter("intervalTimeForBetweenTime");
	if(intervalTimeForBetweenTime == null || intervalTimeForBetweenTime.trim().equals("")){
		intervalTimeForBetweenTime = "0";
	}
	String startTimeInDay = request.getParameter("startTimeInDay");
	String endTimeInDay = request.getParameter("endTimeInDay");
	String mapName = request.getParameter("mapName");

	String sayContentToWorld = request.getParameter("sayContentToWorld");
	if(mapName != null){
		ActivityFlushBossItem afbi = bossFlush.getActivityFlushBossItem(id);
		if(afbi == null)
			afbi = bossFlush.createActivityBuffItem(descp);
		else
			afbi.setDesp(descp);
		
		afbi.setBossId(bossId);
		afbi.setTimeType(timeType);
		afbi.setWeekDay(weekDay);
		afbi.setFixDay(fixDay);
		afbi.setBetweenTime(betweenTime);
		afbi.setIntervalTimeForBetweenTime(Long.parseLong(intervalTimeForBetweenTime));
		afbi.setStartTimeInDay(startTimeInDay);
		afbi.setEndTimeInDay(endTimeInDay);
		afbi.setMapName(mapName);
		afbi.setMonsterFlag("true".equals(request.getParameter("MonsterFlag")));
		

		int xxx[][] = new int[][]{{0}};
		int yyy[][] = new int[][]{{0}};
		try{
			String xssTemp[] = request.getParameter("x").split("\\|");
			String yssTemp[] = request.getParameter("y").split("\\|");
			xxx = new int[xssTemp.length][];
			yyy = new int[yssTemp.length][];
			for(int j = 0; j < xssTemp.length; j++){
				String[] xsTemp = xssTemp[j].split(",");
				xxx[j] = new int[xsTemp.length];
				for(int k = 0; k < xsTemp.length; k++){
					xxx[j][k] = Integer.parseInt(xsTemp[k]);
				}
			}
			
			for(int j = 0; j < yssTemp.length; j++){
				String[] ysTemp = yssTemp[j].split(",");
				yyy[j] = new int[ysTemp.length];
				for(int k = 0; k < ysTemp.length; k++){
					yyy[j][k] = Integer.parseInt(ysTemp[k]);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			out.println("<h1><font color='red'>boss刷新位置设置出错</font></h1>");
			if(afbi.getX() != null && afbi.getX().length != 0){
				xxx = afbi.getX();
				yyy = afbi.getY();
			}else{
				xxx = new int[][]{{0}};
				yyy = new int[][]{{0}};
			}
		}
		
		afbi.setX(xxx);
		afbi.setY(yyy);
		out.println("x:"+bossFlush.getArrayString(afbi.getX())+"y:"+bossFlush.getArrayString(afbi.getY()));

		
	
		
		afbi.setSayContentToWorld(sayContentToWorld);
		bossFlush.saveAll();
	}
	}catch(Exception e){
		out.println("<font color='red'>创建失败，请正确填写信息</font><br>"+StringUtil.getStackTrace(e));
	}
	
}else if(action != null && action.equals("delete")){
	String idStr = request.getParameter("id");
	try{
		int id = Integer.parseInt(idStr);
		bossFlush.deleteActivityFlushBossItem(id);	
	}catch(Exception e){
		
	}
	
}else if(action != null && action.equals("saveAll")){
	bossFlush.saveAll();
}
ActivityFlushBossItem[] afbis = bossFlush.getActivityFlushBossItems();

ActivityFlushBossItem ai = null;
boolean modify = false;
if(action != null && action.equals("modify")){
	String idStr = request.getParameter("id");
	modify = true;
	try{
		int id = Integer.parseInt(idStr);
		ai = bossFlush.getActivityFlushBossItem(id);	
	}catch(Exception e){
		
	}
}else if(action != null && action.equals("copy")){
	String idStr = request.getParameter("id");
	modify =false;
	try{
		int id = Integer.parseInt(idStr);
		ai = bossFlush.getActivityFlushBossItem(id);	
	}catch(Exception e){
		
	}
}
%>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titleColor{
background-color:#C2CAF5;
}
</style>
<script type="text/javascript">
function deleteBoss(ids){
	document.getElementById("id").value=ids;
	document.f2.submit();
}
</script>
</head>
<body>
<a href="flushBoss.jsp">刷新此页面</a>
<br>
<br>
<table>
<tr class="titleColor">
<td>ID</td>
<td>刷新时间类型</td>
<td>每周几</td>
<td>日期</td>
<td>每隔一段时间</td>
<td>间隔多少秒</td>
<td>开始时间</td>
<td>结束时间</td>
<td>地图限制</td>
<td>刷新坐标点</td>
<td>怪或NPCId</td>
<td>是否为怪</td>
<td>世界喊话</td>
<td>描述</td>
<td>最后一次刷新时间</td>
<td></td>
</tr>
<%
if(afbis != null){
	for(ActivityFlushBossItem afbi : afbis){
		if(afbi != null){
			String day = "";
			switch(afbi.getWeekDay()){
			case 0:day="星期日";break;
			case 1:day="星期一";break;
			case 2:day="星期二";break;
			case 3:day="星期三";break;
			case 4:day="星期四";break;
			case 5:day="星期五";break;
			case 6:day="星期六";break;
			}
			
			StringBuffer sb = new StringBuffer();
			for(int j = 0 ;afbi.getX() != null && j < afbi.getX().length ; j++){
				if(afbi.getY() != null && j < afbi.getY().length){
					for(int k = 0; afbi.getX()[j] != null && k < afbi.getX()[j].length; k++){
						if(afbi.getY()[j] != null && k < afbi.getY()[j].length)
							sb.append("("+afbi.getX()[j][k]+","+afbi.getY()[j][k]+")");
						else
							sb.append("("+afbi.getX()[j][k]+",---)");
					}
				}
				if(j < afbi.getX().length -1){
					sb.append("|");
				}
			}
			
			
			%>
			
			<tr>
			<td><%=afbi.getId() %></td>
			<td><%=ActivityFlushBossItem.TIME_TYPE_NAMES[afbi.getTimeType()] %></td>
			<td><%=day %></td>
			<td><%=afbi.getFixDay() %></td>
			<td><%=afbi.getBetweenTime() %></td>
			<td><%=afbi.getIntervalTimeForBetweenTime() %>秒</td>
			<td><%=afbi.getStartTimeInDay() %></td>
			<td><%=afbi.getEndTimeInDay() %></td>
			<td><%=afbi.getMapName() %></td>
			<td><%= sb.toString() %></td>
			<td><%=afbi.getBossId() %></td>
			<td><%=afbi.isMonsterFlag() %></td>
			<td><%=afbi.getSayContentToWorld() %></td>
			<td><%=afbi.getDesp() %></td>
			<td><%= DateUtil.formatDate(new Date(afbi.getLastFlushTime()),"yyyy-MM-dd HH:mm:ss")  %></td>
			<td><a href='./flushBoss.jsp?action=modify&id=<%= afbi.getId() %>'>修改</a></td>
			<td><a href='./flushBoss.jsp?action=copy&id=<%= afbi.getId() %>'>复制</a></td>
			<td><input type="button" value="删除" onclick="javascript:deleteBoss(<%=afbi.getId() %>);">
			</td>
			</tr>
			<%
		}
	}
}
%>
</table>

<h3>创建新的条目/修改条目</h3>

<form name="f1" id="f1">
<input type="hidden" name="action" value="create">
<input type="hidden" name="itemid" value="<%= (ai == null?0:(modify?ai.getId():0)) %>">
<table>

<tr>
<td>刷新时间类型:</td><td width='60%'><select name="timeType">
<option value="0" <% if(ai !=null && ai.getTimeType()==0){out.print("selected");} %> >每天
<option value="1" <% if(ai !=null && ai.getTimeType()==1){out.print("selected");} %> >每周几
<option value="2" <% if(ai !=null && ai.getTimeType()==2){out.print("selected");} %> >固定日期
<option value="3" <% if(ai !=null && ai.getTimeType()==3){out.print("selected");} %> >每隔一段时间
</select></td><td></td></tr>


<tr>
<td>每周几:</td>
<td><select name="weekDay">
<option value="0" <% if(ai !=null && ai.getWeekDay()==0){out.print("selected");} %> >星期日
<option value="1" <% if(ai !=null && ai.getWeekDay()==1){out.print("selected");} %> >星期一
<option value="2" <% if(ai !=null && ai.getWeekDay()==2){out.print("selected");} %> >星期二
<option value="3" <% if(ai !=null && ai.getWeekDay()==3){out.print("selected");} %> >星期三
<option value="4" <% if(ai !=null && ai.getWeekDay()==4){out.print("selected");} %> >星期四
<option value="5" <% if(ai !=null && ai.getWeekDay()==5){out.print("selected");} %> >星期五
<option value="6" <% if(ai !=null && ai.getWeekDay()==6){out.print("selected");} %> >星期六
</select></td>
<td>只有刷新时间类型为每周几的时候才有用</td></tr>

<tr>
<td>固定日期(填写格式:2010-06-20):</td>
<td><input type="text" name="fixDay" id="fixDay" value="<%= (ai==null?"":ai.getFixDay()) %>"></td>
<td>只有刷新时间类型为固定时间的才需要填写</td></tr>
<tr>
<tr>
<td>每隔一段时间(填写格式:2010-06-20-00-01~2010-12-31-23-59):</td>
<td><input type="text" style="width:300px" name="betweenTime" id="betweenTime" value="<%= (ai==null?"":ai.getBetweenTime()) %>">间隔多长时间刷一次怪，单位为秒:<input type="text" name="intervalTimeForBetweenTime" id="intervalTimeForBetweenTime" value="<%= (ai==null?"":ai.getIntervalTimeForBetweenTime()) %>"></td>
<td>只有刷新时间类型为每隔一段时间的才需要填写</td></tr>
<tr>
<td>开始时间(填写格式:00:00):</td>
<td><input type="text" name="startTimeInDay" id="startTimeInDay" value="<%= (ai==null?"":ai.getStartTimeInDay()) %>"></td>
<td>每天那个时间点开始刷新</td></tr>
<tr>
<td>结束时间(填写格式:23:59):</td>
<td><input type="text" name="endTimeInDay" id="endTimeInDay" value="<%= (ai==null?"":ai.getEndTimeInDay()) %>"></td>
<td>不太重要的属性，一般填写开始刷新时间延后1小时</td></tr>

<tr>
<td>地图(<font color="red">不能为空</font>):</td>
<td><input type="text" name="mapName" id="mapName" value="<%= (ai==null?"":ai.getMapName()) %>"></td>
<td>刷新的怪或者NPC地图</td></tr>
<tr>
<td>x坐标(<font color="red">不能为空</font>):</td>
<td><input type="text" name="x" id="x" size='100' value="<%= (ai==null?"":bossFlush.getArrayString(ai.getX())) %>">x坐标，多个用英文的逗号分隔,如果想随机刷需要在坐标后加  | 格式如 0,0,0|0,0,0</td>
<td></td></tr>
<tr>
<td>y坐标(<font color="red">不能为空</font>):</td>
<td><input type="text" name="y" id="y" size='100' value="<%= (ai==null?"":bossFlush.getArrayString(ai.getY())) %>">y坐标，多个用英文的逗号分隔,如果想随机刷需要在坐标后加  | 格式如 0,0,0|0,0,0</td>
<td></td></tr>

<tr>
<td>怪或者NPC的ID(<font color="red">不能为空</font>):</td>
<td><input type="text" name="bossId" id="bossId" value="<%= (ai==null?"":ai.getBossId()) %>"></td>
<td></td></tr>

<tr>
<td>是否为怪:</td>
<td><input type="text" name="MonsterFlag" id="MonsterFlag" value="<%= (ai==null?"":ai.isMonsterFlag()) %>"></td>
<td></td></tr>

<tr>
<td>世界喊话:</td>
<td><input type="text" name="sayContentToWorld" size='100' id="sayContentToWorld" value="<%= (ai==null?"":ai.getSayContentToWorld()) %>"></td>
<td></td></tr>

<tr>
<td>描述:</td>
<td><input type="text" name="descp" id="descp" size='100' value="<%= (ai==null?"":ai.getDesp()) %>"></td>
<td></td></tr>

<tr>
<td><input type="submit" value="创建"><input type="reset" value="重写"></td>
<td></td><td></td></tr>
</table>

</form>

<form name="f2" id="f2">
<input type="hidden" name="action" value="delete">
<input type="hidden" name="id" id="id">
</form>
<br>
<form name="f3" id="f3">
<input type="hidden" name="action" value="saveAll">
<input type="submit" value="保存全部信息">
</form>
</body>
</html>
