<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager"%>
<%@page import="com.fy.boss.gm.newfeedback.FeedbackState"%>
<%@page import="com.fy.boss.gm.newfeedback.NewFeedback"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="gm/css/style.css"/>
<title>挂起</title>
</head>

<script language="javascript"> 
	function back(vip){
		if(vip>=5){
	 		window.location.replace("NewFeedback_handle.jsp");
		}else{
			window.location.replace("NewFeedback_handle2.jsp");
		}

	}
</script>
<body bgcolor="#c8edcc">
	<% 
		Object obj = session.getAttribute("authorize.username");
		String handle = request.getParameter("handle");
		String fbid = request.getParameter("fbid");
		String chakan = request.getParameter("chakan");
		long viplevel = 0;
	 	if(obj!=null&&handle!=null){
	 		NewFeedbackQueueManager fbman = NewFeedbackQueueManager.getInstance();
			if(fbid!=null&&chakan==null&&Long.parseLong(fbid)>0){
		 		try{
			 		NewFeedback fb = fbman.getNewFeedbackById(Long.parseLong(fbid));
			 		if(fb!=null){
			 			fb.setGmstat(2);
			 			fb.setGmHandler(handle);
			 			fb.setIsHandleNow(0);
			 			fb.setIsInQueue(0);
			 			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "isInQueue");
			 			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "isHandleNow");
			 			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "gmHandler");
			 			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "gmstat");
			 			FeedbackState state = new FeedbackState();
			 			state.setFbid(Long.parseLong(fbid));
			 			state.setGuaqiTime(System.currentTimeMillis());
			 			state.setGmnum(handle);
			 			
			 			NewFeedbackStateManager statemanager = NewFeedbackStateManager.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String recordid = sdf.format(new Date());
						if(handle!=null && NewFeedbackStateManager.getInstance().isaddNewState(recordid, handle)){
							FeedbackState stat = new FeedbackState();
							stat.setGetFeedbackTime(1);
							stat.setFbid(Long.parseLong(fbid));
							stat.setGuaqiTime(1);
							stat.setStateid(recordid);
							stat.setGmnum(handle);
							statemanager.addNewState(stat);
						}else{
							List<FeedbackState> states = statemanager.getStates();
							for(FeedbackState pp:states){
								if(recordid.equals(pp.getStateid()) && handle.equals(pp.getGmnum())){
									pp.setGuaqiTime(pp.getGuaqiTime()+1);
								}
							}
						}
						fbman.removeQueueIndex(fb);
			 		}
		 		}catch(Exception e){
		 			e.printStackTrace();
		 			out.print("挂起异常："+e);
		 		}	
		 	
			}	
	 	}else{
			out.print("<h1>挂起出错，请找相关人员处理！</h1><hr>");
		}
	%>

</body>
</html>

