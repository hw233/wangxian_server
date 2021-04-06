<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%-- <%@page import="com.xuanzhi.boss.game.model.GamePlayer"%> --%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp"%>
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

	function del() {
		//删除所选的邮件
		var checks = document.getElementsByName("delmid");
		if (window.confirm("你是否确定要删除此模块")) {
			var str = "?action=del"
			for (i = 0; i < checks.length; i++) {
				if (checks[i].checked == true)
					str = str + "&delid=" + checks[i].value;
			}
			window.location.replace(str);
		}
	}

	function resetm() {
		//重置所有的删除选项
		var checks = document.getElementsByName("delmid");
		for (i = 0; i < checks.length; i++)
			checks[i].checked = false;
	}
    function clear1(tag){
      tag.innerHTML="";
    }
      function appendnew(ptag,tagname,type1,name1,value1){
        var ob = document.createElement(tagname);
         ob.type=type1;
         ob.name=name1;
         ob.value=value1;
         ptag.appendChild(ob);
    }	
	function replay(mid) {
	    var ftag = $("f5");
	    clear1(ftag);
		var str = "?action=add&mid=" + mid;
		appendnew(ftag,"input","hidden","mid",mid);
		appendnew(ftag,"input","hidden","action","add");
		var mailId = $(mid + "receiver").value;
		str = str + "&mailid=" + mailId;
		appendnew(ftag,"input","hidden","mailid",mailId);
		var ssid1 = $("ssid1").value;
		str = str + "&ssid1=" + ssid1;
		appendnew(ftag,"input","hidden","ssid1",ssid1);
		var title = $("restitle" + mid).value;
		appendnew(ftag,"input","hidden","title",title);
		str = str + "&title=" + title;
		var content = $("repcontent" + mid).value;
		appendnew(ftag,"input","hidden","content",content);
		str = str + "&content=" + content;
		var isban = $("isban").checked;
		appendnew(ftag,"input","hidden","isban",isban);
		str = str + "&isban="+isban;
		var items = getmselect("item" + mid);
		if (items.length > 6)
			alert("发送的附件超过限制");
		else {
			for (i = 0; i < items.length; i++) {
				str = str + "&item=" + items[i];
				appendnew(ftag,"input","hidden","item",items[i]);
			}
			//window.location.replace(str);
			ftag.submit();
		}
	}
	function send() {
	    var ftag = $("f5");
		var str = "?action=add&mid=1";
		clear1(ftag);
		appendnew(ftag,"input","hidden","mid",1);
		appendnew(ftag,"input","hidden","action","add");
		var mailname = $("playname").value;
		str = str + "&playname=" + mailname;
		appendnew(ftag,"input","hidden","playname",mailname);
		var ssid1 = $("ssid1").value;
		appendnew(ftag,"input","hidden","ssid1",ssid1);
		str = str + "&ssid1=" + ssid1;
		var title = $("restitle" + 1).value;
		str = str + "&title=" + title;
		appendnew(ftag,"input","hidden","title",title);
		var content = $("repcontent" + 1).value;
		str = str + "&content=" + content;
		appendnew(ftag,"input","hidden","content",content);
		var isban = $("isban1").checked;
		str = str +"&isban="+isban;
		appendnew(ftag,"input","hidden","isban",isban);
		var items = getmselect("item" + 1);
		var mailid = $("mailid").value;
		str = str + "&mailid=" + mailid;
		appendnew(ftag,"input","hidden","mailid",mailid);
		if (items.length > 6)
			alert("发送的附件超过限制");
		else {
			for (i = 0; i < items.length; i++) {
				str = str + "&item=" + items[i];
				appendnew(ftag,"input","hidden","item",items[i]);
			}
			//window.location.replace(str);
			ftag.submit();
		}
	}
	function getmselect(tagid) {
		var res = new Array();
		var setag = $(tagid);
		for (i = 0; i < setag.length; i++) {
			if (setag.options[i].selected) {
				res[res.length] = setag.options[i].value;
			}
		}
		return res;
	}
	function query(pn) {
		var paname = document.getElementById("paname").value;
		if (pn)
			paname = pn;
		window.location.replace("gmmail_manager1.jsp?paname=" + paname);

	}
	function chooseall() {
		var cas = document.getElementsByName("delmid");
		if (cas) {
			for (i = 0; i < cas.length; i++) {
				cas[i].checked = true;
			}

		}
	}
	function unsetall() {
		var cas = document.getElementsByName("delmid");
		if (cas) {
			for (i = 0; i < cas.length; i++) {
				cas[i].checked = false;
			}

		}
	}
	function queryc() {
		var mcontent = $("mcontent").value;
		if (mcontent)
			window.location.replace("gmmail_manager1.jsp?mcontent=" + mcontent);
	}

	function querybyId(pid1) {
		var pid = $("pid").value;
		if (pid1)
			pid = pid1;
		if (pid)
			window.location.replace("gmmail_manager1.jsp?pid=" + pid);
	}
	function jump(index) {
		var starty = $("starty" + index).value;
		if (starty) {
			window.location.replace("gmmail_manager1.jsp?starty=" + starty);
		} else
			alert("页码不能为空！！！！");
	}
	function queryBydate() {
		var syear = $("syear").value;
		var smonth = $("smonth").value;
		var sday = $("sday").value;
		var shour = $("shour").value;
		var sminute = $("sminute").value;
		var eyear = $("eyear").value;
		var emonth = $("emonth").value;
		var eday = $("eday").value;
		var ehour = $("ehour").value;
		var eminute = $("eminute").value;
		var stime = syear + "-" + smonth + "-" + sday + " " + shour + ":"
				+ sminute;
		var etime = eyear + "-" + emonth + "-" + eday + " " + ehour + ":"
				+ eminute;
		if (syear && smonth && sday && shour && sminute && eyear && emonth
				&& eday && ehour && eminute) {
			window.location.replace("gmmail_manager1.jsp?stime=" + stime
					+ "&etime=" + etime);
		} else {
			alert("请输入正确的数据");
		}
	}
