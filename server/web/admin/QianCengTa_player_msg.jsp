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
		if (action != null) {
			if (action.equals("onePlayer")) {
				String id = request.getParameter("pid");
				Player pp = null;
				try {
					pp = PlayerManager.getInstance().getPlayer(Long.parseLong(id));
				}catch(Exception e) {
					
				}
				if (pp != null) {
					QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(pp.getId());
					while(ta.getDaoList().size() > 6){
						ta.getDaoList().remove(ta.getDaoList().size() - 1);
					}
					while(ta.getHardDaoList().size() > 6){
						ta.getHardDaoList().remove(ta.getHardDaoList().size() - 1);
					}
					out.println("玩家名字:["+pp.getName()+"] 等级:["+pp.getSoulLevel()+"] <br>");
					out.println("千层塔难度0:["+ta.getDaoList2Nandu(0).size()+"] ["+ta.getMaxDao()+"::"+ta.getMaxCengInDao()+"] <br>");
					boolean isOpen = true;
					for (int i = 0; i < ta.getDaoList2Nandu(0).size(); i++) {
						QianCengTa_Dao dao = ta.getDaoList2Nandu(0).get(i);
						if (isOpen && dao.getDaoIndex() != i) {
							dao.setDaoIndex(i);
							ta.notifyChanager("daoList");
						}
						out.println("第"+i+"道:["+dao.getCengList().size()+"] ["+dao.getDaoIndex()+"] ["+dao.getMaxReachCengIndex()+"] ["+dao.getCurrentCengIndex()+"] <br>");
					}
					out.println("千层塔难度1:["+ta.getDaoList2Nandu(1).size()+"] ["+ta.getMaxHardDao()+"--"+ta.getMaxHardInDao()+"] <br>");
					for (int i = 0; i < ta.getDaoList2Nandu(1).size(); i++) {
						QianCengTa_Dao dao = ta.getDaoList2Nandu(1).get(i);
						if (isOpen && dao.getDaoIndex() != i) {
							dao.setDaoIndex(i);
							ta.notifyChanager("hardDaoList");
						}
						out.println("第"+i+"道:["+dao.getCengList().size()+"] ["+dao.getDaoIndex()+"] ["+dao.getMaxReachCengIndex()+"] ["+dao.getCurrentCengIndex()+"] <br>");
					}
					out.println("千层塔难度2:["+ta.getDaoList2Nandu(2).size()+"] ["+ta.getMaxGulfDao()+"--"+ta.getMaxGulfInDao()+"] <br>");
					for (int i = 0; i < ta.getDaoList2Nandu(2).size(); i++) {
						QianCengTa_Dao dao = ta.getDaoList2Nandu(2).get(i);
						if (isOpen && dao.getDaoIndex() != i) {
							dao.setDaoIndex(i);
							ta.notifyChanager("gulfDaoList");
						}
						out.println("第"+i+"道:["+dao.getCengList().size()+"] ["+dao.getDaoIndex()+"] ["+dao.getMaxReachCengIndex()+"] ["+dao.getCurrentCengIndex()+"] <br>");
					}
				}
			}
		}
	%>
	<br>
	查询某个玩家的千层塔详细
	<form>
	<input type="hidden" name="action" value="onePlayer">
	<input type="text" name="pid">
	<input type="submit" value="查询">
	</form>
</body>
</html>