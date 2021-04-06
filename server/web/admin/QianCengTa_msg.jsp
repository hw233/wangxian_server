<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaActivity_RefDao"%>
<%@page import="java.util.ArrayList"%>
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
	千层塔缓存个数:<%=QianCengTaManager.getInstance().cache.getSize() %>个
	<table border="1">
	<tr>
		<td><%="ID" %></td>
		<td><%="状态" %></td>
		<td><%="线程Index" %></td>
		<td><%="game" %></td>
		<td><%="刷新次数" %></td>
		<td><%="maxDao" %></td>
		<td><%="maxHDao" %></td>
		<td><%="maxGDao" %></td>
		<td><%="maxCheng" %></td>
		<td><%="maxDao" %></td>
		<td><%="maxHDao" %></td>
		<td><%="maxGDao" %></td>
		<td><%="怪物列表" %></td>
		<td><%="活着" %></td>
		<td><%="刷新Index" %></td>
		<td><%="应该刷新次数" %></td>
	</tr>
	<% 
	
	for (int i = 0; i < QianCengTaManager.getInstance().getThreads().length; i++) {
		QianCengTa_Thread thread = QianCengTaManager.getInstance().getThreads()[i];
		QianCengTa_Ta[] ids = thread.getTaLists().toArray(new QianCengTa_Ta[0]);
		for (int j = 0; j < thread.getTaLists().size(); j++) {
			QianCengTa_Ta ta = ids[j];
			if (ta == null) {
				continue;
			}
			boolean isGame = ta.getGame() != null;
			int num = 0;
			for (int k = 0; k < ta.monsterList.size();k++) {
				if(ta.monsterList.get(k).isDeath() == false){
					num += 1;
				}
			}
			QianCengTa_CengInfo ci = null;
			if (ta.getGame() != null) {
				ci = QianCengTa_TaInfo.taInfo.getCengInfo(ta.getGame().getQianCengTa().getDao().getDaoIndex(), ta.getGame().getQianCengTa().getCengIndex());
			}
			int reNum = 0;
			if (ci != null) {
				reNum = ci.getMonsters().size();
			}
		%>
		<tr>
			<td><%=ta.getPlayerId() %></td>
			<td><%=ta.getStatus() %></td>
			<td><%=ta.getThreadIndex() %></td>
			<td><%=isGame %></td>
			<td><%=ta.getTotalFlushTimes() %></td>
			<td><%=ta.getMaxDao() %></td>
			<td><%=ta.getMaxHardDao() %></td>
			<td><%=ta.getMaxGulfDao() %></td>
			<td><%=ta.getMaxCengInDao() %></td>
			<td><%=ta.getDaoList().size() %></td>
			<td><%=ta.getHardDaoList().size() %></td>
			<td><%=ta.getGulfDaoList().size() %></td>
			<td><%=ta.monsterList.size() %></td>
			<td><%=num %></td>
			<td><%=ta.refMonsterIndex %></td>
			<td><%=reNum %></td>
		</tr>
	<%
		}
	}
	%>
	
	
	<%
		QianCengTa_TaInfo taInfo = QianCengTa_TaInfo.hardTaInfo;
		for (int i = 0; i < taInfo.daoList.size(); i++) {
			QianCengTa_DaoInfo daoInfo = taInfo.daoList.get(i);
			for (int j = 0; j < daoInfo.getCengList().size(); j++) {
				QianCengTa_CengInfo cengInfo = daoInfo.getCengList().get(j);
				for (int k = 0; k < cengInfo.getFlopSets().size(); k++) {
					QianCengTa_FlopSet flop = cengInfo.getFlopSets().get(k);
					out.println(Arrays.toString(flop.getArticleNames()));
				}
				out.println("<br>");
			}
		}
	%>
	
	<%
	ArrayList<QianCengTaActivity_RefDao> refActivitys = QianCengTaManager.getInstance().refActivitys;
	out.println("活动个数:" + refActivitys.size() + "<br>");
	SimpleDateFormat f = new SimpleDateFormat("yy/MM/dd HH:mm");
	for(int i = 0; i < refActivitys.size(); i++) {
		QianCengTaActivity_RefDao r1 = refActivitys.get(i);
		out.println(f.format(new Date(r1.getStartTime())) + "   " + f.format(new Date(r1.getEndTime())) + "  平台" + r1.getPlatformType() + "   " + r1.getRefNandu() + "  服务器 " + Arrays.toString(r1.getServerNames()) + "   开启" + r1.isStart(0));
		out.println("<br>");
	}
	%>
</body>
</html>