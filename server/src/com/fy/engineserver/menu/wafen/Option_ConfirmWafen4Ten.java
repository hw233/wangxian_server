package com.fy.engineserver.menu.wafen;

import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Private;
import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel;
import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel4Client;
import com.fy.engineserver.activity.wafen.manager.WaFenManager;
import com.fy.engineserver.activity.wafen.model.FenMuModel;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.DIG_FENMU_TEN_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_ConfirmWafen4Ten extends Option {
	
	private String fenmuName;
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		int[] result = WaFenManager.instance.wafen4Ten(player, fenmuName, true);
		if (result == null) {
			return;
		}
		Arrays.sort(result);
		WaFenInstance4Private wfip = WaFenManager.instance.privateMaps.get(player.getId());
		int leftYinChanzi = 0;
		int leftJinChanzi = 0;
		int leftLiuliChanzi = 0;
		FenDiModel4Client[] fendiModel = null;
		if (wfip != null) {
			List<FenDiModel> fList = wfip.getOpenFendiMaps().get(fenmuName);
			FenMuModel fmm = WaFenManager.instance.fenmuMap.get(fenmuName);
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
		DIG_FENMU_TEN_RES resp = new DIG_FENMU_TEN_RES(GameMessageFactory.nextSequnceNum(), fenmuName, leftYinChanzi, 
			leftJinChanzi, leftLiuliChanzi,result, fendiModel);
		player.addMessageToRightBag(resp);
	}

	public String getFenmuName() {
		return fenmuName;
	}

	public void setFenmuName(String fenmuName) {
		this.fenmuName = fenmuName;
	}
}
