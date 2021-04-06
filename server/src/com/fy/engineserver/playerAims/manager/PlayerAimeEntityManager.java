package com.fy.engineserver.playerAims.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.GameDataRecord;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_PARTICLE_REQ;
import com.fy.engineserver.message.NOTICE_PLAYER_MUBIAO_CHANGE_RES;
import com.fy.engineserver.message.QUERY_COMPLETE_AIM_RES;
import com.fy.engineserver.playerAims.instance.PlayerAim;
import com.fy.engineserver.playerAims.instance.PlayerAimsEntity;
import com.fy.engineserver.playerAims.instance.PlayerChapter;
import com.fy.engineserver.playerAims.model.ChapterModel;
import com.fy.engineserver.playerAims.model.PlayerAimModel;
import com.fy.engineserver.playerAims.model.RewordArticle;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class PlayerAimeEntityManager {
	public static Logger logger = PlayerAimManager.logger;
	
	public static SimpleEntityManager<PlayerAimsEntity> em;
	
	public static PlayerAimeEntityManager instance;
	
	public LruMapCache cache = new LruMapCache(10240,60 * 60 * 1000,false, "玩家目标");
	
	public Map<Long, PlayerAimsEntity> tempCache = new Hashtable<Long, PlayerAimsEntity>();
	
	private Object total_lock = new Object();
	
	private Map<Long, Object> p_lock = new Hashtable<Long, Object>();
	
	public static final byte UN_RECEIVE = 0;
	public static final byte NOMAL_RECEIVE = 1;
	public static final byte VIP_RECEIVE = 2;
	public static final byte VIP_AND_NOMAL_RECEIVE = 3;
	
	public void init() {
		instance = this;
		
		em = SimpleEntityManagerFactory.getSimpleEntityManager(PlayerAimsEntity.class);
		ServiceStartRecord.startLog(this);
	}
	
	public int getChapterCanReceiveAmount(Player player, String chapterName) {
		try {
			int result = 0;
			ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(chapterName);
			PlayerAimsEntity entity = this.getEntity(player.getId());
			for (PlayerAim pa : entity.getAimList()) {
				for (PlayerAimModel pam : cm.getAimsList()) {
					if (pa.getAimId() == pam.getId() ) {
						if (this.checkReceiveReward(player, pa.getAimId(), chapterName, false).getBooleanValue()) {
							result ++ ;
						} else if (pam.getMulReward4Vip() > 0 && this.checkReceiveReward(player, pa.getAimId(), chapterName, true).getBooleanValue()) {
							result ++ ;
						}
						break;
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("[目标系统] [获取章节可领取奖励个数] [可领取奖励个数:" + result + "] [" + player.getLogString() + "]");
			}
			return result;
		} catch (Exception e) {
			logger.error("[目标系统] [获取章节可领取奖励个数] [异常] [" + player.getLogString() + "] [chapterName:" + chapterName + "]", e);
			return 0;
		}
	}
	/**
	 * 检测玩家是否有为领取的可领取奖励
	 * @param player
	 * @return  true代表有可领取但未领取的奖励，亮粒子
	 */
	public int checkPlayerAimReceiveStatus(Player player) {
		int result = 0;
		PlayerAimsEntity entity = this.getEntity(player.getId());
		result = entity.checkReceiveStatus();
		return result;
	}
	
	/**
	 * 获取玩家章节获取到的总积分
	 * @param player
	 * @param chapterName
	 * @return
	 */
	public int getChapterScore(Player player, String chapterName) {
		ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(chapterName);
		if(cm == null) {
			logger.warn("[目标系统] [查询目标总积分] [失败] [配表中没有此章节] [" + player.getLogString() + "] [章节名" +chapterName + "]");
			return 0 ;
		}
		int num = -1;
		PlayerAimsEntity entity = this.getEntity(player.getId());
		for(PlayerChapter pc : entity.getChapterList()) {
			if(pc.getChapterName().equals(chapterName)) {
				num = pc.getScore();
				break;
			}
		}
		if (num <= 0) {
//			synchronized (player) {
				num = entity.updateChapterScore(cm, true);
//			}
		}
		
		return num;
	}
	public int getChapterComplateNum(Player player, String chapterName) {
		ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(chapterName);
		if(cm == null) {
			logger.warn("[目标系统] [查询目标总积分] [失败] [配表中没有此章节] [" + player.getLogString() + "] [章节名" +chapterName + "]");
			return 0 ;
		}
		int num = 0;
		PlayerAimsEntity entity = this.getEntity(player.getId());
		for(PlayerAimModel pc : cm.getAimsList()) {
			PlayerAim[] bons = entity.getAimList().toArray(new PlayerAim[entity.getAimList().size()]);
			for (PlayerAim pa : bons) {
				if (pa.getAimId() == pc.getId()) {
					num++;
				}
			}
		}
		
		return num;
	}
	

	/**
	 * 获取章节奖励领取状态
	 * @param player
	 * @param chapterName
	 * @return
	 */
	public byte getChapterReceiveStatus(Player player, String chapterName) {
		byte status = 0;
		PlayerAimsEntity entity = this.getEntity(player.getId());
		status = entity.getChapterReceiveStatus(chapterName);
		if(logger.isDebugEnabled()) {
			logger.debug("[目标系统] [获取章节奖励领取状态] [" + player.getLogString() + "] [" + chapterName + "] [" + status + "]");
		}
		return status;
	}
	/**
	 * 获取每条目标奖励领取状态
	 * @param player
	 * @param chapterName
	 * @return
	 */
	public byte getAimReceiveStatus(Player player, int aimId) {
		byte status = 0;
		PlayerAimsEntity entity = this.getEntity(player.getId());
		status = entity.getAimReceiveStatus(aimId);
		if(logger.isDebugEnabled()) {
			logger.debug("[目标系统] [获取目标奖励领取状态] [" + player.getLogString() + "] [" + aimId + "] [" + status + "]");
		}
		return status;
	}
	public long getAimCompleteTime(Player player, int aimId) {
		long status = 0;
		PlayerAimsEntity entity = this.getEntity(player.getId());
		status = entity.getAimCompleteTime(aimId);
		if(logger.isDebugEnabled()) {
			logger.debug("[目标系统] [获取目标奖励领取状态] [" + player.getLogString() + "] [" + aimId + "] [" + status + "]");
		}
		return status;
	}
	/**
	 * 检查玩家是否达成了成就
	 * @param player
	 * @param gdr
	 */
	public void checkPlayerAims(Player player, GameDataRecord gdr, boolean isNotify) {
		long now = System.currentTimeMillis();
		if(player == null || gdr == null) {
			logger.error("[目标系统] [checkPlayerAims] [参数为NULL] [player:{}] [GameDataRecord:{}]", new Object[] { player, gdr });
			throw new IllegalArgumentException();
		}
		List<PlayerAimModel> plmList = PlayerAimManager.instance.systemAimsMap.get(gdr.getDataType());
		logger.warn("[checkPlayerAims] [plmList:"+plmList+"] ["+player.getName()+"]");
		if(plmList == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("[目标系统] [" + player.getLogString() + "] [对于数据类型:{} 无配置的目标]", new Object[] { gdr.getDataType() });
			}
			return;
		}
		boolean noticeBtnPar = false;
		List<String> tempList = new ArrayList<String>();
		List<Integer> tempIn = new ArrayList<Integer>();
		Object obj = null;
		long pId = player.getId();
		synchronized (total_lock) {
			obj = p_lock.get(pId);
			if (obj == null) {
				obj = new Object();
				p_lock.put(pId, obj);
			}
		}
		logger.warn("[checkPlayerAims] [plmList:"+plmList.size()+"] ["+player.getName()+"]");
		synchronized (obj) {
			PlayerAimsEntity entity = this.getEntity(player.getId());
			for(PlayerAimModel pam : plmList) {
				if(!isAimComplete(player.getId(), pam)) {		//已经完成的目标不管
					if (pam.getNum() <= gdr.getNum()) {			//玩家达成了这个目标
						PlayerAim aim = new PlayerAim();
						aim.setAimId(pam.getId());
						aim.setCompletTime(now);
						aim.setReveiveStatus((byte) 0);
						List<PlayerAim> aimList = entity.getAimList();
						aimList.add(aim);
						entity.setAimList(aimList);
						doOnDeliver(player, pam, isNotify);
						noticeBtnPar = true;
						ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(pam.getChapterName());
						if(cm != null) {
							entity.updateChapterScore(cm, true);
						} else {
							logger.warn("[目标系统] [获取章节错误] [章节名:" + pam.getChapterName() + "] [aimId:" + aim.getAimId() + "] [" + player.getLogString() + "]");
						}
					}
					tempList.add(pam.getChapterName());
					tempIn.add(pam.getId());
				}
			}
		}
		try {
			if (tempList != null && tempList.size() > 0 && player.isOnline()) {
				for (int i=0; i<tempList.size(); i++) {
					String tempName = tempList.get(i);
					int chapterScore = this.getChapterScore(player, tempName);;			//当前章节获得的总积分
					int totalScore = this.getChapterComplateNum(player, tempName);				//所有章节总积分
					byte complate = this.getChapterReceiveStatus(player, tempName);		//章节奖励是否可领取
					NOTICE_PLAYER_MUBIAO_CHANGE_RES res = new NOTICE_PLAYER_MUBIAO_CHANGE_RES(GameMessageFactory.nextSequnceNum(), tempName, tempIn.get(i), gdr.getNum(), chapterScore, totalScore, complate);
					player.addMessageToRightBag(res);
				}
			}
		} catch (Exception e) {
			logger.error("[目标系统] [处理NOTICE_PLAYER_MUBIAO_CHANGE_RES] [异常] [" + player.getLogString() + "]", e);
		}
		if(noticeBtnPar && isNotify && player.isOnline()) {			//推送目标按钮粒子显示
			NOTICE_PARTICLE_REQ req = new NOTICE_PARTICLE_REQ(GameMessageFactory.nextSequnceNum(), this.checkPlayerAimReceiveStatus(player), 1);
			player.addMessageToRightBag(req);
			if(logger.isDebugEnabled()) {
				logger.debug("[目标系统] [玩家登陆] [通知客户端显示按钮粒子] [" + player.getLogString() + "]");
			}
		}
	}
	/**
	 * 玩家达成某个成就调用(达成目标粒子)
	 * @param player
	 * @param pam
	 */
	private void doOnDeliver(Player player, PlayerAimModel pam, boolean isNotify) {
		try {
			if(logger.isDebugEnabled()) {
				logger.debug("[目标系统] [完成目标调用doOnDeliver] [" + player.getLogString() + "] [" + pam.getId() + "--" + pam.getAimName() + "]");
			}
			if(isNotify)
			{
				int colorType = pam.getFrameColor();
				String completeTime = new Timestamp(System.currentTimeMillis()).toString().split(" ")[0];
				QUERY_COMPLETE_AIM_RES resp = new QUERY_COMPLETE_AIM_RES(GameMessageFactory.nextSequnceNum(), player.getName(), pam.getIcon(), pam.getAimName(),colorType, 
						pam.getDescription(), pam.getScore(), completeTime, (byte)1);
				player.addMessageToRightBag(resp);
				if (pam.getAimLevel() == 3) {
					ChatMessage msg = new ChatMessage();
					try {
						// 做世界广播
						StringBuffer sbf = new StringBuffer();
						sbf.append("<f color='0xF3F349'>").append(Translate.translateString(Translate.恭喜达成目标, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, pam.getAimName() } })).append("</f>");
						msg.setMessageText(sbf.toString());
						ChatMessageService.getInstance().sendMessageToSystem(msg);
					} catch (Exception e) {
						AchievementManager.logger.error(player.getLogString() + "[达成成就:" + pam.getAimName() + "] [系统广播异常:" + msg.getMessageText() + "]", e);
					}
				} else if (pam.getAimLevel() == 4) {
					Player players[] = GamePlayerManager.getInstance().getOnlinePlayers();
					for (Player pp : players) {
						if (!pp.isChatChannelOpenning(ChatChannelType.SYSTEM) || pp.getCurrentGame() == null) {
							continue;
						}
						//将玩家达成的目标发到世界  格式 <f onclick='playeraim' onclickType='3' aimid='%d' playerId='%lld' color='%lld'>%s</f>"
						String str3 = "0xF3F349";
						if (pam.getFrameColor() == 2) {
							str3 = "0xffFDA700";
						} else {
							str3 = "0xffE706EA";
						}
						String str2 = String.format(str, pam.getId()+"", player.getId()+"", str3, "【" +pam.getAimName()+ "】");
						String str1 = Translate.translateString(Translate.恭喜达成目标2, new String[][] { { Translate.STRING_1, player.getName() }});
						pp.sendNotice(str1 + str2);
						// 测试日志
						if (logger.isDebugEnabled()) logger.debug("[send_to_system] [接受人:{}]", new Object[] { pp.getName()});
					}
				}
			}
		} catch (Exception e) {
			logger.error("[目标系统] [调用doOnDeliver异常] [" + player.getLogString() + "] [isNotify:" + isNotify + "] [pamId:" + pam.getId() + "]", e);
		}
	}
	public static String str = "<f onclick='playeraim' onclickType='3' aimid='%s' playerId='%s' color='%s'>%s</f>";
	/**
	 * 判断某个目标是否已经完成
	 * @param playerId
	 * @param pam
	 * @return true代表已完成
	 */
	public boolean isAimComplete(long playerId, PlayerAimModel pam) {
		PlayerAimsEntity entity = this.getEntity(playerId);
		boolean result = entity.isAimCompleted(pam.getId());
		if (logger.isDebugEnabled()) {
			logger.debug("[目标系统] [判断目标是否达成] [playerId :" + playerId + "] [aimId:" + pam.getId() + "] [result :" + result + "]");
		} 
		return result;
	}
	/**
	 * 领取目标奖励
	 * @param player
	 * @param aimId 领取的是章节奖励时候传-1
	 * @param chapterName 只有aimid=-1时有用
	 * @param isVipReveive
	 */
	public CompoundReturn receiveReward(Player player, int aimId, String chapterName, boolean isVipReveive) {
		CompoundReturn cr = null;
		long pId = player.getId();
		Object lock = null;
		synchronized (total_lock) {
			lock = p_lock.get(pId);
			if (lock == null) {
				lock = new Object();
				p_lock.put(pId, lock);
			}
		}
		synchronized (lock) {
//		synchronized (player) {
			cr = checkReceiveReward(player, aimId, chapterName, isVipReveive);
			if(!cr.getBooleanValue()) {		//不可领取奖励
				if(logger.isWarnEnabled()) {
					logger.warn("[目标系统] [领取目标奖励失败] [失败原因] [" + cr.getIntValue() + "] [" + player.getLogString() + "] [" + cr.getStringValue() + "][chapterName:" 
				+ chapterName +"][aimId:" + aimId + "] [isVipReveive:" + isVipReveive + "]");
				}
				player.sendError(getInfo(cr.getIntValue()));
				return cr; 
			}
			boolean receiveFlag = false;
			PlayerAimModel am = null;
			ChapterModel cm = null;
			PlayerAimsEntity entity = this.getEntity(player.getId());
			{				//更改领取状态	
				if(aimId > 0) {
					List<PlayerAim> amList = entity.getAimList();
					for(PlayerAim pa : amList) {
						if(pa.getAimId() == aimId) {
							receiveFlag = true;
							byte oldStatus = pa.getReveiveStatus();
							byte receiveStatus = oldStatus;
							if(receiveStatus <= 0) {
								receiveStatus = isVipReveive ? VIP_RECEIVE : NOMAL_RECEIVE;
							} else {
								receiveStatus = VIP_AND_NOMAL_RECEIVE;
							}
							pa.setReveiveStatus(receiveStatus);
							am = PlayerAimManager.instance.aimMaps.get(aimId);
							if(logger.isDebugEnabled()) {
								logger.debug("[目标系统] [领取目标奖励，更改领取状态] [之前状态:" + oldStatus + "] [更新后状态:" + receiveStatus + "] [" + player.getLogString() + "] [aimId:" + aimId + "]");
							}
							break;
						}
					}
					entity.setAimList(amList);
				} else {
					List<PlayerChapter> pcList = entity.getChapterList();
					for(PlayerChapter pc : pcList) {
						if(pc.getChapterName().equals(chapterName)) {
							receiveFlag = true;
							cm = PlayerAimManager.instance.chapterMaps.get(chapterName);
							byte oldStatus = pc.getReceiveType();
							byte receiveStatus = oldStatus;
							if(receiveStatus <= 0) {
								receiveStatus = isVipReveive ? VIP_RECEIVE : NOMAL_RECEIVE;
							} else {
								receiveStatus = VIP_AND_NOMAL_RECEIVE;
							}
							pc.setReceiveType(receiveStatus);
							break;
						}
					}
					entity.setChapterList(pcList);
				}
			}
			if(receiveFlag)			//多一层判断---只有在状态改变成功后才会发奖
			{			//读配表发奖励
				if(am != null) {
					sendReward2Player(player, am.getRewardArticles(), am.getRewardTitle(), am.getRewardGongzi(), am.getRewardBindYin(), am.getRewardGongXun(), am.getMulReward4Vip(), isVipReveive);
					if(logger.isWarnEnabled()) {
						logger.warn("[目标系统] [领取目标奖励] [成功] [" + player.getLogString() + "] [aimId :" + aimId + "] [chapterName:" + chapterName
								+ "] [isVipReceive :" + isVipReveive + "]");
					}
				} else if(cm != null) {
					sendReward2Player(player, cm.getRewardArticles(), cm.getRewardTitle(), cm.getRewardGongzi(), cm.getRewardBindYin(), cm.getRewardGongXun(), cm.getMulReward4Vip(), isVipReveive);
					if(logger.isWarnEnabled()) {
						logger.warn("[目标系统] [领取目标奖励] [成功] [" + player.getLogString() + "] [aimId :" + aimId + "] [chapterName:" + chapterName
								+ "] [isVipReceive :" + isVipReveive + "]");
					}
				} else {
					if(logger.isWarnEnabled()) {
						logger.warn("[目标系统] [领取目标奖励] [失败] [没有取到对应奖励模板] [" + player.getLogString() + "] [aimId :" + aimId + "] [chapterName:" + chapterName
								+ "] [isVipReceive :" + isVipReveive + "]");
					}
				}
			}
		}
		return cr;
	}
	/**
	 * 给玩家发奖励
	 * @param player
	 * @param rewardArticles
	 * @param rewardTitle
	 * @param rewardGongzi
	 * @param rewardBindYin
	 * @param rewardGongXun
	 */
	private void sendReward2Player(Player player, List<RewordArticle> rewardArticles, String rewardTitle, long rewardGongzi, long rewardBindYin, long rewardGongXun, int vipMul, boolean isVipReveive) {
		try {
			int mul = isVipReveive ? vipMul : 1;
			if (mul <= 0) {
				player.sendError(Translate.没有可领取的奖励);
				return ;
			}
			player.sendError(Translate.领取成功);
			logger.warn("[目标系统] [玩家领取奖励] [" + player.getLogString() + "] [倍数:" + mul + "] [isVipReveive:" + isVipReveive + "]");
			for(int ii=0; ii<mul; ii++) {
				if(rewardGongzi > 0) {		//奖励工资
					boolean result = BillingCenter.getInstance().playerSaving(player, rewardGongzi, CurrencyType.GONGZI, SavingReasonType.完成目标奖励, "完成目标奖励");
					logger.warn("[目标系统] [玩家领取工资] [" + player.getLogString() + "] [rewardGongzi:" + rewardGongzi + "] [结果:" + result + "]");
				}
				if(rewardBindYin > 0) {		//奖励绑银
					boolean result = BillingCenter.getInstance().playerSaving(player, rewardBindYin, CurrencyType.BIND_YINZI, SavingReasonType.完成目标奖励, "完成目标奖励");
					logger.warn("[目标系统] [玩家领取绑银] [" + player.getLogString() + "] [rewardBindYin:" + rewardBindYin + "] [结果:" + result + "]");
				}
				if(rewardGongXun > 0) {		//奖励功勋
					boolean result = BillingCenter.getInstance().playerSaving(player, rewardGongXun, CurrencyType.GONGXUN, SavingReasonType.完成目标奖励, "完成目标奖励");
					logger.warn("[目标系统] [玩家领取功勋] [" + player.getLogString() + "] [rewardGongXun:" + rewardGongXun + "] [结果:" + result + "]");
				}
				if(rewardTitle != null && !rewardTitle.isEmpty()) {	//奖励称号
					boolean result = PlayerTitlesManager.getInstance().addTitle(player, rewardTitle, true);
					logger.warn("[目标系统] [玩家领取称号] [" + player.getLogString() + "] [title:" + rewardTitle + "] [结果:" + result + "]");
				}
				if(rewardArticles != null && rewardArticles.size() > 0) {		//奖励物品
					List<ArticleEntity> atList = new ArrayList<ArticleEntity>();
					List<Integer> list = new ArrayList<Integer>();
					List<String> articleCNNName = new ArrayList<String>();
					for(RewordArticle ra : rewardArticles) {
//						for(int i=0; i<ra.getNum(); i++) {
						Article a = ArticleManager.getInstance().getArticleByCNname(ra.getArticleCNName());
						articleCNNName.add(a.getName_stat());
						ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.完成目标奖励, player, ra.getColorType(), 1, true);
						atList.add(ae);
						list.add(ra.getNum());
//						}
					}
					put2Bag(player, atList, list, articleCNNName);
				}
			} 
		}catch (Exception e) {
			logger.error("[目标系统] [领取目标奖励异常] [" + player.getLogString() + "]", e);
		}
	}
	
	private void put2Bag(Player p, List<ArticleEntity> atList, List<Integer> nums, List<String> articleCNNName) {
		List<Player> players = new ArrayList<Player>();
		players.add(p);
		ActivityProp[] props = new ActivityProp[atList.size()];
		for(int i=0; i<atList.size(); i++) {
			props[i] = new ActivityProp(articleCNNName.get(i), atList.get(i).getColorType(), nums.get(i), atList.get(i).isBinded());
//			if(!p.putToKnapsacks(atList.get(i), nums.get(i), "目标系统")) {
//				p.sendError(Translate.背包空间不足);
//				if(logger.isInfoEnabled()) {
//					logger.info("[目标系统] [背包空间不足发邮件] [" + p.getLogString() + "]");
//				}
//				try{
//					MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[] { atList.get(i) }, new int[] { nums.get(i) }, Translate.系统, Translate.目标系统奖励邮件, 0L, 0L, 0L, "目标系统奖励");
//				} catch(Exception e) {
//					logger.error("[目标系统][合成物品没有放入背包且发邮件出异常] [" + p.getLogString() + "][物品名:" + atList.get(i).getArticleName() + "&&颜色:" + atList.get(i).getColorType() + "&&是否绑定" + atList.get(i).isBinded() +"]");
//				}
//			}
		}
		ActivityManager.sendMailForActivity(players, props,Translate.系统, Translate.目标系统奖励邮件, "目标系统奖励");
	}
	
	/**
	 * 得到对应给玩家的提示信息
	 * @param id
	 * @return
	 */
	public String getInfo(int id) {
		String result = "";
		switch (id) {
		case 1:
		case 2:
		case 3:result = Translate.服务器数据异常;
		break;
		case 4:result = Translate.目标未达成;
		break;
		case 5:result = Translate.章节积分不足;
		break;
		case 6:result = Translate.已领取奖励;
		break;
		case 7:result = Translate.等级不符;
		break;
		default: result = Translate.text_jiazu_082;
		break;
		}
		return result;
	}
	
	/**
	 * 检测玩家是否可以领取奖励
	 * @param player
	 * @param aimId   -1代表领取的是章节奖励
	 * @param chapterName	章节名
	 * @param isVipReveive	是否是领取vip的额外奖励
	 * @return 
	 */
	public CompoundReturn checkReceiveReward(Player player, int aimId, String chapterName, boolean isVipReveive) {
		PlayerAimModel aimModel = null;
		ChapterModel chapterModel = null;
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false).setStringValue("未知原因").setIntValue(99);
		if(logger.isDebugEnabled()) {
			logger.debug("[目标系统] [领取目标奖励] [" + player.getLogString() + "] [aimId:" + aimId + "] [chapterName:" + chapterName + "] [isvipreveive:" + isVipReveive + "]");
		}
		if(aimId > 0) {
			aimModel = PlayerAimManager.instance.aimMaps.get(aimId);
			if(aimModel == null) {
				return cr.setBooleanValue(false).setStringValue("配表中没有此目标").setIntValue(1);
			}
			if (player.getLevel() < aimModel.getLevelLimit()) {
				return cr.setBooleanValue(false).setStringValue("等级不足").setIntValue(7);
			}
		} else{
			if(chapterName == null) {
				return cr.setBooleanValue(false).setStringValue("章节名错误").setIntValue(2);
			} 
			chapterModel = PlayerAimManager.instance.chapterMaps.get(chapterName); 
			if(chapterModel == null) {
				return cr.setBooleanValue(false).setStringValue("配表中没有此章节").setIntValue(3);
			}
			if (player.getLevel() < chapterModel.getLevelLimit()) {
				return cr.setBooleanValue(false).setStringValue("等级不足").setIntValue(7);
			}
		}
			PlayerAimsEntity entity = getEntity(player.getId());
			List<PlayerAim> aimList = entity.getAimList();
			if(aimList == null || aimList.size() <= 0) {
				return cr.setBooleanValue(false).setStringValue("玩家没有达成任何目标").setIntValue(4);
			}
			if(aimModel != null) {
				boolean flag = false;
				for(PlayerAim am : aimList) {
					if(am.getAimId() == aimId) {
						if(!isVipReveive) {		//非vip领取
							if(am.getReveiveStatus() != NOMAL_RECEIVE && am.getReveiveStatus() != VIP_AND_NOMAL_RECEIVE){
								flag = true;
							}
						} else {
							if(am.getReveiveStatus() != VIP_RECEIVE && am.getReveiveStatus() != VIP_AND_NOMAL_RECEIVE){
								flag = true;
							}
						}
						break;
					}
				}
				if(!flag) {
					return cr.setBooleanValue(false).setStringValue("玩家没有达成此目标或已领取过目标奖励").setIntValue(4);
				}
			}
			if(chapterModel != null) {
				int aimScores = this.getChapterScore(player, chapterName);
				if(aimScores < chapterModel.getScoreLimit()) {
					return cr.setBooleanValue(false).setStringValue("此章节积分不足").setIntValue(5);
				} else {
					PlayerChapter pc = null;
					boolean flag = false;
					if(entity.getChapterList() != null && entity.getChapterList().size() > 0) {
						for(PlayerChapter pcc : entity.getChapterList()) {
							if(pcc.getChapterName().equals(chapterName)) {
								pc = pcc;
								break;
							}
						}
						if(!isVipReveive) {		//非vip领取
							if(pc.getReceiveType() != NOMAL_RECEIVE && pc.getReceiveType() != VIP_AND_NOMAL_RECEIVE){
								flag = true;
							}
						} else {
							if(pc.getReceiveType() != VIP_RECEIVE && pc.getReceiveType() != VIP_AND_NOMAL_RECEIVE){
								flag = true;
							}
						}
						if(!flag) {
							return cr.setBooleanValue(false).setStringValue("已经领取过此章节奖励").setIntValue(6);
						}
					}
				}
			}
		return cr.setBooleanValue(true);
	}
	/**
	 * 获得玩家目标实体
	 * @param playerId
	 * @return
	 */
	public PlayerAimsEntity getEntity(long playerId) {
		PlayerAimsEntity entity = (PlayerAimsEntity) cache.get(playerId);
		if(entity == null) {
			try {
				entity = em.find(playerId);
				if(entity == null) {
					entity = new PlayerAimsEntity();
					entity.setId(playerId);
//					/entity = new PlayerAimsEntity(playerId);
					entity.setAimList(new ArrayList<PlayerAim>());
					em.notifyNewObject(entity);
					cache.put(playerId, entity);
					try {					//创建时检测玩家的数据记录，自动完成玩家已达成过的目标--发送事件，不在此直接处理
						EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.CHECK_PLAYER_AIMS, new Object[] { playerId});
						EventRouter.getInst().addEvent(evt);
					} catch (Exception e) {
						logger.error("[目标系统] [检查gamedatarecord异常] [playerId : " + playerId + "]", e);
					}
				}
			} catch (Exception e) {
				logger.error("[目标系统] [从数据库加载数据异常] [playerId : " + playerId + "]", e);
			}
		}
		if (!tempCache.containsKey(playerId)) {
			tempCache.put(playerId, entity);
		}
		return entity;
	}
	
	public void destory() {
		if (em != null) {
			em.destroy();
			logger.info("**************************************[服务器关闭][playeraimeEntityManager调用destory]*********************************************");
		}
	}
	public void notifyRemoveFromCache(PlayerAimsEntity entity) {
		try {
			em.save(entity);

		} catch (Exception e) {
			logger.error("[目标系统] [异常] [移除保存错误] [" + entity.getId() + "]", e);
		}
	}
}
