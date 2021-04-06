package com.fy.engineserver.activity.peoplesearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.CompoundReturn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 正常的斩妖降魔
 * 
 */
@SimpleEmbeddable
public class PeopleSearch {

	/** 模板ID */
	private int templetId;
	/** 目标模板数据,需要在初始化的时候赋值, */
	private transient PeopleTemplet peopleTemplet;
	/** 所有者,需要在初始化的时候赋值, */
	private transient Player owner;
	/** 访问过的NPC 不存库,每次重启后清空 */
	private transient List<CountryNpc> visitedNpc = new ArrayList<CountryNpc>();
	/** 信息探听情况记录,谈听过,记录为true */
	private boolean[] snooped = new boolean[PeopleSearchManager.messageNum];

	/** 提供消息的NPC */
	private CountryNpc[] messageNpc = new CountryNpc[PeopleSearchManager.messageNum];

	/** 每条消息的索引 */
	private int[] messageIndex = new int[PeopleSearchManager.messageNum];

	/** 是否找到了目标 */
	private boolean found = false;
	/** 当前分数 */
	private int score;

	public PeopleSearch() {

	}

	/**
	 * 初始化一个斩妖降魔
	 * @param peopleTemplet
	 * @param player
	 */
	public PeopleSearch(PeopleTemplet peopleTemplet, Player player) {
		this.templetId = peopleTemplet.getId();
		this.owner = player;
		this.peopleTemplet = peopleTemplet;
		this.messageNpc = PeopleSearchManager.getInstance().getRandomMessageNpc(player);
		this.messageIndex = peopleTemplet.getRandomMessageIndexs();
		this.score = PeopleSearchManager.maxScore;
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [得到了一个斩妖降魔] " + toString());
		}
	}

	/**
	 * 在某个NPC身上探听消息
	 * @param npc
	 * @return
	 */
	public synchronized CompoundReturn getMessageForSearch(NPC npc) {
		for (int i = 0; i < messageNpc.length; i++) {
			if (messageNpc[i].isSame(npc)) {
				if (!snooped[i]) {
					snooped[i] = true;
					setDirty();
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [探听npc消息] [新的] [npc名字:" + npc.getName() + "] [所在国家:" + npc.getCountry() + "] [所在地图:" + npc.getGameCNName() + "]");
					}
				} else {
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [探听npc消息] [旧的] [npc名字:" + npc.getName() + "] [所在国家:" + npc.getCountry() + "] [所在地图:" + npc.getGameCNName() + "]");
					}
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setStringValue(peopleTemplet.getDes()[i][messageIndex[i]]);
			}
		}
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [探听npc消息] [错的] [npc名字:" + npc.getName() + "] [所在国家:" + npc.getCountry() + "] [所在地图:" + npc.getGameCNName() + "]");
		}

		return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(Translate.我不是你要找的人);
	}

	/**
	 * 猜测是这个NPC
	 * @param npc
	 */
	public synchronized CompoundReturn guess(NPC npc, Game game) {
		boolean inVisitList = true;// 是否在访问过的列表中
		CountryNpc countryNpc = new CountryNpc(npc, game);
		if (!visitedNpc.contains(countryNpc)) { // 如果没点过这个NPC,把它加到列表中
			visitedNpc.add(countryNpc);
			// setDirty();
			inVisitList = false;
		}
		if (peopleTemplet.getTarget().equals(countryNpc)) {// 是目标NPC
			if (!found) {
				found = true;
				setDirty();
			}
			if (!PeopleSearchManager.getInstance().isPeopleSearchSceneExist(owner)) {
				PeopleSearchScene peopleSearchScene = null;
				try {
					peopleSearchScene = new PeopleSearchScene(owner);
				} catch (Exception e) {
					ActivitySubSystem.logger.error(owner.getLogString() + "[创建boss说场景失败]");
				}
				if (peopleSearchScene != null) {
					PeopleSearchManager.getInstance().putToNewList(peopleSearchScene);
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [创建场景 ] [成功] [并放入的到newList]");
					}
				}
			}
			PeopleSearchManager.getInstance().transferToSelfScence(owner);
			return CompoundReturn.createCompoundReturn().setBooleanValue(true).setStringValue(Translate.恭喜你猜对了);
		}
		boolean subScore = false;
		{
			if (!found && !inVisitList) {
				// TODO 玩家没找到正确的,且是错误的,且不在之前的错误列表中 扣分--------
				if (this.score >= 1) {
					this.score--;
					setDirty();
					subScore = true;
				}
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(Translate.很可不是我 + (subScore ? "<f color='0xFF0000'>" + Translate.扣除一分 + "</f>" : "") + "," + Translate.当前分数 + ":(<f color='0xFF0000'>" + this.score + "</f>/<f color='0x33FF00'>" + PeopleSearchManager.maxScore + "</f>)");
	}

	/**
	 * 是否达到进入地图条件
	 * @return
	 */
	public boolean canEnterGame() {
		if (owner == null) {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn("[斩妖降魔] [查看能否能进入] [false] [主人不存在]");
			}
			return false;
		}
		if (!found) {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [查看能否能进入] [false] [还没找到目标]");
			}
			return false;
		}
		PeopleSearchScene peopleSearchScene = PeopleSearchManager.getInstance().getPeopleSearchScene(owner);
		if (peopleSearchScene == null) {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [查看能否能进入] [false] [场景不存在]");
			}
			return false;
		}

		// 这里有点别扭
		peopleSearchScene.setPlayerLastExistTime(SystemTime.currentTimeMillis());
		return true;
	}

	public void setDirty() {
		if (owner != null) {
			owner.setDirty(true, "peopleSearch");
		} else {
			ActivitySubSystem.logger.error("[斩妖降魔] [致命错误] [斩妖降魔所有者不存在]", new Exception());
		}
	}

	/**
	 * 得到提示,给用户显示用的
	 * @return
	 */
	public String getNotice() {
		StringBuffer sbf = new StringBuffer();

		// StringBuffer snoopedSbf = new StringBuffer();// 打听过消息的
		sbf.append("<f color='0x5ABEE9'>" + Translate.你可以向已下知情人打探信息 + ":</f>\n");
		for (int i = 0; i < snooped.length; i++) {
			CountryNpc message = messageNpc[i];
			sbf.append("<f color='0xFF6600'>").append(CountryManager.得到国家名(message.getCountry())).append("</f><f color='0x33CC00'>").append(message.getMapName()).append("</f>的<f color='0xFFFF00'>").append(message.getName()).append("</f>\n");
		}
		sbf.append("<f color='0x5ABEE9'>" + Translate.已经获得情报 + "</f>:\n");
		boolean hasSnoopedSome = false;
		for (int i = 0; i < snooped.length; i++) {
			if (snooped[i]) { // 访问过
				int index = messageIndex[i];
				sbf.append("<f color='0x33CC00'>").append(peopleTemplet.getDes()[i][index]).append("</f>\n");
				hasSnoopedSome = true;
			}
		}
		if (!hasSnoopedSome) {
			sbf.append("<f>" + Translate.你没有得到任何情报懒货快去打探消息 + "</f>\n");
		}
		CountryNpc target = getPeopleTemplet().getTarget();
		if (isFound()) {
			sbf.append("<f>" + Translate.你已经找到了目标Ta就是 + ":</f>\n");
			sbf.append("<f color='0xFF6600'>").append(CountryManager.得到国家名(target.getCountry())).append("</f><f color='0x33CC00'>").append(target.getMapName()).append("</f>的<f color='0xFF0000'>").append(target.getName()).append("</f>\n");
		} else {
			sbf.append("<f>" + Translate.你还没有找到目标据说Ta隐藏在本国的 + "</f><f color='0x33CC00'>" + target.getMapName() + ":</f>\n");
		}
		StringBuffer WorR = new StringBuffer();

		for (int i = 0; i < PeopleSearchManager.maxScore; i++) {
			if (i < (PeopleSearchManager.maxScore - score)) {
				WorR.append("<f color='0xFF0000'>X </f>");
			} else {
				WorR.append("<f color='0x33FF00'>√ </f>");
			}
		}
		sbf.append("<f color='0x5ABEE9'>" + Translate.你当前的分数 + ":" + score + "</f>\n" + WorR.toString() + "\n");
		sbf.append("<f color='0x5ABEE9'>" + Translate.可获得经验 + ":</f>\n<f color='" + (score == 0 ? PeopleSearchManager.color_article[0] : PeopleSearchManager.color_article[(score + 1) / 2 - 1]) + "'>" + PeopleSearchManager.getExp(owner, score) + "</f>\n");
		return sbf.toString();
	}

	public boolean isMessageNpc(NPC npc, Game game) {
		CountryNpc countryNpc = new CountryNpc(npc, game);
		for (CountryNpc cNPC : messageNpc) {
			if (countryNpc.equals(cNPC)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 当达成时候调用此方法
	 * 1)奖励经验
	 * @param game
	 */
	public void onDeilver() {
		// 奖励经验
		long exp = PeopleSearchManager.getExp(owner, score);
		owner.addExp(exp, ExperienceManager.ADDEXP_REASON_PEOPLESEARCH);
		CountryManager.getInstance().addExtraExp(owner, exp);
		// 奖励道具
		Article article = ArticleManager.getInstance().getArticle(PeopleSearchManager.prizeArticleName);
		if (article != null) {
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_PEOPLESEARCH, owner, PeopleSearchManager.prizeArticleColor, 1, true);
				MailManager.getInstance().sendMail(owner.getId(), new ArticleEntity[] { ae }, Translate.恭喜你完成了斩妖降魔, Translate.恭喜你完成了斩妖降魔, 0, 0, 0, Translate.斩妖降魔);
				// 活跃度统计
				ActivenessManager.getInstance().record(owner, ActivenessType.斩妖降魔);
				try {
					EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { owner.getId(), RecordAction.完成斩妖除魔次数, 1L});
					EventRouter.getInst().addEvent(evt2);
				} catch (Exception eex) {
					PlayerAimManager.logger.error("[目标系统] [统计斩妖除魔次数异常] [" + owner.getLogString() + "]", eex);
				}
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [杀死了BOSS] [完成] [" + peopleTemplet.getTarget().toString() + "] [获得经验:" + exp + "] [奖励物品:" + article.getName() + "] [物品ID:" + ae.getId() + "] [颜色:" + ae.getColorType() + "]");

				}
				
			} catch (Exception e) {
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [杀死了BOSS] [完成] [" + peopleTemplet.getTarget().toString() + "] [奖励物品失败]", e);
				}
			}
		}
	}

	public boolean isTarget (CountryNpc countryNpc){
		return peopleTemplet.getTarget().equals(countryNpc);
	}
	
	public int getTempletId() {
		return templetId;
	}

	public void setTempletId(int templetId) {
		this.templetId = templetId;
	}

	public PeopleTemplet getPeopleTemplet() {
		return peopleTemplet;
	}

	public void setPeopleTemplet(PeopleTemplet peopleTemplet) {
		this.peopleTemplet = peopleTemplet;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public List<CountryNpc> getVisitedNpc() {
		return visitedNpc;
	}

	public void setVisitedNpc(List<CountryNpc> visitedNpc) {
		this.visitedNpc = visitedNpc;
	}

	public boolean[] getSnooped() {
		return snooped;
	}

	public void setSnooped(boolean[] snooped) {
		this.snooped = snooped;
	}

	public CountryNpc[] getMessageNpc() {
		return messageNpc;
	}

	public void setMessageNpc(CountryNpc[] messageNpc) {
		this.messageNpc = messageNpc;
	}

	public int[] getMessageIndex() {
		return messageIndex;
	}

	public void setMessageIndex(int[] messageIndex) {
		this.messageIndex = messageIndex;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "PeopleSearch [templetId=" + templetId + ", visitedNpc=" + visitedNpc + ", snooped=" + Arrays.toString(snooped) + ", messageNpc=" + Arrays.toString(messageNpc) + ", messageIndex=" + Arrays.toString(messageIndex) + ", found=" + found + "]";
	}
}