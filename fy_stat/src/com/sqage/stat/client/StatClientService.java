package com.sqage.stat.client;


import java.io.File;
import java.io.IOException;
import java.nio.channels.SelectionKey;

import org.apache.log4j.Logger;

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
import com.sqage.stat.message.SPENDMONEY_LOG_REQ;
import com.sqage.stat.message.STATUSERGUIDE_LOG_REQ;
import com.sqage.stat.message.StatMessageFactory;
import com.sqage.stat.message.TRANSACTION_FACE_LOG_REQ;
import com.sqage.stat.message.TRANSACTION_LOG_REQ;
import com.sqage.stat.message.TRANSACTION_SPECIAL_LOG_REQ;
import com.sqage.stat.message.TRANSFER_PLATFORM_LOG_REQ;
import com.sqage.stat.message.USEREGIST_LOG_REQ;
import com.sqage.stat.message.YINZIKUCUN_LOG_REQ;
import com.sqage.stat.model.AcceptHuoDonginfoFlow;
import com.sqage.stat.model.AcceptTaskFlow;
import com.sqage.stat.model.Battle_PlayerStatFlow;
import com.sqage.stat.model.Battle_TeamStatFlow;
import com.sqage.stat.model.Battle_costTimeFlow;
import com.sqage.stat.model.CreatePlayerFlow;
import com.sqage.stat.model.DaoJuFlow;
import com.sqage.stat.model.DayChangFlow;
import com.sqage.stat.model.EnterGameFlow;
import com.sqage.stat.model.FinishHuoDonginfoFlow;
import com.sqage.stat.model.FinishTaskFlow;
import com.sqage.stat.model.FuMoFlow;
import com.sqage.stat.model.GameChongZhiFlow;
import com.sqage.stat.model.LibaoFlow;
import com.sqage.stat.model.LogOutGameFlow;
import com.sqage.stat.model.NpcinfoFlow;
import com.sqage.stat.model.OnLineUsersCountFlow;
import com.sqage.stat.model.PayMoneyFlow;
import com.sqage.stat.model.PayMoneyUpGradeFlow;
import com.sqage.stat.model.SpendMoneyFlow;
import com.sqage.stat.model.StatUserGuideFlow;
import com.sqage.stat.model.TransactionFlow;
import com.sqage.stat.model.Transaction_FaceFlow;
import com.sqage.stat.model.Transaction_SpecialFlow;
import com.sqage.stat.model.Transfer_PlatformFlow;
import com.sqage.stat.model.UserRegistFlow;
import com.sqage.stat.model.YinZiKuCunFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionCreatedHandler;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.SelectorPolicy;

public class StatClientService implements ConnectionCreatedHandler,Runnable {

	public static Logger logger = Logger.getLogger(StatClientService.class);

	private String queueFile;
	AdvancedFilePersistentQueue queue;

	/**
	 * 是否开启统计
	 */
	private boolean openning;

	private boolean running;

	private Thread thread;

	//protected HashMap<String, DefaultQueue> queueMap;

	DefaultConnectionSelector selector;
	// private List<RequestMessage> reqList;

	public DefaultConnectionSelector getSelector() {
		return selector;
	}

	public void setSelector(DefaultConnectionSelector selector) {
		this.selector = selector;
		this.selector.setConnectionCreatedHandler(this);
	}

	protected static StatClientService self;

	public static StatClientService getInstance() {
		return self;
	}

	public void initialize() throws Exception {
		
		FileUtils.chkFolder(queueFile);
		queue = new AdvancedFilePersistentQueue(new File(queueFile), 1000, 64 * 1024 * 1024L);
		long now = System.currentTimeMillis();
		//queueMap = new HashMap<String, DefaultQueue>();
		self = this;
		if (openning) {
			start();
		}
		logger.info(this.getClass().getName() + " initialize successfully ] ["+openning+"] ["
				+ (System.currentTimeMillis() - now) + "]");
	}

