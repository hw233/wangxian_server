<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%>
<%@page import="com.fy.engineserver.gm.feedback.*"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>


<%@page import="com.fy.engineserver.gametime.SystemTime"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../css/style.css"/>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<script type="text/javascript">

	function queryfeedback(){
		 obj = document.getElementById('gmSelect'); 
		 var index = obj.selectedIndex; // 选中索引
		 var str = obj.options[index].text; // 选中文本
		 document.getElementById('gm').value = str;
		 document.queryFeedBack.submit();
	}
	
	function queryfollow(){
		
	}

</script>


</head>
<body>

	<%
		String gmName = request.getParameter("gmName");
// 		String sname = session.getAttribute("gm").toString();
// 		if(sname!=null&&sname.trim().length()>0){
// 			gmName = sname;
// 		}
		if(gmName != null && !gmName.equals("")){
			GMRecord gr1 =  FeedbackManager.getInstance().getGMRecord(gmName.trim());
			if(gr1 == null){
				 FeedbackManager.getInstance().createGM(gmName);
			}
			session.setAttribute("gm",gmName);
			
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
			String startTime = sdf.format(SystemTime.currentTimeMillis() - 24*60*60*1000);
			String endTime = sdf.format(SystemTime.currentTimeMillis() + 24*60*60*1000);
			
	%>
			
			
      			<table>
      				<form action="queryFeedback.jsp" name="queryFeedBack">
			
      				<tr><th>开始时间:(2011-01-11)</th><td><input name="beginTime" type="text" value="<%=startTime %>"/></td></tr>
      				<tr><th>截止时间:(2011-01-11)</th><td><input name="endTime" type="text" value="<%=endTime %>"/></td></tr>
      				<tr><th>反馈类型（可多选）</th><input type="hidden" name="hiddenNum" value="0"/>
      					<td><input type="checkbox" name="feedbackType" value ="0" checked="checked">bug
							<input type="checkbox" name="feedbackType" value ="1" checked="checked">建议
							<input type="checkbox" name="feedbackType" value ="2" checked="checked">投诉
							<input type="checkbox" name="feedbackType" value ="3" checked="checked">充值
							<input type="checkbox" name="feedbackType" value ="4" checked="checked">其他
						</td>
					</tr>
      				<tr><th>反馈状态（可多选）：</th>
      					<td>
      						<input type="checkbox" name="feedbackState" value ="0" checked="checked">未处理
							<input type="checkbox" name="feedbackState" value ="1">等待处理
							<input type="checkbox" name="feedbackState" value ="2" checked="checked">新反馈
							<input type="checkbox" name="feedbackState" value ="3">已关闭
      					</td>
      				</tr>
      				<tr><th>gm编号：</th>
      					<td>
      						<input type="hidden" id="gm" name="gm" /> 
						  		 <select name="gm" id="gmSelect">
								   	<%
								   	List<GMRecord> list = FeedbackManager.getInstance().getAllGm();
								   	boolean bln = true;
								   	int i= 0;
									out.print("<option value=\""+0+"\" selected=\"selected\">全部</option>");
									i++;
								   	for(GMRecord gr : list){
								   		if(bln){
								   			out.print("<option value=\""+i+">"+gr.getGmName()+"</option>");
								   			bln = false;
								   		}else{
								   			out.print("<option value=\""+i+"\">"+gr.getGmName()+"</option>");
								   		}
								   		++i;
								   	}
								   	%>
			      				</select>
      					</td>
      				</tr>
<!--       				<tr><th>角色名查询</th><td><input type="text" name="queryname"></td></tr> -->
<!-- 	      			<tr><th>评分查询</th><td><input type="text" name="queryscore"></td></tr> -->
<!-- 	      			<tr><th>VIP查询</th><td><input type="text" name="queryvip"></td></tr> -->
	      			<tr align="center">
	     			<th colspan="2"><input type="button"  onclick="queryfeedback()" value="查询"/></th></tr>
	      			<tr><th align="center"><input type="button"  onclick="queryfollow()" value="追踪查询"/></th></tr>
	      			</form>
      			</table>
			<%
			
		}else{
			%>
			<form action="">
				GMname:<input type="text" name="gmName"/>
				<input type="submit" value="提交"/>
			</form>
			
			<%
		}
	
	%>

</body>
</html>