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
<%
	int[][] playerNums = new int[6][25];
	List<QianCengTa_Ta> list = new ArrayList<QianCengTa_Ta>();
	try {
		long count = QianCengTaManager.getInstance().em.count();
		long queryIndex = 1;
		while (count > 5000) {
			list.addAll(QianCengTaManager.getInstance().em.query(QianCengTa_Ta.class, "", new Object[] {}, "", queryIndex, queryIndex + 5000));
			count -= 5000;
			queryIndex += 5000;
		}
		if (count > 0) {
			list.addAll(QianCengTaManager.getInstance().em.query(QianCengTa_Ta.class, "", new Object[] {}, "", queryIndex, queryIndex + count));
		}
	} catch (Exception e) {
		QianCengTaManager.getInstance().logger.error("QianCengTa_Statistics1.jsp===", e);
		return;
	}
	for (int i = 0; i < list.size(); i++) {
		QianCengTa_Ta ta = list.get(i);
		int daoIndex = ta.getMaxDao();
		int cengIndex = ta.getMaxCengInDao();
		if (daoIndex >=0 && daoIndex < 6) {
			if (cengIndex >= 0 && cengIndex <25) {
				playerNums[daoIndex][cengIndex] += 1;
			}else if (cengIndex == -1) {
				playerNums[daoIndex][0] += 1;
			}
		}
	}
%>
<body>
	千层塔分布统计:一共<%=QianCengTaManager.getInstance().em.count() %>个
	<% for (int i = 0; i < 6; i++) {
		
	%>
	<table border="1">
		<tr>
			<td><%=Translate.text_qiancengta_DaoNames[i] %></td>
			<td><%="1层" %></td>
			<td><%="2层" %></td>
			<td><%="3层" %></td>
			<td><%="4层" %></td>
			<td><%="5层" %></td>
			<td><%="6层" %></td>
			<td><%="7层" %></td>
			<td><%="8层" %></td>
			<td><%="9层" %></td>
			<td><%="10层" %></td>
			<td><%="11层" %></td>
			<td><%="12层" %></td>
			<td><%="13层" %></td>
			<td><%="14层" %></td>
			<td><%="15层" %></td>
			<td><%="16层" %></td>
			<td><%="17层" %></td>
			<td><%="18层" %></td>
			<td><%="19层" %></td>
			<td><%="20层" %></td>
			<td><%="21层" %></td>
			<td><%="22层" %></td>
			<td><%="23层" %></td>
			<td><%="24层" %></td>
			<td><%="25层" %></td>
		</tr>
		<tr>
			<td><%=Translate.text_qiancengta_DaoNames[i] %></td>
			<td><%=playerNums[i][0] %></td>
			<td><%=playerNums[i][1] %></td>
			<td><%=playerNums[i][2] %></td>
			<td><%=playerNums[i][3] %></td>
			<td><%=playerNums[i][4] %></td>
			<td><%=playerNums[i][5] %></td>
			<td><%=playerNums[i][6] %></td>
			<td><%=playerNums[i][7] %></td>
			<td><%=playerNums[i][8] %></td>
			<td><%=playerNums[i][9] %></td>
			<td><%=playerNums[i][10] %></td>
			<td><%=playerNums[i][11] %></td>
			<td><%=playerNums[i][12] %></td>
			<td><%=playerNums[i][13] %></td>
			<td><%=playerNums[i][14] %></td>
			<td><%=playerNums[i][15] %></td>
			<td><%=playerNums[i][16] %></td>
			<td><%=playerNums[i][17] %></td>
			<td><%=playerNums[i][18] %></td>
			<td><%=playerNums[i][19] %></td>
			<td><%=playerNums[i][20] %></td>
			<td><%=playerNums[i][21] %></td>
			<td><%=playerNums[i][22] %></td>
			<td><%=playerNums[i][23] %></td>
			<td><%=playerNums[i][24] %></td>
		</tr>
	</table>
	<br>
	<%}%>
	
	
</body>
</html>