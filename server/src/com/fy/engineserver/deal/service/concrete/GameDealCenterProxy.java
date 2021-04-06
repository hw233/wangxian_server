package com.fy.engineserver.deal.service.concrete;

import java.util.Map;

import com.fy.engineserver.dajing.DajingStudioManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.deal.Deal;
import com.fy.engineserver.deal.service.DealCenter;
import com.fy.engineserver.exception.KnapsackFullException;
import com.fy.engineserver.message.CREATE_DEAL_REQ;
import com.fy.engineserver.message.DEAL_CANCEL_REQ;
import com.fy.engineserver.message.DEAL_LOCK_RES;
import com.fy.engineserver.message.DEAL_MAKED_REQ;
import com.fy.engineserver.message.DEAL_UPDATE_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.abnormalCount.TradeAbnormalManager;
import com.fy.engineserver.util.ServiceStartRecord;

/**
 * 交易中心代理1
 * 
 * 
 */
public class GameDealCenterProxy extends DealCenter implements Runnable {

	// 超时时间
	public static long timeout = 5 * 60 * 1000;
	// 邀请超时时间
	public static long reqTimeOut = 10*1000;

	protected DealCenter dataDealCenter;

	public void initialize() {
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		Thread t = new Thread(this, Translate.text_3947);
		t.start();

		if(logger.isInfoEnabled())
			logger.info("{} initialize successfully[{}]", new Object[]{this.getClass().getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)});
		ServiceStartRecord.startLog(this);
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void setDataDealCenter(DealCenter dataDealCenter) {
		this.dataDealCenter = dataDealCenter;
	}

	public void run() {
	}

	@Override
	public Map<String, Deal> getDealMap() {
		return dataDealCenter.getDealMap();
	}

	@Override
	public int getAgreed() {
		return dataDealCenter.getAgreed();
	}

	@Override
	public int getCanceled() {
		return dataDealCenter.getCanceled();
	}

	@Override
	public int getTotalDeal() {
		return dataDealCenter.getTotalDeal();
	}

	@Override
	public boolean agreeDeal(Deal deal, Player player) throws KnapsackFullException, Exception {
		try {
			boolean allAgreed = dataDealCenter.agreeDeal(deal, player);
			// 通知玩家
			if (allAgreed) {
				TradeAbnormalManager.getInstance().isNormalDeal(deal);
				if (deal.getCoinsA() > 0 || deal.getCoinsB() > 0) {
					DajingStudioManager.getInstance().notify_面对面交易银锭(deal.getPlayerA(), deal.getPlayerB(), deal.getCoinsA(), deal.getCoinsB());
				}
				int a银块数 = 0;
				int b银块数 = 0;
				for (int i = 0; i < deal.getEntityIdsA().length ;i ++) {
					if (deal.getEntityIdsA()[i] > 0) {
						ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(deal.getEntityIdsA()[i]);
						if (entity != null) {
							if (entity.getArticleName().equals(Translate.银块)) {
								a银块数 += deal.getCountsA()[i];
							}
						}
					}
				}
				for (int i = 0; i < deal.getEntityIdsB().length ;i ++) {
					if (deal.getEntityIdsB()[i] > 0) {
						ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(deal.getEntityIdsB()[i]);
						if (entity != null) {
							if (entity.getArticleName().equals(Translate.银块)) {
								b银块数 += deal.getCountsB()[i];
							}
						}
					}
				}
				if (a银块数 > 0 || b银块数 > 0) {
					DajingStudioManager.getInstance().notify_面对面交易银块(deal.getPlayerA(), deal.getPlayerB(), a银块数, b银块数);
				}
				DEAL_MAKED_REQ notifyReqA = new DEAL_MAKED_REQ(GameMessageFactory.nextSequnceNum());
				deal.getPlayerA().addMessageToRightBag(notifyReqA);
				DEAL_MAKED_REQ notifyReqB = new DEAL_MAKED_REQ(GameMessageFactory.nextSequnceNum());
				deal.getPlayerB().addMessageToRightBag(notifyReqB);
				return allAgreed;
			} else {
				// 更新状态
				DEAL_UPDATE_REQ reqA = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.getPackageTypeA(),  deal.getIndexesA(), deal.isAgreedA(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.isAgreedB());
				deal.getPlayerA().addMessageToRightBag(reqA);
				DEAL_UPDATE_REQ reqB = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.getPackageTypeB(), deal.getIndexesB(), deal.isAgreedB(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.isAgreedA());
				deal.getPlayerB().addMessageToRightBag(reqB);
			}
		} catch (Exception e) {
			if(logger.isWarnEnabled())
				logger.warn("[达成交易时发生错误] [" + deal.getId() + "] [" + player.getUsername() + "][" + player.getId() + "][" + player.getName() + "] [" + e.getMessage() + "]", e);
			// 通知用户交易被取消
			String message = e.getMessage();
			if (message == null) {
				message = Translate.text_3952;
			}
			DEAL_CANCEL_REQ notifyReqA = new DEAL_CANCEL_REQ(GameMessageFactory.nextSequnceNum(), message);
			deal.getPlayerA().addMessageToRightBag(notifyReqA);
			DEAL_CANCEL_REQ notifyReqB = new DEAL_CANCEL_REQ(GameMessageFactory.nextSequnceNum(), message);
			deal.getPlayerB().addMessageToRightBag(notifyReqB);
		}
		return false;
	}

