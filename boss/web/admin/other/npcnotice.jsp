<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.XmlServerManager"%>
<%@page import="com.fy.boss.gm.npcnotice.NpcNoticeManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.boss.gm.npcnotice.BoardItem"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%><%@page import="java.util.HashMap"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	
	String noticetype = request.getParameter("noticetype");
	String noticetitle = request.getParameter("name");
	String noticecontent = request.getParameter("content");
	String beginTime = request.getParameter("beginTime");
	String endTime = request.getParameter("endTime");
	String serverurl = request.getParameter("serverurl");
	Object o = session.getAttribute("authorize.username");
	String servername = "";
	XmlServerManager xsm = XmlServerManager.getInstance();
	List<XmlServer> xs =  xsm.getServers();
	if(xs!=null && xs.size()>0){
		for(XmlServer server : xs){
			if(server.getUri().contains(serverurl)){
				servername = server.getDescription();
			}
		}
	}
	serverurl = serverurl.substring(0,serverurl.indexOf("gm"))+"admin/operation/router.jsp";
	BoardItem bi = new BoardItem();
	bi.setTitle(noticetitle);
	bi.setContent(noticecontent);
	bi.setNoticetype(noticetype);
	bi.setBeginShowTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beginTime));
	bi.setEndShowTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
	bi.setServername(servername);
	bi.setAuthorName(o==null?"--":o.toString());
	NpcNoticeManager rbm = NpcNoticeManager.getInstance();
	rbm.addNotice(bi);
	HashMap headers = new HashMap();
	String reqip = request.getLocalAddr();
	String contentP = "class=com.mieshi.korea.BoardDataProc&action=signalNewData&authorize.username=koreaAdmin&authorize.password=KoreaWXadmin252@";
	if(reqip.equals("116.213.192.200")){
		contentP = "class=com.mieshi.korea.BoardDataProc&action=signalNewData&authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p";
	}
	byte[] b = HttpUtils.webPost(new URL(serverurl), contentP.getBytes(), headers, 15000, 15000);
	out.print("添加完毕"+new String(b));
%>