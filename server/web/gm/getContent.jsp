<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.engineserver.gm.feedback.FeedbackForJsio"%>
<%@page import="java.text.SimpleDateFormat"%><%@page import="java.util.Date"%><%@page import="com.fy.engineserver.sprite.PlayerManager"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.gm.feedback.Reply"%><%@page import="com.fy.engineserver.gm.feedback.Feedback"%><%@page import="java.util.List"%><%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String id = request.getParameter("fbId");
// 	out.print("in getserver....gmname:"+gmname);
// 	System.out.print("gmname:"+gmname);
	if(id!=null && id.trim().length()>0){
		Feedback fb = FeedbackManager.getInstance().getFeedBack(Long.parseLong(id));
		if(fb!=null){
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
				if(fb.getUserName()!=null&&fb.getServername()!=null){
					List<Reply> replys = fb.getList();
					if(replys.size()>0){
						try{
							if(fb.getSubject().trim().equals("")){
								fb.setSubject("空标题");
							}
							for(Reply r: replys){
								if(r.getGmName().equals("")){
									sb.append("<font color = 'blue'>"+fb.getUserName()+"(玩家)&nbsp;&nbsp;"+format.format(new Date(r.getSendDate()))+"</font><br/>");
									sb.append("&nbsp;&nbsp;&nbsp;"+r.getFcontent()+"<br/>");
								}else{
									sb.append("<font color = 'red'>"+r.getGmName()+"(GM)&nbsp;&nbsp;"+format.format(new Date(r.getSendDate()))+"</font><br/>");
									sb.append("&nbsp;&nbsp;&nbsp;"+r.getFcontent()+"<br/>");							
								}
							}
							fb.setDialogs(sb.toString());
							FeedbackForJsio fbjs = new FeedbackForJsio(fb);
							String ss = JsonUtil.jsonFromObject(fbjs);
							out.print(ss+"#$&*^");
						}catch(Exception e){
							e.printStackTrace();
							System.out.print("异常："+e);
						}
					}
					
				}
		}else{
			out.print("没有记录！");
			System.out.print("没有记录！！");
		}
	}

%>
		
