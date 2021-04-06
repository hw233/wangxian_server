package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;

/**
 * 特殊道具：根骨丹道具
 * 
 *
 */
public class GenguDanProps extends Props implements Gem{
	
	//根骨丹等级
	private int level;
	
	/**
	 * 得到根骨丹等级
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isUsedUndisappear() {
		return true;
	}

	/**
	 * 使用方法(重写父类方法)
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity aee){
		
		return false;
	}

	/**
	 * 判断某个玩家是否可以使用此物品 子类可以重载此方法
	 * 返回null表示可以使用
	 * 返回字符串表示不能使用
	 * 字符串为不能使用的详细信息
	 * @param p
	 * @return
	 */
	public String canUse(Player p) {

		String resultStr = super.canUse(p);
		return resultStr;
	}

	public String getComment(){
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}
