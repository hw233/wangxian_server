package com.fy.engineserver.gm.newfeedback;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.GameSubSystem;
import com.fy.engineserver.message.FEEDBACK_COMMIT_REQ;
import com.fy.engineserver.message.FEEDBACK_DELETE_REQ;
import com.fy.engineserver.message.FEEDBACK_HOME_PAGE_REQ;
import com.fy.engineserver.message.FEEDBACK_LOOK_REQ;
import com.fy.engineserver.message.FEEDBACK_LOOK_SCORE_REQ;
import com.fy.engineserver.message.FEEDBACK_PLAYER_TALK_REQ;
import com.fy.engineserver.message.FEEDBACK_SCORE_REQ;
import com.fy.engineserver.message.NOTICE_OPEN_URL_REQ;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class NewFeedbackSubSystem implements GameSubSystem {

	public static Logger logger = LoggerFactory.getLogger(NewFeedbackSubSystem.class);

	GameNetworkFramework framework;

	public void init() throws Exception {
		
		ServiceStartRecord.startLog(this);
	}

	@Override
	public String getName() {
		return "NewFeedbackSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[] { "FEEDBACK_HOME_PAGE_REQ", "FEEDBACK_DELETE_REQ", "FEEDBACK_SCORE_REQ", "FEEDBACK_PLAYER_TALK_REQ", "FEEDBACK_GM_TALK_RES", "FEEDBACK_LOOK_REQ", "FEEDBACK_LOOK_SCORE_REQ", "FEEDBACK_COMMIT_REQ" };
	}

	public static boolean useWebFeedBack = false;
	public static String baseWebURL = "";//"http://w.sqage.com/m/service_index.jsp";

	static {
		if (TestServerConfigManager.isTestServer()) {
			useWebFeedBack = false;
		}
		if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
			useWebFeedBack = false;
		}
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = (Player) conn.getAttachmentData("Player");
		String playername = "";
		int viplevel = 0;
		String username = "";
		if (player != null) {
			player.setLastRequestTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			playername = player.getName();
			viplevel = player.getVipLevel();
			username = player.getUsername();
		}
		logger.warn("[req:" + message.getTypeDescription() + "] [用户名：" + username + "] [角色名：" + playername + "] [VIP:" + viplevel + "]");
		if (message instanceof FEEDBACK_HOME_PAGE_REQ) {
			if (useWebFeedBack) {
				GameConstants gc = GameConstants.getInstance();
				StringBuffer url = new StringBuffer();
				url.append(baseWebURL);
				url.append("?").append("userName=").append(URLEncoder.encode(player.getUsername(), "utf-8"));
				url.append("&").append("playerId=").append(player.getId());
				url.append("&").append("playerName=").append(URLEncoder.encode(player.getName(), "utf-8"));
				url.append("&").append("serverName=").append(gc == null ? "--" : URLEncoder.encode(gc.getServerName(), "utf-8"));
				url.append("&").append("vipLevel=").append(player.getVipLevel());
				NOTICE_OPEN_URL_REQ req = new NOTICE_OPEN_URL_REQ(message.getSequnceNum(), url.toString());
				player.addMessageToRightBag(req); 
				return null;
			}
			FEEDBACK_HOME_PAGE_REQ req = (FEEDBACK_HOME_PAGE_REQ) message;
			return NewFeedbackManager.getInstance().getFeedbackHomePageRes(req.getSequnceNum(), player);
		} else if (message instanceof FEEDBACK_DELETE_REQ) {
			FEEDBACK_DELETE_REQ req = (FEEDBACK_DELETE_REQ) message;
			return NewFeedbackManager.getInstance().getFeedbackDeleteRes(req.getSequnceNum(), player, req.getId());
		} else if (message instanceof FEEDBACK_SCORE_REQ) {
			FEEDBACK_SCORE_REQ req = (FEEDBACK_SCORE_REQ) message;
			return NewFeedbackManager.getInstance().socreFeedback(req.getSequnceNum(), req.getId(), player, req.getSelect(), username);
		} else if (message instanceof FEEDBACK_PLAYER_TALK_REQ) {
			FEEDBACK_PLAYER_TALK_REQ req = (FEEDBACK_PLAYER_TALK_REQ) message;
			return NewFeedbackManager.getInstance().playerTalk(req.getSequnceNum(), player, req.getId(), username, req.getTalk());
		} else if (message instanceof FEEDBACK_LOOK_REQ) {
			FEEDBACK_LOOK_REQ req = (FEEDBACK_LOOK_REQ) message;
			return NewFeedbackManager.getInstance().lookFeedback(req.getSequnceNum(), player, req.getId(), username);
		} else if (message instanceof FEEDBACK_LOOK_SCORE_REQ) {
			FEEDBACK_LOOK_SCORE_REQ req = (FEEDBACK_LOOK_SCORE_REQ) message;
			return NewFeedbackManager.getInstance().lookScore(req.getSequnceNum(), player, req.getId(), username);
		} else if (message instanceof FEEDBACK_COMMIT_REQ) {
			FEEDBACK_COMMIT_REQ req = (FEEDBACK_COMMIT_REQ) message;
			return NewFeedbackManager.getInstance().FeedbackCommit(req.getSequnceNum(), player, username);
		} else {
			logger.warn(".........receive client Message error, unknow message...........messagename:" + message.getTypeDescription());
		}
		return null;

	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		return false;
	}

	public void setGameNetworkFramework(GameNetworkFramework framework) {
		this.framework = framework;
	}
}
