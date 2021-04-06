package com.fy.engineserver.sprite.npc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.activity.dice.DiceManager;
import com.fy.engineserver.activity.disaster.DisasterConstant;
import com.fy.engineserver.activity.refreshbox.PickBoxProgressBar;
import com.fy.engineserver.activity.wolf.WolfGame;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.carbon.devilSquare.DevilSquareConstant;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.props.RandomPackageProps;
import com.fy.engineserver.datasource.article.data.props.RefreshMonsterPackage;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.message.NOTICE_NPC_FLASH_RES;
import com.fy.engineserver.message.NPC_ACTION_RES;
import com.fy.engineserver.message.PLAY_SOUND_REQ;
import com.fy.engineserver.sound.manager.SoundManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.TeamSubSystem;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.sprite.petdao.PetDao;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.CompoundReturn;
import com.xuanzhi.tools.transport.Message;

/**
 * 物品掉落到地上后的采集NPC
 * 
 * 
 * 
 * 
 */
public class FlopCaijiNpc extends NPC implements Cloneable {

	private static final long serialVersionUID = -4393238389156456460L;
	/**
	 * 物品实体
	 */
	public ArticleEntity ae;

	private Article article;

	private int count = 1;

	/**
	 * 刷新出的时间
	 */
	public long startTime;

	/**
	 * 刷没的时间点
	 */
	public long endTime;

	public Player owner;

	public Team team;

	public boolean isMonsterFlop = false;
	
	//经验草类型
	public int grassType = -1;

	// 可以捡取的人 不是队伍那种
	private List<Long> playersList = new ArrayList<Long>();
	
	private Message message;
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<Long> getPlayersList() {
		return playersList;
	}

	public void setPlayersList(List<Long> playersList) {
		this.playersList = playersList;
	}

	/**
	 * 0为都可以拾取，1为只有owner可以拾取(设置为1有俩种情况1：掉落是私有的，2，掉落是共有的，但是分配方式是顺序拾取(这种情况时间到了会设置为0))
	 */
	public byte flopType;

	/**
	 * 开始后多长时间所有人可以拾取，默认为开始就可以
	 */
	public long allCanPickAfterTime;

