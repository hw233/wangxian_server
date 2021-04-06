<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.mail.Mail"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!Map<String, Set<Long>> map = new HashMap<String, Set<Long>>();%>
<%

	String pwd = request.getParameter("pwd");

	add(new Data("似水流年",1254000000000023111L));
	add(new Data("卧虎藏龙",1285000000000002814L));
	add(new Data("华夏战神",1205000000000840545L));
	add(new Data("飘渺王城",1102000000006764094L));
	add(new Data("飘渺王城",1102000000003302875L));
	add(new Data("飘渺王城",1102000000005432505L));
	add(new Data("飘渺王城",1102000000006989271L));
	add(new Data("飘渺王城",1102000000004429285L));
	add(new Data("飘渺王城",1102000000009062923L));
	add(new Data("飘渺王城",1102000000002318102L));
	add(new Data("飘渺王城",1102000000007419313L));
	add(new Data("飘渺王城",1102000000006374963L));
	add(new Data("飘渺王城",1102000000006579413L));
	add(new Data("飘渺王城",1102000000006866209L));
	add(new Data("飘渺王城",1102000000000576936L));
	add(new Data("飘渺王城",1102000000001293513L));
	add(new Data("蓬莱仙阁",1116000000001762146L));
	add(new Data("蓬莱仙阁",1116000000000614727L));
	add(new Data("蓬莱仙阁",1116000000000369159L));
	add(new Data("蓬莱仙阁",1116000000000350353L));
	add(new Data("蓬莱仙阁",1116000000002028469L));
	add(new Data("蓬莱仙阁",1116000000001167983L));
	add(new Data("蓬莱仙阁",1116000000000635493L));
	add(new Data("蓬莱仙阁",1116000000006207003L));
	add(new Data("蓬莱仙阁",1116000000000471064L));
	add(new Data("蓬莱仙阁",1116000000001147421L));
	add(new Data("蓬莱仙阁",1116000000000512773L));
	add(new Data("峨嵋金顶",1118000000000348746L));
	add(new Data("峨嵋金顶",1118000000000226077L));
	add(new Data("峨嵋金顶",1118000000000491796L));
	add(new Data("峨嵋金顶",1118000000000143994L));
	add(new Data("峨嵋金顶",1118000000001208835L));
	add(new Data("峨嵋金顶",1118000000002703669L));
	add(new Data("峨嵋金顶",1118000000000880766L));
	add(new Data("峨嵋金顶",1118000000000082758L));
	add(new Data("峨嵋金顶",1118000000000532615L));
	add(new Data("峨嵋金顶",1118000000003461248L));
	add(new Data("峨嵋金顶",1118000000000696484L));
	add(new Data("峨嵋金顶",1118000000000513835L));
	add(new Data("峨嵋金顶",1118000000002744327L));
	add(new Data("峨嵋金顶",1118000000000389850L));
	add(new Data("峨嵋金顶",1118000000003338351L));
	add(new Data("峨嵋金顶",1118000000000043751L));
	add(new Data("峨嵋金顶",1118000000000513948L));
	add(new Data("峨嵋金顶",1118000000000553692L));
	add(new Data("峨嵋金顶",1118000000001413690L));
	add(new Data("峨嵋金顶",1118000000000553760L));
	add(new Data("峨嵋金顶",1118000000000044899L));
	add(new Data("峨嵋金顶",1118000000000204801L));
	add(new Data("峨嵋金顶",1118000000001168121L));
	add(new Data("峨嵋金顶",1118000000000553710L));
	add(new Data("峨嵋金顶",1118000000000062502L));
	add(new Data("峨嵋金顶",1118000000000044329L));
	add(new Data("峨嵋金顶",1118000000000716907L));
	add(new Data("峨嵋金顶",1118000000000205234L));
	add(new Data("峨嵋金顶",1118000000003010662L));
	add(new Data("勇者无敌",1184000000001373289L));
	add(new Data("问鼎江湖",1192000000000006492L));
	add(new Data("问鼎江湖",1192000000000006414L));
	add(new Data("绝代风华",1239000000000011647L));
	add(new Data("绝代风华",1239000000000103425L));
	add(new Data("绝代风华",1239000000000143916L));
	add(new Data("天之骄子",1277000000000022020L));
	add(new Data("天之骄子",1277000000000022146L));
	add(new Data("天之骄子",1277000000000226372L));
	add(new Data("天之骄子",1277000000000186025L));
	add(new Data("天之骄子",1277000000000123169L));
	add(new Data("天之骄子",1277000000000031038L));
	add(new Data("天之骄子",1277000000000021087L));
	add(new Data("天之骄子",1277000000000020554L));
	add(new Data("天之骄子",1277000000000023143L));
	add(new Data("天之骄子",1277000000000185690L));
	add(new Data("天之骄子",1277000000000083524L));
	add(new Data("明空梵天",1138000000002642270L));
	add(new Data("龙争虎斗",1257000000000819334L));
	add(new Data("龙争虎斗",1257000000000512053L));
	add(new Data("龙争虎斗",1257000000000512197L));
	add(new Data("龙争虎斗",1257000000001106821L));
	add(new Data("龙争虎斗",1257000000000246362L));
	add(new Data("龙争虎斗",1257000000000205600L));
	add(new Data("龙争虎斗",1257000000000533329L));
	add(new Data("龙争虎斗",1257000000000205620L));
	add(new Data("龙争虎斗",1257000000000533309L));
	add(new Data("龙争虎斗",1257000000000779086L));
	add(new Data("龙争虎斗",1257000000001679602L));
	add(new Data("群雄逐鹿",1282000000000021412L));
	add(new Data("群雄逐鹿",1282000000000020842L));
	add(new Data("洪武大帝",1294000000000009355L));
	add(new Data("洪武大帝",1294000000000002067L));
	add(new Data("洪武大帝",1294000000000002206L));
	add(new Data("洪武大帝",1294000000000165005L));
	add(new Data("洪武大帝",1294000000000002235L));
	add(new Data("洪武大帝",1294000000000005078L));
	add(new Data("洪武大帝",1294000000000225654L));
	add(new Data("墨染流年",1301000000000021106L));
	add(new Data("修真境界",1308000000000043706L));
	add(new Data("修真境界",1308000000000041704L));
	add(new Data("修真境界",1308000000000046852L));
	add(new Data("修真境界",1308000000000043202L));
	add(new Data("修真境界",1308000000000044401L));
	add(new Data("修真境界",1308000000000043865L));


	GameConstants gc = GameConstants.getInstance();
	String serverName = gc.getServerName();
	out.print("<H1>当前伺服器:" + serverName + "</H1>");
	if (!map.containsKey(serverName)) {
		out.print("<H1>本服务器无数据</H1>");
		return;
	}
	Set<Long> idSet = map.get(serverName);

	if ("@#$qwefgh".equals(pwd)) {
		Long[] server_ids = idSet.toArray(new Long[0]);
		MailManager mm = MailManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		for (long playerId : server_ids) {
			Player p = null;
			try {
				p = PlayerManager.getInstance().getPlayer(playerId);
			} catch (Exception e) {
				out.print("<H2>[" + serverName + "]角色不存在[null]:" + playerId + "</H2>");
				continue;
			}
			try {
				if (p != null) {
					
					QuerySelect<Mail> querySelect = new QuerySelect<Mail>();
					List<Mail> mails = querySelect.selectAll(Mail.class, "status<>? and receiver=?", new Object[]{Mail.DELETED,p.getId()} , null, MailManager.getInstance().em);
					
					int removeMailNum = 0;
					for (Mail mail : mails) {
						if (mail.getPoster() == -1 && mail.getTitle().equals("活跃度大抽奖")) {
							mm.deleteMail(mail.getId());
							removeMailNum++;
						}
					}
					MailManager.logger.error("[" + serverName + "] [" + p.getLogString() + "] [后台删除活跃度大抽奖异常邮件] [数量：" + removeMailNum + "]");
					out.print("[" + serverName + "] [后台删除活跃度大抽奖异常邮件] [" + p.getLogString() + "] [数量：" + removeMailNum + "]<br/>");

					int beibaoNum = 0;
					int fangbaoNum = 0;
					int cangkuNum = 0;

					//检查背包
					for (int i = 0; i < p.getKnapsacks_common().length; i++) {
						Knapsack knapsack = p.getKnapsacks_common()[i];
						if (knapsack == null) {
							continue;
						}
						for (int index = 0; index < knapsack.getCells().length; index++) {
							Cell cell = knapsack.getCells()[index];
							if (cell.getEntityId() > 0) {
								ArticleEntity ae = aem.getEntity(cell.getEntityId());

								int count = cell.count;

								if (ae != null && ae.getColorType() >= 1 && rightName(ae)) {
									p.getKnapsacks_common()[0].clearCell(index, "后台删除活跃度大抽奖异常获得物品", true);
									beibaoNum++;
									out.print("[" + serverName + "] [" + p.getLogString() + "] [背包] [删除物品:" + ae.getArticleName() + "] [color:" + ae.getColorType() + "] [数量:" + count + "]" + "<BR/>");
								}
							}
						}
					}
					//检查防爆包
					Knapsack fangbao = p.getKnapsack_fangbao();
					if (fangbao != null) {
						for (int index = 0; index < fangbao.getCells().length; index++) {
							Cell cell = fangbao.getCells()[index];
							if (cell.getEntityId() > 0) {

								int count = cell.count;

								ArticleEntity ae = aem.getEntity(cell.getEntityId());
								if (ae != null && ae.getColorType() >= 1 && rightName(ae)) {
									p.getKnapsack_fangbao().clearCell(index, "后台删除活跃度大抽奖异常获得物品", true);
									fangbaoNum++;
									out.print("[" + serverName + "] [" + p.getLogString() + "] [防爆背包] [删除物品:" + ae.getArticleName() + "] [color:" + ae.getColorType() + "] [数量:" + count + "]" + "<BR/>");
								}
							}
						}
					}
					//检查仓库
					Knapsack cangku = p.getKnapsacks_cangku();
					if (cangku != null) {
						for (int index = 0; index < cangku.getCells().length; index++) {
							Cell cell = cangku.getCells()[index];
							if (cell.getEntityId() > 0) {

								int count = cell.count;

								ArticleEntity ae = aem.getEntity(cell.getEntityId());
								if (ae != null && ae.getColorType() >= 1 && rightName(ae)) {
									cangku.clearCell(index, "后台删除活跃度大抽奖异常获得物品", true);
									cangkuNum++;
									out.print("[" + serverName + "] [" + p.getLogString() + "] [仓库] [删除物品:" + ae.getArticleName() + "] [color:" + ae.getColorType() + "] [数量:" + count + "]" + "<BR/>");
								}
							}
						}
					}
					if ((beibaoNum + fangbaoNum + cangkuNum) > 0) {
						Knapsack.logger.error("[" + serverName + "] [后台删除活跃度大抽奖异常获得物品] [全部] [" + p.getLogString() + "] [格子数量：" + (removeMailNum + fangbaoNum + cangkuNum) + "]");
					} else {
						out.print("[" + serverName + "] [" + p.getLogString() + "没有活跃度大抽奖异常获得物品<br/>");
					}

				} else {
					out.print("<H2>[" + serverName + "] [角色不存在[null2]:" + playerId + "</H2>");
				}
			} catch (Exception e) {
				out.print("<H2>[" + serverName + "] [处理具体数据异常:" + playerId + "</H2>");
				MailManager.logger.error("[" + serverName + "] [处理具体数据异常:" + playerId, e);
			}
		}
	} else {
		out.print("密码不正确<br/>");
	}
