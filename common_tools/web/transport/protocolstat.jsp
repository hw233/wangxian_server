<%@ page contentType="text/html;charset=UTF-8" import="java.util.*,
com.xuanzhi.tools.text.*,com.xuanzhi.tools.servlet.*,org.w3c.dom.*,
com.xuanzhi.tools.transport2.*,java.nio.channels.*"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.net.InetSocketAddress"%>
<%@page import="java.io.PrintStream"%><%! 

public void mergemap(HashMap<String,long[]> map,HashMap<String,long[]> map2){
	if(map2 != null){
		Iterator<java.util.Map.Entry<String,long[]>> it = map2.entrySet().iterator();
		while(it.hasNext()){
			java.util.Map.Entry<String,long[]> entry = it.next();
			long data[] = map.get(entry.getKey());
			if(data == null){
				data = new long[2];
				map.put(entry.getKey(), data);
			}
			data[0] += entry.getValue()[0];
			data[1] += entry.getValue()[1];
		}
	}
}

public void outputMap(HashMap<String,long[]> map,JspWriter out) throws Exception{
	ArrayList<java.util.Map.Entry<String,long[]>> al = new ArrayList<java.util.Map.Entry<String,long[]>>();
	al.addAll(map.entrySet());
	java.util.Collections.sort(al,new Comparator<java.util.Map.Entry<String,long[]>>(){
		public int compare(java.util.Map.Entry<String,long[]> e1,java.util.Map.Entry<String,long[]> e2){
			if(e1.getValue()[1] < e2.getValue()[1]) return 1;
			if(e1.getValue()[1] > e2.getValue()[1]) return -1;
			if(e1.getValue()[0] < e2.getValue()[0]) return 1;
			if(e1.getValue()[0] > e2.getValue()[0]) return -1;
			return 0;
		}
	});
	long totalNum = 0;
	long totalLength = 0;
	long otherNum = 0;
	long otherLength = 0;
	int N = 100;
	for(int i = 0 ; i < al.size() ; i++){
		java.util.Map.Entry<String,long[]> e = al.get(i);
		totalNum += e.getValue()[0];
		totalLength += e.getValue()[1];
		if(i >= 100){
			otherNum += e.getValue()[0];
			otherLength += e.getValue()[1];
		}
	}
	
	for(int i = 0 ; i < al.size() && i < N ; i++){
		java.util.Map.Entry<String,long[]> e = al.get(i);
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td align='right'>"+e.getKey()+"</td>");
		out.println("<td>"+e.getValue()[0]+"</td>");
		
		if(totalNum == 0)
			out.println("<td>0.0%</td>");
		else
			out.println("<td>"+ String.format("%.1f",e.getValue()[0]*100.0f/totalNum)+"%</td>");
		
		out.println("<td>"+ StringUtil.addcommas(e.getValue()[1])+"</td>");
		
		if(totalLength == 0){
			out.println("<td>0.0%</td>");
		}else{
			out.println("<td>"+ String.format("%.1f",e.getValue()[1]*100.0f/totalLength)+"%</td>");
		}
		if(e.getValue()[0] == 0)
			out.println("<td></td>");
		else
			out.println("<td>"+ (e.getValue()[1]/e.getValue()[0]) +"</td>");
		
		out.println("</tr>");
	}
	if(otherNum > 0){
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td align='right'>其他协议</td>");
		out.println("<td>"+otherNum+"</td>");
		
		if(totalNum == 0)
			out.println("<td>0.0%</td>");
		else
			out.println("<td>"+ String.format("%.1f",otherNum*100.0f/totalNum)+"%</td>");
		
		out.println("<td>"+ StringUtil.addcommas(otherLength)+"</td>");
		
		if(totalLength == 0){
			out.println("<td>0.0%</td>");
		}else{
			out.println("<td>"+ String.format("%.1f",otherLength*100.0f/totalLength)+"%</td>");
		}

		out.println("<td>"+ (otherLength/otherNum) +"</td>");
		
		out.println("</tr>");
	}
	out.println("<tr bgcolor='#00FFFF' align='center'>");
	out.println("<td align='right'>汇总</td>");
	out.println("<td>"+totalNum+"</td>");
	
	if(totalNum == 0)
		out.println("<td>100%</td>");
	else
		out.println("<td>"+ String.format("%.1f",totalNum*100.0f/totalNum)+"%</td>");
	
	out.println("<td>"+ StringUtil.addcommas(otherLength)+"</td>");
	
	if(totalLength == 0){
		out.println("<td>100%</td>");
	}else{
		out.println("<td>"+ String.format("%.1f",totalLength*100.0f/totalLength)+"%</td>");
	}
	
	if(totalNum == 0)
		out.println("<td></td>");
	else
		out.println("<td>"+ (totalLength/totalNum) +"</td>");
	
	out.println("</tr>");
}
%><%

	
	
	String beanName = request.getParameter("name");
	
	DefaultConnectionSelector2 dcs = ConnectionSelectorHelper2.getSelector(beanName);
	ConnectionSelectorHelper2 helper = new ConnectionSelectorHelper2(dcs);
	
	Connection2 conn = null;
	String connName = request.getParameter("conn");
	
	Connection2 conns[] = new Connection2[0];
	try{
		 conns = helper.getAllConnections();
	}catch(Exception e){
		conns = new Connection2[0];
		out.println("<h2>出现访问冲突，请稍后重试！</h2>");
		return;
	}	
	
	
	if(connName != null){
		for(int i = 0 ; i < conns.length ; i++){
			if(conns[i].getName().equals(connName)){
				conn = conns[i];
			}
		}
	}

	
