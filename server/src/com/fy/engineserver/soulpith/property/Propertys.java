package com.fy.engineserver.soulpith.property;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 角色属性枚举
 * 
 * @date on create 2016年3月22日 下午4:22:31
 */
public enum Propertys {
	HP(1, Translate.血量),
	PHYATTACK(2, Translate.物理攻击),
	PHYDEFANCE(3, Translate.物理防御),
	BREAKDEFANCE(4, Translate.破甲),
	DODGE(5, Translate.闪躲),
	CIRT(6, Translate.暴击),
	MP(7, Translate.法力值),
	MAGICATTACK(8, Translate.法术攻击),
	MAGICDEFANCE(9, Translate.法术防御),
	HIT(10, Translate.命中),
	CRITICALDEFANCE(12, Translate.免暴),
	ACCURATE(11, Translate.精准),
	FIREATTACK(13,Translate.庚金攻击),
	FIREDEFANCE(14, Translate.庚金抗性),
	FIREBREAKDEFANCE(15,Translate.庚金减抗),
	WINDATTACK(16, Translate.离火攻击),
	WINDDEFANCE(17, Translate.离火抗性),
	WINDBREAKDEFANCE(18, Translate.离火减抗),
	BLIZZATTACK(19, Translate.葵水攻击),
	BLIZZDEFANCE(20, Translate.葵水抗性),
	BLIZZBREAKDEFANCE(21, Translate.葵水减抗),
	THUNDATTACK(22, Translate.乙木攻击),
	THUNDDEFANCE(23, Translate.乙木抗性),
	THUNDERBREAKDEFANCE(24, Translate.乙木减抗),
	DODGE_RATE_OTHER(25, Translate.闪避率),
	CRITICAL_RATE_OTHER(26, Translate.暴击率),
	HIT_RATE_OTHER(27, Translate.命中率),
	HALF_RECOVER_RATE_OTHER(28, Translate.半秒回血),
	FIVE_RECOVER_RATE_OTHER(29, Translate.五秒回血),
	FIVE_RECOVER_HPRATE_OTHER(30, Translate.五秒回血千分比),
	HP_STEAL_OTHER(31, Translate.百分比吸血),
	ABNORMAL_TIME_DECREASE(32, "受到异常状态持续时间减少百分比"),
	SPECIAL_DAMAGE_DECREASE(33, "中毒被反伤效果减少百分比"),
	ANTIINJURE(34, "反伤比例"),
	PROBGOTFLAG(35, "攻击时一定概率触发标记5s有效，叠加两次造成血上限比例伤害"),
	ADDANTIBYHP(36, "血量降低一定比例增加反伤"),
	IMMUDINGSHEN(37, "免疫定身"),
	;
	private int index;
	private String name;
	private Propertys(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	public static Propertys valueOf(int index) {
		for (Propertys p : Propertys.values()) {
			if (p.getIndex() == index) {
				return p;
			}
		}
		return null;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
