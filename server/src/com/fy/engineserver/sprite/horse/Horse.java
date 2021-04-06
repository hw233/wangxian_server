package com.fy.engineserver.sprite.horse;

import static com.fy.engineserver.datasource.language.Translate.包裹空间不够;
import static com.fy.engineserver.datasource.language.Translate.非战斗坐骑不能穿装备;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.core.FieldChangeEvent;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.HorseFoodArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.HunshiEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.HorseEquipmentColumn;
import com.fy.engineserver.datasource.article.data.props.AvataProps;
import com.fy.engineserver.datasource.article.data.props.HorseProps;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.charge.CardFunction;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.hunshi.Hunshi2Cell;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.hunshi.HunshiSuit;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NOTIFY_HORSE_CHANGE_REQ;
import com.fy.engineserver.message.NOTIFY_HORSE_SELFCHANGE_REQ;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.sprite.horse2.model.HorseAttrModel;
import com.fy.engineserver.sprite.horse2.model.HorseColorModel;
import com.fy.engineserver.sprite.horse2.model.HorseRankModel;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 
 * @author Administrator
 * 
 */
@SimpleEntity
public class Horse implements Cacheable, CacheListener {

	public static Logger logger = HorseManager.logger;

	public Horse() {

	}

	@SimpleId
	private long horseId;

	@SimpleVersion
	private int version;

	private boolean isLimitTime;
	// private long 到期时间;
	private long dueTime;

	private String horseParticle;

	// 饱和度下降间隔时间
	public static long INTERVALTIME = 10 * 60 * 1000;
	// 饱和度每间隔时间下降值
	private static int DOWNNUM = 15;

	private transient int bloodStar;
	/** 可获得的最大技能数 */
	private transient int skillNum;
	/** 坐骑颜色 */
	private transient int colorType;
	/** 成长(乘以100以后的数) */
	private transient int growRate;
	/**
	 * 新坐骑系统添加
	 */
	private HorseOtherData otherData = new HorseOtherData();
	private long[] hunshiArray = new long[16]; // 魂石镶嵌数组
	private boolean[] hunshiOpen = new boolean[16]; // 魂石开启数组
	private long[] hunshi2Array = new long[16];// 套装魂石镶嵌数组
	private boolean[] hunshi2Open = new boolean[16];// 套装魂石格子开启数组
	/**生效的坐骑魂石套装技能id*/
	private transient List<Integer> hunshiSkills=new ArrayList<Integer>();

	// 100以上 100%
	// 81-99 90%
	// 71-80 80%
	// 61-70 70%
	// 1-60 60%
	// 0 50%
	// private static int PART0NUM = 1;
	// private static int PART1NUM = 0;
	private static int PART2NUM = 200;
	private static int PART3NUM = 300;
	private static int PART4NUM = 400;

	// private static int LEVEL0RATE = 5;
	// private static int LEVEL1RATE = 6;
	private static int LEVEL2RATE = 2;
	private static int LEVEL3RATE = 5;
	private static int LEVEL4RATE = 8;
	private static int LEVEL5RATE = 10;

	public static int white = 60;
	public static int green = 120;
	public static int blue = 180;
	public static int purple = 240;
	public static int orange = 300;

	@Override
	public void remove(int arg0) {
		try {
			HorseManager.em.save(this);
		} catch (Exception e) {
			HorseManager.logger.error("[从缓存删除异常]", e);
		}
	}

	@Override
	public int getSize() {
		return 10;
	}

	public String getLogString() {
		return "{horseId:" + getHorseId() + "}{horsename:" + horseName + "}";
	}

	// 跟人的等级一样 查询坐骑的时候赋值
	private transient int horseLevel;

	// 从道具配置的到的 avata部件
	protected transient String avataKey = "";
	// 仙界avata部件
	private transient String newAvatarKey = "";

	// 不用保存的
	protected transient String avata = "";

	// 马匹在马匹界面上的图标
	protected transient String icon = "";

	private transient int maxEnergy;

	// 坐骑的速度
	private transient int speed;
	// 等阶
	private transient int rank;
	// 对应人物职业
	private transient int type;
	// true 为战斗
	private transient boolean fight;

	// true 为飞行
	private transient boolean fly;

	private transient long[] equipmentIds = { -1, -1, -1, -1, -1 };

	// 坐骑道具name
	@SimpleColumn(length = 256)
	private String horseName = "";

	private transient String horseShowName = "";

	// 上次心跳时间
	private long lastUpdateTime;

	// 检查限时坐骑且删除
	public boolean checkHorseTime(long time) {
		if (this.isLimitTime) {
			if (time >= getDueTime()) {

				if (owner != null) {
					if (owner.isIsUpOrDown() && owner.getRidingHorseId() == this.horseId) {
						// 下马
						owner.downFromHorse();
					}

					boolean bln = owner.getHorseIdList().remove(this.horseId);
					if (bln) {
						owner.sendError(Translate.translateString(Translate.限时坐骑已到期将从坐骑列表中删除, new String[][] { { Translate.STRING_1, this.getHorseName() } }));

						if (owner.getRideHorseId() == horseId) {
							// 设置默认坐骑
							owner.setRideHorseId(-1);
						}

						owner.setHorseIdList(owner.getHorseIdList());
					}

					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[限时坐骑到期] [" + this.getLogString() + "] [" + owner.getLogString() + "] [horseId:" + horseId + "] [删除成功:" + bln + "]");
					}
					return bln;
				}
			}
		}

