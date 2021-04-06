<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.HunshiEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.mail.Mail"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.stat.ArticleStatManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="java.util.LinkedList"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>扫有魂石的玩家</title>
</head>
<body>
<%!Set<Long> has = new HashSet<Long>();
	Set<Long> repet = new HashSet<Long>();
	Set<Long> pids = new HashSet<Long>(); //有紫色橙色魂石的玩家
	Set<Long> repetPids = new HashSet<Long>(); //有重复id魂石的玩家
	int num = 0;%>
<%
	SimpleEntityManager<Player> newPem = ((GamePlayerManager) GamePlayerManager.getInstance()).em;
	long count = newPem.count(Player.class, "quitGameTime>1464105600000");
	out.print("要查询的玩家数量：" + count + "<br>");
	long[] tempId = newPem.queryIds(Player.class, "quitGameTime>1464105600000", "", 1, count + 1);

	List<List<Long>> playerIdGroup = new ArrayList<List<Long>>(); //tempId分组

	for (int i = 0; i < 15; i++) {
		playerIdGroup.add(new ArrayList<Long>());
	}

	for (int i = 0; i < tempId.length; i += 15) {
		for (int inner = 0; inner < playerIdGroup.size(); inner++) {
			if (i + inner < tempId.length) {
				playerIdGroup.get(inner).add(tempId[i + inner]);
			}
		}
	}

	int total = 0;
	for (List<Long> set : playerIdGroup) {
		out.print(set.size() + "<BR>");
		total += set.size();
	}
	out.print("tempID:" + tempId.length + ",total" + total);

	//每个分组创建一个线程处理
	List<Thread> threads = new LinkedList<Thread>();
	List<FindPlayer> findPlayerList = new ArrayList<FindPlayer>();
	for (int i = 0; i < playerIdGroup.size(); i++) {
		FindPlayer fp = new FindPlayer(playerIdGroup.get(i), "扫描魂石线程" + i);
		findPlayerList.add(fp);
		threads.add(new Thread(fp, "扫描魂石线程" + i));
	}
	for (Thread t : threads) {
		t.start();
	}
	CheckEnd ce = new CheckEnd(findPlayerList);
	Thread t1 = new Thread(ce, "检查魂石线程");
	t1.start();
	while (true) {
		Thread.sleep(120000l);
		HorseManager.logger.warn("检查T1:" + ce.finish);
		if (!ce.finish) {
			continue;
		}
		long start = System.currentTimeMillis();
		HorseManager.logger.warn("[后台扫描魂石] [有紫色和橙色魂石的玩家id:" + Arrays.toString(pids.toArray(new Long[0])) + "]");

		HorseManager.logger.warn("[后台扫描魂石] 已有魂石个数：" + has.size() + "<br>");
		HorseManager.logger.warn("[后台扫描魂石] 重复id个数：" + repet.size() + "<br>");
		HorseManager.logger.warn("[后台扫描魂石] 重复id总量：" + num + "<br>");
		HorseManager.logger.warn("[后台扫描魂石] 重复id：" + Arrays.toString(repet.toArray(new Long[0])) + "<br>");

		//清理
		int deleteNum = 0;
		long start2 = System.currentTimeMillis();
		int removeIndex = 0;
		for (Long id : repetPids) {
			Player p = PlayerManager.getInstance().getPlayer(id);
			for (Soul s : p.getSouls()) {
				ArrayList<Long> horseId = s.getHorseArr();

				//魂石面板
				for (Long hid : horseId) {
					Horse h = HorseManager.em.find(hid);
					long[] hunshiArray = h.getHunshiArray();
					if (hunshiArray != null && hunshiArray.length > 0) {
						for (int k = 0; k < hunshiArray.length; k++) {
							if (hunshiArray[k] > 0 && repet.contains(hunshiArray[k])) {
								ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hunshiArray[k]);
								hunshiArray[k] = -1;
								// 统计
								if (ae != null) {
									ArticleStatManager.addToArticleStat(null, p, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "刷魂石", null);
								}
								HorseManager.logger.error("[后台刷魂石] [删除:魂石面板] [" + p.getLogString() + "] [" + ae.getId() + "] [" + ae.getArticleName() + "] [color:" + ae.getColorType() + "] [totalnum:" + (++deleteNum) + "]");
							}
						}
					}
					h.setHunshiArray(hunshiArray);
					long[] hunshi2Array = h.getHunshi2Array();
					if (hunshi2Array != null && hunshi2Array.length > 0) {
						for (int k = 0; k < hunshi2Array.length; k++) {
							if (hunshi2Array[k] > 0 && repet.contains(hunshi2Array[k])) {
								ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hunshiArray[k]);
								hunshi2Array[k] = -1;
								// 统计
								if (ae != null) {
									ArticleStatManager.addToArticleStat(null, p, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "刷魂石", null);
								}
								HorseManager.logger.error("[后台刷魂石] [删除:套装魂石面板] [" + p.getLogString() + "] [" + ae.getId() + "] [" + ae.getArticleName() + "] [color:" + ae.getColorType() + "] [totalnum:" + (++deleteNum) + "]");
							}
						}
					}
					h.setHunshi2Array(hunshi2Array);

				}
			}

			//背包仓库
			deleteFromBeibao(p.getKnapsack_common(), repet, p, deleteNum);
			deleteFromBeibao(p.getKnapsack_fangbao(), repet, p, deleteNum);
			deleteFromBeibao(p.getKnapsacks_cangku(), repet, p, deleteNum);
			deleteFromBeibao(p.getKnapsacks_warehouse(), repet, p, deleteNum);

			//邮件
			List<Mail> mails = MailManager.getInstance().getAllMails(p);
			if (mails != null && mails.size() > 0) {
				for (Mail mail : mails) {
					if (mail != null) {
						Cell[] cells = mail.getCells();
						if (cells != null && cells.length > 0) {
							for (int j = 0; j < cells.length; j++) {
								Cell cell = cells[j];
								if (cell != null && cell.getEntityId() > 0) {
									ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
									if (ae != null && ae instanceof HunshiEntity) {
										if (repet.contains(cell.getEntityId())) {
											cells[j].setEntityId(-1);
											// 统计
											ArticleStatManager.addToArticleStat(null, p, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "刷魂石", null);
											HorseManager.logger.error("[后台刷魂石] [删除:邮件] [" + p.getLogString() + "] [" + ae.getId() + "] [" + ae.getArticleName() + "] [color:" + ae.getColorType() + "] [totalnum:" + (++deleteNum) + "/" + num + "]");
										}
									}
								}
							}
						}
						mail.setCells(cells);
					}
				}
			}
			HorseManager.logger.warn("[后台清理魂石] [第" + (++removeIndex) + "/" + tempId.length + "个玩家] [总耗时:" + (System.currentTimeMillis() - start2) + "ms] [重复id个数:" + repet.size() + "]");
		}
		break;
	}
