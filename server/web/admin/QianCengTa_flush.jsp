<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Dao"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ceng"%>
<%@page import="com.fy.engineserver.authority.AuthorityAgent"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ta"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	String playerFlushNum = request.getParameter("flushNum");
	if (playerFlushNum != null) {
		Player player = null;
		try {
			player = PlayerManager.getInstance().getPlayer(Long.parseLong(playerFlushNum));
		}catch(Exception e) {
	%>
			角色不存在
	<%
		}
		if (player != null) {
			QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(player.getId());
			ta.setTotalCostDaoSilver2Index(0, 0);
			ta.setTotalFlushDaoTimes2Index(0, 0, 0);
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 6; j++) {
					ta.getAuthority()[i][j].lastRefreshDay = -1;
					ta.getAuthority()[i][j].lastRefreshItem = -1;
					AuthorityAgent.getInstance().refreshPlayerAuthority(ta.getAuthority()[i][j], player);
				}
			}
		}
	}
	//刷新塔
	String playerFlushTa =  request.getParameter("flushTa");
	if (playerFlushTa != null) {
		Player player = null; 
		try {
			player = PlayerManager.getInstance().getPlayer(Long.parseLong(playerFlushTa));
		}catch(Exception e) {
	%>
			角色不存在
	<%
		}
		if (player != null) {
			QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(player.getId());
			if (ta != null) {
				ta.getDaoList().clear();
				ta.getHardDaoList().clear();
				ta.getGulfDaoList().clear();
				ta.setMaxCengInDao(0);
				ta.setMaxDao(0);
				ta.setMaxHardDao(0);
				ta.setMaxHardInDao(0);
				ta.setMaxGulfDao(0);
				ta.setMaxGulfInDao(0);
				ta.init();
				ta.doInitBef();
				ta.setTotalCostDaoSilver(new long[6]);
				ta.setTotalFlushDaoTimes(new int[6]);
				//QianCengTaManager.getInstance().cache.put(player.getId(), ta);
			}
		}
	}
	
	String action = request.getParameter("action");
	if (action != null) {
		if ("flushNanDuTa".equals(action)) {
			long id = Long.parseLong(request.getParameter("pID"));
			int nandu = Integer.parseInt(request.getParameter("nandu"));
			int daoIndex = Integer.parseInt(request.getParameter("dao"));
			Player p = PlayerManager.getInstance().getPlayer(id);
			QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(p.getId());
			QianCengTa_Dao dao = null;
			if (nandu == 0) {
				if (ta.getDaoList().size() <= daoIndex) {
					for (int i = 0; i < daoIndex - ta.getDaoList().size() + 1; i++) {
						ta.openDao(0, ta.getDaoList().size() + i);
					}
				}
				dao = ta.getDaoList().get(daoIndex);
			}else if (nandu == 1) {
				if (ta.getHardDaoList().size() <= daoIndex) {
					for (int i = 0; i < daoIndex - ta.getHardDaoList().size() + 1; i++) {
						ta.openDao(0, ta.getHardDaoList().size() + i);
					}
				}
				dao = ta.getHardDaoList().get(daoIndex);
			}else if (nandu == 2) {
				if (ta.getGulfDaoList().size() <= daoIndex) {
					for (int i = 0; i < daoIndex - ta.getGulfDaoList().size() + 1; i++) {
						ta.openDao(0, ta.getGulfDaoList().size() + i);
					}
				}
				dao = ta.getGulfDaoList().get(daoIndex);
			}
			dao.getCengList().clear();
			dao.setCurrentCengIndex(0);
			dao.setMaxReachCengIndex(-1);
			QianCengTa_Ceng ceng = new QianCengTa_Ceng();
			ceng.setDao(dao);
			ceng.setCengIndex(0);
			dao.getCengList().add(ceng);
			if (nandu == 0) {
				ta.notifyChanager("daoList");
			}else if (nandu == 1) {
				ta.notifyChanager("hardDaoList");
			}else if (nandu == 2) {
				ta.notifyChanager("gulfDaoList");
			}
		}else if ("tong1Ceng".equals(action)) {
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(request.getParameter("pID")));
			int nandu = Integer.parseInt(request.getParameter("nandu"));
			if (p != null) {
				QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(p.getId());
				ta.setNandu(nandu);
				QianCengTa_Dao dao = ta.getDao(nandu,0);
				out.println("["+ta.getMaxCengInDao()+"]["+dao.getCengCount()+"]["+dao.getCeng(dao.getCurrentCengIndex()).getCengIndex()+"]");
				ta.notifyPassCeng(p, dao.getCeng(dao.getCurrentCengIndex()));
			}
		} else if ("zhijietong".equals(action)) {
			Player player = GamePlayerManager.getInstance().getPlayer(request.getParameter("pID"));
			QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(player.getId());
			ta.notifyPassCeng(player, player.getCurrentGame().getQianCengTa());
		}
	}
	%>
	
	<form name="f1">
		输入人物id：<input type="text" name="flushNum">
		<input type="submit" value="刷新千层塔次数">
	</form>
	<br>
	<br>
	<form name="f2">
		输入人物id：<input type="text" name="flushTa">
		<input type="submit" value="刷新千层塔">所有塔全部重新手动打
	</form>
	<br>
	刷新某个难度的某道
	<br>
	<form name="f3">
		<input type="hidden" name="action" value="flushNanDuTa">
		输入人物id：<input type="text" name="pID">
		难度(0-2):<input type="text" name="nandu">
		道(0-5):<input type="text" name="dao">
		<input type="submit" value="刷新千层塔">这个难度的塔重新手动打
	</form>
	<br>
	<form>
		<input type="hidden" name="action" value="tong1Ceng">
		<input type="text" name="nandu">
		<input type="text" name="pID">
		<input type="submit" value="通一层">
	</form>
	<br>
	<form>
		<input type="hidden" name="action" value="zhijietong">
		<input type="text" name="pID">角色名:
		<input type="submit" value="直接通">
	</form>
</body>
</html>