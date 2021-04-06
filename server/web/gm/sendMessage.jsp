<%@page import="com.fy.engineserver.gm.feedback.Reply"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%>
<%@page import="com.fy.engineserver.gm.feedback.Feedback"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%>

	<%
		//GM回复玩家反馈
		String gmreply = ParamUtils.getParameter(request, "gmreply", "");
		String gmid = ParamUtils.getParameter(request, "gmid", "");
		String sendFeedbackId = ParamUtils.getParameter(request, "sendFeedbackId", "");
		FeedbackManager fbm = FeedbackManager.getInstance();
		String issucc = fbm.GmAnserFeedBack(gmid, Long.parseLong(sendFeedbackId), gmreply);
		if("yes".equals(issucc)){
			FeedbackManager.logger.warn("[GM恢复反馈] [成功] [gmid:"+gmid+"] [内容："+gmreply+"] [反馈id:"+sendFeedbackId+"] [in sendmessage......]");
		}else{
			FeedbackManager.logger.warn("[GM恢复反馈] [失败] [gmid:"+gmid+"] [内容："+gmreply+"] [反馈id:"+sendFeedbackId+"] [in sendmessage......]");
		}
	%>