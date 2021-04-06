package com.fy.engineserver.datasource.article.concrete;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.activity.dig.DigPropsEntity;
import com.fy.engineserver.activity.explore.ExploreResourceMapEntity;
import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.billboard.special.SpecialEquipmentManager;
import com.fy.engineserver.combat.CombatCaculator;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.ExchangeArticle;
import com.fy.engineserver.datasource.article.data.articles.QiLingArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData;
import com.fy.engineserver.datasource.article.data.entity.BottlePropsEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.ExchangeArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.FateActivityPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.HunshiEntity;
import com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.MagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.SoulPithArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.Special_1EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.Special_2EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.YinPiaoEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeArticle;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager;
import com.fy.engineserver.datasource.article.data.magicweapon.model.AdditiveAttrModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponAttrModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponBaseModel;
import com.fy.engineserver.datasource.article.data.props.ArticleProperty;
import com.fy.engineserver.datasource.article.data.props.BottleProps;
import com.fy.engineserver.datasource.article.data.props.DigProps;
import com.fy.engineserver.datasource.article.data.props.ExploreResourceMap;
import com.fy.engineserver.datasource.article.data.props.FateActivityProps;
import com.fy.engineserver.datasource.article.data.props.HunshiProps;
import com.fy.engineserver.datasource.article.data.props.MagicWeaponProps;
import com.fy.engineserver.datasource.article.data.props.PetEggProps;
import com.fy.engineserver.datasource.article.data.props.PetProps;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.data.props.SingleForPetProps;
import com.fy.engineserver.datasource.article.data.props.WingProps;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticle;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.soulpith.SoulPithEntityManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.SingleForPetPropsEntity;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticle;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticleEntity;
import com.fy.engineserver.time.SystemMaintainManager;
import com.fy.engineserver.time.Timer;
import com.fy.engineserver.util.ProbabilityUtils;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.CacheObject;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.DateUtil;

public class DefaultArticleEntityManager extends ArticleEntityManager implements Runnable {
	public static boolean forceToChiBangBind = true;
	// 更新周期，s
	protected long updatePeriod;

	protected boolean running = true;
	protected Thread thread;

	// 本地缓存
	protected static LruMapCache mCache;

	// 内存中的物品实体，这些物品如果需要存入数据库，那么就往mcache中放，如果不需要存入数据库就在物品消失的时候删除
	protected static Hashtable<Long, ArticleEntity> aTable = new Hashtable<Long, ArticleEntity>();

	// 可堆叠物品缓存,对于创建可堆叠的物品的请求，直接返回已创建的可堆叠物品
	protected static Hashtable<String, ArticleEntity> overlapEntityMap;

	// 内存中的临时物品实体，这些不会存放到数据库中
	public static Hashtable<Long, ArticleEntity> aTempTable = new Hashtable<Long, ArticleEntity>();

	// 内存中的临时物品实体，这些不会存放到数据库中
	protected static Hashtable<String, ArticleEntity> overlapTempEntityMap = new Hashtable<String, ArticleEntity>();

	public static Random random = new Random();

	public void initialize() throws Exception {
		

		em = SimpleEntityManagerFactory.getSimpleEntityManager(ArticleEntity.class);
		baoShiXiLianEM = SimpleEntityManagerFactory.getSimpleEntityManager(BaoShiXiaZiData.class);

		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		mCache = new LruMapCache(512 * 1024 * 1024, 8 * 60 * 60 * 1000);
		overlapEntityMap = new Hashtable<String, ArticleEntity>();

		self = this;

		thread = new Thread(this, "DefaultArticleEntityManager");
		thread.start();

		ServiceStartRecord.startLog(this);
	}

	public void setUpdatePeriod(long updatePeriod) {
		this.updatePeriod = updatePeriod;
	}

	private synchronized long nextId() throws Exception {
		return em.nextId();
	}

	@Override
	public ArticleEntity createEntity(Article a, boolean binded, int reason, Player player, int color, int referenceCount, int level, boolean saveNow) throws Exception {
		if (a == null) {
			return null;
		}
		boolean overlap = a.isOverlap();
		if (a instanceof HuntLifeArticle) { // 猎命道具需要单独创建
			HuntLifeArticleEntity ae = initHuntlifeEntity((HuntLifeArticle) a, binded, reason, player, color, referenceCount, saveNow, overlap, false, level);
			return ae;
		} else if (a instanceof SoulPithArticle) {
			SoulPithArticleEntity ae = initSoulPithEntity((SoulPithArticle) a, binded, reason, player, color, referenceCount, saveNow, overlap, false, level);
			return ae;
		} else if (a instanceof PetSuitArticle) {
			PetSuitArticleEntity mw = initPetSuitArticleEntity((PetSuitArticle) a, binded, reason, player, color, referenceCount, saveNow, overlap, false);
			return mw;
		} else {
			return createEntity(a, binded, reason, player, color, referenceCount, saveNow);
		}
	}

	public ArticleEntity createTempEntity(Article a, boolean binded, int reason, Player player, int color, int referenceCount, int level, boolean saveNow) throws Exception {
		if (a == null) {
			return null;
		}
		boolean overlap = a.isOverlap();
		if (a instanceof HuntLifeArticle) { // 猎命道具需要单独创建
			HuntLifeArticleEntity ae = initHuntlifeEntity((HuntLifeArticle) a, binded, reason, player, color, referenceCount, saveNow, overlap, true, level);
			return ae;
		} else if (a instanceof SoulPithArticle) {
			SoulPithArticleEntity ae = initSoulPithEntity((SoulPithArticle) a, binded, reason, player, color, referenceCount, saveNow, overlap, true, level);
			return ae;
		} else if (a instanceof PetSuitArticle) {
			PetSuitArticleEntity pae = initPetSuitArticleEntity((PetSuitArticle)a, binded, reason, player, color, referenceCount, saveNow, overlap, true);
			return pae;
		} else {
			return createEntity(a, binded, reason, player, color, referenceCount, saveNow);
		}
	}

	private SoulPithArticleEntity initSoulPithEntity(SoulPithArticle a, boolean binded, int reason, Player player, int color, int referenceCount, boolean saveNow, boolean overlap, boolean istemp, int level) throws Exception {
		long id = nextId();
		SoulPithArticleEntity ae = new SoulPithArticleEntity(id);
		ae.setReferenceCount(referenceCount);
		ae.setOverlapFlag(overlap);
		ae.setArticleName(a.getName());
		ae.setShowName(a.getName());
		ae.setBinded(binded);
		ae.setDirty(true);
		if (color >= 1) {
			ae.setColorType(color);
		} else {
			ae.setColorType(1);
		}
		if (SoulPithEntityManager.getInst() != null) {
			SoulPithEntityManager.getInst().getArticleExtraData(ae, istemp);
		}
		if (!istemp) {
			mCache.put(ae.getId(), ae);
			em.notifyNewObject(ae);
		} else {
			aTempTable.put(ae.getId(), ae);
		}
		return ae;
	}

	private HuntLifeArticleEntity initHuntlifeEntity(HuntLifeArticle a, boolean binded, int reason, Player player, int color, int referenceCount, boolean saveNow, boolean overlap, boolean istemp, int level) throws Exception {
		long id = nextId();
		HuntLifeArticleEntity mw = new HuntLifeArticleEntity(id);
		if (!istemp) {
			mCache.put(mw.getId(), mw);
			em.notifyNewObject(mw);
		} else {
			aTempTable.put(mw.getId(), mw);
		}
		mw.setReferenceCount(referenceCount);
		mw.setOverlapFlag(overlap);
		mw.setArticleName(a.getName());
		mw.setShowName(a.getName());
		mw.setBinded(binded);
		mw.setDirty(true);
		mw.setBinded(true); // 兽魂全部绑定
		mw.setColorType(color);
		HuntLifeEntityManager.instance.createNewData(mw, level);
		return mw;
	}

	@Override
	/**
	 * referenceCount引用计数，此数据在创建的时候设置，当玩家每删除一个的时候，引用计数减少一，只有引用计数减少到0的时候，才可以真正的删除
	 * 从数据库删除物品规则
	 * 无时间不可堆叠的物品当且仅当引用计数为0的时候才进行删除
	 * 有时间不可堆叠的物品当且仅当引用计数为0的时候才进行删除
	 * 无时间可堆叠的物品不执行删除操作
	 * 有时间可堆叠的物品当且仅当引用计数为0的时候且过期后才进行删除
	 */
	public ArticleEntity createEntity(Article a, boolean binded, int reason, Player player, int color, int referenceCount, boolean saveNow) throws Exception {
		if (a == null) {
			return null;
		}

		if (referenceCount <= 0) {
			referenceCount = 1;
		}

		if (color < 0) {
			color = a.getColorType();
		}
		if (color < 0) {
			color = 0;
		}

		// 绿色服 银票特殊道具创建
		if (a.getName().equals(GreenServerManager.bindpropName)) {
			long id = nextId();
			YinPiaoEntity ye = new YinPiaoEntity(id);
			ye.setReferenceCount(referenceCount);
			ye.setOverlapFlag(a.isOverlap());
			ye.setArticleName(a.getName());
			ye.setShowName(a.getName());
			ye.setBinded(binded);
			ye.setColorType(color);
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				long lastingTime = a.getMaxValidDays() * 60000;
				// ae.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+lastingTime);
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
				timer.setEndTime(endTime);
				ye.setTimer(timer);
				ye.setEndTime(endTime);
			}
			ye.setDirty(true);

			if (saveNow) {
				mCache.put(id, ye);
				em.notifyNewObject(ye);
			} else {
				aTable.put(id, ye);
			}
			return ye;
		}

