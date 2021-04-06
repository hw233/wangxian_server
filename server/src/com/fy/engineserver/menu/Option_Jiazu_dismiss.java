package com.fy.engineserver.menu;

import java.util.List;
import java.util.Set;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.petHouse.HouseData;
import com.fy.engineserver.jiazu.petHouse.PetHouseManager;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.JIAZU_DISMISS_RES;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

public class Option_Jiazu_dismiss extends Option {
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		// oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_CONFIRM_APPLY_MASTER;
	}

	@Override
	public void doSelect(Game game, Player player) {

		JiazuManager jiazuManager = JiazuManager.getInstance();
		if (player.getJiazuId() <= 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6199);
			player.addMessageToRightBag(nreq);

			return;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		if (jiazu == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6199);
			player.addMessageToRightBag(nreq);
			return;
		}

		List<HouseData> jiazuDatas = PetHouseManager.getInstance().getJiazuDatas(jiazu.getJiazuID());
		if(jiazuDatas != null && jiazuDatas.size() > 0){
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.家族中有宠物正在挂机不能解散);
			player.addMessageToRightBag(nreq);
			JiazuManager.logger.warn("[维护] [家族中有宠物正在挂机不能解散] [解散家族] [" + jiazu.getLogString() + "] [jiazuMoney:" + jiazu.getJiazuMoney() + "] [level:" + jiazu.getLevel() + "] [" + player.getLogString() + "]");
			return;
		}
		
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			JIAZU_DISMISS_RES nreq = new JIAZU_DISMISS_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6129);
			player.addMessageToRightBag(nreq);
		} else if (member.getTitle() != JiazuTitle.master) {
			JIAZU_DISMISS_RES res = new JIAZU_DISMISS_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6158);
			player.addMessageToRightBag(res);
		} else {
			if(player.getCountryPosition() == CountryManager.国王){
				player.send_HINT_REQ(Translate.国王不能解散家族);
				return;
			}
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn(player.getLogString() + "[解散家族] [{}]", new Object[] { jiazu.getName() });
			}
			try {
				JiazuManager.jiazuEm.remove(jiazu);
			} catch (Exception e1) {
				TaskSubSystem.logger.info(player.getLogString() + "[解散家族] [存库异常] [" + jiazu.getName() + "]", e1);
			}
			Set<JiazuMember> set = jiazuManager.getJiazuMember(jiazu.getJiazuID());
			ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(jiazu.getZongPaiId());
			if (zp != null) {
				try {
					zp.getJiazuIds().remove(jiazu.getJiazuID());
					ZongPaiManager.em.notifyFieldChange(zp, "jiazuIds");
				} catch (Exception e) {
					JiazuSubSystem.logger.error("[解散家族] [宗派获取异常] [异常]", e);
				}
			}
			PlayerManager playerManager = PlayerManager.getInstance();
			for (JiazuMember mem : set) {
				Player mplayer = null;
				try {
					mplayer = playerManager.getPlayer(mem.getPlayerID());
					if (mplayer != null) {
						mplayer.setJiazuName(null);
						mplayer.setJiazuId(0);
						mplayer.setLeaveJiazuTime(System.currentTimeMillis());
						mplayer.initJiazuTitleAndIcon();
						mplayer.setZongPaiName("");
						mplayer.initZongPaiName();
						if (PlayerManager.getInstance().isOnline(mplayer.getId())) {
							SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
							mplayer.sendError(Translate.text_jiazu_017);
							if (septStation != null && septStation.getGame().contains(mplayer)) {// 在家族驻地内
								septStation.setUsed(false);
								septStation.getGame().transferGame(mplayer, new TransportData(0, 0, 0, 0, mplayer.getHomeMapName(), mplayer.getHomeX(), mplayer.getHomeY()));
							} 
						} else {// 不在线的发邮件
							MailManager.getInstance().sendMail(mplayer.getId(), null, Translate.text_6380, Translate.text_6379, 0, 0, 0, "");
							
							mplayer.setX(mplayer.getResurrectionX());
							mplayer.setY(mplayer.getResurrectionY());
							mplayer.setGame(mplayer.getResurrectionMapName());
						}
					}
					JiazuManager.memberEm.remove(mem);
				} catch (Exception e) {
					JiazuSubSystem.logger.error("[解散家族] [异常]", e);
				}
			}
			JIAZU_DISMISS_RES res = new JIAZU_DISMISS_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.家族解散成功);
			player.addMessageToRightBag(res);
		}
	}

}
