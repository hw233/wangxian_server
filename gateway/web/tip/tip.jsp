<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%><%
	String title = request.getParameter("title");
	String descriptionInUUB = request.getParameter("descriptionInUUB");
	String widthStr = request.getParameter("width");
	String heightStr = request.getParameter("height");
	String btnss = request.getParameter("btns");
	String oTypess = request.getParameter("oType");
	String tipOpenStr = request.getParameter("tipOpen");
	if(title != null && !title.trim().equals("") && descriptionInUUB != null && !descriptionInUUB.trim().equals("") && widthStr != null && heightStr != null && btnss != null && !btnss.trim().equals("") && oTypess != null && !oTypess.trim().equals("") && tipOpenStr != null){
		try{
			int width = Integer.parseInt(widthStr);
			int height = Integer.parseInt(heightStr);
			String[] btns = btnss.split("，");
			if(btns.length == 1){
				btns = btnss.split(",");
			}
			String[] oTypes = oTypess.split("，");
			if(oTypes.length == 1){
				oTypes = oTypess.split(",");
			}
			byte[] oType = new byte[oTypes.length];
			for(int i = 0; i < oTypes.length; i++){
				oType[i] = Byte.parseByte(oTypes[i]);
			}
			boolean tipOpen = (tipOpenStr.trim().equals("true")?true:false);
			TipManager.title = title;
			TipManager.descriptionInUUB = descriptionInUUB;
			TipManager.width = width;
			TipManager.height = height;
			TipManager.btns = btns;
			TipManager.oType = oType;
			TipManager.tipOpen = tipOpen;
		}catch(Exception ex){
			out.println("出现异常，请按照格式填写");
			ex.printStackTrace();
		}
	}
	
%><html>
<head>
</head>
<body>
<center>
<h2></h2><br><a href="./tip.jsp">刷新此页面</a><br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>title</td>
	<td>详细描述</td>
	<td>宽度(可以不填)</td><td>高度(可以不填)</td>
	<td>按钮名称(多个按钮中间用，分隔)</td><td>按钮类型(需要和按钮对应，0表示关闭，1表示退出游戏)</td>
	<td>tip状态(true开启，false关闭)</td>
<%
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			out.println("<td>"+TipManager.title+"</td>");
			out.println("<td>"+TipManager.descriptionInUUB+"</td>");
			out.println("<td>"+TipManager.width+"</td>");
			out.println("<td>"+TipManager.height+"</td>");
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < TipManager.btns.length; i++){
				if(i == TipManager.btns.length - 1){
					sb.append(TipManager.btns[i]);
				}else{
					sb.append(TipManager.btns[i]+"，");
				}
			}
			out.println("<td>"+sb.toString()+"</td>");
			
			StringBuffer sb1 = new StringBuffer();
			for(int i = 0; i < TipManager.oType.length; i++){
				if(i == TipManager.oType.length - 1){
					sb1.append(TipManager.oType[i]);
				}else{
					sb1.append(TipManager.oType[i]+"，");
				}
			}
			out.println("<td>"+sb1.toString()+"</td>");
			out.println("<td>"+TipManager.tipOpen+"</td>");
			out.println("</tr>");

%>	
</table><br>
<br/>
<form name="f1">
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>title</td>
	<td>详细描述</td>
	<td>宽度(可以不填)</td><td>高度(可以不填)</td>
	<td>按钮名称(多个按钮中间用，分隔)</td><td>按钮类型(需要和按钮对应，0表示关闭，1表示退出游戏，用，分隔)</td>
	<td>tip状态(true开启，false关闭)</td>
<%

			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			out.println("<td><input type='text' name='title' value='"+TipManager.title+"'></td>");
			out.println("<td><textarea name='descriptionInUUB' width='200' height='200'>"+TipManager.descriptionInUUB+"</textarea></td>");
			out.println("<td><input type='text' name='width' value='"+TipManager.width+"'></td>");
			out.println("<td><input type='text' name='height' value='"+TipManager.height+"'></td>");
			out.println("<td><input type='text' name='btns' value='"+sb.toString()+"'></td>");
			out.println("<td><input type='text' name='oType' value='"+sb1.toString()+"'></td>");
			out.println("<td><input type='text' name='tipOpen' value='"+TipManager.tipOpen+"'></td>");
			out.println("</tr>");
out.println("<tr bgcolor='#00FFFF'><td colspan='7'><input type='submit' value='修改提示信息'></td></tr>");
%>	
</table>
</form><br>
<br/>
</center>
</body>
</html> 
