<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.AritcleInfoHelper"%><%@page
	import="com.fy.engineserver.datasource.article.data.entity.PropsEntity"%><%@page
	import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%><%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@ page
	language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%
	String inputStr = request.getParameter("input");
	String typeStr = request.getParameter("type");
	if (inputStr == null || typeStr == null) {
		return;
	}
	int type = Integer.valueOf(typeStr);
	if (type == 0) {//id.确切的物品
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(Long.valueOf(inputStr));
		if (ae == null) {
			out.print("<font color=red>[错误]物品不存在[id=" + inputStr + "]</font>");
			return;
		} else {
			Article article = ArticleManager.getInstance().getArticle(ae.getArticleName());
			if (article == null) {
				out.print("物品实体不存在[id=" + inputStr + "] [name:" + ae.getArticleName() + "]");
				return;
			}
			if (ae instanceof EquipmentEntity) {
				String s = ae.getArticleName() + "<BR/>";
				s +=  AritcleInfoHelper.generate(null, (EquipmentEntity) ae);
				s = s.replaceAll("\\[/color\\]", "</font>");
				s = s.replaceAll("\\[color=(.*)\\]", "<font color='$1'>");

				s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]", "<img src='/icons/$1'>");
				out.println("<pre  style='color:green;font-weight: bold;width:18px;'>" + ae.getId() + "\n" + s + "</pre><br>");
			} else if (ae instanceof PropsEntity) {
				String s = ae.getArticleName() + "<BR/>";
				s += AritcleInfoHelper.generate(null, (PropsEntity) ae);
				s = s.replaceAll("\\[/color\\]", "</font>");
				s = s.replaceAll("\\[color=(.*)\\]", "<font color='$1'>");
				s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]", "<img src='/icons/$1'>");
				out.println("<pre  style='color:green;font-weight: bold;width:18px;'>" + ae.getId() + "\n" + s + "</pre><br>");

			} else if (ae instanceof ArticleEntity) {
				String s = ae.getArticleName() + "<BR/>";
				s +=AritcleInfoHelper.generate(null, (ArticleEntity) ae);
				s = s.replaceAll("\\[/color\\]", "</font>");
				s = s.replaceAll("\\[color=(.*)\\]", "<font color='$1'>");
				s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]", "<img src='/icons/$1'>");
				out.println("<pre  style='color:green;font-weight: bold;width:18px;'>" + ae.getId() + "\n" + s + "</pre><br>");
			}
			return;
		}
	} else if (type == 1) {//name.物品模板数据
		Article article = ArticleManager.getInstance().getArticle(inputStr);
		if (article == null) {
			out.print("<h1><font color=red>[错误][物品不存在:" + inputStr + "]</font></h1>");
			return;
		}
		out.print("<h3><font color=green>[正确][物品存在:" + article.getName() + "]"+(article.isOverlap() ? "[可堆叠:" + article.getOverLapNum() + "]" :"[不可堆叠]")+"</font></h3>");
	} else {
		out.print("输入错误:type="+type);
	}
%>