</script>
	</head>
	<body>
		<%
// 			try {
// 				out
// 						.print("<input type='button' value='过滤刷新' onclick='window.location.replace(\"gmmail_manager1.jsp?a=update\")' />");
// 				out
// 						.print("<input type='button' value='刷新' onclick='window.location.replace(\"gmmail_manager1.jsp\")' />");
// 				GmMailReplay gmreplay = GmMailReplay.getInstance();
// 				PlayerManager pmanager = PlayerManager.getInstance();
// 				CustomManager cmanager = CustomManager.getInstance();
// 				GmItemManager gmanager = GmItemManager.getInstance();
// 				MailManager mmanager = MailManager.getInstance();
// 				MailRecordManager mrmanager = MailRecordManager.getInstance();
// // 				ArticleManager acmanager = ArticleManager.getInstance();
// // 				ActionManager amanager = ActionManager.getInstance();
// // 				ArticleEntityManager aemanager = ArticleEntityManager
// 						.getInstance();
// 				String updatea = request.getParameter("a");
// 				if (updatea != null && "update".equals(updatea)) {
// // 					gmreplay.setGmmails("GM01");
// 					out.print("[" + gmreplay.getGmmails().size() + "]");
// 					out.print("更新成功！<br/>");
// 				}
// 				String username = session.getAttribute("username").toString();
// 				String action = request.getParameter("action");
// 				String ssid = null;
// 				//ssid用来保存 一个令牌值
// 				if (request.getParameter("ssid1") != null) {
// 					//如果有参数则
// 					ssid = request.getParameter("ssid1").trim();
// 				} else {
// 					ssid = StringUtil.randomString(14);
// 					session.setAttribute("ssid", ssid);
// 				}

