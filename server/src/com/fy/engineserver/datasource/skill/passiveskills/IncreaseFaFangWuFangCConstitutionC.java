package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseFaFangWuFangCConstitutionC extends PassiveSkill{
	
	/**
	 * 增加 法术减伤  物理减伤  耐力
	 */
	//
	//内法伤害减伤比例
	int[] faFang;
	//外功伤害减伤比例
	int[] wuFangC;
	//耐力值百分比
	int[] constitutionC;
	public int[] getFaFang() {
		return faFang;
	}
	public void setFaFang(int[] faFang) {
		this.faFang = faFang;
	}
	public int[] getWuFangC() {
		return wuFangC;
	}
	public void setWuFangC(int[] wuFangC) {
		this.wuFangC = wuFangC;
	}
	public int[] getConstitutionC() {
		return constitutionC;
	}
	public void setConstitutionC(int[] constitutionC) {
		this.constitutionC = constitutionC;
	}
	public void levelUp(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}
	public void run(Fighter fighter, int skillLevel) {}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {}
	
	public String getDescription(int level){
		StringBuffer sb = new StringBuffer();
		
		if(level < 1 || level > faFang.length){
			sb.append(Translate.text_3874);
		}else{
			sb.append(Translate.text_3875+faFang[level-1]+"%\n");
		}
		
		if(level < 1 || level > wuFangC.length){
			sb.append(Translate.text_3876);
		}else{
			sb.append(Translate.text_3877+wuFangC[level-1]+"%\n");
		}
		
		if(level < 1 || level > constitutionC.length){
			sb.append(Translate.text_3878);
		}else{
			sb.append(Translate.text_3879+constitutionC[level-1]+"%\n");
		}
		
		return sb.toString();
	}
}
