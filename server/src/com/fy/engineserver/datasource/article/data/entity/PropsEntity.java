package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.props.HunshiProps;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

/**
 * 道具实体
 * 
 */
@SimpleEntity
public class PropsEntity extends ArticleEntity {

	public static final long serialVersionUID = 235454754543052L;

	public PropsEntity() {
	}

	public PropsEntity(long id) {
		super(id);
	}

	/**
	 * 剩余使用次数
	 */

	int leftUsingTimes = 1;

	public int getLeftUsingTimes() {
		return leftUsingTimes;
	}

	public void setLeftUsingTimes(int leftUsingTimes) {
		this.leftUsingTimes = leftUsingTimes;
		saveData("leftUsingTimes");
	}

	/**
	 * 重写玩家获得物品详细信息
	 * @return
	 */
	public String getInfoShow(Player p) {
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			return Translate.道具 + ":" + articleName;
		}
		Article a = am.getArticle(articleName);
		StringBuffer sb = new StringBuffer();
		if (a instanceof Props) {
			sb.append("<f  size='28'>");
			sb.append(Translate.使用等级).append(":");
			int levelLimit = ((Props) a).getLevelLimit();
			if (levelLimit > 220) {
				sb.append(Translate.仙);
				levelLimit -= 220;
			}
			sb.append(levelLimit);
			sb.append("</f>\n");
			Props prop = (Props) a;
			if (prop instanceof HunshiProps) {
				HunshiProps hprop = (HunshiProps) prop;
				if (hprop.getIndex() != -1) {
					sb.append("<f  size='28'>").append(hprop.getIndex() + 1).append(Translate.窍).append("</f>\n");
				}
			}
		}
		String str = super.getCommonInfoShow(p);
		sb.append(str);
		return sb.toString();
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		int size = super.getSize();
		size += CacheSizes.sizeOfInt(); // leftUsingTimes
		return size;
	}

}
