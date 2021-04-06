package com.fy.engineserver.achievement;

import java.util.List;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.concrete.DefaultMailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_DONE_ACHIEVEMENT_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostPersist;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "playerId" }), @SimpleIndex(members = { "deliverTime" }) })
public class AchievementEntity {

	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	/** 角色ID */
	private long playerId;
	/** 成就ID,对应AchievementList */
	private int achievementId;
	/** 完成时间 */
	private long deliverTime;

	/** 是否被保存过了 */
	private transient boolean saved;

	public AchievementEntity() {

	}

	@SimplePostPersist
	public void notifySaved() {
		setSaved(true);
	}

	// /**
	// * 做一些刚刚开启成就时要做的,比如刚获得就已经达成了等等
	// */
	// public void doOnUnlock() {
	//
	// }

	/**
	 * 做一些成就达成时要做的,比如给奖励等
	 */
	public void doOnDeliver(Player player) {
		Achievement achievement = AchievementManager.getInstance().getSystemAchievement(achievementId);
		if (achievement == null) {
			AchievementManager.logger.error(toString() + "[达成] [系统成就配置不存在]");
			return;
		}
		if (achievement.getPrizeAchievementNum() <= 0) {
			AchievementManager.logger.error(toString() + "[达成] [系统成就配置异常] [数量:" + achievement.getPrizeAchievementNum() + "]");
			return;
		}
		// Player player = null;
		// try {
		// player = GamePlayerManager.getInstance().getPlayer(playerId);
		// } catch (Exception e) {
		// AchievementManager.logger.error(toString() + "[达成] [所属角色不存在]", e);
		// return;
		// }

		if (achievement.getPrizeArticle() != null && !"".equals(achievement.getPrizeArticle())) {
			String prizeName = achievement.getPrizeArticle();
			Article article = ArticleManager.getInstance().getArticle(prizeName);
			if (article == null) {
				AchievementManager.logger.error(player.getLogString() + "[达成成就:" + achievement.getName() + "] [奖励物品不存在:" + prizeName + "]");
				return;
			}
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_ACHIEVEMENT, player, article.getColorType(), 1, true);
				ArticleEntity[] aeArr = new ArticleEntity[1];
				aeArr[0] = ae;
				boolean canAdd = player.putAllOK(aeArr);
				if (canAdd) {
					player.putAll(aeArr, "成就奖励");
					if (AchievementManager.logger.isWarnEnabled()) {
						AchievementManager.logger.warn(player.getLogString() + "[达成成就:" + achievement.getName() + "] [获得物品:" + ae.getArticleName() + "] [物品颜色:" + ae.getColorType() + "] [直接放入背包]");
					}
				} else {
					// 发邮件
					DefaultMailManager.getInstance().sendMail(playerId, aeArr, Translate.成就奖励, Translate.translateString(Translate.达成成就, new String[][] { { Translate.STRING_1, achievement.getName() } }), 0, 0, 0, "成就奖励");
					if (AchievementManager.logger.isWarnEnabled()) {
						AchievementManager.logger.warn(player.getLogString() + "[达成成就:" + achievement.getName() + "] [获得物品:" + ae.getArticleName() + "] [物品颜色:" + ae.getColorType() + "] [发送邮件]");
					}
				}
				// player.sendError(Translate.translateString(Translate.text_achievement_001, new String[][] { { Translate.STRING_1, achievement.getName() }, { Translate.STRING_2,
				// article.getName() } }));
			} catch (Exception e) {
				AchievementManager.logger.error(player.getLogString() + "[达成成就:" + achievement.getName() + "] [创建奖励物品异常:" + prizeName + "]", e);
			}
		}
		if (achievement.getPrizeTitle() != null && !"".equals(achievement.getPrizeTitle())) {
			// TODO 奖励称号
			int key = PlayerTitlesManager.getInstance().getKeyByType(achievement.getPrizeTitle());
			if (key == -1) {
				AchievementManager.logger.error(player.getLogString() + "[达成成就:" + achievement.getName() + "] [奖励称号:" + achievement.getPrizeTitle() + "] [失败] [称号不存在]");
			} else {
				PlayerTitlesManager.getInstance().addTitle(player, achievement.getPrizeTitle(), false);
				if (AchievementManager.logger.isInfoEnabled()) {
					AchievementManager.logger.info(player.getLogString() + "[达成成就:" + achievement.getName() + "] [奖励称号:" + achievement.getPrizeTitle() + "] [成功]");
				}
			}
		}
		{
			// 增加成就点
			player.addAchievementDegree(achievement.getPrizeAchievementNum());
			// player.sendError(Translate.translateString(Translate.text_achievement_002, new String[][] { { Translate.STRING_1, achievement.getName() }, { Translate.COUNT_1,
			// String.valueOf(achievement.getPrizeAchievementNum()) } }));
			if (AchievementManager.logger.isInfoEnabled()) {
				AchievementManager.logger.info(player.getLogString() + "[达成成就:" + achievement.getName() + "] [获得成就点数:" + achievement.getPrizeAchievementNum() + "] [增加后成就点数:" + player.getAchievementDegree() + "]");
			}
		}
		if (achievement.getLevel() > 3) {
			ChatMessage msg = new ChatMessage();
			try {
				// 做世界广播
				StringBuffer sbf = new StringBuffer();
				sbf.append("<f color='0xF3F349'>").append(Translate.translateString(Translate.恭喜达成成就, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, achievement.getName() } })).append("</f>");
				msg.setMessageText(sbf.toString());
				ChatMessageService.getInstance().sendMessageToSystem(msg);
			} catch (Exception e) {
				AchievementManager.logger.error(player.getLogString() + "[达成成就:" + achievement.getName() + "] [系统广播异常:" + msg.getMessageText() + "]", e);
			}
		}
		this.notifyDepend(player);
		{
			// 发送成就完成协议
			ParticleData[] particleDatas = new ParticleData[1];
			particleDatas[0] = new ParticleData(player.getId(), "任务光效/达成成就文字", -1, 2, 1, 1);
			NOTICE_CLIENT_PLAY_PARTICLE_RES client_PLAY_PARTICLE_RES = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
			player.addMessageToRightBag(client_PLAY_PARTICLE_RES);

			NOTICE_CLIENT_DONE_ACHIEVEMENT_REQ notice_CLIENT_DONE_ACHIEVEMENT_REQ = new NOTICE_CLIENT_DONE_ACHIEVEMENT_REQ(GameMessageFactory.nextSequnceNum(), achievement);
			player.addMessageToRightBag(notice_CLIENT_DONE_ACHIEVEMENT_REQ);
		}
	}

	private void notifyDepend(Player player) {
		Achievement achievement = AchievementManager.getInstance().getSystemAchievement(achievementId);
		if (achievement == null) {
			AchievementManager.logger.error(player.getLogString() + "[完成成就] [异常] [成就不存在] [成就ID:" + achievementId + "]");
			return;
		}
		List<Achievement> list = AchievementManager.getInstance().getAchievementDependMap().get(achievement);
		if (list != null) {
			for (Achievement achievement2 : list) {
				AchievementManager.getInstance().record(player, achievement2.getAction());
				if (AchievementManager.logger.isInfoEnabled()) {
					AchievementManager.logger.info(player.getLogString() + "[完成成就] [找到依赖它的成就] [成就ID:" + achievementId + "] [依赖它的成就:" + achievement2.getId() + "]");
				}
			}
		} else {
			if (AchievementManager.logger.isInfoEnabled()) {
				AchievementManager.logger.info(player.getLogString() + "[完成成就] [没有依赖它的其他成就] [成就ID:" + achievementId + "]");
			}
		}
	}

	public long getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(long deliverTime) {
		this.deliverTime = deliverTime;
		AchievementManager.aeEm.notifyFieldChange(this, "deliverTime");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
		AchievementManager.aeEm.notifyFieldChange(this, "playerId");
	}

	public int getAchievementId() {
		return achievementId;
	}

	public void setAchievementId(int achievementId) {
		this.achievementId = achievementId;
		AchievementManager.aeEm.notifyFieldChange(this, "achievementId");
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "AchievementEntity [id=" + id + ", version=" + version + ", playerId=" + playerId + ", achievementId=" + achievementId + ", deliverTime=" + deliverTime + ", saved=" + saved + "]";
	}
}
