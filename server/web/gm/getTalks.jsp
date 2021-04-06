<%@page import="com.fy.engineserver.message.FEEDBACK_NOTICE_CLIENT_RES"%>
<%@page import="com.fy.engineserver.gm.newfeedback.NewFeedbackManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.boss.gm.newfeedback.GmTalk"%>
<%@page import="com.fy.engineserver.message.FEEDBACK_GM_TALK_RES"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%>

	<%
		//GM关闭玩家反馈
		long now = System.currentTimeMillis();
		String id = ParamUtils.getParameter(request, "feedbackid", "");
		String gmnum = ParamUtils.getParameter(request, "gmnum", "");
		String talkmess = ParamUtils.getParameter(request, "talkmess", "");
		String playername = ParamUtils.getParameter(request, "playername", "");
		String version = request.getParameter("version");
		if("new".equals(request.getParameter("version"))){
            talkmess = java.net.URLDecoder.decode(request.getParameter("talkmess"),"utf-8");
            playername=java.net.URLDecoder.decode(request.getParameter("playername"),"utf-8");
    	}
		
		boolean issucc = false;
		try{
			if(id!=null && gmnum!=null && talkmess!=null && playername!=null){
				Player player = PlayerManager.getInstance().getPlayer(playername);
				if(player!=null){
					if(player.isOnline()){
						GmTalk talk = new GmTalk();
						talk.setFeedbackid(Long.parseLong(id));
						talk.setName(gmnum);
						talk.setTalkcontent(talkmess);
						talk.setSendDate(System.currentTimeMillis());
						FEEDBACK_GM_TALK_RES res = new FEEDBACK_GM_TALK_RES(GameMessageFactory.nextSequnceNum(),Long.parseLong(id),talk);
						player.addMessageToRightBag(res);
						FEEDBACK_NOTICE_CLIENT_RES not_res = new FEEDBACK_NOTICE_CLIENT_RES(GameMessageFactory.nextSequnceNum(),true);
						player.addMessageToRightBag(not_res);
						NewFeedbackManager.logger.warn("[GM回复玩家一个对话] [-------3] [palyerusername:"+player.getUsername()+"] [playervip:"+player.getVipLevel()+"]");
						issucc = true;
					}else{
						out.print("玩家不在线");
					}	
				}
			}
		
		}catch(Exception e){
			e.printStackTrace();
			NewFeedbackManager.logger.warn("[GM回复玩家一个对话] [异常] [GM编号："+gmnum+"] [玩家："+playername+"] [反馈id"+id+"] [内容："+talkmess+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
	%>