<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.xuanzhi.tools.cache.CacheObject"%>
<%@page import="com.fy.engineserver.activity.fateActivity.base.FateActivity"%>
<%@page import="com.fy.engineserver.activity.fateActivity.FateManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
Object os[] = FateManager.getInstance(). mCache.values().toArray(new Object[0]);
int onlineNum = 0;
for (int i = 0; i < os.length; i++) {
	try {
		Object o = os[i];
		FateActivity fa = (FateActivity) ((CacheObject) o).object;
		boolean onLine = GamePlayerManager.getInstance().isOnline(fa.getInviteId());
		if (!onLine) {
			out.print("<font color="+(onLine ? "red" : "green")+">" + fa.getActivityName() + " "+fa.getInviteId()+"----" + fa.getInvitedId()+"</font></BR>");
		}
		if (onLine) {
			onlineNum ++;
		}
	} catch (Exception e) {
		FateManager.logger.error("[情缘活动心跳异常]",e);
	}
}
out.print("总计:" + os.length + ",在线:" + onlineNum);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>