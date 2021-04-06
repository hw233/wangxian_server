package com.fy.engineserver.articleExchange;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.fy.engineserver.activity.explore.ExploreManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.CLOSE_DEAL_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;

public class ArticleExchangeManager implements Runnable {

	public static long 可以重复交换时间 = 30*1000;
	public static long 删除缓存时间 = 5*60*1000;
	public static Logger logger = ExploreManager.logger;
	public static ArticleExchangeManager instance = null;
	public static ArticleExchangeManager getInstance(){
		return instance;
	}
	
	private List<ExchangeDeal> list = new ArrayList<ExchangeDeal>();
	public void init(){
		
		instance = this;
		Thread t = new Thread(this,"ArticleExchangeManager");
		t.start();
		if(logger.isInfoEnabled())
			logger.info("ArticleExchangeManager init");
		ServiceStartRecord.startLog(this);
	}
	
	@Override
	public void run() {
		 while(true){
			 try {
				Thread.sleep(60*1000);
				
				if(!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()){
					continue;
				}
				for(int i=list.size()-1; i>=0; i--) {
					ExchangeDeal ex = list.get(i);
					if((SystemTime.currentTimeMillis() - ex.getCreateTime() >=删除缓存时间 )){
						list.remove(ex);
					}
				}
				
			} catch (Throwable t) {
				logger.error("[交换心跳异常]",t);
			}
		 }
	}

	public ExchangeDeal createExchangeDeal(Player playerA,Player playerB){
		ExchangeDeal ed = new ExchangeDeal();
		ed.setPlayerAId(playerA.getId());
		ed.setPlayerBId(playerB.getId());
		ed.setCreateTime(SystemTime.currentTimeMillis());
		this.list.add(ed);
		return ed;
	}
	
	
	public List<ExchangeDeal> getList() {
		return list;
	}

	public void setList(List<ExchangeDeal> list) {
		this.list = list;
	}

	/**
	 * 
	 * @param playerA 申请方
	 * @param playerB 被邀请方
	 * @return
	 */
	public boolean playerCanExchange(Player playerA,Player playerB){
		long now = SystemTime.currentTimeMillis();
		
		ExchangeDeal dealA = this.getPlayerExchangeDeal(playerA);
		ExchangeDeal dealB = this.getPlayerExchangeDeal(playerB);
		if(dealA == dealB){
			return true;
		}
		if(dealA == null ||(dealA != null && (now - dealA.getCreateTime() > 可以重复交换时间 ))){
			
			if(dealB == null ||(dealB != null && (now - dealB.getCreateTime() > 可以重复交换时间 ))){
				return true;
			}else{
				playerA.send_HINT_REQ(Translate.现在还不能申请对方正在交换中);
				if(logger.isDebugEnabled()){
					logger.debug("[申请交换失败] [有人正在交换中] ["+playerA.getLogString()+"]");
				}
				return false;
			}
		}else{
			playerA.send_HINT_REQ(Translate.现在还不能申请你正在交换中);
			if(logger.isDebugEnabled()){
				logger.debug("[申请交换失败] [有人正在交换中] ["+playerA.getLogString()+"]");
			}
			return false;
		}
		
	}
	
	/**
	 * B方同意  新建一个新的交换实体
	 * @param playerA
	 * @param playerB
	 * @return
	 */
	public ExchangeDeal createNewExchangeDeal(Player playerA,Player playerB){
		PlayerManager pm = PlayerManager.getInstance();
		boolean can = this.playerCanExchange(playerA, playerB);
		if(can){
			ExchangeDeal dealA = this.getPlayerExchangeDeal(playerA);
			ExchangeDeal dealB = this.getPlayerExchangeDeal(playerB);
			if(dealA != null && dealA == dealB){
				return dealA;
			}
			if(dealA != null){
//				if(!dealA.isState()){
//					dealA.setState(true);
					long tempA = dealA.getPlayerAId();
					long tempB = dealA.getPlayerBId();
					CLOSE_DEAL_RES res = new CLOSE_DEAL_RES(GameMessageFactory.nextSequnceNum(), dealA.getId());
					try {
						pm.getPlayer(tempA).addMessageToRightBag(res);
						pm.getPlayer(tempB).addMessageToRightBag(res);
					} catch (Exception e) {
						logger.error("[新的交换实体] ["+playerB.getLogString()+"]",e);
					}
//				}
			}
			if(dealB != null){
//				if(!dealB.isState()){
//					dealB.setState(true);
					long tempA = dealB.getPlayerAId();
					long tempB = dealB.getPlayerBId();
					CLOSE_DEAL_RES res = new CLOSE_DEAL_RES(GameMessageFactory.nextSequnceNum(), dealB.getId());
					try {
						pm.getPlayer(tempA).addMessageToRightBag(res);
						pm.getPlayer(tempB).addMessageToRightBag(res);
					} catch (Exception e) {
						logger.error("[新的交换实体] ["+playerB.getLogString()+"]",e);
					}
//				}
			}
			ExchangeDeal ed = this.createExchangeDeal(playerA, playerB);
			if(logger.isWarnEnabled()){
				logger.warn("[新建交换成功] ["+playerA.getLogString()+"] ["+playerB.getLogString()+"]");
			}
			return ed;
		}else{
			if(logger.isWarnEnabled()){
				logger.warn("[新建交换失败] [有人正在交换中] ["+playerA.getLogString()+"] ["+playerB.getLogString()+"]");
			}
			return null;
		}
	}
	
	public ExchangeDeal getDealById(long dealId){
		for(ExchangeDeal ed : list){
			if(ed.getId() == dealId){
				return ed;
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("[根据id取交换实体错误] [没有这个交换实体] []");
		}
		return null;
	}

	
	public ExchangeDeal getPlayerExchangeDeal(Player player){
		for(ExchangeDeal deal :list){
			if(deal.getPlayerAId() == player.getId() || deal.getPlayerBId() == player.getId()){
				return deal;
			}
		}
		return null;
	}
}
