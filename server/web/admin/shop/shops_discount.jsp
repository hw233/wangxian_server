<%@page contentType="text/html;charset=utf-8" import="com.fy.engineserver.operating.*,com.xuanzhi.gameresource.*,com.fy.engineserver.shop.*,com.fy.engineserver.sprite.*,com.fy.engineserver.gang.model.*"%>

<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page import="com.fy.engineserver.shop.*"%>
<%@page import="com.fy.engineserver.datasource.repute.ReputeManager"%>
<%@page import="com.fy.engineserver.datasource.repute.ReputeDefine"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="com.fy.engineserver.gang.model.*"%>
<%@page import="com.fy.engineserver.gang.service.GangManager"%><%@include file="../IPManager.jsp" %><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		<script language="javascript">
		</script>
	</head>
	<body>
		<b>声望打折</b></br>
		<table border="1">
		<tr>
			<td width="10%">声望名称</td>
			<td width="10%">级别</td>
			<td width="10%">折扣</td>
			<td width="70%">打折商店</td>
		</tr>
		<%
			DiscountManager dm = DiscountManager.getInstance();
		    ReputeDiscountSupplier rds =(ReputeDiscountSupplier)dm.getDiscountSupplier(DiscountSupplier.NAME_REPUTE);
			ReputeManager rm = ReputeManager.getInstance();
			String[] reputeNames = rds.getDiscountReputes();
			
			for(String reputeName : reputeNames){
				ReputeDiscountSupplier.ReputeDiscountRecord ddr = rds.getReputeDiscountRecord(reputeName);
				ReputeDefine rd = rm.getReputeDefine(reputeName);
				
				for(int i = 0; i < rd.getMaxLevel() ; i++){
					out.println("<tr>");
					if(i == 0){
						out.println("<td valign=\"center\" rowspan=\""+rd.getMaxLevel()+"\">"+rd.getName()+"</td>");
					}
	
					out.println("<td>"+(i+1)+"</td>");
					out.println("<td>"+ddr.getDiscount(i)+"</td>");
					
					if(i == 0){
						out.println("<td valign=\"center\" rowspan=\""+rd.getMaxLevel()+"\">"+Arrays.toString(rds.getShopNamesWithDiscountForRepute(reputeName))+"</td>");	
					}
					
					out.println("</tr>");
				}
			}
		%>
		
		</table>
		
		<table border="1">
		
		<b>帮派打折</b></br>
		<tr>
			<td width="10%">级别</td>
			<td width="10%">折扣</td>
			<td width="80%">打折商店</td>
		</tr>
		<%
		    GangLevelDiscountSupplier gds =(GangLevelDiscountSupplier)dm.getDiscountSupplier(DiscountSupplier.NAME_GANG);
			int[] ls = gds.getLevels();
			
			for(int i = 0 ;i < ls.length ; i++){
				out.println("<tr>");
				out.println("<td>"+(i+1)+"</td>");
				out.println("<td>"+gds.getDiscountForLevel(ls[i])+"</td>");
				
				if(i == 0){
					out.println("<td valign=\"center\" rowspan=\""+ls.length+"\">"+Arrays.toString(gds.getShopNamesWithDiscount())+"</td>");	
				}
				
				out.println("</tr>");				
			}
		%>
		
		</table>

		<b>家园打折</b></br>
		<table border="1">
		<tr>
			<td width="10%">级别</td>
			<td width="10%">折扣</td>
			<td width="80%">打折商店</td>
		</tr>
		<%
			HomeLandDiscountSupplier hds = (HomeLandDiscountSupplier)dm.getDiscountSupplier(DiscountSupplier.NAME_HOME_LAND);
		    ls = hds.getLevels();
		    for(int i = 0 ;i < ls.length ; i++){
		    	out.println("<tr>");
				out.println("<td>"+(i+1)+"</td>");
				out.println("<td>"+hds.getDiscountForLevel(ls[i])+"</td>");
				
				if(i == 0){
					out.println("<td valign=\"center\" rowspan=\""+ls.length+"\">"+Arrays.toString(hds.getShopNamesWithDiscount())+"</td>");	
				}
				
				out.println("</tr>");
		    }
		%>
		
		</table>
		
	</body>
</html>
