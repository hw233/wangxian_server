package com.fy.engineserver.menu.spirit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.arborday.ArborDayManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;

/**
 * 消耗工资或经验或道具，兑换历练或经验
 * 
 * 
 */
public class Option_Activity_Exchange extends Option implements NeedCheckPurview{
	
	/** 开始和结束时间 */
	private String startTimeStr;
	private String endTimeStr;
	/**
	 * reduceType 消耗类型：exp经验，wage工资，articleName道具名称
	 * reduceNum 消耗数量：经验直接是数值，工资单位是文，道具是个数
	 * exchangeType 获得类型：lilian历练，yuanqi元气
	 * exchangeNum 获得数量：直接是数值
	 * maxTime 每天最多几次
	 */
	private String reduceType;
	private long reduceNum;
	private String exchangeType;
	private long exchangeNum;
	public int maxTime;
	public String succMess = Translate.膜拜成功;

	/** 角色使用记录 */
	public static Map<String, Map<Long, Integer>> playerUseRecord = new HashMap<String, Map<Long, Integer>>();
	
	@Override
	public void doSelect(Game game, Player player) {
		
		String today = TimeTool.formatter.varChar10.format(new Date());
		if (playerUseRecord.containsKey(today) && playerUseRecord.get(today).containsKey(player.getId()) && playerUseRecord.get(today).get(player.getId()) >= maxTime) {
			player.sendError(Translate.translateString(Translate.已经完成xx次,new String[][]{{Translate.STRING_1, maxTime+""}}));
			return;
		}
		{
			/**扣*/
			if(reduceType.equals("exp")){
				if (player.getExp() < reduceNum) {
					player.sendError(Translate.不能烧香);
					return;
				}
				player.subExp(reduceNum, "百鬼夜行");
//				player.sendError(Translate.烧香成功+reduceNum);
				player.sendError(succMess);
				if (ActivitySubSystem.logger.isInfoEnabled()) {
					ActivitySubSystem.logger.warn("[百鬼夜行][" + player.getLogString() + "] [消耗经验:" + reduceNum + "]");
				}
			}else if(reduceType.equals("wage")){
				if (player.getWage() < reduceNum) {
					player.sendError(Translate.不能点蜡);
					return;
				}
				try {
					BillingCenter.getInstance().playerExpense(player, reduceNum, CurrencyType.GONGZI, ExpenseReasonType.活动, "百鬼夜行");
					player.sendError(Translate.点蜡成功+BillingCenter.得到带单位的银两(reduceNum));
					if (ActivitySubSystem.logger.isInfoEnabled()) {
						ActivitySubSystem.logger.warn("[百鬼夜行][" + player.getLogString() + "] [消耗工资:" + reduceNum + "]");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					return;
				}
			}else{
				Article article = ArticleManager.getInstance().getArticle(reduceType);
				if (article == null) {
					player.sendError(reduceType + "不存在");
					return;
				}
				int hasNum = player.getArticleNum(reduceType, article.getColorType(), BindType.BOTH);
				if (hasNum <= 0) {
					player.sendError(Translate.不能送元宝);
					return;
				}
				player.removeArticleByNameColorAndBind(reduceType, article.getColorType(), BindType.BOTH, "百鬼夜行", true);
				player.sendError(Translate.送元宝成功+reduceType+"*"+reduceNum);
				if (ActivitySubSystem.logger.isInfoEnabled()) {
					ActivitySubSystem.logger.warn("[百鬼夜行][" + player.getLogString() + "] [消耗"+reduceType+"*"+reduceNum+ "]");
				}
			}
		}
		
		{
			/**发奖励*/
			if(exchangeType.equals("yuanqi")){
				if (exchangeNum > 0) {
					player.setEnergy(player.getEnergy() + (int)exchangeNum);
					String msg = Translate.translateString(Translate.获得修法值,new String[][]{{Translate.STRING_1, exchangeNum+""}});
					player.sendError(msg);
					record(player.getId(), today);
					if (ActivitySubSystem.logger.isInfoEnabled()) {
						ActivitySubSystem.logger.warn("[百鬼夜行][" + player.getLogString() + "] [获得元气:" + exchangeNum + "] [总元气:"+player.getEnergy()+"]");
					}
				}
			}else if(exchangeType.equals("lilian")){
				
			}else{
				//兑换得到啥？
			}
		}
	}
	

	public synchronized void record(long playerId, String day) {
		if (!playerUseRecord.containsKey(day)) {
			playerUseRecord.put(day, new HashMap<Long, Integer>());
		}
		if (!playerUseRecord.get(day).containsKey(playerId)) {
			playerUseRecord.get(day).put(playerId, 0);
		}
		playerUseRecord.get(day).put(playerId, playerUseRecord.get(day).get(playerId) + 1);
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		long startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		long endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		
		long now = SystemTime.currentTimeMillis();
		if(startTime <= now && now <= endTime){
			return true;
		}
		return false;
	}

	public String getReduceType() {
		return reduceType;
	}

	public void setReduceType(String reduceType) {
		this.reduceType = reduceType;
	}

	public long getReduceNum() {
		return reduceNum;
	}

	public void setReduceNum(long reduceNum) {
		this.reduceNum = reduceNum;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}

	public long getExchangeNum() {
		return exchangeNum;
	}

	public void setExchangeNum(long exchangeNum) {
		this.exchangeNum = exchangeNum;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public static Map<String, Map<Long, Integer>> getPlayerUseRecord() {
		return playerUseRecord;
	}

	public static void setPlayerUseRecord(Map<String, Map<Long, Integer>> playerUseRecord) {
		Option_Activity_Exchange.playerUseRecord = playerUseRecord;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}
}
