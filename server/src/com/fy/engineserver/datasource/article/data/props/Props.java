package com.fy.engineserver.datasource.article.data.props;

import java.util.Map;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PropsUseRecord;
import com.fy.engineserver.time.SystemMaintainManager;
import com.fy.engineserver.time.Timer;
import com.fy.engineserver.vip.VipManager;

/**
 * 小道具
 * 
 */
public class Props extends Article {
	public static ArticleManager am = ArticleManager.getInstance();
	/**
	 * 道具的类型定义
	 */
	public static String[] propsTypeStrings = new String[]{Translate.text_3775,Translate.text_3776,Translate.text_3777,Translate.text_3778,Translate.text_3779,Translate.text_3780,Translate.text_3781,Translate.text_3782,Translate.text_3783,Translate.text_3425,Translate.text_3784,Translate.text_3785,Translate.text_3786,Translate.text_3787,Translate.text_3788,Translate.text_3789};
	/**
	 * 是否有使用次数限制
	 */
	protected boolean usingTimesLimit = true;

	/**
	 * 使用次数限制-1代表没有限制
	 */
	protected int maxUsingTimes = 1;

	/**
	 * 使用后不消失
	 */
	protected boolean usedUndisappear;

	/**
	 * 道具类分类类名
	 */
	protected String categoryName = "";

	/**
	 * 可使用道具的类型定义如下：  0 未定义 1 食品 2 加血药品 3 加蓝药品 4 加血和蓝药品 5传送符 6 坐骑 7复活道具  8洗点 9触发任务道具 10背包扩展 11离线经验 12宝箱 13随机宝箱 14材料 15配方
	 */
	protected byte propsType;

	/**
	 * 玩家等级限制
	 */
	private int levelLimit;

	/**
	 * 可使用类型，0代表战场外可用，1代表战场中可用，2代表都可用
	 */
	protected byte canUseType;

	/**
	 * 战斗状态限制，true战斗状态下不可用，false战斗状态下可用
	 */
	private boolean fightStateLimit;

	/**
	 * 是否地图限制
	 */
	private boolean gameMapLimitFlag;

	/**
	 * 地图限制
	 */
	private GameMapLimit gml;

	/**
	 * 玩家境界限制
	 */
	protected int classLimit;
	@Override
	public int getKnapsackType() {
		// TODO Auto-generated method stub
		return Article.KNAP_奇珍;
	}
	public int getClassLimit() {
		return classLimit;
	}

	public void setClassLimit(int classLimit) {
		this.classLimit = classLimit;
	}

	public long getStalemateTime() {
		return 0;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	public byte getCanUseType() {
		return canUseType;
	}

	public void setCanUseType(byte canUseType) {
		this.canUseType = canUseType;
	}

	public boolean isFightStateLimit() {
		return fightStateLimit;
	}

	public void setFightStateLimit(boolean fightStateLimit) {
		this.fightStateLimit = fightStateLimit;
	}

	public boolean isGameMapLimitFlag() {
		return gameMapLimitFlag;
	}

	public void setGameMapLimitFlag(boolean gameMapLimitFlag) {
		this.gameMapLimitFlag = gameMapLimitFlag;
	}

	public GameMapLimit getGml() {
		return gml;
	}

	public void setGml(GameMapLimit gml) {
		this.gml = gml;
	}

	public byte getPropsType() {
		return propsType;
	}

	public void setPropsType(byte propsType) {
		this.propsType = propsType;
	}

	public boolean isUsedUndisappear() {
		return usedUndisappear;
	}

	public void setUsedUndisappear(boolean usedUndisappear) {
		this.usedUndisappear = usedUndisappear;
	}

	public String getCategoryName() {
		if (categoryName == null)
			categoryName = "";
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * 使用道具
	 * 
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		ArticleManager.logger.warn("[使用Props] [article:"+ae.getArticleName()+"] [maxValidDays:"+maxValidDays+"] [{}] [{}]", new Object[]{haveValidDays, ae.getTimer() != null});
		if(haveValidDays && ae.getTimer() != null && ae.getTimer().isClosed()){
			HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, "["+this.name+Translate.text_3752);
			player.addMessageToRightBag(error);
//			ArticleManager.logger.warn("[使用道具] [失败] [user:"+player.getUsername()+"] ["+player.getName()+"] [playerId:"+player.getId()+"] ["+this.name+"] [id:"+ae.getId()+"] [已经到期，不能使用]");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[使用道具] [失败] [user:{}] [{}] [playerId:{}] [{}] [id:{}] [已经到期，不能使用]", new Object[]{player.getUsername(),player.getName(),player.getId(),this.name,ae.getId()});
			return false;
		}
		if(haveValidDays && maxValidDays != 0 && ae.getTimer() == null && activationOption == Article.ACTIVATION_OPTION_USE){
			long lastingTime = maxValidDays*60000;
			Timer timer = SystemMaintainManager.createSystemMaintainByType(getTimeType());
			long inValidTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+lastingTime;
			timer.setEndTime(inValidTime);
			ae.setTimer(timer);
			ae.setEndTime(inValidTime);
		}

		usingTimesLimitProp(player);
        return true;
	}
	
