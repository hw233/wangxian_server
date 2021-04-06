<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.smith.*"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
	String username = request.getParameter("username");	
	String servername = request.getParameter("serverid");	
	if(username!=null&&servername!=null){
		try{
			if(GameConstants.getInstance().getServerName().equals(servername.trim())){
				MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
				ArticleRelationShipManager ars = ArticleRelationShipManager.getInstance();
				Player []ps = PlayerManager.getInstance().getPlayerByUser(username);
				for(Player p:ps){
					long pid = p.getId();
					Integer sid = msm.getPlayerRelationMap().get(pid);
					if(sid != null) {
						MoneyRelationShip ship = msm.getRelationShipMap().get(sid);
						if(ship != null && msm.isForbid(ship)) {
							Player pp = PlayerManager.getInstance().getPlayer(pid);
							if(pp!=null){
								MoneyMaker mm =	msm.getMoneyMaker(pid);
								if(mm!=null){
									StringBuffer sb = new StringBuffer();
									sb.append(servername);
									sb.append("#####");
									sb.append(mm.getUsername());
									sb.append("#####");
									sb.append(mm.getName());
									sb.append("#####");
									sb.append(mm.getLevel());
									sb.append("#####");
									sb.append(p.getVipLevel());
									sb.append("#####");
									sb.append(mm.getCurrentSilver());
									sb.append("#####");
									sb.append(mm.getTotalSilver());
									sb.append("#####");
									sb.append(mm.getTotalUp());
									sb.append("#####");
									sb.append(mm.getUpTimes());
									sb.append("#####");
									sb.append(mm.getRmb());
									sb.append("#####");
									sb.append(mm.getMt());
									sb.append("#####");
									sb.append("汇聚银子");
									sb.append("#####");
									sb.append(pid);
									sb.append("@@@@@");
									out.print(sb.toString());
								}else{
									MoneyRelationShipManager.logger.warn("[通过帐号查询角色是否封禁] [银子] [出错] [帐号:"+username+"] [id:"+pid+"]");
								}
							}
						}
					}
					
					sid = ars.getPlayerRelationMap().get(pid);
					if(sid != null) {
						ArticleRelationShip ship = ars.getRelationShipMap().get(sid);
						if(ship != null && ars.isForbid(ship)) {
							Player pp = PlayerManager.getInstance().getPlayer(pid);
							if(pp!=null){
								ArticleMaker mm2 = ars.getArticleMaker(pid);
								if(mm2!=null){
									StringBuffer sb2 = new StringBuffer();
									sb2.append(servername);
									sb2.append("#####");
									sb2.append(mm2.getUsername());
									sb2.append("#####");
									sb2.append(mm2.getName());
									sb2.append("#####");
									sb2.append(mm2.getLevel());
									sb2.append("#####");
									sb2.append(p.getVipLevel());
									sb2.append("#####");
									sb2.append(mm2.getCurrentSilver());
									sb2.append("#####");
									sb2.append(mm2.getTotalSilver());
									sb2.append("#####");
									sb2.append(mm2.getTotalUp());
									sb2.append("#####");
									sb2.append(mm2.getUpTimes());
									sb2.append("#####");
									sb2.append(mm2.getRmb());
									sb2.append("#####");
									sb2.append(mm2.getMt());
									sb2.append("#####");
									sb2.append("汇聚物品");
									sb2.append("#####");
									sb2.append(pid);
									sb2.append("@@@@@");
									out.print(sb2.toString());
								}else{
									MoneyRelationShipManager.logger.warn("[通过帐号查询角色是否封禁] [物品] [出错] [帐号:"+username+"] [id:"+pid+"]");
								}
							}
						}
					}
				}
			} else {
				out.println("服务器不匹配"+servername+"===" + GameConstants.getInstance().getServerName());
			}
		}catch(Throwable e){
			MoneyRelationShipManager.logger.warn("[通过帐号查询角色是否封禁] [异常] [帐号:"+username+"] [服务器:"+servername+"]",e);			
		}
		
	}
%>
