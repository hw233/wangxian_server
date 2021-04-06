package com.fy.engineserver.uniteserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.auction.Auction;
import com.fy.engineserver.auction.service.AuctionManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.mail.service.concrete.DefaultMailManager;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.qiancengta.QianCengTa_Ceng;
import com.fy.engineserver.qiancengta.QianCengTa_Dao;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerMysqlImpl;

public class DeleteArticleEntity {
	
	public static Logger logger = LoggerFactory.getLogger(UnitedServerManager.class);
	
	public static boolean isRealDelete = true;
	
	public static boolean isCheckUse = true;
	public static boolean isBreak = false;

	public static HashSet<Long> useIDs = new HashSet<Long>();
	public static long maxID = 0L;
	
//	public static void deleteNoInPlayerArticleEntity() {
//		try {
//			if (isCheckUse) {
//				logger.warn("开始拍卖中articleEntity数据获取");
//				getAuctionArticleEntitysToServer();
//				logger.warn("结束拍卖中articleEntity数据获取");
//				logger.warn("开始坐骑中articleEntity数据获取");
//				getHorseArticleEntitysTOServer();
//				logger.warn("结束坐骑中articleEntity数据获取");
//				logger.warn("开始宠物articleEntity数据获取");
//				getPetArticleEntitysToServer();
//				logger.warn("结束宠物articleEntity数据获取");
//				logger.warn("开始家族仓库articleEntity数据获取");
//				getJiaZuCangKuArticleEntitysToServer();
//				logger.warn("结束家族仓库articleEntity数据获取");
//				logger.warn("开始千层塔 articleEntity数据获取");
//				getQianCengTaArticleEntitysToServer();
//				logger.warn("结束千层塔 articleEntity数据获取");
//				logger.warn("开始Player articleEntity数据获取");
//				getPlayerArticleEntitysToServer();
//				logger.warn("结束Player articleEntity数据获取");
//				logger.warn("开始mail articleEntity数据获取");
//				getMailArticleEntitysToServer();
//				logger.warn("结束mail articleEntity数据获取");
//				logger.warn("开始装备中宝石器灵 articleEntity数据获取");
//				getEquipmentEntitysArticleEntity();
//				logger.warn("结束装备中宝石器灵 articleEntity数据获取" + " 总数:" + useIDs.size());
//				
//			}
//			logger.warn("开始删除物品");
//			long deleteNum = 0;
//			SimpleEntityManager<ArticleEntity> articleEntityEm = ArticleEntityManager.getInstance().em;
//			long allCount = articleEntityEm.count();
//			int pagIndex = 0;
//			int pagNum = 5000;
//			long starttt = System.currentTimeMillis();
//			
//			if (articleEntityEm instanceof SimpleEntityManagerMysqlImpl) {
//				long gg = System.currentTimeMillis();
//				logger.warn("删除索引开始");
//				articleEntityEm.dropEntityIndex(ArticleEntity.class);
//				logger.warn("删除索引结束 ["+(System.currentTimeMillis()-gg)+"]");
//			}
//			long lastID = 0;
//			while(true) {
//				if (isBreak) {
//					break;
//				}
//				try {
//					long[] ids = articleEntityEm.queryIds(ArticleEntity.class, "id > ? and id <= ?", new Object[]{lastID, lastID+10000}, "id", pagNum*pagIndex+1, pagNum*pagIndex+pagNum+1);
//					long oldDeleteNum = deleteNum;
//					long tt = System.currentTimeMillis();
//					for (long ii : ids) {
//						if (!useIDs.contains(ii)) {
//							ArticleEntity entity = articleEntityEm.find(ii);
//							if (entity instanceof EquipmentEntity || entity instanceof PropsEntity || entity instanceof QiLingArticleEntity) {
//								if (isRealDelete) {
//									articleEntityEm.remove(entity);
//								}
//								deleteNum++;
//								logger.warn("[删除物品] ["+entity.getId()+"] ["+entity.getArticleName()+"]");
//							}
//						}
//					}
//					logger.warn("[查询ID] 删除物品 数目["+(deleteNum - oldDeleteNum)+"] [总删除数目:"+deleteNum+"] ["+allCount+"] [删除万分之:"+((deleteNum*10000L)/allCount)+"] [花费:"+(System.currentTimeMillis()-tt)+"ms]");
//					if (pagIndex * pagNum >= allCount) {
//						break;
//					}
//					lastID = ids[ids.length - 1];
//				} catch(Exception e) {
//					logger.error("----------", e);
//				}
//			}
//			logger.warn("删除物品结束， 删除物品数目:" + deleteNum + "  总数:" + allCount + "  有用数目:" + useIDs.size() + "   共花费:" + (System.currentTimeMillis()-starttt));
//		} catch(Exception e) {
//			logger.error("", e);
//		}
//	}
	