// 				long gmid = pmanager.getPlayer("GM01").getId();
// 				if (action != null) {
// 					//操作参数不为空
// 					if ("del".equals(action)) {
// 						//做删除操作
// 						String delmids[] = request.getParameterValues("delid");
// 						if (delmids != null) {
// 							gmreplay.deleteMail(delmids);
// 							amanager.save(username, "删除了 邮件，邮件长度为"
// 									+ delmids.length);
// 						}
// 					}
// 					if ("add".equals(action)) {
// 						//做回复操作
// 						boolean add = false;
// 						Object o = session.getAttribute("ssid");
// 						String ssid2 = "";
// 						if (o != null)
// 							ssid2 = o.toString();
// 						if (ssid2.equals(ssid)) {
// 							add = true;
// 							ssid = StringUtil.randomString(14);
// 							session.setAttribute("ssid", ssid);
// 						}
// 						if (add) {
// 							boolean res = true;//条件变量
// 							long mailtoid = -1;
// 							String mailId = request.getParameter("mailid");//发送ID
// 							Mail mail = null;
// 							int mid = Integer.parseInt(request
// 									.getParameter("mid"));//邮件ID
// 							if (mid != 1)
// 								mail = mmanager.getMail(mid);
// 							if (mailId != null && mailId.trim().length() > 0)
// 								mailtoid = Integer.parseInt(mailId);
// 							String playname = request.getParameter("playname");
// 							try {
// 								if (playname != null
// 										&& playname.trim().length() > 0)
// 									mailtoid = pmanager.getPlayer(
// 											playname.trim()).getId();
// 								boolean isban = Boolean.parseBoolean(request.getParameter("isban").trim());
// 								String title = request.getParameter("title");
// 								String content = request
// 										.getParameter("content");
// 								String items[] = request
// 										.getParameterValues("item");
// 								String message = "";
// 								String istr = "";
// 								if (items != null) {
// 									for (String it : items) {
// 										istr = istr + "|" + it;
// 										if (acmanager.getArticle(it.trim()) == null) {
// 											res = false;
// 											message = it + "物品不存在";
// 											break;
// 										}
// 									}
// 								}
// 								boolean yun = false;
// 								if (res) {
// 									if (mid != 1) {
// 										MailRecord mr1 = new MailRecord();
// 										Mail ma1 = mmanager.getMail(mid);
// 										mr1.setMid(mid + "");
// 										mr1.setGmname(username);
// 										mr1.setTitle(ma1.getTitle());
// 										mr1.setQcontent(ma1.getContent());
// 										mr1.setRescontent("接受");
// 										mr1.setCdate(DateUtil.formatDate(ma1
// 												.getCreateDate(),
// 												"yyyy-MM-dd HH:mm:ss"));
// 										if (pmanager
// 												.getPlayer(mail.getPoster()) != null) {
// 											Player p = pmanager.getPlayer(mail
// 													.getPoster());
// 											mr1.setUsername(p.getUsername());
// 											mr1.setPlayername(p.getName());
// 											mr1.setPlayerid(p.getId());
// 										}
// 										mr1.setItems(" ");
// 										mrmanager.save(mr1);
// 										MailRecord mr = new MailRecord();
// 										mr.setMid(mid + "");
// 										mr.setGmname(username);
// 										mr.setTitle(mail.getTitle());
// 										mr.setQcontent(mail.getContent());
// 										mr.setRescontent(content);
// 										mr.setCdate(DateUtil.formatDate(mail
// 												.getCreateDate(),
// 												"yyyy-MM-dd HH:mm:ss"));
// 										if (pmanager
// 												.getPlayer(mail.getPoster()) != null) {
// 											Player p = pmanager.getPlayer(mail
// 													.getPoster());
// 											mr.setUsername(p.getUsername());
// 											mr.setPlayername(p.getName());
// 											mr.setPlayerid(p.getId());
// 										}
// 										mr.setItems(istr);
// 										mrmanager.save(mr);
// 									} else if (mid == 1) {
// 										MailRecord mr = new MailRecord();
// 										mr.setMid(mid + "");
// 										mr.setGmname(username);
// 										mr.setTitle("主动发送");
// 										mr.setQcontent("主动发送");
// 										mr.setRescontent(content);
// 										mr.setCdate(DateUtil.formatDate(
// 												new java.util.Date(),
// 												"yyyy-MM-dd HH:mm:ss"));
// 										if (pmanager.getPlayer(mailtoid) != null) {
// 											Player p = pmanager
// 													.getPlayer(mailtoid);
// 											mr.setUsername(p.getUsername());
// 											mr.setPlayername(p.getName());
// 											mr.setPlayerid(p.getId());
// 										}
// 										mr.setItems(istr);
										
