package com.fy.engineserver.datasource.article.data.equipments;

//import org.apache.log4j.Logger;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.articleEnchant.EnchantManager;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.res.Avata;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.career.SkillInfo;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.EQUIPMENT_SKILL_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NEW_QUERY_CAREER_INFO_BY_ID_RES;
import com.fy.engineserver.message.PLAYER_SOUL_CHANGE_RES;
import com.fy.engineserver.message.QUERY_ARTICLE_RES;
import com.fy.engineserver.message.QUERY_CAREER_INFO_BY_ID_RES;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.pet2.Pet2SkillCalc;
import com.fy.engineserver.talent.FlyTalentManager;
import com.fy.engineserver.util.ProbabilityUtils;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 武器，装备栏
 * 
 * 装备栏在下面的情况下，改变人物的属性
 * 1. 装载
 * 2. 卸载
 * 3. 耐久值消耗完毕，装备栏提供攻击一次和被攻击一次接口
 * 4. 修复
 * 
 * 与客户端的同步问题：
 * 1. 一种方案是客户端打开装备面版后，向服务器定期发送指令，服务器收到指令后，将
 * 装备栏的信息返回给客户端。这样可以极大的避免在客户端没有打开装备栏查看是，
 * 服务器发送大量的同步信息给客户端。
 * 2. 第二种方案是，服务器定期向客户端发送同步信息，只要装备栏发生变化，服务器就
 * 收集到这个变化，并且发送消息给客户端。由于装备存在磨损，所以变化是实时发生
 * 的。这样会导致大量的同步信息包。
 * 
 * 
 */
@SimpleEmbeddable
public class EquipmentColumn {

	public static Logger logger = LoggerFactory.getLogger(EquipmentColumn.class);

	/**
	 * 此变量可根据不同游戏而变，此变量只用于人物的装备，马匹装备不在这里
	 */
	public static String EQUIPMENT_TYPE_NAMES[] = new String[] { Translate.武器, Translate.头盔, Translate.护肩, Translate.胸, Translate.护腕, Translate.腰带, Translate.靴子, Translate.首饰, Translate.项链, Translate.戒指, Translate.翅膀, Translate.法宝 };

	/**
	 * 此变量可根据不同游戏而变
	 */
	public static String ALL_EQUIPMENT_TYPE_NAMES[] = new String[] { Translate.武器, Translate.头盔, Translate.护肩, Translate.胸, Translate.护腕, Translate.腰带, Translate.靴子, Translate.首饰, Translate.项链, Translate.戒指, Translate.面甲, Translate.颈甲, Translate.体铠, Translate.鞍铠, Translate.蹄甲, Translate.翅膀, Translate.法宝 };

	/**
	 * 此变量可根据不同游戏而变，标记每种装备对应的动画部件的编号，对应的是人物身上的scheme数组的下标
	 */
	public static int EQUIPMENT_AVATAR_MAP[] = new int[] { 5, 0, 1, 2, 3, 4, -1, 6 };

	/**
	 * 此变量标识装备强化过程中，激活额外属性的等级
	 */
	public static int EQUIPMENT_STRENGTHEN_ACTIVE_LEVELS[] = new int[] { 4, 8, 12, 16 };

	public static final byte EQUIPMENT_TYPE_weapon = 0;
	public static final byte EQUIPMENT_TYPE_head = 1;
	public static final byte EQUIPMENT_TYPE_shoulder = 2;
	public static final byte EQUIPMENT_TYPE_body = 3;
	public static final byte EQUIPMENT_TYPE_hand = 4;
	public static final byte EQUIPMENT_TYPE_belt = 5;
	public static final byte EQUIPMENT_TYPE_foot = 6;
	public static final byte EQUIPMENT_TYPE_fingerring = 7;
	public static final byte EQUIPMENT_TYPE_necklace = 8;
	public static final byte EQUIPMENT_TYPE_jewelry = 9;
	public static final byte EQUIPMENT_TYPE_ChiBang = 15;
	public static final byte EQUIPMENT_TYPE_fabao = 16;
	public static final byte EQUIPMENT_TYPE_FOR_PLAYER = 9;
	public static final int DURABILITY_STEP = 200;

	protected transient Player owner;

	private long equipmentIds[] = new long[EQUIPMENT_TYPE_NAMES.length];
	
	public transient String [] currEquipmentName = new String[EQUIPMENT_TYPE_NAMES.length];

	protected transient byte equipmentChangeFlags[] = new byte[EQUIPMENT_TYPE_NAMES.length];

	/**
	 * 标记是否修改过，服务器会定期扫描，发现修改后，会存盘
	 */
	transient boolean modified = false;

	public EquipmentColumn() {
		Arrays.fill(currEquipmentName, "");
		for (int i = 0; i < equipmentIds.length; i++) {
			equipmentIds[i] = -1;
		}
	}

	public long[] getEquipmentIds() {
		fixChiBang();
		fixfabao();
		return equipmentIds;
	}

	public void setEquipmentIds(long[] ids) throws Exception {
		if (ids.length == EQUIPMENT_TYPE_NAMES.length - 1) {
			// 处理法宝。
			System.arraycopy(ids, 0, this.equipmentIds, 0, ids.length);
			equipmentIds[EQUIPMENT_TYPE_fabao - 5] = -1;
		} else if(ids.length == EQUIPMENT_TYPE_NAMES.length - 2) {
			// 处理翅膀+法宝
			System.arraycopy(ids, 0, this.equipmentIds, 0, ids.length);
			equipmentIds[EQUIPMENT_TYPE_ChiBang - 5] = -1;
			equipmentIds[EQUIPMENT_TYPE_fabao - 5] = -1;
		}else if (this.equipmentIds.length != ids.length) {
			throw new Exception("[设置装备栏] [错误] [存储的装备数组长度" + ids.length + "不符合系统要求的长度" + this.equipmentIds.length + "]");
		} else {
			this.equipmentIds = ids;
		}
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
		if (modified && owner != null) {
			owner.notifyKnapsackDirty();
		}
	}

	public byte[] getChangeFlags() {
		return equipmentChangeFlags;
	}

	public void clearChangeFlags() {
		for (int i = 0; i < equipmentChangeFlags.length; i++) {
			equipmentChangeFlags[i] = 0;
		}
	}

	public ArticleEntity get(int index) {
		fixChiBang();
		fixfabao();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long id = equipmentIds[index];
		if (id <= 0) return null;
		return aem.getEntity(id);
	}

