<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.boss.client.BossClientService"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String uname = request.getParameter("uname");
	if (uname != null && !"".equals(uname.trim())) {
		//String ip = request.getParameter("ip");
		String [] names = uname.split("\r\n");
		List<String> nicknames = new ArrayList<String>();
		for (String name : names) {
			Passport p = BossClientService.getInstance().getPassportByUserName(name);
			if(p != null)
			{
				if(p.getNickName() != null && p.getNickName().length() > 0 && !p.getNickName().equals(name))
				{
					nicknames.add(p.getNickName());
					out.println("添加昵称:"+p.getNickName()+"<BR/>");
				}
				if(p.getUserName() != null && p.getUserName().length() > 0 && !p.getUserName().equals(name))
				{
					nicknames.add(p.getUserName());
					
				}
			}
			out.print(name + "<BR/>");
		}
		
		
		EnterLimitManager.getInstance().putTolimit("", names);
		EnterLimitManager.getInstance().putTolimit("", nicknames.toArray(new String[0]));
		out.print("F---OK:" + uname + "/" + uname.length());
	} else {
		uname = "";
	}

	String unameT = request.getParameter("unameT");
	if (unameT != null && !"".equals(unameT.trim())) {
		EnterLimitManager.getInstance().offLine(new String[] { unameT });
		Passport p = BossClientService.getInstance().getPassportByUserName(unameT);
		if(p != null)
		{
			if(p.getNickName() != null && p.getNickName().length() > 0 && !p.getNickName().equals(unameT))
			{
				EnterLimitManager.getInstance().offLine(new String[] { p.getNickName() });
			}
			
			if(p.getUserName() != null && p.getUserName().length() > 0 && !p.getUserName().equals(unameT))
			{
				EnterLimitManager.getInstance().offLine(new String[] { p.getUserName() });
			}
		}
		
		out.print("T---OK:" + unameT);
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="" method="post">
		要封的用户名字:
		<textarea rows="10" cols="15" name="uname"><%=uname %></textarea>
		<input type="submit" value="疯疯疯">
	</form>
	<BR />
	<hr>
	<BR />
	<form action="" method="post">
		要T的用户名字:<input type="text" name="unameT"> <input type="submit"
			value="TTT">
	</form>
</body>
</html>