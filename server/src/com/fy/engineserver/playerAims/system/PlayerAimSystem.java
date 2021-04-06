package com.fy.engineserver.playerAims.system;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.GameDataRecord;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.QUERY_ALL_AIMS_CHAPTER_REQ;
import com.fy.engineserver.message.QUERY_ALL_AIMS_CHAPTER_RES;
import com.fy.engineserver.message.QUERY_COMPLETE_AIM_REQ;
import com.fy.engineserver.message.QUERY_COMPLETE_AIM_RES;
import com.fy.engineserver.message.QUERY_ONE_AIMS_REQ;
import com.fy.engineserver.message.QUERY_ONE_AIMS_RES;
import com.fy.engineserver.message.QUERY_ONE_CHAPTER_REQ;
import com.fy.engineserver.message.QUERY_ONE_CHAPTER_RES;
import com.fy.engineserver.message.RECEIVE_AIM_REWARD_REQ;
import com.fy.engineserver.message.RECEIVE_AIM_REWARD_RES;
import com.fy.engineserver.playerAims.clientModel.Article2Client;
import com.fy.engineserver.playerAims.clientModel.PlayerAims;
import com.fy.engineserver.playerAims.instance.PlayerAim;
import com.fy.engineserver.playerAims.instance.PlayerAimsEntity;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager;
import com.fy.engineserver.playerAims.model.ChapterModel;
import com.fy.engineserver.playerAims.model.PlayerAimModel;
import com.fy.engineserver.playerTitles.PlayerTitle;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class PlayerAimSystem extends SubSystemAdapter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "PlayerAimSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[] {"QUERY_ALL_AIMS_CHAPTER_REQ","QUERY_ONE_CHAPTER_REQ","QUERY_ONE_AIMS_REQ","RECEIVE_AIM_REWARD_REQ","QUERY_COMPLETE_AIM_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, PlayerAimManager.logger);
		if(PlayerAimManager.logger.isDebugEnabled()) {
			PlayerAimManager.logger.debug("[收到玩家请求] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]");
		}
		try {
			if(message instanceof QUERY_ALL_AIMS_CHAPTER_REQ) {			//请求所有目标名
				return handle_QUERY_ALL_AIMS_CHAPTER_REQ(conn, message, player);
			} else if (message instanceof QUERY_ONE_CHAPTER_REQ) {		//请求一个章节的目标
				return handle_QUERY_ONE_CHAPTER_REQ(conn, message, player);
			} else if (message instanceof QUERY_ONE_AIMS_REQ) {			//请求单个目标
				return handle_QUERY_ONE_AIMS_REQ(conn, message, player);
			} else if (message instanceof RECEIVE_AIM_REWARD_REQ) {		//领取目标奖励
				return handle_RECEIVE_AIM_REWARD_REQ(conn, message, player);
			} else if (message instanceof QUERY_COMPLETE_AIM_REQ) {
				return handle_QUERY_COMPLETE_AIM_REQ(conn,message,player);
			}
		} catch(Exception e) {
			PlayerAimManager.logger.error("[目标系统] [处理目标协议出错] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]", e);
		}
		return null;
	}
	
	/**
	 * 请求别人发到世界的目标
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_COMPLETE_AIM_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_COMPLETE_AIM_REQ req = (QUERY_COMPLETE_AIM_REQ) message;
		QUERY_COMPLETE_AIM_RES resp = null;
		try {
			Player tempP = GamePlayerManager.getInstance().getPlayer(req.getPlayerId());
			PlayerAimModel am = PlayerAimManager.instance.aimMaps.get(req.getAimId());
			byte completeType = PlayerAimeEntityManager.instance.getAimReceiveStatus(tempP, req.getAimId());
			long compTie = PlayerAimeEntityManager.instance.getAimCompleteTime(tempP, req.getAimId());
			int aimColorType = am.getFrameColor();
			String completeTime = "";
			if(completeType >= 0) {
				completeType = 1;
				if(compTie > 0) {
					completeTime = new Timestamp(compTie).toString().split(" ")[0];
				} else {
					PlayerAimManager.logger.warn("[目标系统] [获取目标完成时间异常] [" + tempP.getLogString() + "] [aimId :" + req.getAimId() + "]");
				}
			} else {
				completeType = -1;
			}
			resp = new QUERY_COMPLETE_AIM_RES(message.getSequnceNum(), tempP.getName(), am.getIcon(), am.getAimName(), aimColorType, am.getDescription(), 
					am.getScore(), completeTime, completeType);
			if(PlayerAimManager.logger.isDebugEnabled()) {
				PlayerAimManager.logger.debug("[目标系统] [handle_QUERY_COMPLETE_AIM_REQ返回] [" + resp.getAimName() +"、"+ resp.getAimScore() + 
						"、"+ resp.getCompleteTime() +"、"+ resp.getCompleteType()+"、"+resp.getDescription()+"、"+resp.getIcon() +"、"+ resp.getPlayerName() +"]");
			}
		} catch (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [请求查看别人发到世界的目标异常] [" + player.getLogString() + "] [targetPlayerId :" + req.getPlayerId() + "] [目标id:" + req.getAimId() + "]", e);
		}
		if(PlayerAimManager.logger.isDebugEnabled()) {
			PlayerAimManager.logger.debug("[目标系统] [handle_QUERY_COMPLETE_AIM_REQ返回] [" + resp + "]");
		}
		return resp;
	}
	/**
	 * 请求全部目标章节名
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_ALL_AIMS_CHAPTER_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_ALL_AIMS_CHAPTER_REQ req = (QUERY_ALL_AIMS_CHAPTER_REQ) message;
		if (PlayerAimManager.logger.isDebugEnabled()) {
			PlayerAimManager.logger.debug("[目标系统] [handle_QUERY_ALL_AIMS_CHAPTER_REQ] [" + req.getPlayerId() + "] [" + player.getLogString() + "]");
		}
		String currChapter = null;
		PlayerAimsEntity entity = null;
		if(req.getPlayerId() <= 0) {
			currChapter = PlayerAimManager.instance.getPlayerCurrChapter(player);
			entity = PlayerAimeEntityManager.instance.getEntity(player.getId());
		} else {
//			if(GamePlayerManager.getInstance().isOnline(req.getPlayerId())) {
				try {
					Player tempPlayer = GamePlayerManager.getInstance().getPlayer(req.getPlayerId());
					currChapter = PlayerAimManager.instance.getPlayerCurrChapter(tempPlayer);
					entity = PlayerAimeEntityManager.instance.getEntity(req.getPlayerId());
				} catch (Exception e) {
					PlayerAimManager.logger.error("[目标系统] [handle_QUERY_ALL_AIMS_CHAPTER_REQ.查看别人的目标系统异常] [" + player.getLogString() + "] [被查看人id:" + req.getPlayerId() + "]", e);
				}
//			} else {
//				player.sendError(Translate.text_marriage_037);
//				return null;
//			}
		}
		if(currChapter == null || entity == null) {
			PlayerAimManager.logger.error("[目标系统] [handle_QUERY_ALL_AIMS_CHAPTER_REQ.查看别人的目标系统异常,currChapter:"+currChapter + "&entity:" + entity +"] [" + player.getLogString() + "] [被查看人id:" + req.getPlayerId() + "]");
			player.sendError(Translate.text_marriage_037);
			return null;
		}
		int allScore = 0;							//所有目标可以获取到的总积分
		int currentScore = entity.getTotalScore();						//当前玩家获得的总积分(未获取)
		List<String> tempStr = new ArrayList<String>();
		List<Integer> tempSoc = new ArrayList<Integer>();
		List<Integer> tempCurSoc = new ArrayList<Integer>();
		List<String> tempSubTitle = new ArrayList<String>();
		List<Integer> tempCanReceive = new ArrayList<Integer>();
		for(String str : PlayerAimManager.instance.chapterMaps.keySet()) {
			tempStr.add(str);
			ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(str);
			tempSoc.add(cm.getTotalScore());
			tempSubTitle.add(cm.getSubtitle());
			int count = PlayerAimeEntityManager.instance.getChapterCanReceiveAmount(player, str);
			tempCanReceive.add(count);
			int tempCs = 0;
			for (PlayerAimModel pam : cm.getAimsList()) {
				for (PlayerAim pa : entity.getAimList()) {
					if (pam.getId() == pa.getAimId()) {
						tempCs += pam.getScore();
						break;
					}
				}
			}
			tempCurSoc.add(tempCs);
		}
		String[] chapterNames = new String[tempStr.size()];
		String[] subTitle = new String[tempStr.size()];
		int[] totalChapterScore = new int[tempStr.size()];				//章节可获取到的总积分
		int[] currentChapterScore = new int[tempStr.size()];			//当前章节玩家获取到的总积分
		int[] canReceive = new int[tempStr.size()];
		for(int i=0; i<tempStr.size(); i++) {
			chapterNames[i] = tempStr.get(i);
			totalChapterScore[i] = tempSoc.get(i);
			allScore+=tempSoc.get(i);
			subTitle[i] = tempSubTitle.get(i);
			currentChapterScore[i] = tempCurSoc.get(i);
			canReceive[i] = tempCanReceive.get(i);
		}
		QUERY_ALL_AIMS_CHAPTER_RES res = new QUERY_ALL_AIMS_CHAPTER_RES(message.getSequnceNum(), req.getPlayerId(), chapterNames, subTitle, totalChapterScore, currentChapterScore, canReceive, allScore, currentScore, currChapter);
		if (PlayerAimManager.logger.isDebugEnabled()) {
			PlayerAimManager.logger.debug("[目标系统] [QUERY_ALL_AIMS_CHAPTER_RES] [" + Arrays.toString(res.getTotalChapterScore()) + "] [" + Arrays.toString(res.getCurrentChapterScore()));
		}
		return res;
	}
	/**
	 * 请求单个目标章节信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_ONE_CHAPTER_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_ONE_CHAPTER_REQ req = (QUERY_ONE_CHAPTER_REQ) message;
		String chapterName = req.getChapterName();
		Player tempP = null;
		if(req.getPlayerId() <= 0 ) {
			tempP = player;
		} else {
//			if(GamePlayerManager.getInstance().isOnline(req.getPlayerId())) {
				try {
					tempP = GamePlayerManager.getInstance().getPlayer(req.getPlayerId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					PlayerAimManager.logger.error("[目标系统] [获取其他玩家出错] [" + player.getLogString() + "] [pId:" + req.getPlayerId() + "]");
					player.sendError(Translate.text_marriage_037);
					return null;
				}
//			} else {
//				player.sendError(Translate.text_marriage_037);
//				return null;
//			}
		}
		if("default".equals(req.getChapterName().toLowerCase())) {
			chapterName = PlayerAimManager.instance.getPlayerCurrChapter(tempP);
		}
		ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(chapterName);
		if(cm == null) {
			PlayerAimManager.logger.warn("[目标系统] [没有找到对应章节配置] [" + tempP.getLogString() + "] [客户端发过来的章节名:" + chapterName + "]");
			return null;
		}
//		PlayerAimsEntity entity = PlayerAimeEntityManager.instance.getEntity(tempP.getId());
		int complateNum = 0;
		List<PlayerAims> tempMes = new ArrayList<PlayerAims>();
		GameDataRecord gdr = null;
		for(PlayerAimModel pam : cm.getAimsList()) {
			PlayerAims pas = null;
			byte receiveStatus = PlayerAimeEntityManager.instance.getAimReceiveStatus(tempP, pam.getId());
			int process = 0;
			if(pam.isDisplay() || receiveStatus >= 0) {			
				pas = new PlayerAims();
				if(receiveStatus >= 0) {
					process = pam.getNum();
					complateNum++;
				} else {
					gdr = AchievementManager.getInstance().getPlayerDataRecord(tempP, pam.getAction());
					if(gdr != null) {
						process = (int) (gdr.getNum() > pam.getNum() ? pam.getNum() : gdr.getNum());
					}
				}
			}
			if(pas != null) {
				pas.setId(pam.getId());
				pas.setAimName(pam.getAimName());
				pas.setAimNum(pam.getNum());
				String tempDes = pam.getDescription();
				pas.setDescription(tempDes);
				pas.setFrameColor(pam.getFrameColor());
				pas.setIcon(pam.getIcon());
				pas.setReceiveStatus(receiveStatus);
				pas.setShowProcess(pam.getShowProgress());
				pas.setCurrentNum(process);
				pas.setVipReceiveLimit(pam.getVipLimit());
				tempMes.add(pas);
			}
		}
		PlayerAims[] messes = new PlayerAims[tempMes.size()];
		for(int i=0; i<tempMes.size(); i++) {
			messes[i] = tempMes.get(i);
		}
		byte receiveType = PlayerAimeEntityManager.instance.getChapterReceiveStatus(tempP, chapterName);
		int currentScore = PlayerAimeEntityManager.instance.getChapterScore(tempP, chapterName);
		List<Article2Client> articleList = PlayerAimManager.instance.chapterRewardMap.get(chapterName);
		int len = articleList == null ? 0 : articleList.size();
		long[] rewardArticles = new long[len];
		long[] rewardNums = new long[len];
		for(int i=0; i<len; i++) {
			rewardArticles[i] = articleList.get(i).getArticleId();
			rewardNums[i] = articleList.get(i).getRewardNum();
		}
		if(cm.getRewardTitle() != null && !cm.getRewardTitle().isEmpty()) {		//取临时的称号。绑银等称号
			rewardArticles = Arrays.copyOf(rewardArticles, rewardArticles.length + 1);
			rewardNums = Arrays.copyOf(rewardNums, rewardNums.length + 1);
			rewardArticles[rewardArticles.length-1] = PlayerAimManager.instance.otherTempReward.get(0).getArticleId();
			rewardNums[rewardNums.length-1] = PlayerAimManager.instance.otherTempReward.get(0).getRewardNum();
		}
		if(cm.getRewardBindYin() > 0) {
			rewardArticles = Arrays.copyOf(rewardArticles, rewardArticles.length + 1);
			rewardNums = Arrays.copyOf(rewardNums, rewardNums.length + 1);
			rewardArticles[rewardArticles.length-1] = PlayerAimManager.instance.otherTempReward.get(1).getArticleId();
			rewardNums[rewardNums.length-1] = cm.getRewardBindYin() / PlayerAimManager.tempRate[1];
		}
		if(cm.getRewardGongXun() > 0) {
			rewardArticles = Arrays.copyOf(rewardArticles, rewardArticles.length + 1);
			rewardNums = Arrays.copyOf(rewardNums, rewardNums.length + 1);
			rewardArticles[rewardArticles.length-1] = PlayerAimManager.instance.otherTempReward.get(3).getArticleId();
			rewardNums[rewardNums.length-1] = cm.getRewardGongXun() / PlayerAimManager.tempRate[3];
		}
		if(cm.getRewardGongzi() > 0) {
			rewardArticles = Arrays.copyOf(rewardArticles, rewardArticles.length + 1);
			rewardNums = Arrays.copyOf(rewardNums, rewardNums.length + 1);
			rewardArticles[rewardArticles.length-1] = PlayerAimManager.instance.otherTempReward.get(2).getArticleId();
			rewardNums[rewardNums.length-1] = cm.getRewardGongzi() / PlayerAimManager.tempRate[2];
		}
		QUERY_ONE_CHAPTER_RES res = new QUERY_ONE_CHAPTER_RES(message.getSequnceNum(), req.getPlayerId(), chapterName, cm.getIcon(), cm.getTotalScore(), cm.getScoreLimit(), currentScore, cm.getDescription(), 
				rewardArticles, rewardNums, cm.getLevelLimit(), cm.getMulReward4Vip(), cm.getVipLimit(), cm.getAimsList().size(), complateNum, receiveType, messes);
		if(PlayerAimManager.logger.isDebugEnabled()) {
			PlayerAimManager.logger.debug("[目标系统] [QUERY_ONE_CHAPTER_RES] [complateNum:" + res.getComplateNum() + "] [totalNum:" + res.getTotalAimNum() + "] [currentScore:" + res.getCurrentScore() + "]");
		}
		return res;
	}
	/**
	 * 请求单个目标信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_ONE_AIMS_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_ONE_AIMS_REQ req = (QUERY_ONE_AIMS_REQ) message;
		if(PlayerAimManager.logger.isDebugEnabled()) {
			PlayerAimManager.logger.debug("[目标系统] [请求单个目标] [" + req.getPlayerId() + "]");
		}
		Player tempP = null;
		if(req.getPlayerId() <= 0) {
			tempP = player;
		} else {
//			if(GamePlayerManager.getInstance().isOnline(req.getPlayerId())) {
				try {
					tempP = GamePlayerManager.getInstance().getPlayer(req.getPlayerId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					PlayerAimManager.logger.error("[目标系统] [获取其他玩家出错2] [" + player.getLogString() + "] [pId:" + req.getPlayerId() + "]");
					player.sendError(Translate.text_marriage_037);
					return null;
				}
//			} else {
//				player.sendError(Translate.text_marriage_037);
//				if(PlayerAimManager.logger.isDebugEnabled()) {
//					PlayerAimManager.logger.debug("[目标系统] [请求单个目标,失败,对方不在线] [" + req.getPlayerId() + "] ");
//				}
//				return null;
//			}
		}
		PlayerAimModel pam = PlayerAimManager.instance.aimMaps.get(req.getAimId());
		if(pam == null) {
			PlayerAimManager.logger.warn("[目标系统] [没有找到对应id的目标] [" + tempP.getLogString() + "] [客户端传过来的目标id：" + req.getAimId() + "]");
			return null;
		}
		byte receiveType = PlayerAimeEntityManager.instance.getAimReceiveStatus(tempP, req.getAimId());
		List<Article2Client> articleList = PlayerAimManager.instance.aimRewardMap.get(req.getAimId());
		int len = articleList == null ? 0 : articleList.size();
		long[] rewardArticles = new long[len];
		long[] rewardNums = new long[len];
		for(int i=0; i<len; i++) {
			rewardArticles[i] = articleList.get(i).getArticleId();
			rewardNums[i] = articleList.get(i).getRewardNum();
		}
		if(pam.getRewardTitle() != null && !pam.getRewardTitle().isEmpty()) {		//取临时的称号。绑银等称号
			rewardArticles = Arrays.copyOf(rewardArticles, rewardArticles.length + 1);
			rewardNums = Arrays.copyOf(rewardNums, rewardNums.length + 1);
			rewardArticles[rewardArticles.length-1] = PlayerAimManager.instance.otherTempReward.get(0).getArticleId();
			rewardNums[rewardNums.length-1] = PlayerAimManager.instance.otherTempReward.get(0).getRewardNum();
		}
		if(pam.getRewardBindYin() > 0) {
			rewardArticles = Arrays.copyOf(rewardArticles, rewardArticles.length + 1);
			rewardNums = Arrays.copyOf(rewardNums, rewardNums.length + 1);
			rewardArticles[rewardArticles.length-1] = PlayerAimManager.instance.otherTempReward.get(1).getArticleId();
			rewardNums[rewardNums.length-1] = pam.getRewardBindYin() / PlayerAimManager.tempRate[1];
		}
		if(pam.getRewardGongXun() > 0) {
			rewardArticles = Arrays.copyOf(rewardArticles, rewardArticles.length + 1);
			rewardNums = Arrays.copyOf(rewardNums, rewardNums.length + 1);
			rewardArticles[rewardArticles.length-1] = PlayerAimManager.instance.otherTempReward.get(3).getArticleId();
			rewardNums[rewardNums.length-1] = pam.getRewardGongXun() / PlayerAimManager.tempRate[3];
		}
		if(pam.getRewardGongzi() > 0) {
			rewardArticles = Arrays.copyOf(rewardArticles, rewardArticles.length + 1);
			rewardNums = Arrays.copyOf(rewardNums, rewardNums.length + 1);
			rewardArticles[rewardArticles.length-1] = PlayerAimManager.instance.otherTempReward.get(2).getArticleId();
			rewardNums[rewardNums.length-1] = pam.getRewardGongzi() / PlayerAimManager.tempRate[2];
		}
		String tempDes = pam.getNavigationDes();
		try {
			if (pam.getAction().getType() == RecordAction.集齐20个称号1.getType()) {
//				if (player.getPlayerTitles() != null && player.getPlayerTitles().size() > 13) {
					for (String str : PlayerAimManager.称号成就1) {
						for (PlayerTitle pt : tempP.getPlayerTitles()) {
							if (str.equals(pt.getTitleName())) {
								tempDes = tempDes.replace(str, PlayerAimManager.startColor + str + PlayerAimManager.endColor);	
							}
						}
					}
//				}
			}
			if (pam.getAction().getType() == RecordAction.集齐20个称号2.getType()) {
				for (String str : PlayerAimManager.称号成就2) {
					for (PlayerTitle pt : tempP.getPlayerTitles()) {
						if (str.equals(pt.getTitleName())) {
							tempDes = tempDes.replace(str, PlayerAimManager.startColor + str + PlayerAimManager.endColor);	
						}
					}
				}
			}
			if (pam.getAction().getType() == RecordAction.集齐13个称号.getType()) {
				for (String str : PlayerAimManager.称号成就3) {
					for (PlayerTitle pt : tempP.getPlayerTitles()) {
						if (str.equals(pt.getTitleName())) {
							tempDes = tempDes.replace(str, PlayerAimManager.startColor + str + PlayerAimManager.endColor);	
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
		try {
			@SuppressWarnings("unchecked")
			List<Integer> tempList = (List<Integer>) PlayerAimManager.instance.disk.get(PlayerAimManager.shaguaiKey + "_" + tempP.getId());
			@SuppressWarnings("unchecked")
			List<Long> tempList2 = (List<Long>) PlayerAimManager.instance.disk.get(PlayerAimManager.renwuKey + "_" + tempP.getId());
			if (tempList != null && tempList.size() > 0) {
				if (pam.getAction().getType() == RecordAction.击杀漏网的世界BOSS.getType()) {
					for (int i=0; i<PlayerAimManager.杀死漏网的世界boss.length; i++) {
						if (tempList.contains(PlayerAimManager.杀死漏网的世界boss[i])) {
							tempDes = tempDes.replace(PlayerAimManager.世界boss[i], PlayerAimManager.startColor + PlayerAimManager.世界boss[i] + PlayerAimManager.endColor);
						}
					}
				} else if (pam.getAction().getType() == RecordAction.击杀仙界俩boss.getType()) {
					for (int i=0; i<PlayerAimManager.击杀仙界俩boss.length; i++) {
						if (tempList.contains(PlayerAimManager.击杀仙界俩boss[i])) {
							tempDes = tempDes.replace(PlayerAimManager.击杀仙界俩bo[i], PlayerAimManager.startColor + PlayerAimManager.击杀仙界俩bo[i] + PlayerAimManager.endColor);
						}
					}
					
				} else if (pam.getAction().getType() == RecordAction.杀死境界怪物.getType()) {
					for (int i=0; i<PlayerAimManager.杀死境界怪物成就.length; i++) {
						if (tempList.contains(PlayerAimManager.杀死境界怪物成就[i])) {
							tempDes = tempDes.replace(PlayerAimManager.境界怪[i], PlayerAimManager.startColor + PlayerAimManager.境界怪[i] + PlayerAimManager.endColor);
						}
					}
				} else if (pam.getAction().getType() == RecordAction.杀死精英怪.getType()) {
					for (int i=0; i<PlayerAimManager.杀死精英怪物成就.length; i++) {
						if (tempList.contains(PlayerAimManager.杀死精英怪物成就[i])) {
							tempDes = tempDes.replace(PlayerAimManager.精英怪[i], PlayerAimManager.startColor + PlayerAimManager.精英怪[i] + PlayerAimManager.endColor);
						}
					}
				} else if (pam.getAction().getType() == RecordAction.杀死固定20只怪.getType()) {
					int tempNum = 0;
					for (int i=0; i<PlayerAimManager.小试牛刀怪物Id.length; i++) {
						if (tempList.contains(PlayerAimManager.小试牛刀怪物Id[i])) {
							tempDes = tempDes.replace(PlayerAimManager.小试牛刀怪物[i], PlayerAimManager.startColor + PlayerAimManager.小试牛刀怪物[i] + PlayerAimManager.endColor);
							tempNum++;
						}
					}
					GameDataRecord gdr = AchievementManager.getInstance().getPlayerDataRecord(tempP, RecordAction.杀死固定20只怪);
					if (gdr != null && gdr.getNum() < tempNum) {
						int temp = (int) (tempNum - gdr.getNum());
						for (int ii=0; ii<temp; ii++) {
							AchievementManager.getInstance().record(tempP, RecordAction.杀死固定20只怪);
						}
					}
				}
			}
			if (tempList2 != null && tempList2.size() > 0) {
				if (pam.getAction() == RecordAction.极限任务组1) {
					for (int i=0; i<PlayerAimManager.极限任务1Id.length; i++) {
						if (tempList2.contains(Long.parseLong(PlayerAimManager.极限任务1Id[i]+""))) {
							tempDes = tempDes.replace(PlayerAimManager.极限任务1[i], PlayerAimManager.startColor + PlayerAimManager.极限任务1[i] + PlayerAimManager.endColor);
						}
					}
				} else if (pam.getAction() == RecordAction.极限任务组2) {
					for (int i=0; i<PlayerAimManager.极限任务2Id.length; i++) {
						if (tempList2.contains(Long.parseLong(PlayerAimManager.极限任务2Id[i]+""))) {
							tempDes = tempDes.replace(PlayerAimManager.极限任务2[i], PlayerAimManager.startColor + PlayerAimManager.极限任务2[i] + PlayerAimManager.endColor);
						}
					}
				} else if (pam.getAction() == RecordAction.极限任务组3) {
					for (int i=0; i<PlayerAimManager.极限任务3Id.length; i++) {
						if (tempList2.contains(Long.parseLong(PlayerAimManager.极限任务3Id[i]+""))) {
							tempDes = tempDes.replace(PlayerAimManager.极限任务3[i], PlayerAimManager.startColor + PlayerAimManager.极限任务3[i] + PlayerAimManager.endColor);
						}
					}
				} else if (pam.getAction() == RecordAction.极限任务组4) {
					for (int i=0; i<PlayerAimManager.极限任务4Id.length; i++) {
						if (tempList2.contains(Long.parseLong(PlayerAimManager.极限任务4Id[i]+""))) {
							tempDes = tempDes.replace(PlayerAimManager.极限任务4[i], PlayerAimManager.startColor + PlayerAimManager.极限任务4[i] + PlayerAimManager.endColor);
						}
					}
				} else if (pam.getAction() == RecordAction.极限任务组5) {
					for (int i=0; i<PlayerAimManager.极限任务5Id.length; i++) {
						if (tempList2.contains(Long.parseLong(PlayerAimManager.极限任务5Id[i]+""))) {
							tempDes = tempDes.replace(PlayerAimManager.极限任务5[i], PlayerAimManager.startColor + PlayerAimManager.极限任务5[i] + PlayerAimManager.endColor);
						}
					}
				} else if (pam.getAction() == RecordAction.极限任务组6) {
					for (int i=0; i<PlayerAimManager.极限任务6Id.length; i++) {
						if (tempList2.contains(Long.parseLong(PlayerAimManager.极限任务6Id[i]+""))) {
							tempDes = tempDes.replace(PlayerAimManager.极限任务6[i], PlayerAimManager.startColor + PlayerAimManager.极限任务6[i] + PlayerAimManager.endColor);
						}
					}
				} 
			}
		} catch (Exception e) {
			PlayerAimManager.logger.error("[高亮显示玩家杀怪描述] [异常] [" + tempP.getLogString() + "]", e); 
		}
		QUERY_ONE_AIMS_RES res = new QUERY_ONE_AIMS_RES(message.getSequnceNum(), req.getPlayerId(), req.getAimId(),pam.getLevelLimit(), pam.getVipLimit(), pam.getMulReward4Vip(),tempDes, 
				rewardArticles, rewardNums, receiveType);
		if(PlayerAimManager.logger.isDebugEnabled()) {
			PlayerAimManager.logger.debug("[目标系统] [返回导航描述] [" + res.getDescription() + "]");
		}
		return res;
	}
	/**
	 * 领取目标奖励
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_RECEIVE_AIM_REWARD_REQ(Connection conn, RequestMessage message, Player player) {
		RECEIVE_AIM_REWARD_REQ req = (RECEIVE_AIM_REWARD_REQ) message;
		boolean isVipReceive = req.getReceiveType() == 1;
		PlayerAimeEntityManager.instance.receiveReward(player, req.getAimId(), req.getChapterName(), isVipReceive);
		RECEIVE_AIM_REWARD_RES res = new RECEIVE_AIM_REWARD_RES(message.getSequnceNum(), "11");
		return res;
	}
	

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
