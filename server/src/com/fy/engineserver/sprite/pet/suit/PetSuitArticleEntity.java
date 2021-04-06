package com.fy.engineserver.sprite.pet.suit;

import com.fy.engineserver.articleProtect.ArticleProtectData;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.articleProtect.PlayerArticleProtectData;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
/**
 * 宠物套装
 * 
 * @date on create 2016年8月26日 上午10:01:17
 */
@SimpleEntity
public class PetSuitArticleEntity extends ArticleEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getInfoShow(Player player) {
		try {
			ArticleManager am = ArticleManager.getInstance();
			PetSuitArticle a = (PetSuitArticle) am.getArticle(this.getArticleName());
			StringBuffer sb = new StringBuffer();
			sb.append("<f  size='28'>");
			sb.append(Translate.使用等级).append(":");
			int levelLimit = a.getPetLevelLimit();
			if (levelLimit > 220) {
				sb.append(Translate.仙);
				levelLimit -= 220;
			}
			
			sb.append(levelLimit);
			sb.append("</f>\n");
			/*if (a.getCharacter() > 0) {
				sb.append("<f  size='28'>").append(Translate.宠物性格).append(PetManager.宠物性格[a.getCharacter()]).append("</f>\n");
			}
			if (a.getPetEggName() != null) {
				sb.append("<f  size='28'>").append(Translate.宠物限制).append(a.getPetEggName()).append("</f>\n");
			}*/
			if (isBinded() /* && (a instanceof EnchantArticle == false) */) {
				sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.已绑定).append("</f>");
			}
			PlayerArticleProtectData data = ArticleProtectManager.instance.getPlayerData(player);
			if (data != null) {
				ArticleProtectData da = data.getProtectData(this);
				if (da != null) {
					String daString = null;
					if (da.getState() == ArticleProtectDataValues.ArticleProtect_State_Common) {
						daString = Translate.以锁魂;
					} else if (da.getState() == ArticleProtectDataValues.ArticleProtect_State_High) {
						daString = Translate.以高级锁魂;
					}
					if (da.getRemoveTime() > 0) {
						daString = Translate.正在解魂;
						long haveTime = da.getRemoveTime() - System.currentTimeMillis();
						if (haveTime < 0) {
							haveTime = 0;
						}
						daString += ":" + (haveTime / 1000 / 60 / 60) + Translate.小时 + (haveTime / 1000 / 60 % 60) + Translate.分钟;
					}
					if (daString != null) {
						sb.append("\n <f color='0xFFFF00' size='28'>").append(daString).append("</f>");
					}
				}
			}
			if (getTimer() != null) {
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.有效期).append("：");
				long time = getTimer().getEndTime() - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				long day = time / 1000 / 60 / 60 / 24;
				long hour = time / 1000 / 60 / 60 % 24;
				if (day > 0) {
					sb.append(day).append(Translate.text_jiazu_114);
				}
				if (hour > 0) {
					sb.append(hour).append(Translate.text_小时);
				}
				if (day <= 0 && hour <= 0) {
					sb.append(Translate.text_不足1小时);
				}
				sb.append("</f>");
			}
			sb.append(a.getInfoShowBody(player));
			return sb.toString();
		} catch (Exception e) {
			ArticleManager.logger.warn("[宠物饰品] [泡泡异常] [" + this.getArticleName() + "]", e);
		}
		return "服务器错误";
	}

}