	@Override
	public void disagreeDeal(Deal deal, Player player) throws Exception {
		dataDealCenter.disagreeDeal(deal, player);
		// 通知客户端改变
		DEAL_UPDATE_REQ reqA = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.getPackageTypeA(),  deal.getIndexesA(), deal.isAgreedA(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.isAgreedB());
		deal.getPlayerA().addMessageToRightBag(reqA);
		DEAL_UPDATE_REQ reqB = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.getPackageTypeB(), deal.getIndexesB(), deal.isAgreedB(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.isAgreedA());
		deal.getPlayerB().addMessageToRightBag(reqB);
//		if (logger.isInfoEnabled()) logger.info("[disAgree] [" + player.getUsername() + "][" + player.getId() + "][" + player.getName() + "] [" + deal.getId() + "]");
		if (logger.isInfoEnabled()) logger.info("[disAgree] [{}][{}][{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),deal.getId()});
	}

	@Override
	public void cancelDeal(Deal deal, Player player) {
		if(deal == null){
			return;
		}
		if(player==null){
			dataDealCenter.cancelDeal(deal, player);
			return;
		}
		String reason = Translate.text_2317 + player.getName() + Translate.text_3953;
		if (player.isFighting()) {
			reason = Translate.text_deal_001;
		}
		
//		DEAL_CANCEL_REQ notifyReqA = new DEAL_CANCEL_REQ(GameMessageFactory.nextSequnceNum(), reason);
//		player.addMessageToRightBag(notifyReqA);
		Player dstPlayer = deal.getDstPlayer(player);
		DEAL_CANCEL_REQ notifyReqB = new DEAL_CANCEL_REQ(GameMessageFactory.nextSequnceNum(), reason);
		dstPlayer.addMessageToRightBag(notifyReqB);
		dataDealCenter.cancelDeal(deal, player);
//		if (logger.isInfoEnabled()) logger.info("[cancelDeal] [" + deal.getId() + "] [" + player.getUsername() + "][" + player.getId() + "][" + player.getName() + "] [" + reason + "]");
		if (!使用新日志格式) {
			if (logger.isInfoEnabled()) logger.info("[cancelDeal] [{}] [{}][{}][{}] [{}]", new Object[]{deal.getId(),player.getUsername(),player.getId(),player.getName(),reason});
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("[操作类别:交易取消] [dealId:" + deal.getId() + "] [" + player.getLogString4Knap() + "] [reason:" + reason + "]");
			}
		}
	}

	@Override
	public Deal createDeal(Player playerA, Player playerB) throws Exception {
		return dataDealCenter.createDeal(playerA, playerB);
	}
	
