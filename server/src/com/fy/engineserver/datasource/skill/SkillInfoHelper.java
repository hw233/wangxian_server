package com.fy.engineserver.datasource.skill;

import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_JiaXue;
import com.fy.engineserver.datasource.buff.Buff_CouXie;
import com.fy.engineserver.datasource.buff.Buff_GongQiangFangYu;
import com.fy.engineserver.datasource.buff.Buff_JiaXue;
import com.fy.engineserver.datasource.buff.Buff_LiLiangZhuFu;
import com.fy.engineserver.datasource.buff.Buff_WangZheZhuFu;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffectAndQuickMove;
import com.fy.engineserver.datasource.skill.master.SkBean;
import com.fy.engineserver.datasource.skill.master.SkEnConf;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.datasource.skill.passivetrigger.PassiveTriggerImmune;
import com.fy.engineserver.datasource.skill2.GenericSkill;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.PetSkillReleaseProbability;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.fy.engineserver.util.Utils;

public class SkillInfoHelper {
	public static final int color_golden = 0xffba00;
	public static final int color_red = 0xff0000;
	public static final int color_green = 0x00ff00;
	public static final int color_des_green = 0x5aff00;
	public static final int color_des_yellow = 0xfff600;
	public static final int color_des_blue = 0x37dcff;

	/**
	 * 支持一些变量：
	 * 
	 * $mp 标识消耗的魔法值
	 * $bu 标识产生的buff具体的描述
	 * $bp 标识产生buff的概率
	 * $bl 标识产生buff持续的时间
	 * $ap 标识发挥基础攻击力
	 * 
	 * @param player
	 * @param skill
	 * @return
	 */
	public static String generate(Player player, Skill skill) {
		return generate(player, skill, true);
	}

	/**
	 * 支持一些变量：
	 * 
	 * $mp 标识消耗的魔法值
	 * $bu 标识产生的buff具体的描述
	 * $bp 标识产生buff的概率
	 * $bl 标识产生buff持续的时间
	 * $ap 标识发挥基础攻击力
	 * 
	 * @param player
	 * @param skill
	 * @return
	 */
	public static String generate(Player player, Skill skill, boolean includeNextLevel) {
		StringBuffer sb = new StringBuffer();
		int level = -1;
		CareerManager cm = CareerManager.getInstance();
		Career career = cm.getCareer(player.getCareer());

		Skill sk = career.getSkillById(skill.getId());
		int skilLevel = player.getSkillLevel(skill.getId());
		if (!(skill instanceof PassiveSkill)) {
			sb.append("<I iconId='" + skill.getIconId() + "'></I><f color='" + color_des_green + "'>" + skill.getName() + "</f>");
			if (sk == null) {
				// sb.append("\n<f color='" + color_red + "'></f>\n");
				sb.append("\n\n");
			} else {
				if (level == 0) {
					// sb.append("<f color='" + color_green + "'></f>");
				} else {
					sb.append("(" + skilLevel + Translate.级 + ")");
				}
			}
		}

		if (skilLevel < 1) {
			level = 1;
		} else {
			level = skilLevel;
		}
		if (skill instanceof ActiveSkill) {
			genActiveSkDesc(player, skill, includeNextLevel, sb, level);
		} else if (skill instanceof PassiveSkill) {
			PassiveSkill ps = (PassiveSkill) skill;
			if (ps.getShortDescription() != null && ps.getShortDescription().length() > 0) {
				sb.append("\n<f color='" + color_des_yellow + "' size='30'>" + ps.getShortDescription() + "</f>");
			}
			if (skilLevel == 0) {
				String str = ps.getDescription(0, player);
				if (str == null || str.equals("")) {
					str = ps.getDescription(1);
				}
				sb.append("\n<f size='30'>" + ps.getDescription() + "</f>\n<f size='30'>" + str + "</f>");

				if (includeNextLevel && level < skill.getMaxLevel()) {
					sb.append("\n");
					sb.append("<f size='30'>");
					sb.append(Translate.下一级);
					sb.append("</f>");
					sb.append("\n");
					String str1 = ps.getDescription(1, true, player);
					if (str1 == null || str.equals("")) {
						str1 = ps.getDescription(1, true);
					}
					sb.append(str1);
				}
			} else {
				String str = ps.getDescription(level, player);
				if (str == null || str.equals("")) {
					str = ps.getDescription(level);
				}
				sb.append("\n<f size='30'>" + ps.getDescription() + "</f>\n<f size='30'>" + str + "</f>");

				if (includeNextLevel && level < skill.getMaxLevel()) {
					level++;

					sb.append("\n");
					sb.append("<f size='30'>");
					sb.append(Translate.下一级);
					sb.append("</f>");
					sb.append("\n");
					String str1 = ps.getDescription(level, true, player);
					if (str1 == null || str.equals("")) {
						str1 = ps.getDescription(level, true);
					}
					sb.append(str1);
				}
			}
		} else if (skill instanceof AuraSkill) {
			AuraSkill as = (AuraSkill) skill;
			if (as.getShortDescription() != null && as.getShortDescription().length() > 0) {
				sb.append("\n" + as.getShortDescription());
			}
			sb.append("<f size='30'>");
			sb.append(Translate.text_3941);
			sb.append("</f>");
			sb.append("<f color='" + color_green + "' size='30'>");
			String desp = as.getDescription();

			if (desp.indexOf("$bu") >= 0) {
				BuffTemplateManager btm = BuffTemplateManager.getInstance();
				String buffName = as.getBuffName();

				if (level == 0) {
					level = 1;
				}

				if (btm != null) {
					if (level > 0 && as.getBuffLevel() != null && level <= as.getBuffLevel().length) {
						BuffTemplate bt = btm.getBuffTemplateByName(buffName);
						if (bt != null) {
							Buff buff = bt.createBuff(as.getBuffLevel()[level - 1] - 1);
							buff.setCauser(player);
							desp = desp.replaceAll("\\$bu", buff.getDescription());

						}
					}
				}

			}

			sb.append(desp);
			sb.append("</f>");
			if (includeNextLevel && level < skill.getMaxLevel()) {
				level++;
				sb.append("\n");
				sb.append("<f size='30'>");
				sb.append(Translate.下一级);
				sb.append("</f>");
				sb.append("\n");
				sb.append("<f color='" + color_green + "' size='30'>");
				desp = as.getDescription();
				if (desp.indexOf("$bu") >= 0) {
					BuffTemplateManager btm = BuffTemplateManager.getInstance();

					String buffName = as.getBuffName();
					if (btm != null) {
						if (level > 0 && as.getBuffLevel() != null && level <= as.getBuffLevel().length) {
							BuffTemplate bt = btm.getBuffTemplateByName(buffName);
							if (bt != null) {
								Buff buff = bt.createBuff(as.getBuffLevel()[level - 1] - 1);
								buff.setCauser(player);
								desp = desp.replaceAll("\\$bu", buff.getDescription());

							}
						}
					}
				}
				sb.append(desp);
				sb.append("</f>");
			}
		} else if (skill instanceof PassiveTriggerImmune) {
			sb.append(((PassiveTriggerImmune)skill).getDescription(level, player));
		} else {
			sb.append(Translate.未识别的技能类型 + skill.getClass());
		}
		return sb.toString();
	}