	/**
	 * 通知周围玩家播放声音
	 * 必须在这个掉落npc放到游戏中才能调用这个方法
	 */
	public void notifyAroundPlayersPlaySound(Game game) {
		if (game != null) {
			String soundName = SoundManager.getArticleFlopSound(ae);
			Fighter[] fs = game.getVisbleFighter(this, true);
			PLAY_SOUND_REQ psreq = new PLAY_SOUND_REQ(GameMessageFactory.nextSequnceNum(), soundName, true, false);
			if (fs != null) {
				for (Fighter f : fs) {
					if (f instanceof Player) {
						((Player) f).addMessageToRightBag(psreq);
					}
				}
			}
		}
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public byte getFlopType() {
		return flopType;
	}

	public void setFlopType(byte flopType) {
		this.flopType = flopType;
	}

	public long getAllCanPickAfterTime() {
		return allCanPickAfterTime;
	}

	public void setAllCanPickAfterTime(long allCanPickAfterTime) {
		this.allCanPickAfterTime = allCanPickAfterTime;
	}

	public boolean isHasBeenPickUp() {
		return hasBeenPickUp;
	}

	public void setHasBeenPickUp(boolean hasBeenPickUp) {
		this.hasBeenPickUp = hasBeenPickUp;
	}

	public ArticleEntity getAe() {
		return ae;
	}

	public void setAe(ArticleEntity ae) {
		this.ae = ae;
	}

	public int getCount() {
		return count;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public void setCount(int count) {

	}

	// ///////////////////////////////////////////////////////////////////
	// 以下为采集NPC内部数据
	// 是否被采集的标记
	public boolean hasBeenPickUp = false;

	public boolean 上个箱子是否成功开启 = true;

	/**
	 * 采集成功
	 */
	public void callback(Player p) {
		// if(article!=null && article instanceof RefreshMonsterPackage){
		// RefreshMonsterPackage pa = (RefreshMonsterPackage)article;
		// NPC_ACTION_RES res = new NPC_ACTION_RES(GameMessageFactory.nextSequnceNum(), this.getId(), LivingObject.STATE_STAND, LivingObject.UP);
		// p.addMessageToRightBag(res);
		// endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 300;
		// PetDao pd = PetDaoManager.getInstance().getPetDao(p, p.getCurrentGame().gi.name);
		// if (pd != null) {
		// if(pd.getMc().getKeyovernum()>0){
		// pd.getMc().setKeyovernum(pd.getMc().getKeyovernum()-1);
		// }
		// }
		// return;
		// }
		
		
		if (ae == null) {
			Game.logger.warn("[采集NPC物品为空] [NPC:" + this.getName() + "]");
			return;
		}
		if (article != null && DisasterConstant.BOX_ARTICLE_CNNNAME.equals(article.getName_stat())) {
			NPCManager nm = MemoryNPCManager.getNPCManager();
			this.setAlive(false);
			nm.removeNPC(this);
			ae = null;
//			DisasterManager.getInst().pickExpAe(p);
			return ;
		}
		if(article != null && WolfManager.getInstance().boxName.equals(article.getName_stat())){
			if(p.getCurrentGame() != null && p.getCurrentGame().gi != null && p.getCurrentGame().gi.name != null){
				if(p.getCurrentGame().gi.name.equals(WolfManager.getInstance().mapName)){
					WolfGame wg = WolfManager.getInstance().getWolfGame(p);
					if(wg != null){
						if(wg.openBox(p)){
							if (article instanceof RandomPackageProps) {
								((RandomPackageProps) article).use(p.getCurrentGame(), p, ae);
								NPCManager nm = MemoryNPCManager.getNPCManager();
								this.setAlive(false);
								nm.removeNPC(this);
								ae = null;
							}
						}else{
							p.sendError(Translate.不能拾取);
						}
					}
				}
			}
			return;
		}
		Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if (a instanceof RandomPackageProps) {
			((RandomPackageProps) a).use(p.getCurrentGame(), p, ae);
			this.setAlive(false);
			NPCManager nm = MemoryNPCManager.getNPCManager();
			nm.removeNPC(this);
			ae = null;
		}

	}
	
	public static long tempTime = 1100;

	/**
	 * 采集，此方法会通知客户端采集的结果
	 * 
	 * @param p
	 */
	public void pickUp(Player p) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		CompoundReturn cr = isPickupAvailable(p);
		if (!cr.getBooleanValue()) {
			switch (cr.getIntValue()) {
			case 1:
				p.send_HINT_REQ(Translate.物品不存在);
				break;
			case 2:
				p.send_HINT_REQ(Translate.距离物品太远);
				break;
			case 3:
				p.send_HINT_REQ(Translate.此物品不属于你);
				break;
			case 4:
				p.send_HINT_REQ(Translate.掉落时间太久);
				break;

			default:
				break;
			}

		} else {
			// 刷怪道具
			try {
				if (article != null && article instanceof RefreshMonsterPackage) {
					PetDao pd = PetDaoManager.getInstance().getPetDao(p, p.getCurrentGame().gi.name);
					if (pd != null) {
						if (pd.getMc().getKeyovernum() <= 0) {
							p.sendError(PetDaoManager.道具不足提示[PetDaoManager.等级索引(pd.getTypelevel())]);
							return;
						}

						if (pd.openingnum < pd.getMc().getKeyovernum() && pd.openingnum == 0) {
							NPC_ACTION_RES res = new NPC_ACTION_RES(GameMessageFactory.nextSequnceNum(), this.getId(), LivingObject.STATE_STAND, LivingObject.UP);
							p.addMessageToRightBag(res);
							endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 150;
							pd.openingnum++;
						} else {
							p.sendError(Translate.操作过快);
							PetDaoManager.log.warn("[宠物迷城] [采集宝箱] [操作过快] [openingnum:" + pd.openingnum + "] [Keyovernum:" + pd.getMc().getKeyovernum() + "] [账号：" + p.getUsername() + "] [角色名：" + p.getName() + "]");
						}
						return;
					}
					// PickBoxProgressBar pt = new PickBoxProgressBar();
					// pt.setNpc(this);
					// pt.setPlayer(p);
					// p.getTimerTaskAgent().createTimerTask(new PickBoxProgressBar(p,this), 1000l, TimerTask.type_采集);
					// p.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), 1000, ""));
					// return;
				}
			} catch (Exception e) {
				PetDaoManager.log.warn("[宠物迷城] [采集宝箱] [异常] [" + p.getLogString() + "] [" + e + "]");
				return;
			}
			
			if(grassType > -1){
				try{
					if(!playersList.contains(p.getId())){
						p.sendError(Translate.不能操作);
						return;
					}
					if(p.getCurrentGame() != null && p.getCurrentGame().gi != null && p.getCurrentGame().gi.name != null){
						if(p.getCurrentGame().gi.name.equals(WolfManager.getInstance().mapName)){
							WolfGame wg = WolfManager.getInstance().getWolfGame(p);
							long grassExps = WolfManager.getInstance().expDatas.get(grassType).get(p.getCurrSoul().getGrade());
							if(wg != null && wg.eatGrass(p,grassExps)){
								p.addExp(grassExps, ExperienceManager.采集经验草);
								NPCManager nm = MemoryNPCManager.getNPCManager();
								this.setAlive(false);
								this.ae = null;
								this.hasBeenPickUp = true;
								grassType= -1;
								nm.removeNPC(this);
								List<Player> list = new ArrayList<Player>();
								list.add(p);
								WolfManager.getInstance().piaoLiZi(wg.game.getPlayers(), WolfManager.eatGrassLiZhi,"吃草",p);
								WolfManager.logger.warn("[小羊快跑] [采集经验草] [成功] [经验:"+grassExps+"] [当前元神等级:"+p.getCurrSoul().getGrade()+"] [grassType:"+grassType+"] [playersList:"+playersList+"] ["+p.getLogString()+"]");
							}else{
								p.sendError(Translate.不能操作);
								WolfManager.logger.warn("[小羊快跑] [采集经验草] [失败] [grassType:"+grassType+"] ["+p.getLogString()+"]");
							}
						}else{
							WolfManager.logger.warn("[小羊快跑] [采集经验草] [失败1] [grassType:"+grassType+"] ["+p.getLogString()+"]");
						}
					}else{
						WolfManager.logger.warn("[小羊快跑] [采集经验草] [失败2] [grassType:"+grassType+"] ["+p.getLogString()+"]");
					}
				}catch(Exception e){
					e.printStackTrace();
					WolfManager.logger.warn("[小羊快跑] [采集经验草] [异常] [grassType:"+grassType+"] ["+p.getLogString()+"]",e);
				}
				return;
			}

			if(message != null){
				p.addMessageToRightBag(message);
				long oldExps = p.getExp();
				p.addExp(DiceManager.winExps, ExperienceManager.骰子仙居胜利者);
				List<ArticleEntity> aes = new ArrayList<ArticleEntity>();
				for(int i=1;i<DiceManager.getInstance().winArticles.length;i++){
					String name = DiceManager.getInstance().winArticles[i];
					Article article = ArticleManager.getInstance().getArticleByCNname(name);
					if(article == null){
						DiceManager.logger.warn("[骰子仙居] [胜利者点箱子发放奖励] [获取奖励数据出错] [物品不存在:"+name+"] ["+p.getLogString()+"]");
						continue;
					}
					try {
						int colorType = article.getColorType();
						if(name.equals("古董")){
							colorType = 3;
						}
						ArticleEntity nae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.骰子活动物品, p, colorType, 1, true);//createTempEntity(article, true, ArticleEntityManager.骰子活动临时物品, null, colorType);
						if(nae != null){
							aes.add(nae);
						}
					} catch (Exception e) {
						e.printStackTrace();
						DiceManager.logger.warn("[骰子仙居] [胜利者点箱子发放奖励] [创建临时物品出错] [物品不存在:"+name+"] ["+p.getLogString()+"]");
					}
				}
				if(aes.size() > 0){
					if (p.putAll(aes.toArray(new ArticleEntity[]{}), "胜利者点箱子发放奖励")) {
					} else {
						try {
							MailManager.getInstance().sendMail(p.getId(), aes.toArray(new ArticleEntity[]{}),  Translate.骰子奖励通过邮件发送, Translate.骰子奖励通过邮件发送, 0, 0, 0, "胜利者点箱子发放奖励");
							p.sendError(Translate.骰子奖励通过邮件发送);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if(DiceManager.logger.isWarnEnabled()){
					DiceManager.logger.warn("[骰子仙居] [采集npc发送协议] [message:"+message.getTypeDescription()+"] [经验变化:"+oldExps+"-->"+p.getExp()+"] ["+p.getLogString()+"]");
				}
				NPCManager nm = MemoryNPCManager.getNPCManager();
				nm.removeNPC(this);
				DiceManager.getInstance().rewardIds.add(p.getId());
				if(DiceManager.getInstance().getDiceGame() != null){
					DiceManager.getInstance().getDiceGame().clearGameInfo("拾取完毕");
				}
				return;
			}
			
			//

			if (ae != null) {
				boolean picked = false;
				//
				try {
					Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
					
					if (a != null && a instanceof RandomPackageProps) {
						boolean flag = false;
						for(String str : DevilSquareConstant.boxarticlename) {
							if(str.equals(a.getName_stat())) {
								flag = true;
								break;
							}
						}
						if(!flag) {
							PickBoxProgressBar pt = new PickBoxProgressBar();
							pt.setNpc(this);
							pt.setPlayer(p);
							p.getTimerTaskAgent().createTimerTask(new PickBoxProgressBar(p, this), tempTime, TimerTask.type_采集);
							p.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), 2000, Translate.开启宝物));
							return;
						}
						callback(p);
						return;
					}
				} catch (Exception e) {

				}
				if(article != null && article.getName_stat().equals(WolfManager.getInstance().boxName)){
					try{
						if(!playersList.contains(p.getId())){
							p.sendError(Translate.不能拾取);
							return;
						}
						PickBoxProgressBar pt = new PickBoxProgressBar();
						pt.setNpc(this);
						pt.setPlayer(p);
						p.getTimerTaskAgent().createTimerTask(new PickBoxProgressBar(p, this), 900, TimerTask.type_神奇大宝箱);
						p.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), 3000, Translate.开启宝物));
					}catch(Exception e){
						e.printStackTrace();
						WolfManager.logger.warn("[小羊快跑] [采集经验草] [异常1] [grassType:"+grassType+"] ["+p.getLogString()+"]",e);
					}
					return;
				}
				
