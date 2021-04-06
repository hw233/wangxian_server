package com.fy.engineserver.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.base.AddRateActivity;
import com.fy.engineserver.activity.bloodrate.BloodRateAct;
import com.fy.engineserver.activity.dailyTurnActivity.model.DailyTurnActivity;
import com.fy.engineserver.activity.expactivity.ExpActivity;
import com.fy.engineserver.activity.extraquiz.ExtraQuizActivity;
import com.fy.engineserver.activity.floprateActivity.FlopRateActivity;
import com.fy.engineserver.activity.jiazu.LuckTreeActivity;
import com.fy.engineserver.activity.jiazu.RefreshTaskActivity;
import com.fy.engineserver.activity.jiazu.ShangXiangActivity;
import com.fy.engineserver.activity.levelUpReward.LevelUpRewardActivity;
import com.fy.engineserver.activity.shop.ArticleActivityOfUseAndGive;
import com.fy.engineserver.activity.shop.ShopActivityOfBuyAndGive;
import com.fy.engineserver.activity.taskDeliver.TaskDeliverAct;
import com.fy.engineserver.activity.treasure.instance.TreasureActivity;
import com.fy.engineserver.activity.wafen.model.WaFenActivity;
import com.fy.engineserver.carbon.devilSquare.activity.ExtraDevilOpenTimesActivity;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;

public class AllActivityManager {
	public static AllActivityManager instance ;
	public static final String shopBuyAct = "商店买送";			//商店购买赠送
	public static final String useGiveAct = "使用赠送";			//使用赠送
	public static final String tumoExpAct = "封魔录经验";			//封魔录经验
	public static final String allExpAct = "所有经验";			//所有经验
	public static final String chuangongEnergyAct = "传功元气增加";			//传功元气增加
	public static final String datiJifenAct = "答题经验";			//答题积分
	public static final String jifenAct = "积分活动";			//积分活动
	public static final String addTimesAct = "酒、贴、祈福次数活动";			//酒、贴、祈福次数增加活动
	public static final String exchangeAct = "兑换活动";			//兑换活动
	public static final String unitServerAct = "合服活动";			//合服活动
	public static final String dujieAct = "渡劫活动";			//渡劫活动
	public static final String extraAwardAct = "任务额外奖励";			//任务额外奖励
	public static final String taskDeliverAct = "完成N次任务奖励";			//完成n次任务后得到奖励
	public static final String addDevilOpenTimes = "恶魔城堡开放时间";			//额外增加恶魔城堡开放时间
	public static final String addFlopRate = "倍数增加地图怪物掉率";			//倍数增加地图怪物掉率
	public static final String wafenActivity = "挖坟活动";
	public static final String dailyTurnActivity = "每日登陆转盘活动";
	public static final String treasureActivity = "极地寻宝活动";
	public static final String caveActivity = "庄园活动";
	public static final String bloodActivity = "获得血脉增加活动";
	public static final String platLuckTree = "家族炼丹活动";
	public static final String shangxiangActivity = "家族上香活动";
	public static final String refreshTaskActivity = "家族刷新任务";
	public static final String xueShiActivity = "血石倍数活动";
	public static final String datiActivity = "额外答题活动";
	public static final String levelUpRewardActivity = "冲级红利活动";
	public static final String xianlingActivity = "仙灵大会活动";
	
	/** 所有活动管理--key=活动名 */
	public Map<String, List<BaseActivityInstance>> allActivityMap = new Hashtable<String, List<BaseActivityInstance>>();
	
	private AllActivityManager(){
	}
	
