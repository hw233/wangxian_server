<%@page import="com.fy.engineserver.trade.requestbuy.RequestBuyBuConfig"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.trade.requestbuy.RequestOption"%>
<%@page import="com.fy.engineserver.trade.requestbuy.service.RequestBuyManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<%
	String action = request.getParameter("action");
	if (action != null) {
		if (action.equals("add_newRequestBuyBu")) {
			//http://112.25.14.11:8082/admin/requestBuyBuSet.jsp?
			//action=add_newRequestBuyBu&mainType=%E8%A7%92%E8%89%B2%E8%A3%85%E5%A4%8781-100
			//&colorType=%E7%99%BD%E8%89%B2&money=300&saleTime=100&maxNum=2222
			String mainType = request.getParameter("mainType");
			String colorType = request.getParameter("colorType");
			long money = 0;
			int saleTime = 0;
			int maxNum = 0;
			if (request.getParameter("money") != null && !"".equals(request.getParameter("money"))) {
				money = Long.parseLong(request.getParameter("money"));
			}
			if (request.getParameter("saleTime") != null && !"".equals(request.getParameter("saleTime"))) {
				saleTime = Integer.parseInt(request.getParameter("saleTime"));
			}
			if (request.getParameter("maxNum") != null && !"".equals(request.getParameter("maxNum"))) {
				maxNum = Integer.parseInt(request.getParameter("maxNum"));
			}
			if (money > 0 && saleTime > 0 && maxNum > 0) {
				RequestBuyBuConfig requestBuyconfig = new RequestBuyBuConfig();
				int mainIndex = -1;
				for (int i = 0; i < RequestBuyBuConfig.REQUEST_TYPES.length; i++) {
					if (RequestBuyBuConfig.REQUEST_TYPES[i].equals(mainType)) {
						mainIndex = i;
						break;
					}
				}
				if (mainIndex >=0 ) {
					requestBuyconfig.setRequestType(mainIndex);
					int colorIndex = -1;
					for (int i = 0; i < RequestBuyBuConfig.REQUEST_COLOR_TYPES.length; i++) {
						if (RequestBuyBuConfig.REQUEST_COLOR_TYPES[i].equals(colorType)) {
							colorIndex = i;
							break;
						}
					}
					if (colorIndex >= 0) {
						requestBuyconfig.setColorType(colorIndex);
						requestBuyconfig.setPrice(money);
						requestBuyconfig.setTimeSpace(saleTime * 1000);
						requestBuyconfig.setMaxNum(maxNum);
						requestBuyconfig.setBuNum(0);
						RequestBuyManager.getInstance().getBuConfigs().add(requestBuyconfig);
						RequestBuyManager.getInstance().setWriteTime(0);
					}
				}
			}
			//response.sendRedirect("./requestBuyBuSet.jsp");
			System.out.println("新增" + mainType + colorType + money + "~" + saleTime + "~" + maxNum);
		}else if (action.indexOf("xiugai") >= 0) {
			int index = Integer.parseInt(action.substring("xiugai".length()));
			String money = request.getParameter("price"+index);
			String timeSpace = request.getParameter("timeSpace"+index);
			String maxNum = request.getParameter("maxNum"+index);
			if (money != null && timeSpace != null && maxNum != null) {
				RequestBuyBuConfig buConfig = RequestBuyManager.getInstance().getBuConfigs().get(index);
				if (buConfig != null) {
					long moneyL = Long.parseLong(money);
					int timeSpaceI = Integer.parseInt(timeSpace);
					int maxNumI = Integer.parseInt(maxNum);
					buConfig.setPrice(moneyL);
					buConfig.setTimeSpace(timeSpaceI * 1000);
					buConfig.setMaxNum(maxNumI);
				}
			}
			
			//response.sendRedirect("./requestBuyBuSet.jsp");
			System.out.println("修改" + action + "~" + money + "~" + timeSpace + "~" + maxNum);
		}else if (action.indexOf("shanchu") >= 0) {
			int index = Integer.parseInt(action.substring("shanchu".length()));
			if (index >= 0 && index < RequestBuyManager.getInstance().getBuConfigs().size()) {
				RequestBuyManager.getInstance().getRemoveConfigs().add(RequestBuyManager.getInstance().getBuConfigs().get(index));
			}
			//response.sendRedirect("./requestBuyBuSet.jsp");
			System.out.println("删除" + action + "~" + index);
		}else if ("open_close".equals(action)){
			if (RequestBuyManager.getInstance().isOpen) {
				RequestBuyManager.getInstance().isOpen = false;
				RequestBuyBuConfig.logger.warn("~~~~~~~~~~~关闭补求购系统~~~~~~~");
			}else {
				RequestBuyManager.getInstance().isOpen = true;
				RequestBuyBuConfig.logger.warn("~~~~~~~~~~~开启补求购系统~~~~~~~");
			}
			//response.sendRedirect("./requestBuyBuSet.jsp");
		}else if ("chanager_time".equals(action)) {
			String time1_str = request.getParameter("time1");
			String time2_str = request.getParameter("time2");
			String ratio_str = request.getParameter("ratio");
			String num_str = request.getParameter("num");
			long time1 = Long.parseLong(time1_str);
			long time2 = Long.parseLong(time2_str);
			float ratio = Float.parseFloat(ratio_str);
			int num = Integer.parseInt(num_str);
			RequestBuyBuConfig.TIME_NOSALE = time1;
			RequestBuyBuConfig.TIME_SALERATIO = time2;
			RequestBuyBuConfig.SALE_RATIO = ratio;
			RequestBuyBuConfig.BU_RANDOM = num;
		}else if ("refOneTime".equals(action)) {
			String index_str = request.getParameter("configIndex");
			int index = Integer.parseInt(index_str);
			RequestBuyBuConfig ccc = RequestBuyManager.getInstance().getBuConfigs().get(index);
			ccc.setLastHeatTime(ccc.getLastHeatTime() - 24 * 60 * 60 * 1000L);
		}
	}
