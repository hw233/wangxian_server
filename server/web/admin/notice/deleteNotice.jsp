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
<title>删除公告</title>
</head>
<body>

	<%
	
		String idst = request.getParameter("id");
		if(idst == null || idst.equals("")){
			out.print("没有id");
		}else{
			
			long id = Long.parseLong(idst.trim());
			NoticeManager nm = NoticeManager.getInstance();
			List<Notice> circle = NoticeManager.getInstance().getCircleList();
			List<Notice> once = NoticeManager.getInstance().getOnceList();
			List<Notice> activity = NoticeManager.getInstance().getActivityList();
			boolean bln = false;
			for(Notice n:circle){
				if(n.getId() == id){
					bln = true;
					n.setDelete(true);
					if(nm.getEffectNotice() != null && nm.getEffectNotice().getId() == id){
						nm.setEffectNotice(null);
					}
					break;
				}
			}
			if(!bln){
				for(Notice n:once){
					if(n.getId() == id){
						bln = true;
						n.setDelete(true);
						n.setEffectState(NoticeManager.失效);
						break;
					}
				}
			}
			if(!bln){
				for(Notice n:activity){
					if(n.getId() == id){
						bln = true;
						n.setDelete(true);
						n.setEffectState(NoticeManager.失效);
						break;
					}
				}
			}
			if(!bln){
				out.print("没有notice");
			}else{
				out.print("删除成功");
			}
		}
	%>



</body>
</html>