				if (article != null && DisasterConstant.BOX_ARTICLE_CNNNAME.equals(article.getName_stat())) {
//					if (DisasterManager.getInst().isPlayerDead(p)) {
//						return ;
//					}
					
					PickBoxProgressBar pt = new PickBoxProgressBar();
					pt.setNpc(this);
					pt.setPlayer(p);
					p.getTimerTaskAgent().createTimerTask(new PickBoxProgressBar(p, this), 900, TimerTask.type_空岛大冒险);
					p.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), 1000, Translate.开启宝物));
					return ;
				}

				//
				if (!picked) {
					if (count > 1) {
						if (p.putToKnapsacks(ae, count, "拾取")) {
	
							// 统计
							if (this.isMonsterFlop) {
								ArticleStatManager.addToArticleStat(p, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, count, "怪物掉落拾取", null);
							}
	
							picked = true;
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							boolean ok = aem.putToRealSaveCache(ae);
							if (!ok) {
								Game.logger.error("[拾取物品没有成功放入保存队列] [{}] [{}] [{}]", new Object[] { p.getLogString(), ae.getArticleName(), ae.getId() });
							}
						}
					} else {
						if (p.putToKnapsacks(ae, "拾取")) {
	
							// 统计
							if (this.isMonsterFlop) {
								ArticleStatManager.addToArticleStat(p, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "怪物掉落拾取", null);
							}
	
							picked = true;
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							boolean ok = aem.putToRealSaveCache(ae);
							if (!ok) {
								Game.logger.error("[拾取物品没有成功放入保存队列] [{}] [{}] [{}]", new Object[] { p.getLogString(), ae.getArticleName(), ae.getId() });
							}
						}
					}
				}
				if (picked) {
					hasBeenPickUp = true;
					this.setAlive(false);
					NPCManager nm = MemoryNPCManager.getNPCManager();
					nm.removeNPC(this);
					endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					String description = Translate.拾取物品;
					try {
						description = Translate.translateString(Translate.拾取物品提示, new String[][] { new String[] { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
					} catch (Exception ex) {

					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
					p.addMessageToRightBag(hreq);
					ae = null;
				} else {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.背包空间不足);
					p.addMessageToRightBag(hreq);
				}
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.物品不存在);
				p.addMessageToRightBag(hreq);
			}
		}
	}
	
	public int availiableDistance = 860;//360;

	/**
	 * 判断某个玩家当前对此采集NPC是否可以采集
	 * @param p
	 * @return
	 *         返回booleanValue == true 可以拾取 false 不可以拾取<BR/>
	 *         false时,intvalue取值<BR/>
	 *         1:物品已经被拾取了<BR/>
	 *         2.距离太远<BR/>
	 *         3.物品不属于你<BR/>
	 *         4.掉落超时<BR/>
	 * 
	 */
	public CompoundReturn isPickupAvailable(Player p) {
		if (hasBeenPickUp == true) {
			if (CoreSubSystem.logger.isDebugEnabled()) {
				CoreSubSystem.logger.debug(p.getLogString() + "[拾取物品:{}][颜色:{}][物品已经被采集]", new Object[] { this.name, this.nameColor });
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		if (Math.sqrt((p.getX() - x) * (p.getX() - x) + (p.getY() - y) * (p.getY() - y)) > availiableDistance) {
			if (CoreSubSystem.logger.isDebugEnabled()) {
				CoreSubSystem.logger.debug(p.getLogString() + "[拾取物品:{}][颜色:{}][距离太远]", new Object[] { this.name, this.nameColor });
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		if (p == owner) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(0);
		}
		if (flopType == 1) {

			if (CoreSubSystem.logger.isDebugEnabled()) {
				CoreSubSystem.logger.debug(p.getLogString() + "[拾取物品:{}][颜色:{}][物品不属于你,拾取类型:{}]", new Object[] { this.name, this.nameColor, flopType });
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		} else {

			if (playersList != null) {
				for (long playerid : playersList) {
					if (playerid == p.getId()) {
						return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(0);
					}
				}
			}
			if (team != null && team.getMember(p.getId()) != null) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(0);
			}
			if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() >= startTime + allCanPickAfterTime) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(0);
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
	}

	// 通知地图上人闪光
	private List<Player> noticePlayerList = new ArrayList<Player>();

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
		if (this.isAlive() == false) return;

		// 删除不在这个npc可视范围内 已经发送过闪光协议的player
		LivingObject[] visiable = game.getVisbleLivings(this, false);

		if (noticePlayerList.size() > 0) {
			Iterator<Player> it = noticePlayerList.iterator();
			while (it.hasNext()) {
				boolean bln = false;
				Player p = it.next();
				for (LivingObject lo : visiable) {
					if (lo instanceof Player) {
						if (lo.getId() == p.getId()) {
							bln = true;
							break;
						}
					}
				}
				if (!bln) {
					it.remove();
				}
			}
		}

		if (this.owner == null) {
			Player allPlayer = null;
			// 被玩家打死的这个情况看见的人都可以捡 如果playerList不null 只有这些人能拣
			for (LivingObject lo : visiable) {
				if (lo instanceof Player) {
					allPlayer = (Player) lo;
					if (playersList != null) {
						if (!playersList.contains(lo.getId())) {
							continue;
						}
					}
					if (!noticePlayerList.contains(allPlayer)) {
						noticePlayerList.add(allPlayer);
						NOTICE_NPC_FLASH_RES res = new NOTICE_NPC_FLASH_RES(GameMessageFactory.nextSequnceNum(), this.getId());
						allPlayer.addMessageToRightBag(res);
						if (TeamSubSystem.logger.isInfoEnabled()) {
							TeamSubSystem.logger.info("[玩家被玩家杀死给看见的人发送闪光协议] [" + allPlayer.getLogString() + "] []");
						}
					}
				}
			}

		} else {

			if (this.getFlopType() == 1) {
				// 有队伍 顺序拾取
				Player owner = this.getOwner();
				if (owner != null) {
					if (!noticePlayerList.contains(owner)) {
						// 在可视范围
						for (LivingObject lo : visiable) {
							if (lo.getId() == owner.getId()) {
								noticePlayerList.add(owner);
								NOTICE_NPC_FLASH_RES res = new NOTICE_NPC_FLASH_RES(GameMessageFactory.nextSequnceNum(), this.getId());
								owner.addMessageToRightBag(res);
								if (TeamSubSystem.logger.isInfoEnabled()) TeamSubSystem.logger.info("[顺序拾取发送闪光协议] [" + owner.getLogString() + "]");
							}
						}

					}
				}
			} else {
				// 有队伍自由拾取 个人
				Team team = this.getTeam();
				if (team != null) {
					// 有队伍
					List<Player> members = team.getMembers();
					for (Player member : members) {
						boolean bln = false;
						for (LivingObject lo : visiable) {
							if (lo instanceof Player) {
								if (lo.getId() == member.getId()) {
									bln = true;
									break;
								}
							}
						}
						if (bln) {
							if (!noticePlayerList.contains(member)) {

								// 在可视范围
								for (LivingObject lo : visiable) {
									if (lo.getId() == member.getId()) {
										noticePlayerList.add(member);
										NOTICE_NPC_FLASH_RES res = new NOTICE_NPC_FLASH_RES(GameMessageFactory.nextSequnceNum(), this.getId());
										member.addMessageToRightBag(res);
										if (TeamSubSystem.logger.isInfoEnabled()) TeamSubSystem.logger.info("[有队伍发送闪光协议] [" + member.getLogString() + "]");
									}
								}

							}
						}

					}
				} else {
					// 没有队伍
					Player owner = this.getOwner();
					if (owner != null) {
						if (!noticePlayerList.contains(owner)) {
							// 在可视范围
							for (LivingObject lo : visiable) {
								if (lo.getId() == owner.getId()) {
									noticePlayerList.add(owner);
									NOTICE_NPC_FLASH_RES res = new NOTICE_NPC_FLASH_RES(GameMessageFactory.nextSequnceNum(), this.getId());
									owner.addMessageToRightBag(res);
									if (TeamSubSystem.logger.isInfoEnabled()) TeamSubSystem.logger.info("[没有队伍发送闪光协议] [" + owner.getLogString() + "]");
								}
							}
						}
					}
				}
			}
		}

		if (heartBeatStartTime >= endTime) {
			if (this.isAlive()) {
				this.setAlive(false);
				// 特殊装备操作
				if (this.getAe() != null) {
					String articleName = this.ae.getArticleName();
					Equipment[] ets = ArticleManager.getInstance().allSpecialEquipments1;
					for (Equipment e : ets) {
						if (e.getName().equals(articleName)) {
							((EquipmentEntity) ae).destroyEntity(null);
						}
					}
				}
				if (ae != null) {
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					aem.removeTransientATable(ae);
				}
				NPCManager nm = MemoryNPCManager.getNPCManager();
				nm.removeNPC(this);
			}
		}

		if (heartBeatStartTime >= startTime + allCanPickAfterTime) {
			setFlopType((byte) 0);
			// 都可以看见闪光可以拾取
			for (LivingObject lo : visiable) {
				if (lo instanceof Player) {
					Player p1 = (Player) lo;
					if (!noticePlayerList.contains(p1)) {
						noticePlayerList.add(p1);
						NOTICE_NPC_FLASH_RES res = new NOTICE_NPC_FLASH_RES(GameMessageFactory.nextSequnceNum(), this.getId());
						p1.addMessageToRightBag(res);
						if (TeamSubSystem.logger.isInfoEnabled()) TeamSubSystem.logger.info("[发送闪光协议] [" + p1.getLogString() + "]");
					}
				}
			}

		}
	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		FlopCaijiNpc p = new FlopCaijiNpc();
		p.cloneAllInitNumericalProperty(this);
		p.startTime = startTime;
		p.endTime = endTime;
		p.owner = owner;
		p.team = team;
		p.playersList = new ArrayList<Long>();
		p.playersList.addAll(playersList);
		p.flopType = flopType;
		p.allCanPickAfterTime = allCanPickAfterTime;
		p.country = country;

		p.setnPCCategoryId(this.getnPCCategoryId());

		p.windowId = windowId;

		return p;
	}

	public int getGrassType() {
		return grassType;
	}

	public void setGrassType(int grassType) {
		this.grassType = grassType;
	}

}
