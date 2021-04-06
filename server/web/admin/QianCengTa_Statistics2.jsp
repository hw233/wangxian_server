<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_DaoInfo"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Statistics"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
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
	千层塔记录数:<%=QianCengTa_Statistics.statistics.size() %>个
	<table border="1" width ="2000">
		<tr>
			<td width="200"><%="时间" %></td>
			<td><%="道" %></td>
			<td><%="层" %></td>
			<td><%="玩家ID" %></td>
			<td width="100"><%="名字" %></td>
			<td><%="等级" %></td>
			<td><%="vip等级" %></td>
			<td><%="hp" %></td>
			<td><%="外功" %></td>
			<td><%="内功" %></td>
			<td><%="外防" %></td>
			<td><%="内防" %></td>
			<td width="100"><%="宠物名字" %></td>
			<td><%="宠物级别" %></td>
			<td><%="宠物hp" %></td>
			<td><%="宠物外功" %></td>
			<td><%="宠物内功" %></td>
			<td><%="宠物外防" %></td>
			<td><%="宠物内防" %></td>
			<td><%="宠物技能" %></td>
		</tr>
		<%
			for (int i = 0; i < QianCengTa_Statistics.statistics.size(); i++) {
				QianCengTa_Statistics qct_st = QianCengTa_Statistics.statistics.get(i);
				String skills = "";
				for (int nn = 0; nn < qct_st.getPetSkills().length; nn++) {
					skills = skills + qct_st.getPetSkills()[nn] + ",";
				}
		%>
			<tr>
			<td width="200"><%=qct_st.getPassTime() %></td>
			<td><%=QianCengTa_DaoInfo.QianCengDaoName[qct_st.getDaoIndex()] %></td>
			<td><%=qct_st.getCengIndex()+1 %></td>
			<td><%=qct_st.getPlayerID() %></td>
			<td width="100"><%=qct_st.getPlayerName() %></td>
			<td><%=qct_st.getPlayerLevel() %></td>
			<td><%=qct_st.getVipLevel() %></td>
			<td><%=qct_st.getMaxHp() %></td>
			<td><%=qct_st.getPhysicalDamage() %></td>
			<td><%=qct_st.getMagicDamage() %></td>
			<td><%=qct_st.getPhysicalDefence() %></td>
			<td><%=qct_st.getMagicDefence() %></td>
			<td width="100"><%=qct_st.getPetName() %></td>
			<td><%=qct_st.getPetLevel() %></td>
			<td><%=qct_st.getPetMaxHp() %></td>
			<td><%=qct_st.getPetPhysicalDamage() %></td>
			<td><%=qct_st.getPetMagicDamage() %></td>
			<td><%=qct_st.getPetPhysicalDefence() %></td>
			<td><%=qct_st.getPetMagicDefence() %></td>
			<td><%=skills %></td>
		</tr>
		<%
			}
		%>
	</table>
	
</body>
</html>