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
<title>更新登陆公告</title>
</head>
<body>

	<%
		String idst = request.getParameter("id");
		String name = request.getParameter("name");
		String content = request.getParameter("content");
		String contentInner = request.getParameter("contentInner");
		String beginTimest = request.getParameter("beginTime");
		String endTimest = request.getParameter("endTime");
		String havast = request.getParameter("hava");
		String numst = request.getParameter("bindSivlerNum");
		
		if(idst == null || idst.equals("") || name == null || name.equals("") || content == null || content.equals("") ||
				contentInner == null || contentInner.equals("") ||beginTimest == null || beginTimest.equals("") 
				||endTimest == null || endTimest.equals("") ||havast == null || havast.equals("") ||numst == null || numst.equals("")){
			out.print("输入错误");
		}else{
			
			List<Notice> list = NoticeManager.getInstance().getCircleList();
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
				Date d2 = sdf.parse(endTimest);
				
				if(d1.getTime() >= d2.getTime()){
					out.print("时间输入错误");
					return;
				}else{
				
					notice.setBeginTime(d1.getTime());
					notice.setEndTime(d2.getTime());
					
					notice.setNoticeName(name);
					notice.setContent(content);
					notice.setContentInside(contentInner);
					Notice effect = NoticeManager.getInstance().getEffectNotice();
					if( effect != null &&effect.getId() == notice.getId()){
						notice.setEffectState(NoticeManager.失效);
						notice.setDelete(true);
						
						try{
							
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
							//登陆公告
							n.setNoticeType(1);
							n.setContentInside(notice.getContentInside());
							n.setBeginTime(notice.getBeginTime());
							n.setEndTime(notice.getEndTime());
							
							if(havast.equals("0")){
								n.setHavaBindSivler(false);
								n.setBindSivlerNum(0);
								NoticeManager.getInstance().createNotice(n);
								NoticeManager.logger.warn("[更新生成一个绑银登陆公告]");
								
								out.print("更新生成一个绑银登陆公告成功");
								
							}else{
								n.setHavaBindSivler(true);
									try{
									int num = Integer.parseInt(numst);
									n.setBindSivlerNum(num);
									NoticeManager.getInstance().createNotice(n);
									NoticeManager.logger.warn("[更新生成一个无绑银登陆公告]");
									
									out.print("更新生成一个无绑银登陆公告成功");
								}catch(Exception e){
									out.print("绑银输入错误");
								}
							}
						}catch(Exception e){
							out.print("日期输入格式错误，请重新输入");
						}
						
						out.print("<br/>清空列表成功。<br/>");
					}else{
						out.print((effect != null ?effect.getLogString():"")+"<br/>");
						out.print(notice.getLogString()+"<br/>");
					}
				}
			}catch(Exception e){
				out.print("时间格式错误");
				return;
			}
			
		}
	
	%>
</body>
</html>