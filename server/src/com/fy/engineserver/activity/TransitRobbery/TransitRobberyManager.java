package com.fy.engineserver.activity.TransitRobbery;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.TransitRobbery.Robbery.BaseRobbery;
import com.fy.engineserver.activity.TransitRobbery.Robbery.LiuYuRobbery;
import com.fy.engineserver.activity.TransitRobbery.Robbery.PhantomRobbery;
import com.fy.engineserver.activity.TransitRobbery.Robbery.QiQingRobbery;
import com.fy.engineserver.activity.TransitRobbery.Robbery.RayRobbery;
import com.fy.engineserver.activity.TransitRobbery.Robbery.ShenHunRobbery;
import com.fy.engineserver.activity.TransitRobbery.Robbery.SiXiangRobbery;
import com.fy.engineserver.activity.TransitRobbery.Robbery.WuXiangRobbery;
import com.fy.engineserver.activity.TransitRobbery.Robbery.XinMoRobbery;
import com.fy.engineserver.activity.TransitRobbery.Robbery.YihunRobbery;
import com.fy.engineserver.activity.TransitRobbery.model.AwardModel;
import com.fy.engineserver.activity.TransitRobbery.model.CleConditionModel;
import com.fy.engineserver.activity.TransitRobbery.model.EachLevelDetal;
import com.fy.engineserver.activity.TransitRobbery.model.RayRobberyDamage;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyDataModel;
import com.fy.engineserver.activity.TransitRobbery.model.TempGuanliAvata;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.Avata;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.Buff_GuaiWuShangHai;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventPlayerLogin;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.transitrobbery.Option_Confirm_guanli;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.ENTER_ROBBERT_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.message.NOTIFY_FEISHENG_DONGHUA_REQ;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.CountdownAgent;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class TransitRobberyManager implements EventProc{
	public static Logger logger = LoggerFactory.getLogger(TransitRobberyManager.class);
	private static TransitRobberyManager instance;
	/** 渡劫基础表 */
	private Map<Integer, RobberyDataModel> robberyDataModel;
	/** 奖励列表 */
	private Map<Integer, AwardModel> awardModel;
	/** 没劫雷击伤害表 */
	private Map<Integer, RayRobberyDamage> rayDamageMap;
	/** 胜利条件map */
	private Map<Integer, CleConditionModel> cleConditionMap;
	/** 变化avata的时间*/
	private Map<Long, Long> playerChanageAvataTimes;
	public static long CHANAGE_AVATA_CD = 60*1000;				//CD时间
	private static int robberyThreadAmount = 5;					//线程开启数
	/**  天劫心跳线程 */
	private RobberyThread[] robberyThreads = new RobberyThread[robberyThreadAmount];
	/** 上一次天劫加入线程id */
	private volatile static int lastAddedThread = 0;
	/** 神魂劫bossId */
	public static int SHENHUN_BOSSID = 0;
	/** 无相劫bossId */
	public static int WUXIANG_BOSSID = 900027;
	/** 幻象劫bossId */
	public static int HUANXIANG_BOSSID = 0;
	
	public static int openLevel = 110;
	
//	public static String feishengjilu = "";
//	public static String[] avatar;
//	public static byte[] avatarType;
	
	public Map<Long, TempGuanliAvata> tempFeishen = new Hashtable<Long, TempGuanliAvata>();
	/** 领取飞升经验奖励记录 */
	public Map<Long, Set<Long>> awardExpMap = null;
	public Map<Long, Integer> awardAmount = new ConcurrentHashMap<Long, Integer>();		//因观礼领取经验次数记录
	
	private String initFileName;
	
	private String diskCachName;
	
	public static final String key = "transit_guanli";
	/**
	 * 飞升观礼加经验心跳观礼
	 */
	private FeiShengExpThread addExpThread = new FeiShengExpThread(); 
	
	public DiskCache disk = null;
	
	
	public void initDisk() throws IOException {
		File file = new File(diskCachName);
		if (!file.exists()) {
			file.createNewFile();
		}
		disk = new DefaultDiskCache(file, null, "fairybuddha", 100L * 365 * 24 * 3600 * 1000L);
		awardAmount = (Map<Long, Integer>) disk.get(key);
		if (awardAmount == null) {
			awardAmount = new ConcurrentHashMap<Long, Integer>(); 
		}
	}
	
	/**
	 * 观礼---加入心跳线程
	 * @param playerId
	 */
	public void confirmGuanLi(long playerId){
		synchronized (awardAmount) {
			if(awardAmount.get(playerId) == null) {
				awardAmount.put(playerId, 1);
			}
			int tempCount = awardAmount.get(playerId);
			if(tempCount <= 3) {
				tempCount++;
				awardAmount.put(playerId, tempCount);
				disk.put(key, (Serializable) awardAmount);
				synchronized (addExpThread.addExpMap) {
					addExpThread.addExpMap.put(playerId, System.currentTimeMillis());
				}
			} else {
				logger.info("[此玩家今天已经因观礼获得过三次经验，无法继续获得经验][id=" + playerId + "]");
			}
		}
	}
	
	public boolean 是否在四相劫最后一关(Player player) {
		for(RobberyThread rt : robberyThreads) {
			for(BaseRobbery b : rt.allRobbery) {
				if(b.playerId == player.getId() && b instanceof SiXiangRobbery && b.game.hasMonster(900011)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static int[] tempIndex = new int[]{15,0,3,13};
	/**
	 * 替换仙界avata
	 * @param player
	 * @param a
	 */
	public void replacePlayerAvata(Player player, Avata a) {
		if (player.getLevel() < 221) {
			player.modifyShouAvata();
			return ;
		} 
		try {
			TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
			if (entity != null && entity.getFeisheng() == 1 && player.getAvataRace().equals(Constants.race_human)) {
				if (a.avata != null && a.avata.length > 0) {
					byte[] newAvatarType = RobberyConstant.oldavatarType;
					String[] newAvatar = RobberyConstant.tempAvatar.get(player.getCurrSoul().getCareer());
					for (int i=0;i <newAvatarType.length; i++) {
						boolean has = false;
//						int a2 = newAvatarType[i];
						int tempIndex2 = tempIndex[i];
						for (int j=0; j<a.avataType.length; j++) {
							if (a.avataType[j] == newAvatarType[i]) {
								a.avata[j] = newAvatar[i];
								has = true;
							}
						}
						if (!has && tempIndex2 < player.getEquipmentColumns().getEquipmentIds().length && player.getEquipmentColumns().get(tempIndex2) != null) {
							a.avata = Arrays.copyOf(a.avata, a.avata.length + 1);
							a.avata[a.avata.length-1] = newAvatar[i];
							a.avataType = Arrays.copyOf(a.avataType, a.avataType.length + 1);
							a.avataType[a.avataType.length-1] = newAvatarType[i];
						}
					}
					
					player.setAvataType(a.avataType);
					player.setAvata(a.avata);
					if (ResourceManager.logger.isDebugEnabled()) {
						ResourceManager.logger.debug("[替换飞升切换形象] [成功] [" + player.getLogString() + "] [" + Arrays.toString(player.getAvata()) + "]");
					}
				}
			}
		} catch (Exception e) {
			ResourceManager.logger.warn("[飞升替换仙界avata] [异常] [" + player.getLogString() + "]", e);
		} finally {
			player.modifyShouAvata();
		}
	}
	
	/**
	 * 使用道具杀死所有散仙。。杀不掉保存，下次不刷散仙
	 * @param playerId
	 * @return
	 */
	public boolean killAllImmortal(long playerId, int count){
		for(int i=0; i<robberyThreadAmount; i++){
			for(BaseRobbery b : robberyThreads[i].allRobbery){
				if(playerId == b.playerId){
					return b.killAllImmortal(count);
				}
			}
		}
		logger.info("杀死散仙不成功");
		return false;
	}
	
	public void sendWordMsg(Player player, String jieming){
		String country = CountryManager.得到国家名(player.getCountry());
		String msg =  Translate.translateString(Translate.渡劫世界通告, new String[][] {{Translate.STRING_1, country}, {Translate.STRING_2, player.getName()},{Translate.STRING_3, jieming}});
		logger.info("[渡劫][刷出喊话][" + player.getName() + "][渡劫成功奖励通告]");
		ChatMessage msgA = new ChatMessage();
		msgA.setMessageText(msg);
		try {
			ChatMessageService.getInstance().sendHintMessageToSystem(msgA);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[渡劫][刷出喊话失败][" + player.getName() + "][渡劫成功奖励]");
			e.printStackTrace();
		}
	}
	
	/**************************************************以下测试代码，，华丽分割线******************************************/
	/**
	 * 测试代码  -----------记得删掉
	 * */
	public void testFeishen(Player player){
			player.sendError(Translate.飞升成功);
			String feishengjilu = player.getCareer() + "," + player.getName() + "," + player.getCountry() + "," + player.getZongPaiName() + "," + player.getJiazuName();
			byte[] tempatype = Arrays.copyOf(player.getAvataType(),player.getAvataType().length);
			String[] tempar = Arrays.copyOf(player.getAvata(),player.getAvata().length);
			TempGuanliAvata tt = new TempGuanliAvata(player.getId(), feishengjilu, tempar, tempatype);
			TransitRobberyManager.getInstance().tempFeishen.put(player.getId(), tt);
			String[] newAvatar = null;
			byte[] newAvatarType = RobberyConstant.avatarType;
			switch (player.getCareer()) {
			case 1: 		//修罗
				newAvatar = RobberyConstant.douluoAvatar;
				break;			
			case 3: 		//仙心
				newAvatar = RobberyConstant.lingzunAvatar;
				break;			
			case 4: 		//武皇
				newAvatar = RobberyConstant.wuhuangAvatar;
				break;			
			case 2: 		//鬼杀
				newAvatar = RobberyConstant.guishaAvatar;
				break;			
			case 5:
				newAvatar = RobberyConstant.shoukuiAvatar;
				break;
			} 
			boolean existChiBang = false;
			for(byte bb : player.getAvataType()){
				if(bb == 3){
					existChiBang = true;
					break;
				}
			}
			if(!existChiBang){
				newAvatarType = ArrayUtils.remove(newAvatarType, newAvatarType.length-1);
				newAvatar = (String[]) ArrayUtils.remove(newAvatar, newAvatar.length-1);
			}
			if (player.isShouStatus()) {
				newAvatarType = new byte[]{0,13};
				newAvatar = Player.bianshenAvata4Feisheng[player.getPlayerRank()];
			}
			NOTIFY_FEISHENG_DONGHUA_REQ resp = new NOTIFY_FEISHENG_DONGHUA_REQ(GameMessageFactory.nextSequnceNum(), (byte)1, player.getCareer(), player.getName(), player.getCountry()
					,player.getZongPaiName(), player.getJiazuName(), player.getAvataType(), player.getAvata(), newAvatarType, newAvatar);
			player.addMessageToRightBag(resp);
			doPopShowFeisheng(player);
			Horse h2 = null;
			for (long horseId : player.getCurrSoul().getHorseArr()) {
				 Horse h = HorseManager.getInstance().getHorseById(horseId, player);
				 if (h != null && !h.isFly()) {
					 h2 = h;
					 break;
				 }
			}
			if (h2 != null) {
				ResourceManager.getInstance().getHorseAvataForPlayer(h2, player);
			}
	}
	private void doPopShowFeisheng(Player player){
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		TempGuanliAvata tt = TransitRobberyManager.getInstance().tempFeishen.get(player.getId());
		String[] temp = tt.feishengjilu.split(",");
		if(temp.length < 3){
			return;
		}
//		byte career = Byte.parseByte(temp[0]);
		String name = temp[1];
		String country = CountryManager.得到国家名(Integer.parseInt(temp[2]));
		String msg = Translate.translateString(Translate.飞升推送, new String[][] {{Translate.STRING_1, country}, {Translate.STRING_2, name}});
		mw.setDescriptionInUUB(msg);
		Option_Confirm_guanli option1 = new Option_Confirm_guanli();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setFeishengPlayerId(player.getId());
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		for(int i=0; i<players.length; i++){
			if(players[i].getId() == player.getId()){
				continue;
			}
			if (players[i].getLevel() > 220) {
				continue;
			}
			players[i].addMessageToRightBag(creq);
		}
	}
	/**************************************************以上测试代码，，华丽分割线******************************************/
	
	/**
	 * 判断是否可以获得飞升管理奖励
	 * @param feiShengPId
	 * @param p
	 * @return
	 */
	public synchronized boolean getFeishengExpAward(long feiShengPId, Player p){
		boolean result = false;
		if(awardExpMap == null){
			awardExpMap = new ConcurrentHashMap<Long, Set<Long>>();
		}
		if(awardExpMap.get(feiShengPId) == null){
			Set<Long> temp = new HashSet<Long>();
			awardExpMap.put(feiShengPId, temp);
		}
		Set<Long> list = awardExpMap.get(feiShengPId);
		if(!list.contains(p.getId())){
			list.add(p.getId());
			awardExpMap.put(feiShengPId, list);
			result = true;
		}
		return result;
	}
	
	public RobberyDataModel getRobberyDataModel(int key){
		return robberyDataModel.get(key);
	}
	
	public RayRobberyDamage getRayRobberyDamage(int key){
		return rayDamageMap.get(key);
	}
	
	public AwardModel getAwardModelByLev(int key){
		return awardModel.get(key);
	}
	
	public void add2CountdownHeartBeat(Long playerId, int leftTime){
		int count = lastAddedThread % robberyThreadAmount;
		if(count > robberyThreadAmount) {
			count = 0;
		}
		robberyThreads[count].forceLeftTimes.put(playerId, leftTime);
	}
	
	public void remove2CountdownHeartBeat(Long playerId){
		boolean result = false;
		for(int i=0; i<robberyThreadAmount; i++){
			if(robberyThreads[i].forceLeftTimes.containsKey(playerId)){
				synchronized (robberyThreads[i].remogeList) {
					robberyThreads[i].remogeList.add(playerId);
				}
				result = true;
				break;
			}
		}
		logger.info("[删除强制拉人心跳===" + result + "] [" + playerId + "]");
	}
	
	public Game getRobberyGame(Player p, String gamename){
		if(gamename == null){
			logger.info("[检测是否玩家在渡劫]");
			return null;
		}
		for(RobberyThread robberyThread : robberyThreads){
			for(BaseRobbery r : robberyThread.allRobbery){
				if(r.playerId == p.getId() && r.game.gi.name.equals(gamename)){
					logger.info("[存在玩家渡劫信息][返回玩家渡劫中的game]");
					return r.game;
				}
			}
		}
		return null;
	}
	
	public static int 飞升心法等级  = 90;
	
	/**
	 * 返回空表示可以飞升，否则返回不能飞升的原因
	 * @param player
	 * @return
	 */
	public String feishengPanduan(Player player){
		if (SealManager.getInstance().getSeal().getSealLevel() <= 220) {
			return Translate.服务器封印需要大于220;
		}
		if(player.getLevel() < 220){
			return Translate.本尊等级不够;
		}
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		if(entity.getFeisheng() == 1){
			return Translate.已经飞升过了;
		}
		if(entity.getCurrentLevel() < 9){
			return Translate.渡劫重数不够不够;
		} else if(player.getSoul(Soul.SOUL_TYPE_SOUL) == null ||player.getSoul(Soul.SOUL_TYPE_SOUL).getGrade() < 220) {
			return Translate.元神等级不够;
		} 
		if (player.getExp() < player.getNextLevelExpPool()) {
			return Translate.飞升需要经验;
		}
		int count = 0;
		if(player.getSkillLevel(7100) >= 飞升心法等级){
//			if(player.getSkillLevel(7200) >= 40){
			count += 1;
		}
		if(player.getSkillLevel(4100) >= 飞升心法等级){
//			if(player.getSkillLevel(4200) >= 40){
			count+=1;
		}
		if(player.getSkillLevel(3100) >= 飞升心法等级){
//			if(player.getSkillLevel(3200) >= 40){
			count+=1;
		}
		if(player.getSkillLevel(5100) >= 飞升心法等级){
//			if(player.getSkillLevel(5200) >= 40){
			count+=1;
		}
		if(player.getSkillLevel(6100) >= 飞升心法等级){
//			if(player.getSkillLevel(6200) >= 40){
			count+=1;
		}
		if(count < 4){
			return Translate.心法美满;
		}
		if(player.getClassLevel() < 11){
			return Translate.境界不够;
		}
		return null;
	}
	
	private TransitRobberyManager(){
		instance = this;
	}
	
	public static TransitRobberyManager getInstance(){
		return instance;
	}
	
	public void wuxiang_juqing(Player player){
		for(RobberyThread robberyThread : robberyThreads){
			for(BaseRobbery r : robberyThread.allRobbery){
				if(r instanceof WuXiangRobbery && r.playerId == player.getId()){
					WuXiangRobbery rb = (WuXiangRobbery) r;
					rb.juqingAct();
					return;
				}
			}
		}
	}
	
	/**
	 * 触发神魂劫剧情
	 * @param player
	 */
	public void shenhun_juqing(Player player){
		for(RobberyThread robberyThread : robberyThreads){
			for(BaseRobbery r : robberyThread.allRobbery){
				if(r instanceof ShenHunRobbery && r.playerId == player.getId()){
					ShenHunRobbery rb = (ShenHunRobbery) r;
					rb.juqingAct();
					return;
				}
			}
		}
	}
	
	private boolean isRobberyAct(BaseRobbery robbery){
		for(RobberyThread robberyThread : robberyThreads){
			for(BaseRobbery r : robberyThread.allRobbery){
				if(r == robbery){
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 将参加渡劫的人加入到线程管理中
	 * @param robbery
	 */
	public void addRobbery(BaseRobbery robbery){
		logger.info("[渡劫开始][将玩家渡劫信息存入线程管理中][" + robbery.playerId + "]");
		if(!isRobberyAct(robbery)){
			logger.info("[渡劫开始][线程中不存在此玩家渡劫信息，将玩家渡劫信息存入线程管理中][" + robbery.playerId + "]");
			lastAddedThread++;
			int temp = lastAddedThread % robberyThreadAmount;
			if(temp >= robberyThreadAmount){
				temp = 0;
			}
			robberyThreads[temp].allRobbery.add(robbery);
			robbery.threadIndex = temp;
		}
	}
	public boolean removeRobbery(BaseRobbery robbery){
		boolean result = false;
		Player player = null;
		synchronized (robberyThreads[robbery.threadIndex].allRobbery) {
			try {
				boolean ss = robberyThreads[robbery.threadIndex].allRobbery.remove(robbery);
				logger.warn("删除结果:" + ss + "[" + robbery.playerId + "] [" + robbery.threadIndex + "]");
				TransitRobberyEntityManager.getInstance().removeFromRobbering(robbery.playerId);
				player = GamePlayerManager.getInstance().getPlayer(robbery.playerId);
				player.checkAndClearRobberyBuff();
				TransitRobberyEntityManager.getInstance().护身罡气生效(player.getId());
				TransitRobberyEntityManager.getInstance().删除火神玄魄效果(player.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("[渡劫][删除玩家心跳线程成功]");
			result = true;
		}
		if(!result){
			logger.warn("[渡劫线程管理][删除玩家心跳线程不成功]");
		}
		return result;
	}
	
	public boolean removeRobbery(long playerId) {
		boolean result = false;
		Player player = null;
		for(int i=0; i<robberyThreadAmount; i++){
			synchronized (robberyThreads[i].allRobbery) {
				for(BaseRobbery robbery : robberyThreads[i].allRobbery) {
					if(robberyThreads[i].allRobbery.remove(robbery)){
						try {
							player = GamePlayerManager.getInstance().getPlayer(robbery.playerId);
							player.checkAndClearRobberyBuff();
							TransitRobberyEntityManager.getInstance().护身罡气生效(player.getId());
							TransitRobberyEntityManager.getInstance().删除火神玄魄效果(player.getId());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						logger.info("[渡劫][删除玩家心跳线程成功][" + playerId + "]");
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
	
	private int getLeftTimeFromThread(long playerId){
		for(int i=0; i<robberyThreads.length; i++){
			if(robberyThreads[i].forceLeftTimes.get(playerId) != null){
				return robberyThreads[i].forceLeftTimes.get(playerId);
			}
		}
		return 0;
	}
	
	/**
	 * 检测是否需要通知前端开启天劫强制拉人倒计时
	 */
	public void check4NextRobberyLevel(long playerId){
		if(true){
			return;
		}
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[渡劫][获取player错误---manager(311) e :" + e);
			return;
		}
		Timestamp cutTime = new Timestamp(System.currentTimeMillis());
		if(cutTime.compareTo(RobberyConstant.OPENTIME) < 0){
			NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
			resp.setCountdownType(CountdownAgent.COUNT_TYPE_DUJIE);
			int openLeftTime = (int) ((RobberyConstant.OPENTIME.getTime() - cutTime.getTime()) / 1000);
			resp.setLeftTime(openLeftTime);
			resp.setDescription(Translate.天劫功能开启倒计时);
			player.addMessageToRightBag(resp);
			return;
		}
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(playerId);
		if(entity == null){
			return;
		}
		if(entity.getCurrentLevel() >= RobberyConstant.MAXLEVEL){				//渡劫封印
			return;
		}
		if(entity.getCurrentLevel() >= 9 || entity.getFeisheng() != 0){
			return;
		}
		int leftTime = getLeftTimeFromThread(playerId);
		if(leftTime <= 0) {
			if(entity.getCurrentLevel() < 9 && entity.getRobberyLeftTime() < 0){
				RobberyDataModel model = TransitRobberyManager.getInstance().getRobberyDataModel(entity.getCurrentLevel() + 1);
				if(player.getLevel() >= 110){
					if(model.getSoulChallenge() && player.getSoul(Soul.SOUL_TYPE_SOUL) == null){
						logger.info("[开启下次渡劫强制拉人失败][玩家元神等级不足110][level=" + player.getLogString() + "]");
						return;
					}
					if(entity.getRobberyLeftTime() == -100){
						TransitRobberyEntityManager.getInstance().updateTransitRobberyEntity(playerId, RobberyConstant.INIT_FORCE_PULL_TIME, 0);
						leftTime = RobberyConstant.ONE_DAY_SECOND;
					}
				}
			} else if(entity.getRobberyLeftTime() > 0){
				leftTime = entity.getRobberyLeftTime();
			}
		}
		if(leftTime > 0){
			//发协议通知前端显示倒计时----时间告诉前端
			NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
			resp.setCountdownType(CountdownAgent.COUNT_TYPE_DUJIE);
			resp.setLeftTime(leftTime);
			resp.setDescription(Translate.天劫拉人倒计时);
			player.addMessageToRightBag(resp);
			this.add2CountdownHeartBeat(playerId, leftTime);
		}
	} 
	
	/**
	 * 刷npc
	 * @param game
	 * @param npcId
	 * @param player
	 */
	public void refreshNPC(Game game, int npcId, Player player){
		try{
			NPC npc = MemoryNPCManager.getNPCManager().createNPC(npcId);
			if(npc != null){
				npc.setX(player.getX() + 20);
				npc.setY(player.getY() + 10);
				game.addSprite(npc);
			} else {
				logger.error("[渡劫][刷npc错误][没找到npc,npcId=" + npcId + "]");
			}
		}catch (Exception e) {
			
		}
	}
	
//	private static int[][] refreashPoint = new int[][]{{100,0},{0,100},{-100,0},{0,-100}};
	
	/**
	 * 刷怪
	 * @param game
	 * @param monsterId
	 * @param x
	 * @param y
	 */
	public void refreshMonster(Game game, int monsterId, Player player, int tempI, int x, int y){
//		int temp = tempI % 4;
		try{
			Sprite sprite = null;
			sprite = MemoryMonsterManager.getMonsterManager().createMonster(monsterId);
			if(sprite != null){
				sprite.setX(x);
				sprite.setY(y);
//				sprite.setX(player.getX() + refreashPoint[temp][0]);
//				sprite.setY(player.getY() + refreashPoint[temp][1]);
				sprite.setBornPoint(new Point(sprite.getX(), sprite.getY()));
				game.addSprite(sprite);
				logger.info("[刷出怪物][monsterId=" + monsterId + " x=" + sprite.getX()  + "  y=" + sprite.getY());
			} else {
				logger.info("[渡劫刷怪][怪物没有创建好][monsterId=" + monsterId + "]");
			}
		}catch(Exception e){
			logger.error("[渡劫刷怪][刷怪异常][e:" + e + "]");
		}
	}
	
	private int bossMaxHp1 = 4000000;		//	第五劫
	private int bossMaxHp2 = 6000000;
	
	private int MINDAMAGE1 = 150000;
	private int MINDAMAGE2 = 220000;
	
	private int MAXDAMAGE1 = 270000;
	private int MAXDAMAGE2 = 350000;
	
	private int bossMinHp1 = 1500000;
	private int bossMinHp2 = 2200000;
	
	/**
	 * 刷怪
	 * @param game
	 * @param monsterId
	 * @param x
	 * @param y
	 * @param propertiesMult  属性石人物属性的倍数
	 */
	public void refreshMonster(Game game, int monsterId, int x, int y, Player player, String monsterName, float propertiesMult, float hpMult, int tempI, byte type){
//		int temp = tempI % 4;
		try{
			Monster sprite = null;
			sprite = MemoryMonsterManager.getMonsterManager().createMonster(monsterId);
			if(sprite != null){
				sprite.setX(x);
				sprite.setY(y);
//				sprite.setX(player.getX() + refreashPoint[temp][0]);
//				sprite.setY(player.getY() + refreashPoint[temp][1]);
				sprite.setName(monsterName);
				sprite.setLevel(player.getSoulLevel());
				sprite.setBornPoint(new Point(sprite.getX(), sprite.getY()));
				if(propertiesMult > 0){
					setSpriteProperties(sprite, player, propertiesMult, hpMult, type);
				}
				game.addSprite(sprite);
				logger.info("[=====][" + game.gi.name + "] [monsterId:"+monsterId+"] [x=" + x + "  y=" + y +"][hp=" + sprite.getHp() + "]maxhp=" + sprite.getMaxHP() + "]");
			} else {
				logger.info("[渡劫刷怪][怪物没有创建好][monsterId=" + monsterId + "]");
			}
		}catch(Exception e){
			logger.error("[渡劫刷怪][刷怪异常][e:" + e + "]");
		}
	}
	/**
	 * 怪物血量是玩家血量的倍数
	 */
	public static float bossHpMul = 1.5f;
	/**
	 *  设置怪物属性
	 * @param player
	 * @param propertiesMult
	 */
	private void setSpriteProperties(Monster sprite, Player player, float propertiesMult, float hpMul, byte type){
		//攻击
		int maxAttack = player.getMagicAttack() > player.getPhyAttack() ? player.getMagicAttack() : player.getPhyAttack();
		sprite.setMagicAttack((int) (maxAttack * propertiesMult));
		sprite.setPhyAttack((int) (maxAttack * propertiesMult));
		sprite.setWindAttack((int) (player.getWindAttack() * propertiesMult));
		sprite.setFireAttack((int) (player.getFireAttack() * propertiesMult));
		sprite.setThunderAttack((int) (player.getThunderAttack() * propertiesMult));
		sprite.setBlizzardAttack((int) (player.getBlizzardAttack() * propertiesMult));
		//防御
		sprite.setMagicDefence((int) (player.getMagicDefence() * propertiesMult));
		sprite.setPhyDefence((int) (player.getPhyDefence() * propertiesMult));
		sprite.setWindDefence((int) (player.getWindDefence() * propertiesMult));
		sprite.setFireAttack((int) (player.getFireDefence() * propertiesMult));
		sprite.setThunderDefence((int) (player.getThunderDefence() * propertiesMult));
		sprite.setBlizzardDefence((int) (player.getBlizzardDefence() * propertiesMult));
		//血魔
		int hp = (int) (player.getMaxHP() * bossHpMul);
		if(type == 1) {			//渡劫第五关---特殊需求特殊做--------血量上限，不同关卡不一样
			if(hp > bossMaxHp1){
				hp = bossMaxHp1;
			} else if(hp < bossMinHp1) {
				hp = bossMinHp1;
			}
			if(sprite.getPhyAttack() < MINDAMAGE1) {
				sprite.setMagicAttack(MINDAMAGE1);
				sprite.setPhyAttack(MINDAMAGE1);
			} else if(sprite.getPhyAttack() > MAXDAMAGE1) {
				sprite.setMagicAttack(MAXDAMAGE1);
				sprite.setPhyAttack(MAXDAMAGE1);
			}
		} else if(type == 2) {		//第七关
			if(hp > bossMaxHp2){
				hp = bossMaxHp2;
			} else if(hp < bossMinHp2) {
				hp = bossMinHp2;
			}
			if(sprite.getPhyAttack() < MINDAMAGE2) {
				sprite.setMagicAttack(MINDAMAGE2);
				sprite.setPhyAttack(MINDAMAGE2);
			} else if(sprite.getPhyAttack() > MAXDAMAGE2) {
				sprite.setMagicAttack(MAXDAMAGE2);
				sprite.setPhyAttack(MAXDAMAGE2);
			}
		}
//		if(hp < bossMinHp){
//			hp = bossMinHp;
//		}
		sprite.setMaxHP(hp);
		sprite.setMaxMP((int) (player.getMaxMP() * hpMul));
		sprite.setMp(sprite.getMaxMP());
		sprite.setHp(hp);
	}
	
	
	public void init() throws Exception{
		
		logger.info("[渡劫系统][开始启动渡劫系统]");
		loadInitFile();
		for(int i=0; i<robberyThreadAmount; i++){
			robberyThreads[i] = new RobberyThread();
			robberyThreads[i].start();
			robberyThreads[i].setName("[天劫线程-" + i + "]");
		}
		addExpThread.start();
		addExpThread.setName("[飞升经验增加线程]");
		if(ExperienceManager.maxExpOfLevel != null && ExperienceManager.maxExpOfLevel.length >= 221) {
			RobberyConstant.MAXEXPAWARD = ExperienceManager.maxExpOfLevel[220];
		}
		initDisk();
		doReg();
		ServiceStartRecord.startLog(this);
		if (TestServerConfigManager.isTestServer()) {
			RobberyConstant.玩家可升最高等级 = 300;
		}
	}
	/**
	 * 使用天劫专属道具---------给玩家加buff
	 * @param player
	 * @param ae
	 */
	public void useRobberyProp(Player player, ArticleEntity ae, String liziguangxiao, String buffName, int invalidTime){
		logger.info("[天劫管理][使用道具=" + ae.getArticleName() + "][播放光效:" + liziguangxiao + "]" + "物品名:" + buffName + " 时间：" + invalidTime);
		ParticleData[] particleDatas = new ParticleData[1];
		particleDatas[0] = new ParticleData(player.getId(), liziguangxiao, -1, 2, 1, 1);
		NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
		player.addMessageToRightBag(resp);
		//除了屏蔽散仙的道具其他的都调buff
		for(int i=0; i<RobberyConstant.屏蔽散仙.length; i++){
			String s = RobberyConstant.屏蔽散仙[i];
			if(s.equals(ae.getArticleName())){
				TransitRobberyEntityManager.getInstance().使用护身罡气(player.getId(), RobberyConstant.屏蔽个数[i]);
				return;
			}
		}
		for(int i=0; i<RobberyConstant.减少劫兽伤害.length; i++){
			String s = RobberyConstant.减少劫兽伤害[i];
			if(s.equals(ae.getArticleName())){
				placeBuff2Monster(buffName, player, invalidTime, RobberyConstant.IMMORTAL_ID, RobberyConstant.对应buff级别[i]);
				return;
			}
		}
		if(RobberyConstant.火神减伤.equals(ae.getArticleName())) {
			TransitRobberyEntityManager.getInstance().使用火神玄魄(player.getId(), invalidTime);
			try{
				boolean flag = false;
				for(RobberyThread t :robberyThreads){
					for(BaseRobbery b : t.allRobbery){
						if(b.playerId == player.getId()){
							b.game.addParticle2Monster(RobberyConstant.HUOSHENID, RobberyConstant.BUFF定身);
							flag = true;
							break;
						}
					}
					if(flag) {
						break;
					}
				}
			}catch(Exception e){
				logger.error("[给火神加粒子光效出错===]",e);
			}
			return;
		}
		placeBuff(buffName, player, invalidTime);					
	}
	
	public void 删除火神玄魄效果(long playerId) {
		try{
			boolean flag = false;
			for(RobberyThread t :robberyThreads){
				for(BaseRobbery b : t.allRobbery){
					if(b.playerId == playerId){
						b.game.removeParticle2Monster(RobberyConstant.HUOSHENID);
						flag = true;
						break;
					}
				}
				if(flag) {
					break;
				}
			}
		}catch(Exception e){
			logger.error("[给火神加粒子光效出错===]",e);
		}
	}
	
	
	 public void placeBuff2Monster(String buffName, Player player, int invalidTime, int monsterId, int level){
		 try {
			 BuffTemplateManager btm = BuffTemplateManager.getInstance();
			 BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			 Game game = player.getCurrentGame();
			 if(game != null && bt != null){
				LivingObject[] los = game.getLivingObjects();
				for(int i=0; i<los.length; i++){
				Buff buff = bt.createBuff(level);
					if (buff != null && buff instanceof Buff_GuaiWuShangHai && los[i] instanceof Monster) {
						Monster m = (Monster) los[i];
						if(m.getSpriteCategoryId() != monsterId) {
							continue;
						}
						buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + invalidTime * 1000);
						buff.setCauser(player);
						m.placeBuff(buff);
					}
				}
			 }
		} catch (Exception ex) {
			logger.warn("[天劫buff] [失败] [" + player.getLogString() + "]" + ex);
		}
	 }
	
	/**
	 * 渡劫失败惩罚
	 * @param player
	 */
	public void robberyFail(Player player){
		ArrayList<Buff> buffs = player.getBuffs();
		//要存储的buff
		int level1 = 0;
		for(int i=0; i<buffs.size(); i++){ 
			if(buffs.get(i).getTemplateName().trim().equals(RobberyConstant.FAILBUFF)){
				level1 = buffs.get(i).getLevel() + 1;
				break;
			}
		}
		if(level1 >= 3){
			level1 = 3;
		}
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate bt = btm.getBuffTemplateByName(RobberyConstant.FAILBUFF);
		if (bt != null) {
			Buff buff = bt.createBuff(level1);
			if (buff != null) {
				buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 24 * 60 * 60 * 1000);
				buff.setCauser(player);
				player.placeBuff(buff);
			}
		} else {
			logger.error("[渡劫][buff模板没取到:" + RobberyConstant.FAILBUFF + "]");
		}
	}
//	
//	public static void main(String[] args) {
//		long tempT = 0;
//		int hour = (int) (tempT / 3600);
//		int minits = (int) ((tempT - hour * 3600) / 60);
//		int seconds = (int) (tempT - hour * 3600 - minits * 60);
//		String str = Translate.translateString(Translate.使用渡劫回魂丹, new String[][] {{Translate.STRING_1, hour + ""}, {Translate.STRING_2, minits + ""}, {Translate.STRING_3, seconds + ""}});
//		System.out.println(str);
//	}
	
	public void 使用渡劫回魂丹(Player player){
		ArrayList<Buff> buffs = player.getBuffs();
		//要存储的buff
		int level1 = -1;
		long time = 0;
		long startTime = 0;
		for(int i=0; i<buffs.size(); i++){ 
			if(buffs.get(i).getTemplateName().trim().equals(RobberyConstant.FAILBUFF)){
				level1 = buffs.get(i).getLevel();
				time = buffs.get(i).getInvalidTime() - 3 * 60 * 60 * 1000;
				startTime = buffs.get(i).getStartTime();
				long currentTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				long tempT = (time - currentTime) / 1000;
				int hour = (int) (tempT / 3600);
				int minits = (int) ((tempT - hour * 3600) / 60);
				int seconds = (int) (tempT - hour * 3600 - minits * 60);
				if(tempT > 0) {
					String str = Translate.translateString(Translate.使用渡劫回魂丹, new String[][] {{Translate.STRING_1, hour + ""}, {Translate.STRING_2, minits + ""}, {Translate.STRING_3, seconds + ""}});
					player.sendError(str);
				} else {
					player.sendError(Translate.渡劫虚弱buff消失);
				}
				break;
			}
		}
		if(level1 < 0){
			return;
		}
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate bt = btm.getBuffTemplateByName(RobberyConstant.FAILBUFF);
		if (bt != null) {
			Buff buff = bt.createBuff(level1);
			if (buff != null) {
				buff.setStartTime(startTime);
				buff.setInvalidTime(time);
				buff.setCauser(player);
				player.placeBuff(buff);
			}
		}
	}
	
	public void placeBuff(String buffName, Player player, long invalidTime){
		try {
			// buff
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			logger.info("天劫种植buff,模板=" + bt);
			if (bt != null) {
				Buff buff = bt.createBuff(0);
				logger.info("天劫种植buff,buff=" + bt + "  持续时间=" + invalidTime);
				if (buff != null) {
					buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + invalidTime * 1000);
					buff.setCauser(player);
					player.placeBuff(buff);
					logger.warn("[天劫buff] [成功] [" + player.getLogString() + "]");
				}
			} else {
				logger.error("[天劫buff] [失败，没有找到模板][" + player.getLogString() + "]");
			}
		} catch (Exception ex) {
			logger.warn("[天劫buff] [失败] [" + player.getLogString() + "]" + ex);
		}
	}
	public void placeBuff(String buffName, Player player, long invalidTime, int level){
		try {
			// buff
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			logger.info("天劫种植buff,模板=" + bt + "========" + buffName);
			if (bt != null) {
				Buff buff = bt.createBuff(level);
				logger.info("天劫种植buff,buff=" + bt + "  持续时间=" + invalidTime);
				if (buff != null) {
					buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + invalidTime * 1000);
					buff.setCauser(player);
					player.placeBuff(buff);
				}
			}
			logger.warn("[天劫buff] [成功] [" + player.getLogString() + "]");
		} catch (Exception ex) {
			logger.warn("[天劫buff] [失败] [" + player.getLogString() + "]" + ex);
		}
	}
	/***
	 * 
	 * @param buffName
	 * @param player
	 * @param invalidTime
	 * @param level
	 */
	public void placeBuff(String buffName, Player player, long invalidTime, int level, Monster monster){
		try {
			// buff
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			logger.info("天劫种植buff,模板=" + bt + "========" + buffName);
			if (bt != null) {
				Buff buff = bt.createBuff(level);
				logger.info("天劫种植buff,buff=" + bt + "  持续时间=" + invalidTime);
				if (buff != null) {
					buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + invalidTime * 1000);
					buff.setCauser(monster);
					player.placeBuff(buff);
				}
			}
			logger.warn("[天劫buff] [成功] [" + player.getLogString() + "]");
		} catch (Exception ex) {
			logger.warn("[天劫buff] [失败] [" + player.getLogString() + "]" + ex);
		}
	}
	/**
	 * 出场景渡劫失败
	 * @param player
	 */
	public void failRobbery(Player player, String mapName){
		if(!TransitRobberyEntityManager.getInstance().isPlayerInRobbery(player.getId())){
			return;
		}
		for(int i=0; i<robberyThreads.length; i++){
			for(BaseRobbery b : robberyThreads[i].allRobbery){
				if(b.playerId == player.getId() && !mapName.equals(b.game.gi.name)){
					b.isSucceed = false;
					return;
				}
			}
		}
	}
	
	/**
	 * 开始渡劫
	 */
	public void startRobbery(Player player){
		for(int i=0; i<robberyThreads.length; i++){
			for(BaseRobbery b : robberyThreads[i].allRobbery){
				if(b.playerId == player.getId()){
					logger.info("找到玩家渡劫的信息，开始渡劫=====" + player.getId());
					b.start();
					return;
				}
			}
		}
	}
	public void 回城(Player player){
		回城(player, true);
	}
	/**
	 * 渡劫中回城
	 * @param player
	 */
	public void 回城(Player player, boolean flag){
		for(int i=0; i<robberyThreads.length; i++){
			for(BaseRobbery b : robberyThreads[i].allRobbery){
				if(b.playerId == player.getId()){
					logger.info("找到玩家渡劫的信息，回城=====" + player.getId());
					b.endRobbery(flag);
					return;
				}
			}
		}
	}
	
	/**
	 * 进入渡劫场景前的检测
	 * @param player
	 * @param force	是否是强制拉入（强制切换成元神状态）
	 */
	public synchronized boolean ready2EnterRobberyScene(Player player, boolean force){
		Timestamp cutTime = new Timestamp(System.currentTimeMillis());
		if(cutTime.compareTo(RobberyConstant.OPENTIME) < 0){
			player.sendError(Translate.渡过功能未开启);
			return false;
		}
		if (force) {
			TransitRobberyEntityManager.getInstance().updateTransitRobberyEntity(player.getId(), RobberyConstant.UPDATE_FORCE_PULL_TIME, -1);
		}
		List<Buff> buffs = player.getAllBuffs();
		if (buffs != null) {
			for (Buff buff : buffs) {
				if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName()) && buff.getInvalidTime() > 0) {
					player.sendError(Translate.囚禁状态不可渡劫);
					return false;
				}
			}
		}
		try{
			if(player.isInBattleField()){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(TransitRobberyEntityManager.getInstance().isPlayerInRobbery(player.getId())){		//多做一层判断--防止多次传送
			return false;
		}
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		if(entity == null){
			return false;
		}
		if (force && entity.getRobberyLeftTime() < 0) {
			return false;
		}
		if(entity.getCurrentLevel() >= RobberyConstant.MAXLEVEL){			//加渡劫封印
			player.sendError(Translate.渡劫封印);
			return false;
		}
		if(player.getCurrentGame() == null){
			return false;
		}
		PetDaoManager pdm = PetDaoManager.getInstance();
		if (pdm != null && pdm.isPetDao(player.getCurrentGame().gi.name)) {
			return false;
		}
		if(entity.getCurrentLevel() >= 9) {
			player.sendError(Translate.渡过九重天劫);
			return false;
		}
		if(entity.getFeisheng() == 1){
			player.sendError(Translate.飞升后不需要再次渡劫);
			return false;
		}
		if (player.getTeamMark() != Player.TEAM_MARK_NONE) {
			player.sendError(Translate.组队中不可渡劫);
			return false;
		}
		int robberylevel = entity.getCurrentLevel() + 1;
		RobberyDataModel model = TransitRobberyManager.getInstance().getRobberyDataModel(robberylevel);	//获得下一劫条件
//		RobberyDataModel model = TransitRobberyManager.getInstance().getRobberyDataModel(1);	//获得下一劫条件
		int level = player.getLevel();
		if(level < model.getLevelLimit()){
			player.sendError(Translate.等级不够渡劫);
			logger.error("[渡劫][进入渡劫场景失败，等级不足]["+ player.getName() +"][人物等级=" + player.getLevel() + "  渡劫需要等级=" + model.getLevelLimit() + "]");
			return false;
		}
		TransitRobberyEntityManager.getInstance().updateTransitRobberyEntity(player.getId(), RobberyConstant.UPDATE_FORCE_PULL_TIME, -1);
		if(model.getSoulChallenge()){				//如果是元神调挑战
			logger.info("[渡劫][当前天劫只允许元神挑战][是否强制切换元神force=" + force + "][" + player.getLogString() + "]");
//			boolean switchResult = false;
			if(player.getSoul(Soul.SOUL_TYPE_SOUL) != null){
				if(Soul.SOUL_TYPE_SOUL != player.getCurrSoul().getSoulType()){			//非元神状态才需要走下边逻辑
					player.sendError(Translate.只能元神挑战);
					return false;
				}
				if(player.getSoul(Soul.SOUL_TYPE_SOUL).getGrade() < model.getLevelLimit()){
					player.sendError(Translate.元神等级不够);
					return false;
				}
			} else {
				logger.error("[渡劫][出异常了，没有元神不应该开启元神渡劫!!] [" + player.getLogString() + "]");
				player.sendError(Translate.只能元神挑战);
				return false;
			}
		} else {
			logger.info("[渡劫][当前天劫只允许元神挑战][是否强制切换元神force=" + force + "][" + player.getLogString() + "]");
			if(player.getSoul(Soul.SOUL_TYPE_BASE) != null){
				if(Soul.SOUL_TYPE_BASE != player.getCurrSoul().getSoulType()){			//非元神状态才需要走下边逻辑
						player.sendError(Translate.只能本尊挑战);
						return false;
				}
			} else {
				logger.error("[渡劫][出异常了，没有本尊不应该开启元神渡劫!!] [" + player.getLogString() + "]");
				return false;
			}
		}
		//强制收回宠物
		//在transitRobberyEntityManager中的map中加入此玩家标志  = =在换装、召唤宠物、切换元神时做判断
		if(player.getActivePetId() > 0){			//宠物本来在出战状态强制回收
			player.packupPet(false);
		}
		player.downFromHorse(true);
		BaseRobbery a = getRobberyByLevel(robberylevel, player);
		logger.info("[进入渡劫][当前重数=" + entity.getCurrentLevel() + "][" + player.getLogString() + "] [是否服务器强制拉人:" + force + "]");
		a.handlBaseRobbery();
		if(force){						//强拉需要推送提示框协议给客户端
			ENTER_ROBBERT_RES req = new ENTER_ROBBERT_RES(GameMessageFactory.nextSequnceNum(), 0, (byte) robberylevel, model.getTips2());
			player.addMessageToRightBag(req);
		}
		return true;
	}
	/*************************************测试华丽分割线***********************************/
	public synchronized void ready2EnterRobberyScene(Player player, boolean force, int lev){
		if(TransitRobberyEntityManager.getInstance().isPlayerInRobbery(player.getId())){		//多做一层判断--防止多次传送
			return;
		}
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		if(entity == null){
			return;
		}
		logger.info("[进入渡劫][当前重数=" + entity.getCurrentLevel() + "]");
		int robberylevel = lev;
		RobberyDataModel model = TransitRobberyManager.getInstance().getRobberyDataModel(robberylevel);	//获得下一劫条件
//		RobberyDataModel model = TransitRobberyManager.getInstance().getRobberyDataModel(1);	//获得下一劫条件
		int level = player.getLevel();
		logger.info("[需要等级=" + model.getLevelLimit() + "  等级= " + level + "]");
		if(level < model.getLevelLimit()){
			logger.error("[渡劫][进入渡劫场景失败，等级不足]["+ player.getName() +"][人物等级=" + player.getLevel() + "  渡劫需要等级=" + model.getLevelLimit() + "]");
			return;
		}
		if(player.isIsUpOrDown()){			//乘骑状态强制下坐骑
			player.downFromHorse(true);
		}
		if(model.getSoulChallenge()){				//如果是元神调挑战
			logger.info("[渡劫][当前天劫只允许元神挑战][是否强制切换元神force=" + force + "][" + player.getId() + "]");
			boolean switchResult = false;
			if(player.getSoul(Soul.SOUL_TYPE_SOUL) != null){
				if(Soul.SOUL_TYPE_SOUL != player.getCurrSoul().getSoulType()){			//非元神状态才需要走下边逻辑
					if(force){			//强制切换成元神状态
						player.switchSoul(Soul.SOUL_TYPE_SOUL, true);
						switchResult = true;
					} else {
						//弹出确认框----------走正常切换元神
						player.sendError("此劫需要切换元神才可以进入！");
					}
					if(!switchResult){
						logger.error("[渡劫][只允许元神渡劫][切换元神失败][" + player.getId() + "]");
						return;
					}
				}
			} else {
				logger.error("[渡劫][出异常了，没有元神不应该开启元神渡劫!!]");
				player.sendError("先开启元神去吧！");
				return;
			}
		}
		//强制收回宠物
		//在transitRobberyEntityManager中的map中加入此玩家标志  = =在换装、召唤宠物、切换元神时做判断
		if(player.getActivePetId() > 0){			//宠物本来在出战状态强制回收
			player.packupPet(false);
		}
		TransitRobberyEntityManager.getInstance().add2Robbering(player.getId());		//正在渡劫状态
		BaseRobbery a = getRobberyByLevel(robberylevel, player);
		a.handlBaseRobbery();
		if(force){						//强拉需要推送提示框协议给客户端
			logger.info("[强制拉人推送提示框协议给客户端]");
			ENTER_ROBBERT_RES req = new ENTER_ROBBERT_RES(GameMessageFactory.nextSequnceNum(), 0, (byte) robberylevel, model.getTips2());
			player.addMessageToRightBag(req);
		}
	}
	/*************************************测试华丽分割线***********************************/
	
	private BaseRobbery getRobberyByLevel(int robberylevel, Player player){
		BaseRobbery instance = null;
		switch (robberylevel) {
		case 1:instance = new RayRobbery(robberylevel, player); break;
		case 2:instance = new PhantomRobbery(robberylevel, player);break;
		case 3:instance = new YihunRobbery(robberylevel, player); break;
		case 4:instance = new SiXiangRobbery(robberylevel, player); break;
		case 5:instance = new XinMoRobbery(robberylevel, player); break;
		case 6:instance = new LiuYuRobbery(robberylevel, player); break;
		case 7:instance = new QiQingRobbery(robberylevel, player); break;
		case 8:instance = new ShenHunRobbery(robberylevel, player); break;
		case 9:instance = new WuXiangRobbery(robberylevel, player); break;
		default:
			break;
		}
		return instance;
	}
	/**
	 * 创建渡劫场景
	 * @param player
	 * @param mapname
	 * @return
	 */
	public Game createNewGame(Player player , String mapname){
		try{
			GameManager gameManager = GameManager.getInstance();
			GameInfo gi = gameManager.getGameInfo(mapname);
			if(gi == null){
				if(logger.isWarnEnabled())
					logger.warn("[渡劫] [创建渡劫场景失败] [对应的地图信息不存在][玩家:{}][{}][{}]", new Object[]{player.getName(),player.getId(),mapname});
				return null;
			}
			Game newGame = new Game(gameManager,gi);
			try {
				newGame.init();
			} catch (Exception e) {
				logger.error("[渡劫][初始化场景异常][e:" + e + "]");
				e.printStackTrace();
			}
			return newGame; 
		}catch(Exception e){
			logger.error("[渡劫][创建渡劫场景异常][e:" + e + "]");
		}
		return null;
	}

	private void loadInitFile() throws Exception{
//		File f = new File("E://javacode_2//hg-1//game_mieshi_server//conf//game_init_config//transitRobbery//transitRobbery.xls");
//		File f = new File("E://javacode//hg//game_mieshi_server//conf//game_init_config//transitRobbery//transitRobbery.xls");
		File f = new File(initFileName);
		if(!f.exists()){
			throw new Exception("transitRobbery.xlsx配表不存在! " + f.getAbsolutePath());
		}
		robberyDataModel = new ConcurrentHashMap<Integer, RobberyDataModel>();
		awardModel = new ConcurrentHashMap<Integer, AwardModel>();
		rayDamageMap = new ConcurrentHashMap<Integer, RayRobberyDamage>();
		cleConditionMap = new ConcurrentHashMap<Integer, CleConditionModel>();
		playerChanageAvataTimes = new ConcurrentHashMap<Long, Long>();
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);			//天劫基础配表
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			RobberyDataModel value = getRobberyData(row);
			robberyDataModel.put(value.getId(), value);
		}
//		openLevel = robberyDataModel.get(1).getLevelLimit();
		
		sheet = workbook.getSheetAt(1);					//每一次天劫对应雷劫伤害
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			RayRobberyDamage rd = getRayRobberyDamage(row);
			rayDamageMap.put(rd.getId(), rd);
		}
		
		sheet = workbook.getSheetAt(2);					//奖励配表
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			AwardModel am = getAwardModel(row);
			awardModel.put(am.getId(), am);
		}
		
		sheet = workbook.getSheetAt(3);					//过关配表
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			
			boolean exist = true;
			HSSFCell cell = row.getCell(0);
			int id = (int) cell.getNumericCellValue();
			CleConditionModel cm = cleConditionMap.get(id);
			if(cm == null){
				cm = new CleConditionModel();
				exist = false;
			}
			cell = row.getCell(1);
			int level= (int) cell.getNumericCellValue();
			EachLevelDetal ed = getEachLevelDetal(row);
			cm.setId(id);
			if(!exist){
				Map<Integer, EachLevelDetal> map = new HashMap<Integer, EachLevelDetal>();
				map.put(level, ed);
				cm.setLevelDetails(map);
				cleConditionMap.put(id, cm);
			} else {
				cleConditionMap.get(id).getLevelDetails().put(level, ed);
			}
		}
		SHENHUN_BOSSID = Integer.parseInt(cleConditionMap.get(8).getLevelDetails().get(1).getvCondition().split(",")[0]);
//		WUXIANG_BOSSID = Integer.parseInt(cleConditionMap.get(9).getLevelDetails().get(1).getvCondition().split(",")[0]);
		HUANXIANG_BOSSID = Integer.parseInt(cleConditionMap.get(2).getLevelDetails().get(1).getvCondition().split(",")[0]);
	}
	
	private EachLevelDetal getEachLevelDetal(HSSFRow row){
		EachLevelDetal ed = new EachLevelDetal();
		int rwoNum = 2;
		int duration;
		String vcondition;
		String initPoint;
		int interval;
		String tips;
		HSSFCell cell = row.getCell(rwoNum++);
		duration = (int) cell.getNumericCellValue();
		cell = row.getCell(rwoNum++);
		vcondition = cell.getStringCellValue().trim();
		cell = row.getCell(rwoNum++);
		initPoint = cell.getStringCellValue().trim();
		cell = row.getCell(rwoNum++);
		interval = (int) cell.getNumericCellValue();
		cell = row.getCell(rwoNum++);
		tips = cell.getStringCellValue();
		ed.setDuration(duration);
		ed.setInitPoint(initPoint);
		ed.setInterval(interval);
		ed.setvCondition(vcondition);
		ed.setTips(tips);
		return ed;
	}
	
	private RayRobberyDamage getRayRobberyDamage(HSSFRow row){
		RayRobberyDamage rd = new RayRobberyDamage();
		int rwoNum = 0;
		HSSFCell cell = row.getCell(rwoNum++);
		int id = (int) cell.getNumericCellValue();
		cell = row.getCell(rwoNum++);
		int defaultDamage = (int) cell.getNumericCellValue();
		Map<Integer, Integer[]> rayDamage = new HashMap<Integer, Integer[]>();
		cell = row.getCell(rwoNum++);
		int amount = (int) cell.getNumericCellValue();
		for(int i=1; i<=amount; i++){
			Integer[] temp = new Integer[3];
			cell = row.getCell(rwoNum++);
			temp[0] = (int) cell.getNumericCellValue();
			cell = row.getCell(rwoNum++);
			temp[1] = (int) cell.getNumericCellValue();
			cell = row.getCell(rwoNum++);
			temp[2] = (int) cell.getNumericCellValue();
			rayDamage.put(i, temp);
		}
		cell = row.getCell(rwoNum++);
		int maxDamage = (int) cell.getNumericCellValue();
		rd.setId(id);
		rd.setDefaultDamage(defaultDamage);
		rd.setRayDamage(rayDamage);
		rd.setMaxDamage(maxDamage);
		return rd;
	}
	
	private AwardModel getAwardModel(HSSFRow row){
		AwardModel am = new AwardModel();
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		int id = (int) cell.getNumericCellValue();
		int type;
		String items;
		int amount;
		cell = row.getCell(rowNum++);
		type = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		items = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		amount = (int) cell.getNumericCellValue();
		am.setId(id);
		am.setAwardType(type);
		am.setAwardItem(items);
		am.setAmount(amount);
		return am;
	}
	
	private RobberyDataModel getRobberyData(HSSFRow row){
		RobberyDataModel rdm = new RobberyDataModel();
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		int id = (int) cell.getNumericCellValue();
		String name;
		String sceneName;
		int levelLimit;
		int soulChanllge;
		String cleConditions;	//对应胜利条件表
		int immortalProbability;
		int immortalAmount;
		int beastProbability;
		int beastAmount;
		String tips;
		String tips2;
		String initPoint;
		String immortalPoint;
		String beastPoint;
		String beastIds;
		String immortalIds;
		String awards;
		String tips3;
		cell = row.getCell(rowNum++);
		name = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		sceneName = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		levelLimit = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		tips = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		tips2 = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		soulChanllge = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		initPoint = cell.getStringCellValue().trim();
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell = row.getCell(rowNum++);
		cleConditions = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		immortalProbability = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		immortalAmount = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		beastProbability = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		beastAmount = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		immortalPoint = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		beastPoint = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		beastIds = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		immortalIds = cell.getStringCellValue().trim(); 
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		awards = cell.getStringCellValue().trim(); 
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		tips3 = cell.getStringCellValue().trim(); 
		rdm.setId(id);
		rdm.setAward(awards);
		rdm.setBeastId(beastIds);
		rdm.setBeastPoint(beastPoint);
		rdm.setBeastProbability(beastProbability);
		rdm.setCleCondition(cleConditions);;
		rdm.setImmortalId(immortalIds);
		rdm.setImmortalPoint(immortalPoint);
		rdm.setImmortalProbability(immortalProbability);
		rdm.setLevelLimit(levelLimit);
		rdm.setRobberyName(name);
		rdm.setSoulChallenge(soulChanllge==1);
		rdm.setSceneName(sceneName);
		rdm.setMaxImmortalAmount(immortalAmount);
		rdm.setMaxBeastAmount(beastAmount);
		rdm.setInitPoint(initPoint);
		rdm.setTips(tips);
		rdm.setTips2(tips2);
		rdm.setTips3(tips3);
		return rdm;
	}

	public String getInitFileName() {
		return initFileName;
	}


	public void setInitFileName(String initFileName) {
		this.initFileName = initFileName;
	}

	@Override
	public void proc(Event evt) {
		// TODO Auto-generated method stub
		switch (evt.id) {
		case Event.PLAYER_LOGIN:
			Player p = ((EventPlayerLogin) evt).player;
			check4NextRobberyLevel(p.getId());
			break;
		case Event.SERVER_DAY_CHANGE:
			synchronized (awardAmount) {
				awardAmount = new ConcurrentHashMap<Long, Integer>();
				disk.put(key, (Serializable) awardAmount);
			}
			break;
		default: break;
		}
	}

	@Override
	public void doReg() {
		// TODO Auto-generated method stub
		EventRouter.register(Event.PLAYER_LOGIN, this);
		EventRouter.register(Event.SERVER_DAY_CHANGE, this);
	}

	public Map<Integer, CleConditionModel> getCleConditionMap() {
		return cleConditionMap;
	}

	public void setCleConditionMap(Map<Integer, CleConditionModel> cleConditionMap) {
		this.cleConditionMap = cleConditionMap;
	}

	public void setPlayerChanageAvataTimes(Map<Long, Long> playerChanageAvataTimes) {
		this.playerChanageAvataTimes = playerChanageAvataTimes;
	}

	public Map<Long, Long> getPlayerChanageAvataTimes() {
		return playerChanageAvataTimes;
	}

	public String getDiskCachName() {
		return diskCachName;
	}

	public void setDiskCachName(String diskCachName) {
		this.diskCachName = diskCachName;
	}
}
