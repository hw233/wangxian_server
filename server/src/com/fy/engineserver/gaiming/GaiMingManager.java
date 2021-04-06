package com.fy.engineserver.gaiming;

import java.util.Hashtable;
import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.VALIDATE_DEVICE_INFO_REQ;
import com.fy.engineserver.message.VALIDATE_DEVICE_INFO_RES;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.trade.requestbuy.RequestBuy;
import com.fy.engineserver.trade.requestbuy.RequestBuyInfo4Client;
import com.fy.engineserver.trade.requestbuy.RequestBuyRule;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.boss.client.BossClientService;
import com.fy.boss.message.BossMessageFactory;
import com.fy.boss.message.MIGU_COMMUNICATE_REQ;

public class GaiMingManager {
	public static GaiMingManager instance;
	
	public static long gaimingCD=24*60*60*1000;

	public void init() {
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}

	public static GaiMingManager getInstance() {
		return instance;
	}

	public void setInstance(GaiMingManager instance) {
		this.instance = instance;
	}

	/**
	 * 改名涉及的操作
	 * 1.家族,仙府,拍卖,求购,关系以及关系涉及到的称号,这几个调用UnitedServerManager里面的notifyPlayerChanageName方法修改
	 * 2.战队调用JJCManager里面的changePlayerNameForJJCBillboardData方法修改
	 * 3.本方法修改上面两条之外的改名:仙尊
	 * @param player
	 * @param oldName
	 */
	public void notifyPlayerChanageName(Player player, String oldName) {
		// 仙尊
		FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
		fbm.notifyPlayerChanageName(player, oldName);
		// 玩家宠物
		notifyOtherChangeName(player);

		// 求购
		RequestBuyManager rbm = RequestBuyManager.getInstance();
		Hashtable<Long, RequestBuy> requestBuyMap = rbm.getRequestBuyMap();
		for (Long id : requestBuyMap.keySet()) {
			RequestBuy rb = requestBuyMap.get(id);
			notifyChangeRequestBuy(player, oldName, rb);
		}
		Hashtable<RequestBuyRule, List<RequestBuy>> underRuleMap = rbm.getUnderRuleMap();
		for (RequestBuyRule rbr : underRuleMap.keySet()) {
			List<RequestBuy> buyList = underRuleMap.get(rbr);
			for (RequestBuy rb : buyList) {
				notifyChangeRequestBuy(player, oldName, rb);
			}
		}
		Hashtable<Long, List<RequestBuy>> playerRuleMap = rbm.getPlayerRuleMap();
		for (Long id : playerRuleMap.keySet()) {
			List<RequestBuy> buyList = playerRuleMap.get(id);
			for (RequestBuy rb : buyList) {
				notifyChangeRequestBuy(player, oldName, rb);
			}
		}

		// 通知米谷
		String[] info = new String[4];
		info[0] = "updateusername";
		info[1] = player.getUsername();
		info[2] = player.getId()+"";
		info[3] = player.getName();
		MIGU_COMMUNICATE_REQ req = new MIGU_COMMUNICATE_REQ(BossMessageFactory.nextSequnceNum(), info);
		try {
			BossClientService.getInstance().sendMessageAndWithoutResponse(req);
		} catch (Exception e) {
			GamePlayerManager.logger.warn("[角色改名] [通知米谷异常] [" + player.getLogString() + "] [oldName:" + oldName + "]",e);
			e.printStackTrace();
		}
		// 排行榜
		BillboardsManager bbm = BillboardsManager.getInstance();
		// bbm.notifyBillboardChangeName(menu, subMenu, playerOldName, player, index);
	}

	public void notifyChangeRequestBuy(Player player, String oldName, RequestBuy rb) {
		if (rb != null && rb.getReleaserName().equals(oldName) && rb.getReleaserId() == player.getId()) {
			GamePlayerManager.logger.warn("[角色改名] [已发布的求购列表] [RequestBuy] [" + player.getLogString() + "] [oldName:" + oldName + "]");
			rb.setReleaserName(player.getName());
			RequestBuyInfo4Client rb4c = rb.getRequestBuyInfo4Client();
			if (rb4c != null && rb4c.getReleasePlayerName().equals(oldName)) {
				GamePlayerManager.logger.warn("[角色改名] [已发布的求购列表] [RequestBuyInfo4Client] [" + player.getLogString() + "] [oldName:" + oldName + "]");
				rb4c.setReleasePlayerName(player.getName());
			}
		}
	}