// 										mrmanager.save(mr);
// 									}
// 									yun = gmreplay.sendMail(gmid, mid,
// 											mailtoid, items,isban, title, content);
// 								} else {
// 									out.print(message);
// 								}
// 								if (!yun)
// 									out.print("发送失败！");
// 								else
// 									out.print("发送成功！");
// 							} catch (Exception e) {
// 								e.printStackTrace();
// 								out.print("角色名称不存在或者角色已删除！！");
// 							}
// 						} else {
// 							out.print("<br/>重复发送！发送失败！！<br/>");
// 						}
// 					}
// 				}
// 				out.print("<input type='hidden' value='" + ssid
// 						+ "' id='ssid1' name='ssid1' />");
// 				String gmName = session.getAttribute("gmid").toString();//判断是否登录
// 				String paname = request.getParameter("paname");//获取过滤参数
// 				String pid = request.getParameter("pid");//获取过滤参数
// 				String mcontent = request.getParameter("mcontent");
// 				int start = 0;
// 				int size = 20;
// 				long totalsize = gmreplay.getGmMailSize("GM01");//总数为GM01的邮件
// 				if (request.getParameter("start") != null)
// 					start = Integer.parseInt(request.getParameter("start"));
// 				if (request.getParameter("starty") != null) {
// 					int starty = Integer.parseInt(request
// 							.getParameter("starty").trim());
// 					if (starty < 1 || starty > ((totalsize - 1) / 20 + 1))
// 						out.print("请输入正确的页码");
// 					else
// 						start = (starty - 1) * 20;
// 				}
// 				String stime = request.getParameter("stime");  
// 				String etime = request.getParameter("etime");
// 				List<Mail> mails = new ArrayList<Mail>();
// 				if ((pid == null || "".equals(pid.trim()))
// 						&& (paname == null || "".equals(paname.trim()))
// 						&& (stime == null || etime == null)
// 						&& (mcontent == null || "".equals(mcontent.trim())))//如果没有过滤过程则获取固定长度的邮件
// 					mails = gmreplay.getGmMail("GM01", start, size);
// 				if (paname != null && !"".equals(paname)) {//如果过滤参数不为空则获取过滤邮件
// 					out.print(paname);
// 					// paname = URLDecoder.decode(paname);
// 					List<Mail> ms = gmreplay.getGmmails();//获取所有的GM01邮件
// 					mails = new ArrayList<Mail>();
// 					for (Mail m : ms) {//如果邮件的发件人和过滤人相同，则获取！
// 						try {
// 							if (paname.equals(pmanager.getPlayer(m.getPoster())
// 									.getName())
// 									&& m.getStatus() != 2)
// 								mails.add(m);
// 						} catch (Exception e) {
// 							continue;
// 						}
// 					}

// 				}
// 				if (pid != null && !"".equals(pid)) {//如果过滤参数不为空则获取过滤邮件
// 					out.print(pid);
// 					// paname = URLDecoder.decode(paname);
// 					List<Mail> ms = gmreplay.getGmmails();//获取所有的GM01邮件
// 					mails = new ArrayList<Mail>();
// 					for (Mail m : ms) {//如果邮件的发件人和过滤人相同，则获取！
// 						try {
// 							if (m.getPoster() == Integer.parseInt(pid.trim())
// 									&& m.getStatus() != 2)
// 								mails.add(m);
// 						} catch (Exception e) {
// 							continue;
// 						}
// 					}

