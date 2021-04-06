<%@page contentType="text/html;charset=utf-8" import="com.fy.engineserver.operating.*,com.xuanzhi.gameresource.*,com.fy.engineserver.shop.*,com.fy.engineserver.sprite.*,com.fy.engineserver.gang.model.*"%>

<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page import="com.fy.engineserver.shop.*"%>
<%@page import="com.fy.engineserver.datasource.repute.ReputeManager"%>
<%@page import="com.fy.engineserver.datasource.repute.ReputeDefine"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="com.fy.engineserver.gang.model.*"%>
<%@page import="com.fy.engineserver.gang.service.GangManager"%>
<%@page import="com.fy.engineserver.operating.activities.GangChargeHistoryManager"%><%@include file="../IPManager.jsp" %><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		
		<script language="JavaScript">
			function search_by_gang_name(){
				var subtype = document.getElementById("subtype");
				subtype.value = 1;
				document.gang_credit_form.submit();
			}

			function search_charge_record(){
				var subtype = document.getElementById("subtype");
				subtype.value = 2;
				document.gang_credit_form.submit();
			}

			function charge_gang_credits(){
				var subtype = document.getElementById("subtype");
				subtype.value = 3;
				document.gang_credit_form.submit();
			}
		</script>
	</head>
	<body>
		<%
			
			GangManager gm = GangManager.getInstance();
		
			PlayerManager pm = PlayerManager.getInstance();
			String pageStr = request.getParameter("page");
			
			int numRecordPerPage = 20;
			
			List<Gang> l = new ArrayList<Gang>();
			if(pageStr == null){
				String subType = request.getParameter("subtype");
				if(subType != null && subType.equals("1")){
				}else if(subType != null && subType.equals("2")){
				}else if(subType != null && subType.equals("3")){
					String gangName = request.getParameter("gang_name3");
					String credit = request.getParameter("gang_credit_points");
					int gangCredit = 0;
					boolean ok = true;
					
					int gangId = -1;
					
					try{
						gangId = Integer.parseInt(gangName);						
					}catch(Exception e){
						ok = false;
					}
					
					
					if(gangName == null || credit == null){
						ok = false;
					}else{
						try{
							gangCredit = Integer.parseInt(credit);
						}catch(Exception e){
							ok = false;
						}
					}
					
					if(ok){
						Gang g = gm.getGang(gangId);
						if(g == null){
							out.println("id为 "+gangName+" 的帮会不存在！");
						}else{
							long ov = g.getGangPoint();
							g.setGangPoint(ov + gangCredit);
							
							GangChargeHistoryManager cm = GangChargeHistoryManager.getInstance();
							cm.addGangChargeRecord(g.getId() , String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS",new Date()) , gangCredit);
							
							out.println("帮会["+gangName+"]的帮会积分从 "+ov+" 修改为 "+g.getGangPoint()+"。");						
						}
					}else{
						out.println("输入非法，帮会名称:"+gangName+"，积分点数:"+credit);
					}
				}
				
				l.addAll(gm.getGangs(0,numRecordPerPage));
			}else{
				int pageId = Integer.parseInt(pageStr);
				l.addAll(gm.getGangs(pageId,numRecordPerPage));
			}
		%>
		<form action="" name="gang_credit_form" method="post">
			帮会id:<input type="text" name="gang_name3" ></input>
			帮会积分:<input type="text" name="gang_credit_points" ></input>
			<input type="button" name="adjust_gang_credit" value="调整帮会积分" onclick="javascript:charge_gang_credits()"></br>
			
			<input type="hidden" id="subtype" name="subtype" value="" />
		</form>
		
		<%
			int count = gm.getCount();
			int numPages = (count + numRecordPerPage - 1) / numRecordPerPage;
			for(int i = 0 ; i < numPages ; i++){
				out.println("<b><a href='gang_credit.jsp?page="+(i * numRecordPerPage)+"'>"+(i+1)+"</a></b>");
			}
		%>
		
		<table border="1">
		<tr>
			<td width="10%">帮会id</td>
			<td width="10%">帮会名称</td>
			<td width="10%">级别</td>
			<td width="10%">创建日期</td>
			<td width="10%">创建人</td>
			<td width="10%">帮主</td>
			<td width="10%">帮会积分</td>
		</tr>
		<%
			for(Gang g : l){
				//g = gm.getGang(g.getId());
				out.println("<tr>");
				out.println("<td>"+g.getId()+"</td>");
				out.println("<td>"+g.getName()+"</td>");
				out.println("<td>"+g.getGanglevel()+"</td>");
				out.println("<td>"+g.getCreatedate().toString()+"</td>");
				
				Player p = pm.getPlayer(g.getCreator().intValue());
				out.println("<td>"+(p == null ? "无法获得玩家对象" : p.getName() )+"("+g.getCreator()+")</td>");
				
				p = pm.getPlayer(g.getBangzhu().intValue());
				out.println("<td>"+(p == null ? "无法获得玩家对象" : p.getName())+"("+g.getBangzhu()+")</td>");
				out.println("<td>"+g.getGangPoint()+"</td>");
				out.println("</tr>");	
			}
		%>
		</table>
		
	</body>
</html>