%>
<%!public void findFromBeibao(Knapsack beibao, Set<Long> has, Set<Long> repet, int num, Set<Long> pids, long pid, Set<Long> repetPids) {

		if (beibao == null) {
			return;
		}
		Cell[] cells = beibao.getCells();
		if (cells != null && cells.length > 0) {
			for (int i = 0; i < cells.length; i++) {
				Cell cell = cells[i];
				if (cell != null && cell.getEntityId() > 0) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
					if (ae != null && ae instanceof HunshiEntity) {
						if (ae.getColorType() >= 3) {
							pids.add(pid);
						}
						recordId(cell.getEntityId(), pid);
					}
				}
			}
		}

	}

	public void deleteFromBeibao(Knapsack beibao, Set<Long> repet, Player p, int deleteNum) {
		if (beibao == null) {
			return;
		}
		Cell[] cells = beibao.getCells();
		if (cells != null && cells.length > 0) {
			for (int i = 0; i < cells.length; i++) {
				Cell cell = cells[i];
				if (cell != null && cell.getEntityId() > 0) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
					if (ae != null && ae instanceof HunshiEntity) {
						if (repet.contains(cell.getEntityId())) {
							beibao.remove(i, "刷魂石", false);
							// 统计
							ArticleStatManager.addToArticleStat(null, p, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "刷魂石", null);
							HorseManager.logger.error("[后台刷魂石] [删除:" + beibao.knapName + "] [" + p.getLogString() + "] [" + ae.getId() + "] [" + ae.getArticleName() + "] [color:" + ae.getColorType() + "] [totalnum:" + (++deleteNum) + "]");
						}
					}
				}
			}
		}
	}

	public synchronized void recordId(long hunshiId, long pid) {
		if (!has.contains(hunshiId)) {
			has.add(hunshiId);
		} else {
			repet.add(hunshiId);
			repetPids.add(pid);
			num++;
			HorseManager.logger.error("[发现重复的魂石ID] [魂石id:" + hunshiId +"] [角色id:" + pid+ "]");
		}
	}

	Set<Long> result;

	public class FindPlayer implements Runnable {
		List<Long> playerIds;
		String name;
		boolean over = false;
		long start = System.currentTimeMillis();

		public FindPlayer(List<Long> playerIds, String name) {
			this.playerIds = playerIds;
			this.name = name;
		}

		public void run() {
			for (int i = 0; i < playerIds.size(); i++) {
				long id = playerIds.get(i);
				Player p = null;
				try {
					p = PlayerManager.getInstance().getPlayer(id);

				} catch (Exception e) {

				}
				for (Soul s : p.getSouls()) {
					ArrayList<Long> horseId = s.getHorseArr();

					//魂石面板
					for (Long hid : horseId) {
						Horse h = null;
						try {
							h = HorseManager.em.find(hid);

						} catch (Exception e) {

						}
						long[] hunshiArray = h.getHunshiArray();
						if (hunshiArray != null && hunshiArray.length > 0) {
							for (Long hunshiId : hunshiArray) {
								if (hunshiId > 0) {
									ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hunshiId);
									if (ae != null) {
										if (ae.getColorType() >= 3) {
											pids.add(id);
										}
									}
									recordId(hunshiId, id);
								}
							}
						}
						long[] hunshi2Array = h.getHunshi2Array();
						if (hunshi2Array != null && hunshi2Array.length > 0) {
							for (Long hunshiId : hunshi2Array) {
								if (hunshiId > 0) {
									ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hunshiId);
									if (ae != null) {
										if (ae.getColorType() >= 3) {
											pids.add(id);
										}
									}
									recordId(hunshiId, id);
								}
							}
						}

					}
				}

				//背包仓库
				findFromBeibao(p.getKnapsack_common(), has, repet, num, pids, id, repetPids);
				findFromBeibao(p.getKnapsack_fangbao(), has, repet, num, pids, id, repetPids);
				findFromBeibao(p.getKnapsacks_cangku(), has, repet, num, pids, id, repetPids);
				findFromBeibao(p.getKnapsacks_warehouse(), has, repet, num, pids, id, repetPids);

				//邮件
				List<Mail> mails = MailManager.getInstance().getAllMails(p);
				if (mails != null && mails.size() > 0) {
					for (Mail mail : mails) {
						if (mail != null) {
							Cell[] cells = mail.getCells();
							if (cells != null && cells.length > 0) {
								for (int j = 0; j < cells.length; j++) {
									Cell cell = cells[j];
									if (cell != null && cell.getEntityId() > 0) {
										ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
										if (ae != null && ae instanceof HunshiEntity) {
											if (ae.getColorType() >= 3) {
												pids.add(id);
											}
											recordId(cell.getEntityId(), id);
										}
									}
								}
							}
						}
					}
				}
				HorseManager.logger.warn("[后台扫描魂石] [" + name + "] [第" + i + "/" + playerIds.size() + "个] [总耗时:" + (System.currentTimeMillis() - start) + "ms] [重复id个数:" + repet.size() + "]");
			}

		}
	}

	public class CheckEnd implements Runnable {
		List<FindPlayer> findPlayerList;
		boolean finish = false;

		public CheckEnd(List<FindPlayer> findPlayerList) {
			this.findPlayerList = findPlayerList;
		}

		public void run() {
			while (true) {
				boolean over = true;
				for (FindPlayer fp : findPlayerList) {
					if (!fp.over) {
						over = false;
						break;
					}
				}
				if (!over) {
					try {
						HorseManager.logger.warn("检查魂石");
						Thread.sleep(60000l);
					} catch (Exception e) {

					}
				} else {
					break;
				}
			}
			finish = true;
			HorseManager.logger.warn("检查魂石线程执行完毕");
		}
	}%>

</body>
</html>