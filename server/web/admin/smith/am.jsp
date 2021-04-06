<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.smith.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" href="css/style.css" />
	<link rel="stylesheet" href="css/atalk.css" />
	<script type="text/javascript">
	</script>
	</head>

	<body>
		<%     
		ArticleRelationShipManager msm = ArticleRelationShipManager.getInstance();
		boolean openning = msm.isOpenning();
		if(!openning) {
			String flag = request.getParameter("action");
			if(flag != null && flag.equals("open")) {
				msm.start();
				openning = msm.isOpenning();
			}
		} else {
			String flag = request.getParameter("action");
			if(flag != null && flag.equals("close")) {
				msm.stop();
				openning = msm.isOpenning();
			}
		}
		String maxrmb = request.getParameter("maxrmb");
		if(maxrmb != null && maxrmb.matches("\\d+")) {
			msm.set不采样充值RMB(Integer.valueOf(maxrmb));
		}
		String maxlevel = request.getParameter("maxlevel");
		if(maxlevel != null && maxlevel.matches("\\d+")) {
			msm.set不采样充值等级(Integer.valueOf(maxlevel));
		}
		List<ArticleRelationShip> ships = null;
		String downCountStr = request.getParameter("downCount");
		String layerCountStr = request.getParameter("layerCount");
		Integer mary = (Integer)session.getAttribute("mary");
		if(mary != null && session.getAttribute("ttt").equals("dc")) {
			ships = msm.getMinDownCountShips(mary); 
		} else if(mary != null && session.getAttribute("ttt").equals("lc")) {
			ships = msm.getMinLayerCountShips(mary);
		}
		if(downCountStr != null && downCountStr.matches("\\d+")) {
			ships = msm.getMinDownCountShips(Integer.valueOf(downCountStr)); 
			session.setAttribute("mary", Integer.valueOf(downCountStr));
			session.setAttribute("ttt", "dc");
		} else if(layerCountStr != null && layerCountStr.matches("\\d+")) {
			ships = msm.getMinLayerCountShips(Integer.valueOf(layerCountStr));
			session.setAttribute("mary", Integer.valueOf(layerCountStr));
			session.setAttribute("ttt", "lc");
		}
	    %>                                              
	    <h2 style="font-weight:bold;">          
	    	物品汇聚组织网络
	    </h2> 
		<center>
			<form action="am.jsp" name=f0 method=post>
				基础配置:当前状态<%=openning?"开启":"关闭" %>
				<%if(openning) {%>
				<input type=button name=sub1 value=' 关 闭 ' onclick="location.replace('am.jsp?action=close')">
				<%} else {%>
				<input type=button name=sub1 value=' 开 启 ' onclick="location.replace('am.jsp?action=open')">				
				<%} %>
				下线采样充值<=<input type=text name=maxrmb size=10 value="<%=msm.get不采样充值RMB() %>">分 ,
				下线采样等级<=<input type=text name=maxlevel size=10 value="<%=msm.get不采样充值等级() %>">
				<input type=submit name=sub3 value=" 修 改 ">
				<br>
			</form>
			<form action="am.jsp" name=f1 method=post>
				过滤器1:单层下线超过<input type=text name=downCount size=10>(最小为3)<input type=submit name=sub1 value="提取"><br>
			</form>
			<form action="am.jsp" name=f2 method=post>
				过滤器2:总层级超过<input type=text name=layerCount size=10>(最小为2)<input type=submit name=sub2 value="提取"><br>
			</form>
			<%if(ships != null && ships.size() > 0) {%>
			<table id="test1" align="center" width="70%" cellpadding="0"
				cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr>
					<th width=10%>
						<b>关系Id</b>
					</th>
					<th width=20%>
						<b>单层次最大下线</b>
					</th>
					<th width=20%>
						<b>最大层次</b>
					</th>
					<th width=20%>
						<b>累计汇集数量</b>
					</th>
					<th width=10%>
						<b>状态</b>
					</th>
					<th width=15%>
						<b>操作</b>
					</th>
				</tr>
				<%
				for(ArticleRelationShip ship : ships) {
				%>
				<tr>
					<td><%=ship.getId() %></td>
					<td><%=ship.getMaxDownCount() %></td>
					<td><%=ship.getMaxLayerCount() %></td>
					<td><%=ship.getBottomLevelTotalUp() %></td>
					<td><%=msm.isForbid(ship)?"<font color='red'><b>封禁</b></font>":"正常" %></td>
					<td><a href="am_detail.jsp?id=<%=ship.getId() %>">查看详情</a></td>
				</tr>
				<%} %>
			</table><br><br>
			<%} %>
			<center>
		<br>
		<br>
	</body>
</html>