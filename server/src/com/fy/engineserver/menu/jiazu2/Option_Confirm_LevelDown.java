package com.fy.engineserver.menu.jiazu2;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SEPTBUILDING_LVDOWN_RES;
import com.fy.engineserver.sept.exception.ActionIsCDException;
import com.fy.engineserver.sept.exception.BuildingNotFoundException;
import com.fy.engineserver.sept.exception.WrongLvOnLvDownException;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.sprite.Player;

public class Option_Confirm_LevelDown extends Option {
	private SeptStation station;
	
	private long NPCId;
	
	private SeptBuildingTemplet template;
	
	@Override
	public void doSelect(Game game, Player player) {
		SEPTBUILDING_LVDOWN_RES res = null;
		try {
			getTemplate().levelDown(station, NPCId, true, player);
			if (JiazuManager2.logger.isWarnEnabled()) {
				JiazuManager2.logger.warn(player.getJiazuLogString() + "[降级驻地建筑] [确认] [成功] [{}] [当前等级:{}]", new Object[] { getTemplate().getName(), station.getSeptBuild(getTemplate().getBuildingType()).getBuildingState().getLevel()});
			}
		} catch (BuildingNotFoundException e) {
			res = new SEPTBUILDING_LVDOWN_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_134);
		} catch (WrongLvOnLvDownException e) {
			res = new SEPTBUILDING_LVDOWN_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_152);
		} catch (ActionIsCDException e) {
			res = new SEPTBUILDING_LVDOWN_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_153 + e.getMsg());
		}
		if (res == null) {
			res = new SEPTBUILDING_LVDOWN_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_154);
		}
		player.addMessageToRightBag(res);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public SeptStation getStation() {
		return station;
	}

	public void setStation(SeptStation station) {
		this.station = station;
	}

	public long getNPCId() {
		return NPCId;
	}

	public void setNPCId(long nPCId) {
		NPCId = nPCId;
	}

	public SeptBuildingTemplet getTemplate() {
		return template;
	}

	public void setTemplate(SeptBuildingTemplet template) {
		this.template = template;
	}
	
	

}
