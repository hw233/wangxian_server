<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>操作记录 </title>
		<link rel="stylesheet" href="../style.css" />
		<script type="text/javascript">
	      function $(tag){
	        return document.getElementById(tag);
	      }
	      function query(){
	        var f1 = $("f1");
	        var mmid = $("mmid").value;
	        var pname = $("pname").value;
	        var gmname = $("gmname").value;
	        var pid = $("pid").value;
	        if(!mmid&&!pname&&!gmname&&!pid){
	          alert("请检查输入信息！！");
	        }else{
	         f1.submit();
	        }
	      }
	      function stat(){
	        var gmserve = $("gmserve").value;
	        if(gmserve){
	          $("f2").submit();
	        }
	      }
        </script>
	</head>
	<body>
		<%
			try {
				out
						.print("<input type='button' value='刷新' onclick='window.location.replace(\"mail_record.jsp\")' />");
				String gmusername = session.getAttribute("username").toString();
				MailRecordManager mrmanager = MailRecordManager.getInstance();
				List<String> sss = mrmanager.getSrcdate();
				int start = sss.size() - 1;
				if (request.getParameter("start") != null)
					start = Integer.parseInt(request.getParameter("start"));
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
				String gmserve = request.getParameter("gmserve");
                out.print("<form action='' method='post' id='f1' >");
                out.print("<table align='center' width='60%' >");
                out.print("<tr><th>GM名称</th><td class='top'> ");
                out.print("<input type='hidden' id='start' name='start' value='"+start+"' /><input type='text' name='gmname' value='' id='gmname' />");
                out.print("</td></tr>");
                 out.print("<tr><th>邮件ID</th><td > ");
                out.print("<input type='text' name='mmid' value='' id='mmid' />");
                out.print("</td></tr>");
                out.print("<tr><th>角色名称</th><td > ");
                out.print("<input type='text' name='pname' value='' id='pname' />");
                out.print("</td></tr>");
                out.print("<tr><th>角色ID</th><td > ");
                out.print("<input type='text' name='pid' value='' id='pid' />");
                out.print("</td></tr>");
                out.print("<tr><td colspan='2' ><input type='button' value='查询' onclick='query();' /></td></tr></table></form>");
               
                out.print("<br/><form id='f2' method='post' action='mail_record.jsp'>统计gm:<input type='text' value='' name='gmserve' id='gmserve' />");
                out.print("<input type='hidden' value='"+start+"' name='start'");
                out.print("选择天数：<select id='cmails' name='cmails'  multiple=true size='5' > >");
                int ii=0;
                for(int j=sss.size();j>0;j--){
                  String ss = sss.get(j-1);
                  out.print("<option value='"+ss+"' "+(ii==0?"selected":"")+">"+ss+"</option>");
                  ii++;
                }
                out.print("</selected>");
                out.print("<input type='button' value='统计' onclick='stat();' /><br/></form>");
                
				List<MailRecord> mrs = new ArrayList<MailRecord>();
				if (start != -1)
					mrs = mrmanager.getActions(sss.get(start));
				String gmname = request.getParameter("gmname");
				if (gmname != null&&gmname.trim().length()>0) {
				    gmname = gmname.trim();
					List<MailRecord> mrs1 = new ArrayList<MailRecord>();
					for (MailRecord mr : mrs) {
						if (gmname.equals(mr.getGmname()))
							mrs1.add(mr);
					}
					mrs = mrs1;
				}
				String mid = request.getParameter("mmid");
				if (mid != null&&mid.trim().length()>0) {
				    mid = mid.trim();
					List<MailRecord> mrs2 = new ArrayList<MailRecord>();
					for (MailRecord mr : mrs) {
						if (mid.equals(mr.getMid()))
							mrs2.add(mr);
					}
					mrs = mrs2;

				}
				String pname = request.getParameter("pname");
				if (pname != null&&pname.trim().length()>0) {
				    pname = pname.trim();
					List<MailRecord> mrs3 = new ArrayList<MailRecord>();
					for (MailRecord mr : mrs) {
						if (mr.getPlayername().equals(pname))
							mrs3.add(mr);
					}
					mrs = mrs3;

				}
				String pid = request.getParameter("pid");
				if (pid != null&&pid.trim().length()>0) {
				    pid = pid.trim();
					List<MailRecord> mrs3 = new ArrayList<MailRecord>();
					for (MailRecord mr : mrs) {
						if (pid.trim().equals(mr.getPlayerid()+""))
							mrs3.add(mr);
					}
					mrs = mrs3;

				}
				out.print("消息的总长度为" + mrs.size());
				out
						.print("<table width='80%' ><caption>邮件回复日志<caption><tr><th  >gm名称</th><th>邮件ID</th><th>邮件标题</th><th>邮件内容</th><th>回复内容</th><th>回复礼品</th><th>账号名</th><th>角色名</th><th>发送时间</th><th>回复时间</th><th>时间差</th></tr>");
                Set<String> gmserves = null;
                Set<String> gmserves1 = null;
                long 回复数 = 0;
                if(gmserve!=null){
                  gmserves = new HashSet<String>();
                  gmserves1 = new HashSet<String>();
                  String[] ses = request.getParameterValues("cmails");
                  for(String se:ses){
                     List<MailRecord> mrs1 = mrmanager.getActions(se);
                     for(MailRecord mr1 :mrs1){
                        if(mr1.getGmname().equals(gmserve.trim())){
                          gmserves1.add(mr1.getUsername());
                      	    回复数++;
                        }
                     }
                  }
                }
                
				for (int i = mrs.size() - 1; i >= 0; i--) {
					MailRecord mr = mrs.get(i);
					if(gmserves!=null&&mr.getGmname().equals(gmserve.trim())){
					  gmserves.add(mr.getUsername());
					}
					out.print("<tr ");
					if(mr.getRescontent().contains("接受"))
					  out.print(" style='color:purple' ");
					out.print("><td><a href ='?start=" + start + "&gmname="
							+ mr.getGmname() + "' >" + mr.getGmname()
							+ "</a></td><td><a href='?start=" + start
							+ "&mmid=" + mr.getMid() + "' >" + mr.getMid()
							+ "</a></td><td>" + mr.getTitle() + "</td>");
					long fdate = DateUtil.parseDate(mr.getCdate(),
							"yyyy-MM-dd HH:mm:ss").getTime();
					long sdate = DateUtil.parseDate(
							sss.get(start) + mr.getResdate(),
							"yyyy-MM-ddHH:mm:ss").getTime();
					out.print("<td>" + mr.getQcontent() + "</td><td ");
					if(mr.getRescontent().contains("接受"))
					  out.print(" style='color:purple' ");
					out.print(">"
							+ mr.getRescontent() + "</td><td>" + mr.getItems()
							+ "</td><td>" + mr.getUsername()
							+ "</td><td><a href='?start=" + start + "&pid="
							+ mr.getPlayerid() + "' >" + mr.getPlayername()+"("
							+mr.getPlayerid()+ ")</a></td><td>" + mr.getCdate() + "</td><td>"
							+ mr.getResdate() + "</td><td>" + (sdate - fdate) / 60000
							+ "分钟</td></tr>");
				}
				if(gmserves!=null){
				   out.print("<h3 align='center' ><font color='red'>gm："+gmserve+"当天回复玩家的用户数为："+gmserves.size()+"</font></h3>");
				   out.print("<h3 align='center' ><font color='red'>gm："+gmserve+"所有回复玩家的用户数为："+gmserves1.size()+"</font></h3>");
				   out.print("<h3 align='center' ><font color='red'>gm："+gmserve+"所有回复玩家的邮件数为："+回复数+"</font></h3>");
				}
			} catch (Exception e) {
			  out.print(StringUtil.getStackTrace(e));
				//e.printStackTrace();
				//out.print(e.getMessage());
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("visitfobiden.jsp");
				//rdp.forward(request, response);
			}
		%>
	</body>
