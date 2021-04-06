<%@page import="java.util.*"%><%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	String id = request.getParameter("feedbackid");
	if (id!=null) {
		NewFeedbackQueueManager fq = NewFeedbackQueueManager.getInstance();
		fq.setRecordid(Long.parseLong(id));
	}
%>