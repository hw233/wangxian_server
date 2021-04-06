<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,com.fy.engineserver.shop.*,com.fy.engineserver.warehouse.service.*,
com.fy.engineserver.warehouse.*,com.fy.engineserver.compensation.*,java.io.*"%><%!

%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@include file="IPManager.jsp" %><html><head>
<%
CompensationManager cm = CompensationManager.getInstance();
File file = new File("//home/game/resin/webapps/game_server/WEB-INF/game_runtime_data/wanjia.txt");
BufferedReader buffr = new BufferedReader(new FileReader(file));
List<Integer> list = new ArrayList<Integer>();
String str = null;
final Hashtable<Integer,Integer> ht = new Hashtable<Integer,Integer>();
while((str = buffr.readLine()) != null){
	try{
	list.add(Integer.parseInt(str));
	}catch(Exception e){
		
	}
}
%>
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<script language="javascript">

</script>
<style>
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<table>
<tr class="titlecolor"><td>玩家id</td><td>补偿总元宝数</td></tr>
<%if(cm != null){
	int count = 0;
	for(Integer id : list){
		int yuanbao = cm.getCompensation(id);
		ht.put(id,yuanbao);
		count += yuanbao;
	}
	Integer[] idArray = list.toArray(new Integer[0]);
	Arrays.sort(idArray,new Comparator<Integer>(){
		public int compare(Integer i1,Integer i2){
			return ht.get(i2) - ht.get(i1);
		}
	});
	out.println("<tr><td colspan='2'>总补偿元宝数"+count+"</td></tr>");
	for(Integer id : idArray){
		out.println("<tr><td>"+id+"</td><td>"+cm.getCompensation(id)+"</td></tr>");
	}
	}
 %>
</table>
</BODY></html>
