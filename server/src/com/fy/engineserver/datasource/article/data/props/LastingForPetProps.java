package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.util.Utils;

/**
 * 有时间持续的道具 宠物的血瓶
 * 
 * 修改某些属性，保持一段时间，时间过后，修改消失
 * 此道具都是产生所谓一个BUFF
 * 
 * 
 *
 */
public class LastingForPetProps extends Props{

	
	private int value;
	
	public static final int PLANT_TYPE_SELF = 0;
	public static final int PLANT_TYPE_MONSTER = 1;
	public static final int PLANT_TYPE_NPC_ENEMY = 2;
	public static final int PLANT_TYPE_NPC_FRIEND = 3;
	public static final int PLANT_TYPE_NPC_FULL = 4;
	public static final int PLANT_TYPE_PLAYER_ENEMY = 5;
	public static final int PLANT_TYPE_PLAYER_FRIEND = 6;
	public static final int PLANT_TYPE_PLAYER_FULL = 7;
	public static final int PLANT_TYPE_LIVINGOBJECT = 8;
	public static final int PLANT_TYPE_LIVINGPET = 9;
	/**
	 * 种植类型:
	 * 0只用于自己
	 * 1只用于怪物
	 * 2只用于敌对npc
	 * 3只用于友方npc
	 * 4只用于全体npc
	 * 5只用于敌对玩家
	 * 6只用于友方玩家包括自己
	 * 7只用于全体玩家
	 * 8只用于全体生物
	 */
	protected int plantType = 9;
	
	/**
	 * 范围，半径，像素
	 */
	protected int range;

	/**
	 * 每一个级别的技能，使用的Buff的名称，
	 * 此名称必须索引到一个存在的Buff
	 */
	protected String buffName;

	/**
	 * 每一个级别的技能，通过buffName确定要使用的Buff后，
	 * 指定这个buff的级别，用于表示buff的威力。
	 * 为了表示方便以及策划编辑方便，所以buffLevel从1开始
	 */
	protected int buffLevel;

	/**
	 * 每一个级别的技能，通过buffName确定要使用的Buff后，
	 * 指定这个buff持续的时间（单位秒）
	 */
	protected long buffLastingTime;

	public int getPlantType() {
		return plantType;
	}

