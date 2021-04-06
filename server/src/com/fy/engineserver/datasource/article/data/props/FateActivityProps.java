package com.fy.engineserver.datasource.article.data.props;

import static com.fy.engineserver.datasource.language.Translate.不在指定地图;
import static com.fy.engineserver.datasource.language.Translate.你不在凤栖梧桐区域;
import static com.fy.engineserver.datasource.language.Translate.你不在指定国家;
import static com.fy.engineserver.datasource.language.Translate.对方不在凤栖梧桐区域;
import static com.fy.engineserver.datasource.language.Translate.对方不在指定国家;
import static com.fy.engineserver.datasource.language.Translate.对方不在指定地图;
import static com.fy.engineserver.datasource.language.Translate.对方不在线;
import static com.fy.engineserver.datasource.language.Translate.没有对应的活动;
import static com.fy.engineserver.datasource.language.Translate.距离太远;
import static com.fy.engineserver.datasource.language.Translate.还没有找到有缘人请选择有缘人跟你一起进行活动;
import static com.fy.engineserver.datasource.language.Translate.还达不到使用条件;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.fateActivity.FateActivityType;
import com.fy.engineserver.activity.fateActivity.FateManager;
import com.fy.engineserver.activity.fateActivity.base.FateActivity;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.FateActivityPropsEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.BEGIN_ACTIVITY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SEEM_HINT_RES;
import com.fy.engineserver.sprite.ActivityRecordOnPlayer;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.Utils;


public class FateActivityProps extends Props {

	/**
	 * 国内 国外 仙缘  论道 
	 */
	private byte type;
	protected String avata;
	