	/**
	 * 如果子类中的实现，需要二次确认才能真正知道是否使用此道具
	 * 那么子类可以重装此方法，方法中什么都不做。
	 * 
	 * @param player
	 */
	public void usingTimesLimitProp(Player player){
		if(this.usingTimesLimit == true && this.maxUsingTimes > 0){
			Map<String, PropsUseRecord> map = player.getPropsUseRecordMap();
			PropsUseRecord pr = map.get(this.get物品二级分类());
			if(pr == null){
				pr = new PropsUseRecord(this.get物品二级分类(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis(),(byte)1);
				map.put(this.get物品二级分类(), pr);
				if (ArticleManager.logger.isInfoEnabled()) {
					ArticleManager.logger.info("[第一次使用有使用次数限制的道具] [道具名:"+this.getName()+"] [二级分类:"+this.get物品二级分类()+"] ["+player.getLogString()+"] []");
				}
			}else{
				pr.useProps();
				if (ArticleManager.logger.isInfoEnabled()) {
					ArticleManager.logger.info("[使用有使用次数限制的道具] [道具名:"+this.getName()+"] [二级分类:"+this.get物品二级分类()+"] ["+player.getLogString()+"] []");
				}
			}
			
			player.setDirty(true, "propsUseRecordMap");
		}
	}

	/**
	 * 不使用道具
	 * 
	 * @param player
	 */
	public void unUse(Game game, Player player) {

	}

	public String getComment() {
		return "";
	}

	public boolean isUsingTimesLimit() {
		return usingTimesLimit;
	}

	public void setUsingTimesLimit(boolean usingTimesLimit) {
		this.usingTimesLimit = usingTimesLimit;
	}

	public int getMaxUsingTimes() {
		return maxUsingTimes;
	}

	public void setMaxUsingTimes(int maxUsingTimes) {
		this.maxUsingTimes = maxUsingTimes;
	}
	
	@Override
	public String getSpecialInfo(Player player, int colorType) {
		return this.getInfoShow();
	}
	
	@Override
	public String getInfoShow(){
		StringBuffer sb = new StringBuffer();
		sb.append(Translate.使用等级).append(":");
		if(levelLimit>220){
			sb.append(Translate.仙);
			levelLimit -= 220;
		}
		sb.append(levelLimit);
		sb.append("\n");
		sb.append("<f color='0x00e4ff' size='28'>").append(Translate.有效期).append("：");
		long time = this.getMaxValidDays() * 60000;
		long day = time/1000/60/60/24;
		long hour = time/1000/60/60%24;
		if(day>0){
			sb.append(day).append(Translate.text_jiazu_114);
		}
		if(hour>0){
			sb.append(hour).append(Translate.text_小时);
		}
		if (day <= 0 && hour <=0) {
			sb.append(Translate.text_不足1小时);
		}
		sb.append("</f>");
		sb.append("\n");
		sb.append("<f color='0x00FF00'>").append(Translate.境界限定).append(":").append(PlayerManager.classlevel[classLimit]).append("</f>\n");
		if(this.getUseMethod() != null && !this.getUseMethod().trim().equals("")){
			sb.append("<f color='0xFFFF00'>").append(this.getUseMethod()).append("</f>\n");
		}
		if(this.getGetMethod() != null && !this.getGetMethod().trim().equals("")){
			sb.append("<f color='0xFFFF00'>").append(this.getGetMethod()).append("</f>\n");
		}
		return sb.toString();
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
		if(getCanUseType() == 3){
			
		}
		if (getCanUseType() == 0 && p.isInBattleField()) {
			return Translate.text_3753+this.name+"]";
		}
		if (getCanUseType() == 1 && !p.isInBattleField()) {
			return Translate.text_3754+this.name+"]";
		}
		if (fightStateLimit && p.isFighting()) {
			return Translate.text_3755+this.name+"]";
		}
		if (this.levelLimit > p.getLevel())
			return Translate.text_3570+this.name+Translate.text_3571+this.levelLimit+Translate.text_99;
		if(this.classLimit > p.getClassLevel()){
			return Translate.境界不够不能使用;
		}
		if(this.usingTimesLimit == true && this.maxUsingTimes > 0){
			Map<String, PropsUseRecord> map = p.getPropsUseRecordMap();
			PropsUseRecord pr = map.get(this.get物品二级分类());
			if(pr == null){
				pr = new PropsUseRecord(this.get物品二级分类(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis(),(byte)0);
				map.put(this.get物品二级分类(), pr);
			}else{
				int vipAdd = VipManager.getInstance().vip每日增加的道具使用次数(p,this.get物品二级分类());
//				int exchangeAdd = ActivityManager.getInstance().getDayExchangeAddNum(p, this.get物品二级分类());
//				int specialDateAdd = ActivityManager.getInstance().specialActivityAddNum(p);
//				int activityAdd = 0;
//				if (Translate.酒.equals(this.get物品二级分类())) {
//					activityAdd = TimesActivityManager.instacen.getAddNum(p, TimesActivityManager.HEJIU_ACTIVITY);
//				}else if (Translate.封魔录.equals(this.get物品二级分类())) {
//					activityAdd = TimesActivityManager.instacen.getAddNum(p, TimesActivityManager.TUMOTIE_ACTIVITY);
//				}
				int totalNum = this.maxUsingTimes+vipAdd;//+exchangeAdd+specialDateAdd + activityAdd;
				if(!pr.canUseProps(totalNum)){
					if(this.get物品二级分类().equals("封魔录") || this.get物品二级分类().equals("酒") ){
						ArticleManager.logger.warn("[使用次数已经用光] ["+p.getLogString()+"] ["+this.get物品二级分类()+"] ["+this.name+"]");
						return Translate.translateString(Translate.今天使用次数已经用光2, new String[][]{{Translate.COUNT_1,(totalNum)+""}});
					}else{
						ArticleManager.logger.warn("[使用次数已经用光] ["+p.getLogString()+"] ["+this.get物品二级分类()+"] ["+this.name+"]");
						return Translate.translateString(Translate.今天使用次数已经用光, new String[][]{{Translate.COUNT_1,(totalNum)+""}});
					}
				}
			}
		}
		if (this.gameMapLimitFlag && this.gml != null) {
			if(gml.getGameMapName() == null || gml.getGameMapName().trim().equals("")){
				return null;
			}
			String[] gameMaps = gml.getGameMapName().split(",");
			if(gameMaps != null && gameMaps.length == 1){
				gameMaps = gml.getGameMapName().split("，");
			}
			if(gameMaps != null){
				for(String mapStr : gameMaps){
					if(mapStr != null){
						if (p.getGame() != null
								&& p.getGame().equals(mapStr.trim())
								&& (p.getX() - gml.getX()) < gml.getRedius() && (p.getX() - gml.getX()) > -gml.getRedius() && (p.getY() - gml.getY()) < gml.getRedius() &&
										(p.getY() - gml.getY()) > -gml.getRedius()) {
							return null;
						}
					}
				}
			}
			GameManager gm = GameManager.getInstance();
			Game game = gm.getGameByName(gml.getGameMapName(),p.getCountry());
			if(game == null){
				game = gm.getGameByName(gml.getGameMapName(),0);
			}
			if(game != null && game.getGameInfo() != null){
				return Translate.text_3756+this.name+Translate.text_3757+gml.getGameMapName()+Translate.text_3758+gml.getX()/game.getGameInfo().getCellW()+","+gml.getY()/game.getGameInfo().getCellH()+Translate.text_3759;
			}else{
				return Translate.text_3756+this.name+Translate.text_3757+gml.getGameMapName()+Translate.text_3758+gml.getX()/32+","+gml.getY()/16+Translate.text_3759;
			}
		}
		return null;

	}
	
	/**
	 * 重写获得物种显示信息
	 */
//	public String getInfoShow() {
//		StringBuffer sb = new StringBuffer();
//		ArticleManager am = ArticleManager.getInstance();
//		int pt = this.getPropsType();
//		int color = 0xffffff;
//		if(pt == 1){
//			color = 0x1dcd00; //绿
//		}else if(pt == 2){
//			color = 0x009cff; //蓝
//		}else if(pt == 3){
//			color = 0xe6028d; //紫
//		}
//
//		if(this.isHaveValidDays() && this.getMaxValidDays() != 0){
//			sb.append(Translate.translate.text_3439+AritcleInfoHelper.formatTimeDisplay(this.getMaxValidDays()*60000)+"\n");
//		}
//
//		sb.append("[color="+color+"]"+this.getName()+"[/color]");
//		
//
//			if(this.getBindStyle() == Article.BINDTYPE_USE){
//				sb.append("\n[color="+color+Translate.translate.text_3741);
//			}else if(this.getBindStyle() == Article.BINDTYPE_PICKUP){
//				sb.append("\n[color="+color+Translate.translate.text_3579);
//			}else if(this.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP){
//				sb.append("\n[color="+color+Translate.translate.text_3579);
//			}
////			else{
////				sb.append("\n[color="+color+"]不绑定[/color]");
////			}
//
//		
//		//使用限制
//		if(this.getLevelLimit() > 0){
//			
//				sb.append("\n[color="+0xff0000+Translate.translate.text_3743 + this.getLevelLimit()+"[/color]");
//			
//		}
//		if(this.isFightStateLimit()){
//			sb.append(Translate.translate.text_3744);
//		}
////		if(this.isGameMapLimitFlag()){
////			GameMapLimit gml = this.getGml();
////			if(gml != null && gml.getHint() != null){
////				
////					sb.append("\n[color="+0xff0000+"]"+gml.getHint()+"[/color]");
////				
////			}
////		}
//		
//		PropsCategory pc = am.getPropsCategoryByCategoryName(this.getCategoryName());
//		if(pc != null && pc.getCooldownLimit() > 1000){
//			sb.append("\n[color="+0x1dcd00+Translate.translate.text_3745+Utils.formatTimeDisplay2(pc.getCooldownLimit())+Translate.translate.text_3746);
//		}
//		
//		if(this.getDescription() != null && this.getDescription().length() != 0){
//			sb.append("\n[color="+0xffba00+"]"+this.getDescription()+"[/color]");
//		}
//		
//		if(this.getComment() != null && this.getComment().length() != 0){
//			sb.append("\n[color="+0xffba00+"]"+this.getComment()+"[/color]");
//		}
//		return sb.toString();
//	}
}
