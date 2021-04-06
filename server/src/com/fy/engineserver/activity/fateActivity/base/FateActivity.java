package com.fy.engineserver.activity.fateActivity.base;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.STRING_2;
import static com.fy.engineserver.datasource.language.Translate.translateString;
import static com.fy.engineserver.datasource.language.Translate.xx已经开始活动;
import static com.fy.engineserver.datasource.language.Translate.xx已经离开凤栖梧桐区域将不能得到经验;
import static com.fy.engineserver.datasource.language.Translate.与你进行XX的XX已经到达凤栖梧桐;
import static com.fy.engineserver.datasource.language.Translate.你已到达凤栖梧桐请等待XX到达进行XX;
import static com.fy.engineserver.datasource.language.Translate.请求过期xx选择了别人;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.fateActivity.FateActivityType;
import com.fy.engineserver.activity.fateActivity.FateManager;
import com.fy.engineserver.activity.fateActivity.FateTemplate;
import com.fy.engineserver.activity.fateActivity.FinishRecord;
import com.fy.engineserver.activity.fateActivity.FinishRecord.FinishRecordComparator;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.ENTER_GETACTIVITY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_DELIVER_TASK_REQ;
import com.fy.engineserver.message.SEEM_HINT_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.ActivityRecordOnPlayer;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class FateActivity implements Cacheable, CacheListener{
	
	//类型 国内外仙缘  国内外论道
	protected byte activityType;
	
	protected static FinishRecordComparator comparator= new FinishRecord.FinishRecordComparator();
	
	@SimpleId
	protected long id;
	@SimpleVersion
	protected int version;
	
	public static byte 开始状态 = 0;
	public static byte 选人状态 = 1;
	public static byte 选人成功 = 2;
	public static byte 可以进行 = 3;
	public static byte 进行状态 = 4;
	public static byte 完成状态 = 5;
	public static byte 删除状态 = 6;
	public static byte 距离太远 = 7;
	
	public transient String [] stateStr = {"开始状态","选人状态","选人成功","可以进行","进行状态","完成状态","删除状态","距离太远"};
	public String getStateStr(){
		try {
			return stateStr[state];
		} catch (Exception e) {
			return state+"";
		}
	}
	
	public transient String [] types = {"国内仙缘","国外仙缘","国内论道","国外论道"};
	public String getActivityType(){
		try {
			return types[activityType];
		} catch (Exception e) {
			return activityType+"";
		}
	}
	
	public transient String [] countrys = {"","昆仑","九州","万法"};
	public String getCountryName(){
		return countrys[country];
	}
	
	public transient boolean bln1 = false;
	public transient boolean bln2 = false;
	
	public boolean deleted;
	
	public FateActivity(){
		
	}
	public FateActivity(long id){
		setId(id);
		setState(开始状态);
	}
	
	
	private int fateNPC = 500002; 
	private int beerNPC = 500003;
	private int npcTemplate;
	private transient long npcId;
	private int x;
	private int y;
	
	public transient boolean npcInit;
	
	protected long templateId = -1; // 跟任务id对应
	protected transient String activityName;
	protected transient FateTemplate template;
	protected transient String mapName;
	private int country;
	/**
	 * 邀请人id
	 */
	protected long inviteId;
	
	private boolean inviteArrive;
	
	/**
	 * 被邀请人id
	 */
	protected long invitedId = -1;
	private boolean invitedArrive;
	
	/**
	 * 客户端点刷新时间
	 */
	protected long lastFlushTime;
	
	/**
	 * 体力级别
	 */
	protected int energyLevel = 1;
	
	protected List<Long> randomUndo = new ArrayList<Long>();

	protected List<Long> randomdo = new ArrayList<Long>();
	
	protected long lastUpdateTime;
	
	/**
	 * 开始时间  使用物品开始
	 */
	protected long startTime;
	
	/**
	 * 上次增加经验时间
	 */
	protected long lastAddExpTime;
	
	
	protected transient boolean forceFlush;
	/**
	 * 活动状态 
	 */
	protected byte state = -1;
	
	/**
	 * 第几次成功
	 */
	protected int successNum = 0;
	
//	protected transient Random random = new Random();
	
	
	public void init(){
		if(this.getTemplateId() >= 0){
			FateTemplate ft = FateManager.getInstance().getMap().get((long)(this.getTemplateId()));
			setTemplate(ft);
			this.activityType = ft.getType();
			setActivityName(ft.getActivityName());
			setMapName(ft.getMapName());
		}else{
			if(FateManager.logger.isWarnEnabled())
				FateManager.logger.warn("[初始化活动错误] [对应的模板id小于0] ["+inviteId+"] []");
			return;
		}
	}
	
	public void flush(Player player){
		
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PlayerManager pm = PlayerManager.getInstance();
		if(state == 开始状态 || forceFlush){
			
			if(state == 开始状态){
				state = 选人状态;
			}
			
			ActivityRecordOnPlayer active = null;
			Player activePlayer = null;
			try {
//				activePlayer = pm.getPlayer(this.inviteId);
				activePlayer = player;
				active = activePlayer.getActivityRecord(this.template.getType());
			} catch (Exception e1) {
				FateManager.logger.error("[刷新仙缘活动异常] ["+player.getLogString()+"]",e1);
			}
			List<FinishRecord> list = active.getFinishRecordList();
			List<Long> listUndo = new ArrayList<Long>();
			//上次完成    下次开始   或则第一次
			this.lastFlushTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			// 刷新没有跟自己做过的
			int unDoNum = this.template.getUndoNum();
			Player[] ps = pm.getOnlinePlayers();
			if(ps.length <=1){
				if(FateManager.logger.isInfoEnabled()){
					FateManager.logger.info("在线人数在1");
				}
				return;
			}
			int max = ps.length;
			int last = 0;
			boolean sameSex = true;
			if(this.template.getType() == FateActivityType.国内仙缘.type || this.template.getType() == FateActivityType.国外仙缘.type){
				sameSex = false;
			}
			
			boolean sameCountry = false;
			if(this.template.getType() == FateActivityType.国内仙缘.type || this.template.getType() == FateActivityType.国内论道.type){
				sameCountry = true;
			}
			for(int i= 0;i<unDoNum;){
				int num = FateManager.getInstance().random.nextInt(max);
				Player invited = ps[num];
				if(last >= 200)break;
				++last;
				//40级别后才能被选中
				if(invited.getLevel() < FateManager.参加活动的级别){
					continue;
				}
				ActivityRecordOnPlayer ar = invited.getActivityRecord(template.getType());
				if(sameSex){
					if(activePlayer.getSex() != invited.getSex()){
						continue;
					}
				}else{
					if(activePlayer.getSex() == invited.getSex()){
						continue;
					}
				}
				
				if(sameCountry){
					if(activePlayer.getCountry() != invited.getCountry()){
						continue;
					}
				}else{
					if(activePlayer.getCountry() == invited.getCountry()){
						continue;
					}
				}
				
				if(ar.isCanInvited(template)){
					if(invited.getId() != this.inviteId){
						if(!listUndo.contains(invited.getId())){
							boolean bln = true;
							for(FinishRecord fr : list){
								if(fr.getPlayerId() == invited.getId()){
									bln = false;
									break;
								}
							}
							if(bln){
								listUndo.add(invited.getId());
								i++;
							}
						}
					}
				}
			}
			setRandomUndo(listUndo);
				
			
			List<Long> listdo = new ArrayList<Long>();
			
			//刷新跟自己做过的
			int doNum = this.template.getDoNum();
			
			long lastPlayerId = active.getLastFinishActivityPlayerId();
			try {
				if(lastPlayerId > 0){
					if(pm.isOnline(lastPlayerId)){
						Player first= pm.getPlayer(lastPlayerId);
						boolean bln = true;
						if(sameSex){
							if(activePlayer.getSex() != first.getSex()){
								bln = false;
							}
						}else{
							if(activePlayer.getSex() == first.getSex()){
								bln = false;
							}
						}
						
						if(sameCountry){
							if(activePlayer.getCountry() != first.getCountry()){
								bln = false;
							}
						}else{
							if(activePlayer.getCountry() == first.getCountry()){
								bln = false;
							}
						}
						
						if(bln){
							if(first.getActivityRecord(this.template.getType()).isCanInvited(this.template)){
								listdo.add(lastPlayerId);
							}
						}
					}
				}
				
			} catch (Exception e) {
				FateManager.logger.error("[刷新活动] ["+player.getLogString()+"]",e);
			}
			if(list.size() > 1){
				Collections.sort(list, comparator);
				
//				for(FinishRecord fr :list){
				int frLength = list.size();
				FinishRecord fr = null;
				for(int i= 0;i<frLength;i++){
					fr = list.get(i);
					if(listdo.size() >=doNum){
						break;
					}else{
						long id = fr.getPlayerId();
						try {
							if(pm.isOnline(id)){
								Player second = pm.getPlayer(id);
								boolean bln = true;
								if(sameSex){
									if(activePlayer.getSex() != second.getSex()){
										bln = false;
									}
								}else{
									if(activePlayer.getSex() == second.getSex()){
										bln = false;
									}
								}
								
								if(sameCountry){
									if(activePlayer.getCountry() != second.getCountry()){
										bln = false;
									}
								}else{
									if(activePlayer.getCountry() == second.getCountry()){
										bln = false;
									}
								}
								
								if(bln){
									if(second.isOnline() && second.getActivityRecord(template.getType()).isCanInvited(template)){
										if(!listdo.contains(id)){
											listdo.add(id);
										}
									}
								}
							}
						} catch (Exception e) {
							FateManager.logger.error("[刷新活动] ["+player.getLogString()+"]",e);
						}
					}
				}
			}
			setRandomdo(listdo);
			if(FateManager.logger.isWarnEnabled())
				FateManager.logger.warn("["+activityName+"] [刷新] [成功] ["+inviteId+"] [] ",com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start);
		}
		
	}
	public  String rewardname = Translate.仙缘论道令碎片;
	public  boolean isreward = true;
	public transient Platform [] limitplatform = {};
	public void 仙缘论道令奖励(){
		try {
			if(isreward&&(!PlatformManager.getInstance().isPlatformOf(limitplatform))){
				PlayerManager pm = PlayerManager.getInstance();
				Player invite = pm.getPlayer(this.getInviteId());
				Player invited = pm.getPlayer(this.getInvitedId());
				byte fatetype = this.getTemplate().getType();
				int rewardnum = 0;
				if(fatetype == 1 || fatetype == 3){
					rewardnum = 2;
				}else if(fatetype == 0 || fatetype == 2){
					rewardnum = 1;
				}
				if(!invited.getName().equals(ActivitySubSystem.helpPlayerName)){
					Article a = ArticleManager.getInstance().getArticle(rewardname);
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.仙缘论道, invited, a.getColorType(), rewardnum, true);
					MailManager mm = MailManager.getInstance();
					String mes = Translate.translateString(Translate.恭喜好心人内容, new String[][] {{Translate.STRING_1, invite.getName()}});
					mm.sendMail(invited.getId(), new ArticleEntity[]{ae},  new int[]{rewardnum},Translate.恭喜好心人, mes, 0, 0, 0, "仙缘论道");
					invited.sendError(Translate.奖励已通过邮件的形式发送);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	long lastLoggerTime = 0;
	
	transient Random  random = new Random();
	
	public void heartBeat(){
		
		if(System.currentTimeMillis() - lastLoggerTime >= 10000){
			lastLoggerTime = System.currentTimeMillis();
			FateManager.logger.warn("[====仙缘活动测试====] ["+getStateStr()+"] ["+getActivityType()+"] [邀请id:"+inviteId+"] [是否到达:"+inviteArrive+"] [被邀请id:"+invitedId+"] [是否到达:"+invitedArrive+"]");
		}
		
		if(state == 删除状态) {
			setDeleted(true);
			try{
				if(npcInit && npcId > 0){
					npcInit = false;
					String map = this.template.getMapName();
					Game game = GameManager.getInstance().getGameByName(map, country);
					
					for(LivingObject lo :game.getLivingObjects()){
						if(lo.getId() == this.npcId){
							game.removeSprite((NPC)lo);
						}
					}
					npcId = -1;
					if(FateManager.logger.isWarnEnabled()){
						FateManager.logger.warn("[活动过期移除npc] ["+getInviteId()+"] ["+getInvitedId()+"]");
					}
				}
			}catch(Exception e){
				FateManager.logger.error("[活动过期移除npc异常] ["+getInviteId()+"] ["+getInvitedId()+"]",e);
			}
			return;
		}
		if(state == 进行状态){
			PlayerManager pm = PlayerManager.getInstance();
			if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime >= template.getDuration()*1000){
				setState(完成状态);
				仙缘论道令奖励();
			}else{
				
				if(!npcInit){
					
					byte fatetype = this.getTemplate().getType();
					if(fatetype == FateActivityType.国内仙缘.type || fatetype == FateActivityType.国外仙缘.type){
						npcTemplate = fateNPC;
					}else{
						npcTemplate = beerNPC;
					}
					npcInit = true;
					Player invited = null;
					try {
						invited = pm.getPlayer(this.getInvitedId());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					if(invited != null && !invited.getName().equals(ActivitySubSystem.helpPlayerName)){
						try {
							NPC npc = MemoryNPCManager.getNPCManager().createNPC(npcTemplate);
							setNpcId(npc.getId());
							String map = this.template.getMapName();
							Game game = GameManager.getInstance().getGameByName(map, country);
							Player invite = pm.getPlayer(this.getInviteId());
							int x = (invite.getX()+invited.getX())/2;
							int y = (invite.getY()+invited.getY())/2;
							
							npc.setX(x);
							npc.setY(y);
							setX(x);
							setY(y);
							game.addSprite(npc);
							
							FateManager.logger.warn("[活动初始化放入npc] [npc:"+npc.getName()+"] ["+invite.getLogString()+"] ["+invited.getLogString()+"]");
						} catch (Exception e) {
							FateManager.logger.error("[活动开始] [放入npc]",e);
						}
					}
					
				}
			}
		}
		
		
		if(getState() == 完成状态){
			
			setState(删除状态);
			if(npcInit && npcId > 0){
				npcInit = false;
				String map = this.template.getMapName();
				Game game = GameManager.getInstance().getGameByName(map, country);
				
				for(LivingObject lo :game.getLivingObjects()){
					if(lo.getId() == this.npcId){
						game.removeSprite((NPC)lo);
					}
				}
				npcId = -1;
				if(FateManager.logger.isWarnEnabled()){
					FateManager.logger.warn("[活动结束移除npc] ["+getInviteId()+"] ["+getInvitedId()+"]");
				}
			}
			//添加记录  通知客户端图标
			Player p1 = null;
			Player p2 = null;
			try {
				p1 = PlayerManager.getInstance().getPlayer(this.getInviteId());
				if(p1 != null && p1.isOnline()){
					long id = this.getTemplate().getId();
					Task task = TaskManager.getInstance().getTask(id);
					TaskEntity te = p1.getTaskEntity(id);
					if (task != null && te != null) {
						if(te.getStatus() ==  TaskEntity.TASK_STATUS_GET){
							te.setStatus(TaskEntity.TASK_STATUS_COMPLETE);
							NOTICE_CLIENT_DELIVER_TASK_REQ req = new NOTICE_CLIENT_DELIVER_TASK_REQ(GameMessageFactory.nextSequnceNum(), id);
							p1.addMessageToRightBag(req);
							
							if(this.getTemplate().getType() == FateActivityType.国内仙缘.type ||this.getTemplate().getType() == FateActivityType.国外仙缘.type ){
								AchievementManager.getInstance().record(p1, RecordAction.仙缘完成次数);
								if (this.getTemplate().getType() == FateActivityType.国内仙缘.type) {
									AchievementManager.getInstance().record(p1, RecordAction.国内仙缘次数);
								} else {
									AchievementManager.getInstance().record(p1, RecordAction.国外仙缘次数);
								}
								ActivenessManager.getInstance().record(p1, ActivenessType.仙缘论道);
							}else if(this.getTemplate().getType() == FateActivityType.国内论道.type ||this.getTemplate().getType() == FateActivityType.国外论道.type ){
								AchievementManager.getInstance().record(p1, RecordAction.论道完成次数);
								if (this.getTemplate().getType() == FateActivityType.国内论道.type) {
									AchievementManager.getInstance().record(p1, RecordAction.国内论道次数);
								} else {
									AchievementManager.getInstance().record(p1, RecordAction.国外论道次数);
								}
								ActivenessManager.getInstance().record(p1, ActivenessType.仙缘论道);
							}
							try {
								Player tempP = PlayerManager.getInstance().getPlayer(this.getInvitedId());
								AchievementManager.getInstance().record(tempP, RecordAction.接受并完成仙缘或论道任务次数);
							} catch (Exception e) {
								PlayerAimManager.logger.error("[目标系统] [统计玩家帮忙完成仙缘论道次数] [异常] [" + p1.getLogString() + "] [this.getInvitedId():" + this.getInvitedId() + "]", e);
							}
							
							if(FateManager.logger.isWarnEnabled())
								FateManager.logger.warn("[正常完成活动] [活动id:"+this.getId()+"] [统计成就] ["+this.getTemplate().getType()+"] ["+this.getActivityName()+"] ["+p1.getLogString()+"]");
						}
					}else{
						FateManager.logger.error("[完成活动错误] [没有指定任务]  ["+p1.getLogString()+"] [任务id:"+id+"]");
					}
				}
				
				p2 = PlayerManager.getInstance().getPlayer(this.getInvitedId());
			} catch (Exception e) {
				FateManager.logger.error("[仙缘活动完成错误] [没有指定玩家] [邀请者:"+this.inviteId+"] [被邀请者:"+this.invitedId+"]",e);
			}
			
			try{
				if(p1 != null && p1.isOnline()){
					
					p1.getActivityRecord(this.getTemplate().getType()).addFinishRecord(p2, false,this.getTemplate().getType());
					if(p1.isSitdownState()){
						p1.setSitdownState(false);
					}
					
					FateManager fm = FateManager.getInstance();
					FateActivityType fatype = fm.getfateType(this.getTemplate().getType());
	//				fm.noticePlayerFateActivity(p1, fatype);
					fm.finishNotice(p1, fatype.type);
					if(FateManager.logger.isWarnEnabled()){
						FateManager.logger.warn("[活动正常完成] ["+p1.getLogString()+"] ["+fatype.type+"]");
					}
				}
				if(p2 != null && p2.isOnline() && !p2.getName().equals(ActivitySubSystem.helpPlayerName)){
					p2.getActivityRecord(this.getTemplate().getType()).addFinishRecord(p1, true,this.getTemplate().getType());
					if(p2.isSitdownState()){
						p2.setSitdownState(false);
					}
					
					FateManager fm = FateManager.getInstance();
					FateActivityType fatype = fm.getfateType(this.getTemplate().getType());
	//				fm.noticePlayerFateActivity(p2, fatype);
					fm.finishNotice(p2, fatype.type);
					if(FateManager.logger.isWarnEnabled()){
						FateManager.logger.warn("[活动正常完成] ["+p2.getLogString()+"] ["+fatype.type+"]");
					}
				}
			}catch(Exception e){
				FateManager.logger.error("[仙缘活动完成错误] [邀请者:"+this.inviteId+"] [被邀请者:"+this.invitedId+"]",e);
			}
			
			return;
		}
		
		
		if(state == 选人成功){
			if(invitedId >0 && inviteId >0){
				Player p1 = null;
				try {
					p1 = PlayerManager.getInstance().getPlayer(inviteId);
				} catch (Exception e) {
					FateManager.logger.error("["+this.getActivityName()+"] [选人状态inviteId] ["+inviteId+"] [玩家不存在]",e);
					return;
				}
				Player p2 = null;
				try {
					p2 = PlayerManager.getInstance().getPlayer(invitedId);
				} catch (Exception e) {
					FateManager.logger.error("["+this.getActivityName()+"] [选人状态invitedId] ["+p1.getName()+"] ["+invitedId+"] [玩家不存在]",e);
					this.setInvitedId(-1);
					return;
				}
				try{
				if(!inviteArrive && !invitedArrive){
					if(p1.isOnline() && p1.getCurrentGame()!=null &&p1.getCurrentGame().country ==this.country && p1.getCurrentGame().getGameInfo().getName().equals(this.mapName) && this.chechRegin(p1, this.template.getRegionName())){
						// p1到  
						setInviteArrive(true);
//						SEEM_HINT_RES res1 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, "你已达到凤栖梧桐，请等待"+p2.getName()+"到达，进行"+this.getTemplate().getActivityName());
						SEEM_HINT_RES res1 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, translateString(你已到达凤栖梧桐请等待XX到达进行XX, new String[][]{{STRING_1,p2.getName()},{STRING_2,this.getTemplate().getActivityName()}}));
						p1.addMessageToRightBag(res1);
						if(p2.isOnline()){
//							SEEM_HINT_RES res2 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, "与你进行"+this.getTemplate().getActivityName()+"的"+p1.getName()+"已经到达凤栖梧桐");
							SEEM_HINT_RES res2 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, translateString(与你进行XX的XX已经到达凤栖梧桐, new String[][]{{STRING_1,this.getTemplate().getActivityName()},{STRING_2,p1.getName()}}));
							p2.addMessageToRightBag(res2);
						}
						if(FateManager.logger.isWarnEnabled()){
							FateManager.logger.warn("[活动id:"+this.getId()+"] [邀请者先到] [被邀请者没到] ["+p1.getLogString()+"]");
						}
					}else if(p2.isOnline() && p2.getCurrentGame()!=null &&p2.getCurrentGame().country ==this.country && p2.getCurrentGame().getGameInfo().getName().equals(this.mapName) && this.chechRegin(p2, this.template.getRegionName())){
						// p2到
						setInvitedArrive(true);
//						SEEM_HINT_RES res1 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, "你已到达凤栖梧桐，请等待"+p1.getName()+"到达，进行"+this.getTemplate().getActivityName());
						SEEM_HINT_RES res1 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, translateString(你已到达凤栖梧桐请等待XX到达进行XX, new String[][]{{STRING_1,p1.getName()},{STRING_2,this.getTemplate().getActivityName()}}));
						p2.addMessageToRightBag(res1);
						if(p1.isOnline()){
//							SEEM_HINT_RES res2 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, "与你进行"+this.getTemplate().getActivityName()+"的"+p2.getName()+"已经到达凤栖梧桐");
							SEEM_HINT_RES res2 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, translateString(与你进行XX的XX已经到达凤栖梧桐, new String[][]{{STRING_1,this.getTemplate().getActivityName()},{STRING_2,p2.getName()}}));
							p1.addMessageToRightBag(res2);
						}
						if(FateManager.logger.isWarnEnabled()){
							FateManager.logger.warn("[活动id:"+this.getId()+"] [被邀请者先到] [邀请者没到] ["+p2.getLogString()+"]");
						}
					}
				}else if(inviteArrive && !invitedArrive){
					if(p2.isOnline() && p2.getCurrentGame()!=null &&p2.getCurrentGame().country ==this.country && p2.getCurrentGame().getGameInfo().getName().equals(this.mapName) && this.chechRegin(p2, this.template.getRegionName())){
						//邀请者已到，
							setInvitedArrive(true);
							if(p1.isOnline()){
//								SEEM_HINT_RES res2 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, "与你进行"+this.getTemplate().getActivityName()+"的"+p2.getName()+"已经到达凤栖梧桐");
								SEEM_HINT_RES res2 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, translateString(与你进行XX的XX已经到达凤栖梧桐, new String[][]{{STRING_1,this.getTemplate().getActivityName()},{STRING_2,p2.getName()}}));
								p1.addMessageToRightBag(res2);
							}
							if(FateManager.logger.isWarnEnabled()){
								FateManager.logger.warn("[活动id:"+this.getId()+"] [邀请者已到] [被邀请者也到] ["+p2.getLogString()+"]");
							}
						}
				}else if(!inviteArrive && invitedArrive){
					if(p1.isOnline() &&p1.getCurrentGame()!=null && p1.getCurrentGame().country ==this.country && p1.getCurrentGame().getGameInfo().getName().equals(this.mapName) && this.chechRegin(p1, this.template.getRegionName())){
						//被邀请已到
						setInviteArrive(true);
						if(p2.isOnline()){
//							SEEM_HINT_RES res2 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, "与你进行"+this.getTemplate().getActivityName()+"的"+p1.getName()+"已经到达凤栖梧桐");
							SEEM_HINT_RES res2 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, translateString(与你进行XX的XX已经到达凤栖梧桐, new String[][]{{STRING_1,this.getTemplate().getActivityName()},{STRING_2,p1.getName()}}));							p2.addMessageToRightBag(res2);
						}
						if(FateManager.logger.isWarnEnabled()){
							FateManager.logger.warn("[活动id:"+this.getId()+"] [被邀请者已到] [邀请者也到] ["+p2.getLogString()+"]");
						}
					}
				}
				
				if(inviteArrive && invitedArrive){
					setState(可以进行);
				}
				}catch(Exception e){
					FateManager.logger.error("[选人状态错误] [invite:"+inviteId+"] [invited"+invitedId+"]",e);
				}
			}
		}
		
		if(state == 进行状态){
			
			Player p1 = null;
			try {
				p1 = PlayerManager.getInstance().getPlayer(inviteId);
			} catch (Exception e) {
				FateManager.logger.error("["+this.getActivityName()+"] [进行中] ["+inviteId+"] [玩家不存在]",e);
			}
			Player p2 = null;
			try {
				p2 = PlayerManager.getInstance().getPlayer(invitedId);
			} catch (Exception e) {
				FateManager.logger.error("["+this.getActivityName()+"] [进行中] ["+invitedId+"] [玩家不存在]",e);
			}
			boolean addexp1 = false;
			boolean addexp2 = false;
			//不是同时加俩个人经验 或是都不加
			if(p1 != null && p1.isOnline()){
				
				if(this.chechRegin(p1, this.template.getRegionName())){
					bln1 = false;
					addexp1 = true;
					//addexp
				}else{
					if(!bln1){
						bln1 = true;
//						SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.getTemplate().getType(), p1.getName()+已经离开凤栖梧桐区域将不能得到经验);
						SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.getTemplate().getType(),translateString(xx已经离开凤栖梧桐区域将不能得到经验, new String[][]{{STRING_1,p1.getName()}}));
						p1.addMessageToRightBag(res);
						// 广播
						if(p2 != null && p2.isOnline()){
							p2.addMessageToRightBag(res);
						}
					}
					
				}
			}
			
			if(p2 != null && p2.isOnline()){
				
				if(this.chechRegin(p2, this.template.getRegionName())){
					bln2 = false;
					addexp2 = true;
					//addexp
				}else{
					if(!bln2){
						bln2 = true;
//						SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.getTemplate().getType(), p2.getName()+已经离开凤栖梧桐区域将不能得到经验);
						SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.getTemplate().getType(), translateString(xx已经离开凤栖梧桐区域将不能得到经验, new String[][]{{STRING_1,p2.getName()}}));
						p2.addMessageToRightBag(res);
						// 广播
						if(p1 != null && p1.isOnline()){
							p1.addMessageToRightBag(res);
						}
					}
					
				}
			}
