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
		var nextRow = tab.insertRow(index + 1);
		nextRow.onmouseover = function() {
			overTag(this);
		};
		nextRow.onmouseout = function() {
			outTag(this);
		};
		var cel1 = nextRow.insertCell(-1);
		var cel2 = nextRow.insertCell(-1);
		var cel3 = nextRow.insertCell(-1);
		var cel4 = nextRow.insertCell(-1);
		var cel5 = nextRow.insertCell(-1);
		cel1.innerHTML = "礼品名：<input type='text'  id='pname" + index
				+ "' name='pname" + index + "' value = ''/>";
		cel2.innerHTML = "开始时间：<input type='text'  id='smon" + index
				+ "' name='smon" + index
				+ "' size='5' value = ''/>月<input type='text'  id='sday"
				+ index + "' name='sday" + index + "' size='5' value = ''/>日";
		cel3.innerHTML = "结束时间：<input type='text'  id='emon" + index
				+ "' name='emon" + index
				+ "' size='5' value = ''/>月<input type='text'  id='eday"
				+ index + "' name='eday" + index + "' size='5' value = ''/>日";
		cel4.innerHTML = "次数：<input type='text'  id='count" + index
				+ "' name='count" + index + "' value = ''/>";
		cel5.innerHTML = "<a href='#' onclick='add(this);'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
	}
	function add(tag) {
		var index = findpos(tag);
		var str = "?action=add&index=" + index;
		var pname = document.getElementById("pname" + index).value;
		str = str + "&pname=" + pname;
		var smon = document.getElementById("smon" + index).value;
		str = str + "&smon=" + smon;
		var sday = document.getElementById("sday" + index).value;
		str = str + "&sday=" + sday;
		var emon = document.getElementById("emon" + index).value;
		str = str + "&emon=" + emon;
		var eday = document.getElementById("eday" + index).value;
		str = str + "&eday=" + eday;
		var count = document.getElementById("count" + index).value;
		str = str + "&count=" + count;
		if (pname && smon && sday && emon && eday && count) {
			var ff = document.getElementById("f1");
			ff.action = str;
			ff.submit();
		} else {
			alert("输入的内容不能为空");
		}
	}
	function update(tag) {
		var trRow = tag.parentNode.parentNode;
		var cells = trRow.cells;
		var index = findpos(tag);
		var pname = cells[0].innerHTML;
		var sdate = cells[1].innerHTML.split("-");
		var edate = cells[2].innerHTML.split("-");
		var count = cells[3].innerHTML;
		cells[0].innerHTML = "礼品名：<input type='text'  id='pname" + index
				+ "' name='pname" + index + "' value = '" + pname + "'/>";
		cells[1].innerHTML = "开始时间：<input type='text'  id='smon" + index
				+ "' name='smon" + index + "' size='5' value = '" + sdate[0]
				+ "'/>月<input type='text'  id='sday" + index + "' name='sday"
				+ index + "' size='5' value = '" + sdate[1] + "'/>日";
		cells[2].innerHTML = "结束时间：<input type='text'  id='emon" + index
				+ "' name='emon" + index + "' size='5' value = '" + edate[0]
				+ "'/>月<input type='text'  id='eday" + index
				+ "' name='eday' size='5' value = '" + edate[1] + "'/>日";
		cells[3].innerHTML = "次数：<input type='text'  id='count" + index
				+ "' name='count" + index + "' value = '" + count + "'/>";
		cells[4].innerHTML = "<a href='#' onclick='udcommit(this);'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
	}
	function udcommit(tag) {
		var index = findpos(tag);
		var str = "?action=update&index=" + index;
		var pname = document.getElementById("pname" + index).value;
		str = str + "&pname=" + pname;
		var smon = document.getElementById("smon" + index).value;
		str = str + "&smon=" + smon;
		var sday = document.getElementById("sday" + index).value;
		str = str + "&sday=" + sday;
		var emon = document.getElementById("emon" + index).value;
		str = str + "&emon=" + emon;
		var eday = document.getElementById("eday" + index).value;
		str = str + "&eday=" + eday;
		var count = document.getElementById("count" + index).value;
		str = str + "&count=" + count;
		if (pname && smon && sday && emon && eday && count) {
			var ff = document.getElementById("f1");
			ff.action = str;
			ff.submit();
		} else {
			alert("输入框中的内容不能为空");
		}
	}
	function delete1(tag) {
		var index = findpos(tag);
		if (window.confirm("你是否确定要删除此模块")) {
			window.location.replace("gmitem_manager.jsp?delindex=" + index);
		}
	}
	function inserth(ind) {
		var tab = document.getElementById("tt");
		var index = ind;
		var nextRow = tab.insertRow(index + 1);
		nextRow.onmouseover = function() {
			overTag(this);
		};
		nextRow.onmouseout = function() {
			outTag(this);
		};
		var cel1 = nextRow.insertCell(-1);
		var cel2 = nextRow.insertCell(-1);
		var cel3 = nextRow.insertCell(-1);
		var cel4 = nextRow.insertCell(-1);
		var cel5 = nextRow.insertCell(-1);
		cel1.innerHTML = "礼品名：<input type='text'  id='pname" + index
				+ "' name='pname" + index + "' size='7' value = ''/>";
		cel2.innerHTML = "开始时间：<input type='text'  id='smon" + index
				+ "' name='smon" + index
				+ "' size='5' value = ''/>月<input type='text'  id='sday"
				+ index + "' name='sday" + index + "' size='5' value = ''/>日";
		cel3.innerHTML = "结束时间：<input type='text'  id='emon" + index
				+ "' name='emon" + index
				+ "' size='5' value = ''/>月<input type='text'  id='eday"
				+ index + "' name='eday" + index + "' size='5' value = ''/>日";
		cel4.innerHTML = "次数：<input type='text'  id='count" + index
				+ "' name='count" + index + "' size='5' value = ''/>";
		cel5.innerHTML = "<a href='#' onclick='add(this);'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
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
						.print("<input type='button' value='刷新' onclick='window.location.replace(\"gmitem_manager.jsp\")' />");
				GmItemManager gmanager = GmItemManager.getInstance();
				ActionManager amanager = ActionManager.getInstance();
				ArticleManager acmanager = ArticleManager.getInstance();
				String username = session.getAttribute("username").toString();
				String delid = request.getParameter("delindex");
				if (delid != null) {
					PresentItem pi = gmanager.getItems().get(
							Integer.parseInt(delid));
					gmanager.deletePresentItem(Integer.parseInt(delid));
					amanager.save(username, "删除了一个邮件礼品" + pi.getItemname());
					response.sendRedirect("gmitem_manager.jsp");
				}
				String action = request.getParameter("action");
				if (action != null) {
					if ("add".equals(action.trim())) {
						PresentItem pi = new PresentItem();
						int index = Integer.parseInt(request
								.getParameter("index"));
						String pname = request.getParameter("pname");
						String sdate = request.getParameter("smon") + "-"
								+ request.getParameter("sday");
						String edate = request.getParameter("emon") + "-"
								+ request.getParameter("eday");
						String count = request.getParameter("count");
						pi.setItemname(pname);
						pi.setCount(count);
						pi.setStartDate(sdate);
						pi.setEndDate(edate);
						if (acmanager.getArticle(pname.trim()) != null) {
							gmanager.insertPresentItem(pi, index);
							amanager.save(username, "添加了一个游戏礼品：" + pname);
						} else
							out.print(pname + "物品不存在！添加失败！请输入正确的物品名！！<br/>");
					}
					if ("update".equals(action.trim())) {
						PresentItem pi = new PresentItem();
						int index = Integer.parseInt(request
								.getParameter("index"));
						String pname = request.getParameter("pname");
						String sdate = request.getParameter("smon") + "-"
								+ request.getParameter("sday");
						String edate = request.getParameter("emon") + "-"
								+ request.getParameter("eday");
						String count = request.getParameter("count");
						pi.setItemname(pname);
						pi.setCount(count);
						pi.setStartDate(sdate);
						pi.setEndDate(edate);
						if (acmanager.getArticle(pname.trim()) != null) {
							gmanager.updatePresentItem(pi, index);
							amanager.save(username, "更新了一个游戏礼品： " + pname
									+ " 位置： " + index);
						} else
							out.print(pname + "物品不存在！修改失败！请输入正确的物品名！！<br/>");
					}
				}

				List<PresentItem> pis = gmanager.getItems();
				out
						.println("<form id ='f1' method='post' action='#'><table width=80% ><tbody id='tt'><tr><th>奖品名称</th><th>活动开始时间</th><th>活动结束时间</th><th>最大次数</th><th>操作</th></tr>");
				if (pis != null && pis.size() > 0) {
					for (int i = 0; i < pis.size(); i++) {
						//遍历礼品
						PresentItem pi = pis.get(i);
						out
								.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);'><td>"
										+ pi.getItemname()
										+ "</td><td>"
										+ pi.getStartDate()
										+ "</td><td>"
										+ pi.getEndDate()
										+ "<td>"
										+ pi.getCount() + "</td><td>");
						out
								.print("<input type ='button' value='增加' onclick='insert(this);'/> | ");
						out
								.print("<input type ='button' value='删除' onclick='delete1(this);'/> | ");
						out
								.print("<input type ='button' value='修改' onclick='update(this);'/></td></tr>");
					}
				}
				out
						.print("</tbody></table><p align=\"center\"><input type ='button' value='增加' onclick='inserth("
								+ pis.size() + ");'/></p></form>");

			} catch (Exception e) {
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				//rdp.forward(request, response);
				out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
