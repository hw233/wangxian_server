package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.fireActivity.FireManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.ComposeInterface;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.Confirm_Use_Lasting_For_Compose_Props;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.engineserver.vip.VipManager;

/**
 * 有时间持续的道具
 * 
 * 修改某些属性，保持一段时间，时间过后，修改消失
 * 此道具都是产生所谓一个BUFF
 * 
 * 
 */
public class Lasting_For_Compose_Props extends Props implements ComposeInterface {

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

	// 玩家使用物品的级别上限 大于就不能使用
	private int useMaxLevel;

	@Override
	public boolean isUsedUndisappear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public byte getComposeArticleType() {
		// TODO Auto-generated method stub
		return 1;
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
	 * drinkTimes
	 * lastDrinkDate
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		ArticleManager.logger.warn("[使用道具] [酒:"+ae.getArticleName()+"] ["+player.getName()+"]");
		if (!super.use(game, player, ae)) {
			return false;
		}
		ArticleManager.logger.warn("[使用道具2] [酒:"+ae.getArticleName()+"] [buffName:"+buffName+"] ["+player.getName()+"]");
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate bt = btm.getBuffTemplateByName(buffName);
		if (bt == null) {
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[使用道具] [BUFF道具] [失败] [{}] [{}] [{}] [buff不存在]", new Object[] { player.getName(), this.getName(), buffName });
			return false;
		}
		int drinkTimes = player.getDrinkTimes();
		long lastDrinkDate = player.getLastDrinkDate();
		int vipAdd = VipManager.getInstance().vip每日增加的道具使用次数(player,this.get物品二级分类());
		boolean isSameWeek = TimeTool.instance.isSame(lastDrinkDate, SystemTime.currentTimeMillis(), TimeDistance.DAY, 7);
		if(!isSameWeek){
			player.setDrinkTimes(0);
			player.setLastDrinkDate(SystemTime.currentTimeMillis());
			drinkTimes = 0;
		}
		drinkTimes++;
		if(drinkTimes > 15 + vipAdd){
			player.sendError(Translate.每周只能喝酒15次);
			return false;
		}
		player.setDrinkTimes(drinkTimes);
		

		Buff[] buffs = player.getActiveBuffs();
		int level = buffLevel - 1 + ae.getColorType();
		for (Buff b : buffs) {
			if (bt == b.getTemplate() && (b.getLevel() != level)) {
				WindowManager wm = WindowManager.getInstance();
				MenuWindow mw = wm.createTempMenuWindow(600);
				Confirm_Use_Lasting_For_Compose_Props option = new Confirm_Use_Lasting_For_Compose_Props();
				option.setId(ae.getId());
				option.setBt(bt);
				option.setText(Translate.确定);
				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.取消);
				mw.setDescriptionInUUB(Translate.使用后将替换身上已有的状态);
				Option[] options = new Option[] { option, cancel };
				mw.setOptions(options);
				CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
				player.addMessageToRightBag(creq);
				if (ArticleManager.logger.isDebugEnabled()) {
					ArticleManager.logger.debug("[使用道具] [BUFF道具] [成功弹出确认框] [blevel:{}] [buffLevel:{}] [{}] [{}] [{}] [{}]", new Object[] {b.getLevel(), buffLevel,player.getUsername(), player.getName(), player.getId(), this.getName() });
				}
				return true;
			}
		}

		plantBuffOnLivingObject(bt, player, player, ae);
	