	@Override
	public String canUse(Player p) {
		return super.canUse(p);
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		
		if(!super.use(game,player,ae)){
			return false;
		}
		if(ae instanceof FateActivityPropsEntity){
			FateActivityPropsEntity fe = (FateActivityPropsEntity)ae;
			
			// 仙缘 0       论道1
			long activityId = -1;
			int activityType = fe.getActivityType();
			if(activityType == 0){
				activityId = player.getActivityRecord((byte)0).getInitiativeActivityId();
				if(activityId < 0){
					activityId = player.getActivityRecord((byte)1).getInitiativeActivityId();
				}
			}else{
				activityId = player.getActivityRecord((byte)2).getInitiativeActivityId();
				if(activityId < 0){
					activityId = player.getActivityRecord((byte)3).getInitiativeActivityId();
				}
			}
			FateActivity fa = FateManager.getInstance().getFateActivity(activityId);
			if(fa != null){
				byte type = fa.getTemplate().getType();
				if(进行活动(player, activityId,type)){
					PlayerManager pm = PlayerManager.getInstance();
					
					Player p1;
					Player p2;
						try {
							p1 = pm.getPlayer(fa.getInviteId());
							p2 = pm.getPlayer(fa.getInvitedId());
							
							if(p1.isIsUpOrDown()){
								p1.downFromHorse();
							}
							if(p2.isIsUpOrDown()){
								p2.downFromHorse();
							}
							
							//设置坐的状态
							p1.setSitdownState(true);
							p2.setSitdownState(true);
							
							if(fa.getTemplate().getType() == FateActivityType.国内仙缘.type || fa.getTemplate().getType() == FateActivityType.国外仙缘.type){
//								String st = "等红烛燃尽,你就能完成仙缘任务了";
								String st = Translate.text_仙缘;
								SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), fa.getTemplate().getType(), st);
								p1.addMessageToRightBag(res);
								p2.addMessageToRightBag(res);
							}else{
//								String st = "等宴席结束,你就能完成论道任务了";
								String st = Translate.text_论道;
								SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), fa.getTemplate().getType(), st);
								p1.addMessageToRightBag(res);
								p2.addMessageToRightBag(res);
							}
							return true;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}else{
				FateManager.logger.error("[玩家使用仙缘道具失败] [没有对应的活动] ["+player.getLogString()+"] [活动类型:"+activityType+"] [对应的活动id:"+activityId+"]");
			}
		}
		return false;
	}

	
	public boolean 进行活动(Player player,long activityId,byte type){
		
		if (FateManager.logger.isDebugEnabled()) {
			FateManager.logger.debug("[进行活动参数] [活动类型] ["+type+"] []");
		}
		ActivityRecordOnPlayer ar = player.getActivityRecord(type);
		long id = ar.getInitiativeActivityId();
		if(id == activityId){
			FateActivity fa = FateManager.getInstance().getFateActivity(activityId);
			PlayerManager pm = PlayerManager.getInstance();
			
			if(fa.getInvitedId() < 0){
				player.sendError(还没有找到有缘人请选择有缘人跟你一起进行活动);
				return false;
			}
			Player p1;
			Player p2;
			try {
				p1 = pm.getPlayer(fa.getInviteId());
				p2 = pm.getPlayer(fa.getInvitedId());
				boolean isHelpP = false;
				if(p2.getName().equals(ActivitySubSystem.helpPlayerName)){
					isHelpP = true;
				}
				
				if(p1.isOnline() && (p2.isOnline() || isHelpP)){
					if(p1.getCurrentGame() == null || (!isHelpP && p2.getCurrentGame() == null)){
						FateManager.logger.error("[使用仙缘道具] [game null] ["+p1.getLogString()+"]");
						return false;
					}
					if(p1.getCurrentGame().country == fa.getCountry()  && (isHelpP || p2.getCurrentGame().country == fa.getCountry())){
						if((isHelpP || (p1.getCurrentGame().getGameInfo().getName().equals(p2.getCurrentGame().getGameInfo().getName())) && p1.getCurrentGame().getGameInfo().getName().equals(fa.getMapName()))) {
							
							String targeReginName = fa.getTemplate().getRegionName().trim();
							boolean bln = false;
							boolean bln1 = false;
							String map1 = p1.getCurrentMapAreaName();
							String[] maps1 = p1.getCurrentMapAreaNames();
							
							if(map1 != null && map1.equals(targeReginName)){
								bln1 = true;
							}
							if(!bln1){
								if(maps1 != null){
									for(String st : maps1){
										if(st.equals(targeReginName)){
											bln1 = true;
											break;
										}
									}
								}
							}
							if(bln1 && !isHelpP){
								String map2 = p2.getCurrentMapAreaName();
								String[] maps2 = p2.getCurrentMapAreaNames();
								
								if(map2 != null && map2.equals(targeReginName)){
									bln = true;
								}
								if(!bln){
									if(maps2 != null){
										for(String st : maps2){
											if(st.equals(targeReginName)){
												bln = true;
												break;
											}
										}
									}
								}
							}
							
							if(bln1 && isHelpP){
								bln = true;
							}
							if(bln){
								if(isHelpP || Utils.getDistanceA2B(p1.getX(),p1.getY(), p2.getX(), p2.getY()) <= fa.getTemplate().getDistance()){
									fa.setState(FateActivity.可以进行);
								}else{
									p1.sendError(距离太远);
									return false;
								}
							}else{
								if(bln1){
									p1.sendError(对方不在凤栖梧桐区域);
								}else{
									p1.sendError(你不在凤栖梧桐区域);
								}
								return false;
							}
						}else{
							if(p1.getCurrentGame().getGameInfo().getName().equals(fa.getMapName())){
								p1.sendError(对方不在指定地图);
							}else{
								p1.sendError(不在指定地图);
							}
							return false;
						}
					}else{
						if(p1.getCurrentGame().country == fa.getCountry()){
							p1.sendError(对方不在指定国家);
						}else{
							p1.sendError(你不在指定国家);
						}
						return false;
					}
				}else{
					p1.sendError(对方不在线);
					return false;
				}
				
			} catch (Exception e1) {
				FateManager.logger.error("[使用仙缘道具] ["+player.getLogString()+"]",e1);
			}
			
			if(fa.getState() == FateActivity.可以进行 && fa.checkActive(player)){
				fa.setState(FateActivity.进行状态);
				fa.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				try {
					Player invited = PlayerManager.getInstance().getPlayer(fa.getInvitedId());
					
					if(!invited.getName().equals(ActivitySubSystem.helpPlayerName)){
						int x = (player.getX()+invited.getX())/2;
						int y = (player.getY()+invited.getY())/2;
						BEGIN_ACTIVITY_RES res = new BEGIN_ACTIVITY_RES(GameMessageFactory.nextSequnceNum(), type, x, y,fa.getTemplate().getDuration());
						player.addMessageToRightBag(res);
						invited.addMessageToRightBag(res);
					}else{
						int x = (player.getX()+player.getX())/2;
						int y = (player.getY()+player.getY())/2;
						BEGIN_ACTIVITY_RES res = new BEGIN_ACTIVITY_RES(GameMessageFactory.nextSequnceNum(), type, x, y,fa.getTemplate().getDuration());
						player.addMessageToRightBag(res);
					}
					if(FateManager.logger.isWarnEnabled()){
						FateManager.logger.warn("[使用道具完成活动] ["+player.getLogString()+"] ["+invited.getLogString()+"]");
					}
					return true;
					
				} catch (Exception e) {
					FateManager.logger.error("[使用道具完成活动] ["+player.getLogString()+"]",e);
				}
				
			}else{
				player.sendError(还达不到使用条件);
			}
		}else{
			player.sendError(没有对应的活动);
		}
		return false;
	}
	
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}
	
	public String getAvata() {
		return avata;
	}
	public void setAvata(String avata) {
		this.avata = avata;
	}
}
