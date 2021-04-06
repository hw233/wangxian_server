<%@page import="com.xuanzhi.confirmation.bean.User"%>
<%@page import="com.xuanzhi.confirmation.bean.ConfirmationCode"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.confirmation.bean.GameActivity"%>
<%@page import="com.xuanzhi.confirmation.service.server.DataManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String option = request.getParameter("option");

	String ip = request.getRemoteAddr();
	String recorder = "";
	Object o = session.getAttribute("authorize.username");
	if (o != null) {
		recorder = o.toString();
	}
	
	
	if (option == null) {
		option  = "0";
	}
	
	String [] codeArr = null;
	String codes = request.getParameter("codes");
	boolean success = true;
	HashMap<GameActivity,List<CodeState>> resultForGameActivity = new HashMap<GameActivity,List<CodeState>>();
	List<String> errorCodes = new ArrayList<String>();
	if (codes != null) {
		List<String> optionCodes = new ArrayList<String>();
		
		codeArr = codes.split("\r\n");
		for (String c : codeArr) {
			optionCodes.add(c);
		}
		DataManager dataManager = DataManager.getInstance();
		List<GameActivity>  list = dataManager.getCodeStorer().getAllActivity();
		for (GameActivity ga : list) {
			if (!ga.isActive()) {
				continue;
			}
			List<String> gaCodes = ga.getAllCodes();
			Iterator<String> itor = optionCodes.iterator();
			while (itor.hasNext()) {
				String c = itor.next();
				if (gaCodes.contains(c)) {
					if (!resultForGameActivity.containsKey(ga)) {
						resultForGameActivity.put(ga, new ArrayList<CodeState>());
					}
					resultForGameActivity.get(ga).add(new CodeState(c));
					itor.remove();
				}
			}
		}
		
		if (optionCodes.size() > 0) {
			errorCodes.addAll(optionCodes);
		} else {
			for (Iterator<GameActivity> itor = resultForGameActivity.keySet().iterator();itor.hasNext();) {
				GameActivity ga = itor.next();
				List<CodeState> codeStateList = resultForGameActivity.get(ga);
				for (CodeState cs : codeStateList) {
					ConfirmationCode code = dataManager.getCodeStorer().getConfirmationCode(cs.code);
					if ((code.isValid() && "1".equals(option))|| (!code.isValid() && "0".equals(option))) {
						success = false;
						cs.status = "[失败] [激活码状态:"+(code.isValid() ? "有效" : "无效" )+"] [要改变成:" + ("0".equals(option) ? "无效" : "有效") + "]";
					} else {
						cs.status = "ok";
					}
				}
			}
			if (success) {
				StringBuffer content = new StringBuffer();
				content.append("<table border=1 style='font-size:12px;'>");
				content.append("<tr>");
				content.append("<td>").append("序号").append("</td>");
				content.append("<td>").append("活动ID").append("</td>");
				content.append("<td>").append("活动名").append("</td>");
				content.append("<td>").append("活动奖励").append("</td>");
				content.append("<td>").append("激活码").append("</td>");
				content.append("</tr>");
				int index = 0;
				for (Iterator<GameActivity> itor = resultForGameActivity.keySet().iterator();itor.hasNext();) {
					GameActivity ga = itor.next();
					List<CodeState> codeStateList = resultForGameActivity.get(ga);
					for (CodeState cs : codeStateList) {
						ConfirmationCode code = dataManager.getCodeStorer().getConfirmationCode(cs.code);
						User  user = new User("后台", recorder.toString(), "飘渺寻仙曲", "GM",0, "", 0) ;
						code.recordExchange(user);
						dataManager.getCodeStorer().recordOnceExchange(user, code, ga);
						content.append("<tr>");
						content.append("<td>").append(++index).append("</td>");
						content.append("<td>").append(ga.getId()).append("</td>");
						content.append("<td>").append(ga.getName()).append("</td>");
						content.append("<td>").append(Arrays.toString(ga.getPrizes())).append("</td>");
						content.append("<td>").append(cs.code).append("</td>");
						content.append("</tr>");
						
					}
				}
				content.append("</table>");
				sendMail("［" + recorder.toString() + "] 批量使用激活码(" + codeArr.length + ")", content.toString());
			}
		}
	}
%>
<%! class CodeState{
	
	String code;
	String status;
	
	public CodeState (String code) {
		this.code = code;
	}
	
} %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>批量修改激活码</title>
<style type="text/css">
table {
	font-size:12px;
}
</style>
</head>
<body>
<form action="./batchReviseCode.jsp" method="post">
<%
	if(codeArr != null) {
		if (!success) {
			out.print("<font color=red size=20px>出现错误,并未执行</font><hr>");
		} else {
			out.print("<font color=red size=20px>执行完成</font><hr>");
		}
		%>
			<table border="1">
				<tr>
					<th>活动</th>
					<th>激活码</th>
					<th>结果</th>
				</tr>
				<%
				
					for (Iterator<GameActivity> itor = resultForGameActivity.keySet().iterator();itor.hasNext();) {
						GameActivity ga = itor.next();
						List<CodeState> codeStateList = resultForGameActivity.get(ga);
						for (CodeState cs : codeStateList) {
							%>
								<tr>
									<td><%=ga.getName() + "("+ga.getId()+")" %></td>
									<td><%=cs.code %></td>
									<td><%=cs.status %></td>
								</tr>
							<%
						}
					}
					
				%>
			</table>
			<%
				if (errorCodes.size() > 0) {
					%>
						以下激活码不存在(<%=errorCodes.size() %>):<HR>
					<%
					for (String c : errorCodes) {
						out.print(c +"<br>");
					}
				}
			%>
		<%	
	}
%>
要批量操作的激活码:
<HR>
<textarea rows="15" cols="15" name="codes"><%=codes%></textarea>
<BR>
<span style="font-size:12px;">
<input type="submit" value="执行" style="background-color:red;" >
</span>
</form>
</body>
</html>