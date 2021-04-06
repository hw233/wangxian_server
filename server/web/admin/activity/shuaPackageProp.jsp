<%@page import="java.util.Arrays"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiSpecialActivity"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.sqage.stat.commonstat.entity.ChongZhi"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiServerConfig"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		ArticleManager am = ArticleManager.getInstance();
		ArrayList<String> needPropNames = new ArrayList<String>();
		HashMap<String, String[]> addPropNames = new HashMap<String, String[]>();
		HashMap<String, Integer[]> addPropCounts = new HashMap<String, Integer[]>();
		HashMap<String, Integer[]> addPropColors = new HashMap<String, Integer[]>();
		try {
			InputStream is = new FileInputStream(am.getConfigFile());
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = workbook.getSheetAt(ArticleManager.宝箱所在sheet);
			int rows = sheet.getPhysicalNumberOfRows();
			String lastName = "";
			ArrayList<String> articleNames = new ArrayList<String>();
			ArrayList<Integer> counts = new ArrayList<Integer>();
			ArrayList<Integer> articleColors = new ArrayList<Integer>();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					String name = "";
					try {
						name = row.getCell(0).getStringCellValue();
					} catch (Exception e) {
					}
					name.trim();
					if (lastName.length() == 0) {
						lastName = name;
					}
					if (name.length() > 0 && !name.equals(lastName)) {
						out.println(lastName+"--" + Arrays.toString(articleNames.toArray(new String[0])) + "--" + Arrays.toString(counts.toArray(new Integer[0])) + "--" + Arrays.toString(articleColors.toArray(new Integer[0])));
						out.println("<br>");
						addPropNames.put(lastName, articleNames.toArray(new String[0]));
						addPropCounts.put(lastName, counts.toArray(new Integer[0]));
						addPropColors.put(lastName, articleColors.toArray(new Integer[0]));
						lastName = name;
						needPropNames.add(lastName);
						articleNames.clear();
						counts.clear();
						articleColors.clear();
					}
					HSSFCell cell = row.getCell(ArticleManager.宝箱道具_包裹中的物品名字);
					String articleName = (cell.getStringCellValue().trim());
					articleNames.add(articleName);

					cell = row.getCell(ArticleManager.宝箱道具_包裹中的物品数量);
					int count = (int) cell.getNumericCellValue();
					counts.add(count);

					cell = row.getCell(ArticleManager.宝箱道具_包裹中的物品颜色);
					int articleColor = (int) cell.getNumericCellValue();
					articleColors.add(articleColor);
				}
			}
		} catch (Exception e) {
			out.println(e);
		}
		try{
			Article[] as = am.getAllArticles();
			for (Article a : as) {
				if (a instanceof PackageProps) {
					PackageProps p = (PackageProps)a;
					if (needPropNames.contains(p.getName())) {
						String[] addNames = addPropNames.get(p.getName());
						Integer[] addCounts = addPropCounts.get(p.getName());
						Integer[] addColors = addPropColors.get(p.getName());
						ArticleProperty[] aps = new ArticleProperty[addNames.length];
						for (int j = 0; j < aps.length; j++) {
							aps[j] = new ArticleProperty();
							aps[j].setArticleName(addNames[j]);
							aps[j].setCount(addCounts[j]);
							aps[j].setColor(addColors[j]);
						}
						String oldSize = "" + p.getArticleNames().length;
						p.setArticleNames(aps);
						out.println("修改保箱" + p.getName() + "原来：" +oldSize + "  现在:" + p.getArticleNames().length);
						out.println("<br>");
					}
				}
			}
		}catch(Exception e) {
			out.println(e);
		}
	%>
</body>
</html>
