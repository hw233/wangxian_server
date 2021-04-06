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
public class Option_NPC_Notice extends Option implements NeedCheckPurview{
	private String startTime = "2013-06-08 00:00:00";
	private String endTime = "2013-09-05 23:59:59";
	private Platform[] openplats = { Platform.官方 ,Platform.腾讯,Platform.台湾};
	private Set<String> limitservers = new HashSet<String>();
	private static String contentmess = "活动期间每日20:00—21:30依次在三个国家的昆华古城、昆仑圣殿以及中立地区的灭魔神域全部地图内随机刷新诸天秘宝，所有玩家都可以拾取，拾取后会随机获得诸天宝箱、诸天密钥、诸天金密钥、诸天七彩密钥等，使用不同品质的诸天密钥开启诸天宝箱，将会获得不同品质的宝库,同期做活动任务开启古董也会获得诸天宝箱\n"
+"诸天密钥在昆华古城财神处可以兑换更高品质的密钥，商城出售的为诸天铜密钥。活动期间，充值10元即可获赠价值10元的诸天密钥，绝对超值，绝对给力。\n"
+"<f color='#ffff00' >诸天密钥开启诸天宝箱：</f>\n"
+"<f color='# 0x00ff00' >获得礼包内容：</f>\n强化石、对应等级职业蓝装（随机个数）、鉴定符（随机个数）、育兽丹（随机个数较大几率）、诸天铜密钥*1其中之一\n"
+"<f color='#ffff00' >诸天铜密钥开启诸天宝箱：</f>\n"
+"<f color='# 0x00ff00' >获得礼包内容：</f>\n绿色强化石（随机个数较大几率）、传奇鉴定符（随机个数较大几率）、绿色育兽丹（随机个数）、对应等级职业完美紫色装备*1、诸天银密钥*1其中之一\n"
+"<f color='#ffff00' >诸天银密钥开启诸天宝箱：</f>\n"
+"<f color='# 0x00ff00' >获得礼包内容：</f> \n蓝色强化石（随机个数较大几率）、传说鉴定符（随机个数）、蓝色育兽丹（随机个数较大几率）、对应等级职业橙色装备*1、诸天金密钥*1其中之一\n"
+"<f color='#ffff00' >诸天金密钥开启诸天宝箱：</f>\n"
+"<f color='# 0x00ff00' >获得礼包内容：</f>\n紫色强化石（随机个数）、神话鉴定符（随机个数）、紫色育兽丹（随机个数）、飞行坐骑碎片（随机个数）、绝品圣宠精魄（随机个数较大几率）、仙宠宝箱*1、对应等级职业完美橙色装备*1、诸天七彩密钥*1其中之一\n"
+"<f color='#ffff00' >诸天七彩密钥开启诸天宝箱：</f>\n"
+"<f color='# 0x00ff00' >获得礼包内容：</f>\n橙色强化石（随机个数）、永恒鉴定符（随机个数）、橙色育兽丹（随机个数）、飞行坐骑碎片（随机个数）、绝品圣宠精魄（随机个数较大几率）、仙宠宝箱*1、对应等级职业完美橙色装备*1其中之一\n";

	private static String title = "<f color='0xffff00'>诸天守望系列活动规则</f>\n";
	
	static {
		if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
			title = "<f color='0xffff00'>諸天守望系列活動規則</f>\n";
			contentmess = "活動期間每日20:00—21:30依次在三個國家的昆华古城、飄渺王城以及中立地區的灭魔神域全部地圖內隨機刷新諸天秘寶，所有玩家都可以拾取，拾取後會隨機獲得諸天寶箱、諸天密鑰、諸天金密鑰、諸天七彩密鑰等，使用不同品質的諸天密鑰開啟諸天寶箱，將會獲得不同品質的寶庫,同期做活動任務開啟古董也會獲得諸天寶箱\n"
					+"諸天密鑰在昆华古城財神處可以兌換更高品質的密鑰，商城出售的為諸天銅密鑰。活動期間，充值10元即可獲贈價值10元的諸天密鑰，絕對超值，絕對給力。\n"
					+"<f color='#ffff00' >諸天密鑰開啟諸天寶箱：</f>\n"
					+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n煉星符、對應等級職業藍裝（隨機個數）、鑒定符（隨機個數）、靈獸內丹（隨機個數較大幾率）、諸天銅密鑰*1其中之一\n"
					+"<f color='#ffff00' >諸天銅密鑰開啟諸天寶箱：</f>\n"
					+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n綠色煉星符（隨機個數較大幾率）、傳奇鑒定符（隨機個數較大幾率）、綠色靈獸內丹（隨機個數）、對應等級職業完美紫色裝備*1、諸天銀密鑰*1其中之一\n"
					+"<f color='#ffff00' >諸天銀密鑰開啟諸天寶箱：</f>\n"
					+"<f color='# 0x00ff00' >獲得禮包內容：</f> \n藍色煉星符（隨機個數較大幾率）、傳說鑒定符（隨機個數）、藍色靈獸內丹（隨機個數較大幾率）、對應等級職業橙色裝備*1、諸天金密鑰*1其中之一\n"
					+"<f color='#ffff00' >諸天金密鑰開啟諸天寶箱：</f>\n"
					+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n紫色煉星符（隨機個數）、神話鑒定符（隨機個數）、紫色靈獸內丹（隨機個數）、飛行坐騎碎片（隨機個數）、絕品聖寵精魄（隨機個數較大幾率）、仙寵寶箱*1、對應等級職業完美橙色裝備*1、諸天七彩密鑰*1其中之一\n"
					+"<f color='#ffff00' >諸天七彩密鑰開啟諸天寶箱：</f>\n"
					+"<f color='# 0x00ff00' >獲得禮包內容：</f>\n橙色煉星符（隨機個數）、永恆鑒定符（隨機個數）、橙色靈獸內丹（隨機個數）、飛行坐騎碎片（隨機個數）、絕品聖寵精魄（隨機個數較大幾率）、仙寵寶箱*1、對應等級職業完美橙色裝備*1其中之一\n";  
		}
		
	}
	
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


