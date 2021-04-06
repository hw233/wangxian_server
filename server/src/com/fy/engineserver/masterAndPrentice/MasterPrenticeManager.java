package com.fy.engineserver.masterAndPrentice;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.STRING_2;
import static com.fy.engineserver.datasource.language.Translate.STRING_3;
import static com.fy.engineserver.datasource.language.Translate.STRING_4;
import static com.fy.engineserver.datasource.language.Translate.translateString;
import static com.fy.engineserver.datasource.language.Translate.xx想拜你为师描述;
import static com.fy.engineserver.datasource.language.Translate.xx想收你为徒描述;
import static com.fy.engineserver.datasource.language.Translate.不在线;
import static com.fy.engineserver.datasource.language.Translate.不能向自己请求拜师;
import static com.fy.engineserver.datasource.language.Translate.不能向自己请求收徒;
import static com.fy.engineserver.datasource.language.Translate.你的徒弟xx已经出师;
import static com.fy.engineserver.datasource.language.Translate.保存记录失败;
import static com.fy.engineserver.datasource.language.Translate.叛出了师门;
import static com.fy.engineserver.datasource.language.Translate.把你逐出师门;
import static com.fy.engineserver.datasource.language.Translate.的徒弟;
import static com.fy.engineserver.datasource.language.Translate.系统错误;
import static com.fy.engineserver.datasource.language.Translate.级别达到出师出师成功;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.society.masterPrentice.Option_Cancle_Register_Confirm;
import com.fy.engineserver.menu.society.masterPrentice.Option_Evict_Prentice_Confirm;
import com.fy.engineserver.menu.society.masterPrentice.Option_Rebel_Master_Confirm;
import com.fy.engineserver.menu.society.masterPrentice.Option_Take_Master_Agree;
import com.fy.engineserver.menu.society.masterPrentice.Option_Take_Master_DisAgree;
import com.fy.engineserver.menu.society.masterPrentice.Option_Take_Prentice_Agree;
import com.fy.engineserver.menu.society.masterPrentice.Option_Take_Prentice_DisAgree;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_MASTERPRENTICEINFO_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.REBEL_EVICT_RES;
import com.fy.engineserver.message.TAKE_MASTER_PRNTICE_RES;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class MasterPrenticeManager implements UpdateMasterPrenticeInterface{
	
	
	public static Logger logger = MasterPrenticeSubSystem.logger;
	public static SimpleEntityManager<MasterPrenticeInfo> em;
	
	public static String 师徒称号  = Translate.徒弟;
	
	/**
	 * 师父最低等级
	 */
	public static final int MASTER_LEVLE_LIMIT = 80;

	/**
	 * 徒弟的等级限制  10 - 80
	 */
	public static final int PRENTICE_LEVLE_MIN = 10;
	public static final int PRENTICE_LEVLE_MAX = 80;
	
	public static final long 徒弟不上线逐徒没惩罚时间 = 1000*60*60*24*3;
	public static final long 师傅不上线判师没惩罚时间 = 1000*60*60*24*3;
	
	/**
	 * 徒弟个数
	 */
	public static final int PRENTICE_NUM = 3;

	/**
	 * 判师惩罚 
	 */
	public static final long PENALTY_LEAVEL_MASTER_TIME = 24 * 60 *60 * 1000;

	/**
	 * 逐出师门惩罚
	 */
	public static final long PENALTY_BANISH_PRENTICE_TIME = 24 * 60 *60 * 1000;
	
	/**
	 * 出师登记
	 */
	public static final int FINISH_PRENTICE  = 80;
	
	/**
	 * 登记保存时间 24小时
	 */
	public static final long REGISTER_LAST_TIME = 24 * 60 *60 * 1000;
	
	public static final int PER_REGESTER_NUM = 5;
	
	private SocialManager socialManager;

	public LruMapCache mCache = new LruMapCache(32*1024*1024, 60*60*1000);
	
	public static MasterPrenticeManager self;
	
	public void init(){
		
		self = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(MasterPrenticeInfo.class);
		ServiceStartRecord.startLog(this);
	}
	
	public static MasterPrenticeManager getInstance() {
		return self;
	}
	
	
	public void destroy() {
		
		em.destroy();
	}
	
	public void removeFromCache(MasterPrenticeInfo info){
		if(mCache.get(info.getId()) != null){
			mCache.remove(info);
		}
	}
	
	/**
	 * 收徒判断
	 * @param master
	 * @param prentice
	 * @return
	 */
	public String takePrenticeCheck(Player master ,Player prentice){
		
		if(prentice.getId() == master.getId()){
			return 不能向自己请求收徒;
		}
		if(!prentice.isOnline()){
			return prentice.getName()+不在线;
		}
		synchronized (prentice) {
			if(master.getCountry() != prentice.getCountry()){
				return Translate.text_你跟徒弟不是一个国家;
			}
			if (master.getLevel() < MASTER_LEVLE_LIMIT) {
				return Translate.text_你级别太低不能收徒;
			}
			if(prentice.getLevel()< PRENTICE_LEVLE_MIN ){
				return Translate.text_对方级别太低不能收徒;
			}
			if(prentice.getLevel()>= PRENTICE_LEVLE_MAX){
				return Translate.text_对方级别太高不能收徒;
			}
			
			Relation relationA = socialManager.getRelationById(master.getId());
			Relation relationB = socialManager.getRelationById(prentice.getId());
			MasterPrentice mpA = relationA.getMasterPrentice();
			MasterPrentice mpB = relationB.getMasterPrentice();

			List<Long> tempList = mpA.getPrentices();
//			for(long id: mpA.getPrentices()){
//				try {
//					PlayerManager.getInstance().getPlayer(id);
//				} catch (Exception e) {
//					logger.error("[收徒判断异常] ["+master.getLogString()+"] ["+id+"]",e);
//					if(tempList == null)
//					tempList = new ArrayList<Long>(mpA.getPrentices());
//					tempList.remove(id);
//				}
//			}
//			
			if(tempList == null){
				mpA.setPrentices(new ArrayList<Long>());
			}
			
			if(mpA.getPrentices().size() >= PRENTICE_NUM){
				return Translate.text_你徒弟个数已满;
			}
			if(mpA.getPrentices().contains(prentice.getId())){
				return Translate.text_已经是你的徒弟;
			}
			if(mpB.getMasterId() > 0){
				try {
//					PlayerManager.getInstance().getPlayer(mpB.getMasterId());
					return Translate.text_已经有师傅;
				} catch (Exception e) {
					logger.error("[收徒判断异常] ["+master.getLogString()+"]",e);
					mpB.setMasterId(-1);
				}
			}
			
			if(mpB.isRebel()){
			
			}else if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - mpB.getRebelTime() > PENALTY_LEAVEL_MASTER_TIME ){
				
			}else{
				String description = Translate.translateString(Translate.text_xx判师惩罚不能拜师, new String[][]{{Translate.PLAYER_NAME_1,prentice.getName()}});
				return description;
			}
			
			if(mpA.isEvict()){
				
			}else if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - mpA.getEvictTime() > PENALTY_BANISH_PRENTICE_TIME ){
				
			}else{
				String description = Translate.translateString(Translate.text_xx逐徒惩罚不能收徒, new String[][]{{Translate.PLAYER_NAME_1,master.getName()}});
				return description;
			}
			
		return "";
		}
	}
	
	/**
	 * 收徒
	 * @param master
	 * @param prentice
	 * @param reqOrRes
	 * @return
	 */
	public String takePrentice(Player master ,Player prentice){
		
		String result = this.takePrenticeCheck(master, prentice);
		if(result.equals("")){
			
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setTitle(Translate.text_收徒申请);
//			String description = master.getName()+"想收你为徒，"+"职业："+CareerManager.careerNames[master.getMainCareer()]+"，" +
//					"等级："+master.getLevel()+"，境界："+PlayerManager.classlevel[master.getClassLevel()];
			String description = translateString(xx想收你为徒描述, new String[][]{{STRING_1,master.getName()},{STRING_2,CareerManager.careerNames[master.getMainCareer()]},{STRING_3,master.getLevelDes(master.getLevel())+""},{STRING_4,PlayerManager.classlevel[master.getClassLevel()]}});
			
			mw.setDescriptionInUUB(description);

			Option_Take_Prentice_Agree agree = new Option_Take_Prentice_Agree();
			agree.setText(Translate.text_62);
			agree.setColor(0xffffff);
			agree.setMaster(master);

			Option_Take_Prentice_DisAgree disAgree = new Option_Take_Prentice_DisAgree();
			disAgree.setText(Translate.text_364);
			disAgree.setColor(0xffffff);
			disAgree.setMaster(master);

			mw.setOptions(new Option[] {agree ,disAgree});
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			prentice.addMessageToRightBag(res);
			if(logger.isInfoEnabled())
				logger.info("[收徒申请成功发送] ["+master.getLogString()+"]");
			
		}else{
			if(logger.isInfoEnabled()){
				logger.info("[收徒申请失败] ["+master.getLogString()+"] ["+result+"]");
			}
		}
		return result;
	}
	
	/**
	 * 师傅向徒弟发送后，徒弟确定拜师
	 * @param prentice
	 * @param master
	 * @return
	 */
	public synchronized String takeMasterConfirm(Player prentice ,Player master){

		String result = this.takeMasterCheck(prentice, master);
		if(result.equals("")){
			Relation relationA = socialManager.getRelationById(master.getId());
			Relation relationB = socialManager.getRelationById(prentice.getId());
//			MasterPrentice mpA = relationA.getMasterPrentice();
//			MasterPrentice mpB = relationB.getMasterPrentice();
			
			relationB.takeMaster(master.getId());
			socialManager.addFriend(prentice, master);
//			relationB.setDirty(true);
			relationA.takePrentice(prentice.getId());
			socialManager.addFriend(master, prentice);
//			relationA.setDirty(true);
			
			TAKE_MASTER_PRNTICE_RES resA = new TAKE_MASTER_PRNTICE_RES(GameMessageFactory.nextSequnceNum(), false, prentice.getId(), prentice.getName(), prentice.getMainCareer(), prentice.getLevel());
			TAKE_MASTER_PRNTICE_RES resB = new TAKE_MASTER_PRNTICE_RES(GameMessageFactory.nextSequnceNum(), false, master.getId(), master.getName(), master.getMainCareer(), master.getLevel());
			master.addMessageToRightBag(resA);
			prentice.addMessageToRightBag(resB);
			
			//徒弟确定拜师成功   更新登记记录
			addMaster(master,prentice);
			addPrentice(master,prentice);
			
			AchievementManager.getInstance().record(master, RecordAction.收徒次数);
			if(AchievementManager.logger.isWarnEnabled()){
				AchievementManager.logger.warn("[成就统计收徒次数] ["+master.getLogString()+"]");
			}
			if(logger.isWarnEnabled()){
				logger.warn("[徒弟确定拜师成功] [师傅:"+master.getLogString()+"] [徒弟:"+prentice.getLogString()+"]");
			}
			return "";
		}
		if(logger.isWarnEnabled()){
			logger.warn("[徒弟确定拜师失败] ["+prentice.getLogString()+"] ["+result+"] ");
		}
		return result;
	
	}
	
	/**
	 * 徒弟向师傅发送后，师傅确定收徒
	 * @param prentice
	 * @param master
	 * @return
	 */
	public synchronized String takePrenticeConfirm(Player master ,Player prentice){
		
			String result = this.takePrenticeCheck(master, prentice);
			if(result.equals("")){
				Relation relationA = socialManager.getRelationById(master.getId());
				Relation relationB = socialManager.getRelationById(prentice.getId());
	//			MasterPrentice mpA = relationA.getMasterPrentice();
	//			MasterPrentice mpB = relationB.getMasterPrentice();
				
				relationB.takeMaster(master.getId());
				socialManager.addFriend(prentice, master);
	//			relationB.setDirty(true);
				relationA.takePrentice(prentice.getId());
				socialManager.addFriend(master, prentice);
	//			relationA.setDirty(true);
				
				
	//			mpA.takePrentice(prentice.getId());
	//			relationA.setDirty(true);
	//			mpB.takeMaster(master.getId());
	//			relationB.setDirty(true);
				TAKE_MASTER_PRNTICE_RES resA = new TAKE_MASTER_PRNTICE_RES(GameMessageFactory.nextSequnceNum(), false, prentice.getId(), prentice.getName(), prentice.getMainCareer(), prentice.getLevel());
				TAKE_MASTER_PRNTICE_RES resB = new TAKE_MASTER_PRNTICE_RES(GameMessageFactory.nextSequnceNum(), false, master.getId(), master.getName(), master.getMainCareer(), master.getLevel());
				master.addMessageToRightBag(resA);
				prentice.addMessageToRightBag(resB);
				
				//师傅确定收徒成功   更新登记记录
				addMaster(master,prentice);
				addPrentice(master,prentice);
				
				AchievementManager.getInstance().record(prentice, RecordAction.拜师次数);
				if(AchievementManager.logger.isWarnEnabled()){
					AchievementManager.logger.warn("[成就统计拜师次数] ["+prentice.getLogString()+"]");
				}
				if(logger.isWarnEnabled()){
					logger.warn("[师傅确定收徒成功] [师傅:"+master.getLogString()+"] [徒弟:"+prentice.getLogString()+"]");
				}
				return "";
			}
			if(logger.isWarnEnabled()){
				logger.warn("[师傅确定收徒失败] ["+prentice.getLogString()+"] ["+result+"] ");
			}
			return result;
	}
	
	/**
	 * 请求拜师判断（徒弟）
	 * @param prentice
	 * @param master
	 * @return
	 */
	public String takeMasterCheck(Player prentice ,Player master){

		if(prentice.getId() == master.getId()){
			return 不能向自己请求拜师;
		}
		if(!master.isOnline()){
			return master.getName()+不在线;
		}
		synchronized (master) {
			if(master.getCountry() != prentice.getCountry()){
				return Translate.text_你跟师傅不是一个国家;
			}
			if(prentice.getLevel()< PRENTICE_LEVLE_MIN){
				return Translate.text_你级别太低不能拜师;
			}
			if(prentice.getLevel()>= PRENTICE_LEVLE_MAX){
				return Translate.text_你级别太高不能拜师;
			}
			if (master.getLevel() < MASTER_LEVLE_LIMIT) {
				return Translate.text_对方级别太低你不能拜师;
			}
			
			Relation relationA = socialManager.getRelationById(master.getId());
			Relation relationB = socialManager.getRelationById(prentice.getId());
			MasterPrentice mpA = relationA.getMasterPrentice();
			MasterPrentice mpB = relationB.getMasterPrentice();
			
			List<Long> tempList = null;
			for(long id: mpA.getPrentices()){
				try {
					PlayerManager.getInstance().getPlayer(id);
				} catch (Exception e) {
					logger.error("[请求拜师判断异常] ["+prentice.getLogString()+"]",e);
					if(tempList == null)
					tempList = new ArrayList<Long>(mpA.getPrentices());
					tempList.remove(id);
				}
			}
			
			if(tempList != null)mpA.setPrentices(tempList);
			
			if(mpA.getPrentices().size() >= PRENTICE_NUM){
				
				return Translate.text_师傅的徒弟个数已满;
			}
			if(mpA.getPrentices().contains(prentice.getId())){
				return Translate.text_你已经是他的徒弟;
			}
			if(mpB.getMasterId() != -1){
				try {
					PlayerManager.getInstance().getPlayer(mpB.getMasterId());
					return Translate.text_已经有师傅;
				} catch (Exception e) {
					logger.error("[请求拜师判断异常] ["+prentice.getLogString()+"]",e);
					mpB.setMasterId(-1);
				}
			}
			
			if(mpB.isRebel()){
				// 可以拜
			}else if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - mpB.getRebelTime() > PENALTY_LEAVEL_MASTER_TIME ){
				// 可以拜
			}else{
				String description = Translate.translateString(Translate.text_xx判师惩罚不能拜师, new String[][]{{Translate.PLAYER_NAME_1,prentice.getName()}});
				return description;
			}
			
			if(mpA.isEvict()){
				
			}else if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - mpA.getEvictTime() > PENALTY_BANISH_PRENTICE_TIME ){
				
			}else{
				String description = Translate.translateString(Translate.text_xx逐徒惩罚不能收徒, new String[][]{{Translate.PLAYER_NAME_1,master.getName()}});
				return description;
			}
		return "";
		}
	
	}
	
	/**
	 * 拜师
	 * @param prentice
	 * @param master
	 * @param reqOrRes
	 * @return
	 */
	public String takeMaster(Player prentice ,Player master){
		String result = takeMasterCheck(prentice, master);
		if(result.equals("")){
			
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setTitle(Translate.text_拜师申请);
//			String description = prentice.getName()+"想拜你为师，职业："+CareerManager.careerNames[prentice.getMainCareer()]+"，" +
//			"等级："+prentice.getLevel()+"，境界："+PlayerManager.classlevel[prentice.getClassLevel()];
			String description = translateString(xx想拜你为师描述, new String[][]{{STRING_1,prentice.getName()},{STRING_2,CareerManager.careerNames[prentice.getMainCareer()]},{STRING_3,prentice.getLevelDes(prentice.getLevel())+""},{STRING_4,PlayerManager.classlevel[prentice.getClassLevel()]}});
			mw.setDescriptionInUUB(description);

			Option_Take_Master_Agree agree = new Option_Take_Master_Agree();
			agree.setText(Translate.text_62);
			agree.setColor(0xffffff);
			agree.setPrentice(prentice);

			Option_Take_Master_DisAgree disAgree = new Option_Take_Master_DisAgree();
			disAgree.setText(Translate.text_364);
			disAgree.setColor(0xffffff);
			disAgree.setPrentice(prentice);
			
			mw.setOptions(new Option[] {agree ,disAgree});
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			master.addMessageToRightBag(res);
			if(logger.isInfoEnabled()){
				logger.info("[拜师申请成功发送] ["+prentice.getLogString()+"]");
			}
			
		}else{
			if(logger.isInfoEnabled()){
				logger.info("[拜师申请成功拜师失败] ["+prentice.getLogString()+"] ["+result+"]" );
			}
		}
		return result;
	}

	/**
	 * 查看拜师，收徒的信息 可能(n+1)
	 * @param type (true 收徒)
	 * @return
	 */
	public void queryMessageAll(Player player,int start,boolean type){
		if(start < 1){
			logger.warn("[查询所有的登记的师徒信息错误] [参数错误] ["+player.getLogString()+"] [查询拜师还是(true)收徒:"+type+"] ["+start+"]");
			return ;
		}
		long now = SystemTime.currentTimeMillis();
		PlayerManager pm = PlayerManager.getInstance();
		
		String typeSt = "F";
		if(type){
			typeSt = "T";
		}
		List<Long> onlines = new ArrayList<Long>();
		List<MasterPrenticeInfo>  list = null;
		try {
			//list = this.em.query(MasterPrenticeInfo.class, "type = "+ "'"+typeSt+"'" +" AND country = "+player.getCountry()+" AND registerEndTime >"+now,"registerEndTime desc",1,200);
			list = this.em.query(MasterPrenticeInfo.class, "type = ?  AND country =? AND registerEndTime > ?", new Object[]{typeSt,player.getCountry(),now},"registerEndTime desc",1,200);
		} catch (Exception e1) {
			MasterPrenticeManager.logger.error("[查询所有的登记的师徒信息异常] ["+player.getLogString()+"] [查询拜师还是(true)收徒:"+type+"] ["+start+"]" ,e1);
		}
		Player[] ps = new Player[0];
		if(list != null && list.size() > 0){
			for(MasterPrenticeInfo info : list){
				ps = PlayerManager.getInstance().getOnlinePlayers();
				for(Player p : ps){
					if(p.getId() == info.getPlayerId()){
						onlines.add(info.getPlayerId());
						break;
					}
				}
			}
		}else{
			logger.warn("[查询所有的登记的师徒信息] [没有记录] ["+player.getLogString()+"] [查询拜师还是(true)收徒:"+type+"] ["+start+"]");
			QUERY_MASTERPRENTICEINFO_RES res = new QUERY_MASTERPRENTICEINFO_RES(GameMessageFactory.nextSequnceNum(), type, start,1, new long[0], new String[0], new short[0] ,new byte[0] ,new int[0], new String[0]);
			player.addMessageToRightBag(res);
			return ;
		}
	
		int begin ;
		int end;
		if(onlines.size() == 0){
			start = 0;
			begin = 0;
			end = 0;
		}else{
			begin = (start-1)*PER_REGESTER_NUM;
			end = start*PER_REGESTER_NUM;
		}
	
		if(begin > onlines.size()) {
			if (logger.isWarnEnabled()) {
				logger.warn("[查询所有的登记的师徒信息错误] [开始页大于总页数] ["+player.getLogString()+"] ");
			}
			return;
		}
		if(end >= onlines.size()) {
			end = onlines.size();
		}
		
		List<Long> temp = onlines.subList(begin, end);
		MasterPrenticeInfo info = null;
		List<Long> allPlayerIds = new ArrayList<Long>();
		List<String> allNames = new ArrayList<String>();
		List<Byte> allCareers = new ArrayList<Byte>();
		List<Integer> allLevels = new ArrayList<Integer>();
		List<Short> allClassLevels = new ArrayList<Short>();
		List<String> allEndTimes = new ArrayList<String>();
		Player player2 = null;
		for(long id:temp){
			long mpId = socialManager.getRelationById(id).getMasterPrenticeInfoId();
			
			info = this.getMasterPrenticeInfoById(mpId);
			if(info != null){
				try {
					player2 = pm.getPlayer(info.getPlayerId());
					
					allPlayerIds.add(info.getPlayerId());
					allNames.add(player2.getName());
					allCareers.add(player2.getMainCareer());
					allLevels.add(player2.getLevel());
					allClassLevels.add(player2.getClassLevel());
				
					
					long endTime = info.getRegisterEndTime();
					long remainTime = endTime - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//					endTimes[i] = String.valueOf((int)remainTime/(60*60*1000));
					allEndTimes.add((int)remainTime/(60*60*1000)+"");
				} catch (Exception e) {
					logger.error("[查看拜师收徒信息异常] ["+player.getLogString()+"]",e);
				}
			
			}
		}
		int num = allPlayerIds.size();
		long[] onlinePlayerIds = new long[allPlayerIds.size()];
		String[] names = new String[allPlayerIds.size()];
		byte[] careers = new byte[allPlayerIds.size()];
		int[] levels = new int[allPlayerIds.size()];
		short[] classlevels = new short[allPlayerIds.size()];
		String[] endTimes = new String[allPlayerIds.size()];

		for(int i= 0;i<num;i++){
			onlinePlayerIds[i] = allPlayerIds.get(i);
			names[i] = allNames.get(i);
			careers[i] = allCareers.get(i);
			levels[i] = allLevels.get(i);
			classlevels[i] = allClassLevels.get(i);
			endTimes[i] = allEndTimes.get(i);
		}
		
		int all = onlines.size();
		int max = 0;
		if(all%PER_REGESTER_NUM == 0){
			max = all/PER_REGESTER_NUM;
		}else{
			max = (all/PER_REGESTER_NUM)+1;
		}
		if(logger.isInfoEnabled()){
			logger.info("[查询所有的登记的师徒信息成功] ["+player.getLogString()+"] [总页数:"+max+"] [本页几个:"+temp.size()+"] [查询拜师还是(true)收徒:"+type+"] ["+start+"]");
		}
		QUERY_MASTERPRENTICEINFO_RES res = new QUERY_MASTERPRENTICEINFO_RES(GameMessageFactory.nextSequnceNum(), type, start,max, onlinePlayerIds, names, classlevels,careers, levels,endTimes);
		player.addMessageToRightBag(res);
		
	}
	
	
	
	
	
	/**
	 * 徒弟判师师傅逐出徒弟条件验证
	 * @param prentice
	 * @param master
	 * @return
	 */
	private String rebelOrEvictCheck(Player prentice ,Player master){
		Relation relationA = socialManager.getRelationById(master.getId());
		Relation relationB = socialManager.getRelationById(prentice.getId());
		MasterPrentice mpa = relationA.getMasterPrentice();
		MasterPrentice mpb = relationB.getMasterPrentice();
		if(mpa == null || mpb == null){
			logger.error("[判师逐徒判断错误] ["+prentice.getLogString()+"] ["+master.getLogString()+"]");
			return "error";
		}
		if(!mpa.getPrentices().contains(prentice.getId())){
			return Translate.text_你还没有这个徒弟;
		}
		if(mpb.getMasterId() != master.getId()){
			return Translate.text_你没有这个师傅;
		}
		return "";
	}
	
	/**
	 * 徒弟判师
	 * @param prentice
	 * @param master
	 * @return
	 */
	public String rebelMaster(Player prentice ,Player master){

		String result = this.rebelOrEvictCheck(prentice, master);
		if(!result.equals("")){
			return result;
		}
		
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setTitle(Translate.text_确定判师);
		
		if(SystemTime.currentTimeMillis() - master.getEnterGameTime() > MasterPrenticeManager.师傅不上线判师没惩罚时间){
			mw.setDescriptionInUUB(Translate.text_判师没有惩罚);
		}else{
			mw.setDescriptionInUUB(Translate.text_判师惩罚);
		}
		
		Option_Rebel_Master_Confirm agree = new Option_Rebel_Master_Confirm();
		agree.setText(Translate.text_62);
		agree.setColor(0xffffff);
		agree.setMaster(master);
		
		Option_Cancel cancle = new Option_Cancel();
		cancle.setText(Translate.text_364);
		cancle.setColor(0xffffff);
		
		mw.setOptions(new Option[] {agree ,cancle});
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		prentice.addMessageToRightBag(res);
//		logger.info("[徒弟发送判师成功] [] ["+prentice.getId()+"] ["+prentice.getName()+"] ["+prentice.getUsername()+"] []");
		if(logger.isInfoEnabled())
			logger.info("[徒弟发送判师成功] ["+prentice.getLogString()+"] ["+master.getLogString()+"]");
		return "";
	}
	
	/**
	 * 徒弟确定判师
	 * @param prentice
	 * @param master
	 * @return
	 */
	public String rebelMasterConfirm(Player prentice ,Player master){
		
		String result = this.rebelOrEvictCheck(prentice, master);
		if(!result.equals("")){
			return result;
		}
		Relation relationA = socialManager.getRelationById(master.getId());
		Relation relationB = socialManager.getRelationById(prentice.getId());
		
		relationB.rebelMaster();
		relationA.autoRemovePrentice(prentice.getId());
		rebel(master,prentice);
		prentice.sendError(Translate.text_判师成功);
		REBEL_EVICT_RES res = new REBEL_EVICT_RES(GameMessageFactory.nextSequnceNum(), master.getId());
		prentice.addMessageToRightBag(res);
		if(master.isOnline()){
			String st = Translate.translateString(Translate.text_xx判师, new String[][]{{Translate.PLAYER_NAME_1,prentice.getName()}});
			master.sendError(st);
			res = new REBEL_EVICT_RES(GameMessageFactory.nextSequnceNum(), prentice.getId());
			master.addMessageToRightBag(res);
			
		}else{
			//发邮件
			try {
				MailManager.getInstance().sendMail(master.getId(), null, Translate.text_徒弟判师邮件, prentice.getName()+叛出了师门, 0, 0, 0, "");
			} catch (Exception e) {
				logger.error("[徒弟背叛师傅发送邮件异常] ["+prentice.getLogString()+"]",e);
			}
		}
		
		AchievementManager.getInstance().record(prentice, RecordAction.叛师次数);
		if(AchievementManager.logger.isWarnEnabled()){
			AchievementManager.logger.warn("[成就统计叛师次数] ["+prentice.getLogString()+"]");
		}
		if(logger.isWarnEnabled()){
			logger.info("[徒弟判师成功] [徒弟:"+prentice.getLogString()+"] [师傅:"+master.getLogString()+"]");
			logger.info("[师傅删除徒弟成功] [徒弟:"+prentice.getLogString()+"] [师傅:"+master.getLogString()+"]");
		}
		return "";
	}
	
	
	/**
	 * 师傅逐出徒弟
	 * @param master
	 * @param prentice
	 * @return
	 */
	public String evictPrentice(Player master ,Player prentice){
		
		String result = this.rebelOrEvictCheck(prentice, master);
		if(!result.equals("")){
			return result;
		}
		
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setTitle(Translate.text_确定逐出徒弟);
		
		if(SystemTime.currentTimeMillis() - prentice.getEnterGameTime() > MasterPrenticeManager.徒弟不上线逐徒没惩罚时间){
			mw.setDescriptionInUUB(Translate.text_逐出徒弟没有惩罚);
		}else{
			mw.setDescriptionInUUB(Translate.text_逐出徒弟惩罚);
		}
		
		Option_Evict_Prentice_Confirm agree = new Option_Evict_Prentice_Confirm();
		agree.setText(Translate.text_62);
		agree.setColor(0xffffff);
		agree.setPrentice(prentice);
		
		Option_Cancel cancle = new Option_Cancel();
		cancle.setText(Translate.text_364);
		cancle.setColor(0xffffff);
		
		mw.setOptions(new Option[] {agree ,cancle});
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		master.addMessageToRightBag(res);
		
		if(logger.isInfoEnabled()){
			logger.info("[师傅发送逐出徒弟成功] ["+master.getLogString()+"] [徒弟:"+prentice.getLogString()+"]");
		}
		return "";
	}
	
	/**
	 * 师傅确定逐出徒弟
	 * @param master
	 * @param prentice
	 * @return
	 */
	public String evictPrenticeConfirm(Player master ,Player prentice){
		
		String result = this.rebelOrEvictCheck(prentice, master);
		if(!result.equals("")){
			return result;
		}
		
		Relation relationA = socialManager.getRelationById(master.getId());
		Relation relationB = socialManager.getRelationById(prentice.getId());
		
		relationA.evictPrentice(prentice.getId());
		
		relationB.autoRemoveMaster();
		
		evict(master,prentice);
		
		master.send_HINT_REQ(Translate.text_逐出徒弟成功);
		REBEL_EVICT_RES res = new REBEL_EVICT_RES(GameMessageFactory.nextSequnceNum(), prentice.getId());
		master.addMessageToRightBag(res);
		if(prentice.isOnline()){
			String st = Translate.translateString(Translate.text_xx把你逐出, new String[][]{{Translate.PLAYER_NAME_1,master.getName()}});
			prentice.sendError(st);
			res = new REBEL_EVICT_RES(GameMessageFactory.nextSequnceNum(), master.getId());
			prentice.addMessageToRightBag(res);
		}else{
			//发邮件
			try {
				MailManager.getInstance().sendMail(prentice.getId(), null, Translate.text_师傅逐徒邮件, master.getName()+把你逐出师门, 0, 0, 0, "");
			} catch (Exception e) {
				logger.error("[师傅逐出徒弟发送邮件异常] ["+master.getLogString()+"]",e);
			}
		}
		
		if(logger.isWarnEnabled()){
			logger.info("[徒弟被逐成功] [徒弟:"+prentice.getLogString()+"] [师傅:"+master.getLogString()+"]");
			logger.info("[师傅删除徒弟成功] [徒弟:"+prentice.getLogString()+"] [师傅:"+master.getLogString()+"]");
		}
		return "";
	}
	
	/**
	 * 发布拜师还是收徒验证
	 * @param player
	 * @param masterOrPrentice(true 收徒)
	 * @return
	 */
	private String registerMasterOrPrenticeCheck(Player player , boolean masterOrPrentice){
		
		List<Long> copyList = null;
		Relation relation = socialManager.getRelationById(player.getId());
		MasterPrentice mp = relation.getMasterPrentice();
		if(masterOrPrentice){
			if(player.getLevel() < MASTER_LEVLE_LIMIT ){
				return Translate.text_发布收徒你不满足级别条件;
			}
			
			for(long id :mp.getPrentices()){
				try {
					PlayerManager.getInstance().getPlayer(id);
				} catch (Exception e) {
					logger.error("[发布拜师还是收徒] ["+player.getLogString()+"]",e);
					if(copyList == null) copyList = new ArrayList<Long>(mp.getPrentices());
					copyList.remove(id);
				}
			}
			if(copyList != null) mp.setPrentices(copyList);
			
			if(mp.getPrentices().size() >= PRENTICE_NUM){
				return Translate.text_师傅的徒弟个数已满;
			}
			
			//惩罚
			if( mp.getEvictTime()+ PENALTY_BANISH_PRENTICE_TIME > com.fy.engineserver.gametime.SystemTime.currentTimeMillis())
				return Translate.text_逐徒惩罚不能发布;
			
		}else{

			if(player.getLevel()< PRENTICE_LEVLE_MIN){
				return Translate.text_你级别太低不能发布拜师;
			}
			if(player.getLevel()>= PRENTICE_LEVLE_MAX){
				return Translate.text_你级别太高不能发布拜师;
			}
			
			
			if(mp.getMasterId() != -1){
				try {
					PlayerManager.getInstance().getPlayer(mp.getMasterId());
					return Translate.text_已经有师傅;
				} catch (Exception e) {
					logger.error("[发布拜师还是收徒] ["+player.getLogString()+"]",e);
				}
			}
			//惩罚
			if( mp.getRebelTime()+ PENALTY_LEAVEL_MASTER_TIME > com.fy.engineserver.gametime.SystemTime.currentTimeMillis())
				return Translate.text_判师惩罚不能发布;
		}
		return "";
	}
	
	
	/**
	 * 发布拜师还是收徒
	 * @param player
	 * @param masterOrPrentice(true收徒 )
	 * @return
	 */
	public String registerMasterOrPrentice(Player player , boolean masterOrPrentice){
		
		String result = this.registerMasterOrPrenticeCheck(player, masterOrPrentice);
		if(result.equals("")){
			Relation relation = socialManager.getRelationById(player.getId());
			MasterPrenticeInfo info = null;
			if(relation.getMasterPrenticeInfoId() > 0){
				info = this.getMasterPrenticeInfoById(relation.getMasterPrenticeInfoId());
				if(info != null){
									
					if(info.isType() == masterOrPrentice){
						return Translate.text_不能重复发送收徒申请;
					}else{
						return Translate.text_不能重复发送拜师申请;
					}
					
				}
			}
			
			info = new MasterPrenticeInfo(player, masterOrPrentice);
			long id;
			try {
				id = MasterPrenticeManager.em.nextId();
				info.setId(id);
			} catch (Exception e) {
				logger.error("[发布拜师还是收徒记录异常] ["+player.getLogString()+"]",e);
				return 系统错误;
			}
			
			MasterPrenticeInfo infos = this.createMasterPrenticeInfo(info);
			if(infos != null){
				relation.setMasterPrenticeInfoId(info.getId());
				if (logger.isInfoEnabled()) {
					logger.info("[注册成功] ["+player.getLogString()+"] ["+masterOrPrentice+"]");
				}
				return "";
			}else{
				if (logger.isWarnEnabled()) {
					logger.info("[注册失败] [保存记录失败] ["+player.getLogString()+"] ["+masterOrPrentice+"]");
				}
				return 保存记录失败;
			}
		}
		return result;
	}
	
	
	
	/**
	 * 取消拜师或是收徒登记
	 * @param player
	 * @param masterOrPrentice
	 * @return
	 */
	public String cancleRegister(Player player ,boolean masterOrPrentice){
		
		Relation relation = socialManager.getRelationById(player.getId());
		if(relation.getMasterPrenticeInfoId() > 0){
			
			MasterPrenticeInfo info = this.getMasterPrenticeInfoById(relation.getMasterPrenticeInfoId());
			if(info != null){
				if(info.isType() == masterOrPrentice){
					// 发送确认取消
					MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
					mw.setTitle(Translate.text_确定取消登记);
					mw.setDescriptionInUUB(Translate.text_确定取消登记);
					
					Option_Cancle_Register_Confirm agree = new Option_Cancle_Register_Confirm();
					agree.setText(Translate.text_62);
					agree.setColor(0xffffff);
					agree.setMasterOrPrentice(masterOrPrentice);
					
					Option_Cancel cancle = new Option_Cancel();
					cancle.setText(Translate.text_364);
					cancle.setColor(0xffffff);
					
					mw.setOptions(new Option[] {agree ,cancle});
					QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					player.addMessageToRightBag(res);
					if(logger.isWarnEnabled()){
						logger.warn("[发送取消拜师或收徒登记成功] ["+player.getLogString()+"]");
					}
					return "";
				}
			}
		}
		if(logger.isWarnEnabled()){
			logger.warn("[你还没发布拜师或是收徒登记] ["+player.getLogString()+"]");
		}
		if(masterOrPrentice){
			return Translate.text_你还没发布收徒登记;
		}else{
			return Translate.text_你还没发布拜师登记;
		}
		
	}
	
	/**
	 * 确认取消拜师或是收徒登记
	 * @param player
	 * @param masterOrPrentice
	 * @return
	 */
	public String cancleRegisterConfirm(Player player ,boolean masterOrPrentice){
		
		Relation relation = socialManager.getRelationById(player.getId());
		long recordId = relation.getMasterPrenticeInfoId();
		if(recordId > 0){
			MasterPrenticeInfo info = this.getMasterPrenticeInfoById(recordId);
			if(info != null){
				if(info.isType() == masterOrPrentice){
					
					try {
						em.remove(info);
						removeFromCache(info);
						relation.setMasterPrenticeInfoId(-1);
					} catch (Exception e) {
						logger.error("[确认取消拜师或是收徒登记异常] ["+player.getLogString()+"]",e);
					}
					player.sendError(Translate.text_取消发布成功);
					if(logger.isWarnEnabled()){
						logger.warn("[取消拜师或收徒登记成功] ["+player.getLogString()+"]");
					}
					return "";
				}
			}
		}
		relation.setMasterPrenticeInfoId(-1);
		if(masterOrPrentice){
			return Translate.text_你还没发布收徒登记;
		}else{
			return Translate.text_你还没发布拜师登记;
		}
	}
	

	/**
	 * 判断俩人是否是师徒关系
	 * @param master
	 * @param prentice
	 * @return
	 */
	public String checkMasterPrentice(Player master,Player prentice){
		Relation ra = socialManager.getRelationById(master.getId());
		Relation rb = socialManager.getRelationById(prentice.getId());
		if(ra.getMasterPrentice().getPrentices().contains(prentice.getId()) && rb.getMasterPrentice().getMasterId() == master.getId())
			return "";
		return Translate.text_不是师徒关系;
	}
	
	/**
	 * 创建一个拜师收徒信息登记实体
	 * @param pet
	 * @return
	 */
	public MasterPrenticeInfo createMasterPrenticeInfo(MasterPrenticeInfo info) {
		
		mCache.put(info.getId(), info);
		try {
			em.save(info);
			if(logger.isInfoEnabled()){
				logger.info("[创建拜师收徒信息登记实体] ["+(info==null?"NULL":info.getLogString())+"]");
			}
			return info;
		} catch (Exception e) {
			logger.error("[创建拜师收徒信息登记实体异常] ["+(info==null?"NULL":info.getLogString())+"]",e);
		}
		return null;
	}
	
	public MasterPrenticeInfo getMasterPrenticeInfoById(long id){
		MasterPrenticeInfo info = (MasterPrenticeInfo) mCache.get(id);
		if(info == null){
			try {
				info = em.find(id);
				if(logger.isDebugEnabled()){
					logger.debug("[查询拜师收徒记录] ["+id+"]");
				}
			} catch (Exception e) {
				logger.error("[查询拜师收徒记录异常] ["+id+"]",e);
			}
		}else{
			return info;
		}
		if(info != null){
			mCache.put(info.getId(), info);
		}
		return info;
	}
	
	public void setSocialManager(SocialManager socialManager) {
		this.socialManager = socialManager;
	}
	
	
	@Override
	public void chushi(Player player) {
		
		if(player.getLevel() >= PRENTICE_LEVLE_MAX){
			PlayerManager pm = PlayerManager.getInstance();
			Relation relation = socialManager.getRelationById(player.getId());
			long mpId = relation.getMasterPrenticeInfoId();
			if( mpId > 0){
				MasterPrenticeInfo info = this.getMasterPrenticeInfoById(relation.getMasterPrenticeInfoId());
				if(info != null){
					try {
						em.remove(info);
						removeFromCache(info);
						if (logger.isWarnEnabled()) {
							logger.warn("[出师删除师徒记录成功] ["+player.getLogString()+"]");
						}
					} catch (Exception e) {
						logger.error("[出师删除师徒记录] ["+player.getLogString()+"]",e);
					}
				}else{
					relation.setMasterPrenticeInfoId(-1);
						if (logger.isDebugEnabled()) logger.debug("[出师删除师徒记录] [师徒记录过期]");
				}
			}
			
			MasterPrentice mp = relation.getMasterPrentice();
			long masterId = mp.getMasterId();
			if(masterId < 0){
				return ;
			}
			mp.setMasterId(-1);
			relation.setMasterPrentice(mp);
			
//			player.sendError("你已经达到"+PRENTICE_LEVLE_MAX+"级，可以出师");
			player.sendError(Translate.translateString(Translate.你已经达到xx级可以出师, new String[][]{{STRING_1,MasterPrenticeManager.PRENTICE_LEVLE_MAX+""}}));
			try {
				Player master = pm.getPlayer(masterId);
				
				removeShiTuChenghao(master, player);
				
				Relation masterRelation = socialManager.getRelationById(master.getId());
				List<Long> prentices = masterRelation.getMasterPrentice().getPrentices();
				if(prentices.contains(player.getId())){
					prentices.remove(player.getId());
					masterRelation.setMasterPrentice(masterRelation.getMasterPrentice());
					if(master.isOnline()){
//						master.sendError("你的徒弟"+player.getName()+"已经出师");
						master.sendError(translateString(你的徒弟xx已经出师, new String[][]{{STRING_1,player.getName()}}));
					}else{
//						发邮件
						try {
							MailManager.getInstance().sendMail(masterId, null, Translate.text_徒弟出师邮件, player.getName()+级别达到出师出师成功, 0, 0, 0, "");
						} catch (Exception e) {
							logger.error("[徒弟完成出师发送邮件异常] ["+player.getLogString()+"]",e);
						}
					}
					AchievementManager.getInstance().record(master, RecordAction.出师次数);
					if(AchievementManager.logger.isWarnEnabled()){
						AchievementManager.logger.warn("[徒弟出师成就统计] ["+master.getLogString()+"] [徒弟:"+player.getLogString()+"]");
					}
					
					if (logger.isWarnEnabled()){
						logger.warn("[出师师傅删除徒弟] [成功] ["+player.getLogString()+"] [师傅:"+master.getLogString()+"]");
					}
				}else{
					logger.error("[出师师傅删除徒弟错误] [没有徒弟] ["+player.getLogString()+"]");
				}

			} catch (Exception e) {
				logger.error("[出师异常] ["+player.getLogString()+"]",e);
			}
		}else{
			logger.error("[出师错误] ["+player.getLogString()+"]");
		}
	}

	@Override
	public void addMaster(Player master,Player prentice) {
		Relation relation = socialManager.getRelationById(prentice.getId());
		PlayerManager pm = PlayerManager.getInstance();
		this.addShiTuChenghao(master, prentice);
		if(relation.getMasterPrenticeInfoId() > 0){
			MasterPrenticeInfo info = this.getMasterPrenticeInfoById(relation.getMasterPrenticeInfoId());
			if(info != null){
				try {
					em.remove(info);
					removeFromCache(info);
				} catch (Exception e) {
					logger.error("[拜师成功删除师徒登记记录] ["+prentice.getLogString()+"]",e);
				}
			}
			relation.setMasterPrenticeInfoId(-1);
			
			if (logger.isWarnEnabled()){
				logger.debug("[拜师成功删除师徒登记记录成功] ["+prentice.getLogString()+"]");
			}
		}
	}

	@Override
	public void addPrentice(Player master,Player prentice) {
		
		Relation relation = socialManager.getRelationById(master.getId());
		
		MasterPrentice mp = relation.getMasterPrentice();
		if(mp.getPrentices() != null && mp.getPrentices().size() >= PRENTICE_NUM){
			
			if(relation.getMasterPrenticeInfoId() > 0){
				MasterPrenticeInfo info = this.getMasterPrenticeInfoById(relation.getMasterPrenticeInfoId());
				if(info != null){
					try {
						em.remove(info);
						removeFromCache(info);
					} catch (Exception e) {
						logger.error("[收徒成功删除师徒登记记录] ["+master.getLogString()+"]",e);
					}
				}
				relation.setMasterPrenticeInfoId(-1);
				if (logger.isWarnEnabled()){
					logger.warn("[收徒成功删除师徒登记记录成功] ["+master.getLogString()+"]");
				}
			}
		}
	}

	@Override
	public void evict(Player master,Player prentice) {
		Relation relation = socialManager.getRelationById(master.getId());
		if(relation.getMasterPrenticeInfoId() > 0){
			MasterPrenticeInfo info = this.getMasterPrenticeInfoById(relation.getMasterPrenticeInfoId());
			if(info != null){
				try {
					em.remove(info);
					removeFromCache(info);
				} catch (Exception e) {
					logger.error("[逐徒成功调用删除登记] ["+master.getLogString()+"]",e);
				}
			}
		}
		relation.setMasterPrenticeInfoId(-1);
		removeShiTuChenghao(master, prentice);
		if (logger.isInfoEnabled()) {
			logger.info("[逐徒成功调用删除登记] ["+master.getLogString()+"]");
		}
	}

	@Override
	public void rebel(Player master,Player prentice) {
		Relation relation = socialManager.getRelationById(prentice.getId());
		if(relation.getMasterPrenticeInfoId() > 0){
			MasterPrenticeInfo info = this.getMasterPrenticeInfoById(relation.getMasterPrenticeInfoId());
			if(info != null){
				try {
					em.remove(info);
					removeFromCache(info);
				} catch (Exception e) {
					logger.error("[判师成功调用删除登记] ["+prentice.getLogString()+"]",e);
				}
			}
		}
		removeShiTuChenghao(master, prentice);
		relation.setMasterPrenticeInfoId(-1);
		if (logger.isWarnEnabled()) {
			logger.warn("[判师成功调用删除登记] ["+prentice.getLogString()+"]");
		}
	}

	@Override
	public void addShiTuChenghao(Player master, Player prentice) {
		
		boolean bln = PlayerTitlesManager.getInstance().addSpecialTitle(prentice, MasterPrenticeManager.师徒称号, master.getName()+的徒弟, true);
		if(MasterPrenticeManager.logger.isWarnEnabled()){
			MasterPrenticeManager.logger.warn("[徒弟增加称号] ["+prentice+"] ["+bln+"]");
		}
		
	}

	@Override
	public void removeShiTuChenghao(Player master, Player prentice) {
		
		boolean bln = PlayerTitlesManager.getInstance().removeTitle(prentice, MasterPrenticeManager.师徒称号);
		
		if(MasterPrenticeManager.logger.isWarnEnabled()){
			MasterPrenticeManager.logger.warn("[徒弟删除称号] ["+prentice+"] ["+bln+"]");
		}
	}
	
	
	
	
}
