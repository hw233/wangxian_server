package com.sqage.stat.server;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.ChongZhi;
import com.sqage.stat.commonstat.entity.HuoDonginfo;
import com.sqage.stat.commonstat.entity.PlayGame;
import com.sqage.stat.commonstat.entity.Taskinfo;
import com.sqage.stat.commonstat.entity.User;
import com.sqage.stat.commonstat.manager.Impl.ChongZhiManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.HuoDonginfoManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.OnLineUsersCountManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.TaskinfoManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.TransactionManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.Transaction_SpecialManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.UserManagerImpl;
import com.sqage.stat.dao.ChannelDAO;
import com.sqage.stat.language.MultiLanguageManager;
import com.sqage.stat.message.ACCEPTHUODONGINFO_LOG_REQ;
import com.sqage.stat.message.ACCEPTTASK_LOG_REQ;
import com.sqage.stat.message.BATTLE_COSTTIME_LOG_REQ;
import com.sqage.stat.message.BATTLE_PLAYERSTAT_LOG_REQ;
import com.sqage.stat.message.BATTLE_TEAMSTAT_LOG_REQ;
import com.sqage.stat.message.CREATEPLAYER_LOG_REQ;
import com.sqage.stat.message.DAOJU_LOG_REQ;
import com.sqage.stat.message.DAYCHENG_LOG_REQ;
import com.sqage.stat.message.ENTERGAME_LOG_REQ;
import com.sqage.stat.message.FINISHHUODONGINFO_LOG_REQ;
import com.sqage.stat.message.FINISHTASK_LOG_REQ;
import com.sqage.stat.message.FUMO_LOG_REQ;
import com.sqage.stat.message.GAMECHONGZHI_LOG_REQ;
import com.sqage.stat.message.LIBAO_LOG_REQ;
import com.sqage.stat.message.LOGOUTGAME_LOG_REQ;
import com.sqage.stat.message.NPC_LOG_REQ;
import com.sqage.stat.message.ONLINEUSERSCOUNT_LOG_REQ;
import com.sqage.stat.message.PAYMONEYUPDATE_LOG_REQ;
import com.sqage.stat.message.PAYMONEY_LOG_REQ;
import com.sqage.stat.message.StatMessageFactory;
import com.sqage.stat.message.TRANSACTION_FACE_LOG_REQ;
import com.sqage.stat.message.TRANSACTION_LOG_REQ;
import com.sqage.stat.message.TRANSACTION_SPECIAL_LOG_REQ;
import com.sqage.stat.message.TRANSFER_PLATFORM_LOG_REQ;
import com.sqage.stat.message.USEREGIST_LOG_REQ;
import com.sqage.stat.message.YINZIKUCUN_LOG_REQ;
import com.sqage.stat.model.AcceptHuoDonginfoFlow;
import com.sqage.stat.model.AcceptTaskFlow;
import com.sqage.stat.model.Channel;
import com.sqage.stat.model.ChannelItem;
import com.sqage.stat.model.CreatePlayerFlow;
import com.sqage.stat.model.DaoJuFlow;
import com.sqage.stat.model.DayChangFlow;
import com.sqage.stat.model.EnterGameFlow;
import com.sqage.stat.model.FinishHuoDonginfoFlow;
import com.sqage.stat.model.FinishTaskFlow;
import com.sqage.stat.model.GameChongZhiFlow;
import com.sqage.stat.model.LogOutGameFlow;
import com.sqage.stat.model.PayMoneyFlow;
import com.sqage.stat.model.PayMoneyUpGradeFlow;
import com.sqage.stat.model.SavingYuanbao;
import com.sqage.stat.model.UserRegistFlow;
import com.sqage.stat.model.UserStat;
import com.sqage.stat.service.ChannelItemManager;
import com.sqage.stat.service.SavingYuanbaoManager;
import com.sqage.stat.service.ShengDaCallManager;
import com.sqage.stat.service.UserStatManager;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;


public class StatServerService implements ConnectionConnectedHandler, MessageHandler, Runnable {

	private String queueFile;
	static AdvancedFilePersistentQueue queue;
	
	static AdvancedFilePersistentQueue registUserqueue;
	static AdvancedFilePersistentQueue enterGamequeue;

	
	static AdvancedFilePersistentQueue daoJuqueue;
	static AdvancedFilePersistentQueue daoJu_mohu_queue;//只需要粗略统计的道具队列
	static AdvancedFilePersistentQueue gameChongZhiqueue;
	static AdvancedFilePersistentQueue gameChongZhiJingXiqueue;//不排重的队列
	
	static AdvancedFilePersistentQueue taskAcceptqueue;
	static AdvancedFilePersistentQueue taskFinishqueue;
	static AdvancedFilePersistentQueue taskAnalysisFinishqueue;
	static AdvancedFilePersistentQueue taskAnalysisAcceptqueue;
	static AdvancedFilePersistentQueue onLineUsersqueue;
	static AdvancedFilePersistentQueue acceptHuoDonginfoqueue;
	static AdvancedFilePersistentQueue finishHuoDonginfoqueue;
	static AdvancedFilePersistentQueue transactionqueue;
	static AdvancedFilePersistentQueue transaction_Specialqueue;
	static AdvancedFilePersistentQueue yinZiKunCuqueue;//银子库存
	static AdvancedFilePersistentQueue liBaoqueue;//周月礼包
	static AdvancedFilePersistentQueue transaction_Facequeue;//面对面交易队列
	static AdvancedFilePersistentQueue transfer_PlatFormqueue;//交易平台队列
	
	static AdvancedFilePersistentQueue battle_PlayerStatqueue;//对战平台个人功勋队列
	static AdvancedFilePersistentQueue battle_costTimeFlowqueue;//对战平台花费时间队列
	static AdvancedFilePersistentQueue battle_TeamStatFlowqueue;//对战平台队团队功勋队列
	static AdvancedFilePersistentQueue fuMoFlowqueue;//附魔队列
	static AdvancedFilePersistentQueue npcinfoFlowqueue;//NPC队列
	
	
	static public HashMap<String,String[]> CurrencyTypeMap=new HashMap<String,String[]>();
	static public HashMap<String,String[]> ReasonTypeMap=new HashMap<String,String[]>();
	static public HashMap<String,String[]> GetTypeMap=new HashMap<String,String[]>();
	static public HashMap<String,String[]> DaoJuNameMap=new HashMap<String,String[]>();
	
	static String shetname=null;
	//static public HashMap<String,String[]> channelMap=new HashMap<String,String[]>();
	
	static Logger logger = Logger.getLogger(StatServerService.class);
	Thread thread1;
	Thread thread2;
	Thread thread3;
	Thread thread4;
	Thread thread5;
	
	Thread thread6;//游戏充值线程
	Thread thread7;//道具信息处理线程
	Thread thread8;//道具信息处理线程
	Thread thread9;//游戏充值线程
	Thread thread10;//游戏充值线程
//	Thread thread11;//游戏充值线程
	
	Thread thread12;//游戏充值排重线程
	Thread thread13;//道具排重线程
	
	//Thread thread14;//道具排重线程
	//Thread thread15;//道具排重线程
	//Thread thread16;//游戏充值线程
	//Thread thread17;//游戏充值线程
	
	Thread thread18;//接受任务入库线程
	Thread thread19;//接受任务转到任务分析
	Thread thread20;//接受任务分析排重
	Thread thread21;//完成任务转到任务分析
	Thread thread22;//完成任务分析排重
	Thread thread23;//完成任务入库线程
	

//	Thread thread24;//游戏充值线程
//	Thread thread25;//完成任务入库线程
	Thread thread26;//道具信息处理线程
//	Thread thread27;//道具信息处理线程
//	Thread thread28;//游戏充值线程
//	Thread thread29;//游戏充值线程
	
	Thread thread30;//用户在线信息线程
	Thread thread31;//用户在线信息线程
	
	Thread thread32;//接受活动线程
	Thread thread33;//完成活动线程
	Thread thread34;//交易线程
	Thread thread35;//异常交易线程
	Thread thread36;//银子留存线程
	
	Thread thread37;//粗略道具线程
	Thread thread38;//粗略道具排重线程
	
	Thread thread39;//精细游戏充值线程
	Thread thread40;//周月礼包线程
	Thread thread41;//面对面交易线程
	Thread thread42;//平台交易线程
	
	Thread thread43;//对战玩家功勋线程
	Thread thread44;//对战耗时线程
	Thread thread45;//对战团队功勋线程
	Thread thread46;//佛魔线程
	
	Thread thread47;
	Thread thread48;
	
	Thread thread49;  //NPC统计线程
	
	
	UserManagerImpl userManager;
	PlayGameManagerImpl playGameManager;
	OnLineUsersCountManagerImpl onLineUsersCountManager;
	ChongZhiManagerImpl chongZhiManager;
	ChannelDAO channelDAO;
	ChannelItemManager channelItemManager;
	SavingYuanbaoManager savingYuanbaoManager;
	UserStatManager userStatManager;
	TransactionManagerImpl transactionManager;
	HuoDonginfoManagerImpl huoDonginfoManager;
	TaskinfoManagerImpl taskinfoManager;
//	GameChongZhiManagerImpl gameChongZhiManager;
	Transaction_SpecialManagerImpl transaction_SpecialManager;
	
	
	DaoJu_MoHuService daoJu_MoHuService;
	DaoJuMoHuService_merge daoJuMoHuService_merge;
	
	DaoJuService daoJuService;//=DaoJuService.getInstance();
	DaoJuService_merge daoJuService_merge;//道具排重线程
	GameChongZhiService gameChongZhiService;//=GameChongZhiService.getInstance();
	GameChongZhiService_merge gameChongZhiService_merge;//游戏充值排重线程
	
	GameChongZhi_JingXiService gameChongZhi_JingXiService;
	
	///////////////////任务信息/////////////////////////
	TaskAcceptService taskAcceptService;//接受任任务线程
	TaskFinishService taskFinishService;//完成任务线程
	Task2AnalysisService task2AnalysisService;
	TaskAnalysisService_merge taskAnalysisService_merge;
	TaskFinish2AnalysisService taskFinish2AnalysisService;
	TaskFinishService_merge taskFinishService_merge;
	OnLineUsersService onLineUsersService;//在线用户数线程
	
	AcceptHuoDonginfoService acceptHuoDonginfoService;      // 接受活动线程
	FinishHuoDonginfoService finishHuoDonginfoService;      // 完成活动线程
	TransactionService transactionService;                  // 交易线程
	Transaction_SpecialService transaction_SpecialService;  // 异常交易线程
	
