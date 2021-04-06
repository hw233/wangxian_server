package com.fy.engineserver.jiazu.petHouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;

import com.fy.engineserver.core.PetExperienceManager;
import com.fy.engineserver.core.PetSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class PetHouseManager {

	private Logger logger = PetManager.logger;
	private long MAX_STORE_TIME = 1000L * 60 * 60 * 48;
	private Map<Long,List<HouseData>> jiazuData = new ConcurrentHashMap<Long,List<HouseData>>();
	private Map<Long,List<HouseData>> playerData = new ConcurrentHashMap<Long,List<HouseData>>();
	
	public static final ReentrantLock blessLock = new ReentrantLock();
	public static SimpleEntityManager<HouseData> petEm;
	
	private static PetHouseManager self;
	public static PetHouseManager getInstance(){
		return self;
	}
	
	public void init(){
		self = this;
		petEm = SimpleEntityManagerFactory.getSimpleEntityManager(HouseData.class);
	}
	
	public boolean petIsStore(Player player,Pet pet){
		List<HouseData> datas = PetHouseManager.getInstance().getPlayerData(player.getId());
		if(datas != null && datas.size() > 0){
			for(HouseData data : datas){
				if(data.getPetId()==pet.getId()&& !data.isCallBack()){
					return true;
				}
			}
		}
		return false;
	}
	
	public synchronized HouseData storePet(Player player,Pet pet,Jiazu jiazu){
		HouseData data = null;
		boolean isNew = false;
		try {
			data = petEm.find(pet.getId());
			if(data == null){
				isNew = true;
				data = new HouseData(player.getId(), pet.getId(),pet.getName(), jiazu.getJiazuID(), player.getName(),pet.getIcon(), pet.getLevel());
			}
			List<HouseData> jList = getJiazuDatas(jiazu.getJiazuID());
			List<HouseData> pList = playerData.get(new Long(player.getId()));
			if(jList == null){
				jList = new ArrayList<HouseData>();
			}
			if(pList == null){
				pList = new ArrayList<HouseData>();
			}
			if(data.getPlayerId() != player.getId()){
				data.setPlayerId(player.getId());
			}
			if(!data.getPlayerName().equals(player.getName())){
				data.setPlayerName(player.getName());
			}
			data.setCallBack(false);
			data.setStartStoreTime(SystemTime.currentTimeMillis());
			pList.add(data);
			jList.add(data);
			jiazuData.put(jiazu.getJiazuID(), jList);
			playerData.put(player.getId(), pList);
			logger.warn("[宠物挂机] [存库成功] [isNew:{}] [jiazuData:{}] [playerData:{}] [data:{}] [{}] [{}] [{}]",new Object[]{isNew,jList.size(), pList.size(), data,pet.getLogString(), player.getLogString()});
			petEm.save(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[宠物挂机] [存库异常] [isNew:{}] [jiazuData:{}] [playerData:{}] [data:{}] [{}] [{}] [{}]",new Object[]{isNew,jiazuData.size(), playerData.size(), data,pet.getLogString(), player.getLogString(),e});
		}
		return data;
	}
	
	public List<HouseData> getJiazuDatas(long  jiazuId){
		List<HouseData> list = jiazuData.get(new Long(jiazuId));
		if(list == null){
			list = new ArrayList<HouseData>();
		}
		if(list.size() == 0){
			long now = System.currentTimeMillis();
			try {
				list = petEm.query(HouseData.class, " jiaZuId = " + jiazuId + " and startStoreTime > 0", "blessCount DESC ", 1, 4000);
				logger.warn("[获取家族宠物挂机信息] [成功] [数量:{}] [jiazuId:{}] [cost:{}]",new Object[]{(list!=null?list.size():"null"),jiazuId,(System.currentTimeMillis()-now)});
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("[获取家族宠物挂机信息] [异常] [数量:{}] [jiazuId:{}] [cost:{}] [{}]",new Object[]{(list!=null?list.size():"null"),jiazuId,(System.currentTimeMillis()-now),e});
			}
		}
		List<HouseData> list2 = new ArrayList<HouseData>();
		for(HouseData d : list){
			if(!d.isCallBack()){
				list2.add(d);
			}
		}
		jiazuData.put(jiazuId, list2);
		return list2;
	}
	
	public HouseData getDataByPetId(long petId){
		try {
			List<HouseData> list = petEm.query(HouseData.class, " petId = " + petId + " and startStoreTime > 0", "petLevel DESC ", 1, 10);
			if(list != null && list.size() > 0){
				return list.get(0);
			}
			logger.warn("[根据宠物id获取宠物挂机信息] [成功] [数量:{}] [petId:{}] ",new Object[]{(list!=null?list.size():"null"),petId});
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[根据宠物id获取宠物挂机信息] [异常] [petId:{}] [{}]",new Object[]{petId,e});
		}
		return null;
	}
	
	public List<HouseData> getPlayerData(long playerId){
		List<HouseData> list = playerData.get(new Long(playerId));
		if(list == null || list.size() == 0){
			try {
				list = petEm.query(HouseData.class, " playerId = " + playerId + " and startStoreTime > 0", "startStoreTime DESC ", 1, 10);
				logger.warn("[获取玩家宠物挂机信息] [成功] [数量:{}] [playerId:{}]",new Object[]{(list!=null?list.size():"null"),playerId});
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("[获取玩家宠物挂机信息] [异常] [数量:{}] [playerId:{}] [{}]",new Object[]{(list!=null?list.size():"null"),playerId,e});
			}
		}
		List<HouseData> list2 = new ArrayList<HouseData>();
		for(HouseData d : list){
			if(!d.isCallBack()){
				list2.add(d);
			}
		}
		playerData.put(playerId, list2);
		return list2;
	}
	
	/**
	 * 收回
	 */
	public synchronized void recallPet(long petId,Player player){
		if(petId <= 0 || player == null){
			logger.warn("[收回挂机宠物] [失败:参数错误] [petId:{}] [player:{}]",new Object[]{petId ,(player!=null?player.getLogString():"null")});
			return;
		}
		
		Pet pet = PetManager.getInstance().getPet(petId);
		if(pet == null){
			logger.warn("[收回挂机宠物] [失败:宠物不存在] [petId:{}] [player:{}]",new Object[]{petId ,player.getLogString()});
			return;
		}
		if(pet.isSummoned()){
			player.sendError(Translate.text_pet_37);
			return;
		}
		
		List<HouseData> datas = getPlayerData(player.getId());
		if(datas == null || datas.size() <= 0){
			logger.warn("[收回挂机宠物] [失败:宠物挂机信息不存在] [petId:{}] [player:{}]",new Object[]{petId ,player.getLogString()});
			return;
		}
		
		HouseData data = null;
		for(HouseData d : datas){
			if(d != null && (d.getPetId() == petId) && (d.getPlayerId() == player.getId())){
				data = d;
				break;
			}
		}
		if(data == null){
			logger.warn("[收回挂机宠物] [失败:宠物挂机信息不存在2] [petId:{}] [datas:{}] [player:{}]",new Object[]{petId ,datas.size(), player.getLogString()});
			return;
		}
		
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if(jiazu == null){
			logger.warn("[收回挂机宠物] [失败:家族不存在] [jiazuid:{}] [petId:{}] [datas:{}] [player:{}]",new Object[]{player.getJiazuId(),petId ,datas.size(), player.getLogString()});
			return;
		}
		
		try {
			List<Long> blessIds = data.getBlessIds();
			blessIds.clear();
			data.setBlessIds(blessIds);
			data.setBlessCount(0);
			data.setCallBack(true);
			long addExp = getStoreExp(jiazu.getLevel(), pet.getLevel(), data.getStartStoreTime(),data.getBlessCount());
			pet.addExp(addExp, PetExperienceManager.ADDEXP_REASON_HOOK);
			data.setStartStoreTime(0);
			PetSubSystem.getInstance().handlerPetHousePage(player);
			player.sendError(Translate.宠物召回成功);
			logger.warn("[收回挂机宠物] [成功] [pdatas:"+datas.size()+"/"+playerData.size()+"] [addExp:{}] [祝福改宠物的玩家:{}] [data:{}] [pet:{}] [player:{}]",
					new Object[]{addExp,blessIds,data, pet.getLogString() ,player.getLogString()});
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[收回挂机宠物] [异常] [data:{}] [pet:{}] [player:{}] [{}]",new Object[]{data, pet.getLogString() ,player.getLogString(),e});
		}
	}
	
	/**
	 * ((基础经验  * 经验系数  * 挂架时间  )/ 1000) * 祝福系数
	 * 祝福系数 : 祝福一次2%
	 * 经验系数 ：和家族等级有关1-20(大于20)
	 * @return
	 */
	public long getStoreExp(int jiazuLevel,int petLevel,long startStoreTime,int blessCount){
		long sTime = SystemTime.currentTimeMillis() - startStoreTime;
		long storeTime = sTime > MAX_STORE_TIME ? MAX_STORE_TIME : sTime;
		int jlevel = jiazuLevel > 20 ? 20 : jiazuLevel;
		double parm1 = JiazuManager2.instance.petOtherExpMap.get(jlevel);
		double parm2 = JiazuManager2.instance.petBasicExpMap.get(petLevel);
		double parm3 = JiazuManager2.instance.petBlessExpMap.get(blessCount) / 100;
		if(parm1 == 0 || parm2 == 0 || storeTime == 0){
			logger.warn("[计算挂机经验] [失败:计算挂机经验参数错误] [parm1:{}] [parm2:{}] [parm3:{}] [storeTime:{}] [jiazuLevel:{}] [petLevel:{}]",
					new Object[]{parm1,parm2,parm3,storeTime,jiazuLevel,petLevel });
			return 0;
		}
		int addExp = (int)((parm1 * parm2 * storeTime) / 1000 * (1 + parm3));
		logger.warn("[计算挂机经验] [成功] [addExp:{}] [parm1:{}] [parm2:{}] [parm3:{}] [storeTime:{}] [jiazuLevel:{}] [blessCount:{}] [petLevel:{}]",
				new Object[]{addExp,parm1,parm2,parm3,storeTime,jiazuLevel,blessCount,petLevel });
		return addExp;
	}
	
	
}
