package com.fy.engineserver.unite;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.translateString;
import static com.fy.engineserver.datasource.language.Translate.xx结义者不属于同一国家;
import static com.fy.engineserver.datasource.language.Translate.xx结义者有其他结义;
import static com.fy.engineserver.datasource.language.Translate.xx结义者等级不够;
import static com.fy.engineserver.datasource.language.Translate.xx结义者距离太远;
import static com.fy.engineserver.datasource.language.Translate.xx结义解散了;
import static com.fy.engineserver.datasource.language.Translate.xx退出了结义;
import static com.fy.engineserver.datasource.language.Translate.结义解散了;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.society.unite.Option_Add_Unite_Confirm;
import com.fy.engineserver.menu.society.unite.Option_Captain_Agree;
import com.fy.engineserver.menu.society.unite.Option_Unite_Add_Agree;
import com.fy.engineserver.menu.society.unite.Option_Unite_Add_Disagree;
import com.fy.engineserver.menu.society.unite.Option_Unite_Apply_Agree;
import com.fy.engineserver.menu.society.unite.Option_Unite_Apply_Disagree;
import com.fy.engineserver.menu.society.unite.Option_Unite_zhaoji;
import com.fy.engineserver.message.CLOSE_WINDOWS_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.UNITE_AGREEORNO_RES;
import com.fy.engineserver.message.UNITE_APPLY_RES;
import com.fy.engineserver.message.UNITE_CONFIRM_REQ;
import com.fy.engineserver.message.UNITE_DISMISS_RES;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.Utils;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class UniteManager {

	private UnitedServerManager unitedManager;
	
//	public static Logger logger = Logger.getLogger(UniteManager.class.getName());
	public	static Logger logger = LoggerFactory.getLogger(UniteManager.class);
	public static SimpleEntityManager<Unite> em;
	
	
	public static String 结义称号类型 = Translate.结义称号类型;
	
	private static int CLOSE_UNITE_WINDOW = -999;
	private static String UNITE_NPC_NAME = Translate.结义管理员npc;
	private static long MAXDISTANCE = 1000;
	private static NPC npc = null;
	private static int UNITE_LV = 45;
	private static UniteManager self = null;
	
	public static int UNITE_MEMBER_NUM = 20;
	public static int UNITE_ZHAOJI_NUM = 10;
	public static int TITLENAME_NUM = 18;
	public static int MEMBERNAME_NUM = 18;
	
	public static int 结义消耗 = 500*1000;
	public static String 结义消耗两 = "500";
	public static int 加入结义消耗 = 50*1000;
	public static String 加入结义消耗两 = "50";
	public static UniteManager getInstance(){
		return self;
	}
	public String tagforbid[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", " ", "　", "　" };
	
	private String limitMapNames[] = {"binglingjidao","fengguimingdi","fengyintiaozhan","huohuangfenjing","leidouhuanyu","lingxiaotiancheng","wanhuaxiangu","xiandibaoku","xiandimijing"};
	
	/**
	 * 判断是否含有禁用的标签字符
	 * 
	 * @param name
	 * @return
	 */
	public boolean tagValid(String name) {
		String aname = name.toLowerCase();
		for (String s : tagforbid) {
			if (aname.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}
	
	public void init(){
		
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Unite.class);
		if (UnitServerFunctionManager.isActvity(UnitServerFunctionManager.ACTIVITY_JIEYI_MONEY)) {
			结义消耗 = (int)(结义消耗 * UnitServerFunctionManager.JIEYI_ZHEKOU);
			结义消耗两 = ""+(结义消耗/1000);
			加入结义消耗 = (int)(加入结义消耗 * UnitServerFunctionManager.JIEYI_ZHEKOU);
			加入结义消耗两 = "" + (加入结义消耗/1000);
		}
		logger.warn("[初始化结义manager] [成功] [{}ms]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		ServiceStartRecord.startLog(this);
	}
	
	public void destroy(){
		em.destroy();
	}
	
	
	public LruMapCache mCache = new LruMapCache(1024*1024, 60*60*1000);
	
	/**
	 * 结义申请前的条件判断
	 * ""成功,没有队伍,不是队长,距离不够，有其他结义，等级不满足条件 , 不是一个阵营
	 * @param p
	 * @return
	 */
	public String uniteBeforeValidate(Player p){
		String result = "";
		Team team = p.getTeam();
		if(team == null){
			result = Translate.text_结义必须要在组队中;
			return result;
		}
		if(team.getCaptain() != p){
			result = Translate.text_结义要队长申请;
			return result;
		}
		
		for(Player p1 : team.getMembers()){
			
			if(npc == null){
				Game game = p.getCurrentGame();
				LivingObject[] los = game.getLivingObjects();
				for(LivingObject lo : los){
					if(lo instanceof NPC){
						if(((NPC) lo).getName().equals(UNITE_NPC_NAME)){
							npc = ((NPC) lo);
							break;
						}
					}
				}
			}
			if(npc != null){
				if(Utils.getDistanceA2B(p1.getX(),p1.getY(), npc.getX(), npc.getY()) > MAXDISTANCE){
//					result =p1.getName()+ Translate.text_结义者距离太远;
					result =translateString(xx结义者距离太远, new String[][]{{STRING_1,p1.getName()}});
					break;
				}
			}else{
				UniteManager.logger.warn("[结义申请验证] ["+p.getLogString()+"] [结义npc为null] [npcName:"+UNITE_NPC_NAME+"]");
			}
			
			if(p1.getLevel() < UNITE_LV){
				result = translateString(xx结义者等级不够, new String[][]{{STRING_1,p1.getName()}});
//				result = p1.getName()+ Translate.text_结义者等级不够;
				return result;
			}
			if(p.getCountry() != p1.getCountry()){
				result = translateString(xx结义者不属于同一国家, new String[][]{{STRING_1,p1.getName()}});
//				result = p1.getName()+ Translate.text_结义者不属于同一国家;
				return result;
			}
			Relation relation = SocialManager.getInstance().getRelationById(p1.getId());
			if(relation.getUniteId() != -1){
				result = translateString(xx结义者有其他结义, new String[][]{{STRING_1,p1.getName()}});
//				result = p1.getName()+ Translate.text_结义者有其他结义;
				return result;
			}
		}
		return result;
	}
	
	/**
	 * 加入结义
	 * @param player
	 * @return
	 */
	public String addToUniteCheck(Player player){
		SocialManager socialManager = SocialManager.getInstance();
		String result ="";
		Team team = player.getTeam();
		long uniteId = -1;
		if(team != null){
			List<Player> list = team.getMembers();
			for(Player p1 :list){
				Relation relation = socialManager.getRelationById(p1.getId());
				long id = relation.getUniteId();
				if(id == -1)
					continue;
				else{
					if(uniteId == id || uniteId == -1){
						uniteId = id;
					}else{
						result = Translate.text_队伍中有两个结义;
						return result;
					}
				}
			}
			Unite u = this.getUnite(uniteId);
			if(u != null){
				if(u.getMemberIds().contains(player.getId())){
					result = Translate.text_你已经有结义;
					return result;
				}
				
				PlayerManager pm = PlayerManager.getInstance();
				List<Long> members = u.getMemberIds();
				List<Long> copyList = null;
				if(members.size() >= UNITE_MEMBER_NUM ){
					result = Translate.text_结义玩家人数已达最大;
					return  result;
				}
				for(long id : members){
					Player p = null;
					try {
						p = PlayerManager.getInstance().getPlayer(id);
						if(!p.isOnline()){
							result = Translate.text_结义玩家有不在线;
							return result;
						}
					} catch (Exception e) {
						if(copyList == null)
							copyList = new ArrayList<Long>(members);
						copyList.remove(id);
					}
				}
				if(copyList != null){
					u.setMemberIds(copyList);
					members = copyList;
				}
				
				List<Boolean> stateList = new ArrayList<Boolean>();
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				mw.setTitle(Translate.text_加入结义);
				String st = Translate.translateString(Translate.text_xx想加入结义, new String[][]{{Translate.PLAYER_NAME_1,player.getName()}});
				mw.setDescriptionInUUB(st);
				mw.setNpcId(-999);
				
				Option_Unite_Add_Agree agree = new Option_Unite_Add_Agree();
				agree.setText(Translate.text_62);
				agree.setColor(0xffffff);
				agree.setP1(player);
				agree.setStateList(stateList);
				
				Option_Unite_Add_Disagree disagree = new Option_Unite_Add_Disagree();
				disagree.setP1(player);
				disagree.setText(Translate.text_364);
				disagree.setColor(0xffffff);
				mw.setOptions(new Option[]{agree,disagree});
				
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.send_HINT_REQ(Translate.text_等待其他结义玩家同意);
				
				for(long id :members){
					Player p = null;
					try {
						p = pm.getPlayer(id);
						if(p.isOnline()){
							p.addMessageToRightBag(res);
						}
					} catch (Exception e) {
						logger.error("[加入结义异常] ["+player.getLogString()+"]",e);
					}
				}
				
				if(logger.isInfoEnabled())
					logger.info("[发送加入结义申请成功] ["+player.getLogString()+"]");
			}else{
				result = Translate.text_加入结义队伍中没有结义;
			}

		}else{
			result = Translate.text_加入结义你没有队伍;
		}
		
		return result;
	}
	
	/**
	 * 队长结义申请
	 * @param p
	 */
	public void uniteApply(Player p) throws Exception{
		
		String result = this.uniteBeforeValidate(p);
		List<Boolean> stateList = new ArrayList<Boolean>();
		if(result.equals("")){
			
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setTitle(Translate.text_结义确定);
			mw.setDescriptionInUUB(Translate.text_结义申请提示);
			mw.setNpcId(CLOSE_UNITE_WINDOW);
			
			Option_Unite_Apply_Agree agree = new Option_Unite_Apply_Agree();
			agree.setText(Translate.text_62);
			agree.setColor(0xffffff);
			agree.setStateList(stateList);
			agree.setNum(p.getTeamMembers().length -1);
			
			Option_Unite_Apply_Disagree cancle = new Option_Unite_Apply_Disagree();
			cancle.setText(Translate.text_364);
			cancle.setColor(0xffffff);
			
			mw.setOptions(new Option[] {agree ,cancle});
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			Player[] ps = p.getTeamMembers();
			long[] ids = new long[ps.length];
			
			if(ps != null){
				int i = 0;
				for(Player member :ps){
					ids[i] = member.getId();
					if(member != p){
						member.addMessageToRightBag(res);
					}
					i++;
				}

				//返回队长
				UNITE_APPLY_RES res1 = new UNITE_APPLY_RES(GameMessageFactory.nextSequnceNum(),ids);
				p.addMessageToRightBag(res1);
			
				
				List<Boolean> list = new ArrayList<Boolean>();
				list.add(true);
				p.getTeam().setUniteStateList(list);
				
				if(logger.isWarnEnabled())
					logger.warn("[队长发送结义申请成功] ["+p.getLogString()+"]");
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[队长结义申请失败] ["+p.getLogString()+"] [队伍为空]");
				}
			}
		}else{
			p.send_HINT_REQ(result);
			if(logger.isWarnEnabled()){
				logger.warn("[队长结义申请失败] ["+p.getLogString()+"] ["+result+"]");
			}
		}

	} 
	
	/**
	 * 成员同意结义
	 * @param player
	 */
	public void member_agree_unite(Player player,List<Boolean> stateList,int num){
		
		Team team = player.getTeam();
		if(team != null){
			Player p = team.getCaptain();
			UNITE_AGREEORNO_RES res = new UNITE_AGREEORNO_RES(GameMessageFactory.nextSequnceNum(), player.getId(), true);
			p.addMessageToRightBag(res);
			player.send_HINT_REQ(Translate.text_你同意了结义);
			stateList.add(true);
			
			team.uniteStateList.add(true);
			if(stateList.size() >= num){
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_队长正在为咱们起个响亮的名头请稍后);
				for(Player p1 : team.getMembers()){
					if(p1 != p){
						p1.addMessageToRightBag(hint);
					}
				}
			}
			if(logger.isInfoEnabled()){
				logger.info("[成员同意结义] ["+player.getLogString()+"]");
			}
		}else{
			if(logger.isWarnEnabled())
				logger.warn("[成员同意结义错误] ["+player.getLogString()+"] [队伍为空]");
		}
	}
	/**
	 * 结义成员不同意
	 * @param player
	 */
	public void member_disAgree(Player player){
		
		Team team = player.getTeam();
		if(team != null){
			Player p = team.getCaptain();
			if(p != player){
				UNITE_AGREEORNO_RES res = new UNITE_AGREEORNO_RES(GameMessageFactory.nextSequnceNum(), player.getId(), false);
				p.addMessageToRightBag(res);
			}
			
			CLOSE_WINDOWS_RES res = new CLOSE_WINDOWS_RES(GameMessageFactory.nextSequnceNum(), CLOSE_UNITE_WINDOW);
			String descript = Translate.translateString(Translate.text_xx结义还未考虑清楚结义失败, new String[][]{{Translate.STRING_1,player.getName()}});
			
			for(Player p1 : team.getMembers()){
				
				if(p1 != player){
					p1.send_HINT_REQ(descript);
					p1.addMessageToRightBag(res);
				}
			}
			player.send_HINT_REQ(Translate.text_你拒绝了结义);
			
			if(logger.isWarnEnabled()){
				logger.info("[成员拒绝结义成功] ["+player.getLogString()+"]");
			}
		}else{
			if(logger.isWarnEnabled()){
				logger.info("[成员拒绝结义错误] ["+player.getLogString()+"] [没有队伍]");
			}
		}
	}
	
	
	/**
	 * 成员同意对方加入结义
	 * @param player
	 * @param p1
	 * @param stateList
	 */
	public void member_agree_add_unite(Player player,Player p1,List<Boolean> stateList){
		long uniteId = SocialManager.getInstance().getRelationById(player.getId()).getUniteId();
		Unite u = this.getUnite(uniteId);
		if(u != null){
			stateList.add(true);
			player.send_HINT_REQ(Translate.translateString(Translate.text_你同意xx加入结义, new String[][]{{Translate.STRING_1,p1.getName()}}));
			p1.send_HINT_REQ(Translate.translateString(Translate.text_xx同意你加入结义, new String[][]{{Translate.STRING_1,player.getName()}}));
			
			if(logger.isWarnEnabled())
				logger.warn("[成员同意加入结义] ["+player.getLogString()+"]");
			
			if(stateList.size() >= u.getMemberIds().size()){
				
				// 弹出消费提示
				
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				mw.setTitle(Translate.text_确定加入结义);
				String st = Translate.translateString(Translate.text_确定加入结义消耗提示, new String[][]{{Translate.COUNT_1,加入结义消耗两}});
				mw.setDescriptionInUUB(st);
				
				Option_Add_Unite_Confirm agree = new Option_Add_Unite_Confirm();
				agree.setText(Translate.text_62);
				agree.setUnite(u);
				
				Option_Cancel disagree = new Option_Cancel();
				disagree.setText(Translate.text_364);
				
				mw.setOptions(new Option[]{agree,disagree});
				
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				p1.addMessageToRightBag(res);
				
			}
		}
	}
	
	/**
	 * 成员不同意对方加入结义
	 * @param player
	 * @param p1
	 * @param stateList
	 */
	public void member_disAgree_add_unite(Player player,Player p1){
		
		CLOSE_WINDOWS_RES res = new CLOSE_WINDOWS_RES(GameMessageFactory.nextSequnceNum(), CLOSE_UNITE_WINDOW);
		Relation re = SocialManager.getInstance().getRelationById(player.getId());
		if(re.getUniteId() != -1){
			Unite u = this.getUnite(re.getUniteId());
			if(u != null){
				
				String des = Translate.translateString(Translate.text_xx拒绝了xx加入结义, new String[][]{{Translate.STRING_1,player.getName()},{Translate.STRING_2,p1.getName()}});
				for(long pid: u.getMemberIds()){
					if(pid != player.getId()){
						try {
							Player members = PlayerManager.getInstance().getPlayer(pid);
							members.addMessageToRightBag(res);
							members.send_HINT_REQ(des);
							logger.warn("[成员不同意加入结义] ["+player.getLogString()+"]");
						} catch (Exception e) {
							logger.error("[成员不同意加入结义异常] ["+player.getLogString()+"]",e);
						}
					}
				}
			}
		}
		
		player.send_HINT_REQ(Translate.translateString(Translate.text_你拒绝了xx加入结义, new String[][]{{Translate.STRING_1,p1.getName()}}));
		p1.send_HINT_REQ(Translate.translateString(Translate.text_xx拒绝了你加入结义, new String[][]{{Translate.STRING_1,player.getName()}}));
		
	}
	

	/**
	 * 队长确认结义 弹出消费
	 * @param player
	 * @param req
	 * @return
	 */
	public String unite_apply_confirm_first(Player player,UNITE_CONFIRM_REQ req){
		
		long[] memberIds = req.getMemberIds();
		
		Team team = player.getTeam();
		if(team == null){
			return Translate.text_结义必须要在组队中;
		}
		
		List<Player> playerList = team.getMembers();
		
		if(memberIds.length != playerList.size()){
			return Translate.text_结义队伍发生变化;
		}
		
		if(team.uniteStateList != null){
			if(team.uniteStateList.size() != memberIds.length){
				return Translate.text_有人还未同意结义;
			}
		}
		
		for(long memberId : memberIds){
			boolean temp = false;
			for(Player p1 : playerList){
				if(p1.getId() == memberId){
					temp = true;
					break;
				}
			}
			if(!temp){
				return Translate.text_结义队伍发生变化;
			}
		}
		for(Player p1 : playerList){
			
			if(npc != null){
				if(Utils.getDistanceA2B(p1.getX(),p1.getY(), npc.getX(), npc.getY()) > MAXDISTANCE){
//					return p1.getName()+ Translate.text_结义者距离太远;
					return translateString(xx结义者距离太远, new String[][]{{STRING_1,p1.getName()}});
				}
			}else{
				UniteManager.logger.warn("[队长确认结义] [结义npc为null] ["+player.getLogString()+"] [npcName:"+UNITE_NPC_NAME+"]");
			}
			
			if(p1.getLevel() < UNITE_LV){
				return Translate.text_结义者等级不够;
			}
			
			if(player.getCountry() != p1.getCountry()){
				return Translate.text_结义者不属于同一国家;
			}
			
			Relation relation = SocialManager.getInstance().getRelationById(p1.getId());
			if(relation.getUniteId() != -1){
				return Translate.text_结义者有其他结义;
			}
		}
		
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setTitle(Translate.text_队长确定结义);
		String st = Translate.translateString(Translate.text_队长确定结义消耗提示, new String[][]{{Translate.COUNT_1,结义消耗两}});
		mw.setDescriptionInUUB(st);
		
		Option_Captain_Agree agree = new Option_Captain_Agree();
		agree.setText(Translate.text_62);
		agree.setReq(req);
		
		Option_Cancel disagree = new Option_Cancel();
		disagree.setText(Translate.text_364);
		
		mw.setOptions(new Option[]{agree,disagree});
		
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
		
		if(logger.isWarnEnabled()){
			logger.warn("[队长确认结义 弹出消费] ["+player.getLogString()+"]");
		}
		
		return "";
	}
	
	
	
	/**
	 * 队长确认结义
	 * @param player
	 * @param u
	 */
	public synchronized String unite_apply_confirm(Player player,UNITE_CONFIRM_REQ req){
		
		long[] memberIds = req.getMemberIds();
		
		Team team = player.getTeam();
		if(team == null){
			return Translate.text_结义必须要在组队中;
		}
		
		List<Player> playerList = team.getMembers();
		
		if(memberIds.length != playerList.size()){
			return Translate.text_结义队伍发生变化;
		}
		
		if(team.uniteStateList != null){
			if(team.uniteStateList.size() != memberIds.length){
				return Translate.text_有人还未同意结义;
			}
		}
		
		for(long memberId : memberIds){
			boolean temp = false;
			for(Player p1 : playerList){
				if(p1.getId() == memberId){
					temp = true;
					break;
				}
			}
			if(!temp){
				return Translate.text_结义队伍发生变化;
			}

		}
		
		for(Player p1 : playerList){
			
			if(npc != null){
				if(Utils.getDistanceA2B(p1.getX(),p1.getY(), npc.getX(), npc.getY()) > MAXDISTANCE){
//					return p1.getName()+ Translate.text_结义者距离太远;
					return translateString(xx结义者距离太远, new String[][]{{STRING_1,p1.getName()}});
				}
			}else{
				UniteManager.logger.warn("[队长确认结义] [结义npc为null] [npcName:"+UNITE_NPC_NAME+"]");
			}
			
			if(p1.getLevel() < UNITE_LV){
				return Translate.text_结义者等级不够;
			}
			
			if(player.getCountry() != p1.getCountry()){
				return Translate.text_结义者不属于同一国家;
			}
			
			Relation relation = SocialManager.getInstance().getRelationById(p1.getId());
			if(relation.getUniteId() != -1){
				return Translate.text_结义者有其他结义;
			}
		}
		long uniteId = 0l;
		try {
			uniteId = em.nextId();
		} catch (Exception e1) {
			UniteManager.logger.error("[生成结义id异常] [队长确定] ["+player.getLogString()+"]",e1);
			return Translate.系统错误;
		}
		Unite u = new Unite(uniteId);
		u.setUniteName(req.getTitle());
		u.setMemberName(req.getName());
		u.setFrontOrBack(req.getPosition());
		Player[] ps = player.getTeamMembers();
		List<Long> list = new ArrayList<Long>();
		for(Player p1 : ps){
			list.add(p1.getId());
		}
		u.setMemberIds(list);

		
		BillingCenter bc = BillingCenter.getInstance();
		//合服打折活动
		CompoundReturn cr = ActivityManagers.getInstance().getValue(2, player);
		if(cr!=null && cr.getBooleanValue()){
			结义消耗 = cr.getIntValue();
		}
		//
		if(player.getSilver() + player.getShopSilver() >= 结义消耗){
			try {
				bc.playerExpense(player, 结义消耗, CurrencyType.SHOPYINZI, ExpenseReasonType.JIEYI_COST, "结义消耗");
			} catch (Exception e) {
				logger.error("[结义消耗] ["+player.getLogString()+"]",e);
				return null;
			}
		}else{
			return Translate.translateString(Translate.text_结义消耗银子不足,new String[][]{{Translate.COUNT_1,结义消耗两}});
		}
		SocialManager sm = SocialManager.getInstance();
		for(long id :u.getMemberIds()){
			Relation r = sm.getRelationById(id);
			if(r != null){
				r.setUniteId(u.getUniteId());
			}
		}
		
		for(Player member : playerList){
			for(Player member1 : playerList){
				if(member != member1){
					sm.addFriend(member, member1);
				}
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[结义互相添加好友] ["+member.getLogString()+"] []");
			}
		}
		
		u.calculateName();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		Article zhaoji = am.getArticle(Translate.兄弟令);
		if(zhaoji != null){
			//获得10个召集令
			for(Player member : playerList){
				ArticleEntity[] aes = new ArticleEntity[UNITE_ZHAOJI_NUM];
				
				for(int i= 0;i<UNITE_ZHAOJI_NUM;i++){
					try{
						ArticleEntity ae = aem.createEntity(zhaoji, true, ArticleEntityManager.CREATE_REASON_结义, player, 0, 1, true);
						aes[i] = ae;
					}catch(Exception ex){
						logger.error("[结义完成送兄弟令] ["+player.getLogString()+"]",ex);
					}
				}
				if(member.putAll(aes,"结义")){
					UniteManager.logger.warn("[结义完成获得兄弟令成功] [放入玩家包裹] ["+member.getLogString()+"] []");
				}else{
					String title = Translate.系统邮件提示;
					String content = Translate.由于您的背包已满您得到的部分物品通过邮件发送;
					try {
						MailManager.getInstance().sendMail(member.getId(), aes, new int[]{UNITE_ZHAOJI_NUM}, title, content, 0, 0, 0, "结义");
						UniteManager.logger.warn("[结义完成获得兄弟令成功] [没有放入玩家包裹发送邮件] ["+member.getLogString()+"] []");
					} catch (Exception e) {
						UniteManager.logger.error("[结义完成获得兄弟令失败] [没有放入玩家包裹发送邮件失败] ["+member.getLogString()+"] []",e);
					}
				}
			}
		}else{
			UniteManager.logger.error("[结义完成获得兄弟令错误] [没有召集令道具] [uniteId"+u.getUniteId()+"] []");
		}
		
		saveUnite(u);
		//发送广播
		uniteSendMessage(u, player);
		int num = playerList.size();
		for(Player member : playerList){
			AchievementManager.getInstance().record(member, RecordAction.结义次数);
			AchievementManager.getInstance().record(member, RecordAction.结义人数,num);
			if(AchievementManager.logger.isWarnEnabled()){
				AchievementManager.logger.warn("[成就统计结义次数] ["+member.getLogString()+"]");
				AchievementManager.logger.warn("[成就统计结义人数] ["+member.getLogString()+"] ["+num+"]");
			}
		}
		
		if(logger.isWarnEnabled()){
			logger.warn("[结义创建成功] [队长:"+player.getLogString()+"] [结义名称:"+u.getUniteName()+"] [玩家称号:"+u.getMemberName()+"] []");
		}
		return "";
	}

	
	public String uniteNameCheck(String title,String memberName){
		
		String result = "";
		if(title.equals("")){
			result = Translate.text_结义名不能为空;
		}else if(title.getBytes().length >= TITLENAME_NUM){
			result = Translate.text_结义名太长;
		}
		if(memberName.equals("")){
			result = Translate.text_个人称号不能为空;
		}else if(memberName.getBytes().length >= MEMBERNAME_NUM){
			result = Translate.text_结义个人称号太长;
		}
		
		int t1 = this.uniteTitleCheck(title.trim());
		if(t1 == 0){
			int t2 = this.uniteNameCheck(memberName);
			if(t2 == 0){
			}else if(t2 == 1){
				result = Translate.text_结义个人称号有禁用字;
			}else if(t2 ==2){
				result = Translate.text_结义个人称号重复;
			}
		}else if(t1 == 1){
			result = Translate.text_结义名称有禁用字;
		}else if(t1 == 2){
			result = Translate.text_结义名称重复;
		}
		return result;
	}
	
	/**
	 * 结义称号申请
	 * @param p  1禁用字  2 重复  0
	 * @param value
	 * @return
	 */
	private int uniteTitleCheck(String value){
		
		Unite u1 = null;
		if(this.tagValid(value)){
			
			try{
				Set set = mCache.keySet();
				Iterator it = set.iterator();
				while(it.hasNext()){
					long id = (Long)it.next();
					Unite u = (Unite)mCache.get(id);
					if(u.getUniteName().equals(value)){
						u1 = u;
						break;
					}
				}
			}catch(Exception e){
				logger.error("[验证结义称号异常]",e);
			}
			
			if(u1 == null){
				try {
					List<Unite> temp = em.query(Unite.class, "uniteName = ?",new Object[]{value.trim()}, null, 1, 10);
					if(temp != null && temp.size() > 0){
						u1 = temp.get(0);
					}
				} catch (Exception e) {
					UniteManager.logger.error("[结义称号验证异常]",e);
				}
			}
			if( u1 != null){
				return 2;
			}
			return 0;
		}else{
			return 1;
		}
	}
	
	/**
	 * 用户结义名称申请
	 * @param p 1禁用字 2 重复 0
	 * @param value
	 * @return
	 */
	private int uniteNameCheck(String value){
		
		Unite u1 = null;
		if(this.tagValid(value)){
			try{
				Set set = mCache.keySet();
				Iterator it = set.iterator();
				while(it.hasNext()){
					long id = (Long)it.next();
					Unite u = (Unite)mCache.get(id);
					if(u.getMemberName().equals(value)){
						u1 = u;
						break;
					}
				}
			}catch(Exception e){
				logger.error("[验证结义玩家称号异]",e);
			}
			if(u1 == null){
				try {
					List<Unite> temp = em.query(Unite.class, "memberName = ?",new Object[]{value.trim()}, null, 1, 10);
					if(temp != null && temp.size() > 0){
						u1 = temp.get(0);
					}
				} catch (Exception e) {
					UniteManager.logger.error("[验证结义玩家称号异常]",e);
				}
			}
			if( u1 != null){
				return 2;
			}
			return 0;
		}else{
			return 1;
		}
	}
	
	/**
	 * 成员离开结义
	 * @param player
	 */
	public synchronized void exit_unite(Player player){
		
		Relation relation = SocialManager.getInstance().getRelationById(player.getId());
		long uniteId = relation.getUniteId();
		Unite u = UniteManager.getInstance().getUnite(uniteId);
		PlayerManager pm = PlayerManager.getInstance();
		if( u!= null){
			List<Long> list = u.getMemberIds();
			List<Long> copyList = null;
			for(long id : list){
				try {
					PlayerManager.getInstance().getPlayer(id);
				} catch (Exception e) {
					logger.error("[退出结义某玩家不存在] [id:"+id+"]",e);
					if(copyList == null)
						copyList = new ArrayList<Long>(list);
					copyList.remove(id);
				}
			}
			if(copyList != null){
				u.setMemberIds(copyList);
				list = copyList;
			}
			
			int key = PlayerTitlesManager.getInstance().getKeyByType(结义称号类型);
			if(key < 0){
				logger.error("[成员离开修改称号错误] [没有指定称号] ["+player.getLogString()+"] ["+key+"]");
				return ;
			}
			
			if(list.size() > 2 ){
				u.getMemberIds().remove(player.getId());
				u.setMemberIds(u.getMemberIds());
				relation.setUniteId(-1);
				
				//结义称号修改
				
				player.removePersonTitle(key);
				String hint = Translate.translateString(Translate.text_你离开了xx结义, new String[][]{{Translate.STRING_1,u.getUniteName()}});
				player.send_HINT_REQ(hint);
				for(long id :u.getMemberIds()){
					try {
						Player p = pm.getPlayer(id);
						if(p != null && p.isOnline()){
							hint = Translate.translateString(Translate.text_xx离开了结义, new String[][] {{Translate.STRING_1,p.getName()}});
							p.send_HINT_REQ(hint);
						}else{
							//发邮件
							try {
//								MailManager.getInstance().sendMail(id, null, Translate.text_成员退出结义邮件, player.getName()+退出了结义, 0, 0, 0, "");
								MailManager.getInstance().sendMail(id, null, Translate.text_成员退出结义邮件, translateString(xx退出了结义, new String[][]{{Translate.STRING_1,player.getName()}}), 0, 0, 0, "");
								logger.error("[退出结义发送邮件] ["+player.getLogString()+"]");
							} catch (Exception e) {
								logger.error("[退出结义发送邮件异常] ["+player.getLogString()+"]",e);
							}
						}
					} catch (Exception e) {
						logger.error("[成员离开结义异常] ["+player.getLogString()+"]",e);
					}
				}
				u.calculateName();
				
				if(logger.isWarnEnabled()){
					logger.warn("[成员离开结义成功] ["+player.getLogString()+"] ["+u.getLogString()+"]");
				}
			}else{
				// 解散
				UNITE_DISMISS_RES res = new UNITE_DISMISS_RES(GameMessageFactory.nextSequnceNum());
				for(long id : list){
					Relation r1 = SocialManager.getInstance().getRelationById(id);
					if(r1 != null){
						r1.setUniteId(-1);
					}
					try {
						Player p1 = pm.getPlayer(id);
						p1.removePersonTitle(key);
						if(p1.isOnline()){
							p1.send_HINT_REQ(结义解散了);
							p1.addMessageToRightBag(res);
						}else{
							//发邮件
							try {
								
//								MailManager.getInstance().sendMail(id, null, Translate.text_结义解散邮件, u.getUniteName()+结义解散了, 0, 0, 0, "");
								MailManager.getInstance().sendMail(id, null, Translate.text_结义解散邮件, translateString(xx结义解散了, new String[][]{{STRING_1,u.getUniteName()}}), 0, 0, 0, "");
								logger.warn("[解散结义发送邮件] ["+player.getLogString()+"]");
								
							} catch (Exception e) {
								logger.error("[解散结义发送邮件异常] ["+player.getLogString()+"]",e);
							}
						}
					} catch (Exception e) {
						logger.error("[结义解散异常] ["+player.getLogString()+"]",e);
					}
				}
				mCache.remove(u.getUniteId());
				try {
					em.remove(u);
					if(logger.isWarnEnabled()){
						logger.warn("[结义解散成功] ["+player.getLogString()+"]");
					}
				} catch (Exception e) {
					logger.error("[结义解散异常] ["+player.getLogString()+"]",e);
				}
			}
		}
	}
	
	public boolean uniteCall(Player player,String articleName){
		
		Relation relation = SocialManager.getInstance().getRelationById(player.getId());
		if(relation.getUniteId() > 0){
			Unite u = UniteManager.getInstance().getUnite(relation.getUniteId());
			
			if(u != null){
				boolean isXianjie = false;
				try {
					String mapname = player.getCurrentGame().gi.name;
					isXianjie = RobberyConstant.没飞升玩家不可进入的地图.contains(mapname);
				} catch (Exception e) {
					
				}
				
				List<Long> list = u.getMemberIds();
				boolean bln = true;
				DevilSquareManager inst = DevilSquareManager.instance;
				for(long id :list){
					try {
						if(id == player.getId()){
							continue;
						}
						if(inst != null && inst.isPlayerInDevilSquare(id)) {		//恶魔广场不弹框
							continue;
						}
						
						if(GlobalTool.verifyTransByOther(id,player.getCurrentGame()) != null) {
							continue;
						}
						if(GamePlayerManager.getInstance().isOnline(id)){
							bln = false;
						}
					} catch (Exception e) {
						logger.error("[使用召集令] ["+player.getLogString()+"]",e);
					}
				}
				if(bln){
					player.sendError(Translate.text_你结义中的人都不在线);
					return false;
				}
				
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				mw.setTitle(articleName);
				mw.setDescriptionInUUB(Translate.text_使用了兄弟令);
				
				Option_Unite_zhaoji option = new Option_Unite_zhaoji();
				option.setText(Translate.召唤);
				option.setArticleName(articleName);
				option.setInvite(player);
				
				Option_UseCancel cancel = new Option_UseCancel();
				cancel.setText(Translate.取消);
				mw.setOptions(new Option[] { option, cancel });
				
				INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, articleName, Translate.召唤, Translate.取消, new byte[0]);
				player.addMessageToRightBag(res);
				return false;
			}else{
				logger.error("[使用兄弟令] ["+player.getLogString()+"] [结义实体为null]");
			}
		}else{
			player.send_HINT_REQ(Translate.text_你还没有结义);
			logger.error("[使用兄弟令] ["+player.getLogString()+"] [你还没有结义]");
		}
		return false;
		
	}
	
	
	public void uniteSendMessage(Unite unite,Player player){
		
		StringBuffer sb = new StringBuffer();
		PlayerManager pm = PlayerManager.getInstance();
		List<Long> list = unite.getMemberIds();
		Player member = null;
		int i =0;
		int max = list.size()-1;
		for(long playerId : list){
			try {
				member = pm.getPlayer(playerId);
				if(i == 0){
					sb.append(member.getName());
				}else if(i == max){
					sb.append("和"+member.getName());
				}else {
					sb.append("，"+member.getName());
				}
				++i;
			} catch (Exception e) {
				logger.error("[结义完成发送系统消息异常] ["+player.getLogString()+"]",e);
			}
		}
		//发送广播世界
		ChatMessageService cms = ChatMessageService.getInstance();
		ChatMessage msg = new ChatMessage();
//		<玩家姓名>、<玩家姓名>和<玩家姓名>义结金兰，人送外号<结义称号>！！（国家）
		String des = Translate.translateString(Translate.text_xxs义结金兰人送外号, new String[][]{{Translate.STRING_1,sb.toString()},{Translate.STRING_2,unite.getUniteName()}});
		msg.setMessageText(des);
		CountryManager cm = CountryManager.getInstance();
		Country country = cm.getCountryMap().get(player.getCountry());
		try {
			cms.sendMessageToCountry(country.getCountryId(), msg);
			if (UniteManager.logger.isWarnEnabled()){
				UniteManager.logger.warn("[结义完成发送世界广播] ["+player.getLogString()+"] ["+unite.getLogString()+"]");
			}
		} catch (Exception e) {
			UniteManager.logger.error("[结义完成发送世界广播错误] ["+player.getLogString()+"] ["+unite.getLogString()+"]",e);
		}
	}
	
	/**
	 * 保存unite
	 * @param id
	 * @return
	 */
	public Unite saveUnite(Unite u) {
		mCache.put(u.getUniteId(), u);
		em.notifyNewObject(u);
		return u;
	}
	
	/**
	 * 通过id得到结义
	 * @param id
	 * @return
	 */
	public Unite getUnite(long id) {
		Unite unite = (Unite)mCache.get(id);
		if(unite == null) {
			try {
				unite = em.find(id);
			} catch (Exception e) {
				logger.error("[通过id获得结义异常] [id:"+id+"]",e);
			}
			if(unite != null) {
				mCache.put(unite.getUniteId(), unite);
			}
		}
		if(unite != null){
			if (logger.isDebugEnabled()){
				logger.debug("[通过id获得结义] [{}] [{}]", new Object[]{id,(unite==null?"NULL":unite.getLogString())});
			}
		}else{
			logger.error("[通过id获得结义错误] [没有结义] [id:"+id+"]");
		}
		return unite;
	}

	/**
	 * 召集令类型道具是否可以前往召唤者身边的限制
	 */
	public boolean callLimit(Player p){
		try{
			logger.warn("[判断是否可以被召集] [当前所在地图："+p.getCurrentGame().gi.name+"] [玩家信息："+p.getLogString()+"]");
			if (p.getCurrentGame().gi.name.matches("fudidongtian.*ceng") || PetDaoManager.getInstance().isPetDao(p.getCurrentGame().gi.name) || p.getCurrentGame().gi.name.equals(RobberyConstant.DUJIEMAP) || DevilSquareManager.instance.isPlayerIndevilSquareMap(p)) {
				return false;
			}
			for(String name : limitMapNames){
				if(p.getCurrentGame().gi.name.contains(name)){
					return false;
				}
			}
			if(GlobalTool.verifyTransByOther(p.getId()) != null) {
				return false;
			}
		}catch(Exception e){
			logger.warn("[判断是否可以被召集] [异常] [{}] [{}]",new Object[]{p.getLogString(),e});
			return false;
		}
		return true;
	}

	public void setUnitedManager(UnitedServerManager unitedManager) {
		this.unitedManager = unitedManager;
	}

	public UnitedServerManager getUnitedManager() {
		return unitedManager;
	}
	
}