	public static void genActiveSkDesc(Player player, Skill skill, boolean includeNextLevel, StringBuffer sb, int level) {
		String skEnhance = "";
		int skAEnhancePoint = 0;
		int buffTimeAdd = 0;
		Logger log = Skill.logger;
		SkEnhanceManager skMgr = SkEnhanceManager.getInst();
		do {
			if (skill.pageIndex < 0) {
				// log.debug("SkillInfoHelper.generate: 1");
				break;
			}
			if (player.getLevel() < TransitRobberyManager.openLevel) {
				// log.debug("SkillInfoHelper.generate: 2");
				break;
			}
			SkBean bean = skMgr.findSkBean(player);
			if (bean == null) {
				// log.debug("SkillInfoHelper.generate: 3");
				break;
			}
			if (bean.useLevels == null) {
				// log.debug("SkillInfoHelper.generate: 4");
				break;
			}
			if (bean.useLevels[skill.pageIndex] <= 0) {
				// log.debug("SkillInfoHelper.generate: 5");
				break;
			}
			int lv = bean.useLevels[skill.pageIndex] - 1;
			if (lv + 1 >= 10 && skill.getId() == 703) {// 修罗镇魂
				int vv[] = { 0, 3, 5, 8 };
				buffTimeAdd = vv[(lv + 1) / 10];
				log.debug("SkillInfoHelper.genActiveSkDesc: add 修罗镇魂 time {}s", buffTimeAdd);
			} else if (skill.id == 903) {// 903血祭人阶10重,增加的是技能的持续时间，不是技能buff的持续时间。
			} else if (skill.id == 716) {// 716:// 影魅 716 夺魂 //(+1秒)不用写,后面有人阶10重的描述
			} else {
				buffTimeAdd = skMgr.getBuffAddTime(player, skill);
			}
			int[] levelAddPoint = SkEnConf.conf[player.getCareer() - 1][skill.pageIndex].levelAddPoint;
			if (lv >= levelAddPoint.length) {
				log.debug("SkillInfoHelper.generate: 6");
				break;
			}
			skAEnhancePoint = levelAddPoint[lv];
			// ud += skAEnhancePoint;
			skEnhance = "(+" + skAEnhancePoint + ")";
			if(skill != null && skill.getId() == 9044){
				skEnhance = "(+" + skAEnhancePoint + "%)";
			}
			log.debug("SkillInfoHelper.generate: [pname:{}] [skname:{}] [lv:{}] [level:{}] [skEnhance:{}] [skAEnhancePoint:{}]", new Object[]{player.getName(), skill.getName(), lv,level,skEnhance,skAEnhancePoint});
		} while (false);

		ActiveSkill as = (ActiveSkill) skill;
		appendSkBaseInfo(player, sb, level, as, buffTimeAdd, skMgr, skEnhance);
		if (includeNextLevel && level < skill.getMaxLevel()) {
			level++;
			sb.append("\n<f size='30'>" + Translate.下一级 + "</f>");
			appendSkBaseInfo(player, sb, level, as, buffTimeAdd, skMgr, skEnhance);
		}
	}

