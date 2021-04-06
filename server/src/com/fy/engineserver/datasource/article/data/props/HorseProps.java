package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.HorseEquipmentColumn;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.USE_HORSE_RESULT_REQ;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse.HorseOtherData;
import com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.sprite.horse2.model.HorseColorModel;
import com.fy.engineserver.sprite.horse2.model.HorseRankModel;

public class HorseProps extends Props {

	private int speed;
	
	/**
	 * (职业，修罗，武皇等)
	 */
	private byte type;
	
	private String horseParticle;
	
	public static byte 通用职业 = 0;
	/**
	 * (对应人物境界)
	 */
	private byte rank;
	
	/**
	 * 是否是战斗坐骑
	 */
	private boolean fight;
	
	/**
	 * 是否是飞行坐骑
	 */
	private boolean  fly;
	
	/**
	 * 最大体力值
	 */
	private transient int maxEnergy;
	
	// avata部件全名
	protected String avata;
	
	private boolean specialHorse = false;
	
	@Override
	public String canUse(Player p) {
		
		if(super.canUse(p) == null){
			if(this.getType() == p.getCareer() || this.getType() ==  通用职业){
				return null;
			}else{
				return Translate.职业不匹配;
			}
		}else{
			return super.canUse(p);
		}
	
	}
	
	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		
		try {
			if(!super.use(game,player,ae)){
				return false;
			}
			
			// 判断是否满足生成条件
			ArrayList<Long> list = HorseManager.getInstance().getPlayerHorses(player);
			if (this.isFly() || this.rank < 1) {
				if(list.size() == 0){
					HorseRankModel hrm = Horse2Manager.instance.rankModelMap.get(1);
					HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(0);
					Horse horse = new Horse();
					long id = HorseManager.em.nextId();
					horse.setFly(this.fly);
					horse.setHorseName(this.getName());
					horse.setHorseId(id);
					horse.setSpeed(hrm.getSpeed());
					HorseOtherData otherData = new HorseOtherData();
					horse.owner = player;
					horse.setCareer(player.getCareer());
					otherData.setRankStar(1);
					horse.setOtherData(otherData);
					horse.setRank(otherData.getRankStar());
					horse.setType(this.getType());
					horse.setFight(this.isFight());
					horse.setOwnerId(player.getId());
					horse.setIcon(this.getIconId());
					horse.setAvataKey( this.getAvata());					
					horse.setNewAvatarKey(hrm.getNewAvatar()[horse.getCareer()-1]);
					horse.setMaxEnergy(this.getMaxEnergy());
					horse.setEnergy(this.getMaxEnergy());
					horse.setGrowRate((int) (hcm.getGrowUpRate() * 100));
					horse.setHorseParticle(this.getHorseParticle());
					if (hrm.getHorseName() != null && horse.getCareer() < hrm.getHorseName().length) {
						horse.setHorseShowName(hrm.getHorseName()[horse.getCareer()-1]);
					}
					horse.initBasicAttr();
					
					//限时坐骑
					if(this.isHaveValidDays()){
						if(ae.getEndTime() > SystemTime.currentTimeMillis()){
							horse.setLimitTime(true);
	//						horse.set到期时间(ae.getEndTime());
							horse.setDueTime(ae.getEndTime());
							player.sendError(Translate.你获得限时坐骑时间到就会自动消失);
							HorseManager.logger.warn("[生成限时坐骑] ["+player.getLogString()+"] [到期时间:"+ae.getEndTime()+"]");
						}else{
							player.sendError(Translate.限时道具已经到期);
							return false;
						}
					}
					
					HorseEquipmentColumn column = new HorseEquipmentColumn();
					column.init();
					column.setHorse(horse);
					horse.setEquipmentColumn(column);
					ResourceManager.getInstance().getHorseAvataForPlayer(horse, player);
					HorseManager.getInstance().addHorse(horse);
					list.add(horse.getHorseId());
					
	//				player.setHorseArr(list);
					player.setHorseIdList(list);
	//				ResourceManager.getInstance().getHorseAvataForPlayer(horse, player);
					USE_HORSE_RESULT_REQ res = new USE_HORSE_RESULT_REQ(GameMessageFactory.nextSequnceNum(), horse.getHorseId());
					player.addMessageToRightBag(res);
					
	//				boolean result = player.setDefaultHorse(horse.getHorseId());
	//				SET_DEFAULT_HORSE_RES res1 = new SET_DEFAULT_HORSE_RES(GameMessageFactory.nextSequnceNum(), true, horse.getHorseId());
	//				player.addMessageToRightBag(res1);
					
					AchievementManager.getInstance().record(player, RecordAction.获得坐骑阶数,horse.getRank());
					if(AchievementManager.logger.isWarnEnabled()){
						AchievementManager.logger.warn("[成就统计获得坐骑阶数] ["+player.getLogString()+"] ["+horse.getRank()+"]");
					}
					if(horse.isFly()){
						HorseManager.getInstance().horseAchievement(player, this);
						try {
							EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.获得飞行坐骑次数, 1L });
							EventRouter.getInst().addEvent(evt);
							if (this.getName_stat().equals(PlayerAimManager.黑暗魔龙) || this.getName_stat().equals(PlayerAimManager.光明圣龙) || this.getName_stat().equals(PlayerAimManager.圣域天龙) || this.getName_stat().equals(PlayerAimManager.冥域血龙)) {
								EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.获得黑雾, 1L });
								EventRouter.getInst().addEvent(evt2);
							}
						} catch (Exception e) {
							PlayerAimManager.logger.error("[目标系统] [统计玩家获得飞行坐骑次数异常] [" + player.getLogString() + "]", e);
						}
					}
					if(HorseManager.logger.isWarnEnabled()){
						HorseManager.logger.warn("[获取坐骑成功] ["+player.getLogString()+"] ["+this.getName()+"] ["+horse.getLogString()+"]");
					}
					
					return true;
				}else{
					for (long horseId : list) {
						
						Horse horse = HorseManager.getInstance().getHorseById(horseId,player);
						
						if(horse == null){
							HorseManager.logger.error("[生成坐骑错误] [玩家没有这个坐骑] ["+horseId+"]");
						}else{
//							Article a = ArticleManager.getInstance().getArticle(horse.getHorseName());
//							if(((HorseProps)a).getRank() == this.rank && !horse.isFly()){
							if(!this.fly && !horse.isFly()){
								player.sendError(Translate.text_同等级的只能有一个坐骑);
								if (HorseManager.logger.isWarnEnabled()) {
									HorseManager.logger.warn("[生成坐骑错误] [用户已经有同等级的坐骑] ["+player.getLogString()+"]");
								}
								return false;
							}else if(horse.isFly()){
								if(horse.getHorseName().equals(this.getName())){
									player.sendError(Translate.已经有相同一个坐骑);
									if (HorseManager.logger.isWarnEnabled()) {
										HorseManager.logger.warn("[生成坐骑错误] [已经有相同一个坐骑] ["+player.getLogString()+"] ["+this.getName()+"]");
									}
									return false;
								}
							}
						}
					}
					
					HorseRankModel hrm = Horse2Manager.instance.rankModelMap.get(1);
					HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(0);
					Horse horse = new Horse();
					long id = HorseManager.em.nextId();
					horse.setHorseId(id);
					horse.setHorseName(this.getName());
					horse.setFly(this.fly);
					horse.setSpeed(hrm.getSpeed());
					HorseOtherData otherData = new HorseOtherData();
					horse.owner = player;
					horse.setCareer(player.getCareer());
					otherData.setRankStar(1);
					horse.setOtherData(otherData);
					horse.setRank(otherData.getRankStar());
					horse.setType(this.getType());
					horse.setFight(this.isFight());
					horse.setOwnerId(player.getId());
					horse.setIcon(this.getIconId());
					horse.setAvataKey( this.getAvata());					
					horse.setNewAvatarKey(hrm.getNewAvatar()[horse.getCareer()-1]);
					horse.setMaxEnergy(this.getMaxEnergy());
					horse.setEnergy(this.getMaxEnergy());
					horse.setGrowRate((int) (hcm.getGrowUpRate() * 100));
					horse.setHorseParticle(this.getHorseParticle());
					if (hrm.getHorseName() != null && horse.getCareer() < hrm.getHorseName().length) {
						horse.setHorseShowName(hrm.getHorseName()[horse.getCareer()-1]);
					}
					horse.initBasicAttr();
					
					HorseEquipmentColumn column = new HorseEquipmentColumn();
					
					//限时坐骑
					if(this.isHaveValidDays()){
						if(ae.getEndTime() > SystemTime.currentTimeMillis()){
							horse.setLimitTime(true);
	//						horse.set到期时间(ae.getEndTime());
							horse.setDueTime(ae.getEndTime());
							player.sendError(Translate.你获得限时坐骑时间到就会自动消失);
							HorseManager.logger.warn("[生成限时坐骑] ["+player.getLogString()+"] [到期时间:"+ae.getEndTime()+"]");
						}else{
							player.sendError(Translate.限时道具已经到期);
							return false;
						}
					}
					
					column.init();
					column.setHorse(horse);
					horse.setEquipmentColumn(column);
					ResourceManager.getInstance().getHorseAvataForPlayer(horse, player);
					HorseManager.getInstance().addHorse(horse);
					list.add(horse.getHorseId());
		
	//				player.setHorseArr(list);
					player.setHorseIdList(list);
					
					AchievementManager.getInstance().record(player, RecordAction.获得坐骑阶数,horse.getRank());
					if(AchievementManager.logger.isWarnEnabled()){
						AchievementManager.logger.warn("[成就统计获得坐骑阶数] ["+player.getLogString()+"] ["+horse.getRank()+"]");
					}
					if(horse.isFly()){
						HorseManager.getInstance().horseAchievement(player, this);
						try {
							EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.获得飞行坐骑次数, 1L });
							EventRouter.getInst().addEvent(evt);
							if (this.getName_stat().equals(PlayerAimManager.黑暗魔龙) || this.getName_stat().equals(PlayerAimManager.光明圣龙) || this.getName_stat().equals(PlayerAimManager.圣域天龙) || this.getName_stat().equals(PlayerAimManager.冥域血龙)) {
								EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.获得黑雾, 1L });
								EventRouter.getInst().addEvent(evt2);
							}
						} catch (Exception e) {
							PlayerAimManager.logger.error("[目标系统] [统计玩家获得飞行坐骑次数异常] [" + player.getLogString() + "]", e);
						}
					}
					USE_HORSE_RESULT_REQ res = new USE_HORSE_RESULT_REQ(GameMessageFactory.nextSequnceNum(), horse.getHorseId());
					player.addMessageToRightBag(res);
					if(HorseManager.logger.isWarnEnabled()){
						HorseManager.logger.warn("[获取坐骑成功] ["+player.getLogString()+"] ["+this.getName()+"] ["+horse.getLogString()+"]");
					}
					return true;
				}
			} else {
				try {
					int giveNum = Horse2EntityManager.horseChangePropNums[this.getRank()];
					Article a = ArticleManager.getInstance().getArticle(Horse2EntityManager.horseChangePropName);
					ArticleEntity aee = DefaultArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.新坐骑碎片转换, player, a.getColorType(), 1, true);
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{aee}, new int[]{giveNum}, Translate.系统, Translate.坐骑转换, 0L, 0L, 0L, "坐骑转换获得");
					player.sendError(Translate.高阶坐骑自动转换);
					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[新坐骑系统] [使用坐骑道具] [转换成碎片] [" + player.getLogString() + "] [horseName:" + this.name_stat + "] [rank:" + this.getRank() + "] [fly:" + this.isFly() + "]");
					}
				} catch (Exception e) {
					HorseManager.logger.error("[使用坐骑道具] ["+player.getLogString()+"] [rank:" + this.getRank() + "]",e);
				}
				return true;
			}
		} catch (Exception e) {
			HorseManager.logger.error("[使用坐骑道具] ["+player.getLogString()+"]",e);
			return false;
		}
	}
	
	
	
	/********************************get set**********************************/
	
	
	public int getMaxEnergy() {
		return maxEnergy;
	}
	public void setMaxEnergy(int maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	
	public String getAvata() {
		return avata;
	}
	public void setAvata(String avata) {
		this.avata = avata;
	}
	public boolean isFly() {
		return fly;
	}
	public void setFly(boolean fly) {
		this.fly = fly;
	}
	public boolean isFight() {
		return fight;
	}
	public void setFight(boolean fight) {
		this.fight = fight;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	
	public byte getRank() {
		return rank;
	}
	public void setRank(byte rank) {
		this.rank = rank;
	}

	public String getHorseParticle() {
		return horseParticle;
	}

	public void setHorseParticle(String horseParticle) {
		this.horseParticle = horseParticle;
	}

	public boolean isSpecialHorse() {
		return specialHorse;
	}

	public void setSpecialHorse(boolean specialHorse) {
		this.specialHorse = specialHorse;
	}
	
}
