package com.fy.engineserver.economic.thirdpart.migu.entity.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseOtherData;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.sprite.horse2.model.HorseSkillModel;
import com.xuanzhi.tools.text.JsonUtil;

public class SaleConstants {
	/** 人物属性 */
	//角色属性
	public static String roleAttr1 = "<div title ='人物属性'><p>气血(总)：%s</p><p>仙法(总)：%s</p><p>外功(总)：%s</p><p>外防(总)：%s</p><p>法攻(总)：%s</p><p>法防(总)：%s</p><p>破甲(总)：%s</p><p>命中(总)：%s</p><p>闪躲(总)：%s</p><p>精准(总)：%s</p><p>暴击(总)：%s</p><p>免暴(总)：%s</p><p>乙木攻击：%s</p><p>乙木抗性：%s</p><p>乙木减抗：%s</p><p>葵水攻击：%s</p><p>葵水抗性：%s</p><p>葵水减抗：%s</p><p>离火攻击：%s</p><p>离火抗性：%s</p><p>离火减抗：%s</p><p>庚金攻击：%s</p><p>庚金抗性：%s</p><p>庚金减抗：%s</p></div>";
	public static String[] roleJson1 = new String[]{"气血(总)","仙法(总)","外功(总)","外防(总)","法攻(总)","法防(总)","破甲(总)","命中(总)","闪躲(总)","精准(总)","暴击(总)","免暴(总)","乙木攻击","乙木抗性","乙木减抗","葵水攻击","葵水抗性","葵水减抗","离火攻击","离火抗性","离火减抗","庚金攻击","庚金抗性","庚金减抗"};
	//基本信息
	public static String roleAttr2 = "<div title ='基本信息'><p>境界：%s</p><p>官职：%s</p><p>家族：%s</p><p>宗派：%s</p><p>配偶：%s</p><p>称号：%s</p><p>历练：%s</p><p>功勋：%s</p><p>元气：%s</p><p>移动速度：%s</p><p>文采值：%s</p><p>恶名值：%s</p><p>元神加成：%s</p><p>仙录总积分：%s</p><p>渡劫：%s</p><p>千层塔通过层数：%s</p></div>";
	public static String[] roleJson2 = new String[]{"银子","绑银","工资","积分","境界","官职","家族","宗派","配偶","称号","历练","功勋","元气","移动速度","文采值","恶名值","元神加成","仙录总积分","渡劫","千层塔通过层数"};
	//仙府信息
	public static String roleAttr3 = "<div title ='仙府信息'><p>逍遥居：%s</p><p>万宝库：%s</p><p>驭兽斋：%s</p><p>法门：%s</p><p>田地：%s</p><p>粮食：%s</p><p>木材：%s</p><p>石材：%s</p></div>";
	public static String roleAttr3_2 = "<div title ='仙府信息'></div>";
	//战队信息
	public static String roleAttr4 = "<div title ='战队信息'><p>战队名称：%s</p><p>战队战勋：%s</p><p>个人战勋：%s</p></div>";
	public static String roleAttr4_2 = "<div title ='战队信息'></div>";
	
	public static String divStart = "<div title ='%s'>";		//基本div
	public static String divStEnd = "</div>";		//基本div
	public static String base = "<p>%s：%s</p>";		//
	public static String base_2 = "<p>%s</p>";		//
	/** 当前装备 */
	public static String[] equInfos = new String[]{"本尊装备", "元神装备"};
	public static String currentEqu2 = "<img src='%s' num='%s' equiponlyid ='%s'></img>";	//icon  数量  装备id
	/** 背包/仓库 */
	public static String[] bags = new String[]{"背包", "防爆包", "仓库", "器灵仓库", "二号仓库"};
	/** 技能/修炼 */
	public static String[] soulInfo = new String[]{"基础/进阶技能", "大师技能", "怒气技能", "心法/注魂", "家族修炼"};		//本尊都有    元神只有前三个
	/** 宠物 */
	public static String[] petInfos = new String[]{"背包宠物"};
	/** 坐骑 */
	public static String horse_land = "<div title ='陆地坐骑'><p>坐骑名称：%s</p><p>等级：%s</p><p>品质：%s</p><p>移动速度：%s</p><p>血脉：%s</p><p>品阶：%s</p><p>气血：%s</p><p>仙法：%s</p><p>外攻：%s</p><p>外防：%s</p><p>破甲：%s</p><p>命中：%s</p><p>闪躲：%s</p><p>精准：%s</p><p>暴击：%s</p><p>免暴：%s</p><p>乙木攻击：%s</p><p>乙木抗性：%s</p><p>乙木减抗：%s</p><p>葵水攻击：%s</p><p>葵水抗性：%s</p><p>葵水减抗：%s</p><p>离火攻击：%s</p><p>离火抗性：%s</p><p>离火减抗：%s</p><p>庚金攻击：%s</p><p>庚金抗性：%s</p><p>庚金减抗：%s</p><p>技能：%s</p></div>";
	public static String[] horseJson = new String[]{"坐骑名称","等级","品质","移动速度","血脉","品阶","气血","仙法","外攻","外防","法攻","法防","破甲","命中","闪躲","精准","暴击","免暴","乙木攻击","乙木抗性","乙木减抗","葵水攻击","葵水抗性","葵水减抗","离火攻击","离火抗性","离火减抗","庚金攻击","庚金抗性","庚金减抗","技能"};
	/** 翅膀 */
	public static String[] wingInfos = new String[]{"翅膀","基础属性","奖励属性","宝石插槽"};
	
