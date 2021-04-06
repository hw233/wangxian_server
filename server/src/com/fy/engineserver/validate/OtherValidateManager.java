package com.fy.engineserver.validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;


import org.slf4j.Logger;

import com.fy.engineserver.core.MailSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.deal.service.concrete.GameDealCenterProxy;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.message.DENY_USER_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAYER_PROPERTY_MAX_VALUE_INFO_REQ;
import com.fy.engineserver.message.VALIDATE_INFO_REQ;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.RequestMessage;

/**
 * 这个答题管理器是随机答题
 * 
 *
 */
public class OtherValidateManager implements ValidateManager {
	
	public static boolean isOpenRequestBuy = true;
	public static boolean isOpenTask = true;
	public static boolean isOpenMail = true;
	public static boolean isOpenDeal = true;
	public static boolean isOpenPlayerEnter = false;
	
	//随机答题的几率，跟着今天答题正确次数减少
	public static int[] ask_random = new int[]{15,12,10,8,5,3,1};
	//随机答题几次没随机中，跟着今天答题正确次数增加
	public static int[] ask_pass_num = new int[]{6,12,20,40,60,200};
	
	public static int ASK_TYPE_REQUESTBUY = 0;					//求购
	public static int ASK_TYPE_TASK = 1;						//任务
	public static int ASK_TYPE_MAIL = 2;						//邮件
	public static int ASK_TYPE_DEAL = 3;						//面对面交易
	public static int ASK_TYPE_PLAYER_ENTER = 4;				//选择角色
	
	public static boolean isOpen = false;
	
	public static Logger logger = null;
	
	public static int MAX_ANSWER_WRONG_TIME = 3;				//错误数目封号
	public static int TODAY_MAX_RIGHT_TIME = 5;					//一天的正确答题数目
	
	public static Random ran = new Random();
	
	public static int VALIDATE_TYPE_PICTURE = 0;							//图片一致  0
	public static int VALIDATE_TYPE_ANIMATION = 1;						//动画一致  1
	public static int VALIDATE_TYPE_PARTICLE = 2;						//粒子一致  2
	public static int VALIDATE_TYPE_WORD = 3;							//文字一致  3
	public static int VALIDATE_TYPE_CHOOSE_ANSWER = 4;					//文字选答案  4
	public static int VALIDATE_TYPE_CHOOSE_ANSWER_RANDOM = 5;					//文字选答案  5
	
	public static int MAX_ASK_TYPE = 6;				//最大答题类型数目

	public static int Validate_RMB = 70000000;				//需要答题的rmb
	
	public static int Validate_MAXLEVEL = 3000;			//需要答题的等级
	public static int Validate_MINLEVEL = 10;			//需要答题的等级
	
	private static OtherValidateManager instance;
	
	//用户答题信息
	public static String DISK_CACHE_ALLIDS_KEY = "ValidateUserDataIDS";
	public ArrayList<Long> userDataPlayerIDS = new ArrayList<Long>();
	public static String DISK_CACHE_DATA_KEY = "VUserData";
	public ConcurrentHashMap<Long, UserAskData> userDataMap = new ConcurrentHashMap<Long, UserAskData>();
	//用户题目信息
	public ConcurrentHashMap<Long, ValidateAsk> userAskMap = new ConcurrentHashMap<Long, ValidateAsk>();
	
	//图片名字
	public ArrayList<String> pictureNames = new ArrayList<String>();
	//动画名字
	public ArrayList<String> animationNames = new ArrayList<String>();
	//粒子名字
	public ArrayList<String> particleNames = new ArrayList<String>();
	//文字
	public ArrayList<String> wordNames = new ArrayList<String>();
	
	//选择答案文字
	public ArrayList<ChooseAnswerAskData> chooseWordAsks = new ArrayList<ChooseAnswerAskData>();
	//key是类型   value是随机的值
	public ConcurrentHashMap<String, ArrayList<String>> chooseAnswerRandom = new ConcurrentHashMap<String, ArrayList<String>>();
	
	private String configFile;				//excel配置的路径
	private String diskCachePath;
	public DefaultDiskCache diskCache;
	
	public static OtherValidateManager getInstance() {
		if (instance == null) {
			instance = new OtherValidateManager();
		}
		return instance;
	}
	
