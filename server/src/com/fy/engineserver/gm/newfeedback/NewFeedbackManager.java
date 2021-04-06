package com.fy.engineserver.gm.newfeedback;

import org.apache.log4j.Logger;

import com.fy.boss.gm.newfeedback.client.BossGmClientService;
import com.fy.boss.message.FEEDBACK_COMMIT_BOSS_RES;
import com.fy.boss.message.FEEDBACK_DELETE_BOSS_RES;
import com.fy.boss.message.FEEDBACK_HOME_PAGE_BOSS_RES;
import com.fy.boss.message.FEEDBACK_LOOK_BOSS_RES;
import com.fy.boss.message.FEEDBACK_LOOK_SCORE_BOSS_RES;
import com.fy.boss.message.FEEDBACK_PLAYER_TALK_BOSS_RES;
import com.fy.boss.message.FEEDBACK_SCORE_BOSS_RES;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.FEEDBACK_COMMIT_RES;
import com.fy.engineserver.message.FEEDBACK_DELETE_RES;
import com.fy.engineserver.message.FEEDBACK_HOME_PAGE_RES;
import com.fy.engineserver.message.FEEDBACK_LOOK_RES;
import com.fy.engineserver.message.FEEDBACK_LOOK_SCORE_RES;
import com.fy.engineserver.message.FEEDBACK_PLAYER_TALK_RES;
import com.fy.engineserver.message.FEEDBACK_SCORE_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.text.WordFilter;

public class NewFeedbackManager {

	public static Logger logger = Logger.getLogger(NewFeedbackManager.class);

	private static NewFeedbackManager self;

	BossGmClientService bossgmclientservice;

	public static NewFeedbackManager getInstance() {
		return self;
	}

