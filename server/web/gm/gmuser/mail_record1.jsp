<%@ page language="java" contentType="text/html; charset=utf-8"pageEncoding="utf-8"%>
<%@include file="../header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>邮件统计 </title>
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
				out.print("<input type='button' value='刷新' onclick='window.location.replace(\"mail_record1.jsp\")' />");
				MailRecordManager mrmanager = MailRecordManager.getInstance();
                out.print("<form action='' method='post' id='f1' >");
                out.print("<table align='center' width='40%' >");
                out.print("<tr><th>GM名称</th><td class='top'> <input type='text' name='gmname' value='' id='gmname' /></td></tr>");
                out.print("<tr><th>按时间段查询</th><td> <input type='text' name='date' value='' id='date' /></td></tr>");
                out.print("<tr><th>按时间间隔查询</th><td> <input type='text' name='date1' value='' id='date1' /></td></tr>");
                out.print("<tr><td colspan='2' ><input type='button' value='查询' onclick='query();' /></td></tr></table></form>");
               
// 				String gmname = request.getParameter("gmname");
// 				if (gmname != null&&gmname.trim().length()>0) {
// 				    gmname = gmname.trim();
// 					List<MailRecord> mrs1 = new ArrayList<MailRecord>();
// // 					for (MailRecord mr : mrs) {
// // 						if (gmname.equals(mr.getGmname()))
// // 							mrs1.add(mr);
// // 					}
// // 					mrs = mrs1;
// 				}
// 				String mid = request.getParameter("mmid");
// 				if (mid != null&&mid.trim().length()>0) {
// 				    mid = mid.trim();
// 					List<MailRecord> mrs2 = new ArrayList<MailRecord>();
// // 					for (MailRecord mr : mrs) {
// // 						if (mid.equals(mr.getMid()))
// // 							mrs2.add(mr);
// // 					}
// // 					mrs = mrs2;

// 				}
				
// 				out.print("消息的总长度为" + mrs.size());
				out.print("<table width='80%' ><caption>邮件统计<caption><tr><th  >日期</th><th  >邮件总数</th><th>回复完成数</th><th>未回复数</th><th>平均回复时间间隔（分）</th><th>最长回复时间间隔（分）</th></tr>");
				out.print("<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td></tr>");
				out.print("</table>");
			
			} catch (Exception e) {
			  out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>