	public void init () throws Exception {
		
		logger = EnterLimitManager.logger;
		instance = this;
		
		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			isOpen = true;
		}
		
		loadFile();
		
		//diskCache
		diskCache = new DefaultDiskCache(new File(getDiskCachePath()), "", 1000L * 60 * 60 * 24 * 365);
		Object ob = diskCache.get(DISK_CACHE_ALLIDS_KEY);
		if (ob == null) {
			diskCache.put(DISK_CACHE_ALLIDS_KEY, userDataPlayerIDS);
		}else {
			userDataPlayerIDS = (ArrayList<Long>)ob;
		}
		
		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				//国服开启 答题  和  IOS  服务器检查  和  握手只用新的协议
			EnterLimitManager.isCheckIOS_Server = true;
		}
		ServiceStartRecord.startLog(this);
	}
	
	public void loadFile() throws Exception{
		//载入表
		pictureNames.clear();
		animationNames.clear();
		particleNames.clear();
		wordNames.clear();
		chooseWordAsks.clear();
		File f = new File(getConfigFile());
		f = new File(ConfigServiceManager.getInstance().getFilePath(f));
		if (!f.exists()) {
			logger.error("新充值活动配置不存在");
			return;
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		//图片题目
		{
			HSSFSheet sheet0 = workbook.getSheetAt(0);
			int maxRow0 = sheet0.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow0; i++) {
				HSSFCell cell = sheet0.getRow(i).getCell(0);
				pictureNames.add(cell.getStringCellValue());
			}
			logger.warn("图片题目---" + pictureNames.size());
		}
		//动画题目
		{
			HSSFSheet sheet1 = workbook.getSheetAt(1);
			int maxRow1 = sheet1.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow1; i++) {
				HSSFCell cell = sheet1.getRow(i).getCell(0);
				animationNames.add(cell.getStringCellValue());
			}
			logger.warn("动画题目---" + animationNames.size());
		}
		//粒子题目
		{
			HSSFSheet sheet2 = workbook.getSheetAt(2);
			int maxRow2 = sheet2.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow2; i++) {
				HSSFCell cell = sheet2.getRow(i).getCell(0);
				particleNames.add(cell.getStringCellValue());
			}
			logger.warn("粒子题目---" + particleNames.size());
		}
		//文字一致题目
		{
			HSSFSheet sheet3 = workbook.getSheetAt(3);
			int maxRow3 = sheet3.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow3; i++) {
				HSSFCell cell = sheet3.getRow(i).getCell(0);
				wordNames.add(cell.getStringCellValue());
			}
			logger.warn("文字一致题目---" + wordNames.size());
		}
		//文字选择题
		{
			HSSFSheet sheet4 = workbook.getSheetAt(4);
			int maxRow4 = sheet4.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow4; i++) {
				HSSFRow row = sheet4.getRow(i);
				HSSFCell cell0 = row.getCell(0);
				ChooseAnswerAskData data = new ChooseAnswerAskData();
				data.setAnswerMsg(cell0.getStringCellValue());
				ArrayList<String> answers = new ArrayList<String>();
				HSSFCell cell1 = row.getCell(1);
				String cell1_s = cell1.getStringCellValue();
				if (cell1_s != null && cell1_s.length() > 0) {
					answers.add(cell1_s);
				}
				HSSFCell cell2 = row.getCell(2);
				String cell2_s = cell2.getStringCellValue();
				if (cell2_s != null && cell2_s.length() > 0) {
					answers.add(cell2_s);
				}
				HSSFCell cell3 = row.getCell(3);
				if (cell3 != null) {
					String cell3_s = cell3.getStringCellValue();
					if (cell3_s != null && cell3_s.length() > 0) {
						answers.add(cell3_s);
					}
				}
				
				HSSFCell cell4 = row.getCell(4);
				if (cell4 != null) {
					String cell4_s = cell4.getStringCellValue();
					if (cell4_s != null && cell4_s.length() > 0) {
						answers.add(cell4_s);
					}
				}
				
				data.setAnswers(answers.toArray(new String[0]));
				HSSFCell cell5 = row.getCell(5);
				data.setRightAnswerIndex((int)cell5.getNumericCellValue());
				chooseWordAsks.add(data);
			}
			logger.warn("文字选择题---" + chooseWordAsks.size());
		}
		{
			//随机的文字选择题
			HSSFSheet sheet5 = workbook.getSheetAt(5);
			int maxRow5 = sheet5.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow5; i++) {
				HSSFRow row = sheet5.getRow(i);
				ArrayList<String> vv = new ArrayList<String>();
				for (int j = 1; j < row.getPhysicalNumberOfCells(); j++) {
					vv.add(row.getCell(j).getStringCellValue());
					logger.warn("[载入到] 行["+i+"] ["+maxRow5+"] 列["+j+"] ["+row.getPhysicalNumberOfCells()+"]");
				}
				chooseAnswerRandom.put(row.getCell(0).getStringCellValue(), vv);
			}
			logger.warn("随机文字选择题---" + chooseAnswerRandom.size());
		}
	}
	
	//载入用户答题信息
	public UserAskData getUserAskData (Player p) {
		UserAskData ask = userDataMap.get(p.getId());
		if (ask ==null) {
			Object ob = diskCache.get(DISK_CACHE_DATA_KEY+p.getId());
			if (ob == null) {
				ask = new UserAskData();
				ask.setPid(p.getId());
				ask.setpName(p.getName());
				ask.setuName(p.getUsername());
				ask.setLevel(p.getSoulLevel());
				diskCache.put(DISK_CACHE_DATA_KEY+p.getId(), ask);
				userDataPlayerIDS.add(p.getId());
				diskCache.put(DISK_CACHE_ALLIDS_KEY, userDataPlayerIDS);
			}else {
				ask = (UserAskData)ob;
			}
		}
		ask.setLastGetTime(System.currentTimeMillis());
		ArrayList<Long> removes = new ArrayList<Long>();
		for (Long key : userDataMap.keySet()) {
			UserAskData d = userDataMap.get(key);
			if (System.currentTimeMillis() - d.getLastGetTime() > 30*60*1000L) {
				removes.add(key);
			}
		}
		for (int i = 0; i < removes.size(); i++) {
			userDataMap.remove(removes.get(i));
		}
		return ask;
	}
	
	public static ArrayList<String> noValidateChannel = new ArrayList<String>();
	static {
	}
	public int getValidateState(Player p, int askType) {
		if (!isOpen) {
			return VALIDATE_STATE_通过;
		}
		if (p.getPassport() != null) {
			if (noValidateChannel.contains(p.getPassport().getLastLoginChannel())) {
				return VALIDATE_STATE_通过;
			}
		}
		if (!isOpenRequestBuy && askType == ASK_TYPE_REQUESTBUY) {
			return VALIDATE_STATE_通过;
		}
		if (!isOpenTask && askType == ASK_TYPE_TASK) {
			return VALIDATE_STATE_通过;
		}
		if (!isOpenMail && askType == ASK_TYPE_MAIL) {
			return VALIDATE_STATE_通过;
		}
		if (!isOpenDeal && askType == ASK_TYPE_DEAL) {
			return VALIDATE_STATE_通过;
		}
		if (!isOpenPlayerEnter && askType == ASK_TYPE_PLAYER_ENTER) {
			return VALIDATE_STATE_通过;
		}
		if (p.getRMB() > Validate_RMB) {
			return VALIDATE_STATE_通过;
		}
		if (p.getSoulLevel() > Validate_MAXLEVEL || p.getSoulLevel() <= Validate_MINLEVEL) {
			return VALIDATE_STATE_通过;
		}
		UserAskData ask = getUserAskData(p);
		//如果用户上次答题状态不是 通过 直接返回上次状态
		if (ask.getLastValidateState() == VALIDATE_STATE_拒绝) {
			return ask.getLastValidateState();
		}
		if (ask.getLastValidateState() == VALIDATE_STATE_需要答题) {
			ValidateAsk newAsk = createValidateAsk(p, askType);
			if (newAsk != null) {
				ask.addAnswerTimes(askType);
				diskCache.put(DISK_CACHE_DATA_KEY+p.getId(), ask);
				userAskMap.put(p.getId(), newAsk);
				return VALIDATE_STATE_需要答题;
			}else {
				return VALIDATE_STATE_通过;
			}
		}
		if ((!ask.isTodayRightEnough()) || askType == ASK_TYPE_PLAYER_ENTER) {
			int r = ran.nextInt(100);
			if (r < ask_random[ask.getTodayRihgtTimes()] || askType == ASK_TYPE_PLAYER_ENTER) {
				ValidateAsk newAsk = createValidateAsk(p, askType);
				if (newAsk != null) {
					ask.addAnswerTimes(askType);
					diskCache.put(DISK_CACHE_DATA_KEY+p.getId(), ask);
					userAskMap.put(p.getId(), newAsk);
					return VALIDATE_STATE_需要答题;
				}else {
					return VALIDATE_STATE_通过;
				}
			}else {
				ask.addPassCount();
				if (ask.getLastDirectPassCount() >= ask_pass_num[ask.getTodayRihgtTimes()]) {
					ValidateAsk newAsk = createValidateAsk(p, askType);
					if (newAsk != null) {
						ask.addAnswerTimes(askType);
						diskCache.put(DISK_CACHE_DATA_KEY+p.getId(), ask);
						userAskMap.put(p.getId(), newAsk);
						return VALIDATE_STATE_需要答题;
					}else {
						return VALIDATE_STATE_通过;
					}
				}
			}
		}
		return VALIDATE_STATE_通过;
	}

	public String getValidateStateReason(Player p, int askType) {
		return "多次答题错误";
	}

	public ValidateAsk getNextValidteAsk(Player p, int askType) {
		ValidateAsk ask = userAskMap.get(p.getId());
		return ask;
	}
	
	//窗口倒计时
	public static int CLIENT_WINDOW_TIME = 1000*60;
	//客户端控件是否变化，数值越大变化越多
	public static int CLIENT_MOVE_SCALE = 9;
	public ValidateAsk sendValidateAsk (Player p, int askType) {
		ValidateAsk ask = getNextValidteAsk(p, askType);
		UserAskData data = getUserAskData(p);
		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;
		if (ask instanceof IdenticalValidateAsk) {
			IdenticalValidateAsk idAsk = (IdenticalValidateAsk)ask;
			if (idAsk.getAskType() == VALIDATE_TYPE_ANIMATION) {
				w = 420;
				h = 340;
			}else {
				w = 420;
				h = 340;
			}
			x = ran.nextInt(960-w);
			y = ran.nextInt(640-h);
			String[] cMsgs = new String[idAsk.getAsks().length + 2];
			cMsgs[0] = Translate.答题标题;
			cMsgs[1] = Translate.translateString(Translate.一致性答题说明, new String[][]{{Translate.STRING_1, Translate.答题Names[idAsk.getAskType()]}});
			for (int i = 0; i < idAsk.getAsks().length; i++) {
				cMsgs[i+2] = idAsk.getAsks()[i];
			}
			VALIDATE_INFO_REQ req = new VALIDATE_INFO_REQ(GameMessageFactory.nextSequnceNum(), idAsk.getAskType(), new int[]{x,y,w,h,CLIENT_WINDOW_TIME,CLIENT_MOVE_SCALE, data.getLastanswerWrongTimes(), MAX_ANSWER_WRONG_TIME}, cMsgs);
			p.addMessageToRightBag(req);
			EnterLimitManager.logger.warn("[发送答题协议] [成功] ["+askType+"] ["+p.getLogString()+"] ["+ask.toString()+"]");
		}else if (ask instanceof ChooseAnswerValidateAsk) {
			ChooseAnswerValidateAsk chAsk = (ChooseAnswerValidateAsk)ask;
			w = 420;
			h = 340;
			x = ran.nextInt(960-w);
			y = ran.nextInt(640-h);
			VALIDATE_INFO_REQ req = new VALIDATE_INFO_REQ(GameMessageFactory.nextSequnceNum(), chAsk.getAskType(), new int[]{x,y,w,h,CLIENT_WINDOW_TIME,CLIENT_MOVE_SCALE, data.getLastanswerWrongTimes(), MAX_ANSWER_WRONG_TIME}, chAsk.getData().getClientMsg());
			p.addMessageToRightBag(req);
			EnterLimitManager.logger.warn("[发送答题协议] [成功] ["+askType+"] ["+p.getLogString()+"] ["+ask.toString()+"]");
		}
		return ask;
	}

	public void notifyTakeOneTask(Player p) {}

	public void notifyRequestBuySale(Player p) {}

	public boolean notifyAnswerAsk(Player p, ValidateAsk ask, String inputStr, int asktype) {
		UserAskData uData = getUserAskData(p);
		ask = userAskMap.get(p.getId());
		if (ask == null) {
			EnterLimitManager.logger.warn("[答题出错] [题目不存在 ] ["+p.getLogString()+"] ["+inputStr+"]");
			return false;
		}
		userAskMap.remove(p.getId());
		if (ask.isRight(inputStr)) {
			uData.answerRight(ask.getAskFormType());
			diskCache.put(DISK_CACHE_DATA_KEY+p.getId(), uData);
			logger.warn("[答题正确] ["+p.getLogString()+"] ["+ask.toString()+"] ["+uData.toString()+"]");
			if (ask.getAskFormType() == ASK_TYPE_REQUESTBUY) {
				Object[] pars = ask.getAskFormParameters();
				RequestBuyManager.getInstance().releaseRequestSale(p, Integer.parseInt(pars[0].toString()), Long.parseLong(pars[1].toString()), Integer.parseInt(pars[2].toString()), Long.parseLong(pars[3].toString()), false);
			}else if (ask.getAskFormType() == ASK_TYPE_MAIL) {
				Object[] pars = ask.getAskFormParameters();
				MailSubSystem.getInstance().creatMail((RequestMessage)pars[0], p, true);
			}else if (ask.getAskFormType() == ASK_TYPE_TASK) {
				Object[] pars = ask.getAskFormParameters();
				CompoundReturn cr = p.addTaskByServer((Task)pars[0]);
				if (cr.getBooleanValue()) {
					TaskSubSystem.validateManager.notifyTakeOneTask(p);
				}
			}else if (ask.getAskFormType() == ASK_TYPE_DEAL) {
				Object[] pars = ask.getAskFormParameters();
				((GameDealCenterProxy)GameDealCenterProxy.getInstance()).doCreateDeal(p, (Player)pars[0]);
			}else if (ask.getAskFormType() == ASK_TYPE_PLAYER_ENTER) {
				Object[] pars = ask.getAskFormParameters();
				p.addMessageToRightBag((Message)pars[0]);
				try {
					PLAYER_PROPERTY_MAX_VALUE_INFO_REQ ppmvir = new PLAYER_PROPERTY_MAX_VALUE_INFO_REQ(GameMessageFactory.nextSequnceNum(), p.getCareer(), PlayerManager.各个职业各个级别的所有战斗属性理论最大数值[p.getCareer()][p.getLevel() - 1]);
					p.addMessageToRightBag(ppmvir);
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[玩家进入游戏] [异常] [{}] [{}] [{}]", new Object[] { p.getId(), p.getUsername(), p.getName() }, ex);
				}
			}
			return true;
		}else {
			uData.answerWrong(ask.getAskFormType());
			boolean isFeng = false;
			if (uData.getLastanswerWrongTimes() >= MAX_ANSWER_WRONG_TIME) {
				isFeng = true;
				int hours = 1;
				int wrongTims = uData.getAllWrongTimes();
				int rightTims = uData.getAllRightTimes();
				int fengTims = wrongTims - rightTims/2;
				if (fengTims <= 0) {
					fengTims = 3;
				}
				if (fengTims < 20) {
					hours = fengTims/MAX_ANSWER_WRONG_TIME * 1;
				}else if(fengTims < 50) {
					hours = fengTims/MAX_ANSWER_WRONG_TIME * 5;
				}else {
					hours = fengTims/MAX_ANSWER_WRONG_TIME * 24;
				}
				DENY_USER_REQ req = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", p.getUsername(), Translate.答题错误次数过多封号提示, GameConstants.getInstance().getServerName() + "-外挂检测模块", false, true, false, 0, hours);
				MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
				if (p.getConn() != null) {
					p.getConn().close();
				}
			}
			if (!isFeng) {
				getValidateState(p, ask.getAskFormType());
				ValidateAsk newVA = sendValidateAsk(p, ask.getAskFormType());
				newVA.setAskFormParameters(ask.getAskFormParameters());
			}
			diskCache.put(DISK_CACHE_DATA_KEY+p.getId(), uData);
			logger.warn("[答题错误] [封"+isFeng+"] ["+p.getLogString()+"] ["+ask.toString()+"] ["+uData.toString()+"] ["+inputStr+"]");
		}
		
		return false;
	}
	
	//创建题目是否完全随机，这种随机就是从几种类型的活动中随机选一种
	public static boolean isAllRandom = false;
	//活动选择类型   0为一周一换
	public static int typeChooseType = 0;
	//指定某个活动类型
	public static int setValidateType = 5;
	/**
	 * 创建题目
	 * @param p
	 * @return
	 */
	public ValidateAsk createValidateAsk(Player p, int askType) {
		try {
			int type = -1;
			if (setValidateType >=0) {
				type = setValidateType;
			}else if (isAllRandom) {
				type = ran.nextInt(MAX_ASK_TYPE);
			}else {
				if (typeChooseType == 0) {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(System.currentTimeMillis());
					int w = cal.get(Calendar.WEEK_OF_YEAR);
					type = w % MAX_ASK_TYPE;
				}
			}
			if (type >= 0) {
				if (type < VALIDATE_TYPE_CHOOSE_ANSWER) {
					return createIdenticalValidateAsk(p, type, askType);
				}else if (type == VALIDATE_TYPE_CHOOSE_ANSWER) {
					return createChooseAnswerValidateAsk(p, askType);
				}else if (type == VALIDATE_TYPE_CHOOSE_ANSWER_RANDOM) {
					return createChooseAnswerRandomValidateAsk(p, askType);
				}
			}
			EnterLimitManager.logger.warn("[创建题目失败] ["+p.getLogString()+"] [全随机:"+isAllRandom+"] [创建方式:"+typeChooseType+"] [活动类型:"+type+"] [请求类型:"+askType+"]");
		} catch (Exception e) {
			EnterLimitManager.logger.error("创建题目失败", e);
		}
		return null;
	}
	
	public ValidateAsk createChooseAnswerValidateAsk(Player player, int askType) {
		int index = ran.nextInt(chooseWordAsks.size());
		ChooseAnswerValidateAsk ask = new ChooseAnswerValidateAsk();
		ask.setAskFormType(askType);
		ask.setAskType(VALIDATE_TYPE_CHOOSE_ANSWER);
		ask.setData(chooseWordAsks.get(index));
		EnterLimitManager.logger.warn("[创建一个选择答案题] [成功] [请求类型:"+askType+"] ["+player.getLogString()+"] ["+ask.toString()+"]");
		return ask;
	}
	
	public ValidateAsk createChooseAnswerRandomValidateAsk(Player player, int askType) {
		ChooseAnswerValidateAsk ask = new ChooseAnswerValidateAsk();
		ask.setAskFormType(askType);
		ask.setAskType(VALIDATE_TYPE_CHOOSE_ANSWER);
		int isShi = ran.nextInt(2);
		int keyIndex = ran.nextInt(chooseAnswerRandom.size()) + 1;
		String key = "";
		Enumeration<String> ee = chooseAnswerRandom.keys();
		for (int i = 0; i < keyIndex; i++) {
			key = ee.nextElement();
		}
		if (key.length() > 0) {
			ChooseAnswerAskData data = new ChooseAnswerAskData();
			data.setRightAnswerIndex(0);
			ArrayList<String> right = null;
			ArrayList<String> wrong = null;
			if (isShi == 0) {
				data.setAnswerMsg(Translate.translateString(Translate.请选择下列是XX的选项, new String[][]{{Translate.STRING_1, key}}));
				wrong = new ArrayList<String>();
				for (String kk : chooseAnswerRandom.keySet()) {
					if (kk.equals(key)) {
						right = chooseAnswerRandom.get(kk);
					}else {
						wrong.addAll(chooseAnswerRandom.get(kk));
					}
				}
			}else {
				data.setAnswerMsg(Translate.translateString(Translate.请选择下列不是XX的选项, new String[][]{{Translate.STRING_1, key}}));
				right = new ArrayList<String>();
				for (String kk : chooseAnswerRandom.keySet()) {
					if (kk.equals(key)) {
						wrong = chooseAnswerRandom.get(kk);
					}else {
						right.addAll(chooseAnswerRandom.get(kk));
					}
				}
			}
			String[] answers = new String[4];
			int rightIndex = ran.nextInt(right.size());
			answers[0] = right.get(rightIndex);
			for (int i = 0; i < 3; i++) {
				int wrongIndex = ran.nextInt(wrong.size());
				answers[i+1] = wrong.get(wrongIndex);
			}
			data.setAnswers(answers);
			ask.setData(data);
			EnterLimitManager.logger.warn("[创建一个随机选择答案题] [成功] [请求类型:"+askType+"] [是否:"+isShi+"] ["+player.getLogString()+"] ["+ask.toString()+"]");
			return ask;
		}else {
			return null;
		}
	}
	
	/**
	 * 创建一个 图片一致的题目
	 * @return
	 */
	public int MIN_PICTURE_NUM = 4;			//最小图片数目   不能小于3  不能大于MAX
	public int MAX_PICTURE_NUM = 6;			//最大图片数目
	public int MIN_ANIMATION_NUM = 4;		//最小动画数目   4个界面能放下  多了放不下
	public int MAX_ANIMATION_NUM = 4;		//最大动画数目
	public int MIN_PARTICLE_NUM = 4;		//最小粒子数目   不能小于3  不能大于MAX
	public int MAX_PARTICLE_NUM = 4;		//最大粒子数目
	public int MIN_WORD_NUM = 4;			//最小文字数目   不能小于3  不能大于MAX
	public int MAX_WORD_NUM = 6;			//最大文字数目
	public ValidateAsk createIdenticalValidateAsk(Player player, int type, int askType) {
		//题目图片数目
		int min = 0;
		int max = 0;
		ArrayList<String> askNames = null;
		if (type == VALIDATE_TYPE_PICTURE) {
			min = MIN_PICTURE_NUM;
			max = MAX_PICTURE_NUM;
			askNames = pictureNames;
		}else if (type == VALIDATE_TYPE_ANIMATION) {
			min = MIN_ANIMATION_NUM;
			max = MAX_ANIMATION_NUM;
			askNames = animationNames;
		}else if (type == VALIDATE_TYPE_PARTICLE) {
			min = MIN_PARTICLE_NUM;
			max = MAX_PARTICLE_NUM;
			askNames = particleNames;
		}else if (type == VALIDATE_TYPE_WORD) {
			min = MIN_WORD_NUM;
			max = MAX_WORD_NUM;
			askNames = wordNames;
		}
		int picNum = ran.nextInt(max - min + 1) + min;
		ArrayList<String> picNames = new ArrayList<String>();
		//找到比题目需要图片数目少1的列表
		for (int i = 0; i < picNum - 1; i++) {
			int rr = ran.nextInt(askNames.size());
			String findName = askNames.get(rr);
			while (picNames.contains(findName)) {
				rr = ran.nextInt(askNames.size());
				findName = askNames.get(rr);
			}
			picNames.add(findName);
		}
		//
		int rightIndex = ran.nextInt(picNames.size());
		String answerName = picNames.get(rightIndex);
		picNames.add(answerName);
		//随机打乱答案的位置
		for (int i = 0; i < 3; i++) {
			int mm = ran.nextInt(picNames.size());
			String a = picNames.remove(mm);
			picNames.add(a);
		}
		
		IdenticalValidateAsk ask = new IdenticalValidateAsk();
		ask.setAskFormType(askType);
		ask.setAskType(type);
		ask.setAsks(picNames.toArray(new String[0]));
		EnterLimitManager.logger.warn("[创建一个选一致的题] [成功] [请求类型:"+askType+"] ["+player.getLogString()+"] [数目范围:"+min + "-" + max+"] ["+ask.toString()+"]");
		return ask;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setDiskCachePath(String diskCachePath) {
		this.diskCachePath = diskCachePath;
	}

	public String getDiskCachePath() {
		return diskCachePath;
	}
	
	
}