	public void setOpenning(boolean openning) {
		this.openning = openning;
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

	public void start() {
		if (!running) {
			running = true;
			thread = new Thread(this,"StatClientService-Thread");
			thread.start();
		}
	}

	public void stop() {
		running = false;
		thread.interrupt();
	}

	public void addRequestMessage(String game, RequestMessage req) {
		if (openning) {
			queue.push(req);
		}
	}

	
	public void run() {
		while (running) {
			try {
					if (!queue.isEmpty()) {
						while (!queue.isEmpty()) {
							RequestMessage req = (RequestMessage) queue.peek();
							if(req != null){
								try {
									long time=System.currentTimeMillis();
									Connection conn = null;
									try{
										conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy, 6000);
									}catch(Exception e){
										logger.info("[sendReq] [Fail_takeoutConection_error] ["+ req.getTypeDescription() + "] [queue:"
												+ queue.size() + "] [时间："+(System.currentTimeMillis()-time)+"ms]",e);
										Thread.sleep(1000);
									}
									
									try{
										if(conn != null) {
											conn.writeMessage(req,false);
											logger.info("[sendReq] [SUCC] ["+ req.getTypeDescription() + "] [queue:"
													+ queue.size() + "] [时间："+(System.currentTimeMillis()-time)+"ms]");
											queue.pop();
										} else {
											logger.info("[sendReq] [FAIL:conn_is_null] ["+ req.getTypeDescription() + "] [queue:"
													+ queue.size() + "] [时间："+(System.currentTimeMillis()-time)+"ms]");
										}
									}catch(Exception e){
										logger.info("[sendReq] [Fail_writeMessage_error] ["+ req.getTypeDescription() + "] [queue:"
												+ queue.size() + "] [时间："+(System.currentTimeMillis()-time)+"ms]",e);
										Thread.sleep(1000);
										if(conn != null)
											selector.returnConnectoin(conn, SelectionKey.OP_READ);
										queue.pop();
									
									}
								} catch (Exception e) {
									logger.info("[sendReq] [FAILED] [" + req.getTypeDescription() + "] [queue:" + queue.size() + "]", e);
									Thread.sleep(1000);
								}
							}else{
								queue.pop();
							}
						}
					} else{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
			} catch (Throwable e) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				logger.error("增加日志消息时发生异常", e);
			}
		}
	}

