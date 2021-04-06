package com.fy.engineserver.menu.activity.oldplayer;


import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.translateString;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.oldPlayerComeBack.OldPlayerComeBackManager;
import com.fy.engineserver.activity.oldPlayerComeBack.OldPlayerInfo;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.uniteserver.UnitedServerManager;

public class Option_BackId_Input extends Option{
	private String activityKey;
	
	public Option_BackId_Input() {
		super();
	}

	public Option_BackId_Input(String activityKey) {
		super();
		this.activityKey=activityKey;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void doInput(Game game, Player player, String input) {
		if ("".equals(input)) {
			player.sendError(Translate.text_5272);
			return;
		}
		
		if(!input.matches("\\d*")){
			player.sendError(Translate.召回id是由纯数字组成的);
			return;
		}
		
		try{
			int num = Integer.parseInt(input);
		}catch(Exception e){
			String description = translateString(Translate.没有与召回ID相匹配的玩家, new String[][]{{STRING_1,input}});
			player.sendError(description);
			return;
		}
		OldPlayerComeBackManager ocb = OldPlayerComeBackManager.getInstance();
		OldPlayerInfo oldInfo = (OldPlayerInfo)ocb.getDdc().get(player.getId()+activityKey);
		
		if(oldInfo!=null && oldInfo.getOLD_PLAYER_TYPE()!=2){
			player.sendError(Translate.没有登录了的玩家才能填写召回id);
			return;
		}
		
		//如果通过召回id找不到对应的召回玩家，AB类玩家都不发奖
		Long playerId = (Long) OldPlayerComeBackManager.getInstance().getDdc().get(UnitedServerManager.getInstance().getPlayerComebackKey() + Integer.parseInt(input));
		if(playerId==null){
			String description = translateString(Translate.没有与召回ID相匹配的玩家, new String[][]{{STRING_1,input}});
			player.sendError(description);
			return;
		}
		
		if(playerId.longValue()==player.getId()){
			player.sendError(Translate.召回id不能是自己);
			return;
		}
		
		
		
		{// B类玩家奖励
			try {
				Article a = ArticleManager.getInstance().getArticleByCNname(Translate.回归锦囊);
				if(a!=null){
					ArticleEntity ae =  ArticleEntityManager.getInstance().createEntity(a, true,ArticleEntityManager.老玩家回归,player, a.getColorType(), 1, true);
					if(ae!=null){
						MailManager.getInstance().sendMail(
								player.getId(),
								new ArticleEntity[]{ae},
								Translate.飘渺寻仙曲欢迎您回家_title,
								Translate.飘渺寻仙曲欢迎您回家_content,
								0, 0, 0, "回归礼包");
						player.sendError(Translate.飘渺寻仙曲欢迎您回家请去邮件领取回归奖励);
						OldPlayerInfo info = (OldPlayerInfo) OldPlayerComeBackManager.getInstance().getDdc().get(player.getId() + activityKey);
						if (info == null) {
							info = new OldPlayerInfo();
							OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [B类玩家奖励] 【创建新的数据】 ["+player.getLogString()+"]");
						}
						info.setOLD_PLAYER_TYPE(1);
//						ActivityManagers.lastClickTime = System.currentTimeMillis()-1;
						OldPlayerComeBackManager.getInstance().getDdc().put(player.getId() + activityKey, info);
						OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [B类玩家奖励 [发送奖励成功] [被召回人信息："+player.getLogString()+"] ");
					}
				}else{
					OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [B类玩家奖励] [出错] ["+Translate.回归锦囊+"]");
				}
			} catch (Exception e) {
				e.printStackTrace();
				OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [B类玩家奖励] [异常] [" + e + "]");
			}
		}
		
		{// A类玩家奖励
			if (playerId != null) {
				String rewardNames [] = {Translate.召回5人奖励锦囊,Translate.召回10人奖励锦囊,Translate.召回20人奖励锦囊,Translate.召回50人奖励锦囊};
				int rewardIndex [] = {1,10,20,50};
				int gongzi[] = {100,200,500,2000};
				try {
					Player p = PlayerManager.getInstance().getPlayer(playerId.longValue());
					if (p != null) {
						OldPlayerInfo info = (OldPlayerInfo) OldPlayerComeBackManager.getInstance().getDdc().get(p.getId() + activityKey);
						int nums = info.getPlayerBackNums() + 1;
						int rewardnums = info.getRewardNums() + 1;
						// 获取对应的奖励
						boolean isreward = false;
						int rewardindex = 0;
						for(int i=0;i<rewardIndex.length;i++){
							if(rewardIndex[i]==nums){
								isreward = true;
								rewardindex = i;
								break;
							}
						}
						
						if(isreward){
							String rewardname = rewardNames[rewardindex];
							Article a = ArticleManager.getInstance().getArticle(rewardname);
							if(a!=null){
								ArticleEntity ae =  ArticleEntityManager.getInstance().createEntity(a, true,ArticleEntityManager.老玩家回归,p, a.getColorType(), 1, true);
								if(ae!=null){
									p.setWage(p.getWage()+1000*gongzi[rewardindex]);
									MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[]{ae}, Translate.translateString(Translate.恭喜您召回满_title, new String[][] { { Translate.COUNT_1, nums+"" } }),
											Translate.translateString(Translate.恭喜您召回满_content, new String[][] { { Translate.COUNT_1, nums+"" } , { Translate.STRING_1, rewardname } , { Translate.STRING_2, gongzi[rewardindex]+"" } }), 0, 0,0, "老玩家回归活动");
								}
							}
						}
						p.setWage(p.getWage()+100000);
						MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[]{}, Translate.恭喜您成功召回了1位战友_title,
								Translate.恭喜您成功召回了1位战友_content, 0, 0,0, "老玩家回归活动");
						
						p.sendError(Translate.恭喜您成功召回一位老友请去邮件查收您的召回奖励);
						OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [A类玩家奖励 [发送奖励成功] [召回人信息："+p.getLogString()+"] [被召回人信息："+player.getLogString()+"] ");
						info.setPlayerBackNums(nums);
						info.setRewardNums(rewardnums);
						OldPlayerComeBackManager.getInstance().getDdc().put(p.getId() + activityKey, info);
					}else{
						OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [A类玩家奖励] [异常] [通过玩家id活动player错误]");
					}
				} catch (Exception e) {
					e.printStackTrace();
					OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [A类玩家奖励] [异常] [" + e + "]");
				}
			}
		}
		

	}

	public int getRewardIndex(int rewardIndex [],int reward){
		int index = -1;
		int sum = 0;
		for(int i = 0;i<rewardIndex.length;i++){
			sum += rewardIndex[i]; 
			if(sum >= reward){
				index = i;
				break;
			}
		}
		return index;
	}

	public String getActivityKey() {
		return activityKey;
	}

	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}
	
	
}
