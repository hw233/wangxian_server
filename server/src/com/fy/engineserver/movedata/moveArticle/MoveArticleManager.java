package com.fy.engineserver.movedata.moveArticle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;

import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayEntityManager;
import com.fy.engineserver.datasource.article.data.horseInlay.instance.HorseEquInlay;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.PetHookInfo;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.movedata.DataMoveManager;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.qiancengta.QianCengTa_Ceng;
import com.fy.engineserver.qiancengta.QianCengTa_Dao;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
import com.fy.engineserver.soulpith.SoulPithEntityManager;
import com.fy.engineserver.soulpith.instance.PlayerSoulpithInfo;
import com.fy.engineserver.soulpith.instance.SoulPithEntity;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
import com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory;

public class MoveArticleManager {
	public static MoveArticleManager inst;
	
	public static Logger logger = DataMoveManager.logger;
	/** 新库的factory */
	private DefaultSimpleEntityManagerFactory factory;
	/** 新库的articleManager  用此实例存库 **/
	public SimpleEntityManager<ArticleEntity> aeEm;
	
	public Field aeVersion;
	
	Field versionField ;
	
	public static final long MONTH = 30L * 24 * 60 * 60 * 1000;
	public static long now = System.currentTimeMillis();
	public String sql = "";				//不需要任何条件。因为数据库已经删除过角色
//	public String sql = "RMB > 0 or quitGameTime >" + (now - 6 * MONTH) + " or level > 110";		//原数据库未执行删除操作
	
	public List<Long> playerIds = new ArrayList<Long>();
	/** 正在跑的线程，所有正在跑的线程都需呀加入到此列表中 */
	public volatile List<MoveArticleThread> currentThread = new ArrayList<MoveArticleThread>();
	
	public static int 批量存储数量 = 2000;
	/** 在导入装备时检测并记录宝石合器灵的id */
	public Queue<Long> 宝石和器灵id = new LinkedList<Long>();
	
	public static boolean hasStart = false;
	
	public MoveArticleManager(String emPath) {
		if (inst != null) {
			logger.warn("***************************************************************************************************");
			logger.warn("*****************************************已经初始化过此类************************************************");
			logger.warn("***************************************************************************************************");
			return ;
		}
		factory = new DefaultSimpleEntityManagerFactory(emPath);
		aeEm = factory.getSimpleEntityManager(ArticleEntity.class);
		versionField = DataMoveManager.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
		versionField.setAccessible(true);
		DefaultArticleEntityManager.getInstance().em.setReadOnly(true);
		aeEm.setBatchSaveOrUpdateSize(批量存储数量);
		inst = this;
	}
	
	public static int 开启几个收集玩家物品的线程 = 15;
	
	public long startTime = 0;
	