// 				}
// 				if (mcontent != null && !"".equals(mcontent.trim())) {//如果过滤参数不为空则获取过滤邮件
// 					out.print(mcontent);
// 					// paname = URLDecoder.decode(paname);
// 					List<Mail> ms = gmreplay.getGmmails();//获取所有的GM01邮件
// 					mails = new ArrayList<Mail>();
// 					for (Mail m : ms) {//如果邮件的发件人和过滤人相同，则获取！
// 						try {
// 							if (m.getContent().contains(mcontent))
// 								mails.add(m);
// 						} catch (Exception e) {
// 							continue;
// 						}
// 					}

// 				}
// 				Date sdate = null;
// 				Date edate = null;
// 				if (stime != null && etime != null) {
// 					sdate = DateUtil.parseDate(stime, "yyyy-MM-dd HH:mm");
// 					edate = DateUtil.parseDate(etime, "yyyy-MM-dd HH:mm");
// 					mails = gmreplay.getBetweenMail("GM01", sdate, edate);
// 					out.print("时间过滤长度为" + mails.size());
// 				}
// 				List<String> customs = cmanager.getCustoms();//获取所有自定义回复留言
// 				List<String> gmitems = gmanager.getItemNames();//获取搜有礼品
// 				out
// 						.print("<br/>过滤查询(通过角色名)：<input type='text' id='paname' name ='paname' value='' />");
// 				out
// 						.print("<input type='button' value='查询' onclick='query();' /><br/>");
// 				out
// 						.print("过滤查询(通过角色ID)：<input type='text' id='pid' name ='pid' value='' />");
// 				out
// 						.print("<input type='button' value='查询' onclick='querybyId();' /><br/>");
// 				out
// 						.print("过滤查询(通过邮件内容)：<input type='text' id='mcontent' name ='mcontent' value='' />");
// 				out
// 						.print("<input type='button' value='查询' onclick='queryc();' /><br/>");
// 				String ss[] = DateUtil.formatDate(new Date(),
// 						"yyyy-MM-dd-HH-mm").split("-");
// 				out
// 						.print("开始时间：<input type='text' id='syear' name='syear' size='3' value='"
// 								+ (sdate == null ? ss[0] : DateUtil.formatDate(
// 										sdate, "yyyy")) + "' >年");
// 				out
// 						.print("<input type='text' id='smonth' name='smonth' size='3' value='"
// 								+ (sdate == null ? ss[1] : DateUtil.formatDate(
// 										sdate, "MM")) + "' >月");
// 				out
// 						.print("<input type='text' id='sday' name='sday' size='3' value='"
// 								+ (sdate == null ? ss[2] : DateUtil.formatDate(
// 										sdate, "dd")) + "' >日");
// 				out
// 						.print("<input type='text' id='shour' name='shour' size='3' value='"
// 								+ (sdate == null ? ss[3] : DateUtil.formatDate(
// 										sdate, "HH")) + "' >时");
// 				out
// 						.print("<input type='text' id='sminute' name='sminute' size='3' value='"
// 								+ (sdate == null ? ss[4] : DateUtil.formatDate(
// 										sdate, "mm")) + "' >分 ~ ");
// 				out
// 						.print("结束时间：<input type='text' id='eyear' name='eyear' size='3' value='"
// 								+ (edate == null ? ss[0] : DateUtil.formatDate(
// 										edate, "yyyy")) + "' >年");
// 				out
// 						.print("<input type='text' id='emonth' name='emonth' size='3' value='"
// 								+ (edate == null ? ss[1] : DateUtil.formatDate(
// 										edate, "MM")) + "' >月");
// 				out
// 						.print("<input type='text' id='eday' name='eday' size='3' value='"
// 								+ (edate == null ? ss[2] : DateUtil.formatDate(
// 										edate, "dd")) + "' >日");
// 				out
// 						.print("<input type='text' id='ehour' name='ehour' size='3' value='"
// 								+ (edate == null ? ss[3] : DateUtil.formatDate(
// 										edate, "HH")) + "' >时");
// 				out
// 						.print("<input type='text' id='eminute' name='eminute' size='3' value='"
// 								+ (edate == null ? ss[4] : DateUtil.formatDate(
// 										edate, "mm")) + "' >分  ");
// 				out
// 						.print("<input type='button' value='查询' onclick='queryBydate();' /><br/>");
// 				if (paname == null && mcontent == null && pid == null
// 						&& stime == null && etime == null) {//如果没有过滤则显示分页
// 					//out.print(start + "|" + totalsize);
// 					out.print("<a href='?start=1'>首页</a>  ");
// 					if (start > 0)
// 						out.print("<a href='?start=" + (start - 20)
// 								+ "'>上一页</a>  ");
// 					out.print((start / 20 + 1) + "/"
// 							+ ((totalsize - 1) / 20 + 1));
// 					if ((start + 20) < totalsize)
// 						out.print("  <a href='?start=" + (start + 20)
// 								+ "' >下一页</a>  ");
// 					out.print("  <a href='?starty="
// 							+ ((totalsize - 1) / 20 + 1) + "'>末页</a>");
// 					out
// 							.print("转到<input type='text' size=5 value='' name='starty0' id='starty0' /><a href='javascript:jump(\""
// 									+ 0 + "\");'>页</a>");
// 				} else
// 					out.print("过滤长度为：" + mails.size());
// 				StringBuffer tablehead = new StringBuffer(
// 						"<form action=\"\" name=\"form2\" method=\"post\"><input type=\"hidden\" name=playerName value=\""
// 								+ gmName
// 								+ "\" /> <input type=hidden name=stype value=\"delete\">");
// 				tablehead
// 						.append("<table style='TABLE-LAYOUT: fixed; WORD-WRAP: break-word'  width='100%' ><tr >");
// 				tablehead.append("<th  width='5%' nowrap=\"nowrap\">ID</td>");
// 				tablehead.append("<th  width='5%' nowrap=\"nowrap\">账号</th>");
// 				tablehead.append("<th  width='5%' nowrap=\"nowrap\">发件时间</th>");
// 				tablehead.append("<th  width='10%' nowrap=\"nowrap\">发件人</th>");
// 				tablehead.append("<th  width='10%' nowrap=\"nowrap\">标题</th>");
// 				tablehead.append("<th  width='20%' nowrap=\"nowrap\">内容</th>");
// 				tablehead.append("<th  width='15%' nowrap=\"nowrap\">附件</th>");
// 				tablehead
// 						.append("<th  width='20%' nowrap=\"nowrap\">回复内容</th>");
// 				tablehead
// 						.append("<th  width='10%' nowrap=\"nowrap\">操作</th></tr>");
// 				out.print(tablehead.toString());
// 				double yidu = 0.0d;
// 				double weidu = 0.0d;
// 				if (mails != null) {
// 					for (Mail ml : mails) {//遍历邮件

