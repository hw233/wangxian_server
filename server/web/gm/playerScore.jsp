<%@page import="com.fy.engineserver.message.FEEDBACK_LOOK_SCORE_RES"%>
<%@page import="com.fy.engineserver.gm.newfeedback.NewFeedbackManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.boss.gm.newfeedback.GmTalk"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%>

	<%
		//GM关闭玩家反馈
		long now = System.currentTimeMillis();
		String id = ParamUtils.getParameter(request, "fbid", "");
		String mess = ParamUtils.getParameter(request, "mess", "");
		String pname = ParamUtils.getParameter(request, "pname", "");
		try{
			if(id!=null && mess!=null &&pname!=null){
				Player player = PlayerManager.getInstance().getPlayer(pname);
				if(player!=null){
					if(player.isOnline()){
						FEEDBACK_LOOK_SCORE_RES res = new FEEDBACK_LOOK_SCORE_RES(GameMessageFactory.nextSequnceNum(),Long.parseLong(id),mess);
						player.addMessageToRightBag(res);
						NewFeedbackManager.logger.warn("[强制评分] [成功] [palyerusername:"+player.getUsername()+"] [mess:"+mess+"] [playervip:"+player.getVipLevel()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
					}else{
						out.print("玩家不在线");
					}	
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			NewFeedbackManager.logger.warn("[强制评分] [异常] [id："+id+"] [mess："+mess+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
	%>