<%@page import="com.fy.engineserver.gm.feedback.FeedbackForJsio"%>
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
		Calendar cl = Calendar.getInstance();
		cl.clear();
		cl.setTimeInMillis(System.currentTimeMillis());
		int min = cl.get(Calendar.MINUTE);
		String[] states = {"0","2"};
		String[] types = {"0","1","2","3","4"};
		String replydate = ParamUtils.getParameter(request, "replydate", "");
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		String endTime = sdf.format(SystemTime.currentTimeMillis() + 24*60*60*1000);
		List<Feedback> list = new ArrayList<Feedback>();
		List<Feedback> list3 = FeedbackManager.getInstance().getFeedbacks();
		String cb = ParamUtils.getParameter(request, "cb", "");
		PlayerManager pm = PlayerManager.getInstance();
		if(replydate!=null){
			for(Feedback fb:list3){
				if(fb.getSendTimes()>0){
					if(Long.parseLong(replydate)<fb.getSendTimes()){
						list.add(fb);
					}
				}
			}
		}
		if(list!=null&&list.size()>0){
			for(Feedback fb:list){
					
					long time = fb.getSendTimes();
					SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
					String st = format1.format(new Date(time));
					fb.setSendTimestr(st);
					StringBuffer sb = new StringBuffer();
					List<Reply> replys = fb.getList();
					long playerId = fb.getPlayerId();
					try{
						Player p = pm.getPlayer(playerId);
						if(fb.getSubject().trim().equals("")){
							fb.setSubject("空标题");
						}
						for(Reply r: replys){
							if(r.getGmName().equals("")){
								sb.append("<font color = 'blue'>"+p.getName()+"(玩家)&nbsp;&nbsp;"+format1.format(new Date(r.getSendDate()))+"</font><br/>");
								sb.append("&nbsp;&nbsp;&nbsp;"+r.getFcontent()+"<br/>");
							}else{
								sb.append("<font color = 'red'>"+r.getGmName()+"(GM)&nbsp;&nbsp;"+format1.format(new Date(r.getSendDate()))+"</font><br/>");
								sb.append("&nbsp;&nbsp;&nbsp;"+r.getFcontent()+"<br/>");							
							}
						}
						fb.setDialogs(sb.toString());
						long idd = fb.getId();
						String sid = idd+"";
						fb.setFid(sid);
						FeedbackForJsio fbjs = new FeedbackForJsio(fb);
						String ss = JsonUtil.jsonFromObject(fbjs);
						if(!StringUtils.isEmpty(ss)){
// 							Game.logger.info("[反馈test:] [replydate:"+replydate+"] [sendtime:"+fb.getSendTimes()+"] [channel:"+fb.getChannel()+"] [title:"+fb.getSubject()+"][replysize:"+replys.size()+"] [id:"+fb.getId()+"] [pid:"+p.getId()+"] [name:"+p.getName()+"]  [title:"+fb.getSubject()+"] [ss:"+ss+"]");
					
					
						
			%>
						<%=cb%>(<%=ss%>);
			<%
					}
					
					}catch(Exception e){
						e.printStackTrace();
						Game.logger.info("[反馈test:] [玩家不存在] [id:"+playerId+"]");
						continue;
					}
				}
			}
			
	%>
		
	
		