%><html>
<head>
</head>
<body>
<center>
<h2><%=dcs.getName()%><%= conn != null?("，链接"+conn.getName()):"" %>的流量详细情况</h2><br><a href="./conns2.jsp?name=<%=beanName%>">返回链接页面</a><br>
<br>
整体发送协议流量分布情况：<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>发送协议名称</td><td>数量</td><td>数量占比</td><td>数据大小</td><td>数据占比</td><td>单消息大小</td></tr>
<%
{	
	HashMap<String,long[]> map = helper.getSendMessageStatMap();
	if(map == null) map = new HashMap<String,long[]>();
	for(int i = 0 ; i < conns.length ; i++){
		mergemap(map,helper.getReceiveMessageStatMap(conns[i]));
	}
	outputMap(map,out);
}	
%>	
</table><br>

整体接收协议流量分布情况：<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>接收协议名称</td><td>数量</td><td>数量占比</td><td>数据大小</td><td>数据占比</td><td>单消息大小</td></tr>
<%
{	
	HashMap<String,long[]> map = helper.getReceiveMessageStatMap();
	if(map == null) map = new HashMap<String,long[]>();
	for(int i = 0 ; i < conns.length ; i++){
		mergemap(map,helper.getReceiveMessageStatMap(conns[i]));
	}
	outputMap(map,out);
}	
%>	
</table><br>
<%
	if(conn != null){
%>
<%= conn.getName() %>上发送协议流量分布情况：<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>发送协议名称</td><td>数量</td><td>数量占比</td><td>数据大小</td><td>数据占比</td><td>单消息大小</td></tr>
<%
{	
	HashMap<String,long[]> map = helper.getSendMessageStatMap(conn);
	if(map == null) map = new HashMap<String,long[]>();
	outputMap(map,out);
}	
%>	
</table><br>

<%= conn.getName() %>上接收协议流量分布情况：<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>接收协议名称</td><td>数量</td><td>数量占比</td><td>数据大小</td><td>数据占比</td><td>单消息大小</td></tr>
<%
{	
	HashMap<String,long[]> map = helper.getReceiveMessageStatMap(conn);
	if(map == null) map = new HashMap<String,long[]>();
	outputMap(map,out);
}	
%>	
</table><br>

<%
	} //end if(conn != null)
%>
</center>
</body>
</html> 
