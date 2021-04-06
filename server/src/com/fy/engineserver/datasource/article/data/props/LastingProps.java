package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_StealFruit;
import com.fy.engineserver.datasource.buff.Buff_StealFruit;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_Confirm_UseLastingProp;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.Utils;
import com.fy.engineserver.vip.VipManager;

/**
 * 有时间持续的道具
 * 
 * 修改某些属性，保持一段时间，时间过后，修改消失
 * 此道具都是产生所谓一个BUFF
 * 
 *
 */
public class LastingProps extends Props{

	public static final int PLANT_TYPE_SELF = 0;
	public static final int PLANT_TYPE_MONSTER = 1;
	public static final int PLANT_TYPE_NPC_ENEMY = 2;
	public static final int PLANT_TYPE_NPC_FRIEND = 3;
	public static final int PLANT_TYPE_NPC_FULL = 4;
	public static final int PLANT_TYPE_PLAYER_ENEMY = 5;
	public static final int PLANT_TYPE_PLAYER_FRIEND = 6;
	public static final int PLANT_TYPE_PLAYER_FULL = 7;
	public static final int PLANT_TYPE_LIVINGOBJECT = 8;
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
	protected int plantType;
	
	/**
	 * 范围，半径，像素
	 */
	protected int range;

	/**
	 * 每一个级别的技能，使用的Buff的名称，
	 * 此名称必须索引到一个存在的Buff
	 */
	protected String buffName;
	
	protected String buffName_stat;

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
	/**
	 * buff伴生物品名
	 */
	private String articleCNNName;
	/** 产生非绑物品概率(万分比) */
	private int unBindProb;
	/** 物品限制使用地图 */
	private String[] limitMaps = null;

	public String getBuffName_stat() {
		return buffName_stat;
	}

