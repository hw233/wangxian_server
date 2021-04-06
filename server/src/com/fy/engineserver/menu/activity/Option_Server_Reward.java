package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.godDown.GodDwonManager;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.notice.Option_Query_Notice_Forever;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 服务器维护，领取物品
 * @author Administrator
 *
 */
public class Option_Server_Reward extends Option implements NeedCheckPurview{
	
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setDescriptionInUUB(" 飘渺寻仙曲团队对于部分玩家丢失道具，造成您无法正常游戏，向您表达真诚的道歉！游戏时出现的种种问题，造成仙友们无法正常游戏，给您造成的不良影响我们深感自责，工作人员在第一时间投入问题修复，截止目前，游戏运转已大部分恢复正常。 如您在游戏中发现道具缺失的情况，请玩家联系在线客服或拨打客服电话400-666-6170联系官方人员。为了表达我们深刻的歉意，我们将送给每位仙友5份美酒锦囊，里面包含着我们对您的深情歉意，礼包请在【昆华古城】-【NPC财神】处领取。《飘渺寻仙曲》团队全体成员敬上2013年6月8日");
		Option_Server_Reward_Sure option = new Option_Server_Reward_Sure();
		option.setText("领取");
		mw.setOptions(new Option[]{option});
		QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(req);
	}

	@Override
	public boolean canSee(Player player) {
		long starttime = TimeTool.formatter.varChar19.parse("2013-06-08 00:00:00");
		long endtime = TimeTool.formatter.varChar19.parse("2013-06-10 23:59:59");
		long now = System.currentTimeMillis();
		if(now < starttime || endtime < now){
			return false;
		}
		
		String servernames [] = {"桃源仙境","国内本地测试"};
		String servername = GameConstants.getInstance().getServerName();
		if(servername!=null){
			for(String name:servernames){
				if(servername.equals(name)){
					return true;
				}
			}
		}
		return false;
	}
	
}


