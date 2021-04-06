package com.fy.engineserver.activity.tengXun;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_Shop_Get;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.tengxun.TengXunDataManager;

public class TengXunActivityManager {

	public static TengXunActivityManager instance = new TengXunActivityManager();
	
	public static Logger logger = null;
	
	public static String NPC_NAME = "蓝钻精灵";
	
	public static int[] NPC_POINT = new int[]{3451,1238};
	
	public static String ACTIVITY_INDEX_CATCH_NAME = "txActivityIndex";
	public int activityIndex;
	public ArrayList<TengXunActivity> activitys = new ArrayList<TengXunActivity>();
	
	public long ACTIVITY_SPACETIME_MOULD = 1000L * 60 * 60 * 24 * 7;		//一周
	public long ACTIVITY_ENDTIME = 1000L * 60 * 60 * 24 - 1;				//一天
	
	public int ACTIVITY_WEEKDAY = Calendar.FRIDAY;
	
	public void init() {
		logger = TengXunDataManager.logger;
		Object obj = TengXunDataManager.instance.txDiskCatch.get(ACTIVITY_INDEX_CATCH_NAME);
		if (obj == null) {
			activityIndex = 0;
			TengXunDataManager.instance.txDiskCatch.put(ACTIVITY_INDEX_CATCH_NAME, "" + activityIndex);
			obj = "0";
		}
		int activityID = 300000;
		 
		{
			//喝酒和封魔录经验加5%
			TengXunExp_HJ_TMTActivity activity = new TengXunExp_HJ_TMTActivity(0, 0, "【经验主题日】\n周五当天，诸位蓝钻仙友，使用蓝色品质以上的酒类物品，将获得5%的经验加成。\n当天内，诸位蓝钻仙友，使用蓝色品质以上的封魔录，将获得5%的经验加成。", activityID, 500, activityID+1, 500);
			activity.setName("喝酒封魔录经验5%");
			activitys.add(activity);
			activityID++;
			activityID++;
		}
		
//		{
//			//喝酒经验加5%
//			TengXunHeJiuActivity actiivty  = new TengXunHeJiuActivity(0, 0, "今日主题：\n【经验美酒日】\n活动当天，所有魔钻用户活动当天使用蓝色品质以上的酒品，将获得5%的经验加成。", activityID, 500);
//			actiivty.setName("喝酒加经验");
//			activitys.add(actiivty);
//			activityID++;
//		}
//		{
//			//打贴加经验 5%
//			TengXunTuMoTieActivity activity = new TengXunTuMoTieActivity(0, 0, "活动当天，所有魔钻用户活动当天使用蓝色品质以上的封魔录，将获得5%的经验加成。", activityID, 500);
//			activity.setName("封魔录加经验");
//			activitys.add(activity);
//			activityID++;
//		}
		
		{
			//祈福次数
			TengXunQiFuActivity activity = new TengXunQiFuActivity(0, 0, "【五谷丰登日】\n周五当天，诸位蓝钻仙友，祈福次数增加2次。", 2);
			activity.setName("祈福次数");
			activitys.add(activity);
			activityID++;
		}
		
		{
			//喝酒次数
			TengXunHeJiuTimesActivity activity = new TengXunHeJiuTimesActivity(0, 0, "【杏花美酒日】\n周五当天，诸位蓝钻仙友，使用酒类物品次数增加2次。", 2);
			activity.setName("喝酒次数");
			activitys.add(activity);
			activityID++;
		}
		
		{
			//封魔录次数
			TengXunTuMoTieTimesActivity activity = new TengXunTuMoTieTimesActivity(0, 0, "【除魔卫道日】\n周五当天，诸位蓝钻仙友，封魔录使用次数增加2次。", 2);
			activity.setName("封魔录次数");
			activitys.add(activity);
			activityID++;
		}
		
//		{
//			//兑换商店活动
//			TengXunShopActivity activity = new TengXunShopActivity(0, 0, "【翱翔天际日】\n周五当天，诸位魔钻仙友，可使用“飞行坐骑碎片”兑换魔钻专属的飞行坐骑。");
//			activity.setName("兑换商店");
//			activitys.add(activity);
//			activityID++;
//		}
		
		activityIndex = Integer.parseInt(obj.toString());
		creatActivityTime();
		
		creatMoZhuanNpc();
		
		System.out.println("腾讯活动管理器启动成功");
	}
	