	public static void checkGuiDha710(Player casterP, Skill skill, StringBuffer sb, SkEnhanceManager skMgr) {
		if (skill.id != 710) {
			return;
		}
		checkActiveBigStep(casterP, skill, sb);
	}

	public static void checkActiveBigStep(Player casterP, Skill skill, StringBuffer sb) {
		int step = SkEnhanceManager.getInst().getSlotStep(casterP, skill.pageIndex);
		if (step <= 0) {
			return;
		}
		try {
			SkEnConf cc = SkEnConf.conf[casterP.getCareer() - 1][skill.pageIndex];
			sb.append("(" + cc.desc[step - 1] + ")");
		} catch (Exception e) {
			Skill.logger.error("SkillInfoHelper.checkActiveBigStep: {}", e.toString());
		}
	}

	public static int calcDD(Player player, int level, ActiveSkill as) {
		int dd = (int) (player.getWeaponDamageLowerLimit() * (as.getParam1() + as.getParam2() * level) + player.getPhyAttack() * (as.getParam3() + as.getParam4() * level) + player.getMagicAttack() * (as.getParam5() + as.getParam6() * level) + as.getParam7() + as.getParam8() * level);
		return dd;
	}

	public static int calcUD(Player player, int level, ActiveSkill as) {
		int ud = (int) (player.getWeaponDamageUpperLimit() * (as.getParam1() + as.getParam2() * level) + player.getPhyAttack() * (as.getParam3() + as.getParam4() * level) + player.getMagicAttack() * (as.getParam5() + as.getParam6() * level) + as.getParam7() + as.getParam8() * level);
		return ud;
	}

