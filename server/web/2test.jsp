
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.core.PetSubSystem"%>
<%@page import="com.fy.engineserver.core.PetExperienceManager"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.jiazu.petHouse.PetHouseManager"%>
<%@page import="com.fy.engineserver.util.config.ConfigServiceManager"%>
<%@page import="java.io.File"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.jiazu.petHouse.*"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@page import="com.fy.engineserver.shop.Shop"%><%@page import="java.util.ArrayList"%><%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewMoneyActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
List<HouseData> ds = PetHouseManager.getInstance().getPlayerData(1205000000000042076L);
Player p = PlayerManager.getInstance().getPlayer(1205000000000042076L);
for(HouseData d : ds){
	recallPet(d.getPetId(),p);
}
%>
<%!
public synchronized void recallPet(long petId,Player player){
	if(petId <= 0 || player == null){
		PetManager.logger.warn("[收回挂机宠物] [失败:参数错误] [petId:{}] [player:{}]",new Object[]{petId ,(player!=null?player.getLogString():"null")});
		return;
	}
	
	Pet pet = PetManager.getInstance().getPet(petId);
	if(pet == null){
		PetManager.logger.warn("[收回挂机宠物] [失败:宠物不存在] [petId:{}] [player:{}]",new Object[]{petId ,player.getLogString()});
		return;
	}
	if(pet.isSummoned()){
		player.sendError(Translate.text_pet_37);
		return;
	}
	
	List<HouseData> datas = PetHouseManager.getInstance().getPlayerData(player.getId());
	if(datas == null || datas.size() <= 0){
		PetManager.logger.warn("[收回挂机宠物] [失败:宠物挂机信息不存在] [petId:{}] [player:{}]",new Object[]{petId ,player.getLogString()});
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
		PetManager.logger.warn("[收回挂机宠物] [失败:宠物挂机信息不存在2] [petId:{}] [datas:{}] [player:{}]",new Object[]{petId ,datas.size(), player.getLogString()});
		return;
	}
	
	Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
	if(jiazu == null){
		PetManager.logger.warn("[收回挂机宠物] [失败:家族不存在] [jiazuid:{}] [petId:{}] [datas:{}] [player:{}]",new Object[]{player.getJiazuId(),petId ,datas.size(), player.getLogString()});
		return;
	}
	
	try {
		List<Long> blessIds = data.getBlessIds();
		blessIds.clear();
		data.setBlessIds(blessIds);
		data.setBlessCount(0);
		data.setCallBack(true);
		long addExp = PetHouseManager.getInstance().getStoreExp(jiazu.getLevel(), pet.getLevel(), data.getStartStoreTime(),data.getBlessCount());
		pet.addExp(addExp, PetExperienceManager.ADDEXP_REASON_HOOK);
		data.setStartStoreTime(0);
		//PetSubSystem.getInstance().handlerPetHousePage(player);
		player.sendError(Translate.宠物召回成功);
		PetManager.logger.warn("[收回挂机宠物] [成功] [pdatas:"+datas.size()+"] [addExp:{}] [祝福改宠物的玩家:{}] [data:{}] [pet:{}] [player:{}]",
				new Object[]{addExp,blessIds,data, pet.getLogString() ,player.getLogString()});
	} catch (Exception e) {
		e.printStackTrace();
		PetManager.logger.warn("[收回挂机宠物] [异常] [data:{}] [pet:{}] [player:{}] [{}]",new Object[]{data, pet.getLogString() ,player.getLogString(),e});
	}
}
%>