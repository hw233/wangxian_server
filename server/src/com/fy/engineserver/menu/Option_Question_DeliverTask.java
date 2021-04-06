package com.fy.engineserver.menu;

import com.fy.engineserver.datasource.language.Translate;


public class Option_Question_DeliverTask extends Option {

//	TaskEntity te;
//
//	public Option_Question_DeliverTask(TaskEntity te) {
//		setText(Translate.translate.text_671);
//		setColor(0xFFFFFF);
//		this.te = te;
//	}
//
//
//	/**
//	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
//	 * @param game
//	 * @param player
//	 */
//	public void doSelect(Game game,Player player){
//		TaskManager tm = TaskManager.getInstance();
//		
//		TASK_SEND_ACTION_REQ req = new TASK_SEND_ACTION_REQ(GameMessageFactory.nextSequnceNum(),(byte)2,te.getTaskId(),new String[] {});
//		
//		tm.handle_TASK_SEND_ACTION_REQ(player, req);
//	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_进入商店;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_671 ;
	}
}