	public static String appendBuffDesc(Player player, int level, ActiveSkill as, String desp) {
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		String buffName = as.getBuffName();

		if (btm != null) {
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			Logger log = Skill.logger;
			log.debug("SkillInfoHelper.appendBuffDesc: {}", buffName);
			if (bt != null && as.getBuffLevel() != null && level <= as.getBuffLevel().length) {
				Buff buff = bt.createBuff(as.getBuffLevel()[level - 1] - 1);
				buff.setCauser(player);
				{
					int pageIndex = as.pageIndex;
					Player casterP = player;
					Logger logger = Skill.logger;
					log.debug("SkillInfoHelper.appendBuffDesc: pageIndex{} {} [技能："+(as!=null?as.getName():"nul")+"] [等级："+level+"] [玩家："+player.getLogString()+"] [desp:"+desp+"] [isImmunity:"+player.isImmunity()+"]", pageIndex, bt.getClass().getSimpleName());
					do {
						int lv = SkEnhanceManager.getInst().getLayerValue(player, as.pageIndex);
						if (lv <= 0) break;
						int point = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].levelAddPoint[lv - 1];
						if (point <= 0) break;
						if (buff instanceof Buff_GongQiangFangYu) {
							Buff_GongQiangFangYu bb = (Buff_GongQiangFangYu) buff;
							bb.fixEnhance(point);
							if (logger.isDebugEnabled()) {
								logger.debug("appendBuffDesc: 有大师技能 {} point {}", as.getName(), point);
								logger.debug("appendBuffDesc: buff name {}", bb.getTemplateName());
							}
						} else if (as.id == 402) {// 402普度 气血回复百分比增强
							if (false == buff instanceof Buff_JiaXue) {
								log.debug("普度buff类型判断不对{}", buff.getClass().getSimpleName());
								break;
							}
							Buff_JiaXue bb = (Buff_JiaXue) buff;
							BuffTemplate_JiaXue bbbtt = (BuffTemplate_JiaXue) bt;
							bbbtt.fixDesc(buff.getLevel(), bb, point);
							log.debug("SkillInfoHelper.appendBuffDesc: {}", buff.getDescription());
						} else if (as.id == 812) {// 812 反噬 九黎
							Buff_CouXie cx = (Buff_CouXie) buff;
							cx.calc();
							int pre = cx.hpStealPercent;
							cx.hpStealPercent += point;
							cx.setDescription(Translate.text_3162 + pre + "(+" + point + ")" + Translate.text_3163);
						} else if (as.id == 8257) {		//8257  刚胆
							Buff_WangZheZhuFu wz = (Buff_WangZheZhuFu) buff;
							double extraRate = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].specilAddPoint[lv - 1];
							wz.calc();
							wz.setExtraRate(extraRate);
							wz.setDescription(wz.getTempDes());
						} else if (as.id == 8256) {			//8256兽印
							Buff_LiLiangZhuFu bl = (Buff_LiLiangZhuFu) buff;
							double extraRate = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].specilAddPoint[lv - 1];
							bl.calc();
							bl.setExtraRate(extraRate);
							bl.setDescription(bl.getTempDes());
						}
					} while (false);
				}
				String skInfo = "";
				do {
					if (player == null) break;
					switch (as.id) {
					case 401:// 血印 额外增加生命上限
					case 403: {// //仙心 403 焱印 人阶10重:激活焱印技能暴击率增加2%
						try {
							int lvP = SkEnhanceManager.getInst().getLayerValue(player, as.pageIndex);
							if (lvP > 0) {
								SkEnConf skC = SkEnConf.conf[player.getCareer() - 1][as.pageIndex];
								int point = skC.levelAddPoint[lvP - 1];
								skInfo = String.format("(" + skC.levelDesc + ")", point);
							}
						} catch (Exception e) {
							log.error("SkillInfoHelper.appendBuffDesc: {}", as.getName());
							log.error("处理每层加成描述错误。", e);
						}
						break;
					}
					case 602:
						int lvP = SkEnhanceManager.getInst().getLayerValue(player, as.pageIndex);
						int step = 0;
						if (lvP > 0) {
							step = lvP / 10;
							if (step == 1) {
								skInfo = "(+5%)";
							} else if (step == 2) {
								skInfo = "(+15%)";
							} else if (step == 3) {
								skInfo = "(+25%)";
							}
						}
						break;
					}
				} while (false);
				desp = desp.replaceAll("\\$bu", buff.getDescription() + skInfo);
			}
		}
		return desp;
	}

	public static void appendSkBaseInfo(Player player, StringBuffer sb, int level, ActiveSkill as, int buffTimeAdd, SkEnhanceManager skMgr, String skEnhance) {
		Logger log = Skill.logger;
		int lvIdx = level - 1;
		if (level > 0 && player != null) {
			if (as.nuqiFlag) {
				// sb.append("\n<f color='" + color_des_yellow + "'>"+Translate.translateString(Translate.怒气消耗点数,new
				// String[][]{{Translate.COUNT_1,player.getTotalXp()+""}})+"</f>");
			} else {
				int mp = as.calculateMp(player, level);
				sb.append("\n<f color='" + color_des_yellow + "' size='30'>" + Translate.translateString(Translate.法术消耗点数, new String[][] { { Translate.COUNT_1, mp + "" } }) + "</f>");
				if(player.getCareer() == 5){
					Career career = CareerManager.getInstance().getCareer(player.getCareer());
					if(career != null){
						int sklevel = player.getSkillLevel(as.getId());
						int costPoint = as.calculateDou(player, sklevel);
						if(costPoint > 0){
							sb.append("\n<f color='" + color_des_yellow + "' size='30'>" + Translate.translateString(Translate.法术增加豆数, new String[][] { { Translate.COUNT_1, costPoint  + "" } }) + "</f>");
						}else{
							sb.append("\n<f color='" + color_des_yellow + "' size='30'>" + Translate.translateString(Translate.法术消耗豆数, new String[][] { { Translate.COUNT_1, Math.abs(costPoint)  + "" } }) + "</f>");
						}
					}
				}
			}
		}
		if (as.getRange() >= 5) {
			if (as instanceof SkillWithoutEffectAndQuickMove) {
				sb.append("\n<f color='" + color_des_yellow + "' size='30'>" + Translate.translateString(Translate.距离详细, new String[][] { { Translate.COUNT_1, ((SkillWithoutEffectAndQuickMove)as).getRange(level) / 5 + "" } }) + "</f>");
			} else {
				sb.append("\n<f color='" + color_des_yellow + "' size='30'>" + Translate.translateString(Translate.距离详细, new String[][] { { Translate.COUNT_1, as.getRange() / 5 + "" } }) + "</f>");
			}
		}
		if (as.getShortDescription() != null && as.getShortDescription().length() > 0) {
			sb.append("\n<f  size='30'>" + as.getShortDescription() + "</f>");
		}
		if (as.getDuration1() == 0) {
			sb.append("\n<f  size='30'>" + Translate.瞬发 + "</f>");
		} else {
			sb.append("\n <f  size='30'>" + Utils.formatTimeDisplay2(as.getDuration1()) + Translate.吟唱 + "</f>");
		}
		if (as.getDuration3() > 100) {
			sb.append("  <f  size='30'>" + Utils.formatTimeDisplay2(as.getDuration3()) + " " + Translate.冷却 + "</f>");
		}
		// long subCD = SkEnhanceManager.getInst().calcCDSub(player, as);
		// if (subCD > 0) {
		// sb.append("(-" + subCD / 1000 + Translate.text_49 + ")");
		// }
		if (as != null && player != null) {
			if (as.id == 8213) {
				int c = player.getCareer() - 1;
				SkEnConf[] conf = SkEnConf.conf[c];
				if (conf != null) {
					int lvP = SkEnhanceManager.getInst().getLayerValue(player, as.pageIndex);
					if (lvP >= 10) {
						sb.append("<f  size='30'>(-" + lvP + Translate.text_49 + ")</f>");
					}
					Skill.logger.debug("[影魅遁形技能描述] [lvP:" + lvP + "] [" + player.getName() + "]");
				}
			}
		}

		sb.append("\n");
		String desp = as.getDescription();
		if (desp.indexOf("$mp") >= 0) {
			if (level > 0 && player != null) {
				int mp = as.calculateMp(player, level);
				desp = desp.replaceAll("\\$mp", "" + mp);
			}
		}
		if (desp.indexOf("$bu") >= 0) {
			desp = appendBuffDesc(player, level, as, desp);
		}
		if (desp.indexOf("$bp") >= 0) {
			int bps[] = as.getBuffProbability();
			int skAdd = SkEnhanceManager.getInst().getBuffAddBp(player, as);
			if (level > 0 && level <= bps.length) {
				desp = desp.replaceAll("\\$bp", bps[level - 1] + (skAdd > 0 ? ("(+" + skAdd + ")") : ("")) + "%");
			}
		}
		if (desp.indexOf("$sh") >= 0) {
			int layerValue = SkEnhanceManager.getInst().getLayerValue(player, as.pageIndex);
			double baseValue = as.getId() == 8258 ?50:0;			//嗜血基础为50
			if (layerValue > 0) {
				double point = SkEnConf.conf[player.getCareer() - 1][as.pageIndex].specilAddPoint[layerValue - 1];
				desp = desp.replaceAll("\\$sh", baseValue + "%+("+point+"%)");
			}else{
				desp = desp.replaceAll("\\$sh", baseValue + "%");
			}
		}
		if (desp.indexOf("$bl") >= 0) {
			long bps[] = as.getBuffLastingTime();
			if (level > 0 && level <= bps.length) {
				desp = desp.replaceAll("\\$bl", Utils.formatTimeDisplay2(bps[level - 1]) + (buffTimeAdd > 0 ? "(+" + buffTimeAdd + Translate.text_49 + ")" : ""));
			}
		}
		if (desp.indexOf("$ct") >= 0) {
			int tt = 20 + level;
			desp = desp.replaceAll("\\$ct", tt+"");
		}

		int[] dmgArr = as.attackDamages;
		if (desp.indexOf("$ap") >= 0) {
			int ud = calcUD(player, level, as);

			int dd = calcDD(player, level, as);
			if (ud == dd && ud == 0) {
				if (lvIdx >= 0 && lvIdx < dmgArr.length) {
					ud = dmgArr[lvIdx];
				} else {
					log.error("SkillInfoHelper.genActiveSkDesc: 等级越界 {} {}", level, as.getName());
				}
				dd = ud;
			}
			if (ud == dd) {// 观察了下，大师技能只修改一个值，所以只在这个里放。
				/*
				 * 由于伤害的提升是在最后算的，所以这计算的结果不对。去掉。
				 * if (as.id == 803) {// 仙心 803 霜暴 人阶10重:激活霜爆伤害加成10% 10,15,20
				 * int addV = skMgr.fixLingZun803(ud, player, as);
				 * if (addV > 0) {
				 * skEnhance += "(+" + addV + ")";
				 * }
				 * } else if (as.id == 21007) {// 仙心 21007 乾蓝 人阶10重:激活乾蓝伤害加成5% 5,8,10
				 * int addV = skMgr.fixLingZun21007(ud, player, as);
				 * if (addV > 0) {
				 * skEnhance += "(+" + addV + ")";
				 * }
				 * }else if (as.id == 21005) {// 影魅 21005 碎影 人阶10重:获得碎影技能伤害提升5% 10,15
				 * int step = SkEnhanceManager.getInst().getSlotStep(player, as.pageIndex);
				 * step = step * 5;
				 * if (step > 0) {
				 * int addV = ud * step / 100;
				 * skEnhance += "(+" + addV + ")";
				 * }
				 * }
				 */
				desp = desp.replaceAll("\\$ap", ud + skEnhance);
			} else {
				desp = desp.replaceAll("\\$ap", dd + " ~ " + ud);
			}
		}

		if (desp.indexOf("$wa") >= 0) {
			int wa = (int) (100 * (as.getParam1() + as.getParam2() * level));
			desp = desp.replaceAll("\\$wa", wa + "%");
		}
		if (desp.indexOf("$mCnt") >= 0) {
			// 禁忌技能
			desp = desp.replaceAll("\\$mCnt", "" + (level == 1 ? 2 : 3));
		}

		sb.append("<f  size='30'>" + desp + "</f>");
		// checkDouLuo603(player, as, sb, skMgr);
		// checkGuiDha710(player, as, sb, skMgr);
		// if (as.id == 404) checkActiveBigStep(player, as, sb);// 仙心 404 灵印 人阶10重:激活永久提升法攻+2%
		// if (as.id == 902) checkActiveBigStep(player, as, sb);// 仙心 902 坠天 人阶10重:激活所有技能附带3%减速目标减速30%5秒 rate 3,4,5
		// if (as.id == 502) checkActiveBigStep(player, as, sb);// 仙心 502 耀天 人阶10重:激活增加2%双防
		// if (as.id == 805) checkActiveBigStep(player, as, sb);// 仙心 805 雪葬 人阶10重:激活所有技能有1%眩晕目标5秒 rate 1,2,3
		// switch (as.id) {
		// case 400:// 狂印
		// case 702:// 双舞
		// case 601:// 旋风
		// case 401:// 血印
		// case 602:// 杀戮
		// case 21006:// 赤焰
		// case 406:// 熊印
		// case 708:// 斩首
		// case 606:// 轮刺
		// case 407:// 狼印
		// case 709:// 709 血毒 人阶10重:获得所有技能有1%触发所有技能减少目标50%命中持续5秒
		// case 714:
		// case 713:// 天诛
		// case 21005:// 碎影
		// case 21007:// 乾蓝
		// case 408:// 骨印
		// case 715:// 噬灵
		// case 506:
		// case 809:
		// case 21008:
		// case 409:// 九黎 409 妖印 人阶10重:获得法攻加成1%
		// case 712:// //仙心 712 火月 人阶10重:激活所有技能有1%触发减少法攻防御5%持续5秒
		// if(as.getId()!=8213){
		if (as.pageIndex >= 0) checkActiveBigStep(player, as, sb);
		// }

		// break;
		// }
	}

	/**
	 * 支持一些变量：
	 * 
	 * $mp 标识消耗的魔法值
	 * $bu 标识产生的buff具体的描述
	 * $bp 标识产生buff的概率
	 * $bl 标识产生buff持续的时间
	 * $ap 标识发挥基础攻击力
	 * 
	 * @param player
	 * @param skill
	 * @param gainDesc
	 * @return
	 */
	public static String generate(Pet pet, Skill skill, int level, String gainDesc) {
		StringBuffer sb = new StringBuffer();

		Skill sk = skill;
		if (!(skill instanceof PassiveSkill)) {
			sb.append("<I iconId='" + skill.getIconId() + "'></I>");
			sb.append("<f color='" + color_des_green + "'>");
			sb.append(skill.getName());
			sb.append("</f>");
			sb.append("<f color='" + color_des_green + "' size='30'>");
			if (gainDesc == null || gainDesc.isEmpty()) {
				sb.append("Lv." + level + "");
			} else if (gainDesc.equals("1")) {// //宠物天生 、天赋 技能不要等级。
				gainDesc = "";
			}
			sb.append("</f>");
			if (sk == null) {
				sb.append("\n<f color='" + color_red + "'  size='30'></f>\n");
			}
		}

		if (level < 1) level = 1;

		if (skill instanceof ActiveSkill) {
			ActiveSkill as = (ActiveSkill) skill;

			if (as.getRange() >= 5) {
				if (as instanceof SkillWithoutEffectAndQuickMove) {
					sb.append("\n<f color='" + color_des_yellow + "' size='30'>" + Translate.translateString(Translate.距离详细, new String[][] { { Translate.COUNT_1, ((SkillWithoutEffectAndQuickMove)as).getRange(level) / 5 + "" } }) + "</f>");
				} else {
					sb.append("\n<f color='" + color_des_yellow + "' size='30'>" + Translate.translateString(Translate.距离详细, new String[][] { { Translate.COUNT_1, as.getRange() / 5 + "" } }) + "</f>");
				}
			} else {
				sb.append("\n<f color='" + color_des_yellow + " size='30'>" + Translate.translateString(Translate.距离详细, new String[][] { { Translate.COUNT_1, 100 + "" } }) + "</f>");
			}

			if (as.getDuration1() == 0) {
				sb.append("\n<f size='30'>" + Translate.瞬发 + "</f>");
			} else {
				sb.append("\n<f  size='30'>" + Utils.formatTimeDisplay2(as.getDuration1()) + Translate.吟唱 + "</f>");
			}
			if (as.getDuration3() > 100) {
				sb.append("  <f  size='30'>" + Utils.formatTimeDisplay2(as.getDuration3()) + " " + Translate.冷却 + "</f>");
			}
			sb.append("\n");
			// sb.append("<f color='" + color_green + "'>");
			String desp = as.getDescription();

			if (desp.indexOf("$bu") >= 0) {

				String buffName = as.getBuffName();
				BuffTemplateManager btm = BuffTemplateManager.getInstance();
				if (btm != null) {
					BuffTemplate bt = btm.getBuffTemplateByName(buffName);
					if (bt != null && as.getBuffLevel() != null && level <= as.getBuffLevel().length) {
						Buff buff = bt.createBuff(as.getBuffLevel()[level - 1] - 1);
						buff.setCauser(pet);
						desp = desp.replaceAll("\\$bu", buff.getDescription());
					} else {
						Pet2Manager.log.error("SkillInfoHelper.generate: 等级不对{}", skill.getName());
					}
				} else {
					Pet2Manager.log.error("SkillInfoHelper.generate: 没有buff模板{}", skill.getName());
				}
				;
			}
			if (desp.indexOf("$bp") >= 0) {
				int bps[] = as.getBuffProbability();
				if (level > 0 && level <= bps.length) {
					desp = desp.replaceAll("\\$bp", bps[level - 1] + "%");
				} else {
					Pet2Manager.log.error("SkillInfoHelper.generate: B 等级不对{}", skill.getName());
				}
			}
			if (desp.indexOf("$bl") >= 0) {
				long bps[] = as.getBuffLastingTime();
				if (level > 0 && level <= bps.length) {
					desp = desp.replaceAll("\\$bl", Utils.formatTimeDisplay2(bps[level - 1]) + "");
				} else {
					Pet2Manager.log.error("SkillInfoHelper.generate: C 等级不对{}", skill.getName());
				}
			}

			if (desp.indexOf("$ap") >= 0) {
				int ud = (int) (pet.getWeaponDamage() * (as.getParam1() + as.getParam2() * level) + pet.getPhyAttack() * (as.getParam3() + as.getParam4() * level) + pet.getMagicAttack() * (as.getParam5() + as.getParam6() * level) + as.getParam7() + as.getParam8() * level);

				desp = desp.replaceAll("\\$ap", "" + ud);
			}

			if (desp.indexOf("$wa") >= 0) {
				int wa = (int) (100 * (as.getParam1() + as.getParam2() * level));
				desp = desp.replaceAll("\\$wa", wa + "%");
			}

			sb.append("<f size='30'>");
			sb.append(desp/* + "</f>" */);
			sb.append("</f>");

			// 判断等级是否达到最大
			if (level < skill.getMaxLevel()) {
				try {
					int nextLevel = level + 1;
					sb.append("\n<f color='" + color_red + "'  size='30'>");
					sb.append(Translate.下一级 + "</f>\n");
					sb.append("<f color='0xA9A9A9'  size='30'>");
					String desp2 = as.getDescription();
					if (desp2.indexOf("$bu") >= 0) {

						String buffName = as.getBuffName();
						BuffTemplateManager btm = BuffTemplateManager.getInstance();
						if (btm != null) {
							BuffTemplate bt = btm.getBuffTemplateByName(buffName);
							if (bt != null && as.getBuffLevel() != null && nextLevel <= as.getBuffLevel().length) {
								Buff buff = bt.createBuff(as.getBuffLevel()[nextLevel - 1] - 1);
								buff.setCauser(pet);
								desp2 = desp2.replaceAll("\\$bu", buff.getDescription());
							} else {
								Pet2Manager.log.error("SkillInfoHelper.generate: 等级不对{}", skill.getName());
							}
						} else {
							Pet2Manager.log.error("SkillInfoHelper.generate: 没有buff模板{}", skill.getName());
						}
						;
					}
					if (desp2.indexOf("$bp") >= 0) {
						int bps[] = as.getBuffProbability();
						if (nextLevel > 0 && nextLevel <= bps.length) {
							desp2 = desp2.replaceAll("\\$bp", bps[nextLevel - 1] + "%");
						} else {
							Pet2Manager.log.error("SkillInfoHelper.generate: B 等级不对{}", skill.getName());
						}
					}
					if (desp2.indexOf("$bl") >= 0) {
						long bps[] = as.getBuffLastingTime();
						if (level > 0 && level <= bps.length) {
							desp2 = desp2.replaceAll("\\$bl", Utils.formatTimeDisplay2(bps[nextLevel - 1]) + "");
						} else {
							Pet2Manager.log.error("SkillInfoHelper.generate: C 等级不对{}", skill.getName());
						}
					}

					if (desp2.indexOf("$ap") >= 0) {
						int ud = (int) (pet.getWeaponDamage() * (as.getParam1() + as.getParam2() * nextLevel) + pet.getPhyAttack() * (as.getParam3() + as.getParam4() * nextLevel) + pet.getMagicAttack() * (as.getParam5() + as.getParam6() * nextLevel) + as.getParam7() + as.getParam8() * nextLevel);

						desp2 = desp2.replaceAll("\\$ap", "" + ud);
					}

					if (desp2.indexOf("$wa") >= 0) {
						int wa = (int) (100 * (as.getParam1() + as.getParam2() * nextLevel));
						desp2 = desp2.replaceAll("\\$wa", wa + "%");
					}
					sb.append(desp2);
					sb.append("</f>\n");
				} catch (Exception e) {
					DevilSquareManager.logger.error("[宠物下级技能描述出错]", e);
				}
			} else {
				sb.append("\n");
			}
			PetManager pm = PetManager.getInstance();
			if (pm != null) {
				Map<Integer, PetSkillReleaseProbability> map = pm.getMap();
				if (map != null) {
					PetSkillReleaseProbability psrp = map.get(skill.id);
					if (psrp != null) {
						sb.append(psrp.得到技能概率描述());
					}
				}
			}
		} else if (skill instanceof PassiveSkill) {
			PassiveSkill ps = (PassiveSkill) skill;
			if (level == 0) {
				sb.append("\n<f  size='30'>" + ps.getDescription(1) + "</f>");
			} else {
				sb.append("\n<f  size='30'>" + ps.getDescription() + "</f>\n<f  size='30'>" + ps.getDescription(level) + "</f>");
			}
		} else if (skill instanceof AuraSkill) {
			AuraSkill as = (AuraSkill) skill;

			sb.append("<f size='30'>" + Translate.text_3941 + "</f>");
			sb.append("<f color='" + color_green + "' size='30'>");
			String desp = as.getDescription();

			if (desp.indexOf("$bu") >= 0) {
				BuffTemplateManager btm = BuffTemplateManager.getInstance();
				String buffName = as.getBuffName();

				if (level == 0) {
					level = 1;
				}

				if (btm != null) {
					if (level > 0 && as.getBuffLevel() != null && level <= as.getBuffLevel().length) {
						BuffTemplate bt = btm.getBuffTemplateByName(buffName);
						if (bt != null) {
							Buff buff = bt.createBuff(as.getBuffLevel()[level - 1] - 1);
							buff.setCauser(pet);
							desp = desp.replaceAll("\\$bu", buff.getDescription());

						}
					}
				}

			}

			sb.append(desp);
			sb.append("</f>");
		} else if (skill instanceof GenericSkill) {
			sb.append("\n<f size='30'>");
			sb.append(skill.getDescription());
			sb.append("</f>");
			if (gainDesc.isEmpty() == false) {
				sb.append(gainDesc);
			}
		} else {
			sb.append(Translate.text_3942 + skill.getClass());
		}
		return sb.toString();
	}
}
