<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String keyWord = request.getParameter("keyWord");
	String pageNum = request.getParameter("pageNum");
	String _page = request.getParameter("_page");
	String country = request.getParameter("country");
	String option = request.getParameter("option");
	List<Jiazu> result = new ArrayList<Jiazu>();
	if ("query".equals(option)) {
		result = getOrder(keyWord, Byte.valueOf(_page), Byte.valueOf(pageNum), 1, false, Byte.valueOf(country));
	}
%>
<%!
	public List<Jiazu> getOrder(String keyword, int page, int pageNum, int order, boolean isAsc, byte country) {

	List<Jiazu> list = new ArrayList<Jiazu>();
	int start = page * pageNum + 1;
	int end = start + pageNum;
	if (order == 2) {
		try {
			list = JiazuManager.jiazuEm.query(Jiazu.class, " country = " + country + " and name like '%" + keyword + "%'",  "level DESC " , start, end);
		} catch (Exception e) {
			JiazuManager.logger.error("[按条件查询家族异常] [keyword:" + keyword + "] [page:" + page + "] [pageNum:" + pageNum + "] [order:" + order + "] [isAsc:" + isAsc + "]", e);
		}
		return list;
	} else if (order == 1) {
		try {
			list = JiazuManager.jiazuEm.query(Jiazu.class, " country = " + country + " and name like '%" + keyword + "%'", "level DESC ," + "createTime" + " " + (isAsc ? "ASC" : "DESC") , start, end);
		} catch (Exception e) {
			JiazuManager.logger.error("[按条件查询家族异常] [keyword:" + keyword + "] [page:" + page + "] [pageNum:" + pageNum + "] [order:" + order + "] [isAsc:" + isAsc + "]", e);
		}

		return list;
	}
	// TODO 按照家族驻地的繁荣度来排序
	else {

		return null;
	}


}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="./jiazuOrder.jsp" method="post">
	关键字:<input type="text" name="keyWord" value="<%=keyWord%>"><BR/>
	每页条数:<input type="text" name="pageNum" value="<%=pageNum%>"><BR/>
	页&nbsp;&nbsp;&nbsp;&nbsp;码:<input type="text" name="_page" value="<%=_page%>"><BR/>
	国&nbsp;&nbsp;&nbsp;&nbsp;家:<input type="text" name="country" value="<%=country%>"><BR/>
	<input type="hidden" name="option" value="query">
	<input type="submit" value="查询">
</form>
<hr>
<%
	if ("query".equals(option)) {
		%>
		<table border="1">
		<tr>
			<td>家族ID</td>
			<td>家族名</td>
			<td>家族等级</td>
		</tr>
		<%
		if (result != null) {
			for (Jiazu jiazu : result) {
			%>
			<tr>
				<td><%=jiazu.getJiazuID() %></td>
				<td><%=jiazu.getName() %></td>
				<td><%=jiazu.getLevel() %></td>
			</tr>
			<%		
			}
		}
		%>
		
		</table>
		<%
	}
%>
</body>
</html>