<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">

<%@page import="com.fy.engineserver.notice.NoticeManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.notice.Notice"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置新的临时公告</title>
</head>
<body>

	<%
	
	String name = request.getParameter("name");
	if(name == null || name.equals("")){
		%>
		<form action="" method="post">
		<table cellspacing="1" cellpadding="2" border="1" >
			<tr>
				<td>公告名称:</td>
				<td><input type="text" name="name" /></td>
			</tr>
			<tr>
				<td>公告内容:</td>
				<td><textarea rows="3" cols="20" name="content"></textarea></td>
			</tr>
			<tr>
				<td>公告内容(内部看):</td>
				<td><textarea rows="3" cols="20" name="contentInner"></textarea></td>
			</tr>
			<tr>
				<td>公告开启时间(2011-01-11 00:00:00):</td>
				<td><input type="text" name="beginTime" /></td>
			</tr>
		</table>
		<input type="submit" value="提交" />
 	</form>
		<%
	}else{
		
		//保存
		String content = request.getParameter("content");
		String contentInner = request.getParameter("contentInner");
		String beginTimeSt = request.getParameter("beginTime");
		
		if(content == null || contentInner == null ||beginTimeSt == null){
			out.print("输入不正确，请重新输入");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			Date d1 =  sdf.parse(beginTimeSt);
			long id = 1l;
			try{
				id = NoticeManager.em.nextId();
			}catch(Exception e){
				out.print("生成id 错误");
				return ;
			}
			Notice n = new Notice(id);
			n.setEffectState(NoticeManager.开始);
			n.setNoticeName(name);
			n.setContent(content);
			//临时公告
			n.setNoticeType(0);
			n.setContentInside(contentInner);
			
			n.setBeginTime(d1.getTime());
			
			if(d1.getTime()<= System.currentTimeMillis()){
				out.print("输入日期错误");
				return;
			}
			NoticeManager.getInstance().createNotice(n);
			NoticeManager.logger.warn("[生成一个新的临时公告]");
			out.print("生成一个新临时公告成功");

		}catch(Exception e){
			out.print("日期输入格式错误，请重新输入");
		}
	}
	%>

</body>
</html>