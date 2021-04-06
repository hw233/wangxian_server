<%@page import="com.fy.engineserver.activity.treasure.model.TreasureArticleModel"%>
<%@page import="com.fy.engineserver.activity.treasure.TreasureActivityManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
</html>
<head>
<title>test</title>
</head>
<body>
<%
	int[] needChangeId = new int[]{304,305,311,312,313,314,315,322,323,324,325,326,332};
	String[] needAeName = new String[]{"妖魂(180)","妖魂(220)","高级天赋礼包(通用)","高级天赋礼包(精明)","高级天赋礼包(忠诚)","高级天赋礼包(谨慎)","高级天赋礼包(狡诈)"
			,"青龙精魂","朱雀精魂","白虎精魂","玄武精魂","麒麟精魂","高级奇迹蛋碎片"};
	int[] chang2Num = new int[]{2,2,5,5,5,5,5,5,5,5,5,5,5};
	int num = 0;
	for (int i=0; i<needChangeId.length; i++) {
		String name = TreasureActivityManager.instance.articleModels.get(needChangeId[i]).getArticleCNNName();
		if (needAeName[i].equals(name)) {
			int oldNum = TreasureActivityManager.instance.articleModels.get(needChangeId[i]).getArticleNum();
			TreasureActivityManager.instance.articleModels.get(needChangeId[i]).setArticleNum(chang2Num[i]);
			TreasureArticleModel m = TreasureActivityManager.instance.articleModels.get(needChangeId[i]);
			num++;
			out.println("["+ m.getId() +"] [" + m.getArticleCNNName() + "] [odlNum:" + oldNum + "] [newNum:" + m.getArticleNum() + "]<br>");
		}
	}
	out.println("总共修改数量:" + num + "<br>");
%>
</table>
</body>
</html>
