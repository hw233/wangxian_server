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
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 服务器维护，领取物品
 * @author Administrator
 *
 */
public class Option_NPC_Notice_TW extends Option implements NeedCheckPurview{
	private String startTime = "2013-06-08 00:00:00";
	private String endTime = "2013-07-24 23:59:59";
	private Platform[] openplats = { Platform.台湾};
	private Set<String> limitservers = new HashSet<String>();
	private String contentmess = "活動期間累計充值滿一定金額即可獲贈1個禮包，同時在灭魔神域狂歡周使者、狂歡周大使身上開啟一次狂歡周回饋寶庫，可以以超低的價格購買一個超實惠禮包（注：回饋寶庫只有充值滿固定金額才會開啟，未達到要求不會開啟）\n"
			+"<f color='#ffff00' >累計充值滿500兩：</f>\n"
			+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n寵物經驗丹（500萬）*2，令旗*3\n"
			+"<f color='# 00b0f0' >寶庫250兩購買禮包內容：</f>\n對應等級藍酒*4，鑒定符*10，魯班令*1，銘刻符*3，冰棒*3\n"
			+"<f color='#ffff00' >累計充值滿900兩：</f>\n"
			+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n煉星符*5竹青寶石折扣劵*1（商城購買2級竹青寶石2個打5折）\n"
			+"<f color='# 00b0f0' >寶庫250兩購買禮包內容：</f>\n香火*2，靈獸內丹*4，初級煉妖石*10，紅玫瑰*99，萬里神識符*1\n"
			+"<f color='#ffff00' >累計充值滿1錠900兩：</f>\n"
			+"<f color='# 0x00ff00' >獲得禮包內容：</f> \n7星破碎星辰*1，綠色防爆包*1，對應等級藍酒*1\n"
			+"<f color='# 00b0f0' >寶庫500兩購買禮包內容：</f>\n鑄靈石碎片*4，傳奇?鑒定符*3，免罪金牌*1，押鏢令*1，田地令*1\n"
			+"<f color='#ffff00' >累計充值滿2錠900兩：</f>\n"
			+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n寵物經驗丹（500萬）*2，煉星符（白）劵*1（商城購買30個白色煉星符5折），靈獸內丹（白）劵*1（商城購買5個白色靈獸內丹7折）\n"
			+"<f color='# 00b0f0' >寶庫500兩購買禮包內容：</f>\n聖獸碎片*3，對應職業攻擊寶石3級*1，高級魯班令*1，軟糖*99，海鮮大餐*1\n"
			+"<f color='#ffff00' >累計充值滿4錠400兩：</f>\n"
			+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n萬里神識符*10,鑒定符*10,紫色裝備*2\n"
			+"<f color='# 00b0f0' >寶庫750兩購買禮包內容：</f>\n極品洗髓丹（返還100%元氣），鑄靈石（4級）*1，押鏢令（橙）*2，小藍片你懂的*10，羅漢果（橙）*10\n"
			+"<f color='#ffff00' >累計充值滿6錠900兩：</f>\n"
			+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n2級墨輪寶石劵*1（商城購買2級墨輪寶石4顆5折），2級湛天寶石卷*1（商城購買2級湛天寶石4顆7折），元氣丹*1\n"
			+"<f color='# 00b0f0' >寶庫1錠購買禮包內容：</f>\n逆天大禮包*2，通天大禮包*10 ，進階技能包（隨機）*1，山菅蘭*20   ，聖獸大禮包*5\n"
			+"<f color='#ffff00' >累計充值滿9錠400兩：</f>\n"
			+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n美酒錦囊*1，中級煉妖石*5，鑒定符*5\n"
			+"<f color='# 00b0f0' >寶庫1.1錠購買禮包內容：</f>\n相應職業攻擊寶石3級*2，香火*5，萬里神識符打折卷*5（商城購買10個萬里神識符打4折），藍色妖姬99打折卷*5（商城購買99個藍色妖姬打6折），高級鎖魂符*2\n"
			+"<f color='#ffff00' >累計充值滿13錠400兩：</f>\n"
			+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n靈獸內丹（綠）*3 傳說?鑒定符*3 煉器符*10\n"
			+"<f color='# 00b0f0' >寶庫2錠購買禮包內容：</f>\n鑄靈石（5級）*1 ，屬性寶石錦囊*4 ，魯班令*3 ，高級魯班令*3 ，鬼斧神工令*3\n"
			+"<f color='#ffff00' >累計充值滿24錠400兩：</f>\n"
			+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n靈獸內丹50個券*1（商城購買靈獸內丹（白）50個5折），煉星符50券*2 （商城購買煉星符（白）50個7折），寵物經驗丹（1億）*1\n"
			+"<f color='# 00b0f0' >寶庫9錠購買禮包內容：</f>\n寵物精魄碎片*200 ，永恆?鑒定符*2，萬里神識符*20，香火*2 ，高級煉妖石*5\n"
			+"<f color='#ffff00' >累計充值滿44錠400兩：</f>\n"
			+"<f color='# 0x00ff00' >獲得禮包內容：</f>工資轉換卡*1（3錠銀子換6錠工資），藍色佳釀錦囊*1，封魔录禮包(藍)*1\n"
			+"<f color='# 00b0f0' >寶庫15錠購買禮包內容：</f>\n煉星符（橙）*1，靈獸內丹（紫）*2 ，寶石墨輪（4級）*1，寶石湛天（4級）*1，才子印記*5\n";
	private String title = "<f color='0xffff00'>狂歡周活動規則</f>\n";
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setDescriptionInUUB(contentmess);
		mw.setTitle(title);
		Option_Cancel oc = new Option_Cancel();
		oc.setText(Translate.确定);
		mw.setOptions(new Option[]{oc});
		QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
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
		
		return true;
	}
	
}


