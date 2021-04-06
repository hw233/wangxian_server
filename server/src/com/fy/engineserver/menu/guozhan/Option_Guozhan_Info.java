package com.fy.engineserver.menu.guozhan;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GUOZHAN_INFO_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
/**
 * 加入国战
 * 
 * 
 *
 */
public class Option_Guozhan_Info extends Option {

	/**
	 * 国战说明
	 * 1.判断当前国家有无国战，有国战发送确认消息
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
//		WindowManager wm = WindowManager.getInstance();
//		MenuWindow mw = wm.createTempMenuWindow(60 * 2);
//		mw.setTitle("国战说明");
		String info = Translate.translateString(Translate.国战规则, new String[][]{{"@startTime@", GuozhanOrganizer.getInstance().getConstants().开始宣战时间},{"@endTime@", GuozhanOrganizer.getInstance().getConstants().结束宣战时间},{"@startFightTime@", GuozhanOrganizer.getInstance().getConstants().国战开启时间},{"@guozhanTime@", String.valueOf(GuozhanOrganizer.getInstance().getConstants().国战初始时长)}});
		/*String info = "【报名】\n每天0点-18点可以向其他国家宣战。（第二天进行战斗，有1天的备战时间） 。\n" +
					"每天只能被一个国家宣战，也能对一个国家宣战。 \n" +
					"两个国家两天内只能进行一次战争。\n" +
					"最弱的一个国家2天内只能被宣战一次，第一名国家每天都能被宣战。\n" +
					"只有混元至圣、纯阳真圣或司命天王才能发起，点击【神威天将】-【发起国战】提出申请。\n" +
					"\n" +
					"【国战规则】\n" +
					"开始时间：\n" +
					GuozhanOrganizer.getInstance().getConstants().开始宣战时间+"-"+GuozhanOrganizer.getInstance().getConstants().结束宣战时间+"宣战，则第二天"+GuozhanOrganizer.getInstance().getConstants().国战开启时间+"国家正式战争开始，战斗时间为"+GuozhanOrganizer.getInstance().getConstants().国战初始时长+"分钟。\n" +
					"\n" +
					"国战胜败条件：\n" +
					"攻守双方的混元至圣、纯阳真圣或司命天王可以通过指挥界面进行战斗指挥，在"+GuozhanOrganizer.getInstance().getConstants().国战初始时长+"分钟的战斗中击杀掉防守国家的【龙象释帝】即获得国战胜利！\n" +
					"\n" +
					"国战路线：\n" +
					"参与国战的玩家通过3条路线抵达防守国家的王城。\n" +
					"第一条：由边境→佳梦关→王城\n" +
					"第二条：由边境→佳梦关→王城密道→王城\n" +
					"第三条：由边境→昆仑要塞→王城密道→王城\n" +
					"\n" +
					"国战复活点：\n" +
					"在佳梦关、昆仑要塞、王城分别由三个【空灵天兵】、【空玄天兵】、【龙象卫士】守卫，进攻方击杀守卫可占领其所在位置复活点。防守方可击杀进攻方守护NPC破坏进攻方复活点。\n" +
					"没有任何复活点，进攻方在本国边境复活。\n" +
					"防守国家复活在【龙象卫长】身边，如果【龙象卫长】被击杀，则防守方全部在王城默认复活点复活。\n" +
					"\n" +
					"国战资源：\n" +
					"进攻方可使用国战资源进行拉人，延长国战时间。每次国战最多可拉3次人，可将本国玩家拉到当前的复活区；延长2次国战，每次15分钟；\n" +
					"防守方可使用国战资源进行拉人，给龙象释帝加血。每次国战最多可拉3次人，可将本国玩家拉到到当前的复活区；给龙象释帝加2次血，每次15%；\n" +
					"\n" +
					"经验奖励：\n" + 
					"进攻方在国战开始后，在防守国地图内每占领一个【空灵天兵】、【空玄天兵】、【龙象卫士】守护的复活点，将获得更多经验；击杀了【龙象卫长】也会获得更多经验，击杀【龙象释帝】后获得国战胜利（只击杀【龙象释帝】只会获得基础经验）\n" + 
					"防守方在国战开始后，在本国地图内每守卫一个【空灵天兵】、【空玄天兵】、【龙象卫士】守护的复活点，将获得更多经验；保护【龙象卫长】未被击杀，也会获得更多经验，90分钟内【龙象释帝】未被击杀，获得国战胜利（只守护【龙象释帝】只会获得基础经验）\n" + 
					"\n" + 
					"注：国战状态死亡不掉落道具。";*/
//		mw.setDescriptionInUUB(info);
//		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
//		player.addMessageToRightBag(res);
		
		GUOZHAN_INFO_REQ req = new GUOZHAN_INFO_REQ(GameMessageFactory.nextSequnceNum(), info);
		player.addMessageToRightBag(req);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}
}
