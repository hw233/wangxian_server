package com.fy.engineserver.datasource.article.data.props;

import java.util.Arrays;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.dice.DiceManager;
import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.activity.fairyBuddha.challenge.FairyChallengeManager;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.activity.xianling.XianLingChallengeManager;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_Find_Way;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.cave.Option_Enter_Cave_Confirm;
import com.fy.engineserver.message.CAVE_QUERY_SELFFAERY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.unite.UniteManager;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

/**
 * 
 * 召集令道具
 * 
 */
public class ZhaoJiLingProps extends Props {

	// 召集令类型
	private byte zhaoJiLingType;
	private String transferGame;
	private boolean countryLimit;

	public static int 混元至圣令 = 0;
	public static int 兄弟令 = 1;
	public static int 比翼令 = 2;
	public static int 族长令 = 3;
	public static int 宗主令 = 4;
	public static int 回城 = 5;
	public static int 回仙府 = 6;

	/**
	 * 使用传送符。
	 * 1，判断传送目标地图是不是玩家当前所在地图。如果是，发送SET_POSITION_REQ指令
	 * 2，如果不是，直接把玩家从当前地图中退出，设置跳转点，发送CHANGE_GAME_REQ指令
	 * 
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {

		try {
			if (FairyChallengeManager.getInst().isPlayerChallenging(player)/* && zhaoJiLingType == 回城*/) {
				player.sendError(Translate.挑战仙尊限制回城);
				return false;
			}
		} catch (Exception e) {

		}
		
		try{
			if(WolfManager.getInstance().isWolfGame(player)){
				player.sendError(Translate.参加活动限制回城);
				return false;
			}
			if(game.gi != null && game.gi.name.contains("jiazuBOSSditu")){
				player.sendError(Translate.此副本不能回城);
				return false;
			}
			if(game.gi != null && game.gi.name.contains("shijieBOSSditu")){
				player.sendError(Translate.此副本不能回城);
				return false;
			}
			if(game.gi != null && game.gi.name.contains("jiefengBOSStiaozhanditu")){
				player.sendError(Translate.此副本不能回城);
				return false;
			}
			if(game.gi != null && DownCityManager2.instance.inSingleCityGame(game.gi.name)){
				player.sendError(Translate.此副本不能回城);
				return false;
			}
			if(game.gi != null && game.gi.name.contains("bingfenghuanjing")){
				player.sendError(Translate.此副本不能回城);
				return false;
			}
			if(game.gi != null && game.gi.name.contains("zhanmotianyu")){
				player.sendError(Translate.此副本不能回城);
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		if (!super.use(game, player, ae)) {
			return false;
		}
		// 0 1 2 3 4 混元至圣令 兄弟令 比翼令 族长令 宗主令 回城
		if(zhaoJiLingType != 回城){
			try{
//				if(JJCManager.isJJCBattle(player.getCurrentGame().gi.name)){
//					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.不能前往混元至圣);
//					player.addMessageToRightBag(hreq);
//					return false;
//				}
				if(Arrays.asList(RobberyConstant.限制使用拉令的仙界地图集).contains(player.getCurrentGame().gi.name)){
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.此地图不可使用特殊传送);
					player.addMessageToRightBag(hreq);
					return false;
				}
				String result = GlobalTool.isLimitAllTrans(player);
				if (result != null) {
					player.sendError(result);
					return false;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if (zhaoJiLingType == 混元至圣令) {
			CountryManager cm = CountryManager.getInstance();
			cm.召集令申请(player);
		} else if (zhaoJiLingType == 族长令) {
			return JiazuSubSystem.getInstance().zuzhangCallin(player);
		} else if (zhaoJiLingType == 兄弟令) {
			UniteManager um = UniteManager.getInstance();
			return um.uniteCall(player, ae.getArticleName());
		} else if (zhaoJiLingType == 比翼令) {
			return MarriageManager.getInstance().使用比翼令(player);
		} else if (zhaoJiLingType == 回城) {
			if (transferGame != null) {
				String tName = transferGame;
				if(getName_stat().contains("初心传送符")){
					tName = TransportData.getXinShouCityMap(player.getCountry());
				}else if(getName_stat().contains("主城传送符")){
					tName = TransportData.getMainCityMap(player.getCountry());
				}
				
				GameManager gm = GameManager.getInstance();
				Game g = gm.getGameByName(tName, player.getCountry());
				if (g == null) {
					g = gm.getGameByName(tName, CountryManager.中立);
				}
				if (g == null) {
					return false;
				}

				MapArea area = null;

				if (g.gi != null) {
					area = g.gi.getMapAreaByName(Translate.出生点);
				}
				Game.logger.warn("[传送符传送] [符:"+getName()+"] [gameCountry:"+g.country+"] [playerCount:"+player.getCountry()+"] [tName:"+tName+"] [area:"+area+"] ["+player.getLogString()+"]");
				if (area != null) {
					player.setTransferGameCountry(player.getCountry());
					player.initJiazuTitleAndIcon();
					TransportData transportData = new TransportData(0, 0, 0, 0, tName, (int) (area.getX() + Math.random() * area.getWidth()), (int) (area.getY() + Math.random() * area.getHeight()));
					game.transferGame(player, transportData, true);
					DevilSquareManager.instance.notifyUseTransProp(player);
					try {
						FairyChallengeManager.getInst().notifyPlayerUseTransProp(player);
					} catch (Exception e) {
						DevilSquareManager.logger.error("[仙尊挑战] [通知玩家使用回城复活出异常] [使用回城] [" + player.getLogString() + "]", e);
					}
					return true;
				}

			}
			return false;
		} else if (zhaoJiLingType == 宗主令) {
			return ZongPaiManager.getInstance().使用宗主令(player, ae.getArticleName());
		} else if (zhaoJiLingType == 回仙府) {
			Cave cave = FaeryManager.getInstance().getCave(player);
			if (cave != null) {// 仙府存在 且 未封印
				Faery faery = cave.getFaery();
				if (faery == null) {
					player.sendError("仙府数据异常~code:1001");
					return false;
				}
				Game targetGame = faery.getGame();
				if (targetGame == null) {
					player.sendError("仙府数据异常~code:1002");
					return false;
				}

				if (player.isFlying()) {
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(60);
					mw.setDescriptionInUUB(Translate.飞行坐骑不能进仙府);
					Option_Enter_Cave_Confirm confirm = new Option_Enter_Cave_Confirm(cave, this.getName());
					confirm.setText(Translate.确定);
					Option_Cancel cancel = new Option_Cancel();
					cancel.setText(Translate.取消);
					Option[] options = new Option[] { confirm, cancel };
					mw.setOptions(options);
					player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
					return false;
				} else {
					player.markLastGameInfo();
					if (FaeryManager.logger.isWarnEnabled()) {
						FaeryManager.logger.warn(player.getLogString() + "[是否在仙府地图" + player.isInCave() + "] [是否封印:" + (FaeryManager.getInstance().getKhatamMap().containsKey(player.getId())) + "] [要进入自己家园] [mapResName:{}] [mapName:{}]", new Object[] { cave.getFaery().getGameName(), cave.getFaery().getMemoryMapName() });
					}
					game.playerLeaveGame(player);
					CAVE_QUERY_SELFFAERY_RES res = new CAVE_QUERY_SELFFAERY_RES(GameMessageFactory.nextSequnceNum(), cave.getFaery().getGameName(), cave.getFaery().getMemoryMapName());
					player.addMessageToRightBag(res);
					return true;
				}
			} else {
				// 被封印或者没有仙府,则弹出寻路框.引导寻路
				if (player.getCurrentGameCountry() != 0 && player.getCurrentGameCountry() != player.getCountry()) {
					player.sendError(Translate.只能在自己国家使用);
					return false;
				}
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.取消);
				String text = "";
				String uub = "";
				if (FaeryManager.getInstance().getKhatamMap().containsKey(player.getId())) {// 封印
					text = Translate.去解封仙府;
					uub = Translate.解封仙府提示;
					if (FaeryManager.getInstance().getKhatamMap().containsKey(player.getId())) {
						// 在仙府地图,且被封印
						player.sendError(Translate.仙府被封印 + "," + Translate.translateString(Translate.不能使用道具, new String[][] { { Translate.STRING_1, getName() } }));
						return false;
					}
				} else {
					text = Translate.去创建仙府;
					uub = Translate.创建仙府提示;
				}
				mw.setDescriptionInUUB(uub);
				Option_Find_Way find_Way = new Option_Find_Way(4078, 2368, "kunlunshengdian");
				find_Way.setText(text);
				find_Way.setColor(0xff0000);
				Option[] options = new Option[] { find_Way, cancel };
				mw.setOptions(options);
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, options);
				player.addMessageToRightBag(res);
				return false;
			}
		}
		return false;
	}

	/**
	 * 判断某个玩家是否可以使用此物品 子类可以重载此方法
	 * 返回null表示可以使用
	 * 返回字符串表示不能使用
	 * 字符串为不能使用的详细信息
	 * @param p
	 * @return
	 */
	public String canUse(Player p) {

		String resultStr = super.canUse(p);

		if (zhaoJiLingType != 回城) {
			if (p.getCurrentGame() != null && p.getCurrentGame().gi != null && PetDaoManager.getInstance().isPetDao(p.getCurrentGame().gi.name)) {
				return Translate.圣兽阁里不能使用 + name;
			}
			if (SealManager.getInstance().isSealDownCity(p.getCurrentGame().gi.name)) {
				if (name.equals(Translate.王城传送符) || name.equals(Translate.古城传送符) || name.equals(Translate.天虹传送符) || name.equals(Translate.仙府传送符)) {

				} else {
					return Translate.封印副本不能使用 + name;
				}
			}
			if (!DoomsdayManager.getInstance().isGameCanUseZhaoJi(p.getCurrentGame().gi.name)) {
				return Translate.translateString(Translate.本地图禁止使用这个道具, new String[][] { { Translate.STRING_1, name } });
			}
			DownCityManager dcm = DownCityManager.getInstance();
			if (dcm != null && dcm.isDownCityByName(p.getGame())) {
				return Translate.副本里不能使用 + name;
			}
			if (p.getCurrentGame() != null && p.getCurrentGame().gi.禁止使用召唤玩家道具) {
				return Translate.translateString(Translate.本地图禁止使用这个道具, new String[][] { { Translate.STRING_1, name } });
			}
			if (QianCengTaManager.getInstance().isInQianCengTa(p)) {
				return Translate.translateString(Translate.本地图禁止使用这个道具, new String[][] { { Translate.STRING_1, name } });
			}
			if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(p.getId())) {
				return Translate.渡劫中不允许使用此道具;
			}

			if (DevilSquareManager.instance.isPlayerInDevilSquare(p)) {
				return Translate.恶魔广场中不允许使用此道具;
			}
			String vr = GlobalTool.verifyMapTrans(p.getId());
			if (vr != null) {
				return vr;
			}
			if (!this.countryLimit) {
				return resultStr;
			}

			try{
//				if(JJCManager.isJJCBattle(p.getCurrentGame().gi.name)){
//					return Translate.战场中不能使用混元至圣;
//				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(WolfManager.getInstance().isWolfGame(p)){
					return Translate.副本中不可使用回城;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(DiceManager.getInstance().isDiceGame(p)){
					return Translate.副本中不可使用回城;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			if (!p.isIsGuozhan()) {
				if (p.getCurrentGame() != null && p.getCurrentGame().country != p.getCountry()) {
					return Translate.非国战状态不能在国外使用;
				}
			}

		} 
		try {
			if(XianLingChallengeManager.getInstance().findXLChallenge(p)!=null){
				return Translate.副本里不能使用;
			}
		} catch (Exception e) {
			if(XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [副本中不可使用传送符] [异常]", e);
		}

		return resultStr;
	}

	public byte getZhaoJiLingType() {
		return zhaoJiLingType;
	}

	public void setZhaoJiLingType(byte zhaoJiLingType) {
		this.zhaoJiLingType = zhaoJiLingType;
	}

	public String getTransferGame() {
		return transferGame;
	}

	public void setTransferGame(String transferGame) {
		this.transferGame = transferGame;
	}

	public boolean isCountryLimit() {
		return countryLimit;
	}

	public void setCountryLimit(boolean countryLimit) {
		this.countryLimit = countryLimit;
	}

}
