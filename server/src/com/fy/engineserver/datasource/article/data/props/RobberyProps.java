package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 渡劫专属物品
 *
 */
public class RobberyProps extends Props{
	/** 粒子光效 */
	private String liziguangxiao;
	/** 增加buff的名字 */
	private String buffName;
	/** buff持续时间 */
	private int invalidTime;

	@Override
	public String canUse(Player p) {
		// TODO Auto-generated method stub
		String result = super.canUse(p);
		if(result == null){
			if(!TransitRobberyEntityManager.getInstance().isPlayerInRobbery(p.getId())){
				result = Translate.渡劫道具失败;
			}
		}
		return result;
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		// TODO Auto-generated method stub
		if(!super.use(game, player, ae)){
			return false;
		}
		TransitRobberyManager.getInstance().useRobberyProp(player, ae, liziguangxiao, buffName, invalidTime);		//使用效果
		return true;
	}

	public String getLiziguangxiao() {
		return liziguangxiao;
	}

	public void setLiziguangxiao(String liziguangxiao) {
		this.liziguangxiao = liziguangxiao;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public int getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(int invalidTime) {
		this.invalidTime = invalidTime;
	}

}