// 						Mail m = mmanager.getMail(ml.getId());
// 						if (ml.getStatus() == Mail.NORMAL_UNREAD)
// 							weidu++;
// 						if (ml.getStatus() == Mail.NORMAL_READED)
// 							yidu++;
// 						StringBuffer tablebody = new StringBuffer("<tr ");
// 						if (m.getStatus() == 1)
// 							tablebody.append("style='color:red'  ");
// 						tablebody.append("><td  ");
// 						if (m.getStatus() == 1)
// 							tablebody.append("style='color:red'  ");
// 						tablebody.append(">" + m.getStatus());
// 						tablebody.append(
// 								" <input type='checkbox' name='delmid' value='"
// 										+ m.getId() + "'").append(
// 								"/>" + m.getId() + "</td>");
// 						try {
// 							tablebody.append("<td>"
// 									+ pmanager.getPlayer(m.getPoster())
// 											.getUsername()
// 									+ "</td><td>"
// 									+ DateUtil.formatDate(m.getCreateDate(),
// 											"yyyy-MM-dd HH:mm:ss") + "</td>");
// 							tablebody.append("<td ");
// 							if (m.getStatus() == 1)
// 								tablebody.append("  style='color:red'  ");
// 							tablebody
// 									.append("><a href='javascript:querybyId(\""
// 											+ m.getPoster()
// 											+ "\");' >"
// 											+ pmanager.getPlayer(m.getPoster())
// 													.getName()
// 											+ "("
// 											+ m.getPoster()
// 											+ ")</a><input type='button' value='聊天' onclick='window.location.replace(\"gm_chat.jsp?action=a&playerid="
// 											+ ml.getPoster()
// 											+ "\")' /><input type='hidden' id='"
// 											+ m.getId() + "receiver' value='"
// 											+ m.getPoster() + "'</td>");
// 						} catch (Exception e) {
// 							tablebody.append("<td>"
// 									+ m.getPoster()
// 									+ "[该角色已删除]</td><td>"
// 									+ DateUtil.formatDate(m.getCreateDate(),
// 											"yyyy-MM-dd HH:mm:ss") + "</td>");
// 							tablebody.append("<td ");
// 							if (m.getStatus() == 1)
// 								tablebody.append("  style='color:red'  ");
// 							tablebody
// 									.append("><a href='javascript:querybyId(\""
// 											+ m.getPoster()
// 											+ "\");' >"
// 											+ "("
// 											+ m.getPoster()
// 											+ ")</a><input type='button' value='聊天' onclick='window.location.replace(\"gm_chat.jsp?action=a&playerid="
// 											+ ml.getPoster()
// 											+ "\")' /><input type='hidden' id='"
// 											+ m.getId() + "receiver' value='"
// 											+ m.getPoster() + "'</td>");
// 						}
// 						tablebody.append("<td  ");
// 						if (m.getStatus() == 1)
// 							tablebody.append("  style='color:red'  ");
// 						tablebody.append(">" + m.getTitle()
// 								+ "<input type='hidden' id='restitle"
// 								+ m.getId() + "' value='回复:" + m.getTitle()
// 								+ "' /></td><td  ");
// 						if (m.getStatus() == 1)
// 							tablebody.append("style='color:red'  ");