%>

<body>
	<form name="f123">
		<input type='hidden' name='action' value='chanager_time'>
		多长时间未有人求购<input name="time1" value="<%=RequestBuyBuConfig.TIME_NOSALE %>">
		多长时间未达到比例<input name="time2" value="<%=RequestBuyBuConfig.TIME_SALERATIO %>">
		求购比例<input name="ratio" value="<%=RequestBuyBuConfig.SALE_RATIO %>">
		个数<input name="num" value="<%=RequestBuyBuConfig.BU_RANDOM %>">
		<input type='submit' value='确定'>
	</form>

	<form id='f1' name='f1' action="">
		<input type='hidden' name='action' value='add_newRequestBuyBu'>
		<select name="mainType" id="mainType" style="width:110px" >
		<%for(int i = 0; i < RequestBuyBuConfig.REQUEST_TYPES.length; i++){ %>
		<option id="<%=RequestBuyBuConfig.REQUEST_TYPES[i] %>"><%=RequestBuyBuConfig.REQUEST_TYPES[i] %></option>
		<% }%>
		</select>
		<select name="colorType" id="colorType" style="width:110px" >
		<%for (int i = 0; i < RequestBuyBuConfig.REQUEST_COLOR_TYPES.length; i++) { %>
		<option id="<%=RequestBuyBuConfig.REQUEST_COLOR_TYPES[i] %>"><%=RequestBuyBuConfig.REQUEST_COLOR_TYPES[i] %></option>
		<%} %>
		</select>
		底价<input type="text" name="money" id="money">
		出售频率(秒)<input type="text" name="saleTime" id="saleTime">
		总量<input type="text" name="maxNum" id="maxNum">
		
		<input type='submit' value='新  增'>
	</form>
	<table border="1">
		<tr>
			<td><%="type"%></td>
			<td><%="color"%></td>
			<td><%="money"%></td>
			<td><%="间隔(秒)"%></td>
			<td><%="总量"%></td>
			<td><%="已补个数"%></td>
			<td><%="回收金额"%></td>
			<td><%="求购ID"%></td>
			<td><%="修改"%></td>
			<td><%="删除"%></td>
		</tr>
		<%for (int i = 0; i < RequestBuyManager.getInstance().getBuConfigs().size(); i++){ 
			RequestBuyBuConfig ccc = RequestBuyManager.getInstance().getBuConfigs().get(i);
			if (RequestBuyManager.getInstance().getRemoveConfigs().contains(ccc)) {
				continue;
			}
			String xiugai = "xiugai" + i;
			String shanchu = "shanchu" + i;
			String price = "price" + i;
			String timeSpace = "timeSpace" + i;
			String maxNum = "maxNum" + i;
		%>
			<tr>
			<form action="">
				<td><%=RequestBuyBuConfig.REQUEST_TYPES[ccc.getRequestType()]%></td>
				<td><%=RequestBuyBuConfig.REQUEST_COLOR_TYPES[ccc.getColorType()]%></td>
				<td><input type='text' name=<%=price%> id=<%=price%> value=<%=ccc.getPrice()%>></td>
				<td><input type='text' name=<%=timeSpace%> id=<%=timeSpace%> value=<%=ccc.getTimeSpace()/1000%>></td>
				<td><input type='text' name=<%=maxNum%> id=<%=maxNum%> value=<%=ccc.getMaxNum()%>></td>
				<td><%=ccc.getBuNum()%></td>
				<td><%=ccc.buAllMoney%></td>
				<td><%=ccc.getRequestID()%></td>
				<td><input type='hidden' name='action' value=<%=xiugai %>><input type='submit' value='修  改'></td>
			</form>
				<td><form action=""><input type='hidden' name='action' value=<%=shanchu %>><input type='submit' value='删  除'></form></td>
			</tr>
			
		<%} %>
	</table>
	
	<br>
	<br>
	求购补货情况
	<br>
	<%
		for (int i = 0; i < RequestBuyManager.getInstance().getBuConfigs().size(); i++){ 
			RequestBuyBuConfig ccc = RequestBuyManager.getInstance().getBuConfigs().get(i);
	%>
	<%=RequestBuyBuConfig.REQUEST_TYPES[ccc.getRequestType()] %>
		<table border="1">
			<tr>
				<td><%="时间"%></td>
				<td><%="情况"%></td>
			</tr>
			<%
				for (String key : ccc.buMsg.keySet()) {
			%>
			<tr>
				<td><%=key%></td>
				<td><%=ccc.buMsg.get(key)%></td>
			</tr>
			<%
				}
			%>
		</table>
	<% 
		}
	%>
	<br>
	这个是把当前的求购补货情况设置成昨天的
	<br>
	<form id='f3'>
		<input type='hidden' name='action' value='refOneTime'>
		补求购Index<input name='configIndex'>
		<input type='submit' value="确定">
	</form>
	<br>
	<form id='f2' name='f2' action="">
	<input type='hidden' name='action' value='open_close'>
	<% String open_closeS = RequestBuyManager.getInstance().isOpen?"关闭补求购":"开启补求购"; %>
	<input type='submit' value=<%=open_closeS%>>
	</form>
	
</body>
</html>