//			
			
			if(lastAddExpTime <= 0){
				if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime >= template.getExpInterval()*1000){
					lastAddExpTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					addExp(p1, p2,addexp1,addexp2);
				}
			}else{
				if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lastAddExpTime >= template.getExpInterval()*1000){
					lastAddExpTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					addExp(p1, p2,addexp1,addexp2);
				}
			}
		}
	}
	
	public void addExp(Player invite,Player invited,boolean addexp1,boolean addexp2){
		
	}

	public void addSucessNum(){
		this.successNum++;
	}
	
	public boolean checkActive(Player player){
		if(player.getId() == inviteId){
			return true;
		}
		return false;
	}
	
	public boolean havaActive(Player player){
		if(player.getId() == inviteId || player.getId() == invitedId) return true;
		return false;
	}
	
	
	public String getLogString(){
		return getId() + " " + this.activityName+" "+this.inviteId + " "+ getSuccessNum() + " "+ getState();
	}
	
	/**
	 * 邀请者邀请玩家时弹出的选择级别的描述
	 * @param invite
	 * @param invited
	 * @return
	 */
	public String getKeyString(Player invite,Player invited){
		return "";
	}
	
	/**
	 * 活动的级别 对应体力值  给不同的道具
	 * @param invite
	 * @param invited
	 * @return
	 */
	public String[] getLevelContent(Player invite,Player invited){
		return  null;
	}
	
	/**
	 * 被邀请者收到邀请协议的时候的描述
	 * @param invite
	 * @param invited
	 * @return
	 */
	public String getRealInviteKeyString(Player invite,Player invited){
		return null;
	}
	
	public boolean invitedAgree(Player player,FateActivity fa,int successNum,int level){
		try{
			// 修改状态
			if(fa != null){
				
				FateManager.logger.warn("[观察玩家同意活动] ["+player.getLogString()+"] ["+fa.getLogString()+"] [" + successNum + "]");
				
				Player invite = null;
				Player invited = player;
				if(!PlayerManager.getInstance().isOnline(fa.getInviteId())){
					player.send_HINT_REQ(Translate.邀请方不在线);
					return false;
				}
				
				try {
					invite = PlayerManager.getInstance().getPlayer(fa.getInviteId());
				} catch (Exception e) {
					FateManager.logger.error("[被邀请者同意活动异常] ["+player.getLogString()+"]",e);
					return false;
				}
				
				//判断性别
				if(!invited.getName().equals(ActivitySubSystem.helpPlayerName)){
					if(fa.activityType == FateActivityType.国内仙缘.type || fa.activityType == FateActivityType.国外仙缘.type){
						//异性
						if(player.getSex() == invite.getSex()){
							FateManager.logger.error("[同意活动错误] [仙缘活动要异性] ["+player.getLogString()+"] ["+invite.getLogString()+"]");
							return false;
						}
					}else{
						//同性
						if(player.getSex() != invite.getSex()){
							FateManager.logger.error("[同意活动错误] [论道活动要同性] ["+player.getLogString()+"] ["+invite.getLogString()+"]");
							return false;
						}
					}
				}
				
				synchronized (invite) {
					boolean canInvite = invite.getActivityRecord(template.getType()).isCanInvite(template);
					boolean canInvited = true;
					if(invited != null){
						canInvited = invited.getActivityRecord(template.getType()).isCanInvited(template);
					}
					if(invited.getName().equals(ActivitySubSystem.helpPlayerName)){
						canInvited = true;
					}
					if(successNum >= fa.getSuccessNum() &&canInvite&& canInvited){
						
						if(fa.getState() == FateActivity.进行状态){
							player.send_HINT_REQ(Translate.对方活动已经开始);
							if(FateManager.logger.isWarnEnabled()){
								FateManager.logger.warn("[同意活动] [错误1] [邀请者:"+invite.getLogString()+"] ["+player.getLogString()+"] [类型:"+fa.getTemplate().getType()+"] [对方活动已经开始]");
							}
						}else if(fa.getState() == FateActivity.完成状态){
							player.send_HINT_REQ(Translate.对方活动已经完成);
							if(FateManager.logger.isWarnEnabled()){
								FateManager.logger.warn("[同意活动] [错误2] [邀请者:"+invite.getLogString()+"] ["+player.getLogString()+"] [类型:"+fa.getTemplate().getType()+"] [对方活动已经完成]");
							}
						}else{
							fa.setState(FateActivity.选人成功);
							long temp = fa.getInvitedId();
							if(temp > 0){
								try {
									Player p2 = PlayerManager.getInstance().getPlayer(temp);
									p2.getActivityRecord(template.getType()).setPassivityActivityId(-1,p2,fa.getTemplate().getType());
									if(p2.isOnline()){
	//									p2.sendError(invite.getName()+"重新选择了有缘人进行活动");
										p2.sendError(Translate.translateString(Translate.xx重新选择了有缘人进行活动, new String[][]{{Translate.STRING_1,invite.getName()}}));
										FateManager.getInstance().finishNotice(p2, fa.getTemplate().getType());
									}
									if(FateManager.logger.isWarnEnabled()){
										FateManager.logger.warn("[活动id:"+fa.getId()+"] [邀请者重选了人进行活动] [邀请者:"+invite.getLogString()+"] ["+p2.getLogString()+"] [类型:"+fa.getTemplate().getType()+"]");
									}
								} catch (Exception e) {
									FateManager.logger.error("[某人同意活动] ["+player.getLogString()+"] [替换上个被选择对象]",e);
								}
							}
							player.getActivityRecord(template.getType()).setPassivityActivityId(fa.getId(),player,fa.getTemplate().getType());
							fa.setInvitedId(player.getId());
							fa.addSucessNum();
							fa.setMapName(TransportData.getXinShouCunMap(player.getCountry()));
							fa.setEnergyLevel(level);
							fa.setInviteArrive(false);
							fa.setInvitedArrive(false);
							
							if(player.getName().equals(ActivitySubSystem.helpPlayerName)){
								if(activityType == FateActivityType.国内仙缘.type || activityType == FateActivityType.国内论道.type ){
									setCountry(invite.getCountry());
								}else{
									int countrys [] = {1,2,3};
									int newCountrys [] = new int[2];
									int index = 0;
									for(int c : countrys){
										if(c != invite.getCountry()){
											newCountrys[index] = c;
											index++;
										}
									}
									int ranIndex = random.nextInt(2);
									setCountry(newCountrys[ranIndex]);
								}
								
//								if(invite.isOnline()){
//									SEEM_HINT_RES res2 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), this.activityType, translateString(Translate.与你进行XX的XX已经到达XX凤栖梧桐, new String[][]{{STRING_1,this.getTemplate().getActivityName()},{STRING_2,player.getName()},{Translate.STRING_3,getCountryName()}}));
//									invite.addMessageToRightBag(res2);
//								}
								
								setInvitedArrive(true);
								FateManager.logger.warn("[仙缘活动] [小助手已经就位] ["+getActivityType()+"] [country1:"+(invite!=null?invite.getCountry():"null")+"] [country2:"+this.country+"] [name1:"+(invite!=null?invite.getName():"null")+"] [name2:"+player.getName()+"]");
							}else{
								fa.setCountry(player.getCountry());
							}
							
							ENTER_GETACTIVITY_RES res = new ENTER_GETACTIVITY_RES(GameMessageFactory.nextSequnceNum(), activityType);
							player.addMessageToRightBag(res);
							if(FateManager.logger.isWarnEnabled()){
								FateManager.logger.warn("[活动id:"+fa.getId()+"] [活动system] [选人成功] ["+activityType+"] [country:"+getCountry()+"] ["+player.getLogString()+"] ["+(invite != null ? invite.getLogString():"null")+"]");
							}
							return true;
						}
					}else if(!canInvite){
	//					player.send_HINT_REQ(invite.getName()+"已经开始活动");
						player.send_HINT_REQ(translateString(xx已经开始活动, new String[][]{{STRING_1,invite.getName()}}));
						if(FateManager.logger.isWarnEnabled()){
							FateManager.logger.warn("[活动id:"+fa.getId()+"] [同意活动] [错误3] [邀请者:"+invite.getLogString()+"] ["+player.getLogString()+"] [类型:"+fa.getTemplate().getType()+"] [对方活动已经开始]");
						}
					}else if(!canInvited){
						player.send_HINT_REQ(Translate.你正在进行活动);
						if(FateManager.logger.isWarnEnabled()){
							FateManager.logger.warn("[活动id:"+fa.getId()+"] [同意活动] [错误4] [邀请者:"+invite.getLogString()+"] ["+player.getLogString()+"] [类型:"+fa.getTemplate().getType()+"] [你正在进行活动]");
						}
					}else{
	//					player.send_HINT_REQ("请求过期，"+invite.getName()+"选择了别人");
						player.send_HINT_REQ(translateString(请求过期xx选择了别人, new String[][]{{STRING_1,invite.getName()}}));
						if(FateManager.logger.isWarnEnabled()){
							FateManager.logger.warn("[活动id:"+fa.getId()+"] [同意活动] [错误5] [邀请者:"+invite.getLogString()+"] ["+player.getLogString()+"] [类型:"+fa.getTemplate().getType()+"] [请求过期]");
						}
					}
				}
			}
		}catch(Exception e) {
			FateManager.logger.error("invitedAgree" + player.getLogString() + " [" + fa.getLogString()+"]", e);
		}
		return false;
	
	}
	
	
