<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更新宗派每天增加繁荣度次数</title>
</head>
<body>

<%
	
	String update = request.getParameter("update");

	if(update != null && !update.equals("")){
		
		long[] ids = ZongPaiManager.em.queryIds(ZongPai.class,"");
		for(long id : ids){
			ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(id);
			zp.setDayAddProsperityNum(100);
			ZongPaiManager.logger.error("[后台修改宗派繁荣度每天增加次数] ["+zp.getLogString()+"]");
		}
		out.print("success");
		return;
	}else{
		long num = ZongPaiManager.em.count();
		out.print("宗派个数:"+num);
	}

%>


	<form action="">
		更新:<input type="text" name="update"/>
		<input type="submit" value="submit"/>
	</form>

</body>
</html>