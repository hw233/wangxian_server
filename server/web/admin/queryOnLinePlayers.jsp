<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	//账号，角色，id，vip等级，玩家等级, 客户端id，机型,登陆ip
	String descType = request.getParameter("descType");
	Player [] ps = PlayerManager.getInstance().getOnlinePlayers();
	String 列分割符 = "@@@@";
	String 行分割符 = "##@##";
	if(descType == null || descType.isEmpty()){
		descType = "0";
	}
	Game.logger.warn("[玩家在线测试] [descType:"+descType+"] [ps:"+(ps==null?"null":ps.length)+"]");
	if(ps != null && ps.length > 0){
		if(descType.equals("1")){
			Arrays.sort(ps, new Comparator<Player>(){
				@Override
				public int compare(Player o1, Player o2) {
					return Integer.valueOf(o2.getLevel()) - Integer.valueOf(o1.getLevel());
				}
			});
		}else if(descType.equals("2")){
			Arrays.sort(ps, new Comparator<Player>(){
				@Override
				public int compare(Player o1, Player o2) {
					return Integer.valueOf(o2.getVipLevel()) - Integer.valueOf(o1.getVipLevel());
				}
			});
		}
		
		StringBuffer sb = new StringBuffer();
		Passport passport = null;
		for(Player p : ps){
			passport = p.getPassport();
			sb.append(p.getUsername()).append(列分割符).append(p.getName()).append(列分割符).append(p.getId()).append(列分割符).append(p.getVipLevel()).append(列分割符).append(p.getLevel());
			if(passport != null){
				if(!passport.getLastLoginClientId().isEmpty()){
					sb.append(列分割符).append(passport.getLastLoginClientId());
				}
				if(!passport.getLastLoginMobileType().isEmpty()){
					sb.append(列分割符).append(passport.getLastLoginMobileType());
				}
				if(!passport.getLastLoginIp().isEmpty()){
					sb.append(列分割符).append(passport.getLastLoginIp());
				}
			}
			sb.append(行分割符);
		}
		out.print(sb.toString());
	}else{
		out.print("无在线玩家.");
	}
%>
