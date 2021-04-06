package com.fy.engineserver.activity.TransitRobbery.Robbery;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.AwardModel;
import com.fy.engineserver.activity.TransitRobbery.model.CleConditionModel;
import com.fy.engineserver.activity.TransitRobbery.model.EachLevelDetal;
import com.fy.engineserver.activity.TransitRobbery.model.RayRobberyDamage;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyDataModel;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.transitrobbery.Option_HuiCheng;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.ENTER_ROBBERT_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.message.NOTIFY_CLIENT_ROBBERY_CLIENT_REQ;
import com.fy.engineserver.message.NOTIFY_CLIENT_VICTORY_TIPS_REQ;
import com.fy.engineserver.message.NOTIFY_RAYROBBERY_REQ;
import com.fy.engineserver.message.NOTIFY_ROBBERY_COUNTDOWN_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.CountdownAgent;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

/**
 * 所有劫基类
 *
 */
public abstract class BaseRobbery {
	public static Logger log = TransitRobberyManager.logger;
	/** 当前正在渡第几重天劫 */
	public int currentLevel;
	public Random ran = new Random(System.currentTimeMillis());
	
	public int threadIndex;
	
	/** 渡劫者 */
	public long playerId;
	/** 渡劫结果 --默认true  改为false就证明玩家已经败了*/
	public boolean isSucceed = true;
	/** 渡劫前准备已就绪 */
	public boolean isReady = false;
	/** 渡劫的专用场景 */
	public Game game;
	/** 初始数据model */
	public RobberyDataModel rdm;
	/** 奖励model */
	public AwardModel am;
	/** 雷劫model */
	public RayRobberyDamage rayModel;
	/** 上次小关开始时间 */
	public long laseLevelTime;
	/** 需要通过小关的关卡数 */
	public int levelCount;
	/** 每次天劫胜利条件----再次之前需要加入雷劫（单独结算） */
	public CleConditionModel cm;
	/** 渡劫失败时间 */
	private long failTime = 0;				//金全最新需求，
	/** 具体天劫处理 */
	public abstract void handlRobbery();
	public long passCountDownTime = 0;
	public boolean isStartFlag = false;
	public boolean isAwardFlag = false;
	public int tempI = 0;
	