	public void doCreateDeal (Player player, Player withPlayer) {
		try {
			Deal deal = createDeal(player, withPlayer);
			deal.setCreateFlagA(true);
			CREATE_DEAL_REQ notifyReq = new CREATE_DEAL_REQ(GameMessageFactory.nextSequnceNum(), player.getId());
			withPlayer.addMessageToRightBag(notifyReq);
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.交易申请, new String[][] { { Translate.STRING_1, withPlayer.getName() } }));
			player.addMessageToRightBag(hint);
			if (DealCenter.logger.isInfoEnabled() && deal != null) {
				if (DealCenter.logger.isInfoEnabled()) DealCenter.logger.info("[交易] [发起交易] [playerA:{}] [playerAid:{}] [playerAname:{}] [playerB:{}] [playerBid:{}] [playerBname:{}]", new Object[] { (deal.getPlayerA() != null ? deal.getPlayerA().getUsername() : ""), (deal.getPlayerA() != null ? deal.getPlayerA().getId() : ""), (deal.getPlayerA() != null ? deal.getPlayerA().getName() : ""), (deal.getPlayerB() != null ? deal.getPlayerB().getUsername() : ""), (deal.getPlayerB() != null ? deal.getPlayerB().getId() : ""), (deal.getPlayerB() != null ? deal.getPlayerB().getName() : "") });
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public Deal getDeal(String id) {
		return dataDealCenter.getDeal(id);
	}

	@Override
	public Deal getDeal(Player player) {
		return dataDealCenter.getDeal(player);
	}

	@Override
	public Deal getDeal(Player playerA, Player playerB) {
		return dataDealCenter.getDeal(playerA, playerB);
	}

	@Override
	public boolean addArticle(Player player, byte pakType, int index, long entityId, int count) throws Exception {
		try {
			boolean succ = dataDealCenter.addArticle(player, pakType, index, entityId, count);
			if (succ) {
				// 通知客户端交易栏改变
				Deal deal = getDeal(player);
				DEAL_UPDATE_REQ reqA = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.getPackageTypeA(), deal.getIndexesA(), deal.isAgreedA(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.isAgreedB());
				deal.getPlayerA().addMessageToRightBag(reqA);
				DEAL_UPDATE_REQ reqB = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.getPackageTypeB(), deal.getIndexesB(), deal.isAgreedB(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.isAgreedA());
				deal.getPlayerB().addMessageToRightBag(reqB);
				DEAL_LOCK_RES resA1 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerA().getId());
				deal.getPlayerA().addMessageToRightBag(resA1);
				DEAL_LOCK_RES resB1 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerA().getId());
				deal.getPlayerB().addMessageToRightBag(resB1);
				DEAL_LOCK_RES resA2 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerB().getId());
				deal.getPlayerA().addMessageToRightBag(resA2);
				DEAL_LOCK_RES resB2 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerB().getId());
				deal.getPlayerB().addMessageToRightBag(resB2);
			}else {
				Deal deal = getDeal(player);
				if (deal != null) {
					DEAL_UPDATE_REQ reqA = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.getPackageTypeA(), deal.getIndexesA(), deal.isAgreedA(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.isAgreedB());
					deal.getPlayerA().addMessageToRightBag(reqA);
					DEAL_UPDATE_REQ reqB = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.getPackageTypeB(), deal.getIndexesB(), deal.isAgreedB(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.isAgreedA());
					deal.getPlayerB().addMessageToRightBag(reqB);
				}
			}
			return succ;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public boolean deleteArticle(Player player, int index) {
		boolean succ = dataDealCenter.deleteArticle(player, index);
		if (succ) {
			// 通知客户端交易栏改变
			Deal deal = getDeal(player);
			DEAL_UPDATE_REQ reqA = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.getPackageTypeA(), deal.getIndexesA(), deal.isAgreedA(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.isAgreedB());
			deal.getPlayerA().addMessageToRightBag(reqA);
			DEAL_UPDATE_REQ reqB = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.getPackageTypeB(), deal.getIndexesB(), deal.isAgreedB(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.isAgreedA());
			deal.getPlayerB().addMessageToRightBag(reqB);
			DEAL_LOCK_RES resA1 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerA().getId());
			deal.getPlayerA().addMessageToRightBag(resA1);
			DEAL_LOCK_RES resB1 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerA().getId());
			deal.getPlayerB().addMessageToRightBag(resB1);
			DEAL_LOCK_RES resA2 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerB().getId());
			deal.getPlayerA().addMessageToRightBag(resA2);
			DEAL_LOCK_RES resB2 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerB().getId());
			deal.getPlayerB().addMessageToRightBag(resB2);
		}
		return succ;
	}

	@Override
	public boolean updateCoins(Player player, int coins) {
		boolean succ = dataDealCenter.updateCoins(player, coins);
		if (succ) {
			Deal deal = getDeal(player);
			DEAL_UPDATE_REQ reqA = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.getPackageTypeA(), deal.getIndexesA(), deal.isAgreedA(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.isAgreedB());
			deal.getPlayerA().addMessageToRightBag(reqA);
			DEAL_UPDATE_REQ reqB = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.getPackageTypeB(), deal.getIndexesB(), deal.isAgreedB(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.isAgreedA());
			deal.getPlayerB().addMessageToRightBag(reqB);
			DEAL_LOCK_RES resA1 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerA().getId());
			deal.getPlayerA().addMessageToRightBag(resA1);
			DEAL_LOCK_RES resB1 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerA().getId());
			deal.getPlayerB().addMessageToRightBag(resB1);
			DEAL_LOCK_RES resA2 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerB().getId());
			deal.getPlayerA().addMessageToRightBag(resA2);
			DEAL_LOCK_RES resB2 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerB().getId());
			deal.getPlayerB().addMessageToRightBag(resB2);
