<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.engineserver.gm.feedback.FeedbackForJsio"%>
<%@page import="java.text.SimpleDateFormat"%><%@page import="java.util.Date"%><%@page import="com.fy.engineserver.sprite.PlayerManager"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.gm.feedback.Reply"%><%@page import="com.fy.engineserver.gm.feedback.Feedback"%><%@page import="java.util.List"%><%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String gmname = request.getParameter("gmname");
	String begintime = request.getParameter("begintime");
	String endtime = request.getParameter("endtime");
// 	out.print("in getserver....gmname:"+gmname);
// 	System.out.print("gmname:"+gmname);
	if(gmname!=null){
		List<Feedback> list = FeedbackManager.getInstance().getFeedbacksByGameId(gmname,begintime,endtime);
		if(list.size()>0){
			for(int i = 0; i < list.size(); i++){
				Feedback fb = list.get(i);
				if(fb.getUserName()!=null&&fb.getServername()!=null&&fb.getFid()!=null){
					try{
						if(fb.getSubject().trim().equals("")){
							fb.setSubject("空标题");
						}
						fb.setDialogs("");
						FeedbackForJsio fbjs = new FeedbackForJsio(fb);
						String ss = JsonUtil.jsonFromObject(fbjs);
						out.print(ss+"#$&*^");
					}catch(Exception e){
						e.printStackTrace();
						System.out.print("in getServer.jsp..异常："+e);
					}
				}
				
			}
		}
	}

%>
		