	public void setPlantType(int plantType) {
		this.plantType = plantType;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public int getBuffLevel() {
		return buffLevel;
	}

	public void setBuffLevel(int buffLevel) {
		this.buffLevel = buffLevel;
	}

	public long getBuffLastingTime() {
		return buffLastingTime;
	}

	public void setBuffLastingTime(long buffLastingTime) {
		this.buffLastingTime = buffLastingTime;
	}

	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity ae){
		if(!super.use(game,player,ae)){
			return false;
		}
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate bt = btm.getBuffTemplateByName(buffName);
		if(bt == null){
//			ArticleManager.logger.warn("[使用道具] [BUFF道具] [失败] ["+player.getName()+"] ["+this.getName()+"] ["+buffName+"] [buff不存在]");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[使用道具] [BUFF道具] [失败] [{}] [{}] [{}] [buff不存在]", new Object[]{player.getName(),this.getName(),buffName});
			return false;
		}
		Pet pet = PetManager.getInstance().getPet(player.getActivePetId());
		if(pet != null){
			String result = plantBuffOnLivingObject(bt,pet,player);
			if(!result.equals("")){
				player.sendError(result);
				return false;
			}
		}else{
			player.sendError(Translate.translateString(Translate.没有指定宠物不能使用, new String[][]{{Translate.STRING_1,this.getName()}}));
			return false;
		}
		if(ArticleManager.logger.isDebugEnabled()){
//			ArticleManager.logger.debug("[使用道具] [BUFF道具] [成功] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+this.getName()+"]");
			if(ArticleManager.logger.isDebugEnabled())
				ArticleManager.logger.debug("[使用道具] [BUFF道具] [成功] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getName(),player.getId(),this.getName()});
		}
		return true;
	}
	
	
	public String plantBuffOnLivingObject(BuffTemplate bt,Fighter livingObject,Player causer){
		
		if(livingObject.getHp() == livingObject.getMaxHP()){
			return Translate.此宠物气血已经达到最大;
		}
		//因为buffLevel最小从1开始，所以需要进行-1操作
		Buff buff = bt.createBuff(buffLevel-1);
		buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		buff.setInvalidTime(buff.getStartTime() + buffLastingTime * 1000);
		buff.setLevel(buffLevel-1);
		buff.setGroupId(bt.getGroupId());
		buff.setCauser(causer);
		livingObject.placeBuff(buff);
		if (PetManager.logger.isDebugEnabled()) {
			PetManager.logger.debug("[宠物喂养加持续buff] [成功] ["+livingObject.getName()+"] ["+causer.getLogString()+"]");
		}
		
		if(ArticleManager.logger.isDebugEnabled()){
//			ArticleManager.logger.debug("[道具效果] [BUFF道具] [成功] ["+causer.getName()+"] ["+this.getName()+"] ["+buff.getDescription()+"] [级别:"+buff.getLevel()+"] [持续时间："+buffLastingTime+"秒] [施加BUFF到"+livingObject.getName()+"身上] ["+livingObject.getName()+"] ["+livingObject.getId()+"]");
			if(ArticleManager.logger.isDebugEnabled())
				ArticleManager.logger.debug("[道具效果] [BUFF道具] [成功] [{}] [{}] [{}] [级别:{}] [持续时间：{}秒] [施加BUFF到{}身上] [{}] [{}]", new Object[]{causer.getName(),this.getName(),buff.getDescription(),buff.getLevel(),buffLastingTime,livingObject.getName(),livingObject.getName(),livingObject.getId()});
		}
		return "";
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
		if(resultStr == null){
			Buff old = null;
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			if(bt == null){
//				ArticleManager.logger.warn("[使用道具] [BUFF道具] [失败] ["+p.getName()+"] ["+this.getName()+"] ["+buffName+"] [buff不存在]");
				if(ArticleManager.logger.isWarnEnabled())
					ArticleManager.logger.warn("[使用道具] [BUFF道具] [失败] [{}] [{}] [{}] [buff不存在]", new Object[]{p.getName(),this.getName(),buffName});
				return Translate.text_3726+buffName+Translate.text_3727;
			}
			//因为buffLevel最小从1开始，所以需要进行-1操作
			Buff buff = bt.createBuff(buffLevel-1);
			buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			buff.setInvalidTime(buff.getStartTime() + buffLastingTime * 1000);
			buff.setLevel(buffLevel-1);
			buff.setGroupId(bt.getGroupId());
			buff.setCauser(p);
			Buff[] buffs = p.getActiveBuffs();
			for (Buff b : buffs ) {
				if (buff.getTemplate() == b.getTemplate()) {
					old = b;
					break;
				}
			}
			if (old != null) {
				if (buff.getLevel() < old.getLevel()) {
					resultStr = Translate.text_3728;
				}
			}
		}
		return resultStr;
	}
	public String getComment(){
		StringBuffer sb = new StringBuffer();
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate bt = btm.getBuffTemplateByName(buffName);

		if(bt != null){
			Buff buff = bt.createBuff(buffLevel-1);
			if(plantType == PLANT_TYPE_MONSTER){
				sb.append(Translate.text_3729+range+Translate.text_3730);
			}else if(plantType == PLANT_TYPE_NPC_ENEMY){
				sb.append(Translate.text_3729+range+Translate.text_3731);
			}else if(plantType == PLANT_TYPE_NPC_FRIEND){
				sb.append(Translate.text_3729+range+Translate.text_3732);
			}else if(plantType == PLANT_TYPE_NPC_FULL){
				sb.append(Translate.text_3729+range+Translate.text_3733);
			}else if(plantType == PLANT_TYPE_PLAYER_ENEMY){
				sb.append(Translate.text_3729+range+Translate.text_3734);
			}else if(plantType == PLANT_TYPE_PLAYER_FRIEND){
				sb.append(Translate.text_3729+range+Translate.text_3735);
			}else if(plantType == PLANT_TYPE_PLAYER_FULL){
				sb.append(Translate.text_3729+range+Translate.text_3736);
			}else if(plantType == PLANT_TYPE_LIVINGOBJECT){
				sb.append(Translate.text_3729+range+Translate.text_3737);
			}
			sb.append(buff.getDescription());
			if(Utils.formatTimeDisplay2(buffLastingTime*1000) != null && !Utils.formatTimeDisplay2(buffLastingTime*1000).trim().equals("")){
				sb.append(Translate.text_3738 + Utils.formatTimeDisplay2(buffLastingTime*1000));
			}
			
		}
		return sb.toString();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}


}
