<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%

//"dailyLoginActivities"
	UnitedServerManager manager = UnitedServerManager.getInstance();
	Field f = UnitedServerManager.class.getDeclaredField("dailyLoginActivities");
	f.setAccessible(true);
	
	List<DailyLoginActivity> dailyLoginActivities  = (List<DailyLoginActivity>)f.get(manager);
	List<DailyLoginActivity> newList = read(out);
	dailyLoginActivities.clear();
	for (DailyLoginActivity dla : newList) {
		dailyLoginActivities.add(dla);
	}
	out.print("加载完成");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.unitedserver.DailyLoginActivity"%>
<%@page import="java.io.File"%>
<%@page import="jxl.Workbook"%>
<%@page import="jxl.Sheet"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="java.util.ArrayList"%>
<%@page import="jxl.Cell"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重新加载合服表每日登录活动页</title>
</head>
<body>

</body>
</html>
<%!

	public List<DailyLoginActivity> read(JspWriter out) throws Exception {
	List<DailyLoginActivity> dailyLoginActivities=new ArrayList<DailyLoginActivity>();
	File file = new File(UnitedServerManager.getInstance().getFilePath());
	out.print("FILE:" + file.getAbsolutePath() + "<BR>");
	if (!file.exists()) {
		throw new Exception("文件不存在");
	}
	try {
		Workbook workbook = Workbook.getWorkbook(file);
		
		//
		Sheet sheet5 = workbook.getSheet("合服后登陆活动");
		int rowNum5 = sheet5.getRows();

		for (int i = 1; i < rowNum5; i++) {
			Cell[] cells = sheet5.getRow(i);
			int index = 0;
			long id = Long.valueOf(cells[index++].getContents());
			String platformName = (StringTool.modifyContent(cells[index++]));
			String[] serverNames = StringTool.string2Array(StringTool.modifyContent(cells[index++]), ",", String.class);
			String day = (StringTool.modifyContent(cells[index++]));
			String articleName = (StringTool.modifyContent(cells[index++]));
			int articleColor = Integer.valueOf(StringTool.modifyContent(cells[index++]));
			int articleNum = Integer.valueOf(StringTool.modifyContent(cells[index++]));
			boolean articleBind = "true".equalsIgnoreCase(StringTool.modifyContent(cells[index++]));
			String mailTitle = (StringTool.modifyContent(cells[index++]));
			String mailContent = (StringTool.modifyContent(cells[index++]));
			ActivityProp activityProp = new ActivityProp(articleName, articleColor, articleNum, articleBind);
			Set<String> servers = new HashSet<String>();
			for (String serverName : serverNames) {
				servers.add(serverName);
			}
			DailyLoginActivity dailyLoginActivity = new DailyLoginActivity(id, PlatformManager.getInstance().getPlatformByCNName(platformName), servers, day, activityProp, mailTitle, mailContent);
			dailyLoginActivities.add(dailyLoginActivity);
			UnitedServerManager.logger.warn("[系统启动] [加载登录活动]" + dailyLoginActivity.toString());
		}
		UnitedServerManager.logger.warn("[系统初始化] [合服相关]");
	} catch (Exception e) {
		throw e;
	}
		return dailyLoginActivities;
}
%>