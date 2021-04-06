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

public class Option_MergeServer_Help_tw extends Option implements NeedCheckPurview{

	public Option_MergeServer_Help_tw() {
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
	public static String mess[] = {"為合服做最後的衝刺！即日起至9月19日23：59，玩家可享受合服前饕餮活動盛宴！詳情見下：\n<f color='#ffff00'>\n活動一、\n合服戰備錦囊每日領</f>9月17日維護後-9月19日23:59分開始,雪域冰城的玩家每日可在飄渺王城-合服大使npc處領取合服錦囊好禮,錦囊含(寵物經驗丹(2.5億)、逆天元氣丹、當前等級橙色酒*1、當前等級橙色屠魔貼*2);<f color='#ffff00'>\n活動二、\n海量經驗助力仙友</f>"
			+"在正式合服前,[雪域冰城]伺服器每日喝酒,打貼次數增加2次哦,暢享極速升級樂趣!"
			+"<f color='#ffff00'>\n活動三、\n勇奪飄渺王城</f>"
			+"飄渺王城爭奪戰戰備報名,9月17日維護後-9月19日23:59分宗派宗主可以至NPC處，報名宗派合服後攻佔飄渺王城,提前報名宗派奪取合服後首次“勇奪飄渺王城”活動獎勵同時，每個宗派成員加送2錠工資哦;"
			+"<f color='#ffff00'>\n活動四、\n拉幫結派備戰新服</f>" 
			+"宗派、結義史無前例最低折扣,正式合服前9月17日維護後—9月19日23:59分;建立宗派、結義所需銀兩全部5折啦,為家人,為兄弟而戰吧!",
		"為合服做最後的衝刺！即日起至9月19日23：59，玩家可享受合服前饕餮活動盛宴！詳情見下：\n<f color='#ffff00'>\n活動一、\n合服戰備錦囊每日領</f>9月17日維護後-9月19日23:59分開始,雪域冰城的玩家每日可在飄渺王城-合服大使npc處領取合服錦囊好禮,錦囊含(寵物經驗丹(2.5億)、逆天元氣丹、當前等級橙色酒*1、當前等級橙色屠魔貼*2);<f color='#ffff00'>\n活動二、\n海量經驗助力仙友</f>"
+"在正式合服前,[雪域冰城]伺服器每日喝酒,打貼次數增加2次哦,暢享極速升級樂趣!"
+"<f color='#ffff00'>\n活動三、\n勇奪飄渺王城</f>"
+"飄渺王城爭奪戰戰備報名,9月17日維護後-9月19日23:59分宗派宗主可以至NPC處，報名宗派合服後攻佔飄渺王城,提前報名宗派奪取合服後首次“勇奪飄渺王城”活動獎勵同時，每個宗派成員加送2錠工資哦;"
+"<f color='#ffff00'>\n活動四、\n拉幫結派備戰新服</f>"
+"宗派、結義史無前例最低折扣,正式合服前9月17日維護後—9月19日23:59分;建立宗派、結義所需銀兩全部5折啦,為家人,為兄弟而戰吧!"
,"為合服做最後的衝刺！即日起至9月19日23：59，玩家可享受合服前饕餮活動盛宴！詳情見下：\n"
+"<f color='#ffff00'>\n活動一、\n合服戰備錦囊每日領</f>"
+"9月17日維護後-9月19日23:59分開始,雪域冰城的玩家每日可在飄渺王城-合服大使npc處領取合服回饋錦囊好禮,錦囊含(寵物經驗丹(5億)、逆天元氣丹、當前等級橙色酒*2（橙色佳釀錦囊）、當前等級橙色屠魔貼*2（封魔录錦囊(橙)）);"
+"<f color='#ffff00'>\n活動二、\n海量經驗助力仙友</f>"
+"在正式合服前,[皇圖霸業]伺服器每日喝酒,打貼次數增加5次哦,暢享極速升級樂趣!"
+"<f color='#ffff00'>\n活動三、\n雙倍集結戰備軍</f>"
+"9月17日維護後-9月19日23:59分開始，每天18：00-22：00仙友們可享受雙倍經驗哦！"
+"<f color='#ffff00'>\n活動四、\n勇奪飄渺王城</f>"
+"飄渺王城爭奪戰戰備報名,9月17日維護後-9月19日23:59分宗派宗主可以至NPC處，報名宗派合服後攻佔飄渺王城,提前報名宗派奪取合服後首次“勇奪飄渺王城”活動獎勵同時，每個宗派成員加送2錠工資哦;"
+"<f color='#ffff00'>\n活動五、\n拉幫結派備戰新服</f>"
+"宗派、結義史無前例最低折扣,正式合服前9月17日維護後—9月19日23:59分;建立宗派、結義所需銀兩全部5折啦,為家人,為兄弟而戰吧!"

	};
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
			String servername = GameConstants.getInstance().getServerName();
			int index = 0;
			for(int i=0;i<servernames.length;i++){
				if(servernames[i].equals(servername)){
					index = i;
					break;
				}
			}
			String des = mess[index];
			
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setDescriptionInUUB(des);
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