	public static void putHorseDes(Horse horse, List<String> map) {
		HorseOtherData otherData = horse.getOtherData();
		StringBuffer skillDes = new StringBuffer();
		for (int i=0; i<otherData.getSkillList().length; i++) {
			if (otherData.getSkillList()[i] > 0) {
				HorseSkillModel model = Horse2Manager.instance.horseSkillMap.get(otherData.getSkillList()[i]);
				if (model != null) {
					skillDes.append(model.getName()+"LV"+(otherData.getSkillLevel()[i]+1));
					if (i < (otherData.getSkillList().length-1)) {
						skillDes.append(",");
					}
				}
			}
		}
		for (int i=0; i<horseJson.length; i++) {
			String value = "";
			int index = i+1;
			switch (index) {
			case 1:value=horse.getHorseShowName(); break;
			case 2:value=horse.getHorseLevel()+""; break;
			case 3:value=Horse2Manager.colorDes[horse.getColorType()]; break;
			case 4:value=horse.getSpeed()+""; break;
			case 5:value=otherData.getBloodStar() + "星"; break;
			case 6:value=Horse2Manager.getJieJiMess(otherData.getRankStar()); break;
			case 7:value=horse.getMaxHP()+""; break;
			case 8:value=horse.getMaxMP()+"";break;
			case 9:value=horse.getPhyAttack()+""; break;
			case 10:value=horse.getPhyDefence()+""; break;
			case 11:value=horse.getMagicAttack()+"";break;
			case 12:value=horse.getMagicDefence()+"";break;
			case 13:value=horse.getBreakDefence()+""; break;
			case 14:value=horse.getHit()+""; break;
			case 15:value=horse.getDodge()+""; break;
			case 16:value=horse.getAccurate()+""; break;
			case 17:value=horse.getCriticalHit()+""; break;
			case 18:value=horse.getCriticalDefence()+""; break;
			case 19:value=horse.getThunderAttack()+""; break;
			case 20:value=horse.getThunderDefence()+""; break;
			case 21:value=horse.getThunderIgnoreDefence()+""; break;
			case 22:value=horse.getBlizzardAttack()+""; break;
			case 23:value=horse.getBlizzardDefence()+""; break;
			case 24:value=horse.getBlizzardIgnoreDefence()+""; break;
			case 25:value=horse.getWindAttack()+""; break;
			case 26:value=horse.getWindDefence()+""; break;
			case 27:value=horse.getWindIgnoreDefence()+""; break;
			case 28:value=horse.getFireAttack()+""; break;
			case 29:value=horse.getFireDefence()+""; break;
			case 30:value=horse.getFireIgnoreDefence()+""; break;
			case 31:value=skillDes.toString(); break;
			}
			map.add(horseJson[i]+":"+value);
		}
	}
	
	public static Map<String, Object> createPlayerAttrDes(Player player) throws Exception {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("title", "人物属性");
		List<String> map2 = new ArrayList<String>();
//		Map<String, String> map2 = new HashMap<String, String>();
		for (int i=0; i<roleJson1.length; i++) {
			map2.add(roleJson1[i]+":"+getDesByType(player, (i+1)));
		}
		map.put("text", map2);
		return map;
	}
	