	public void init() {
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}
	/**
	 * 将活动list加入到map中
	 * @param actType
	 * @param activity
	 */
	public void add2AllActMap(String actType, List<BaseActivityInstance> list) {
		if(actType == null) {
			ActivitySubSystem.logger.error("[所有活动管理] [add2AllActMap方法中传入空值 ] [活动类型 : " + actType + "]");
			return ;
		}
		if(allActivityMap.get(actType) != null) {
			if(ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn("[所有活动管理] [map中已经存在此类型活动又要替换 ] [活动类型 : " + actType + "]");
			}
		} 
		allActivityMap.put(actType, list);
	}
	/**
	 * 将单个活动加入到map中
	 * @param actType
	 * @param activity
	 */
	public void add2AllActMap(String actType, BaseActivityInstance base) {
		if(actType == null) {
			ActivitySubSystem.logger.error("[所有活动管理] [add2AllActMap方法中传入空值 ] [活动类型 : " + actType + "]");
			return ;
		}
		if(allActivityMap.get(actType) == null) {
			List<BaseActivityInstance> list = new ArrayList<BaseActivityInstance>();
			allActivityMap.put(actType, list);
		} 
		List<BaseActivityInstance> list = allActivityMap.get(actType);
		if(!list.contains(base)) {
			list.add(base);
		}
		allActivityMap.put(actType, list);
	}
	/**
	 * 通知有活动相关的操作发生(不用反射，效率低)
	 * @param actType
	 * @param args
	 * @return 配合需要返回数值等变量继续执行代码的活动类
	 */
	public CompoundReturn notifySthHappened(String actType, Object ... args) {
		if(ActivitySubSystem.logger.isDebugEnabled()) {
			ActivitySubSystem.logger.debug("[所有活动管理] [有活动通知:" + actType + "]");
		}
		List<BaseActivityInstance> list = allActivityMap.get(actType);						
		if(list == null || list.size() <= 0) {					//list为空的时候返回根据之后需求再定
			if(ActivitySubSystem.logger.isDebugEnabled()) {
				ActivitySubSystem.logger.debug("[所有活动管理] [没有相应类型的活动录入] [活动类型 : " + actType + "]");
			}
			return null;
		}
		try {
			if(shopBuyAct.equals(actType)) {						//商店购买赠送
				Player temoP = (Player) args[0];
				Shop shop = (Shop) args[1];
				Goods goods = (Goods) args[2];
				Integer num = (Integer) args[3];
				doCheckShopActivity(list, temoP, shop, goods, num);
			} else if(useGiveAct.equals(actType)) {					//使用物品赠送
				Player temoP = (Player) args[0];
				@SuppressWarnings("unchecked")
				ArrayList<ArticleEntity> articles = (ArrayList<ArticleEntity>)args[1];
				byte useType = 0;
				if(args.length > 2) {
					try {
						useType = Byte.parseByte(args[2]+"");
					} catch (Exception e) {
						ActivitySubSystem.logger.error("[所有活动管理] [没有相应类型的活动录入]", e);
					}
				}
				doCheckUseActivity(list, temoP, articles, useType);
			} else if(tumoExpAct.equals(actType) || allExpAct.equals(actType)) {		//封魔录&&所有经验活动	
				return doCheckExpActivity(list, actType);
			} else if (chuangongEnergyAct.equals(actType) || datiJifenAct.equals(actType) || jifenAct.equals(actType)) {
				return doCheckAddRateActivity(list, actType);
			} else if(taskDeliverAct.equals(actType)) {
				Player tempP = (Player) args[0];
				String tkName = (String) args[1];
				docheckDeliverTaskActivity(list, tempP, tkName, actType);
			} else if (addDevilOpenTimes.equals(actType)) {
				return docheckDevilSquareOpenTimeActivity(list, actType);
			} else if (addFlopRate.equals(actType)) {
				String mapName = (String) args[0];
				return docheckFlopRateActivity(list, actType, mapName);
			} else if (wafenActivity.equals(actType)) {
				return docheckWaFenActivity(list);
			} else if (bloodActivity.equals(actType)) {
				byte rarit = (Byte) args[0];
				return docheckBloodActivity(list, actType, rarit);
			} else if (platLuckTree.equals(actType)) {
				int longtuLevel = Integer.parseInt(args[0].toString());
				int colorType = Integer.parseInt(args[1].toString());
				return doCheckPlatLuckTreeActivity(list, actType, longtuLevel, colorType);
			} else if (shangxiangActivity.equals(actType)) {
				int shangxiangLevel = Integer.parseInt(args[0].toString());
				return doCheckShangXiangActivity(list, actType, shangxiangLevel);
			} else if (refreshTaskActivity.equals(actType)) {
				return doCheckRefreshTaskActivity(list, actType);
			} else if (treasureActivity.equals(actType)) {
				return doCheckTreasureActivity(list, actType);
			} else if (dailyTurnActivity.equals(actType)) {
				return doCheckDailyTurnActivity(list, actType);
			} else if (levelUpRewardActivity.equals(actType)) {
				return doCheckLevelUpRewardActivity(list, actType);
			} else if (datiActivity.endsWith(actType)) {		//额外答题活动
				int currentHour = Integer.parseInt(args[0].toString());
				int currentMinit = Integer.parseInt(args[1].toString());
				return doCheckDatiActivity(list, currentHour, currentMinit);
			} else {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [存在录入活动，但是未实现方法] [活动类型 : " + actType + "]");
				}
			}
		} catch (Exception e) {
			String errorMsg = "[所有活动管理] [异常]";
			if(args != null && args.length > 0 && args[0] != null && args[0] instanceof Player) {
				errorMsg += "[" + ((Player)args[0]).getLogString() + "]";
			}
			ActivitySubSystem.logger.error(errorMsg, e);
		}
		return null;
	}
	
	private CompoundReturn doCheckLevelUpRewardActivity(List<BaseActivityInstance> list, String actType) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false).setIntValue(-1);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof LevelUpRewardActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + actType + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			if (activity.isThisServerFit() == null) {
				cr.setBooleanValue(true);
				break;
			}
		}
		return cr;
	}
	
	private CompoundReturn doCheckDailyTurnActivity(List<BaseActivityInstance> list, String actType) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false).setIntValue(-1);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof DailyTurnActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			if (activity.isThisServerFit() == null) {
				cr.setBooleanValue(true);
				break;
			}
		}
		return cr;
	}
	
	private CompoundReturn doCheckTreasureActivity(List<BaseActivityInstance> list, String actType) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false).setIntValue(-1);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof TreasureActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			if (activity.isThisServerFit() == null) {
				cr.setBooleanValue(true).setLongValue(activity.getEndTime());
				break;
			}
		}
		return cr;
	}
	private CompoundReturn doCheckDatiActivity(List<BaseActivityInstance> list, int currentHour, int currentMinit) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false).setIntValue(-1);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof ExtraQuizActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			ExtraQuizActivity lta = (ExtraQuizActivity) activity;
			if (lta.isThisServerFit() == null) {
				return lta.getExtraQuizInfo(currentHour, currentMinit);
			}
		}
		return cr;
	}
	
	private CompoundReturn doCheckRefreshTaskActivity(List<BaseActivityInstance> list, String actType) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false).setIntValue(-1);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof RefreshTaskActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			RefreshTaskActivity lta = (RefreshTaskActivity) activity;
			if (lta.isThisServerFit() == null) {
				cr.setBooleanValue(true).setLongValue(lta.getCostMoney());
				break;
			}
		}
		return cr;
	}
	
	private CompoundReturn doCheckShangXiangActivity(List<BaseActivityInstance> list, String actType, int level) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false).setLongValue(-1L).setIntValue(-1);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof ShangXiangActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			ShangXiangActivity lta = (ShangXiangActivity) activity;
			if (lta.isThisServerFit() == null) {
				long cost = lta.getCostMoney(level);
				if (cost > 0) {
					cr.setBooleanValue(true).setLongValue(cost).setIntValue(lta.getXiulianNum(level));
					break;
				}
			}
		}
		return cr;
	}
	
	private CompoundReturn doCheckPlatLuckTreeActivity(List<BaseActivityInstance> list, String actType, int longtuLevel, int colorType) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false).setLongValue(-1L);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof LuckTreeActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			LuckTreeActivity lta = (LuckTreeActivity) activity;
			if (lta.isThisServerFit() == null) {
				cr.setLongValue(lta.getCostMoney(colorType));
				if (lta.canPlant(longtuLevel, colorType)) {
					cr.setBooleanValue(true);
					break;
				}
			}
		}
		return cr;
	}
	
	private CompoundReturn docheckFlopRateActivity(List<BaseActivityInstance> list, String actType, String mapName) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof FlopRateActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			FlopRateActivity ea = (FlopRateActivity) activity;
			String result = ea.isThisServerFit(mapName);
			if (result == null) {
				cr.setBooleanValue(true).setDoubleValue(ea.getExtraRate());
			} else if(ActivitySubSystem.logger.isDebugEnabled()) { 
				ActivitySubSystem.logger.debug("[地图怪物掉率翻倍活动][判断结果:" + result + "]");
			}
		}
		return cr;
	}
	
	private CompoundReturn docheckBloodActivity(List<BaseActivityInstance> list, String actType, byte rarity) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof BloodRateAct)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			String result = activity.isThisServerFit();
			if (result == null) {
				cr.setBooleanValue(true).setDoubleValue(((BloodRateAct)activity).getRate(rarity));
			} else if(ActivitySubSystem.logger.isDebugEnabled()) { 
				ActivitySubSystem.logger.debug("["+useGiveAct+"][任务判断结果:" + result + "]");
			}
		}
		return cr;
	}
	
	private CompoundReturn docheckWaFenActivity(List<BaseActivityInstance> list) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof WaFenActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			String result = activity.isThisServerFit();
			if (result == null) {
				cr.setBooleanValue(true);
			} else if(ActivitySubSystem.logger.isDebugEnabled()) { 
				ActivitySubSystem.logger.debug("["+useGiveAct+"][任务判断结果:" + result + "]");
			}
		}
		return cr;
	}
	/**
	 * 恶魔城堡开启活动
	 * @param list
	 * @param actType
	 * @return
	 */
	private CompoundReturn docheckDevilSquareOpenTimeActivity(List<BaseActivityInstance> list, String actType) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false);
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof ExtraDevilOpenTimesActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			String result = activity.isThisServerFit();
			if (result == null) {
				ExtraDevilOpenTimesActivity ea = (ExtraDevilOpenTimesActivity) activity;
				cr.setBooleanValue(true).setIntValues(ea.getAddTimeHours());
			} else if(ActivitySubSystem.logger.isDebugEnabled()) { 
				ActivitySubSystem.logger.debug("[恶魔城堡额外开放次数][任务判断结果:" + result + "]");
			}
		}
		return cr;
	}
	
	/**
	 * 完成N次任务赠送活动
	 * @param list
	 * @param player
	 * @param tkName
	 * @param actType
	 * @return
	 */
	private CompoundReturn docheckDeliverTaskActivity(List<BaseActivityInstance> list, Player player, String tkName, String actType) {
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof TaskDeliverAct)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			String result = activity.isThisServerFit();
			if (result == null) {
				TaskDeliverAct tempAct = (TaskDeliverAct) activity;
				tempAct.notifyDeliverTask(player, tkName);
			} else if(ActivitySubSystem.logger.isDebugEnabled()) {
				ActivitySubSystem.logger.debug("[完成N次赠送活动][任务判断结果:" + result + "][" + player.getLogString() + "][" + tkName + "]");
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param list
	 * @param actType
	 * @return
	 */
	private CompoundReturn doCheckAddRateActivity(List<BaseActivityInstance> list, String actType) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false).setDoubleValue(0);
		for(BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof AddRateActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + actType + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			if (activity.isThisServerFit() == null) {
				AddRateActivity tempAct = (AddRateActivity) activity;
				cr.setBooleanValue(true).setDoubleValue(tempAct.getAddRate());
			}
		}
		return cr;
	}
	
	/**
	 * 经验类活动-
	 * @param list
	 * @param actType
	 * @return		--额外增加的数量，乘时需要+1
	 */
	private CompoundReturn doCheckExpActivity(List<BaseActivityInstance> list, String actType) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false).setDoubleValue(0);
		for(BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof ExpActivity)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + actType + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			if (activity.isThisServerFit() == null) {
				ExpActivity tempAct = (ExpActivity) activity;
				cr = tempAct.getExpActivityMultiple(Calendar.getInstance());
				if (cr.getBooleanValue()) {
					break;
				}
			}
			
		}
		return cr;
	}
	
	/**
	 * 使用赠送
	 * @param list
	 * @param player
	 * @param articles
	 * @return
	 */
	private CompoundReturn doCheckUseActivity(List<BaseActivityInstance> list, Player player,  ArrayList<ArticleEntity> articles, byte useType) {
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof ArticleActivityOfUseAndGive)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + useGiveAct + "] [此类型活动中出现其他类型实例 : " + (activity == null ? null : activity.getClass().getName()) + "]");
				}
				continue;
			}
			if (activity.isThisServerFit() == null) {
				((ArticleActivityOfUseAndGive)activity).afterUseArticle(player, articles, useType);
			}
		}
		return null;
	}
	/**
	 * 商店买送活动
	 * @param list
	 * @param player
	 * @param shop
	 * @param goods
	 * @param num
	 * @return
	 */
	private CompoundReturn doCheckShopActivity(List<BaseActivityInstance> list, Player player, Shop shop, Goods goods, int num) {
		for (BaseActivityInstance activity : list) {
			if(activity == null || !(activity instanceof ShopActivityOfBuyAndGive)) {
				if(ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[所有活动管理] [活动类型错误 ] [活动类型 : " + shopBuyAct + "] [此类型活动中出现其他类型实例 : " + activity.getClass().getName() + "]");
				}
				continue;
			}
			if (activity.isThisServerFit() == null) {
				((ShopActivityOfBuyAndGive)activity).afterBuyGoods(player, shop, goods, num);
			}
		}
		return null;
	}
	
}
