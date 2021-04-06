package com.fy.engineserver.gm.feedback;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gm.feedback.service.FeedbackManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.REPLY_SPECIAL_FEEDBACK_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

public class FeedbackForJsio {

	private long id;

	// 未处理,等待，新，关闭
	private int GmState;
	private int playerState;

	private String fid;

	private String countryname;

	private String career;

	private int playerlevel;

	private String servername;

	// 标志在浏览器显示，当shownum等于0，是没有显示，大于0，是在浏览器的标示
	private int showNum;

	private String channel;

	// private boolean isShow;

	// 玩家的创建新消息的时间，玩家回复新消息的时间
	private long sendTimes;

	private String userName;

	private String playerName;

	private String content;

	// 玩家的创建新消息的时间，玩家回复新消息的时间
	private String sendTimestr;

	// @SimpleColumn(length=1024)
	private String dialogs;

	// BUG 建议 投诉 充值 其他
	private int feedbackType;

	// 是否发送了评价
	private boolean sendJudge = false;

	// 玩家已经评价
	private boolean alreadyJudge = false;
	// 评价等级
	private int judge;

	// 发送反馈的玩家Id
	private long playerId;

	private long beginDate;

	// VIP等级
	private int vipLevel;

	// 跟踪状态
	private boolean isFollow;

	// 上一个回复的gm 的id
	private String lastGmId = "";

	// 主题
	private String subject;

	public boolean noticePlayer = false;

	public boolean noticeGm = false;

	public FeedbackForJsio() {

	}

	public FeedbackForJsio(Feedback feedback) {
		this.id = feedback.getId();
		this.GmState = feedback.getGmState();
		this.playerState = feedback.getPlayerState();
		this.fid = feedback.getFid();
		this.countryname = feedback.getCountryname();
		this.career = feedback.getCareer();
		this.playerlevel = feedback.getPlayerlevel();
		this.servername = feedback.getServername();
		this.channel = feedback.getChannel();
		this.sendTimes = feedback.getSendTimes();
		this.userName = feedback.getUserName();
		this.playerName = feedback.getPlayerName();
		this.content = feedback.getContent();
		this.sendTimestr = feedback.getSendTimestr();
		this.dialogs = feedback.getDialogs();
		this.feedbackType = feedback.getFeedbackType();
		this.sendJudge = feedback.isSendJudge();
		this.alreadyJudge = feedback.isAlreadyJudge();
		this.judge = feedback.getJudge();
		this.playerId = feedback.getPlayerId();
		this.beginDate = feedback.getBeginDate();
		this.vipLevel = feedback.getVipLevel();
		this.isFollow = feedback.isFollow();
		this.lastGmId = feedback.getLastGmId();
		this.subject = feedback.getSubject();
		
	}

}
