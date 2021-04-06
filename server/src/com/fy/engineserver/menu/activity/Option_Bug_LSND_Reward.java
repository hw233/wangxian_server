package com.fy.engineserver.menu.activity;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 2013-09-14育兽丹bug
 * @author Administrator
 *
 */
public class Option_Bug_LSND_Reward extends Option implements NeedCheckPurview{
	
	private long startTime = TimeTool.formatter.varChar19.parse("2013-09-14 11:11:11");
	private long endTime = TimeTool.formatter.varChar19.parse("2013-09-15 23:59:59");
	private String[] openServerNames = {"国内本地测试","昆仑圣殿","桃源仙境","风雪之巅","燃烧圣殿","西方灵山","傲啸封仙","再续前缘","一统江湖","龙隐幽谷","前尘忆梦","诸神梦境","九霄龙吟","天下无双","盛世永安","浩瀚乾坤","千娇百媚","九仙揽月","天府之国","金戈铁马","一战成名","千秋北斗","绿野仙踪","琴瑟和鸣","破晓之穹","菩提众生","上善若水","傲视群雄","伏虎降龙","云游魂境","普陀梵音","春暖花开","似水流年","华夏战神","群雄逐鹿","一方霸主","问鼎江湖","勇者无敌","雄霸天下","明空梵天","洪武大帝","半城烟沙","策马江湖","龙争虎斗","龙翔凤舞","鱼跃龙门","笑傲江湖","仙界至尊","金蛇圣殿","修罗转生","飘渺仙道","海纳百川","洪荒古殿","狂傲天下","月满西楼","程门立雪","天上人间","豪杰聚义","百花深谷","奇门辉煌","天涯海角","侠骨柔情","卧虎藏龙","仙山楼阁","天魔神谭","一代天骄"};
	@Override
	public void doSelect(Game game, Player player) {
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle("《飘渺寻仙曲》9月14日紧急维护说明");
		mw.setDescriptionInUUB("《飘渺寻仙曲》9月14日紧急维护说明\n尊敬的仙友：\n今日0点商城上架橙色育兽丹后，部分玩家利用系统漏洞非法获取大量橙色育兽丹，并进行多起非法交易 ，对广大仙友和游戏环境造成了极其恶劣的影响。为了在第一时间尽快阻止非法行为，保护广大仙友的正当利 益，维护游戏公平公正的环境，我们在凌晨4点半对有利用漏洞非法获利玩家所在的服务器进行临时维护。给 大家带来的不便，我们深感抱歉。\n"

+"目前我们正全力以赴的查询数据，将尽快排查出非法获利的玩家，以及与之相关的非法交易。为减少对大家正 常游戏的影响，数据查询结束后我们将尽快开服并对临时维护的服务器给予补偿，请大家关注论坛和游戏。\n"

+"请广大仙友放心，官方对于这种严重影响游戏公平性的非法行为将进行严厉处理！所有非法获取的道具，包括 通过非法交易获得的道具，将进行收回及销毁；对于已经用于宠物幻化的育兽丹，也将按照使用非法道具的 数量对宠物幻化进行降级处理。并将对非法获利玩家进行临时封号处理！\n"

+"为了不辜负所有支持热爱《飘渺寻仙曲》玩家的期待，为了《飘渺寻仙曲》的长期健康发展，始终维持公平、欢乐、良好的 游戏环境，请您主动遵守游戏规则，欢迎您举报非法游戏行为。对于所有破坏《飘渺寻仙曲》游戏世界平衡、牟取恶 利，同时严重损害其他正常玩家利益，影响其他玩家正常游戏的行为，我们将进行严厉处罚，并保留依法追究 法律责任的权利！\n"

+"另，为补偿所有仙友，大家可去[昆华古城]财神-NPC处领取补偿。\n");
		Option_Bug_LSND_Reward_Sure option = new Option_Bug_LSND_Reward_Sure();
		option.setText("领取");
		mw.setOptions(new Option[]{option});
		QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(req);
	}
	
	@Override
	public boolean canSee(Player player) {
		long now = SystemTime.currentTimeMillis();
		String servername = GameConstants.getInstance().getServerName();
		if(startTime > now || now > endTime){
			return false;
		}
		
		for(String name : openServerNames){
			if(servername.equals(name)){
				return true;
			}
		}
		
		return false;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
}