	YinZiKuCunService yinZiKuCunService;
	LiBaoqueueService liBaoqueueService;
	Transaction_FaceService transaction_FaceService;
	Transfer_PlatformService transfer_PlatformService;
	
	Battle_PlayerStatService battle_PlayerStatService;
	Battle_costTimeService battle_costTimeService;
	Battle_TeamStatService battle_TeamStatService;
	FuMoStatService fuMoStatService;
	NpcinfoStatService npcinfoStatService;//NPC 信息
	
	EnterGameService enterGameService;
	RegistUserService registUserService;

	
	
	static StatServerService self;
	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static StatServerService getInstance() {
		return self;
	}

	public void init() throws Exception {

		FileUtils.chkFolder(queueFile);
		FileUtils.chkFolder(queueFile+"/registUserqueue");
		FileUtils.chkFolder(queueFile+"/enterGamequeue");
		
		
		FileUtils.chkFolder(queueFile+"/daoJu_queue");
		FileUtils.chkFolder(queueFile+"/daoJu_mohu_queue");
		FileUtils.chkFolder(queueFile+"/gameChongZhi_queue");
		FileUtils.chkFolder(queueFile+"/gameChongZhiJingXiqueue");
		
		FileUtils.chkFolder(queueFile+"/taskAccept_queue");
		FileUtils.chkFolder(queueFile+"/taskFinish_queue");
		FileUtils.chkFolder(queueFile+"/taskAnalysisFinish_queue");
		FileUtils.chkFolder(queueFile+"/taskAnalysisAccept_queue");
		FileUtils.chkFolder(queueFile+"/onLineUsers_queue");
		
		FileUtils.chkFolder(queueFile+"/acceptHuoDonginfo_queue");
		FileUtils.chkFolder(queueFile+"/finishHuoDonginfo_queue");
		FileUtils.chkFolder(queueFile+"/transaction_queue");
		FileUtils.chkFolder(queueFile+"/transaction_Special_queue");
		FileUtils.chkFolder(queueFile+"/yinZiKunCuqueue");
		
		FileUtils.chkFolder(queueFile+"/liBaoqueue");
		FileUtils.chkFolder(queueFile+"/transaction_Facequeue");
		FileUtils.chkFolder(queueFile+"/transfer_PlatFormqueue");
		
		FileUtils.chkFolder(queueFile+"/battle_PlayerStatqueue");
		FileUtils.chkFolder(queueFile+"/battle_costTimeFlowqueue");
		FileUtils.chkFolder(queueFile+"/battle_TeamStatFlowqueue");
		FileUtils.chkFolder(queueFile+"/fuMoFlowqueue");
		
		FileUtils.chkFolder(queueFile+"/npcinfoFlowqueue");
		
		
		queue                    = new AdvancedFilePersistentQueue(new File(queueFile), 1000, 64 * 1024 * 1024L);
		registUserqueue          = new AdvancedFilePersistentQueue(new File(queueFile+"/registUserqueue"), 1000, 64 * 1024 * 1024L);
		enterGamequeue           = new AdvancedFilePersistentQueue(new File(queueFile+"/enterGamequeue"), 1000, 64 * 1024 * 1024L);
		
		daoJuqueue               = new AdvancedFilePersistentQueue(new File(queueFile+"/daoJu_queue"), 1000, 64 * 1024 * 1024L);
		daoJu_mohu_queue         = new AdvancedFilePersistentQueue(new File(queueFile+"/daoJu_mohu_queue"), 1000, 64 * 1024 * 1024L);
		gameChongZhiqueue        = new AdvancedFilePersistentQueue(new File(queueFile+"/gameChongZhi_queue"), 1000, 64 * 1024 * 1024L);
		gameChongZhiJingXiqueue  = new AdvancedFilePersistentQueue(new File(queueFile+"/gameChongZhiJingXiqueue"), 1000, 64 * 1024 * 1024L);
		
		taskAcceptqueue          = new AdvancedFilePersistentQueue(new File(queueFile+"/taskAccept_queue"), 1000, 64 * 1024 * 1024L);
		taskFinishqueue          = new AdvancedFilePersistentQueue(new File(queueFile+"/taskFinish_queue"), 1000, 64 * 1024 * 1024L);
		taskAnalysisFinishqueue  = new AdvancedFilePersistentQueue(new File(queueFile+"/taskAnalysisFinish_queue"), 1000, 64 * 1024 * 1024L);
		taskAnalysisAcceptqueue  = new AdvancedFilePersistentQueue(new File(queueFile+"/taskAnalysisAccept_queue"), 1000, 64 * 1024 * 1024L);
		onLineUsersqueue         = new AdvancedFilePersistentQueue(new File(queueFile+"/onLineUsers_queue"), 1000, 64 * 1024 * 1024L);
		
		acceptHuoDonginfoqueue   = new AdvancedFilePersistentQueue(new File(queueFile+"/acceptHuoDonginfo_queue"), 1000, 64 * 1024 * 1024L);
		finishHuoDonginfoqueue   = new AdvancedFilePersistentQueue(new File(queueFile+"/finishHuoDonginfo_queue"), 1000, 64 * 1024 * 1024L);
		transactionqueue         = new AdvancedFilePersistentQueue(new File(queueFile+"/transaction_queue"), 1000, 64 * 1024 * 1024L);//交易
		transaction_Specialqueue = new AdvancedFilePersistentQueue(new File(queueFile+"/transaction_Special_queue"), 1000, 64 * 1024 * 1024L);//异常交易
		yinZiKunCuqueue          = new AdvancedFilePersistentQueue(new File(queueFile+"/yinZiKunCuqueue"), 1000, 64 * 1024 * 1024L);//银子库存
		
		liBaoqueue               = new AdvancedFilePersistentQueue(new File(queueFile+"/liBaoqueue"), 1000, 64 * 1024 * 1024L);//周月礼包库存
		transaction_Facequeue    = new AdvancedFilePersistentQueue(new File(queueFile+"/transaction_Facequeue"), 1000, 64 * 1024 * 1024L);//面对面交易
		transfer_PlatFormqueue   = new AdvancedFilePersistentQueue(new File(queueFile+"/transfer_PlatFormqueue"), 1000, 64 * 1024 * 1024L);//平台交易
		
		battle_PlayerStatqueue    = new AdvancedFilePersistentQueue(new File(queueFile+"/battle_PlayerStatqueue"), 1000, 64 * 1024 * 1024L);//平台交易
		battle_costTimeFlowqueue  = new AdvancedFilePersistentQueue(new File(queueFile+"/battle_costTimeFlowqueue"), 1000, 64 * 1024 * 1024L);//平台交易
		battle_TeamStatFlowqueue  = new AdvancedFilePersistentQueue(new File(queueFile+"/battle_TeamStatFlowqueue"), 1000, 64 * 1024 * 1024L);//平台交易
		fuMoFlowqueue             = new AdvancedFilePersistentQueue(new File(queueFile+"/fuMoFlowqueue"), 1000, 64 * 1024 * 1024L);//附魔信息
		
		npcinfoFlowqueue             = new AdvancedFilePersistentQueue(new File(queueFile+"/npcinfoFlowqueue"), 1000, 64 * 1024 * 1024L);//NPC信息
		
		
		thread1 = new Thread(this, "StatServerService-Thread1");
		thread1.start();
		
		thread2 = new Thread(this, "StatServerService-Thread2");
		thread2.start();
		
		thread3 = new Thread(this, "StatServerService-Thread3");
		thread3.start();
		
		thread4 = new Thread(this, "StatServerService-Thread4");
		thread4.start();

		thread5 = new Thread(this, "StatServerService-Thread5");
		thread5.start();
		
		//thread16 = new Thread(this, "StatServerService-Thread16");
		//thread16.start();
		//thread17 = new Thread(this, "StatServerService-Thread17");
		//thread17.start();
		
		
		
		
		// 游戏充值线程
		thread6 = new Thread(gameChongZhiService, "gameChongZhiService-Thread6");
		thread6.start();
		thread9 = new Thread(gameChongZhiService, "gameChongZhiService-Thread9");
		thread9.start();
		thread10 = new Thread(gameChongZhiService, "gameChongZhiService-Thread10");
		thread10.start();
//		thread11 = new Thread(gameChongZhiService, "gameChongZhiService-Thread11");
//		thread11.start();
//		thread28 = new Thread(gameChongZhiService, "gameChongZhiService-Thread28");
//		thread28.start();
//		thread29 = new Thread(gameChongZhiService, "gameChongZhiService-Thread29");
//		thread29.start();		
//		thread24 = new Thread(gameChongZhiService, "taskFinishService-Thread24");
//		thread24.start();
		
		//thread12 = new Thread(gameChongZhiService_merge, "gameChongZhiService_merge-Thread12");//
		//thread12.start();
		
		
		
		//道具处理线程
		//daoJuService.setDaoJuqueue(daoJuqueue);
		thread7 = new Thread(daoJuService, "daoJuService-Thread7");
		thread7.start();
		thread8 = new Thread(daoJuService, "daoJuService-Thread8");
		thread8.start();
		thread26 = new Thread(daoJuService, "daoJuService-Thread26");
		thread26.start();
//		thread27 = new Thread(daoJuService, "daoJuService-Thread27");
//		thread27.start();	
		thread13 = new Thread(daoJuService_merge, "daoJuService_merge-Thread13");
		thread13.start();
//		thread14 = new Thread(daoJuService_merge, "daoJuService_merge-Thread14");
//		thread14.start();
//		thread15 = new Thread(daoJuService_merge, "daoJuService_merge-Thread15");
//		thread15.start();
		
		
		
		thread18 = new Thread(taskAcceptService, "taskAcceptService-Thread18");
		thread18.start();
		thread19 = new Thread(task2AnalysisService, "task2AnalysisService-Thread19");
		thread19.start();
		thread20 = new Thread(taskAnalysisService_merge, "taskAnalysisService_merge-Thread20");
		thread20.start();
		thread21 = new Thread(taskFinish2AnalysisService, "taskFinish2AnalysisService-Thread21");
		thread21.start();
		thread22 = new Thread(taskFinishService_merge, "taskFinishService_merge-Thread22");
		thread22.start();
		thread23 = new Thread(taskFinishService, "taskFinishService-Thread23");
		thread23.start();
		//thread25 = new Thread(taskFinishService, "taskFinishService-Thread25");
		//thread25.start();
		
		thread30 = new Thread(onLineUsersService, "onLineUsersService-Thread30"); //用户在线线程
		thread30.start();
		thread31 = new Thread(onLineUsersService, "onLineUsersService-Thread31"); //用户在线线程
		thread31.start();
		
		thread32 = new Thread(acceptHuoDonginfoService,"acceptHuoDonginfoService-Thread32");//接受活动线程
		thread32.start();
		thread33 = new Thread(finishHuoDonginfoService,"finishHuoDonginfoService-Thread33");//完成活动线程
		thread33.start();
		thread34 = new Thread(transactionService,"transactionService-Thread34");//交易线程
		thread34.start();
		thread35 = new Thread(transaction_SpecialService,"transaction_SpecialService-Thread35");//异常交易线程
		thread35.start();

		thread36 = new Thread(yinZiKuCunService,"yinZiKuCunService-Thread36");//银子留存线程
		thread36.start();
		
		
		thread37=new Thread(daoJu_MoHuService,"daoJu_MoHuService-Thread37");//粗略道具线程
		thread37.start();
		thread38=new Thread(daoJuMoHuService_merge,"daoJuMoHuService_merge-Thread38");//粗略道具排重线程
		thread38.start();
		
		thread39=new Thread(gameChongZhi_JingXiService,"gameChongZhi_JingXiService-Thread39");//粗略道具排重线程
		thread39.start();
		
		thread40=new Thread(liBaoqueueService,"liBaoqueueService-Thread40");//周月礼包线程
		thread40.start();
		
		thread41=new Thread(transaction_FaceService,"transaction_FaceService-Thread41");//周月礼包线程
		thread41.start();
		
		thread42=new Thread(transfer_PlatformService,"transaction_FaceService-Thread42");//周月礼包线程
		thread42.start();
		

		thread43=new Thread(battle_PlayerStatService,"Battle_PlayerStatService-Thread43");//对战玩家功勋线程
		thread43.start();
		thread44=new Thread(battle_costTimeService,"battle_costTimeService-Thread44");//对战耗时线程
		thread44.start();
		
		thread45=new Thread(battle_TeamStatService,"Battle_TeamStatService-Thread45");//对战团队功勋线程
		thread45.start();
		
		thread46=new Thread(fuMoStatService,"FuMoStatService--Thread46");//对战团队功勋线程
		thread46.start();
		
		
		thread47=new Thread(enterGameService,"EnterGameService--Thread47");//对战团队功勋线程
		thread47.start();
		thread48=new Thread(registUserService,"RegistUserService--Thread48");//对战团队功勋线程
		thread48.start();

		thread49=new Thread(npcinfoStatService,"npcinfoStatService--Thread49");//NPC信息线程
		thread49.start();
		System.out.println("[StatServerService] [初始化成功]");
		logger.warn("[StatServerService] [初始化成功]");
		self = this;
	}

