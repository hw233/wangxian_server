package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.talent.FlyTalentManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;

/**
 * 简单的道具，直接用于修改属性值
 *
 */
public class SingleProps extends Props{

	/**
	 * 此简单道具要修改的属性，以及增加的值
	 * 修改都是加法运算
	 * 按照规定的顺序给玩家修改属性
	 * 数组顺序:hp,mp,exp
	 */
	@SimpleColumn(mysqlName="SingleValues")
	protected int []values;

	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		this.values = values;
	}
@Override
public int getKnapsackType() {
	// TODO Auto-generated method stub
	return Article.KNAP_奇珍;
}
	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity ae){
//		if(TransitRobberyEntityManager.getInstance().isPlayerInSixiang(player)) {
//			player.sendError(Translate.渡劫不允许用药);
//			return false;
//		}
		if(!super.use(game,player,ae)){
			return false;
		}
		if (CareerManager.兽魁进阶道具.equals(this.getName_stat())) {
			if (player.getCareer() != 5) {
				player.sendError(Translate.只有兽魁可以使用);
				return false;
			} else if (player.getPlayerRank() != 1) {
				player.sendError(Translate.需要进阶后使用);
				return false;
			} else {
				player.setPlayerRank(2);
				Game.logger.warn("[兽魁进阶] [成功] [" + player.getLogString() + "]" );
			}
		}
		changePlayerProperty(player);
		
		if(ArticleManager.logger.isDebugEnabled()){
//			ArticleManager.logger.debug("[使用道具] [简单道具] [成功] ["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"] [直接改变人物属性]");
			if(ArticleManager.logger.isDebugEnabled())
				ArticleManager.logger.debug("[使用道具] [简单道具] [成功] [{}] [{}] [{}] [直接改变人物属性]", new Object[]{player.getName(),this.getName(),getComment()});
		}
		return true;
	}
	
	/**
	 * 修改玩家属性
	 * @param player
	 */
	public void changePlayerProperty(Player player){
		if(this.getName().equals(Translate.渡劫回魂丹)){
			TransitRobberyManager.getInstance().使用渡劫回魂丹(player);
			return;
		}
		if(player != null && values != null){
			for(int i = 0; i < values.length; i++){
				int value = values[i];
				
				
				if(value != 0){
					switch(i){
					case 0 :
						//血
//						value -= value*player.minusHp()/100;
//						if(Skill.logger.isDebugEnabled()){
//							Skill.logger.debug("[技能触发buff] [降低治疗的buff] [value:"+value+"] [1] [血]");
//						}
						int pvalue = player.minusHp();
						value -= value*pvalue/100;
						if(Skill.logger.isDebugEnabled()){
							Skill.logger.debug("[技能触发buff] [降低治疗的buff] [value:"+value+"] [pvalue:"+pvalue+"] [2] [血]");
						}
						value += FlyTalentManager.getInstance().handle_仙疗(player, value);
						if(player.isCanNotIncHp()){
							Skill.logger.debug("[无法回血状态] [屏蔽仙疗加血] ["+player.getLogString()+"] [血]");
							break;
						}
						player.setHp(player.getHp() + value);
						if(player.getHp() < 0){
							player.setHp(0);
						}else if(player.getHp() > player.getMaxHP()){
							player.setHp(player.getMaxHP());
						}
						break;
					case 1 :
						//蓝 
//						value -= value*player.minusHp()/100;
//						if(Skill.logger.isDebugEnabled()){
//							Skill.logger.debug("[技能触发buff] [降低治疗的buff] [value:"+value+"] [1] [蓝]");
//						}
						int pvalue2 = player.minusHp();
						value -= value*pvalue2/100;
						if(Skill.logger.isDebugEnabled()){
							Skill.logger.debug("[技能触发buff] [降低治疗的buff] [value:"+value+"] [pvalue2:"+pvalue2+"] [2] [蓝]");
						}
						value += FlyTalentManager.getInstance().handle_仙疗(player, value);
						player.setMp(player.getMp() + value);
						if(player.getMp() < 0){
							player.setMp(0);
						}else if(player.getMp() > player.getMaxMP()){
							player.setMp(player.getMaxMP());
						}
						break;
					case 2 :
						//经验
						player.addExp(value, ExperienceManager.ADDEXP_REASON_SINGLE_PROPS);
						break;
					case 3 :
						//元气
						player.setEnergy(player.getEnergy() + value);
						ArticleManager.logger.warn(player.getLogString() + " [使用渡劫回魂丹] [增加元气:" + value + "] [总元气:" + player.getEnergy() + "]");
						break;
					}
				}
			}
		}
	}
}
