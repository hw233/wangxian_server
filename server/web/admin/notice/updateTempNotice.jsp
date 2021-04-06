<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">

<%@page import="com.fy.engineserver.notice.NoticeManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.notice.Notice"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更新临时公告</title>
</head>
<body>

	<%
		String idst = request.getParameter("id");
		String name = request.getParameter("name");
		String content = request.getParameter("content");
		String contentInner = request.getParameter("contentInner");
		String beginTimest = request.getParameter("beginTime");
		
		if(idst == null || idst.equals("") || name == null || name.equals("") || content == null || content.equals("") ||
				contentInner == null || contentInner.equals("") ||beginTimest == null || beginTimest.equals("")){
			out.print("输入错误");
		}else{
			List<Notice> list = NoticeManager.getInstance().getOnceList();
			Notice notice = null;
			for(Notice n:list){
				if(n.getId() == Long.parseLong(idst.trim())){
					notice = n;
					break;
				}
			}
			if(notice == null){
				out.print("没有公告"+idst);
				return ;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			try{
				Date d1 = sdf.parse(beginTimest);
			
				notice.setBeginTime(d1.getTime());
				
				notice.setNoticeName(name);
				notice.setContent(content);
				notice.setContentInside(contentInner);
				notice.setEffectState(NoticeManager.失效);
				notice.setDelete(true);
				
				
				long id = 1l;
				try{
					id = NoticeManager.em.nextId();
				}catch(Exception e){
					out.print("生成id 错误");
					return ;
				}
				
				Notice n = new Notice(id);
				n.setEffectState(NoticeManager.开始);
				n.setNoticeName(notice.getNoticeName());
				n.setContent(notice.getContent());
				//临时公告
				n.setNoticeType(0);
				n.setContentInside(notice.getContentInside());
				
				n.setBeginTime(notice.getBeginTime());
				
				if(d1.getTime()<= System.currentTimeMillis()){
					out.print("输入日期错误");
					return;
				}
				NoticeManager.getInstance().createNotice(n);
				NoticeManager.logger.warn("[更新生成一个新的临时公告]");
				out.print("更新生成一个新临时公告成功");
			}catch(Exception e){
				out.print("时间格式错误");
				return;
			}
		}
	%>
</body>
</html>