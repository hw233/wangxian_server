<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.activity.fairylandTreasure.FairylandBox"%>
<%@page import="java.util.Random"%>
<%@page
	import="com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.activity.fairylandTreasure.ArticleForDraw"%>
<%@page
	import="com.fy.engineserver.activity.fairylandTreasure.WaitForSend"%>
<%@page import="java.util.Arrays"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试产出概率</title>
</head>
<body>
<%
	FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
	String prayType = request.getParameter("prayType");
	String boxLevel = request.getParameter("boxLevel");
%>
<form action="">祈福类型:<input type="text" name="prayType"
	value="<%=prayType%>" />(0无祈福;1人物经验;2工资类;3元气类;4神兽碎片;5技能碎片;6宠物经验类)<br>
宝箱类型:<input type="text" name="boxLevel" value="<%=boxLevel%>" />(0-仙气宝箱;1-精致仙气宝箱;2-稀有仙气宝箱)<br>
<input type="submit" value="提交"></form>
<%
	if (boxLevel != null && !"".equals(boxLevel) && prayType == null) {
		FairylandBox box = ftm.getFairylandBoxList().get(Integer.parseInt(boxLevel));
		out.print(show(0, box));
	} else if (prayType != null && !"".equals(prayType) && boxLevel != null && !"".equals(boxLevel)) {
		FairylandBox box = ftm.getFairylandBoxList().get(Integer.parseInt(boxLevel));
		out.print(show(Integer.parseInt(prayType), box));
	}
%>
<%!public String show(int prayType, FairylandBox box) {
		FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
		StringBuffer sbf = new StringBuffer();
		Random random = new Random();
		long[] tempArticleIds = new long[24];
		String[] tempArticleNames = new String[24];// 打log用的
		int target = random.nextInt(24);
		// 通过buff获得玩家祈福类型
		sbf.append("[宝箱名:" + box.getBoxNameStat() + "] [祈福状态:" + prayType + "] [祈福类型:" + ftm.prayTypeMap.get(prayType) + "]<br>");
		boolean putWorth = false;
		List<Integer> resultList = new ArrayList<Integer>();

		// 获取玩家最终得到的物品
		int randomType = ftm.getRandomType(prayType, box);
		if (randomType > 0) {
			List<ArticleForDraw> articleForDraws = ftm.getDrawMap().get(randomType);
			ArticleForDraw afd = articleForDraws.get(ftm.getRandomArticle(articleForDraws, box));
			tempArticleIds[target] = afd.getTempArticleId();
			tempArticleNames[target] = afd.getNameStat();
			resultList.add(afd.getId());
			sbf.append("[目标宝箱类型:" + ftm.prayTypeMap.get(randomType) + "] [抽中物品:" + afd.getNameStat() + "] [下标:" + target + "]<br>");
			if (!afd.isWorth()) {
				putWorth = true;
			}
		} else {
			sbf.append("[仙界宝箱] [摆放目标] [随机类型为0]<br>");
		}

		// 摆放值钱物品
		if (putWorth) {
			int before = random.nextInt(2) + 1;
			if ((target - before) >= 0) {
				boolean find = true;
				while (find) {
					randomType = ftm.getRandomType(prayType, box);
					if (randomType > 0) {
						List<ArticleForDraw> articleForDraws = ftm.getDrawMap().get(randomType);
						ArticleForDraw afd = articleForDraws.get(ftm.getRandomArticle(articleForDraws, box));
						if (afd.isWorth()) {
							tempArticleIds[target - before] = afd.getTempArticleId();
							tempArticleNames[target - before] = afd.getNameStat();
							resultList.add(afd.getId());
							find = false;
							//sbf.append("[仙界宝箱] [摆放目标前" + before + "放值钱物品:" + afd.getNameStat() + "] [下标:" + (target - before) + "]<br>");
						}
					} else {
						sbf.append("[仙界宝箱] [摆放目标前] [随机类型为0]<br>");
					}
				}
			}
			int after = random.nextInt(2) + 1;
			if ((target + after) < 24) {
				boolean find = true;
				while (find) {
					randomType = ftm.getRandomType(prayType, box);
					if (randomType > 0) {
						List<ArticleForDraw> articleForDraws = ftm.getDrawMap().get(randomType);
						ArticleForDraw afd = articleForDraws.get(ftm.getRandomArticle(articleForDraws, box));
						if (afd.isWorth()) {
							tempArticleIds[target + after] = afd.getTempArticleId();
							tempArticleNames[target + after] = afd.getNameStat();
							resultList.add(afd.getId());
							find = false;
							//sbf.append("[仙界宝箱] [摆放目标后" + after + "放值钱物品:" + afd.getNameStat() + "] [下标:" + (target + after) + "]<br>");
						}
					} else {
						sbf.append("[仙界宝箱] [摆放目标后] [随机类型为0]<br>");
					}
				}
			}
		}

		// 摆放必现物品
		int sureShow = random.nextInt(2) + 1;
		for (int i = 0; i < sureShow; i++) {
			boolean find = true;
			while (find) {
				List<ArticleForDraw> articleForDraws = null;
				if (prayType > 0) {
					articleForDraws = ftm.getDrawMap().get(prayType);
				} else {
					//sbf.append("[仙界宝箱] [无祈福,不摆放必现]<br>");
					find = false;
				}
				if (articleForDraws != null) {
					ArticleForDraw afd = articleForDraws.get(ftm.getRandomArticle(articleForDraws, box));
					int index = random.nextInt(24);
					if (resultList.contains(afd.getId())) {
						find = false;
						//sbf.append("[仙界宝箱] [摆放必现:" + afd.getNameStat() + "] [数组中已有,无需再找]<br>");
					} else if (tempArticleIds[index] <= 0 && !resultList.contains(afd.getId())) {
						tempArticleIds[index] = afd.getTempArticleId();
						tempArticleNames[index] = afd.getNameStat();
						resultList.add(afd.getId());
						find = false;
						//sbf.append("[仙界宝箱] [摆放必现:" + afd.getNameStat() + "] [下标:" + index + "]<br>");
					}
				}
			}
		}

		// 摆放剩余物品
		for (int i = 0; i < 24; i++) {
			randomType = ftm.getRandomType(prayType, box);
			if (randomType > 0) {
				List<ArticleForDraw> articleForDraws = ftm.getDrawMap().get(randomType);
				ArticleForDraw afd = articleForDraws.get(ftm.getRandomArticle(articleForDraws, box));
				// int index = random.nextInt(24);
				if (tempArticleIds[i] <= 0) {
					tempArticleIds[i] = afd.getTempArticleId();
					tempArticleNames[i] = afd.getNameStat();
					//sbf.append("[仙界宝箱] [摆放剩余:" + afd.getNameStat() + "] [下标:" + i + "]<br>");
				}
			} else {
				sbf.append("[仙界宝箱] [摆放剩余] [随机类型为0]<br>");
			}
		}

		sbf.append("[仙界宝箱] [展示物品] " + Arrays.toString(tempArticleNames) + "<br>");
		return sbf.toString();
	}%>

</body>
</html>