<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.datasource.props.Knapsack.Cell"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>潜龙GM邮箱 </title>
		<link rel="stylesheet" href="../style.css" />
		<script type="text/javascript">
	function $(tag) {
		return document.getElementById(tag);
	}
	function change(mid, tag) {
		//将子定义的内容填入回复框
		var instr = document.getElementById("repcontent" + mid);
		if (tag.value != "") {
			instr.value = tag.value;
		}
	}
	function add() {
		var sdate = $("sdate").value;
		var edate = $("edate").value;
		var dhour = $("dhour").value;
		var dminute = $("dminute").value;
		var message = $("message").value;
		if (sdate && edate && dhour && dminute) {
			$("f1").submit();
		} else
			alert("请输入正确的信息");
	}
	function delete1(index) {
		if (window.confirm("你是否确定要删除此广播"))
			window.location.replace("broadcast1.jsp?delindex=" + index);

	}
</script>
	</head>
	<body>
		<%!private List<BroadCast> bcs = new ArrayList<BroadCast>();%>
		<%
			try {
				String username = session.getAttribute("username").toString();
				ActionManager amanager = ActionManager.getInstance();
				BroadManager bmanager = BroadManager.getInstance();
				String delindex = request.getParameter("delindex");
				if (delindex != null) {
					int index = Integer.parseInt(delindex);
					BroadCast b1 = bcs.get(index);
					b1.stop();
					bcs.remove(b1);
					amanager.save(username, "删除了一广播 位置： " + delindex);
					BroadCastRecord bcr = new BroadCastRecord();
					bcr.setStartDate(DateUtil.formatDate(new Date(b1
							.getStartTime()), "yyyy-MM-dd HH:mm"));
					bcr.setEndDate(DateUtil.formatDate(
							new Date(b1.getEndTime()), "yyyy-MM-dd HH:mm"));
					bcr.setMessage(b1.getMessage());
					bcr.setCycletime(b1.getSleepingTime() + "毫秒");
					bmanager.saveRecord(bcr);
					out.print("删除成功");
				}
				String message = request.getParameter("message");
				if (message != null && request.getParameter("add") != null) {
					try {
						String stimestr = request.getParameter("sdate");
						String etimestr = request.getParameter("edate");
						long dtime = (Integer.parseInt(request
								.getParameter("dhour")) * 60 + Integer
								.parseInt(request.getParameter("dminute"))) * 60000;
						if (!"".equals(stimestr) && !"".equals(etimestr)) {
							long stime = DateUtil.parseDateSafely(stimestr,
									"yyyy-MM-dd HH:mm").getTime();
							long etime = DateUtil.parseDateSafely(etimestr,
									"yyyy-MM-dd HH:mm").getTime();
							BroadCast bc = new BroadCast(message, stime, etime,
									dtime);
							List<BroadCast> bcss = bcs;
							for (int j = 0; j < bcss.size(); j++) {
								if (bc.getStartTime() > bcss.get(j)
										.getStartTime()) {
									bcss.add(j, bc);
									break;
								}
							}
							if (!bcss.contains(bc))
								bcss.add(bc);
							bcs = bcss;
							bc.start();
							amanager.save(username, "添加了一个广播内容： " + message);
							out.print("广播开始 ：内容为 " + message + "    开始时间:"
									+ new Date(stime) + "  结束时间:"
									+ new Date(etime) + " 发送周期:" + dtime);
						}
					} catch (Exception e) {
						out.print("广播发送失败 " + e.getMessage());
					}

				}
				StringBuffer fasong = new StringBuffer(
						"<p><input type='button' value='刷新' onclick='window.location.replace(\"broadcast1.jsp\")' /></p>");
				fasong
						.append("<form action='?add=true' method='post' id='f1' ><table width='80%' ><caption>添加广播序列</caption><tr>");
				fasong
						.append("<td class='top'>开始时间<input type='text' name='sdate' id='sdate' value='"
								+ DateUtil.formatDate(new Date(),
										"yyyy-MM-dd HH:mm") + "' /></td>");
				fasong
						.append("<td class='top'>结束时间<input type='text' name='edate' id='edate' value='"
								+ DateUtil.formatDate(new Date(),
										"yyyy-MM-dd HH:mm") + "' />");
				fasong
						.append("</td><td class='top'>循环周期<input input='text' size=5 name='dhour' id='dhour' value='0' />时");
				fasong
						.append("<input input='text' size=5 name='dminute' id='dminute' value='1' />分</td></tr><tr>");
				fasong
						.append("<td colspan='3'><textarea id='message' name='message' cols='60' rows='10'/></textarea></td></tr>");
				fasong
						.append("<tr><td colspan='3' /><input type='button' value='添加' onclick='add();' /></td></tr></table></form>");
				out.print(fasong.toString());
				StringBuffer action = new StringBuffer("<p></p><hr/><p></p>");
				out.print(bcs.size());
				action.append("<table width=80% ><caption>已有序列</caption>");
				action
						.append("<tr><th width=30% >内容 </th><th width=20% >起止时间</th><th width=15% >频率</th><th width=15% >状态</th><th width=20% >操作</tr></tr>");
				List<BroadCast> delbcs = new ArrayList<BroadCast>();
				if (bcs.size() > 0) {
					out.println(bcs.get(0).getMessage());
					long date = new Date().getTime();
					for (int i = 0; i < bcs.size(); i++) {
						BroadCast bcast = bcs.get(i);
						if (bcast.getEndTime() < date) {
							bcast.stop();
							delbcs.add(bcast);
							continue;
						} else {
							action.append("<tr><td>"
									+ bcast.getMessage()
									+ "</td><td>"
									+ DateUtil.formatDate(new Date(bcast
											.getStartTime()), "dd HH:mm"));
							action.append("/"
									+ DateUtil.formatDate(new Date(bcast
											.getEndTime()), "dd HH:mm")
									+ "</td><td>"
									+ (bcast.getSleepingTime() / 60000)
									+ "分钟/次</td>");
							if (date > bcast.getStartTime()
									&& date < bcast.getEndTime())
								action.append("<td>发送中</td>");
							else if (date > bcast.getEndTime())
								action.append("<td>已过期</td>");
							else if (date < bcast.getStartTime())
								action.append("<td>等待发送</td>");
							action
									.append("<td><input type='button' value='删除' onclick='delete1("
											+ i + ");' /></td></tr>");
						}
					}
				}
				bcs.remove(delbcs);
				for (BroadCast bc : delbcs) {
					BroadCastRecord bcr = new BroadCastRecord();
					bcr.setStartDate(DateUtil.formatDate(new Date(bc
							.getStartTime()), "yyyy-MM-dd HH:mm"));
					bcr.setEndDate(DateUtil.formatDate(
							new Date(bc.getEndTime()), "yyyy-MM-dd HH:mm"));
					bcr.setMessage(bc.getMessage());
					bcr.setCycletime(bc.getSleepingTime() + "毫秒");
					bmanager.saveRecord(bcr);
					amanager.save("系统", "删除 了广播 位置为 " + bc.getMessage());
				}
				action.append("</table>");
				out.print(action.toString());
				List<BroadCastRecord> bcrs = bmanager.getBrs();
				out
						.print("<table width='80%' ><caption> 历史记录 </caption><tr><th>信息</th><th>开始时间</th><th>结束时间</th><th>周期</th></tr>");
				for (int i=bcrs.size()-1;i>=0;i--) {
				BroadCastRecord b = bcrs.get(i);
					out.print("<tr><td>" + b.getMessage() + "</td><td>"
							+ b.getStartDate() + "</td><td>" + b.getEndDate()
							+ "</td><td>" + b.getCycletime() + "</td></tr>");
				}
				out.print("</table>");
			} catch (Exception e) {
			    out.print(StringUtil.getStackTrace(e));
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("visitfobiden.jsp");
				//rdp.forward(request, response);
			}
		%>
	</body>