	public static String getDesByType(Player player, int type) {
		switch (type) {
		case 1:	return player.getMaxHP() +"";
		case 2:	return player.getMaxMP()+"";
		case 3:	return player.getPhyAttack()+"";
		case 4:	return player.getPhyDefence() + SaleConstants.getAttrPercentDes(player, 1);
		case 5:	return player.getMagicAttack()+"";
		case 6:	return player.getMagicDefence() + SaleConstants.getAttrPercentDes(player, 2);
		case 7:	return player.getBreakDefence() + SaleConstants.getAttrPercentDes(player, 3);
		case 8:	return player.getHit() + SaleConstants.getAttrPercentDes(player, 4);
		case 9:	return player.getDodge() + SaleConstants.getAttrPercentDes(player, 5);
		case 10:return player.getAccurate()+SaleConstants.getAttrPercentDes(player, 6);	
		case 11:return player.getCriticalHit()+SaleConstants.getAttrPercentDes(player, 7);	
		case 12:return player.getCriticalDefence()+SaleConstants.getAttrPercentDes(player, 8);	
		case 13:return (player.getThunderAttack()+player.getThunderAttackX())+"";	
		case 14:return (player.getThunderDefence() + player.getThunderDefenceX())+SaleConstants.getAttrPercentDes(player, 9);
		case 15:return (player.getThunderIgnoreDefence() + player.getThunderIgnoreDefenceX())+SaleConstants.getAttrPercentDes(player, 10);	
		case 16:return (player.getBlizzardAttack() + player.getBlizzardAttackX())+"";	
		case 17:return (player.getBlizzardDefence()+player.getBlizzardDefenceX())+SaleConstants.getAttrPercentDes(player, 11);
		case 18:return (player.getBlizzardIgnoreDefence()+player.getBlizzardIgnoreDefenceX())+SaleConstants.getAttrPercentDes(player, 12);	
		case 19:return (player.getWindAttack() + player.getWindAttackX())+"";	
		case 20:return (player.getWindDefence()+player.getWindDefenceX())+SaleConstants.getAttrPercentDes(player, 13);
		case 21:return (player.getWindIgnoreDefence()+player.getWindIgnoreDefenceX())+SaleConstants.getAttrPercentDes(player, 14);	
		case 22:return (player.getFireAttack() + player.getFireAttackX())+"";	
		case 23:return (player.getFireDefence()+player.getFireDefenceX())+SaleConstants.getAttrPercentDes(player, 15);
		case 24:return (player.getFireIgnoreDefence() + player.getFireIgnoreDefenceX())+SaleConstants.getAttrPercentDes(player, 16);	
		}
		return "";
	}
	
	public static String getAttrPercentDes(Player player, int type) {
		int resultNum = 0;
		switch(type) {
		case 1:			//物防
			resultNum = (Integer) player.getSelfValue(35);
			break;
		case 2:			//法防
			resultNum = (Integer) player.getSelfValue(39);
			break;
		case 3:			//破甲
			resultNum = (Integer) player.getSelfValue(44);
			break;
		case 4:			//命中
			resultNum = (Integer) player.getSelfValue(47);
			break;
		case 5:			//闪躲
			resultNum = (Integer) player.getSelfValue(50);
			break;
		case 6:			//精准
			resultNum = (Integer) player.getSelfValue(53);
			break;
		case 7:			//暴击
			resultNum = (Integer) player.getSelfValue(97);
			break;
		case 8:			//免暴
			resultNum = (Integer) player.getSelfValue(96);
			break;
		case 9:			//雷抗
			resultNum = (Integer) player.getSelfValue(86);
			break;
		case 10:		//雷减抗
			resultNum = (Integer) player.getSelfValue(89);
			break;
		case 11:		//冰抗
			resultNum = (Integer) player.getSelfValue(70);
			break;
		case 12:		//冰减抗
			resultNum = (Integer) player.getSelfValue(73);
			break;
		case 13:		//风抗
			resultNum = (Integer) player.getSelfValue(78);
			break;
		case 14:		//风减抗
			resultNum = (Integer) player.getSelfValue(81);
			break;
		case 15:		//火炕
			resultNum = (Integer) player.getSelfValue(62);
			break;
		case 16:		//火减抗
			resultNum = (Integer) player.getSelfValue(65);
			break;
		}
		return "("+resultNum/10+"%)";
	}
}
