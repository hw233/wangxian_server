
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.gm.newfeedback.NewFeedbackManager"%><%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@ page language="java" pageEncoding="UTF-8" import="com.jspsmart.upload.*"%><%
	String actionname = request.getParameter("actionname");
	Object o = session.getAttribute("authorize.username");
	if(o==null && !GameConstants.getInstance().getServerName().equals("潘多拉星")){
		out.print("下载失败，请重新登录再下载。");
		return;
	}
	if(actionname!=null && actionname.equals("ok")){
		String fs = request.getParameter("filename");
		if(fs!=null && fs.length()>0){
		 	response.reset();//必须reset，否则会出现文件不完整
			SmartUpload su = new SmartUpload();
			su.initialize(pageContext);
			su.setContentDisposition(null);
			try{
				//SmartUpload这个下载工具，执行下载的时候，在Java脚本范围外（即<%% >之外），不要包含HTML代码、空格、回车或换行等字符，有的话将不能正确下载。 
				su.downloadFile(fs);
				response.getOutputStream().close(); 
		 		out.print(fs+"下载成功<br>");
				StringBuffer str = new StringBuffer();
				str.append("["+GameConstants.getInstance().getServerName()+"] ").append("[具体操作] ").append("[").append(o.toString()).append("] ").append("[").append(TimeTool.formatter.varChar19.format(System.currentTimeMillis())).append("] ").append("[").append("下载").append("] ").append("[").append(fs).append("] ");
				NewFeedbackManager.logger.warn(str.toString());
		 	}catch(Exception e){
		 		out.print(fs+" 下载异常："+e);
		 	}	
		}else{
			out.print("请选择要上传的文件-------");
			out.print("<a href='gameResourceUpdateTool_downlist.jsp'>返回</a>");
		}
	}else{
		out.print("非法");
	}
%>
