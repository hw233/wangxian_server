<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="
java.util.*,
com.fy.engineserver.battlefield.*,
com.fy.engineserver.battlefield.concrete.* " %>
<!DOCTYPE html  "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.battlefield.concrete.BattleFieldLineupService.WaittingItem"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.engineserver.battleorganizingcommittee.*" %>

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%><%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<link rel="stylesheet" type="text/css" href="../../css/table.css" />
<title>战场</title>
<%
	BattleFieldManager bfm = BattleFieldManager.getInstance();
	BattleOrganizingCommitteeManager bocm = BattleOrganizingCommitteeManager.getInstance();
	if(bocm == null){
		out.println("<tr><td>比武大会管理还未开启");
		return;
	}
	
	String needArticleName = request.getParameter("needArticleName");
	String watchNeedMoney = request.getParameter("watchNeedMoney");
	String running = request.getParameter("running");
	String signUpdate = request.getParameter("signUpdate");
	String endSignUpdate = request.getParameter("endSignUpdate");
	String makeGroupsFlag = request.getParameter("makeGroupsFlag");
	String matchFlag = request.getParameter("matchFlag");
	String qualifier1End = request.getParameter("qualifier1End");
	String qualifier2End = request.getParameter("qualifier2End");
	String qualifier3End = request.getParameter("qualifier3End");
	String qualifier4End = request.getParameter("qualifier4End");
	String finalMatchStart = request.getParameter("finalMatchStart");
	String finalMatch1End = request.getParameter("finalMatch1End");
	String finalMatch2End = request.getParameter("finalMatch2End");
	String finalMatch3End = request.getParameter("finalMatch3End");
	String finalMatch4End = request.getParameter("finalMatch4End");
	String finalMatch5End = request.getParameter("finalMatch5End");
	String finalMatch6End = request.getParameter("finalMatch6End");
	String finalMatch2OneSideLeftPlayerCount = request.getParameter("finalMatch2OneSideLeftPlayerCount");
	String finalMatch3OneSideLeftPlayerCount = request.getParameter("finalMatch3OneSideLeftPlayerCount");
	String finalMatch4OneSideLeftPlayerCount = request.getParameter("finalMatch4OneSideLeftPlayerCount");
	String finalMatch5OneSideLeftPlayerCount = request.getParameter("finalMatch5OneSideLeftPlayerCount");
	String finalMatch6LeftPlayerCount = request.getParameter("finalMatch6LeftPlayerCount");
	String qualifierBeginTime = request.getParameter("qualifierBeginTime");
	String allBigGroupCounty = request.getParameter("allBigGroupCounty");
	String allBigGroupCount = request.getParameter("allBigGroupCount");
	String bigGroupCountInEveryday = request.getParameter("bigGroupCountInEveryday");
	String final1BeginTime = request.getParameter("final1BeginTime");
	String final2BeginTime = request.getParameter("final2BeginTime");
	String final3BeginTime = request.getParameter("final3BeginTime");
	String final4BeginTime = request.getParameter("final4BeginTime");
	String final5BeginTime = request.getParameter("final5BeginTime");
	String final6BeginTime = request.getParameter("final6BeginTime");
	String signUpNumber = request.getParameter("signUpNumber");
	String signUpMinLevel = request.getParameter("signUpMinLevel");
	String NEXT_MATCH_START_TIME_LENGTH = request.getParameter("NEXT_MATCH_START_TIME_LENGTH");
	String START_REST_TIME_LENGTH = request.getParameter("START_REST_TIME_LENGTH");
	String NEXT_FINAL_MATCH_START_TIME_LENGTH = request.getParameter("NEXT_FINAL_MATCH_START_TIME_LENGTH");
	String START_FINAL_REST_TIME_LENGTH = request.getParameter("START_FINAL_REST_TIME_LENGTH");
	String isAllowWatcherGoInTo = request.getParameter("isAllowWatcherGoInTo");
	String watcherNumMultiple = request.getParameter("watcherNumMultiple");
	Calendar calendar = Calendar.getInstance();
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	boolean modify = false;
	if(needArticleName != null && !needArticleName.trim().equals("")){
		BattleOrganizingCommitteeManager.needArticleName = needArticleName.trim();
		modify = true;
	}
	if(watchNeedMoney != null && !watchNeedMoney.trim().equals("")){
		try{
		int money = Integer.parseInt(watchNeedMoney.trim());
		bocm.watchNeedMoney = money;
		modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
		
	}
	if(running != null && !running.trim().equals("")){
		if(running.trim().equals("true")){
	bocm.setRunning(true);
		}else{
	bocm.setRunning(false);
		}
		modify = true;
	}
	if(signUpdate != null && !signUpdate.trim().equals("")){
		if(signUpdate.trim().length() == 12){
	try{
	int year = Integer.parseInt(signUpdate.trim().substring(0,4));
	int month = Integer.parseInt(signUpdate.trim().substring(4,6));
	int day = Integer.parseInt(signUpdate.trim().substring(6,8));
	int hour = Integer.parseInt(signUpdate.trim().substring(8,10));
	int minute = Integer.parseInt(signUpdate.trim().substring(10,12));
	calendar.clear();
	calendar.set(year,month-1,day,hour,minute);
	bocm.setSignUpdate(calendar.getTimeInMillis());
	modify = true;
	}catch(Exception ex){
		ex.printStackTrace();
	}
		}
	}
	if(endSignUpdate != null && !endSignUpdate.trim().equals("")){
		if(endSignUpdate.trim().length() == 12){
	try{
	int year = Integer.parseInt(endSignUpdate.trim().substring(0,4));
	int month = Integer.parseInt(endSignUpdate.trim().substring(4,6));
	int day = Integer.parseInt(endSignUpdate.trim().substring(6,8));
	int hour = Integer.parseInt(endSignUpdate.trim().substring(8,10));
	int minute = Integer.parseInt(endSignUpdate.trim().substring(10,12));
	calendar.clear();
	calendar.set(year,month-1,day,hour,minute);
	bocm.setEndSignUpdate(calendar.getTimeInMillis());
	modify = true;
	}catch(Exception ex){
		ex.printStackTrace();
	}
		}
	}
	if(makeGroupsFlag != null && !makeGroupsFlag.trim().equals("")){
		if(makeGroupsFlag.trim().equals("true")){
	bocm.setMakeGroupsFlag(true);
		}else{
	bocm.setMakeGroupsFlag(false);
		}
		modify = true;
	}
	if(matchFlag != null && !matchFlag.trim().equals("")){
		if(matchFlag.trim().equals("true")){
	bocm.setMatchFlag(true);
		}else{
	bocm.setMatchFlag(false);
		}
		modify = true;
	}
	if(qualifier1End != null && !qualifier1End.trim().equals("")){
		if(qualifier1End.trim().equals("true")){
	bocm.setQualifier1End(true);
		}else{
	bocm.setQualifier1End(false);
		}
		modify = true;
	}
	if(qualifier2End != null && !qualifier2End.trim().equals("")){
		if(qualifier2End.trim().equals("true")){
	bocm.setQualifier2End(true);
		}else{
	bocm.setQualifier2End(false);
		}
		modify = true;
	}
	if(qualifier3End != null && !qualifier3End.trim().equals("")){
		if(qualifier3End.trim().equals("true")){
	bocm.setQualifier3End(true);
		}else{
	bocm.setQualifier3End(false);
		}
		modify = true;
	}
	if(qualifier4End != null && !qualifier4End.trim().equals("")){
		if(qualifier4End.trim().equals("true")){
	bocm.setQualifier4End(true);
		}else{
	bocm.setQualifier4End(false);
		}
		modify = true;
	}
	if(finalMatchStart != null && !finalMatchStart.trim().equals("")){
		if(finalMatchStart.trim().equals("true")){
	bocm.setFinalMatchStart(true);
		}else{
	bocm.setFinalMatchStart(false);
		}
		modify = true;
	}
	if(finalMatch1End != null && !finalMatch1End.trim().equals("")){
		if(finalMatch1End.trim().equals("true")){
	bocm.setFinalMatch1End(true);
		}else{
	bocm.setFinalMatch1End(false);
		}
		modify = true;
	}
	if(finalMatch2End != null && !finalMatch2End.trim().equals("")){
		if(finalMatch2End.trim().equals("true")){
	bocm.setFinalMatch2End(true);
		}else{
	bocm.setFinalMatch2End(false);
		}
		modify = true;
	}
	if(finalMatch3End != null && !finalMatch3End.trim().equals("")){
		if(finalMatch3End.trim().equals("true")){
	bocm.setFinalMatch3End(true);
		}else{
	bocm.setFinalMatch3End(false);
		}
		modify = true;
	}
	if(finalMatch4End != null && !finalMatch4End.trim().equals("")){
		if(finalMatch4End.trim().equals("true")){
	bocm.setFinalMatch4End(true);
		}else{
	bocm.setFinalMatch4End(false);
		}
		modify = true;
	}
	if(finalMatch5End != null && !finalMatch5End.trim().equals("")){
		if(finalMatch5End.trim().equals("true")){
	bocm.setFinalMatch5End(true);
		}else{
	bocm.setFinalMatch5End(false);
		}
		modify = true;
	}
	if(finalMatch6End != null && !finalMatch6End.trim().equals("")){
		if(finalMatch6End.trim().equals("true")){
	bocm.setFinalMatch6End(true);
		}else{
	bocm.setFinalMatch6End(false);
		}
		modify = true;
	}
	if(finalMatch2OneSideLeftPlayerCount != null && !finalMatch2OneSideLeftPlayerCount.trim().equals("")){
		try{
	int finalMatch2OneSideLeftPlayerCountValue = Integer.parseInt(finalMatch2OneSideLeftPlayerCount.trim());
	bocm.finalMatch2OneSideLeftPlayerCount = finalMatch2OneSideLeftPlayerCountValue;
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(finalMatch3OneSideLeftPlayerCount != null && !finalMatch3OneSideLeftPlayerCount.trim().equals("")){
		try{
	int finalMatch2OneSideLeftPlayerCountValue = Integer.parseInt(finalMatch3OneSideLeftPlayerCount.trim());
	bocm.finalMatch3OneSideLeftPlayerCount = finalMatch2OneSideLeftPlayerCountValue;
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(finalMatch4OneSideLeftPlayerCount != null && !finalMatch4OneSideLeftPlayerCount.trim().equals("")){
		try{
	int finalMatch2OneSideLeftPlayerCountValue = Integer.parseInt(finalMatch4OneSideLeftPlayerCount.trim());
	bocm.finalMatch4OneSideLeftPlayerCount = finalMatch2OneSideLeftPlayerCountValue;
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(finalMatch5OneSideLeftPlayerCount != null && !finalMatch5OneSideLeftPlayerCount.trim().equals("")){
		try{
	int finalMatch2OneSideLeftPlayerCountValue = Integer.parseInt(finalMatch5OneSideLeftPlayerCount.trim());
	bocm.finalMatch5OneSideLeftPlayerCount = finalMatch2OneSideLeftPlayerCountValue;
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(finalMatch6LeftPlayerCount != null && !finalMatch6LeftPlayerCount.trim().equals("")){
		try{
	int finalMatch2OneSideLeftPlayerCountValue = Integer.parseInt(finalMatch6LeftPlayerCount.trim());
	bocm.finalMatch6LeftPlayerCount = finalMatch2OneSideLeftPlayerCountValue;
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(qualifierBeginTime != null && !qualifierBeginTime.trim().equals("")){
		if(qualifierBeginTime.trim().length() == 12){
	try{
	int year = Integer.parseInt(qualifierBeginTime.trim().substring(0,4));
	int month = Integer.parseInt(qualifierBeginTime.trim().substring(4,6));
	int day = Integer.parseInt(qualifierBeginTime.trim().substring(6,8));
	int hour = Integer.parseInt(qualifierBeginTime.trim().substring(8,10));
	int minute = Integer.parseInt(qualifierBeginTime.trim().substring(10,12));
	calendar.clear();
	calendar.set(year,month-1,day,hour,minute);
	bocm.setQualifierBeginTime(calendar.getTimeInMillis());
	modify = true;
	}catch(Exception ex){
		ex.printStackTrace();
	}
		}
	}
	if(allBigGroupCounty != null && !allBigGroupCounty.trim().equals("")){
		try{
	int allBigGroupCountValue = Integer.parseInt(allBigGroupCounty.trim());
	bocm.setAllBigGroupCount(allBigGroupCountValue);
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(allBigGroupCount != null && !allBigGroupCount.trim().equals("")){
		try{
	int allBigGroupCountValue = Integer.parseInt(allBigGroupCount.trim());
	bocm.setAllBigGroupCount(allBigGroupCountValue);
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(bigGroupCountInEveryday != null && !bigGroupCountInEveryday.trim().equals("")){
		try{
	int allBigGroupCountValue = Integer.parseInt(bigGroupCountInEveryday.trim());
	bocm.setBigGroupCountInEveryday(allBigGroupCountValue);
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(final1BeginTime != null && !final1BeginTime.trim().equals("")){
		if(final1BeginTime.trim().length() == 12){
	try{
	int year = Integer.parseInt(final1BeginTime.trim().substring(0,4));
	int month = Integer.parseInt(final1BeginTime.trim().substring(4,6));
	int day = Integer.parseInt(final1BeginTime.trim().substring(6,8));
	int hour = Integer.parseInt(final1BeginTime.trim().substring(8,10));
	int minute = Integer.parseInt(final1BeginTime.trim().substring(10,12));
	calendar.clear();
	calendar.set(year,month-1,day,hour,minute);
	bocm.setFinal1BeginTime(calendar.getTimeInMillis());
	modify = true;
	}catch(Exception ex){
		ex.printStackTrace();
	}
		}
	}
	if(final2BeginTime != null && !final2BeginTime.trim().equals("")){
		if(final2BeginTime.trim().length() == 12){
	try{
	int year = Integer.parseInt(final2BeginTime.trim().substring(0,4));
	int month = Integer.parseInt(final2BeginTime.trim().substring(4,6));
	int day = Integer.parseInt(final2BeginTime.trim().substring(6,8));
	int hour = Integer.parseInt(final2BeginTime.trim().substring(8,10));
	int minute = Integer.parseInt(final2BeginTime.trim().substring(10,12));
	calendar.clear();
	calendar.set(year,month-1,day,hour,minute);
	bocm.setFinal2BeginTime(calendar.getTimeInMillis());
	modify = true;
	}catch(Exception ex){
		ex.printStackTrace();
	}
		}
	}
	if(final3BeginTime != null && !final3BeginTime.trim().equals("")){
		if(final3BeginTime.trim().length() == 12){
	try{
	int year = Integer.parseInt(final3BeginTime.trim().substring(0,4));
	int month = Integer.parseInt(final3BeginTime.trim().substring(4,6));
	int day = Integer.parseInt(final3BeginTime.trim().substring(6,8));
	int hour = Integer.parseInt(final3BeginTime.trim().substring(8,10));
	int minute = Integer.parseInt(final3BeginTime.trim().substring(10,12));
	calendar.clear();
	calendar.set(year,month-1,day,hour,minute);
	bocm.setFinal3BeginTime(calendar.getTimeInMillis());
	modify = true;
	}catch(Exception ex){
		ex.printStackTrace();
	}
		}
	}
	if(final4BeginTime != null && !final4BeginTime.trim().equals("")){
		if(final4BeginTime.trim().length() == 12){
	try{
	int year = Integer.parseInt(final4BeginTime.trim().substring(0,4));
	int month = Integer.parseInt(final4BeginTime.trim().substring(4,6));
	int day = Integer.parseInt(final4BeginTime.trim().substring(6,8));
	int hour = Integer.parseInt(final4BeginTime.trim().substring(8,10));
	int minute = Integer.parseInt(final4BeginTime.trim().substring(10,12));
	calendar.clear();
	calendar.set(year,month-1,day,hour,minute);
	bocm.setFinal4BeginTime(calendar.getTimeInMillis());
	modify = true;
	}catch(Exception ex){
		ex.printStackTrace();
	}
		}
	}
	if(final5BeginTime != null && !final5BeginTime.trim().equals("")){
		if(final5BeginTime.trim().length() == 12){
	try{
	int year = Integer.parseInt(final5BeginTime.trim().substring(0,4));
	int month = Integer.parseInt(final5BeginTime.trim().substring(4,6));
	int day = Integer.parseInt(final5BeginTime.trim().substring(6,8));
	int hour = Integer.parseInt(final5BeginTime.trim().substring(8,10));
	int minute = Integer.parseInt(final5BeginTime.trim().substring(10,12));
	calendar.clear();
	calendar.set(year,month-1,day,hour,minute);
	bocm.setFinal5BeginTime(calendar.getTimeInMillis());
	modify = true;
	}catch(Exception ex){
		ex.printStackTrace();
	}
		}
	}
	if(final6BeginTime != null && !final6BeginTime.trim().equals("")){
		if(final6BeginTime.trim().length() == 12){
	try{
	int year = Integer.parseInt(final6BeginTime.trim().substring(0,4));
	int month = Integer.parseInt(final6BeginTime.trim().substring(4,6));
	int day = Integer.parseInt(final6BeginTime.trim().substring(6,8));
	int hour = Integer.parseInt(final6BeginTime.trim().substring(8,10));
	int minute = Integer.parseInt(final6BeginTime.trim().substring(10,12));
	calendar.clear();
	calendar.set(year,month-1,day,hour,minute);
	bocm.setFinal6BeginTime(calendar.getTimeInMillis());
	modify = true;
	}catch(Exception ex){
		ex.printStackTrace();
	}
		}
	}
	if(signUpNumber != null && !signUpNumber.trim().equals("")){
		try{
	int signUpNumberValue = Integer.parseInt(signUpNumber.trim());
	bocm.setSignUpNumber(signUpNumberValue);
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(signUpMinLevel != null && !signUpMinLevel.trim().equals("")){
		try{
	int signUpMinLevelValue = Integer.parseInt(signUpMinLevel.trim());
	bocm.setSignUpMinLevel(signUpMinLevelValue);
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}

	if(NEXT_MATCH_START_TIME_LENGTH != null && !NEXT_MATCH_START_TIME_LENGTH.trim().equals("")){
		try{
	long NEXT_MATCH_START_TIME_LENGTHValue = Long.parseLong(NEXT_MATCH_START_TIME_LENGTH.trim());
	BattleOrganizingCommitteeManager.NEXT_MATCH_START_TIME_LENGTH = NEXT_MATCH_START_TIME_LENGTHValue;
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(START_REST_TIME_LENGTH != null && !START_REST_TIME_LENGTH.trim().equals("")){
		try{
	long START_REST_TIME_LENGTHValue = Long.parseLong(START_REST_TIME_LENGTH.trim());
	BattleOrganizingCommitteeManager.START_REST_TIME_LENGTH = START_REST_TIME_LENGTHValue;
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(NEXT_FINAL_MATCH_START_TIME_LENGTH != null && !NEXT_FINAL_MATCH_START_TIME_LENGTH.trim().equals("")){
		try{
	long NEXT_FINAL_MATCH_START_TIME_LENGTHValue = Long.parseLong(NEXT_FINAL_MATCH_START_TIME_LENGTH.trim());
	BattleOrganizingCommitteeManager.NEXT_FINAL_MATCH_START_TIME_LENGTH = NEXT_FINAL_MATCH_START_TIME_LENGTHValue;
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(START_FINAL_REST_TIME_LENGTH != null && !START_FINAL_REST_TIME_LENGTH.trim().equals("")){
		try{
	long START_FINAL_REST_TIME_LENGTHValue = Long.parseLong(START_FINAL_REST_TIME_LENGTH.trim());
	BattleOrganizingCommitteeManager.START_FINAL_REST_TIME_LENGTH = START_FINAL_REST_TIME_LENGTHValue;
	modify = true;
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
	if(isAllowWatcherGoInTo != null && !isAllowWatcherGoInTo.trim().equals("")){
		if(isAllowWatcherGoInTo.trim().equals("true")){
	BattleOrganizingCommitteeManager.isAllowWatcherGoInTo = true;
		}else{
	BattleOrganizingCommitteeManager.isAllowWatcherGoInTo = false;
		}
	}
	if(watcherNumMultiple != null && !watcherNumMultiple.trim().equals("")){
		try{
		int money = Integer.parseInt(watcherNumMultiple.trim());
		BattleOrganizingCommitteeManager.watcherNumMultiple = money;
		}catch(Exception ex){
	ex.printStackTrace();
		}
		
	}
	if(modify){
		bocm.saveMatchData();
		out.println("修改已经保存");
	}
	}
	out.println("<form name='f1'>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("数据");
	out.println("</td><td>修改值</td></tr>");
	out.println("<tr><td>running:"+bocm.running+"</td><td><input name='running' id='running'></td></tr>");
	out.println("<tr colspan='2'>日期修改需要遵循格式201210101010</tr>");
	out.println("<tr><td>报名物品名:"+BattleOrganizingCommitteeManager.needArticleName+"</td><td><input name='needArticleName' id='needArticleName'></td></tr>");
	out.println("<tr><td>观看需要金钱:"+bocm.watchNeedMoney+"</td><td><input name='watchNeedMoney' id='watchNeedMoney'></td></tr>");
	out.println("<tr><td>能够观看:"+BattleOrganizingCommitteeManager.isAllowWatcherGoInTo+"</td><td><input name='isAllowWatcherGoInTo' id='isAllowWatcherGoInTo'></td></tr>");
	out.println("<tr><td>观看人数系数:"+BattleOrganizingCommitteeManager.watcherNumMultiple+"</td><td><input name='watcherNumMultiple' id='watcherNumMultiple'></td></tr>");
	out.println("<tr><td>报名号:"+bocm.getSignUpNumber()+"</td><td><input name='signUpNumber' id='signUpNumber'></td></tr>");
	out.println("<tr><td>最小报名级别:"+bocm.getSignUpMinLevel()+"</td><td><input name='signUpMinLevel' id='signUpMinLevel'></td></tr>");
	out.println("<tr><td>共分多少大组:"+bocm.getAllBigGroupCount()+"</td><td><input name='allBigGroupCount' id='allBigGroupCount'></td></tr>");
	out.println("<tr><td>每天比赛几组:"+bocm.getBigGroupCountInEveryday()+"</td><td><input name='bigGroupCountInEveryday' id='bigGroupCountInEveryday'></td></tr>");
	out.println("<tr><td>&nbsp;</td><td></td></tr>");
	out.println("<tr><td>&nbsp;</td><td></td></tr>");
	out.println("<tr><td>预赛每轮时长:"+BattleOrganizingCommitteeManager.NEXT_MATCH_START_TIME_LENGTH+"毫秒</td><td><input name='NEXT_MATCH_START_TIME_LENGTH' id='NEXT_MATCH_START_TIME_LENGTH'></td></tr>");
	out.println("<tr><td>预赛中途休息时间点:"+BattleOrganizingCommitteeManager.START_REST_TIME_LENGTH+"毫秒</td><td><input name='START_REST_TIME_LENGTH' id='START_REST_TIME_LENGTH'></td></tr>");
	out.println("<tr><td>决赛每轮时长:"+BattleOrganizingCommitteeManager.NEXT_FINAL_MATCH_START_TIME_LENGTH+"毫秒</td><td><input name='NEXT_FINAL_MATCH_START_TIME_LENGTH' id='NEXT_FINAL_MATCH_START_TIME_LENGTH'></td></tr>");
	out.println("<tr><td>决赛中途休息时间点:"+BattleOrganizingCommitteeManager.START_FINAL_REST_TIME_LENGTH+"毫秒</td><td><input name='START_FINAL_REST_TIME_LENGTH' id='START_FINAL_REST_TIME_LENGTH'></td></tr>");
	out.println("<tr><td>&nbsp;</td><td></td></tr>");
	out.println("<tr><td>&nbsp;</td><td></td></tr>");
	out.println("<tr><td>报名开始日期:"+DateUtil.formatDate(new Date(bocm.getSignUpdate()),"yyyy年MM月dd日HH时mm分")+"</td><td><input name='signUpdate' id='signUpdate'>格式201210101010</td></tr>");
	out.println("<tr><td>报名结束日期:"+DateUtil.formatDate(new Date(bocm.getEndSignUpdate()),"yyyy年MM月dd日HH时mm分")+"</td><td><input name='endSignUpdate' id='endSignUpdate'>格式201210101010</td></tr>");
	out.println("<tr><td>&nbsp;</td><td></td></tr>");
	out.println("<tr><td>&nbsp;</td><td></td></tr>");
	out.println("<tr><td>预赛开始日期:"+DateUtil.formatDate(new Date(bocm.getQualifierBeginTime()),"yyyy年MM月dd日HH时mm分")+"</td><td><input name='qualifierBeginTime' id='qualifierBeginTime'>格式201210101010</td></tr>");
	out.println("<tr><td>&nbsp;</td><td></td></tr>");
	out.println("<tr><td>&nbsp;</td><td></td></tr>");
	out.println("<tr><td>决赛第一轮开始日期:"+DateUtil.formatDate(new Date(bocm.getFinal1BeginTime()),"yyyy年MM月dd日HH时mm分")+"</td><td><input name='final1BeginTime' id='final1BeginTime'>格式201210101010</td></tr>");
	out.println("<tr><td>决赛第二轮开始日期:"+DateUtil.formatDate(new Date(bocm.getFinal2BeginTime()),"yyyy年MM月dd日HH时mm分")+"</td><td><input name='final2BeginTime' id='final2BeginTime'>格式201210101010</td></tr>");
	out.println("<tr><td>决赛第三轮开始日期:"+DateUtil.formatDate(new Date(bocm.getFinal3BeginTime()),"yyyy年MM月dd日HH时mm分")+"</td><td><input name='final3BeginTime' id='final3BeginTime'>格式201210101010</td></tr>");
	out.println("<tr><td>决赛第四轮开始日期:"+DateUtil.formatDate(new Date(bocm.getFinal4BeginTime()),"yyyy年MM月dd日HH时mm分")+"</td><td><input name='final4BeginTime' id='final4BeginTime'>格式201210101010</td></tr>");
	out.println("<tr><td>决赛第五轮开始日期:"+DateUtil.formatDate(new Date(bocm.getFinal5BeginTime()),"yyyy年MM月dd日HH时mm分")+"</td><td><input name='final5BeginTime' id='final5BeginTime'>格式201210101010</td></tr>");
	out.println("<tr><td>决赛第六轮开始日期:"+DateUtil.formatDate(new Date(bocm.getFinal6BeginTime()),"yyyy年MM月dd日HH时mm分")+"</td><td><input name='final6BeginTime' id='final6BeginTime'>格式201210101010</td></tr>");
	out.println("<tr><td>&nbsp;</td><td></td></tr>");
	out.println("<tr><td>&nbsp;</td><td></td></tr>");
	out.println("<tr><td>makeGroupsFlag:"+bocm.isMakeGroupsFlag()+"</td><td><input name='makeGroupsFlag' id='makeGroupsFlag'></td></tr>");
	out.println("<tr><td>matchFlag:"+bocm.isMatchFlag()+"</td><td><input name='matchFlag' id='matchFlag'></td></tr>");
	out.println("<tr><td>预赛第一组是否结束:"+bocm.isQualifier1End()+"</td><td><input name='qualifier1End' id='qualifier1End'></td></tr>");
	out.println("<tr><td>预赛第二组是否结束:"+bocm.isQualifier2End()+"</td><td><input name='qualifier2End' id='qualifier2End'></td></tr>");
	out.println("<tr><td>预赛第三组是否结束:"+bocm.isQualifier3End()+"</td><td><input name='qualifier3End' id='qualifier3End'></td></tr>");
	out.println("<tr><td>预赛第四组是否结束:"+bocm.isQualifier4End()+"</td><td><input name='qualifier4End' id='qualifier4End'></td></tr>");
	out.println("<tr><td>决赛轮开始标记:"+bocm.isFinalMatchStart()+"</td><td><input name='finalMatchStart' id='finalMatchStart'></td></tr>");
	out.println("<tr><td>决赛第一轮结束标记:"+bocm.isFinalMatch1End()+"</td><td><input name='finalMatch1End' id='finalMatch1End'></td></tr>");
	out.println("<tr><td>决赛第二轮结束标记:"+bocm.isFinalMatch2End()+"</td><td><input name='finalMatch2End' id='finalMatch2End'></td></tr>");
	out.println("<tr><td>决赛第三轮结束标记:"+bocm.isFinalMatch3End()+"</td><td><input name='finalMatch3End' id='finalMatch3End'></td></tr>");
	out.println("<tr><td>决赛第四轮结束标记:"+bocm.isFinalMatch4End()+"</td><td><input name='finalMatch4End' id='finalMatch4End'></td></tr>");
	out.println("<tr><td>决赛第五轮结束标记:"+bocm.isFinalMatch5End()+"</td><td><input name='finalMatch5End' id='finalMatch5End'></td></tr>");
	out.println("<tr><td>决赛第六轮结束标记:"+bocm.isFinalMatch6End()+"</td><td><input name='finalMatch6End' id='finalMatch6End'></td></tr>");
	out.println("<tr><td>finalMatch2OneSideLeftPlayerCount:"+bocm.finalMatch2OneSideLeftPlayerCount+"</td><td><input name='finalMatch2OneSideLeftPlayerCount' id='finalMatch2OneSideLeftPlayerCount'></td></tr>");
	out.println("<tr><td>finalMatch3OneSideLeftPlayerCount:"+bocm.finalMatch3OneSideLeftPlayerCount+"</td><td><input name='finalMatch3OneSideLeftPlayerCount' id='finalMatch3OneSideLeftPlayerCount'></td></tr>");
	out.println("<tr><td>finalMatch4OneSideLeftPlayerCount:"+bocm.finalMatch4OneSideLeftPlayerCount+"</td><td><input name='finalMatch4OneSideLeftPlayerCount' id='finalMatch4OneSideLeftPlayerCount'></td></tr>");
	out.println("<tr><td>finalMatch5OneSideLeftPlayerCount:"+bocm.finalMatch5OneSideLeftPlayerCount+"</td><td><input name='finalMatch5OneSideLeftPlayerCount' id='finalMatch5OneSideLeftPlayerCount'></td></tr>");
	out.println("<tr><td>finalMatch6LeftPlayerCount:"+bocm.finalMatch6LeftPlayerCount+"</td><td><input name='finalMatch6LeftPlayerCount' id='finalMatch6LeftPlayerCount'></td></tr>");
	out.println("</table>");
	out.println("<input type='submit' value='提交'>");
	out.println("</form>");
	if(bocm != null){
		PlayerManager pm = PlayerManager.getInstance();
		List<MatchItem> matchItemList = bocm.getMatchItemList();
		if(matchItemList != null){
	out.println("<table>");
	String finalSecondRound = "";
	if(bocm.finalSecondRound){
		finalSecondRound = "败者组比赛";
	}else{
		finalSecondRound = "胜者组败者组比赛";
	}
	String battlePress = "预赛阶段 轮数"+(bocm.qualifier1Round);
	if(bocm.isQualifier1End()){
		battlePress = "预赛第二天 轮数"+(bocm.qualifier2Round);
	}
	if(bocm.isQualifier2End()){
		battlePress = "预赛第三天 轮数"+(bocm.qualifier3Round);
	}
	if(bocm.isQualifier3End()){
		battlePress = "预赛第四天 轮数"+(bocm.qualifier4Round);
	}
	if(bocm.isQualifier4End()){
		battlePress = "决赛阶段";
	}
	if(bocm.isFinalMatch1End()){
		battlePress = "决赛第二天 轮数"+finalSecondRound;
	}
	if(bocm.isFinalMatch2End()){
		battlePress = "决赛第三天 轮数"+finalSecondRound;
	}
	if(bocm.isFinalMatch3End()){
		battlePress = "决赛第四天 轮数"+finalSecondRound;
	}
	if(bocm.isFinalMatch4End()){
		battlePress = "决赛第五天 轮数"+finalSecondRound;
	}
	if(bocm.isFinalMatch5End()){
		battlePress = "决赛第六天 轮数"+finalSecondRound;
	}
	out.println("<tr><td colspan='7'>"+battlePress+"</td></tr>");
	out.println("<tr><td>A方人员</td><td>A失败次数</td><td>A决赛失败次数</td><td>B方人员</td><td>B失败次数</td><td>B决赛失败次数</td><td>比赛状态</td></tr>");
	for(MatchItem matchItem : matchItemList){
		if(matchItem != null && !matchItem.isMatchItemEnd()){
	long playerIdA = matchItem.getPbiA().getPlayerId();
	long playerIdB = matchItem.getPbiB().getPlayerId();
	Player playerA = null;
	Player playerB = null;
	try{
		playerA = pm.getPlayer(playerIdA);
	}catch(Exception ex){
	}
	try{
		playerB = pm.getPlayer(playerIdB);
	}catch(Exception ex){
	}
	out.println("<tr>");
	if(playerA != null){
		out.println("<td>"+"("+playerA.getLevel()+"级)"+playerA.getName()+"(报名号:"+matchItem.getPbiA().getSignUpNumber()+")</td><td>"+matchItem.getPbiA().getFailCount()+"</td><td>"+matchItem.getPbiA().getFinalMatchFailRound()+"</td>");
	}else{
		out.println("<td>(报名号:"+matchItem.getPbiA().getSignUpNumber()+")</td><td>"+matchItem.getPbiA().getFailCount()+"</td><td>"+matchItem.getPbiA().getFinalMatchFailRound()+"</td>");
	}
	if(playerB != null){
		out.println("<td>"+"("+playerB.getLevel()+"级)"+playerB.getName()+"(报名号:"+matchItem.getPbiB().getSignUpNumber()+")</td><td>"+matchItem.getPbiB().getFailCount()+"</td><td>"+matchItem.getPbiB().getFinalMatchFailRound()+"</td>");
	}else{
		out.println("<td>(报名号:"+matchItem.getPbiB().getSignUpNumber()+")</td><td>"+matchItem.getPbiB().getFailCount()+"</td><td>"+matchItem.getPbiB().getFinalMatchFailRound()+"</td>");
	}
	out.println("<td>正在进行比赛</td></tr>");
		}else if(matchItem != null){
	long playerIdA = matchItem.getPbiA().getPlayerId();
	long playerIdB = matchItem.getPbiB().getPlayerId();
	Player playerA = null;
	Player playerB = null;
	try{
		playerA = pm.getPlayer(playerIdA);
		playerB = pm.getPlayer(playerIdB);
	}catch(Exception ex){
	}
	out.println("<tr>");
	if(playerA != null){
		out.println("<td>"+"("+playerA.getLevel()+"级)"+playerA.getName()+"(报名号:"+matchItem.getPbiA().getSignUpNumber()+")</td><td>"+matchItem.getPbiA().getFailCount()+"</td><td>"+matchItem.getPbiA().getFinalMatchFailRound()+"</td>");
	}else{
		out.println("<td>(报名号:"+matchItem.getPbiA().getSignUpNumber()+")</td><td>"+matchItem.getPbiA().getFailCount()+"</td><td>"+matchItem.getPbiA().getFinalMatchFailRound()+"</td>");
	}
	if(playerB != null){
		out.println("<td>"+"("+playerB.getLevel()+"级)"+playerB.getName()+"(报名号:"+matchItem.getPbiB().getSignUpNumber()+")</td><td>"+matchItem.getPbiB().getFailCount()+"</td><td>"+matchItem.getPbiB().getFinalMatchFailRound()+"</td>");
	}else{
		out.println("<td>(报名号:"+matchItem.getPbiB().getSignUpNumber()+")</td><td>"+matchItem.getPbiB().getFailCount()+"</td><td>"+matchItem.getPbiB().getFinalMatchFailRound()+"</td>");
	}
	out.println("<td>比赛已经结束</td></tr>");
		}
	}
	out.println("</table>");
		}
		out.println("<br>");
		out.println("<br>");
		out.println("<br>");
		MaimType[] groups = bocm.getGroups();
		if(groups != null){
	out.println("<table>");
	out.println("<tr><td colspan='7'>现在还存在的人</td></tr>");
	out.println("<tr><td>报名人账户</td><td>报名人</td><td>报名人id</td><td>报名号</td><td>所在组</td><td>小败次数</td><td>大败次数</td></tr>");
	for(MaimType group : groups){
		if(group != null && group.getPbiList() != null){
	for(PlayerBattleItem pbi : group.getPbiList()){
		if(pbi != null){
			Player player = null;
			try{
				player = pm.getPlayer(pbi.getPlayerId());
			}catch(Exception ex){
			}
			String name = "";
			String userName = "";
			if(player != null){
				userName = player.getUsername();
				name = player.getName();
			}
			out.println("<tr><td>"+userName+"</td><td>"+name+"</td><td>"+pbi.getPlayerId()+"</td><td>"+pbi.getSignUpNumber()+"</td><td>"+group.getGroupId()+"</td><td>"+pbi.getFailCount()+"</td><td>"+pbi.getFinalMatchFailRound()+"</td></tr>");
			}
		}
	}
	}
	out.println("</table>");
		}
		out.println("<br>");
		out.println("<br>");
		out.println("<br>");
		Map<Long,Integer> signUpMap = bocm.getSignUpPlayerMap();
		if(signUpMap != null){
	out.println("<table>");
	out.println("<tr><td colspan='5'>报名人</td></tr>");
	out.println("<tr><td>报名人账户</td><td>报名人</td><td>报名人id</td><td>报名号</td><td>级别</td></tr>");
	Set<Long> keySet = signUpMap.keySet();
	if(keySet != null){
		for(Long i : keySet){
	Player player = null;
	try{
		player = pm.getPlayer(i);
	}catch(Exception ex){
	}
	String name = "";
	String userName = "";
	int level = 0;
	if(player != null){
		userName = player.getUsername();
		name = player.getName();
		level = player.getLevel();
	}
	out.println("<tr><td>"+userName+"</td><td>"+name+"</td><td>"+i+"</td><td>"+signUpMap.get(i)+"</td><td>"+level+"</td></tr>");
		}
	}
	out.println("</table>");
		}
		long[][] final32Players = BattleOrganizingCommitteeManager.final32Players;
		StringBuffer sbb = new StringBuffer();
		if(final32Players != null){
	for(int i = 0; i < final32Players.length; i++){
		long[] a = final32Players[i];
		if(a == null){
	a = new long[2];
		}
		sbb.append("("+a[0]+"号");
		try{
	Player p = pm.getPlayer(a[1]);
	sbb.append(p.getName()+") ");
		}catch(Exception ex){
	ex.printStackTrace();
		}
	}
		}
		out.println("32强(只有产生32强后才有意义):"+sbb.toString());
		out.println("冠军牌号:"+BattleOrganizingCommitteeManager.WinnerHaoPai);
	}
%>
</head>
<body>

</body>
</html>
