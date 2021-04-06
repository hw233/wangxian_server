<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfoManager"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.*"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>把Cache中的玩家角色数据导入到数据库中</h1>	
<a href='./test_playerdata_from_cache_to_database.jsp'>刷新</a>
	<%
		MieshiPlayerInfoManager pm = MieshiPlayerInfoManager.getInstance();
		if("chaxun".equals(request.getParameter("action"))){
			String passwd = request.getParameter("passwd");
			if(passwd.equals("wangxian123")){
				if(pm.thread == null){
					pm.runForDataFromCacheToDataBase();
				}else{
					pm.runningForDataFromCacheToDataBase = false;
				}
			}
		} 
		
	%>
	<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000">
		<tr bgcolor="#00FFFF" align="center">
		</tr><tr><td align='right'  bgcolor="#00FFFF">是否正在导入</td><td  bgcolor="#FFFFFF"><%= pm.thread==null?"没有导入":"正在导入" %></td>
		</tr><tr><td align='right'  bgcolor="#00FFFF">开始时间</td><td  bgcolor="#FFFFFF"><%= DateUtil.formatDate(new Date(pm.runStartTime),"yyyy-MM-dd HH:mm:ss") %></td>
		</tr><tr><td align='right'  bgcolor="#00FFFF">已耗时</td><td  bgcolor="#FFFFFF"><%= pm.totalCostTime/1000 %>秒</td>
		</tr><tr><td align='right'  bgcolor="#00FFFF">总帐号数量</td><td  bgcolor="#FFFFFF"><%= pm.totalUserNum %></td>
		
		</tr><tr><td align='right'  bgcolor="#00FFFF">已处理帐号数量</td><td  bgcolor="#FFFFFF"><%= pm.currentUserNum %></td>
		</tr><tr><td align='right'  bgcolor="#00FFFF">导入角色成功数量</td><td  bgcolor="#FFFFFF"><%= pm.handledPlayerNum %></td>
		</tr><tr><td align='right'  bgcolor="#00FFFF">读取帐号出错数量</td><td  bgcolor="#FFFFFF"><%= pm.errorUserNum %></td>
		</tr><tr><td align='right'  bgcolor="#00FFFF">导入角色出错数量</td><td  bgcolor="#FFFFFF"><%= pm.errorPlayerNum %></td>
		</tr><tr><td align='right'  bgcolor="#00FFFF">导入间隔</td><td  bgcolor="#FFFFFF"><%= pm.sleepStep %></td>
		</tr><tr><td align='right'  bgcolor="#00FFFF">状态描述</td><td  bgcolor="#FFFFFF"><%= pm.errorMessage %></td>
		</tr>
	</table>
	<form>
		<input type="hidden" name="action" value="chaxun">
		<input name="passwd" type="text">
		<input type="submit" value="<%= pm.thread == null ?"开始导入":"停止导入" %>">
	</form>
	
	<br>
</body>
</html>