	public BaseRobbery(int currentLevel, Player player){
		log.info("初始化雷劫");
		this.currentLevel = currentLevel;
		this.playerId = player.getId();
		TransitRobberyManager m = TransitRobberyManager.getInstance();
		rdm = m.getRobberyDataModel(currentLevel);
		am = m.getAwardModelByLev(currentLevel);
		rayModel = m.getRayRobberyDamage(currentLevel);
		levelCount = m.getCleConditionMap().size();	
		cm = m.getCleConditionMap().get(currentLevel);
	}
	/**
	 * 一小关结束通知客户端弹窗
	 */
	public void oneRobberyUnitEnd(int lev){
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			if(isAwardFlag || game == null || player.getCurrentGame() == null || !game.gi.name.equals(player.getCurrentGame().gi.name)){
				return;
			}
			EachLevelDetal detail = cm.getLevelDetails().get(lev);
			game.removeAllMonster();
			if(detail == null){
				ENTER_ROBBERT_RES req = new ENTER_ROBBERT_RES(GameMessageFactory.nextSequnceNum(), currentLevel, (byte) 1, "每一小关通过的提示内容！");
				player.addMessageToRightBag(req);
				return;
			}
			ENTER_ROBBERT_RES req = new ENTER_ROBBERT_RES(GameMessageFactory.nextSequnceNum(), currentLevel, (byte) 1, detail.getTips());
			player.addMessageToRightBag(req);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void playRayAct(){
		Player player;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
			ParticleData[] particleDatas = new ParticleData[1];
			particleDatas[0] = new ParticleData(player.getId(), "渡劫/天雷", -1, 2, 1, 1);
			NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
			player.addMessageToRightBag(resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void popBackTownWindow(Player player){
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		String msg = rdm.getTips3();
		mw.setDescriptionInUUB(msg);
		Option_HuiCheng option1 = new Option_HuiCheng();
		option1.setRobbery(this);
		option1.setText(RobberyConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(RobberyConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
	}
	
	
	/**
	 * 奖励活惩罚逻辑相同，调用这个就可以了
	 */
	public void afterRobbery() {			//更新transitrobberyentitymanager中正在渡劫的玩家列表
		// TODO Auto-generated method stub
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.error("[渡劫结束时取玩家信息错误][ " + playerId + "]");
			return;
		}
		if(isAwardFlag){
			return;
		}
		int pasLayer = getPassLayer();
		byte result = (byte) (isSucceed ? 1 : 0);
		if( isSucceed){
			notifyActRay(false);
			isAwardFlag = true;
			NOTIFY_CLIENT_ROBBERY_CLIENT_REQ req2 = new NOTIFY_CLIENT_ROBBERY_CLIENT_REQ(GameMessageFactory.nextSequnceNum());
			player.addMessageToRightBag(req2);
			passCountDownTime = System.currentTimeMillis();
			String[] awardList = rdm.getAward().split(",");
			AwardModel am = null;
			for(int i=0; i<awardList.length; i++){
				am = TransitRobberyManager.getInstance().getAwardModelByLev(Integer.parseInt(awardList[i]));
				if(am.getAwardType() == 2){				//只处理物品奖励,其他奖励具体地方做判断
					for(int j=0; j<am.getAmount(); j++){
						addAward2Bag(am.getAwardItem(), player);
						player.sendError(Translate.text_boothsale_010 + ":" + am.getAwardItem());
					}
				} else if(am.getAwardType() == 1){		//大师技能
					player.sendError(am.getAwardItem());
				} else if(am.getAwardType() == 3){
					player.sendError(Translate.translateString(Translate.心法学习上限提高, new String[][] {{Translate.STRING_1, am.getAmount()+""}}));
				}
			}
			pasLayer += 300;
			game.removeAllMonster();
			TransitRobberyEntityManager.getInstance().updateTransitRobberyEntity(playerId, RobberyConstant.UPDATE_DOBBERY_LEVEL, -1);
			TransitRobberyEntityManager.getInstance().updateTransitRobberyEntity(playerId, RobberyConstant.UPDATE_END_TIME, 0);
//			sendRayNotify2Client(60, "距离强制传出剩余时间", (byte) 2);
			sendRayNotice2Client(60, Translate.渡劫胜利强制出场景剩余时间);			//此提示内容也没用到，先改了。
			popBackTownWindow(player);
			TransitRobberyManager.getInstance().sendWordMsg(player, rdm.getRobberyName());
			PlatformManager pm = PlatformManager.getInstance();
			String titleName = Translate.渡劫称号+currentLevel;
			Platform p = null;
			try {
				p = getPlatForm("taiwan");
				if(pm != null && pm.isPlatformOf(p)) {
					titleName = "渡劫稱號"+currentLevel;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("[获取平台错误][" + player.getLogString() + "] ", e);
			}
			boolean notitle = false;
			for (int i=0; i<RobberyConstant.noTitle.length; i++) {
				if (RobberyConstant.noTitle[i] == currentLevel) {
					notitle = true;
					break;
				}
			}
			if (!notitle) {
				PlayerTitlesManager.getInstance().addTitle(player, titleName, true);
			}
		} else {
			long cutt = System.currentTimeMillis();
//			pasLayer -= 1;
			if(failTime == 0){
				failTime = cutt;
			}
			if(cutt < (failTime + RobberyConstant.延迟传出时间 * 1000)){
				return;
			}
			game.removeAllMonster();
			//给玩家添加虚弱buff---属性降低百分之十---buff里边需要写可累加逻辑。时效为24小时。。可是利用道具减少buff持续时间
			log.info("[天劫渡劫失败][" + player.getLogString() + "] [是否死亡:" + player.isDeath() + "]");
			TransitRobberyManager.getInstance().robberyFail(player);
			notifyActRay(false);
			endRobbery();
			回城复活(player);
			if(pasLayer > 0) {
				pasLayer += 200;
			}
		}
		if(pasLayer > 0) {
			TransitRobberyEntityManager.getInstance().updateTransitRobberyEntity(playerId, RobberyConstant.UPDATE_PASSLAYER, pasLayer);
		}
		NOTIFY_CLIENT_VICTORY_TIPS_REQ req = new NOTIFY_CLIENT_VICTORY_TIPS_REQ(GameMessageFactory.nextSequnceNum(), result);
		player.addMessageToRightBag(req);
	}
	
	private Platform getPlatForm(String pN) throws Exception {
		for(Platform p : Platform.values()) {
			if(p.getPlatformName().equals(pN)) {
				return p;
			}
		}
		throw new Exception("配置了错误的平台名{}" + pN);
	}
	/**
	 * 计算通关的层数
	 * @return
	 */
	public abstract int getPassLayer();
	
	public void 回城复活(Player player){
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate buffTemplate = btm.getBuffTemplateByName(Translate.无敌buff);
		if(buffTemplate != null) {
			Buff buff1 = buffTemplate.createBuff(1);
			if (buff1 != null) {
				buff1.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 2000);
				player.placeBuff(buff1);
				player.是否回城复活 = true;
			}
		}
		player.setHp(player.getMaxHP() / 2);
		player.setMp(player.getMaxMP() / 2);
		player.setState(Player.STATE_STAND);
		player.notifyRevived();
		// 对player的囚禁操作，用buff形式表示，此buff累计还是已经囚禁的囚犯不让再次囚禁
		List<Buff> buffs = player.getAllBuffs();
		if (buffs != null) {
			for (Buff buff : buffs) {
				if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName())) {
					return;
				}
			}
		}
		PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte)0, "", player.getMaxHP() / 2, player.getMaxMP() / 2);
		player.addMessageToRightBag(res);
	}
	
