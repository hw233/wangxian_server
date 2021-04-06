package com.fy.engineserver.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.Player_Process;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_TradeConfirm4ReleaseRB;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GET_PINGTAI_PARAM_REQ;
import com.fy.engineserver.message.GET_PINGTAI_PARAM_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.REQUESTBUY_ASK_KNAP_REMAI_RES;
import com.fy.engineserver.message.REQUESTBUY_CANCEL_SELF_REQ;
import com.fy.engineserver.message.REQUESTBUY_CONTIDION_TYPE_RES;
import com.fy.engineserver.message.REQUESTBUY_FASTBUY_RES;
import com.fy.engineserver.message.REQUESTBUY_GET_ENTITY_REQ;
import com.fy.engineserver.message.REQUESTBUY_GET_ENTITY_RES;
import com.fy.engineserver.message.REQUESTBUY_GET_PROPNAME_REQ;
import com.fy.engineserver.message.REQUESTBUY_GET_PROPNAME_RES;
import com.fy.engineserver.message.REQUESTBUY_QRY_BY_TERM_REQ;
import com.fy.engineserver.message.REQUESTBUY_QRY_BY_TERM_RES;
import com.fy.engineserver.message.REQUESTBUY_QRY_HIGH_PRICE_REQ;
import com.fy.engineserver.message.REQUESTBUY_QRY_HIGH_PRICE_RES;
import com.fy.engineserver.message.REQUESTBUY_QUERY_SELF_REQ;
import com.fy.engineserver.message.REQUESTBUY_QUERY_SELF_RES;
import com.fy.engineserver.message.REQUESTBUY_RELEASE_REQ;
import com.fy.engineserver.message.REQUESTBUY_SALE_REQ;
import com.fy.engineserver.message.REQUESTBUY_SUISHEN_RES;
import com.fy.engineserver.smith.RelationShipHelper;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.requestbuy.RequestBuy;
import com.fy.engineserver.trade.requestbuy.RequestBuyInfo4Client;
import com.fy.engineserver.trade.requestbuy.RequestBuyRule;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.vip.VipManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class RequestBuySubSystem   extends SubSystemAdapter {

//	public static Logger logger = Logger.getLogger(RequestBuySubSystem.class);
public	static Logger logger = LoggerFactory.getLogger(RequestBuySubSystem.class);
	public static RequestBuySubSystem instance;
	public void init() {
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}
	@Override
	public String getName() {
		return "RequestBuySubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[]{"REQUESTBUY_RELEASE_REQ", "REQUESTBUY_QUERY_SELF_REQ", "REQUESTBUY_CANCEL_SELF_REQ", 
				"REQUESTBUY_QRY_HIGH_PRICE_REQ", "REQUESTBUY_SALE_REQ", "REQUESTBUY_QRY_BY_TERM_REQ",
				"REQUESTBUY_ASK_KNAP_REMAI_REQ", "REQUESTBUY_CONTIDION_TYPE_REQ", "REQUESTBUY_GET_PROPNAME_REQ", 
				"REQUESTBUY_GET_ENTITY_REQ", "REQUESTBUY_FASTBUY_REQ", "REQUESTBUY_SUISHEN_REQ","GET_PINGTAI_PARAM_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,RequestMessage message) throws ConnectionException, Exception {
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
	public boolean handleResponseMessage(Connection conn,RequestMessage request, ResponseMessage response)throws ConnectionException, Exception {
		return false;
	}
	
	public ResponseMessage handle_REQUESTBUY_RELEASE_REQ(Connection conn, RequestMessage message, Player player){
		if (UnitServerFunctionManager.needCloseFunctuin(Function.求购)) {
			player.sendError(Translate.合服功能关闭提示);
			return null;
		}
		if (player.isAppStoreChannel() && player.getLevel() < 20) {
			player.sendError(Translate.级才能发布求购);
			return null;
		}
		try {
			if(RelationShipHelper.checkForbidAndSendMessage(player)) {
				if(RelationShipHelper.logger.isInfoEnabled()) {
					RelationShipHelper.logger.info("[玩家因为打金行为被限制求购] ["+player.getLogString()+"]");
				}
				return null;
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
		REQUESTBUY_RELEASE_REQ req = (REQUESTBUY_RELEASE_REQ)message;
		int num = req.getNum();
		long price = req.getPerPrice();
		RequestBuyRule requestBuy = RequestBuyManager.getInstance().getRequestBuyRule(req.getMainType(), req.getSubType(), req.getArticleName(), req.getColor());
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setTitle("");
		mw.setDescriptionInUUB(Translate.您确定发布此求购信息);
		Option_TradeConfirm4ReleaseRB rb = new Option_TradeConfirm4ReleaseRB(requestBuy, num, price);
		Option_Cancel cancel = new Option_Cancel();
		rb.setText(Translate.确定);
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { rb, cancel });
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(creq);
//		RequestBuyManager.getInstance().relReleaseRequestBuy(player, requestBuy, num, price);
		return null;
	}
	
	/**
	 * 查看自己的求购
	 */
	public ResponseMessage handle_REQUESTBUY_QUERY_SELF_REQ(Connection conn, RequestMessage message, Player player){
		REQUESTBUY_QUERY_SELF_REQ req = null;
		RequestBuyInfo4Client[] requestBuy = null;
		try {
			req = (REQUESTBUY_QUERY_SELF_REQ) message;
			int startNum = req.getStartNum();
			int length = req.getLenth();
			CompoundReturn compoundReturn = RequestBuyManager.getInstance().msg_lookUpSelfRequestBuys(player);
			List<RequestBuyInfo4Client> list = (List<RequestBuyInfo4Client>) compoundReturn.getObjValue();
			if(list.size()<=0){
				return new REQUESTBUY_QUERY_SELF_RES(message.getSequnceNum(), 0, 0, new RequestBuyInfo4Client[0]);
			}
			RequestBuyInfo4Client[] requestBuyInfo4Clients = new RequestBuyInfo4Client[0];
			requestBuyInfo4Clients = list.toArray(requestBuyInfo4Clients);
			if(requestBuyInfo4Clients.length<=startNum){
				if(requestBuyInfo4Clients.length<length){
					startNum = 0;
					length = requestBuyInfo4Clients.length;
				}else{
					startNum = requestBuyInfo4Clients.length-length;
					if(startNum<0){
						startNum = 0;
					}
				}
			}else{
				if(requestBuyInfo4Clients.length<startNum+length){
					length = requestBuyInfo4Clients.length-startNum;
				}
			}
			if(length>0){
				requestBuy = new RequestBuyInfo4Client[length];
				System.arraycopy(requestBuyInfo4Clients, startNum, requestBuy, 0,length);
				return new REQUESTBUY_QUERY_SELF_RES(message.getSequnceNum(), startNum, requestBuyInfo4Clients.length, requestBuy);
			}else{
				return new REQUESTBUY_QUERY_SELF_RES(message.getSequnceNum(), 0, 0, new RequestBuyInfo4Client[0]);
			}
		} catch (Exception e) {
			if(logger.isWarnEnabled())
				logger.warn("查看自己的求购:", e);
		}
		return null;
	}

	/**
	 * 取消自己的求购
	 */
	public ResponseMessage handle_REQUESTBUY_CANCEL_SELF_REQ(Connection conn, RequestMessage message, Player player){
		REQUESTBUY_CANCEL_SELF_REQ req = (REQUESTBUY_CANCEL_SELF_REQ)message;
		long id = req.getId();
		String failreason = null;
		byte result = 0;
		RequestBuyManager.getInstance().msg_cancel(player, id);
		return null;
	}

	/**
	 * 试图出售物品(查看最高价)
	 */
	public ResponseMessage handle_REQUESTBUY_QRY_HIGH_PRICE_REQ(Connection conn, RequestMessage message, Player player){
		REQUESTBUY_QRY_HIGH_PRICE_REQ req = (REQUESTBUY_QRY_HIGH_PRICE_REQ)message;
		long id = req.getArticleId();
		RequestBuy buy = RequestBuyManager.getInstance().getAccordRequestBuy(id);
		if(buy!=null){
//			logger.debug("最高价物品"+buy);
			RequestBuyInfo4Client buyInfo4Client = buy.getRequestBuyInfo4Client();
			return new REQUESTBUY_QRY_HIGH_PRICE_RES(message.getSequnceNum(), buyInfo4Client);
		}else{
			RequestBuyInfo4Client buyInfo4Client = new RequestBuyInfo4Client();
			return new REQUESTBUY_QRY_HIGH_PRICE_RES(message.getSequnceNum(), buyInfo4Client);
		}
	}

	/**
	 * 出售物品
	 */
	public ResponseMessage handle_REQUESTBUY_SALE_REQ(Connection conn, RequestMessage message, Player player){
		try {
			if(RelationShipHelper.checkForbidAndSendMessage(player)) {
				if(RelationShipHelper.logger.isInfoEnabled()) {
					RelationShipHelper.logger.info("[玩家因为打金行为被限制快售] ["+player.getLogString()+"]");
				}
				return null;
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
		REQUESTBUY_SALE_REQ req = (REQUESTBUY_SALE_REQ)message;
		long reqId = req.getId();
		long entityId = req.getEntityId();
		int knapIndex = req.getIndex();
		int num = req.getNum();
		RequestBuyManager.getInstance().msg_sale(player, entityId, knapIndex, num, reqId);
		return null;
	}

	/**
	 * 查询当前所有符合条件的求购
	 */
	public ResponseMessage handle_REQUESTBUY_QRY_BY_TERM_REQ(Connection conn, RequestMessage message, Player player){
		try{
		REQUESTBUY_QRY_BY_TERM_REQ req = (REQUESTBUY_QRY_BY_TERM_REQ)message;
		CompoundReturn compoundReturn = RequestBuyManager.getInstance().msg_lookOtherRequestBuys(player, req.getMainType(), req.getSubType(), req.getArticleName(), req.getColor(), req.getLevel());
		List<RequestBuyInfo4Client> buys = (List<RequestBuyInfo4Client>)compoundReturn.getObjValue();
		RequestBuyInfo4Client[] buyInfo4Clients = buys.toArray(new RequestBuyInfo4Client[0]);
		int startNum = req.getStartNum();
		int len = req.getLenth();
		if (len > 10 || len <= 0) {
			logger.error("[查询求购长度，外挂] [pla={}] [len={}]", new Object[]{player.getLogString(), len});
			return null;
		}
		if(buyInfo4Clients.length<=startNum){
			if(buyInfo4Clients.length<len){
				startNum = 0;
				len = buyInfo4Clients.length;
			}else{
				startNum = buyInfo4Clients.length-len;
				if(startNum<0){
					startNum = 0;
				}
			}
		}else{
			if(buyInfo4Clients.length<startNum+len){
				len = buyInfo4Clients.length-startNum;
			}
		}
		if(len>0){
			RequestBuyInfo4Client[] requestBuy = new RequestBuyInfo4Client[len];
			System.arraycopy(buyInfo4Clients, startNum, requestBuy, 0,len);
			return new REQUESTBUY_QRY_BY_TERM_RES(message.getSequnceNum(), startNum, buyInfo4Clients.length, requestBuy);
		}else{
			return new REQUESTBUY_QRY_BY_TERM_RES(message.getSequnceNum(), 0, 0, new RequestBuyInfo4Client[0]);
		}
		}catch(Exception e){
			if(logger.isWarnEnabled())
				logger.warn("handle_REQUESTBUY_QRY_BY_TERM_REQ", e);
		}
		return null;
	}

	/**
	 * 查询当前包裹符合热卖条件的求购
	 */
	public ResponseMessage handle_REQUESTBUY_ASK_KNAP_REMAI_REQ(Connection conn, RequestMessage message, Player player){
		CompoundReturn compoundReturn = RequestBuyManager.getInstance().msg_ask4FastSale(player);
		if(compoundReturn!=null){
			ArrayList<Long> list = (ArrayList<Long>)compoundReturn.getObjValue();
			long[] entityId = new long[list.size()];
			for(int i = 0; i< entityId.length; i++){
				entityId[i] = list.get(i);
			}
			return new REQUESTBUY_ASK_KNAP_REMAI_RES(message.getSequnceNum(), entityId);
		}
		return null;
	}
	
	public ResponseMessage handle_REQUESTBUY_CONTIDION_TYPE_REQ(Connection conn, RequestMessage message, Player player){
		return new REQUESTBUY_CONTIDION_TYPE_RES(message.getSequnceNum(), RequestBuyManager.option, RequestBuyManager.LEVEL_STRINGS);
	}
	
	public ResponseMessage handle_REQUESTBUY_GET_PROPNAME_REQ(Connection conn, RequestMessage message, Player player){
		REQUESTBUY_GET_PROPNAME_REQ req = (REQUESTBUY_GET_PROPNAME_REQ)message;
		String first = req.getFirst();
		String second = req.getSecond();
		CompoundReturn compoundReturn=null;
		compoundReturn = RequestBuyManager.getInstance().msg_getProp(first, second);
		return new REQUESTBUY_GET_PROPNAME_RES(message.getSequnceNum(), first, second, compoundReturn.getStringValues(), compoundReturn.getIntValues());
	}
	
	public ResponseMessage handle_REQUESTBUY_GET_ENTITY_REQ(Connection conn, RequestMessage message, Player player){
		REQUESTBUY_GET_ENTITY_REQ req = (REQUESTBUY_GET_ENTITY_REQ)message;
		String mainType = req.getMainType();
		String subType = req.getSubType();
		String articleName = req.getArticleName();
		int color = req.getColor();
		RequestBuy buy = RequestBuyManager.getInstance().queryMaxRequestBuy(mainType, subType, articleName, color);
		long perPrice = 0;
		if(buy!=null){
			perPrice = buy.getItem().getPerPrice();
		}
		if (req.getMainType().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类])){
			return new REQUESTBUY_GET_ENTITY_RES(message.getSequnceNum(), -1, RequestBuy.equ_icon, RequestBuyManager.getInstance().feeMoney, perPrice);
		}else if(req.getMainType().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类])){
			return new REQUESTBUY_GET_ENTITY_RES(message.getSequnceNum(), -1, RequestBuy.zuoqi_icon, RequestBuyManager.getInstance().feeMoney, perPrice);
		}else{
			Article a = ArticleManager.getInstance().getArticle(articleName);
			ArticleEntity articleEntity = RequestBuyManager.getInstance().getTempEntity(a, color);
			return new REQUESTBUY_GET_ENTITY_RES(message.getSequnceNum(), articleEntity.getId(), a.getIconId(), RequestBuyManager.getInstance().feeMoney, perPrice);
		}
	}
	
	public ResponseMessage handle_REQUESTBUY_FASTBUY_REQ(Connection conn, RequestMessage message, Player player){
		CompoundReturn compoundReturn = RequestBuyManager.getInstance().msg_getFastBuy(player);
		return new REQUESTBUY_FASTBUY_RES(message.getSequnceNum(), (RequestBuyInfo4Client[])compoundReturn.getObjVlues());
	}
	
	public ResponseMessage handle_REQUESTBUY_SUISHEN_REQ(Connection conn, RequestMessage message, Player player){
		boolean isOpen = VipManager.getInstance().vip是否开启随身求购(player);
		if (!isOpen){
			player.sendError(Translate.text_requestbuy_026);
		}
		REQUESTBUY_SUISHEN_RES res = new REQUESTBUY_SUISHEN_RES(GameMessageFactory.nextSequnceNum(), isOpen);
		return res;
	}
	
	public ResponseMessage handle_GET_PINGTAI_PARAM_REQ(Connection conn, RequestMessage message, Player player){
		long now = System.currentTimeMillis();
		GET_PINGTAI_PARAM_REQ req = (GET_PINGTAI_PARAM_REQ)message;
		String[] infos = req.getInfos();
		String op = infos[0];
		
		
		try
		{
			if("GET_MIGU_TOKEN".equals(op))
			{
				if(true)return null;
				if(infos.length < 3)
				{
					logger.warn("[平台获取参数] [失败] [passport对象为空] ["+op+"] ["+infos.length+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
					return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), new String[0]);
				}
				
				String mac = infos[1];
				String idfa = infos[2];
				String[] realInfos = new String[14];
				
				realInfos[0] = op;
				Passport passport = (Passport) conn.getAttachment();
				
				if(passport == null)
				{
					logger.warn("[平台获取参数] [失败] [passport对象为空] ["+op+"] ["+infos.length+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
					return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), new String[0]);
				}
				
				if(player == null)
				{
					logger.warn("[平台获取参数] [失败] [player对象为空] ["+op+"] ["+infos.length+"] ["+passport.getUserName()+"] ["+passport.getNickName()+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
					return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), new String[0]);
				}
				
				if(!player.isOnline())
				{
					logger.warn("[平台获取参数] [失败] [玩家不在线] ["+op+"] ["+infos.length+"] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
					return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), new String[0]);
				}
				/**
				 * 在实际应用当中我们发现了一种情况
				 * 对于百度sdk的用户游戏端存的有时候
				 * 是百度sdk用户对应的passport上的nickname
				 * 所以此时需要兼容这种情况，此时采用player身上的用户名
				 * 而不用passport上的用户名
				 */
				
				
				realInfos[1] = passport.getUserName().equals(player.getUsername()) ? passport.getUserName() : player.getUsername();
				realInfos[2] = GameConstants.getInstance().getServerName();
				realInfos[3] = player.getId()+"";
				realInfos[4] = player.getName();
				realInfos[5] = player.getLevel() + "";
				realInfos[6] = player.getSex()+"";
				realInfos[7] = player.getCareer()+"";
				realInfos[8] = GameConstants.getInstance().getPlatForm();
				realInfos[9] = passport.getLastLoginChannel();
				realInfos[10] = mac;
				realInfos[11] = idfa;
				realInfos[12] = player.getEnterServerTime()+"";
				String clientId = "";
				if (EnterLimitManager.player_process != null && EnterLimitManager.player_process.get(player.getId()) != null) {
					Player_Process pp = EnterLimitManager.player_process.get(player.getId());
					clientId = pp.getClientID();
				}
				realInfos[13] = clientId;
				
				
				
				//调用bossclientserver去申请token，拿到后直接返回给客户端
				String[] results = BossClientService.getInstance().miguCommunicate(realInfos);
				String[] realResults = new String[2];
				realResults[0] = op;
				realResults[1] = results[5];
