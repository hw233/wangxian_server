<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager"%>
<%@page import="com.fy.boss.gm.newfeedback.FeedbackState"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String gmname = request.getParameter("gmnum");
String id = request.getParameter("id");
NewFeedbackQueueManager fbb = NewFeedbackQueueManager.getInstance();
fbb.removeForJsp(Long.parseLong(id));
fbb.closeNewFeedback(gmname, id);
//
NewFeedbackStateManager statemanager = NewFeedbackStateManager.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String recordid = sdf.format(new Date());
if(gmname!=null && NewFeedbackStateManager.getInstance().isaddNewState(recordid, gmname)){
	FeedbackState stat = new FeedbackState();
	stat.setCloseFeedbackTime(1);
	stat.setStateid(recordid);
	stat.setGmnum(gmname);
	stat.setFbid(Long.parseLong(id));
	statemanager.addNewState(stat);
}else{
	List<FeedbackState> states = statemanager.getStates();
	for(FeedbackState pp:states){
		if(recordid.equals(pp.getStateid()) && gmname.equals(pp.getGmnum())){
			pp.setCloseFeedbackTime(pp.getCloseFeedbackTime()+1);
		}
	}
}
//
%>