	public static void getEquipmentEntitysArticleEntity() {
		int num = 0;
		int baoshi = 0;
		int qiling = 0;
		int jianchaNum = 0;
		ArrayList<Long> entityArrays = new ArrayList<Long>();
		entityArrays.addAll(useIDs);
		Collections.sort(entityArrays);
		SimpleEntityManager<ArticleEntity> manager = ArticleEntityManager.getInstance().em;
		HashSet<Long> add = new HashSet<Long>();
		Iterator<Long> iIds = entityArrays.iterator();
		while (true) {
			try {
				ArrayList<Long> id50 = new ArrayList<Long>();
				int findNum = 1000;
				boolean isOver = false;
				for (;;) {
					Long id = iIds.next();
					id50.add(id);
					findNum--;
					jianchaNum++;
					if (!iIds.hasNext()) {
						isOver = true;
						break;
					}
					if (findNum <= 0) {
						break;
					}
				}
				String sq = "";
				for (int i = 0; i < id50.size(); i++) {
					if (i == id50.size() - 1) {
						sq += "id=" + id50.get(i);
					} else {
						sq += "id=" + id50.get(i) + " or ";
					}
				}
				List<ArticleEntity> entitys = manager.query(ArticleEntity.class, sq, "", 1, id50.size() + 1);
				logger.warn("[id个数:" + id50.size() + "] [对象个数:" + entitys.size() + "/" + entityArrays.size() + "] [" + (id50.size() == entitys.size() ? "一样" : "不一样") + "]");
				for (ArticleEntity entity : entitys) {
					if (entity instanceof EquipmentEntity) {
						EquipmentEntity eq = (EquipmentEntity) entity;
						for (int i = 0; i < eq.getInlayArticleIds().length; i++) {
							if (eq.getInlayArticleIds()[i] > 0) {
								add.add(eq.getInlayArticleIds()[i]);
								num++;
								baoshi++;
							}
						}
						for (int i = 0; i < eq.getInlayQiLingArticleIds().length; i++) {
							if (eq.getInlayQiLingArticleIds()[i] > 0) {
								add.add(eq.getInlayQiLingArticleIds()[i]);
								num++;
								qiling++;
							}
						}
					}
				}
				logger.warn("检查物品ID   ids " + useIDs.size() + " 已检查" + jianchaNum + "  宝石" + baoshi + "  器灵" + qiling + ",isOver:" + isOver);
				if (isOver) {
					break;
				}
			} catch (Exception e) {
				logger.error("getAllArticleEntitys出错A:", e);
				break;
			}
		}
		useIDs.addAll(add);
		manager.releaseReference();
		logger.warn("装备中宝石器灵" + " 宝石=" + baoshi + " 器灵=" + qiling);
	}
	