// 						tablebody.append(" >" + m.getContent());
// 						if (m.getCells() != null && m.getCells().length > 0) {
// 							tablebody.append("{");
// 							for (Knapsack.Cell cs : m.getCells()) {
// 								if (cs.getEntityId() > 0)
// 									tablebody.append(aemanager.getEntity(
// 											cs.getEntityId()).getArticleName()
// 											+ "(" + cs.getCount() + "):");
// 							}
// 							tablebody.append("}");
// 						}
// 						tablebody.append("</td><td>");
// 						tablebody.append("<input type='checkbox' id='isban' name='isban' />选中为绑定<br/>");
// 						tablebody.append("<select name='item' id='item"
// 								+ m.getId() + "' multiple=true  size='5' >");
// 						if (gmitems != null && gmitems.size() > 0) {
// 							for (String item : gmitems)
// 								//遍历gm礼品
// 								tablebody.append("<option value='" + item
// 										+ "' >" + item + "</option>");
// 						}
// 						tablebody.append("</select></td><td>");
// 						tablebody
// 								.append("<select style='width:98%' name='repcus' id ='repcus' onchange='change("
// 										+ m.getId()
// 										+ ",this);' ><option value=''>--</option>");
// 						if (customs != null && customs.size() > 0) {
// 							for (String cu : customs)
// 								//遍历gm自定义数据
// 								tablebody.append("<option value='" + cu + "'>"
// 										+ cu + "</option>");
// 						}
// 						tablebody.append("</select>");
// 						tablebody
// 								.append("<textarea style='width:98%' cows='5' name ='repcontent' id='repcontent"
// 										+ m.getId() + "' value=''></textarea>");

// 						tablebody.append("</td><td>");
// 						tablebody
// 								.append("<input type='button' value='回复' onclick='replay("
// 										+ m.getId() + ")' /></td></tr>");
// 						out.print(tablebody.toString());

// 					}

// 				}

