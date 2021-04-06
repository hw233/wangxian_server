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
<title>设置新的登陆公告</title>
</head>
<body>


	<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
				<%
					String begin = sdf.format(new Date(System.currentTimeMillis()));
				%>
				<td><input type="text" name="beginTime" value="<%=begin %>"/></td>
			</tr>
			<tr>
				<%
					String end = sdf.format(new Date(System.currentTimeMillis()+24*60*60*1000));
				%>
				<td>公告失效时间(2011-01-11 00:00:00):</td>
				<td><input type="text" name="endTime" value="<%=end %>"/></td>
			</tr>
			<tr>
				<td>是否有绑银:</td>
				<td>
				没有:<input type="radio" name="hava"  checked="checked"  value="0" />
				有:<input type="radio"name="hava" value="1" />
				</td>
			</tr>
			<tr>
				<td>绑银数量:</td>
				<td><input type="text" name="bindSivlerNum" /></td>
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
		String endTimeSt = request.getParameter("endTime");
		String havaSt = request.getParameter("hava");
		String bindSivlerNumSt = request.getParameter("bindSivlerNum");
		String qudao = request.getParameter("qudao");
		NoticeManager.logger.warn("[生成新公告] [beginTime:"+beginTimeSt+"] [endTime:"+endTimeSt+"] [qudao:"+qudao+"] [name:"+name+"]");
		if(content == null || contentInner == null ||beginTimeSt == null || endTimeSt ==null ){
			out.print("输入不正确，请重新输入");
		}
		
		try{
			Date d1 =  sdf.parse(beginTimeSt);
			Date d2 =  sdf.parse(endTimeSt);
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
			if(qudao!=null){
				n.setQudao(qudao);
			}else{
				n.setQudao("");
			}
			//登陆公告
			n.setNoticeType(1);
			n.setContentInside(contentInner);
			if(d1.getTime() >= d2.getTime()){
				out.print("日期输入错误");
				return ;
			}
			n.setBeginTime(d1.getTime());
			n.setEndTime(d2.getTime());
			if(havaSt.equals("0")){
				n.setHavaBindSivler(false);
				n.setBindSivlerNum(0);
				NoticeManager.getInstance().createNotice(n);
				NoticeManager.logger.warn("[生成一个新的登陆公告]");
				
				out.print("生成一个新登陆公告成功");
				
			}else{
				n.setHavaBindSivler(true);
				try{
				int num = Integer.parseInt(bindSivlerNumSt);
				n.setBindSivlerNum(num);
				NoticeManager.getInstance().createNotice(n);
				NoticeManager.logger.warn("[生成一个新的登陆公告]");
				
				out.print("生成一个新登陆公告成功");
				}catch(Exception e){
					out.print("绑银输入错误");
				}
			}
		}catch(Exception e){
			out.print("日期输入格式错误，请重新输入");
		}
	}
	%>


	
</body>
</html>