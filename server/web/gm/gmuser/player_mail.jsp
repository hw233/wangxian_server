<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.*" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@include file="../header.jsp"%>
<%-- <%@include file="../authority.jsp"%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>被删邮件 </title>
		<link rel="stylesheet" href="../style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<%
			try {
			 //回复角色删除邮件
				String gmname = session.getAttribute("username").toString();
				MailManager mmanager = MailManager.getInstance();
				PlayerManager pmanager = PlayerManager.getInstance();
				ArticleManager acmanager = ArticleManager.getInstance();
				ArticleEntityManager aemanager = ArticleEntityManager
						.getInstance();
				int start = 0;
				int length = 1000;
				if (request.getParameter("start") != null)
					start = Integer.parseInt(request.getParameter("start"));
				String action = request.getParameter("action");
				Player p = null;
				List<Mail> mails = null;
				if (action != null && !"".equals(action)) {
					if ("query".equals(action)) {
						String pname = request.getParameter("pname");
						if (pname != null && !"".equals(pname.trim()))
							p = pmanager.getPlayer(pname.trim());
						String pid = request.getParameter("pid");
						if (pid != null && !"".equals(pid.trim()))
							p = pmanager
									.getPlayer(Integer.parseInt(pid.trim()));
						if (p != null) {
							mails = mmanager.getPlayerDeletedMails(p, start,
									length);
							out.print("邮件数量为：" + mails.size());
						} else {
							out.print("<font color='red' >请输入正确的查询信息！！</font>");

						}

					}
					if ("update".equals(action)) {
						p = pmanager.getPlayer(Integer.parseInt(request
								.getParameter("pid").trim()));
						Mail mail = mmanager.getMail(Long.parseLong(request
								.getParameter("mid").trim()));
						mail.setStatus(Mail.NORMAL_READED);
						mmanager.updateMail(mail);
						mails = mmanager
								.getPlayerDeletedMails(p, start, length);

					}
					if ("updateall".equals(action)) {
						p = pmanager.getPlayer(Integer.parseInt(request
								.getParameter("pid").trim()));
						List<Mail> mailss = mmanager.getPlayerDeletedMails(p,
								start, length);
						for (Mail mail : mailss) {
							mail.setStatus(Mail.NORMAL_READED);
							mmanager.updateMail(mail);
						}
						mails = new ArrayList<Mail>();

					}
				}
				if (p == null)
					out
							.print("<input type='button' value='刷新' onclick='window.location.replace(\"player_mail.jsp\")' />");
				else {
					out
							.print("<input type='button' value='刷新' onclick='window.location.replace(\"player_mail.jsp?action=query&pid="
									+ p.getId() + "\")' />");
					out
							.print("<input type='button' value='全部恢复' onclick='window.location.replace(\"player_mail.jsp?action=updateall&pid="
									+ p.getId() + "\")' />");
				}
				out
						.print("<form action='?action=query' method ='post' >角色ID：<input type='text' name='pid' id='pid' value='"
								+ (p == null ? "" : p.getId()) + "' />");
				out
						.print("角色名：<input type='text' id='pname' name='pname' value='"
								+ (p == null ? "" : p.getName())
								+ "' /><input type='submit' value='查询' /></form>");

				StringBuffer tablehead = new StringBuffer(
						"<form action=\"\" name=\"form2\" method=\"post\">");
				tablehead.append("<table width='80%' ><tr >");
				tablehead.append("<th  nowrap=\"nowrap\">ID</td>");
				tablehead.append("<th  nowrap=\"nowrap\">账号</th>");
				tablehead.append("<th  nowrap=\"nowrap\">发件时间</th>");
				tablehead.append("<th  nowrap=\"nowrap\">发件人</th>");
				tablehead.append("<th  nowrap=\"nowrap\">标题</th>");
				tablehead.append("<th  nowrap=\"nowrap\">内容</th>");
				tablehead.append("<th  nowrap=\"nowrap\">附件</th>");
				tablehead.append("<th  nowrap=\"nowrap\">游戏币</th>");
				tablehead.append("<th  nowrap=\"nowrap\">操作</th></tr>");
				out.print(tablehead.toString());
				if (mails != null) {
					for (Mail ml : mails) {//遍历邮件
						try {
							out.print("<tr><td>"
									+ ml.getId()
									+ "</td><td>"
									+ (ml.getPoster() == -1 ? "系统" : pmanager
											.getPlayer(ml.getPoster())
											.getUsername()) + "</td>");
							out.print("<td>"
									+ DateUtil.formatDate(ml.getCreateDate(),
											"yyyy-MM-dd HH:mm")
									+ "</td><td>"
									+ (ml.getPoster() == -1 ? "系统" : pmanager
											.getPlayer(ml.getPoster())
											.getName()) + "</td>");
							out.print("<td>" + ml.getTitle() + "</td><td>"
									+ ml.getContent() + "</td><td>");
							if (ml.getCells() != null
									&& ml.getCells().length > 0) {
								for (Cell cs : ml.getCells()) {
									if (cs.getEntityId() > 0)
										out.print(aemanager.getEntity(
												cs.getEntityId())
												.getArticleName()
												+ "("
												+ cs.getCount()
												+ ")<br/>");
								}
							}
                            out.print("</td><td>"+ml.getCoins()+"(收)/"+ml.getPrice()+"(付费)</td>");
							out
									.print("<td><input type='button' value='还原' onclick='window.location.replace(\"player_mail.jsp?action=update&pid="
											+ p.getId()
											+ "&mid="
											+ ml.getId()
											+ "\")' /></td></tr>");
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}

				}
				out.print("</table></form>");

			} catch (Exception e) {
				//e.printStackTrace();
				out.print(StringUtil.getStackTrace(e));
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("visitfobiden.jsp");
				//rdp.forward(request, response);

			}
		%>
	</body>
