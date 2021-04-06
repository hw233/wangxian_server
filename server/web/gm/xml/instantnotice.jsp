<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.operating.RuntimeString"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>自定义及时广播</title>
		<link rel="stylesheet" href="../style.css" />
		<script type="text/javascript">
	function overTag(tag) {
		tag.style.color = "red";
		tag.style.backgroundColor = "lightcyan";
	}

	function outTag(tag) {
		tag.style.color = "black";
		tag.style.backgroundColor = "white";
	}
	function $(tag) {
		return document.getElementById(tag);
	}
	function update(tag) {
		var trRow = tag.parentNode.parentNode;
		var cells = trRow.cells;
		var key = cells[0].innerHTML;
		var value = cells[1].innerHTML;
		cells[0].innerHTML = "标识：" + key
				+ "<input type='hidden' id='key' name='key' value='" + key
				+ "' />";
		cells[1].innerHTML = "内容：<input type='text' size='80' id='value' name='value' value = '"
				+ value + "'/>";
		cells[2].innerHTML = "<a href='#' onclick='udcommit();'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
	}
	function udcommit() {
		var key = $("key").value;
		var value = $("value").value;
		if (key && value) {
			var ff = $("f1");
			ff.action = "?action=update";
			ff.submit();
		} else {
			alert("输入框中的内容不能为空");
		}
	}
	function del(key) {
		if (window.confirm("你确定要删除该广告吗？")) {
			window.location.replace("instantnotice.jsp?delstr=" + key);
		}
	}
	function admit() {
		var key = $("key1").value;
		var value = $("value1").value;
		if (key && value) {
			$("f2").submit();
		} else {
			alert("请输入正确的信息");
		}
	}
	function cancer() {
		//取消操作 
		location.reload();
	}
</script>
	</head>
	<body>
		<%
			try {
				out
						.print("<input type='button' value='刷新' onclick='window.location.replace(\"instantnotice.jsp\")' />");
				String username = session.getAttribute("username").toString();
				ActionManager amanager = ActionManager.getInstance();
				RuntimeString rmanager = RuntimeString.getInstance();
				Map<String, String> its = rmanager.getStringH();
				String delstr = request.getParameter("delstr");
				if (delstr != null) {
					rmanager.remove(delstr);
					amanager.save(username, "删除了一及时广告： " + delstr);
					response.sendRedirect("instantnotice.jsp");
				}
				String action = request.getParameter("action");
				if (action != null) {
					if ("add".equals(action.trim())) {
						String key = request.getParameter("key1");
						String value = request.getParameter("value1");
						boolean a = rmanager.add(key, value);
						if (!a) {
							out
									.print("<p><font color='red'>输入的表示已存在，请换一个再添加</font></p>");
							amanager.save(username, "添加了一及时广告失败：" + value);
						} else {
							out.print("<p>添加成功</p>");
							amanager.save(username, "添加了一及时广告成功：" + value);
						}

					}
					if ("update".equals(action.trim())) {
						String key = request.getParameter("key");
						String value = request.getParameter("value");
						rmanager.edit(key, value);
						amanager.save(username, "更新了一及时广告：" + value);
					}
				}
				out
						.println("<form id ='f1' method='post' action='#'><table width=80% align='center' ><tbody id='tt'><tr><th>唯一标示</th><th>内容</th><th>操作</th></tr>");
				if (its != null && its.size() > 0) {
					for (String k : its.keySet()) {
						//遍历自定义回复
						out
								.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);'><td>"
										+ k
										+ "</td><td>"
										+ its.get(k)
										+ "</td><td>");
						out
								.print("<input type ='button' value='修改' onclick='update(this);'/> | ");
						out
								.print("<input type ='button' value='删除' onclick='del(\""
										+ k + "\")'/></td></tr>");
					}
				}
				out.print("</tbody></table></form>");
				out
						.print("<form id='f2' method='post' action='?action=add'><table width='40%' align=center >'");
				out
						.print("<tr><th>唯一标识</th><td class='top' ><input type='text' id='key1' name='key1' value=''/></td></tr>");
				out
						.print("<tr><th>内容</th><td class='top'><input type='text' id='value1' name='value1' value='' /></td></tr></table>");
				out
						.print("<input type='button' value='添加' onclick = 'admit();' /><input type='reset' value='重置' /></form> ");
			} catch (Exception e) {
				//e.printStackTrace();
				//   out.print(e.getMessage());
				//	RequestDispatcher rdp = request
				//			.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				//	rdp.forward(request, response);
				out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