	public ArticleEntity[] getEquipmentArrayByCopy() {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleEntity ees[] = new ArticleEntity[equipmentIds.length];
		for (int i = 0; i < ees.length; i++) {
			if (equipmentIds[i] == -1) {
				ees[i] = null;
			} else {
				try {
					ees[i] = aem.getEntity(equipmentIds[i]);
				} catch (Exception e) {
					if (logger.isWarnEnabled()) logger.warn("[playerId:" + owner.getId() + "][" + owner.getName() + "][getEquipmentArrayByCopy] [" + equipmentIds[i] + "] [exception] e:", e);
				}
			}
		}
		return ees;
	}

	/**
	 * 修复某个装备
	 * 只修当前元神的
	 * @param index
	 */
	public void repaire(int index) {
		ArticleEntity aee = get(index);
		if(index != 11) {
			EquipmentEntity ee = (EquipmentEntity) aee;
			ArticleManager am = ArticleManager.getInstance();
			if (am != null && ee != null) {
				Equipment re = (Equipment) am.getArticle(ee.getArticleName());
				if (ee.isDurability() == false) {
					ee.setDurability(re.durability);
					if (ee.isDurability()) {
						if (owner.isCurrentEquipmentColumn(this)) {
							am.loaded(owner, ee, owner.getCurrSoul().getSoulType());
						}
					}
				} else {
					ee.setDurability(re.durability);
				}
				ee.setAttackDefenceCount(0);
				modified = true;
				CoreSubSystem.notifyEquipmentChange(owner, new EquipmentEntity[] { ee });
			}
		}
	}

	public void equipmentInlayOn(EquipmentEntity ee) {
	}

	public void equipmentInlayOff(EquipmentEntity ee) {
	}

	/**
	 * 对应的玩家死亡
	 */
	public void beKilled() {
		if (owner.isCurrentEquipmentColumn(this)) {
			ArticleManager am = ArticleManager.getInstance();
			for (int i = 0; i < equipmentIds.length; i++) {
				if(i == 11) {
					continue;
				}
				EquipmentEntity ee = (EquipmentEntity) get(i);
				if (am != null && ee != null) {
					if (ee.isDurability()) {
						Equipment re = (Equipment) am.getArticle(ee.getArticleName());
						if (re != null) {
							int d1 = ee.getDurability();
							double dlp = Player.得到玩家死亡掉落耐久百分比(owner);
							int d2 = (int) (re.getDurability() * dlp);
							
							Game game = owner.getCurrentGame();
//							if(game != null){
//								GameInfo gi = game.gi;
//								if(gi != null){
//									if(JJCManager.isJJCBattle(gi.name)){
//										if (logger.isInfoEnabled()) logger.info("[playerId:{}] [{}] [{}] [JJC中死亡不掉耐久] [死亡掉耐久{}] [{}] [{} -> {}]", new Object[] { owner.getId(), owner.getName(), owner.getUsername(), dlp, ee.getArticleName(), d1, ee.getDurability() });
//										return;
//									}
//								}
//							}
							ee.setDurability(d1 - d2);
							notifyDurLow(ee);

							// 副本统计
							if (game != null && game.getDownCity() != null) {
								DownCity dc = game.getDownCity();
								dc.statPlayerDurability(owner, ee, d2);
							}

							if (ee.getDurability() < 0) {
								ee.setDurability(0);
							}
							if (logger.isInfoEnabled()) logger.info("[playerId:{}] [{}] [{}] [死亡掉耐久{}] [{}] [{} -> {}]", new Object[] { owner.getId(), owner.getName(), owner.getUsername(), dlp, ee.getArticleName(), d1, ee.getDurability() });
							postReduceDur(am, i, ee);
						}
					}
				}
			}
		}
	}

	/**
	 * 对应的玩家攻击一次，武器耐久值减一
	 */
	public void attack() {
		if (owner.isCurrentEquipmentColumn(this)) {
			ArticleManager am = ArticleManager.getInstance();
			EquipmentEntity ee = (EquipmentEntity) get(EQUIPMENT_TYPE_weapon);
			if (am != null && ee != null) {
				if (ee.isDurability()) {
					int attackCount = ee.getAttackDefenceCount();
					attackCount++;
					ee.setAttackDefenceCount(attackCount);
					if (attackCount % DURABILITY_STEP == 0) {
						ee.setDurability(ee.getDurability() - 1);
						notifyDurLow(ee);
						if (ee.enchantType == EnchantEntityManager.附魔类型_属性 &&  !ee.getEnchantData().lostDurable(owner, ee, 1)) {			//附魔耐久消耗完移除附魔效果
//							EnchantEntityManager.instance.unLoadEnchantAttr(owner, ee);
							try {
								String des = String.format(Translate.附魔消失邮件标题, ee.getArticleName());
								MailManager.getInstance().sendMail(owner.getId(), new ArticleEntity[0], new int[0], Translate.附魔消失, des, 0L, 0L, 0L, "附魔消失");
								owner.sendError(des);
							} catch (Exception e) {
								EnchantManager.logger.warn("[checkPassiveEnchant] [附魔消失邮件通知] [异常] [" + owner.getLogString() + "]", e);
							}
						}
						try {
							if (ee.getEnchantData() != null && ee.getEnchantData() .getEnchants().size() > 0 && ee.getEnchantData().getEnchants().get(0).getDurable() < EnchantManager.耐久) {
								owner.sendError(String.format(Translate.低于10点通知, ee.getArticleName()));
							}
						} catch (Exception e) {
							EnchantManager.logger.warn("[checkPassiveEnchant] [低于10点通知] [异常] [" + owner.getLogString() + "]", e);
						}

						// 副本统计
						Game game = owner.getCurrentGame();
						if (game != null && game.getDownCity() != null) {
							DownCity dc = game.getDownCity();
							dc.statPlayerDurability(owner, ee, 1);
						}

						// logger.info("[" + ee.getArticleName() + "的使用耐久" + ee.getDurability() + "][playerId:" + owner.getId() + "][" + owner.getName() + "]");
						if (logger.isInfoEnabled()) logger.info("[{}的使用耐久{}] [playerId:{}] [{}] [{}]", new Object[] { ee.getArticleName(), ee.getDurability(), owner.getId(), owner.getName(), owner.getUsername() });
						if (ee.isDurability() == false) {
							am.unloaded(owner, ee, owner.getCurrSoul().getSoulType());
							// 下面是碎装的操作
							玩家碎装操作(EQUIPMENT_TYPE_weapon);
						}
						modified = true;
						CoreSubSystem.notifyEquipmentChange(owner, new EquipmentEntity[] { ee });
					}
				}
			}
		}
	}

