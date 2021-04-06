<%@page import="java.text.SimpleDateFormat"%><%@page import="java.util.*"%><%@page import="com.fy.boss.gm.newfeedback.GmTalk"%><%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	String id = request.getParameter("feedbackid");
	if (id!=null) {
		NewFeedbackQueueManager fq = NewFeedbackQueueManager.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		GmTalk[] list = fq.getGmTalkById(Long.parseLong(id));
		StringBuffer sb = new StringBuffer();
		for(GmTalk talk:list){
			if(talk.getFeedbackid()==Long.parseLong(id)){
				if(talk.getName()!=null&&talk.getSendDate()>0&&talk.getTalkcontent()!=null){
					if(!talk.getName().contains("GM")){
						sb.append(talk.getId()+"@@##$$<input type='hidden' id='"+talk.getId()+"' value='"+talk.getId()+"'><font color = 'blue'>"+talk.getName()+"(玩家)&nbsp;&nbsp;"+format.format(new Date(talk.getSendDate()))+"</font><br/>");
						sb.append("&nbsp;&nbsp;&nbsp;"+talk.getTalkcontent()+"<br/>@#$%^");
					}else{
						sb.append(talk.getId()+"@@##$$<input type='hidden' id='"+talk.getId()+"' value='"+talk.getId()+"'><font color = 'red'>"+talk.getName()+"(GM)&nbsp;&nbsp;"+format.format(new Date(talk.getSendDate()))+"</font><br/>");
						sb.append("&nbsp;&nbsp;&nbsp;"+talk.getTalkcontent()+"<br/>@#$%^");							
					}
					out.print(sb.toString());
				}
			}
		}
	}
%>