	@Override
	public void connected(Connection conn) throws IOException {
		StatMessageFactory mf = StatMessageFactory.getInstance();
		conn.setMessageFactory(mf);
		conn.setMessageHandler(this);
		logger.warn("[StatServerService] [connected成功]");
	}

	@Override
	public void discardRequestMessage(Connection arg0, RequestMessage arg1) throws ConnectionException {
		// TODO Auto-generated method stub

	}

	@Override
	public ResponseMessage receiveRequestMessage(Connection conn, RequestMessage req) throws ConnectionException {
		if(logger.isDebugEnabled())
			logger.debug("[receive-message] ["+conn.getName()+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"]");
		queue.push(req);
		return null;
	}

	@Override
	public void receiveResponseMessage(Connection arg0, RequestMessage arg1, ResponseMessage arg2) throws ConnectionException {
	}

	@Override
	public RequestMessage waitingTimeout(Connection arg0, long arg1) throws ConnectionException {
		return null;
	}

	private boolean handle(RequestMessage message) {

		StatServerService.shetname=MultiLanguageManager.translateMap.get("dailySheetName");
		
		boolean result = true;
		
			if (message instanceof USEREGIST_LOG_REQ) {
				registUserqueue.push(message);
				//result= doUserRegist(message);
			} else if (message instanceof ENTERGAME_LOG_REQ) {
				//result= doEnterGame(message);
				enterGamequeue.push(message);
			} else if (message instanceof LOGOUTGAME_LOG_REQ) {
				//result= doLogOutGame(message);
				enterGamequeue.push(message);
			} else if (message instanceof DAYCHENG_LOG_REQ) {
				result= doDayChang(message);
				//enterGamequeue.push(message);
			} else if (message instanceof PAYMONEY_LOG_REQ) {
				result= doPayMoney(message);
			} 
			
			else if (message instanceof PAYMONEYUPDATE_LOG_REQ) {
				
				PAYMONEYUPDATE_LOG_REQ req = (PAYMONEYUPDATE_LOG_REQ) message;
				PayMoneyUpGradeFlow flow = req.getPayMoneyUpGradeFlow();
				String userName=flow.getUserName();
				if("wsx01".equals(userName)||"edc01".equals(userName)||"rfv01".equals(userName)||"tgb001".equals(userName)||"yhn001".equals(userName)||"ujm002".equals(userName)
						||"ikl001".equals(userName)||"olp001".equals(userName)||"olp002".equals(userName)||"olp003".equals(userName)||"olp004".equals(userName)||"olp005".equals(userName)
						||"olp006".equals(userName)||"olp0077".equals(userName)||"olp008".equals(userName)){
					doPayMoney_unRecord(message);
				}else{
				result= doPayMoneyUpDate(message);
				}
			} 
			
			else if (message instanceof CREATEPLAYER_LOG_REQ) {
				result=	doCreatePlayer(message);
			} else if (message instanceof ONLINEUSERSCOUNT_LOG_REQ) {
				result=onLineUsersqueue.push(message);
			} else if (message instanceof DAOJU_LOG_REQ) {
				DAOJU_LOG_REQ req = (DAOJU_LOG_REQ) message;
				DaoJuFlow flow = req.getDaoJuFlow();
				String daoJuName = flow.getDaoJuName();
				String position=flow.getPosition();
				long createDate=flow.getCreateDate();
				if(shetname!=null&&shetname.indexOf("韩国")!=-1){createDate+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
				flow.setCreateDate(createDate);
				
				String getType=flow.getGetType();
				if(getType.indexOf("-<f")!=-1){
				flow.setGetType(getType.substring(0,getType.indexOf("-<f")));
				}
				
				if(!daoJuName.equals("混元一气丹")&&!daoJuName.equals("文武二圣丹")&&!daoJuName.equals("三古造化丹")&&!daoJuName.equals("四海归心丹")&&
						!daoJuName.equals("五雷正法丹")&&!daoJuName.equals("六丁六甲丹")&&!daoJuName.equals("七巧玲珑丹")&&!daoJuName.equals("八宝如意丹")&&
						!daoJuName.equals("九天玄女丹")&&!daoJuName.equals("百福具臻丹")){
				if(daoJuName.indexOf("角色装备")==-1){
				if(logger.isDebugEnabled()){
				logger.debug("接受到日志："+flow.toString());}
				if(position.indexOf("积分")!=-1||position.indexOf("積分")!=-1||position.indexOf("포인트")!=-1){  //포인트
					result=daoJu_mohu_queue.push(flow);
				}
				else{
				result=daoJuqueue.push(flow);
				}
			}
				}
			} else if (message instanceof TRANSACTION_LOG_REQ) {
				result=transactionqueue.push(message);
			} 
			else if (message instanceof ACCEPTTASK_LOG_REQ) {
				ACCEPTTASK_LOG_REQ req = (ACCEPTTASK_LOG_REQ) message;
				AcceptTaskFlow flow = req.getAcceptTaskFlow();
				String fenqu=flow.getFenQu();
				String taskType=flow.getTaskType();
				
				if("主线".equals(taskType)&&("竹林雅居".equals(fenqu)||"玄星云海".equals(fenqu)))
				{
					result=taskAcceptqueue.push(flow);
				}
				
				//result=taskAcceptqueue.push(flow);
//				//result= doAcceptTaskFlow(message);
//				//result=true;
			}
			else if (message instanceof FINISHTASK_LOG_REQ) {
				FINISHTASK_LOG_REQ req = (FINISHTASK_LOG_REQ) message;
				FinishTaskFlow flow = req.getFinishTaskFlow();
				String fenqu=flow.getFenQu();
				if("竹林雅居".equals(fenqu)||"玄星云海".equals(fenqu))
				{
				result=taskFinishqueue.push(flow);
				}
				//result=taskFinishqueue.push(flow);
				
//				//result= doFinishTaskFlow(message);
//				//result=true;
			}
			
//			else if (message instanceof ACCEPTHUODONGINFO_LOG_REQ) {
//				result=acceptHuoDonginfoqueue.push(message);
//				//result= doAcceptHuoDonginfoFlow(message);
//			} else if (message instanceof FINISHHUODONGINFO_LOG_REQ) {
//				result=finishHuoDonginfoqueue.push(message);
//				//result= doFinishHuoDonginfoFlow(message);
//			}
			
			else if (message instanceof GAMECHONGZHI_LOG_REQ) {
				GAMECHONGZHI_LOG_REQ req = (GAMECHONGZHI_LOG_REQ) message;
				GameChongZhiFlow flow = req.getGameChongZhiFlow();
				String currencyType=flow.getCurrencyType();
				String fenqu=flow.getFenQu();
				long now = System.currentTimeMillis();
				long time=flow.getTime();
				if(logger.isDebugEnabled()){
					logger.debug("[游戏币信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
					}
				
				if(shetname!=null&&shetname.indexOf("韩国")!=-1){time+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
				flow.setTime(time);
				
				//if(!"绑定银子".equals(currencyType)&&!"綁定銀子".equals(currencyType)&&fenqu.indexOf("pan")==-1){
				if(!"绑定银子".equals(currencyType)&&!"綁定銀子".equals(currencyType)){
				if(currencyType.indexOf("积分")==-1&&currencyType.indexOf("積分")==-1&&currencyType.indexOf("포인트")==-1){
				result=gameChongZhiqueue.push(flow);
				}else
				{
					gameChongZhiJingXiqueue.push(flow);
				}
				}
				
			} else if (message instanceof TRANSACTION_SPECIAL_LOG_REQ){
				result=transaction_Specialqueue.push(message);
			}else if(message instanceof YINZIKUCUN_LOG_REQ)
			{
				result=yinZiKunCuqueue.push(message);
			}else if(message instanceof LIBAO_LOG_REQ)
			{
				result=liBaoqueue.push(message);
			}else if(message instanceof TRANSACTION_FACE_LOG_REQ)
			{
				result=transaction_Facequeue.push(message);
			}
			else if(message instanceof TRANSFER_PLATFORM_LOG_REQ)
			{
				transfer_PlatFormqueue.push(message);
			}
			else if(message instanceof BATTLE_PLAYERSTAT_LOG_REQ)
			{
				battle_PlayerStatqueue.push(message);
			}
			else if(message instanceof BATTLE_COSTTIME_LOG_REQ)
			{
				battle_costTimeFlowqueue.push(message);
			}
			else if(message instanceof BATTLE_TEAMSTAT_LOG_REQ)
			{
				battle_TeamStatFlowqueue.push(message);
			}
			else if(message instanceof FUMO_LOG_REQ)
			{
				fuMoFlowqueue.push(message);
			}else if(message instanceof NPC_LOG_REQ)
			{
				npcinfoFlowqueue.push(message);
			}
			
			
		return result;
	}
	

	private boolean doFinishHuoDonginfoFlow(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		FINISHHUODONGINFO_LOG_REQ req = (FINISHHUODONGINFO_LOG_REQ) message;
		FinishHuoDonginfoFlow flow = req.getFinishHuoDonginfoFlow();
		if(logger.isDebugEnabled()){
		logger.info("[完成活动信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		}
		String fenQu=flow.getFenQu();
		Integer getDaoJu=flow.getGetDaoJu();
		Integer getWuPin=flow.getGetWuPin();
		Integer getYouXiBi=flow.getGetYOuXiBi();
		String status=flow.getStatus();
		String taskName=flow.getTaskName();
		String userName=flow.getUserName();
		String award=flow.getAward();
		String jixing=flow.getJixing();
		
		String sql="select * from stat_huodong_info t " +
				" where t.username='"+userName+"' and t.taskname='"+taskName+"'";
		List<HuoDonginfo> HuoDonginfoList=huoDonginfoManager.getBySql(sql);
		if(HuoDonginfoList!=null&&HuoDonginfoList.size()>0){
			HuoDonginfo huoDonginfo= HuoDonginfoList.get(0);
			huoDonginfo.setStatus(status);
			huoDonginfo.setGetDaoJu(getDaoJu);
			huoDonginfo.setGetWuPin(getWuPin);
			huoDonginfo.setGetYOuXiBi(getYouXiBi);
			huoDonginfo.setAward(award);
			huoDonginfo.setJixing(jixing);
			result=huoDonginfoManager.update(huoDonginfo);
		}else{
			result=true;
		}
		return result;
	}

	private boolean doFinishTaskFlow(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		FINISHTASK_LOG_REQ req = (FINISHTASK_LOG_REQ) message;
		FinishTaskFlow flow = req.getFinishTaskFlow();
		if(logger.isDebugEnabled()){
		logger.debug("[完成任务信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		}
		String fenQu=flow.getFenQu();
		Integer getDaoJu=flow.getGetDaoJu();
		Integer getWuPin=flow.getGetWuPin();
		Integer getYouXiBi=flow.getGetYOuXiBi();
		String status=flow.getStatus();
		String taskName=flow.getTaskName();
		String userName=flow.getUserName();
		String award=flow.getAward();
		
		String sql="select * from stat_task_info t " +
				" where t.username='"+userName+"' and t.taskname='"+taskName+"'";
		
		//logger.info("[完成任务信息上报] " + flow.toLogString() + " [队列:" + queue.elementNum() + "] [sql:"+sql+"] [" + (System.currentTimeMillis() - now) + "ms]");
		
		List<Taskinfo> taskinfoList=taskinfoManager.getBySql(sql);
		if(taskinfoList!=null&&taskinfoList.size()>0){
			Taskinfo taskinfo= taskinfoList.get(0);
			taskinfo.setStatus(status);
			taskinfo.setGetDaoJu(getDaoJu);
			taskinfo.setGetWuPin(getWuPin);
			taskinfo.setGetYOuXiBi(getYouXiBi);
			taskinfo.setAward(award);
			result=taskinfoManager.update(taskinfo);
		}
		return result;
	}

	private boolean  doAcceptHuoDonginfoFlow(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		ACCEPTHUODONGINFO_LOG_REQ req = (ACCEPTHUODONGINFO_LOG_REQ) message;
		AcceptHuoDonginfoFlow flow = req.getAcceptHuoDonginfoFlow();
		if(logger.isDebugEnabled()){
		logger.debug("[接受活动信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		}
		Long createDate=flow.getCreateDate();
		String fenQu=flow.getFenQu();
		String gameLevel=flow.getGameLevel();
		String taskName=flow.getTaskName();
		String taskType=flow.getTaskType();
		String userName=flow.getUserName();
		String jixing=flow.getJixing();
		
		HuoDonginfo huoDonginfo=new HuoDonginfo();
		huoDonginfo.setCreateDate(new Date(createDate));
		huoDonginfo.setFenQu(fenQu);
		huoDonginfo.setGameLevel(gameLevel);
		huoDonginfo.setGetDaoJu(0);
		
		huoDonginfo.setGetWuPin(0);
		huoDonginfo.setGetYOuXiBi(0);
		huoDonginfo.setStatus("接受");
		huoDonginfo.setTaskName(taskName);
		
		huoDonginfo.setTaskType(taskType);
		huoDonginfo.setUserName(userName);
		huoDonginfo.setJixing(jixing);
		
		HuoDonginfo resulthuoDonginfo=huoDonginfoManager.add(huoDonginfo);
		if(resulthuoDonginfo!=null){ result=true;}
		return result;
	}

//	private boolean doAcceptTaskFlow(RequestMessage message) {
//		boolean result=false;
//		long now = System.currentTimeMillis();
//		ACCEPTTASK_LOG_REQ req = (ACCEPTTASK_LOG_REQ) message;
//		AcceptTaskFlow flow = req.getAcceptTaskFlow();
//		if(logger.isDebugEnabled()){
//		logger.debug("[接受任务信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
//		}
//		Long createDate=flow.getCreateDate();
//		String fenQu=flow.getFenQu();
//		String gameLevel=flow.getGameLevel();
//		String taskName=flow.getTaskName();
//		String taskType=flow.getTaskType();
//		String userName=flow.getUserName();
//		
//		Taskinfo taskinfo=new Taskinfo();
//		taskinfo.setCreateDate(new Date(createDate));
//		taskinfo.setFenQu(fenQu);
//		taskinfo.setGameLevel(gameLevel);
//		taskinfo.setGetDaoJu(0);
//		
//		taskinfo.setGetWuPin(0);
//		taskinfo.setGetYOuXiBi(0);
//		taskinfo.setStatus("接受");
//		taskinfo.setTaskName(taskName);
//		
//		taskinfo.setTaskType(taskType);
//		taskinfo.setUserName(userName);
//		Taskinfo returntaskinfo=taskinfoManager.add(taskinfo);
//		if(returntaskinfo!=null){
//			result=true;
//		}
//		return result;
//	}

	private boolean doCreatePlayer(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		CREATEPLAYER_LOG_REQ req = (CREATEPLAYER_LOG_REQ) message;
		CreatePlayerFlow flow = req.getCreatePlayerFlow();
		logger.debug("[玩家创建角色] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		
		Long createPlayerTime=flow.getCreatPlayerTime();
		if(shetname!=null&&shetname.indexOf("韩国")!=-1){createPlayerTime+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
		String game=flow.getGame();
		String playerName=flow.getPlayerName();
		String userName=flow.getUserName();
		String qudao=flow.getQudao();
		
		String sex=flow.getSex();
		String nation=flow.getNation();
		String fenQu=flow.getFenQu();
		
		//String sql_=" select * from stat_user u where u.name='"+userName+"' ";
		//List<User> userList_=userManager.getBySql(sql_);
		
		
		User user=null;
		List<User> userList_=userManager.getByUserName(userName);
		if(userList_!=null&&userList_.size()>0){	user=userList_.get(0);}

		User userNew=new User();
		if(user!=null)
		  {
			userNew.setRegistTime(user.getRegistTime());
			}else{
				userNew.setRegistTime(new Date(createPlayerTime));
				userNew.setName(userName);
				userNew.setGame(game);
				userNew.setPlayerName(playerName);
				if(createPlayerTime!=null){ userNew.setCreatPlayerTime(new Date(createPlayerTime));}
				userNew.setFenQu(fenQu);
				userNew.setSex(sex);
				userNew.setGuojia(nation);
			}
		
		User returnuser=userManager.add(userNew);
		if(returnuser!=null){result=true;}
		
		if(user!=null)
		{
			//记录渠道查询信息
			Long quDaoItem_id=0L;
			Long quDaoId=null;
			List quDaoList=new ArrayList();
			if(qudao==null||"".equals(qudao)){qudao="无渠道";}
			
			if(qudao!=null&&!"".equals(qudao)){
				quDaoList=channelDAO.findByKey(qudao);
				if(quDaoList.size()>0){
					Channel channel=(Channel)quDaoList.get(0);
					quDaoId=channel.getId();
				}
				else{
					Channel channel_temp=new Channel();
					channel_temp.setName(qudao);
					channel_temp.setKey(qudao);
					channelDAO.save(channel_temp);
					quDaoList=channelDAO.findByKey(qudao);
					Channel channel=(Channel)quDaoList.get(0);
					quDaoId=channel.getId();
				}
				
				//添加子渠道
				if(channelItemManager.getChannelItem(qudao)==null)
				{
					ChannelItem channelItem=new ChannelItem();
					channelItem.setChannelid(quDaoId);
					channelItem.setKey(qudao);
					channelItem.setName(qudao);
					channelItem.setCmode(0L);
					channelItem.setPrate(1F);
					channelItemManager.createChannelItem(channelItem);
				}
				quDaoItem_id=channelItemManager.getChannelItem(qudao).getId();
			}
			
			////记录渠道统计信息
			UserStat userStat=new UserStat();
			userStat.setChannel(quDaoId);
			userStat.setMobile(userName);
			userStat.setUsername(userName);
			userStat.setFirstlogintime(new Date(createPlayerTime));
			userStat.setRegtime(user.getRegistTime());
			userStat.setNowlevel(1L);
			userStat.setRegtype(new Long(UserStat.REGTYPE_NORMAL));
			userStat.setChannelitem(quDaoItem_id);
			UserStat returnuserStat=	userStatManager.createUserStat(userStat);
			if(returnuserStat!=null){result=true;}
		}
		return result;
	}

	private boolean doPayMoney_unRecord(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		PAYMONEYUPDATE_LOG_REQ req = (PAYMONEYUPDATE_LOG_REQ) message;
		PayMoneyUpGradeFlow flow = req.getPayMoneyUpGradeFlow();
		logger.info("[玩家支付费用升级版,不记录的充值] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		long date = flow.getDate();
		if(shetname!=null&&shetname.indexOf("韩国")!=-1){date+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
		String fenQu = flow.getFenQu();
		String game = flow.getGame();
		String level = flow.getLevel();
		String name = flow.getUserName();
		long payMoney = flow.getPayMoney();
		String type=flow.getType();
		String quDao=flow.getQuDao();
		String cardType=flow.getCardType();
		String jixing=flow.getJixing();
		String modelType=flow.getModelType();///设备型号
		String vip 	=flow.getVip();
		long registDate=flow.getRegistDate();
		String playName=flow.getPlayName();//角色名称
		String colum1=flow.getColum1();///备用字段
		String cost=flow.getCost();
		Long quDaoId=0L;
		
		if(quDao==null||"".equals(quDao)){quDao="无渠道";}
		
		// 记录充值记录
		ChongZhi chongZhi=new ChongZhi();
		chongZhi.setFenQu(fenQu);
		chongZhi.setGame(game);
		chongZhi.setGameLevel(level);
		chongZhi.setMoney(payMoney);
		chongZhi.setTime(new Date(date));
		chongZhi.setUserName(name);
		
		chongZhi.setType(type);
		chongZhi.setQuDao(quDao);
		chongZhi.setQuDaoId(quDaoId.toString());
		chongZhi.setCardType(cardType);
		chongZhi.setCost(cost);
		chongZhi.setJixing(jixing);
		chongZhi.setModelType(modelType);
		chongZhi.setVip(vip);
		chongZhi.setRegistDate(new Date(registDate));
		chongZhi.setPlayName(playName);
		chongZhi.setColum1(colum1);
		
		ChongZhi returnchongZhi=chongZhiManager.addUnRecorde(chongZhi);

		if(returnchongZhi!=null){
			result=true;
		}
		return result;
	}
	
	
	private boolean doPayMoneyUpDate(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		PAYMONEYUPDATE_LOG_REQ req = (PAYMONEYUPDATE_LOG_REQ) message;
		PayMoneyUpGradeFlow flow = req.getPayMoneyUpGradeFlow();
		logger.info("[玩家支付费用升级版] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		long date = flow.getDate();
		if(shetname!=null&&shetname.indexOf("韩国")!=-1){date+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
		String fenQu = flow.getFenQu();
		String game = flow.getGame();
		String level = flow.getLevel();
		String name = flow.getUserName();
		long payMoney = flow.getPayMoney();
		
		String type=flow.getType();
		String quDao=flow.getQuDao();
		String cardType=flow.getCardType();
		String jixing=flow.getJixing();
		
		
		//String modelType=flow.getModelType();///设备型号
		String modelType="";///设备型号
		String vip 	=flow.getVip();
		long registDate=flow.getRegistDate();
		String playName=flow.getPlayName();//角色名称
		String colum1=flow.getColum1();///备用字段
		
		
		
		
		String cost=flow.getCost();
		if("189store_MIESHI".equals(quDao)){cost="0";}else{
			cost=PayMoneyCost.getCost(cardType, type, payMoney);
		}
		
		if("天猫商城".equals(type)){
			quDao="APP_TMALL";
		}
		
		Long quDaoItem_id=0L;
		Long quDaoId=null;
		List quDaoList=new ArrayList();
		

		if(quDao==null||"".equals(quDao)){quDao="无渠道";}
		
		if(quDao!=null&&!"".equals(quDao)){
			quDaoList=channelDAO.findByKey(quDao);
			if(quDaoList.size()>0){
				Channel channel=(Channel)quDaoList.get(0);
				quDaoId=channel.getId();
			}
			else{
				Channel channel_=new Channel();
				channel_.setName(quDao);
				channel_.setKey(quDao);
				channelDAO.save(channel_);
				quDaoList=channelDAO.findByKey(quDao);
				Channel channel=(Channel)quDaoList.get(0);
				quDaoId=channel.getId();
			}
			//添加子渠道
			if(channelItemManager.getChannelItem(quDao)==null)
			{
				ChannelItem channelItem=new ChannelItem();
				channelItem.setChannelid(quDaoId);
				channelItem.setKey(quDao);
				channelItem.setName(quDao);
				channelItem.setCmode(0L);
				channelItem.setPrate(1F);
				channelItemManager.createChannelItem(channelItem);
			}
			ChannelItem channelItem=channelItemManager.getChannelItem(quDao);
			quDaoItem_id=channelItem.getId();
			
		}
		//记录渠道统计信息
		SavingYuanbao savingYuanbao=new SavingYuanbao();
		
		
		savingYuanbao.setCreatetime(new Date(date));
		savingYuanbao.setRmb(payMoney);
		savingYuanbao.setChannelid(quDaoId);
		savingYuanbao.setChannelitemid(quDaoItem_id);
		savingYuanbao.setSucc(1L);
		savingYuanbao.setPlatformid(0L);
		if(cost==null||"".equals(cost.trim())){cost="0";}
		savingYuanbao.setCost((long)Float.parseFloat(cost));
		SavingYuanbao returnsavingYuanbao =savingYuanbaoManager.createSavingYuanbao(savingYuanbao);
		if(returnsavingYuanbao!=null){ result=true; }
		
		// 记录充值记录
		ChongZhi chongZhi=new ChongZhi();
		chongZhi.setFenQu(fenQu);
		chongZhi.setGame(game);
		chongZhi.setGameLevel(level);
		chongZhi.setMoney(payMoney);
		chongZhi.setTime(new Date(date));
		chongZhi.setUserName(name);
		
		chongZhi.setType(type);
		chongZhi.setQuDao(quDao);
		chongZhi.setQuDaoId(quDaoId.toString());
		chongZhi.setCardType(cardType);
		chongZhi.setCost(cost);
		chongZhi.setJixing(jixing);
		
		chongZhi.setModelType(modelType);
		chongZhi.setVip(vip);
		chongZhi.setRegistDate(new Date(registDate));
		chongZhi.setPlayName(playName);
		chongZhi.setColum1(colum1);
		
		ChongZhi returnchongZhi=chongZhiManager.add(chongZhi);
		
//////////////////给盛大发送充值日志   start////////////////////
		try{
		if(quDao.indexOf("SHENGDA")!=-1){
			long tt=System.currentTimeMillis();
			ShengDaCallManager.getInstance().cardSaving(chongZhi);
			logger.info("给盛大发送充值日志: [ok]"+chongZhi.toString()+"cost:"+(System.currentTimeMillis()-tt));
		}
		}catch(Exception e){
			logger.error("给盛大发送充值日志: [fail]"+chongZhi.toString()+e.getMessage(),e);
		}
		
        ////////////////////给盛大发送充值日志   end////////////////////
		if(returnchongZhi!=null){
			result=true;
		}
		return result;
	}
	
	
	
	
	private boolean doPayMoney(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		PAYMONEY_LOG_REQ req = (PAYMONEY_LOG_REQ) message;
		PayMoneyFlow flow = req.getPayMoneyFlow();
		logger.info("[玩家支付费用] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		long date = flow.getDate();
		if(shetname!=null&&shetname.indexOf("韩国")!=-1){date+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
		String fenQu = flow.getFenQu();
		String game = flow.getGame();
		String level = flow.getLevel();
		String name = flow.getUserName();
		long payMoney = flow.getPayMoney();
		
		String type=flow.getType();
		String quDao=flow.getQuDao();
		String cardType=flow.getCardType();
		String jixing=flow.getJixing();
		
		String cost=flow.getCost();
		if("189store_MIESHI".equals(quDao)){cost="0";}else{
			cost=PayMoneyCost.getCost(cardType, type, payMoney);
		}
		
		if("天猫商城".equals(type)){
			quDao="APP_TMALL";
		}
		
		Long quDaoItem_id=0L;
		Long quDaoId=null;
		List quDaoList=new ArrayList();
		

		if(quDao==null||"".equals(quDao)){quDao="无渠道";}
		
		if(quDao!=null&&!"".equals(quDao)){
			quDaoList=channelDAO.findByKey(quDao);
			if(quDaoList.size()>0){
				Channel channel=(Channel)quDaoList.get(0);
				quDaoId=channel.getId();
			}
			else{
				Channel channel_=new Channel();
				channel_.setName(quDao);
				channel_.setKey(quDao);
				channelDAO.save(channel_);
				quDaoList=channelDAO.findByKey(quDao);
				Channel channel=(Channel)quDaoList.get(0);
				quDaoId=channel.getId();
			}
			//添加子渠道
			if(channelItemManager.getChannelItem(quDao)==null)
			{
				ChannelItem channelItem=new ChannelItem();
				channelItem.setChannelid(quDaoId);
				channelItem.setKey(quDao);
				channelItem.setName(quDao);
				channelItem.setCmode(0L);
				channelItem.setPrate(1F);
				channelItemManager.createChannelItem(channelItem);
			}
			ChannelItem channelItem=channelItemManager.getChannelItem(quDao);
			quDaoItem_id=channelItem.getId();
			
		}
		//记录渠道统计信息
		SavingYuanbao savingYuanbao=new SavingYuanbao();
		
		
		savingYuanbao.setCreatetime(new Date(date));
		savingYuanbao.setRmb(payMoney);
		savingYuanbao.setChannelid(quDaoId);
		savingYuanbao.setChannelitemid(quDaoItem_id);
		savingYuanbao.setSucc(1L);
		savingYuanbao.setPlatformid(0L);
		if(cost==null||"".equals(cost.trim())){cost="0";}
		savingYuanbao.setCost((long)Float.parseFloat(cost));
		SavingYuanbao returnsavingYuanbao =savingYuanbaoManager.createSavingYuanbao(savingYuanbao);
		if(returnsavingYuanbao!=null){ result=true; }
		
		// 记录充值记录
		ChongZhi chongZhi=new ChongZhi();
		chongZhi.setFenQu(fenQu);
		chongZhi.setGame(game);
		chongZhi.setGameLevel(level);
		chongZhi.setMoney(payMoney);
		chongZhi.setTime(new Date(date));
		chongZhi.setUserName(name);
		
		chongZhi.setType(type);
		chongZhi.setQuDao(quDao);
		chongZhi.setQuDaoId(quDaoId.toString());
		chongZhi.setCardType(cardType);
		chongZhi.setCost(cost);
		chongZhi.setJixing(jixing);
		ChongZhi returnchongZhi=chongZhiManager.add(chongZhi);
		
//////////////////给盛大发送充值日志   start////////////////////
		try{
		if(quDao.indexOf("SHENGDA")!=-1){
			long tt=System.currentTimeMillis();
			ShengDaCallManager.getInstance().cardSaving(chongZhi);
			logger.info("给盛大发送充值日志: [ok]"+chongZhi.toString()+"cost:"+(System.currentTimeMillis()-tt));
		}
		}catch(Exception e){
			logger.error("给盛大发送充值日志: [fail]"+chongZhi.toString()+e.getMessage(),e);
		}
		
        ////////////////////给盛大发送充值日志   end////////////////////
		if(returnchongZhi!=null){
			result=true;
		}
		return result;
	}

	private boolean doDayChang(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		DAYCHENG_LOG_REQ req = (DAYCHENG_LOG_REQ) message;
		DayChangFlow flow = req.getDayChangFlow();
		if(logger.isDebugEnabled()){
		logger.debug("[日期变化] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		}
		
		 long date = flow.getDate();
         if(shetname!=null&&shetname.indexOf("韩国")!=-1){date+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
		
		String fenqu = flow.getFenQu();
		String game = flow.getGame();
		String level = flow.getLevel();
		String name = flow.getUserName();
		Long youXiBi=flow.getYouXiBi();
		Long yuanBaoCount=flow.getYuanBaoCount();
		String quDao=flow.getQuDao();
		String jixing=flow.getJixing();
		String zhiye   =flow.getZhiye();
		String column1 =flow.getColumn1();
		String column2 =flow.getColumn2();
		
		long onLineTime_1 = flow.getOnLineTime();
		PlayGame playGame = null;
//		String sql = "select *  from stat_playgame t where t.username = '" + flow.getUserName()+ "' " + "and trunc(t.enterdate) =to_date('"
//				+ sf.format(new Date(date)) + "','YYYY-MM-DD')  and t.fenqu = '" + flow.getFenQu() + "'  ";
		
		String sql = "select *  from stat_playgame t where t.username = '" + flow.getUserName()+ "' " 
		+ "and t.enterdate  between to_date('"+ sf.format(new Date(date)) + " 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
				" and to_date('"+ sf.format(new Date(date)) + " 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and t.fenqu = '" + flow.getFenQu() + "'  ";

		List<PlayGame> list = playGameManager.getBySql(sql);
		if (list != null && list.size() != 0) {
			playGame = list.get(0);
		}
		if (playGame != null) {
			Long onlineTime = playGame.getOnLineTime();
				playGame.setOnLineTime(onLineTime_1+onlineTime);
			if (level != null) {
				long level_temp = Long.parseLong(level);
				long maxlevel = playGame.getMaxLevel();
				long minlevel = playGame.getMinLevel();
				if (level_temp > maxlevel) {
					playGame.setMaxLevel(Long.parseLong(level));
				}
				if (level_temp < minlevel) {
					playGame.setMinLevel(Long.parseLong(level));
				}
			}
			if(youXiBi!=null){playGame.setYouXiBi(youXiBi);}
			if(yuanBaoCount!=null){playGame.setYuanBaoCount(yuanBaoCount);}
			playGame.setQuDao(quDao);
			playGame.setJixing(jixing);
			
			result= playGameManager.update(playGame);
		}
		PlayGame playGame_nextDay = new PlayGame();
		playGame_nextDay.setFenQu(fenqu);
		playGame_nextDay.setEnterDate(new Date(date + 24 * 3600 * 1000));
		playGame_nextDay.setUserName(name);
		playGame_nextDay.setGame(game);
		playGame_nextDay.setEnterTimes(1L);
		playGame_nextDay.setOnLineTime(0L);
		if (level != null) {
			playGame_nextDay.setMaxLevel(Long.parseLong(level));
			playGame_nextDay.setMinLevel(Long.parseLong(level));
		}
		playGame_nextDay.setQuDao(quDao);
		playGame_nextDay.setJixing(jixing);
		
		playGame_nextDay.setZhiye(zhiye);
		playGame_nextDay.setColumn1(column1);
		playGame_nextDay.setColumn2(column2);
		
		PlayGame returnplaygeme=playGameManager.add(playGame_nextDay);
		if(returnplaygeme!=null){result= true ;}
		
		return result;
	}

	private boolean doLogOutGame(RequestMessage message) {

		boolean result=false;
		long now = System.currentTimeMillis();
		LOGOUTGAME_LOG_REQ req = (LOGOUTGAME_LOG_REQ) message;
		LogOutGameFlow flow = req.getLogOutGameFlow();
		PlayGame playGame = null;
		
		 long date = flow.getDate();
         if(shetname!=null&&shetname.indexOf("韩国")!=-1){date+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
		
		
		String fenQu = flow.getFenQu();
		String game = flow.getGame();
		String level = flow.getLevel();
		String name = flow.getUserName();
		Long youXiBi=flow.getYouXiBi();
		Long yaunBaoCount=flow.getYuanBaoCount();
		long onLineTime_1 = flow.getOnLineTime();
		String quDao   = flow.getQuDao();
		String jixing  = flow.getJixing();
		

	
//		String sql = "select *  from stat_playgame t where t.username = '" +name + "' " + "and  trunc(t.enterdate) =to_date('"
//				+ sf.format(new Date(date)) + "','YYYY-MM-DD')  and t.fenqu = '" + flow.getFenQu() + "' ";
		
		
		String sql = "select *  from stat_playgame t where t.username = '" +name + "' and  t.enterdate " +
				" between to_date('"+ sf.format(new Date(date)) + " 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
						" and to_date('"+ sf.format(new Date(date)) + " 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
						" and t.fenqu = '" + flow.getFenQu() + "' ";
		
		if(logger.isDebugEnabled()){
		logger.debug("[玩家退出游戏] " + flow.toString() + " [队列:" + queue.elementNum()+ "] [" + (System.currentTimeMillis() - now) +"ms]");
		}
		List<PlayGame> list = playGameManager.getBySql(sql);
		if (list != null && list.size() != 0) {
			playGame = list.get(0);
		}
		if (playGame != null) {
			Long onlineTime = playGame.getOnLineTime();
				playGame.setOnLineTime(onLineTime_1+onlineTime);
			if (level != null) {
				long level_temp = Long.parseLong(level);
				long maxlevel = playGame.getMaxLevel();
				long minlevel = playGame.getMinLevel();
				if (level_temp > maxlevel) {
					playGame.setMaxLevel(Long.parseLong(level));
				}
				if (level_temp < minlevel) {
					playGame.setMinLevel(Long.parseLong(level));
				}
			}
			if(youXiBi!=null){playGame.setYouXiBi(youXiBi);}
			if(yaunBaoCount!=null){playGame.setYuanBaoCount(yaunBaoCount);}
			playGame.setQuDao(quDao);
			playGame.setJixing(jixing);
	
			result= playGameManager.update(playGame);
		}else{
			playGame=new PlayGame();
			playGame.setFenQu(fenQu);
			playGame.setGame(game);
			playGame.setUserName(name);
			
			playGame.setEnterDate(new Date(date));
			playGame.setOnLineTime(onLineTime_1);
			playGame.setMaxLevel(Long.parseLong(level));
			playGame.setMinLevel(Long.parseLong(level));
			if(youXiBi!=null){playGame.setYouXiBi(youXiBi);}
			if(yaunBaoCount!=null){playGame.setYuanBaoCount(yaunBaoCount);}
			playGame.setQuDao(quDao);
			playGame.setJixing(jixing);
			
			playGame=playGameManager.add(playGame);
			if(playGame!=null){result=true;}
		}
		return result;
	}

	private boolean doEnterGame(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		ENTERGAME_LOG_REQ req = (ENTERGAME_LOG_REQ) message;
		EnterGameFlow flow = req.getEnterGameFlow();
		
		//System.out.println("-----------------------------------"+flow.toString());
		PlayGame playGame = null;
		
		 long date = flow.getDate();
         if(shetname!=null&&shetname.indexOf("韩国")!=-1){date+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
		
		String fenQu  = flow.getFenQu();
		String game   = flow.getGame();
		String level  = flow.getLevel();
		String name   = flow.getUserName();
		String quDao  = flow.getQuDao();
		String jixing = flow.getJixing();
		
		String zhiye   = flow.getZhiye();
		String column1 = flow.getColumn1();
		String column2 = flow.getColumn2();
		
		
		
		long selecttime = System.currentTimeMillis();
		
//		String sql = "select *  from stat_playgame t where t.username = '" + flow.getUserName() + "' " + "and t.enterdate =to_date('"
//				+ sf.format(new Date(date)) + "','YYYY-MM-DD')  and t.fenqu = '" +flow.getFenQu() + "'  ";
		
		String sql = "select *  from stat_playgame t where t.username = '" + flow.getUserName() + "' " 
		+ " and t.enterdate between to_date('"+ sf.format(new Date(date)) + " 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
				" and to_date('"+ sf.format(new Date(date)) + " 23:59:59','YYYY-MM-DD hh24:mi:ss')  " +
			" and t.fenqu = '" +flow.getFenQu() + "'  ";
		
		List<PlayGame> list = playGameManager.getBySql(sql);
		if(logger.isDebugEnabled()){
		logger.debug("[玩家进入游戏]"+ flow.toString()+" [查询数据库用时:] [" + (System.currentTimeMillis() - selecttime) + "ms]");
		}
		if (list != null && list.size() != 0) {
			playGame = list.get(0);
		}
		if (playGame == null) {
			playGame = new PlayGame();
			playGame.setFenQu(fenQu);
			playGame.setEnterDate(new Date(date));
			playGame.setUserName(name);
			playGame.setGame(game);
			playGame.setEnterTimes(1L);
			if (level != null) {
				playGame.setMaxLevel(Long.parseLong(level));
				playGame.setMinLevel(Long.parseLong(level));
			} else {
				playGame.setMaxLevel(1L);
				playGame.setMinLevel(1L);
			}
			playGame.setQuDao(quDao);
			playGame.setJixing(jixing);
			
			playGame.setZhiye(zhiye);
			playGame.setColumn1(column1);
			playGame.setColumn2(column2);
			
			PlayGame resultplayGame =playGameManager.add(playGame);
			if(resultplayGame!=null){ result=true; }
		} else {
			playGame.setEnterTimes(playGame.getEnterTimes() + 1L);
			if (level != null) {
				long level_temp = Long.parseLong(level);
				long maxlevel = playGame.getMaxLevel();
				long minlevel = playGame.getMinLevel();
				playGame.setQuDao(quDao);
				playGame.setJixing(jixing);
				
				playGame.setZhiye(zhiye);
				playGame.setColumn1(column1);
				playGame.setColumn2(column2);
				
				if (level_temp > maxlevel) {
					playGame.setMaxLevel(Long.parseLong(level));
				}
				if (level_temp < minlevel) {
					playGame.setMinLevel(Long.parseLong(level));
				}
			}
			result= playGameManager.update(playGame);
		}
		return result;
	}

	private boolean doUserRegist(RequestMessage message) {
		boolean result=false;
		USEREGIST_LOG_REQ req = (USEREGIST_LOG_REQ) message;
		long now = System.currentTimeMillis();
		UserRegistFlow flow = req.getUserRegistFlow();
		logger.info("[用户注册] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");

		User user = new User();
		user.setDiDian(flow.getDidian());
		user.setImei(flow.getEmei());
		user.setGame(flow.getGame());
		user.setHaoMa(flow.getHaoma());
		user.setUuid(flow.getHaoma());
		user.setJiXing(flow.getJixing());
		user.setName(flow.getUserName());
		String quDao=flow.getQudao();
		
		if(quDao==null||"".equals(quDao)){quDao="无渠道";}
		List channelList=channelDAO.findByKey(quDao);
		Channel channel=null;
		Long quDaoId=null;
		if(channelList!=null&&channelList.size()>0)
		{
			channel=(Channel)channelList.get(0);
			quDaoId=channel.getId();
			
		}else{
			Channel channel_temp=new Channel();
			channel_temp.setName(quDao);
			channel_temp.setKey(quDao);
			channelDAO.save(channel_temp);
			channelList=channelDAO.findByKey(quDao);
			channel=(Channel)channelList.get(0);
			quDaoId=channel.getId();
		}
		
		//添加子渠道
		if(channelItemManager.getChannelItem(quDao)==null)
		{
			ChannelItem channelItem=new ChannelItem();
			channelItem.setChannelid(quDaoId);
			channelItem.setKey(quDao);
			channelItem.setName(quDao);
			channelItem.setCmode(0L);
			channelItem.setPrate(1F);
			channelItemManager.createChannelItem(channelItem);
		}
		user.setQuDaoId(quDaoId.toString());
		user.setQuDao(quDao);
		 long registtime= flow.getRegisttime();
         if(shetname!=null&&shetname.indexOf("韩国")!=-1){registtime+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
		java.util.Date date = new java.util.Date(registtime);
		user.setRegistTime(date);
		
//		if(flow.getCreatPlayerTime()>30*365*24*60*60*1000){
//		user.setCreatPlayerTime(new Date(flow.getCreatPlayerTime()));
//		}
		user.setPlayerName(flow.getPlayerName());
		
		String sql=" select * from stat_user u where u.name='"+flow.getUserName()+"'";
		List<User> userList=userManager.getBySql(sql);
		if(userList==null||userList.size()<=0){
			
			User returnUser=userManager.add(user);
			if(returnUser!=null){
				result=true;
			}
		}else{
			result=true;
		}
		return result;
	}

	@Override
	public void run() {
		
		while (Thread.currentThread().isInterrupted() == false) {
			try {
				while(!queue.isEmpty()) {
					RequestMessage req = (RequestMessage) queue.pop();
					if(req != null){
						if(!handle(req))
						{
							//queue.push(req);
						}
					}
				}
				if(queue.isEmpty()){
					 synchronized(this){
						 wait(100);
					 }
				}
			} catch (Exception e) {
				logger.error("[reqest处理错误] [队列:" + queue.elementNum() + "] ",e);
				
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + queue.elementNum() + "]");
	}
	}

	public String getQueueFile() {
		return queueFile;
	}

	public void setQueueFile(String queueFile) {
		this.queueFile = queueFile;
	}

	public AdvancedFilePersistentQueue getQueue() {
		return queue;
	}

	public void setQueue(AdvancedFilePersistentQueue queue) {
		this.queue = queue;
	}

	public UserManagerImpl getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManagerImpl userManager) {
		this.userManager = userManager;
	}

	public PlayGameManagerImpl getPlayGameManager() {
		return playGameManager;
	}

	public void setPlayGameManager(PlayGameManagerImpl playGameManager) {
		this.playGameManager = playGameManager;
	}

	public OnLineUsersCountManagerImpl getOnLineUsersCountManager() {
		return onLineUsersCountManager;
	}

	public void setOnLineUsersCountManager(OnLineUsersCountManagerImpl onLineUsersCountManager) {
		this.onLineUsersCountManager = onLineUsersCountManager;
	}

	public ChongZhiManagerImpl getChongZhiManager() {
		return chongZhiManager;
	}

	public void setChongZhiManager(ChongZhiManagerImpl chongZhiManager) {
		this.chongZhiManager = chongZhiManager;
	}

	public ChannelDAO getChannelDAO() {
		return channelDAO;
	}

	public void setChannelDAO(ChannelDAO channelDAO) {
		this.channelDAO = channelDAO;
	}

	public ChannelItemManager getChannelItemManager() {
		return channelItemManager;
	}

	public void setChannelItemManager(ChannelItemManager channelItemManager) {
		this.channelItemManager = channelItemManager;
	}

	public SavingYuanbaoManager getSavingYuanbaoManager() {
		return savingYuanbaoManager;
	}

	public void setSavingYuanbaoManager(SavingYuanbaoManager savingYuanbaoManager) {
		this.savingYuanbaoManager = savingYuanbaoManager;
	}

	public UserStatManager getUserStatManager() {
		return userStatManager;
	}

	public void setUserStatManager(UserStatManager userStatManager) {
		this.userStatManager = userStatManager;
	}

	public TransactionManagerImpl getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(TransactionManagerImpl transactionManager) {
		this.transactionManager = transactionManager;
	}

	public HuoDonginfoManagerImpl getHuoDonginfoManager() {
		return huoDonginfoManager;
	}

	public void setHuoDonginfoManager(HuoDonginfoManagerImpl huoDonginfoManager) {
		this.huoDonginfoManager = huoDonginfoManager;
	}

	public TaskinfoManagerImpl getTaskinfoManager() {
		return taskinfoManager;
	}

	public void setTaskinfoManager(TaskinfoManagerImpl taskinfoManager) {
		this.taskinfoManager = taskinfoManager;
	}


	public DaoJuService getDaoJuService() {
		return daoJuService;
	}

	public void setDaoJuService(DaoJuService daoJuService) {
		this.daoJuService = daoJuService;
	}

	public GameChongZhiService getGameChongZhiService() {
		return gameChongZhiService;
	}

	public void setGameChongZhiService(GameChongZhiService gameChongZhiService) {
		this.gameChongZhiService = gameChongZhiService;
	}

	public DaoJuService_merge getDaoJuService_merge() {
		return daoJuService_merge;
	}

	public void setDaoJuService_merge(DaoJuService_merge daoJuServiceMerge) {
		daoJuService_merge = daoJuServiceMerge;
	}

	public GameChongZhiService_merge getGameChongZhiService_merge() {
		return gameChongZhiService_merge;
	}

	public void setGameChongZhiService_merge(GameChongZhiService_merge gameChongZhiServiceMerge) {
		gameChongZhiService_merge = gameChongZhiServiceMerge;
	}

	public TaskAcceptService getTaskAcceptService() {
		return taskAcceptService;
	}

	public void setTaskAcceptService(TaskAcceptService taskAcceptService) {
		this.taskAcceptService = taskAcceptService;
	}

	public TaskFinishService getTaskFinishService() {
		return taskFinishService;
	}

	public void setTaskFinishService(TaskFinishService taskFinishService) {
		this.taskFinishService = taskFinishService;
	}

	public Task2AnalysisService getTask2AnalysisService() {
		return task2AnalysisService;
	}

	public void setTask2AnalysisService(Task2AnalysisService task2AnalysisService) {
		this.task2AnalysisService = task2AnalysisService;
	}

	public TaskAnalysisService_merge getTaskAnalysisService_merge() {
		return taskAnalysisService_merge;
	}

	public void setTaskAnalysisService_merge(TaskAnalysisService_merge taskAnalysisServiceMerge) {
		taskAnalysisService_merge = taskAnalysisServiceMerge;
	}

	public TaskFinish2AnalysisService getTaskFinish2AnalysisService() {
		return taskFinish2AnalysisService;
	}

	public void setTaskFinish2AnalysisService(TaskFinish2AnalysisService taskFinish2AnalysisService) {
		this.taskFinish2AnalysisService = taskFinish2AnalysisService;
	}

	public TaskFinishService_merge getTaskFinishService_merge() {
		return taskFinishService_merge;
	}

	public void setTaskFinishService_merge(TaskFinishService_merge taskFinishServiceMerge) {
		taskFinishService_merge = taskFinishServiceMerge;
	}

	public static AdvancedFilePersistentQueue getOnLineUsersqueue() {
		return onLineUsersqueue;
	}

	public static void setOnLineUsersqueue(AdvancedFilePersistentQueue onLineUsersqueue) {
		StatServerService.onLineUsersqueue = onLineUsersqueue;
	}

	public static AdvancedFilePersistentQueue getGameChongZhiqueue() {
		return gameChongZhiqueue;
	}

	public static void setGameChongZhiqueue(AdvancedFilePersistentQueue gameChongZhiqueue) {
		StatServerService.gameChongZhiqueue = gameChongZhiqueue;
	}

	public static AdvancedFilePersistentQueue getTaskAcceptqueue() {
		return taskAcceptqueue;
	}

	public static void setTaskAcceptqueue(AdvancedFilePersistentQueue taskAcceptqueue) {
		StatServerService.taskAcceptqueue = taskAcceptqueue;
	}

	public static AdvancedFilePersistentQueue getTaskFinishqueue() {
		return taskFinishqueue;
	}

	public static void setTaskFinishqueue(AdvancedFilePersistentQueue taskFinishqueue) {
		StatServerService.taskFinishqueue = taskFinishqueue;
	}

	public static AdvancedFilePersistentQueue getTaskAnalysisFinishqueue() {
		return taskAnalysisFinishqueue;
	}

	public static void setTaskAnalysisFinishqueue(AdvancedFilePersistentQueue taskAnalysisFinishqueue) {
		StatServerService.taskAnalysisFinishqueue = taskAnalysisFinishqueue;
	}

	public static AdvancedFilePersistentQueue getTaskAnalysisAcceptqueue() {
		return taskAnalysisAcceptqueue;
	}

	public static void setTaskAnalysisAcceptqueue(AdvancedFilePersistentQueue taskAnalysisAcceptqueue) {
		StatServerService.taskAnalysisAcceptqueue = taskAnalysisAcceptqueue;
	}

	public OnLineUsersService getOnLineUsersService() {
		return onLineUsersService;
	}

	public void setOnLineUsersService(OnLineUsersService onLineUsersService) {
		this.onLineUsersService = onLineUsersService;
	}

	public Transaction_SpecialManagerImpl getTransaction_SpecialManager() {
		return transaction_SpecialManager;
	}

	public void setTransaction_SpecialManager(Transaction_SpecialManagerImpl transactionSpecialManager) {
		transaction_SpecialManager = transactionSpecialManager;
	}

	public static AdvancedFilePersistentQueue getAcceptHuoDonginfoqueue() {
		return acceptHuoDonginfoqueue;
	}

	public static void setAcceptHuoDonginfoqueue(AdvancedFilePersistentQueue acceptHuoDonginfoqueue) {
		StatServerService.acceptHuoDonginfoqueue = acceptHuoDonginfoqueue;
	}

	public static AdvancedFilePersistentQueue getFinishHuoDonginfoqueue() {
		return finishHuoDonginfoqueue;
	}

	public static void setFinishHuoDonginfoqueue(AdvancedFilePersistentQueue finishHuoDonginfoqueue) {
		StatServerService.finishHuoDonginfoqueue = finishHuoDonginfoqueue;
	}

	public static AdvancedFilePersistentQueue getTransactionqueue() {
		return transactionqueue;
	}

	public static void setTransactionqueue(AdvancedFilePersistentQueue transactionqueue) {
		StatServerService.transactionqueue = transactionqueue;
	}

	public static AdvancedFilePersistentQueue getTransaction_Specialqueue() {
		return transaction_Specialqueue;
	}

	public static void setTransaction_Specialqueue(AdvancedFilePersistentQueue transactionSpecialqueue) {
		transaction_Specialqueue = transactionSpecialqueue;
	}

	public AcceptHuoDonginfoService getAcceptHuoDonginfoService() {
		return acceptHuoDonginfoService;
	}

	public void setAcceptHuoDonginfoService(AcceptHuoDonginfoService acceptHuoDonginfoService) {
		this.acceptHuoDonginfoService = acceptHuoDonginfoService;
	}

	public FinishHuoDonginfoService getFinishHuoDonginfoService() {
		return finishHuoDonginfoService;
	}

	public void setFinishHuoDonginfoService(FinishHuoDonginfoService finishHuoDonginfoService) {
		this.finishHuoDonginfoService = finishHuoDonginfoService;
	}

	public TransactionService getTransactionService() {
		return transactionService;
	}

	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	public Transaction_SpecialService getTransaction_SpecialService() {
		return transaction_SpecialService;
	}

	public void setTransaction_SpecialService(Transaction_SpecialService transactionSpecialService) {
		transaction_SpecialService = transactionSpecialService;
	}

	public static AdvancedFilePersistentQueue getDaoJuqueue() {
		return daoJuqueue;
	}

	public static void setDaoJuqueue(AdvancedFilePersistentQueue daoJuqueue) {
		StatServerService.daoJuqueue = daoJuqueue;
	}

	public static AdvancedFilePersistentQueue getYinZiKunCuqueue() {
		return yinZiKunCuqueue;
	}

	public static void setYinZiKunCuqueue(AdvancedFilePersistentQueue yinZiKunCuqueue) {
		StatServerService.yinZiKunCuqueue = yinZiKunCuqueue;
	}

	public YinZiKuCunService getYinZiKuCunService() {
		return yinZiKuCunService;
	}

	public void setYinZiKuCunService(YinZiKuCunService yinZiKuCunService) {
		this.yinZiKuCunService = yinZiKuCunService;
	}

	public DaoJu_MoHuService getDaoJu_MoHuService() {
		return daoJu_MoHuService;
	}

	public void setDaoJu_MoHuService(DaoJu_MoHuService daoJuMoHuService) {
		daoJu_MoHuService = daoJuMoHuService;
	}

	public DaoJuMoHuService_merge getDaoJuMoHuService_merge() {
		return daoJuMoHuService_merge;
	}

	public void setDaoJuMoHuService_merge(DaoJuMoHuService_merge daoJuMoHuServiceMerge) {
		daoJuMoHuService_merge = daoJuMoHuServiceMerge;
	}

	public static AdvancedFilePersistentQueue getDaoJu_mohu_queue() {
		return daoJu_mohu_queue;
	}

	public static void setDaoJu_mohu_queue(AdvancedFilePersistentQueue daoJuMohuQueue) {
		daoJu_mohu_queue = daoJuMohuQueue;
	}

	public static AdvancedFilePersistentQueue getGameChongZhiJingXiqueue() {
		return gameChongZhiJingXiqueue;
	}

	public static void setGameChongZhiJingXiqueue(AdvancedFilePersistentQueue gameChongZhiJingXiqueue) {
		StatServerService.gameChongZhiJingXiqueue = gameChongZhiJingXiqueue;
	}

	public GameChongZhi_JingXiService getGameChongZhi_JingXiService() {
		return gameChongZhi_JingXiService;
	}

	public void setGameChongZhi_JingXiService(GameChongZhi_JingXiService gameChongZhiJingXiService) {
		gameChongZhi_JingXiService = gameChongZhiJingXiService;
	}

	public LiBaoqueueService getLiBaoqueueService() {
		return liBaoqueueService;
	}

	public void setLiBaoqueueService(LiBaoqueueService liBaoqueueService) {
		this.liBaoqueueService = liBaoqueueService;
	}

	public static AdvancedFilePersistentQueue getLiBaoqueue() {
		return liBaoqueue;
	}

	public static void setLiBaoqueue(AdvancedFilePersistentQueue liBaoqueue) {
		StatServerService.liBaoqueue = liBaoqueue;
	}

	public Transaction_FaceService getTransaction_FaceService() {
		return transaction_FaceService;
	}

	public void setTransaction_FaceService(Transaction_FaceService transactionFaceService) {
		transaction_FaceService = transactionFaceService;
	}

	public static AdvancedFilePersistentQueue getTransaction_Facequeue() {
		return transaction_Facequeue;
	}

	public static void setTransaction_Facequeue(AdvancedFilePersistentQueue transactionFacequeue) {
		transaction_Facequeue = transactionFacequeue;
	}

	public static AdvancedFilePersistentQueue getTransfer_PlatFormqueue() {
		return transfer_PlatFormqueue;
	}

	public static void setTransfer_PlatFormqueue(AdvancedFilePersistentQueue transferPlatFormqueue) {
		transfer_PlatFormqueue = transferPlatFormqueue;
	}

	public Transfer_PlatformService getTransfer_PlatformService() {
		return transfer_PlatformService;
	}

	public void setTransfer_PlatformService(Transfer_PlatformService transferPlatformService) {
		transfer_PlatformService = transferPlatformService;
	}

	public static AdvancedFilePersistentQueue getBattle_PlayerStatqueue() {
		return battle_PlayerStatqueue;
	}

	public static void setBattle_PlayerStatqueue(AdvancedFilePersistentQueue battlePlayerStatqueue) {
		battle_PlayerStatqueue = battlePlayerStatqueue;
	}

	public static AdvancedFilePersistentQueue getBattle_costTimeFlowqueue() {
		return battle_costTimeFlowqueue;
	}

	public static void setBattle_costTimeFlowqueue(AdvancedFilePersistentQueue battleCostTimeFlowqueue) {
		battle_costTimeFlowqueue = battleCostTimeFlowqueue;
	}

	public static AdvancedFilePersistentQueue getBattle_TeamStatFlowqueue() {
		return battle_TeamStatFlowqueue;
	}

	public static void setBattle_TeamStatFlowqueue(AdvancedFilePersistentQueue battleTeamStatFlowqueue) {
		battle_TeamStatFlowqueue = battleTeamStatFlowqueue;
	}

	public Battle_PlayerStatService getBattle_PlayerStatService() {
		return battle_PlayerStatService;
	}

	public void setBattle_PlayerStatService(Battle_PlayerStatService battlePlayerStatService) {
		battle_PlayerStatService = battlePlayerStatService;
	}

	public Battle_costTimeService getBattle_costTimeService() {
		return battle_costTimeService;
	}

	public void setBattle_costTimeService(Battle_costTimeService battleCostTimeService) {
		battle_costTimeService = battleCostTimeService;
	}

	public Battle_TeamStatService getBattle_TeamStatService() {
		return battle_TeamStatService;
	}

	public void setBattle_TeamStatService(Battle_TeamStatService battleTeamStatService) {
		battle_TeamStatService = battleTeamStatService;
	}

	public FuMoStatService getFuMoStatService() {
		return fuMoStatService;
	}

	public void setFuMoStatService(FuMoStatService fuMoStatService) {
		this.fuMoStatService = fuMoStatService;
	}

	public static AdvancedFilePersistentQueue getFuMoFlowqueue() {
		return fuMoFlowqueue;
	}

	public static void setFuMoFlowqueue(AdvancedFilePersistentQueue fuMoFlowqueue) {
		StatServerService.fuMoFlowqueue = fuMoFlowqueue;
	}

	public EnterGameService getEnterGameService() {
		return enterGameService;
	}

	public void setEnterGameService(EnterGameService enterGameService) {
		this.enterGameService = enterGameService;
	}

	public RegistUserService getRegistUserService() {
		return registUserService;
	}

	public void setRegistUserService(RegistUserService registUserService) {
		this.registUserService = registUserService;
	}

	public static AdvancedFilePersistentQueue getRegistUserqueue() {
		return registUserqueue;
	}

	public static void setRegistUserqueue(AdvancedFilePersistentQueue registUserqueue) {
		StatServerService.registUserqueue = registUserqueue;
	}

	public static AdvancedFilePersistentQueue getEnterGamequeue() {
		return enterGamequeue;
	}

	public static void setEnterGamequeue(AdvancedFilePersistentQueue enterGamequeue) {
		StatServerService.enterGamequeue = enterGamequeue;
	}

	public static AdvancedFilePersistentQueue getNpcinfoFlowqueue() {
		return npcinfoFlowqueue;
	}

	public static void setNpcinfoFlowqueue(AdvancedFilePersistentQueue npcinfoFlowqueue) {
		StatServerService.npcinfoFlowqueue = npcinfoFlowqueue;
	}

	public NpcinfoStatService getNpcinfoStatService() {
		return npcinfoStatService;
	}

	public void setNpcinfoStatService(NpcinfoStatService npcinfoStatService) {
		this.npcinfoStatService = npcinfoStatService;
	}
	
	
}
