package com.fy.engineserver.activity.wafen.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Common;
import com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Private;
import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel;
import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel4Client;
import com.fy.engineserver.activity.wafen.model.FenMuModel;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.BICHAN_ARTICLE_REQ;
import com.fy.engineserver.message.BICHAN_ARTICLE_RES;
import com.fy.engineserver.message.DIG_FENMU_REQ;
import com.fy.engineserver.message.DIG_FENMU_TEN_REQ;
import com.fy.engineserver.message.DIG_FENMU_TEN_RES;
import com.fy.engineserver.message.EXCHANGE_CHANZI_REQ;
import com.fy.engineserver.message.GET_ONE_FENMU_INFO_REQ;
import com.fy.engineserver.message.GET_ONE_FENMU_INFO_RES;
import com.fy.engineserver.message.OPEN_WAFEN_ACTIVITY_WINDOW_REQ;
import com.fy.engineserver.message.OPEN_WAFEN_ACTIVITY_WINDOW_RES;
import com.fy.engineserver.message.RECEIVE_FENMU_REWARD_REQ;
import com.fy.engineserver.message.RECEIVE_FENMU_REWARD_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.util.CompoundReturn;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class WaFenSystem extends SubSystemAdapter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "WaFenSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[]{"OPEN_WAFEN_ACTIVITY_WINDOW_REQ", "GET_ONE_FENMU_INFO_REQ", "DIG_FENMU_REQ", "RECEIVE_FENMU_REWARD_REQ"
				, "EXCHANGE_CHANZI_REQ", "DIG_FENMU_TEN_REQ", "BICHAN_ARTICLE_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, Horse2Manager.logger);
		if(Horse2Manager.logger.isDebugEnabled()) {
			Horse2Manager.logger.debug("[Horse2System] [收到玩家请求] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]");
		}
		try {
			if (message instanceof OPEN_WAFEN_ACTIVITY_WINDOW_REQ) {
				return handle_OPEN_WAFEN_ACTIVITY_WINDOW_REQ(conn, message, player);
			} else if (message instanceof GET_ONE_FENMU_INFO_REQ) {
				return handle_GET_ONE_FENMU_INFO_REQ(conn, message, player);
			} else if (message instanceof DIG_FENMU_REQ) {
				return handle_DIG_FENMU_REQ(conn, message, player);
			} else if (message instanceof RECEIVE_FENMU_REWARD_REQ) {
				return handle_RECEIVE_FENMU_REWARD_REQ(conn, message, player);
			} else if (message instanceof EXCHANGE_CHANZI_REQ) {
				return handle_EXCHANGE_CHANZI_REQ(conn, message, player);
			} else if (message instanceof DIG_FENMU_TEN_REQ) {
				return handle_DIG_FENMU_TEN_REQ(conn, message, player);
			} else if (message instanceof BICHAN_ARTICLE_REQ) {
				return handle_BICHAN_ARTICLE_REQ(conn, message, player);
			}
		} catch (Exception e) {
			WaFenManager.logger.error("[挖坟活动] [处理协议] [" + message.getTypeDescription() + "] [异常] [" + player.getLogString() + "]", e);
		}
		return null;
	}
	/**
	 * 客户端请求打开坟墓界面
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_OPEN_WAFEN_ACTIVITY_WINDOW_REQ(Connection conn, RequestMessage message, Player player) {
		OPEN_WAFEN_ACTIVITY_WINDOW_REQ req = (OPEN_WAFEN_ACTIVITY_WINDOW_REQ) message;
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.wafenActivity);
		boolean flag = false;
		if (cr != null) {
			if (req.getFenmuType() == 0) {
				flag  = cr.getBooleanValue();
			} else {
				WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
				if (wp != null) {
					flag = cr.getBooleanValue() && wp.isAllOpened();
					if (!flag) {
						player.sendError("您尚未完成前三页，无法开启琉璃幻境");
						return null;
					}
				}else{
					player.sendError("您尚未完成前三页，无法开启琉璃幻境");
					return null;
				}
			}
		}
		if (!flag) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		List<String> list = new ArrayList<String>();
		String currentFenMu = "";
		int fenMuIndex = 0;
		WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
		for (FenMuModel fm : WaFenManager.instance.fenmuMap.values()) {
			if (fm.getShareType() == req.getFenmuType()) {
				list.add(fm.getName());
				if (fm.getId() == 1) {
					currentFenMu = fm.getName();
				} else if (wp != null && wp.canDig(fm.getName())) {
					currentFenMu = fm.getName();
					fenMuIndex = fm.getId() - 1;
				}
			}
		}
		String[] fenMuNames = list.toArray(new String[0]);
		String description = Translate.挖坟帮助描述;
		int leftYinChanzi = 0;
		int leftJinChanzi = 0;
		int leftLiuliChanzi = 0;
		int weigth = 0;
		int heigth = 0;
		int[] useChanziType = null;
		FenDiModel4Client[] fendiModel = null;
		if (wp != null) {
			List<FenDiModel> fList = null;
			if (req.getFenmuType() == 0) {
				fList = wp.getOpenFendiMaps().get(currentFenMu);
			} else {
				WaFenInstance4Common wc = WaFenManager.instance.commonMaps.get(currentFenMu);
				if (wc != null) {
					fList = wc.getFendiList();
				}
			}
			FenMuModel fmm = WaFenManager.instance.fenmuMap.get(currentFenMu);
			weigth = fmm.getWidth();
			heigth = fmm.getHeight();
			leftYinChanzi = wp.getLeftYinChanzi();
			leftJinChanzi = wp.getLeftJinChanzi();
			leftLiuliChanzi = wp.getLeftLiuLiChanzi();
			description = fmm.getHelpInfo();
			useChanziType = fmm.getCostChanziType();
			if (fList != null && fList.size() > 0) {
				fendiModel = new FenDiModel4Client[fList.size()];
				for (int i=0; i<fList.size(); i++) {
					long aId = fmm.getArticleMap().get(fList.get(i).getArticleId()).getTempArticleEntityId();
					FenDiModel4Client fc = new FenDiModel4Client(fList.get(i).getIndex(), aId, fList.get(i).getReciveType());
					fendiModel[i] = fc;
				}
			} else {
				fendiModel = new FenDiModel4Client[0];
			}
		} else {
			fendiModel = new FenDiModel4Client[0];
			FenMuModel fmm = WaFenManager.instance.fenmuMap.get(currentFenMu);
			weigth = fmm.getWidth();
			heigth = fmm.getHeight();
			useChanziType = fmm.getCostChanziType();
			description = fmm.getHelpInfo();
		}
		if (req.getFenmuType() != 0) {
			WaFenInstance4Common wc = WaFenManager.instance.commonMaps.get(currentFenMu);
			FenMuModel fmm = WaFenManager.instance.fenmuMap.get(currentFenMu);
			if (wc != null && wc.getFendiList() != null) {
				fendiModel = new FenDiModel4Client[wc.getFendiList().size()];
				for (int i=0; i<wc.getFendiList().size(); i++) {
					long aId = fmm.getArticleMap().get(wc.getFendiList().get(i).getArticleId()).getTempArticleEntityId();
					FenDiModel4Client fc = new FenDiModel4Client(wc.getFendiList().get(i).getIndex(), aId, wc.getFendiList().get(i).getReciveType());
					fendiModel[i] = fc;
				}
			} 
		}
//		if (WaFenManager.挖坟客户端容错开关 && fenMuNames.length > 1) {
//			fenMuNames = Arrays.copyOf(fenMuNames, fenMuNames.length + 1);
//			fenMuNames[fenMuNames.length - 1] = "";
//		}
		OPEN_WAFEN_ACTIVITY_WINDOW_RES resp = new OPEN_WAFEN_ACTIVITY_WINDOW_RES(message.getSequnceNum(), req.getFenmuType(), fenMuNames, fenMuIndex, currentFenMu,
				weigth, heigth, description, leftYinChanzi, leftJinChanzi, leftLiuliChanzi, useChanziType, fendiModel);
		if (WaFenManager.logger.isDebugEnabled()) {
			WaFenManager.logger.debug("[挖坟活动] [handle_OPEN_WAFEN_ACTIVITY_WINDOW_REQ] [type:" + resp.getFenmuType() + "] [fenMuNames:" + Arrays.toString(resp.getFenMuNames()) + "] [current:" + resp.getCurrentFenMu() + "] [" + player.getLogString() + "]");
		}
		return resp;
	}
	/**
	 * 请求单个坟墓信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_ONE_FENMU_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		GET_ONE_FENMU_INFO_REQ req = (GET_ONE_FENMU_INFO_REQ) message;
		FenDiModel4Client[] fendiModel = null;
		WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
		int weigth = 0;
		int heigth = 0;
		int fenmuId = 0;
		int[] useChanziType = new int[0];
		String description = "";
		if (wp != null) {
			List<FenDiModel> fList = wp.getOpenFendiMaps().get(req.getFenmuName());
			FenMuModel fmm = WaFenManager.instance.fenmuMap.get(req.getFenmuName());
			weigth = fmm.getWidth();
			useChanziType = fmm.getCostChanziType();
			heigth = fmm.getHeight();
			description = fmm.getHelpInfo();
			fenmuId = fmm.getId()-1;
			if (fList != null && fList.size() > 0) {
				fendiModel = new FenDiModel4Client[fList.size()];
				for (int i=0; i<fList.size(); i++) {
					long aId = fmm.getArticleMap().get(fList.get(i).getArticleId()).getTempArticleEntityId();
					FenDiModel4Client fc = new FenDiModel4Client(fList.get(i).getIndex(), aId, fList.get(i).getReciveType());
					fendiModel[i] = fc;
				}
			} else {
				fendiModel = new FenDiModel4Client[0];
			}
		} else {
			fendiModel = new FenDiModel4Client[0];
			FenMuModel fmm = WaFenManager.instance.fenmuMap.get(req.getFenmuName());
			weigth = fmm.getWidth();
			heigth = fmm.getHeight();
			useChanziType = fmm.getCostChanziType();
			fenmuId = fmm.getId() - 1;
			description = fmm.getHelpInfo();
		}
		GET_ONE_FENMU_INFO_RES resp = new GET_ONE_FENMU_INFO_RES(message.getSequnceNum(), req.getFenmuName(), fenmuId, weigth, heigth, useChanziType, description, fendiModel);
		if (WaFenManager.logger.isDebugEnabled()) {
			WaFenManager.logger.debug("[挖坟活动] [handle_GET_ONE_FENMU_INFO_REQ] [type:" + resp.getFenmuName() + "] [" + player.getLogString() + "]");
		}
		return resp;
	}
	/**
	 * 挖坟
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_DIG_FENMU_REQ(Connection conn, RequestMessage message, Player player) {
		DIG_FENMU_REQ req = (DIG_FENMU_REQ) message;
		WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
		if (wp == null) {
			player.sendError(Translate.请先兑换铲子);
			return null;
		}
		WaFenManager.instance.waFen(player, req.getFenmuName(), req.getFendiIndex(), (byte)-1);
		if (WaFenManager.logger.isDebugEnabled()) {
			WaFenManager.logger.debug("[挖坟活动] [handle_DIG_FENMU_REQ] [name:" + req.getFenmuName() + "] [req.getFendiIndex():" + req.getFendiIndex() + "] [" + player.getLogString() + "]");
		}
		return null;
	}
	/**
	 * 一键领取奖励
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_RECEIVE_FENMU_REWARD_REQ(Connection conn, RequestMessage message, Player player) {
		RECEIVE_FENMU_REWARD_REQ req = (RECEIVE_FENMU_REWARD_REQ) message;
		boolean result = WaFenManager.instance.receiveRewards(player, req.getFenmuName());
		if (!result) {
			return null;
		}
		int fenmuIndex = 0;
		WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
		FenDiModel4Client[] fendiModel = null;
		if (wp != null) {
			List<FenDiModel> fList = wp.getOpenFendiMaps().get(req.getFenmuName());
			FenMuModel fmm = WaFenManager.instance.fenmuMap.get(req.getFenmuName());
			fenmuIndex = fmm.getId() - 1;
			if (fList != null && fList.size() > 0) {
				fendiModel = new FenDiModel4Client[fList.size()];
				for (int i=0; i<fList.size(); i++) {
					long aId = fmm.getArticleMap().get(fList.get(i).getArticleId()).getTempArticleEntityId();
					FenDiModel4Client fc = new FenDiModel4Client(fList.get(i).getIndex(), aId, fList.get(i).getReciveType());
					fendiModel[i] = fc;
				}
			} else {
				fendiModel = new FenDiModel4Client[0];
			}
		}
		RECEIVE_FENMU_REWARD_RES resp = new RECEIVE_FENMU_REWARD_RES(message.getSequnceNum(), req.getFenmuName(), fenmuIndex, fendiModel);
		if (WaFenManager.logger.isDebugEnabled()) {
			WaFenManager.logger.debug("[挖坟活动] [handle_RECEIVE_FENMU_REWARD_REQ] [name:" + req.getFenmuName() + "] [领取结果:" + result + "] [" + player.getLogString() + "]");
		}
		return resp;
	}
	/**
	 * 兑换铲子
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_EXCHANGE_CHANZI_REQ(Connection conn, RequestMessage message, Player player) {
		EXCHANGE_CHANZI_REQ req = (EXCHANGE_CHANZI_REQ) message;
		WaFenManager.instance.exchangeChanZi(player, req.getExchangeType(), false);
		if (WaFenManager.logger.isDebugEnabled()) {
			WaFenManager.logger.debug("[挖坟活动] [handle_EXCHANGE_CHANZI_REQ] [name:" + req.getExchangeType() + "] [" + player.getLogString() + "]");
		}
		return null;
	}
	/**
	 * 请求坟墓必产物品
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BICHAN_ARTICLE_REQ(Connection conn, RequestMessage message, Player player) {
		BICHAN_ARTICLE_REQ req = (BICHAN_ARTICLE_REQ) message;
		FenMuModel fmm = WaFenManager.instance.fenmuMap.get(req.getFenmuName());
		if (fmm == null) {
			WaFenManager.logger.error("[挖坟活动] [客户端发过来坟墓名没有对应配置] [fenmuName:" + req.getFenmuName() + "] [" + player.getLogString() + "]");
			return null;
		}
		long[] articleIds = null;
		String description = String.format(Translate.挖坟必产物品标题, fmm.getName());
		String description2 = Translate.挖坟必产物品描述;
		if (fmm.getWaFenArticleId() != null) {
			articleIds = new long[fmm.getWaFenArticleId().size()];
			for (int i=0; i<articleIds.length; i++) {
				articleIds[i] = fmm.getArticleMap().get(fmm.getWaFenArticleId().get(i)).getTempArticleEntityId();
			}
		} else {
			articleIds = new long[0];
		}
		if (articleIds.length <= 0) {
			description2 = "";
		}
		BICHAN_ARTICLE_RES resp = new BICHAN_ARTICLE_RES(message.getSequnceNum(), req.getFenmuName(), articleIds, description, description2);
		return resp;
	}
	/**
	 * 十连挖
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	
	public ResponseMessage handle_DIG_FENMU_TEN_REQ(Connection conn, RequestMessage message, Player player) {
		DIG_FENMU_TEN_REQ req = (DIG_FENMU_TEN_REQ) message;
		int[] result = WaFenManager.instance.wafen4Ten(player, req.getFenmuName(), false);
		if (result == null) {
			return null;
		}
		Arrays.sort(result);
		WaFenInstance4Private wfip = WaFenManager.instance.privateMaps.get(player.getId());
		int leftYinChanzi = 0;
		int leftJinChanzi = 0;
		int leftLiuliChanzi = 0;
		FenDiModel4Client[] fendiModel = null;
		if (wfip != null) {
			List<FenDiModel> fList = wfip.getOpenFendiMaps().get(req.getFenmuName());
			FenMuModel fmm = WaFenManager.instance.fenmuMap.get(req.getFenmuName());
			leftYinChanzi = wfip.getLeftYinChanzi();
			leftJinChanzi = wfip.getLeftJinChanzi();
			leftLiuliChanzi = wfip.getLeftLiuLiChanzi();
			if (fList != null && fList.size() > 0) {
				fendiModel = new FenDiModel4Client[fList.size()];
				for (int i=0; i<fList.size(); i++) {
					long aId = fmm.getArticleMap().get(fList.get(i).getArticleId()).getTempArticleEntityId();
					FenDiModel4Client fc = new FenDiModel4Client(fList.get(i).getIndex(), aId, fList.get(i).getReciveType());
					fendiModel[i] = fc;
				}
			} else {
				fendiModel = new FenDiModel4Client[0];
			}
		}
		if (fendiModel == null) {
			fendiModel = new FenDiModel4Client[0];
		}
		DIG_FENMU_TEN_RES resp = new DIG_FENMU_TEN_RES(message.getSequnceNum(), req.getFenmuName(), leftYinChanzi, 
			leftJinChanzi, leftLiuliChanzi,result, fendiModel);
		return resp;
	}
	

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