	public void moveBegin() throws Exception {
		if (hasStart) {
			logger.warn("***************************************************************************************************");
			logger.warn("*****************************************已经开始转移了*************************************************");
			logger.warn("***************************************************************************************************");
			return ;
		}
		hasStart = true;
		startTime = System.currentTimeMillis();
		logger.warn("***************************************************************************************************");
		logger.warn("*****************************************转移物品开始**************************************************");
		logger.warn("***************************************************************************************************");
		init();					//初始化
		if (playerIds.size() <= 0) {
			collectIds();			//收集playerId
		}
		int totalNum = playerIds.size();
		int a1 = totalNum / 开启几个收集玩家物品的线程;
		for (int i=0; i<开启几个收集玩家物品的线程; i++) {
			List<Long> idList = new ArrayList<Long>();
			int aa = a1 * (i);
			int aa2 = a1 * (i+1);
			if ((i+1) == 开启几个收集玩家物品的线程) {
				aa2 = totalNum;
			}
			for (int k=aa; k<aa2; k++) {
				idList.add(playerIds.get(k));
			}
			MoveArticleThread thread4Player = new MoveArticleThread("movePlayerArticles", new Class<?>[]{List.class}, new Object[] {idList});
			Thread thread = new Thread(thread4Player, "收集转移玩家物品-" + i);
			thread.start();
			currentThread.add(thread4Player);
		}
		MoveArticleThread thread4xiazi = new MoveArticleThread("moveBaoshiXiaziData", new Class<?>[0], new Object[0]);		//宝石匣子中的物品
		Thread xiaziThread = new Thread(thread4xiazi, "转移宝石匣子中的宝石");
		xiaziThread.start();
		currentThread.add(thread4xiazi);
		
		MoveArticleThread thread4Jiazu = new MoveArticleThread("collectJiazuArticleIds", new Class<?>[0], new Object[0]);
		Thread jiazu = new Thread(thread4Jiazu, "转移家族仓库数据");
		jiazu.start();
		currentThread.add(thread4Jiazu);
		
		MoveArticleThread thread4Baoshi = new MoveArticleThread("moveBaoshiAndQiling", new Class<?>[0], new Object[0]);
		Thread thread = new Thread(thread4Baoshi, "转移宝石和器灵");
		thread.start();
//		currentThread.add(thread4Baoshi);
		
		while (!allThreadOver(currentThread) || thread4Baoshi.isRunning()) {
			removeDoneThread(); // 在此线程中清理应该删除 但是未删除的已经运行完毕的线程
			logger.warn("[数据转移中] [每分钟检查一次] [" + getCurrentRunThreadInfo() + "] [总耗时:" + (System.currentTimeMillis() - startTime) + "]");
			Thread.sleep(60000);
		}
		
		logger.warn("***************************************************************************************************");
		logger.warn("*****************************************转移物品结束**************************************************");
		logger.warn("***************************************************************************************************");
		logger.warn("****************************空格子数量:" + 空格子物品id列表.size() + "**************************");
	}
	
	public String getCurrentRunThreadInfo() {
		StringBuffer sbf = new StringBuffer();
		try {
			MoveArticleThread[] aaa = currentThread.toArray(new MoveArticleThread[currentThread.size()]);
			for (MoveArticleThread ut : aaa) {
				if (ut == null) {
					continue;
				}
				String name = "";
				if (ut.getArgs() != null && ut.getArgs().length > 0) {
					name = ut.getArgs()[0].toString();
				}
				if (name != null) {
					name = ut.getMethodName();
				}
				sbf.append("[");
				sbf.append(name);
				sbf.append(",已执行:").append(TimeTool.instance.getShowTime(System.currentTimeMillis() - ut.getStartTime()));
				sbf.append("] ");

			}
		} catch (Exception e) {
			logger.error("getCurrentRunThreadInfo出错:", e);
		}
		return sbf.toString();
	}
	
	private void removeDoneThread() {
		Iterator<MoveArticleThread> itor = currentThread.iterator();
		while (itor.hasNext()) {
			MoveArticleThread ut = itor.next();
			if (!ut.running) {
				logger.warn("[移除进程] [成功] [" + ut.getMethodName() + "]");
				itor.remove();
			}
		}
	}
	
