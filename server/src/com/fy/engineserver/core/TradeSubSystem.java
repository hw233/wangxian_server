package com.fy.engineserver.core;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.charge.CardFunction;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.BOOTHSALE_ADVERTISING_REQ;
import com.fy.engineserver.message.BOOTHSALE_ADVERTISING_RES;
import com.fy.engineserver.message.BOOTHSALE_BOOTHSALE_REQUEST_REQ;
import com.fy.engineserver.message.BOOTHSALE_BOOTHSALE_REQUEST_RES;
import com.fy.engineserver.message.BOOTHSALE_BUY_REQ;
import com.fy.engineserver.message.BOOTHSALE_BUY_RES;
import com.fy.engineserver.message.BOOTHSALE_CANCEL_BOOTHSALE_REQ;
import com.fy.engineserver.message.BOOTHSALE_CANCEL_BOOTHSALE_RES;
import com.fy.engineserver.message.BOOTHSALE_CANCEL_FOR_SALE_REQ;
import com.fy.engineserver.message.BOOTHSALE_CANCEL_FOR_SALE_RES;
import com.fy.engineserver.message.BOOTHSALE_CHANAGE_ADVERTISING_REQ;
import com.fy.engineserver.message.BOOTHSALE_CHANAGE_ADVERTISING_RES;
import com.fy.engineserver.message.BOOTHSALE_LEAVE_BOOTH_REQ;
import com.fy.engineserver.message.BOOTHSALE_LEAVE_WORD_REQ;
import com.fy.engineserver.message.BOOTHSALE_LEAVE_WORD_RES;
import com.fy.engineserver.message.BOOTHSALE_LOOK_OVER_REQ;
import com.fy.engineserver.message.BOOTHSALE_LOOK_OVER_RES;
import com.fy.engineserver.message.BOOTHSALE_NEW_BUY_REQ;
import com.fy.engineserver.message.BOOTHSALE_SELECT_FOR_SALE_REQ;
import com.fy.engineserver.message.BOOTHSALE_SELECT_FOR_SALE_RES;
import com.fy.engineserver.message.BOOTH_OFFLINE_REQ;
import com.fy.engineserver.message.BOOTH_OFFLINE_RES;
import com.fy.engineserver.message.BOOTH_OFFLINE_SURE_REQ;
import com.fy.engineserver.message.BOOTH_OFFLINE_SURE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.smith.RelationShipHelper;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.trade.boothsale.BoothInfo4Client;
import com.fy.engineserver.trade.boothsale.BoothSale;
import com.fy.engineserver.trade.boothsale.SoftLink4Package;
import com.fy.engineserver.trade.boothsale.service.BoothsaleManager;
import com.fy.engineserver.trade.exceptions.ArticleHasBindedException;
import com.fy.engineserver.trade.exceptions.CellEmptyException;
import com.fy.engineserver.trade.exceptions.EntityNotFoundException;
import com.fy.engineserver.trade.exceptions.NoEnoughNumberException;
import com.fy.engineserver.trade.exceptions.NoEnoughPositionException;
import com.fy.engineserver.trade.exceptions.OutOfIndexException;
import com.fy.engineserver.trade.exceptions.WrongNumberException;
import com.fy.engineserver.trade.exceptions.WrongPriceException;
import com.fy.engineserver.trade.exceptions.WrongStateException;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class TradeSubSystem extends SubSystemAdapter  {

//	public static Logger logger = Logger.getLogger(TradeSubSystem.class);
public	static Logger logger = LoggerFactory.getLogger(TradeSubSystem.class);

	@Override
	public String getName() {
		return "TradeSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[] { "BOOTHSALE_BOOTHSALE_REQUEST_REQ", "BOOTHSALE_NEW_BUY_REQ", "BOOTHSALE_LOOK_OVER_REQ", "BOOTHSALE_SELECT_FOR_SALE_REQ",
				"BOOTHSALE_CANCEL_FOR_SALE_REQ", "BOOTHSALE_ADVERTISING_REQ", "BOOTHSALE_BUY_REQ", "BOOTHSALE_LEAVE_WORD_REQ", "BOOTHSALE_CANCEL_BOOTHSALE_REQ", 
				"BOOTHSALE_RESALE_REQUEST_REQ", "BOOTHSALE_LEAVE_BOOTH_REQ" ,"BOOTHSALE_RESALE_OVER_REQ", "BOOTHSALE_CHANAGE_ADVERTISING_REQ",
				"BOOTH_OFFLINE_REQ","BOOTH_OFFLINE_SURE_REQ"};
	}

	private static TradeSubSystem instance;

	public void init() {
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}

		Class clazz = this.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		m1.setAccessible(true);
		return (ResponseMessage) m1.invoke(this, conn, message, player);
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		return false;
	}

	/***
	 * 选择物品出售
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BOOTHSALE_SELECT_FOR_SALE_REQ(Connection conn, RequestMessage message, Player player) {
		BOOTHSALE_SELECT_FOR_SALE_REQ req = (BOOTHSALE_SELECT_FOR_SALE_REQ) message;
		int index = req.getIndex();
		long perPrice = req.getPerPrice();
		int saleNum = req.getSaleNum();

		int result = 1;
		String failreason;
		CompoundReturn returns = null;
		try {
			returns = BoothsaleManager.getInstance().msg_selectSale(player, index, saleNum, perPrice);
			if (returns == null) {
				result = 1;
				return null;
			}else {
				result = returns.getIntValue();
			}
			failreason = "";
		} catch (CellEmptyException e) {
			failreason = Translate.text_trade_004;
		} catch (OutOfIndexException e) {
			failreason = Translate.text_trade_004;
		} catch (ArticleHasBindedException e) {
			failreason = Translate.text_trade_005;
		} catch (NoEnoughNumberException e) {
			failreason = Translate.text_trade_006;
		} catch (NoEnoughPositionException e) {
			failreason = Translate.text_trade_008;
		} catch (WrongPriceException e) {
			failreason = Translate.text_trade_010;
		} catch (WrongNumberException e) {
			failreason = Translate.text_trade_009;
		}
		return returns == null ? new BOOTHSALE_SELECT_FOR_SALE_RES(req.getSequnceNum(), result, failreason,(byte)-1,-1, -1, -1 ,-1) : new BOOTHSALE_SELECT_FOR_SALE_RES(req.getSequnceNum(), result, failreason, index, returns.getIntValues()[0], returns.getLongValues()[0], returns.getIntValues()[1], returns.getLongValues()[1]);
	}

	/**
	 * 取消出售
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BOOTHSALE_CANCEL_FOR_SALE_REQ(Connection conn, RequestMessage message, Player player) {
		BOOTHSALE_CANCEL_FOR_SALE_REQ req = (BOOTHSALE_CANCEL_FOR_SALE_REQ) message;
		String failreason = "";
		byte result = -1;
		int index = req.getIndex();
		int pagType = 0;
		int pagIndex = 0;
		try {
			CompoundReturn returns = BoothsaleManager.getInstance().msg_cancel(player, index);
			if (returns != null) {
				result = returns.getByeValue();
				failreason = returns.getBooleanValue() ? "" : Translate.text_trade_015;
				index = returns.getIntValues()[0];
				pagType = returns.getIntValues()[1];
				pagIndex = returns.getIntValues()[2];
			}
		} catch (EntityNotFoundException e) {
			failreason = Translate.text_trade_011;
		} catch (OutOfIndexException e) {
			failreason = Translate.text_trade_004;
		}
		return new BOOTHSALE_CANCEL_FOR_SALE_RES(req.getSequnceNum(), result, failreason, index, pagType, pagIndex);
	}

	public ResponseMessage handle_BOOTHSALE_NEW_BUY_REQ(Connection conn, RequestMessage message, Player player) {
		try {
			try {
				if(RelationShipHelper.checkForbidAndSendMessage(player)) {
					if(RelationShipHelper.logger.isInfoEnabled()) {
						RelationShipHelper.logger.info("[玩家因为打金行为被限制摆摊购买] ["+player.getLogString()+"]");
					}
					return null;
				}
			} catch(Throwable e) {
				e.printStackTrace();
			}
			
			//被限制交易了
			if (ChatMessageService.getInstance().isSilence(player.getId()) >= 2) {
				EnterLimitManager.logger.warn("[用户被限制摆摊] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+player.getLevel()+"] 银子:["+player.getSilver()+"]");
				return null;
			}
			
			BOOTHSALE_NEW_BUY_REQ req = (BOOTHSALE_NEW_BUY_REQ) message;
			Player saler = null;
			try {
				saler = GamePlayerManager.getInstance().getPlayer(req.getSaler());
			} catch (Exception e1) {
				logger.error("[交易系统][查看摆摊人][不存在]ID = " + req.getSaler(), e1);
			}
			if (saler == null) {
				HINT_REQ rq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, Translate.text_trade_002);
				player.addMessageToRightBag(rq);
//				logger.info("[购买摆摊物品][摊主没找到][ID=" + req.getSaler() + "]");
				logger.info("[购买摆摊物品][摊主没找到][ID={}]", new Object[]{req.getSaler()});
				return null;
			}
			int index = req.getIndex();
			int num = req.getSaleNum();
			long entityID = req.getEntityID();
			String failreason = "";
			byte result = 1;
			try {
				CompoundReturn re = BoothsaleManager.getInstance().msg_trade(player, saler, index, num, entityID);
				if (re == null){
					return null;
				}else{
					result = 0;
				}
			} catch (OutOfIndexException e) {
				failreason = Translate.text_trade_004;
			} catch (EntityNotFoundException e) {
				failreason = Translate.text_trade_011;
			} catch (NoEnoughNumberException e) {
				failreason = Translate.text_trade_006;
			} catch (com.fy.boss.authorize.exception.NoEnoughMoneyException e) {
				failreason = Translate.text_trade_007;
			} catch (NoEnoughPositionException e) {
				failreason = Translate.text_trade_008;
			} catch (WrongStateException e) {
				failreason = Translate.text_trade_003;
			}
			return new BOOTHSALE_BUY_RES(message.getSequnceNum(), result, failreason);
		} catch (Exception e) {
			logger.error("handle_BOOTHSALE_NEW_BUY_REQ", e);
		}
		return null;
	}
	
	public ResponseMessage handle_BOOTH_OFFLINE_REQ(Connection conn, RequestMessage message, Player player) {
		BOOTH_OFFLINE_REQ req = (BOOTH_OFFLINE_REQ)message;
		//月卡 TODO
		Map<Integer, Long> boothC = BoothsaleManager.getInstance().getBoothConfig();
		if(boothC.isEmpty()){
			player.sendError(Translate.服务器配置错误);
			return null;
		}
		int buyDays [] = new int[boothC.size()];
		long costMoney [] = new long[boothC.size()];
		
		Iterator<Entry<Integer, Long>> it = boothC.entrySet().iterator();
		int index = 0;
		while(it.hasNext()){
			Entry<Integer, Long> entry = it.next();
			buyDays[index] = entry.getKey();
			costMoney[index] = entry.getValue();
			index++;
		}
		return new BOOTH_OFFLINE_RES(req.getSequnceNum(), player.getId(), buyDays, costMoney);
	}
	
	public ResponseMessage handle_BOOTH_OFFLINE_SURE_REQ(Connection conn, RequestMessage message, Player player) {
		BOOTH_OFFLINE_SURE_REQ req = (BOOTH_OFFLINE_SURE_REQ)message;
		int hour = req.getBuyDays();
		long money = req.getCostMoney();
		try{
			if(!player.ownMonthCard(CardFunction.开放离线摆摊功能)){
				player.sendError(Translate.没开放离线摆摊功能);
				return new BOOTH_OFFLINE_SURE_RES(req.getSequnceNum(), false); 
			}
			Map<Integer, Long> boothC = BoothsaleManager.getInstance().getBoothConfig();
			if(boothC.isEmpty()){
				player.sendError(Translate.服务器配置错误);
				return new BOOTH_OFFLINE_SURE_RES(req.getSequnceNum(), false);
			}
			if(boothC.get(hour) == null || boothC.get(hour).longValue() != money){
				player.sendError(Translate.服务器配置错误+"!");
				return new BOOTH_OFFLINE_SURE_RES(req.getSequnceNum(), false);
			}
			BoothsaleManager.getInstance().beginBoothSaleOffline(player, hour, money);
			return new BOOTH_OFFLINE_SURE_RES(req.getSequnceNum(), true);
		}catch (Exception e) {
			e.printStackTrace();
			player.sendError(Translate.离线摆摊出错);
			return new BOOTH_OFFLINE_SURE_RES(req.getSequnceNum(), false);
		}
	}
	
	/**
	 * 购买
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BOOTHSALE_BUY_REQ(Connection conn, RequestMessage message, Player player) {
		try {
			if(RelationShipHelper.checkForbidAndSendMessage(player)) {
				if(RelationShipHelper.logger.isInfoEnabled()) {
					RelationShipHelper.logger.info("[玩家因为打金行为被限制摆摊购买] ["+player.getLogString()+"]");
				}
				return null;
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
		
		//被限制交易了
		if (ChatMessageService.getInstance().isSilence(player.getId()) >= 2) {
			EnterLimitManager.logger.warn("[用户被限制摆摊] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+player.getLevel()+"] 银子:["+player.getSilver()+"]");
			return null;
		}
		
		BOOTHSALE_BUY_REQ req = (BOOTHSALE_BUY_REQ) message;
		Player saler = null;
		try {
			saler = GamePlayerManager.getInstance().getPlayer(req.getSaler());
		} catch (Exception e1) {
			logger.error("[交易系统][查看摆摊人][不存在]ID = " + req.getSaler(), e1);
		}
		if (saler == null) {
			HINT_REQ rq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, Translate.text_trade_002);
			player.addMessageToRightBag(rq);
//			logger.info("[购买摆摊物品][摊主没找到][ID=" + req.getSaler() + "]");
			logger.info("[购买摆摊物品][摊主没找到][ID={}]", new Object[]{req.getSaler()});
			return null;
		}
		int index = req.getIndex();
		int num = req.getSaleNum();
		String failreason = "";
		byte result = 1;
		try {
			CompoundReturn re = BoothsaleManager.getInstance().msg_trade(player, saler, index, num, -1000);
			if (re == null){
				return null;
			}else{
				result = 0;
			}
		} catch (OutOfIndexException e) {
			failreason = Translate.text_trade_004;
		} catch (EntityNotFoundException e) {
			failreason = Translate.text_trade_011;
		} catch (NoEnoughNumberException e) {
			failreason = Translate.text_trade_006;
		} catch (com.fy.boss.authorize.exception.NoEnoughMoneyException e) {
			failreason = Translate.text_trade_007;
		} catch (NoEnoughPositionException e) {
			failreason = Translate.text_trade_008;
		} catch (WrongStateException e) {
			failreason = Translate.text_trade_003;
		}
		return new BOOTHSALE_BUY_RES(message.getSequnceNum(), result, failreason);
	}

	/**
	 * 玩家申请摆摊
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BOOTHSALE_BOOTHSALE_REQUEST_REQ(Connection conn, RequestMessage message, Player player) {
		try{
			try {
				if(RelationShipHelper.checkForbidAndSendMessage(player)) {
					if(RelationShipHelper.logger.isInfoEnabled()) {
						RelationShipHelper.logger.info("[玩家因为打金行为被限制摆摊] ["+player.getLogString()+"]");
					}
					return null;
				}
			} catch(Throwable e) {
				e.printStackTrace();
			}
			
			BOOTHSALE_BOOTHSALE_REQUEST_REQ req = (BOOTHSALE_BOOTHSALE_REQUEST_REQ) message;
			CompoundReturn compoundReturn = BoothsaleManager.getInstance().msg_canBoothSale(player);
			byte result = compoundReturn.getByeValue();
			String failreason = compoundReturn.getStringValue();
			
			BoothInfo4Client boothInfo4Client = new BoothInfo4Client();
			if (compoundReturn.getBooleanValue()){
				if(player.getBoothSale() != null){
					BoothSale boothSale = player.getBoothSale();
					boothSale.clearMessage();
					for(int i = 0; i<boothSale.getLink().length; i++){
						if(boothSale.getItem(i).getPackageType() < 0 || boothSale.getItem(i).getIndexOfPackage() < 0){
							continue;
						}
						if(player.getKnapsack_common().getCell(boothSale.getItem(i).getIndexOfPackage()).getEntityId() != boothSale.getItem(i).getEntityId()){
							boothSale.getLink()[i] = new SoftLink4Package(-1, -1, -1);
						}
					}
				}else{
					BoothSale boothSale = BoothsaleManager.createDefalutBoothSale(player);
					player.setBoothSale(boothSale);
				}
				player.setSeeBoothSale(0);
				boothInfo4Client = player.getBoothSale().getBoothInfo4Client();
				player.setState(Player.STATE_DAZUO);
			}
			if (BoothsaleManager.logger.isWarnEnabled()) BoothsaleManager.logger.warn("[收到协议:申请摆摊]result = {},failreason = {} boothSale={}", new Object[]{result, failreason, player.getBoothSale()});
			return new BOOTHSALE_BOOTHSALE_REQUEST_RES(req.getSequnceNum(), result, failreason, boothInfo4Client);
		}catch(Exception e){
			logger.warn("申请摆摊出错:", e);
		}
		return null;
	}

	/**
	 * 收摊
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BOOTHSALE_CANCEL_BOOTHSALE_REQ(Connection conn, RequestMessage message, Player player) {
		BOOTHSALE_CANCEL_BOOTHSALE_REQ req = (BOOTHSALE_CANCEL_BOOTHSALE_REQ) message;
		CompoundReturn compoundReturn = BoothsaleManager.getInstance().msg_cancelBoothSale(player);
		player.setState(Player.STATE_STAND);
		return new BOOTHSALE_CANCEL_BOOTHSALE_RES(req.getSequnceNum(), compoundReturn.getByeValue(), compoundReturn.getStringValue());
	}

	/**
	 * 设置广告�
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BOOTHSALE_ADVERTISING_REQ(Connection conn, RequestMessage message, Player player) {
		BOOTHSALE_ADVERTISING_REQ req = (BOOTHSALE_ADVERTISING_REQ) message;
		if (!BoothsaleManager.getInstance().currPlaceCanBooth(player)) {
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.text_trade_000);
			player.addMessageToRightBag(hint);
			return null;
		}
		String info = req.getSalername();
		String advertising = req.getAdvertising();
		CompoundReturn compoundReturn = BoothsaleManager.getInstance().msg_setNameAdvertising(player, info, advertising);
		if (compoundReturn == null) {
			if (player.getBoothSale() != null) {
				player.getBoothSale().clearMessage();
				for(int i = 0; i<player.getBoothSale().getLink().length; i++){
					player.getBoothSale().getLink()[i] = new SoftLink4Package(-1, -1, -1);
				}
			}
			return null;
		}
		if (compoundReturn.getBooleanValue()) {
			advertising = compoundReturn.getStringValue();
		} else {
			player.sendError(Translate.text_trade_012);
			return null;
		}
		if (player.isIsUpOrDown()) {
			player.downFromHorse(true);
		}
		EnterLimitManager.setValues(player, PlayerRecordType.摆摊次数);
		try {
			EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.摆摊次数, 1L});
			EventRouter.getInst().addEvent(evt);
		} catch (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [统计玩家摆摊次数异常] [" + player.getLogString() + "]", e);
		}
		return new BOOTHSALE_ADVERTISING_RES(req.getSequnceNum(), compoundReturn.getByeValue(), info, advertising, BoothsaleManager.cycle);
	}

	/**
	 * 查看某人出售物品
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BOOTHSALE_LOOK_OVER_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isAppStoreChannel() && player.getLevel() < 20) {
			player.sendError(Translate.text_trade_018);
			return null;
		}
		try{
			BOOTHSALE_LOOK_OVER_REQ req = (BOOTHSALE_LOOK_OVER_REQ) message;
			long salerId = req.getSalerId();
			Player saler = null;
			try {
				saler = GamePlayerManager.getInstance().getPlayer(salerId);
			} catch (Exception e) {
				HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, Translate.text_trade_002);
				player.addMessageToRightBag(req2);
				logger.info("[交易系统][查看摆摊人][不存在]ID = " + salerId, e);
				return null;
			}
			// 是否在摆摊状�
			BoothSale boothSale = saler.getBoothSale();
			if (!saler.isInBoothSale() || boothSale == null) {
				HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.text_trade_003);
				player.addMessageToRightBag(req2);
				return null;
			}
			
			//被限制交易了
			if (player.getId() != saler.getId() && ChatMessageService.getInstance().isSilence(saler.getId()) >= 2) {
				EnterLimitManager.logger.warn("[用户被限制摆摊] ["+saler.getUsername()+"] ["+saler.getName()+"] ["+saler.getId()+"] ["+saler.getLevel()+"] 银子:["+saler.getSilver()+"]");
				return null;
			}
			
			boolean isInBoothMapArea = false;
			String quyuName = "";
			for (int i = 0;i < saler.getCurrentMapAreaNames().length; i++) {
				quyuName = quyuName + saler.getCurrentMapAreaNames()[i] + ",";
				if (saler.getCurrentMapAreaNames()[i].indexOf(Translate.摆摊) >= 0) {
					isInBoothMapArea = true;
					break;
				}
			}
			if (!isInBoothMapArea) {
				BoothsaleManager.getInstance().msg_cancelBoothSale(saler);
				player.send_HINT_REQ(Translate.text_trade_017);
				if (logger.isWarnEnabled()) {
					logger.warn("[关闭不在摆摊区域摆摊的摊位] [{}] [{}] [{}]", new Object[]{saler.getLogString(), quyuName, saler.getX() + "~" + saler.getY()});
				}
				return null;
			}
			
			if (logger.isInfoEnabled()) logger.info("[handle_BOOTHSALE_LOOK_OVER_REQ] [开始] [pid={}] [pName{}] [sid={}] [{}]", new Object[]{player.getId(), player.getName(), saler.getId(), saler.getBoothSale().getLogString()});
			if (player.getId()!=salerId) {
				for (int i = 0; i < boothSale.getLink().length; i++) {
					SoftLink4Package link = boothSale.getLink()[i];
					if (link != null ) {
						if (link.getPackageType() == 0 && link.getIndexOfPackage() >= 0) {
							Cell cell = saler.getKnapsack_common().getCell(link.getIndexOfPackage());
							if (cell != null){
								ArticleEntity en = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
								if (en != null && en.isBinded()) {
									player.sendError(Translate.text_trade_019);
									return null;
								}
							}
							for (int j = 0; j < boothSale.getLink().length; j++) {
								SoftLink4Package linkJ = boothSale.getLink()[j];
								if (linkJ != null) {
									if (linkJ.getIndexOfPackage() == link.getIndexOfPackage() && i != j) {
										player.sendError(Translate.text_trade_020);
										return null;
									}
								}
							}
						}
					}
				}
			}
			if(player.getId()!=salerId&&!boothSale.getObserver().contains(player.getId())){
				//如果是卖家就不加
				player.setSeeBoothSale(boothSale.getOwner());
				boothSale.getObserver().add(player.getId());
			}
			return new BOOTHSALE_LOOK_OVER_RES(req.getSequnceNum(), salerId, boothSale.getSalername(), boothSale.getAdvertising(), boothSale.getBoothInfo4Client(), boothSale.getBoard().getInfo(), BoothsaleManager.BUY_TAX);
		}catch(Exception e){
			logger.warn("查看别人摆摊出错:", e);
		}
		return null;
	}

	/**
	 * 给摊主留言
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BOOTHSALE_LEAVE_WORD_REQ(Connection conn, RequestMessage message, Player player) {
		BOOTHSALE_LEAVE_WORD_REQ req = (BOOTHSALE_LEAVE_WORD_REQ) message;
		long salerId = req.getSaler();
		String msg = req.getMessage();

		Player saler = null;

		try {
			saler = GamePlayerManager.getInstance().getPlayer(salerId);
		} catch (Exception e) {
			HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, Translate.text_trade_002);
			player.addMessageToRightBag(req2);
			logger.info("[交易系统][查看摆摊人][不存在]ID = " + salerId, e);
			return null;
		}
		
		if(ChatMessageService.getInstance().isSilence(salerId) >= 2) {
			EnterLimitManager.logger.warn("[用户被限制摆摊] ["+saler.getUsername()+"] ["+saler.getName()+"] ["+saler.getId()+"] ["+saler.getLevel()+"] 银子:["+saler.getSilver()+"]");
			return null;
		}
		
		if(ChatMessageService.getInstance().isSilence(player.getId()) >= 2) {
			EnterLimitManager.logger.warn("[用户被限制摆摊] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+player.getLevel()+"] 银子:["+player.getSilver()+"]");
			return null;
		}

		String info = "";
		byte result = 0;
		try {
			BoothsaleManager.getInstance().msg_leaveWord(player, saler, msg);
			result = 1;
		} catch (WrongStateException e) {
			info = Translate.text_trade_003;
		}
		return new BOOTHSALE_LEAVE_WORD_RES(req.getSequnceNum(), result, info);
	}


	/**
	 * 离开摊位
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BOOTHSALE_LEAVE_BOOTH_REQ(Connection conn, RequestMessage message, Player player) {
		BOOTHSALE_LEAVE_BOOTH_REQ req = (BOOTHSALE_LEAVE_BOOTH_REQ) message;
		long id = req.getId();
		try {
			Player player2 = PlayerManager.getInstance().getPlayer(id);
			player.setSeeBoothSale(0);
			player2.getBoothSale().getObserver().remove(player.getId());
		} catch (Exception e) {
//			logger.error("[离开摊位][摊主没拿到，ID=" + id + "]");
			logger.error("[离开摊位][摊主没拿到，ID={}]", new Object[]{id});
		}
		return null;
	}
	
	/**
	 * 修改广告语
	 * **/
	public ResponseMessage handle_BOOTHSALE_CHANAGE_ADVERTISING_REQ(Connection conn, RequestMessage message, Player player){
		BOOTHSALE_CHANAGE_ADVERTISING_REQ req = (BOOTHSALE_CHANAGE_ADVERTISING_REQ) message;
		String advertising = req.getAdvertising();
		CompoundReturn compoundReturn = BoothsaleManager.getInstance().mes_changeAdvertising(player, advertising);
		if (compoundReturn.getBooleanValue()) {
		}else{
			player.sendError(Translate.text_trade_012);
			return null;
//			advertising = Translate.text_trade_012;
		}
		return new BOOTHSALE_CHANAGE_ADVERTISING_RES(req.getSequnceNum(), compoundReturn.getByeValue(), compoundReturn.getStringValue(), BoothsaleManager.cycle);
	}
}