	public static int getHorseArticleEntitysTOServer() throws Exception {
		SimpleEntityManager<Horse> manager = HorseManager.em;
		int num = 0;
		long count = manager.count();
		long start = 1;
		long end = 1;
		long pagSize = 5000;

		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<Horse> horses = manager.query(Horse.class, "", "", start, end);
			if (horses != null) {
				for (Horse h : horses) {
					long[] horseEquIDs = h.getEquipmentColumn().getEquipmentIds();
					for (long i : horseEquIDs) {
						if (i > 0) {
							useIDs.add(i);
							num++;
						}
					}
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[deleteNoInPlayerArticleEntity->从坐骑中取出entityID] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] {start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		return num;
	}
	
	public static int getPetArticleEntitysToServer() throws Exception {
		SimpleEntityManager<Pet> manager = PetManager.em;
		int num = 0;
		// long count = manager.count();
		long count = manager.count(Pet.class, " delete='F' or (delete='T' and rarity = 2)");
		long start = 1;
		long end = 1;
		long pagSize = 10000;

		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<PetProp> queryFields = manager.queryFields(PetProp.class, manager.queryIds(Pet.class, " delete='F' or (delete='T' and rarity = 2)", "id", start, end));
			for (PetProp pp : queryFields) {
				if (pp.getPetPropsId() > 0) {
					useIDs.add(pp.getPetPropsId());
					num++;
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[deleteNoInPlayerArticleEntity->从宠物中取出entityID] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] {start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		return num;
	}
	
	public static int getJiaZuCangKuArticleEntitysToServer() throws Exception {
		SimpleEntityManager<Jiazu> manager = JiazuManager.jiazuEm;
		int num = 0;
		long count = manager.count();
		long start = 1;
		long end = 1;
		long pagSize = 500;
		int entityNum = 0;
		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<Jiazu> jiazus = manager.query(Jiazu.class, "", "", start, end);
			if (jiazus != null) {
				for (Jiazu j : jiazus) {
					for (int i = 0; i < j.getWareHouse().size(); i++) {
						if (j.getWareHouse().get(i).getEntityId() > 0) {
							useIDs.add(j.getWareHouse().get(i).getEntityId());
							num++;
							entityNum++;
						}
					}
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[deleteNoInPlayerArticleEntity->从家族仓库中取出entityID] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] {start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		logger.warn("从家族中取出:" + entityNum);
		return num;
	}
	
	public static int getQianCengTaArticleEntitysToServer() throws Exception {
		SimpleEntityManager<QianCengTa_Ta> manager = QianCengTaManager.getInstance().em;
		int num = 0;
		long count = manager.count();
		long start = 1;
		long end = 1;
		long pagSize = 500;
		int entityNum = 0;
		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<QianCengTa_Ta> tas = manager.query(QianCengTa_Ta.class, "", "", start, end);
			if (tas != null) {
				for (QianCengTa_Ta q : tas) {
					for (int i = 0; i < q.getDaoList().size(); i++) {
						QianCengTa_Dao dao = q.getDaoList().get(i);
						for (int j = 0; j < dao.getCengList().size(); j++) {
							QianCengTa_Ceng ceng = dao.getCengList().get(j);
							for (int k = 0; k < ceng.getRewards().size(); k++) {
								if (ceng.getRewards().get(k).getEntityId() > 0) {
									useIDs.add(ceng.getRewards().get(k).getEntityId());
									num++;
									entityNum++;
								}
							}
						}
					}
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[deleteNoInPlayerArticleEntity->从千层塔奖励中取出entityID] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] {start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		logger.warn("从千层塔中取出:" + entityNum);
		return num;
	}
	
	public static int getPlayerArticleEntitysToServer() throws Exception {
		int num = 0;
		SimpleEntityManager<Player> manager = ((GamePlayerManager)GamePlayerManager.getInstance()).em;
		long count = manager.count();
		long start = 1;
		long end = 1;
		long pagSize = 2000;
		int beibao = 0; // 背包物品数
		int cangku = 0; // 仓库物品数
		int fangbaobao = 0; // 防暴包物品数+防暴包本身
		int zhuangbei = 0; // 装备数目，包括元神
		int qiling = 0;
		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<Player> players = manager.query(Player.class, "", "", start, end);
			if (players != null) {
				for (Player p : players) {
					int pFbb = 0;
					int pBb = 0;
					int pCk = 0;
					int pZb = 0;
					int pQl = 0;
					// 防暴包
					if (p.getKnapsack_fangBao_Id() > 0) {
						useIDs.add(p.getKnapsack_fangBao_Id());
						num++;
						fangbaobao++;
					}
					if (p.getKnapsack_fangbao() != null) {
						for (int i = 0; i < p.getKnapsack_fangbao().size(); i++) {
							if (p.getKnapsack_fangbao().getCell(i).getEntityId() > 0) {
								useIDs.add(p.getKnapsack_fangbao().getCell(i).getEntityId());
								num++;
								fangbaobao++;
								pFbb++;
							}
						}
					}
					// 背包
					for (int i = 0; i < p.getKnapsack_common().size(); i++) {
						if (p.getKnapsack_common().getCell(i).getEntityId() > 0) {
							useIDs.add(p.getKnapsack_common().getCell(i).getEntityId());
							num++;
							beibao++;
							pBb++;
						}
					}
					// 仓库
					for (int i = 0; i < p.getKnapsacks_cangku().size(); i++) {
						if (p.getKnapsacks_cangku().getCell(i).getEntityId() > 0) {
							useIDs.add(p.getKnapsacks_cangku().getCell(i).getEntityId());
							num++;
							cangku++;
							pCk++;
						}
					}
					// 装备
					for (int i = 0; i < p.getSouls().length; i++) {
						Soul soul = p.getSouls()[i];
						if (soul == null || soul.getEc() == null) {
							continue;
						}
						for (int j = 0; j < soul.getEc().getEquipmentIds().length; j++) {
							if (soul.getEc().getEquipmentIds()[j] > 0) {
								useIDs.add(soul.getEc().getEquipmentIds()[j]);
								num++;
								zhuangbei++;
								pZb++;
							}
						}
					}
					// 器灵
					for (int i = 0; i < p.getKnapsacks_QiLing().size(); i++) {
						if (p.getKnapsacks_QiLing().getCell(i).getEntityId() > 0) {
							useIDs.add(p.getKnapsacks_QiLing().getCell(i).getEntityId());
							num++;
							qiling++;
							pQl++;
						}
					}
					logger.warn("从玩家身上取出物品: [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "]");
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[deleteNoInPlayerArticleEntity->从player中取出entityID] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] {start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		logger.warn("从角色中取出: 背包=" + beibao + " 仓库=" + cangku + " 防暴=" + fangbaobao + " 装备=" + zhuangbei + " 器灵=" + qiling);
		return num;
	}
	
	public static void getMailArticleEntitysToServer() throws Exception {
		SimpleEntityManager<Mail> em = ((DefaultMailManager)(MailManager.getInstance())).em;
		long count = em.count();
		long start = 1;
		long end = 1;
		long pagSize = 500;
		int entityNum = 0;
		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<Mail> mails = em.query(Mail.class, "id > 0", "", start, end);
			if (mails != null) {
				for (Mail m : mails) {
					Cell[] cls = m.getCells();
					for (Cell cc : cls) {
						if (cc != null && cc.getCount() > 0 && cc.getEntityId() > 0) {
							useIDs.add(cc.getEntityId());
							entityNum++;
						}
					}
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[deleteNoInPlayerArticleEntity->从邮件中取出entityID] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] {start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		logger.warn("从邮件中取出:" + entityNum);
	}
	
	public static void getAuctionArticleEntitysToServer() {
		Hashtable<Long, Auction> map = AuctionManager.getInstance().getAuctionMap();
		int entityNum = 0;
		for (Auction a : map.values()) {
			useIDs.add(a.getArticleEntityId());
			entityNum++;
		}
		logger.warn("[从拍卖中取出] ["+entityNum+"]");
	}
}
