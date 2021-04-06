<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>自定义回复内容</title>
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
	function findpos(tag) {
		var index = 0;
		if (tag != null) {
			var tab = document.getElementById("tt");
			var trs = tab.rows;
			var trNode = tag.parentNode.parentNode;
			for ( var i = 0; i < tab.rows.length; i++) {
				if (trs[i] == trNode) {
					index = i;
					break;
				}
			}
		}
		if (index != 0)
			index = index - 1;
		return index;
	}
	function insert(tag) {
		var tab = document.getElementById("tt");
		var index = findpos(tag);
		if (tag == null)
			index = -2;
		var nextRow = tab.insertRow(index + 1);
		nextRow.onmouseover = function() {
			overTag(this);
		};
		nextRow.onmouseout = function() {
			outTag(this);
		};
		var cel1 = nextRow.insertCell(-1);
		var cel2 = nextRow.insertCell(-1);
		cel1.innerHTML = "内容：<input type='text' size='80' id='value' name='value' value = ''/>";
		cel2.innerHTML = "<a href='#' onclick='add(this);'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
	}
	function add(tag) {
		var index = findpos(tag);
		var value = document.getElementById("value").value;
		if (value) {
			var ff = document.getElementById("f1");
			ff.action = "?action=add&index=" + index;
			ff.submit();
		} else {
			alert("输入的内容不能为空");
		}
	}
	function update(tag) {
		var trRow = tag.parentNode.parentNode;
		var cells = trRow.cells;
		var value = cells[0].innerHTML;
		cells[0].innerHTML = "内容：<input type='text' size='80' id='value' name='value' value = '"
				+ value + "'/>";
		cells[1].innerHTML = "<a href='#' onclick='udcommit(this);'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
	}
	function udcommit(tag) {
		var index = findpos(tag);
		var value = document.getElementById("value").value;
		if (value) {
			var ff = document.getElementById("f1");
			ff.action = "?action=update&index=" + index;
			ff.submit();
		} else {
			alert("输入框中的内容不能为空");
		}
	}
	function delete1(tag) {
		var index = findpos(tag);
		if (window.confirm("你是否确定要删除此模块")) {
			window.location.replace("custom_manager.jsp?delindex=" + index);
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
						.print("<input type='button' value='刷新' onclick='window.location.replace(\"custom_manager.jsp\")' />");
				String username = session.getAttribute("username").toString();
				ActionManager amanager = ActionManager.getInstance();
				CustomManager cmanager = CustomManager.getInstance();

				String delid = request.getParameter("delindex");
				if (delid != null) {
					cmanager.deleteCustom(Integer.parseInt(delid));
					amanager.save(username, "删除了一自定义留言 位置： " + delid);
					response.sendRedirect("custom_manager.jsp");
				}
				String action = request.getParameter("action");
				if (action != null) {
					if ("add".equals(action.trim())) {
						int index = Integer.parseInt(request.getParameter(
								"index").trim());
						String value = request.getParameter("value");
						cmanager.insertCustom(value, index);
						amanager.save(username, "添加了一自定义留言：" + value);
					}
					if ("update".equals(action.trim())) {
						int index = Integer.parseInt(request.getParameter(
								"index").trim());
						String value = request.getParameter("value");
						cmanager.updateCustom(value, index);
						amanager.save(username, "更新了一自定义留言：" + value + " 位置： "
								+ index);
					}
				}

				List<String> customs = cmanager.getCustoms();
				out
						.println("<form id ='f1' method='post' action='#'><table width=80% ><tbody id='tt'><tr><th>自定义内容</th><th>操作</th></tr>");
				if (customs != null && customs.size() > 0) {
					for (int i = 0; i < customs.size(); i++) {
						//遍历自定义回复
						out
								.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);'><td>"
										+ customs.get(i) + "</td><td>");
						out
								.print("<input type ='button' value='增加' onclick='insert(this);'/> | ");
						out
								.print("<input type ='button' value='删除' onclick='delete1(this);'/> | ");
						out
								.print("<input type ='button' value='修改' onclick='update(this);'/></td></tr>");
					}
				}
				out
						.print("</tbody></table><p align=\"center\"><input type ='button' value='增加' onclick='insert();'/></p></form>");
			} catch (Exception e) {
				// out.print(e.getMessage());
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				//rdp.forward(request, response);
				out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