	public void heatBeat() {
		try{
			if (activityIndex >= 0 && activityIndex < activitys.size()) {
				
				TengXunActivity activity = activitys.get(activityIndex);
				if (activity.isStart()) {
					activity.onActivityStart();
					logger.warn("[魔钻活动开始] [{}] [{}]", new Object[]{activityIndex, activity.getName()});
				}
				if (activity.isEnd()) {
					activity.onActivityEnd();
					logger.warn("[魔钻活动结束] [{}] [{}]", new Object[]{activityIndex, activity.getName()});
					//这个活动结束了 切换下个活动 修改下个活动的时间
					nextActivity();
				}
			}else {
				activityIndex = 0;
			}
		}catch(Exception e) {
			logger.error("腾讯 activityManager 心跳出错", e);
		}
	}
	
	public void saveActivityIndex() {
		TengXunDataManager.instance.txDiskCatch.put(ACTIVITY_INDEX_CATCH_NAME, "" + activityIndex);
	}
	
	public void nextActivity() {
		activityIndex++;
		if (activityIndex >= activitys.size()) {
			activityIndex = 0;
		}
		saveActivityIndex();
		creatActivityTime();
	}

	public void creatActivityTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, ACTIVITY_WEEKDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 990);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String a = format.format(calendar.getTime());
		long endTime = 0;
		try {
			endTime = format.parse(a).getTime();
		} catch (ParseException e) {
			logger.error("creatActivityTime", e);
		}
		long now = System.currentTimeMillis();
		if (endTime < now) {
			endTime += ACTIVITY_SPACETIME_MOULD;
		}
		long startTime = endTime - ACTIVITY_ENDTIME;
		if (!(activityIndex >= 0 && activityIndex < activitys.size())) {
			activityIndex = 0;
		}
		activitys.get(activityIndex).setStartTime_Long(startTime);
		activitys.get(activityIndex).setEndTime_Long(endTime);
	}
	
	public static void creatMoZhuanNpc() {
		Game game1 = GameManager.getInstance().getGameByName("kunhuagucheng", 1);
		Game game2 = GameManager.getInstance().getGameByName("kunhuagucheng", 2);
		Game game3 = GameManager.getInstance().getGameByName("kunhuagucheng", 3);
		if (game1 != null) {
			NPC npc = MemoryNPCManager.getNPCManager().createNPC(600103);
			if (npc != null) {
				npc.setGameNames(game1.gi);
				npc.setX(NPC_POINT[0]);
				npc.setY(NPC_POINT[1]);
				game1.addSprite(npc);
			}
		}
		if (game2 != null) {
			NPC npc = MemoryNPCManager.getNPCManager().createNPC(600103);
			if (npc != null) {
				npc.setGameNames(game2.gi);
				npc.setX(NPC_POINT[0]);
				npc.setY(NPC_POINT[1]);
				game2.addSprite(npc);
			}
		}
		if (game3 != null) {
			NPC npc = MemoryNPCManager.getNPCManager().createNPC(600103);
			if (npc != null) {
				npc.setGameNames(game3.gi);
				npc.setX(NPC_POINT[0]);
				npc.setY(NPC_POINT[1]);
				game3.addSprite(npc);
			}
		}
	}
	
	
	/**
	 * 点击查看魔钻活动信息
	 * @param player
	 */
	public void option_activityMsg(Player player) {
		try{
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(200);
			Option_Cancel opt1 = new Option_Cancel();
			mw.setOptions(new Option[]{opt1});
			mw.setDescriptionInUUB(activitys.get(activityIndex).getActivityMsg());
			if (!TengXunDataManager.instance.isGameLevel(player.getId())) {
				mw.setDescriptionInUUB(mw.getDescriptionInUUB() + "\n您还暂时不是蓝钻用户哦！成为蓝钻用户，立享专属特权！");
			}
			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
			player.addMessageToRightBag(req);
		}catch(Exception e) {
			logger.error("option_activityMsg出错", e);
		}
	}
	
	public boolean isShopActivityOpen() {
		TengXunActivity activity = activitys.get(activityIndex);
		if (activity instanceof TengXunShopActivity) {
			if (activity.isStart() && !activity.isEnd()) {
				return true;
			}
		}
		return false;
	}
	
	public void option_mozhuanShop(Player player) {
		try{
			if (TengXunDataManager.instance.getGameLevel(player.getId()) > 0) {
				Option_Shop_Get shopGet = new Option_Shop_Get();
				shopGet.setShopName("魔钻会员专属商城");
				shopGet.doSelect(player.getCurrentGame(), player);
			}
		}catch(Exception e) {
			logger.error("option_mozhuanShop出错", e);
		}
	}
	
}
