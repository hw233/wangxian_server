<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Props"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.YinPiaoEntity"%>
<%@page import="com.fy.engineserver.green.GreenServerManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.TeamSubSystem"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>重新加载随机礼包</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
</script>
	</head>
	<body>
		<br> 
		<%
			String pathname = "/home/game/resin_server/webapps/game_server/WEB-INF/game_init_config/article.xls";
			
			//过滤有问题的礼包礼包
			try {
				File file = new File(pathname);
				InputStream is = new FileInputStream(file);
				POIFSFileSystem pss = new POIFSFileSystem(is);
				HSSFWorkbook workbook = new HSSFWorkbook(pss);
				HSSFSheet sheet = workbook.getSheetAt(ArticleManager.随机宝箱所在sheet);
				ArrayList<RandomPackageProps> allProps = new ArrayList<RandomPackageProps>();
				ArrayList<Article> allArticle = new ArrayList<Article>();
				int rows = sheet.getPhysicalNumberOfRows();
				for (int r = 1; r < rows; r++) {
					HSSFRow row = sheet.getRow(r);
					if (row != null) {
						RandomPackageProps a = new RandomPackageProps();
						try {
							ArticleManager.getInstance().设置物品一般属性(a, row);
							ArticleManager.设置一般道具属性((Props) a, row);
							r = 设置随机宝箱道具属性(a, sheet, r, rows);
						} catch (Exception e) {
							out.print("articleinit随机宝箱所在sheet,[行数:" + r + "] ["+e+"]");
							e.printStackTrace();
							throw e;
						}
						allProps.add(a);
						allArticle.add(a);
// 						out.print("[随机宝箱] [行：" + r + "] [总行：" + rows + "] [物品：" + a.getName() + "]");
					}
				}

				//map添加值
				Class c = ArticleManager.class;
				ArticleManager amm = ArticleManager.getInstance();
				Field f = c.getDeclaredField("allArticleNameMap");
				Field f2 = c.getDeclaredField("allArticleCNNameMap");
				Field f3 = c.getDeclaredField("allRandomPackageProps");
				f.setAccessible(true);
				f2.setAccessible(true);
				f3.setAccessible(true);
				f3.set(amm, allProps.toArray(new RandomPackageProps[0]));
	
				Map<String,Article> allNameMap = (Map<String,Article>)f.get(amm);
				Map<String,Article> allNCNameMap = (Map<String,Article>)f2.get(amm);
				Article[] all = ArticleManager.getInstance().getAllArticles();
				for (Article a : allArticle) {
					allNameMap.put(a.getName(), a);
					allNCNameMap .put(a.getName_stat(), a);
					for (int i = 0;i < all.length;i++) {
						Article aa = all[i];
						if (aa.getName().equals(a.getName())) {
							all[i] = a;
						}
					}
				}
				//
				out.print("加载成功:allArticle:"+allArticle.size());
			} catch (Exception e) {
				e.printStackTrace();
				out.print("[随机宝箱] [异常：" + e + "]");
			}
		%>
		
		<%!
		public static int 设置随机宝箱道具属性(RandomPackageProps a, HSSFSheet sheet, int start, int max) throws Exception {
			int num = start;
			ArrayList<String> articleNames = new ArrayList<String>();
			ArrayList<String> articleNames_stat = new ArrayList<String>();
			ArrayList<Integer> probs = new ArrayList<Integer>();
			ArrayList<Integer> counts = new ArrayList<Integer>();
			ArrayList<Integer> articleColors = new ArrayList<Integer>();
			for (; num < max; num++) {
				HSSFRow row = sheet.getRow(num);
				try {
					HSSFCell cell = row.getCell(ArticleManager.所有物品_物品名_列);
					String name = (cell.getStringCellValue().trim());
					if (!name.equals(a.getName())) {
						ArticleProperty[] aps = new ArticleProperty[articleNames.size()];
						for (int j = 0; j < aps.length; j++) {
							aps[j] = new ArticleProperty();
							aps[j].setArticleName(articleNames.get(j));
							aps[j].setProb(probs.get(j));
							aps[j].setCount(counts.get(j));
							aps[j].setColor(articleColors.get(j));
						}
						a.setApps(aps);

						ArticleProperty[] aps_stat = new ArticleProperty[articleNames_stat.size()];
						for (int j = 0; j < aps_stat.length; j++) {
							aps_stat[j] = new ArticleProperty();
							aps_stat[j].setArticleName(articleNames_stat.get(j));
							aps_stat[j].setProb(probs.get(j));
							aps_stat[j].setCount(counts.get(j));
							aps_stat[j].setColor(articleColors.get(j));
						}
						a.setApps_stat(aps_stat);
						break;
					} else {
						try {

							cell = row.getCell(ArticleManager.随机宝箱道具_物品绑定标记);
							byte bindType = (byte) cell.getNumericCellValue();
							a.setOpenBindType(bindType);

							cell = row.getCell(ArticleManager.随机宝箱道具_包裹中的物品广播);
							String str = cell.getStringCellValue().trim();
							a.setSendMessageArticles(str);

							cell = row.getCell(ArticleManager.随机宝箱道具_包裹中的物品广播_STAT);
							String str_stat = cell.getStringCellValue().trim();
							a.setSendMessageArticles_stat(str_stat);

							cell = row.getCell(ArticleManager.随机宝箱道具_消耗的道具名);
							String costname = cell.getStringCellValue().trim();
							a.setCostName(costname);

							cell = row.getCell(ArticleManager.随机宝箱道具_消耗的道具名_STAT);
							String costname_stat = cell.getStringCellValue().trim();
							a.setCostName_stat(costname_stat);

							cell = row.getCell(ArticleManager.随机宝箱道具_消耗的道具颜色);
							int costcolor = (int) cell.getNumericCellValue();
							a.setCostColor(costcolor);

							cell = row.getCell(ArticleManager.随机宝箱道具_消耗的道具数量);
							int costnum = (int) cell.getNumericCellValue();
							a.setCostNum(costnum);
						} catch (Exception e) {
							e.printStackTrace();
// 							JspWriter.print("[测试======] [" + a.getName() + "] [" + a.getParticleName() + "]");
						}
						try {

							cell = row.getCell(ArticleManager.随机宝箱道具_使用时播放哪个粒子);
							String particleName = cell.getStringCellValue().trim();
							a.setParticleName(particleName);

							cell = row.getCell(ArticleManager.随机宝箱道具_播放粒子时长);
							long delayTime = (long) cell.getNumericCellValue();
							a.setDelayTime(delayTime);

							cell = row.getCell(ArticleManager.随机宝箱道具_播放位置上);
							double upValue = cell.getNumericCellValue();
							a.setUpValue(upValue);

							cell = row.getCell(ArticleManager.随机宝箱道具_播放位置左);
							double leftValue = cell.getNumericCellValue();
							a.setLeftValue(leftValue);

							cell = row.getCell(ArticleManager.随机宝箱道具_播放位置前);
							double frontValue = cell.getNumericCellValue();
							a.setFrontValue(frontValue);

							cell = row.getCell(ArticleManager.随机宝箱道具_光效可见类型);
							byte canseeType = (byte) cell.getNumericCellValue();
							a.setCanseeType(canseeType);

							cell = row.getCell(ArticleManager.随机宝箱道具_世界广播);
							String broadcast = cell.getStringCellValue().trim();
							a.setBroadcast(broadcast);
// 							out.print("[测试======] [" + a.getName() + "] [" + a.getParticleName() + "]");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				HSSFCell cell = row.getCell(ArticleManager.随机宝箱道具_包裹中的物品名字);
				String articleName = (cell.getStringCellValue().trim());
				articleNames.add(articleName);

				cell = row.getCell(ArticleManager.随机宝箱道具_包裹中的物品名字_STAT);
				String articleName_stat = (cell.getStringCellValue().trim());
				articleNames_stat.add(articleName_stat);

				cell = row.getCell(ArticleManager.随机宝箱道具_包裹中的物品几率);
				int prob = (int) cell.getNumericCellValue();
				probs.add(prob);

				cell = row.getCell(ArticleManager.随机宝箱道具_包裹中的物品数量);
				int count = (int) cell.getNumericCellValue();
				counts.add(count);

				cell = row.getCell(ArticleManager.随机宝箱道具_包裹中的物品颜色);
				int articleColor = (int) cell.getNumericCellValue();
				articleColors.add(articleColor);
			}
			if (num == max) {
				ArticleProperty[] aps = new ArticleProperty[articleNames.size()];
				for (int j = 0; j < aps.length; j++) {
					aps[j] = new ArticleProperty();
					aps[j].setArticleName(articleNames.get(j));
					aps[j].setProb(probs.get(j));
					aps[j].setCount(counts.get(j));
					aps[j].setColor(articleColors.get(j));
				}
				a.setApps(aps);

				ArticleProperty[] aps_stat = new ArticleProperty[articleNames_stat.size()];
				for (int j = 0; j < aps_stat.length; j++) {
					aps_stat[j] = new ArticleProperty();
					aps_stat[j].setArticleName(articleNames_stat.get(j));
					aps_stat[j].setProb(probs.get(j));
					aps_stat[j].setCount(counts.get(j));
					aps_stat[j].setColor(articleColors.get(j));
				}
				a.setApps_stat(aps_stat);
			}
			return num - 1;
		}
		%>
	</body>
</html>
