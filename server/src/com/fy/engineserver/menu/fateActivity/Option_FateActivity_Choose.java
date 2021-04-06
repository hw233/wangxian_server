package com.fy.engineserver.menu.fateActivity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 仙缘活动选项
 * @author Administrator
 *
 */
public class Option_FateActivity_Choose extends Option {
	
	private long activityId;
	private Player invited;
	private byte type;
	private int levle;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
//		if(!invited.isOnline()){
//			player.send_HINT_REQ(invited.getName()+"不在线");
//			return;
//		}
//		//向被邀请人发送
//		if(invited.getActivityId(type) > 0){
//			player.send_HINT_REQ(invited.getName()+"正在进行任务");
//		}
//		FateActivity fa = FateManager.getInstance().getFateActivity(activityId);
//		
//		if(invited.getFinishActivityNum(type) <= fa.getTemplate().getPerNum()){
//			
//			//弹出框选择框
//			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
//			String descript = Translate.translateString(Translate.text_请求仙缘描述, new String[][] {{Translate.PLAYER_NAME_1,invited.getName()}});
//			mw.setDescriptionInUUB(descript);
//			
//			long activityId = player.getActivityId(type);
//			Option_FateActivity_Agree agree = new Option_FateActivity_Agree();
//			agree.setActivityId(activityId);
//			agree.setSuccessNum(fa.getSuccessNum());
//			agree.setType(type);
//			agree.setLevel(levle);
//			
//			Option_FateActivity_Disagree disAgree = new Option_FateActivity_Disagree();
//			disAgree.setInvite(player);
//			disAgree.setType(type);
//			mw.setOptions(new Option[] { agree, disAgree });
//			
//			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
//			invited.addMessageToRightBag(res);
//			
//			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, "已经选择了"+invited.getName()+"作为你的有缘人，请等待回应");
//			player.addMessageToRightBag(hreq);
//			
//		}else{
//			player.send_HINT_REQ(invited.getName()+"今天已经做够");
//		}
		
	}

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public Player getInvited() {
		return invited;
	}

	public void setInvited(Player invited) {
		this.invited = invited;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getLevle() {
		return levle;
	}

	public void setLevle(int levle) {
		this.levle = levle;
	}
}