		return false;
	}

	/**
	 * 宠物身上的装备栏
	 */
	@SimpleColumn(length = 50)
	private HorseEquipmentColumn equipmentColumn;

	private transient long startHeartBeatTime;

	/**
	 * 使用坐骑持续时间
	 */
	private long holdOnTime = 0;

	private long ownerId;

	/**
	 * 体力值
	 */
	private int energy;

	/**
	 * 上一个体力变化阶段 100以上 81-99 71-80 61-70 1-60 0 对下坐骑作用 5 4 3 2 1 0
	 */
	private int lastEnergyIndex = LEVEL5RATE;

	private transient boolean equipmentUpdate = false;

	private transient boolean lastfull = true;

	public transient Player owner;
	private transient boolean inited = false;
	private transient Object syn = new Object();

	private transient byte career = 1;

	public boolean isInited() {
		return inited;
	}

	public void setInited(boolean inited) {
		this.inited = inited;
	}

	/**
	 * 初始化坐骑
	 */
	public void init() {

		synchronized (syn) {
			if (inited) {
				HorseManager.logger.error("[重复初始化坐骑] [" + this.getLogString() + "] [" + ownerId + "]");
				return;
			}
			if (owner == null) {
				HorseManager.logger.error("[坐骑初始化失败] [owner为null]");
				return;
			}
			this.setHorseLevel(owner.getSoulLevel());
			// boolean due = this.checkHorseTime(SystemTime.currentTimeMillis());
			// if(due){
			// return;
			// }
			Article article = ArticleManager.getInstance().getArticle(this.horseName);
			if (article instanceof HorseProps) {
				HorseProps hp = (HorseProps) article;
				this.setType(hp.getType());
				this.setFight(hp.isFight());
				this.setFly(hp.isFly());
				this.setRank(hp.getRank());
				this.setAvataKey(hp.getAvata());
				this.setHorseParticle(hp.getHorseParticle());
				this.setMaxEnergy(hp.getMaxEnergy());
				this.setHorseParticle(hp.getHorseParticle());
				this.setIcon(hp.getIconId());
				this.setNewAvatarKey(hp.getAvata());
				// try {
				// if (this.isFly() && hp.getName_stat().equals(PlayerAimManager.黑幕)) {
				// AchievementManager.getInstance().record(owner, RecordAction.获得黑雾);
				// }
				// } catch (Exception e) {
				// PlayerAimManager.logger.error("[目标系统] [init坐骑统计黑幕] [异常] [" + owner.getLogString() + "]", e);
				// }
			} else {
				logger.error("[坐骑初始化错误] [没有对应的坐骑道具:" + this.horseName + "] [|" + owner.getLogString() + "]");
				return;
			}
			if (!this.isFly()) {
				if (Horse2Manager.logger.isDebugEnabled()) {
					Horse2Manager.logger.debug("[新坐骑系统] [非飞行坐骑初始化otherData中的属性] [horseId :" + this.getHorseId() + "] [" + this.otherData + "]");
				}
				this.setRank(this.otherData.getRankStar());
				colorType = this.otherData.getColorType();
				bloodStar = this.otherData.getBloodStar();
				HorseRankModel hrm = Horse2Manager.instance.rankModelMap.get(this.getRank());
				HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(this.getColorType());
				if (hrm != null && hcm != null) {
					skillNum = hrm.getOpenSkillNum();
					this.setSpeed(hrm.getSpeed());
					growRate = (int) (hcm.getGrowUpRate() * 100);
					if (career > 0) {
						if(owner.ownMonthCard(CardFunction.坐骑进阶形象额外加1)){
							this.setAvataKey(hrm.getMonthAvatar()[career - 1]);
						}else{
							this.setAvataKey(hrm.getAvatar()[career - 1]);
						}
						this.setNewAvatarKey(hrm.getNewAvatar()[career - 1]);
						if (hrm.getPartical() != null && hrm.getPartical().length >= 4) {
							this.setHorseParticle(hrm.getPartical()[career - 1]);
						}
						if (hrm.getHorseName() != null && career <= hrm.getHorseName().length) {
							this.horseShowName = hrm.getHorseName()[career - 1];
						} else {
							this.horseShowName = this.horseName;
						}
					}
				} else {
					Horse2Manager.logger.error("[新坐骑系统] [获取HorseRankModel为空,异常] [等阶:" + this.getRank() + "] [horseId :" + this.getHorseId() + "][颜色:" + this.getColorType() + "]");
				}
				try {
					if (otherData.getSkillLevel() != null && otherData.getSkillLevel().length > 2) {
						int lv2Num = 0;
						int lv3Num = 0;
						for (int i = 0; i < otherData.getSkillLevel().length; i++) {
							if (otherData.getSkillLevel()[i] >= 1) {
								lv2Num++;
								if (otherData.getSkillLevel()[i] >= 2) {
									lv3Num++;
								}
							}
						}
						if (lv2Num > 0) {
							AchievementManager.getInstance().record(owner, RecordAction.坐骑拥有2级技能个数, lv2Num);
							if (lv3Num > 0) {
								AchievementManager.getInstance().record(owner, RecordAction.坐骑拥有3级技能个数, lv3Num);
							}
						}
					}
					if (otherData.getBloodStar() > 5) {
						AchievementManager.getInstance().record(owner, RecordAction.坐骑升级最大血脉阶, otherData.getBloodStar());
					}
					try {
						EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { owner.getId(), RecordAction.坐骑升级最大阶数, otherData.getRankStar() });
						EventRouter.getInst().addEvent(evt3);
					} catch (Exception eex) {
						PlayerAimManager.logger.error("[目标系统] [统计坐骑阶][" + owner.getLogString() + "]", eex);
					}
				} catch (Exception e) {
					PlayerAimManager.logger.error("[目标系统] [检测坐骑技能等级] [异常] [" + this.owner.getLogString() + "] [horseId:" + this.getHorseId() + "]", e);
				}
				if (Horse2Manager.logger.isDebugEnabled()) {
					Horse2Manager.logger.debug("[新坐骑系统] [非飞行坐骑初始化初始化完毕] [horseId :" + this.getHorseId() + "] [" + this.otherData + "] [horseShowName:" + horseShowName + "] [setAvataKey:" + getAvataKey() + "]");
				}
			} else {
				this.horseShowName = this.horseName;
			}
			ResourceManager.getInstance().getHorseAvataForPlayer(this, owner);
			if (equipmentColumn == null) {
				equipmentColumn = new HorseEquipmentColumn();
				equipmentColumn.init();
			}
			equipmentColumn.setHorse(this);
			this.clearAll();
			initBasicAttr();
			initAttr();
			initHunshi();
			inited = true;
		}
	}

	/**
	 * 初始化坐骑魂石
	 */
	public void initHunshi() {
		HunshiManager hm = HunshiManager.getInstance();
		try {
			if (getHunshiArray() == null) {
				setHunshiArray(new long[16]);
			}
			if (getHunshiOpen() == null) {
				boolean[] open = new boolean[16];
				for (int i = 0; i < open.length; i++) {
					if (hm.openHole.get(i).getPlayerLevel() <= owner.getLevel()) {
						open[i] = true;
					}
				}
				setHunshiOpen(open);
			}
			if (getHunshi2Array() == null) {
				setHunshi2Array(new long[16]);
			}
			if (getHunshi2Open() == null) {
				boolean[] open = new boolean[16];
				for (int i = 0; i < open.length; i++) {
					if (hm.openHole2.get(i).getPlayerLevel() <= owner.getLevel()) {
						open[i] = true;
					}
				}
				setHunshi2Open(open);
			}
		} catch (Exception e) {
			logger.error("[初始化坐骑魂石异常] [" + horseId + "]", e);
			e.printStackTrace();
		}
		try {
			if (check4Hunshi(this.getHunshiArray())) {
				long[] oldHunshiArray = Arrays.copyOf(this.getHunshiArray(), this.getHunshiArray().length);
				long[] result = deleteHunshi(this.getHunshiArray());
				this.setHunshiArray(result);
				logger.warn("[刷魂石] [删除坐骑身上魂石] [" + owner.getLogString() + "] [horseId:" + this.getHorseId() + "] [原魂石列表:" + Arrays.toString(oldHunshiArray) + "] [删除后列表:" + Arrays.toString(this.getHunshiArray()) + "]");
			}
			if (check4Hunshi(this.getHunshi2Array())) {
				long[] oldHunshiArray = Arrays.copyOf(this.getHunshi2Array(), this.getHunshi2Array().length);
				long[] result = deleteHunshi(this.getHunshi2Array());
				this.setHunshi2Array(result);
				logger.warn("[刷魂石] [删除坐骑身上魂石] [" + owner.getLogString() + "] [horseId:" + this.getHorseId() + "] [原魂石套装列表:" + Arrays.toString(oldHunshiArray) + "] [删除后列表:" + Arrays.toString(this.getHunshi2Array()) + "]");
			}
			
		} catch (Exception e) {
			logger.warn("[刷魂石] [异常] [" + owner.getLogString() + "] [horseId:" + this.getHorseId() + "]", e);
		}
		initHunshiAttr(1);
	}
	
	public boolean check4Hunshi(long[] hunshiArray) {
		for (int i=0; i<hunshiArray.length; i++) {
			for (long id : RobberyConstant.需要清除的魂石Id) {
				if (id == hunshiArray[i]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public long[] deleteHunshi(long[] hunshiArray) {
		long[] result = Arrays.copyOf(hunshiArray, hunshiArray.length);
		for (int i=0; i<result.length; i++) {
			for (long id : RobberyConstant.需要清除的魂石Id) {
				if (id == result[i]) {
					result[i] = -1;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 计算坐骑魂石属性加成
	 * @param onORoff
	 *            -1：卸载，1加载
	 */
	public void initHunshiAttr(int onORoff) {
		try {
			long start = System.currentTimeMillis();
			HunshiManager hm = HunshiManager.getInstance();
			int propValue = 0;
			// TODO 属性
			for (long hunshiId : hunshiArray) {
				if (hunshiId > 0) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hunshiId);
					if (ae != null && ae instanceof HunshiEntity) {
						HunshiEntity he = (HunshiEntity) ae;
						for (int i = 0; i < he.getMainPropValue().length; i++) {
							if (onORoff == -1) {
								propValue = -he.getMainPropValue()[i];
							} else if (onORoff == 1) {
								propValue = he.getMainPropValue()[i];
							}
							hm.addRemoveProp(this, i, propValue);
						}
						for (int i = 0; i < he.getExtraPropValue().length; i++) {
							if (onORoff == -1) {
								propValue = -he.getExtraPropValue()[i];
							} else if (onORoff == 1) {
								propValue = he.getExtraPropValue()[i];
							}
							hm.addRemoveProp(this, i, propValue);
						}
					}
				}
			}
			for (long hunshiId : hunshi2Array) {
				if (hunshiId > 0) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hunshiId);
					if (ae != null && ae instanceof HunshiEntity) {
						HunshiEntity he = (HunshiEntity) ae;
						for (int i = 0; i < he.getMainPropValue().length; i++) {
							if (onORoff == -1) {
								propValue = -he.getMainPropValue()[i];
							} else if (onORoff == 1) {
								propValue = he.getMainPropValue()[i];
							}
							hm.addRemoveProp(this, i, propValue);
						}
						for (int i = 0; i < he.getExtraPropValue().length; i++) {
							if (onORoff == -1) {
								propValue = -he.getExtraPropValue()[i];
							} else if (onORoff == 1) {
								propValue = he.getExtraPropValue()[i];
							}
							hm.addRemoveProp(this, i, propValue);
						}
					}
				}
			}
			hunshiSkills.clear();
			List<HunshiSuit> suitList = hm.getHunshiSuitList(hunshi2Array);
			if (suitList != null && suitList.size() > 0) {
				for (HunshiSuit suit : suitList) {
					for (int i = 0; i < suit.getPropValue().length; i++) {
						if (onORoff == -1) {
							propValue = -suit.getPropValue()[i];
						} else if (onORoff == 1) {
							propValue = suit.getPropValue()[i];
						}
						hm.addRemoveProp(this, suit.getPropId()[i], propValue);
					}
					
					//更新坐骑已有的魂石套装技能
					hunshiSkills.add(suit.getSkillId()); 
				}
			}
			notifySelfChange();
			if (HorseManager.logger.isDebugEnabled()) {
				HorseManager.logger.debug("[初始化坐骑魂石属性] [" + getLogString() + "] [" + (System.currentTimeMillis() - start) + "ms]");
			}
		} catch (Exception e) {
			logger.error("[初始化坐骑魂石属性异常] [" + horseId + "]", e);
			e.printStackTrace();
		}
	}

	private transient HorseAttrModel oldBasicAttr;

	/**
	 * 初始化基础属性
	 */
	public void initBasicAttr() {
		int[] skills = otherData.getSkillList();
		int[] skillLevels = otherData.getSkillLevel();
		Horse2EntityManager.instance.initHorseBasicAttr(this, owner, career, getColorType(), getRank(), getBloodStar(), skills, skillLevels);
		ArrayList<FieldChangeEvent> fieldList = new ArrayList<FieldChangeEvent>();
		boolean marks[] = this.selfMarks;
		for (int l = 0; l < marks.length; l++) {
			if (marks[l]) {
				FieldChangeEvent event = new FieldChangeEvent(this, (byte) l, this.getSelfValue(l));
				marks[l] = false;
				fieldList.add(event);
				if (HorseManager.logger.isDebugEnabled()) {
					HorseManager.logger.debug("[新坐骑系统] [NOTIFY_HORSE_SELFCHANGE_REQ] [变化的属性type:" + l + "] [变化后的属性为:" + this.getSelfValue(l).toString() + "]");
				}
			}
		}
		if (fieldList.size() > 0) {
			NOTIFY_HORSE_SELFCHANGE_REQ req = new NOTIFY_HORSE_SELFCHANGE_REQ(GameMessageFactory.nextSequnceNum(), fieldList.toArray(new FieldChangeEvent[0]));
			owner.addMessageToRightBag(req);
		}
	}

	public void notifySelfChange() {
		ArrayList<FieldChangeEvent> fieldList = new ArrayList<FieldChangeEvent>();
		boolean marks[] = this.selfMarks;
		for (int l = 0; l < marks.length; l++) {
			if (marks[l]) {
				FieldChangeEvent event = new FieldChangeEvent(this, (byte) l, this.getSelfValue(l));
				marks[l] = false;
				fieldList.add(event);
				if (HorseManager.logger.isDebugEnabled()) {
					HorseManager.logger.debug("[新坐骑系统] [NOTIFY_HORSE_SELFCHANGE_REQ] [变化的属性type:" + l + "] [变化后的属性为:" + this.getSelfValue(l).toString() + "]");
				}
			}
		}
		if (fieldList.size() > 0) {
			NOTIFY_HORSE_SELFCHANGE_REQ req = new NOTIFY_HORSE_SELFCHANGE_REQ(GameMessageFactory.nextSequnceNum(), fieldList.toArray(new FieldChangeEvent[0]));
			owner.addMessageToRightBag(req);
		}
	}

	public Object getSelfValue(int id) {
		switch (id) {
		case 0:
			return maxHP;
		case 1:
			return phyAttack;
		case 2:
			return magicAttack;
		case 3:
			return phyDefence;
		case 4:
			return magicDefence;
		case 5:
			return maxMP;
		case 6:
			return breakDefence;
		case 7:
			return accurate;
		case 8:
			return criticalDefence;
		case 9:
			return criticalHit;
		case 10:
			return hit;
		case 11:
			return dodge;
		case 12:
			return blizzardAttack;
		case 13:
			return blizzardDefence;
		case 14:
			return blizzardIgnoreDefence;
		case 15:
			return fireAttack;
		case 16:
			return fireDefence;
		case 17:
			return fireIgnoreDefence;
		case 18:
			return windAttack;
		case 19:
			return windDefence;
		case 20:
			return windIgnoreDefence;
		case 21:
			return thunderAttack;
		case 22:
			return thunderDefence;
		case 23:
			return thunderIgnoreDefence;
		case 24:
			return otherData.getColorType();
		case 25:
			return otherData.getBloodStar();
		case 26:
			return skillNum;
		case 27:
			return growRate;
		case 28:
			return otherData.getRankStar();
		case 29:
			return speed;
		case 30:
			return this.horseShowName;
		case 31:
			return this.avata;
		case 32:
			return this.horseParticle;
		case 33:
			HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(this.getColorType());
			if (hcm != null) {
				return hcm.getMaxStar();
			}
			return 6;
		}
		return null;
	}

	/**
	 * 初始化装备属性
	 */
	private void initAttr() {

		this.equipmentIds = this.equipmentColumn.getEquipmentIds();

		long[] epIds = new long[this.equipmentIds.length];
		for (int i = 0; i < epIds.length; i++) {
			epIds[i] = -1;
		}
		this.equipmentColumn.setEquipmentIds(epIds);
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < equipmentIds.length; j++) {
			long id = equipmentIds[j];
			sb.append("id:" + id);
			if (id < 0) continue;
			ArticleEntity ae = aem.getEntity(id);
			if (ae != null) {
				if (ae instanceof EquipmentEntity) {
					try {
						this.putOn((EquipmentEntity) ae, true);
					} catch (Exception e) {
						logger.error("[初始化坐骑异常] [穿装备异常] [装备Id:" + id + "] [" + this.getLogString() + "] [" + owner.getLogString() + "]", e);
					}
				} else {
					this.equipmentColumn.getEquipmentIds()[j] = id;
					logger.error(" [初始化坐骑错误] [" + id + "不是装备]  [" + this.getLogString() + "] [" + owner.getLogString() + "]");
				}
			} else {
				this.equipmentColumn.getEquipmentIds()[j] = id;
				logger.error("[初始化坐骑错误] [" + id + "不存在]  [" + this.getLogString() + "] [" + owner.getLogString() + "]");
			}
		}

		HorseManager.em.notifyFieldChange(this, "equipmentColumn");

		if (logger.isWarnEnabled()) logger.warn("[初始化坐骑] [穿装备成功] [装备id:" + sb.toString() + "] [" + this.getLogString() + "] [" + owner.getLogString() + "]");

		if (this.fashionId > 0) {
			ArticleEntity ae = aem.getEntity(fashionId);
			if (ae != null) {
				Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
				if (a != null && a instanceof AvataProps) {
					this.putOnFashion(ae, true);
					if (logger.isWarnEnabled()) logger.warn("[初始化坐骑] [穿时装成功] [时装id:" + fashionId + "] [" + this.getLogString() + "] [" + owner.getLogString() + "]");
				} else {
					logger.error(" [初始化坐骑时装错误] [" + fashionId + "不存在] []");
				}
			} else {
				logger.error("[初始化坐骑时装错误] [不是时装道具] [id:" + fashionId + "] []");
			}
		}
	}

	private void clearAll() {

		lastfull = false;
		equipmentUpdate = false;
		// 所有属性

		maxHP = 0;
		maxHPA = 0;
		maxHPB = 0;
		maxHPC = 0;
		phyAttack = 0;
		phyAttackA = 0;
		phyAttackB = 0;
		phyAttackC = 0;
		magicAttack = 0;
		magicAttackA = 0;
		magicAttackB = 0;
		magicAttackC = 0;
		phyDefence = 0;
		phyDefenceA = 0;
		phyDefenceB = 0;
		phyDefenceC = 0;
		magicDefence = 0;
		magicDefenceA = 0;
		magicDefenceB = 0;
		magicDefenceC = 0;
		maxMP = 0;
		maxMPA = 0;
		maxMPB = 0;
		maxMPC = 0;
		breakDefence = 0;
		breakDefenceA = 0;
		breakDefenceB = 0;
		breakDefenceC = 0;
		accurate = 0;
		accurateA = 0;
		accurateB = 0;
		accurateC = 0;
		criticalDefence = 0;
		criticalDefenceA = 0;
		criticalDefenceB = 0;
		criticalDefenceC = 0;
		criticalHit = 0;
		criticalHitA = 0;
		criticalHitB = 0;
		criticalHitC = 0;
		hit = 0;
		hitA = 0;
		hitB = 0;
		hitC = 0;
		dodge = 0;
		dodgeA = 0;
		dodgeB = 0;
		dodgeC = 0;
		blizzardAttack = 0;
		blizzardAttackA = 0;
		blizzardAttackB = 0;
		blizzardAttackC = 0;
		blizzardDefence = 0;
		blizzardDefenceA = 0;
		blizzardDefenceB = 0;
		blizzardDefenceC = 0;
		fireAttack = 0;
		fireAttackA = 0;
		fireAttackB = 0;
		fireAttackC = 0;
		fireDefence = 0;
		fireDefenceA = 0;
		fireDefenceB = 0;
		fireDefenceC = 0;
		windAttack = 0;
		windAttackA = 0;
		windAttackB = 0;
		windAttackC = 0;
		windDefence = 0;
		windDefenceA = 0;
		windDefenceB = 0;
		windDefenceC = 0;
		thunderAttack = 0;
		thunderAttackA = 0;
		thunderAttackB = 0;
		thunderAttackC = 0;
		thunderDefence = 0;
		thunderDefenceA = 0;
		thunderDefenceB = 0;
		thunderDefenceC = 0;
		windIgnoreDefence = 0;
		windIgnoreDefenceA = 0;
		windIgnoreDefenceB = 0;
		windIgnoreDefenceC = 0;
		thunderIgnoreDefence = 0;
		thunderIgnoreDefenceA = 0;
		thunderIgnoreDefenceB = 0;
		thunderIgnoreDefenceC = 0;
		fireIgnoreDefence = 0;
		fireIgnoreDefenceA = 0;
		fireIgnoreDefenceB = 0;
		fireIgnoreDefenceC = 0;
		blizzardIgnoreDefence = 0;
		blizzardIgnoreDefenceA = 0;
		blizzardIgnoreDefenceB = 0;
		blizzardIgnoreDefenceC = 0;
		if (this.oldBasicAttr != null) {
			this.oldBasicAttr = null;
		}

	}

	private long fashionId = -1;

	public long getFashionId() {
		return fashionId;
	}

	public void setFashionId(long fashionId) {
		this.fashionId = fashionId;
		HorseManager.em.notifyFieldChange(this, "fashionId");
	}

	public void putOnFashion(ArticleEntity ae, boolean init) {
		boolean bln = true;

		Player p;
		synchronized (this.owner.tradeKey) {
			try {
				p = this.owner;
				Article article = ArticleManager.getInstance().getArticle(ae.getArticleName());

				if (!init) {
					if (this.fashionId > 0) {
						bln = this.takeOffFashion(false);
					}
					if (!bln) {
						if (logger.isWarnEnabled()) logger.warn("[坐骑穿时装错误] [包裹空间不足] [" + (owner != null ? owner.getLogString() : "null") + "]");
						return;
					}
					ArticleEntity aee = p.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "坐骑穿装备", false);
					if (aee == null) {
						String description = Translate.删除物品不成功;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						p.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[坐骑穿装备] [" + description + "] [id:" + ae.getId() + "]");
						return;
					}
				}
				if (article instanceof AvataProps) {
					int[] values = ((AvataProps) article).getValues();
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							int value = values[i];
							if (value != 0) {
								if (p.isIsUpOrDown() && p.getRidingHorseId() == this.horseId) {
									float index = (float) (this.getLastEnergyIndex() * 0.1);
									switch (i) {
									case 0:
										p.setPhyAttackB(p.getPhyAttackB() - (int) (this.getPhyAttack() * index));
										this.setPhyAttackC(this.getPhyAttackC() + value);
										p.setPhyAttackB(p.getPhyAttackB() + (int) (this.getPhyAttack() * index));
										break;
									case 1:
										p.setMagicAttackB(p.getMagicAttackB() - (int) (this.getMagicAttack() * index));
										this.setMagicAttackC(this.getMagicAttackC() + value);
										p.setMagicAttackB(p.getMagicAttackB() + (int) (this.getMagicAttack() * index));
										break;
									case 2:
										p.setPhyDefenceB(p.getPhyDefenceB() - (int) (this.getPhyDefence() * index));
										this.setPhyDefenceC(this.getPhyDefenceC() + value);
										p.setPhyDefenceB(p.getPhyDefenceB() + (int) (this.getPhyDefence() * index));
										break;
									case 3:
										p.setMagicDefenceB(p.getMagicDefenceB() - (int) (this.getMagicDefence() * index));
										this.setMagicDefenceC(this.getMagicDefenceC() + value);
										p.setMagicDefenceB(p.getMagicDefenceB() + (int) (this.getMagicDefence() * index));
										break;
									case 4:
										p.setMaxHPB(p.getMaxHPB() - (int) (this.getMaxHP() * index));
										this.setMaxHPC(this.getMaxHPC() + value);
										p.setMaxHPB(p.getMaxHPB() + (int) (this.getMaxHP() * index));
										break;
									case 5:
										p.setCriticalHitB(p.getCriticalHitB() - (int) (this.getCriticalHit() * index));
										this.setCriticalHitC(this.getCriticalHitC() + value);
										p.setCriticalHitB(p.getCriticalHitB() + (int) (this.getCriticalHit() * index));
										break;
									}

								} else {
									switch (i) {
									case 0:
										this.setPhyAttackC(this.getPhyAttackC() + value);
										break;
									case 1:
										this.setMagicAttackC(this.getMagicAttackC() + value);
										break;
									case 2:
										this.setPhyDefenceC(this.getPhyDefenceC() + value);
										break;
									case 3:
										this.setMagicDefenceC(this.getMagicDefenceC() + value);
										break;
									case 4:
										this.setMaxHPC(this.getMaxHPC() + value);
										break;
									case 5:
										this.setCriticalHitC(this.getCriticalHitC() + value);
										break;
									}
								}

							}
						}
					}
					setFashionId(ae.getId());
					this.equipmentUpdate = true;

					// if(!init){
					// ResourceManager.getInstance().getAvata(p);
					// }
				} else {
					if (logger.isWarnEnabled()) logger.warn("[坐骑穿时装错误] [指定物品不是时装] [" + (owner != null ? owner.getLogString() : "null") + "] [" + ae.getId() + "]");
				}
			} catch (Exception e) {
				logger.error("[坐骑穿时装异常] [" + (owner != null ? owner.getLogString() : "null") + "]", e);
			}
		}
	}

	/**
	 * 
	 * @param destroy
	 *            (true 不放入背包)
	 * @return
	 */
	public boolean takeOffFashion(boolean destroy) {
		if (this.fashionId < 0) {
			if (logger.isWarnEnabled()) logger.warn("[坐骑脱时装错误] [坐骑还没有时装] [" + (owner != null ? owner.getLogString() : "null") + "]");
		} else {
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleEntity ae = aem.getEntity(fashionId);
			if (ae != null) {
				Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
				if (a instanceof AvataProps) {
					// 判断包裹空间
					try {
						// Player player = PlayerManager.getInstance().getPlayer(this.getOwnerId());
						Player p = this.owner;
						Knapsack knapsack = p.getKnapsack_common();
						int empty = knapsack.getEmptyNum();
						if (empty <= 0) {
							p.send_HINT_REQ(包裹空间不够);
							return false;
						} else {
							if (!destroy) {
								p.putToKnapsacks(ae, "坐骑");
							}
							int[] values = ((AvataProps) a).getValues();
							if (values != null) {
								for (int i = 0; i < values.length; i++) {
									int value = values[i];
									if (value != 0) {
										if (p.isIsUpOrDown() && p.getRidingHorseId() == this.horseId) {
											float index = (float) (this.getLastEnergyIndex() * 0.1);
											switch (i) {
											case 0:
												p.setPhyAttackB(p.getPhyAttackB() - (int) (this.getPhyAttack() * index));
												this.setPhyAttackC(this.getPhyAttackC() - value);
												p.setPhyAttackB(p.getPhyAttackB() + (int) (this.getPhyAttack() * index));
												break;
											case 1:
												p.setMagicAttackB(p.getMagicAttackB() - (int) (this.getMagicAttack() * index));
												this.setMagicAttackC(this.getMagicAttackC() - value);
												p.setMagicAttackB(p.getMagicAttackB() + (int) (this.getMagicAttack() * index));
												break;
											case 2:
												p.setPhyDefenceB(p.getPhyDefenceB() - (int) (this.getPhyDefence() * index));
												this.setPhyDefenceC(this.getPhyDefenceC() - value);
												p.setPhyDefenceB(p.getPhyDefenceB() + (int) (this.getPhyDefence() * index));
												break;
											case 3:
												p.setMagicDefenceB(p.getMagicDefenceB() - (int) (this.getMagicDefence() * index));
												this.setMagicDefenceC(this.getMagicDefenceC() - value);
												p.setMagicDefenceB(p.getMagicDefenceB() + (int) (this.getMagicDefence() * index));
												break;
											case 4:
												p.setMaxHPB(p.getMaxHPB() - (int) (this.getMaxHP() * index));
												this.setMaxHPC(this.getMaxHPC() - value);
												p.setMaxHPB(p.getMaxHPB() + (int) (this.getMaxHP() * index));
												break;
											case 5:
												p.setCriticalHitB(p.getCriticalHitB() - (int) (this.getCriticalHit() * index));
												this.setCriticalHitC(this.getCriticalHitC() - value);
												p.setCriticalHitB(p.getCriticalHitB() + (int) (this.getCriticalHit() * index));
												break;
											}

										} else {
											switch (i) {
											case 0:
												this.setPhyAttackC(this.getPhyAttackC() - value);
												break;
											case 1:
												this.setMagicAttackC(this.getMagicAttackC() - value);
												break;
											case 2:
												this.setPhyDefenceC(this.getPhyDefenceC() - value);
												break;
											case 3:
												this.setMagicDefenceC(this.getMagicDefenceC() - value);
												break;
											case 4:
												this.setMaxHPC(this.getMaxHPC() - value);
												break;
											case 5:
												this.setCriticalHitC(this.getCriticalHitC() - value);
												break;
											}
										}

									}
								}
							}
							this.setFashionId(-1);
							this.equipmentUpdate = true;
							// ResourceManager.getInstance().getAvata(player);
						}
					} catch (Exception e) {
						logger.error("[脱时装] []", e);
					}
				} else {
					if (logger.isWarnEnabled()) logger.warn("[坐骑脱时装错误] [指定的物品不是坐骑时装] [" + (owner != null ? owner.getLogString() : "null") + "]");
				}
			} else {
				this.setFashionId(-1);
				this.equipmentUpdate = true;
				if (logger.isWarnEnabled()) logger.warn("[坐骑脱时装错误] [指定的物品为null] [" + (owner != null ? owner.getLogString() : "null") + "]");
			}
		}
		return true;
	}

	/**
	 * 坐骑穿装备
	 * 
	 * @param equipment
	 * @param bln
	 *            是否是初始化true 是 false 不是(初始化不删除此装备，不添加原有的装备 )
	 */
	public synchronized void putOn(EquipmentEntity ee, boolean init) throws Exception {

		if (!this.fight) {
			owner.send_HINT_REQ(非战斗坐骑不能穿装备);
			return;
		}
		synchronized (owner.tradeKey) {
			try {
				ArticleManager am = ArticleManager.getInstance();
				Equipment e = (Equipment) am.getArticle(ee.getArticleName());
				Knapsack knapsack = owner.getKnapsack_common();
				this.setHorseLevel(owner.getSoulLevel());
				String result = e.canUse(this);
				if (result == null) {
					// 坐骑穿装备
					if (this.equipmentColumn == null) {
						this.equipmentColumn = new HorseEquipmentColumn();
						this.equipmentColumn.init();
					}

					this.equipmentColumn.setHorse(this);
					if (init) {
						// this.equipmentIds = this.equipmentColumn.equipmentIds;
						EquipmentEntity oldEE = this.equipmentColumn.putOn(ee);

					} else {
						EquipmentEntity oldEE = this.equipmentColumn.putOn(ee);
						this.equipmentIds = this.equipmentColumn.getEquipmentIds();

						ArticleEntity temp = owner.removeArticleEntityFromKnapsackByArticleId(ee.getId(), "坐骑穿装备", false);
						if (oldEE != null) {
							knapsack.put(oldEE, "坐骑");
							// logger.warn("[坐骑穿装备成功] ["+this.owner.getLogString()+"] ["+this.getLogString()+"] [装备Id:"+ee.getId()+"]");
						}

					}
					setLastUpdateTime(SystemTime.currentTimeMillis());
					if (!init) {
						owner.send_HINT_REQ(Translate.translateString(Translate.你已经将装备穿到xx上, new String[][] { { Translate.STRING_1, this.getHorseName() } }));
					}
					boolean winAchieve = true;
					if (this.getEquipmentColumn().getEquipmentIds() != null) {
						for (long eqId : this.getEquipmentColumn().getEquipmentIds()) {
							if (eqId <= 0) {
								winAchieve = false;
							}
						}
					}
					if (winAchieve) {
						AchievementManager.getInstance().record(owner, RecordAction.获得坐骑装备套数);
						if (AchievementManager.logger.isWarnEnabled()) {
							AchievementManager.logger.warn("[成就统计获得坐骑装备套数] [" + owner.getLogString() + "]");
						}
					}
					if (logger.isWarnEnabled()) {
						logger.warn("[坐骑穿装备成功] [" + this.owner.getLogString() + "] [" + this.getLogString() + "] [装备Id:" + ee.getId() + "]");
					}
				} else {
					owner.send_HINT_REQ(result);
					if (logger.isDebugEnabled()) {
						logger.debug("[坐骑穿装备不允许] [" + this.owner.getLogString() + "] [" + result + "]");
					}
					if (init) {
						Article a = am.getArticle(ee.getArticleName());
						if (a != null && a instanceof Equipment) {
							Equipment e1 = (Equipment) a;
							int index = e1.getEquipmentType() - 10;
							equipmentIds[index] = ee.getId();
							setEquipmentIds(equipmentIds);
							this.equipmentColumn.getEquipmentIds()[index] = ee.getId();
							this.equipmentColumn.setEquipmentIds(equipmentIds);
							logger.warn("[新坐骑初始化装备] [失败] [还原原有装备栏装备] [成功] [index:" + index + "] [equipmentId:" + ee.getId() + "] [" + this.owner.getLogString() + "]");
						} else {
							logger.warn("[坐骑初始化穿装备] [失败] [原因:" + result + "] [" + this.getLogString() + "] [" + this.owner.getLogString() + "] [装备id:" + ee.getId() + "]");
						}
					}
				}
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[坐骑穿装备异常] [" + this.owner.getLogString() + "] []", e);
			}
		}
	}

	/**
	 * 坐骑脱装备
	 * 
	 * @param equipment
	 * @param bln
	 *            是否正在骑乘的马换装备
	 */
	public synchronized void takeOff(int index) {

		if (!this.fight) {
			return;
		}
		Player owner = null;
		try {
			owner = PlayerManager.getInstance().getPlayer(this.getOwnerId());
		} catch (Exception e) {
			HorseManager.logger.error("[坐骑脱装备] [" + this.getLogString() + "]", e);
			return;
		}
		Knapsack knapsack = owner.getKnapsack_common();
		if (knapsack == null) {
			return;
		}
		if (knapsack.isFull()) {
			HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.背包已经满了请先腾出空格来再卸载装备);
			owner.addMessageToRightBag(error);
			if (logger.isWarnEnabled()) logger.warn("[坐骑脱装备] [背包已满] [部位:" + index + "] [" + owner.getLogString() + "] [" + this.getLogString() + "]");
		} else {
			// 坐骑脱装备
			EquipmentEntity ee = null;
			try {
				if (this.equipmentColumn == null) {
					equipmentColumn = new HorseEquipmentColumn();
					equipmentColumn.init();
				}
				ee = this.equipmentColumn.takeOff(index);
				this.equipmentIds = this.equipmentColumn.getEquipmentIds();
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[坐骑脱装备异常] [" + owner.getLogString() + "] [" + this.getLogString() + "]", e);
			}
			if (ee != null) {
				knapsack.put(ee, "坐骑");
				if (logger.isWarnEnabled()) logger.warn("[坐骑脱装备成功] [" + this.getLogString() + "] [" + owner.getLogString() + "] [装备Id:" + ee.getId() + "] [装备位置:" + index + "]");
			} else {
				if (logger.isWarnEnabled()) logger.warn("[坐骑脱装备失败] [" + this.getLogString() + "] [" + owner.getLogString() + "] [装备为null] [装备位置:" + index + "]");
			}
		}
		this.equipmentUpdate = true;
		setLastUpdateTime(SystemTime.currentTimeMillis());
	}

	/**
	 * 上坐骑 根据精力值按百分比加属性
	 * 
	 * @param player
	 */
	public void upHorse(Player player) {
		if (this.fly) {
			player.setFlying(true);
		}
		if (holdOnTime != 0) {
			startHeartBeatTime -= holdOnTime;
		}
		player.addHorseProperty(this);

	}

	/**
	 * 下坐骑 根据精力值按百分比减人物属性
	 * 
	 * @param player
	 */
	public void downHorse(Player player) {

		if (this.fly) {
			player.setFlying(false);
		}
		long current = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		setHoldOnTime(current - startHeartBeatTime);

		player.removeHorseProperty(this);

	}

	public transient boolean isFeed;

	/**
	 * 喂养坐骑，改变体力值，把现在的体力值属于的index返回
	 * @param a
	 */
	public int feed(Article a) {
		if (!this.fight) return -1;
		int old = this.getEnergy();
		((HorseFoodArticle) a).addProperty(this);
		if (owner != null && this.energy <= owner.autoFeedLine) {
			EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.AUTO_FEED_HORSE, new Object[] { owner.getId(), this.getHorseId() });
			EventRouter.getInst().addEvent(evt3);
		}
		if (logger.isWarnEnabled()) {
			logger.warn("[坐骑喂养成功] [" + (owner != null ? owner.getLogString() : null) + "] [old:" + old + "] [now:" + this.getEnergy() + "]");
		}
		isFeed = true;
		if (energy >= Horse.PART4NUM) {
			return Horse.LEVEL5RATE;
		} else {
			if (energy >= Horse.PART3NUM) {
				return Horse.LEVEL4RATE;
			} else {
				if (energy >= Horse.PART2NUM) {
					return Horse.LEVEL3RATE;
				} else {
					return Horse.LEVEL2RATE;
					/*
					 * if(energy >= Horse.PART1NUM){
					 * return Horse.LEVEL2RATE;
					 * }else{
					 * if(energy >= Horse.PART0NUM){
					 * return Horse.LEVEL1RATE;
					 * }else{
					 * return Horse.LEVEL0RATE;
					 * }
					 * }
					 */
				}
			}
		}
	}

	/**
	 * 改变体力值 ，并根据上一阶体力值修改人物属性
	 * 
	 * @param player
	 */
	public void heartBeat(Player player) {
		try {
			if (!this.fight) return;
			if (player.getState() == Player.STATE_DEAD) return;
			long currentTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if (currentTime - startHeartBeatTime >= INTERVALTIME || equipmentUpdate || isFeed) {
				if (equipmentUpdate) {
					equipmentUpdate = false;
				} else if (isFeed) {
					isFeed = false;
				} else {
					// 时间到
					startHeartBeatTime = currentTime;
					energy -= DOWNNUM;
					if (energy <= 0) {
						energy = 0;
					}
					setEnergy(energy);
					try {
						if (player.autoFeedHorse && player.autoFeedLine >= 0) {
							if (this.energy <= player.autoFeedLine) {
								EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.AUTO_FEED_HORSE, new Object[] { player.getId(), this.getHorseId() });
								EventRouter.getInst().addEvent(evt3);
							}
						}
					} catch (Exception e) {
						logger.warn("[坐骑修改] [坐骑体力减少通知事件自动购买使用物品] [异常] [" + player.getLogString() + "] [" + this.getLogString() + "]", e);
					}
				}
				if (energy >= PART4NUM) {
					// 饱和
					// 设置马饱和度 马属性
					if (!lastfull) {
						lastfull = true;
						energyIndexUpdate(LEVEL5RATE);
					}
				} else {
					lastfull = false;
					if (energy >= PART3NUM) {
						// 饱和90%
						// 设置马饱和度 马属性
						energyIndexUpdate(LEVEL4RATE);
					} else {
						if (energy >= PART2NUM) {
							// 饱和80%
							energyIndexUpdate(LEVEL3RATE);
						} else {
							energyIndexUpdate(LEVEL2RATE);
							/*
							 * if (energy >= PART1NUM) {
							 * // 饱和70%
							 * energyIndexUpdate(LEVEL2RATE);
							 * } else {
							 * if (energy >= PART0NUM) {
							 * // 饱和60%
							 * energyIndexUpdate(LEVEL1RATE);
							 * } else {
							 * // 饱和50%
							 * energyIndexUpdate(LEVEL0RATE);
							 * }
							 * }
							 */
						}
					}
				}
			}
		} catch (Exception e) {
			HorseManager.logger.error("[坐骑心跳错误] [主人id:" + this.ownerId + "] [" + this.getLogString() + "]", e);
		}
	}

	public synchronized void energyIndexUpdate(int nextIndex) {

		if (nextIndex != this.lastEnergyIndex) {
			Player owner = null;
			try {
				owner = PlayerManager.getInstance().getPlayer(this.getOwnerId());
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[马匹体力改变异常] [" + owner.getLogString() + "] [" + this.getLogString() + "]", e);
				return;
			}
			owner.removeHorseProperty(this);
			this.setLastEnergyIndex(nextIndex);
			owner.addHorseProperty(this);
			this.setLastUpdateTime(SystemTime.currentTimeMillis());
			if (logger.isWarnEnabled()) {
				logger.warn("[马匹体力改变] [" + owner.getLogString() + "] [" + this.getLogString() + "]");
			}
			NOTIFY_HORSE_CHANGE_REQ req = new NOTIFY_HORSE_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), this.horseId, this.energy, this.lastEnergyIndex);
			owner.addMessageToRightBag(req);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[喂养马匹体力比例没变] [" + (owner != null ? owner.getLogString() : "null") + "] [" + this.getLogString() + "]");
			}
		}
	}

	public transient boolean[] selfMarks = new boolean[35];

	/********* 基本属性 血量值 HP= 物理攻击 AP= 法术攻击 AP= 物理防御 AC= 法术防御 AC= **********/

	// 增加的最大生命值
	protected transient int maxHP;
	protected transient int maxHPA;
	protected transient int maxHPB;
	protected transient int maxHPC;

	public int getMaxHP() {
		return maxHP;
	}

	public int getMaxHPA() {
		return maxHPA;
	}

	public int getMaxHPB() {
		return maxHPB;
	}

	public int getMaxHPC() {
		return maxHPC;
	}

	public void setMaxHP(int maxHP) {
		int old = this.maxHP;
		this.maxHP = maxHP;
		selfMarks[0] = true;
		if (this.maxHP < 0) {		//2182646   390646   896000
			try {
				throw new Exception();
			} catch (Exception e) {
				HorseManager.logger.warn("[血量为负] [" + this.getHorseId() + "] [ownerId:" + this.getOwnerId() + "]"
						+ " [" + old + "->" + this.getMaxHP() + "] [a:" + this.getMaxHPA() + "] [b:" + this.getMaxHPB() + "] [c:" + this.getMaxHPC() + "]", e);
			}
		}
	}

	public void setMaxHPA(int value) {
		this.maxHPA = value;
		//this.setMaxHP((this.maxHPA + this.maxHPB) * (100 + this.maxHPC) / 100);
		this.setMaxHP((int) ((this.maxHPA + this.maxHPB) * (float)((100 + this.maxHPC) / 100)));
	}

	public void setMaxHPB(int value) {
		this.maxHPB = value;
		this.setMaxHP((int) ((this.maxHPA + this.maxHPB) * (float)((100 + this.maxHPC) / 100)));
	}

	public void setMaxHPC(int value) {
		this.maxHPC = value;
//		this.setMaxHP((this.maxHPA + this.maxHPB) * (100 + this.maxHPC) / 100);
		this.setMaxHP((int) ((this.maxHPA + this.maxHPB) * (float)((100 + this.maxHPC) / 100)));
	}

	// 近战攻击强度
	protected transient int phyAttack;
	protected transient int phyAttackA;
	protected transient int phyAttackB;
	protected transient int phyAttackC;

	public void setPhyAttack(int value) {
		this.phyAttack = value;
		selfMarks[1] = true;
	}

	public int getPhyAttack() {
		return this.phyAttack;
	}

	public void setPhyAttackA(int value) {
		this.phyAttackA = value;
		this.setPhyAttack((this.phyAttackA + this.phyAttackB) * (100 + this.phyAttackC) / 100);
	}

	public int getPhyAttackA() {
		return this.phyAttackA;
	}

	public void setPhyAttackB(int value) {
		this.phyAttackB = value;
		this.setPhyAttack((this.phyAttackA + this.phyAttackB) * (100 + this.phyAttackC) / 100);
	}

	public int getPhyAttackB() {
		return this.phyAttackB;
	}

	public void setPhyAttackC(int value) {
		this.phyAttackC = value;
		this.setPhyAttack((this.phyAttackA + this.phyAttackB) * (100 + this.phyAttackC) / 100);
	}

	public int getPhyAttackC() {
		return this.phyAttackC;
	}

	// 法术攻击强度
	protected transient int magicAttack;
	protected transient int magicAttackA;
	protected transient int magicAttackB;
	protected transient int magicAttackC;

	public void setMagicAttack(int value) {
		this.magicAttack = value;
		selfMarks[2] = true;
	}

	public int getMagicAttack() {
		return this.magicAttack;
	}

	public void setMagicAttackA(int value) {
		this.magicAttackA = value;
		this.setMagicAttack((this.magicAttackA + this.magicAttackB) * (this.magicAttackC + 100) / 100);
	}

	public int getMagicAttackA() {
		return this.magicAttackA;
	}

	public void setMagicAttackB(int value) {
		this.magicAttackB = value;
		this.setMagicAttack((this.magicAttackA + this.magicAttackB) * (this.magicAttackC + 100) / 100);
	}

	public int getMagicAttackB() {
		return this.magicAttackB;
	}

	public void setMagicAttackC(int value) {
		this.magicAttackC = value;
		this.setMagicAttack((this.magicAttackA + this.magicAttackB) * (this.magicAttackC + 100) / 100);
	}

	public int getMagicAttackC() {
		return this.magicAttackC;
	}

	// 物理防御
	protected transient int phyDefence;
	protected transient int phyDefenceA;
	protected transient int phyDefenceB;
	protected transient int phyDefenceC;

	public void setPhyDefence(int value) {
		this.phyDefence = value;
		selfMarks[3] = true;
	}

	public int getPhyDefence() {
		return this.phyDefence;
	}

	public void setPhyDefenceA(int value) {
		this.phyDefenceA = value;
		this.setPhyDefence((this.phyDefenceA + this.phyDefenceB) * (this.phyDefenceC + 100) / 100);
	}

	public int getPhyDefenceA() {
		return this.phyDefenceA;
	}

	public void setPhyDefenceB(int value) {
		this.phyDefenceB = value;
		this.setPhyDefence((this.phyDefenceA + this.phyDefenceB) * (this.phyDefenceC + 100) / 100);
	}

	public int getPhyDefenceB() {
		return this.phyDefenceB;
	}

	public void setPhyDefenceC(int value) {
		this.phyDefenceC = value;
		this.setPhyDefence((this.phyDefenceA + this.phyDefenceB) * (this.phyDefenceC + 100) / 100);
	}

	public int getPhyDefenceC() {
		return this.phyDefenceC;
	}

	// 法术防御力
	protected transient int magicDefence;
	protected transient int magicDefenceA;
	protected transient int magicDefenceB;
	protected transient int magicDefenceC;

	public void setMagicDefence(int value) {
		this.magicDefence = value;
		selfMarks[4] = true;
	}

	public int getMagicDefence() {
		return this.magicDefence;
	}

	public void setMagicDefenceA(int value) {
		this.magicDefenceA = value;
		this.setMagicDefence((this.magicDefenceA + this.magicDefenceB) * (this.magicDefenceC + 100) / 100);
	}

	public int getMagicDefenceA() {
		return this.magicDefenceA;
	}

	public void setMagicDefenceB(int value) {
		this.magicDefenceB = value;
		this.setMagicDefence((this.magicDefenceA + this.magicDefenceB) * (this.magicDefenceC + 100) / 100);
	}

	public int getMagicDefenceB() {
		return this.magicDefenceB;
	}

	public void setMagicDefenceC(int value) {
		this.magicDefenceC = value;
		this.setMagicDefence((this.magicDefenceA + this.magicDefenceB) * (this.magicDefenceC + 100) / 100);
	}

	public int getMagicDefenceC() {
		return this.magicDefenceC;
	}

	/******
	 * 附加属性 法力值 MMP= 破甲值 DAC= 命中值 HITP= 闪躲值 DGP= 精准值 ACTP=
	 * 会心一击 CHP= 会心防御 DCHP= 火属性攻击强度 FAP= 冰属性攻击强度 IAP= 风属性攻击强度 WAP=
	 * 雷属性攻击强度 TAP= 火属性抗性 FRT= 冰属性抗性 IRT= 风属性抗性 WRT= 雷属性抗性 TRT=
	 ***********/
	// 增加的最大魔法值
	protected transient int maxMP;
	protected transient int maxMPA;
	protected transient int maxMPB;
	protected transient int maxMPC;

	public void setMaxMP(int maxMP) {
		this.maxMP = maxMP;
		selfMarks[5] = true;
	}

	public void setMaxMPA(int value) {
		this.maxMPA = value;
		this.setMaxMP((this.maxMPA + this.maxMPB) * (100 + this.maxMPC) / 100);
	}

	public void setMaxMPB(int value) {
		this.maxMPB = value;
		this.setMaxMP((this.maxMPA + this.maxMPB) * (100 + this.maxMPC) / 100);
	}

	public void setMaxMPC(int value) {
		this.maxMPC = value;
		this.setMaxMP((this.maxMPA + this.maxMPB) * (100 + this.maxMPC) / 100);
	}

	public int getMaxMP() {
		return maxMP;
	}

	public int getMaxMPA() {
		return maxMPA;
	}

	public int getMaxMPB() {
		return maxMPB;
	}

	public int getMaxMPC() {
		return maxMPC;
	}

	// 破防
	protected transient int breakDefence;
	protected transient int breakDefenceA;
	protected transient int breakDefenceB;
	protected transient int breakDefenceC;

	public void setBreakDefence(int value) {
		breakDefence = (value);
		selfMarks[6] = true;
	}

	public void setBreakDefenceA(int value) {
		this.breakDefenceA = value;
		this.setBreakDefence((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100);
	}

	public void setBreakDefenceB(int value) {
		this.breakDefenceB = value;
		this.setBreakDefence((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100);
	}

	public void setBreakDefenceC(int value) {
		this.breakDefenceC = value;
		this.setBreakDefence((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100);
	}

	public int getBreakDefence() {
		return breakDefence;
	}

	public int getBreakDefenceA() {
		return breakDefenceA;
	}

	public int getBreakDefenceB() {
		return breakDefenceB;
	}

	public int getBreakDefenceC() {
		return breakDefenceC;
	}

	// 精准
	protected transient int accurate;
	protected transient int accurateA;
	protected transient int accurateB;
	protected transient int accurateC;

	public int getAccurate() {
		return accurate;
	}

	public void setAccurate(int accurate) {
		this.accurate = accurate;
		selfMarks[7] = true;
	}

	public void setAccurateA(int value) {
		this.accurateA = value;
		this.setAccurate((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100);
	}

	public void setAccurateB(int value) {
		this.accurateB = value;
		this.setAccurate((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100);
	}

	public void setAccurateC(int value) {
		this.accurateC = value;
		this.setAccurate((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100);
	}

	public int getAccurateA() {
		return accurateA;
	}

	public int getAccurateB() {
		return accurateB;
	}

	public int getAccurateC() {
		return accurateC;
	}

	// 会心防御
	protected transient int criticalDefence;
	protected transient int criticalDefenceA;
	protected transient int criticalDefenceB;
	protected transient int criticalDefenceC;

	public void setCriticalDefence(int value) {
		this.criticalDefence = value;
		selfMarks[8] = true;
	}

	public void setCriticalDefenceA(int value) {
		this.criticalDefenceA = value;
		this.setCriticalDefence((this.criticalDefenceA + this.criticalDefenceB) * (100 + this.criticalDefenceC) / 100);
	}

	public void setCriticalDefenceB(int value) {
		this.criticalDefenceB = value;
		this.setCriticalDefence((this.criticalDefenceA + this.criticalDefenceB) * (100 + this.criticalDefenceC) / 100);
	}

	public void setCriticalDefenceC(int value) {
		this.criticalDefenceC = value;
		this.setCriticalDefence((this.criticalDefenceA + this.criticalDefenceB) * (100 + this.criticalDefenceC) / 100);
	}

	public int getCriticalDefence() {
		return criticalDefence;
	}

	public int getCriticalDefenceA() {
		return criticalDefenceA;
	}

	public int getCriticalDefenceB() {
		return criticalDefenceB;
	}

	public int getCriticalDefenceC() {
		return criticalDefenceC;
	}

	// 会心一击，也叫暴击
	protected transient int criticalHit;
	protected transient int criticalHitA;
	protected transient int criticalHitB;
	protected transient int criticalHitC;

	public void setCriticalHit(int value) {
		this.criticalHit = value;
		selfMarks[9] = true;
	}

	public void setCriticalHitA(int value) {
		this.criticalHitA = value;
		this.setCriticalHit((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100);
	}

	public void setCriticalHitB(int value) {
		this.criticalHitB = value;
		this.setCriticalHit((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100);
	}

	public void setCriticalHitC(int value) {
		this.criticalHitC = value;
		this.setCriticalHit((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100);
	}

	public int getCriticalHit() {
		return criticalHit;
	}

	public int getCriticalHitA() {
		return criticalHitA;
	}

	public int getCriticalHitB() {
		return criticalHitB;
	}

	public int getCriticalHitC() {
		return criticalHitC;
	}

	protected transient int hit;
	protected transient int hitA;
	protected transient int hitB;
	protected transient int hitC;

	public int getHit() {
		return this.hit;
	}

	public int getHitA() {
		return this.hitA;
	}

	public int getHitB() {
		return this.hitB;
	}

	public int getHitC() {
		return this.hitC;
	}

	public void setHit(int value) {
		this.hit = value;
		selfMarks[10] = true;
	}

	public void setHitA(int value) {
		this.hitA = value;
		this.setHit((this.hitA + this.hitB) * (100 + this.hitC) / 100);
	}

	public void setHitB(int value) {
		this.hitB = value;
		this.setHit((this.hitA + this.hitB) * (100 + this.hitC) / 100);
	}

	public void setHitC(int value) {
		this.hitC = value;
		this.setHit((this.hitA + this.hitB) * (100 + this.hitC) / 100);
	}

	// 回避
	protected transient int dodge;
	protected transient int dodgeA;
	protected transient int dodgeB;
	protected transient int dodgeC;

	public void setDodge(int value) {
		this.dodge = value;
		selfMarks[11] = true;
	}

	public void setDodgeA(int value) {
		this.dodgeA = value;
		this.setDodge((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100);
	}

	public void setDodgeB(int value) {
		this.dodgeB = value;
		this.setDodge((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100);
	}

	public void setDodgeC(int value) {
		this.dodgeC = value;
		this.setDodge((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100);
	}

	public int getDodge() {
		return dodge;
	}

	public int getDodgeA() {
		return dodgeA;
	}

	public int getDodgeB() {
		return dodgeB;
	}

	public int getDodgeC() {
		return dodgeC;
	}

	// 冰攻
	protected transient int blizzardAttack;
	protected transient int blizzardAttackA;
	protected transient int blizzardAttackB;
	protected transient int blizzardAttackC;

	public int getBlizzardAttack() {
		return this.blizzardAttack;
	}

	public int getBlizzardAttackA() {
		return this.blizzardAttackA;
	}

	public int getBlizzardAttackB() {
		return this.blizzardAttackB;
	}

	public int getBlizzardAttackC() {
		return this.blizzardAttackC;
	}

	public void setBlizzardAttack(int value) {
		this.blizzardAttack = value;
		selfMarks[12] = true;
	}

	public void setBlizzardAttackA(int value) {
		this.blizzardAttackA = value;
		this.setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC) / 100);
	}

	public void setBlizzardAttackB(int value) {
		this.blizzardAttackB = value;
		this.setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC) / 100);
	}

	public void setBlizzardAttackC(int value) {
		this.blizzardAttackC = value;
		this.setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC) / 100);
	}

	// 水防
	protected transient int blizzardDefence;
	protected transient int blizzardDefenceA;
	protected transient int blizzardDefenceB;
	protected transient int blizzardDefenceC;

	public int getBlizzardDefenceA() {
		return blizzardDefenceA;
	}

	public int getBlizzardDefenceB() {
		return blizzardDefenceB;
	}

	public int getBlizzardDefenceC() {
		return blizzardDefenceC;
	}

	public void setBlizzardDefence(int value) {
		this.blizzardDefence = value;
		selfMarks[13] = true;
	}

	public int getBlizzardDefence() {
		return this.blizzardDefence;
	}

	public void setBlizzardDefenceA(int value) {
		this.blizzardDefenceA = value;
		this.setBlizzardDefence((this.blizzardDefenceA + this.blizzardDefenceB) * (100 + this.blizzardDefenceC) / 100);
	}

	public void setBlizzardDefenceB(int value) {
		this.blizzardDefenceB = value;
		this.setBlizzardDefence((this.blizzardDefenceA + this.blizzardDefenceB) * (100 + this.blizzardDefenceC) / 100);
	}

	public void setBlizzardDefenceC(int value) {
		this.blizzardDefenceC = value;
		this.setBlizzardDefence((this.blizzardDefenceA + this.blizzardDefenceB) * (100 + this.blizzardDefenceC) / 100);
	}

	// 火攻
	protected transient int fireAttack;
	protected transient int fireAttackA;
	protected transient int fireAttackB;
	protected transient int fireAttackC;

	public int getFireAttack() {
		return this.fireAttack;
	}

	public int getFireAttackA() {
		return this.fireAttackA;
	}

	public int getFireAttackB() {
		return this.fireAttackB;
	}

	public int getFireAttackC() {
		return this.fireAttackC;
	}

	public void setFireAttack(int value) {
		this.fireAttack = value;
		selfMarks[15] = true;
	}

	public void setFireAttackA(int value) {
		this.fireAttackA = value;
		this.setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC) / 100);
	}

	public void setFireAttackB(int value) {
		this.fireAttackB = value;
		this.setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC) / 100);
	}

	public void setFireAttackC(int value) {
		this.fireAttackC = value;
		this.setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC) / 100);
	}

	// 金防
	protected transient int fireDefence;
	protected transient int fireDefenceA;
	protected transient int fireDefenceB;
	protected transient int fireDefenceC;

	public int getFireDefence() {
		return fireDefence;
	}

	public int getFireDefenceA() {
		return fireDefenceA;
	}

	public int getFireDefenceB() {
		return fireDefenceB;
	}

	public int getFireDefenceC() {
		return fireDefenceC;
	}

	public void setFireDefence(int fireDefence) {
		this.fireDefence = fireDefence;
		selfMarks[16] = true;
	}

	public void setFireDefenceA(int value) {
		this.fireDefenceA = value;
		this.setFireDefence((this.fireDefenceA + this.fireDefenceB) * (100 + this.fireDefenceC) / 100);
	}

	public void setFireDefenceB(int value) {
		this.fireDefenceB = value;
		this.setFireDefence((this.fireDefenceA + this.fireDefenceB) * (100 + this.fireDefenceC) / 100);
	}

	public void setFireDefenceC(int value) {
		this.fireDefenceC = value;
		this.setFireDefence((this.fireDefenceA + this.fireDefenceB) * (100 + this.fireDefenceC) / 100);
	}

	// 风攻
	protected transient int windAttack;
	protected transient int windAttackA;
	protected transient int windAttackB;
	protected transient int windAttackC;

	public int getWindAttack() {
		return this.windAttack;
	}

	public int getWindAttackA() {
		return this.windAttackA;
	}

	public int getWindAttackB() {
		return this.windAttackB;
	}

	public int getWindAttackC() {
		return this.windAttackC;
	}

	public void setWindAttack(int value) {
		this.windAttack = value;
		selfMarks[18] = true;
	}

	public void setWindAttackA(int value) {
		this.windAttackA = value;
		this.setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC) / 100);

	}

	public void setWindAttackB(int value) {
		this.windAttackB = value;
		this.setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC) / 100);
	}

	public void setWindAttackC(int value) {
		this.windAttackC = value;
		this.setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC) / 100);
	}

	// 火防
	protected transient int windDefence;
	protected transient int windDefenceA;
	protected transient int windDefenceB;
	protected transient int windDefenceC;

	public int getWindDefence() {
		return windDefence;
	}

	public int getWindDefenceA() {
		return windDefenceA;
	}

	public int getWindDefenceB() {
		return windDefenceB;
	}

	public int getWindDefenceC() {
		return windDefenceC;
	}

	public void setWindDefence(int windDefence) {
		this.windDefence = windDefence;
		selfMarks[19] = true;
	}

	public void setWindDefenceA(int value) {
		this.windDefenceA = value;
		this.setWindDefence((this.windDefenceA + this.windDefenceB) * (100 + this.windDefenceC) / 100);
	}

	public void setWindDefenceB(int value) {
		this.windDefenceB = value;
		this.setWindDefence((this.windDefenceA + this.windDefenceB) * (100 + this.windDefenceC) / 100);
	}

	public void setWindDefenceC(int value) {
		this.windDefenceC = value;
		this.setWindDefence((this.windDefenceA + this.windDefenceB) * (100 + this.windDefenceC) / 100);
	}

	// 雷攻
	protected transient int thunderAttack;
	protected transient int thunderAttackA;
	protected transient int thunderAttackB;
	protected transient int thunderAttackC;

	public void setThunderAttack(int value) {
		this.thunderAttack = value;
		selfMarks[21] = true;
	}

	public int getThunderAttack() {
		return this.thunderAttack;
	}

	public void setThunderAttackA(int value) {
		this.thunderAttackA = value;
		this.setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC) / 100);
	}

	public int getThunderAttackA() {
		return this.thunderAttackA;
	}

	public void setThunderAttackB(int value) {
		this.thunderAttackB = value;
		this.setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC) / 100);
	}

	public int getThunderAttackB() {
		return this.thunderAttackB;
	}

	public void setThunderAttackC(int value) {
		this.thunderAttackC = value;
		this.setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC) / 100);
	}

	public int getThunderAttackC() {
		return this.thunderAttackC;
	}

	// 木防
	protected transient int thunderDefence;
	protected transient int thunderDefenceA;
	protected transient int thunderDefenceB;
	protected transient int thunderDefenceC;

	public int getThunderDefence() {
		return thunderDefence;
	}

	public int getThunderDefenceA() {
		return thunderDefenceA;
	}

	public int getThunderDefenceB() {
		return thunderDefenceB;
	}

	public int getThunderDefenceC() {
		return thunderDefenceC;
	}

	public void setThunderDefence(int thunderDefence) {
		this.thunderDefence = thunderDefence;
		selfMarks[22] = true;
	}

	public void setThunderDefenceA(int value) {
		this.thunderDefenceA = value;
		this.setThunderDefence((this.thunderDefenceA + this.thunderDefenceB) * (100 + this.thunderDefenceC) / 100);
	}

	public void setThunderDefenceB(int value) {
		this.thunderDefenceB = value;
		this.setThunderDefence((this.thunderDefenceA + this.thunderDefenceB) * (100 + this.thunderDefenceC) / 100);
	}

	public void setThunderDefenceC(int value) {
		this.thunderDefenceC = value;
		this.setThunderDefence((this.thunderDefenceA + this.thunderDefenceB) * (100 + this.thunderDefenceC) / 100);
	}

	/******** 套装属性 火属性减抗 DFRT= 冰属性减抗 DIRT= 风属性减抗 DWRT= 雷属性减抗 DTRT= ********/
	// 减少对方火防
	protected transient int windIgnoreDefence;
	protected transient int windIgnoreDefenceA;
	protected transient int windIgnoreDefenceB;
	protected transient int windIgnoreDefenceC;

	public void setWindIgnoreDefence(int windIgnoreDefence) {
		this.windIgnoreDefence = windIgnoreDefence;
		selfMarks[20] = true;
	}

	public void setWindIgnoreDefenceA(int value) {
		this.windIgnoreDefenceA = value;
		this.setWindIgnoreDefence((this.windIgnoreDefenceA + this.windIgnoreDefenceB) * (100 + this.windIgnoreDefenceC) / 100);
	}

	public void setWindIgnoreDefenceB(int value) {
		this.windIgnoreDefenceB = value;
		this.setWindIgnoreDefence((this.windIgnoreDefenceA + this.windIgnoreDefenceB) * (100 + this.windIgnoreDefenceC) / 100);
	}

	public void setWindIgnoreDefenceC(int value) {
		this.windIgnoreDefenceC = value;
		this.setWindIgnoreDefence((this.windIgnoreDefenceA + this.windIgnoreDefenceB) * (100 + this.windIgnoreDefenceC) / 100);
	}

	public int getWindIgnoreDefence() {
		return windIgnoreDefence;
	}

	public int getWindIgnoreDefenceA() {
		return windIgnoreDefenceA;
	}

	public int getWindIgnoreDefenceB() {
		return windIgnoreDefenceB;
	}

	public int getWindIgnoreDefenceC() {
		return windIgnoreDefenceC;
	}

	// 减少对方木防
	protected transient int thunderIgnoreDefence;
	protected transient int thunderIgnoreDefenceA;
	protected transient int thunderIgnoreDefenceB;
	protected transient int thunderIgnoreDefenceC;

	public void setThunderIgnoreDefence(int thunderIgnoreDefence) {
		this.thunderIgnoreDefence = thunderIgnoreDefence;
		selfMarks[23] = true;
	}

	public void setThunderIgnoreDefenceA(int value) {
		this.thunderIgnoreDefenceA = value;
		this.setThunderIgnoreDefence((this.thunderIgnoreDefenceA + this.thunderIgnoreDefenceB) * (100 + this.thunderIgnoreDefenceC) / 100);
	}

	public void setThunderIgnoreDefenceB(int value) {
		this.thunderIgnoreDefenceB = value;
		this.setThunderIgnoreDefence((this.thunderIgnoreDefenceA + this.thunderIgnoreDefenceB) * (100 + this.thunderIgnoreDefenceC) / 100);
	}

	public void setThunderIgnoreDefenceC(int value) {
		this.thunderIgnoreDefenceC = value;
		this.setThunderIgnoreDefence((this.thunderIgnoreDefenceA + this.thunderIgnoreDefenceB) * (100 + this.thunderIgnoreDefenceC) / 100);
	}

	public int getThunderIgnoreDefenceA() {
		return thunderIgnoreDefenceA;
	}

	public int getThunderIgnoreDefenceB() {
		return thunderIgnoreDefenceB;
	}

	public int getThunderIgnoreDefenceC() {
		return thunderIgnoreDefenceC;
	}

	public int getThunderIgnoreDefence() {
		return thunderIgnoreDefence;
	}

	// 减少对方金防
	protected transient int fireIgnoreDefence;
	protected transient int fireIgnoreDefenceA;
	protected transient int fireIgnoreDefenceB;
	protected transient int fireIgnoreDefenceC;

	public void setFireIgnoreDefence(int fireIgnoreDefence) {
		this.fireIgnoreDefence = fireIgnoreDefence;
		selfMarks[17] = true;
	}

	public void setFireIgnoreDefenceA(int value) {
		this.fireIgnoreDefenceA = value;
		this.setFireIgnoreDefence((this.fireIgnoreDefenceA + this.fireIgnoreDefenceB) * (100 + this.fireIgnoreDefenceC) / 100);
	}

	public void setFireIgnoreDefenceB(int value) {
		this.fireIgnoreDefenceB = value;
		this.setFireIgnoreDefence((this.fireIgnoreDefenceA + this.fireIgnoreDefenceB) * (100 + this.fireIgnoreDefenceC) / 100);
	}

	public void setFireIgnoreDefenceC(int value) {
		this.fireIgnoreDefenceC = value;
		this.setFireIgnoreDefence((this.fireIgnoreDefenceA + this.fireIgnoreDefenceB) * (100 + this.fireIgnoreDefenceC) / 100);
	}

	public int getFireIgnoreDefence() {
		return fireIgnoreDefence;
	}

	public int getFireIgnoreDefenceA() {
		return fireIgnoreDefenceA;
	}

	public int getFireIgnoreDefenceB() {
		return fireIgnoreDefenceB;
	}

	public int getFireIgnoreDefenceC() {
		return fireIgnoreDefenceC;
	}

	// 减少对方水防
	protected transient int blizzardIgnoreDefence;
	protected transient int blizzardIgnoreDefenceA;
	protected transient int blizzardIgnoreDefenceB;
	protected transient int blizzardIgnoreDefenceC;

	public int getBlizzardIgnoreDefence() {
		return blizzardIgnoreDefence;
	}

	public int getBlizzardIgnoreDefenceA() {
		return blizzardIgnoreDefenceA;
	}

	public int getBlizzardIgnoreDefenceB() {
		return blizzardIgnoreDefenceB;
	}

	public int getBlizzardIgnoreDefenceC() {
		return blizzardIgnoreDefenceC;
	}

	public void setBlizzardIgnoreDefence(int blizzardIgnoreDefence) {
		this.blizzardIgnoreDefence = blizzardIgnoreDefence;
		selfMarks[14] = true;
	}

	public void setBlizzardIgnoreDefenceA(int value) {
		this.blizzardIgnoreDefenceA = value;
		this.setBlizzardIgnoreDefence((this.blizzardIgnoreDefenceA + this.blizzardIgnoreDefenceB) * (100 + this.blizzardIgnoreDefenceC) / 100);
	}

	public void setBlizzardIgnoreDefenceB(int value) {
		this.blizzardIgnoreDefenceB = value;
		this.setBlizzardIgnoreDefence((this.blizzardIgnoreDefenceA + this.blizzardIgnoreDefenceB) * (100 + this.blizzardIgnoreDefenceC) / 100);
	}

	public void setBlizzardIgnoreDefenceC(int value) {
		this.blizzardIgnoreDefenceC = value;
		this.setBlizzardIgnoreDefence((this.blizzardIgnoreDefenceA + this.blizzardIgnoreDefenceB) * (100 + this.blizzardIgnoreDefenceC) / 100);
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
		HorseManager.em.notifyFieldChange(this, "energy");
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}

	public void setMaxEnergy(int energy) {
		this.maxEnergy = energy;
	}

	public int getLastEnergyIndex() {
		return lastEnergyIndex;
	}

	public void setLastEnergyIndex(int lastEnergyIndex) {
		this.lastEnergyIndex = lastEnergyIndex;
		HorseManager.em.notifyFieldChange(this, "lastEnergyIndex");
	}

	public int getSpeed() {
		if (this.isFly()) {
			return 300;
		}
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		this.selfMarks[29] = true;
	}

	public int getRank() {
		if (this.isFly()) {
			return 6;
		}
		return this.otherData.getRankStar();
	}

	public static transient boolean isDebug = true;

	public void setRank(int rank) {
		this.rank = rank;
		this.selfMarks[28] = true;
		if (!fly) {
			HorseRankModel hrm = Horse2Manager.instance.rankModelMap.get(rank);
			String avata = "";
			String mavata = "";
			if(hrm != null){
				avata = Arrays.toString(hrm.getAvatar());
				mavata = Arrays.toString(hrm.getMonthAvatar());
			}
			Horse2Manager.logger.warn("[新坐骑系统] [错误:没有获取到数据] [avata:"+avata+"] [mavata:"+mavata+"] [career:"+career+"] [AvataKey():"+this.getAvataKey()+"] [hrm:"+hrm+"] [rank:"+rank+"] ["+(owner!=null?owner.getName():"null")+"] [" + this.horseShowName + "]");
			if (hrm != null && hrm.getOpenSkillNum() > this.skillNum) {
				this.setSkillNum(hrm.getOpenSkillNum());
			}
			if (hrm != null && hrm.getSpeed() > this.speed) {
				this.setSpeed(hrm.getSpeed());
			}
			String oldavata ="";
			if (hrm != null && career > 0) {
				oldavata = hrm.getAvatar()[career - 1];
				if(!isFly()){
					if(owner.ownMonthCard(CardFunction.坐骑进阶形象额外加1)){
						oldavata = hrm.getMonthAvatar()[career - 1];
					}else{
						oldavata = hrm.getAvatar()[career - 1];
					}
					
				}
				if (this.getAvataKey() == null || !this.getAvataKey().equals(oldavata)) {
//					if(owner.ownMonthCard(CardFunction.坐骑进阶形象额外加1)){
//						this.setAvataKey(hrm.getMonthAvatar()[career - 1]);
//					}else{
						this.setAvataKey(oldavata);
//					}
						Horse2Manager.logger.warn("[新坐骑系统] [错误:没有获取到数据2] ");
					this.setNewAvatarKey(hrm.getNewAvatar()[career - 1]);
					if (isDebug) {
						Horse2Manager.logger.warn("[新坐骑系统] [错误:没有获取到数据3] [AvataKey:"+getAvataKey()+"] ["+getAvata()+"]");
						ResourceManager.getInstance().getHorseAvataForPlayer(this, owner);
						Horse2Manager.logger.warn("[新坐骑系统] [错误:没有获取到数据5] [AvataKey:"+getAvataKey()+"] ["+getAvata()+"]");
					}
					this.selfMarks[31] = true;
					this.notifySelfChange();
				}
				if (hrm.getPartical() != null && hrm.getPartical().length >= 4) {
					this.setHorseParticle(hrm.getPartical()[career - 1]);
				} else { // 清粒子
					this.setHorseParticle("");
				}
			}
			if (hrm != null) {
				Horse2Manager.logger.warn("[新坐骑系统] [测试九黎] [oldavata:"+oldavata+"] ["+(owner!=null?owner.getName():"null")+"] [career:"+career+"] ["+hrm.getAvatar()!=null?Arrays.toString(hrm.getAvatar()):"null"+"] [AvataKey:"+this.getAvataKey()+"] [" + this.horseShowName + "]");
			}
			if (hrm != null && career > 0 && career <= hrm.getHorseName().length && !this.horseShowName.equals(hrm.getHorseName()[career - 1])) {
				this.setHorseShowName(hrm.getHorseName()[career - 1]);
			}
		}else{
			Horse2Manager.logger.warn("[新坐骑系统] [错误:是飞行坐骑，不可以] ["+(owner!=null?owner.getName():"null")+"] [" + this.horseShowName + "]");
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public HorseEquipmentColumn getEquipmentColumn() {
		return equipmentColumn;
	}

	public void setEquipmentColumn(HorseEquipmentColumn equipmentColumn) {
		this.equipmentColumn = equipmentColumn;
	}

	public String getHorseName() {
		return horseName;
	}

	public void setHorseName(String horseName) {
		this.horseName = horseName;
		setHorseShowName(horseName);
		HorseManager.em.notifyFieldChange(this, "horseName");
	}

	public long getHorseId() {
		return horseId;
	}

	public void setHorseId(long horseId) {
		this.horseId = horseId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAvata() {
		Article article = ArticleManager.getInstance().getArticle(this.horseName);
		if (article != null && article instanceof HorseProps) {
			HorseProps hp = (HorseProps) article;
			if (hp.isSpecialHorse()) {
				return ResourceManager.partHead + this.getAvataKey() + ResourceManager.partTail;
			}

		}
		return avata;
	}

	public void setAvata(String avata) {
		this.avata = avata;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
		HorseManager.em.notifyFieldChange(this, "lastUpdateTime");
	}

	public int getHorseLevel() {
		return horseLevel;
	}

	public void setHorseLevel(int horseLevel) {
		this.horseLevel = horseLevel;
	}

	public boolean isFight() {
		return fight;
	}

	public void setFight(boolean fight) {
		this.fight = fight;
	}

	public boolean isFly() {
		return fly;
	}

	public void setFly(boolean fly) {
		this.fly = fly;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public long[] getEquipmentIds() {
		return equipmentIds;
	}

	public void setEquipmentIds(long[] equipmentIds) {
		this.equipmentIds = equipmentIds;
	}

	public String getAvataKey() {
		return avataKey;
	}

	public void setAvataKey(String avataKey) {
		this.avataKey = avataKey;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getHoldOnTime() {
		return holdOnTime;
	}

	public void setHoldOnTime(long holdOnTime) {
		this.holdOnTime = holdOnTime;
		HorseManager.em.notifyFieldChange(this, "holdOnTime");
	}

	public String getHorseParticle() {
		return horseParticle;
	}

	public void setHorseParticle(String horseParticle) {
		this.horseParticle = horseParticle;
		this.selfMarks[32] = true;
	}

	public boolean isLimitTime() {
		return isLimitTime;
	}

	public void setLimitTime(boolean isLimitTime) {
		this.isLimitTime = isLimitTime;
		HorseManager.em.notifyFieldChange(this, "isLimitTime");
	}

	// public long get到期时间() {
	// return 到期时间;
	// }
	//
	// public void set到期时间(long 到期时间) {
	// this.到期时间 = 到期时间;
	// HorseManager.em.notifyFieldChange(this, "到期时间");
	// }

	public long getDueTime() {
		return dueTime;
	}

	public void setDueTime(long dueTime) {
		this.dueTime = dueTime;
		HorseManager.em.notifyFieldChange(this, "dueTime");
	}

	public int getBloodStar() {
		return this.bloodStar;
	}

	public void setBloodStar(int bloodStar) {
		this.bloodStar = bloodStar;
		this.selfMarks[25] = true;
	}

	public int getSkillNum() {
		return skillNum;
	}

	public void setSkillNum(int skillNum) {
		this.skillNum = skillNum;
		this.selfMarks[26] = true;
	}

	public HorseOtherData getOtherData() {
		return otherData;
	}

	public void setOtherData(HorseOtherData otherData) {
		this.otherData = otherData;
		HorseManager.em.notifyFieldChange(this, "otherData");
	}

	public int getColorType() {
		return this.otherData.getColorType();
	}

	public void setColorType(int colorType) {
		HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(colorType);
		if (hcm != null && hcm.getGrowUpRate() != this.growRate) {
			this.setGrowRate((int) (hcm.getGrowUpRate() * 100));
		}
		this.colorType = colorType;
		selfMarks[24] = true;
		selfMarks[33] = true;
	}

	public int getGrowRate() {
		return growRate;
	}

	public void setGrowRate(int growRate) {
		this.growRate = growRate;
		selfMarks[27] = true;
	}

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

	public HorseAttrModel getOldBasicAttr() {
		return oldBasicAttr;
	}

	public void setOldBasicAttr(HorseAttrModel oldBasicAttr) {
		this.oldBasicAttr = oldBasicAttr;
	}

	public String getHorseShowName() {
		if (this.isFly()) {
			return horseName;
		}
		return horseShowName;
	}

	public void setHorseShowName(String horseShowName) {
		this.horseShowName = horseShowName;
		this.selfMarks[30] = true;
	}

	public String getNewAvatarKey() {
		return newAvatarKey;
	}

	public void setNewAvatarKey(String newAvatarKey) {
		this.newAvatarKey = newAvatarKey;
	}

	public long[] getHunshiArray() {
		return hunshiArray;
	}

	public void setHunshiArray(long[] hunshiArray) {
		this.hunshiArray = hunshiArray;
		HorseManager.em.notifyFieldChange(this, "hunshiArray");
	}

	public long[] getHunshi2Array() {
		return hunshi2Array;
	}

	public void setHunshi2Array(long[] hunshi2Array) {
		this.hunshi2Array = hunshi2Array;
		HorseManager.em.notifyFieldChange(this, "hunshi2Array");
	}

	public boolean[] getHunshi2Open() {
		return hunshi2Open;
	}

	public void setHunshi2Open(boolean[] hunshi2Open) {
		this.hunshi2Open = hunshi2Open;
		HorseManager.em.notifyFieldChange(this, "hunshi2Open");
	}

	public Hunshi2Cell[] getHunshi2Cells() {
		Hunshi2Cell[] hunshi2Cells = new Hunshi2Cell[16];
		for (int i = 0; i < hunshi2Cells.length; i++) {
			hunshi2Cells[i] = new Hunshi2Cell(i);
		}

		for (int i = 0; i < hunshi2Cells.length; i++) {
			hunshi2Cells[i].setHunshiId(hunshi2Array[i]);
			if (i < 4) { // 第一行四格直接开启
				hunshi2Open[i] = true;
			}
			hunshi2Cells[i].setOpen(hunshi2Open[i]);
		}
		return hunshi2Cells;
	}

	public void setHunshi2Cells(Hunshi2Cell[] hunshi2Cells) {
		// this.hunshi2Cells = hunshi2Cells;
	}

	public boolean[] getHunshiOpen() {
		return hunshiOpen;
	}

	public void setHunshiOpen(boolean[] hunshiOpen) {
		this.hunshiOpen = hunshiOpen;
		HorseManager.em.notifyFieldChange(this, "hunshiOpen");
	}

	public List<Integer> getHunshiSkills() {
		return hunshiSkills;
	}

	public void setHunshiSkills(List<Integer> hunshiSkills) {
		this.hunshiSkills = hunshiSkills;
	} 

}
