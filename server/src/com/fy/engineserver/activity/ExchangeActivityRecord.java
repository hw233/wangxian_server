package com.fy.engineserver.activity;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.Utils;


//兑换花，糖增加 喝酒使用封魔录次数
public class ExchangeActivityRecord {

	
	public  long exchangeBeerTime;
	public int addBeerNum;
	
	public long exchangeTieTime;
	public int addTieNum;
	
	//兑换指定的类型0：酒  1：帖增加的次数   articleType"白玫瑰","蓝色妖姬","棒棒糖","巧克力"
	public boolean exchangeNum(Player player,int type,int articleType){
		
		int num = this.returnNum(player, type);
		if(num >= ActivityManager.maxNum){
//			player.sendError("每天兑换"+ActivityManager.des[type]+"次数只能增加"+ActivityManager.maxNum+"次，你今天已经完成，不能在兑换");
			String result = Translate.translateString(Translate.每天兑换XX次数只能增加XX次你今天已经完成不能在兑换, new String[][]{{Translate.STRING_1,ActivityManager.des[type]},{Translate.COUNT_1,ActivityManager.maxNum+""}});
			player.sendError(result);
			if(ActivitySubSystem.logger.isWarnEnabled()){
				ActivitySubSystem.logger.warn("[增加次数] [次数已满] ["+player.getLogString()+"] ["+type+"]");
			}
			return false;
		}
	
		String articleName = ActivityManager.articleName[articleType];
		
		int need = ActivityManager.consumeNum[articleType][num];
		
		int have = player.getArticleEntityNum(articleName);
		if(have >= need){
			for(int i= 0;i<need;i++){
				ArticleEntity ae = player.getArticleEntity(articleName);
				if(ae != null){
					ae = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "兑换次数", false);
					if(ae == null){
						ActivitySubSystem.logger.error("[兑换删除失败] ["+player.getLogString()+"] ["+articleName+"]");
						player.sendError(Translate.删除物品错误兑换失败);
						return false;
					}
				}else{
					ActivitySubSystem.logger.error("[兑换删除失败] ["+player.getLogString()+"] ["+articleName+"]");
					player.sendError(Translate.删除物品错误兑换失败);
					return false;
				}
			}
			
			if(type == 0){
				exchangeBeerTime = SystemTime.currentTimeMillis();
				addBeerNum++;
			}else if(type == 1){
				exchangeTieTime = SystemTime.currentTimeMillis();
				addTieNum++;
			}
			ActivitySubSystem.logger.warn("[兑换增加次数成功] ["+player.getLogString()+"] ["+type+"] [物品类型:"+articleType+"] ["+(++num)+"]");
			return true;
		}else{
			ActivitySubSystem.logger.warn("[本次兑换数量不够] ["+player.getLogString()+"] ["+articleName+"] ["+have+"] [需要:"+need+"]");
//			player.sendError("本次兑换需要"+need+"个，你包里不够，兑换失败");
			String result = Translate.translateString(Translate.本次兑换需要XX个你包里不够兑换失败, new String[][]{{Translate.STRING_1,need+""}});
			player.sendError(result);
		}
		return false;
	}
	//返回指定的类型0：酒  1：帖增加的次数
	public int returnNum(Player player ,int type){
		int num = 0;
		if(type == 0){
			if(exchangeBeerTime > 0){
				boolean same = Utils.isSameDay(SystemTime.currentTimeMillis(), exchangeBeerTime);
				if(same){
					num = addBeerNum;
				}
			}
			if(ActivitySubSystem.logger.isWarnEnabled()){
				ActivitySubSystem.logger.warn("[返回增加的次数] [酒] ["+player.getLogString()+"] ["+exchangeBeerTime+"] ["+addBeerNum+"] [实际次数:"+num+"]");
			}
		}else if(type == 1){
			if(exchangeTieTime > 0){
				boolean same = Utils.isSameDay(SystemTime.currentTimeMillis(), exchangeTieTime);
				if(same){
					num = addTieNum;
				}
			}
			if(ActivitySubSystem.logger.isWarnEnabled()){
				ActivitySubSystem.logger.warn("[返回增加的次数] [帖] ["+player.getLogString()+"] ["+exchangeTieTime+"] ["+addTieNum+"] [实际次数:"+num+"]");
			}
		}
		return num;
	}
	
}
