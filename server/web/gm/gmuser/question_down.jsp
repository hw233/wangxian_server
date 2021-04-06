<%@ page language="java"
	contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@include file="../authority.jsp" %>
<%@page import="com.xuanzhi.boss.game.model.GamePlayer"%>
<%@include file="../header.jsp"%>
		<%
			try {
				GmMailReplay gmreplay = GmMailReplay.getInstance();
				PlayerManager pmanager = PlayerManager.getInstance();
				MailManager mmanager = MailManager.getInstance();
				MailRecordManager mrmanager = MailRecordManager.getInstance();
				ArticleManager acmanager = ArticleManager.getInstance();
				ActionManager amanager = ActionManager.getInstance();
				String username = session.getAttribute("username").toString();
				String action = request.getParameter("action");
				String gmName = session.getAttribute("gmid").toString();//判断是否登录
				String paname = request.getParameter("paname");//获取过滤参数
				String mcontent = request.getParameter("mcontent");
				String gname = request.getParameter("gname");
				List<Mail> mails = new ArrayList<Mail>();
				if ((paname == null || "".equals(paname.trim()))
						&& (mcontent == null || "".equals(mcontent.trim())))//如果没有过滤过程则获取固定长度的邮件
					mails = gmreplay.getAllGmMail(gname);
				if (paname != null && !"".equals(paname.trim())) {//如果过滤参数不为空则获取过滤邮件
					// paname = URLDecoder.decode(paname);
					List<Mail> ms = gmreplay.getAllGmMail(gname);//获取所有的GM..邮件
					mails = new ArrayList<Mail>();
					for (Mail m : ms) {//如果邮件的发件人和过滤人相同，则获取！
						try {
							if (paname.equals(pmanager.getPlayer(m.getPoster())
									.getName()))
								mails.add(m);
						} catch (Exception e) {
							continue;
						}
					}

				}
				if (mcontent != null && !"".equals(mcontent.trim())) {//如果过滤参数不为空则获取过滤邮件
					out.print(mcontent);
					// paname = URLDecoder.decode(paname);
					List<Mail> ms = gmreplay.getAllGmMail(gname);//获取所有的gname邮件
					mails = new ArrayList<Mail>();
					for (Mail m : ms) {
						try {
							if (m.getContent().contains(mcontent))
								mails.add(m);
						} catch (Exception e) {
							continue;
						}
					}

				}
				StringBuffer sb = new StringBuffer();
				for (Mail m : mails) {
					try {
						sb.append(
								"【账户名】："
										+ pmanager.getPlayer(m.getPoster())
												.getUsername()).append(
												"【角色名】:"+pmanager.getPlayer(m.getPoster()).getName()).append(
								"【发送时间】"
										+ DateUtil.formatDate(
												m.getCreateDate(),
												"yyyy-MM-dd HH:mm:ss")).append(
								"【邮件标题】：" + m.getTitle()).append(
								"【邮件内容】" + m.getContent() + "\n");
					} catch (Exception e) {
						sb.append("【角色Id】：" + m.getPoster()).append(
								"【发送时间】"
										+ DateUtil.formatDate(
												m.getCreateDate(),
												"yyyy-MM-dd HH:mm:ss")).append(
								"【邮件标题】：" + m.getTitle()).append(
								"【邮件内容】" + m.getContent() + "\n");
						continue;
					}
				}
				response.reset();//可以加也可以不加  
				response.setContentType("application/x-download");

				//application.getRealPath("/main/mvplayer/CapSetup.msi");获取的物理路径   
				String filedisplay = "questionmail.txt";
				filedisplay = URLEncoder.encode(filedisplay, "utf-8");
				response.addHeader("Content-Disposition",
						"attachment;filename=" + new String(filedisplay.getBytes("utf-8"),"gbk"));

				java.io.OutputStream outp = null;
				try {
					outp = response.getOutputStream();

					outp.write(sb.toString().getBytes());
					//    
					outp.flush();
					// 要加以下两句话，否则会报错  
					//java.lang.IllegalStateException: getOutputStream() has already been called for //this response    
					//out.clear();
					out = pageContext.pushBody();
				} catch (Exception e) {
					System.out.println("Error!");
					e.printStackTrace();
				} finally {

					// 这里不能关闭    
					//if(outp != null)  
					//{  
					//outp.close();  
					//outp = null;  
					//}  
				}

			} catch (Exception e) {
				  out.print(StringUtil.getStackTrace(e));
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("visitfobiden.jsp");
				//rdp.forward(request, response);

			}
		%>
