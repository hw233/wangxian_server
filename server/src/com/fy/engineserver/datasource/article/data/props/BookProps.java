package com.fy.engineserver.datasource.article.data.props;

import org.slf4j.Logger;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Player;

/**
 * 技能书
 */
public class BookProps extends Props{

	int skillId;
	
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
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
		Logger log = Skill.logger;
		log.error("BookProps.use: {} want use {}", player.getName(), ae.getArticleName());
		if(!super.use(game,player,ae)){
			log.debug("BookProps.use: super stop.");
			return false;
		}
		CareerManager cm = CareerManager.getInstance();
		Career career = cm.getCareer(player.getCareer());
		Skill skill = career.getSkillById(skillId);
		if(skill != null){
			int needP = skill.getNeedPoint()[0];
			log.debug("BookProps.use: book conf need p {}", needP);
			player.setUnallocatedSkillPoint(player.getUnallocatedSkillPoint() + needP);
			player.tryToLearnSkill(skillId,false,true);
			log.debug("BookProps.use: finish try to learn.");
		}else{
			log.debug("BookProps.use: skill is null");
		}
		
		if(ArticleManager.logger.isDebugEnabled()){
//			ArticleManager.logger.debug("[使用道具] [书籍道具] [成功] ["+player.getName()+"] ["+player.getId()+"] ["+this.getName()+"] ["+getComment()+"] [直接增加人物技能] ["+skillId+"]");
			if(ArticleManager.logger.isDebugEnabled())
				ArticleManager.logger.debug("[使用道具] [书籍道具] [成功] [{}] [{}] [{}] [{}] [直接增加人物技能] [{}]", new Object[]{player.getName(),player.getId(),this.getName(),getComment(),skillId});
		}
		return true;
	}
	@Override
	public String canUse(Player p) {
		// TODO Auto-generated method stub
		String result = super.canUse(p);
		if(result == null){
			CareerManager cm = CareerManager.getInstance();
			Career career = cm.getCareer(p.getCareer());
			result = career.isUpgradable(p, skillId, true);
		}
		return result;
	}
}
