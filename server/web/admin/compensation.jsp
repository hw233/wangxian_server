<%@page contentType="text/html;charset=utf-8" import="com.fy.engineserver.compensation.*,java.util.*"%>

<%@page import="com.fy.engineserver.core.CoreSubSystem"%><%@include file="IPManager.jsp" %><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		<%
			final CompensationManager cm = CompensationManager.getInstance();
		%>
		<script language="javascript">
		</script>
	</head>
	<body>
		<%
			int[] ids = cm.getPlayerIdsWithConsumeRecord();
		
			int total = 0;
			for(int i = 0 ;i < ids.length ;i ++){
				int id = ids[i];
				total += cm.getCompensation(id);
			}
			
			out.println("领取补偿人数 "+ids.length+" 人，一共补偿 "+total+" 元宝</br>");
		%>
	
		<table border="1">
		<tr>
			<td width="10%">玩家编号</td>
			<td width="70%">消费记录</td>
			<td width="10%">补偿金额</td>
			<td width="10%">是否领取过补偿</td>
		</tr>
		<%
			Integer[] ivs = new Integer[ids.length];
			for(int i = 0 ;i < ids.length ;i ++){
				int id = ids[i];
				ivs[i] = id;
			}
			
			Arrays.sort(ivs,new Comparator<Integer>(){
				public int compare(Integer i1 , Integer i2){
					int c1 = cm.getCompensation(i1);
					int c2 = cm.getCompensation(i2);
					if(c1 == c2){
						return 0;
					}else if(c1 < c2){
						return 1;
					}else {
						return -1;
					}
				}
			});
			
			ids = new int[ids.length];
			for(int i = 0 ;i < ids.length ;i ++){
				ids[i] = ivs[i];
			}
				
			
			for(int i = 0 ;i < ids.length ;i ++){
				int id = ids[i];
				
				out.println("<tr>");
				out.println("<td>"+id+"</td>");
				out.println("<td>"+Arrays.toString(cm.getConsumeRecords(id))+"</td>");
				out.println("<td>"+cm.getCompensation(id)+" 元宝</td>");
				out.println("<td>"+(cm.isPlayerCompensated(id) ? "已经领取" : "未领取")+"</td>");
				out.println("</tr>");
			}
			
			
		%>
		</table>
	</body>
</html>
