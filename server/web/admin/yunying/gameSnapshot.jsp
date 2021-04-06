<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.tools.gamestat.GameStatService"%>
<%@page import="com.fy.engineserver.util.GameStatTool"%>
<%@page import="com.fy.engineserver.util.RowData"%>
<%@page import="com.xuanzhi.tools.gamestat.AnalyseTool"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String option = request.getParameter("option");
	if ("snapshot".equals(option)) {
		Player [] players = GamePlayerManager.getInstance().getOnlinePlayers();
		for (Player p : players) {
			if (p.isOnline()) {
				GameStatTool.update(p);
			}
		}
		
		AnalyseTool analyseTool = new AnalyseTool(RowData.class);
		analyseTool.analyse();
		
		RowData[] allDatas = GameStatTool.map.values().toArray(new RowData[0]);
		
		long [] 充值金额 = {-1,10,599,5999,29999,Long.MAX_VALUE};
		
		for (int k = 0; k < GameStatService.userTypeNames.length - 1; k ++) {
			ArrayList<RowData> al = new ArrayList<RowData>();
			for(int j = 0 ; j < allDatas.length ; j++){
				if( allDatas[j].充值金额  > 充值金额[k]  &&  allDatas[j].充值金额  <= 充值金额[k + 1] ){
					al.add(allDatas[j]);
				}
			}
			RowData[] data = al.toArray(new RowData[0]);
			
			RowData[] result = new RowData[260];
			for (int i = 0; i < result.length; i++) {
				result[i] = (RowData) analyseTool.mergeByIndex(data, i + 1);
			}
			GameStatService.getInstance().saveData(RowData.class.getName(), DateUtil.formatDate(new Date(), "yyyy-MM-dd"), k, result);

		}
		RowData[] result = new RowData[260];
		int realNum = 0;
		for (int i = 0; i < result.length; i++) {
			result[i] = (RowData) analyseTool.mergeByIndex(allDatas, i + 1);
			if (result[i]  != null) {
				realNum ++ ;
			}
		}
		GameStatService.getInstance().saveData(RowData.class.getName(), DateUtil.formatDate(new Date(), "yyyy-MM-dd"), GameStatService.userTypeNames.length - 1, result);

		
		out.print("<H2>数据生成成功,点击查询" + realNum +"<H2>");
		out.print("<HR>");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<form action="./gameSnapshot.jsp" method="post">
	<input type="hidden" name="option" value="snapshot">
	<input type="submit" value="生成今天数据">
</form>
<hr>
<a href="./gamestat.jsp?className=<%=RowData.class.getName()%>">数据查询</a>
<hr>
</body>
</html>