	public void init() {
		
		self = this;
		bossgmclientservice = BossGmClientService.getInstance();
		ServiceStartRecord.startLog(this);
	}
	/**
	 * 联系GM
	 * @param seqid
	 * @param player
	 * @return
	 */
	public FEEDBACK_HOME_PAGE_RES getFeedbackHomePageRes(long seqid, Player player) {
		long now = System.currentTimeMillis();
		FEEDBACK_HOME_PAGE_RES res = null;
		if(true){
			player.sendError(Translate.此功能暂未开放);
			return null;
		}
		try {
			FEEDBACK_HOME_PAGE_BOSS_RES boss_res = bossgmclientservice.getHomePageMess(player.getUsername(), player.getName(), player.getVipLevel());
			if (boss_res != null) {
				if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
					res = new FEEDBACK_HOME_PAGE_RES(seqid, boss_res.getQueueCountMess(), boss_res.getGmServiceMess(), boss_res.getShowNumLimit(), boss_res.getFeedbacks());
				} else {
					String queueCount = boss_res.getQueueCountMess().substring(boss_res.getQueueCountMess().indexOf("：") + 1);
					res = new FEEDBACK_HOME_PAGE_RES(seqid, Translate.当前排队问题数 + queueCount, Translate.亲爱的玩家, boss_res.getShowNumLimit(), boss_res.getFeedbacks());
				}
				logger.warn("[获得首界面] [成功] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [VIP:" + player.getVipLevel() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
				return res;
			} else {
				logger.warn("[获得首界面] [出错] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [VIP:" + player.getVipLevel() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
				player.sendError(Translate.首界面信息);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得首界面] [异常] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [VIP:" + player.getVipLevel() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]", e);
			player.sendError(e.getMessage());
			return null;
		}
	}

	public FEEDBACK_DELETE_RES getFeedbackDeleteRes(long seqid, Player player, long id) {
		long now = System.currentTimeMillis();
		FEEDBACK_DELETE_RES res = null;
		try {
			FEEDBACK_DELETE_BOSS_RES boss_res = bossgmclientservice.deleteFeedback(id, player.getUsername(), player.getName());
			if (boss_res != null) {
				res = new FEEDBACK_DELETE_RES(seqid, id, Translate.删除成功, boss_res.getResult());
				logger.warn("[玩家删除一条反馈] [成功] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [反馈id：" + id + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
				return res;
			} else {
				logger.warn("[玩家删除一条反馈] [失败] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [反馈id：" + id + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
				player.sendError(Translate.删除反馈信息);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家删除一条反馈] [异常] [用户名：" + player.getUsername() + "] [反馈id：" + id + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]", e);
			player.sendError(e.getMessage());
			return null;
		}
	}

	public FEEDBACK_COMMIT_RES FeedbackCommit(long seqid, Player player, String username) {
		long now = System.currentTimeMillis();
		FEEDBACK_COMMIT_RES res = null;
		try {
			GameConstants con = GameConstants.getInstance();
			FEEDBACK_COMMIT_BOSS_RES boss_res = bossgmclientservice.isCommitNewFeedback(player.getName(), username, player.getVipLevel(), con.getServerName());
			if (boss_res != null) {
				// replace result sure mess
				byte result = boss_res.getResult();
				if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
					if (result == 0) {
						res = new FEEDBACK_COMMIT_RES(seqid, boss_res.getId(), boss_res.getResult(), Translate.详细描述);
					} else if (result == 1) {
						res = new FEEDBACK_COMMIT_RES(seqid, boss_res.getId(), boss_res.getResult(), Translate.联系GM);
					}
				} else {
					res = new FEEDBACK_COMMIT_RES(seqid, boss_res.getId(), boss_res.getResult(), boss_res.getResultMess());
				}
				logger.warn("[玩家提交一条反馈] [成功] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [反馈id：" + boss_res.getId() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
				return res;
			} else {
				logger.warn("[玩家提交一条反馈] [失败] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [反馈id：" + boss_res.getId() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
				player.sendError(Translate.提交新反馈信息);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家提交一条反馈] [异常] [用户名：" + player.getUsername() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]", e);
			player.sendError(e.getMessage());
			return null;
		}
	}

	public FEEDBACK_LOOK_SCORE_RES lookScore(long seqid, Player player, long id, String username) {
		long now = System.currentTimeMillis();
		FEEDBACK_LOOK_SCORE_RES res = null;
		try {
			FEEDBACK_LOOK_SCORE_BOSS_RES boss_res = bossgmclientservice.lookFeedbackScore(id, username);
			if (boss_res != null) {
				if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
					res = new FEEDBACK_LOOK_SCORE_RES(seqid, id, boss_res.getScoreMess());
				} else {
					res = new FEEDBACK_LOOK_SCORE_RES(seqid, id, Translate.进行评价);
				}
				logger.warn("[玩家查看评分的反馈] [成功] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [反馈id：" + boss_res.getId() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
				return res;
			} else {
				logger.warn("[玩家查看评分的反馈] [失败] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [反馈id：" + boss_res.getId() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
				player.sendError(Translate.查看评分状态);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家查看评分的反馈] [异常] [用户名：" + player.getUsername() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]", e);
			player.sendError(e.getMessage());
			return null;
		}
	}

	public FEEDBACK_LOOK_RES lookFeedback(long seqid, Player player, long id, String username) {
		long now = System.currentTimeMillis();
		FEEDBACK_LOOK_RES res = null;
		try {
			FEEDBACK_LOOK_BOSS_RES boss_res = bossgmclientservice.lookFeedback(username, id);
			if (boss_res != null) {
				if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
					res = new FEEDBACK_LOOK_RES(seqid, id, boss_res.getResultMess(), (byte) 0, boss_res.getTalk());
				} else {
					res = new FEEDBACK_LOOK_RES(seqid, id, Translate.亲爱的玩家您好, (byte) 0, boss_res.getTalk());
				}
				logger.warn("[玩家查看反馈] [成功] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [id:" + id + "] [反馈id：" + boss_res.getId() + "] [talks:" + boss_res.getTalk().length + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
				return res;
			} else {
				logger.warn("[玩家查看反馈] [失败] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [id:" + id + "] [反馈id：" + boss_res.getId() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
				player.sendError(Translate.查看成功);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家查看反馈] [异常] [用户名：" + player.getUsername() + "] [玩家：" + player.getName() + "] [id:" + id + "] [反馈id:" + id + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]", e);
			player.sendError(e.getMessage());
			return null;
		}
	}

	public FEEDBACK_SCORE_RES socreFeedback(long seqid, long id, Player player, int select, String username) {
		long now = System.currentTimeMillis();
		FEEDBACK_SCORE_RES res = null;
		try {
			FEEDBACK_SCORE_BOSS_RES boss_res = bossgmclientservice.scoreFeedback(id, select, username);
			if (boss_res != null) {
				res = new FEEDBACK_SCORE_RES(seqid, id, 0, Translate.评分成功);
			} else {
				player.sendError(Translate.玩家评分失败);
				return null;
			}
			logger.warn("[玩家评分] [成功] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [反馈id：" + boss_res.getId() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家评分] [异常] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [选项：" + select + "] [反馈id：" + id + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]", e);
			player.sendError(e.getMessage());
			return null;
		}
	}

	public FEEDBACK_PLAYER_TALK_RES playerTalk(long seqid, Player player, long id, String username, com.fy.boss.gm.newfeedback.GmTalk talk) {
		long now = System.currentTimeMillis();
		FEEDBACK_PLAYER_TALK_RES res = null;
		try {
			WordFilter filter = WordFilter.getInstance();
			boolean valid = filter.cvalid(talk.getTalkcontent(), 0) && filter.evalid(talk.getTalkcontent(), 1);
			if (!valid) {
				res = new FEEDBACK_PLAYER_TALK_RES(seqid, id, talk.getId(), talk.getSendDate(), (byte) 1, Translate.内容不合法);
				player.sendError(Translate.内容不合法);
				logger.warn("[玩家提交的问题有敏感词:" + talk.getTalkcontent() + "] " + player.getLogString());
				return res;
			}
			FEEDBACK_PLAYER_TALK_BOSS_RES boss_res = bossgmclientservice.playerTalk(player.getVipLevel(), username, id, talk);
			if (boss_res != null) {
				if (boss_res.getResultMess() != null && boss_res.getResultMess().equals("1000")) {
					player.sendError(Translate.不能继续回复);
				} else {
					res = new FEEDBACK_PLAYER_TALK_RES(seqid, id, talk.getId(), talk.getSendDate(), (byte) 0, boss_res.getResultMess());
				}
			} else {
				res = new FEEDBACK_PLAYER_TALK_RES(seqid, id, talk.getId(), talk.getSendDate(), (byte) 1, Translate.反馈服务器返回失败);
				player.sendError(Translate.反馈服务器返回失败);
				return null;
			}
			logger.warn("[玩家说话] [成功] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [反馈id：" + id + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家说话] [异常] [用户名：" + player.getUsername() + "] [角色名：" + player.getName() + "] [反馈id：" + id + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]", e);
			player.sendError(e.getMessage());
			return null;
		}

	}

	public void setBossgmclientservice(BossGmClientService bossgmclientservice) {
		this.bossgmclientservice = bossgmclientservice;
	}

}
