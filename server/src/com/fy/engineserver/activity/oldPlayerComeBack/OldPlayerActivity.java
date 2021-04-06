package com.fy.engineserver.activity.oldPlayerComeBack;

import java.util.Set;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 国服老玩家回归活动
 * 
 *
 */
public class OldPlayerActivity extends ActivityConfig implements MConsole{
	
	@ChangeAble("上次登录距离现在的时间")
	public static long timeLength = 15*24*60*60*1000;
	
	public OldPlayerActivity(){}
	
	public OldPlayerActivity(long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers,int levelLimit[]){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
		this.levelLimit = levelLimit;
		MConsoleManager.register(this);
	}

	@Override
	public void doPrizes(Player player) {
		// TODO Auto-generated method stub
		OldPlayerInfo oldInfo = null;
		DefaultDiskCache ddc = OldPlayerComeBackManager.getInstance().getDdc();
		if(ddc.get(activityKey(player))==null){
			oldInfo = new OldPlayerInfo();
		}else{
			oldInfo = (OldPlayerInfo)ddc.get(activityKey(player));
		}
		
		if(System.currentTimeMillis() - player.getPlayerLastLoginTime() >= timeLength){
			oldInfo.setOLD_PLAYER_TYPE(2);
		}
		noticeOptionMess(player,oldInfo);
		ddc.put(activityKey(player), oldInfo);	
	}
	
	/**
	 * 根据类型去弹框通知玩家
	 * @param type
	 */
	private void noticeOptionMess(Player p,OldPlayerInfo oldInfo){
		String msg = "";
		if(oldInfo.getOLD_PLAYER_TYPE() == 2){
			msg = Translate.老玩家回归奖励描述;
		}else{
			msg =Translate.translateString(Translate.老玩家回归奖励描述2, new String[][]{{Translate.COUNT_1,oldInfo.getPlayerBackNums()+""},{Translate.COUNT_2,oldInfo.getPlayerBackNums()+""}}); 
		}
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(200);
		Option_Cancel op2 = new Option_Cancel();
		op2.setText(Translate.确定);
		mw.setDescriptionInUUB(msg);
		Option[] options = new Option[] { op2 };
		mw.setOptions(options);
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), msg, mw.getOptions());
		p.addMessageToRightBag(req);
	}

	@Override
	public String activityKey(Player player) {
		return player.getId()+OldPlayerComeBackManager.getInstance().activitykey;
//		return player.getId()+this.getClass().getSimpleName()+"20131114";
	}

	@Override
	public String getMConsoleName() {
		return "老玩家回归活动控制";
	}

	@Override
	public String getMConsoleDescription() {
		return "老玩家回归活动控制";
	}
	

}
