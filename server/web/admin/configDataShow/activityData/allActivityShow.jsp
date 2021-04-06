<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Collections"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.activity.BaseActivityInstance"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.AllActivityManager"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivityManager"%>
<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<html>
	<head>
	</head>
	<body>
		<%
			String action = request.getParameter("action");
			String checkType1 = request.getParameter("checkType1");
			String checkType2 = request.getParameter("checkType2");
			checkType1 = checkType1 == null ? "" : checkType1;
			checkType2 = checkType2 == null ? "" : checkType2;
		
			String color_red = "#CD0000";
			String color_yellow = "#949494";
			String color_greew = "#008B00";
			String color_blue = "#000080";
			String color_black = "#000000";
		%>
		<table style="text-align: right; font-size: 12px; float: left; border-width: 20%;" border="1">
			<tr>
				<th>活动类型</th>
				<th>查看操作</th>
			</tr>
			<tr bgcolor="red">
				<td>全部活动</td>
				<td>
					<form action="allActivityShow.jsp" style="float: left;" method="post">
					<input type="hidden" name="checkType1" value="0"></input>
					<input type="hidden" name="checkType2" value=全部活动></input>
						<input type="submit" value="全部活动"></input>
					</form>
					<form action="allActivityShow.jsp" method="post"  style="color: <%=color_greew%>;float: left;">
					<input type="hidden" name="checkType1" value="3"></input>
					<input type="hidden" name="checkType2" value=全部活动></input>
						<input type="submit" value="正在开启活动" style="background-color: #90EE90;"></input>
					</form>
					<form action="allActivityShow.jsp" method="post" style="float: left;">
					<input type="hidden" name="checkType1" value="1"></input>
					<input type="hidden" name="checkType2" value=全部活动></input>
						<input type="submit" value="即将开启活动" style="background-color: #CDC673;"></input>
					</form>
					<form action="allActivityShow.jsp" method="post" style="float: left;">
					<input type="hidden" name="checkType1" value="2"></input>
					<input type="hidden" name="checkType2" value=全部活动></input>
						<input type="submit" value="过期活动" style="background-color: red;"></input>
					</form>
				</td>
			</tr>
			<%
				Iterator<String> ite = AllActivityManager.instance.allActivityMap.keySet().iterator();
				int index1 = 0;
				while(ite.hasNext()) {
					String key = ite.next();
					%>
			<tr bgcolor="<%=(index1++ % 2) == 0 ? "#949494" : "#FFFFFF"%>">
				<td><%=key%></td>
				<td>
					<form action="allActivityShow.jsp" style="float: left;" method="post">
					<input type="hidden" name="checkType1" value="0"></input>
					<input type="hidden" name="checkType2" value=<%=key%>></input>
						<input type="submit" value="全部活动"></input>
					</form>
					<form action="allActivityShow.jsp" method="post"  style="color: <%=color_greew%>;float: left;">
					<input type="hidden" name="checkType1" value="3"></input>
					<input type="hidden" name="checkType2" value=<%=key%>></input>
						<input type="submit" value="正在开启活动" style="background-color: #90EE90;"></input>
					</form>
					<form action="allActivityShow.jsp" method="post" style="float: left;">
					<input type="hidden" name="checkType1" value="1"></input>
					<input type="hidden" name="checkType2" value=<%=key%>></input>
						<input type="submit" value="即将开启活动" style="background-color: #CDC673;"></input>
					</form>
					<form action="allActivityShow.jsp" method="post" style="float: left;">
					<input type="hidden" name="checkType1" value="2"></input>
					<input type="hidden" name="checkType2" value=<%=key%>></input>
						<input type="submit" value="过期活动" style="background-color: red;"></input>
					</form>
				</td>
			</tr>
			<%
				}
			%>
		</table>
		<table style="text-align: right; font-size: 12px; float: left; border-width: 60%;" border="1">
			<tr>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>本服是否开放</th>
				<th>开放平台</th>
				<th>开放服务器</th>
				<th>不开放服务器</th>
				<th>活动内容</th>
			</tr>
			<%
				List<BaseActivityInstance> resultList = AllActivityManager.instance.allActivityMap.get(checkType2);
				if("全部活动".equals(checkType2)) {
					resultList = new ArrayList<BaseActivityInstance>();
					Iterator<String> ite2 = AllActivityManager.instance.allActivityMap.keySet().iterator();
					while(ite2.hasNext()) {
						String key = ite2.next();
						for(int ii=0; ii<AllActivityManager.instance.allActivityMap.get(key).size(); ii++) {
							resultList.add(AllActivityManager.instance.allActivityMap.get(key).get(ii));
						}
					}
				}
				if(resultList != null && resultList.size() > 0) {
					List<BaseActivityInstance> showList = new ArrayList<BaseActivityInstance>();
					if("0".equals(checkType1)) {					//查看全部活动
						showList = resultList;
					} else if("1".equals(checkType1)) {				//查看即将开启活动
						long now = System.currentTimeMillis();
						for (int i=0; i<resultList.size(); i++) {
							if(resultList.get(i).getStartTime() > now) {
								showList.add(resultList.get(i));
							}
						}
					} else if("2".equals(checkType1)) {				//查看已经过期活动
						long now = System.currentTimeMillis();
						for (int i=0; i<resultList.size(); i++) {
							if(resultList.get(i).getEndTime() < now) {
								showList.add(resultList.get(i));
							}
						}
					} else if("3".equals(checkType1)) {				//查看正在开启活动
						long now = System.currentTimeMillis();
						for (int i=0; i<resultList.size(); i++) {
							if(now >= resultList.get(i).getStartTime() && now <= resultList.get(i).getEndTime()) {
								showList.add(resultList.get(i));
							}
						}
					}
					int index = 0;
					Collections.sort(showList,desc);
					for(int ii =0; ii<showList.size(); ii++) {
						String openResult = showList.get(ii).isThisServerFit();
						if(openResult == null) {
							openResult = "正常开放";
						}
						String colorType = color_yellow;
						if("正常开放".equals(openResult)) {
							colorType = color_greew;
						} else if("未到达开放时间".equals(openResult)) {
							colorType = color_red;
						}
						%>
						<tr bgcolor="<%=(index++ % 2) == 0 ? "#FFFFFF" : "#8CACE8"%>">
							<td><%=(new Timestamp(showList.get(ii).getStartTime())) %></td>
							<td><%=(new Timestamp(showList.get(ii).getEndTime())) %></td>
							<td style="color: <%= colorType %>"><%=openResult %></td>
							<td><%=showList.get(ii).getPlatForm4Show() %></td>
							<td><%=showList.get(ii).getOpenServer4Show() %></td>
							<td><%=showList.get(ii).getNotOpenServer4Show() %></td>
							<td><%=showList.get(ii).getInfoShow() %></td>
						</tr>
						<%
					}
				}
			%>
			<%!
			Comparator <BaseActivityInstance> desc = new Comparator<BaseActivityInstance>(){
				public int compare(BaseActivityInstance o1, BaseActivityInstance o2) {
					long now1 = System.currentTimeMillis();
					if(now1 >= o1.getStartTime() && now1 <= o1.getEndTime()) {
						return -1;
					}
					if((o1.getEndTime() - o2.getEndTime()) > 0) {
						return -1;
					}
					return 1;
				}
			};
			%>
		</table>
	</body>
</html>
