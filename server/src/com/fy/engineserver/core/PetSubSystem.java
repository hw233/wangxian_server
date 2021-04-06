package com.fy.engineserver.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.article.data.magicweapon.model.TunShiModle;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.career.SkillInfo;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.charge.CardFunction;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.petHouse.HouseData;
import com.fy.engineserver.jiazu.petHouse.PetHouseManager;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.pet.Option_Two_Breed_Cancel;
import com.fy.engineserver.menu.pet.Option_Two_Breed_Confirm;
import com.fy.engineserver.message.BLESS_PET_REQ;
import com.fy.engineserver.message.BLESS_PET_RES;
import com.fy.engineserver.message.CONFIRM_MAGICWEAPON_EAT_RES;
import com.fy.engineserver.message.ENTER_PET_DAO_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.JIAZU_PET_HOUSE_RES;
import com.fy.engineserver.message.OPRATE_PET_ALLOCATE_POINTS_REQ;
import com.fy.engineserver.message.PET2_LiZi_RES;
import com.fy.engineserver.message.PET2_QUERY_RES;
import com.fy.engineserver.message.PET_ALLOCATE_POINTS_REQ;
import com.fy.engineserver.message.PET_ALLOCATE_POINTS_RES;
import com.fy.engineserver.message.PET_CALL_BACK_REQ;
import com.fy.engineserver.message.PET_CELL_HELP_RES;
import com.fy.engineserver.message.PET_CELL_UPGRADE_CONFIRM_REQ;
import com.fy.engineserver.message.PET_CELL_UPGRADE_CONFIRM_RES;
import com.fy.engineserver.message.PET_CELL_UPGRADE_QUERY_RES;
import com.fy.engineserver.message.PET_CLEAR_PROP_CD_REQ;
import com.fy.engineserver.message.PET_CLEAR_PROP_CD_RES;
import com.fy.engineserver.message.PET_EAT_FLY_PROP_REQ;
import com.fy.engineserver.message.PET_EAT_FLY_PROP_RES;
import com.fy.engineserver.message.PET_FEED_REQ;
import com.fy.engineserver.message.PET_FEED_RES;
import com.fy.engineserver.message.PET_FLY_ANIMATION_RES;
import com.fy.engineserver.message.PET_FLY_BUTTON_ONCLICK_REQ;
import com.fy.engineserver.message.PET_FLY_BUTTON_ONCLICK_RES;
import com.fy.engineserver.message.PET_FLY_STATE_REQ;
import com.fy.engineserver.message.PET_FLY_STATE_RES;
import com.fy.engineserver.message.PET_IDENTIFY_QUALITY_REQ;
import com.fy.engineserver.message.PET_IDENTIFY_QUALITY_RES;
import com.fy.engineserver.message.PET_JOIN_CELL_REQ;
import com.fy.engineserver.message.PET_MATING_ADDPET_REQ;
import com.fy.engineserver.message.PET_MATING_CANCEL_REQ;
import com.fy.engineserver.message.PET_MATING_CHANGE_REQ;
import com.fy.engineserver.message.PET_MATING_CONFIRM_REQ;
import com.fy.engineserver.message.PET_MATING_PERSONAL_REQ;
import com.fy.engineserver.message.PET_MATING_REQ;
import com.fy.engineserver.message.PET_MATING_SESSION_OVER_REQ;
import com.fy.engineserver.message.PET_MERGE_REQ;
import com.fy.engineserver.message.PET_QUERY_BY_ARTICLE_REQ;
import com.fy.engineserver.message.PET_QUERY_REQ;
import com.fy.engineserver.message.PET_QUERY_RES;
import com.fy.engineserver.message.PET_RENAME_REQ;
import com.fy.engineserver.message.PET_RENAME_RES;
import com.fy.engineserver.message.PET_REPAIR_REPLACE_REQ;
import com.fy.engineserver.message.PET_REPAIR_REPLACE_RES;
import com.fy.engineserver.message.PET_REPAIR_REQ;
import com.fy.engineserver.message.PET_SEAL_REQ;
import com.fy.engineserver.message.PET_STORE_REQ;
import com.fy.engineserver.message.PET_STRONG_REQ;
import com.fy.engineserver.message.PLAYER_HOOK_PET_RES;
import com.fy.engineserver.message.PLAYER_PET_HOUSE_RES;
import com.fy.engineserver.message.PUTON_PET_ORNA_REQ;
import com.fy.engineserver.message.PUTON_PET_ORNA_RES;
import com.fy.engineserver.message.QUERY_PET_CHUZHAN_RES;
import com.fy.engineserver.message.QUERY_PET_FLY_SKILLS_REQ;
import com.fy.engineserver.message.QUERY_PET_FLY_SKILLS_RES;
import com.fy.engineserver.message.QUERY_PET_INFO_RES;
import com.fy.engineserver.message.QUERY_PET_MERGE_REQ;
import com.fy.engineserver.message.QUERY_PET_ORNAID_REQ;
import com.fy.engineserver.message.QUERY_PET_ORNAID_RES;
import com.fy.engineserver.message.QUERY_PET_STRONG_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.SET_PET_ACTIVATIONTYPE_REQ;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetCellInfo;
import com.fy.engineserver.sprite.pet.PetEatProp2Rule;
import com.fy.engineserver.sprite.pet.PetEatPropRule;
import com.fy.engineserver.sprite.pet.PetFlySkillInfo;
import com.fy.engineserver.sprite.pet.PetFlyState;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.PetMating;
import com.fy.engineserver.sprite.pet.PetMatingManager;
import com.fy.engineserver.sprite.pet.PetPropertyUtility;
import com.fy.engineserver.sprite.pet.PetSkillReleaseProbability;
import com.fy.engineserver.sprite.pet2.GradePet;
import com.fy.engineserver.sprite.pet2.OptionEatFlayPropAgree;
import com.fy.engineserver.sprite.pet2.Option_Clear_Prop_CD;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.engineserver.vip.VipManager;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.WordFilter;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 宠物协议处理子系统
 * 
 */
public class PetSubSystem extends SubSystemAdapter{

	public static final int GAME_DATA_PACKET_SIZE = 1024;

	public static boolean FORCE_USE_DEFAULT_VIEW = false;

	public static int DEFAULT_PLAYER_VIEWWIDTH = 360;