	public boolean allThreadOver(List<MoveArticleThread> list) {
		for (MoveArticleThread serverThread : list) {
			if (serverThread.running) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 收集有用的playerId
	 * @return
	 * @throws Exception
	 */
	public List<Long> collectIds() throws Exception {
		long startTime = System.currentTimeMillis();
		long count = ((GamePlayerManager)GamePlayerManager.getInstance()).em.count(Player.class, sql);
		long[] tempId = ((GamePlayerManager)GamePlayerManager.getInstance()).em.queryIds(Player.class, sql, "", 1, count + 1);
		for (long id : tempId) {
			if (!playerIds.contains(id)) {
				playerIds.add(id);
			}
		}
		long endTime = System.currentTimeMillis();
		logger.warn("[数据转移] [收集有用id] [收集总数:" + count + "] [实际数:" + playerIds.size() + "] [耗时：" + (endTime - startTime) + "ms]");
		return playerIds;
	}
	
	public void init() {
		DefaultArticleEntityManager.getInstance().em.setReadOnly(true);
		aeVersion = DataMoveManager.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
		aeVersion.setAccessible(true);
	}
	
	public static int 一次性导入宝石个数 = 2000;
	
	public static long 宝石存储堆过多少个后检测存储 = 5000;
	public long lastCheckBaoshi = 0L;
	public long checkVersionTime = 3 * 60 * 1000L;
	
//	public List<ArticleEntity> 为存储到数据库的宝石器灵 = new ArrayList<ArticleEntity>();
	
	public void moveHorseEquInlayData() {
		try {
			long totalCount = HorseEquInlayEntityManager.em.count();
			if (totalCount > 0) {
				Field idField = DataMoveManager.getFieldByAnnotation(HorseEquInlay.class, SimpleId.class);
				List<Long> idList = new ArrayList<Long>();
				long leftNum = totalCount + 1;
				int page = 0;// 分页查询的页码
				int pageSize = DataMoveManager.ONCE_QUERY * 6;
				long start = 1;// 开始位置
				long end = 2;// 结束位置
				while (leftNum > 0) {
					start = page * pageSize + 1;
					end = start + pageSize;
					long[] tempId = HorseEquInlayEntityManager.em.queryIds(HorseEquInlay.class, "", idField.getName(), start, end);
					for (long id : tempId) {
						idList.add(id);
					}
					leftNum -= pageSize;
					page++;
				}
				Long[] allIds = idList.toArray(new Long[0]);
				idList = null;
				List<Long> ll = new ArrayList<Long>();
				for (int i=0; i<allIds.length; i++) {
					HorseEquInlay data = HorseEquInlayEntityManager.em.find(allIds[i]);
					for (int j=0; j<data.getInlayArticleIds().length; j++) {
						if (data.getInlayArticleIds()[j] > 0) {
							ll.add(data.getInlayArticleIds()[j]);
						}
					}
				}
				if (ll.size() > 0) {
					synchronized (宝石和器灵id) {
						宝石和器灵id.addAll(ll);
					}
				}
			}
		} catch (Exception e) {
			logger.warn("[转移宝石匣子数据] [异常]", e);
		}
	}
	
	
	public void moveBaoshiXiaziData() {
		try {
			long totalCount = ArticleEntityManager.baoShiXiLianEM.count();
			if (totalCount > 0) {
				Field idField = DataMoveManager.getFieldByAnnotation(BaoShiXiaZiData.class, SimpleId.class);
				List<Long> idList = new ArrayList<Long>();
				long leftNum = totalCount + 1;
				int page = 0;// 分页查询的页码
				int pageSize = DataMoveManager.ONCE_QUERY * 6;
				long start = 1;// 开始位置
				long end = 2;// 结束位置
				while (leftNum > 0) {
					start = page * pageSize + 1;
					end = start + pageSize;
					long[] tempId = ArticleEntityManager.baoShiXiLianEM.queryIds(BaoShiXiaZiData.class, "", idField.getName(), start, end);
					for (long id : tempId) {
						idList.add(id);
					}
					leftNum -= pageSize;
					page++;
				}
				Long[] allIds = idList.toArray(new Long[0]);
				idList = null;
				List<Long> ll = new ArrayList<Long>();
				for (int i=0; i<allIds.length; i++) {
					BaoShiXiaZiData data = ArticleEntityManager.baoShiXiLianEM.find(allIds[i]);
					if (data.getIds() != null &&  data.getIds().length > 0) {
						for (int j=0; j<data.getIds().length; j++) {
							long id = data.getIds()[j];
							if (id > 0) {
								ll.add(id);
							}
						}
					}
				}
				if (ll.size() > 0) {
					synchronized (宝石和器灵id) {
						宝石和器灵id.addAll(ll);
					}
				}
			}
		} catch (Exception e) {
			logger.warn("[转移宝石匣子数据] [异常]", e);
		}
	}
	
	
	/**
	 * 宝石和器灵单独起线程操作（使用 宝石和器灵id 中的数据） 必须在其他线程都结束后此线程才可以结束
	 */
	public void moveBaoshiAndQiling() {
		while (!allThreadOver(currentThread) || 宝石和器灵id.size() > 0) {
			try {
				if (宝石和器灵id.size() > 0) {
					List<Long> list = new ArrayList<Long>();
					synchronized (宝石和器灵id) {
						while (宝石和器灵id.size() > 0 && list.size() < 一次性导入宝石个数) {
							long id = 宝石和器灵id.poll();
							if (!list.contains(id)) {
								list.add(id);
							}
						}
					}
					if (list.size() > 0) {
						String sq = "";
						for (int i = 0; i < list.size(); i++) {
							if (i == list.size() - 1) {
								sq += "id=" + list.get(i);
							} else {
								sq += "id=" + list.get(i) + " or ";
							}
						}
						List<Long> tempBaoshi = new ArrayList<Long>();
						List<ArticleEntity> entitys = DefaultArticleEntityManager.getInstance().em.query(ArticleEntity.class, sq, "", 1, list.size() + 2);
						for (ArticleEntity ae : entitys) {
							long ct = aeEm.count(ArticleEntity.class, "id="+ae.getId());
							if (ct <= 0) {
								aeVersion.set(ae, 0);
								aeEm.flush(ae);
								/*Article article = ArticleManager.getInstance().getArticle(ae.getArticleName());
								if(article != null && article instanceof InlayArticle){
									if(((InlayArticle)article).getInlayType() > 1){
										BaoShiXiaZiData xiazidata = ArticleEntityManager.baoShiXiLianEM.find(ae.getId());
										if(xiazidata != null && xiazidata.getIds() != null){
											for(long id : xiazidata.getIds()){
												if(id > 0){
													tempBaoshi.add(id);
												}
											}
										}
									}
								}*/
							}
							if (tempBaoshi.size() > 0) {
								synchronized (宝石和器灵id) {
									宝石和器灵id.addAll(tempBaoshi);
								}
							}
//							为存储到数据库的宝石器灵.add(ae);
						}
					}
				}
			} catch (Exception e) {
				logger.warn("[转移宝石合器灵] [异常] ", e);
			}
			
			try {
				Thread.sleep(2000);
			} catch (Exception e) {}
		}
	}
	
//	public List<ArticleEntity> 为存储到数据库的物品 = new ArrayList<ArticleEntity>();
	
	public static int 休息时间 = 100;
	
	public void movePlayerArticles(List<Long> idList) {
		int len = idList.size();
		for (int i=0; i<idList.size(); i++) {
			collectPlayerArticleIds(idList.get(i));
			if (logger.isInfoEnabled()) {
				logger.info("[转移物品] [当前线程已经转移数量:" + (i+1) + "] [总数:" + len + "] [playerId:" + idList.get(i) + "]");
			}
			try {
				Thread.sleep(休息时间);
			} catch (Exception e) {
				
			}
		}
	}
	
	public static boolean debug = true;
	
	public List<Long> 空格子物品id列表 = new ArrayList<Long>();
	
	
	public void collectPlayerArticleIds(long playerId) {
		try {
			Player player = ((GamePlayerManager)GamePlayerManager.getInstance()).em.find(playerId);
			List<Long> resultList = collectArticleIds(player);
			List<Long> tempList = new ArrayList<Long>();
			for (int i=0; i<resultList.size(); i++) {
				if (tempList.contains(resultList.get(i))) {
					continue;
				}
				tempList.add(resultList.get(i));
			}
			if (tempList != null && tempList.size() > 0) {
				String sql = "";
				Object[] objattr = new Object[0];
				for (int i = 0; i < tempList.size(); i++) {
					if (i == tempList.size() - 1) {
						sql += "id=?";// + tempList.get(i);
					} else {
						sql += "id=? or "; //+ tempList.get(i) + " or ";
					}
					objattr = Arrays.copyOf(objattr, objattr.length+1);
					objattr[objattr.length-1] = tempList.get(i);
				}
				List<ArticleEntity> entitys = DefaultArticleEntityManager.getInstance().em.query(ArticleEntity.class,sql,objattr, "", 1, tempList.size() + 2);
				if (logger.isInfoEnabled()) {
					logger.info("[转移角色物品] [收集物品] [不重复的数量 : " + tempList.size() + "] [实际收集到的数量:"+resultList.size()+"] [实际从数据库中查出的物品数量:" + entitys.size() + "] [是否相同:"+(tempList.size()==entitys.size())+"] [" + player.getLogString() + "]");
				}
				for (ArticleEntity ae : entitys) {
					long ct = aeEm.count(ArticleEntity.class, "id="+ae.getId());
					if (debug) {
						tempList.remove(ae.getId());
					}
					if (ct > 0) {
						continue;
					}
					versionField.set(ae, 0);
					aeEm.flush(ae);
					if (ae instanceof EquipmentEntity) {
						List<Long> ll = collectBaoshiIds((EquipmentEntity) ae);
						if (ll != null && ll.size() > 0) {
							synchronized (宝石和器灵id) {
								宝石和器灵id.addAll(ll);
							}
						}
						ll = null;
					} else {
						List<Long> ll = collectPetSuitIds(ae);
						if (ll != null && ll.size() > 0) {
							synchronized (宝石和器灵id) {
								宝石和器灵id.addAll(ll);
							}
						}
					}
				}
			}
			if (debug && tempList.size() > 0) {
				synchronized (空格子物品id列表) {
					空格子物品id列表.addAll(tempList);
				}
			}
			tempList = null;
		} catch (Exception e) {
			logger.warn("[数据转移] [收集玩家物品] [异常] [playerId:" + playerId + "]", e);
		}
	}
	
	public static int 检测重复id时间 = 20000;
	
//	public List<ArticleEntity> 为存储到数据库的家族的物品 = new ArrayList<ArticleEntity>();
	
	public long lastCheckJiazu = 0L;
	
	/**
	 * 收集家族仓库中的物品id  (单独线程做)
	 * @return
	 */
	public void collectJiazuArticleIds() {
		List<Long> resultList = new ArrayList<Long>();
		Long[] ids = JiazuManager.getInstance().getJiazuLruCacheByID().keySet().toArray(new Long[0]);
		for (int i = 0; i < ids.length; i++) {
			try {
				Jiazu jiazu = JiazuManager.getInstance().getJiazuLruCacheByID().get(ids[i]);
				if (jiazu != null && jiazu.getWareHouse() != null && jiazu.getWareHouse().size() > 0) {
					for (Cell c : jiazu.getWareHouse()) {
						if (c.getEntityId() > 0) {
							resultList.add(c.getEntityId());
						}
					}
				}
				if (resultList.size() >= 一次性导入宝石个数) {
					String sql = "";
					for (int k = 0; k < resultList.size(); k++) {
						if (k == resultList.size() - 1) {
							sql += "id=" + resultList.get(k);
						} else {
							sql += "id=" + resultList.get(k) + " or ";
						}
					}
					List<ArticleEntity> entitys = DefaultArticleEntityManager.getInstance().em.query(ArticleEntity.class,sql, "", 1, resultList.size() + 2);
					for (ArticleEntity ae : entitys) {
						long ct = aeEm.count(ArticleEntity.class, "id="+ae.getId());
						if (ct <= 0) {
							aeVersion.set(ae, 0);
							aeEm.notifyNewObject(ae);
						}
//						为存储到数据库的家族的物品.add(ae);
					}
					resultList.clear();
//					if (为存储到数据库的家族的物品.size() > 宝石存储堆过多少个后检测存储) {
//						try {
//							long currentTime = System.currentTimeMillis();
//							boolean isCheck = false;
//							Iterator<ArticleEntity> itor = 为存储到数据库的家族的物品.iterator();
//							while(itor.hasNext()) {
//								ArticleEntity articleEntity = itor.next();
//								int version = versionField.getInt(articleEntity);
//								if (version != 0) {
//									itor.remove();
//								} else if (currentTime > lastCheckJiazu + checkVersionTime) {
//									if (!isCheck) {
//										isCheck = true;
//									}
//									long ct = aeEm.count(ArticleEntity.class, "id="+articleEntity.getId());
//									if (ct > 0) {
//										itor.remove();
//									}
//								}
//							}
//							if (isCheck) {
//								lastCheckJiazu = currentTime;
//								isCheck = false;
//							}
//						} catch (Exception e) {
//							
//						}
//					}
				}
			} catch (Exception e) {
				logger.warn("[收集存储家族物品] [异常] [jiazuId:" + ids[i] + "]", e);
			}
		}
		if (resultList.size() > 0) {
			try {
				String sql = "";
				for (int k = 0; k < resultList.size(); k++) {
					if (k == resultList.size() - 1) {
						sql += "id=" + resultList.get(k);
					} else {		//
						sql += "id=" + resultList.get(k) + " or ";
					}
				}
				List<ArticleEntity> entitys = DefaultArticleEntityManager.getInstance().em.query(ArticleEntity.class,sql, "", 1, resultList.size() + 2);
				for (ArticleEntity ae : entitys) {
					long ct = aeEm.count(ArticleEntity.class, "id="+ae.getId());
					if (ct <= 0) {
						aeVersion.set(ae, 0);
						aeEm.notifyNewObject(ae);
					}
//					为存储到数据库的家族的物品.add(ae);
				}
			} catch (Exception e) {
				logger.warn("[收集存储家族物品2] [异常]", e);
			}
		}
//		while (为存储到数据库的家族的物品.size() > 0) {
//			Iterator<ArticleEntity> itor = 为存储到数据库的家族的物品.iterator();
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			while (itor.hasNext()) {
//				ArticleEntity articleEntity = itor.next();
//				try {
//					int version = versionField.getInt(articleEntity);
//					if (version != 0) {
//						itor.remove();
//					} else if (version == 0 ) {
//						long ct = aeEm.count(ArticleEntity.class, "id="+articleEntity.getId());
//						if (ct > 0) {
//							itor.remove();
//						}
//					}
//				} catch (Exception e) {
//					logger.warn("[最后检测家族物品] [异常] [从检测列表中移除] [aeID:" + articleEntity.getId() + "]", e);
//					itor.remove();
//				}
//			}
//		}
	}
	
	/**
	 * 收集角色背包、仓库、器灵仓库、防爆包、宠物背包、翅膀上的宝石
	 * @param player
	 * @return
	 * @throws Exception 
	 */
	public List<Long> collectArticleIds(Player player) throws Exception {
		List<Long> resultList = new ArrayList<Long>();
		{		//收集装备
			for (int i=0; i<player.getSouls().length; i++) {
				Soul soul = player.getSouls()[i];
				if (soul == null) {
					continue;
				}
				for (int j=0; j<soul.getEc().getEquipmentIds().length; j++) {
					if (soul.getEc().getEquipmentIds()[j] > 0) {
						resultList.add(soul.getEc().getEquipmentIds()[j]);
					}
				}
			}
		}
		{		//背包 (包括宠物背包)
			Knapsack bag1 = player.getKnapsack_common();			//普通背包
			if (bag1 != null) {
				for (int i=0; i<bag1.getCells().length; i++) {
					if (bag1.getCells()[i] == null) {
						continue;
					}
					if (bag1.getCells()[i].getEntityId() > 0) {
						resultList.add(bag1.getCells()[i].getEntityId());
					}
				}
			}
			Knapsack bag2 = player.getPetKnapsack();				//宠物背包
			if (bag2 != null) {
				for (int i=0; i<bag2.getCells().length; i++) {
					if (bag2.getCells()[i] == null) {
						continue;
					}
					if (bag2.getCells()[i].getEntityId() > 0) {
						resultList.add(bag2.getCells()[i].getEntityId());
					}
				}
			}
		}
		{		//仓库
			Knapsack bag = player.getKnapsacks_cangku();
			if (bag != null) {
				for (int i=0; i<bag.getCells().length; i++) {
					if (bag.getCells()[i] == null) {
						continue;
					}
					if (bag.getCells()[i].getEntityId() > 0) {
						resultList.add(bag.getCells()[i].getEntityId());
					}
				}
			}
		}
		{		//2号仓库
			Knapsack bag = player.getKnapsacks_warehouse();
			if (bag != null) {
				for (int i=0; i<bag.getCells().length; i++) {
					if (bag.getCells()[i] == null) {
						continue;
					}
					if (bag.getCells()[i].getEntityId() > 0) {
						resultList.add(bag.getCells()[i].getEntityId());
					}
				}
			}
		}
		{		//兽魂仓库
			if (player.getShouhunKnap() != null) {
				for (int i=0; i<player.getShouhunKnap().length; i++) {
					if (player.getShouhunKnap()[i] > 0) {
						resultList.add(player.getShouhunKnap()[i]);
					}
				}
			}
		}
		{		//器灵仓库
			Knapsack bag = player.getKnapsacks_QiLing();
			if (bag != null) {
				for (int i=0; i<bag.getCells().length; i++) {
					if (bag.getCells()[i] == null) {
						continue;
					}
					if (bag.getCells()[i].getEntityId() > 0) {
						resultList.add(bag.getCells()[i].getEntityId());
					}
				}
			}
		}
		if (player.getKnapsack_fangBao_Id() > 0 ) {
			resultList.add(player.getKnapsack_fangBao_Id());
		}
		{		//防爆包  （防爆包本身也是个道具）
			Knapsack bag = player.getKnapsack_fangbao();
			if (bag != null) {
				for (int i=0; i<bag.getCells().length; i++) {
					if (bag.getCells()[i] == null) {
						continue;
					}
					if (bag.getCells()[i].getEntityId() > 0) {
						resultList.add(bag.getCells()[i].getEntityId());
					}
				}
			}
		}
		{		//千层塔
			QianCengTa_Ta q = QianCengTaManager.getInstance().em.find(player.getId());
			if (q != null) {
				for (int i = 0; i < q.getDaoList().size(); i++) {
					QianCengTa_Dao dao = q.getDaoList().get(i);
					for (int j = 0; j < dao.getCengList().size(); j++) {
						QianCengTa_Ceng ceng = dao.getCengList().get(j);
						for (int k = 0; k < ceng.getRewards().size(); k++) {
							if (ceng.getRewards().get(k).getEntityId() > 0) {
								resultList.add(ceng.getRewards().get(k).getEntityId());
							}
						}
					}
				}
			}
		}
		{		//坐骑装备
			for (Soul soul : player.getSouls()) {
				if (soul == null) {
					continue;
				}
				for (long horseId : soul.getHorseArr()) {
					Horse horse = HorseManager.em.find(horseId);
					if (horse != null && !horse.isFly()) {
						long[] horseEquIDs = horse.getEquipmentColumn().getEquipmentIds();
						for (long i : horseEquIDs) {
							if (i > 0) {
								resultList.add(i);
							}
						}
						for (long i : horse.getHunshiArray()) {
							if (i > 0) {
								resultList.add(i);
							}
						}
						for (long i : horse.getHunshi2Array()) {
							if (i > 0) {
								resultList.add(i);
							}
						}
					}
				}
			}
		}
		{
			SoulPithEntity se = SoulPithEntityManager.em.find(player.getId());
			if (se != null) {
				for (PlayerSoulpithInfo info : se.getPithInfos()) {
					for (long id : info.getPiths()) {
						if (id > 0) {
							resultList.add(id);
						}
					}
				}
			}
		}
		
		{			//兽魂面板
			HuntLifeEntity entity = HuntLifeEntityManager.em.find(player.getId());
			if (entity != null) {
				for (int i=0; i<entity.getHuntDatas().length; i++) {
					if (entity.getHuntDatas()[i] > 0) {
						resultList.add(entity.getHuntDatas()[i]);
					}
				}
			}
		}
		{			//玩家仙府宠物仓库
			if (player.getCaveId() > 0) {
				Cave cave = FaeryManager.caveEm.find(player.getCaveId());
				if (cave != null && cave.getPethouse() != null && cave.getPethouse().getHookInfos() != null) {
					for (PetHookInfo phi : cave.getPethouse().getHookInfos()) {
						if (phi != null) {
							resultList.add(phi.getArticleId());
						}
					}
				}
				if (cave != null) {
					long[] pps = cave.getPethouse().getStorePets();
					if (pps != null) {
						for (int i=0; i<pps.length; i++) {
							if (pps[i] > 0) {
								resultList.add(pps[i]);
							}
						}
					}
				}
			}
		}
		return resultList;
	}
	
	public List<Long> collectPetSuitIds(ArticleEntity ae) {
		try {
			long petId = -1;
			if (ae instanceof PetPropsEntity) {
				petId = ((PetPropsEntity) ae).getPetId();
			} else if (ae instanceof PetEggPropsEntity) {
				petId = ((PetEggPropsEntity) ae).getPetId();
			}
			if (petId <= 0) {
				return null;
			}
			Pet pet = PetManager.em.find(petId);
			if (pet != null && pet.getOrnaments() != null && pet.getOrnaments().length > 0) {
				List<Long> suitList = new ArrayList<Long>();
				for (long id : pet.getOrnaments()) {
					if (id > 0) {
						suitList.add(id);
					}
				}
				return suitList;
			}
		} catch (Exception e) {
			logger.warn("[转移宠物饰品道具] [异常] [宠物道具id:" + ae.getId() + "]", e);
		}
		
		return null;
	}
	
	/**
	 * 收集装备上的宝石、器灵id
	 * @param ee
	 * @return
	 */
	public List<Long> collectBaoshiIds(EquipmentEntity ee) {
		List<Long> resultList = new ArrayList<Long>();
		if (ee.getInlayArticleIds() != null && ee.getInlayArticleIds().length > 0) {
			for (int i=0; i<ee.getInlayArticleIds().length; i++) {
				if (ee.getInlayArticleIds()[i] > 0) {
					if (!resultList.contains(ee.getInlayArticleIds()[i])) {
						resultList.add(ee.getInlayArticleIds()[i]);
					}
				}
			}
		}
		HorseEquInlay inlay = null;
		try {
			inlay = HorseEquInlayEntityManager.em.find(ee.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("[收集坐骑装备宝石] [异常] [" + ee.getId() + "]", e);
		}
		if (inlay != null) {
			for (int i=0; i<inlay.getInlayArticleIds().length; i++) {
				long id = inlay.getInlayArticleIds()[i];
				if (id > 0) {
					resultList.add(id);
				}
			}
		}
		if (ee.getInlayQiLingArticleIds() != null && ee.getInlayQiLingArticleIds().length > 0) {
			for (int i=0; i<ee.getInlayQiLingArticleIds().length; i++) {
				if (ee.getInlayQiLingArticleIds()[i] > 0) {
					if (!resultList.contains(ee.getInlayQiLingArticleIds()[i])) {
						resultList.add(ee.getInlayQiLingArticleIds()[i]);
					}
				}
			}
		}
		return resultList;
	}
}
