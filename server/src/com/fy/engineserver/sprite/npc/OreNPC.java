package com.fy.engineserver.sprite.npc;

import java.util.Arrays;
import java.util.Hashtable;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.village.data.OreNPCData;
import com.fy.engineserver.activity.village.data.VillageFightTimeBar;
import com.fy.engineserver.activity.village.manager.VillageFightManager;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.villagefight.Option_VillageFight_lingqubangyin;
import com.fy.engineserver.menu.villagefight.Option_VillageFight_lingqujiazuzijin;
import com.fy.engineserver.menu.villagefight.Option_VillageFight_lingquyinzi;
import com.fy.engineserver.menu.villagefight.Option_VillageFight_shenqing;
import com.fy.engineserver.menu.villagefight.Option_VillageFight_shuoming;
import com.fy.engineserver.menu.villagefight.Option_VillageFight_xiugaikuangming;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 矿npc
 * 
 *
 */
public class OreNPC extends NPC implements FightableNPC{

	/**
	 * 
	 */
	private static final long serialVersionUID = 64281266653370996L;

	/**
	 * 数组下标，即在矿数组的位置，这个位置固定，一旦生成就不更改了
	 */
	public int arrayIndex;
	
	/**
	 * 占领家族id
	 */
	public long jiazuId;
	
	/**
	 * 占领后族长修改的矿名
	 */
	public String newName;
	
	public transient long 暂时占领帮派id;
	
	long 申请占领时间;

	/**
	 * 怪出生后，调用此初始化方法
	 */
	public void init(){
		super.init();

	}

