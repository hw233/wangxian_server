package com.fy.engineserver.activity.wolf.runtask;

import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.language.Translate;

public class NoticeTask implements Runnable{
	
	private int runState;
	private int runState1 = 1;
	private int runState5 = 5;
	private int runState10 = 10;
	
	public void nonticeActivityStart(int state){
		if(state > 0){
			String mess = Translate.translateString(Translate.狼抓羊活动开启通知, new String[][] { { Translate.COUNT_1, String.valueOf(state)}});
			ChatMessage msg = new ChatMessage();
			msg.setMessageText(mess);
			try {
				ChatMessageService.getInstance().sendRoolMessageToSystem(msg);
				if(WolfManager.logger.isWarnEnabled()){
					WolfManager.logger.warn("[小羊快跑] [通知活动即将开启] [state:"+state+"] ["+runState+"]");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		while(WolfManager.START_RUN){
			try {
				Thread.sleep(WolfManager.SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			WolfManager.getInstance().checkOnlinePlayer();
			WolfManager.getInstance().initConfig();
			WolfManager.getInstance().matchTeam();
			WolfManager.getInstance().noticeEnter();
			if(WolfManager.getInstance().currConfig != null){
				if(WolfManager.getInstance().currConfig.notice10Minute() && runState != runState10){
					runState = runState10;
					nonticeActivityStart(runState);
				}else if(WolfManager.getInstance().currConfig.notice5Minute() && runState != runState5){
					runState = runState5;
					nonticeActivityStart(runState);
				}else if(WolfManager.getInstance().currConfig.notice1Minute() && runState != runState1){
					runState = runState1;
					nonticeActivityStart(runState);
				}
			}
		}
	}

}
