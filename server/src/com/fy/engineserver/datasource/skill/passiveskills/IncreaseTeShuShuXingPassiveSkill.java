package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseTeShuShuXingPassiveSkill extends PassiveSkill {
	
	public static int level_limit = 111;							//等级限制
	public static String limit_msg = Translate.新心法等级限制提示;
	public static int skill_num = 3;								//只能有3个升级了
	public static int skill_num2 = 10;								//又加了10个可以升级的
	
	public static String[] attributeNames = new String[]{
		Translate.庚金攻击,Translate.离火攻击,Translate.葵水攻击,Translate.乙木攻击,
		Translate.庚金抗性,Translate.离火抗性,Translate.葵水抗性,Translate.乙木抗性,
		Translate.庚金减抗,Translate.离火减抗,Translate.葵水减抗,Translate.乙木减抗
	};
	
	//属性类型
	private int attributeType;
	//属性具体值
	private int[] addAttribute;

	@Override
	public void levelUp(Fighter fighter,final int skillLevel) {
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	@Override
	public void run(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		int currentLevel = skillLevel;
		int addValue = this.addAttribute[currentLevel - 1];
		switch (attributeType) {
		case 0:
			//庚金攻击
			player.setFireAttackB(player.getFireAttackB() + addValue);
			break;
		case 1:
			player.setWindAttackB(player.getWindAttackB() + addValue);
			break;
		case 2:
			player.setBlizzardAttackB(player.getBlizzardAttackB() + addValue);
			break;
		case 3:
			player.setThunderAttackB(player.getThunderAttackB() + addValue);
			break;
		case 4:
			//庚金防御
			player.setFireDefenceB(player.getFireDefenceB() + addValue);
			break;
		case 5:
			player.setWindDefenceB(player.getWindDefenceB() + addValue);
			break;
		case 6:
			player.setBlizzardDefenceB(player.getBlizzardDefenceB() + addValue);
			break;
		case 7:
			player.setThunderDefenceB(player.getThunderDefenceB() + addValue);
			break;
		case 8:
			//庚金减抗
			player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + addValue);
			break;
		case 9:
			player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + addValue);
			break;
		case 10:
			player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + addValue);
			break;
		case 11:
			player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + addValue);
			break;
		}
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		
		super.close(player, skillLevel);

		int currentLevel = skillLevel;
		
		int addValue = this.addAttribute[currentLevel - 1];
		
		switch (attributeType) {
		case 0:
			//庚金攻击
			player.setFireAttackB(player.getFireAttackB() - addValue);
			break;
		case 1:
			player.setWindAttackB(player.getWindAttackB() - addValue);
			break;
		case 2:
			player.setBlizzardAttackB(player.getBlizzardAttackB() - addValue);
			break;
		case 3:
			player.setThunderAttackB(player.getThunderAttackB() - addValue);
			break;
		case 4:
			//庚金防御
			player.setFireDefenceB(player.getFireDefenceB() - addValue);
			break;
		case 5:
			player.setWindDefenceB(player.getWindDefenceB() - addValue);
			break;
		case 6:
			player.setBlizzardDefenceB(player.getBlizzardDefenceB() - addValue);
			break;
		case 7:
			player.setThunderDefenceB(player.getThunderDefenceB() - addValue);
			break;
		case 8:
			//庚金减抗
			player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() - addValue);
			break;
		case 9:
			player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() - addValue);
			break;
		case 10:
			player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() - addValue);
			break;
		case 11:
			player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() - addValue);
			break;
		}
	}

	public String getDescription(int level){
		if(level < 1 || level > addAttribute.length){
			return "物理攻击增加";
		}else{
			return Translate.translateString(Translate.累计某种属性增加, new String[][]{{Translate.STRING_1, attributeNames[attributeType]},{Translate.COUNT_1,addAttribute[level-1]+""}});
		}
	}

	public String getDescription(int level, boolean nextLevel){
		if(level < 1 || level > addAttribute.length){
			return "物理攻击增加";
		}else{
			if(level == 1){
				return Translate.translateString(Translate.某种属性增加, new String[][]{{Translate.STRING_1, attributeNames[attributeType]}, {Translate.COUNT_1,addAttribute[level-1]+""}});
			}else{
				return Translate.translateString(Translate.某种属性增加, new String[][]{{Translate.STRING_1, attributeNames[attributeType]}, {Translate.COUNT_1,(addAttribute[level-1]-addAttribute[level-2])+""}});
			}
		}
	}

	public void setAttributeType(int attributeType) {
		this.attributeType = attributeType;
	}

	public int getAttributeType() {
		return attributeType;
	}

	public void setAddAttribute(int[] addAttribute) {
		this.addAttribute = addAttribute;
	}

	public int[] getAddAttribute() {
		return addAttribute;
	}
}
