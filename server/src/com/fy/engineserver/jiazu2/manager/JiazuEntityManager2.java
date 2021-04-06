package com.fy.engineserver.jiazu2.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.jiazu2.instance.JiazuXiulian;
import com.fy.engineserver.jiazu2.model.PracticeModel;
import com.fy.engineserver.jiazu2.model.QifuModel;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.jiazu2.Option_CostSilver4ShangXiangConfirm;
import com.fy.engineserver.menu.activity.jiazu2.Option_CostSilver4XiulianConfirm;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class JiazuEntityManager2 {
	public static JiazuEntityManager2 instance;
	
	public static Logger logger = JiazuSubSystem.logger;
	
	public static SimpleEntityManager<JiazuMember2> em;
	
	public static final byte 优先扣除工资 = 3;
	public static final byte 只扣除银子 = 1;
	public static final byte 只扣除绑银 = 0;
	public static final byte 只扣除工资 = 2;
	
	public static final int 增加数值 = 0;
	public static final int 增加千分比 = 1;
	
	public static final double subRate = 10d;
	
	public LruMapCache cache = new LruMapCache(10240, 60 * 60 * 1000, false, "mj2_jiazumember2");
	
	public Map<Long, JiazuMember2> tempCache = new Hashtable<Long, JiazuMember2>();
	
	public void init() {
		
		instance = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(JiazuMember2.class);
		ServiceStartRecord.startLog(this);
	}
	
	public void 清空BCX值(Player player) {
		JiazuMember2 jm = this.getEntity(player.getId());
		jm.setOldXiuLian(null);
	}
	
	public long getParatical(Player player) {
		try {
			JiazuMember2 jm2 = this.getEntity(player.getId());
			return jm2.getPracticeValue();
		} catch (Exception e){
			logger.error("[新家族] [获取家族修炼值] [异常] [" + player.getLogString() + "]", e);
		}
		return 0;
	}
	
	
	public void reward4KillBiaoche(Player player) {
		synchronized (player) {
			JiazuMember2 jm = this.getEntity(player.getId());
			jm.reset();
			jm.setKillBiaocheTimes(jm.getKillBiaocheTimes() + 1);
			if (logger.isDebugEnabled()) {
				logger.debug("[家族运镖] [镖车被击杀奖励击杀者] [击杀者:" + player.getLogString() + "] [今天已经领奖次数:" + jm.getKillBiaocheTimes() + "]");
			}
			if (jm.getKillBiaocheTimes() <= JiazuManager2.每天击杀镖车获得奖励次数) {
				//获得奖励
				JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
				jiazuMember.setCurrentWeekContribution(jiazuMember.getCurrentWeekContribution() + JiazuManager.CONTRIBUTION_ADD_BY_JIAZU_SILVERCAR);
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
				jiazu.initMember4Client();
				jm.setPracticeValue(jm.getPracticeValue() + JiazuManager2.击杀镖车奖励修炼值);
				player.sendError(String.format(Translate.杀死镖车获得奖励, JiazuManager.CONTRIBUTION_ADD_BY_JIAZU_SILVERCAR, JiazuManager2.击杀镖车奖励修炼值));
				logger.warn("[家族运镖] [击杀其他国家镖车] [奖励] [成功] [" + player.getLogString() + "] [奖励家族贡献:" + JiazuManager.CONTRIBUTION_ADD_BY_JIAZU_SILVERCAR + 
						"] [奖励修炼值:" + JiazuManager2.击杀镖车奖励修炼值 + "] [现有贡献:" + jiazuMember.getCurrentWeekContribution() + "] [修炼值:" + jm.getPracticeValue() + "]");
			}
		}
	}
	/**
	 * 增加属性
	 * @param player
	 */
	public void addPracticeAttr(Player player) {
		try {
			JiazuMember2 jm = this.getEntity(player.getId());
			List<JiazuXiulian> old = jm.getOldXiuLian();
			PracticeModel pm = null;
			if (old != null) {
				for (JiazuXiulian jxl : old) {
					pm = JiazuManager2.instance.praticeMaps.get(jxl.getSkillId());
					if (pm == null) {
						logger.warn("[新家族] [减掉原有家族技能属性失败] [没有找到对应id技能] [id:" + jxl.getSkillId() + "] [" + player.getLogString() + "]");
						continue;
					}
					int[] type = pm.getAddNumAndTypeByLevel(jxl.getSkillLevel());
					this.addAttrP2Player(player, pm.getAddAttrType(), -type[1], type[0]);
				}
			}
			List<JiazuXiulian> xiul = jm.getXiulian();
			if (xiul != null && xiul.size() > 0) {
				old = null;
				old = new ArrayList<JiazuXiulian>();
				for (JiazuXiulian jxl : xiul) {
					pm = JiazuManager2.instance.praticeMaps.get(jxl.getSkillId());
					if (pm == null) {
						logger.warn("[新家族] [增加家族技能失败] [没有找到对应id技能] [id:" + jxl.getSkillId() + "] [" + player.getLogString() + "]");
						continue;
					}
					int[] type = pm.getAddNumAndTypeByLevel(jxl.getSkillLevel());
					this.addAttrP2Player(player, pm.getAddAttrType(), type[1], type[0]);
					JiazuXiulian oxl = new JiazuXiulian();
					oxl.setSkillExp(jxl.getSkillExp());
					oxl.setSkillId(jxl.getSkillId());
					oxl.setSkillLevel(jxl.getSkillLevel());
					old.add(oxl);
				}
				jm.setOldXiuLian(old);
			}
		} catch (Exception e) {
			logger.error("[新家族] [家族技能增加属性] [异常] [" +  player.getLogString() + "]", e);
		}
	}
	
	/**
	 * 
	 * @param p
	 * @param aaType  增加类型  气血等
	 * @param value
	 * @param addType2  增加类型  千分比or数值
	 */
	private void addAttrP2Player(Player p, int aaType, int value, int addType2) {
		if (logger.isDebugEnabled()) {
			logger.debug("[测试增加属性]  [增加值:" + value/subRate + "] [增加类型： " +aaType+ "] [增加类型2："+addType2+"] [" + p.getLogString() + "]");
		}
		switch (aaType) {
		case MagicWeaponConstant.hpNum:
			if (addType2 == 增加数值) {
				p.setMaxHPB(p.getMaxHPB() + value);
			} else if (addType2 == 增加千分比) {
				p.setMaxHPC(p.getMaxHPC() + value/subRate);
			}
			break;
		case MagicWeaponConstant.physicAttNum:
			if (addType2 == 增加数值) {
				p.setPhyAttackB(p.getPhyAttackB() + value);
			} else if (addType2 == 增加千分比) {
				p.setPhyAttackC(p.getPhyAttackC() + value/subRate);
			}
			break;
		case MagicWeaponConstant.magicAttNum:
			if (addType2 == 增加数值) {
				p.setMagicAttackB(p.getMagicAttackB() + value);
			} else if (addType2 == 增加千分比) {
				p.setMagicAttackC(p.getMagicAttackC() + value/subRate);
			}
			break;
		case MagicWeaponConstant.physicDefanceNum:
			if (addType2 == 增加数值) {
				p.setPhyDefenceB(p.getPhyDefenceB() + value);
			} else if (addType2 == 增加千分比) {
				p.setPhyDefenceC(p.getPhyDefenceC() + value/subRate);
			}
			break;
		case MagicWeaponConstant.magicDefanceNum:
			if (addType2 == 增加数值) {
				p.setMagicDefenceB(p.getMagicDefenceB() + value);
			} else if (addType2 == 增加千分比) {
				p.setMagicDefenceC(p.getMagicDefenceC() + value/subRate);
			}
			break;
//		case MagicWeaponConstant.mpNum:
//			if (addType2 == 增加数值) {
//				p.setMaxMPB(p.getMaxMPB() + value);
//			} else if (addType2 == 增加千分比) {
//				p.setMaxMPC(p.getMaxMPC() + value/subRate);
//			}
//			break;
		case MagicWeaponConstant.dodgeNum:
			if (addType2 == 增加数值) {
				p.setDodgeB(p.getDodgeB() + value);;
			} else if (addType2 == 增加千分比) {
				p.setDodgeC(p.getDodgeC() + value/subRate);
			}
			break;
		case MagicWeaponConstant.cirtNum:
			if (addType2 == 增加数值) {
				p.setCriticalHitB(p.getCriticalHitB() + value);
			} else if (addType2 == 增加千分比) {
				p.setCriticalHitC(p.getCriticalHitC() + value/subRate);
			}
			break;
		case MagicWeaponConstant.hitNum:
			if (addType2 == 增加数值) {
				p.setHitB(p.getHitB() + value);
			} else if (addType2 == 增加千分比) {
				p.setHitC(p.getHitC() + value/subRate);
			}
			break;
		case MagicWeaponConstant.破甲:
			if (addType2 == 增加数值) {
				p.setBreakDefenceB(p.getBreakDefenceB() + value);
			} else if (addType2 == 增加千分比) {
				p.setBreakDefenceC(p.getBreakDefenceC() + value/subRate);
			}
			break;
		case MagicWeaponConstant.精准:
			if (addType2 == 增加数值) {
				p.setAccurateB(p.getAccurateB() + value);
			} else if (addType2 == 增加千分比) {
				p.setAccurateC(p.getAccurateC() + value/subRate);
			}
			break;
		case MagicWeaponConstant.免爆:
			if (addType2 == 增加数值) {
				p.setCriticalDefenceB(p.getCriticalDefenceB() + value);
			} else if (addType2 == 增加千分比) {
				p.setCriticalDefenceC(p.getCriticalDefenceC() + value/subRate);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 家族技能修炼
	 * @param player
	 * @param skillId
	 * @param learnTimes 一次学习次数，多次需要扣除更多金钱
	 */
	public String upgradePratice(Player player, int skillId, int learnTimes, boolean confirm) {
		if (learnTimes == 0) {
			learnTimes = 1;
		} else if (learnTimes == 1) {
			learnTimes = 10;
		}
		if (learnTimes <= 0) {
			logger.warn("[新家族] [修炼家族技能] [错误] [客户端发过来的修炼倍数错误] [learnTimes : " + learnTimes + "] [" + player.getLogString() + "]");
			return "学习次数输入错误";
		}
		PracticeModel model = JiazuManager2.instance.praticeMaps.get(skillId);
		if (model == null) {
			logger.warn("[新家族] [修炼家族技能] [异常] [没有对应家族技能配置] [" + player.getLogString() + "] [skillId : " + skillId + "]");
			return "模板是空";
		}
		if (player.getJiazuId() <= 0) {
			player.sendError(Translate.您还没有家族);
			return "没有家族";
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			player.sendError(Translate.您还没有家族);
			return "没有家族";
		}
		if (jiazu.getJiazuStatus() == JiazuManager2.封印家族功能) {
			player.sendError(Translate.家族资金不足封印);
			return "家族封印";
		}
		SeptStation station = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		if (station == null) {
			player.sendError(Translate.text_jiazu_065);
			return "没有驻地";
		}
		int weaponLevel = station.getBuildingLevel(BuildingType.武器坊);
		int armorLevel = station.getBuildingLevel(BuildingType.防具坊);
		int canLearnLevel = model.getCanLearnLevel(weaponLevel, armorLevel);
		int type = model.getNoticeType();;
		synchronized (player) {
			JiazuMember2 member = JiazuEntityManager2.instance.getEntity(player.getId());
			List<JiazuXiulian> tempXiuLian = member.getXiulian();
			JiazuXiulian xiulian = member.getJiazuXiulianBySkillId(skillId);
			for (JiazuXiulian temp : tempXiuLian) {
				if (temp.getSkillId() == skillId) {
					xiulian = temp;
					break;
				}
			}
			if (xiulian == null) {
				xiulian = new JiazuXiulian();
				xiulian.setSkillId(skillId);
				tempXiuLian.add(xiulian);
			}
			int skillLevel = xiulian.getSkillLevel();
			if (skillLevel >= model.getMaxLevel()) {
				player.sendError(Translate.已达到最高等级);
				return "达到最高级";
			}
			if (skillLevel >= canLearnLevel) {
				player.sendError(String.format(Translate.家族建筑等级不足, Translate.武器防具坊[type]));
				return "建筑等级不足";
			}
			int costP = JiazuManager2.instance.base.getCostXiulian() * learnTimes;
			long costS = JiazuManager2.instance.base.getCostSiliverNums() * learnTimes;
			if (member.getPracticeValue() < costP) {
				player.sendError(Translate.修炼值不足);
				return "修炼值不足";
			}
			int resultLevel = skillLevel;
			long exp = xiulian.getSkillExp() + JiazuManager2.instance.base.getAddExp() * learnTimes;
			for (int i=skillLevel; i<model.getMaxLevel(); i++) {
				if (exp >= model.getExp4next()[i]) {
					exp = exp - model.getExp4next()[i];
					resultLevel ++;
					if (resultLevel > canLearnLevel) {
						player.sendError(String.format(Translate.家族建筑等级不足, Translate.武器防具坊[type]));
						return "建筑等级不足";
					}
				} else {
					break;
				}
			}
			byte costType = 0;
			if (model.getCostSiliverType()[skillLevel] == 0) {
				costType = 优先扣除工资;
			} else {
				costType = (byte) model.getCostSiliverType()[skillLevel];
			}
			String result = this.expenseSiliver(player, costType, costS, (byte) (confirm?0:2), "", "家族修炼");
			if (result != null) {
				if (result.indexOf(JiazuEntityManager2.needConfirm2) >= 0) {
					costSiliverConfirm(player, Long.parseLong(result.split("_")[1]), skillId, learnTimes);
					return "二次确认";
				}
				player.sendError(result);
				return "银子不足";
			}
			member.setPracticeValue(member.getPracticeValue() - costP);		//扣除修炼点
			xiulian.setSkillExp(exp);
			xiulian.setSkillLevel(resultLevel);
			member.setXiulian(tempXiuLian);
			if (resultLevel > skillLevel) {
				this.addPracticeAttr(player);
			}
			logger.warn("[新家族] [家族修炼] [成功] [" + player.getLogString() + "] [" + xiulian + "]");
			try {
				if (tempXiuLian.size() >= 10) {		//只记录10个修炼的最小等级
					List<JiazuXiulian> tt = new ArrayList<JiazuXiulian>();
					for (int i=0; i<tempXiuLian.size(); i++) {
						tt.add(tempXiuLian.get(i));
					}
					Collections.sort(tt, new SortByLevel());
					AchievementManager.getInstance().record(player, RecordAction.随意10个家族修炼最高等级, tt.get(9).getSkillLevel());
				}
			} catch (Exception e) {
				logger.warn("[目标系统] [统计玩家家族修炼] [异常] [" +  player.getLogString() + "]", e);
			}
		}
		return null;
	}
	
	class SortByLevel implements Comparator<JiazuXiulian> {
		@Override
		 public int compare(JiazuXiulian o1, JiazuXiulian o2) {
			if (o1.getSkillLevel() > o2.getSkillLevel()) {
				return -1;
			}
			return 1;
		 }
	}
	
	public void costSiliverConfirm(Player player, long needMoney, int skillId, int learnTimes) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_CostSilver4XiulianConfirm option1 = new Option_CostSilver4XiulianConfirm();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setSkillId(skillId);
		option1.setLearnTimes(learnTimes==1?0:1);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String costDes = BillingCenter.得到带单位的银两(needMoney);
		String msg = JiazuManager2.instance.translate.get(5);
		mw.setDescriptionInUUB(String.format(msg, costDes));
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
	}
	
	public static final String needConfirm = "needConfirm";
	public static final String needConfirm2 = "needConfirmT";
	public static final String needConfirm3 = "needConfirmQ";
	
	/**
	 * 家族祈福
	 * @param player
	 * @param qifuLevel
	 */
	public void qifu(Player player, int qifuLevel, boolean confirm) {
		QifuModel model = JiazuManager2.instance.qifuMaps.get(qifuLevel);
		if (model == null) {
			logger.warn("[新家族] [进行家族祈福] [异常] [出现未配置的祈福等级] [" + player.getLogString() + "] [qifuLevel:" + qifuLevel + "]");
			return ;
		}
		if (player.getJiazuId() <= 0) {		//没有家族
			player.sendError(Translate.您还没有家族);
			return;
		}
		
		synchronized (player) {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu.getJiazuStatus() == JiazuManager2.封印家族功能) {
				player.sendError(Translate.家族资金不足封印);
				return;
			}
			JiazuMember2 member = JiazuEntityManager2.instance.getEntity(player.getId());
			int times = JiazuManager2.instance.getQifuTimesByVip(player.getVipLevel());
			if (member.getQifuTimes() >= times) {
				player.sendError(Translate.当日祈福次数已达最大);
				return;
			}
			int leftTimes = times - member.getQifuTimes();
			byte costType = model.getCostType();
//			if (costType == 0) {				//修正扣费类型
//				costType = 只扣除工资;
//			}
			StringBuffer sb = new StringBuffer();
			sb.append(",");
			sb.append(String.format(Translate.获得修炼值,  model.getAddNum())).append(",");
			sb.append(String.format(Translate.获得家族资金, BillingCenter.得到带单位的银两(model.getAddJiazuZiyuan()))).append(",");
			sb.append(String.format(Translate.剩余祈福次数, (leftTimes-1)+""));
			String result = this.expenseSiliver(player, costType, model.getCostNum(), (byte) (confirm?0:3), sb.toString(), "家族上香");
			if (result != null) {
				if (result.indexOf(JiazuEntityManager2.needConfirm3) >= 0) {
					costSiliver4ShangxiangConfirm(player, Long.parseLong(result.split("_")[1]), qifuLevel);
					return;
				}
				player.sendError(result);
				return ;
			}
			member.setQifuTimes(member.getQifuTimes() + 1);
			member.setPracticeValue(member.getPracticeValue() + model.getAddNum());
			String result22 = jiazu.addJiazuMoney(model.getAddJiazuZiyuan());
			if (result22 != null) {
				player.sendError(result22);
			}
			
			if(qifuLevel == 2){
				JiazuManager2.instance.addLiveness(player, JiaZuLivenessType.家族上香_中档);
			}else if(qifuLevel == 3){
				JiazuManager2.instance.addLiveness(player, JiaZuLivenessType.家族上香_高档);
			}
			if (logger.isInfoEnabled()) {
				logger.info("[新家族] [进行家族祈福] [成功] [" + player.getLogString() + "] [增加后修炼值:" + member.getPracticeValue() + "] [新增值:" + model.getAddNum() + "] [当日修炼次数:" + member.getQifuTimes() + "]");
			} 
			if (model.getQifuLevel() >= 3) {
				try {
					ChatMessageService.getInstance().sendMessageToJiazu(jiazu, String.format(JiazuManager2.instance.translate.get(7), player.getName(), model.getAddNum()+""), Translate.系统 + ":");
				} catch (Exception e) {
					logger.warn("[新家族] [家族上香] [发消息给所有家族成员] [异常] [" + player.getLogString() + "]", e);
				}
			}
		}
	}
	
	public void costSiliver4ShangxiangConfirm(Player player, long needMoney, int qifuLevel) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_CostSilver4ShangXiangConfirm option1 = new Option_CostSilver4ShangXiangConfirm();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setQifuLevel(qifuLevel);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String costDes = BillingCenter.得到带单位的银两(needMoney);
		String msg = JiazuManager2.instance.translate.get(6);
		mw.setDescriptionInUUB(String.format(msg, costDes));
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
	}
	
	public String expenseSiliver(Player player, byte costType, long costNum, String reson) {
		return expenseSiliver(player, costType, costNum, (byte) 0, "", reson);
	}
	
//	public String expenseSiliver(Player player, byte costType, long costNum, byte confirm, String reson) {
//		return expenseSiliver(player, costType, costNum, confirm, "", reson);
//	}
	
	public void destory() {
		if (em != null) {
			em.destroy();
			logger.info("**************************************[服务器关闭][horse2entityManager调用destory]*********************************************");
		}
	}
	/**
	 * 根据类型扣除工资或者银子
	 * @param player
	 * @param costType
	 * @param costNum
	 * @param confirm   0已经确认   1需要发刷任务二次确认   2需要发点修炼二次确认
	 * @return
	 */
	public String expenseSiliver(Player player, byte costType, long costNum, byte confirm, String description, String reson) {
		BillingCenter bc = BillingCenter.getInstance();
		if (bc == null) {
			return Translate.服务器出现错误;
		}
		if (costType == 优先扣除工资) {		
			if (player.getWage() + player.getSilver() + player.getShopSilver() < costNum) {
				return Translate.银子不足;
			}
			long costWage = 0;
			long costSiliver = 0;
			if (player.getWage() < costNum) {
				if (player.getWage() > 0) {
					costWage = player.getWage();
				} 
			} else {
				costWage = costNum;
			}
			costSiliver = costNum - costWage;
			if (costSiliver > 0) {
				if (confirm == 1) {
					return JiazuEntityManager2.needConfirm + "_" + costSiliver;
				} else if (confirm == 2) {
					return JiazuEntityManager2.needConfirm2 + "_" + costSiliver;
				} else if (confirm == 3) {
					return JiazuEntityManager2.needConfirm3 + "_" + costSiliver;
				}
				try {
					bc.playerExpense(player, costSiliver, CurrencyType.SHOPYINZI, ExpenseReasonType.家族祈福, reson);
					player.sendError("扣除银子:" + BillingCenter.得到带单位的银两(costSiliver) + description);
				} catch (Exception e) {
					logger.error("[新家族] [进行家族祈福] [异常2] [" + player.getLogString() + "]", e);
					return Translate.银子不足;
				}
			}
			if (costWage > 0) {
				try {
					bc.playerExpense(player, costWage, CurrencyType.GONGZI, ExpenseReasonType.家族祈福, reson);
					player.sendError(Translate.扣除 + Translate.工资 + ":" + BillingCenter.得到带单位的银两(costWage) + description);
				} catch (Exception e) {
					logger.error("[新家族] [进行家族祈福] [异常] [" + player.getLogString() + "]", e);
					return Translate.text_jiazu_033;
				}
			}
		} else if (costType == 只扣除银子 ) {
			if (player.getShopSilver() + player.getSilver() < costNum) {
				return Translate.银子不足;
			}
			if (confirm == 3) {
				return JiazuEntityManager2.needConfirm3 + "_" + costNum;
			} else if (confirm == 2) {
				return JiazuEntityManager2.needConfirm2 + "_" + costNum;
			}
			try {
				bc.playerExpense(player, costNum, CurrencyType.SHOPYINZI, ExpenseReasonType.家族祈福, reson);
				player.sendError(Translate.扣除 + Translate.银子 + ":" + BillingCenter.得到带单位的银两(costNum) + description);
			} catch (Exception e) {
				logger.error("[新家族] [进行家族祈福] [异常2] [" + player.getLogString() + "]", e);
				return Translate.银子不足;
			}
		} else if (costType == 只扣除绑银) {
			try {
				bc.playerExpense(player, costNum, CurrencyType.BIND_YINZI, ExpenseReasonType.家族祈福, reson);
				player.sendError(Translate.扣除 + Translate.绑银 + ":" +  BillingCenter.得到带单位的银两(costNum) + description);
			} catch (Exception e) {
				logger.error("[新家族] [进行家族祈福] [异常2] [" + player.getLogString() + "]", e);
				return Translate.绑银不足;
			}
		} else if(costType == 只扣除工资) {
			if (player.getWage() < costNum) {
				return Translate.text_jiazu_033;
			}
			try {
				bc.playerExpense(player, costNum, CurrencyType.GONGZI, ExpenseReasonType.家族祈福, reson);
				player.sendError(Translate.扣除 + Translate.工资 + ":" +  BillingCenter.得到带单位的银两(costNum) + description);
			} catch (Exception e) {
				logger.error("[新家族] [进行家族祈福] [异常] [" + player.getLogString() + "]", e);
				return Translate.text_jiazu_033;
			}
		} else {
			return "unknow type";
		}
		return null;
	}
	
	public void notifyRemoveFromCache(JiazuMember2 entity) {
		try {
			em.save(entity);
		} catch (Exception e) {
			logger.error("[新家族] [异常] [移除保存错误] [" + entity.getId() + "]", e);
		}
	}
	
	public JiazuMember2 getEntity(long playerId) {
		JiazuMember2 entity = (JiazuMember2) cache.get(playerId);
		if(entity == null) {
			try {
				entity = em.find(playerId);
				if(entity == null) {
					entity = new JiazuMember2(playerId);
					entity.setId(playerId);
					entity.setLastResetTime(System.currentTimeMillis());
					em.notifyNewObject(entity);
					cache.put(playerId, entity);
					logger.error("[新家族] [从数据库加载数据] [成功] [playerId : " + playerId + "]");
				}
//				logger.error("[新家族] [从数据库加载数据] [成功] [playerId : " + playerId + "]");
			} catch (Exception e) {
				logger.error("[新家族] [从数据库加载数据异常] [playerId : " + playerId + "]", e);
			}
		}
		if (!tempCache.containsKey(playerId)) {
			tempCache.put(playerId, entity);
		}
		return entity;
	}
}
