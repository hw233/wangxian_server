<%@page import="java.util.Date"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.gamegateway.announce.MessageEntityManager"%>
<%@page import="com.fy.gamegateway.announce.MessageContent"%>
<%@page import="com.fy.gamegateway.announce.MessageEntity"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.gamegateway.mieshi.server.HeFuTipConfigInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<table>
<tr>
	<td>类型</td>
	<td>渠道类型</td>
	<td>开始时间</td>
	<td>结束时间</td>
	<td>操作</td>
</tr>
<% 
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	String SERVERTYPE_NAMES[] = MieshiServerInfo.SERVERTYPE_NAMES;
	MessageEntityManager entityManager = MessageEntityManager.getInstance();
	String TYPE_NAMES[] = MessageEntity.TYPE_NAMES;
	List<MessageEntity> lst = entityManager.queryForWhere("",new Object[0]);
	Iterator<MessageEntity> it = lst.iterator();
	while(it.hasNext())
	{
		MessageEntity messageEntity = it.next();
		String beginTime = DateUtil.formatDate(new Date(messageEntity.getStarttime()), "yyyy-MM-dd HH:mm:ss");
		String endTime = DateUtil.formatDate(new Date(messageEntity.getEndtime()), "yyyy-MM-dd HH:mm:ss");
%>
	<tr>
		<td><%=SERVERTYPE_NAMES[messageEntity.getServerType()] %></td>
		<td>
		<%=TYPE_NAMES[messageEntity.getMesType()]%>
		</td>
		<td><%=beginTime %></td>
		<td><%=endTime %></td>
		<td><a href="#">查看/编辑</a></td>
	</tr>
<%
	}
%>
</table>

<%
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	String paraBeginTime = null;
	String paraEndTime = null;
	int serverType = -1;
	String servers = null;
	String content = null;
	
	if(isDo)
	{
		MessageEntity messageEntity = new MessageEntity();
		int mestype = ParamUtils.getIntParameter(request, "mestype", 1);
		
		paraBeginTime = ParamUtils.getParameter(request, "begintime");
		paraEndTime = ParamUtils.getParameter(request, "endtime");
		
		if(StringUtils.isEmpty(paraBeginTime))
		{
			out.print("<script>alert(\"请输入开始时间\");history.go(-1);</script>");
			return;
		}
		
		if(StringUtils.isEmpty(paraEndTime))
		{
			out.print("<script>alert(\"请输入结束时间\");history.go(-1);</script>");
			return;
		}
		
		content = ParamUtils.getParameter(request, "content");
		if(StringUtils.isEmpty(content))
		{
			out.print("<script>alert(\"请输入提示内容\");history.go(-1);</script>");
			return;
		}
		
		long begintime = DateUtil.parseDate(paraBeginTime, "yyyy-MM-dd HH:mm:ss").getTime();
		long endtime = DateUtil.parseDate(paraEndTime, "yyyy-MM-dd HH:mm:ss").getTime();
		serverType = ParamUtils.getIntParameter(request, "serverType", -1);
		MessageContent messageContent = new MessageContent();
		messageContent.setContent(content);
		
		messageEntity.setCreatetime(System.currentTimeMillis());
		messageEntity.setUpdatetime(messageEntity.getCreatetime());
		messageEntity.setStarttime(begintime);
		messageEntity.setEndtime(endtime);
		messageEntity.setMesType(mestype);
		messageEntity.setServerType(serverType);
		messageEntity.setMessageContent(messageContent);
		
		entityManager.saveOrUpdate(messageEntity);
		
		
	}



%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>合服提示</title>
</head>
<body>
<form method="post">
	<input type="hidden" name="isDo" value="true" /><br/>
	<select name="mestype">
		<option value="1">公告</option>
		<option value="2">合服提示</option>
	</select>
	提示开始时间段:<input type="text" name="begintime" value=<%=paraBeginTime %> /> -结束时间段:<input type="text" name="endtime" value=<%=paraEndTime %> />[时间格式:2013-xx-xx 00:00:00]<br/>
	指定内容:<textarea rows="20" cols="30" name="content" value="<%=content%>"></textarea><br/>
	渠道类型(同服务器类型):<input type="text" name="serverType" value="<%=serverType %>"/><br/>
	(<%for(int i = 0; i < SERVERTYPE_NAMES.length; i++)
		{
			out.print(i+":"+SERVERTYPE_NAMES[i]+"&nbsp;");
		}%>	)
	<br/><input type="submit" value="提交">
</form>
</body>
</html>