//	public boolean giveUpActivity(Player invited,FateActivity fa,byte type){
//		
//		if(fa != null){
//			if(fa.getState() >= FateActivity.进行状态){
//				invited.send_HINT_REQ("不能放弃");
//			}else{
//				fa.setState(FateActivity.选人状态);
//				fa.setInvitedId(-1);
//				invited.getActivityRecord(template.getType()).setPassivityActivityId(-1,invited,type);
//				invited.send_HINT_REQ("你放弃了"+fa.getActivityName()+"活动");
//				try {
//					Player invite = PlayerManager.getInstance().getPlayer(fa.getInviteId());
//					invite.getActivityRecord(template.getType()).setInitiativeActivityId(fa.getId(),invite,type);
//					invite.send_HINT_REQ(invited.getName()+"放弃了,请重新选人");
//					return true;
//				} catch (Exception e) {
//					FateManager.logger.error("[某人放弃活动] ["+invited.getLogString()+"]",e);
//				}
//			}
//		}else{
//			FateManager.logger.error("[拒绝活动错误] [参数错误] ["+type+"]");
//		}
//		return false;
//	}
	
	// 活动完成没有  // true完成  false 没有完成
	public boolean isFinish(){
		
		if(this.state >= 进行状态){
			if(this.startTime+this.getTemplate().getDuration()*1000 < SystemTime.currentTimeMillis()){
				return true;
			}
		}
		return false;
	}
	
	/**       set() get()            **/
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;

	}

	public FateTemplate getTemplate() {
		return template;
	}

	public void setTemplate(FateTemplate template) {
		this.template = template;

	}

	public long getInviteId() {
		return inviteId;
	}

	public void setInviteId(long inviteId) {
		this.inviteId = inviteId;
		FateManager.em.notifyFieldChange(this, "inviteId");
	}

	public long getInvitedId() {
		return invitedId;
	}

	public void setInvitedId(long invitedId) {
		this.invitedId = invitedId;
		FateManager.em.notifyFieldChange(this, "invitedId");
	}

	public long getLastFlushTime() {
		return lastFlushTime;
	}

	public void setLastFlushTime(long lastFlushTime) {
		this.lastFlushTime = lastFlushTime;
		FateManager.em.notifyFieldChange(this, "lastFlushTime");
	}

	public int getEnergyLevel() {
		return energyLevel;
	}
	public void setEnergyLevel(int energyLevel) {
		this.energyLevel = energyLevel;
		FateManager.em.notifyFieldChange(this, "energyLevel");
	}
	public List<Long> getRandomUndo() {
		return randomUndo;
	}

	public void setRandomUndo(List<Long> randomUndo) {
		this.randomUndo = randomUndo;
		FateManager.em.notifyFieldChange(this, "randomUndo");
	}

	public List<Long> getRandomdo() {
		return randomdo;
	}

	public void setRandomdo(List<Long> randomdo) {
		this.randomdo = randomdo;
		FateManager.em.notifyFieldChange(this, "randomdo");
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
		FateManager.em.notifyFieldChange(this, "startTime");
	}

	public long getLastAddExpTime() {
		return lastAddExpTime;
	}

	public void setLastAddExpTime(long lastAddExpTime) {
		this.lastAddExpTime = lastAddExpTime;
		FateManager.em.notifyFieldChange(this, "lastAddExpTime");
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
		FateManager.em.notifyFieldChange(this, "state");
	}
	
	public boolean isForceFlush() {
		return forceFlush;
	}

	public void setForceFlush(boolean forceFlush) {
		this.forceFlush = forceFlush;

	}
	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	public int getSuccessNum() {
		return successNum;
	}
	
	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
		FateManager.em.notifyFieldChange(this, "successNum");
	}
	
	
	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
		FateManager.em.notifyFieldChange(this, "templateId");
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
		FateManager.em.notifyFieldChange(this, "country");
	}

	@Override
	public int getSize() {
		return 10;
	}

	@Override
	public void remove(int type) {
		try {
			FateManager.em.save(this);
		} catch (Exception e) {
			FateManager.logger.error("[从缓存删除异常]",e);
		}
	}

	public boolean isInviteArrive() {
		return inviteArrive;
	}

	public void setInviteArrive(boolean inviteArrive) {
		this.inviteArrive = inviteArrive;
	}

	public boolean isInvitedArrive() {
		return invitedArrive;
	}

	public void setInvitedArrive(boolean invitedArrive) {
		this.invitedArrive = invitedArrive;
	}


	public long getNpcId() {
		return npcId;
	}

	public void setNpcId(long npcId) {
		this.npcId = npcId;
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		FateManager.em.notifyFieldChange(this, "x");
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		FateManager.em.notifyFieldChange(this, "y");
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
		FateManager.em.notifyFieldChange(this, "deleted");
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	public boolean chechRegin(Player player,String reginName){

		boolean bln1 = false;
		if(player.isOnline()){
			String targeReginName = this.template.getRegionName().trim();
			String map1 = player.getCurrentMapAreaName();
			String[] maps1 = player.getCurrentMapAreaNames();
			
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
		}
		return bln1;
		
	}
}
