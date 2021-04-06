<%@page import="com.fy.boss.gm.newfeedback.GmTalk"%><%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%><%@page import="java.util.Arrays"%><%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%><%@page import="java.util.HashMap"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	String url = request.getParameter("url");
	String content = request.getParameter("content");
	String playername = request.getParameter("playername");
	String gmnum = request.getParameter("gmnum");
	String id = request.getParameter("id");
	HashMap headers = new HashMap();
	if (url!=null) {
	String contentP = "talkmess="+content+"&playername="+playername+"&gmnum="+gmnum+"&feedbackid="+id+"&authorize.username=lvfei&authorize.password=lvfei321";
	byte[] b = HttpUtils.webPost(new URL(url), contentP.getBytes(), headers, 60000, 60000);
	if(b!=null){
		NewFeedbackQueueManager fq = NewFeedbackQueueManager.getInstance();
		GmTalk talk = new GmTalk();
		talk.setFeedbackid(Long.parseLong(id));
		talk.setName(gmnum);
		talk.setTalkcontent(content);
		talk.setSendDate(System.currentTimeMillis());
		if(fq.addGmTalk(talk)){
			if(fq.getRecordIds().contains(Long.parseLong(id))){
				fq.getTalks().add(talk);
			}
			out.print("yes");
		}
	}
	}
%>