%>
<%!public boolean rightName(ArticleEntity ae) {
		String name = ae.getArticleName();
		if (!ae.isBinded()) {
			return false;
		}
		Set<String> aeticles = new HashSet<String>();
		aeticles.add("杏花村");
		aeticles.add("屠苏酒");
		aeticles.add("妙沁药酒");
		aeticles.add("玉液锦囊(绿色)");
		aeticles.add("绿色屠魔帖锦囊");
		aeticles.add("铭刻符");
		aeticles.add("屠魔帖●降魔");
		aeticles.add("屠魔帖●逍遥");
		aeticles.add("屠魔帖●霸者");
		aeticles.add("屠魔帖●朱雀");
		aeticles.add("屠魔帖●水晶");
		aeticles.add("屠魔帖●倚天");
		aeticles.add("屠魔帖●青虹");
		aeticles.add("屠魔帖●赤霄");
		aeticles.add("屠魔帖●震天");
		aeticles.add("屠魔帖●天罡");
		aeticles.add("屠魔帖●腾龙");
		if (aeticles.contains(name)) {
			return true;
		} else {
			return false;
		}
	}

	class Data {
		String serverName;
		long playerId;

		public Data(String serverName, long playerId) {
			this.serverName = serverName;
			this.playerId = playerId;
		}
	}

	public void add(Data data) {
		if (!map.containsKey(data.serverName)) {
			map.put(data.serverName, new HashSet<Long>());
		}
		map.get(data.serverName).add(data.playerId);
	}%>




<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.Date"%>
<%@page import="com.fy.engineserver.homestead.faery.service.QuerySelect"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>宠物拷贝</title>
</head>
<body>
<form action="">密码：<input type="password" id="pwd" name="pwd">
<input type="submit" value="submit" /></form>

</body>
</html>