	public void 点击弹出菜单(Player player){
		if(this.country != player.getCountry()){
			return;
		}
		if(player.getState() == Player.STATE_DEAD){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.死了就老实点);
			player.addMessageToRightBag(hreq);
			return;
		}
		if(VillageFightManager.开战){
			if (player.getLevel() <= 40) {
				player.sendError(Translate.低于40级以下的玩家不能参加此活动);
				return;
			}
			VillageFightManager vfm = VillageFightManager.getInstance();
			if(vfm != null && vfm.vd != null){
				Hashtable<Byte,OreNPCData[]> countryOres = vfm.vd.countryOres;
				Hashtable<Byte,long[]> countryPrepareToFightOres = vfm.vd.countryPrepareToFightOres;
				//国家所有矿数据
				OreNPCData[] onds = countryOres.get(this.country);
				//攻打过该矿的家族id
				long[] ids = countryPrepareToFightOres.get(this.country);
				if(onds != null && ids != null){
					if(arrayIndex >= 0 && arrayIndex < onds.length && arrayIndex < ids.length){
						OreNPCData ond = onds[arrayIndex];
						long prepareId = ids[arrayIndex];
						if(ond != null){
							VillageFightManager.logger.warn("[点击矿战npc] [test] [jiazuid:"+ond.getJiazuId()+"] [country:"+this.country+"] [arrayIndex:"+arrayIndex+"] [prepareId:"+prepareId+"] [ids:"+Arrays.toString(ids)+"]");
							if(ond.getJiazuId() > 0 && prepareId > 0){
								if(player.getJiazuId() == ond.getJiazuId() || player.getJiazuId() == prepareId){
									if(player.getCurrentGame().gi.getName().equals(vfm.countryMapNames[ond.getCountry()][arrayIndex/2])){
										long length = (vfm.points2[ond.getCountry()][arrayIndex][0] - player.getX())*(vfm.points2[ond.getCountry()][arrayIndex][0] - player.getX())+(vfm.points2[ond.getCountry()][arrayIndex][1] - player.getY())*(vfm.points2[ond.getCountry()][arrayIndex][1] - player.getY());
//										if(length < VillageFightManager.大圈半径*VillageFightManager.大圈半径){
											//弹出占领框
											点击占领灵矿(player);
											NOTICE_CLIENT_READ_TIMEBAR_REQ req = new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), VillageFightManager.占领所需时间, Translate.占领中);
											player.addMessageToRightBag(req);
											return;
//										}
									}
								}
							}
							if(ond.getJiazuId() <= 0 && prepareId > 0){
								VillageFightManager.logger.warn("[点击矿战npc] [test2] [pjiazuid:"+player.getJiazuId()+"] [jiazuid:"+ond.getJiazuId()+"] [arrayIndex:"+arrayIndex+"] [prepareId:"+prepareId+"] [playerName:"+player.getName()+"]");
								if(player.getJiazuId() == prepareId){
									if(player.getCurrentGame().gi.getName().equals(vfm.countryMapNames[ond.getCountry()][arrayIndex/2])){
										long length = (vfm.points2[ond.getCountry()][arrayIndex][0] - player.getX())*(vfm.points2[ond.getCountry()][arrayIndex][0] - player.getX())+(vfm.points2[ond.getCountry()][arrayIndex][1] - player.getY())*(vfm.points2[ond.getCountry()][arrayIndex][1] - player.getY());
//										if(length < VillageFightManager.大圈半径*VillageFightManager.大圈半径){
											//弹出占领框
											点击占领灵矿(player);
											NOTICE_CLIENT_READ_TIMEBAR_REQ req = new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), VillageFightManager.占领所需时间, Translate.占领中);
											player.addMessageToRightBag(req);
											return;
//										}
									}
								}
							}
						}
					}
					VillageFightManager.logger.warn("[点击矿战npc] [错误0] [arrayIndex:"+arrayIndex+"] [countryOres:"+countryOres.size()+"] [countryPrepareToFightOres:"+countryPrepareToFightOres+"] [onds:"+(onds!=null?onds.length:"NULL")+"] [ids:"+(ids!=null?ids.length:"NULL")+"]");
				}
				VillageFightManager.logger.warn("[点击矿战npc] [错误1] [countryOres:"+countryOres.size()+"] [countryPrepareToFightOres:"+countryPrepareToFightOres+"] [onds:"+(onds!=null?onds.length:"NULL")+"] [ids:"+(ids!=null?ids.length:"NULL")+"]");
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.忙着呢别捣乱);
				player.addMessageToRightBag(hreq);
			}else{
				VillageFightManager.logger.warn("[点击矿战npc] [错误2] [vfm:"+vfm+"]");
			}
		}else{
			//弹出一般菜单
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setTitle(Translate.灵矿操作);
			mw.setDescriptionInUUB(Translate.灵矿操作详细);
			mw.setNpcId(this.getId());
			Option_VillageFight_shenqing option1 = new Option_VillageFight_shenqing();
			option1.setText(Translate.申请攻打灵矿);
			option1.setColor(0xFFFFFF);
			option1.arrayIndex = arrayIndex;
			
			Option_VillageFight_xiugaikuangming option2 = new Option_VillageFight_xiugaikuangming();
			option2.setText(Translate.修改灵矿名称);
			option2.setColor(0xFFFFFF);
			option2.oreNPC = this;
			
			Option_VillageFight_lingquyinzi option3 = new Option_VillageFight_lingquyinzi();
			option3.setText(Translate.族长领取银子);
			option3.setColor(0xFFFFFF);
			option3.oreNPC = this;
			
			Option_VillageFight_lingqujiazuzijin option4 = new Option_VillageFight_lingqujiazuzijin();
			option4.setText(Translate.族长领取家族资金);
			option4.setColor(0xFFFFFF);
			option4.oreNPC = this;
			
			Option_VillageFight_lingqubangyin option5 = new Option_VillageFight_lingqubangyin();
			option5.setText(Translate.族员领取绑银);
			option5.setColor(0xFFFFFF);
			option5.oreNPC = this;
			
			Option_VillageFight_shuoming option6 = new Option_VillageFight_shuoming();
			option6.setText(Translate.灵矿争夺战说明);
			option6.setColor(0xFFFFFF);

			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.取消);
			cancel.setColor(0xFFFFFF);
			mw.setOptions(new Option[]{option1,option2,option3,option4,option5,option6,cancel});
			QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(req);
		}
	}

	/**
	 * 点击npc，然后走进度条，走完后占领灵矿
	 * @param player
	 */
	public void 点击占领灵矿(Player player){
		VillageFightTimeBar vft = new VillageFightTimeBar(player, this);
		if(player.getTimerTaskAgent() != null){
			player.getTimerTaskAgent().createTimerTask(vft, VillageFightManager.占领所需时间, TimerTask.type_村庄战);
		}
	}
	
	public synchronized void 申请占领灵矿(Player player){
		if(player == null){
			return;
		}
		String result = 申请占领灵矿合法性判断(player);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, result);
			player.addMessageToRightBag(hreq);
			if(VillageFightManager.logger.isInfoEnabled()){
				VillageFightManager.logger.info("[{}] [点击占领灵矿] [失败] [矿id:{}] [playerJiazuId:{}] [{}]", new Object[]{player.getLogString(),this.getId(),player.getJiazuId(),result});
			}
			return;
		}
		boolean changeFlag = false;
		if(this.暂时占领帮派id != player.getJiazuId()){
			changeFlag = true;
			try{
				if(player.getJiazuId() == jiazuId){
					this.setTitle(Translate.防守方暂时占领);
				}else{
					this.setTitle(Translate.进攻方暂时占领);
				}
			}catch(Exception ex){
				
			}
		}
		this.暂时占领帮派id = player.getJiazuId();
		申请占领时间 = SystemTime.currentTimeMillis();
		if(changeFlag){
			try{
				VillageFightManager vfm = VillageFightManager.getInstance();
				long prepareFightJiazuId = vfm.vd.countryPrepareToFightOres.get(country)[arrayIndex];
				ChatMessageService cms = ChatMessageService.getInstance();
				JiazuManager jm = JiazuManager.getInstance();
				Jiazu jiazu = jm.getJiazu(player.getJiazuId());
				String description = Translate.translateString(Translate.某玩家的努力暂时占有灵矿, new String[][]{{Translate.PLAYER_NAME_1,player.getName()}});
				cms.sendMessageToJiazu(jiazu, description, "");
				cms.sendMessageToJiazu(jiazu, description, "");
				cms.sendMessageToJiazu(jiazu, description, "");
				if(jiazuId == player.getJiazuId()){
					if(prepareFightJiazuId > 0){
						jiazu = jm.getJiazu(prepareFightJiazuId);
						description = Translate.对方家族暂时占有灵矿;
						cms.sendMessageToJiazu(jiazu, description, "");
						cms.sendMessageToJiazu(jiazu, description, "");
						cms.sendMessageToJiazu(jiazu, description, "");
					}
				}else{
					if(jiazuId > 0){
						jiazu = jm.getJiazu(jiazuId);
						description = Translate.对方家族暂时占有灵矿;
						cms.sendMessageToJiazu(jiazu, description, "");
						cms.sendMessageToJiazu(jiazu, description, "");
						cms.sendMessageToJiazu(jiazu, description, "");
					}
				}
			}catch(Exception ex){
				
			}
		}
		if(VillageFightManager.logger.isInfoEnabled()){
			VillageFightManager.logger.info("[{}] [点击占领灵矿] [成功] [矿id:{}] [playerJiazuId:{}]", new Object[]{player.getLogString(),this.getId(),player.getJiazuId()});
		}
	}
	
	public String 申请占领灵矿合法性判断(Player player){
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null){
			return "";
		}
		if(player.getSoulLevel() < 45){
			return Translate.占领矿需要等级达到45级;
		}
		if(!VillageFightManager.开战){
			return Translate.现在不是开战时间;
		}
		long prepareFightJiazuId = 0;
		try{
			prepareFightJiazuId = vfm.vd.countryPrepareToFightOres.get(country)[arrayIndex];
		}catch(Exception ex){
			
		}
		if(jiazuId <= 0 || prepareFightJiazuId <= 0){
			if(jiazuId <= 0 && prepareFightJiazuId <= 0){
				return Translate.不能占领;
			}
			if(jiazuId > 0 && prepareFightJiazuId <= 0){
				return Translate.不能占领;
			}
			if(jiazuId <= 0 && prepareFightJiazuId > 0){
//				return Translate.无需占领;
			}
//			return Translate.不能占领;
		}
		if(player.getJiazuId() != jiazuId && player.getJiazuId() != prepareFightJiazuId){
			return Translate.你没有资格申请占领;
		}
		if(SystemTime.currentTimeMillis() - 申请占领时间 < VillageFightManager.cooldown){
			return Translate.申请占领处于冷却中;
		}
		return null;
	}
	
	public long 得到占领结果(){
		long 暂时占领帮派id = this.暂时占领帮派id;
		this.暂时占领帮派id = 0;
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null){
			return 0;
		}
		long prepareFightJiazuId = 0;
		try{
			prepareFightJiazuId = vfm.vd.countryPrepareToFightOres.get(country)[arrayIndex];
		}catch(Exception ex){
			
		}
		if(jiazuId > 0 && prepareFightJiazuId > 0){
			if(暂时占领帮派id > 0){
				if(jiazuId != 暂时占领帮派id){
					ChatMessageService cms = ChatMessageService.getInstance();
					if(cms != null){
						try{
							JiazuManager jm = JiazuManager.getInstance();
							Jiazu jiazu = jm.getJiazu(jiazuId);
							String description = Translate.贵家族防守失利;
							cms.sendMessageToJiazu(jiazu, description, "");
							cms.sendMessageToJiazu(jiazu, description, "");
							cms.sendMessageToJiazu(jiazu, description, "");
							MailManager mm = MailManager.getInstance();
							mm.sendMail(jiazu.getJiazuMaster(), new ArticleEntity[0], description, description, 0, 0, 0, "");
						}catch(Exception ex){
							if(VillageFightManager.logger.isWarnEnabled())
								VillageFightManager.logger.warn("[得到占领结果发送消息异常]",ex);
						}
						
					}
				}
				jiazuId = 暂时占领帮派id;
				return 暂时占领帮派id;
			}
		}
		if(jiazuId <= 0 && prepareFightJiazuId > 0){
			jiazuId = prepareFightJiazuId;
			return jiazuId;
		}
		return 0;
	}
	
	public void 修改矿名(Player player){
		if(player == null){
			return;
		}
		String result = 修改矿名合法性判断(player);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		//弹出输入框
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.请输入灵矿名);
		mw.setDescriptionInUUB(Translate.请输入灵矿名);
		Option_VillageFight_xiugaikuangming option = new Option_VillageFight_xiugaikuangming();
		option.setText(Translate.请输入灵矿名);
		option.setColor(0xFFFFFF);
		option.oreNPC = this;
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.取消);
		cancel.setColor(0xFFFFFF);
		mw.setOptions(new Option[]{option,cancel});
		INPUT_WINDOW_REQ req = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), title, mw.getDescriptionInUUB(), (byte)2, (byte)15, Translate.请输入灵矿名, Translate.确定, Translate.取消, new byte[0]);
		player.addMessageToRightBag(req);
	}
	
	public void 修改矿名确定(Player player,String oreName){
		if(player == null){
			return;
		}
		String result = 修改矿名合法性判断(player);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		VillageFightManager vfm = VillageFightManager.getInstance();
		OreNPCData[] onds = vfm.vd.countryOres.get(this.getCountry());
		OreNPCData ond = onds[arrayIndex];
		ond.setNewName(oreName);
		this.setName(oreName);
		vfm.vd.setDirty(true);
		String des = Translate.translateString(Translate.您的矿名更改, new String[][]{{Translate.STRING_1,oreName}});
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, des);
		player.addMessageToRightBag(hreq);
	}
	
	public String 修改矿名合法性判断(Player player){
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null || vfm.vd == null){
			return "";
		}
		if(VillageFightManager.开战){
			return Translate.现在是开战时间;
		}
		if(jiazuId <= 0){
			return Translate.您无权进行操作;
		}
		if(jiazuId != player.getJiazuId()){
			return Translate.您无权进行操作;
		}
		JiazuManager jm = JiazuManager.getInstance();
		Jiazu jiazu = jm.getJiazu(jiazuId);
		if(jiazu == null){
			return Translate.家族不存在;
		}
		if(jiazu.getJiazuMaster() != player.getId()){
			return Translate.只有家族族长才可以改名;
		}
		return null;
	}
	
	public synchronized void 族长领取银子(Player player){
		if(player == null){
			return;
		}
		String result = 族长领取银子合法性判断(player);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		int count = 族长领取银子数量();
		try{
			BillingCenter bc = BillingCenter.getInstance();
//			//活动
			CompoundReturn cr = ActivityManagers.getInstance().getValue(6, player);
			int num = 1;
			if (cr != null && cr.getBooleanValue()) {
				num = cr.getIntValue();
			}
			count = count*num;
			VillageFightManager.logger.warn("[族长领取银子] [name:"+player.getName()+"] [num:"+num+"]");
			bc.playerSaving(player, count, CurrencyType.SHOPYINZI, SavingReasonType.ORE, "占领矿给的钱");
		}catch(Exception ex){
			ex.printStackTrace();
			if(VillageFightManager.logger.isWarnEnabled())
				VillageFightManager.logger.warn("[族长领取银子] [给玩家钱异常] [{}] [{}] [{}] [{}]",new Object[]{count,player.getUsername(),player.getId(),player.getName()});
			return;
		}
		VillageFightManager vfm = VillageFightManager.getInstance();
		vfm.vd.everyDayGetSilverJiazuIdMap.put(jiazuId, count);
		vfm.vd.setDirty(true);
		try{
			String description = Translate.领取成功;
			ChatMessageService cms = ChatMessageService.getInstance();
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			description = Translate.translateString(Translate.族长领取了银子, new String[][]{{Translate.COUNT_1,BillingCenter.得到带单位的银两(count)}});
			cms.sendMessageToJiazu(jiazu, description, "");
			description = Translate.translateString(Translate.你领取了银子, new String[][]{{Translate.COUNT_1,BillingCenter.得到带单位的银两(count)}});
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, description);
			player.addMessageToRightBag(hreq);
		}catch(Exception ex){
			
		}

	}
	
	public int 族长领取银子数量(){
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null || vfm.vd == null){
			return 0;
		}
		byte type = vfm.vd.oreType[arrayIndex];
		if(type == VillageFightManager.矿_大_灵脉){
			return VillageFightManager.大矿钱;
		}else if(type == VillageFightManager.矿_中_灵泉){
			return VillageFightManager.中矿钱;
		}else{
			return VillageFightManager.小矿钱;
		}
	}
	
	public String 族长领取银子合法性判断(Player player){
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null || vfm.vd == null){
			return "";
		}
		if(VillageFightManager.开战){
			return Translate.现在是开战时间;
		}
		if(jiazuId <= 0){
			return Translate.您无权进行操作;
		}
		if(jiazuId != player.getJiazuId()){
			return Translate.您无权进行操作;
		}
		JiazuManager jm = JiazuManager.getInstance();
		Jiazu jiazu = jm.getJiazu(jiazuId);
		if(jiazu == null){
			return Translate.家族不存在;
		}
		if(jiazu.getJiazuMaster() != player.getId()){
			return Translate.只有家族族长才可以使用这个功能;
		}
		if(vfm.vd.everyDayGetSilverJiazuIdMap.containsKey(jiazuId)){
			return Translate.您的家族已经领取过银子;
		}
		return null;
	}
	
	public synchronized void 族长领取家族资金(Player player){
		if(player == null){
			return;
		}
		String result = 族长领取家族资金合法性判断(player);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		int count = 族长领取家族资金数量();
		//活动
		CompoundReturn cr = ActivityManagers.getInstance().getValue(6, player);
		int num = 1;
		if (cr != null && cr.getBooleanValue()) {
			num = cr.getIntValue();
		}
		count = count*num;
		VillageFightManager.logger.warn("[族长领取家族资金] [name:"+player.getName()+"] [num:"+num+"]");
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		try{
			jiazu.setJiazuMoney(jiazu.getJiazuMoney()+count);
		}catch(Exception ex){
			ex.printStackTrace();
			if(VillageFightManager.logger.isWarnEnabled())
				VillageFightManager.logger.warn("[族长领取家族资金] [num:"+num+"] [给玩家钱异常] [{}] [{}] [{}] [{}]",new Object[]{count,player.getUsername(),player.getId(),player.getName()});
			return;
		}
		VillageFightManager vfm = VillageFightManager.getInstance();
		vfm.vd.everyDayGetJiazuZiJinJiazuIdMap.put(jiazuId, count);
		vfm.vd.setDirty(true);
		try{
			String description = Translate.领取成功;
			ChatMessageService cms = ChatMessageService.getInstance();
			description = Translate.translateString(Translate.族长领取了家族资金, new String[][]{{Translate.COUNT_1,BillingCenter.得到带单位的银两(count)}});
			cms.sendMessageToJiazu(jiazu, description, "");
			description = Translate.translateString(Translate.你领取了家族资金, new String[][]{{Translate.COUNT_1,BillingCenter.得到带单位的银两(count)}});
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, description);
			player.addMessageToRightBag(hreq);
		}catch(Exception ex){
			
		}
	}
	
	public int 族长领取家族资金数量(){
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null || vfm.vd == null){
			return 0;
		}
		byte type = vfm.vd.oreType[arrayIndex];
		if(type == VillageFightManager.矿_大_灵脉){
			return VillageFightManager.大矿钱;
		}else if(type == VillageFightManager.矿_中_灵泉){
			return VillageFightManager.中矿钱;
		}else{
			return VillageFightManager.小矿钱;
		}
	}
	
	public String 族长领取家族资金合法性判断(Player player){
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null || vfm.vd == null){
			return "";
		}
		if(VillageFightManager.开战){
			return Translate.现在是开战时间;
		}
		if(jiazuId <= 0){
			return Translate.您无权进行操作;
		}
		if(jiazuId != player.getJiazuId()){
			return Translate.您无权进行操作;
		}
		JiazuManager jm = JiazuManager.getInstance();
		Jiazu jiazu = jm.getJiazu(jiazuId);
		if(jiazu == null){
			return Translate.家族不存在;
		}
		if(jiazu.getJiazuMaster() != player.getId()){
			return Translate.只有家族族长才可以使用这个功能;
		}
		if(vfm.vd.everyDayGetJiazuZiJinJiazuIdMap.containsKey(jiazuId)){
			return Translate.您的家族已经领取过家族资金;
		}
		if (jiazu.getJiazuStatus() == JiazuManager2.封印家族功能) {
			return Translate.家族资金不足封印;
		}
		return null;
	}
	
	public synchronized void 族员领取绑银(Player player){
		if(player == null){
			return;
		}
		String result = 族员领取绑银合法性判断(player);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		int count = 族员领取绑银数量();
		//活动
		CompoundReturn cr = ActivityManagers.getInstance().getValue(6, player);
		int num = 1;
		if (cr != null && cr.getBooleanValue()) {
			num = cr.getIntValue();
		}
		count = count*num;
		VillageFightManager.logger.warn("[族员领取绑银] [name:"+player.getName()+"] [num:"+num+"]");
		try{
			BillingCenter bc = BillingCenter.getInstance();
			bc.playerSaving(player, count, CurrencyType.BIND_YINZI, SavingReasonType.ORE, Translate.占领矿给的钱);
		}catch(Exception ex){
			ex.printStackTrace();
			if(VillageFightManager.logger.isWarnEnabled())
				VillageFightManager.logger.warn("[族员领取绑银] [给玩家钱异常] [{}] [{}] [{}] [{}]",new Object[]{count,player.getUsername(),player.getId(),player.getName()});
			return;
		}
		VillageFightManager vfm = VillageFightManager.getInstance();
		vfm.vd.everyDayGetBindSilverPlayerIdMap.put(player.getId(), count);
		vfm.vd.setDirty(true);
		String description = Translate.translateString(Translate.你领取了绑银, new String[][]{{Translate.COUNT_1,BillingCenter.得到带单位的银两(count)}});
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, description);
		player.addMessageToRightBag(hreq);
	}
	
	public int 族员领取绑银数量(){
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null || vfm.vd == null){
			return 0;
		}
		byte type = vfm.vd.oreType[arrayIndex];
		if(type == VillageFightManager.矿_大_灵脉){
			return VillageFightManager.大矿钱;
		}else if(type == VillageFightManager.矿_中_灵泉){
			return VillageFightManager.中矿钱;
		}else{
			return VillageFightManager.小矿钱;
		}
	}
	
	public String 族员领取绑银合法性判断(Player player){
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null || vfm.vd == null){
			return "";
		}
		if(VillageFightManager.开战){
			return Translate.现在是开战时间;
		}
		if(jiazuId <= 0){
			return Translate.您无权进行操作;
		}
		if(jiazuId != player.getJiazuId()){
			return Translate.您无权进行操作;
		}
		JiazuManager jm = JiazuManager.getInstance();
		Jiazu jiazu = jm.getJiazu(jiazuId);
		if(jiazu == null){
			return Translate.家族不存在;
		}
		if(vfm.vd.everyDayGetBindSilverPlayerIdMap.containsKey(player.getId())){
			return Translate.您已经领取过绑银;
		}
		return null;
	}
	
	public synchronized void 族员领取BUFF(Player player){
		if(player == null){
			return;
		}
		String result = 族员领取BUFF合法性判断(player);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		String buffName = 族员领取BUFF名();
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		if(btm != null){
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			if(bt != null){
				Buff buff = bt.createBuff(1);
				buff.setCauser(player);
				buff.setStartTime(SystemTime.currentTimeMillis());
				buff.setInvalidTime(SystemTime.currentTimeMillis() + VillageFightManager.buff_持续时长);
				player.placeBuff(buff);
			}
		}
		VillageFightManager vfm = VillageFightManager.getInstance();
		vfm.vd.everyDayGetBuffPlayerIdMap.put(player.getId(), buffName);
		vfm.vd.setDirty(true);
		String description = Translate.translateString(Translate.你领取了BUFF, new String[][]{{Translate.STRING_1,buffName}});
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, description);
		player.addMessageToRightBag(hreq);
	}
	
	public String 族员领取BUFF名(){
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null || vfm.vd == null){
			return "";
		}
		byte type = vfm.vd.oreType[arrayIndex];
		if(type == VillageFightManager.矿_大_灵脉){
			return VillageFightManager.大矿_BUFF_NAME;
		}else if(type == VillageFightManager.矿_中_灵泉){
			return VillageFightManager.中矿_BUFF_NAME;
		}else{
			return VillageFightManager.小矿_BUFF_NAME;
		}
	}
	
	public String 族员领取BUFF合法性判断(Player player){
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm == null || vfm.vd == null){
			return "";
		}
		if(VillageFightManager.开战){
			return Translate.现在是开战时间;
		}
		if(jiazuId <= 0){
			return Translate.您无权进行操作;
		}
		if(jiazuId != player.getJiazuId()){
			return Translate.您无权进行操作;
		}
		JiazuManager jm = JiazuManager.getInstance();
		Jiazu jiazu = jm.getJiazu(jiazuId);
		if(jiazu == null){
			return Translate.家族不存在;
		}
		if(vfm.vd.everyDayGetBuffPlayerIdMap.containsKey(player.getId())){
			return Translate.您已经领取过BUFF;
		}
		return null;
	}

	long test = 0;
	long lastingHeartbeatTime;
	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		// TODO Auto-generated method stub
		super.heartbeat(heartBeatStartTime, interval, game);
		if(heartBeatStartTime - lastingHeartbeatTime >= 5000){
			给旁边的人设置复活点(game);
		}
	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		OreNPC p = new OreNPC();

		p.cloneAllInitNumericalProperty(this);

		p.country = country;

		p.setnPCCategoryId(this.getnPCCategoryId());

		if (items != null) {
			p.items = new NpcExecuteItem[this.items.length];
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					try {
						p.items[i] = (NpcExecuteItem) items[i].clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		p.windowId = windowId;
		p.id = nextId();
		return p;
	}
	
	private void 给旁边的人设置复活点(Game game){
		if(VillageFightManager.开战){
			VillageFightManager vfm = VillageFightManager.getInstance();
			if(vfm != null && vfm.设置复活点的玩家列表 != null){
				try{
					long prepareFightJiazuId = 0;
					prepareFightJiazuId = vfm.vd.countryPrepareToFightOres.get(country)[arrayIndex];
					if(prepareFightJiazuId <= 0){
						return;
					}
					LivingObject[] los = game.getLivingObjects();
					if(los != null){
						OreNPCData ond = vfm.vd.countryOres.get(this.getCountry())[arrayIndex];
						for(LivingObject l : los){
							if(l instanceof Player){
								if((x-500) <= l.getX() && (x+500) >= l.getX() && (y-500) <= l.getY() && (y+500) >= l.getY()){
									if(vfm.设置复活点的玩家列表.contains(l.getId())){
										Player p = (Player)l;
										if(jiazuId > 0 && jiazuId == p.getJiazuId()){
											p.setResurrectionMapName(game.gi.getName());
											p.setResurrectionX(ond.getRelivePoints()[0]);
											p.setResurrectionY(ond.getRelivePoints()[1]);
										}else if(prepareFightJiazuId == p.getJiazuId()){
											p.setResurrectionMapName(game.gi.getName());
											p.setResurrectionX(ond.getRelivePoints()[2]);
											p.setResurrectionY(ond.getRelivePoints()[3]);
										}
									}
								}
							}
						}
					}
				}catch(Exception ex){
					
				}
			}
		}
	}

	@Override
	public ActiveSkillAgent getActiveSkillAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NPCFightingAgent getNPCFightingAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPursueHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPursueWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void targetDisappear(Fighter target) {
		// TODO Auto-generated method stub
		
	}
}