	/**
	 * 改名涉及的宠物名变化
	 * @param player
	 */
	public void notifyOtherChangeName(Player player) {
		// 出战的宠物改名:某某玩家的灵狐
		long activePetId = player.getActivePetId();
		if (activePetId > 0) {
			Pet pet = PetManager.getInstance().getPet(activePetId);
			if (pet != null) {
				pet.setOwnerName(player.getName());
				GamePlayerManager.logger.warn("[角色改名] [出战宠物修改主人名] [petid:" + activePetId + "] [petName:" + pet.getName() + "]");
			}
		}

		// Knapsack knapsack = player.getPetKnapsack();
		// if (knapsack != null) {
		// Cell[] cells = knapsack.getCells();
		// for (int i = 0; i < cells.length; i++) {
		// Cell cell = cells[i];
		// long entityId = cell.getEntityId();
		// if (entityId > 0) {
		// ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(entityId);
		// if (ae != null) {
		// if(ae != null){
		// long petId=0l;
		// if(ae instanceof PetPropsEntity){
		// PetPropsEntity ppe = (PetPropsEntity)ae;
		// petId = ppe.getPetId();
		// }else if(ae instanceof PetEggPropsEntity){
		// PetEggPropsEntity ppp = (PetEggPropsEntity)ae;
		// petId = ppp.getPetId();
		// }else{
		// GamePlayerManager.logger.warn(player.getLogString()+"[角色改名] [宠物找不到实体] [aeId:"+entityId+"]");
		// }
		// if(petId>0){
		// Pet pet = PetManager.getInstance().getPet(petId);
		// pet.setOwnerName(player.getName());
		// GamePlayerManager.logger.warn(player.getLogString()+"[角色改名] [更新宠物主人名] [petId:"+petId+"] [ownerName:"+pet.getOwnerName()+"]");
		// }
		// }
		// }
		// }
		// }
		// }
	}

	/**
	 * 验证用户的设备合法性
	 * 0 是无效
	 * 1 是有效
	 */
	public static int validateDevice(String[] infos) {
		String username = infos[1];
		MieshiGatewayClientService gatewayClientService = MieshiGatewayClientService.getInstance();

		VALIDATE_DEVICE_INFO_REQ req = new VALIDATE_DEVICE_INFO_REQ(GameMessageFactory.nextSequnceNum(), infos);
		try {
			VALIDATE_DEVICE_INFO_RES res = (VALIDATE_DEVICE_INFO_RES) gatewayClientService.sendMessageAndWaittingResponse(req, 60 * 1000);
			if (res == null) {
				ActivitySubSystem.logger.warn("[角色改名] [验证设备合法性] [出错] [网关返回协议为null] [" + username + "]");
				return 0;
			}

			String[] infoes = res.getInfos();
			if (infoes == null) {
				ActivitySubSystem.logger.warn("[角色改名] [验证设备合法性] [出错] [返回infos为null] [" + username + "]");
				return 0;
			}

			if (infoes.length > 0) {
				if ("true".equals(infoes[0])) {
					ActivitySubSystem.logger.warn("[角色改名] [验证设备合法性] [成功] [是合法设备] [" + username + "]");
					return 1;
				} else {
					ActivitySubSystem.logger.warn("[角色改名] [验证设备合法性] [失败] [不是合法设备] [" + username + "]");
					return 0;
				}
			} else {
				ActivitySubSystem.logger.warn("[角色改名] [验证设备合法性] [出错] [返回infos长度为0] [" + username + "]");
				return 0;
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[角色改名] [验证设备合法性] [出错] [出现未知异常] [" + username + "]", e);
			return 0;
		}

	}
}