	public static int DEFAULT_PLAYER_VIEWHEIGHT = 480;
	Random random = new Random(); 	
	public String tagforbid[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", " ", "　", "　" };

	/**
	 * 判断是否含有禁用的标签字符
	 * 
	 * @param name
	 * @return
	 */
	public boolean tagValid(String name) {
		String aname = name.toLowerCase();
		for (String s : tagforbid) {
			if (aname.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}

	//单位两
	public long 易筋丹每小时少花费银两 = 80;
	public long 易筋丹每小时时间 = 60*60*1000;
	
	private static PetSubSystem self;

	public static PetSubSystem getInstance() {
		return self;
	}

//	public static Logger logger = Logger.getLogger(PetSubSystem.class.getName());
public	static Logger logger = LoggerFactory.getLogger(PetManager.class.getName());

	private PlayerManager playerManager;
	private MailManager mailManager;
	private PetManager petManager;
	private PetMatingManager petMatingManager;

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public MailManager getMailManager() {
		return mailManager;
	}

	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	public PetManager getPetManager() {
		return petManager;
	}

	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

	public PetMatingManager getPetMatingManager() {
		return petMatingManager;
	}

	public void setPetMatingManager(PetMatingManager petMatingManager) {
		this.petMatingManager = petMatingManager;
	}

	public String[] getInterestedMessageTypes() {
		return new String[] { "PET_SKILL_CAST_REQ", "PET_NONTARGET_SKILL_REQ", "PET_TARGET_SKILL_REQ", "PET_QUERY_REQ",
				"PET_MATING_REQ", "PET_MATING_ADDPET_REQ", "PET_MATING_CONFIRM_REQ", "PET_MATING_CHANGE_REQ", "PET_MATING_CANCEL_REQ", 
				"PET_MATING_PERSONAL_REQ", "PET_IDENTIFY_QUALITY_REQ", "PET_GENGUDAN_COMPOUND_REQ", "PET_GENGU_UP_REQ", "QUERY_PET_MERGE_REQ", 
				"PET_MERGE_REQ", "PET_BECHILD_REQ", "PET_FEED_REQ", "PET_NONTARGET_SKILL_BROADCAST_REQ", "PET_TARGET_SKILL_BROADCAST_REQ", 
				"PET_QUERY_BY_ARTICLE_REQ", "PET_RENAME_REQ", "PET_SEAL_REQ", "PET_ALLOCATE_POINTS_REQ", "QUERY_PET_FEED_REQ",
				"QUERY_PET_CHUZHAN_REQ","PET_REPAIR_REQ","PET_REPAIR_REPLACE_REQ","OPRATE_PET_ALLOCATE_POINTS_REQ","QUERY_PET_STRONG_REQ", "PET_STRONG_REQ",
				"SET_PET_ACTIVATIONTYPE_REQ","ENTER_PET_DAO_REQ","PET_FLY_STATE_REQ","PET_FLY_BUTTON_ONCLICK_REQ","PET_EAT_FLY_PROP_REQ","PET_CLEAR_PROP_CD_REQ"
				,"QUERY_PET_FLY_SKILLS_REQ", "TEMPARY_LIKE_WAIGUA_REQ", "QUERY_PET_ORNAID_REQ", "PUTON_PET_ORNA_REQ",
				"PET_CELL_HELP_REQ","PET_CELL_UPGRADE_QUERY_REQ","PET_CELL_UPGRADE_CONFIRM_REQ","PET_CELL_FIRST_PAGE_REQ","PET_JOIN_CELL_REQ",
				"PLAYER_PET_HOUSE_REQ","JIAZU_PET_HOUSE_REQ","PET_STORE_REQ","PET_CALL_BACK_REQ","BLESS_PET_REQ","PLAYER_HOOK_PET_REQ"};
	}

	public String getName() {
		return "PetSubSystem";
	}

	public void init() throws Exception {
		
		self = this;
		ServiceStartRecord.startLog(this);
	}
	
	public ResponseMessage handle_PLAYER_PET_HOUSE_REQ(Connection conn, RequestMessage message, Player player) {
		handlerPetHousePage(player);
		return null;
	}
	
	public void openPetHouse(Player player){
		List<HouseData> datas = PetHouseManager.getInstance().getJiazuDatas(player.getJiazuId());
//		if(datas == null || datas.size() == 0){
//			player.sendError(Translate.家族中还没有宠物挂机);
//			return;
//		}
		
		HouseData [] ds = datas.toArray(new HouseData[]{});
		Arrays.sort(ds, new Comparator<HouseData>() {
			@Override
			public int compare(HouseData o1, HouseData o2) {
				return o1.getBlessCount() - o2.getBlessCount() ;
			}
			
		});
		
		long[] ids = new long[datas.size()];
		String [] petNames = new String[datas.size()];
		String[] playerName = new String[datas.size()];
		byte[] canBless = new byte[datas.size()];
		String reason = "可祝福";
		for(int i=0;i<ds.length;i++){
			HouseData data = ds[i];
			if(data == null){
				playerName[i] = "";
				petNames[i] = "";
				canBless[i] = 1;
				reason = "宠物已经收回";
				continue;
			}
			playerName[i] = data.getPlayerName();
			petNames[i] = data.getPetName();
			ids[i] = data.getPetId();
			if(data.getPlayerId() == player.getId()){
				canBless[i] = 1;
				reason = "自己的宠物";
			}
			if(data.getBlessCount() >= 5){
				canBless[i] = 1;
				reason = "宠物被祝福次数5次";
			}
			if(data.getBlessIds().contains(new Long(player.getId()))){
				canBless[i] = 1;
				reason = "您已经祝福过该宠物";
			}
			if(player.getBlessCount() >= 10){
				canBless[i] = 1;
				reason = "您今天祝福过10次了";
			}
		}
		PetManager.logger.warn("[请求家族仙兽房信息] [reason:"+reason+"] [宠物列表:"+Arrays.toString(ids)+"] ["+Arrays.toString(petNames)+"] [祝福状态(1:不能):"+Arrays.toString(canBless)+"] ["+player.getLogString()+"]");
		JIAZU_PET_HOUSE_RES res = new JIAZU_PET_HOUSE_RES(GameMessageFactory.nextSequnceNum(), ids, playerName, canBless);
		player.addMessageToRightBag(res);
	}
	
	public ResponseMessage handle_JIAZU_PET_HOUSE_REQ(Connection conn, RequestMessage message, Player player) {
		openPetHouse(player);
		return null;
	}
	
	public ResponseMessage handle_PET_CALL_BACK_REQ(Connection conn, RequestMessage message, Player player) {
		PET_CALL_BACK_REQ req = (PET_CALL_BACK_REQ)message;
		long petId = req.getPetId();
		PetHouseManager.getInstance().recallPet(petId, player);
		return null;
	}
	public ResponseMessage handle_PLAYER_HOOK_PET_REQ(Connection conn, RequestMessage message, Player player) {
		List<HouseData> list = PetHouseManager.getInstance().getPlayerData(player.getId());
		if(list.size() <= 0 || list == null){
			PLAYER_HOOK_PET_RES res = new PLAYER_HOOK_PET_RES(message.getSequnceNum(),new long[]{});
			return res;
		}
		long ids [] = new long[list.size()];
		for(int i=0;i<ids.length;i++){
			ids[i] = list.get(i).getPetId();
		}
		PLAYER_HOOK_PET_RES res = new PLAYER_HOOK_PET_RES(message.getSequnceNum(),ids);
		logger.warn("[请求正在挂机的宠物] ["+Arrays.toString(ids)+"] ["+player.getLogString()+"]");
		return res;
	}
	
	public ResponseMessage handle_BLESS_PET_REQ(Connection conn, RequestMessage message, Player player) {
		BLESS_PET_REQ req = (BLESS_PET_REQ)message;
		long petId = req.getPetId();
		PetHouseManager.blessLock.lock();
		try {
			HouseData data = PetHouseManager.getInstance().getDataByPetId(petId);
			if(data == null){
				player.sendError(Translate.宠物挂机状态不正确);
				return null;
			}
			if(data.getPlayerId() == player.getId()){
				player.sendError(Translate.不能祝福自己的宠物);
				return null;
			}
			if(data.getBlessCount() >= 5){
				player.sendError(Translate.祝福次数已达上限);
				return null;
			}
			if(!TimeTool.instance.isSame(SystemTime.currentTimeMillis(), player.getBlessDate(), TimeDistance.DAY)){
				player.setBlessCount(0);
				player.setBlessDate(SystemTime.currentTimeMillis());
			}
			if(player.getBlessCount() >= 10){
				player.sendError(Translate.您祝福次数已达上限);
				return null;
			}
			List<Long> blessIds = data.getBlessIds();
			if(blessIds.contains(new Long(player.getId()))){
				player.sendError(Translate.改宠物已经祝福过了);
				return null;
			}
			
			blessIds.add(player.getId());
			data.setBlessIds(blessIds);
			data.setBlessCount(data.getBlessCount()+1);
			player.setBlessCount(player.getBlessCount() + 1);
			player.setBlessDate(SystemTime.currentTimeMillis());
			player.sendError(Translate.祝福成功);
			
			List<HouseData> jDatas = PetHouseManager.getInstance().getJiazuDatas(player.getJiazuId());
			List<HouseData> pDatas = PetHouseManager.getInstance().getPlayerData(player.getId());
			for(HouseData d : jDatas){
				if(d != null && d.getPetId() == petId){
					d.setBlessCount(data.getBlessCount());
					d.setBlessIds(data.getBlessIds());
				}
			}
			for(HouseData d : pDatas){
				if(d != null && d.getPetId() == petId){
					d.setBlessCount(data.getBlessCount());
					d.setBlessIds(data.getBlessIds());
				}
			}
			logger.warn("[玩家祝福宠物] [成功] [被祝福次数:{}] [祝福列表:{}] [宠物id:{}] [宠物名字:{}] [宠物主人:{}] [{}]",
					new Object[]{data.getBlessCount(),data.getBlessIds(), data.getPetId(),data.getPetName(),data.getPlayerName(),player.getLogString()});
			BLESS_PET_RES res = new BLESS_PET_RES(req.getPetId(), petId, data.getBlessCount(), (byte)(1));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[祝福宠物异常] [petId:{}] [player:{}] [{}]",new Object[]{petId,player.getLogString(),e});
		} finally{
			PetHouseManager.blessLock.unlock();
		}
		return null;
	}
	
	public ResponseMessage handle_PET_STORE_REQ(Connection conn, RequestMessage message, Player player) {
		PET_STORE_REQ req = (PET_STORE_REQ)message;
		long petId = req.getPetId();
		
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if(jiazu == null){
			player.sendError(Translate.text_jiazu_041);
			return null;
		}
//		GUOPANSDKUSER_12575C23BFB32234
		Pet pet = petManager.getPet(petId);
		if(pet == null){
			player.sendError(Translate.text_pet_23);
			return null;
		}
		if(pet.getOwnerId() != player.getId()){
			player.sendError(Translate.不能操作别人的宠物);
			return null;
		}
		
		for(long pid : player.getPetCell()){
			if(pid == pet.getId()){
				player.sendError(Translate.不能操作正在助战的宠物);
				return null;
			}
		}
		
		if (player.getActivePetId() == petId) {
			player.sendError(Translate.出站宠物不可做此操作);
			return null;
		}
		
		int maxStroeCount = getPlayerMaxStoreCount(player);
		List<HouseData> datas = PetHouseManager.getInstance().getPlayerData(player.getId());
		if(datas != null && datas.size() >= maxStroeCount){
			player.sendError(Translate.仙兽房已满);
			return null;
		}
		
		boolean hasStore = false;
		if(datas != null && datas.size() > 0){
			for(HouseData data : datas){
				if(data.getPetId() == petId){
					hasStore = true;
					break;
				}
			}
		}
		if(hasStore){
			player.sendError(Translate.宠物已经在挂机中);
			return null;
		}
		HouseData data = PetHouseManager.getInstance().storePet(player, pet, jiazu);
		handlerPetHousePage(player);
		player.sendError(Translate.宠物挂机成功);
		logger.warn("[宠物挂机] [成功] [data:{}] [pet:{}] [player:{}]",new Object[]{data,pet.getLogString() ,player.getLogString()});
		return null;
	}
	
	public int getPlayerMaxStoreCount(Player player){
		int count = 0;
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if(jiazu != null){
			count++;
			if(jiazu.getLevel() >= 8){
				count++;
			}
		}
		if(player.ownMonthCard(CardFunction.仙兽房增加一个挂机位)){
			count++;
		}
		return count;
	}
	
	public void handlePetHouse2(Player player){

		long now = System.currentTimeMillis();
		int[] openCell = {1,0,0};
		long[] ids = {0,0,0};
		long[] storeTimes = {0,0,0};
		int[] blessCounts = {0,0,0};
		long[] addExps = {0,0,0};
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if(jiazu == null){
			player.sendError(Translate.text_jiazu_041);
			return;
		}
		if(jiazu.getLevel() >= 8){
			openCell[1] = 1;
		}
		if(player.ownMonthCard(CardFunction.仙兽房增加一个挂机位)){
			openCell[2] = 1;
		}
		List<HouseData> datas = PetHouseManager.getInstance().getPlayerData(player.getId());
		if(datas == null || datas.size() == 0){
			PLAYER_PET_HOUSE_RES res = new PLAYER_PET_HOUSE_RES(GameMessageFactory.nextSequnceNum(), openCell, ids, storeTimes, blessCounts, addExps);
			player.addMessageToRightBag(res);
			return;
		}
		
		for(int i=0;i<datas.size();i++){
			ids[i] = datas.get(i).getPetId();
			storeTimes[i] = now - datas.get(i).getStartStoreTime();
			blessCounts[i] = datas.get(i).getBlessCount();
			int plevel = 0;
			try {
				Pet pet = PetManager.getInstance().getPet(datas.get(i).getPetId());
				plevel = pet.getLevel();
			} catch (Exception e) {
				e.printStackTrace();
			}
			addExps[i] = PetHouseManager.getInstance().getStoreExp(jiazu.getLevel(),plevel , datas.get(i).getStartStoreTime(),datas.get(i).getBlessCount());
		}
		PLAYER_PET_HOUSE_RES res = new PLAYER_PET_HOUSE_RES(GameMessageFactory.nextSequnceNum(), openCell, ids, storeTimes, blessCounts, addExps);
		player.addMessageToRightBag(res);
		logger.warn("[请求玩家仙兽房] [datas:{}] [openCell:{}] [ids:{}] [storeTimes:{}] [blessCounts:{}] [addExps:{}] [{}]",
				new Object[]{datas.size(), Arrays.toString(openCell),Arrays.toString(ids),Arrays.toString(storeTimes),Arrays.toString(blessCounts),Arrays.toString(addExps),player.getLogString()});
	
	}
	
	public void handlerPetHousePage(Player player){
		handlePetHouse2(player);
	}
	
	/**
	 * 查询宠物身上饰品
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_PET_ORNAID_REQ(Connection conn, RequestMessage message, Player player) {
		try {
			QUERY_PET_ORNAID_REQ req = (QUERY_PET_ORNAID_REQ) message;
			long petId = req.getPetId();
			Pet pet = PetManager.getInstance().getPet(petId);
			if (pet != null) {
				QUERY_PET_ORNAID_RES resp = new QUERY_PET_ORNAID_RES(req.getSequnceNum(), petId, pet.getOrnaments()[0]);
				return resp;
			}
		} catch (Exception e) {
			PetManager.logger.warn("[查询宠物饰品] [异常] [" + player.getLogString() + "]", e);
		}
		return null;
	}
	
	public ResponseMessage handle_PET_CELL_HELP_REQ(Connection conn, RequestMessage message, Player player) {
		return new PET_CELL_HELP_RES(message.getSequnceNum(), "宠物功能帮助信息");
	}
	
	public ResponseMessage handle_PET_CELL_UPGRADE_QUERY_REQ(Connection conn, RequestMessage message, Player player) {
		List<PetCellInfo> cellInfos = petManager.getCellInfos();
		PET_CELL_UPGRADE_QUERY_RES res = new PET_CELL_UPGRADE_QUERY_RES(message.getSequnceNum(), player.getPetAddPropId(), "宠物助战帮助信息", cellInfos.toArray(new PetCellInfo[]{}));
		return res;
	}
	public ResponseMessage handle_PET_JOIN_CELL_REQ(Connection conn, RequestMessage message, Player player) {
		long now = System.currentTimeMillis();
		PET_JOIN_CELL_REQ req = (PET_JOIN_CELL_REQ)message;
		int type = req.getOptionType();
		long id = req.getPetId();
		if(player.getLevel() < 150){
			player.sendError(Translate.功能没开启);
			return null;
		}
		Pet pet = petManager.getPet(id);
		if(pet == null){
			player.sendError(Translate.text_pet_23);
			return null;
		}
		if(pet.isSummoned()){
			player.sendError(Translate.text_pet_37);
			return null;
		}
		if(PetHouseManager.getInstance().petIsStore(player, pet)){
			player.sendError(Translate.宠物正在挂机不能上阵);
			return null;
		}
		boolean isJoin = false;
		int joinCount = 0;
		List<Long> ids = player.getPetCell();
		for(long pid : ids){
			joinCount++;
			if(pid == id){
				isJoin = true;
			}
		}
		
		//上
		if(type == 1){
			if(isJoin){
				player.sendError(Translate.已经上阵);
				return null;
			}
			if(player.getLevel() < 220){
				if(joinCount >= 1){
					player.sendError(Translate.助战栏满150);
					return null;
				}
			}else {
				if(joinCount >= 2){
					player.sendError(Translate.助战栏满220);
					return null;
				}
			}
			ids.add(id);
			player.setPetCell(ids);
			player.sendError(Translate.上阵成功);
			player.handlerPetProp2Player();
		}else if(type == 2){
			if(!isJoin){
				player.sendError(Translate.还没上阵);
				return null;
			}
			ids.remove(id);
			player.setPetCell(ids);
			player.sendError(Translate.撤回成功);
			player.handlerPetProp2Player();
		}
		List<PetCellInfo> cellInfos = petManager.getCellInfos();
		int mess = 0;
		for(PetCellInfo info : cellInfos){
			if(info != null && info.getId() == player.getPetAddPropId()){
				mess = info.getAddProp();
				break;
			}
		}
		long [] joinids = new long[player.getPetCell().size()];
		
		for(int i=0;i<joinids.length;i++){
			joinids[i] = player.getPetCell().get(i);
		}
		
		QUERY_PET_INFO_RES res = new QUERY_PET_INFO_RES(message.getSequnceNum(), joinids, mess);
		player.addMessageToRightBag(res);
		
		logger.warn("[助战宠物操作{}] [pet:{}] [player:{}] [cost:{}]",
				new Object[]{(type==1?"上阵":(type==2?"撤回":"非法")),pet.getLogString(), player.getLogString(), (System.currentTimeMillis()-now)});
		return null;
	}
	public ResponseMessage handle_PET_CELL_FIRST_PAGE_REQ(Connection conn, RequestMessage message, Player player) {
		List<PetCellInfo> cellInfos = petManager.getCellInfos();
		int mess = 0;
		for(PetCellInfo info : cellInfos){
			if(info != null && info.getId() == player.getPetAddPropId()){
				mess = info.getAddProp();
				break;
			}
		}
		long [] ids = new long[player.getPetCell().size()];
		
		for(int i=0;i<ids.length;i++){
			ids[i] = player.getPetCell().get(i);
		}
		
		QUERY_PET_INFO_RES res = new QUERY_PET_INFO_RES(message.getSequnceNum(), ids, mess);
		logger.warn("[请求助战宠物信息] [mess:{}] [{}] [{}]",new Object[]{mess,Arrays.toString(ids),player.getLogString()});
		return res;
	}
	
	public ResponseMessage handle_PET_CELL_UPGRADE_CONFIRM_REQ(Connection conn, RequestMessage message, Player player) {
		PET_CELL_UPGRADE_CONFIRM_REQ req = (PET_CELL_UPGRADE_CONFIRM_REQ)message;
		
//		if(player.getActivePetId() > 0){
//			player.sendError(Translate.text_pet_37);
//			return null;
//		}
		
		if(player.getLevel() < 220){
			player.sendError(Translate.text_bourn_004);
			return new PET_CELL_UPGRADE_CONFIRM_RES(req.getSequnceNum(), 0); 
		}
		
		long upgradeId = req.getUpgradeId();
		int currId = player.getPetAddPropId();
		if(upgradeId != currId){
			player.sendError(Translate.操作过快);
			logger.warn("[助战宠物升级] [失败] [请稍后再试] [upgradeId:{}] [currId:{}] [{}]",new Object[]{upgradeId,currId,player.getLogString()});
			return new PET_CELL_UPGRADE_CONFIRM_RES(req.getSequnceNum(), 0);
		}
		
		if(currId >= 8){
			player.sendError(Translate.助战宠物升级已达最大等级);
			return new PET_CELL_UPGRADE_CONFIRM_RES(req.getSequnceNum(), 0); 
		}
		
		Knapsack k = player.getKnapsack_common();
		if(k == null){
			logger.warn("[助战宠物升级] [失败] [背包不存在] [upgradeId:{}] [{}]",new Object[]{upgradeId,player.getLogString()});
			return new PET_CELL_UPGRADE_CONFIRM_RES(req.getSequnceNum(), 0);
		}
		
		List<PetCellInfo> cellInfos = petManager.getCellInfos();
		PetCellInfo currInfo = null;
		for(PetCellInfo info : cellInfos){
			if(info != null && info.getId() == currId){
				currInfo = info;
				break;
			}
		}
		if(currInfo == null){
			logger.warn("[助战宠物升级] [失败] [可能是外挂] [upgradeId:{}] [{}]",new Object[]{upgradeId,player.getLogString()});
			return new PET_CELL_UPGRADE_CONFIRM_RES(req.getSequnceNum(), 0); 
		}
		
		if(player.getExp() < currInfo.getExp()){
			player.sendError(Translate.text_bourn_003);
			return new PET_CELL_UPGRADE_CONFIRM_RES(req.getSequnceNum(), 0); 
		}
		
		int hasNums = player.getKnapsack_common().countArticle(Translate.助战宠物升级材料);
		int needNums = currInfo.getMaterialNum();
		if(hasNums < needNums){
			player.sendError(Translate.translateString(Translate.您背包的材料不足, new String[][] { { Translate.STRING_1, Translate.助战宠物升级材料 }, { Translate.COUNT_1, String.valueOf(currInfo.getMaterialNum())} }));
			return new PET_CELL_UPGRADE_CONFIRM_RES(req.getSequnceNum(), 0); 
		}
		
		List<Long> reIds = new ArrayList<Long>();
		Cell [] cs = k.getCells();
		end:for(Cell c : cs){
			if(c == null || c.isEmpty()){
				continue;
			}
			int count = c.getCount();
			for(int i=0;i<count;i++){
				if(reIds.size() >= needNums){
					break end;
				}
				reIds.add(c.getEntityId());
			}
		}
		
		if(reIds.size() < needNums || (reIds.size() == 0)){
			logger.warn("[助战宠物升级] [失败] [材料数量不足] [id:{}] [upgradeId:{}] [reIds:{}] [{}]",new Object[]{currInfo.getId(),upgradeId, reIds.size(),player.getLogString()});
			return new PET_CELL_UPGRADE_CONFIRM_RES(req.getSequnceNum(), 0);
		}
		
		for(int i=0;i<reIds.size();i++){
			long id = reIds.get(i);
			ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(id, "宠物助战删除升级", true);
			if(aee == null){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.删除物品失败);
				player.addMessageToRightBag(hreq);
				logger.warn("[助战宠物升级] [道具] [失败:删除物品失败] [aeId:{}] [正在删除第:{}个] [总个数:{}] [{}]",new Object[]{id,i,player.getLogString()});
				return null;
			}
			logger.warn("[助战宠物升级] [删除物品成功] [aeId:{}] [需要数量:{}] [拥有数量:{}] [reIds:{}] [{}]",
					new Object[]{id,needNums,hasNums,hasNums,reIds.size(), player.getLogString()});
		}
		
		player.setExp(player.getExp() - currInfo.getExp());
		player.setPetAddPropId(currId + 1);
		logger.warn("[助战宠物升级] [成功] [扣除exp:{}] [剩余exp:{}] [需要材料数:{}] [拥有材料:{}] [助战等级:{}] [upgradeId:{}] [{}]",new Object[]{currInfo.getExp(),player.getExp(), needNums,hasNums,player.getPetAddPropId(),upgradeId,player.getLogString()});
		player.sendError(Translate.助战宠物升级成功);
		return new PET_CELL_UPGRADE_CONFIRM_RES(req.getSequnceNum(), 1);
	}
	
	/**
	 * 宠物穿饰品
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PUTON_PET_ORNA_REQ(Connection conn, RequestMessage message, Player player) {
		PUTON_PET_ORNA_REQ req = (PUTON_PET_ORNA_REQ) message;
		try {
			String result = PetManager.getInstance().putOnOrnaments(player, req.getPetId(), req.getOrnaId(), false);
			if (result != null && !result.isEmpty()) {
				player.sendError(result);
			}
			Pet pet = PetManager.getInstance().getPet(req.getPetId());
			PUTON_PET_ORNA_RES resp = new PUTON_PET_ORNA_RES(req.getSequnceNum(), req.getPetId(), pet.getOrnaments()[0]);
			if (PetManager.logger.isDebugEnabled()) {
				PetManager.logger.debug("[宠物饰品穿脱] [" + player.getLogString() + "] [RESULT:" + result + "] [petId:" + req.getPetId() + "] [ornaId:" + req.getOrnaId() + "]");
			}
			return resp;
		} catch (Exception e) {
			logger.warn("[宠物穿脱饰品] [异常] [" + player.getLogString() + "] [petId:" + req.getPetId() + "] [ornaId:" + req.getOrnaId() + "]", e);
		}
		return null;
	}

	/**
	 * TODO 如果此方法已经处理了请求消息，但是不需要返回响应，那么最后也会return null<br>
	 * 这样一来，调用者就无法区分出消息是否已经被处理了
	 */
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}

			try {
				Class<?> clazz = this.getClass();
				Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
				ResponseMessage res = null;
				m1.setAccessible(true);
				res = (ResponseMessage) m1.invoke(this, conn, message, player);
				return res;
			} catch (InvocationTargetException e) {
				// TODO: handle exception
				e.getTargetException().printStackTrace();
				throw e;
			}
	}
	
	public Map<Integer, Method> methodCache = new HashMap<Integer, Method>();
	public boolean useMethodCache = true;

	/**
	 * 处理分配属性点协议
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_ALLOCATE_POINTS_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PET_ALLOCATE_POINTS_REQ req = (PET_ALLOCATE_POINTS_REQ) message;
		long petId = req.getPetId();
		int points[] = req.getPoints();
		Pet pet = petManager.getPet(petId);
		if (pet == null) {
//			logger.error("[宠物分配属性点] [失败：宠物不存在] [pet:" + petId + "] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "]", start);
			logger.error("[宠物分配属性点失败] [宠物不存在] [pet:{}] [{}] [{}] [{}] [{}ms]", new Object[]{petId,player.getId(),player.getName(),player.getUsername(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			return null;
		}
		
		if(pet.getHookInfo() != null){
			player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
			return null;
		}
		
		PetFlyState pstate = petManager.getPetFlyState(petId, player);
		if(pstate == null){
			pstate = new PetFlyState();
		}
		int qianNengPoint = 0;
		int costQianNengPoint = 0;
		if(pstate != null){
			qianNengPoint = pstate.getQianNengPoint();
		}
		
		if (pet.getUnAllocatedPoints() == 0 && qianNengPoint == 0) {
//			logger.error("[宠物分配属性点] [失败：宠物没有未分配的属性点] [pet:" + pet.getId() + "] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "]", start);
			logger.error("[宠物分配属性点失败] [宠物没有未分配的属性点] [pet:{}] [{}] [{}] [{}] [{}ms]", new Object[]{pet.getId(),player.getId(),player.getName(),player.getUsername(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			player.sendError(Translate.宠物没有未分配的属性点);
			return null;
		}
		synchronized (pet) {
			int total = 0;
			boolean valid = true;
			for (int i = 0; i < points.length; i++) {
				if (points[i] < 0 || points[i] > 2000) {
					valid = false;
					break;
				}
				total += points[i];
			}
			if (!valid || total < 0) {
				logger.error("[宠物分配属性点失败] [失败：分配属性点不合法（负值）] [pet:{}] [{}] [{}] [{}] [{}ms]", new Object[]{pet.getId(),player.getId(),player.getName(),player.getUsername(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
				return null;
			}
			if (total > (pet.getUnAllocatedPoints() + qianNengPoint)) {
				logger.error("[宠物分配属性点失败] [失败：宠物未分配的属性点不够] [total:{}] [UnAllocatedPoints:{}] [qianNengPoint:{}] [pet:{}] [{}] [{}] [{}] [{}ms]", new Object[]{total,pet.getUnAllocatedPoints(), qianNengPoint,pet.getId(),player.getId(),player.getName(),player.getUsername(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
				return null;
			}
			int oldUnAllPoint = pet.getUnAllocatedPoints();
			String oldDesp = StringUtil.arrayToString(new int[] { pet.getStrengthS(), pet.getSpellpowerS(), pet.getDexterityS(), pet.getConstitutionS(), pet.getDingliS() }, ",");
			String addDesp = StringUtil.arrayToString(points, ",");
			if(pet.getUnAllocatedPoints() >= total){
				pet.setUnAllocatedPoints(pet.getUnAllocatedPoints() - total);
			}else{
				if(qianNengPoint > 0){
					costQianNengPoint = total - pet.getUnAllocatedPoints();
					pet.setUnAllocatedPoints(0);
					pstate.setTempPoint(pstate.getTempPoint()+ costQianNengPoint);
					pstate.setQianNengPoint(pstate.getQianNengPoint() - costQianNengPoint);
					PetManager.getInstance().savePetFlyState(pstate, pet.getId(), player);
				}else{
					logger.error("[宠物分配属性点失败] [1] [失败：宠物未分配的属性点不够] [total:{}] [UnAllocatedPoints:{}] [qianNengPoint:{}] [pet:{}] [{}] [{}] [{}] [{}ms]", new Object[]{total,pet.getUnAllocatedPoints(), qianNengPoint,pet.getId(),player.getId(),player.getName(),player.getUsername(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
					return null;
				}
			}
			int[] tempPoints = null;
			if(pet.getTempPoints() == null){
				tempPoints = new int[5];
			}else{
				tempPoints = pet.getTempPoints();
			}
			
//			 0力量1灵力2身法3耐力4定力
			for (int i = 0; i < points.length; i++) {
				switch (i) {
				case PetPropertyUtility.加点力量:
					pet.setStrengthS(pet.getStrengthS() + points[i]);
					tempPoints[PetPropertyUtility.加点力量] += points[i];
					break;
				case PetPropertyUtility.加点灵力:
					pet.setSpellpowerS(pet.getSpellpowerS() + points[i]);
					tempPoints[PetPropertyUtility.加点灵力] += points[i];
					break;
				case PetPropertyUtility.加点身法:
					pet.setDexterityS(pet.getDexterityS() + points[i]);
					tempPoints[PetPropertyUtility.加点身法] += points[i];
					break;
				case PetPropertyUtility.加点耐力:
					pet.setConstitutionS(pet.getConstitutionS() + points[i]);
					tempPoints[PetPropertyUtility.加点耐力] += points[i];
					break;
				case PetPropertyUtility.加点定力:
					pet.setDingliS(pet.getDingliS() + points[i]);
					tempPoints[PetPropertyUtility.加点定力] += points[i];
					break;
				default:
					//
				}
			}
			
			//潜能加点还原属性
			int tempPointsRecord [] = pstate.getTempPointRecord();
			for (int i = 0; i < points.length; i++) {
				if(points[i] > 0){
					if(costQianNengPoint > 0){
						tempPointsRecord[i] += costQianNengPoint;
					}
				}
			}
			qianNengPoint = pstate.getQianNengPoint();
			pstate.setTempPointRecord(tempPointsRecord);
			pet.setTempPoints(tempPoints);
			pet.reinitFightingProperties(false);
			String newDesp = StringUtil.arrayToString(new int[] { pet.getStrengthS(), pet.getSpellpowerS(), pet.getDexterityS(), pet.getConstitutionS(), pet.getDingliS() }, ",");
//			logger.info("[宠物分配属性点] [成功] [pet:" + pet.getId() + "] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [old:" + oldDesp + "] [add:" + addDesp + "] [new:" + newDesp + "]", start);
//			if(logger.isInfoEnabled()){
//				logger.info("[宠物分配属性点成功] [加点] [成功] [宠物名:{}] [宠物Id:{}] [{}] [{}] [{}] [old:{}] [add:{}] [new:{}] [{}ms]", new Object[]{pet.getName(), pet.getId(),player.getId(),player.getName(),player.getUsername(),oldDesp,addDesp,newDesp,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
//			}
			if(logger.isInfoEnabled()){
				logger.info("[宠物分配属性点成功] [加点] [成功] [{}] {} [total:{}] [old:{}] [add:{}] [new:{}] [costQianNengPoint:{}] [qianNengPoint:{}] [未分配：{}] [tempPointsRecord:{}] [{}ms]", new Object[]{player.getLogString4Knap(),pet.getLogOfInterest(),total,oldDesp,addDesp,newDesp,costQianNengPoint,qianNengPoint,oldUnAllPoint+"-->"+pet.getUnAllocatedPoints(),Arrays.toString(tempPointsRecord), com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			}
			PET_ALLOCATE_POINTS_RES res = new PET_ALLOCATE_POINTS_RES(message.getSequnceNum(), points, pet);
			
			try{
				int flyState = 0;
				
				boolean isCanFly = false;
				ArticleEntity artE = player.getArticleEntity(pet.getPetPropsId());
				if (artE != null && artE instanceof PetPropsEntity) {
					PetPropsEntity entity = (PetPropsEntity) artE;
					GradePet gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
					if (gradePet != null) {
						if(gradePet.flyType == 1){
							flyState = 1;
							isCanFly = true;
						}
						if(pet.grade >= gradePet.maxGrade && gradePet.flyType == 1){
							flyState = 2;
						}
					}
				}
				
				if(isCanFly){
					if(pet.canUpgrade(player).equals("ok")){
						flyState = 3;
					}

					PetFlyState pstate2 = petManager.getPetFlyState(petId, player);
					if(pstate2 != null){
						if(pstate2.getAnimation() == 1){
							flyState = 4;
						}
						if(pstate2.getFlyState() == 1){
							flyState = 5;
						}
						if(pstate2.getLingXingPoint() >= 105){
							flyState = 6;
						}
						qianNengPoint = pstate2.getQianNengPoint();
					}
					
					PET_FLY_STATE_RES stateRes = new PET_FLY_STATE_RES(req.getSequnceNum(),petId, flyState, qianNengPoint, "");
					player.addMessageToRightBag(stateRes);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return res;
		}
	}
	
	

	/**
	 * 确定还是取消分配的属性点
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_OPRATE_PET_ALLOCATE_POINTS_REQ(Connection conn, RequestMessage message, Player player) {
		
		OPRATE_PET_ALLOCATE_POINTS_REQ req = (OPRATE_PET_ALLOCATE_POINTS_REQ)message;
		if (PetManager.logger.isDebugEnabled()) {
			PetManager.logger.debug("["+req.getPetId()+"] ["+req.getOprate()+"] ["+player.getLogString()+"]");
		}
		
		PetManager pm = PetManager.getInstance();
		Pet pet = pm.getPet(req.getPetId());
		
		if(pet != null && pet.getHookInfo() != null){
			player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
			return null;
		}
		
		for(long pid : player.getPetCell()){
			if(pid == pet.getId()){
				player.sendError(Translate.助战中的宠物不能封印);
				return null;
			}
		}
		if(PetHouseManager.getInstance().petIsStore(player, pet)){
			player.sendError(Translate.挂机中的宠物不能封印);
			return null; 
		}
		
		int oldUnAllPoint = pet.getUnAllocatedPoints();
		String result = pm.opratePetAllocatePoint(pet, req.getOprate());
		if(result != null && !result.equals("")){
//			PET_ALLOCATE_POINTS_RES res = new PET_ALLOCATE_POINTS_RES(message.getSequnceNum(), points, pet);
//			player.addMessageToRightBag(res);
			player.sendError(result);
			PetManager.logger.error("[宠物分配属性点失败] [oprate:"+req.getOprate()+"] [点前后变化:"+oldUnAllPoint+"-->"+pet.getUnAllocatedPoints()+"] ["+player.getLogString()+"] ["+pet.getLogString()+"] ["+result+"]");
		}else if(result != null && result.equals("")){
			int[] points = {0,0,0,0,0};
			if(req.getOprate() == 0){
				PET_ALLOCATE_POINTS_RES res = new PET_ALLOCATE_POINTS_RES(message.getSequnceNum(), points, pet);
				player.addMessageToRightBag(res);
				player.sendError(Translate.宠物加点成功);
				if(PetManager.logger.isWarnEnabled())
					PetManager.logger.warn("[宠物分配属性点成功] [确认加点] [点前后变化:"+oldUnAllPoint+"-->"+pet.getUnAllocatedPoints()+"] ["+player.getLogString()+"] ["+pet.getLogString()+"]");
			}else if(req.getOprate()  == 1){
				PET_ALLOCATE_POINTS_RES res = new PET_ALLOCATE_POINTS_RES(message.getSequnceNum(), points, pet);
				player.addMessageToRightBag(res);
				player.sendError(Translate.取消宠物加点成功);
				if(PetManager.logger.isWarnEnabled())
					PetManager.logger.warn("[取消宠物分配属性点成功] [还原加点] [点前后变化:"+oldUnAllPoint+"-->"+pet.getUnAllocatedPoints()+"] ["+player.getLogString()+"] ["+pet.getLogString()+"]");
			}
		}
		
		try{
			PetFlyState pstate = petManager.getPetFlyState(pet.getId(), player);
			if(pstate != null){
				int flyState = 0;
				boolean isCanFly = false;
				ArticleEntity artE = player.getArticleEntity(pet.getPetPropsId());
				if (artE != null && artE instanceof PetPropsEntity) {
					PetPropsEntity entity = (PetPropsEntity) artE;
					GradePet gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
					if (gradePet != null) {
						if(gradePet.flyType == 1){
							flyState = 1;
							isCanFly = true;
						}
						if(pet.grade >= gradePet.maxGrade && gradePet.flyType == 1){
							flyState = 2;
						}
					}
				}
				
				if(isCanFly){
					if(pet.canUpgrade(player).equals("ok")){
						flyState = 3;
					}
					PetFlyState pstate2 = petManager.getPetFlyState(pet.getId(), player);
					if(pstate2 != null){
						if(pstate2.getAnimation() == 1){
							flyState = 4;
						}
						if(pstate2.getFlyState() == 1){
							flyState = 5;
						}
						if(pstate2.getLingXingPoint() >= 105){
							flyState = 6;
						}
					}
					PET_FLY_STATE_RES stateRes = new PET_FLY_STATE_RES(req.getSequnceNum(),pet.getId(), flyState, pstate.getQianNengPoint(), "");
					player.addMessageToRightBag(stateRes);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 处理宠物改名协议
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_RENAME_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PET_RENAME_REQ req = (PET_RENAME_REQ) message;
		long petEntityId = req.getPetEntityId();
		String newName = req.getNewName();
		//宠物名过滤
		WordFilter filter = WordFilter.getInstance();
		if(!filter.cvalid(newName, 0)){
			player.send_HINT_REQ(Translate.text_6385);
			return null;
		}

		ArticleEntity entity = player.getArticleEntity(petEntityId);
		if (entity == null || !(entity instanceof PetPropsEntity)) {
//			logger.error("[宠物改名] [失败：宠物道具不存在或者不是宠物道具] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [petEntityId:" + petEntityId + "] [" + newName + "]", start);
			logger.error("[宠物改名] [失败：宠物道具不存在或者不是宠物道具] [{}] [{}] [{}] [petEntityId:{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),petEntityId,newName,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			return null;
		}
		Pet pet = petManager.getPet(((PetPropsEntity) entity).getPetId());
		if (pet == null) {
//			logger.error("[宠物改名] [失败：宠物不存在] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [petEntityId:" + petEntityId + "] [" + newName + "]", start);
			logger.error("[宠物改名] [失败：宠物不存在] [{}] [{}] [{}] [petEntityId:{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),petEntityId,newName,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			return null;
		}
		if(pet != null && pet.getHookInfo() != null){
			player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
			return null;
		}
		if(newName.getBytes().length > PetManager.宠物最大名字){
			player.sendError(Translate.名字太长);
			return null;
		}
		String oldName = pet.getName();
		pet.setName(newName);
		PET_RENAME_RES res = new PET_RENAME_RES(req.getSequnceNum(), petEntityId, pet.getName());
		player.addMessageToRightBag(res);
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.改名成功);
		player.addMessageToRightBag(hreq);
//		logger.error("[宠物改名] [成功] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [petEntityId:" + petEntityId + "] [" + newName + "]", start);
//		if(logger.isWarnEnabled())
//			logger.warn("[宠物改名] [成功] [玩家id:{}] [角色:{}] [账号:{}] [petEntityId:{}] [老名字:{}] [新名字:{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),petEntityId,oldName,newName,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
		if(logger.isWarnEnabled())
			logger.warn("[宠物改名] [成功] [{}] {} [老名字:{}] [新名字:{}] [{}ms]", new Object[]{player.getLogString4Knap(),pet.getLogOfInterest(), oldName,newName,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
		return null;
	}

	/**
	 * 查询那个宠物出战
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_PET_CHUZHAN_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		QUERY_PET_CHUZHAN_RES res = null;
		int knapsackIndex = -1;
		if (player.getActivePetId() > 0) {
			Knapsack knapsack = player.getPetKnapsack();
			for (int i = 0; i < knapsack.getCells().length; i++) {
				Cell cell = knapsack.getCells()[i];
				if (cell != null && cell.entityId > 0 && cell.count > 0) {
					try {
						ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.entityId);
						if (ae instanceof PetPropsEntity && ((PetPropsEntity) ae).getPetId() == player.getActivePetId()) {
							knapsackIndex = i;
							break;
						}
					} catch (Exception ex) {
						logger.error("[查询那个宠物出战异常] ["+player.getLogString()+"] []",ex);
					}
				}
			}
		}
		res = new QUERY_PET_CHUZHAN_RES(message.getSequnceNum(), knapsackIndex);
		return res;
	}

	
	/**
	 * 喂养时打开背包显示可喂养的食物
	 * @param conn  
	 * @param message
	 * @param player
	 * @return    feedType;  0 1 2 4 0血 1 快乐 2寿命 4经验
	 */
	public ResponseMessage handle_QUERY_PET_FEED_REQ(Connection conn, RequestMessage message, Player player) {
		
		return petManager.query_pet_food(player, message);
	}
	public ResponseMessage handle_TEMPARY_LIKE_WAIGUA_REQ(Connection conn, RequestMessage message, Player player) {
		try {
			if (TransitRobberyManager.logger.isDebugEnabled()) {
				TransitRobberyManager.logger.debug("[处理PET_TARGET_SKILL_REQ异常] [有外挂嫌疑] [" + player.getLogString() + "]");
			}
			if (MagicWeaponConstant.宠物协议出错记录.containsKey(player.getId())) {
				int a = MagicWeaponConstant.宠物协议出错记录.get(player.getId());
				a++;
				if (a >= MagicWeaponConstant.宠物协议出错多少次后将玩家踢下线) {
					a = 0;
//					if (player.getLevel() <= MagicWeaponConstant.宠物协议出错封号等级 && player.getRMB() <= 0) {
//						DENY_USER_REQ req = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", player.getUsername(), "系统检测到您有疑似外挂操作，如有疑问，请联系客服." ,
//								GameConstants.getInstance().getServerName() + "-外挂检测模块",
//								false, true, false, 0, 48);
//						MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
//						if (TransitRobberyManager.logger.isWarnEnabled()) {
//							TransitRobberyManager.logger.warn("[疑似外挂检测] [封号两天] [" + player.getLogString() + "]");
//						}
//					}
					player.getConn().close();
					MagicWeaponConstant.宠物协议出错记录.remove(player.getId());
				} else {
					MagicWeaponConstant.宠物协议出错记录.put(player.getId(), a);
				}
			} else {
				MagicWeaponConstant.宠物协议出错记录.put(player.getId(), 1);
			}
		} catch (Exception e) {
			if (TransitRobberyManager.logger.isInfoEnabled()) {
				//
				TransitRobberyManager.logger.info("[处理PET_TARGET_SKILL_REQ异常] [有外挂嫌疑] [异常]11", e);
			}
		}
		return null;
	}

	/**
	 * 处理喂养宠物协议
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_FEED_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PET_FEED_REQ req = (PET_FEED_REQ) message;
		long petEntityId = req.getPetEntityId();
//		String articleName = req.getPropsEntityName();
		long foodId = req.getFoodId();
		int num = req.getNum();
		
		if(num < 0){
			logger.error("[宠物喂养错误] [num 小于 0] ["+player.getLogString()+"] ["+num+"]");
			return null;
		}
		
		ArticleEntity entity = player.getArticleEntity(petEntityId);
		if (entity == null || !(entity instanceof PetPropsEntity)) {
//			logger.error("[宠物喂养] [失败：宠物道具不存在或者不是宠物道具] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [petEntityId:" + petEntityId + "] [" + foodId + "] [" + num + "]", start);
			logger.error("[宠物喂养] [失败：宠物道具不存在或者不是宠物道具] [{}] [{}] [{}] [petEntityId:{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),petEntityId,foodId,num,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			PET_FEED_RES res = new PET_FEED_RES(req.getSequnceNum(), Translate.text_pet_30);
			return res;
		}
		
		ArticleEntity ee = player.getArticleEntity(foodId);
		if(ee == null){
//			logger.error("[宠物喂养] [失败：没有喂养道具] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [petEntityId:" + petEntityId + "] [" + foodId + "] [" + num + "]", start);
			logger.error("[宠物喂养] [失败：没有喂养道具] [{}] [{}] [{}] [petEntityId:{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),petEntityId,foodId,num,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			PET_FEED_RES res = new PET_FEED_RES(req.getSequnceNum(), Translate.text_pet_41);
			return res;
		}
		int num2 = player.getArticleEntityNum(foodId);
		if(num2 < num){
//			logger.error("[宠物喂养] [失败：喂养宠物不够] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [petEntityId:" + petEntityId + "] [" + foodId + "] [" + num + "]", start);
			logger.error("[宠物喂养] [失败：喂养宠物物品不够] [{}] [{}] [{}] [petEntityId:{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),petEntityId,foodId,num,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			PET_FEED_RES res = new PET_FEED_RES(req.getSequnceNum(), Translate.text_pet_41);
			return res;
		}
		long petid = ((PetPropsEntity)entity).getPetId();
		Pet pet = PetManager.getInstance().getPet(petid);
		if(pet != null && pet.getHookInfo() != null){
			player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
			return null;
		}
		String result = petManager.feedPet(player, (PetPropsEntity) entity, foodId ,num);
		if(result.equals("")){
//			if(logger.isDebugEnabled()){
//				logger.debug("[宠物喂养] [成功] [宠物：{}] [宠物id:{}] [玩家id:{}] [角色:{}] [账号:{}] [petEntityId:{}] [{}] [{}] [{}ms]", new Object[]{(pet==null?"":pet.getName()),(pet==null?"":pet.getId()), player.getId(),player.getName(),player.getUsername(),petEntityId,foodId,num,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
//			}
			if(logger.isDebugEnabled()){
				logger.debug("[宠物喂养] [成功] [{}] {} [petEntityId:{}] [{}] [{}] [{}ms]", new Object[]{player.getLogString4Knap(), (pet==null?"null":pet.getLogOfInterest()), petEntityId,foodId,num,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			}
			PET_FEED_RES res = new PET_FEED_RES(req.getSequnceNum(), "");
			return res;
		}else if(result.equals("stopTip")){
			logger.debug("PetSubSystem.handle_PET_FEED_REQ: 刷天生技能的药物");
		}else{
			if(logger.isWarnEnabled())
				logger.warn("[宠物喂养失败] ["+result+"] ["+player.getLogString()+"]");
			player.sendError(result);
		}
		return null;
		
	}


	/**
	 * 处理宠物合体协议
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_PET_MERGE_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_PET_MERGE_REQ req = (QUERY_PET_MERGE_REQ) message;
		try {
			return petManager.queryPetMergeReq(player, req);
		} catch (Exception ex) {
			logger.error("[宠物合体查询] [异常] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "]", ex);
			return null;
		}
	}

	/**
	 * 处理宠物合体协议
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_MERGE_REQ(Connection conn, RequestMessage message, Player player) {
		
		PET_MERGE_REQ req = (PET_MERGE_REQ) message;

		try {
			petManager.mergePet(player, req);
			return null;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			logger.error("[宠物合体] [异常] ["+player.getLogString()+"]", ex);
		}
		return null;
	}


	/**
	 * 处理宠物鉴定协议
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_IDENTIFY_QUALITY_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PET_IDENTIFY_QUALITY_REQ req = (PET_IDENTIFY_QUALITY_REQ) message;
		long petEntityId = req.getPetEntityId();
		ArticleEntity entity = player.getArticleEntity(petEntityId);
		if (entity == null || !(entity instanceof PetPropsEntity)) {
//			logger.error("[宠物鉴定] [失败:玩家没有此宠物道具] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [" + petEntityId + "]", start);
			logger.error("[宠物鉴定] [失败:玩家没有此宠物道具] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),petEntityId,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
//			PET_IDENTIFY_QUALITY_RES res = new PET_IDENTIFY_QUALITY_RES(req.getSequnceNum(), petEntityId, Translate.text_pet_22, 0, 0);
//			return res;
			player.sendError(Translate.text_pet_22);
			return null;
		}
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, entity.getId())){
			player.sendError(Translate.高级锁魂物品不能做此操作);
			return null;
		}
		PetManager pmanager = PetManager.getInstance();
		Pet pet = pmanager.getPet(((PetPropsEntity) entity).getPetId());
		if (pet == null) {
//			logger.error("[宠物鉴定] [失败:宠物道具对应的宠物没找到] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [" + petEntityId + "]", start);
			logger.error("[宠物鉴定] [失败:宠物道具对应的宠物没找到] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),petEntityId,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			PET_IDENTIFY_QUALITY_RES res = new PET_IDENTIFY_QUALITY_RES(req.getSequnceNum(), petEntityId, Translate.text_pet_23, 0, 0);
			return res;
		}
		if(pet != null && pet.getHookInfo() != null){
			player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
			return null;
		}
		String result = pmanager.identifyQualityAndGrowthClass(player, pet);
		if(result.equals("")){
//			logger.info("[宠物鉴定] [成功] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [" + pet.getId() + "]", start);
			if(logger.isInfoEnabled())
				logger.info("[宠物鉴定] [成功] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),pet.getId(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			PET_IDENTIFY_QUALITY_RES res = new PET_IDENTIFY_QUALITY_RES(req.getSequnceNum(), petEntityId, "", pet.getQuality(), pet.getGrowthClass());
			return res;
		}else{
			player.sendError(result);
		}
		
		return null;
		
	}

	public ResponseMessage handle_PET_NONTARGET_SKILL_REQ(Connection conn, RequestMessage message, Player player) {
		pushMessageToGame(player, message, null);
		return null;
	}

	public ResponseMessage handle_PET_TARGET_SKILL_REQ(Connection conn, RequestMessage message, Player player) {
		pushMessageToGame(player, message, null);
		return null;
	}

	/**
	 * 处理查询宠物详情的协议
	 * 请注意，新版宠物复用了此方法 康建虎 2013年8月29日14:53:17
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public PET_QUERY_RES handle_PET_QUERY_REQ(Connection conn, RequestMessage message, Player player) {
		// 判断玩家是否在跨服服务器
	
		PET_QUERY_REQ req = (PET_QUERY_REQ) message;
		long id = req.getPetId();
		Pet pet = petManager.getPet(id);
		if (pet != null) {
			return getPetQueryRes(pet, message);
		}
		return null;
	}
	
	public PET_QUERY_RES handle_PET_QUERY_REQ(RequestMessage message, Player player) {
		// 判断玩家是否在跨服服务器
	
		PET_QUERY_REQ req = (PET_QUERY_REQ) message;
		long id = req.getPetId();
		Pet pet = petManager.getPet(id);
		if (pet != null) {
			return getPetQueryRes(pet, message);
		}
		return null;
	}

	public PET_QUERY_RES getPetQueryRes(Pet pet, RequestMessage message) {
		if (pet != null) {		
			//处理临时加点
			if(pet.getTempPoints() != null){
				pet.cancleUnAllocate();
				logger.error("[处理临时加点没取消] [ownerid:"+pet.getOwnerId()+"] ["+pet.getLogString()+"]");
			}
			int[] skillIds = pet.getSkillIds();
			SkillInfo[] skillInfos = new SkillInfo[0];
			if (skillIds != null) {
				CareerManager cm = CareerManager.getInstance();
				if (cm != null) {
					skillInfos = new SkillInfo[skillIds.length];
					for (int i = 0; i < skillIds.length; i++) {
						Skill skill = cm.getSkillById(skillIds[i]);
						if (skill != null) {
							skillInfos[i] = new SkillInfo();
							skillInfos[i].setId(skill.getId());
							skillInfos[i].setIconId(skill.getIconId());
							// skillInfos[i].setInfo(pet, skill);
							skillInfos[i].setMaxLevel(skill.getMaxLevel());
							skillInfos[i].setName(skill.getName());
//							skillInfos[i].setDescription(skill.getDescription());
							skillInfos[i].setSkillType(skill.getSkillType());
							skillInfos[i].setColumnIndex((byte) skill.getColIndex());
							skillInfos[i].setNeedCareerThreadPoints(skill.getNeedCareerThreadPoints());
						}else{
							PetManager.logger.error("[宠物技能] [技能为null] [id:"+skillIds[i]+"] ["+pet.getLogString()+"]");
							skillInfos = new SkillInfo[0];
							break;
						}
					}
				}
			}else{
				if(PetManager.logger.isDebugEnabled()){
					PetManager.logger.debug("[宠物技能] [技能为null] ["+pet.getLogString()+"]");
				}
			}
			
			
			PetSkillReleaseProbability[] pp = new PetSkillReleaseProbability[0];
			// 有普通攻击
			if(skillInfos.length > 0){
				pp = new PetSkillReleaseProbability[skillInfos.length];
				for(int i=0;i<pp.length;i++){
					pp[i] = PetManager.getInstance().getMap().get(skillInfos[i].getId());
					if(pp[i] == null){
						logger.error("[宠物技能配置错误] [技能id:"+skillInfos[i].getId()+"] ["+pet.getLogString()+"]");
						skillInfos = new SkillInfo[0];
						pp = new PetSkillReleaseProbability[0];
						break;
					}
				}
			}
			
			StringBuffer sb = new StringBuffer();
			for(int i =0 ;i< skillInfos.length;i++){
				sb.append(":"+skillInfos[i].getId());
			}
			if(logger.isDebugEnabled()){
				PetManager.logger.debug("[宠物技能] ["+pet.getLogString()+"] ["+sb.toString()+"]");
			}
			PET_QUERY_RES res = null;
			String[] descriptions = new String[0];
			if (message == null) {
				res = new PET_QUERY_RES(GameMessageFactory.nextSequnceNum(), pet, skillInfos, pp,descriptions);
			} else {
				res = new PET_QUERY_RES(message.getSequnceNum(), pet, skillInfos, pp,descriptions);
			}

			return res;
		}
		return null;
	}

	/**
	 * 处理查询宠物详情的协议
	 * 请注意，新版宠物复用了此方法 康建虎 2013年8月29日14:53:17
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public PET_QUERY_RES handle_PET_QUERY_BY_ARTICLE_REQ(Connection conn, RequestMessage message, Player player) {
		
		PET_QUERY_BY_ARTICLE_REQ req = (PET_QUERY_BY_ARTICLE_REQ) message;
		try {
			// 判断玩家是否在跨服服务器
			
			
			long id = req.getPetEntityId();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			Pet pet = null;
			boolean isOwner = req.getIsOwner();
//			PetPropsEntity pp = null;
			long petId = -1L;
			logger.debug("PetSubSystem.handle_PET_QUERY_BY_ARTICLE_REQ: isOwner {} ", isOwner);
			if(isOwner){
				ArticleEntity ae = player.getArticleEntity(id);
				if(ae != null){
					if(ae instanceof PetPropsEntity){
						PetPropsEntity pp;
						pp = (PetPropsEntity)ae;
						petId = pp.getPetId();
					}else if(ae instanceof PetEggPropsEntity){
						petId = ((PetEggPropsEntity)ae).getPetId();
					}else{
						logger.error("PetSubSystem.handle_PET_QUERY_BY_ARTICLE_REQ: 未知类型 {}", ae.getClass().getSimpleName());
					}
				}else{
					logger.error("[查询宠物道具错误] [玩家没有指定的物品] ["+player.getLogString()+"] ["+id+"]");
					return null;
				}
			}else{
				ArticleEntity en = aem.getEntity(id);
				if(en == null){
					logger.error("PetSubSystem.handle_PET_QUERY_BY_ARTICLE_REQ: null entity. {}", id);
					return null;
				}
				if(en instanceof PetPropsEntity){
					PetPropsEntity pp;
					pp = (PetPropsEntity)en;
					petId = pp.getPetId();
				}else if(en instanceof PetEggPropsEntity){
					petId = ((PetEggPropsEntity)en).getPetId();
				}else{
					logger.error("PetSubSystem.handle_PET_QUERY_BY_ARTICLE_REQ: 未知类型 {}", en.getClass().getSimpleName());
					return null;
				}
			}

			pet = petManager.getPet(petId);
			if(pet != null){
				//改了宠物的ownerId 为了更新以后的宠物
				if(isOwner && pet.getOwnerId() < 0){
					logger.error("[设置ownerId] [自己宠物] ["+player.getLogString()+"] [pet:"+pet.getLogString()+"] [以前:"+pet.getOwnerId()+"]");
					pet.setOwnerId(player.getId());
					logger.error("[设置ownerId] [自己宠物] ["+player.getLogString()+"] [pet:"+pet.getLogString()+"] [以后:"+pet.getOwnerId()+"]");
				}
			}else{
				logger.error("[查询宠物道具错误] [没有这个宠物] ["+player.getLogString()+"] [物品id:"+id+"] [宠物id:"+petId+"]");
				return null;
			}
			if (pet != null) {
//			logger.info("[查询宠物道具] [成功] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "]");
				if(logger.isDebugEnabled()){
					logger.debug("[查询宠物道具成功] petID[{}] propId[{}] petName[{}]", 
							new Object[]{pet.getId(),pet.getPetPropsId(),pet.getName()});
				}
				return getPetQueryRes(pet, message);
			}
		} catch (Exception e) {
			logger.error("[查询宠物道具异常] ["+player.getLogString()+"] [宠物道具id:"+req.getPetEntityId()+"]",e);
		}
		return null;
	}

	/**
	 * 处理玩家发起繁殖请求的协议
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_MATING_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PET_MATING_REQ req = (PET_MATING_REQ) message;
		long playerBId = req.getPlayerBId();
		Player playerB = null;
		try {
			playerB = playerManager.getPlayer(playerBId);
		} catch (Exception e) {
			logger.error("[发起繁殖会话] [无此玩家] ["+player.getLogString()+"] ["+playerBId+"]",e);
			return null;
		}
		if (playerB == null || !playerB.isOnline()) {
			if(logger.isWarnEnabled())
				logger.warn("[发起繁殖会话] [失败：无此玩家或者玩家不在线] [{}]", new Object[]{player.getLogString()});
			return null;
		}
		
		if (playerB.isFighting()) {
			player.sendError(Translate.text_fight_noPetMating);
			return null;
		}
		if (playerB.hiddenFanzhiPop) {
			player.sendError(Translate.该玩家已经屏蔽繁殖);
			return null;
		}
	
		PetMating mating = petMatingManager.createNewMatingSession(player, playerB);
		if(mating != null){
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			String descritpion = Translate.translateString(Translate.text_xx想跟你进行宠物繁殖你同意吗, new String[][]{{Translate.STRING_1,player.getName()}});
			mw.setDescriptionInUUB(descritpion);
			Option_Two_Breed_Confirm o1 = new Option_Two_Breed_Confirm();
			o1.setPlayerA(player);
			o1.setMatingId(mating.getId());
			o1.setText(Translate.确定);
	
			Option_Two_Breed_Cancel o2 = new Option_Two_Breed_Cancel();
			o2.setPlayerA(player);
			o2.setMatingId(mating.getId());
			o2.setText(Translate.取消);
			mw.setOptions(new Option[] { o1, o2 });
			
			playerB.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw, mw.getOptions()));
			
	//		logger.info("[发起繁殖会话] [成功] [" + player.getLogString() + "] [playerB:" + playerB.getLogString() + "] ["+mating.getId()+"]", start);
			if(logger.isWarnEnabled()){
				logger.warn("[发起繁殖会话] [成功] [{}] [playerB:{}] [{}] [{}ms]", new Object[]{player.getLogString(),playerB.getLogString(),mating.getId(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			}
		}
		return null;
	}

	public ResponseMessage handle_PET_MATING_ADDPET_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PET_MATING_ADDPET_REQ req = (PET_MATING_ADDPET_REQ) message;
		long matingId = req.getMatingId();
		long peid = req.getPetPropsEntityId();
		PetMating mating = petMatingManager.getPlayerMating(matingId);
		if (mating == null) {
			PET_MATING_SESSION_OVER_REQ sreq = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), Translate.text_pet_18);
			player.addMessageToRightBag(sreq);
			
//			logger.warn("[加入宠物道具] [失败：繁殖会话不存在] [" + player.getLogString() + "] [" + matingId + "]", start);
			if(logger.isWarnEnabled())
				logger.warn("[加入宠物道具] [失败：繁殖会话不存在] [{}] [{}] [{}ms]", new Object[]{player.getLogString(),matingId,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			return null;
		}
		ArticleEntity entity = player.getArticleEntity(peid);
		if (entity == null || !(entity instanceof PetPropsEntity)) {
			PET_MATING_SESSION_OVER_REQ sreq = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), Translate.text_pet_23);
			player.addMessageToRightBag(sreq);
			
			Player other = null;
			try {
				if(mating.getPlayerAId() == player.getId()){
					other = playerManager.getPlayer(mating.getPlayerBId());
					other.addMessageToRightBag(sreq);
				}else{
					other = playerManager.getPlayer(mating.getPlayerAId());
					other.addMessageToRightBag(sreq);
					
				}
			} catch (Exception e) {
				logger.error("[宠物繁殖加入道具异常] ["+player.getLogString()+"]",e);
			}
			
//			logger.warn("[加入宠物道具] [失败：玩家没有此宠物道具] [" + player.getLogString() + "] [" + matingId + "] [道具:" + peid + "]", start);
			if(logger.isWarnEnabled())
				logger.warn("[加入宠物道具] [失败：玩家没有此宠物道具] [{}] [{}] [道具:{}] [{}ms]", new Object[]{player.getLogString(),matingId,peid,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			return null;
		}
		
		PetPropsEntity ppe = (PetPropsEntity)entity;
		Pet pet = PetManager.getInstance().getPet(ppe.getPetId());
		if(pet != null && pet.getHookInfo() != null){
			player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
			return null;
		}
		petMatingManager.refreshMatingSession(player, (PetPropsEntity) entity, mating);
//		logger.info("[加入宠物道具] [成功] [" + player.getLogString() + "] [" + matingId + "] [道具:" + peid + "]", start);
		if(logger.isInfoEnabled())
			logger.info("[加入宠物道具] [成功] [{}] [{}] [道具:{}] [{}ms]", new Object[]{player.getLogString(),matingId,peid,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
		long playerId = -1;
		if (mating.getPlayerAId() == player.getId()) {
			playerId = mating.getPlayerBId();
		} else {
			playerId = mating.getPlayerAId();
		}
		Player pp = null;
		try {
			pp = playerManager.getPlayer(playerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (pp == null || !pp.isOnline()) {
//				logger.error("[加入宠物道具] [失败：玩家不存在或不在线] [" + player.getLogString() + "] [" + matingId + "]");
			logger.error("[加入宠物道具] [失败：玩家不存在或不在线] [{}] [{}]", new Object[]{player.getLogString(),matingId});
			PET_MATING_SESSION_OVER_REQ sreq = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), Translate.text_pet_18);
			player.addMessageToRightBag(sreq);
			
			Player other = null;
			try {
				if(mating.getPlayerAId() == player.getId()){
					other = playerManager.getPlayer(mating.getPlayerBId());
					other.addMessageToRightBag(sreq);
				}else{
					other = playerManager.getPlayer(mating.getPlayerAId());
					other.addMessageToRightBag(sreq);
				}
			} catch (Exception e) {
				logger.error("[宠物繁殖加入道具异常] ["+player.getLogString()+"]",e);
			}
		} else {
			if (PetManager.logger.isWarnEnabled()) {
				PetManager.logger.warn("[宠物繁殖] [添加宠物成功] ["+player.getLogString()+"]");
			}
			PET_MATING_CHANGE_REQ nreq = new PET_MATING_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), matingId, peid);
			pp.addMessageToRightBag(nreq);
		}
		
		return null;
	}

	/**
	 * 个人完成繁殖（俩人完成）
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_MATING_CONFIRM_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PET_MATING_CONFIRM_REQ req = (PET_MATING_CONFIRM_REQ) message;
		long matingId = req.getMatingId();
		String result = petMatingManager.confirmMating(player, matingId);
		if(!result.equals("")){
			PET_MATING_SESSION_OVER_REQ sreq = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), result);
			player.addMessageToRightBag(sreq);
			return null;
		}
		PetMating mating = petMatingManager.getPlayerMating(matingId);
		Player playerA = null;
		Player playerB = null;
		try {
			playerA = playerManager.getPlayer(mating.getPlayerAId());
			playerB = playerManager.getPlayer(mating.getPlayerBId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[宠物繁殖加入道具俩人] ["+player.getLogString()+"]",e);
		}
		
		if (!mating.isMatingConfirmed()) {
//			logger.info("[繁殖确认] [成功] [" + player.getLogString() + "] [" + matingId + "]", start);
			if(logger.isInfoEnabled())
				logger.info("[繁殖确认] [成功] [{}] [{}] [{}ms]", new Object[]{player.getLogString(),matingId,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			player.sendError(Translate.繁殖确认成功);
		} else {
			
			result = petMatingManager.finishPetMating(matingId);
			if(result != null){
				if(result.equals("")){
	//				logger.info("[繁殖确认] [成功并且完成繁殖] [" + playerA.getLogString() + "] [another:" + playerB.getLogString() + "] [" + matingId + "]", start);
					if(logger.isInfoEnabled())
						logger.info("[繁殖确认] [成功并且完成繁殖] [{}] [another:{}] [{}] [{}ms]", new Object[]{playerA.getLogString(),playerB.getLogString(),matingId,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
					// 繁殖创建成功，给玩家发送一个会话结束通知
					PET_MATING_SESSION_OVER_REQ sreqA = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), "");
					playerA.addMessageToRightBag(sreqA);
					playerA.sendError(Translate.繁殖成功获得宠物蛋);
					PET_MATING_SESSION_OVER_REQ sreqB = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), "");
					playerB.addMessageToRightBag(sreqB);
					playerB.sendError(Translate.繁殖成功获得宠物蛋);
				}else{
					
					logger.error("[俩人繁殖错误] ["+player.getLogString()+"] ["+result+"]");
					PET_MATING_SESSION_OVER_REQ sreqA = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), result);
					playerA.addMessageToRightBag(sreqA);
					PET_MATING_SESSION_OVER_REQ sreqB = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), result);
					playerB.addMessageToRightBag(sreqB);
					
				}
			}
		}
		return null;
	}

	public ResponseMessage handle_PET_MATING_CANCEL_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PET_MATING_CANCEL_REQ req = (PET_MATING_CANCEL_REQ) message;
		long matingId = req.getMatingId();
		PetMating mating = petMatingManager.getPlayerMating(matingId);
		boolean canceled = petMatingManager.cancelMating(player, matingId);
//		logger.info("[玩家结束繁殖会话] [" + player.getLogString() + "] [" + matingId + "] [canceled:" + canceled + "]", start);
		if(logger.isWarnEnabled())
			logger.warn("[玩家结束繁殖会话] [{}] [{}] [canceled:{}] [{}ms]", new Object[]{player.getLogString(),matingId,canceled,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
		if (canceled) {
			Player playerA = null;
			Player playerB = null;
			try {
				playerA = playerManager.getPlayer(mating.getPlayerAId());
				playerB = playerManager.getPlayer(mating.getPlayerBId());
			} catch (Exception e) {
				logger.error("[取消宠物繁殖] ["+player.getLogString()+"]",e);
			}
			PET_MATING_SESSION_OVER_REQ sreq = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), "");
			if (player.getId() == playerA.getId()) {
				playerB.addMessageToRightBag(sreq);
				playerB.sendError(Translate.text_pet_51);
			} else {
				playerA.addMessageToRightBag(sreq);
				playerA.sendError(Translate.text_pet_51);
			}
		}
		return null;
	}
	
	
	/**
	 * 提交宠物道具后，确认完成(单独完成)
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_MATING_PERSONAL_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PET_MATING_PERSONAL_REQ req = (PET_MATING_PERSONAL_REQ) message;
		long petPropsEntityAId = req.getPetPropsEntityAId();
		long petPropsEntityBId = req.getPetPropsEntityBId();
		ArticleEntity entityA = player.getArticleEntity(petPropsEntityAId);
		ArticleEntity entityB = player.getArticleEntity(petPropsEntityBId);
		if (entityA == null || entityB == null || !(entityA instanceof PetPropsEntity) || !(entityB instanceof PetPropsEntity)) {
//			logger.error("[个人宠物繁殖] [失败:玩家没有宠物道具或者提供的物品不是宠物道具]  [player:" + player.getLogString() + "] [petAEntity:" + petPropsEntityAId + "] [" + petPropsEntityAId + "]", start);
			logger.error("[个人宠物繁殖] [失败:玩家没有宠物道具或者提供的物品不是宠物道具]  [player:{}] [petAEntity:{}] [{}] [{}ms]", new Object[]{player.getLogString(),petPropsEntityAId,petPropsEntityAId,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
//			PET_MATING_SESSION_OVER_REQ sreqA = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), Translate.text_pet_09);
//			player.addMessageToRightBag(sreqA);
			player.sendError(Translate.繁殖道具不存在或者道具不是宠物道具);
			return null;
		}
		
		String result = petMatingManager.createAndFinishMating(player, (PetPropsEntity) entityA, (PetPropsEntity) entityB);
//			logger.info("[个人宠物繁殖] [成功并且完成繁殖] [" + player.getLogString() + "] [petAEntity:" + petPropsEntityAId + "] [" + petPropsEntityAId + "]", start);
		// 繁殖创建成功，给玩家发送一个会话结束通知
		if(result!= null && result.equals("")){
			PET_MATING_SESSION_OVER_REQ sreqA = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), "");
			player.addMessageToRightBag(sreqA);
		}else{
			logger.error("[个人宠物繁殖错误] ["+player.getLogString()+"] ["+result+"]");
			PET_MATING_SESSION_OVER_REQ sreqA = new PET_MATING_SESSION_OVER_REQ(GameMessageFactory.nextSequnceNum(), result);
			player.addMessageToRightBag(sreqA);
		}
		return null;
	}
	
	
	
	
	/**
	 * 封印
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_SEAL_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PetManager petManager = PetManager.getInstance();
		long petPropsId = ((PET_SEAL_REQ)message).getPetPropsId();
		ArticleEntity ae = player.getArticleEntity(petPropsId);
		if(ae == null){
//			logger.error("[宠物封印失败] [不存在宠物道具] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"]", start);
			logger.error("[宠物封印失败] [不存在宠物道具] [{}] [{}] [{}] [{}ms]", new Object[]{player.getUsername(),player.getId(),player.getName(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			return null;
		}
		if(!(ae instanceof PetPropsEntity)){
//			logger.error("[宠物封印失败] [不属于宠物道具] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"]", start);
			logger.error("[宠物封印失败] [不属于宠物道具] [{}] [{}] [{}] [{}ms]", new Object[]{player.getUsername(),player.getId(),player.getName(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			return null;
		}
		boolean bln = petManager.petSeal((PetPropsEntity)ae, player);
		if(bln){
			if(logger.isWarnEnabled()){
				logger.warn("[宠物封印成功] ["+player.getLogString()+"] ["+petPropsId+"]");
			}
			player.sendError(Translate.text_6112);
		}
		return null;
	}

	/**
	 * 炼妖
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_REPAIR_REQ(Connection conn, RequestMessage message, Player player) {
		
		PET_REPAIR_REQ req = (PET_REPAIR_REQ)message;
		long petId = req.getPetId();
		
		boolean consume = req.getFlushType();
		Pet pet = petManager.getPet(petId);
		
		if(pet != null && pet.getHookInfo() != null){
			player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
			return null;
		}
		
		if(req.getFlushLevel() < 0 || req.getFlushLevel() > 2 ){
			player.sendError(Translate.你没有选择炼妖石请选择炼妖石来炼妖);
			PetManager.logger.error("[宠物炼妖参数错误] ["+player.getLogString()+"] [level:"+req.getFlushLevel()+"]");
			return null;
		}
		
		int 中级 = VipManager.getInstance().vip开启中级炼妖级别();
		int 高级 = VipManager.getInstance().vip开启高级炼妖级别();
		int vip = VipManager.getInstance().getPlayerVipLevel(player,false);
		if(req.getFlushLevel() == 1 && vip < 中级){
			PetManager.logger.error("[宠物炼妖错误] [玩家vip级别不够] ["+vip+"] ["+player.getLogString()+"] [level:"+req.getFlushLevel()+"]");
			return null;
		}
		
		if(req.getFlushLevel() == 2 && vip < 高级){
			PetManager.logger.error("[宠物炼妖错误] [玩家vip级别不够] ["+vip+"] ["+player.getLogString()+"] [level:"+req.getFlushLevel()+"]");
			return null;
		}
		
		
		if(pet != null){
			Knapsack k = player.getPetKnapsack();
			Cell[] cells = k.getCells();
			for(Cell c :cells){
				if(c.getEntityId() > 0){
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(c.getEntityId());
					if(ae instanceof PetPropsEntity){
						PetPropsEntity pp = (PetPropsEntity)ae;
						if(petId == pp.getPetId()){
							String result = petManager.repairPet(player, pet,req.getForce(),req.getFlushLevel(),consume);
							if(result != null){
								if(!result.equals("")){
									player.sendError(result);
								}else{
									if(req.getForce()){
										player.send_HINT_REQ(Translate.translateString(Translate.text_炼化宠物成功, new String[][]{{Translate.ARTICLE_NAME_1,pet.getName()}}));
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 炼妖后替换
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_REPAIR_REPLACE_REQ(Connection conn, RequestMessage message, Player player) {
		
		PET_REPAIR_REPLACE_REQ req = (PET_REPAIR_REPLACE_REQ)message;
		long petId = req.getPetId();
		Pet pet = petManager.getPet(petId);
		if(pet != null && pet.getHookInfo() != null){
			player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
			return null;
		}
		if(pet != null){
			Knapsack k = player.getPetKnapsack();
			Cell[] cells = k.getCells();
			for(Cell c :cells){
				if(c.getEntityId() > 0){
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(c.getEntityId());
					if(ae instanceof PetPropsEntity){
						PetPropsEntity pp = (PetPropsEntity)ae;
						if(petId == pp.getPetId()){
							String result = petManager.replacePet(player, pet);
							if(result != null){
								player.sendError(result);
							}else{
								player.send_HINT_REQ(Translate.text_替换成功);
								PET_REPAIR_REPLACE_RES res = new PET_REPAIR_REPLACE_RES(message.getSequnceNum(), petId);
								player.addMessageToRightBag(res);
								if(logger.isWarnEnabled()){
									logger.warn("[炼妖后替换成功] ["+player.getLogString()+"] ["+petId+"]");
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 查询宠物强化道具
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_PET_STRONG_REQ(Connection conn, RequestMessage message, Player player) {
		
		PetManager pm = PetManager.getInstance();
		if (pm != null) {
			return pm.queryPetStrongReq(player, (QUERY_PET_STRONG_REQ) message);
		}
		return null;
	}
	
	/**
	 * 宠物强化
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_STRONG_REQ(Connection conn, RequestMessage message, Player player) {
		
	
		PetManager pm = PetManager.getInstance();
		if (pm != null) {
			pm.petStrongReq(player, (PET_STRONG_REQ)message);
		}
		return null;
	}
	
	/**
	 * 设置宠物跟随模式  0 被动式,1 主动式,2 跟随式
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SET_PET_ACTIVATIONTYPE_REQ(Connection conn, RequestMessage message, Player player) {
		
	
		SET_PET_ACTIVATIONTYPE_REQ req = (SET_PET_ACTIVATIONTYPE_REQ)message;
		long petEntityId = req.getPetEntityId();
		int type = req.getActivityType();
		
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(petEntityId);
		if(ae != null && ae instanceof PetPropsEntity){
			PetPropsEntity ppe = (PetPropsEntity)ae;
			long petId = ppe.getPetId();
			Pet pet = PetManager.getInstance().getPet(petId);
			if(pet != null && pet.getHookInfo() != null){
				player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
				return null;
			}
			if(pet != null){
				if(type < 0 || type > 2){
					PetManager.logger.error("[设置宠物跟随模式错误] [跟随类型错误] ["+player.getLogString()+"] [类型:"+type+"]");
				}else{
					pet.setActivationType(type);
					player.send_HINT_REQ(Translate.translateString(Translate.宠物行为设置为xx, new String[][]{{Translate.STRING_1,PetManager.跟随模式[type]}}));
//					if (PetManager.logger.isDebugEnabled()) {
//						PetManager.logger.warn("[设置宠物跟随模式成功] [行为] ["+player.getLogString()+"] [宠物:"+pet.getLogString()+"] [0:被动,1:主动,2:跟随] [跟随类型:"+type+"]");
//					}
					if (PetManager.logger.isDebugEnabled()) {
						PetManager.logger.warn("[设置宠物跟随模式] [行为] [成功] [0:被动,1:主动,2:跟随] [跟随类型:"+type+"] [{}] {}",new Object[]{player.getLogString4Knap(),pet.getLogOfInterest()});
					}
				}
			}else{
				PetManager.logger.error("[设置宠物跟随模式错误] [pet为null] ["+player.getLogString()+"] [物品id:"+petEntityId+"]");
			}
		}else{
			PetManager.logger.error("[设置宠物跟随模式错误] [物品不是宠物道具] ["+player.getLogString()+"] [物品id:"+petEntityId+"]");
		}
		return null;
		
	}
	
	/**
	 * 玩家申请进入宠物迷城
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ENTER_PET_DAO_REQ(Connection conn, RequestMessage message, Player player) {
		ENTER_PET_DAO_REQ req = (ENTER_PET_DAO_REQ)message;
		try{
			PetDaoManager.getInstance().handleEnterPetDao(player, req);
		}catch(Exception e){
			e.printStackTrace();
			PetDaoManager.log.warn("[宠物迷城] [申请进入迷城] [异常] [迷城等级："+req.getMinlevel()+"] [迷城类型："+req.getDaotype()+"] ["+player.getLogString()+"]",e);
		}
		return null;
	}
	
	/**
	 * 0:不能飞升的宠物
	 * 1:宠物图鉴界面的"仙",--宠物能飞升就满足
	 * 2:进阶按钮变飞升按钮，--能飞升的宠物且图鉴已达最高阶
	 * 3:飞升按钮打开飞升界面，--能飞升的宠物(满足所有飞升条件)
	 * 4:第一次点击飞升按钮
	 * 5:打开宠物首界面的"仙",--已飞升的宠物(灵性值满50)
	 * 6:灵性105
	 * 
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PET_FLY_STATE_REQ(Connection conn, RequestMessage message, Player player){
		if(message instanceof PET_FLY_STATE_REQ){
			PET_FLY_STATE_REQ req = (PET_FLY_STATE_REQ)message;
			long petId = req.getPetId();
			long playerId = req.getPlayerId();
			Player p = null;
			try{
				p = playerManager.getPlayer(playerId);
			}catch(Exception e){
			}
			if(p == null){
				p = player;
			}
			
			Pet pet = PetManager.getInstance().getPet(petId);
			if(pet != null){
				int flyState = 0;
				int qianNengPoint = 0;
				boolean isCanFly = false;
				ArticleEntity artE = p.getArticleEntity(pet.getPetPropsId());
				if (artE != null && artE instanceof PetPropsEntity) {
					PetPropsEntity entity = (PetPropsEntity) artE;
					GradePet gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
					if (gradePet != null) {
						if(gradePet.flyType == 1){
							flyState = 1;
							isCanFly = true;
						}
						if((pet.grade >= gradePet.maxGrade) && gradePet.flyType == 1){
							flyState = 2;
						}
					}
				}
				
				if(flyState != 2){
//					logger.warn("[查询宠物飞升状态] [出错:宠物不可飞升] [playerId:{}] [宠物:{}] [宠物id:{}] [{}] [{}]",new Object[]{playerId,pet.getName(),petId,flyState,player.getLogString()});
					return null;
				}
				
				if(pet.canUpgrade(p).equals("ok")){
					flyState = 3;
				}
				
				PetFlyState pstate = petManager.getPetFlyState(petId, p);
				if(pstate != null){
					if(pstate.getAnimation() == 1){
						flyState = 4;
					}
					if(pstate.getFlyState() == 1){
						flyState = 5;
					}
					if(pstate.getLingXingPoint() >= 105){
						flyState = 6;
					}
					qianNengPoint = pstate.getQianNengPoint();
				}
				
				PET_FLY_STATE_RES res = new PET_FLY_STATE_RES(req.getSequnceNum(),petId, flyState, qianNengPoint, "");
				if(logger.isWarnEnabled()){
					logger.warn("[查询宠物飞升状态] [成功] [{}] [playerId:{}] [flyState:{}] [qianNengPoint:{}] [宠物名:{}] [宠物id:{}]",new Object[]{player.getLogString(),playerId,flyState,qianNengPoint,pet.getName(), String.valueOf(petId)});
				}
				return res;
			}
			if(logger.isWarnEnabled()){
				logger.warn("[查询宠物飞升状态] [出错] [{}] [playerId:{}] [宠物id:{}]",new Object[]{player.getLogString(),playerId,String.valueOf(petId)});
			}
		}
		return null;
	}
	
	public ResponseMessage handle_PET_CLEAR_PROP_CD_REQ(Connection conn, RequestMessage message, Player player){
		return clearPetPropCD(conn, message, player, false);
	}
	
	public ResponseMessage clearPetPropCD(Connection conn, RequestMessage message, Player player,boolean hasSure){
		if(message instanceof PET_CLEAR_PROP_CD_REQ){ 
			PET_CLEAR_PROP_CD_REQ req = (PET_CLEAR_PROP_CD_REQ)message;
			long petId = req.getPetId();
			int clearType = req.getClearType();
			
			Pet pet = PetManager.getInstance().getPet(petId);
			if(pet != null){
				PetFlyState pstate = petManager.getPetFlyState(petId, player);
				if(pstate == null){
					pstate = new PetFlyState();
				}
				
				//返生丹
				if(clearType == 2){
					if(pstate.getLingXingPoint() <= 50){
						player.sendError(Translate.使用升华露灵性不够);
						return null;
					}
					if(!hasSure){
						Option_Clear_Prop_CD option = new Option_Clear_Prop_CD();
						option.conn = conn;
						option.message = message;
						option.setText(Translate.确定);
						
						Option_Cancel cancel = new Option_Cancel();
						cancel.setText(Translate.取消);
						
						WindowManager windowManager = WindowManager.getInstance();
						MenuWindow mw = windowManager.createTempMenuWindow(600);
						mw.setTitle(Translate.反生丹);
						mw.setDescriptionInUUB(Translate.使用反生丹);
						mw.setOptions(new Option[] { option, cancel });
						QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
						player.addMessageToRightBag(res);
						return new PET_CLEAR_PROP_CD_RES(req.getSequnceNum(), petId, 0);
					}else{
						Knapsack bag = player.getKnapsack_common();
						if (bag == null) {
							logger.error("取得的玩家背包是null {}", player.getName());
							return null;
						}
						int idx = bag.indexOf(Translate.反生丹, true);
						if(idx < 0){
							idx = bag.indexOf(Translate.反生丹, false);
						}
						if(idx < 0){
							player.sendError(Translate.背包中没有反生丹);
							return null;
						}
						ArticleEntity ae = bag.remove(idx, "宠物使用反生丹", true);
						if (ae != null) {
							// 统计
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, "宠物使用反生丹", null);
							logger.info("[宠物使用反生丹] [删除道具:{}] [成功] [格子:{}] [{}]", new Object[] {ae.getArticleName(),idx, player.getLogString4Knap() });
						} else {
							logger.info("[宠物使用反生丹] [删除道具] [失败] [格子:{}] [{}]", new Object[] {idx, player.getLogString4Knap() });
							return null;
						}
						//清除之前加点所升级的属性
						try{
							int [] points = pstate.getPointRecord();
							if(points != null){
								for(int i=0;i<points.length;i++){
									switch (i) {
									case PetPropertyUtility.加点力量:
										pet.setStrengthS(pet.getStrengthS() - points[i]);
										break;
									case PetPropertyUtility.加点灵力:
										pet.setSpellpowerS(pet.getSpellpowerS() - points[i]);
										break;
									case PetPropertyUtility.加点身法:
										pet.setDexterityS(pet.getDexterityS() - points[i]);
										break;
									case PetPropertyUtility.加点耐力:
										pet.setConstitutionS(pet.getConstitutionS() - points[i]);
										break;
									case PetPropertyUtility.加点定力:
										pet.setDingliS(pet.getDingliS() - points[i]);
										break;
									}
								}
								pstate.setPointRecord(new int[5]);
								pstate.setTempPointRecord(new int[5]);
							}
						}catch(Exception e){
							e.printStackTrace();
							logger.warn("[宠物使用反生丹] [清除之前加点所升级的属性] [失败] [{}] [{}]",new Object[] {pstate, player.getLogString4Knap() });
						}
						
						pet.init();
						pstate.setTempPointRecord(new int[5]);
						pstate.setQianNengPoint(250);
						pstate.setLingXingPoint(50);
						pstate.setUseTimes(0);
						
						int oldSkillId = pstate.getSkillId();
						if(oldSkillId > 0){
							if(pet.isSummoned()){
								pet.initFlySkill(2,"返生替换之前的技能");
							}
						}
						
						pstate.setSkillId(0);
						PetManager.getInstance().savePetFlyState(pstate, petId, player);
						player.sendError(Translate.使用反生丹成功);
						player.addMessageToRightBag(new PET_CLEAR_PROP_CD_RES(req.getSequnceNum(), petId, 1));
						
						//同步数据
						long seq = GameMessageFactory.nextSequnceNum();
						PET_QUERY_REQ oldReq = new PET_QUERY_REQ(seq, pet.getId());
						PET_QUERY_RES oldRes = PetSubSystem.getInstance().handle_PET_QUERY_REQ(conn, oldReq, player);
						PET2_QUERY_RES res = Pet2Manager.getInst().makeNewPetQueryRes(seq, oldRes);
						PET2_LiZi_RES ret = Pet2Manager.getInst().makePetInfoWithLizi(pet, seq, res);
						player.addMessageToRightBag(ret);
						
						try {
							AchievementManager.getInstance().record(player, RecordAction.使用返生丹次数);
						} catch (Exception e) {
							PlayerAimManager.logger.warn("[目标系统] [统计使用返生丹次数] [异常] [" + player.getLogString() + "]", e);
						}
						return null;
					}
				}
				
				if(pstate.getXiaoHuaDate() <= System.currentTimeMillis()){
					player.sendError(Translate.清除易经丹cd失败);
					return new PET_CLEAR_PROP_CD_RES(req.getSequnceNum(), petId, 0);
				}
				
				//cost money
				int hasEatNums = pstate.getEatNums();
				long costMoney = 0;
				long configCDTimes = 0; 
				for(PetEatPropRule rule : PetManager.eatRules){
					if(rule != null && rule.getEatTime() == hasEatNums){
						costMoney = rule.getDelCDMoney();
						configCDTimes = rule.getCostData();
					}
				}
				
				if(costMoney == 0){
					logger.warn("[清除宠物易筋丹CD] [失败:服务器配置错误] [{}] [吃易筋丹次数:{}] [配置消费钱:{}] [宠物id:{}]", new Object[] { player.getLogString(),hasEatNums, costMoney, petId });
					return null;
				}
				
				long 剩余消化时间 = pstate.getXiaoHuaDate() - System.currentTimeMillis();
				long 已消化时间少扣除的费用 = ((configCDTimes*60*1000 - 剩余消化时间)/易筋丹每小时时间) * 易筋丹每小时少花费银两;
				long realCostMoney = costMoney - 已消化时间少扣除的费用;
				
				logger.warn("[清除宠物易筋丹CD] [测试] [宠物id:{}] [剩余消化时间:{}] [吃易筋丹次数:{}] [配置消化总cd时间:{}分钟] [已经消化的时间:{}秒] [配置消费钱:{}两] [实际扣除的钱:{}] [{}]", new Object[] {petId,剩余消化时间, hasEatNums,configCDTimes, configCDTimes*60*1000 - 剩余消化时间,costMoney,realCostMoney,player.getLogString()});
				if(!hasSure){
					Option_Clear_Prop_CD option = new Option_Clear_Prop_CD();
					option.conn = conn;
					option.message = message;
					option.setText(Translate.确定);
					
					Option_Cancel cancel = new Option_Cancel();
					cancel.setText(Translate.取消);
					
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(600);
					mw.setTitle(Translate.易筋丹);
					mw.setDescriptionInUUB(Translate.translateString(Translate.清除易筋丹CD, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(realCostMoney*1000)}}));
					mw.setOptions(new Option[] { option, cancel });
					QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					player.addMessageToRightBag(res);
					return new PET_CLEAR_PROP_CD_RES(req.getSequnceNum(), petId, 0);
				}
				
				try {
					int getQianNengValue = 0;
					BillingCenter bc = BillingCenter.getInstance();
					bc.playerExpense(player, realCostMoney*1000, CurrencyType.SHOPYINZI, ExpenseReasonType.宠物飞升吃易经丹, "易筋丹");
					pstate.setXiaoHuaDate(0);
					pstate.setLingXingPoint(pstate.getLingXingPoint()+10);
//					if(pstate.getHistoryLingXingPoint() < pstate.getLingXingPoint()){
						pstate.setHistoryLingXingPoint(pstate.getLingXingPoint());
						pstate.setQianNengPoint(pstate.getQianNengPoint()+50);
						getQianNengValue = 50;
//					}
					PetManager.getInstance().savePetFlyState(pstate, petId, player);
					if(getQianNengValue > 0){
						player.sendError(Translate.translateString(Translate.消耗易筋丹成功, new String[][] { { Translate.COUNT_1, String.valueOf(10) }, { Translate.COUNT_2, String.valueOf(50) } }));
					}else{
						player.sendError(Translate.translateString(Translate.消耗易筋丹成功2, new String[][] { { Translate.COUNT_1, String.valueOf(10) } }));
					}

					try {
						if (pstate.getLingXingPoint() == 50) {
							//通知飞升动画
								String nowAvata = "";
								if(pet.getAvata() != null && pet.getAvata().length > 0){
									nowAvata = pet.getAvata()[0];
								}
								String flyAvata = "";
								ArticleEntity artE = player.getArticleEntity(pet.getPetPropsId());
								if (artE != null && artE instanceof PetPropsEntity) {
									PetPropsEntity entity = (PetPropsEntity) artE;
									GradePet gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
									if(gradePet != null){
										flyAvata = gradePet.flyAvata;
										pet.setIcon(gradePet.flyIcon);
									}
								}
								
								pet.initPetFlyAvata("灵性50播放动画");
								PET_FLY_ANIMATION_RES res = new PET_FLY_ANIMATION_RES(GameMessageFactory.nextSequnceNum(), pet.getId(), pet.getName(),nowAvata, flyAvata);
								player.addMessageToRightBag(res);
							AchievementManager.getInstance().record(player, RecordAction.使用易筋丹使宠物灵性达到50);
							logger.warn("[播放宠物飞升动画] ["+player.getLogString()+"]");
						}
					} catch (Exception e) {
						PlayerAimManager.logger.warn("[目标系统] [统计宠物使用易静丹懂啊50次数] [异常] [" + player.getLogString() + "]", e);
					}
					
					logger.warn("[清除宠物易筋丹CD] [成功] [宠物id:{}] [吃易筋丹次数:{}] [配置消化总cd时间:{}分钟] [已经消化的时间:{}秒] [配置消费钱:{}两] [实际扣除的钱:{}] [{}]", new Object[] {petId, hasEatNums,configCDTimes, configCDTimes*1000 - 剩余消化时间,costMoney,realCostMoney,player.getLogString()});
//					player.sendError(Translate.清除易经丹cd成功);
					player.addMessageToRightBag(new PET_CLEAR_PROP_CD_RES(req.getSequnceNum(), petId, 1));
				} catch (Exception e) {
					e.printStackTrace();
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.银子不足);
					player.addMessageToRightBag(hreq);
					logger.warn("[清除宠物易筋丹CD] [失败{}] [{}] [costMoney:{}] [petId:{}]", new Object[] { Translate.银子不足, player.getLogString(), costMoney, petId });
					return new PET_CLEAR_PROP_CD_RES(req.getSequnceNum(), petId, 0);
				}
			}
		}
		return null;
	}
	
	public String canUse(Player p) {
		Pet pet = PetManager.getInstance().getPet(p.getActivePetId());
		if(pet == null){
			return Translate.出战状态的宠物才能使用升华露;
		}
		
		PetFlyState pstate = PetManager.getInstance().getPetFlyState(pet.getId(), p);
		if(pstate == null){
			return Translate.使用升华露异常;
		}
		
		if(pstate.getLingXingPoint() < 50){
			return Translate.使用升华露灵性不够;
		}
		
		if(pstate.getUseTimes() >= 7){
			return Translate.使用升华露次数达上限;
		}
		
		if(pstate.getLingXingPoint() >= 110){
			return Translate.灵性值已达上限;
		}
		
		if(pstate.getLingXingPoint() > 100){
			return Translate.灵性值100不可使用;
		}
		return null;
	}
	
	public boolean use(Player player, String name) {
		List<PetEatProp2Rule> eat2Rules = PetManager.eat2Rules;
		PetEatProp2Rule rule = null;
		for(PetEatProp2Rule ru : eat2Rules){
			if(ru != null && ru.getPropName().equals(name)){
				rule = ru;
			}
		}
		
		if(rule == null){
			ArticleManager.logger.warn("[使用道具] [失败:升华露配置错误] [道具:{}] [] [玩家:{}]",new Object[]{this.getName(),name,player.getLogString()});
			return false;
		}
		
		int basicLingXingValue = rule.getBasicAddPoint();
		int baoJiValue = 0;
		int baoJiPoints [] = rule.getBaoJiAddPoints();
		int baoJiNums [] = rule.getBaoJiNums();
		int rValue = random.nextInt(1000);
		if(baoJiNums == null){
			ArticleManager.logger.warn("[使用道具] [失败:baoJiNums == null] [道具:{}] [] [玩家:{}]",new Object[]{this.getName(),name,player.getLogString()});
			return false;
		}
//		Arrays.sort(baoJiNums);
		
		for(int i=0;i<baoJiNums.length;i++){
			if(baoJiNums[i] >= rValue){
				if(baoJiValue < baoJiPoints[i]){
					baoJiValue = baoJiPoints[i];
				}
			}
		}
		
		if(baoJiValue >= 1000){
			ArticleManager.logger.warn("[使用道具] [失败:baoJiValue >= 1000] [道具:{}] [] [baoJiValue:{}] [玩家:{}]",new Object[]{this.getName(),name,baoJiValue,player.getLogString()});
			return false;
		}
		
		Pet pet = PetManager.getInstance().getPet(player.getActivePetId());
		if(pet != null){
			PetFlyState pstate = PetManager.getInstance().getPetFlyState(pet.getId(), player);
			if(pstate != null){
				pstate.setUseTimes(pstate.getUseTimes()+1);
				int oldLX = pstate.getLingXingPoint();
				int oldQN = pstate.getQianNengPoint();
				int allLXPoints = oldLX+basicLingXingValue+baoJiValue;
				if(allLXPoints > 110){
					allLXPoints = 110;
				}
				
				try {
					if (allLXPoints >= 85) {
						AchievementManager.getInstance().record(player, RecordAction.使用易筋丹使宠物灵性达到85);
						if (allLXPoints >= 100) {
							AchievementManager.getInstance().record(player, RecordAction.使用易筋丹使宠物灵性达到100);
							if (allLXPoints >= 110) {
								AchievementManager.getInstance().record(player, RecordAction.使用易筋丹使宠物灵性达到110);
							}
						}
					}
				} catch (Exception e) {
					PlayerAimManager.logger.warn("[目标系统] [统计宠物灵性次数次数] [异常] [" + player.getLogString() + "]", e);
				}
				String flyAvata = "";
				ArticleEntity artE = player.getArticleEntity(pet.getPetPropsId());
				if (artE != null && artE instanceof PetPropsEntity) {
					PetPropsEntity entity = (PetPropsEntity) artE;
					GradePet gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
					if(gradePet != null){
						flyAvata = gradePet.flyAvata;
					}
				}
				
				int 五十到一百 = 0;
				int 一百零一到一百零五 = 0;
				int 一百零六到一百一 = 0;
				pstate.setLingXingPoint(allLXPoints);
				int basicAddQNValue = 0;
				for(int i = 0;i<basicLingXingValue+baoJiValue;i++){
					if(oldLX + i <= 100){
						五十到一百++;
					}else if(oldLX + i >= 101 && oldLX + i <= 105){
						一百零一到一百零五++;
					}else if(oldLX + i >= 106 && oldLX + i <= 110){
						一百零六到一百一++;
					}
				}
				String 加点分布 = 五十到一百+","+一百零一到一百零五+","+一百零六到一百一;
				if(五十到一百 > 0){
					basicAddQNValue += 五十到一百 *5;
				}
				if(一百零一到一百零五 > 0){
					basicAddQNValue += 一百零一到一百零五 *10;
				}
				if(一百零六到一百一  > 0){
					basicAddQNValue += 一百零六到一百一 *20;
				}
				pstate.setQianNengPoint(oldQN + basicAddQNValue);
			
				pet.initPetFlyAvata("useProp");
				try {
					int buttonType = 0;
					if(pstate.getLingXingPoint() >= 105){
						buttonType = 1;
					}
					PetFlySkillInfo info = PetManager.petFlySkills.get(pstate.getSkillId());
					if(info == null){
						info = new PetFlySkillInfo();
					}
					int animation = 1;
					if(pstate.getAnimation() == 1){
						animation = 0;
					}
//					pstate.setAnimation(1);
					PetManager.getInstance().savePetFlyState(pstate, pet.getId(), player);
					PET_FLY_BUTTON_ONCLICK_RES res = new PET_FLY_BUTTON_ONCLICK_RES(GameMessageFactory.nextSequnceNum(),flyAvata , animation,buttonType,pstate.getLingXingPoint(), pstate.getXiaoHuaDate(), (7 - pstate.getUseTimes()), "",pet,info);
					player.addMessageToRightBag(res);
					if(baoJiValue > 0){
//						String mess = Translate.translateString(Translate.吃升华露成功暴击描述, new String[][] { { Translate.COUNT_1, String.valueOf(basicLingXingValue) },{ Translate.STRING_1, "+"+baoJiValue }, { Translate.COUNT_2, String.valueOf(basicAddQNValue) }, { Translate.STRING_2, "+"+baojiAddQNValue }});
						String mess = Translate.translateString(Translate.吃升华露成功描述2, new String[][] { { Translate.COUNT_1, String.valueOf(basicLingXingValue+baoJiValue) }, { Translate.COUNT_2, String.valueOf(basicAddQNValue) }});
						player.sendError(mess);
					}else{
						String mess = Translate.translateString(Translate.吃升华露成功描述, new String[][] { { Translate.COUNT_1, String.valueOf(basicLingXingValue) }, { Translate.COUNT_2, String.valueOf(basicAddQNValue) }});
						player.sendError(mess);
					}
					
					if(pstate.getLingXingPoint() == 110){
						pet.initTitle();
//						pet.setFlyNBState((byte)1);
					}else{
//						pet.setFlyNBState((byte)0);
					}
				} catch (Exception e) {
					e.printStackTrace();
					ArticleManager.logger.warn("[使用宠物飞升道具] [存储异常]",e);
				}
				ArticleManager.logger.warn("[使用宠物飞升道具] [成功] [宠物:{}] [宠物id:{}] [道具:{}] [加点分布:{}] [灵性变化:{}] [潜能变化:{}] [使用次数:{}] [技能id:{}] [basicLingXingValue:{}] [baoJiValue:{}] [basicAddQNValue:{}] [玩家:{}]",
						new Object[]{pet.getName(),pet.getId(),this.getName(), 加点分布,oldLX+"-->"+pstate.getLingXingPoint(),oldQN+"-->"+pstate.getQianNengPoint(),pstate.getUseTimes(),pstate.getSkillId(), basicLingXingValue,baoJiValue,basicAddQNValue,player.getLogString()});
				return true;
			}
		}
		return false;
	}
	
	public ResponseMessage eatFlyProp(Connection conn, RequestMessage message, Player player,boolean hasSure){
		if(message instanceof PET_EAT_FLY_PROP_REQ){ 
			PET_EAT_FLY_PROP_REQ req = (PET_EAT_FLY_PROP_REQ)message;
			long petId = req.getPetId();
			int eatType = req.getEatType();
			long articleId = req.getArticleId();
			Pet pet = PetManager.getInstance().getPet(petId);
			if(pet != null){
				int flyState = 0;
				GradePet gradePet = null;
				ArticleEntity artE = player.getArticleEntity(pet.getPetPropsId());
				if (artE != null && artE instanceof PetPropsEntity) {
					PetPropsEntity entity = (PetPropsEntity) artE;
					gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
					if (gradePet != null) {
						if((pet.grade >= gradePet.maxGrade) && gradePet.flyType == 1){
							flyState = 2;
						}
					}
				}
				
				if(flyState != 2){
					logger.warn("[宠物吃易筋丹] [出错:宠物不可飞升] [{}] [宠物:{}] [宠物id:{}]",new Object[]{player.getLogString(),pet.getName(), String.valueOf(petId)});
					return null;
				}
				
				if(gradePet == null){
					player.sendError(Translate.赛季奖励配置错误);
					return null;
				}
				
				PetFlyState pstate = petManager.getPetFlyState(petId, player);
				if(pstate == null){
					pstate = new PetFlyState();
				}
				
				String result = pet.canUpgrade(player);
				if(!result.equals("ok")){
					player.sendError(result);
					return null;
				}
				
				
				Knapsack bag = player.getKnapsack_common();
				if (bag == null) {
					logger.error("取得的玩家背包是null {}", player.getName());
					return null;
				}
				if(eatType == 3){
					String result3 = canUse(player);
					if(result3 != null){
						player.sendError(result3);
						return null;
					}
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(articleId);
					if(ae != null){
						if(use(player, ae.getArticleName())){
							ArticleEntity aee1 = player.removeArticleEntityFromKnapsackByArticleId(articleId, "升华露", true);
							if (aee1 == null) {
								String description = Translate.删除物品不成功;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[吃升华露] [扣除物品失败] [id:" +articleId + "] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description});
								return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
							} else {
								// 统计
								ArticleStatManager.addToArticleStat(player, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "升华露", null);
								player.addMessageToRightBag(new PET_EAT_FLY_PROP_RES(req.getSequnceNum(), petId, 1));
							}
						}
					}
					player.addMessageToRightBag(new PET_EAT_FLY_PROP_RES(req.getSequnceNum(), petId, 0));
					return null;
				}
				
				if(eatType == 2){
					if(pet.isSummoned()){
						player.sendError(Translate.text_pet_16);
						return null;
					}
					if(!hasSure){
						if(pstate.getLingXingPoint() < 105){
							player.sendError(Translate.需要灵性达到105);
							return null;
						}
						OptionEatFlayPropAgree option = new OptionEatFlayPropAgree();
						option.conn = conn;
						option.message = message;
						option.setText(Translate.确定);
						
						Option_Cancel cancel = new Option_Cancel();
						cancel.setText(Translate.取消);
						
						WindowManager windowManager = WindowManager.getInstance();
						MenuWindow mw = windowManager.createTempMenuWindow(600);
						mw.setTitle(Translate.顿悟丹);
						mw.setDescriptionInUUB(Translate.是否使用顿悟丹领悟飞升技能);
						mw.setOptions(new Option[] { option, cancel });
						QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
						player.addMessageToRightBag(res);
					}else{
						//优先绑定的
						int idx = bag.indexOf(Translate.顿悟丹, true);
						if(idx < 0){
							idx = bag.indexOf(Translate.顿悟丹, false);
						}
						if(idx < 0){
							player.sendError(Translate.背包中没有顿悟丹);
							return null;
						}
						ArticleEntity ae = bag.remove(idx, "宠物领悟技能吃", true);
						if (ae != null) {
							// 统计
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, "宠物飞升吃", null);
							logger.info("[宠物吃顿悟丹] [删除道具:{}] [成功] [格子:{}] [{}]", new Object[] {ae.getArticleName(),idx, player.getLogString4Knap() });
						} else {
							logger.info("[宠物吃顿悟丹] [删除道具] [失败] [格子:{}] [{}]", new Object[] {idx, player.getLogString4Knap() });
							return null;
						}
						int oldSkillId = pstate.getSkillId();
						int skillCount = PetManager.petFlySkills.size();
						int ranNum = new Random().nextInt(skillCount);
						int skillId = PetManager.petFlySkills.keySet().toArray(new Integer[]{})[ranNum];
						if(oldSkillId > 0){
							if(pet.isSummoned()){
								pet.initFlySkill(2,"领悟替换之前的技能");
							}
						}
						pstate.setSkillId(skillId);
						PetManager.getInstance().savePetFlyState(pstate, petId, player);
						if(pet.isSummoned()){
							pet.initFlySkill(1,"领悟新的技能");
						}	
						player.sendError(Translate.领悟飞升技能成功);
						try {
							AchievementManager.getInstance().record(player, RecordAction.宠物学习一个飞升技能);
						} catch (Exception e) {
							PlayerAimManager.logger.warn("[目标系统] [统计宠物领悟技能] [异常] [" + player.getLogString() + "]", e);
						}
						logger.info("[宠物吃顿悟丹] [领悟技能成功] [宠物:"+petId+"] [之前技能id:"+oldSkillId+"] [新技能id:"+skillId+"] ["+ranNum+"-->"+skillCount+"] ["+player.getLogString()+"]");
						player.addMessageToRightBag(new PET_EAT_FLY_PROP_RES(req.getSequnceNum(), petId, 1));
					}
					return null;
				}
				if(System.currentTimeMillis() < pstate.getXiaoHuaDate()){
					player.sendError(Translate.易筋丹还在cd中);
					return null;
				}
				
				int eatNums = pstate.getEatNums();
				if(eatNums >= 5){
					player.sendError(Translate.易筋丹次数已达上限);
					return null;
				}

				PetEatPropRule petRule = null;
				for(PetEatPropRule rule : PetManager.eatRules){
					if(rule != null && rule.getEatTime() == (eatNums+1)){
						petRule = rule;
					}
				}
				
				if(petRule == null){
					player.sendError(Translate.赛季奖励配置错误+".");
					return null;
				}
				
				int costNums = petRule.getNeedEatNums();
				
				int hasNums = bag.countArticle(Translate.易筋丹);
				if(hasNums < costNums){
					player.sendError(Translate.translateString(Translate.背包中易筋丹数量不足, new String[][] { { Translate.COUNT_1, String.valueOf(costNums) }}));
					return null;
				}
				
				if(!hasSure){
					OptionEatFlayPropAgree option = new OptionEatFlayPropAgree();
					option.conn = conn;
					option.message = message;
					option.setText(Translate.确定);
					
					Option_Cancel cancel = new Option_Cancel();
					cancel.setText(Translate.取消);
					
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(600);
					mw.setTitle(Translate.易筋丹);
					mw.setDescriptionInUUB(Translate.translateString(Translate.消耗易筋丹XX个, new String[][] { { Translate.COUNT_1, String.valueOf(costNums) }}));
					mw.setOptions(new Option[] { option, cancel });
					QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					player.addMessageToRightBag(res);
					return null;
				}
				//扣除
				boolean allSucc = true;
				for (int i = 0; i < costNums; i++) {
					int idx = bag.indexOf(Translate.易筋丹, true);
					if(idx < 0){
						idx = bag.indexOf(Translate.易筋丹, false);
					}
					ArticleEntity ae = bag.remove(idx, "宠物飞升吃", true);
					if (ae != null) {
						// 统计
						ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, "宠物飞升吃", null);
						logger.info("[宠物吃易筋丹] [删除道具:{}] [成功] [宠物id:{}] [格子:{}] [需要删除数:{}] [正在删除第{}个] [{}]", new Object[] {petId,ae.getArticleName(),idx,costNums,i, player.getLogString4Knap() });
					} else {
						allSucc= false;
						logger.info("[宠物吃易筋丹] [删除道具] [失败] [宠物id:{}] [格子:{}] [需要删除数:{}] [正在删除第{}个] [{}]", new Object[] {petId,idx,costNums,i, player.getLogString4Knap() });
						break;
					}
				}
				
				if(!allSucc){
					return null;
				}
				
				pstate.setEatNums(pstate.getEatNums()+1);
				
				pstate.setXiaoHuaDate(System.currentTimeMillis() + petRule.getCostData()*60*1000);
				
				PetManager.getInstance().savePetFlyState(pstate, petId, player);
				player.sendError(Translate.吃易筋丹成功);
//				if(getQianNengValue > 0){
//					player.sendError(Translate.translateString(Translate.消耗易筋丹成功, new String[][] { { Translate.COUNT_1, String.valueOf(10) }, { Translate.COUNT_2, String.valueOf(50) } }));
//				}else{
//					player.sendError(Translate.translateString(Translate.消耗易筋丹成功2, new String[][] { { Translate.COUNT_1, String.valueOf(10) } }));
//				}
				logger.info("[宠物吃易筋丹] [成功] [petID:{}] [信息:{}] [玩家:{}]",new Object[]{petId,pstate,player.getLogString()});
				player.addMessageToRightBag(new PET_EAT_FLY_PROP_RES(req.getSequnceNum(), petId, 1));
			}
		}
		return null;
	
	}
	
	public ResponseMessage handle_PET_EAT_FLY_PROP_REQ(Connection conn, RequestMessage message, Player player){
		return eatFlyProp(conn, message, player,false);
	}
	
	public ResponseMessage handle_QUERY_PET_FLY_SKILLS_REQ(Connection conn, RequestMessage message, Player player){
		if(message instanceof QUERY_PET_FLY_SKILLS_REQ){
			QUERY_PET_FLY_SKILLS_REQ req = (QUERY_PET_FLY_SKILLS_REQ)message;
			return new QUERY_PET_FLY_SKILLS_RES(req.getSequnceNum(), req.getPetId(), PetManager.petFlySkills.values().toArray(new PetFlySkillInfo[]{}));
		}
		return null;
	}
	
	public ResponseMessage handle_PET_FLY_BUTTON_ONCLICK_REQ(Connection conn, RequestMessage message, Player player){
		if(message instanceof PET_FLY_BUTTON_ONCLICK_REQ){
			PET_FLY_BUTTON_ONCLICK_REQ req = (PET_FLY_BUTTON_ONCLICK_REQ)message;
			long petId = req.getPetId();
			Pet pet = PetManager.getInstance().getPet(petId);
			if(pet != null){
				long ownerId = pet.getOwnerId();
				Player owner = null;
				try {
					owner = playerManager.getPlayer(ownerId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(owner == null){
					logger.warn("[点击宠物飞升按钮] [出错:宠物主人不存在] [{}] [宠物id:{}]",new Object[]{player.getLogString(),String.valueOf(petId)});
					return null;
				}
				int flyState = 0;
				GradePet gradePet = null;
				String skillInfo = Translate.宠物飞升提示85;
				ArticleEntity artE = owner.getArticleEntity(pet.getPetPropsId());
				if (artE != null && artE instanceof PetPropsEntity) {
					PetPropsEntity entity = (PetPropsEntity) artE;
					gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
					if (gradePet != null) {
						if(pet.grade >= gradePet.maxGrade && gradePet.flyType == 1){
							flyState = 2;
						}
					}
				}
				
				if(flyState != 2){
					logger.warn("[点击宠物飞升按钮] [出错:按钮类型不对] [{}] [宠物id:{}]",new Object[]{player.getLogString(),String.valueOf(petId)});
					return null;
				}
				
				PetFlyState pstate = petManager.getPetFlyState(petId, owner);
				if(pstate == null){
					pstate = new PetFlyState();
				}
				
				//操作是否是自己的宠物
				if(player.getId() != owner.getId()){
					if(pstate.getFlyState() != 1){
						return null;
					}
				}
				
				String result = pet.canUpgrade(owner);
				if(!result.equals("ok")){
					player.sendError(result);
					return null;
				}
				
				if(pstate.getLingXingPoint() >= 105){
					skillInfo = Translate.宠物飞升提示105;
				}
				int buttonType = 0;
				if(pstate.getLingXingPoint() >= 105){
					buttonType = 1;
				}
				PetFlySkillInfo info = PetManager.petFlySkills.get(pstate.getSkillId());
				if(info == null){
					info = new PetFlySkillInfo();
				}
				int animation = 1;
				if(pstate.getAnimation() == 1){
					animation = 0;
				}
				pstate.setAnimation(1);
				pstate.setFlyState(1);
				petManager.savePetFlyState(pstate, petId, player);
				int qianNengPoint = 0;
				if(pet.canUpgrade(player).equals("ok")){
					flyState = 3;
				}
				
				if(pstate.getAnimation() == 1){
					flyState = 4;
				}
				if(pstate.getFlyState() == 1){
					flyState = 5;
				}
				if(pstate.getLingXingPoint() >= 105){
					flyState = 6;
				}
				qianNengPoint = pstate.getQianNengPoint();
				
				PET_FLY_STATE_RES res2 = new PET_FLY_STATE_RES(req.getSequnceNum(),petId, flyState, qianNengPoint, "");
				player.addMessageToRightBag(res2);
				
				logger.warn("[点击宠物飞升按钮] [成功] [{}] [flyState:{}] [宠物id:{}] [animation:{}] [avataRace:{}] [avataSex:{}] [lingxingPoint:{}] [skillId:{}] [skillId2:{}] [skillDesc:{}] [buttonType:{}]",new Object[]{player.getLogString(),flyState,String.valueOf(petId),animation,pet.getAvataRace() ,pet.getAvataSex(),pstate.getLingXingPoint(),pstate.getSkillId(),info.getSkillId(),info.getSkillDesc(), buttonType});
				PET_FLY_BUTTON_ONCLICK_RES res = new PET_FLY_BUTTON_ONCLICK_RES(req.getSequnceNum(),gradePet.getFlyAvata() ,animation, buttonType,pstate.getLingXingPoint(), pstate.getXiaoHuaDate(), (7 - pstate.getUseTimes()), skillInfo,pet,info);
				return res;
			}
			logger.warn("[点击宠物飞升按钮] [失败] [pet:{}] [{}] [宠物id:{}]",new Object[]{(pet==null?"null":pet.getName()), player.getLogString(),String.valueOf(petId)});
		}
		return null;
	}
	
	
	/**
	 * 把消息加入到游戏的处理队列，让游戏场景单线程处理
	 * @param player
	 * @param message
	 * @param attachment
	 */
	private void pushMessageToGame(Player player, RequestMessage message, Object attachment) {
		
		Game game = player.getCurrentGame();

		if (game != null) {
			game.messageQueue.push(new PlayerMessagePair(player, message, attachment));
		}  else {
			if(logger.isWarnEnabled())
				logger.warn("[{}] [玩家{}不在任何地图上] [指令{}非法]", new Object[]{getName(),player.getName()});
		}
	}

	
	public boolean handleResponseMessage(Connection conn, RequestMessage req, ResponseMessage message) throws ConnectionException, Exception {
		return false;
	}
}