		// 判断是装备还是道具还是物品或者法宝
		if (a instanceof Equipment) {
			return createEntity((Equipment) a, binded, reason, player, color, referenceCount, saveNow);
		} else if (a instanceof Props) {
			return createEntity((Props) a, binded, reason, player, color, referenceCount, saveNow);
		} else if (a instanceof MagicWeapon) {
			return createEntity((MagicWeapon) a, binded, reason, player, color, referenceCount, saveNow);
		} else if (a instanceof HuntLifeArticle) {
			return createEntity((HuntLifeArticle) a, binded, reason, player, color, referenceCount, 1, saveNow);
		} else if (a instanceof SoulPithArticle) {
			return createEntity((SoulPithArticle) a, binded, reason, player, color, referenceCount, 1, saveNow);
		} else if (a instanceof PetSuitArticle) {
			return createEntity((PetSuitArticle)a, binded, reason, player, color, referenceCount, 1, saveNow);
		}
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		boolean overlap = a.isOverlap();
		if (overlap) {
			if (a.isHaveValidDays()) {
				long id = nextId();
				ArticleEntity ae = new ArticleEntity(id);
				ae.setReferenceCount(referenceCount);
				ae.setOverlapFlag(overlap);
				ae.setArticleName(a.getName());
				ae.setShowName(a.getName());
				ae.setBinded(binded);
				ae.setColorType(color);
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long inValidTime = now + a.getMaxValidDays() * 60000;
				timer.setEndTime(inValidTime);
				ae.setTimer(timer);
				ae.setEndTime(inValidTime);
				ae.setDirty(true);
				if (saveNow) {
					mCache.put(id, ae);
					em.notifyNewObject(ae);
				} else {
					aTable.put(id, ae);
				}
				log.info("[创建物品] [{}] [{}] [overlap] [createNew] [物品:{}/{}] [binded:{}] [{}] [角色:{}] [overlapEntityMap:{}] [mCache:{}/{}] [{}] [saveNow:{" + saveNow + "}] [referenceCount:" + referenceCount + "] [invalidTime:{}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ae.getId(), ae.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), overlapEntityMap.size(), mCache.getSize(), mCache.getNumElements(), CREATE_REASON_DESPS[reason], inValidTime, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
				return ae;
			} else {
				String articleName = a.getName();
				ArticleEntity ae = getEntity(articleName, binded, color);
				if (ae != null) {
					return ae;
				} else {
					long id = nextId();
					ae = new ArticleEntity(id);
					ae.setReferenceCount(referenceCount);
					ae.setOverlapFlag(overlap);
					ae.setArticleName(a.getName());
					ae.setShowName(a.getName());
					ae.setBinded(binded);
					ae.setColorType(color);
					ae.setDirty(true);
					// if(saveNow){
					mCache.put(id, ae);
					em.notifyNewObject(ae);
					// }else{
					// aTable.put(id, ae);
					// }
					overlapEntityMap.put(articleName + "#" + binded + "#" + color, ae);
					log.info("[创建物品] [{}] [{}] [overlap] [createNew] [物品:{}/{}] [binded:{}] [{}] [角色:{}] [overlapEntityMap:{}] [mCache:{}/{}] [{}] [saveNow:true] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ae.getId(), ae.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), overlapEntityMap.size(), mCache.getSize(), mCache.getNumElements(), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
					return ae;
				}
			}
		}

		long id = nextId();
		ArticleEntity ae = null;

		if (a instanceof ExchangeArticle) {
			ae = new ExchangeArticleEntity(id);
		} else if (a instanceof QiLingArticle) {
			ae = new QiLingArticleEntity(id);
			if (color == 0) {
				((QiLingArticleEntity) ae).setPropertyValue(1);
				((QiLingArticleEntity) ae).setTunshiValue(ArticleManager.魂值每个颜色基本值[color]);
			} else {
				try {
					// int value = ArticleManager.器灵各个颜色最大属性值[((QiLingArticle)a).getQilingType()][color - 1] + 1;
					int value = 1;
					((QiLingArticleEntity) ae).setPropertyValue(value);
					((QiLingArticleEntity) ae).setTunshiValue(ArticleManager.魂值每个颜色基本值[color]);
				} catch (Exception ex) {
					log.error("[创建物品] [异常] [id:" + id + "]", ex);
				}
			}
			int propertyValueMax = (int) (ArticleManager.器灵各个颜色最大属性值[((QiLingArticle) a).getQilingType()][color] * 0.5);
			Random ran = new Random();
			((QiLingArticleEntity) ae).setPropertyValue(1 + ran.nextInt(propertyValueMax));
		} else {
			ae = new ArticleEntity(id);
		}
		ae.setReferenceCount(referenceCount);
		ae.setOverlapFlag(overlap);
		ae.setArticleName(a.getName());
		ae.setShowName(a.getName());
		ae.setBinded(binded);
		ae.setColorType(color);
		if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
			long lastingTime = a.getMaxValidDays() * 60000;
			// ae.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+lastingTime);
			Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
			long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
			timer.setEndTime(endTime);
			ae.setTimer(timer);
			ae.setEndTime(endTime);
		}
		ArticleManager.getInstance().initBaoShiXiaZiColor(ae);
		ae.setDirty(true);
		if (saveNow) {
			mCache.put(id, ae);
			em.notifyNewObject(ae);
		} else {
			aTable.put(id, ae);
		}
		log.info("[创建物品] [{}] [{}] [normal] [createNew] [{}/{}] [角色:{}] [overlapEntityMap:{}] [mCache:{}/{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ae.getId(), ae.getArticleName(), (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), overlapEntityMap.size(), mCache.getSize(), mCache.getNumElements(), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

		return ae;
	}

	Calendar cal = Calendar.getInstance();

	/**
	 * 两个参数都是以分钟为单位，返回值为一个时间点
	 * @param timeInterval
	 * @param maxValidDays
	 * @return
	 */
	public long 得到有有效期的重叠物品的结束时间点(long timeInterval, long maxValidDays) {
		cal.setTimeInMillis(System.currentTimeMillis());
		if (timeInterval <= 0) {
			timeInterval = 5;
		}
		int minute = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int nowMinutes = hour * 60 + minute;
		int 分段开始位置 = (int) (nowMinutes / timeInterval) + 1;
		long inValidMinutes = (分段开始位置 * timeInterval + maxValidDays) * 60 * 1000;
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long mill = cal.getTimeInMillis();
		return mill + inValidMinutes;
	}

	// public static void main(String[] args) {
	// DefaultArticleEntityManager aem = new DefaultArticleEntityManager();
	// System.out.println(System.currentTimeMillis());
	// System.out.println(aem.得到有有效期的重叠物品的结束时间点(1,1));
	// }
	public NewMagicWeaponEntity createEntity(MagicWeapon a, boolean binded, int reason, Player player, int color, int referenceCount, boolean saveNow) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		boolean overlap = a.isOverlap();
		if (overlap) { // 可以堆叠
			String articleName = a.getName();
			ArticleEntity ae = getEntity(articleName, binded, color);
			if (ae != null) {
				if (ae instanceof NewMagicWeaponEntity) {
					return (NewMagicWeaponEntity) ae;
				} else {
					ArticleManager.logger.error("[法宝异常] [物品:{}] [要转成:{}] [物品Class:{}]", new Object[] { a.getName(), NewMagicWeaponEntity.class, ae.getClass() }, new Exception());
					return null;
				}
			} else {
				NewMagicWeaponEntity mw = initMagicWeapon(a, binded, reason, player, color, referenceCount, saveNow, overlap, false);
				overlapEntityMap.put(articleName + "#" + binded + "#" + color, mw);
				log.info("[创建法宝] [{}] [{}] [overlap] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:true] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), mw.getId(), mw.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
				return mw;
			}
		}
		NewMagicWeaponEntity mw = initMagicWeapon(a, binded, reason, player, color, referenceCount, saveNow, overlap, false);
		log.info("[创建法宝] [{}] [{}] [overlap] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:true] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), mw.getId(), mw.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
		return mw;
	}
	
	private PetSuitArticleEntity initPetSuitArticleEntity(PetSuitArticle a, boolean binded, int reason, Player player, int color, int referenceCount, boolean saveNow, boolean overlap, boolean istemp) throws Exception{
		long id = nextId();
		PetSuitArticleEntity mw = new PetSuitArticleEntity();
		mw.setId(id);
		if (!istemp) {
			mCache.put(mw.getId(), mw);
			em.notifyNewObject(mw);
		}
		mw.setReferenceCount(referenceCount);
		mw.setOverlapFlag(overlap);
		mw.setArticleName(a.getName());
		mw.setShowName(a.getName());
		mw.setBinded(binded); 
		mw.setDirty(true);
		mw.setColorType(color);
		return mw;
	}

	private NewMagicWeaponEntity initMagicWeapon(MagicWeapon a, boolean binded, int reason, Player player, int color, int referenceCount, boolean saveNow, boolean overlap, boolean istemp) throws Exception {
		long id = nextId();
		NewMagicWeaponEntity mw = new NewMagicWeaponEntity(id);
		if (!istemp) {
			mCache.put(mw.getId(), mw);
			em.notifyNewObject(mw);
		}
		mw.setReferenceCount(referenceCount);
		mw.setOverlapFlag(overlap);
		mw.setArticleName(a.getName());
		mw.setShowName(a.getName());
		mw.setBinded(a.isObtainBind()); // 应要求。法宝肯定不是绑定的 //2014年10月9日  增加可绑定的法宝，表里单独一列
		mw.setMcolorlevel(1);
		mw.setMagicWeaponExp(0L);
		mw.setIllusionLevel(0);
		mw.setMdurability(a.getNaijiudu());
		mw.setDirty(true);
		mw.setColorType(color);

		MagicWeaponManager inst = MagicWeaponManager.instance;
		if (inst != null) {
			MagicWeaponBaseModel mbm = inst.mwBaseModel.get(color);

			int additiveNum = 0;
			if (player != null) {
				additiveNum = player.random.nextInt((mbm.getMaxAdditiveAttr() - mbm.getMinAdditiveAttr()) + 1) + mbm.getMinAdditiveAttr();
			} else {
				additiveNum = mbm.getMaxAdditiveAttr();
			}
			List<MagicWeaponAttrModel> additiveAttr = new ArrayList<MagicWeaponAttrModel>();
			if (MagicWeaponManager.logger.isDebugEnabled()) {
				MagicWeaponManager.logger.debug("[创建法宝] [根据概率随机获得附加技能] [" + (player != null ? player.getLogString() : "null") + "] [附加技能个数" + additiveNum + "]");
			}
			int tempId = 0;
			int ranNum = 0;
			for (int j = 0; j < mbm.getAdditiveSkillList().size(); j++) {
				ranNum += mbm.getAdditiveSkillList().get(j).getProbbly();
			}
			for (int i = 0; i < additiveNum; i++) {
				int randomNum = 0;
				if (player != null) {
					randomNum = player.random.nextInt(ranNum);
				} else {
					randomNum = new Random().nextInt(ranNum);
				}
				int oldProbabbly = 0;
				for (int j = 0; j < mbm.getAdditiveSkillList().size(); j++) {
					oldProbabbly += mbm.getAdditiveSkillList().get(j).getProbbly();
					if (oldProbabbly >= randomNum) {
						tempId = mbm.getAdditiveSkillList().get(j).getSkillId();
						AdditiveAttrModel tempSkModel = inst.mwAxtraAttr.get(tempId);
						if (tempSkModel == null) {
							MagicWeaponManager.logger.error("[创建法宝] [ 异常] [附加技能id:" + tempId + "] [mbm:" + mbm + "] [" + a.getName() + "] [" + color + "]");
						}
						int[] result = tempSkModel.getAttrNumByType(tempSkModel.getBaseAttrNumByIndex());
						MagicWeaponAttrModel temp = new MagicWeaponAttrModel(tempId, result[1], result[0], tempSkModel.getDescreption());
						additiveAttr.add(temp);
						mw.setAdditiveAttr(additiveAttr);
						if (MagicWeaponManager.logger.isDebugEnabled() && player != null) {
							MagicWeaponManager.logger.debug("[创建法宝] [根据概率随机获得附加技能] [" + (player != null ? player.getLogString() : "null") + "] [随机数" + randomNum + "获得技能id:" + tempId + "]");
						}
						break;
					}
				}
			}
			if (MagicWeaponManager.logger.isDebugEnabled()) {
				MagicWeaponManager.logger.debug("[创建法宝] [成功 ] [" + (player != null ? player.getLogString() : "null") + "] [" + mw + "]");
			}
			return mw;
		} else {
			MagicWeaponManager.logger.error("[创建法宝] [ 失败 ] [法宝manager未初始化成功！！！！]");
			return null;
		}
	}

	public EquipmentEntity createEntity(Equipment a, boolean binded, int reason, Player player, int color, int referenceCount, boolean saveNow) throws Exception {
		if (a.getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_ChiBang && forceToChiBangBind) {
			// 2013年9月21日15:42:57 康建虎 按刘洋的要求，获得翅膀一定是绑定的。
			boolean pre = binded;
			binded = forceToChiBangBind;
			log.info("DefaultArticleEntityManager.createEntity: 强制翅膀绑定，从{} 到 {}", pre, binded);
		}
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		boolean overlap = a.isOverlap();
		// ArticleManager am = ArticleManager.getInstance();
		if (overlap) {
			String articleName = a.getName();
			ArticleEntity ae = getEntity(articleName, binded, color);
			if (ae != null) {
				if (ae instanceof EquipmentEntity) {
					return (EquipmentEntity) ae;
				} else {
					ArticleManager.logger.error("[装备转换异常] [物品:{}] [要转成:{}] [物品Class:{}]", new Object[] { a.getName(), EquipmentEntity.class, ae.getClass() }, new Exception());
					return null;
				}
			} else {
				long id = nextId();
				// 判断有无此idarticle
				EquipmentEntity ee = new EquipmentEntity(id);
				ee.setReferenceCount(referenceCount);
				ee.setOverlapFlag(overlap);
				ee.setArticleName(a.getName());
				ee.setShowName(a.getName());
				ee.setBinded(binded);
				ee.setLevel(0);

				ee.setDurability(a.getDurability());

				try {
					if (a.getSpecial() != SpecialEquipmentManager.普通) {
						setEquipmentEntity(ee, a, EquipmentEntity.满星, color);
						ee.setEndowments(EquipmentEntity.满资质);
						ee.setInscriptionFlag(true);
					} else {
						setEquipmentEntity(ee, a, 0, color);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				ae = ee;
				if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
					long lastingTime = a.getMaxValidDays() * 60000;
					// ae.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+lastingTime);
					Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
					long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
					timer.setEndTime(endTime);
					ae.setTimer(timer);
					ae.setEndTime(endTime);

				}
				ae.setDirty(true);
				// if(saveNow){
				mCache.put(id, ae);
				em.notifyNewObject(ae);
				// }else{
				// aTable.put(id, ae);
				// }
				overlapEntityMap.put(articleName + "#" + binded + "#" + color, ae);
				log.info("[创建装备] [{}] [{}] [overlap] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:true] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ae.getId(), ae.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
				return (EquipmentEntity) ae;
			}
		}

		long id = nextId();
		EquipmentEntity ee = null;
		if (a.getSpecial() == SpecialEquipmentManager.鸿天帝宝) {
			ee = new Special_1EquipmentEntity();
			ee.setId(id);
		} else if (a.getSpecial() == SpecialEquipmentManager.伏天古宝) {
			ee = new Special_2EquipmentEntity();
			ee.setId(id);
		} else {
			ee = new EquipmentEntity(id);
		}
		ee.setReferenceCount(referenceCount);
		ee.setOverlapFlag(overlap);
		ee.setArticleName(a.getName());
		ee.setShowName(a.getName());
		ee.setBinded(binded);
		ee.setLevel(0);

		ee.setDurability(a.getDurability());

		try {
			if (a.getSpecial() != SpecialEquipmentManager.普通) {
				setEquipmentEntity(ee, a, EquipmentEntity.满星, color);
				ee.setEndowments(EquipmentEntity.满资质);
				ee.setInscriptionFlag(true);
			} else {
				setEquipmentEntity(ee, a, 0, color);
			}
			// setEquipmentEntity(ee, a, 0, color);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
			long lastingTime = a.getMaxValidDays() * 60000;
			// ee.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+lastingTime);
			Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
			long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
			timer.setEndTime(endTime);
			ee.setTimer(timer);
			ee.setEndTime(endTime);
		}
		ee.setDirty(true);

		if (saveNow) {
			mCache.put(id, ee);
			em.notifyNewObject(ee);
		} else {
			aTable.put(id, ee);
		}
		log.info("[创建装备] [{}] [{}] [normal] [createNew] [{}] [{}/{}] [{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ee.getClass().getName(), ee.getId(), ee.getArticleName(), ee.getShowName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

		return ee;
	}

	public PropsEntity createEntity(Props a, boolean binded, int reason, Player player, int color, int referenceCount, boolean saveNow) throws Exception {
		// TODO Auto-generated method stub
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (a instanceof MagicWeaponProps) {
			long id = nextId();
			MagicWeaponEntity ee = new MagicWeaponEntity(id);
			ee.setReferenceCount(referenceCount);
			ee.setOverlapFlag(false);
			ee.setArticleName(a.getName());
			ee.setShowName(a.getName());
			ee.setBinded(binded);
			ee.setLeftUsingTimes(a.getMaxUsingTimes());
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				long lastingTime = a.getMaxValidDays() * 60000;
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
				timer.setEndTime(endTime);
				ee.setTimer(timer);
				ee.setEndTime(endTime);
			}
			return null;
		} else if (a instanceof PetProps) {
			long id = nextId();
			PetPropsEntity ee = new PetPropsEntity(id);
			ee.setReferenceCount(referenceCount);
			ee.setOverlapFlag(false);
			ee.setArticleName(a.getName());
			ee.setShowName(a.getName());
			ee.setBinded(binded);
			ee.setColorType(color);
			ee.setLeftUsingTimes(a.getMaxUsingTimes());
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				long lastingTime = a.getMaxValidDays() * 60000;
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
				timer.setEndTime(endTime);
				ee.setTimer(timer);
				ee.setEndTime(endTime);
			}
			if (saveNow) {
				mCache.put(id, ee);
				em.notifyNewObject(ee);
			} else {
				aTable.put(id, ee);
			}
			log.info("[创建宠物道具] [{}] [{}] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ee.getId(), ee.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

			return ee;
		} else if (a instanceof PetEggProps) {
			long id = nextId();
			PetEggPropsEntity ee = new PetEggPropsEntity(id);
			ee.setReferenceCount(referenceCount);
			ee.setOverlapFlag(false);
			ee.setArticleName(a.getName());
			ee.setShowName(a.getName());
			ee.setBinded(binded);
			ee.setColorType(color);
			ee.setLeftUsingTimes(a.getMaxUsingTimes());
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				long lastingTime = a.getMaxValidDays() * 60000;
				// ee.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+lastingTime);
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
				timer.setEndTime(endTime);
				ee.setTimer(timer);
				ee.setEndTime(endTime);
			}
			if (saveNow) {
				mCache.put(id, ee);
				em.notifyNewObject(ee);
			} else {
				aTable.put(id, ee);
			}
			log.info("[创建宠物蛋道具] [{}] [{}] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ee.getId(), ee.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

			return ee;

		} else if (a instanceof FateActivityProps) {
			long id = nextId();
			FateActivityPropsEntity fpe = new FateActivityPropsEntity(id);
			fpe.setReferenceCount(referenceCount);
			fpe.setOverlapFlag(false);
			fpe.setArticleName(a.getName());
			fpe.setShowName(a.getName());
			fpe.setBinded(binded);
			fpe.setColorType(color);
			fpe.setLeftUsingTimes(a.getMaxUsingTimes());
			fpe.setActivityType(((FateActivityProps) a).getType());
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				long lastingTime = a.getMaxValidDays() * 60000;
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
				timer.setEndTime(endTime);
				fpe.setTimer(timer);
				fpe.setEndTime(endTime);
			}

			if (saveNow) {
				mCache.put(id, fpe);
				em.notifyNewObject(fpe);
			} else {
				aTable.put(id, fpe);
			}

			log.info("[创建道具] [{}] [{}] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), fpe.getId(), fpe.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

			return fpe;

		} else if (a instanceof BottleProps) {
			long id = nextId();
			BottlePropsEntity ee = new BottlePropsEntity(id);
			ee.setReferenceCount(referenceCount);
			ee.setOverlapFlag(false);
			ee.setArticleName(a.getName());
			ee.setShowName(a.getName());
			ee.setBinded(binded);
			ee.setColorType(color);
			ee.setLeftUsingTimes(a.getMaxUsingTimes());
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				long lastingTime = a.getMaxValidDays() * 60000;
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
				timer.setEndTime(endTime);
				ee.setTimer(timer);
				ee.setEndTime(endTime);
			}

			// 特殊操作，根据概率算出宝瓶中能开出的物品，以及宝瓶中剩余的物品
			BottleProps bp = (BottleProps) a;
			int playerLevel = 0;
			if (player != null) {
				playerLevel = player.getLevel();
			}
			List<ArticleProperty> apList = new ArrayList<ArticleProperty>();
			ArticleProperty[] aps = bp.根据级别得到获得物品概率数组(playerLevel);
			if (aps != null) {
				for (ArticleProperty ap : aps) {
					if (ap != null) {
						apList.add(ap);
					}
				}
			}
			ArrayList<ArticleProperty> aList = new ArrayList<ArticleProperty>();
			ArrayList<ArticleProperty> allList = new ArrayList<ArticleProperty>();

			int count = BottleProps.COLOR_OPEN_COUNT[color][random.nextInt(BottleProps.COLOR_OPEN_COUNT[color].length)];

			// 随机打乱集合
			synchronized (apList) {
				Collections.shuffle(apList);

				double[] probabilitys = new double[apList.size()];
				double countValue = 0;
				for (int i = 0; i < apList.size(); i++) {
					if (apList.get(i) != null) {
						countValue += apList.get(i).getProb();
					}
				}
				for (int i = 0; i < apList.size(); i++) {
					if (apList.get(i) != null) {
						probabilitys[i] = apList.get(i).getProb() * 1.0 / countValue;
					}
				}
				Random r = null;
				if (player != null) {
					r = player.random;
				} else {
					r = random;
				}
				for (int i = 0; i < count; i++) {
					int index = ProbabilityUtils.randomProbability(r, probabilitys);
					aList.add(apList.get(index));
					allList.add(apList.get(index));
				}
				for (int i = count; i < BottleProps.DIS_BOTTLE_COUNT && i < apList.size(); i++) {
					allList.add(apList.get(i));
				}
			}
			ee.setAllArticles(allList.toArray(new ArticleProperty[0]));
			ee.setOpenedArticles(aList.toArray(new ArticleProperty[0]));
			ee.setCanOpenCount(count);
			if (saveNow) {
				mCache.put(id, ee);
				em.notifyNewObject(ee);
			} else {
				aTable.put(id, ee);
			}

			log.info("[创建道具] [{}] [{}] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ee.getId(), ee.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

			return ee;
		} else if (a instanceof ExploreResourceMap) {
			long id = nextId();
			ExploreResourceMapEntity ee = new ExploreResourceMapEntity(id);
			ee.setReferenceCount(referenceCount);
			ee.setOverlapFlag(false);
			ee.setArticleName(a.getName());
			ee.setShowName(a.getName());
			ee.setBinded(binded);
			ee.setColorType(color);
			ee.setLeftUsingTimes(a.getMaxUsingTimes());
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				long lastingTime = a.getMaxValidDays() * 60000;
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
				timer.setEndTime(endTime);
				ee.setTimer(timer);
				ee.setEndTime(endTime);
			}
			if (saveNow) {
				mCache.put(id, ee);
				em.notifyNewObject(ee);
			} else {
				aTable.put(id, ee);
			}

			log.info("[创建道具 藏宝图] [{}] [{}] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ee.getId(), ee.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

			return ee;

		} else if (a instanceof DigProps) {
			long id = nextId();
			DigPropsEntity de = new DigPropsEntity(id);
			de.setReferenceCount(referenceCount);
			de.setOverlapFlag(false);
			de.setArticleName(a.getName());
			de.setShowName(a.getName());
			de.setBinded(binded);
			de.setColorType(color);
			de.setLeftUsingTimes(a.getMaxUsingTimes());
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				long lastingTime = a.getMaxValidDays() * 60000;
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
				timer.setEndTime(endTime);
				de.setTimer(timer);
				de.setEndTime(endTime);
			}
			if (saveNow) {
				mCache.put(id, de);
				em.notifyNewObject(de);
			} else {
				aTable.put(id, de);
			}

			log.info("[创建道具 挖宝图] [{}] [{}] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), de.getId(), de.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

			return de;

		} else if (a instanceof HunshiProps) {
			log.info("魂石道具创建1");
			long id = nextId();
			HunshiEntity he = new HunshiEntity(id);
			he.setReferenceCount(referenceCount);
			he.setOverlapFlag(false);
			he.setArticleName(a.getName());
			he.setShowName(a.getName());
			he.setBinded(binded);
			he.setColorType(color);
			he.setMainPropValue(new int[25]);
			he.setExtraPropValue(new int[25]);
			he.setRongHeZhi(0);
			he.setJianding(false);
			he.setType(((HunshiProps) a).getType());
			he.setHoleIndex(((HunshiProps) a).getIndex());
			he.setLeftUsingTimes(a.getMaxUsingTimes());
			he.saveData("id");
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				long lastingTime = a.getMaxValidDays() * 60000;
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
				timer.setEndTime(endTime);
				he.setTimer(timer);
				he.setEndTime(endTime);
			}
			if (saveNow) {
				mCache.put(id, he);
				em.notifyNewObject(he);
			} else {
				aTable.put(id, he);
			}

			log.info("[创建道具 魂石] [{}] [{}] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), he.getId(), he.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

			return he;

		} else {

			if (a instanceof SingleForPetProps) {
				SingleForPetProps props = (SingleForPetProps) a;
				if (!props.isOverlap()) {
					// 由宠物房里生成的经验丹
					long id = nextId();
					SingleForPetPropsEntity entity = new SingleForPetPropsEntity(id);
					entity.setReferenceCount(referenceCount);
					entity.setOverlapFlag(props.isOverlap());
					entity.setArticleName(a.getName());
					entity.setShowName(a.getName());
					entity.setBinded(binded);
					entity.setColorType(color);
					entity.setLeftUsingTimes(a.getMaxUsingTimes());
					if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
						long lastingTime = a.getMaxValidDays() * 60000;
						Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
						long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastingTime;
						timer.setEndTime(endTime);
						entity.setTimer(timer);
						entity.setEndTime(endTime);
					}
					if (saveNow) {
						mCache.put(id, entity);
						em.notifyNewObject(entity);
					} else {
						aTable.put(id, entity);
					}
					log.info("[创建宠物房得到宠物经验丹道具] [{}] [{}] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), entity.getId(), entity.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
					return entity;
				}
			}

			boolean overlap = a.isOverlap();
			if (overlap) {
				if (a.isHaveValidDays()) {
					long id = nextId();
					PropsEntity ae = new PropsEntity(id);
					ae.setReferenceCount(referenceCount);
					ae.setOverlapFlag(overlap);
					ae.setArticleName(a.getName());
					ae.setShowName(a.getName());
					ae.setBinded(binded);
					ae.setColorType(color);
					ae.setLeftUsingTimes(a.getMaxUsingTimes());
					Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
					long inValidTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + a.getMaxValidDays() * 60000;
					timer.setEndTime(inValidTime);
					ae.setTimer(timer);
					ae.setEndTime(inValidTime);
					ae.setDirty(true);
					if (saveNow) {
						mCache.put(id, ae);
						em.notifyNewObject(ae);
					} else {
						aTable.put(id, ae);
					}
					log.info("[创建道具] [{}] [{}] [overlap] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [inValidTime:{}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ae.getId(), ae.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], inValidTime, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

					return ae;
				} else {
					String articleName = a.getName();
					ArticleEntity ae = getEntity(articleName, binded, color);
					if (ae != null) {
						log.info("[创建道具] [{}] [{}] [overlap] [useExists][{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ae.getId(), ae.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
						try {
							return (PropsEntity) ae;
						} catch (Exception ex) {
							log.warn("[创建道具] [异常] [overlap] [{}] [{}] [{}] [改用重新创建新的道具实体进行修正]", new Object[] { ae.getId(), ae.getArticleName(), ae.getClass() }, ex);
						}

					}
					long id = nextId();
					PropsEntity ee = new PropsEntity(id);
					ee.setReferenceCount(referenceCount);
					ee.setOverlapFlag(a.isOverlap());
					ee.setArticleName(a.getName());
					ee.setShowName(a.getName());
					ee.setBinded(binded);
					ee.setColorType(color);
					ee.setLeftUsingTimes(a.getMaxUsingTimes());
					ae = ee;
					ae.setDirty(true);
					// if(saveNow){
					mCache.put(id, ae);
					em.notifyNewObject(ae);
					// }else{
					// aTable.put(id, ae);
					// }
					overlapEntityMap.put(articleName + "#" + binded + "#" + color, ae);
					log.info("[创建道具] [{}] [{}] [overlap] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:true] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ae.getId(), ae.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

					return (PropsEntity) ae;
				}
			}

			long id = nextId();

			PropsEntity ee = new PropsEntity(id);
			ee.setReferenceCount(referenceCount);
			ee.setOverlapFlag(a.isOverlap());
			ee.setArticleName(a.getName());
			ee.setShowName(a.getName());
			ee.setBinded(binded);
			ee.setColorType(color);
			ee.setLeftUsingTimes(a.getMaxUsingTimes());
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + a.getMaxValidDays() * 60000;
				timer.setEndTime(endTime);
				ee.setTimer(timer);
				ee.setEndTime(endTime);
			}
			ee.setDirty(true);

			if (saveNow) {
				mCache.put(id, ee);
				em.notifyNewObject(ee);
			} else {
				aTable.put(id, ee);
			}

			log.info("[创建道具] [{}] [{}] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:{" + saveNow + "}] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), ee.getId(), ee.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

			return ee;
		}
	}

	@Override
	public ArticleEntity createTempEntity(Article a, boolean binded, int reason, Player player, int color) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		if (color < 0) {
			color = a.getColorType();
		}
		if (color < 0) {
			color = 0;
		}
		// 判断是装备还是道具还是物品
		if (a instanceof Equipment) {
			return createTempEntity((Equipment) a, binded, reason, player, 0, color);
		} else if (a instanceof Props) {
			return createTempEntity((Props) a, binded, reason, player, color);
		} else if (a instanceof MagicWeapon) {
			return createTempEntity((MagicWeapon) a, binded, reason, player, color, 1, false);
		} else if (a instanceof HuntLifeArticle) {
			return createTempEntity((HuntLifeArticle) a, binded, reason, player, color, 1, 1, false);
		} else if (a instanceof SoulPithArticle) {
			return createTempEntity((SoulPithArticle) a, binded, reason, player, color, 1, 1, false);
		}

		boolean overlap = a.isOverlap();
		if (overlap) {
			String articleName = a.getName();
			ArticleEntity ae = getTempEntity(articleName, binded, color);
			if (ae != null) {
				return ae;
			} else {
				// 如果可堆叠的物品并且不存在，那么直接创建一个并保存起来
				long id = nextId();
				ae = new ArticleEntity(id);
				ae.setArticleName(a.getName());
				ae.setColorType(color);
				ae.setShowName(a.getName());
				ae.setBinded(binded);
				ae.setTempEntityFlag(true);
				aTempTable.put(id, ae);
				overlapTempEntityMap.put(articleName + "#" + binded + "#" + color, ae);
				log.info("[创建临时物品] [overlap:create_concrete] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [{}ms]", new Object[] { ae.getId(), ae.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

				return ae;
			}
		}

		long id = nextId();

		ArticleEntity ae = null;

		ae = new ArticleEntity(id);

		ae.setColorType(color);
		ae.setArticleName(a.getName());
		ae.setShowName(a.getName());
		ae.setBinded(binded);
		ae.setTempEntityFlag(true);
		aTempTable.put(id, ae);
		log.info("[创建临时物品] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [{}ms]", new Object[] { ae.getId(), ae.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
		return ae;
	}

	public NewMagicWeaponEntity createTempEntity(MagicWeapon a, boolean binded, int reason, Player player, int color, int referenceCount, boolean saveNow) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		boolean overlap = a.isOverlap();
		if (overlap) { // 可以堆叠
			String articleName = a.getName();
			ArticleEntity ae = getEntity(articleName, binded, color);
			if (ae != null) {
				if (ae instanceof NewMagicWeaponEntity) {
					return (NewMagicWeaponEntity) ae;
				} else {
					ArticleManager.logger.error("[法宝异常] [物品:{}] [要转成:{}] [物品Class:{}]", new Object[] { a.getName(), NewMagicWeaponEntity.class, ae.getClass() }, new Exception());
					return null;
				}
			} else {
				NewMagicWeaponEntity mw = initMagicWeapon(a, binded, reason, player, color, referenceCount, saveNow, overlap, true);
				aTempTable.put(mw.getId(), mw);
				overlapTempEntityMap.put(articleName + "#" + binded + "#" + color, ae);
				log.info("[创建法宝] [{}] [{}] [overlap] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:true] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), mw.getId(), mw.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
				return mw;
			}
		}
		NewMagicWeaponEntity mw = initMagicWeapon(a, binded, reason, player, color, referenceCount, saveNow, overlap, true);
		aTempTable.put(mw.getId(), mw);
		log.info("[创建临时法宝法宝] [{}] [{}] [overlap] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [saveNow:true] [{}ms]", new Object[] { Thread.currentThread().getName(), a.getClass().getSimpleName(), mw.getId(), mw.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
		return mw;
	}

	public EquipmentEntity createTempEntity(Equipment a, boolean binded, int reason, Player player, int star, int color) throws Exception {
		// TODO Auto-generated method stub

		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		boolean overlap = a.isOverlap();
		if (overlap) {
			String articleName = a.getName();
			ArticleEntity ae = getTempEntity(articleName, binded, color);
			if (ae != null) {
				if (ae instanceof EquipmentEntity) {
					return (EquipmentEntity) ae;
				} else {
					ArticleManager.logger.error("[装备转换异常][物品:{}][要转成:{}][物品Class:{}]", new Object[] { a.getName(), EquipmentEntity.class, ae.getClass() }, new Exception());
					return null;
				}
			} else {
				// 如果可堆叠的物品并且不存在，那么直接创建一个并保存起来
				long id = nextId();
				EquipmentEntity ee = new EquipmentEntity(id);
				ee.setColorType(color);
				ee.setArticleName(a.getName());
				ee.setShowName(a.getName());
				ee.setBinded(binded);
				ee.setLevel(0);
				ee.setDurability(a.getDurability());
				ee.setTempEntityFlag(true);
				try {
					setEquipmentEntity(ee, a, star, color);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ae = ee;
				aTempTable.put(id, ae);
				overlapTempEntityMap.put(articleName + "#" + binded + "#" + color, ae);
				log.info("[创建临时装备] [overlap:create_concrete] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [{}ms]", new Object[] { ae.getId(), ae.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

				return (EquipmentEntity) ae;
			}
		}

		long id = nextId();

		EquipmentEntity ee = new EquipmentEntity(id);
		ee.setColorType(color);
		ee.setArticleName(a.getName());
		ee.setShowName(a.getName());
		ee.setBinded(binded);
		ee.setLevel(0);
		ee.setDurability(a.getDurability());
		ee.setTempEntityFlag(true);
		try {
			setEquipmentEntity(ee, a, star, color);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ee.setDirty(true);

		aTempTable.put(id, ee);

		log.info("[创建临时装备] [normal] [createNew] [{}] [{}] [binded:{}] [{}] [角色:{}] [{}] [{}ms]", new Object[] { ee.getId(), ee.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

		return ee;
	}

	public PropsEntity createTempEntity(Props a, boolean binded, int reason, Player player, int color) throws Exception {
		// TODO Auto-generated method stub
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		if (a instanceof MagicWeaponProps) {
			long id = nextId();
			MagicWeaponEntity ee = new MagicWeaponEntity(id);
			ee.setColorType(color);
			ee.setArticleName(a.getName());
			ee.setShowName(a.getName());
			ee.setBinded(binded);
			ee.setLeftUsingTimes(a.getMaxUsingTimes());
			ee.setTempEntityFlag(true);
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				// long lastingTime = a.getMaxValidDays()*60000;
				// ee.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+lastingTime);
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + a.getMaxValidDays() * 60000;
				timer.setEndTime(endTime);
				ee.setTimer(timer);
				ee.setEndTime(endTime);
			}
			return null;
		}
		if (a instanceof HunshiProps) {
			log.info("魂石道具创建2");
			long id = nextId();
			HunshiEntity he = new HunshiEntity(id);
			he.saveData("id");
			he.setArticleName(a.getName());
			he.setShowName(a.getName());
			he.setBinded(binded);
			he.setColorType(color);
			he.setJianding(false);
			he.setType(((HunshiProps) a).getType());
			he.setHoleIndex(((HunshiProps) a).getIndex());
			he.setLeftUsingTimes(a.getMaxUsingTimes());
			if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
				// long lastingTime = a.getMaxValidDays()*60000;
				// ee.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+lastingTime);
				Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
				long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + a.getMaxValidDays() * 60000;
				timer.setEndTime(endTime);
				he.setTimer(timer);
				he.setEndTime(endTime);
			}
			aTempTable.put(id, he);
			log.info("[创建临时道具] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [{}ms]", new Object[] { he.getId(), he.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
			return he;
		} else {

			boolean overlap = a.isOverlap();
			if (overlap) {
				String articleName = a.getName();
				ArticleEntity ae = getTempEntity(articleName, binded, color);
				if (ae != null) {
					if (ae instanceof PropsEntity) {
						return (PropsEntity) ae;
					} else {
						ArticleManager.logger.error("[道具转换异常][物品:{}][要转换成:{}][物品Class:{}]", new Object[] { a.getName(), PropsEntity.class, ae.getClass() }, new Exception());
						return null;
					}
				} else {
					long id = nextId();
					PropsEntity ee = new PropsEntity(id);
					ee.setColorType(color);
					ee.setArticleName(a.getName());
					ee.setShowName(a.getName());
					ee.setBinded(binded);
					ee.setLeftUsingTimes(a.getMaxUsingTimes());
					if (a instanceof WingProps) {
						if (a.isHaveValidDays() && a.getMaxValidDays() != 0 && a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE) {
							// long lastingTime = a.getMaxValidDays()*60000;
							// ee.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+lastingTime);
							Timer timer = SystemMaintainManager.createSystemMaintainByType(a.getTimeType());
							long endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + a.getMaxValidDays() * 60000;
							timer.setEndTime(endTime);
							ee.setTimer(timer);
							ee.setEndTime(endTime);
						}
					}
					ae = ee;
					ae.setTempEntityFlag(true);
					ae.setDirty(true);

					aTempTable.put(id, ae);
					overlapTempEntityMap.put(articleName + "#" + binded + "#" + color, ae);
					log.info("[创建临时道具] [overlap:create_concrete] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [{}ms]", new Object[] { ae.getId(), ae.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

					return (PropsEntity) ae;
				}
			}

			long id = nextId();

			PropsEntity ee = new PropsEntity(id);
			ee.setArticleName(a.getName());
			ee.setShowName(a.getName());
			ee.setBinded(binded);
			ee.setLeftUsingTimes(a.getMaxUsingTimes());
			ee.setColorType(color);
			ee.setTempEntityFlag(true);
			aTempTable.put(id, ee);
			log.info("[创建临时道具] [normal] [createNew] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [{}ms]", new Object[] { ee.getId(), ee.getArticleName(), binded, color, (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), CREATE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

			return ee;
		}
	}

	public void recycleEntity(ArticleEntity a, int reason, Player player) {
		if (a == null) {
			return;
		}
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleManager am = ArticleManager.getInstance();
		if (am != null) {
			Article article = am.getArticle(a.getArticleName());
			if (article != null && !article.isOverlap()) {
				a.setAbandoned(true);
			}
		}
		log.info("[回收物品] [{}/{}] [binded:{}] [{}] [角色:{}] [{}] [{}ms]", new Object[] { a.getId(), a.getArticleName(), a.isBinded(), a.getColorType(), (player != null ? player.getId() + "/" + player.getName() + "/" + player.getUsername() : ""), DELETE_REASON_DESPS[reason], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });

	}

	private ArticleEntity NULL_ARTICLE = new ArticleEntity();

	@Override
	public ArticleEntity getEntity(long id) {
		// TODO Auto-generated method stub
		if (id <= 0) {
			return null;
		}
		ArticleEntity ae = (ArticleEntity) mCache.get(id);
		if (ae == null) {
			ae = aTable.get(id);
		}
		if (ae == null) {
			ae = aTempTable.get(id);
		}
		if (ae == null) {
			try {
				ae = em.find(id);
				if (ae == null) {
					ArticleManager.logger.warn("articleerror" + id);
					log.warn("[获取物品] [失败] [id:" + id + "]");
					if (TestServerConfigManager.isTestServer()) {
						log.warn("[获取物品] [失败] [id:" + id + "]", new Exception("查询道具错误"));
					}
					return null;
				}
				if (ae instanceof QiLingArticleEntity) {
					QiLingArticleEntity ql = (QiLingArticleEntity) ae;
					if (ql.getTunshiValue() == 0) {
						ql.setTunshiValue(ArticleManager.魂值每个颜色基本值[ql.getColorType()]);
					}
				}
				if (ae instanceof NewMagicWeaponEntity) {			
					if (!ae.isBinded() && "5周年纪念法宝".equals(ae.getArticleName())) {		//2017年6月20日10:16:43  调整此物品为绑定
						ae.setBinded(true);
					}
				}
				mCache.put(id, ae);
				// 刷新附魔属性
				flushEquipmentFuMo(ae);
				flushEnchant(ae);
				Article article = ArticleManager.getInstance().getArticle(ae.getArticleName());
				if (article != null && article.isOverlap() != ae.isOverlapFlag()) {
					ae.setOverlapFlag(article.isOverlap());
					em.notifyFieldChange(ae, "overlapFlag");
				}

				log.warn("[获取物品] [成功] [id:{}] [{}] [颜色:{}]", new Object[] { id, ae.getArticleName(), ae.getColorType() });
			} catch (ArticleEntityNotFoundException e) {
				log.warn("[获取物品] [失败] [id:" + id + "]", e);
			} catch (Exception e) {
				log.warn("[获取物品] [失败] [id:" + id + "]", e);
				e.printStackTrace();
			}
		}
		if (ae == NULL_ARTICLE) return null;
		if (ae != null && ae instanceof NewMagicWeaponEntity) {
			((NewMagicWeaponEntity) ae).initAttrNum();
		}
		return ae;
	}

	@Override
	public ArticleEntity getEntityInCache(long id) {
		// TODO Auto-generated method stub
		if (id == -1) {
			return null;
		}
		ArticleEntity ae = (ArticleEntity) mCache.get(id);
		if (ae == null) {
			ae = aTable.get(id);
		}
		if (ae == null) {
			ae = aTempTable.get(id);
		}
		if (ae == NULL_ARTICLE) return null;
		if (ae != null && ae instanceof NewMagicWeaponEntity) {
			((NewMagicWeaponEntity) ae).initAttrNum();
		}
		return ae;
	}

	public List<ArticleEntity> getEntityByIds(long ids[]) {
		long startTime = System.currentTimeMillis();

		HashMap<Long, ArticleEntity> resultMap = new HashMap<Long, ArticleEntity>();

		ArrayList<Long> al = new ArrayList<Long>();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] > 0) {
				ArticleEntity ae = (ArticleEntity) mCache.get(ids[i]);
				if (ae == null) {
					ae = (ArticleEntity) aTempTable.get(ids[i]);
				}
				if (ae == null) {
					ae = (ArticleEntity) aTable.get(ids[i]);
				}
				if (ae != null) {
					resultMap.put(ids[i], ae);
				} else {
					al.add(ids[i]);
				}
			}
		}
		Object paramValues[] = new Object[al.size()];
		for (int i = 0; i < al.size(); i++) {
			sb.append("id=?");
			paramValues[i] = al.get(i);
			if (i < al.size() - 1) {
				sb.append(" OR ");
			}
		}
		if (al.size() > 1) {
			try {
				List<ArticleEntity> aeList = null;
				if ("mysql".equalsIgnoreCase(SimpleEntityManagerFactory.getDbType())) {
					aeList = new ArrayList<ArticleEntity>();
					for (int i = 0; i < al.size(); i++) {
						ArticleEntity ae = em.find(al.get(i));
						if (ae != null) {
							aeList.add(ae);
						}
					}
				} else {
					aeList = em.query(ArticleEntity.class, sb.toString(), paramValues, "", 1, al.size() + 1);
				}
				for (ArticleEntity ae : aeList) {
					mCache.put(ae.getId(), ae);
					resultMap.put(ae.getId(), ae);

					Article article = ArticleManager.getInstance().getArticle(ae.getArticleName());
					if (article != null && article.isOverlap() != ae.isOverlapFlag()) {
						ae.setOverlapFlag(article.isOverlap());
						em.notifyFieldChange(ae, "overlapFlag");
					}
					// 刷新附魔属性
					flushEquipmentFuMo(ae);
					flushEnchant(ae);
				}
			} catch (Exception e) {
				log.error("[批量加载道具] [失败] [where:" + sb.toString() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			}
		} else if (al.size() == 1) {
			try {
				ArticleEntity ae = em.find(al.get(0));
				if (ae != null) {
					mCache.put(ae.getId(), ae);
					resultMap.put(ae.getId(), ae);
					// 刷新附魔属性
					flushEquipmentFuMo(ae);
					flushEnchant(ae);
				}
			} catch (Exception e) {
				log.error("[批量加载道具] [失败] [where:" + sb.toString() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			}
		}
		ArrayList<ArticleEntity> resultList = new ArrayList<ArticleEntity>();
		for (int i = 0; i < ids.length; i++) {
			ArticleEntity ae = resultMap.get(ids[i]);
			if (ae != null) {
				resultList.add(ae);
				if (ae instanceof NewMagicWeaponEntity) {
					((NewMagicWeaponEntity) ae).initAttrNum();
				}
			}
		}
		return resultList;

	}

	@Override
	public ArticleEntity getEntity(String articleName, boolean binded, int color) {
		if (articleName == null) {
			return null;
		}
		String key = articleName + "#" + binded + "#" + color;
		ArticleEntity ae = (ArticleEntity) overlapEntityMap.get(key);
		if (ae == null) {
			try {
				// List<ArticleEntity> aeList = em.query(ArticleEntity.class,"articleName='"+articleName+"' and colorType="+color+" and binded='"+(binded?"T":"F")+"'", "", 1, 2);
				List<ArticleEntity> aeList = em.query(ArticleEntity.class, "articleName=? and colorType=? and binded=?", new Object[] { articleName, color, (binded ? "T" : "F") }, "", 1, 2);
				if (aeList != null && aeList.size() > 0) {
					ae = aeList.get(0);
					overlapEntityMap.put(key, ae);

					Article article = ArticleManager.getInstance().getArticle(ae.getArticleName());
					if (article != null && article.isOverlap() != ae.isOverlapFlag()) {
						ae.setOverlapFlag(article.isOverlap());
						em.notifyFieldChange(ae, "overlapFlag");
					}

					log.warn("[获取物品] [成功] [{}] [binded:{}] [id:{}] [color:{}]", new Object[] { articleName, binded, ae.getId(), color });
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("[获取物品] [失败] [" + articleName + "] [binded:" + binded + "] [color:" + color + "]", e);
			}
		}
		if (ae == NULL_ARTICLE) return null;
		if (ae != null && ae instanceof NewMagicWeaponEntity) {
			((NewMagicWeaponEntity) ae).initAttrNum();
		}
		return ae;
	}

	@Override
	public ArticleEntity getTempEntity(String articleName, boolean binded, int color) {
		if (articleName == null) {
			return null;
		}
		String key = articleName + "#" + binded + "#" + color;
		ArticleEntity ae = (ArticleEntity) overlapTempEntityMap.get(key);
		if (ae == NULL_ARTICLE) return null;
		if (ae != null && ae instanceof NewMagicWeaponEntity) {
			((NewMagicWeaponEntity) ae).initAttrNum();
		}
		return ae;
	}

	@Override
	public void batchSaveEntity(List<ArticleEntity> list) {
		// TODO Auto-generated method stub
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			em.batchSave(list.toArray(new ArticleEntity[0]));
			for (int i = 0; i < list.size(); i++) {
				ArticleEntity ae = list.get(i);
				ae.setDirty(false);
				log.info("[保存物品到cache服务器] [{}] [{}] [{}] [{}] [binded:{}] [{}] [{}ms]", new Object[] { Thread.currentThread().getName(), ae.getId(), ae.getArticleName(), ae.getShowName(), ae.isBinded(), ae.getClass().getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("============================严重的错误！=====================================");
			log.error("[创建物品] [exception:保存Entity失败，用户数据有丢失!] [" + Thread.currentThread().getName() + "] [" + list.size() + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]", e);
			log.error("============================严重的错误！=====================================");
			System.out.println("============================严重的错误！=====================================");
			System.out.println("[" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "] 保存Entity失败，用户数据有丢失！");
			System.out.println("============================严重的错误！=====================================");
		}
	}

	public void destroy() {
		long l = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		em.destroy();
		baoShiXiLianEM.destroy();
		System.out.println("[Destroy] 调用destroy方法保存所有物品实体, cost " + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - l) + " ms");
		log.warn("[Destroy] 调用destroy方法保存所有物品实体, cost {} ms", new Object[] { (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - l) });
	}

	@Override
	public long getCount() {
		// TODO Auto-generated method stub
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			long num = em.count();
			if (log.isInfoEnabled()) log.info("[获取物品数量] [{}] [{}ms]", new Object[] { num, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
			return num;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void entityTimeoutFromCache(ArticleEntity ae, int type) {
		long now = System.currentTimeMillis();
		try {
			em.save(ae);
			if (log.isInfoEnabled()) {
				log.info("[保存物品到数据库服务器] [{}] [{}] [{}] [binded:{}] [{}] [type:{}] [{}ms]", new Object[] { ae.getId(), ae.getArticleName(), ae.getShowName(), ae.isBinded(), ae.getClass().getName(), (type == CacheListener.LIFT_TIMEOUT ? "超时移出" : "超限溢出"), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
			}
		} catch (Exception e) {
			log.error("[保存物品到数据库服务器] [" + ae.getId() + "] [" + ae.getArticleName() + "] [{" + ae.getShowName() + "}] [binded:" + ae.isBinded() + "] [" + ae.getClass().getName() + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]", e);
		}
	}

	/**
	 * 内存中的信息：
	 * 
	 * {内存中物品数量，等待存盘的数量}
	 * @return
	 */
	public int[] getStatInfo() {
		Object os[] = null;
		os = mCache.values().toArray(new Object[0]);
		int a = 0;
		int b = 0;
		for (int i = 0; os != null && i < os.length; i++) {
			Object o = os[i];
			ArticleEntity entity = (ArticleEntity) ((CacheObject) o).object;
			if (entity == NULL_ARTICLE) continue;
			if (entity != null) {
				if (entity.isDirty()) {
					b++;
				}
				a++;
			}
		}
		return new int[] { a, b };
	}

	long removeArticleTime;

	public void run() {
		long loop = 0;
		ArticleManager am = ArticleManager.getInstance();
		while (running) {
			loop++;
			try {
				Thread.sleep(5000);
				if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
					continue;
				}
				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

				// 每5分钟检查内存中的物品是不是改删除
				// 从数据库删除物品规则
				// 无时间不可堆叠的物品当且仅当引用计数为0的时候才进行删除
				// 有时间不可堆叠的物品当且仅当引用计数为0的时候才进行删除
				// 无时间可堆叠的物品不执行删除操作
				// 有时间可堆叠的物品当且仅当引用计数为0的时候且过期后才进行删除
				if (now - removeArticleTime > 300000) {
					removeArticleTime = now;
					if (am == null) {
						am = ArticleManager.getInstance();
					}
					if (am != null) {

						Object os[] = null;
						os = mCache.values().toArray(new Object[0]);
						ArrayList<ArticleEntity> removeAes = new ArrayList<ArticleEntity>();
						for (int i = 0; os != null && i < os.length; i++) {
							Object o = os[i];
							ArticleEntity ae = (ArticleEntity) ((CacheObject) o).object;
							if (ae != null) {
								Article a = am.getArticle(ae.getArticleName());
								if (a != null) {
									if (!a.isOverlap() && ae.getReferenceCount() == 0) {
										removeAes.add(ae);
									} else if (a.isOverlap() && ae.getTimer() != null && ae.getTimer().isClosed() && ae.getReferenceCount() == 0) {
										removeAes.add(ae);
									}
								}
							}
						}

						for (ArticleEntity ae : removeAes) {
							// em.remove(ae);
							em.save(ae);
							mCache.remove(ae.getId());
							if (log.isWarnEnabled()) {
								log.warn("[测试数据库删除物品，只打日志并不删除] [物品id:" + ae.getId() + "] [物品名字:" + ae.getArticleName() + "] [物品颜色:" + ae.getColorType() + "] [绑定状态:" + ae.isBinded() + "]");
							}
						}
					}
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				log.error("[检查物品发生异常]", e);
			}
		}
	}

	public static LruMapCache getCache() {
		return mCache;
	}

	/**
	 * 通过颜色星级以及装备物种，生成装备实体
	 * 物种中包含职业，装备等级限制，部件位置 根据这些从数据里取值，组成装备实体
	 * 先通过职业取得该职业的装备属性数据及属性对应权重("通用职业装备取值","蜀山派装备取值","炼狱宗装备取值","昆仑山装备取值","灵隐阁装备取值","万毒谷装备取值")
	 * 在根据部位取得该部位应该具有的属性
	 * 在根据装备等级限制得到相应级别下的属性值
	 * @param ee
	 * @param e
	 * @param star
	 * @param levelLimit
	 */
	public static void setEquipmentEntity(EquipmentEntity ee, Equipment e, int star, int color) throws Exception {
		ReadEquipmentExcelManager reem = ReadEquipmentExcelManager.getInstance();
		if (reem != null) {
			if (star < 0 || star > ArticleManager.starMaxValue) {
				throw new Exception("star < 0 || star > ArticleManager.starMaxValue");
			}
			if (color < 0 || color > ArticleManager.equipmentColorMaxValue) {
				throw new Exception("color < 0 || color > ArticleManager.colorMaxValue");
			}
			ee.setStar(star);// 设定装备星级
			ee.setColorType(color);// 设定装备颜色
			int[][][] careerExcelDatas = reem.careerExcelDatas;
			double[][][] equipmentQuanZhong = reem.equipmentQuanZhong;
			int career = e.getCareerLimit();
			// 权重数组长度必须为2
			if (careerExcelDatas == null || careerExcelDatas.length <= career || equipmentQuanZhong == null || equipmentQuanZhong.length != 2) {
				throw new Exception("通过职业取得该职业的装备属性数据及属性对应权重失败");
			}
			int[][] careerEquipmentDatas = careerExcelDatas[career];
			double[][] equipQuanZhong = null;
			// 仙心、九黎、蓬莱岛装备属性权重
			if (career == CombatCaculator.仙心 || career == CombatCaculator.九黎 || career == CombatCaculator.蓬莱 || career == CombatCaculator.兽魁) {
				equipQuanZhong = equipmentQuanZhong[1];
				// 通用职业、修罗、影魅、灵隐阁装备属性权重
			} else {
				equipQuanZhong = equipmentQuanZhong[0];
			}

			// 根据装备级别得到装备所有属性值
			if (careerEquipmentDatas == null || careerEquipmentDatas.length <= e.getArticleLevel()) {
				throw new Exception("没有等级为" + e.getArticleLevel() + "的装备");
			}
			int[] equipmentPropertyValues = careerEquipmentDatas[e.getArticleLevel() - 1];
			if (equipmentPropertyValues == null) {
				throw new Exception("等级为" + e.getArticleLevel() + "的装备属性值为空");
			}

			// 装备的部件得到装备权重
			int equipmentType = e.getEquipmentType();
			if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
				ArticleManager.logger.info("生成翅膀 {}", e.getName());
				return;
			} else if (equipQuanZhong == null || equipQuanZhong.length <= equipmentType) {
				throw new Exception("通过根据部位取得该部位应该具有的属性失败");
			}
			double[] quanZhong = equipQuanZhong[equipmentType];
			if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
			} else if (quanZhong == null || quanZhong.length <= ArticleManager.suitPropertyInAllPropertyEndIndex) {
				throw new Exception("权重数据长度不足");
			}

			// ////////根据装备部件位置得到装备的基本属性，从权重中选择属性，权重不为零的属性及为装备的基本属性
			// 得到权重不为0的基本属性位置
			ArrayList<Integer> indexList = new ArrayList<Integer>();
			for (int i = ArticleManager.basicPropertyInAllPropertyStartIndex; i <= ArticleManager.basicPropertyInAllPropertyEndIndex; i++) {
				if (quanZhong[i] != 0) {
					indexList.add(i);
				}
			}

			int[] basicPropertyValue = new int[ArticleManager.basicPropertyArrayLength];
			if (indexList != null) {
				for (Integer index : indexList) {
					for (int i = 0; i < basicPropertyValue.length; i++) {
						if (index == (i + ArticleManager.basicPropertyInAllPropertyStartIndex) && equipmentPropertyValues.length > index) {
							// 公式 职业装备属性取值/3.3*颜色权重*部位权重
							if (equipmentPropertyValues[index] > 0) {
								int value = (int) (equipmentPropertyValues[index] * reem.colorValues[color] * quanZhong[index] / 3.3);
								if (value <= 0) {
									value = 1;
								}
								basicPropertyValue[i] = value;
							}
						}
					}
				}
			}
			// ////////////

			// //////根据装备颜色得到装备附加属性个数，根据装备星级的到属性数值
			if (reem.equipmentColorAdditionPropertyNumbers == null || reem.equipmentColorAdditionPropertyNumbers.length <= color) {
				throw new Exception("根据装备颜色得到装备附加属性个数异常");
			}
			int[] addtionPropertyNumber = reem.equipmentColorAdditionPropertyNumbers[color];
			if (addtionPropertyNumber == null || addtionPropertyNumber.length == 0) {
				throw new Exception("根据装备颜色得到装备附加属性对象为空");
			}
			int number = addtionPropertyNumber[random.nextInt(addtionPropertyNumber.length)];
			int[] addtionPropertyValue = new int[ArticleManager.additionPropertyArrayLength];
			if (number != 0) {
				// 得到权重不为0的附加属性位置
				ArrayList<Integer> addtionIndexList = new ArrayList<Integer>();
				if (color <= ArticleManager.equipment_color_蓝) {
					for (int i = ArticleManager.addtionPropertyInAllPropertyStartIndex; i <= ArticleManager.addtionPropertyInAllBluePropertyEndIndex; i++) {
						if (quanZhong[i] != 0) {
							addtionIndexList.add(i);
						}
					}
				} else {
					for (int i = ArticleManager.addtionPropertyInAllPropertyStartIndex; i <= ArticleManager.addtionPropertyInAllPropertyEndIndex; i++) {
						if (quanZhong[i] != 0) {
							addtionIndexList.add(i);
						}
					}
				}

				Collections.shuffle(addtionIndexList);
				ArrayList<Integer> hasPropertyIndexList = new ArrayList<Integer>();
				for (int i = 0; i < number; i++) {
					if (addtionIndexList.size() > i) {
						hasPropertyIndexList.add(addtionIndexList.get(i));
					}
				}
				for (Integer index : hasPropertyIndexList) {
					for (int i = 0; i < addtionPropertyValue.length; i++) {
						if (index == (i + ArticleManager.addtionPropertyInAllPropertyStartIndex) && equipmentPropertyValues.length > index) {
							// 公式 职业装备属性取值/3*颜色权重*部位权重
							int value = (int) (equipmentPropertyValues[index] * reem.colorValues[color] * quanZhong[index] / 3);
							if (value <= 0) {
								value = 1;
							}
							addtionPropertyValue[i] = value;
						}
					}
				}
			}
			// ////////////

			// /////根据装备颜色部件得到装备可以镶嵌宝石的颜色和数量
			/**
			 * 白装,淡绿装,深绿装,淡蓝装,深蓝装,淡紫装,深紫装,橙装
			 * 第一维为镶嵌宝石的颜色
			 * 第二维表示镶嵌宝石的id数组，数组为空表示不能镶嵌，数组中id为-1表示该位置可以镶嵌宝石目前还没有镶嵌宝石
			 */
			long[][] inlayArticleIds = new long[ArticleManager.inlayColorArrayLength][];

			// 得到该装备可镶嵌的宝石数量，戒指项链饰品不能镶嵌宝石
			// 假设马匹装备也可镶嵌宝石
			if (equipmentType != EquipmentColumn.EQUIPMENT_TYPE_fingerring && equipmentType != EquipmentColumn.EQUIPMENT_TYPE_jewelry && equipmentType != EquipmentColumn.EQUIPMENT_TYPE_necklace) {
				if (reem.equipmentColorLimitInlayNumber == null || reem.equipmentColorLimitInlayNumber.length <= equipmentType || reem.equipmentColorLimitInlayNumber[equipmentType] == null || reem.equipmentColorLimitInlayNumber[equipmentType].length <= color) {
					throw new Exception("根据装备颜色部件得到装备可以镶嵌宝石的颜色和数量失败");
				}
				int inlayCount = reem.equipmentColorLimitInlayNumber[equipmentType][color];
				if (inlayCount != 0) {
					if (reem.equipmentInlayColorLimit == null || reem.equipmentInlayColorLimit.length <= career || reem.equipmentInlayColorLimit[career] == null || reem.equipmentInlayColorLimit[career].length <= equipmentType || reem.equipmentInlayColorLimit[career][equipmentType] == null || reem.equipmentInlayColorLimit[career][equipmentType].length <= color) {
						throw new Exception("根据装备颜色部件得到装备可以镶嵌宝石的颜色和数量失败");
					}
					// 各个颜色对应的最大数量
					int[] inlayColorCounts = reem.equipmentInlayColorLimit[career][equipmentType];
					ArrayList<Integer> inlayCountIndex = new ArrayList<Integer>();
					for (int i = 0; i < inlayColorCounts.length; i++) {
						int inlayColorCount = inlayColorCounts[i];
						if (inlayColorCount > 0) {
							inlayCountIndex.add(i);
						}
					}
					Collections.shuffle(inlayCountIndex);
					for (Integer index : inlayCountIndex) {
						while (inlayCount > 0) {
							if (inlayColorCounts[index] >= inlayCount) {
								inlayArticleIds[index] = new long[inlayCount];
								for (int i = 0; i < inlayCount; i++) {
									inlayArticleIds[index][i] = -1;
								}
								inlayCount = 0;
							} else {
								inlayArticleIds[index] = new long[inlayColorCounts[index]];
								for (int i = 0; i < inlayColorCounts[index]; i++) {
									inlayArticleIds[index][i] = -1;
								}
								inlayCount = inlayCount - inlayColorCounts[index];
							}
							break;
						}
					}
				}
			}
			// ///////////////

			ee.setBasicPropertyValue(basicPropertyValue);
			ee.setAdditionPropertyValue(addtionPropertyValue);
			ArrayList<Long> al1 = new ArrayList<Long>();
			ArrayList<Integer> al2 = new ArrayList<Integer>();
			if (inlayArticleIds != null && inlayArticleIds.length != 0) {
				for (int i = 0; i < inlayArticleIds.length; i++) {
					long[] ids = inlayArticleIds[i];
					if (ids != null && ids.length != 0) {
						for (long id : ids) {
							al1.add(id);
							al2.add(i);
						}
					}
				}
			}
			long[] inlayArticleIdss = new long[al1.size()];
			int[] inlayArticleColors = new int[al1.size()];

			for (int i = 0; i < al1.size(); i++) {
				inlayArticleIdss[i] = al1.get(i);
				inlayArticleColors[i] = al2.get(i);
			}

			ee.setInlayArticleIds(inlayArticleIdss);
			ee.setInlayArticleColors(inlayArticleColors);

			// //////////器灵，紫色1个器灵孔，完美紫2个器灵孔，橙3个器灵孔，完美橙4个器灵孔
			if (color >= 3 && !(ee instanceof Special_1EquipmentEntity) && !(ee instanceof Special_2EquipmentEntity)) {
				if (e.getEquipmentType() < Equipment.坐骑装备类型开始index) {
					ArrayList<Integer> qilingTypeList = new ArrayList<Integer>();
					for (int i = 0; i < 13; i++) {
						qilingTypeList.add(i);
					}
					Collections.shuffle(qilingTypeList);

					if (color == 3) {
						ee.setInlayQiLingArticleIds(new long[1]);
						ee.setInlayQiLingArticleTypes(new int[] { qilingTypeList.get(0) });
					} else if (color == 4) {
						ee.setInlayQiLingArticleIds(new long[2]);
						ee.setInlayQiLingArticleTypes(new int[] { qilingTypeList.get(0), qilingTypeList.get(1) });
					} else if (color == 5) {
						ee.setInlayQiLingArticleIds(new long[3]);
						ee.setInlayQiLingArticleTypes(new int[] { qilingTypeList.get(0), qilingTypeList.get(1), qilingTypeList.get(2) });
					} else if (color == 6) {
						ee.setInlayQiLingArticleIds(new long[4]);
						ee.setInlayQiLingArticleTypes(new int[] { qilingTypeList.get(0), qilingTypeList.get(1), qilingTypeList.get(2), qilingTypeList.get(3) });
					}
				}
			}

			StringBuffer sb = new StringBuffer();
			sb.append(ee.getArticleName() + "装备id:" + ee.getId());
			sb.append("孔颜色个数:");
			for (int i = 0; i < inlayArticleIdss.length; i++) {
				sb.append(ArticleManager.getInlayColorString(inlayArticleColors[i]) + ":");
				sb.append(inlayArticleIdss[i] + ",");
			}
			sb.append("基本属性:");
			for (int a : basicPropertyValue) {
				sb.append(a + ",");
			}
			sb.append("附加属性:");
			for (int a : addtionPropertyValue) {
				sb.append(a + ",");
			}
			if (ArticleManager.logger.isInfoEnabled()) {
				ArticleManager.logger.info("[新生成装备:{}]", new Object[] { sb });
			}

		}
	}

	public boolean xilianEquipmentAddProp(EquipmentEntity ee, boolean bind) throws Exception {
		ReadEquipmentExcelManager reem = ReadEquipmentExcelManager.getInstance();
		if (reem != null) {
			int color = ee.getColorType();
			int[][][] careerExcelDatas = reem.careerExcelDatas;
			double[][][] equipmentQuanZhong = reem.equipmentQuanZhong;
			Article a = ArticleManager.getInstance().getArticle(ee.getArticleName());
			if (a != null && a instanceof Equipment) {
				Equipment e = (Equipment) a;
				int career = e.getCareerLimit();
				// 权重数组长度必须为2
				if (careerExcelDatas == null || careerExcelDatas.length <= career || equipmentQuanZhong == null || equipmentQuanZhong.length != 2) {
					throw new Exception("通过职业取得该职业的装备属性数据及属性对应权重失败");
				}
				int[][] careerEquipmentDatas = careerExcelDatas[career];
				double[][] equipQuanZhong = null;
				// 仙心、九黎、蓬莱岛装备属性权重
				if (career == CombatCaculator.仙心 || career == CombatCaculator.九黎 || career == CombatCaculator.蓬莱 || career == CombatCaculator.兽魁) {
					equipQuanZhong = equipmentQuanZhong[1];
					// 通用职业、修罗、影魅、灵隐阁装备属性权重
				} else {
					equipQuanZhong = equipmentQuanZhong[0];
				}

				// 根据装备级别得到装备所有属性值
				if (careerEquipmentDatas == null || careerEquipmentDatas.length <= e.getArticleLevel()) {
					throw new Exception("没有等级为" + e.getArticleLevel() + "的装备");
				}
				int[] equipmentPropertyValues = careerEquipmentDatas[e.getArticleLevel() - 1];
				if (equipmentPropertyValues == null) {
					throw new Exception("等级为" + e.getArticleLevel() + "的装备属性值为空");
				}

				// 装备的部件得到装备权重
				int equipmentType = e.getEquipmentType();
				if (equipQuanZhong == null || equipQuanZhong.length <= equipmentType) {
					throw new Exception("通过根据部位取得该部位应该具有的属性失败");
				}
				double[] quanZhong = equipQuanZhong[equipmentType];
				if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
				} else if (quanZhong == null || quanZhong.length <= ArticleManager.suitPropertyInAllPropertyEndIndex) {
					throw new Exception("权重数据长度不足");
				}

				// //////根据装备颜色得到装备附加属性个数，根据装备星级的到属性数值
				if (reem.equipmentColorAdditionPropertyNumbers == null || reem.equipmentColorAdditionPropertyNumbers.length <= color) {
					throw new Exception("根据装备颜色得到装备附加属性个数异常");
				}
				int[] addtionPropertyNumber = reem.equipmentColorAdditionPropertyNumbers[color];
				int[] addtionPropertyNumberProp = reem.equipmentColorAdditionPropertyNumbersProp[color];
				if (addtionPropertyNumber == null || addtionPropertyNumber.length == 0) {
					throw new Exception("根据装备颜色得到装备附加属性对象为空");
				}
				int allValue = 0;
				for (int i = 0; i < addtionPropertyNumberProp.length; i++) {
					allValue = allValue + addtionPropertyNumberProp[i];
				}
				// 随机出附加属性个数
				int number = 0;
				int randomNum = random.nextInt(allValue) + 1;
				int nowValue = 0;
				for (int i = 0; i < addtionPropertyNumberProp.length; i++) {
					if (nowValue < randomNum && randomNum <= nowValue + addtionPropertyNumberProp[i]) {
						number = addtionPropertyNumber[i];
					}
					nowValue = nowValue + addtionPropertyNumberProp[i];
				}
				if (number == 0) {
					ArticleManager.logger.error("[随机获得附加属性个数失败]");
					throw new Exception("随机获得附加属性个数失败");
				}
				int[] addtionPropertyValue = new int[ArticleManager.additionPropertyArrayLength];
				if (number != 0) {
					// 得到权重不为0的附加属性位置
					ArrayList<Integer> addtionIndexList = new ArrayList<Integer>();
					if (color <= ArticleManager.equipment_color_蓝) {
						for (int i = ArticleManager.addtionPropertyInAllPropertyStartIndex; i <= ArticleManager.addtionPropertyInAllBluePropertyEndIndex; i++) {
							if (quanZhong[i] != 0) {
								addtionIndexList.add(i);
							}
						}
					} else {
						for (int i = ArticleManager.addtionPropertyInAllPropertyStartIndex; i <= ArticleManager.addtionPropertyInAllPropertyEndIndex; i++) {
							if (quanZhong[i] != 0) {
								addtionIndexList.add(i);
							}
						}
					}

					Collections.shuffle(addtionIndexList);
					ArrayList<Integer> hasPropertyIndexList = new ArrayList<Integer>();
					for (int i = 0; i < number; i++) {
						if (addtionIndexList.size() > i) {
							hasPropertyIndexList.add(addtionIndexList.get(i));
						}
					}
					for (Integer index : hasPropertyIndexList) {
						for (int i = 0; i < addtionPropertyValue.length; i++) {
							if (index == (i + ArticleManager.addtionPropertyInAllPropertyStartIndex) && equipmentPropertyValues.length > index) {
								// 公式 职业装备属性取值/3*颜色权重*部位权重
								int value = (int) (equipmentPropertyValues[index] * reem.colorValues[color] * quanZhong[index] / 3);
								if (value <= 0) {
									value = 1;
								}
								addtionPropertyValue[i] = value;
							}
						}
					}
					ee.setAdditionPropertyValue(addtionPropertyValue);
					ee.setBinded(bind);
					ArticleManager.logger.info("[装备洗练完成] [" + ee.getArticleName() + "] [id:" + ee.getId() + "] [bind:" + ee.isBinded() + "]");
					return true;
				}
			}
		}
		ArticleManager.logger.info("[装备洗练] [" + ee.getArticleName() + "] [id:" + ee.getId() + "]");
		return false;
	}

	@Override
	public boolean putToRealSaveCache(ArticleEntity ae) {
		if (ae != null) {
			mCache.put(ae.getId(), ae);
			if (!em.contains(ae.getId())) {
				em.notifyNewObject(ae);
			}
			removeTransientATable(ae);
			return true;
		}
		return false;
	}

	@Override
	public void removeTransientATable(ArticleEntity ae) {
		if (ae != null) {
			aTable.remove(ae.getId());
		}
	}

	public static LruMapCache getmCache() {
		return mCache;
	}

	public static Hashtable<Long, ArticleEntity> getaTable() {
		return aTable;
	}

	public static Hashtable<String, ArticleEntity> getOverlapEntityMap() {
		return overlapEntityMap;
	}

	public static Hashtable<Long, ArticleEntity> getaTempTable() {
		return aTempTable;
	}

	public static Hashtable<String, ArticleEntity> getOverlapTempEntityMap() {
		return overlapTempEntityMap;
	}

	/**
	 * 加载装备上的附魔信息
	 */
	public void flushEnchant(ArticleEntity ae) {
		if (ae instanceof HuntLifeArticleEntity) {
			HuntLifeEntityManager.instance.getHuntArticleExtraData((HuntLifeArticleEntity) ae);
		}
		if (ae instanceof SoulPithArticleEntity) {
			SoulPithEntityManager.getInst().getArticleExtraData((SoulPithArticleEntity) ae);
		}
		if (!(ae instanceof EquipmentEntity)) {
			return;
		}
		EnchantEntityManager.instance.getEntity((EquipmentEntity) ae);
	}

	public void flushEquipmentFuMo(ArticleEntity ae) {
		
		if (ae instanceof NewMagicWeaponEntity) {			
			if (!ae.isBinded() && "5周年纪念法宝".equals(ae.getArticleName())) {		//2017年6月20日10:16:43  调整此物品为绑定
				ae.setBinded(true);
			}
		}
		if (!(ae instanceof EquipmentEntity)) {
			return;
		}
		EquipmentEntity ee = (EquipmentEntity) ae;
		if (ee.getInlayQiLingArticleIds() == null || ee.getInlayQiLingArticleTypes() == null || ee.getInlayQiLingArticleIds().length == 0 || ee.getInlayQiLingArticleTypes().length == 0) {
			int color = ee.getColorType();
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				Article a = am.getArticle(ee.getArticleName());
				if (a instanceof Equipment) {
					Equipment e = (Equipment) a;
					// //////////器灵，紫色1个器灵孔，完美紫2个器灵孔，橙3个器灵孔，完美橙4个器灵孔
					if (color >= 3 && !(ee instanceof Special_1EquipmentEntity) && !(ee instanceof Special_2EquipmentEntity)) {
						if (e.getEquipmentType() < Equipment.坐骑装备类型开始index) {
							ArrayList<Integer> qilingTypeList = new ArrayList<Integer>();
							for (int i = 0; i < 13; i++) {
								qilingTypeList.add(i);
							}
							Collections.shuffle(qilingTypeList);

							if (color == 3) {
								ee.setInlayQiLingArticleIds(new long[1]);
								ee.setInlayQiLingArticleTypes(new int[] { qilingTypeList.get(0) });
							} else if (color == 4) {
								ee.setInlayQiLingArticleIds(new long[2]);
								ee.setInlayQiLingArticleTypes(new int[] { qilingTypeList.get(0), qilingTypeList.get(1) });
							} else if (color == 5) {
								ee.setInlayQiLingArticleIds(new long[3]);
								ee.setInlayQiLingArticleTypes(new int[] { qilingTypeList.get(0), qilingTypeList.get(1), qilingTypeList.get(2) });
							} else if (color == 6) {
								ee.setInlayQiLingArticleIds(new long[4]);
								ee.setInlayQiLingArticleTypes(new int[] { qilingTypeList.get(0), qilingTypeList.get(1), qilingTypeList.get(2), qilingTypeList.get(3) });
							}
						}
					}
				}
			}
		}
	}
}