	/**
	 * 当对人物造成伤害时对敌人任意装备降低耐久度5点
	 * @param value
	 */
	public void decreaseRandomDur(int value) {
		if (!owner.isCurrentEquipmentColumn(this)) {
			return;
		}
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			return;
		}
		int rnd = Pet2SkillCalc.getInst().rnd.nextInt(equipmentIds.length - 1);
		if(!(get(rnd) instanceof EquipmentEntity)) {
			return;
		}
		EquipmentEntity ee = (EquipmentEntity) get(rnd);
		if (ee == null) {
			return;
		}
		if (!ee.isDurability()) {
			return;
		}
		ee.setDurability(ee.getDurability() - value);
		notifyDurLow(ee);
		postReduceDur(am, rnd, ee);
	}

	/**
	 * 对应的玩家被攻击一次,除了武器外，其他装备耐久值减一
	 */
	public void beAttacked() {
		if (owner.isCurrentEquipmentColumn(this)) {
			ArticleManager am = ArticleManager.getInstance();
			for (int i = 0; i < EQUIPMENT_TYPE_NAMES.length; i++) {
				if (i == EQUIPMENT_TYPE_weapon) continue;
				if (i == 11) continue;

				EquipmentEntity ee = (EquipmentEntity) get(i);
				if (am != null && ee != null) {
					if (ee.isDurability()) {
						int beAttackCount = ee.getAttackDefenceCount();
						beAttackCount++;
						ee.setAttackDefenceCount(beAttackCount);
						if (beAttackCount % DURABILITY_STEP == 0) {
							ee.setDurability(ee.getDurability() - 1);
							notifyDurLow(ee);
							if (ee.getId() != owner.decreaseArticleId) {
								if (ee.enchantType == EnchantEntityManager.附魔类型_属性 && ee.getEnchantData() != null && !ee.getEnchantData().lostDurable(owner, ee, 1)) {			//附魔耐久消耗完移除附魔效果
//									EnchantEntityManager.instance.unLoadEnchantAttr(owner, ee);
									try {
										String des = String.format(Translate.附魔消失邮件标题, ee.getArticleName());
										MailManager.getInstance().sendMail(owner.getId(), new ArticleEntity[0], new int[0], Translate.附魔消失, des, 0L, 0L, 0L, "附魔消失");
										owner.sendError(des);
									} catch (Exception e) {
										EnchantManager.logger.warn("[checkPassiveEnchant] [附魔消失邮件通知] [异常] [" + owner.getLogString() + "]", e);
									}
								}
								try {
									if (ee.getEnchantData() != null && ee.getEnchantData() .getEnchants().size() > 0 && ee.getEnchantData().getEnchants().get(0).getDurable() < EnchantManager.耐久) {
										owner.sendError(String.format(Translate.低于10点通知, ee.getArticleName()));
									}
								} catch (Exception e) {
									EnchantManager.logger.warn("[checkPassiveEnchant] [低于10点通知] [异常] [" + owner.getLogString() + "]", e);
								}
							}

							// 副本统计
							Game game = owner.getCurrentGame();
							if (game != null && game.getDownCity() != null) {
								DownCity dc = game.getDownCity();
								dc.statPlayerDurability(owner, ee, 1);
							}

							if (logger.isInfoEnabled()) logger.info("[{}的耐久{}] [playerId:{}] [{}] [{}]", new Object[] { ee.getArticleName(), ee.getDurability(), owner.getId(), owner.getName(), owner.getUsername() });
							postReduceDur(am, i, ee);
						}
					}
				}
			}
		}
	}

	public void postReduceDur(ArticleManager am, int i, EquipmentEntity ee) {
		if (ee.isDurability() == false) {
			am.unloaded(owner, ee, owner.getCurrSoul().getSoulType());
			// 下面是碎装的操作
			玩家碎装操作(i);
		}
		modified = true;
		CoreSubSystem.notifyEquipmentChange(owner, new EquipmentEntity[] { ee });
	}

	public void notifyDurLow(EquipmentEntity ee) {
		if (ee.getDurability() <= 5) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.装备耐久提示, new String[][] { { Translate.STRING_1, ee.getArticleName() }, { Translate.COUNT_1, ee.getDurability() + "" } }));
			owner.addMessageToRightBag(req);
		}
	}

	public void 玩家碎装操作(int i) {
		if(i == 11) {
			logger.info("[法宝没有碎的操作]");
			return;
		}
		EquipmentEntity ee = (EquipmentEntity) get(i);
		if (ee == null) {
			return;
		}
		boolean sui = false;
		if (ee.isBinded()) {
			if (!ee.isInscriptionFlag()) {
				byte type = Player.根据罪恶值得到玩家的罪恶类型(owner);
				if (type >= Player.恶人_类型) {
					sui = true;
				}
			}
		} else {
			sui = true;
		}
		if (sui) {
			double sp = Player.得到玩家碎装几率(owner);
			if (ProbabilityUtils.randomProbability(owner.random, sp)) {
				takeOff(i, owner.getCurrSoul().getSoulType());
				String des = Translate.translateString(Translate.你的装备碎了, new String[][] { { Translate.STRING_1, ee.getArticleName() } });
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
				owner.addMessageToRightBag(hreq);
				MailManager mm = MailManager.getInstance();
				if (mm != null) {
					try {
						mm.sendMail(owner.getId(), new ArticleEntity[0], Translate.装备碎了, des, 0, 0, 0, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (logger.isInfoEnabled()) logger.info("[playerId:{}] [{}] [{}] [装备破碎:几率{}] [{}] [{}] [color:{}]", new Object[] { owner.getId(), owner.getName(), owner.getUsername(), sp, ee.getArticleName(), ee.getDurability(), ee.getColorType() });
			}
		}
	}
	/**
	 * 穿法宝
	 * @param ee
	 * @param soulType
	 */
	public void putOnMagicWeapon(NewMagicWeaponEntity ee, int soulType) {
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			throw new NullPointerException("ArticleManager is null");
		}
		Soul soul = owner.getSoul(soulType);
		if (soul == null) {
			logger.error("[玩家元神不存在] [{}] owenerId[{}] 元神类型[{}]", new Object[] { owner.getName(), owner.getId(), soulType });
			throw new IllegalStateException("玩家元神不存在");
		}
		boolean isCurrent = owner.getCurrSoul().getSoulType() == soulType;
		equipmentIds[11] = ee.getId();
		equipmentChangeFlags[11] = 1;
		modified = true;
		if (isCurrent) {
			owner.setDirty(true, "currSoul");
		} else {
			owner.setDirty(true, "unusedSoul");
		}
	}
	
	public void takeOffMagicWeapon(NewMagicWeaponEntity ee, int soulType) {
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			throw new NullPointerException("ArticleManager is null");
		}
		Soul soul = owner.getSoul(soulType);
		if (soul == null) {
			logger.error("[玩家元神不存在] [{}] owenerId[{}] 元神类型[{}]", new Object[] { owner.getName(), owner.getId(), soulType });
			throw new IllegalStateException("玩家元神不存在");
		}
		boolean isCurrent = owner.getCurrSoul().getSoulType() == soulType;
		if (isCurrent) {
			HuntLifeEntityManager.instance.unloadAllAttr(owner);
		}
		equipmentIds[11] = -1;
		equipmentChangeFlags[11] = 1;
		modified = true;
		if (isCurrent) {
			owner.setDirty(true, "currSoul");
		} else {
			owner.setDirty(true, "unusedSoul");
		}
	}

	/**
	 * 装载某个装备到人物身上
	 * 
	 * 此方法将自动修改玩家身上某些属性的公司中的系数
	 * 
	 * 但出现非常的情况，没有装备成功时，抛出异常。
	 * 
	 * @param ee
	 * @return
	 */
	public EquipmentEntity putOn(EquipmentEntity ee, int soulType) throws Exception {
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			throw new NullPointerException("ArticleManager is null");
		}

		Soul soul = owner.getSoul(soulType);
		boolean isCurrent = owner.getCurrSoul().getSoulType() == soulType;
		if (soul == null) {
			logger.error("[玩家元神不存在] [{}] owenerId[{}] 元神类型[{}]", new Object[] { owner.getName(), owner.getId(), soulType });
			throw new IllegalStateException("玩家元神不存在");
		}
		boolean isChibang = false;
		Article a = am.getArticle(ee.getArticleName());
		if (a != null && a instanceof Equipment) {
			Equipment e = (Equipment) a;
			String result = e.canUse(owner, soulType);
			if (result == null) {
				int index = e.getEquipmentType();
				if (index == EQUIPMENT_TYPE_ChiBang) {
					index = 10;
					isChibang = true;
				}
				EquipmentEntity r = (EquipmentEntity) get(index);
				
				if(r != null && !isChibang){
					try{
						if(r!=null && r.getColorType()>=6){
							am.handleEquipmentSuit(owner, r, soulType, 2);
						}
					}catch(Exception ex){
						ex.printStackTrace();
						logger.error("[处理装备套装属性] [卸载装备套装属性] [异常] [index:"+index+"] ["+owner.getLogString()+"] ["+ex+"]");
					}
				}
				
				equipmentIds[index] = ee.getId();
				currEquipmentName[index] = ee.getArticleName();
				int oldStar = 0;
				int oldColor = 0;
				int newStar = 0;
				int newColor = 0;
				int newLevel = e.getPlayerLevelLimit();
				int qilingNum = 0;
				int baoshiNum = 0;
				int[] baoshiLevel = new int[24];
				int isMingKe = 0;
				int tempIndex = 0;
				int qilin4PurpleNum = 0;
				int qilin4Orange = 0;
				int[] tempStar = new int[10];
				if (r != null) {
					oldStar = r.getStar();
					oldColor = r.getColorType();

				}
				if (ee != null) {
					newStar = ee.getStar();
					newColor = ee.getColorType();
				}

				if (a.getBindStyle() == Article.BINDTYPE_USE || a.getBindStyle() == Article.BINDTYPE_PICKUP || a.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP) {
					if (ee.isBinded() == false) {
						ee.setBinded(true);

						QUERY_ARTICLE_RES res = null;

						res = new QUERY_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[0], new com.fy.engineserver.datasource.article.entity.client.PropsEntity[0], new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate((EquipmentEntity) ee) });

						owner.addMessageToRightBag(res);
					}
				}

				equipmentChangeFlags[index] = 1;
				modified = true;

				if (r != null) {
					if (logger.isInfoEnabled()) {
						logger.info("[装备栏] [{}] [因装载卸载] [耐久：{}] [playerId:{}] [{}] [{}] [{}] [level:{}] [id:{}]", new Object[] { (owner.isCurrentEquipmentColumn(this) ? "当前" : "备用"), r.getDurability(), owner.getId(), owner.getName(), owner.getUsername(), r.getArticleName(), r.getLevel(), r.getId() });
					}
				}

				if (logger.isInfoEnabled()) {
					logger.info("[装备栏] [{}] [装载] [耐久：{}] [playerId:{}] [{}] [{}] [{}] [level:{}] [id:{}]", new Object[] { (owner.isCurrentEquipmentColumn(this) ? "当前" : "备用"), ee.getDurability(), owner.getId(), owner.getName(), owner.getUsername(), ee.getArticleName(), ee.getLevel(), ee.getId() });
				}
				
				try{
					if(!isChibang){
						if(ee!=null && ee.getColorType()>=6){
							am.handleEquipmentSuit(owner, ee, soulType, 1);
						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
					logger.error("[处理装备套装属性] [使用装备套装属性] [异常] ["+owner.getLogString()+"] ["+ex+"]");
				}

				if (r != null) {
					Equipment re = (Equipment) am.getArticle(r.getArticleName());
					// 套装属性修改(此修改没有理会装备耐久度)

					// 增加星级套和颜色套属性
					{
						ArticleEntity[] ees = getEquipmentArrayByCopy();
						int suitCount = 0;
						// 翅膀不加入计算
						for (int i = 0; i < ees.length; i++) {
							if (i == ees.length - 1 || i == ees.length - 2 || ees[i] instanceof NewMagicWeaponEntity) {		//法宝也不计算
								continue;
							}
							EquipmentEntity eqe = (EquipmentEntity) ees[i];
							if (eqe != null) {
								Equipment equipment = (Equipment) (ArticleManager.getInstance().getArticle(eqe.getArticleName()));
								if (equipment.getEquipmentType() == EQUIPMENT_TYPE_ChiBang) {
									continue;
								}
							}
							if (i == index) {
								if (eqe != null) {
									Article equipment = am.getArticle(eqe.getArticleName());
									if (equipment != null && equipment instanceof Equipment) {
										suitCount += 1;
										if (tempIndex < tempStar.length) {
											tempStar[tempIndex] = eqe.getStar();
											tempIndex+=1;
										}
										if (eqe.getStar() < newStar) {
											newStar = eqe.getStar();
										}
										if (eqe.getColorType() < newColor) {
											newColor = eqe.getColorType();
										}
										if (r.getStar() < oldStar) {
											oldStar = r.getStar();
										}
										if (r.getColorType() < oldColor) {
											oldColor = r.getColorType();
										}
										if (((Equipment)equipment).getPlayerLevelLimit() < newLevel) {
											newLevel = ((Equipment)equipment).getPlayerLevelLimit();
										}
										if (eqe.isInscriptionFlag()) {
											isMingKe++;
										}
										
										if(eqe.getInlayQiLingArticleIds() != null && eqe.getInlayQiLingArticleIds().length >= 4) {
											boolean fflag = true;
											for(int iii = 0; iii<eqe.getInlayQiLingArticleIds().length; iii++) {
												if(eqe.getInlayQiLingArticleIds()[iii] <= 0) {
													fflag = false;
													break;
												}
											}
											if(fflag) {
												qilingNum++;
											}
										}
										if(eqe.getInlayArticleIds() != null ) {
											for (long ll : eqe.getInlayArticleIds()) {
												if (ll > 0) {
													ArticleEntity baoshiEntity = DefaultArticleEntityManager.getInstance().getEntity(ll);
													if (baoshiEntity != null) {
														if (baoshiNum < baoshiLevel.length) {
															baoshiLevel[baoshiNum] = ArticleManager.得到宝石等级(baoshiEntity.getArticleName());
														}
														baoshiNum ++;
													}
												}
											}
										}
											
										if (eqe.getInlayQiLingArticleIds() != null) {
											for (long ll : eqe.getInlayQiLingArticleIds()) {
												if (ll > 0) {
													ArticleEntity qilinEntity = DefaultArticleEntityManager.getInstance().getEntity(ll);
													if (qilinEntity != null && qilinEntity.getColorType() >= 3) {
														qilin4PurpleNum++;
														if (qilinEntity.getColorType() >= 4) {
															qilin4Orange++;
														}
													}
												}
											}
										}
									}
								} else {
									oldStar = 0;
									oldColor = 0;
									newStar = 0;
									newColor = 0;
									newLevel = 0;
								}
							} else {
								if (eqe != null) {
									Article equipment = am.getArticle(eqe.getArticleName());
									if (equipment != null && equipment instanceof Equipment) {
										suitCount += 1;
										if (tempIndex < tempStar.length) {
											tempStar[tempIndex] = eqe.getStar();
											tempIndex+=1;
										}
										if (eqe.getStar() < newStar) {
											newStar = eqe.getStar();
										}
										if (eqe.getColorType() < newColor) {
											newColor = eqe.getColorType();
										}
										if (eqe.isInscriptionFlag()) {
											isMingKe++;
										}
										if (eqe.getStar() < oldStar) {
											oldStar = eqe.getStar();
										}
										if (eqe.getColorType() < oldColor) {
											oldColor = eqe.getColorType();
										}
										if (((Equipment)equipment).getPlayerLevelLimit() < newLevel) {
											newLevel = ((Equipment)equipment).getPlayerLevelLimit();
										}
										if(eqe.getInlayQiLingArticleIds() != null && eqe.getInlayQiLingArticleIds().length >= 4) {
											boolean fflag = true;
											for(int iii = 0; iii<eqe.getInlayQiLingArticleIds().length; iii++) {
												if(eqe.getInlayQiLingArticleIds()[iii] <= 0) {
													fflag = false;
													break;
												}
											}
											if(fflag) {
												qilingNum++;
											}
										}
										if(eqe.getInlayArticleIds() != null ) {
											for (long ll : eqe.getInlayArticleIds()) {
												if (ll > 0) {
													ArticleEntity baoshiEntity = DefaultArticleEntityManager.getInstance().getEntity(ll);
													if (baoshiEntity != null) {
														if (baoshiNum < baoshiLevel.length) {
															baoshiLevel[baoshiNum] = ArticleManager.得到宝石等级(baoshiEntity.getArticleName());
														}
														baoshiNum ++;
													}
												}
											}
										}
										if (eqe.getInlayQiLingArticleIds() != null) {
											for (long ll : eqe.getInlayQiLingArticleIds()) {
												if (ll > 0) {
													ArticleEntity qilinEntity = DefaultArticleEntityManager.getInstance().getEntity(ll);
													if (qilinEntity != null && qilinEntity.getColorType() >= 3) {
														qilin4PurpleNum++;
														if (qilinEntity.getColorType() >= 4) {
															qilin4Orange++;
														}
													}
												}
											}
										}
									}
								} else {
									oldStar = 0;
									oldColor = 0;
									newStar = 0;
									newColor = 0;
									newLevel = 0;
								}
							}

						}
						if (logger.isInfoEnabled()) {
							logger.info("[卸下原有套装，并装载现有套装] [套装数量：{}] [playerId:{}] [{}] [{}]", new Object[] { suitCount, owner.getId(), owner.getName(), owner.getUsername() });
						}
						if (!isChibang) {
							am.unloadSuitProperty(owner, oldStar, oldColor, suitCount, soulType);
							am.loadSuitProperty(owner, newStar, newColor, suitCount, soulType, newLevel, qilingNum, baoshiNum, isMingKe, baoshiLevel, tempStar, qilin4PurpleNum, qilin4Orange);
						}
					}

					if (r.isDurability()) {
						if (logger.isInfoEnabled()) {
							logger.info("[卸载装备] [装备({})的级别：{}] [playerId:{}] [{}] [{}]", new Object[] { r.getArticleName(), r.getLevel(), owner.getId(), owner.getName(), owner.getUsername() });
						}
						if (re != null) {
							am.unloaded(owner, r, soulType);
						}
					}
				} else {
					// 增加星级套颜色套属性
					{
						ArticleEntity[] ees = getEquipmentArrayByCopy();
						int suitCount = 0;
						// 翅膀不加入计算
						for (int i = 0; i < ees.length; i++) {
							if (i == ees.length - 1 || i == ees.length - 2) {
								continue;
							}
							EquipmentEntity eqe = (EquipmentEntity) ees[i];
							if (eqe != null) {
								Equipment equipment = (Equipment) (ArticleManager.getInstance().getArticle(eqe.getArticleName()));
								if (equipment.getEquipmentType() == EQUIPMENT_TYPE_ChiBang) {
									continue;
								}
							}
							if (i == index) {
								if (eqe != null) {
									Article equipment = am.getArticle(eqe.getArticleName());
									if (equipment != null && equipment instanceof Equipment) {
										suitCount += 1;
										if (tempIndex < tempStar.length) {
											tempStar[tempIndex] = eqe.getStar();
											tempIndex+=1;
										}
										if (eqe.getStar() < newStar) {
											newStar = eqe.getStar();
										}
										if (eqe.isInscriptionFlag()) {
											isMingKe++;
										}
										if (eqe.getColorType() < newColor) {
											newColor = eqe.getColorType();
										}
										if (((Equipment)equipment).getPlayerLevelLimit() < newLevel) {
											newLevel = ((Equipment)equipment).getPlayerLevelLimit();
										}
										if(eqe.getInlayQiLingArticleIds() != null && eqe.getInlayQiLingArticleIds().length >= 4) {
											boolean fflag = true;
											for(int iii = 0; iii<eqe.getInlayQiLingArticleIds().length; iii++) {
												if(eqe.getInlayQiLingArticleIds()[iii] <= 0) {
													fflag = false;
													break;
												}
											}
											if(fflag) {
												qilingNum++;
											}
										}
										if(eqe.getInlayArticleIds() != null ) {
											for (long ll : eqe.getInlayArticleIds()) {
												if (ll > 0) {
													ArticleEntity baoshiEntity = DefaultArticleEntityManager.getInstance().getEntity(ll);
													if (baoshiEntity != null) {
														if (baoshiNum < baoshiLevel.length) {
															baoshiLevel[baoshiNum] = ArticleManager.得到宝石等级(baoshiEntity.getArticleName());
														}
														baoshiNum ++;
													}
												}
											}
										}
										if (eqe.getInlayQiLingArticleIds() != null) {
											for (long ll : eqe.getInlayQiLingArticleIds()) {
												if (ll > 0) {
													ArticleEntity qilinEntity = DefaultArticleEntityManager.getInstance().getEntity(ll);
													if (qilinEntity != null && qilinEntity.getColorType() >= 3) {
														qilin4PurpleNum++;
														if (qilinEntity.getColorType() >= 4) {
															qilin4Orange++;
														}
													}
												}
											}
										}
									}
								} else {
									newStar = 0;
									newColor = 0;
									newLevel = 0;
								}
								oldStar = 0;
								oldColor = 0;
							} else {
								if (eqe != null) {
									Article equipment = am.getArticle(eqe.getArticleName());
									if (equipment != null && equipment instanceof Equipment) {
										suitCount += 1;
										if (tempIndex < tempStar.length) {
											tempStar[tempIndex] = eqe.getStar();
											tempIndex+=1;
										}
										if (eqe.getStar() < newStar) {
											newStar = eqe.getStar();
										}
										if (eqe.getColorType() < newColor) {
											newColor = eqe.getColorType();
										}
										if (eqe.isInscriptionFlag()) {
											isMingKe++;
										}
										if (eqe.getStar() < oldStar) {
											oldStar = eqe.getStar();
										}
										if (eqe.getColorType() < oldColor) {
											oldColor = eqe.getColorType();
										}
										if (((Equipment)equipment).getPlayerLevelLimit() < newLevel) {
											newLevel = ((Equipment)equipment).getPlayerLevelLimit();
										}
										if(eqe.getInlayQiLingArticleIds() != null && eqe.getInlayQiLingArticleIds().length >= 4) {
											boolean fflag = true;
											for(int iii = 0; iii<eqe.getInlayQiLingArticleIds().length; iii++) {
												if(eqe.getInlayQiLingArticleIds()[iii] <= 0) {
													fflag = false;
													break;
												}
											}
											if(fflag) {
												qilingNum++;
											}
										}
										if(eqe.getInlayArticleIds() != null ) {
											for (long ll : eqe.getInlayArticleIds()) {
												if (ll > 0) {
													ArticleEntity baoshiEntity = DefaultArticleEntityManager.getInstance().getEntity(ll);
													if (baoshiEntity != null) {
														if (baoshiNum < baoshiLevel.length) {
															baoshiLevel[baoshiNum] = ArticleManager.得到宝石等级(baoshiEntity.getArticleName());
														}
														baoshiNum ++;
													}
												}
											}
										}
										if (eqe.getInlayQiLingArticleIds() != null) {
											for (long ll : eqe.getInlayQiLingArticleIds()) {
												if (ll > 0) {
													ArticleEntity qilinEntity = DefaultArticleEntityManager.getInstance().getEntity(ll);
													if (qilinEntity != null && qilinEntity.getColorType() >= 3) {
														qilin4PurpleNum++;
														if (qilinEntity.getColorType() >= 4) {
															qilin4Orange++;
														}
													}
												}
											}
										}
									}
								} else {
									oldStar = 0;
									oldColor = 0;
									newStar = 0;
									newColor = 0;
									newLevel = 0;
								}
							}

						}
						if (logger.isInfoEnabled()) {
							logger.info("[原有部位没有装备，装载装备] [套装数量：{}] [playerId:{}] [{}] [{}]", new Object[] { suitCount, owner.getId(), owner.getName(), owner.getUsername() });
						}
						if (!isChibang) {
							am.unloadSuitProperty(owner, oldStar, oldColor, suitCount - 1, soulType);
							am.loadSuitProperty(owner, newStar, newColor, suitCount, soulType, newLevel, qilingNum, baoshiNum, isMingKe, baoshiLevel, tempStar, qilin4PurpleNum, qilin4Orange);
						}
					}
				}

				// 装备技能
				if (r != null) {
					Equipment re = (Equipment) am.getArticle(r.getArticleName());
					ArticleEntity[] ees = getEquipmentArrayByCopy();
					boolean has = false;
					if (re.getSkillId() > 0) {
						for (int i = 0; i < ees.length; i++) {
							if(i == ees.length - 1) {
								continue;
							}
							EquipmentEntity eqe = (EquipmentEntity) ees[i];
							if (eqe != null) {
								Article equipment = am.getArticle(eqe.getArticleName());
								if (equipment != null && equipment instanceof Equipment) {
									if (re.getSkillId() == ((Equipment) equipment).getSkillId()) {
										has = true;
										break;
									}
								}
							}
						}
						if (!has) {
							EQUIPMENT_SKILL_REQ req = new EQUIPMENT_SKILL_REQ(GameMessageFactory.nextSequnceNum(), re.getSkillId(), soulType, (byte) 1);
							owner.addMessageToRightBag(req);
						}
					}
				}
				if (e.getSkillId() > 0) {
					EQUIPMENT_SKILL_REQ req = new EQUIPMENT_SKILL_REQ(GameMessageFactory.nextSequnceNum(), e.getSkillId(), soulType, (byte) 0);
					owner.addMessageToRightBag(req);
					CareerManager cm = CareerManager.getInstance();
					if (cm != null) {
						Skill skill = cm.getSkillById(e.getSkillId());
						if (skill != null) {
							SkillInfo si = new SkillInfo();
							si.setInfo(owner, skill);
							QUERY_CAREER_INFO_BY_ID_RES qciRes = new QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
							owner.addMessageToRightBag(qciRes);
							NEW_QUERY_CAREER_INFO_BY_ID_RES qciRes1 = new NEW_QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
							owner.addMessageToRightBag(qciRes1);
						}
					}
				}

				if (ee.isDurability()) {
					if (logger.isInfoEnabled()) {
						logger.info("[装载装备] [装备({})的级别：{}] [playerId:{}] [{}] [{}]", new Object[] { ee.getArticleName(), ee.getLevel(), owner.getId(), owner.getName(), owner.getUsername() });
					}
					am.loaded(owner, ee, soulType);
				}

				PLAYER_SOUL_CHANGE_RES res = new PLAYER_SOUL_CHANGE_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, soul);
				owner.addMessageToRightBag(res);

				if (isCurrent) {
					Avata av = ResourceManager.getInstance().getAvata(owner);
					owner.setAvata(av.avata);
					owner.setAvataType(av.avataType);
					owner.modifyShouAvata();

					if (e instanceof Weapon) {
						owner.setWeaponType(((Weapon) e).getWeaponType());
					}

					// 判断装备发光发光效果

					if (e instanceof Weapon && e.getStrongParticles() != null && e.getStrongParticles().length > ee.getLevel()) {
						String particle = e.getStrongParticles()[ee.getLevel()];
						owner.setWeaponParticle(particle);
					}
				}

				if (isCurrent) {
					owner.setDirty(true, "currSoul");
				} else {
					owner.setDirty(true, "unusedSoul");
				}
				
				try{
					FlyTalentManager.getInstance().handle_附体期间穿脱装备(owner, ee, 1);
				}catch(Exception e2){
					e2.printStackTrace();
					logger.warn("[附体期间穿脱装备] [穿] [处理异常] ["+owner.getLogString()+"]",e2);
				}
				
				try {
					if(e instanceof Weapon) {
						EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { owner.getId(), RecordAction.穿戴一件武器, 1L});
						EventRouter.getInst().addEvent(evt);
						EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { owner.getId(), RecordAction.穿戴一件N星以上武器, ee.getStar()});
						EventRouter.getInst().addEvent(evt2);
					}
					if(ee.getColorType() >= 2) {
						EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { owner.getId(), RecordAction.穿一件蓝色以上装备, 1L});
						EventRouter.getInst().addEvent(evt);
					}
				} catch (Exception eee) {
					PlayerAimManager.logger.error("[目标系统] [统计玩家穿戴装备异常] [" + owner.getLogString() + "]", eee);
				}
				
				return r;
			} else {
				logger.error("[装备栏] [{}] [玩家不满足装备条件] [{}] [id={},name={}] [playerId:{}] [{}] [{}]", new Object[] { result, (owner.isCurrentEquipmentColumn(this) ? "当前" : "备用"), ee.getId(), ee.getArticleName(), owner.getId(), owner.getName(), owner.getUsername() });
				throw new Exception(result);
			}
		} else {
			logger.error("[装备栏] [{}] [装备实体对应的装备不存在] [id={},name={}] [playerId:{}] [{}] [{}]", new Object[] { (owner.isCurrentEquipmentColumn(this) ? "当前" : "备用"), ee.getId(), ee.getArticleName(), owner.getId(), owner.getName(), owner.getUsername() });
			throw new Exception("装备实体[" + ee.getArticleName() + "]对应的装备不存在");
		}

	}
	
	public NewMagicWeaponEntity takeOffMw(int index, int soulType) throws Exception {
		if(index != 11) {
			logger.error("[脱法宝] [错误] [" + index + "] [" + owner.getLogString() + "]");
			return null;
		}
		NewMagicWeaponEntity r = (NewMagicWeaponEntity) get(index);
		if(r == null) {
			return null;
		}
		Soul soul = owner.getSoul(soulType);
		boolean isCurrent = owner.getCurrSoul().getSoulType() == soulType;
		if (soul == null) {
			throw new IllegalStateException("卸下装备异常 元神不存在[PLAYER:" + owner.getName() + "][元神类型[" + soulType + "]");
		}
		if (isCurrent) {
			HuntLifeEntityManager.instance.unloadAllAttr(owner);
		}
		equipmentIds[index] = -1;

		equipmentChangeFlags[index] = 2;
		modified = true;
		MagicWeaponManager.instance.putOff(owner, r, soulType, false);
		if (isCurrent) {
			owner.setDirty(true, "currSoul");
		} else {
			owner.setDirty(true, "unusedSoul");
		}
		return r;
	}

	/**
	 * 将某个装备卸下来
	 * 
	 * @param index
	 * @return
	 */
	public EquipmentEntity takeOff(int index, int soulType) {
		if(index == 11) {
			throw new IllegalStateException("卸下装备异常 法宝走新协议卸除[PLAYER:" + owner.getName() + "][元神类型[" + soulType + "]");
		}
		ArticleManager am = ArticleManager.getInstance();
		EquipmentEntity r = (EquipmentEntity) get(index);
		Soul soul = owner.getSoul(soulType);

		boolean isCurrent = owner.getCurrSoul().getSoulType() == soulType;
		if (soul == null) {
			throw new IllegalStateException("卸下装备异常 元神不存在[PLAYER:" + owner.getName() + "][元神类型[" + soulType + "]");
		}
		
		int oldStar = 0;
		int oldColor = 0;
		boolean isChibang = false;
		if (r != null) {
			oldStar = r.getStar();
			oldColor = r.getColorType();
			Equipment equipment = (Equipment) (ArticleManager.getInstance().getArticle(r.getArticleName()));
			if (equipment.getEquipmentType() == EQUIPMENT_TYPE_ChiBang) {
				isChibang = true;
			}
		}
		
		if (!isChibang) {
			try{
				if(r != null && r.getColorType()>=6){
					am.handleEquipmentSuit(owner, r, soulType, 2);
				}
			}catch(Exception ex){
				ex.printStackTrace();
				logger.error("[处理装备套装属性] [takeoff] [异常] ["+owner.getLogString()+"] ["+ex+"]");
			}
		}
		
		
		equipmentIds[index] = -1;
		currEquipmentName[index] = "";
		equipmentChangeFlags[index] = 2;
		modified = true;

		if (r != null) {
			if (logger.isInfoEnabled()) {
				logger.info("[装备栏] [{}] [卸载] [耐久：{}] [{}] [{}] [level:{}] [id:{}]", new Object[] { (owner.isCurrentEquipmentColumn(this) ? "当前" : "备用"), r.getDurability(), owner.getName(), r.getArticleName(), r.getLevel(), r.getId() });
			}
		}

		if (r != null) {
			Equipment re = (Equipment) am.getArticle(r.getArticleName());
			// 套装属性修改(此修改没有理会装备耐久度)

			// 增加星级套和颜色套属性
			{
				ArticleEntity[] ees = getEquipmentArrayByCopy();
				int suitCount = 0;

				for (int i = 0; i < ees.length; i++) {
					if (i == ees.length - 1 || i == ees.length - 2) {
						continue;
					}
					EquipmentEntity eqe = (EquipmentEntity) ees[i];
					if (eqe != null) {
						Equipment equipment = (Equipment) (ArticleManager.getInstance().getArticle(eqe.getArticleName()));
						if (equipment.getEquipmentType() == EQUIPMENT_TYPE_ChiBang) {
							continue;
						}
					}
					if (i == index) {
						suitCount += 1;
					} else {
						if (eqe != null) {
							Article equipment = am.getArticle(eqe.getArticleName());
							if (equipment != null && equipment instanceof Equipment) {
								suitCount += 1;
								if (eqe.getStar() < oldStar) {
									oldStar = eqe.getStar();
								}
								if (eqe.getColorType() < oldColor) {
									oldColor = eqe.getColorType();
								}
							}
						} else {
							oldStar = 0;
							oldColor = 0;
						}
					}

				}
				if (logger.isInfoEnabled()) {
					logger.info("[卸下原有套装] [套装数量：{}] [playerId:{}] [{}] [{}]", new Object[] { suitCount, owner.getId(), owner.getName(), owner.getUsername() });
				}
				if (!isChibang) {
					am.unloadSuitProperty(owner, oldStar, oldColor, suitCount, soulType);
				}
			}

			if (r.isDurability()) {
				if (logger.isInfoEnabled()) {
					logger.info("[卸载装备] [装备({})的级别：{}] [playerId:{}] [{}] [{}]", new Object[] { r.getArticleName(), r.getLevel(), owner.getId(), owner.getName(), owner.getUsername() });
				}
				if (re != null) {
					am.unloaded(owner, r, soulType);
				}
			}

			if (re instanceof Weapon) {
				owner.setWeaponParticle("");
			}

			PLAYER_SOUL_CHANGE_RES res = new PLAYER_SOUL_CHANGE_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, soul);
			owner.addMessageToRightBag(res);
			if (isCurrent) {
				Avata av = ResourceManager.getInstance().getAvata(owner);
				owner.setAvata(av.avata);
				owner.setAvataType(av.avataType);
				owner.modifyShouAvata();
				owner.setDirty(true, "currSoul");
			} else {
				owner.setDirty(true, "unusedSoul");
			}
		}

		// 装备技能
		if (r != null) {
			Equipment re = (Equipment) am.getArticle(r.getArticleName());
			ArticleEntity[] ees = getEquipmentArrayByCopy();
			boolean has = false;
			if (re.getSkillId() > 0) {
				for (int i = 0; i < ees.length; i++) {
					if(i == ees.length - 1) {
						continue;
					}
					EquipmentEntity eqe = (EquipmentEntity) ees[i];
					if (eqe != null) {
						Article equipment = am.getArticle(eqe.getArticleName());
						if (equipment != null && equipment instanceof Equipment) {
							if (re.getSkillId() == ((Equipment) equipment).getSkillId()) {
								has = true;
								break;
							}
						}
					}
				}
				if (!has) {
					EQUIPMENT_SKILL_REQ req = new EQUIPMENT_SKILL_REQ(GameMessageFactory.nextSequnceNum(), re.getSkillId(), soulType, (byte) 1);
					owner.addMessageToRightBag(req);
				}
			}
		}
		
		try{
			FlyTalentManager.getInstance().handle_附体期间穿脱装备(owner, r, 2);
		}catch(Exception e2){
			e2.printStackTrace();
			logger.warn("[附体期间穿脱装备] [脱] [处理异常] ["+owner.getLogString()+"]",e2);
		}
		return r;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setEquipmentChangeFlags(byte[] equipmentChangeFlags) {
		this.equipmentChangeFlags = equipmentChangeFlags;
	}

	public void setEquipments_(EquipmentEntity[] equipments) {
		for (int i = 0; i < equipments.length; i++) {
			if (equipments[i] != null) {
				ArticleManager am = ArticleManager.getInstance();
				Article ar = am.getArticle(equipments[i].getArticleName());
				if (ar != null && ar instanceof Equipment) {
					Equipment e = (Equipment) ar;
					this.equipmentIds[e.getEquipmentType()] = equipments[i].getId();
				}
			}
		}
	}

	public void fixChiBang() {
		if (equipmentIds.length < EQUIPMENT_TYPE_NAMES.length) {
			equipmentIds = ArrayUtils.add(equipmentIds, -1);
			setModified(true);
			logger.debug("EquipmentColumn.fixChiBang: ");
		}
	}
	public void fixfabao() {
		if (equipmentIds.length < EQUIPMENT_TYPE_NAMES.length) {
			equipmentIds = ArrayUtils.add(equipmentIds, -1);
			setModified(true);
			logger.debug("EquipmentColumn.fixfabao: ");
		}
	}

}
