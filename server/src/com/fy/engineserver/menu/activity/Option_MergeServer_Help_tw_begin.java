package com.fy.engineserver.menu.activity;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.console.ChangeAble;
import com.xuanzhi.boss.game.GameConstants;

public class Option_MergeServer_Help_tw_begin extends Option implements NeedCheckPurview{

	public Option_MergeServer_Help_tw_begin() {
	}
	
	@ChangeAble("startTime")
	private String startTime = "2013-08-12 00:00:00";
	@ChangeAble("endTime")
	private String endTime = "2013-09-18 23:59:59";
	@ChangeAble("openplats")
	private Platform[] openplats = { Platform.台湾 };
	@ChangeAble("limitservers")
	private Set<String> limitservers = new HashSet<String>();
	@ChangeAble("openServers")
	private String[] openServers = {"仙尊降世","雪域冰城","皇圖霸業"};
	
	public static String servernames [] = {"仙尊降世","雪域冰城","皇圖霸業"};
	public static String mess = "親愛的玩家,伺服器進入倒計時階段。即日起至9月19日23：59,這階段內,伺服器部分功能即將關閉,內容如下：\n"
+"1.關閉創建角色。此期間，玩家將無法在本服創建新的角色；\n"
+"2.關閉郵件功能。不可發送郵件，請玩家於這階段內將油箱內郵件收取，以免造成損失；\n"
+"3.關閉武聖。此期間玩家將無法參與該活動，但是玩家可以領取武聖獎勵；\n"
+"4.關閉五方聖獸；\n"
+"5.關閉結婚功能；\n"
+"6.關閉求購。玩家無法進行求購，並無法進行快速出售；\n"
+"7.關閉拍賣；\n"
+"8.關閉城戰；\n"
+"9.關閉礦戰；\n"
+"10.關閉仙府。此期間將被封印，合服後玩家自行解封。為避免損失，請將田地收穫；\n"
+"11.關閉鏢局；\n"
+"12.關閉國戰；\n";
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
			
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setDescriptionInUUB(mess);
			Option_Cancel oc = new Option_Cancel();
			oc.setText(Translate.确定);
			mw.setOptions(new Option[]{oc});
			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
			player.addMessageToRightBag(req);
	}
	
	@Override
	public boolean canSee(Player player) {
		long now = System.currentTimeMillis();
		if(!PlatformManager.getInstance().isPlatformOf(openplats)){
			return false;
		}
		
		GameConstants gc = GameConstants.getInstance();
		if(gc==null){
			return false;
		}
		
		if(limitservers.contains(gc.getServerName())){
			return false;
		}
		
		if(TimeTool.formatter.varChar19.parse(startTime) > now || now > TimeTool.formatter.varChar19.parse(endTime)){
			return false;
		}
		
		for(String s:openServers){
			if(s.equals(gc.getServerName())){
				return true;
			}
		}
		
		if(openServers.length==0){
			return true;
		}
		
		return false;
	}
}
