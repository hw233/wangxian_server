package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;

public class Option_QingMing_ShaoXiang extends Option {

//	public String 绑银膜拜_奖励 = "宠物基础天赋包";
//	public long 绑银膜拜_消耗 = 100000;
	public String 经验膜拜_奖励 = "河图礼包";
	public long 经验膜拜_消耗 = 100000;
	public String 工资膜拜_奖励 = "玄牝封天锦囊";
	public long 工资膜拜_消耗 = 200000;
	public String 银子膜拜_奖励 = "豪华羽炼礼包";
	public long 银子膜拜_消耗 = 500000;
	public String 绑银膜拜_奖励 = "宠物基础天赋包";
	public long 绑银膜拜_消耗 = 100000;
//	public String 经验膜拜_奖励 = "基础通用天赋包";
//	public long 经验膜拜_消耗 = 100000;
//	public String 工资膜拜_奖励 = "宠物技能升级残片";
//	public long 工资膜拜_消耗 = 200000;
//	public String 银子膜拜_奖励 = "圣兽精魄礼包";
//	public long 银子膜拜_消耗 = 200000;
	public String selectType;
	public int windowId = 666666; 
	public String 绑银膜拜 = "绑银膜拜";
	public String 经验膜拜 = "经验膜拜";
	public String 工资膜拜 = "工资膜拜";
	public String 银子膜拜 = "银子膜拜";
	
	@Override
	public void doSelect(Game game, Player player) {
		if(player==null || game==null){
			player.sendError(Translate.服务器出现错误);
			return;
		}
		
		if(player.getLevel()<40){
			player.sendError(Translate.等级不符);
			return;
		}
		
		if(selectType==null){
			player.sendError(Translate.服务器出现错误+","+selectType);
			return;
		}
		
		String keyname = this.getText();
		ActivitySubSystem.logger.info("[圣兽赐福活动] ["+this.getSelectType()+"] [keyname:"+keyname+"] ["+player.getLogString()+"]");
		
		if(selectType.equals(绑银膜拜)){
			if(player.getBindSilver()<绑银膜拜_消耗){
				player.sendError(Translate.绑银不足);
				return;
			}
			doOncePrice(player, 绑银膜拜_奖励, keyname);
		}else if(selectType.equals(经验膜拜)){
			if(player.getExp()<经验膜拜_消耗){
				player.sendError(Translate.您的经验不足);
				return;
			}
			doOncePrice(player, 经验膜拜_奖励, keyname);
		}else if(selectType.equals(工资膜拜)){
			if(player.getWage()<工资膜拜_消耗){
				player.sendError(Translate.text_jiazu_033);
				return;
			}
			doOncePrice(player, 工资膜拜_奖励, keyname);
		}else if(selectType.equals(银子膜拜)){
			if(player.getSilver()<银子膜拜_消耗){
				player.sendError(Translate.银子不足);
				return;
			}
			doOncePrice(player, 银子膜拜_奖励, keyname);
		}
		
	}

	/**
	 * 每天一次
	 * @param player
	 * @param rewardname
	 * @param keyname
	 */
	public void doOncePrice(Player player, String rewardname,String keyname){
		Article a = ArticleManager.getInstance().getArticleByCNname(rewardname);
		if(a==null){
			if(ActivitySubSystem.logger.isInfoEnabled()){
				ActivitySubSystem.logger.info("[圣兽赐福活动] [领取奖励] [keyname:"+keyname+"] [物品不存在："+rewardname+"] ["+player.getLogString()+"]");
			}
			return;
		}
		
		Long lasttime = (Long)ActivityManagers.getInstance().getDdc().get(player.getId()+keyname);
		if(lasttime==null){
			ActivityManagers.getInstance().getDdc().put(player.getId()+keyname, 11L);
			lasttime = (Long)ActivityManagers.getInstance().getDdc().get(player.getId()+keyname);
		}
		if(selectType.equals(银子膜拜)==false){
			if(ActivityManagers.isSameDay(lasttime.longValue(), System.currentTimeMillis())){
				player.sendError("您今天已经"+keyname+"了");
				return;
			}
		}
		
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, player,a.getColorType(), 1, true);
			if(ae==null){
				if(ActivitySubSystem.logger.isInfoEnabled()){
					ActivitySubSystem.logger.info("[圣兽赐福活动] [领取奖励] [keyname:"+keyname+"] [创建物品失败："+rewardname+"] ["+player.getLogString()+"]");
				}
				return;
			}
			if(selectType.equals(绑银膜拜)){
				long oldbindsilver = player.getBindSilver();
				BillingCenter.getInstance().playerExpense(player, 绑银膜拜_消耗, CurrencyType.BIND_YINZI, ExpenseReasonType.活动, "圣兽赐福活动");
				ActivitySubSystem.logger.info("[圣兽赐福活动] [奖励成功] [keyname:"+keyname+"] [消耗："+绑银膜拜_消耗+"] [绑银:"+oldbindsilver+"-->"+player.getBindSilver()+"] [rewardname:"+rewardname+"] ["+player.getLogString()+"]");
			}else if(selectType.equals(经验膜拜)){
				long oldExp = player.getExp();
				player.setExp(oldExp-经验膜拜_消耗);
				ActivitySubSystem.logger.info("[圣兽赐福活动] [奖励成功] [keyname:"+keyname+"] [消耗："+经验膜拜_消耗+"] [经验:"+oldExp+"-->"+player.getExp()+"] [rewardname:"+rewardname+"] ["+player.getLogString()+"]");
			}else if(selectType.equals(工资膜拜)){
				long oldwage = player.getWage();
				player.setWage(player.getWage()-工资膜拜_消耗);
				ActivitySubSystem.logger.info("[圣兽赐福活动] [奖励成功] [keyname:"+keyname+"] [消耗："+工资膜拜_消耗+"] [工资:"+oldwage+"-->"+player.getWage()+"] [rewardname:"+rewardname+"] ["+player.getLogString()+"]");
			}else if(selectType.equals(银子膜拜)){
				long oldbindsilver = player.getBindSilver();
				BillingCenter.getInstance().playerExpense(player, 银子膜拜_消耗, CurrencyType.YINZI, ExpenseReasonType.活动, "圣兽赐福活动");
				ActivitySubSystem.logger.info("[圣兽赐福活动] [奖励成功] [keyname:"+keyname+"] [消耗："+银子膜拜_消耗+"] [银子:"+oldbindsilver+"-->"+player.getBindSilver()+"] [rewardname:"+rewardname+"] ["+player.getLogString()+"]");
			}
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, Translate.膜拜成功, Translate.膜拜成功, 0, 0, 0, "端午节活动");
			player.sendError(Translate.膜拜成功);
			ActivityManagers.getInstance().getDdc().put(player.getId()+keyname, System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

}
