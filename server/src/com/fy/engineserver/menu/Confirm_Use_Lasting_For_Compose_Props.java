package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Lasting_For_Compose_Props;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.vip.VipManager;
/**
 * 确认使用Lasting_For_Compose_Props道具
 * 
 * 
 *
 */
public class Confirm_Use_Lasting_For_Compose_Props extends Option{

	long id;
	BuffTemplate bt;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BuffTemplate getBt() {
		return bt;
	}

	public void setBt(BuffTemplate bt) {
		this.bt = bt;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntity ae = player.getArticleEntity(id);
		if(ae != null){
			Article a = am.getArticle(ae.getArticleName());
			if(a instanceof Lasting_For_Compose_Props){
				((Lasting_For_Compose_Props)a).plantBuffOnLivingObject(bt,player,player,ae);
			}
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_TUOYUN;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
