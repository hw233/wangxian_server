package com.fy.engineserver.datasource.career;

import java.util.ArrayList;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.AuraSkill;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.datasource.skill.Skill;

/**
 * 职业倾向
 * 
 * 
 *
 */
public class CareerThread {

	//所属的职业
	protected Career career;

	//此变量表明此线路在职业中是第几个线路，从0开始
	//此变量不需要编辑，在系统加载的时候必须自动设置
	protected int indexOfCareer;
	
	//职业线的名字
	protected String name;
	
	//职业线的描述
	protected String description;
	
	//职业线中技能层次的划分，
	//此划分依据的是各个技能需要的职业线点数
	//下面的设置标识 0～10为第一层，11～20为第二层，21～30为第三层
	//31~40为第四层，41～50为第五层，50～为第六层
	//每层之间的技能，可以采用均匀排列的方式，从左到右可以按照技能本来的顺序排列
	//编辑器可以调整各个技能在列表中的顺序，以达到在一层之间调整技能的顺序
	protected int layerDivides [] = new int[]{10,20,30,40,50};

	public Skill[] skills = new Skill[0];
	
	/**
	 * 根据技能的索引，得到职业的技能
	 * 与玩家无关
	 * 
	 * @param index
	 * @return
	 */
	public Skill getSkillByIndex(int index) {
		Skill[] skills = getSkills();
		if (index < 0 || index >= skills.length) {
//			Game.logger.warn("["+getName()+"] [getSkillByIndex] [error] [技能索引越界] [index:" + index + "] [career:" + getName() + "]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[{}] [getSkillByIndex] [error] [技能索引越界] [index:{}] [career:{}]", new Object[]{getName(),index,getName()});
			return null;
		}
		return skills[index];
	}
	
	public int getIndexBySkillId(int skillId){
		Skill[] skills = getSkills();
		for(int i = 0 ; i < skills.length ; i++){
			if(skills[i] != null && skills[i].getId() == skillId){
				return i;
			}
		}
		return -1;
	}
	/**
	 * 根据技能的索引，得到职业的技能
	 * 与玩家无关
	 * 
	 * @param index
	 * @return
	 */
	public Skill getSkillById(int skillId) {
		Skill[] skills = getSkills();
		for(int i = 0 ; i < skills.length ; i++){
			if(skills[i] != null && skills[i].getId() == skillId){
				return skills[i];
			}
		}
		return null;
	}
	
	public int getSkillIndexOf(int skillId){
		for(int i = 0 ; i < skills.length ; i++){
			if(skills[i] != null && skills[i].getId() == skillId){
				return i;
			}
		}
		return -1;
	}
	
	public boolean contains(Skill skill){
		for(int i = 0 ; i < skills.length ; i++){
			if(skills[i] == skill){
				return true;
			}
		}
		return false;
	}
	
	public int indexOfCareer(){
		return indexOfCareer;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int[] getLayerDivides() {
		return layerDivides;
	}

	public void setLayerDivides(int[] layerDivides) {
		this.layerDivides = layerDivides;
	}
	
	/**
	 * 获得此职业线中所有的技能
	 * @return
	 */
	public Skill[] getSkills(){
		return skills;
	}
	
	public void setSkills(Skill[] skills) {
		this.skills = skills;
	}

	/**
	 * 获得此职业线中所有的主动技能
	 * @return
	 */
	public ActiveSkill[] getActiveSkills(){
		Skill[] skills =  getSkills();
		ArrayList<ActiveSkill> al = new ArrayList<ActiveSkill>();
		for(int i = 0 ; i < skills.length ; i++){
			if(skills[i] instanceof ActiveSkill){
				al.add((ActiveSkill)skills[i]);
			}
		}
		return al.toArray(new ActiveSkill[0]);
	}
	
	/**
	 * 获得此职业线中所有的被动技能
	 * @return
	 */
	public PassiveSkill[] getPassiveSkills(){
		Skill[] skills =  getSkills();
		ArrayList<PassiveSkill> al = new ArrayList<PassiveSkill>();
		for(int i = 0 ; i < skills.length ; i++){
			if(skills[i] instanceof PassiveSkill){
				al.add((PassiveSkill)skills[i]);
			}
		}
		return al.toArray(new PassiveSkill[0]);
	}

	/**
	 * 获得此职业线中所有的光环技能
	 * @return
	 */
	public AuraSkill[] getAuraSkills(){
		Skill[] skills =  getSkills();
		ArrayList<AuraSkill> al = new ArrayList<AuraSkill>();
		for(int i = 0 ; i < skills.length ; i++){
			if(skills[i] instanceof AuraSkill){
				al.add((AuraSkill)skills[i]);
			}
		}
		return al.toArray(new AuraSkill[0]);
	}

}