	public void checkCountDownTime(){
		try {
			Player p = GamePlayerManager.getInstance().getPlayer(playerId);
			if(System.currentTimeMillis() >= (passCountDownTime + 60 * 1000) || !p.isOnline()/* || p.isdeath()*/) {
				endRobbery();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("[渡劫倒计时取玩家信息错误][ " + playerId + "]");
		}
	}
	public void endRobbery(){
		endRobbery(true);
	}
	
	/**
	 * 结束
	 */
	public void endRobbery(boolean flag){
		TransitRobberyEntityManager.getInstance().removeFromRobbering(playerId);
		TransitRobberyManager.getInstance().removeRobbery(this);			//渡劫完成直接剔除心跳
		Player p = null;
		try {
			p = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("[渡劫结束取player错误][ " + playerId + "]");
		}
		if(flag) {
			回城(p);
		}
	}
	
	private void addAward2Bag(String itemName, Player player){
		log.info("渡劫成功，奖励物品=" + itemName + "   " + playerId);
		Article article = ArticleManager.getInstance().getArticle(itemName);
		if(article == null){
			log.error("[渡劫][获得奖励失败，物品表里没有此物品][" + playerId + "][物品名=" + itemName + "]");
			return;
		}
		ArticleEntity awardAe = null;
		try {
			awardAe = ArticleEntityManager.getInstance().
					createEntity(article, false, ArticleEntityManager.DUJIE_CHENGGONG, player, article.getColorType(), 1, false);
		} catch (Exception e) {
			log.error("create award error", e);
			return;
		}
		
		Knapsack bag = player.getKnapsack_common();
		if(bag == null){
			log.error("取得的玩家背包是null {}", playerId);
			return;
		}
		bag.put(awardAe, "渡劫成功");
	}
	
	private void 回城(Player p){
		try{
			Game currentGame = p.getCurrentGame();
			if(currentGame != null && !currentGame.gi.name.equals(game.gi.name)){
				log.info("[玩家已经不再这个区域了，不需要在回程了]");
				return;
			}
			String mapName = p.getResurrectionMapName();
			int x = p.getResurrectionX();
			int y = p.getResurrectionY();
			if(!p.isOnline()){
				mapName = "kunhuagucheng";
			}
			game.transferGame(p, new TransportData(0, 0, 0, 0, mapName, x, y), true);
			log.warn("[渡劫结束] [回城] [成功] ["+p.getLogString()+"]");
		}catch(Exception e){
			log.warn("[渡劫结束] [回城] [异常]"+e.getMessage());
		}
	}
	
	public int initX = 0;
	public int initY = 0;
	
	public void beforeRobbery() {
		// TODO Auto-generated method stub
		//准备工作。。正常完成后将isReady设置为true
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			Game currentGame = player.getCurrentGame();
			if(currentGame == null){
				log.info("[天劫][当前游戏为空]");
				return ;
			}
			String mapName = rdm.getSceneName();
			game = TransitRobberyManager.getInstance().createNewGame(player, mapName);
			String[] point = rdm.getInitPoint().split(",");
			TransitRobberyManager.getInstance().addRobbery(this);			//成功进入渡劫加入心跳管理
			TransitRobberyEntityManager.getInstance().add2Robbering(playerId);		//正在渡劫状态
			currentGame.transferGame(player, new TransportData(0, 0, 0, 0, mapName, Integer.parseInt(point[0]), Integer.parseInt(point[1])), true);
			initX = Integer.parseInt(point[0]);
			initY = Integer.parseInt(point[1]);
			isReady = true;
			NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
			resp.setCountdownType(CountdownAgent.COUNT_TYPE_DUJIE);
			resp.setLeftTime(1);
			resp.setDescription(Translate.天劫拉人倒计时);
			player.addMessageToRightBag(resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("[渡劫][异常][e:" + e.getMessage() + "]");
			TransitRobberyManager.getInstance().removeRobbery(this);
			TransitRobberyEntityManager.getInstance().removeFromRobbering(playerId);
		}
	}
	
	public long startFlagTime = 0;
	/**
	 * 开始
	 */
	public void start(){
		if(!isStartFlag){
			startFlagTime = System.currentTimeMillis();
//			sendRayNotice2Client(5,"距离开始还有:");
			sendRayNotify2Client(5, "", (byte) 3);
			laseLevelTime = System.currentTimeMillis();
			log.info("开始渡劫 +  "  + playerId);
		} else{
			log.error("渡劫出异常了，已经开始又开始了=" + playerId);
		}
	}
	
	/**
	 * 渡劫开始只需要调用此方法
	 */
	public void handlBaseRobbery(){
		this.beforeRobbery();
		TransitRobberyEntityManager.getInstance().updateTransitRobberyEntity(playerId, RobberyConstant.UPDATE_FORCE_PULL_TIME, -1);
		if(isReady){
			TransitRobberyEntityManager.getInstance().updateTransitRobberyEntity(playerId, RobberyConstant.UPDATE_START_TIME, 0);//更新开始时间
		} else {
			TransitRobberyManager.getInstance().removeRobbery(this);
			TransitRobberyEntityManager.getInstance().removeFromRobbering(playerId);
			log.error("[渡劫][传入场景失败]");
		}
	}
	/**
	 * 一次雷劫结束后通知客户端进入下次的倒计时
	 * @param countDownTime
	 * @param contentMass
	 */
//	public void sendRayNotify2Client(int countDownTime, String contentMass){
//		if(countDownTime <= 0){
//			return;
//		}
//		try {
//			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
//			NOTIFY_ROBBERY_COUNTDOWN_REQ resp = new NOTIFY_ROBBERY_COUNTDOWN_REQ();
//			resp.setCountType(RobberyConstant.COUNTDOWN_IN_ROBBERY);
//			resp.setLeftTime(countDownTime);
//			resp.setContentmass(contentMass);
//			player.addMessageToRightBag(resp);
//			log.info("[渡劫]["+contentMass+"][倒计时时间=" + countDownTime + "][" + playerId + "]");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			log.error("[渡劫结束时取玩家信息错误][ " + playerId + "][" + e.getMessage() + "]");
//		}
//	}
	/**
	 * 雷倒计时.
	 * 
	 * @param countDownTime
	 * @param contentMass
	 */
	public void sendRayNotice2Client(int countDownTime, String contentMass){
		if(countDownTime <= 0){
			return;
		}
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
			resp.setCountdownType((byte)4);
			resp.setLeftTime(countDownTime);
			resp.setDescription(contentMass);
			player.addMessageToRightBag(resp);
			log.info("[渡劫]["+contentMass+"][倒计时时间=" + countDownTime + "][" + playerId + "]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("[渡劫结束时取玩家信息错误][ " + playerId + "][" + e.getMessage() + "]");
		}
	}
	/**
	 * 一次雷劫结束后通知客户端进入下次的倒计时
	 * @param countDownTime
	 * @param contentMass		此值客户端没有用到，使用艺术字体，根据type判断
	 * @param type   
	 */
	public void sendRayNotify2Client(int countDownTime, String contentMass, byte type){
		if(countDownTime <= 0){
			return;
		}
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			NOTIFY_ROBBERY_COUNTDOWN_REQ resp = new NOTIFY_ROBBERY_COUNTDOWN_REQ();
			resp.setCountType(type);
			resp.setLeftTime(countDownTime);
			resp.setContentmass(contentMass);			//没有用到，客户端使用艺术字体
			player.addMessageToRightBag(resp);
			log.info("[渡劫]["+contentMass+"][倒计时时间=" + countDownTime + "][" + playerId + "]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 天劫心跳-天劫需要在自己心跳前调用此方法 */
	public void heartBeat(){		
		if(startFlagTime != 0 && System.currentTimeMillis() >= (startFlagTime + 5000)){
			startFlagTime = 0;
			isStartFlag = true;
		}
		if(!isReady){
			return;
		}
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			if(!isSucceed || !player.isOnline() || player.isDeath()/* || !game.isPlayerInGame(player)*/){			//失败-玩家下线、死亡、出场景
				isSucceed = false;
				afterRobbery();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 通过天劫重数和小关level获得通关条件做判定
	 * @param level
	 * @return	
	 */
	public boolean check4eachLevel(int level, boolean isOnlyTime){
		
		if(currentLevel == 4 && level >= 3){
			return check4eachLevel(level, isOnlyTime, true);
		}
		if(currentLevel == 6 && level >= 4){
			return check4eachLevel(level, isOnlyTime, true);
		}
		if(currentLevel == 7 && level == 7){
			return check4eachLevel(level, true, true);
		}
		EachLevelDetal detail = cm.getLevelDetails().get(level);		//通关条件			、、怎么判断杀死怪物》？
		String[] temp = detail.getvCondition().split(",");
		int monsterId = Integer.parseInt(temp[0]);
		int amount = Integer.parseInt(temp[1]);
		if(monsterId == -999){
			amount = level;				
		}
		if(isOnlyTime){				//对于只有时间限制的，只要到时间就判定玩家胜利---需要清除原boss
			int time = (int) (System.currentTimeMillis() - (detail.getDuration() * 1000 + laseLevelTime));
			if(time >= 0){		
				return true;
			}
//			sendRayNotice2Client(time/1000, "击杀boss倒计时:");
		}
		if(TransitRobberyEntityManager.getInstance().isKilledMoster(playerId, monsterId, amount)){
//			TransitRobberyEntityManager.getInstance().clearKilledList(playerId);
			return true;
		}
		if(detail.getDuration() > 0){				//等于或者小于0代表没有时间限制
			if(System.currentTimeMillis() > (detail.getDuration() * 1000 + laseLevelTime)){		//操作超过规定时间直接判定失败
				isSucceed = false;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param level
	 * @param isOnlyTime
	 * @param timeOUt			ture代表到时间算败
	 * @return
	 */
	public boolean check4eachLevel(int level, boolean isOnlyTime, boolean timeOUt){
		EachLevelDetal detail = cm.getLevelDetails().get(level);		//通关条件			、、怎么判断杀死怪物》？
		String[] temp = detail.getvCondition().split(",");
		int monsterId = Integer.parseInt(temp[0]);
		int amount = Integer.parseInt(temp[1]);
		if(monsterId == -999){
			amount = level;				
		}
		if(isOnlyTime){				//对于只有时间限制的，只要到时间就判定玩家胜利---需要清除原boss
			int time = (int) (System.currentTimeMillis() - (detail.getDuration() * 1000 + laseLevelTime));
			if(time >= 0 && timeOUt){	
				isSucceed = false;
				return false;
			}
		}
		if(TransitRobberyEntityManager.getInstance().isKilledMoster(playerId, monsterId, amount)){
			return true;
		}
		if(detail.getDuration() > 0){				//等于或者小于0代表没有时间限制
			if(System.currentTimeMillis() > (detail.getDuration() * 1000 + laseLevelTime)){		//操作超过规定时间直接判定失败
				isSucceed = false;
			}
		}
		return false;
	}
	
	/**
	 * 通知客户端准备霹雷
	 * @param isAct  true为通知开启  false通知关闭
	 */
	public void notifyActRay(boolean isAct){
		byte result;
		if(isAct){
			result = 0;
		} else {
			result = 1;
		}
		NOTIFY_RAYROBBERY_REQ req = new NOTIFY_RAYROBBERY_REQ(GameMessageFactory.nextSequnceNum(), result);
		log.info(playerId + "  发送雷云消失协议给客户端" + isAct);
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			player.addMessageToRightBag(req);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("[通知客户端刷出雷云错误][playerId=" + playerId + "][" + e.getMessage() + "]");
		}
		
	}
	
	public boolean checkStartTime(int lev, long lastTime){
		if(lev >= cm.getLevelDetails().size()){
			return true;
		}
		if(System.currentTimeMillis() < (lastTime + cm.getLevelDetails().get(lev).getInterval() * 1000)){		//根据配表时间
			return false;
		}
		return true;
	}
	/**
	 * 使用道具屏蔽所有散仙
	 * @return
	 */
	public synchronized boolean killAllImmortal(int count){
		String[] immorIds = rdm.getImmortalId().split(",");
		int immorId;
//		boolean exist = false;
		int killCount = 0;
		int tempCount = count;
		for(int i=0; i<immorIds.length; i++){
			immorId = Integer.parseInt(immorIds[i]);
//			if(exist == true){
//				game.removeAllMonsterById(immorId);
//				continue;
//			}
			killCount += game.removeAllMonsterById(immorId, tempCount);	
			tempCount = count - killCount;
			if(tempCount <= 0) {
				break;
			}
//			exist = game.removeAllMonsterById(immorId);
		}
		return killCount > 0;
	}
	
	public abstract int[] getRanPoint();
	
	/**
	 * 判定并刷出散仙
	 * @param ran
	 * @param probability
	 */
	public void actImmortal(Random ran, int probability, int maxCount){
		if(TransitRobberyEntityManager.getInstance().护身罡气生效(playerId)){
			log.info("[渡劫][散仙没刷出来][玩家使用过屏蔽散仙道具][" + playerId + "]");
			return;
		}
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		String[] immorIds = rdm.getImmortalId().split(",");
		int immorId ;
		int ranInt = ran.nextInt(RobberyConstant.HUNDRED);
		if(ranInt <= probability){
			//刷出散仙
			int amount = ran.nextInt(maxCount) + 1;
			log.info("[刷出散仙]["+playerId+"][amount=" + amount + "]");
			int[] a ;
			for(int i=0; i<amount; i++){
				if(immorIds != null){
					immorId = Integer.parseInt(immorIds[ran.nextInt(immorIds.length)]);
				} else {
					log.error("散仙id配置错误");
					return;
				}
				a = getRanPoint();
				TransitRobberyManager.getInstance().refreshMonster(game, immorId, player, tempI++, a[0], a[1]);	//劫兽和散仙出现坐标后读配表
			}
		}
	}
	public void actBeast(Random ran, int probability, int maxCount){
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		int ranInt = ran.nextInt(RobberyConstant.HUNDRED);
		String[] beastIds = rdm.getBeastId().split(",");
		int beastId ;
		if(ranInt <= probability){
			int amount = ran.nextInt(maxCount) + 1;
			log.info("[刷出劫兽]["+playerId+"][amount=" + amount + "]");
			//刷出劫兽
			int[] a ;
			for(int i=0; i<amount; i++){
				if(beastIds != null){
					beastId = Integer.parseInt(beastIds[ran.nextInt(beastIds.length)]);
				} else {
					log.error("劫兽id配置错误");
					return;
				}
				a = getRanPoint();
				TransitRobberyManager.getInstance().refreshMonster(game, beastId, player, tempI++, a[0],a[1]);
			}
		}
	}
}