	public void setBuffName_stat(String buffName_stat) {
		this.buffName_stat = buffName_stat;
	}

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
	public boolean use(Game game,Player player, ArticleEntity ae){
		return this.use(game, player, ae, false);
	}

	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity ae, boolean confirm){
		ArticleManager.logger.warn(player.getLogString() + "[a] [使用道具:" + ae.getArticleName() + "]");
//		if(TransitRobberyEntityManager.getInstance().isPlayerInSixiang(player)) {
//			player.sendError(Translate.渡劫不允许用药);
//			return false;
//		}
		if(!super.use(game,player,ae)){
			return false;
		}
		ArticleManager.logger.warn(player.getLogString() + "[b] [使用道具:" + ae.getArticleName() + "]");
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate bt = btm.getBuffTemplateByName(buffName);
		if(bt == null){
//			ArticleManager.logger.warn("[使用道具] [BUFF道具] [失败] ["+player.getName()+"] ["+this.getName()+"] ["+buffName+"] [buff不存在]");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[使用道具] [BUFF道具] [失败] [{}] [{}] [{}] [buff不存在]", new Object[]{player.getName(),this.getName(),buffName});
			return false;
		}
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
		if(plantType == PLANT_TYPE_SELF){
			if (bt instanceof BuffTemplate_StealFruit && !confirm && player.isPickFruitBuffAct()) {
				WindowManager wm = WindowManager.getInstance();
				MenuWindow mw = wm.createTempMenuWindow(600);
				Option_Confirm_UseLastingProp option1 = new Option_Confirm_UseLastingProp();
				option1.setText(MinigameConstant.CONFIRM);
				option1.setAe(ae);
				option1.setProps(this);
				option1.setGame(game);
				Option_Cancel option2 = new Option_Cancel();
				option2.setText(MinigameConstant.CANCLE);
				Option[] options = new Option[] {option1, option2};
				mw.setOptions(options);
				String msg = Translate.偷果实buff二次确认;
				mw.setDescriptionInUUB(msg);
				CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
				player.addMessageToRightBag(creq);
				return false;
			}
			plantBuffOnLivingObject(bt,player,player, articleCNNName);
			if (VipManager.vipBuffMap.containsKey(buffName)) {
				player.setVipLevel(player.getVipLevel());
				if (VipManager.getInstance() != null) {
					VipManager.getInstance().设置玩家的vip属性(player);
					ArticleManager.logger.warn(player.getLogString() + " [使用道具:" + this.name + "] [增加buff:" + buffName + "] [设置vip等级:" + player.getVipLevel() + "]");
				}
			}
		}else{
			boolean used = false;
			LivingObject[] livingObjects = game.getLivingObjects();
			if(livingObjects != null){
				for(LivingObject livingObject : livingObjects){
					if(plantType == PLANT_TYPE_MONSTER){
						if(livingObject instanceof Monster){
							if(isValidDistance(player.getX(),player.getY(),livingObject.getX(),livingObject.getY(),range)){
								plantBuffOnLivingObject(bt,(Monster)livingObject,player);
								used = true;
							}
						}
					}else if(plantType == PLANT_TYPE_NPC_ENEMY){
						if(livingObject instanceof NPC && player.getFightingType((NPC)livingObject) == Fighter.FIGHTING_TYPE_ENEMY){
							if(isValidDistance(player.getX(),player.getY(),livingObject.getX(),livingObject.getY(),range)){
								plantBuffOnLivingObject(bt,(NPC)livingObject,player);
								used = true;
							}
						}
					}else if(plantType == PLANT_TYPE_NPC_FRIEND){
						if(livingObject instanceof NPC && player.getFightingType((NPC)livingObject) == Fighter.FIGHTING_TYPE_FRIEND){
							if(isValidDistance(player.getX(),player.getY(),livingObject.getX(),livingObject.getY(),range)){
								plantBuffOnLivingObject(bt,(NPC)livingObject,player);
								used = true;
							}
						}
					}else if(plantType == PLANT_TYPE_NPC_FULL){
						if(livingObject instanceof NPC){
							if(isValidDistance(player.getX(),player.getY(),livingObject.getX(),livingObject.getY(),range)){
								plantBuffOnLivingObject(bt,(NPC)livingObject,player);
								used = true;
							}
						}
					}else if(plantType == PLANT_TYPE_PLAYER_ENEMY){
						if(livingObject instanceof Player && player.getFightingType((Player)livingObject) == Fighter.FIGHTING_TYPE_ENEMY){
							if(isValidDistance(player.getX(),player.getY(),livingObject.getX(),livingObject.getY(),range)){
								plantBuffOnLivingObject(bt,(Player)livingObject,player);
								used = true;
							}
						}
					}else if(plantType == PLANT_TYPE_PLAYER_FRIEND){
						if(livingObject instanceof Player && player.getFightingType((Player)livingObject) == Fighter.FIGHTING_TYPE_FRIEND){
							if(isValidDistance(player.getX(),player.getY(),livingObject.getX(),livingObject.getY(),range)){
								plantBuffOnLivingObject(bt,(Player)livingObject,player);
								used = true;
							}
						}
					}else if(plantType == PLANT_TYPE_PLAYER_FULL){
						if(livingObject instanceof Player){
							if(isValidDistance(player.getX(),player.getY(),livingObject.getX(),livingObject.getY(),range)){
								plantBuffOnLivingObject(bt,(Player)livingObject,player);
								used = true;
							}
						}
					}else if(plantType == PLANT_TYPE_LIVINGOBJECT){
						if(isValidDistance(player.getX(),player.getY(),livingObject.getX(),livingObject.getY(),range)){
							if(livingObject instanceof Sprite){
								plantBuffOnLivingObject(bt,(Sprite)livingObject,player);
								used = true;
							}else if(livingObject instanceof Player){
								plantBuffOnLivingObject(bt,(Player)livingObject,player);
								used = true;
							}
						}
					}
				}
			}
			if(!used){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)1, Translate.text_3724+this.getName());
				player.addMessageToRightBag(hreq);
				return false;
			}
		}
		if(ArticleManager.logger.isDebugEnabled()){
//			ArticleManager.logger.debug("[使用道具] [BUFF道具] [成功] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+this.getName()+"]");
			if(ArticleManager.logger.isDebugEnabled())
				ArticleManager.logger.debug("[使用道具] [BUFF道具] [成功] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getName(),player.getId(),this.getName()});
		}
		return true;
	}
	private boolean isValidDistance(float x,float y,float x1,float y1,int range){
		boolean valid = false;
		if(Math.sqrt((x-x1)*(x-x1)+(y-y1)*(y-y1)) <= range){
			valid = true;
		}
		return valid;
	}
	
	private void plantBuffOnLivingObject(BuffTemplate bt,Fighter livingObject,Player causer){
		plantBuffOnLivingObject(bt, livingObject, causer, "");
	}
	private void plantBuffOnLivingObject(BuffTemplate bt,Fighter livingObject,Player causer, String articleName){
		//因为buffLevel最小从1开始，所以需要进行-1操作
		Buff buff = bt.createBuff(buffLevel-1);
		buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		buff.setInvalidTime(buff.getStartTime() + buffLastingTime * 1000);
		buff.setLevel(buffLevel-1);
		buff.setGroupId(bt.getGroupId());
		buff.setCauser(causer);
		buff.setLimitMaps(limitMaps);
		if (buff instanceof Buff_StealFruit) {
			try {
				((Buff_StealFruit)buff).setArticleName(articleName);
				((Buff_StealFruit)buff).setUnBindProb(unBindProb);;
				if (articleName == null || articleName.isEmpty()) {
					HorseManager.logger.warn("[新坐骑系统] [偷取果实伴生buff配置物品名错误] [bt.getname:" + bt.getName() + "] [articleName:" + articleName + "] [" + causer.getLogString() + "]");
				} else {
					String str1 =  Translate.translateString(buff.getDescription(), new String[][] {{Translate.STRING_2, articleName}});
					buff.setDescription(str1);
				}
			} catch (Exception e) {
				HorseManager.logger.error("[新坐骑系统] [种植偷取果实伴生buff] [异常] [bt.getname:" + bt.getName() + "] [" + causer.getLogString() + "]", e);
			}
		}
		livingObject.placeBuff(buff);
		if(livingObject instanceof Player){
			if(livingObject != causer){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)1, Translate.text_2317+causer.getName()+Translate.text_3725+this.name);
				((Player)livingObject).addMessageToRightBag(hreq);
			}
		}
		if(ArticleManager.logger.isDebugEnabled()){
//			ArticleManager.logger.debug("[道具效果] [BUFF道具] [成功] ["+causer.getName()+"] ["+this.getName()+"] ["+buff.getDescription()+"] [级别:"+buff.getLevel()+"] [持续时间："+buffLastingTime+"秒] [施加BUFF到"+livingObject.getName()+"身上] ["+livingObject.getName()+"] ["+livingObject.getId()+"]");
			if(ArticleManager.logger.isDebugEnabled())
				ArticleManager.logger.debug("[道具效果] [BUFF道具] [成功] [{}] [{}] [{}] [级别:{}] [持续时间：{}秒] [施加BUFF到{}身上] [{}] [{}]", new Object[]{causer.getName(),this.getName(),buff.getDescription(),buff.getLevel(),buffLastingTime,livingObject.getName(),livingObject.getName(),livingObject.getId()});
		}
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
			try {
				if (limitMaps != null && limitMaps.length > 0) {
					Game g = p.getCurrentGame();
					boolean b = true;
					if (g != null ) {
						for (String str : limitMaps) {
							if (g.gi.name.equalsIgnoreCase(str)) {
								b = false;
								break;
							}
						}
					}
					if (b) {
						resultStr = Translate.限制地图使用;
					}
				}
			} catch (Exception e) {
				ArticleManager.logger.warn("[使用道具] [判断地图限制] [异常] [" + p.getLogString() + "] [" + this.getName() + "]");
			}
			if (resultStr == null) {
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
				buff.setLimitMaps(limitMaps);
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

	public String getArticleCNNName() {
		return articleCNNName;
	}

	public void setArticleCNNName(String articleCNNName) {
		this.articleCNNName = articleCNNName;
	}

	public int getUnBindProb() {
		return unBindProb;
	}

	public void setUnBindProb(int unBindProb) {
		this.unBindProb = unBindProb;
	}

	public String[] getLimitMaps() {
		return limitMaps;
	}

	public void setLimitMaps(String[] limitMaps) {
		this.limitMaps = limitMaps;
	}
}
