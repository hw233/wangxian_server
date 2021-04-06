package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;

/**
 * 特殊道具：法宝幻化
 * 
 *
 */
public class MagicWeaponProps extends Props implements Gem{

	protected int colorType;
	
	protected int mappingType = 0;
	
	public int getColorType() {
		if(colorType < 2){
			return 2;
		}
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public int getMappingType() {
		return mappingType;
	}

	public void setMappingType(int mappingType) {
		this.mappingType = mappingType;
	}

//	public void setPropsType(byte propsType) {
//		// TODO Auto-generated method stub
//		super.setPropsType(Article.TYPE_MAGIC_PROPS);
//	}

//	public byte getPropsType() {
//		// TODO Auto-generated method stub
//		return Article.TYPE_MAGIC_PROPS;
//	}
	
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
	 * 从玩家身上卸下
	 * @param player
	 */
	public void unloaded(Player player, ArticleEntity ee) {}

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
