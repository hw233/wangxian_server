package com.fy.engineserver.sprite;

import com.fy.engineserver.chuangong.ChuanGongManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_INTERRUPT_REQ;

public class SpecialTimerTask extends TimerTask {

	
	private Player active;
	private Player passive;
	@Override
	public void breakNoticePlayer(Player owner) {
		
		
		active.setChuangongState(false);
		active.setChuangonging(false);
		
		passive.setChuangongState(false);
		passive.setChuangonging(false);
		
		active.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_INTERRUPT_REQ(GameMessageFactory.nextSequnceNum()));
		passive.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_INTERRUPT_REQ(GameMessageFactory.nextSequnceNum()));
		if(ChuanGongManager.logger.isWarnEnabled()){
			ChuanGongManager.logger.warn("[传功被打断] ["+(active != null ?active.getLogString():"")+"] ["+(passive != null?passive.getLogString():"")+"]");
		}
	}
	
	
}
