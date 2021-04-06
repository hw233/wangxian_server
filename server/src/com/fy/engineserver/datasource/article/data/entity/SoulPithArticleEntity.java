package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.articleProtect.ArticleProtectData;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.articleProtect.PlayerArticleProtectData;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticle;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticleLevelData;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.soulpith.SoulPithConstant;
import com.fy.engineserver.soulpith.SoulPithManager;
import com.fy.engineserver.soulpith.instance.SoulPithAeData;
import com.fy.engineserver.soulpith.module.SoulLevelupExpModule;
import com.fy.engineserver.soulpith.property.Propertys;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

/**
 * 灵髓道具
 */
@SimpleEntity
public class SoulPithArticleEntity extends ArticleEntity{

	private static final long serialVersionUID = 1L;
	
	private transient SoulPithAeData extraData;
	
	public SoulPithArticleEntity() {
	}
	public SoulPithArticleEntity(long id) {
		this.id = id;
	}

	public int getLevel() {
		if (extraData != null) {
			return extraData.getPithLevel();
		}
		return 1;			//默认为1级
	}
	
	public String getLogString() {
		return "id:" + this.getId() + "] [name:" + this.getArticleName() + "] [level:" + this.getLevel() + "] [exp:" + this.getExp() +"";
	}
	
	public long getExp() {
		if (extraData != null) {
			return extraData.getExps();
		}
		return 0;			
	}
	
	@Override
	public String getInfoShow(Player p) {
		try {
			StringBuffer sb = new StringBuffer();
			PlayerArticleProtectData padata = ArticleProtectManager.instance.getPlayerData(p);
			if (padata != null) {
				ArticleProtectData da = padata.getProtectData(this);
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
						sb.append("\n<f color='0xFFFF00' size='28'>").append(daString).append("</f>").append("\n");
					}
				}
			}
			
			sb.append("<f size='28'>").append(Translate.等级).append(":").append(this.getLevel()).append("</f>");
			SoulPithArticle a = (SoulPithArticle) ArticleManager.getInstance().getArticle(this.getArticleName());
			SoulPithArticleLevelData data = a.getLevelDatas().get(this.getLevel());
			SoulLevelupExpModule sem = SoulPithManager.getInst().soulLevelModules.get(this.getLevel());
			SoulPithArticleLevelData data2 = a.getLevelDatas().get(this.getLevel()+1);
//			for (int i=0; i<data.getTypes().length; i++) {
//				sb.append("\n").append(data.getTypes()[i].getInfoShow()).append("+").append(data.getSoulNums()[i]);
//			}
			
			long exp = this.getExp();
			int[] propTypes = data.getProperTypes();
			int[] propNums = new int[propTypes.length];
			for (int j=0; j<data.getProperTypes().length; j++) {
				int num = (int) (data.getProperNums()[j] * SoulPithConstant.COLOR_RATE[this.getColorType()]);			
				propNums[j] = num;
				if (exp > 0 && data2 != null) {
					int num2 = (int) (data2.getProperNums()[j] * SoulPithConstant.COLOR_RATE[this.getColorType()]);
					float rate = (float)exp / (float)sem.getNeedExp();
					propNums[j] += (int)((num2 - num) * rate);
				}
			}
//			sb.append("\n");
			for (int i=0; i<propTypes.length; i++) {
				sb.append("\n").append("<f color='0x0FD100' size='28'>").append("").append(Propertys.valueOf(propTypes[i]).getName()).append(":").append(propNums[i]).append("</f>");
			}
			if (this.getLevel() < SoulPithConstant.SOUL_MAX_LEVEL) {
				sb.append("\n").append("<f size='28'>").append(Translate.仙气).append(":").append(exp).append("/").append(sem.getNeedExp()).append("</f>");
			} else {
				sb.append("\n").append("<f size='28'>").append(Translate.已达到最高级).append("</f>");
			}
			for (int i=0; i<data.getTypes().length; i++) {
				sb.append("\n").append(data.getTypes()[i].getInfoShow()).append("+").append(data.getSoulNums()[i]);
			}
			sb.append("\n").append("\n").append(a.getDescription());
			return sb.toString();
		} catch (Exception e) {
			SoulPithManager.logger.warn("[获取泡泡] [异常] [" + p.getLogString() + "] [" + this.getLogString() + "]", e);
			return Translate.服务器数据异常;
		}
	}
	
	public SoulPithAeData getExtraData() {
		return extraData;
	}

	public void setExtraData(SoulPithAeData extraData) {
		this.extraData = extraData;
	}
	
}