//			if (logger.isDebugEnabled()) logger.debug("[trace_setCoins] [update:" + coins + "][" + player.getUsername() + "][" + player.getId() + "] [" + player.getName() + "] [" + deal.getPlayerA().getName() + ":" + deal.getCoinsA() + "] [" + deal.getPlayerB().getName() + ":" + deal.getCoinsB() + "]");
			if (logger.isDebugEnabled()) logger.debug("[trace_setCoins] [update:{}][{}][{}] [{}] [{}:{}] [{}:{}]", new Object[]{coins,player.getUsername(),player.getId(),player.getName(),deal.getPlayerA().getName(),deal.getCoinsA(),deal.getPlayerB().getName(),deal.getCoinsB()});
		}
		return succ;
	}

	@Override
	public boolean updateConditions(Deal deal, Player player, int indexes[], long entityIds[], int counts[], int coins) throws Exception {
		try {
			boolean succ = dataDealCenter.updateConditions(deal, player, indexes, entityIds, counts, coins);
			if (succ) {
				// 通知玩家
				deal.setCoinsA(coins);
				DEAL_UPDATE_REQ reqA = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.getPackageTypeA(), deal.getIndexesA(), deal.isAgreedA(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.isAgreedB());
				deal.getPlayerA().addMessageToRightBag(reqA);
				DEAL_UPDATE_REQ reqB = new DEAL_UPDATE_REQ(GameMessageFactory.nextSequnceNum(), deal.getEntityIdsB(), deal.getCountsB(), deal.getCoinsB(), deal.getPackageTypeB(), deal.getIndexesB(), deal.isAgreedB(), deal.getEntityIdsA(), deal.getCountsA(), deal.getCoinsA(), deal.isAgreedA());
				deal.getPlayerB().addMessageToRightBag(reqB);
				DEAL_LOCK_RES resA1 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerA().getId());
				deal.getPlayerA().addMessageToRightBag(resA1);
				DEAL_LOCK_RES resB1 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerA().getId());
				deal.getPlayerB().addMessageToRightBag(resB1);
				DEAL_LOCK_RES resA2 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerB().getId());
				deal.getPlayerA().addMessageToRightBag(resA2);
				DEAL_LOCK_RES resB2 = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), false, deal.getPlayerB().getId());
				deal.getPlayerB().addMessageToRightBag(resB2);
			}
//			if (logger.isInfoEnabled()) logger.info("[updateConditions] [SUCC:" + succ + "] [" + deal.getId() + "] [" + player.getUsername() + "][" + player.getId() + "][" + player.getName() + "] [" + coins + "]");
			if (logger.isInfoEnabled()) logger.info("[updateConditions] [SUCC:{}] [{}] [{}][{}][{}] [{}]", new Object[]{succ,deal.getId(),player.getUsername(),player.getId(),player.getName(),coins});
			return succ;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void lockDeal(Deal deal, Player player, boolean isLock) {
		dataDealCenter.lockDeal(deal, player, isLock);
		DEAL_LOCK_RES resA = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), isLock, player.getId());
		deal.getPlayerA().addMessageToRightBag(resA);
		DEAL_LOCK_RES resB = new DEAL_LOCK_RES(GameMessageFactory.nextSequnceNum(), isLock, player.getId());
		deal.getPlayerB().addMessageToRightBag(resB);
	}

}
