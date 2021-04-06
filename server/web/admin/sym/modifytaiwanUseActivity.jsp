<%@page import="com.fy.engineserver.activity.shop.ArticleActivityOfUseAndGive"%>
<%@page import="com.fy.engineserver.activity.BaseActivityInstance"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.AllActivityManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	List<BaseActivityInstance> list = AllActivityManager.instance.allActivityMap.get(AllActivityManager.useGiveAct);
	for (BaseActivityInstance bi : list) {
		if (bi instanceof ArticleActivityOfUseAndGive) {
			if(((ArticleActivityOfUseAndGive)bi).isThisServerFit() == null && ((ArticleActivityOfUseAndGive)bi).getNeedBuyProp().getArticleCNName().equals("马上有钱礼炮")) {
				((ArticleActivityOfUseAndGive)bi).getNeedBuyProp().setArticleNum(4);
				out.println("======刷页面成功==========<br>");
			}
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>活动使用次数修改</title>
</head>
<body>

</body>
</html>