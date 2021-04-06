package com.fy.engineserver.menu.wafen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Common;
import com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Private;
import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel;
import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel4Client;
import com.fy.engineserver.activity.wafen.manager.WaFenManager;
import com.fy.engineserver.activity.wafen.model.FenMuModel;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_WAFEN_ACTIVITY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class Option_OpenFenMu extends Option implements NeedCheckPurview{
	
	private byte fenmuType;			//0私有   1全服共有
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.wafenActivity);
		if (cr != null && !cr.getBooleanValue()) {
			player.sendError(Translate.活动尚未开启);
			return;
		}
		try {
			List<String> list = new ArrayList<String>();
			String currentFenMu = "";
			int fenMuIndex = 0;
			WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
			for (FenMuModel fm : WaFenManager.instance.fenmuMap.values()) {
				if (fm.getShareType() == fenmuType) {
					list.add(fm.getName());
					if (fm.getId() == 1) {
						currentFenMu = fm.getName();
					} else if (wp != null && wp.canDig(fm.getName())) {
						currentFenMu = fm.getName();
//						fenMuIndex = list.size() - 1;
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
				if (fenmuType == 0) {
					fList = wp.getOpenFendiMaps().get(currentFenMu);
				} else {
					WaFenInstance4Common wc = WaFenManager.instance.commonMaps.get(currentFenMu);
					if (wc != null) {
						fList = wc.getFendiList();
					}
				}
				FenMuModel fmm = WaFenManager.instance.fenmuMap.get(currentFenMu);
				weigth = fmm.getWidth();
				leftYinChanzi = wp.getLeftYinChanzi();
				leftJinChanzi = wp.getLeftJinChanzi();
				leftLiuliChanzi = wp.getLeftLiuLiChanzi();
				useChanziType = fmm.getCostChanziType();
				description = fmm.getHelpInfo();
				heigth = fmm.getHeight();
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
				useChanziType = fmm.getCostChanziType();
				heigth = fmm.getHeight();
				description = fmm.getHelpInfo();
			}
			if (fenmuType != 0) {
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
			if (WaFenManager.挖坟客户端容错开关 && fenMuNames.length > 1) {
				fenMuNames = Arrays.copyOf(fenMuNames, fenMuNames.length + 1);
				fenMuNames[fenMuNames.length - 1] = "";
			}
			OPEN_WAFEN_ACTIVITY_WINDOW_RES resp = new OPEN_WAFEN_ACTIVITY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), fenmuType, fenMuNames, fenMuIndex, currentFenMu,
					weigth, heigth, description, leftYinChanzi, leftJinChanzi, leftLiuliChanzi, useChanziType, fendiModel);
			player.addMessageToRightBag(resp);
		} catch (Exception e) {
			WaFenManager.logger.error("[挖坟活动] [打开挖坟界面] [异常] [" + player.getLogString() + "] [fenmuType:" + fenmuType + "]", e);
		} 
	}

	public byte getFenmuType() {
		return fenmuType;
	}

	public void setFenmuType(byte fenmuType) {
		this.fenmuType = fenmuType;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.wafenActivity);
		if (cr != null) {
			if (fenmuType == 0) {
				return cr.getBooleanValue();
			} else {
				WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
				if (wp != null) {
					return cr.getBooleanValue() && wp.isAllOpened();
				}
			}
		}
		return false;
	}
	
	
}