// 				out
// 						.print("<tr><td colspan='10' ><input type='button' value='全选' onclick='chooseall();'/><input type='button' value='全消' onclick='unsetall();'/><input type='button' value='删除选中的' onclick='del();'/><input type='button' value='重置' onclick='resetm();' /></td></tr></table></form>");
// 				out.print("已读：" + yidu + "   未读：" + weidu + "回复率："
// 						+ (yidu / (yidu + weidu)));
// 				//跳到下载页面
// 				out.print("<form action='question_down1.jsp' method='get'>");
// 				if (pid != null && !"".equals(pid.trim()))
// 					out
// 							.print("<input type='hidden' id='pid' name='pid' value='"
// 									+ pid + "' />");
// 				if (mcontent != null && !"".equals(mcontent.trim()))
// 					out.print("<input type='hidden' name='mcontent' value='"
// 							+ mcontent + "' />");
// 				if (stime != null && etime != null)
// 					out.print("<input type='hidden' name='stime' value='"
// 							+ stime
// 							+ "' /><input type='hidden' name='etime' value='"
// 							+ etime + "'>");
// 				out
// 						.print("<input type='hidden' name='gname' id='gname' value='GM01'/> ");
// 				out.print("<input type='submit' value='下载' /></form>");
// 				if (paname == null && mcontent == null && pid == null
// 						&& stime == null && etime == null) {//如果没有过滤则显示分页
// 					//out.print(start + "|" + totalsize);
// 					out.print("<a href='?starty=1'>首页</a>  ");
// 					if (start > 0)
// 						out.print("  <a href='?start=" + (start - 20)
// 								+ "'>上一页</a>  ");
// 					out.print((start / 20 + 1) + "/"
// 							+ ((totalsize - 1) / 20 + 1));
// 					if ((start + 20) < totalsize)
// 						out.print("  <a href='?start=" + (start + 20)
// 								+ "' >下一页</a>  ");
// 					out.print("  <a href='?starty="
// 							+ ((totalsize - 1) / 20 + 1) + "'>末页</a>  ");
// 					out
// 							.print("转到<input type='text' size=5 value='' name='starty1' id='starty1' /><a href='javascript:jump(\""
// 									+ 1 + "\");'>页</a>");
// 				}
// 				//选择发送
// 				out
// 						.print("<br/><br/><br/><table width='60%' ><tr><th>角色名</th><td class='top'><input type='text' id='playname' name='playname' value=''/>或ID:<input type ='text' id='mailid' name='mailid' value='' /> </td></tr>");
// 				out
// 						.print("<tr><th>标题</th><td><input type='text' id='restitle1' value='有问题' /></td></tr>");
// 				out
// 						.print("<tr><th>礼品</th><td><input type='checkbox' id='isban1' name='isban1' />选中为绑定</br><select name='item' id='item1' multiple=true size='5' >");
// 				for (String item : gmitems)
// 					out.print("<option value='" + item + "' >" + item
// 							+ "</option>");
// 				out.print("</select></td></tr><tr><th>回复内容</th><td>");
// 				out
// 						.print("<select name='repcus' style='width:50%' id ='repcus' onchange='change(1,this);' ><option value=''>--</option>");
// 				for (String cu : customs)
// 					out.print("<option value='" + cu + "'>" + cu + "</option>");
// 				out
// 						.print("</select><br/><textarea style='width:50%' rows='5' name ='repcontent' id='repcontent1' value=''></textarea>");
// 				out.print("</td></tr>");
// 				out
// 						.print("<tr><td colspan=2 ><input type='button' value='发送' onclick='send()' /></td></tr></table>");
// 				out.print("<div id='ss' style='display:none;'> <form id='f5' action='' method='post' ></form></div>");
// 			} catch (Exception e) {
// 				out.println(StringUtil.getStackTrace(e));
// 				//e.printStackTrace();
// 				//RequestDispatcher rdp = request
// 				//		.getRequestDispatcher("visitfobiden.jsp");
// 				//rdp.forward(request, response);

// 			}
		%>
	</body>
