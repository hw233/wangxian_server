<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Dao"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_FlopSet"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_DaoInfo"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Thread"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_TaInfo"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_CengInfo"%>
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
		String action = request.getParameter("action");
		if (action != null){
			if (action.equals("ref")) {
				QianCengTaManager manager = QianCengTaManager.getInstance();
				if (manager.cache.values().size() > 0) {
					Set pIds = manager.cache.keySet();
					Iterator it = pIds.iterator();
					while(it.hasNext()) {
						Long id = (Long)it.next();
						QianCengTa_Ta ta = (QianCengTa_Ta)manager.cache.get(id);
						{
							for (int i = 0; i < ta.getDaoList().size(); i++) {
								QianCengTa_Dao dao = ta.getDaoList().get(i);
								if (dao.getDaoIndex() != i) {
									out.println("["+ta.getPlayerId()+"]----DI["+dao.getDaoIndex()+"]数组["+i+"]层数["+dao.getCengCount()+"]道最大["+dao.getMaxReachCengIndex()+"]层最大["+ta.getMaxDao()+"-"+ta.getMaxCengInDao()+"]");
									out.println("<br>");
									if (i > 5) {
									}else {
										dao.setDaoIndex(i);
									}
								}
							}

							while(ta.getDaoList().size() > 6){
								ta.getDaoList().remove(ta.getDaoList().size() - 1);
							}
							ta.notifyChanager("daoList");
							
							for (int i = 0; i < ta.getHardDaoList().size(); i++) {
								QianCengTa_Dao dao = ta.getHardDaoList().get(i);
								if (dao.getDaoIndex() != i) {
									out.println("["+ta.getPlayerId()+"]----DI["+dao.getDaoIndex()+"]数组["+i+"]层数["+dao.getCengCount()+"]道最大["+dao.getMaxReachCengIndex()+"]层最大["+ta.getMaxHardDao()+"-"+ta.getMaxHardInDao()+"]");
									out.println("<br>");
									if (i > 5) {
									}else {
										dao.setDaoIndex(i);
									}
								}
							}
							while(ta.getHardDaoList().size() > 6){
								ta.getHardDaoList().remove(ta.getHardDaoList().size() - 1);
							}
							ta.notifyChanager("hardDaoList");
							
							for (int i = 0; i < ta.getGulfDaoList().size(); i++) {
								QianCengTa_Dao dao = ta.getGulfDaoList().get(i);
								if (dao.getDaoIndex() != i) {
									out.println("["+ta.getPlayerId()+"]----DI["+dao.getDaoIndex()+"]数组["+i+"]层数["+dao.getCengCount()+"]道最大["+dao.getMaxReachCengIndex()+"]层最大["+ta.getMaxGulfDao()+"-"+ta.getMaxGulfInDao()+"]");
									out.println("<br>");
									if (i > 5) {
									}else {
										dao.setDaoIndex(i);
									}
								}
							}
						}
						out.println(id);
						out.println("<br>");
					}
				}
			}else if (action.equals("onePlayerXXXXXXXXXXXXX")) {
				long id = Long.parseLong(request.getParameter("pid"));
				QianCengTa_Ta ta = (QianCengTa_Ta)QianCengTaManager.getInstance().cache.get(id);
				{
					ArrayList<QianCengTa_Dao> errDao = new ArrayList<QianCengTa_Dao>();
					for(int i = 0 ; i < ta.getDaoList().size() ; i++){
						QianCengTa_Dao dao = ta.getDaoList().get(i);
						if (dao.getDaoIndex() != i) {
							errDao.add(dao);
							continue;
						}
					}
					if (errDao.size() > 0) {
						QianCengTa_Ta.logger.error("[千层塔] [道数据异常] [列表中daoIndex不正确] 难度[{}] [{}] [{}]", new Object[]{0, errDao.size(), errDao.get(0).getDaoIndex()});
					}
					for (int i = 0; i < errDao.size(); i++) {
						ta.getDaoList().remove(errDao.get(i));
					}
					errDao.clear();
					for(int i = 0 ; i < ta.getHardDaoList().size() ; i++){
						QianCengTa_Dao dao = ta.getHardDaoList().get(i);
						if (dao.getDaoIndex() != i) {
							errDao.add(dao);
							continue;
						}
					}
					if (errDao.size() > 0) {
						QianCengTa_Ta.logger.error("[千层塔] [道数据异常] [列表中daoIndex不正确] 难度[{}] [{}] [{}]", new Object[]{1, errDao.size(), errDao.get(1).getDaoIndex()});
					}
					for (int i = 0; i < errDao.size(); i++) {
						ta.getHardDaoList().remove(errDao.get(i));
					}
					errDao.clear();
					for(int i = 0 ; i < ta.getGulfDaoList().size() ; i++){
						QianCengTa_Dao dao = ta.getGulfDaoList().get(i);
						if (dao.getDaoIndex() != i) {
							errDao.add(dao);
							continue;
						}
					}
					if (errDao.size() > 0) {
						QianCengTa_Ta.logger.error("[千层塔] [道数据异常] [列表中daoIndex不正确] 难度[{}] [{}] [{}]", new Object[]{2, errDao.size(), errDao.get(1).getDaoIndex()});
					}
					for (int i = 0; i < errDao.size(); i++) {
						ta.getGulfDaoList().remove(errDao.get(i));
					}
					//修正bug
					if (ta.getDaoList().size() == ta.getMaxDao()) {
						ta.openDao(0, ta.getMaxDao());
					}
					if (ta.getHardDaoList().size() == ta.getMaxHardDao()) {
						ta.openDao(1, ta.getMaxHardDao());
					}
					
					for (int i = 0; i < ta.getDaoList().size(); i++) {
						QianCengTa_Dao dao = ta.getDaoList().get(i);
						if (dao.getMaxReachCengIndex() == 24) {
							if (ta.getHardDaoList().size() < i+1) {
								ta.openDao(1, i);
								QianCengTa_Ta.logger.warn("修正玩家千层塔错误: ["+ta.getLogString(1)+"]");
							}
						}
					}
					for (int i = 0; i < ta.getHardDaoList().size(); i++) {
						QianCengTa_Dao dao = ta.getHardDaoList().get(i);
						if (dao.getMaxReachCengIndex() == 24) {
							if (ta.getGulfDaoList().size() < i+1) {
								ta.openDao(2, i);
								QianCengTa_Ta.logger.warn("修正玩家千层塔错误: ["+ta.getLogString(2)+"]");
							}
						}
					}
				}
				out.println(id);
				out.println("<br>");
			}
		}
	%>
	<br>
	刷在线玩家BUG
	<form>
	<input type="hidden" name="action" value="ref">
	<input type="submit" value="刷数据">
	</form>
	<br>
	查询某个玩家的千层塔详细
	<form>
	<input type="hidden" name="action" value="onePlayer">
	<input type="text" name="pid">
	<input type="submit" value="查询">
	</form>
</body>
</html>