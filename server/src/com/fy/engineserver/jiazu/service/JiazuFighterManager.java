package com.fy.engineserver.jiazu.service;

import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuXuanZhanData;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;

public class JiazuFighterManager implements Runnable{

	private JiazuFighterManager(){
		
	}
	
	public static Logger logger = LoggerFactory.getLogger(JiazuFighterManager.class);
	private static JiazuFighterManager self;
	public static JiazuFighterManager getInstance(){
		return self;
	}
	
	public void init(){
		
		long time = SystemTime.currentTimeMillis();
		Thread thread = new Thread(this, "JiazuFighterManager");
		thread.start();
		self = this;
		running = true;
		ServiceStartRecord.startLog(this);
	}
	
	boolean running = false;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(running){
			try{
				Thread.sleep(60000);
				flush();
			}catch(Exception ex){
				if(logger.isWarnEnabled())
					logger.warn("[家族宣战管理器] [异常]",ex);
			}
		}
	}
	
	public void flush(){
		if(map == null){
			map = new Hashtable<Long,JiazuXuanZhanData>();
		}
		if(map.keySet() != null){
			for(Long l : map.keySet()){
				if(l != null){
					JiazuXuanZhanData jxzd = map.get(l);
					if(jxzd != null){
						Hashtable<Long,Long> ht = jxzd.getXuanzhanJiazuMap();
						if(ht != null && !ht.isEmpty()){
							if(ht.keySet() != null){
								Long[] ls = ht.keySet().toArray(new Long[0]);
								if(ls != null){
									for(int i = 0; i < ls.length; i++){
										if(ls[i] != null){
											Long ll = ht.get(ls[i]);
											if(ll != null){
												if(SystemTime.currentTimeMillis() > ll){
													ht.remove(ls[i]);
													JiazuManager jm = JiazuManager.getInstance();
													Jiazu jz = jm.getJiazu(l.longValue());
													if(jz != null){
														flushOnlineXuanZhanJiazuPlayer(jz);
													}
												}
											}else{
												ht.remove(ls[i]);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void flushOnlineXuanZhanJiazuPlayer(Jiazu jz){
		
		PlayerManager pm = PlayerManager.getInstance();
		if(pm != null){
			Player[] players = pm.getOnlinePlayers();
			if(players != null){
				for(int i = 0; i < players.length; i++){
					Player p = players[i];
					if(p != null){
						if(jz.isContainPlayer(p.getId())){
							设置玩家宣战属性(p);
						}
					}
				}
			}
		}
	}
	
	public Hashtable<Long,JiazuXuanZhanData> map = new Hashtable<Long,JiazuXuanZhanData>();
	
	public void 家族宣战(Player player, long xuanzhanJiazuId){
		if(player == null){
			return;
		}
		String result = 家族宣战合法性判断(player, xuanzhanJiazuId);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		
		//扣钱
		BillingCenter bc = BillingCenter.getInstance();
		try{
			int count = 宣战所需资金();
			bc.playerExpense(player, count, CurrencyType.SHOPYINZI, ExpenseReasonType.BANGPAI_XUANZHAN, "家族宣战");
		}catch(Exception ex){
			if(logger.isWarnEnabled())
				logger.warn("[家族宣战] [金币异常]",ex);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.您的资金不足);
			player.addMessageToRightBag(hreq);
			return;
		}
		JiazuXuanZhanData jxzd = getJiazuXuanZhanData(player.getJiazuId());
		if(jxzd == null){
			jxzd = new JiazuXuanZhanData(player.getJiazuId());
			map.put(player.getJiazuId(), jxzd);
		}
		Hashtable<Long,Long> xzMap = jxzd.getXuanzhanJiazuMap();
		if(xzMap == null){
			xzMap = new Hashtable<Long,Long>();
			jxzd.setXuanzhanJiazuMap(xzMap);
		}
		xzMap.put(xuanzhanJiazuId, SystemTime.currentTimeMillis()+3600*1000);
		
		JiazuXuanZhanData jxzds = getJiazuXuanZhanData(xuanzhanJiazuId);
		if(jxzds == null){
			jxzds = new JiazuXuanZhanData(player.getJiazuId());
			map.put(xuanzhanJiazuId, jxzds);
		}
		Hashtable<Long,Long> xzMaps = jxzds.getXuanzhanJiazuMap();
		if(xzMaps == null){
			xzMaps = new Hashtable<Long,Long>();
			jxzds.setXuanzhanJiazuMap(xzMaps);
		}
		xzMaps.put(player.getJiazuId(), SystemTime.currentTimeMillis()+3600*1000);
		
		JiazuManager jm = JiazuManager.getInstance();
		Jiazu jz = jm.getJiazu(player.getJiazuId());
		Jiazu xzjz = jm.getJiazu(xuanzhanJiazuId);

		//系统广播
		try{
			ChatMessageService cms = ChatMessageService.getInstance();
			ChatMessage msg = new ChatMessage();
			String descri = Translate.translateString(Translate.某家族对某家族宣战, new String[][]{{Translate.STRING_1,jz.getName()},{Translate.STRING_2,xzjz.getName()}});
			msg.setMessageText(descri);
			cms.sendMessageToSystem(msg);
			cms.sendMessageToSystem(msg);
			cms.sendMessageToSystem(msg);
			
			MailManager mm = MailManager.getInstance();
			if(mm != null){
				String description = Translate.translateString(Translate.你家族对某家族宣战, new String[][]{{Translate.STRING_1,xzjz.getName()}});
				mm.sendMail(jz.getJiazuMaster(), new ArticleEntity[0], Translate.家族宣战, description, 0, 0, 0, "");
				description = Translate.translateString(Translate.某家族对你家族宣战, new String[][]{{Translate.STRING_1,jz.getName()}});
				mm.sendMail(xzjz.getJiazuMaster(), new ArticleEntity[0], Translate.家族宣战, description, 0, 0, 0, "");
			}
			if(logger.isInfoEnabled())
				logger.info("[宣战] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),player.getJiazuId(),xuanzhanJiazuId,descri});
		}catch(Exception ex){
			
		}
		
		flushOnlineXuanZhanJiazuPlayer(jz);
		flushOnlineXuanZhanJiazuPlayer(xzjz);
	}
	
	public String 家族宣战合法性判断(Player player, long xuanzhanJiazuId){
		long jiazuId = player.getJiazuId();
		if(jiazuId == xuanzhanJiazuId){
			return Translate.不能对自己家族宣战;
		}
		JiazuManager jm = JiazuManager.getInstance();
		if(jm != null){
			Jiazu jz = jm.getJiazu(jiazuId);
			if(jz == null){
				return Translate.您还没有家族;
			}
			if(jz.getJiazuMaster() != player.getId()){
				return Translate.您不是家族族长;
			}
			Jiazu xzjz = jm.getJiazu(xuanzhanJiazuId);
			if(xzjz == null){
				return Translate.没有这个家族;
			}
			JiazuXuanZhanData jxzd = getJiazuXuanZhanData(jiazuId);
			if(jxzd != null && jxzd.getXuanzhanJiazuMap() != null && !jxzd.getXuanzhanJiazuMap().isEmpty()){
				if(jxzd.getXuanzhanJiazuMap().keySet() != null){
					if(jxzd.getXuanzhanJiazuMap().keySet().contains(xuanzhanJiazuId)){
						return Translate.您的家族和这个家族正处于宣战状态;
					}
				}
			}
		}
		if(player.getSilver() + player.getShopSilver() < 宣战所需资金()){
			return Translate.您的资金不足;
		}
		return null;
	}
	
	public int 宣战所需资金(){
		return 100000;
	}

	public boolean hasJiazuXuanZhan(long jiazuId){
		boolean has = false;
		JiazuXuanZhanData jxzd = map.get(jiazuId);
		if(jxzd != null && jxzd.getXuanzhanJiazuMap() != null && !jxzd.getXuanzhanJiazuMap().isEmpty()){
			has = true;
		}
		return has;
	}
	
	public boolean isInJiazuXuanZhan(Player playerA, Player playerB){
		if(playerA == null || playerB == null){
			return false;
		}
		if(playerA.getJiazuId() <= 0 || playerB.getJiazuId() <= 0){
			return false;
		}
		if(!hasJiazuXuanZhan(playerA.getJiazuId()) || !hasJiazuXuanZhan(playerB.getJiazuId())){
			return false;
		}
		JiazuXuanZhanData jxzd = getJiazuXuanZhanData(playerA.getJiazuId());
		if(jxzd == null){
			return false;
		}
		Hashtable<Long,Long> xuanzhanJiazuMap = jxzd.getXuanzhanJiazuMap();
		if(xuanzhanJiazuMap != null && xuanzhanJiazuMap.keySet() != null){
			for(Long l : xuanzhanJiazuMap.keySet()){
				if(l != null && l == playerB.getJiazuId()){
					return true;
				}
			}
		}
		return false;
	}
	public JiazuXuanZhanData getJiazuXuanZhanData(long jiazuId){
		return map.get(jiazuId);
	}
	
	public void 设置玩家宣战属性(Player player){
		if(player == null){
			return;
		}
		try{
			boolean xuanzhan = hasJiazuXuanZhan(player.getJiazuId());
			if(xuanzhan != player.isJiazuXuanZhanFlag()){
				player.setJiazuXuanZhanFlag(xuanzhan);
			}
			JiazuXuanZhanData jxzd = getJiazuXuanZhanData(player.getJiazuId());
			if(jxzd != null){
				Hashtable<Long,Long> xuanzhanJiazuMap = jxzd.getXuanzhanJiazuMap();
				if(xuanzhanJiazuMap != null && xuanzhanJiazuMap.keySet() != null){
					long[] xuanzhanJiazuIds = new long[xuanzhanJiazuMap.keySet().size()];
					Long[] temp = xuanzhanJiazuMap.keySet().toArray(new Long[0]);
					for(int i = 0; i < xuanzhanJiazuIds.length; i++){
						xuanzhanJiazuIds[i] = temp[i];
					}
					player.setJiazuXuanZhanData(xuanzhanJiazuIds);
				}else{
					player.setJiazuXuanZhanData(new long[0]);
				}
			}else{
				if(player.getJiazuXuanZhanData() == null || player.getJiazuXuanZhanData().length != 0){
					player.setJiazuXuanZhanData(new long[0]);
				}
			}
		}catch(Exception ex){
			if(logger.isWarnEnabled())
				logger.warn("[JiazuFighterManager] [异常] [{}] [{}] [{}]",new Object[]{player.getUsername(),player.getId(),player.getName()},ex);
		}
	}
}
