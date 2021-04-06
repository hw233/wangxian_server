package com.fy.engineserver.sprite.horse;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.datasource.article.data.props.HorseProps;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class HorseManager {

	private static HorseManager self = null;
	public static long 骑飞行坐骑时间 = 5000;
	public static int 每天喂养次数 = 2;
	public static Logger logger = LoggerFactory.getLogger(HorseManager.class.getName());

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// cache时间不够长导致玩家获得horse时重复增加装备属性
	// 非常严重的错误
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public LruMapCache mCache = new LruMapCache(32 * 1024 * 1024, 4 * 60 * 60 * 1000);

	public static HorseManager getInstance() {
		return self;
	}

	public static SimpleEntityManager<Horse> em;

	public void init() {
		

		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Horse.class);
		if (HorseManager.logger.isWarnEnabled()) {
			HorseManager.logger.warn("[初始化坐骑manager成功]");
		}
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {
		try {
			em.destroy();
		} catch (Exception e) {
			logger.error("[坐骑destroy异常]", e);
		}

	}

	/**
	 * 通过坐骑id获得坐骑
	 * @param horseId
	 * @return
	 */
	public Horse getHorseByHorseId(Player player, long horseId, Soul soul) {
		try {
			Horse horse = (Horse) mCache.get(horseId);
			if (horse == null) {
				try {
					horse = em.find(horseId);
				} catch (Exception e) {
					logger.error("[通过id获取坐骑异常] [" + horseId + "]", e);
					return null;
				}
				if (horse == null) {
					if (logger.isWarnEnabled()) {
						logger.warn("[新坐骑] [通过id获得坐骑] [失败] [" + player.getLogString() + "] [" + horseId + "][soulTYpe:" + soul.getSoulType() + "]");
					}
				}
			}
			return horse;
		} catch (Exception e) {
			logger.error("[新坐骑系统] [通过坐骑id获取坐骑异常] [horseId :" + horseId + "]", e);
		}
		return null;
	}

	/**
	 * 取得当前元神下的坐骑
	 * @param id
	 * @param p
	 * @return
	 */
	public Horse getHorseById(long id, Player p) {
		try {
			Soul soul = p.getCurrSoul();
			Horse horse = (Horse) mCache.get(id);
			if (horse == null) {
				try {
					horse = em.find(id);
				} catch (Exception e) {
					logger.error("[通过id获取坐骑异常] [" + p.getLogString() + "] [" + id + "]", e);
					return null;
				}
				if (horse != null) {
					if (horse.getOwnerId() != p.getId()) {
						return null;
					}
					if (soul.getHorseArr() != null && soul.getHorseArr().contains(id)) {
						horse.owner = p;
					} else {
						HorseManager.logger.error("[通过id获得缓存坐骑错误] [当前元神下没有此坐骑] [" + p.getLogString() + "] [horseId:" + id + "]");
						return null;
					}
					horse.owner = p;
					horse.setCareer(soul.getCareer());
					horse.init();
					mCache.put(horse.getHorseId(), horse);
					if (logger.isWarnEnabled()) {
						logger.warn("[通过id获得坐骑] [" + p.getLogString() + "] [" + id + "]");
					}
				} else {
					logger.error("[通过id获得坐骑不存在] [" + p.getLogString() + "] [" + id + "]");
				}
			} else {
				if (soul.getHorseArr() != null && soul.getHorseArr().contains(id)) {
					horse.owner = p;
				} else {
					HorseManager.logger.error("[通过id获得坐骑错误] [当前元神下没有此坐骑] [" + p.getLogString() + "] [horseId:" + id + "]");
					return null;
				}
			}
			return horse;
		}catch(Exception e){
			logger.error("[通过id获得坐骑异常] ["+(p != null?p.getLogString():"null")+"] ["+id+"]", e);
			return null;
		}
	}

	/**
	 * 切换元神增加稀有坐骑
	 * @param from
	 *            如果from有增加到to
	 * @param to
	 */
	public void switchSoulAddHorse(Player player, Soul from, Soul to) {

		ArrayList<Long> list = from.getHorseArr();
		ArrayList<Long> list2 = to.getHorseArr();
		boolean update = false;
		if (list != null) {
			for (long id : list) {
				Horse horse = this.getHorseById(id, player);
				if (horse != null && horse.isFly()) {
					if (list2 == null) {
						list2 = new ArrayList<Long>();
					}
					if (!list2.contains(id)) {
						list2.add(id);
						update = true;
					}
				}
			}
			if (update) {
				to.setHorseArr(list2);
				if (HorseManager.logger.isWarnEnabled()) {
					HorseManager.logger.warn("[元神切换加稀有坐骑] [增加] [" + player.getLogString() + "]");
				}
			} else {
				if (HorseManager.logger.isWarnEnabled()) {
					HorseManager.logger.warn("[元神切换加稀有坐骑] [不增加] [" + player.getLogString() + "]");
				}
			}
		}
	}

	// 凤舞九天 获得1只碧虚青鸾
	// 章鱼哥保罗 获得1只深渊魔章
	// 快看观音姐姐 获得1只八卦仙蒲
	// 铁扇公主的宝贝 获得1只焚焰火扇
	// 金刚葫芦娃 获得1只乾坤葫芦
	// 驯龙高手 获得1只金极龙皇
	// 驾鹤西游 获得1只梦瞳仙鹤

	public static String[] horseNames = { Translate.碧虚青鸾, Translate.深渊魔章, Translate.八卦仙蒲, Translate.焚焰火扇, Translate.乾坤葫芦, Translate.金极龙皇, Translate.梦瞳仙鹤 };
	public static RecordAction[] RecordActionNames = { RecordAction.获得碧虚青鸾次数, RecordAction.获得八爪鱼坐骑次数, RecordAction.获得莲花坐骑次数, RecordAction.获得飞扇坐骑次数, RecordAction.获得葫芦坐骑次数, RecordAction.获得飞龙坐骑次数, RecordAction.获得飞剑坐骑次数 };

	public void horseAchievement(Player player, HorseProps horseProps) {
		try {
			boolean bln = false;
			String horsePropsName = horseProps.getName();
			int j = 0;
			for (int i = 0; i < horseNames.length; i++) {
				if (horsePropsName.contains(horseNames[i])) {
					bln = true;
					j = i;
					break;
				}
			}
			RecordAction ra = RecordActionNames[j];
			if (bln) {
				AchievementManager.getInstance().record(player, ra);
				if (AchievementManager.logger.isWarnEnabled()) {
					AchievementManager.logger.warn("[成就统计获得飞行坐骑] [" + player.getLogString() + "] [" + horsePropsName + "]");
				}
			} else {
				if (AchievementManager.logger.isWarnEnabled()) {
					AchievementManager.logger.warn("[成就统计获得飞行坐骑错误] [" + player.getLogString() + "] [" + horsePropsName + "]");
				}
			}
		} catch (Exception e) {
			HorseManager.logger.error("[使用飞行坐骑记录玩家成就异常] [" + player.getLogString() + "] [" + horseProps.getName() + "]", e);
		}
	}

	public ArrayList<Long> getPlayerHorses(Player p) {
		ArrayList<Long> list = p.getHorseIdList();
		if (list == null) {
			list = new ArrayList<Long>();
		}
		return list;
	}

	/**
	 * 创建一个坐骑
	 * @param pet
	 * @return
	 */
	public Horse addHorse(Horse horse) {
		mCache.put(horse.getHorseId(), horse);
		horse.setInited(true);
		horse.initHunshi();
		em.notifyNewObject(horse);
		return horse;
	}

}
