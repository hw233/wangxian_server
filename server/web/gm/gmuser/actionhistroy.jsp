<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title> 操作记录 </title>
		<link rel="stylesheet" href="../style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<%
			try {
			
				String gmusername = session.getAttribute("username").toString();
				ActionManager amanager = ActionManager.getInstance();
				List<String> sss = amanager.getSrcdate();
				int start = sss.size() - 1;
				if (request.getParameter("start") != null)
					start = Integer.parseInt(request.getParameter("start"));
				out
					.print("<input type='button' value='刷新' onclick='window.location.replace(\"actionhistroy.jsp?start="+start+"\")' /><br/>");
				String td = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
				int size = 8;
				
				if (start >= 0) {
					out.print("<a href ='?start=" + (sss.size() - 1)
							+ "' alt='首页' >首页</a>&nbsp;&nbsp;");
					if (start < sss.size() - 1)
						out.print("<a href='?start=" + (start + 1) + "' alt='"
								+ sss.get(start + 1) + "' />"
								+ sss.get(start + 1) + "</a>&nbsp;&nbsp;");
					if (start - 8 >= 0) {
						for (int i = start; i > start - 8; i--) {
							String date = sss.get(i);
							out.print("<a href='?start=" + i + "' alt='" + date
									+ "' />" + date + "</a>&nbsp;&nbsp;");
						}
					}
					if (start - 8 < 0) {
						for (int i = start; i > -1; i--) {
							String date = sss.get(i);
							out.print("<a href='?start=" + i + "' alt='" + date
									+ "' />" + date + "</a>&nbsp;&nbsp;");
						}
					}
					if (start - 8 > 0)
						out.print("<a href='?start=" + (start - 8) + "' alt='"
								+ sss.get(start - 8) + "' />"
								+ sss.get(start - 8) + "</a>&nbsp;&nbsp;");
					if (start >= 0)
						out.print("<a href ='?start=" + 0
								+ "' alt='末页' >末页</a>");
				}
				
				List<Action> acs  = new ArrayList<Action>(); 
				if(start!=-1)
				acs = amanager.getActions(sss.get(start));
				String gname = request.getParameter("gname");
				if(gname!=null&&gname.trim().length()>0){
				   List<Action> acs1 = new ArrayList<Action>();
				   for(Action a:acs){
				     if(a.getGmname().equals(gname.trim()))
				       acs1.add(a);
				   }
				   acs = acs1;
				}
                out.print("消息的总长度为" + acs.size());
				out
						.print("<table width='80%' ><caption>操作日志<caption><tr><th width=20% >gm名称</th><th width=80% >操作</th></tr>");
				
				for (int i=acs.size()-1;i>=0;i--) {
				    Action a = acs.get(i);
					out.print("<tr><td><a href='?gname="+a.getGmname()+"&start="+start+"' >" + a.getGmname() + "</a></td><td>" + a.getAction() + "</td></tr>");
				}
			} catch (Exception e) {
			 //e.printStackTrace();
			out.print(StringUtil.getStackTrace(e));
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("visitfobiden.jsp");
				//rdp.forward(request, response);
			}
		%>
	</body>