//				System.arraycopy(results, 0, realResults, 1, results.length);
				if(logger.isInfoEnabled())
				{
					logger.info("[平台获取参数] [成功] [ok] ["+op+"] ["+infos.length+"] ["+realResults.length+"] ["+passport.getUserName()+"] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
				}
				
//				MiGuTradeServiceWorker.notifyBossPlayerEnter(player, MiGuTradeServiceWorker.OP_GET_MIGU_TOKEN_INFO);
				
				return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), realResults);
			}
			else if("SHOW_MIGU".equals(op))
			{
				String[] realResults = new String[2];
				realResults[0] = op;
				if(!MiGuTradeServiceWorker.platFormIsOpen())
				{
					realResults[1] = "false";
					logger.warn("[是否显示米谷按钮] [成功] [不显示] [不是应该开放的平台] ["+op+"] ["+infos.length+"]");
					return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), realResults);
				}
				
				
				Passport passport = (Passport) conn.getAttachment();
				if(passport == null)
				{
					realResults[1] = "false";
					logger.warn("[是否显示米谷按钮] [失败] [fail] [连接上无passport] ["+op+"] ["+infos.length+"]");
					return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), realResults);
				}
				
				realResults[1] = (MiGuTradeServiceWorker.isOpenMigu(passport.getLastLoginChannel(), player)&& player.getVipLevel() >= 4)+"";
				
				return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), realResults);
			} else if ("APPSTORE_GET_MIGU_TOKEN".equals(op)) {
				if(true)return null;
				if(infos.length < 3)
				{
					logger.warn("[苹果服平台获取参数] [失败] [passport对象为空] ["+op+"] ["+infos.length+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
					return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), new String[0]);
				}
				
				String mac = infos[1];
				String idfa = infos[2];
				String[] realInfos = new String[14];
				
				realInfos[0] = op;
				Passport passport = (Passport) conn.getAttachment();
				
				if(passport == null)
				{
					logger.warn("[苹果服平台获取参数] [失败] [passport对象为空] ["+op+"] ["+infos.length+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
					return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), new String[0]);
				}
				
				if(player == null)
				{
					logger.warn("[苹果服平台获取参数] [失败] [player对象为空] ["+op+"] ["+infos.length+"] ["+passport.getUserName()+"] ["+passport.getNickName()+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
					return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), new String[0]);
				}
				
				if(!player.isOnline())
				{
					logger.warn("[苹果服平台获取参数] [失败] [玩家不在线] ["+op+"] ["+infos.length+"] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
					return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), new String[0]);
				}
				/**
				 * 在实际应用当中我们发现了一种情况
				 * 对于百度sdk的用户游戏端存的有时候
				 * 是百度sdk用户对应的passport上的nickname
				 * 所以此时需要兼容这种情况，此时采用player身上的用户名
				 * 而不用passport上的用户名
				 */
				
				realInfos[1] = passport.getUserName().equals(player.getUsername()) ? passport.getUserName() : player.getUsername();
				realInfos[2] = GameConstants.getInstance().getServerName();
				realInfos[3] = player.getId()+"";
				realInfos[4] = player.getName();
				realInfos[5] = player.getLevel() + "";
				realInfos[6] = player.getSex()+"";
				realInfos[7] = player.getCareer()+"";
				realInfos[8] = GameConstants.getInstance().getPlatForm();
				realInfos[9] = passport.getLastLoginChannel();
				realInfos[10] = mac;
				realInfos[11] = idfa;
				realInfos[12] = player.getEnterServerTime()+"";
				String clientId = "";
				if (EnterLimitManager.player_process != null && EnterLimitManager.player_process.get(player.getId()) != null) {
					Player_Process pp = EnterLimitManager.player_process.get(player.getId());
					clientId = pp.getClientID();
				}
				realInfos[13] = clientId;
				
				
				
				//调用bossclientserver去申请token，拿到后直接返回给客户端
				String[] results = BossClientService.getInstance().miguCommunicate(realInfos);
				String[] realResults = new String[2];
				realResults[0] = op;
				realResults[1] = results[5];
//				System.arraycopy(results, 0, realResults, 1, results.length);
				if(logger.isInfoEnabled())
				{
					logger.info("[苹果服平台获取参数] [成功] [ok] ["+op+"] [" + player.getLogString() + "] [" + Arrays.toString(infos) + "] [" + Arrays.toString(results) + "]");
				}
				return null;
			}
			else
			{	
				String[] realResults = new String[1];
				realResults[0] = op;
				logger.warn("[是否显示米谷按钮] [失败] [fail] [未知操作] ["+op+"] ["+infos.length+"]");
				return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), realResults);
			}
		}
		catch(Exception e)
		{
			String[] realResults = new String[1];
			realResults[0] = op;
			if(player != null)
				logger.error("[平台获取参数] [失败] [fail] [未知操作] ["+op+"] ["+infos.length+"] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			else
				logger.error("[平台获取参数] [失败] [fail] [未知操作并且player对象为null] ["+op+"] ["+infos.length+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			return new GET_PINGTAI_PARAM_RES(req.getSequnceNum(), realResults);
		}
		

	}
}
