<%@page import="java.text.SimpleDateFormat"%>
<%@page
	import="com.fy.engineserver.activity.base.TimesActivityManager"%>
<%@page import="com.fy.engineserver.activity.base.TimesActivity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	int blessActivityID = 3000;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	try {
		String startTime = "2013-11-23 00:00:00";
		String endTime = "2013-11-23 23:59:59";
		TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, TimesActivityManager.HEJIU_ACTIVITY);
		activity.setServerNames(new String[] { "霸气乾坤", "凌霄宝殿", "昆仑圣殿", "太虚幻境", "幽冥山谷", "万象更新", "柳暗花明", "神龙摆尾", "华山之巅", "烟雨青山", "三界奇缘", "众仙归来", "化外之境测试服", "霸气无双", "战无不胜", "仙山琼阁", });
		TimesActivityManager.instacen.addActivity(activity);
		blessActivityID++;
	} catch (Exception e) {
	}
	try {
		String startTime = "2013-11-23 00:00:00";
		String endTime = "2013-11-23 23:59:59";
		TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, TimesActivityManager.TUMOTIE_ACTIVITY);
		activity.setServerNames(new String[] { "霸气乾坤", "凌霄宝殿", "昆仑圣殿", "太虚幻境", "幽冥山谷", "万象更新", "柳暗花明", "神龙摆尾", "华山之巅", "烟雨青山", "三界奇缘", "众仙归来", "化外之境测试服", "霸气无双", "战无不胜", "仙山琼阁", });
		TimesActivityManager.instacen.addActivity(activity);
		blessActivityID++;
	} catch (Exception e) {
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>