		if (ArticleManager.logger.isDebugEnabled()) {
			ArticleManager.logger.debug("[使用道具] [BUFF道具] [成功] [已经喝了:{}次] [vip增加:{}次] [{}] [{}] [{}] [{}]", new Object[] {drinkTimes,vipAdd, player.getUsername(), player.getName(), player.getId(), this.getName() });
		}
		return true;
	}

	@Override
	public void usingTimesLimitProp(Player player) {

	}

	public void usingTimesLimitPropRe(Player player) {
		// TODO Auto-generated method stub
		super.usingTimesLimitProp(player);
	}

	public void plantBuffOnLivingObject(BuffTemplate bt, Fighter livingObject, Player causer, ArticleEntity ae) {
		// 因为buffLevel最小从1开始，所以需要进行-1操作
		ArticleEntity aee = causer.removeFromKnapsacks(ae.getId(), "使用持续性道具", true);

//		usingTimesLimitPropRe(causer);
		try {
			if (aee != null) {

				// 统计
				ArticleStatManager.addToArticleStat(causer, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "使用持续性道具", null);

				Buff old = null;
//				BuffTemplateManager btm = BuffTemplateManager.getInstance();
				ArrayList<Buff> buffs = causer.getBuffs();
				for (Buff b : buffs) {
					if (bt == b.getTemplate()) {
						old = b;
						break;
					}
				}
				int level = buffLevel - 1 + ae.getColorType();
				if(old != null){
					if((old.getLevel() != level)){
						old.setInvalidTime(0);
						Buff buff = bt.createBuff(level);
						buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						buff.setInvalidTime(buff.getStartTime() + buffLastingTime * 1000);
						buff.setLevel(level);
						buff.setGroupId(bt.getGroupId());
						old = buff;
					}else{
						Buff buff = bt.createBuff(level);
						buff.setStartTime(old.getStartTime());
						buff.setInvalidTime(old.getInvalidTime() + buffLastingTime * 1000);
						buff.setLevel(level);
						buff.setGroupId(bt.getGroupId());
						old = buff;
					}
				}else{
					Buff buff = bt.createBuff(level);
					buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					buff.setInvalidTime(buff.getStartTime() + buffLastingTime * 1000);
					buff.setLevel(level);
					buff.setGroupId(bt.getGroupId());
					old = buff;
				}
				
				old.setCauser(causer);
				livingObject.placeBuff(old);
				
				if (livingObject instanceof Player) {
					if (livingObject != causer) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_2317 + causer.getName() + Translate.text_3725 + this.name);
						((Player) livingObject).addMessageToRightBag(hreq);
					}
				}

				// 排行榜统计
				BillboardStatDate date = BillboardStatDateManager.getInstance().getBillboardStatDate(causer.getId());
				date.drinkBeerStat(causer, ae, level);

				// 成就统计
				AchievementManager.getInstance().record(causer, RecordAction.喝酒次数);

				// 活跃度统计
				ActivenessManager.getInstance().record(causer, ActivenessType.聚灵阵);
				
				// 酒buff level 从0开始
				int color = (level + 1) % 5;
				switch (color) {
				case 0:
					AchievementManager.getInstance().record(causer, RecordAction.喝白色品质以上酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝橙酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝绿色以上酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝蓝色以上酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝紫色以上酒次数);
					if (AchievementManager.logger.isWarnEnabled()) {
						AchievementManager.logger.warn("[喝酒统计成就] [" + causer.getLogString() + "] [color : " + color + "]");
					}
					// 喝橙酒广播 （人名）开怀畅饮（酒），微醺间恍若进入仙境
					StringBuffer sb = new StringBuffer();
					String 国家 = CountryManager.得到国家名(causer.getCountry());
					sb.append(Translate.translateString(Translate.喝橙酒广播, new String[][]{{Translate.STRING_1, 国家},{Translate.STRING_2, causer.getName()},{Translate.STRING_3, this.getName()}}));
					FireManager.noticeTelevision(sb.toString(), causer);
					break;
				case 4:
					AchievementManager.getInstance().record(causer, RecordAction.喝白色品质以上酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝紫酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝绿色以上酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝蓝色以上酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝紫色以上酒次数);
					if (AchievementManager.logger.isWarnEnabled()) {
						AchievementManager.logger.warn("[喝酒统计成就] [" + causer.getLogString() + "] [color : " + color + "]");
					}
					break;
				case 3:
					AchievementManager.getInstance().record(causer, RecordAction.喝白色品质以上酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝绿色以上酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝蓝色以上酒次数);
					AchievementManager.getInstance().record(causer, RecordAction.喝蓝酒次数);
					if (AchievementManager.logger.isWarnEnabled()) {
						AchievementManager.logger.warn("[喝酒统计成就] [" + causer.getLogString() + "] [color : " + color + "]");
					}
					break;
				case 2:
					AchievementManager.getInstance().record(causer, RecordAction.喝白色品质以上酒次数);
					if (AchievementManager.logger.isWarnEnabled()) {
						AchievementManager.logger.warn("[喝酒统计成就] [" + causer.getLogString() + "] [color : " + color + "]");
					}
					break;
				case 1:
					AchievementManager.getInstance().record(causer, RecordAction.喝白酒次数);
					if (AchievementManager.logger.isWarnEnabled()) {
						AchievementManager.logger.warn("[喝酒统计成就] [" + causer.getLogString() + "] [color : " + color + "]");
					}
					break;
				}

				// ArticleManager.logger.debug("[道具效果] [BUFF道具] [成功] ["+causer.getName()+"] ["+this.getName()+"] ["+buff.getDescription()+"] [级别:"+buff.getLevel()+"] [持续时间："+buffLastingTime+"秒] [施加BUFF到"+livingObject.getName()+"身上] ["+livingObject.getName()+"] ["+livingObject.getId()+"]");
				if (ArticleManager.logger.isWarnEnabled()) {
					ArticleManager.logger.warn("[道具效果] [BUFF道具] [成功] [{}] [{}] [{}] [级别:{}] [持续时间：{}秒] [失效时间:{}] [施加BUFF到{}身上] [{}] [{}]", new Object[] { causer.getName(), this.getName(), old.getDescription(), old.getLevel(), buffLastingTime,TimeTool.formatter.varChar23.format(old.getInvalidTime()), livingObject.getName(), livingObject.getName(), livingObject.getId() });
				}

			}
		} catch (Exception ex) {
			ArticleManager.logger.error("[使用lasting_for_compose_props] [异常] [" + causer.getLogString() + "]", ex);
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
		if (resultStr == null) {

			// 酒加了使用级别上限
			if (p.getLevel() > this.getUseMaxLevel()) {

				if (FireManager.logger.isWarnEnabled()) {
					FireManager.logger.warn("[不能使用酒有上限] [" + p.getLogString() + "] [" + this.getName() + "] [使用上限:" + this.getUseMaxLevel() + "]");
				}
				return Translate.translateString(Translate.您级别高于xx级不能使用, new String[][] { { Translate.COUNT_1, this.getUseMaxLevel() + "" } });
			}

			Buff old = null;
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			if (bt == null) {
				// ArticleManager.logger.warn("[使用道具] [BUFF道具] [失败] ["+p.getName()+"] ["+this.getName()+"] ["+buffName+"] [buff不存在]");
				if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[使用道具] [BUFF道具] [失败] [{}] [{}] [{}] [buff不存在]", new Object[] { p.getName(), this.getName(), buffName });
				return Translate.text_3726 + buffName + Translate.text_3727;
			}
		}
		return resultStr;
	}

	public String getComment() {
		StringBuffer sb = new StringBuffer();
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate bt = btm.getBuffTemplateByName(buffName);
		return sb.toString();
	}

	public ArticleEntity getComposeEntity(Player player, ArticleEntity ae, boolean binded, int createCount) {
		// TODO Auto-generated method stub
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (am == null || aem == null) {
			return null;
		}
		if (ae.getColorType() < ArticleManager.notEquipmentColorMaxValue) {
			Article a = am.getArticle(ae.getArticleName());
			if (a != null) {
				try {
					ArticleEntity aee = aem.createEntity(a, binded, ArticleEntityManager.CREATE_REASON_COMPOSE_ARTICLE, player, ae.getColorType() + 1, createCount, true);
					return aee;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return null;
		}

		return null;
	}

	@Override
	public long getTempComposeEntityId(Player player, ArticleEntity ae, boolean binded) {
		long tempId = -1;
		if (ae != null) {
			if (ae.getColorType() < ArticleManager.notEquipmentColorMaxValue) {
				return 0;
			}
		}
		return tempId;
	}

	@Override
	public String getTempComposeEntityDescription(Player player, ArticleEntity ae, boolean binded) {
		// TODO Auto-generated method stub
		String description = "";
		return description;
	}

	public int getUseMaxLevel() {
		return useMaxLevel;
	} 

	public void setUseMaxLevel(int useMaxLevel) {
		this.useMaxLevel = useMaxLevel;
	}

	public String getBuffName_stat() {
		return buffName_stat;
	}

	public void setBuffName_stat(String buffName_stat) {
		this.buffName_stat = buffName_stat;
	}

}