	/**
	 * 传送增加用户注册日志
	 * 
	 * @param flow
	 */
	public void sendUserRegistFlow(String game, UserRegistFlow flow) {
		USEREGIST_LOG_REQ req = new USEREGIST_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加用户注册日志] "+ flow.toString());
	}
	/**
	 * 传送玩家进入游戏日志
	 * 
	 * @param flow
	 */
	public void sendEnterGameFlow(String game, EnterGameFlow flow) {
		ENTERGAME_LOG_REQ req = new ENTERGAME_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加玩家进入游戏日志] "+ flow.toString());
	}
	/**
	 * 传送玩家退出游戏日志
	 * 
	 * @param flow
	 */
	public void sendLogOutGameFlow(String game, LogOutGameFlow flow) {
		LOGOUTGAME_LOG_REQ req = new LOGOUTGAME_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加玩家退出游戏日志] "+ flow.toString());
	}
	/**
	 * 传送日期变化日志
	 * 
	 * @param flow
	 */
	public void sendDayChangFlow(String game, DayChangFlow flow) {
		DAYCHENG_LOG_REQ req = new DAYCHENG_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加日期变化日志] "+ flow.toString());
	}
    /**
      * 传送玩家支付费用日志
      * @param flow
      */
    public void sendPayMoneyFlow(String game, PayMoneyFlow flow) {
	   PAYMONEY_LOG_REQ req = new PAYMONEY_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
	   addRequestMessage(game, req);
	   logger.info("[增加玩家支付费用日志] "+ flow.toString());
     }

    /**
     * 传送玩家支付费用日志
     * @param flow
     */
   public void sendPayMoneyUpGradeFlow(String game, PayMoneyUpGradeFlow flow) {
	   PAYMONEYUPDATE_LOG_REQ req = new PAYMONEYUPDATE_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
	   addRequestMessage(game, req);
	   logger.info("[增加升级玩家支付费用日志] "+ flow.toString());
    }

    
    
    /**
     * 传送日期变化日志
     * @param flow
     */
   public void sendSpendMoneyFlow(String game, SpendMoneyFlow flow) {
	  SPENDMONEY_LOG_REQ req = new SPENDMONEY_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
	  addRequestMessage(game, req);
	  logger.info("[增加玩家消耗费用日志] "+ flow.toString());
     }
   /**
    * 玩家创建角色
    * @param game
    * @param flow
    */
   public void sendCreatePlayerFlow(String game, CreatePlayerFlow flow) {
	   CREATEPLAYER_LOG_REQ req = new CREATEPLAYER_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		  addRequestMessage(game, req);
		  logger.info("[增加玩家创建角色日志] "+ flow.toString());
	     }

	/**
	 * 玩家在线用户数
	 * 
	 * @param game
	 * @param flow
	 */
	public void sendOnLineUsersCountFlow(String game, OnLineUsersCountFlow flow) {
		ONLINEUSERSCOUNT_LOG_REQ req = new ONLINEUSERSCOUNT_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加玩家在线用户数日志] " + flow.toString());
	}
	
	/**
	 * 道具信息
	 * 
	 * @param game
	 * @param flow
	 */
	public void sendDaoJuFlow(String game, DaoJuFlow flow) {
		DAOJU_LOG_REQ req = new DAOJU_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加用户道具日志] " + flow.toString());
	}
	
	
	/**
	 * 道具交换信息信息
	 * 
	 * @param game
	 * @param flow
	 */
	public void sendTransactionFlow(String game, TransactionFlow flow) {
		TRANSACTION_LOG_REQ req = new TRANSACTION_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加道具交换信息日志] " + flow.toString());
	}
	
	
	/**
	 * 接受任务信息
	 * 
	 * @param game
	 * @param flow
	 */
	public void sendAcceptTaskFlow(String game, AcceptTaskFlow flow) {
		ACCEPTTASK_LOG_REQ req = new ACCEPTTASK_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加 接受任务信息日志] " + flow.toString());
	}
	
	
	/**
	 * 完成任务信息
	 * @param game
	 * @param flow
	 */
	public void sendFinishTaskFlow(String game, FinishTaskFlow flow) {
		FINISHTASK_LOG_REQ req = new FINISHTASK_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加 完成任务信息日志] " + flow.toString());
	}
	
	/**
	 * 接受活动信息
	 * @param game
	 * @param flow
	 */
	public void sendAcceptHuoDonginfoFlow(String game, AcceptHuoDonginfoFlow flow) {
		ACCEPTHUODONGINFO_LOG_REQ req = new ACCEPTHUODONGINFO_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加接受活动信息日志] " + flow.toString());
	}
	
	/**
	 * 完成活动信息
	 * @param game
	 * @param flow
	 */
	public void sendFinishHuoDonginfoFlow(String game, FinishHuoDonginfoFlow flow) {
		FINISHHUODONGINFO_LOG_REQ req = new FINISHHUODONGINFO_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加完成活动信息日志] " + flow.toString());
	}
	
	/**
	 * 游戏中的货币充值，消耗
	 * @param game
	 * @param flow
	 */
	public void sendGameChongZhiFlow(String game, GameChongZhiFlow flow) {
		GAMECHONGZHI_LOG_REQ req = new GAMECHONGZHI_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加游戏中的货币充值，消耗日志] " + flow.toString());
	}
	/**
	 * 新手引导日志
	 * @param game
	 * @param flow
	 */
	public void sendStatUserGuideFlow(String game, StatUserGuideFlow flow) {
		STATUSERGUIDE_LOG_REQ req = new STATUSERGUIDE_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[增加新手引导日志] " + flow.toString());
	}
	
	/**
	 * 异常交易日志
	 * @param game
	 * @param flow
	 */
	public void sendTransaction_SpecialFlow(String game, Transaction_SpecialFlow flow) {
		TRANSACTION_SPECIAL_LOG_REQ req = new TRANSACTION_SPECIAL_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[异常交易日志] " + flow.toString());
	}
   
   
	/**银子库存日志
	 * @param game
	 * @param flow
	 */
	public void sendYinZiKuCunFlow(String game, YinZiKuCunFlow flow) {
		YINZIKUCUN_LOG_REQ req = new YINZIKUCUN_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[银子留存日志] " + flow.toString());
	}
	
	
   public void sendLibaoFlow(String game, LibaoFlow flow) {
	   LIBAO_LOG_REQ req = new LIBAO_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[周月礼包日志] " + flow.toString());
	}
	
   public void sendTransaction_FaceFlow(String game, Transaction_FaceFlow flow) {
	   TRANSACTION_FACE_LOG_REQ req = new TRANSACTION_FACE_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[面对面交易日志] " + flow.toString());
	}
	
   public void sendTransfer_PlatformFlow(String game, Transfer_PlatformFlow flow) {
	   TRANSFER_PLATFORM_LOG_REQ req = new TRANSFER_PLATFORM_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[交易平台日志] " + flow.toString());
	}
   public void sendBattle_costTimeFlow(String game, Battle_costTimeFlow flow) {
	   BATTLE_COSTTIME_LOG_REQ req = new BATTLE_COSTTIME_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[战争所用时间平台日志] " + flow.toString());
	}
   
   public void sendBattle_PlayerStatFlow(String game, Battle_PlayerStatFlow flow) {
	   BATTLE_PLAYERSTAT_LOG_REQ req = new BATTLE_PLAYERSTAT_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[每个玩家的功勋平台日志] " + flow.toString());
	}
   
   public void sendBattle_TeamStatFlow(String game, Battle_TeamStatFlow flow) {
	   BATTLE_TEAMSTAT_LOG_REQ req = new BATTLE_TEAMSTAT_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[战队功勋日志] " + flow.toString());
	}
   
   public void sendFuMoFlow(String game, FuMoFlow flow) {
	   FUMO_LOG_REQ req = new FUMO_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[佛魔日志] " + flow.toString());
	}
   public void sendNpcinfoFlow(String game, NpcinfoFlow flow) {
	   NPC_LOG_REQ req = new NPC_LOG_REQ(StatMessageFactory.nextSequnceNum(), flow);
		addRequestMessage(game, req);
		logger.info("[NPC日志] " + flow.toString());
	}
	@Override
	public void created(Connection conn, String arg1) throws IOException {
		conn.setMessageFactory(StatMessageFactory.getInstance());
	}

}
