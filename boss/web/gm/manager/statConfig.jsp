<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.gmuser.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/style.css" >
<title>统计数据表更新配置</title>
</head>
<body bgcolor="#c8edcc">
	<h2>统计数据表更新配置</h2>
	<form>
		<table>
			<tr><th>表名：</th><td><input type='text' name='tablename'/></td></tr>
			<tr><th>库类型：</th><td><select name = 'datatype'><option>--</option><option>QQ</option></select></td></tr>
			<tr><th>时间范围：</th><td><input type='text' name='timelimit' value='2011-11-11#2012-12-12'/></td></tr>
			<%
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
				String now = sdf.format(new Date()); 
			%>
			<tr><th>添加时间：</th><td><input type='text' name='addtime' value='<%=now %>'/></td></tr>		
			<tr><td colspan="2"><input type="submit" value='提交'></td><tr>
		</table>	
	</form>
	
	<%
		String tablename = request.getParameter("tablename");
		String datatype = request.getParameter("datatype");
		String timelimit = request.getParameter("timelimit");
		String addtime = request.getParameter("addtime");
		
		if(tablename!=null&&tablename.trim().length()>0&&datatype!=null&&datatype.trim().length()>0&&timelimit!=null&&timelimit.trim().length()>0){
			List<Stat_TableConfig> confs = ChargeStatRecord.getInstance().getAllConfigs();
			Stat_TableConfig st =  new Stat_TableConfig();
			st.setTableName(tablename);
			st.setDateType(datatype);
			st.setAddTime(addtime);
			st.setTimeLimits(timelimit);
			if(ChargeStatRecord.getInstance().addNewConfig(st)){
				confs.add(st);
				ChargeStatRecord.getInstance().logger.info("[通过页面添加记录] [成功] [表名： "+tablename+"] [库类型："+datatype+"] [添加时间："+addtime+"] [时间范围："+timelimit+"]");
				out.print("更新成功！！");
			}
			
			if(confs.size()>0){
				out.print("<table>");
				out.print("<tr><th>表名</th><th>库类型</th><th>表中内容时间范围</th><th>添加时间</th></tr>");
				for(Stat_TableConfig conf:confs){
		%>
					<tr><td><%=conf.getTableName()%></td><td><%=conf.getDateType()%></td><td><%=conf.getTimeLimits()%></td><td><%=conf.getAddTime()%></td></tr>
		<% 		
				}
				out.print("</table>");
			}
		}else{
			out.print("信息不完整！！");
		}
	%>	
</body>
</html>