package com.fy.engineserver.homestead.cave;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 栅栏
 * 
 * 
 */

@SimpleEmbeddable
public class CaveFence extends CaveBuilding implements FaeryConfig {

	private int openStatus;

	public CaveFence() {
		setType(CAVE_BUILDING_TYPE_FENCE);
	}

	public boolean isOpen() {
		return getOpenStatus() == FENCE_STATUS_OPEN;
	}

	/**
	 * 得到进门花费
	 * @return
	 */
	public int getEnterCost() {
		return FaeryManager.getInstance().getFenceCfg()[getGrade() - 1].getEnterCost();
	}

	public int getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(int openStatus) {
		this.openStatus = openStatus;
	}

	@Override
	public void modifyName() {
		String ownerName = getCave().getOwnerName();
		StringBuffer sbf = new StringBuffer();
		try {
			// Player player = PlayerManager.getInstance().getPlayer(getCave().getOwnerId());
			ownerName = getCave().getOwnerName();// player.getName();
		} catch (Exception e) {
			FaeryManager.logger.error("[加载仙府,主人不存在]", e);
		}
		sbf.append(Translate.translateString(Translate.xxx的仙府, new String[][]{{Translate.STRING_1,ownerName}}));
		getNpc().setName(sbf.toString());
	}
}
