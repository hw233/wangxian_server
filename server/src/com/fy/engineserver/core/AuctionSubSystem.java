package com.fy.engineserver.core;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.auction.AuctionInfo4Client;
import com.fy.engineserver.auction.service.AuctionManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.AUCTION_BUY_REQ;
import com.fy.engineserver.message.AUCTION_CANCEL_REQ;
import com.fy.engineserver.message.AUCTION_CREATE_REQ;
import com.fy.engineserver.message.AUCTION_LIST_REQ;
import com.fy.engineserver.message.AUCTION_LIST_RES;
import com.fy.engineserver.message.AUCTION_TYPE_GET_RES;
import com.fy.engineserver.smith.RelationShipHelper;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.requestbuy.RequestOption;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class AuctionSubSystem extends SubSystemAdapter {

//	public static Logger logger = Logger.getLogger(AuctionSubSystem.class);
public	static Logger logger = LoggerFactory.getLogger(AuctionSubSystem.class);
	public static AuctionSubSystem instance;
	
	public void init() {
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}
	@Override
	public String getName() {
		return "AuctionSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[]{"AUCTION_CREATE_REQ", "AUCTION_LIST_REQ", "AUCTION_TYPE_GET_REQ"
				,"AUCTION_BUY_REQ", "AUCTION_CANCEL_REQ"};
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
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response)throws ConnectionException, Exception {
		return false;
	}
	
	public ResponseMessage handle_AUCTION_CREATE_REQ(Connection conn, RequestMessage message, Player player){
		if(logger.isDebugEnabled()){
			logger.debug("创建一个求购");
		}
		try {
			if(RelationShipHelper.checkForbidAndSendMessage(player)) {
				if(RelationShipHelper.logger.isInfoEnabled()) {
					RelationShipHelper.logger.info("[玩家因为打金行为被限制拍卖] ["+player.getLogString()+"]");
				}
				return null;
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
		AUCTION_CREATE_REQ req = (AUCTION_CREATE_REQ)message;
		int knapIndex = req.getKnapCellIndex();
		long entityId = req.getEntityId();
		int count = req.getCount();
		long startPrice = req.getStartPrice();
		long buyPrice = req.getBuyPrice();
		synchronized (player.tradeKey) {
			AuctionManager.getInstance().msg_creatAuction(player, knapIndex, entityId, count, startPrice, buyPrice);
		}
		return null;
	}
	
	public ResponseMessage handle_AUCTION_LIST_REQ(Connection conn, RequestMessage message, Player player){
		if(logger.isDebugEnabled()){
			logger.debug("查询求购列表");
		}
		AUCTION_LIST_REQ req = (AUCTION_LIST_REQ)message;
		byte reqType = req.getReqType();
		int startNum = req.getStartNum();
		int reqLen = req.getReqLen();
		if (reqLen > 10 || reqLen <= 0) {
			logger.error("[查询求购] [外挂] [reqLen={}]", new Object[]{reqLen});
			return null;
		}
		CompoundReturn compoundReturn = null;
		if(reqType==0){
			//查询所有拍卖纪录
			String mainType = req.getMainType();
			String subType = req.getSubType();
			int level = req.getLevel();
			int maxlevel = req.getMaxlevel();
			int color = req.getColor();
			String name = req.getName();
			if (Translate.翅膀类.equals(mainType) || (Translate.宝石类.equals(mainType) && Translate.光效宝石.equals(subType))) {		//2014年10月22日   翅膀和法宝不管颜色
				color = -1;
			}
			compoundReturn = AuctionManager.getInstance().msg_requestAuctionList(mainType, subType, level, maxlevel, color, name, player);
		}else if(reqType==1){
			//查询自己的拍卖
			compoundReturn = AuctionManager.getInstance().msg_getSelfAuctionList(player);
		}else if(reqType==2){
			compoundReturn = AuctionManager.getInstance().msg_getSelfBuyList(player);
		}
		try{
			ArrayList<AuctionInfo4Client> arrayList = (ArrayList<AuctionInfo4Client>)compoundReturn.getObjValue();
			if(arrayList.size()<=0){
				return new AUCTION_LIST_RES(message.getSequnceNum(), 0, 0, new AuctionInfo4Client[0], AuctionManager.getInstance().BUY1_TAX);
			}
			AuctionInfo4Client[] auctionInfo4Clients = arrayList.toArray(new AuctionInfo4Client[0]);
			if(auctionInfo4Clients.length<=startNum){
				if(auctionInfo4Clients.length<reqLen){
					startNum = 0;
					reqLen = auctionInfo4Clients.length;
				}else{
					startNum = auctionInfo4Clients.length-reqLen;
					if(startNum<0){
						startNum = 0;
					}
				}
			}else{
				if(auctionInfo4Clients.length<startNum+reqLen){
					reqLen = auctionInfo4Clients.length-startNum;
				}
			}
			if(reqLen>0){
				AuctionInfo4Client[] returnInfo = new AuctionInfo4Client[reqLen];
				System.arraycopy(auctionInfo4Clients, startNum, returnInfo, 0,reqLen);
				return new AUCTION_LIST_RES(message.getSequnceNum(), startNum, auctionInfo4Clients.length, returnInfo, AuctionManager.getInstance().BUY1_TAX);
			}else{
				return new AUCTION_LIST_RES(message.getSequnceNum(), 0, 0, new AuctionInfo4Client[0], AuctionManager.getInstance().BUY1_TAX);
			}
		}catch(Exception e){
			logger.error("查询拍卖纪录出错", e);
		}
		return null;
	}
	
	public ResponseMessage handle_AUCTION_TYPE_GET_REQ(Connection conn, RequestMessage message, Player player){
		if(logger.isDebugEnabled()){
			logger.debug("查询拍卖的一级二级分类");
		}
		CompoundReturn compoundReturn = AuctionManager.getInstance().msg_getType();
		return new AUCTION_TYPE_GET_RES(message.getSequnceNum(), ((RequestOption[])compoundReturn.getObjVlues()), AuctionManager.getInstance().SELL_TAX);
	}
	
	public ResponseMessage handle_AUCTION_BUY_REQ(Connection conn, RequestMessage message, Player player){
		if(logger.isDebugEnabled()){
			logger.debug("竞拍");
		}
		try {
			if(RelationShipHelper.checkForbidAndSendMessage(player)) {
				if(RelationShipHelper.logger.isInfoEnabled()) {
					RelationShipHelper.logger.info("[玩家因为打金行为被限制竞拍] ["+player.getLogString()+"]");
				}
				return null;
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
		AUCTION_BUY_REQ req = (AUCTION_BUY_REQ)message;
		AuctionManager.getInstance().msg_buy(player, req.getId(), req.getPrice());
		return null;
	}

	public ResponseMessage handle_AUCTION_CANCEL_REQ(Connection conn, RequestMessage message, Player player){
		if(logger.isDebugEnabled()){
			logger.debug("取消拍卖");
		}
		AUCTION_CANCEL_REQ req = (AUCTION_CANCEL_REQ)message;
		AuctionManager.getInstance().msg_cancelAuction(player, req.getId());
		return null;
	}
}
