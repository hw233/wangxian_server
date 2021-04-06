package com.fy.engineserver.qiancengta;

import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.QiLingArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.qiancengta.info.QianCengTa_DaoInfo;
import com.fy.engineserver.qiancengta.info.QianCengTa_FlopSet;
import com.fy.engineserver.qiancengta.info.RewardMsg;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.vip.VipManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 此对象需要存储
 * 
 * 
 *
 */

@SimpleEmbeddable
public class QianCengTa_Ceng {

	public QianCengTa_Ceng(){};
	
	private transient long startTime;
	
	public static Logger logger = LoggerFactory.getLogger(QianCengTa_Ta.class);
	
	protected transient QianCengTa_Dao dao;
	
	public QianCengTa_Dao getDao() {
		return dao;
	}
	public void setDao(QianCengTa_Dao dao) {
		this.dao = dao;
	}

	//第几层
	protected int cengIndex;
	
	public int getCengIndex() {
		return cengIndex;
	}
	public void setCengIndex(int cengIndex) {
		this.cengIndex = cengIndex;
	}
	
	private transient int nandu;
	
	public static final int STATUS_无奖励 = 0;
	public static final int STATUS_奖励未领取 = 1;
	public static final int STATUS_奖励已领取 = 2;
	public static final int STATUS_奖励已放弃 = 3;
	
	//记录爬塔的奖励状态
	public transient int rewardStatus = 0;
	
	private transient int rewardExp = 0;
	
	private ArrayList<Cell> rewards = new ArrayList<Cell>();
	
	public ArrayList<Cell> getRewards() {
		return rewards;
	}
	public void setRewards(ArrayList<Cell> rewards) {
		this.rewards = rewards;
	}

	/**
	 * 计算玩家的奖励
	 * 保留奖励在此对象上
	 * 等到玩家下一次来领取。
	 * @throws Exception 
	 * 
	 */
	public void calculateFlopByPaTa() throws Exception{
		Player player = dao.ta.getPlayer();
		if(player==null){
			return;
		}
		long exp = 0;
		if (getCengIndex() < 0) {
			//隐藏层
			exp = (long)((long)QianCengTa_DaoInfo.QianCengDaoHiddenExp[dao.getDaoIndex()] * (VipManager.getInstance().vip爬千层塔的百分比(player) + 100) / 100);
		}else {
			exp = (long)((long)QianCengTa_DaoInfo.QianCengDaoExp[dao.getDaoIndex()]* (VipManager.getInstance().vip爬千层塔的百分比(player) + 100)/100);
		}
		setRewardExp((int)exp);
		player.addExp(exp, ExperienceManager.ADDEXP_REASON_QIANCENGTA);
		if (logger.isWarnEnabled())
			logger.warn("千层塔获得经验: [p={}] [cengIndex={}] [daoIndex={}] [exp={}]", new Object[]{player.getId(), getCengIndex(), dao.getDaoIndex(), exp});
		if(rewardStatus ==STATUS_奖励已领取){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔][初始化奖励][状态异常，奖励已经领取]"+"cengIndex="+cengIndex+dao.ta.getPlayer());
			return ;
		}
		if(rewardStatus ==STATUS_奖励已放弃){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔][初始化奖励][状态异常，奖励已经放弃]"+"cengIndex="+cengIndex);
			return ;
		}
		rewards.clear();
		ArrayList<QianCengTa_FlopSet> info = dao.ta.getCengInfo(nandu,dao.getDaoIndex(), cengIndex).getFlopSets();
		Random r = new Random();
		for(QianCengTa_FlopSet ri : info){
			int nextInt = r.nextInt(QianCengTa_FlopSet.FLOP_CONSULT);
			if (logger.isInfoEnabled()) {
				logger.info("掉落[r={}] [{}] [PK={}]", new Object[]{ri.getRandom(), nextInt, Player.得到玩家打怪掉落的pk惩罚百分比(player)});
			}
			if (ri.getRandom() * Player.得到玩家打怪掉落的pk惩罚百分比(player) > nextInt) {
				String artName = ri.getFlopName(player);
				Article article = ArticleManager.getInstance().getArticle(artName);
				if (article == null) {
					if(logger.isWarnEnabled())
						logger.warn("[千层塔][初始化奖励][状态异常，物品不存在] [p={}] [c={}] [a={}]", new Object[]{player.getId(), dao.getDaoIndex() + "-" + cengIndex, artName});
					continue;
				}
				if (article.isOverlap()) {
					ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(article, ri.isBind(), ArticleEntityManager.CREATE_REASON_QIANCENGTA, player, ri.getColorType(), ri.getNum(), true);
					Cell c = new Cell();
					c.setEntityId(entity.getId());
					c.setCount(ri.getNum());
					rewards.add(c);
					if (logger.isWarnEnabled())
						logger.warn("千层塔奖励: [p={}] [难度={}] [dc={}] [id={}] [name={}] [num={}] [color={}] [bind={}]", new Object[]{player.getLogString(), nandu, dao.getDaoIndex() +"-"+ cengIndex, entity.getId(), entity.getArticleName(), ri.getNum(), entity.getColorType(), entity.isBinded()});
				}else{
					for (int i = 0; i < ri.getNum(); i++) {
						ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(article, ri.isBind(), ArticleEntityManager.CREATE_REASON_QIANCENGTA, player, ri.getColorType(), 1, true);
						Cell c = new Cell();
						c.setEntityId(entity.getId());
						c.setCount(1);
						rewards.add(c);
						if (logger.isWarnEnabled())
							logger.warn("千层塔奖励: [p={}] [难度={}] [dc={}] [id={}] [name={}] [num={}] [color={}] [bind={}]", new Object[]{player.getLogString(), nandu, dao.getDaoIndex() +"-"+ cengIndex, entity.getId(), entity.getArticleName(), 1, entity.getColorType(), entity.isBinded()});
					}
				}
			}
		}
		if(rewards.size()>0){
			rewardStatus = STATUS_奖励未领取;
		}else {
			rewardStatus = STATUS_无奖励;
		}
	}
	
	//尝试把这层的奖励放到玩家背包
	public void givePlayerRewards(Player player){
		if(rewardStatus==STATUS_无奖励){
			if (rewards.size() > 0) {
				rewards.clear();
			}
			if(logger.isWarnEnabled())
				logger.warn("[千层塔][获取奖励][无奖励]"+"cengIndex="+cengIndex+player.getLogString());
			return ;
		}
		if(rewardStatus ==STATUS_奖励已领取){
			if (rewards.size() > 0) {
				rewards.clear();
			}
			if(logger.isWarnEnabled())
				logger.warn("[千层塔][获取奖励][奖励已经领取]"+"cengIndex="+cengIndex+player.getLogString());
			return ;
		}
		if(rewardStatus ==STATUS_奖励已放弃){
			if (rewards.size() > 0) {
				rewards.clear();
			}
			if(logger.isWarnEnabled())
				logger.warn("[千层塔][获取奖励][奖励已经放弃]"+"cengIndex="+cengIndex+player.getLogString());
			return ;
		}
		
		if(rewards==null||rewards.size()<=0){
			rewardStatus = STATUS_无奖励;
			if(logger.isWarnEnabled())
				logger.warn("[千层塔][获取奖励][奖励为空]"+"cengIndex="+cengIndex+"rewards size="+rewards.size()+player.getLogString());
			return ;
		}
		ArrayList<Cell> removeCell = new ArrayList<Cell>();
		for(int i=0;i<rewards.size(); i++){
			try{
				Cell c = rewards.get(i);
				ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(c.getEntityId());
				if (entity == null) {
					removeCell.add(c);
					logger.error("[千层塔] [奖励不存在] [错误] [{}] [{}] [{}]", new String[]{player.getLogString(), dao.getDaoIndex()+"", ""+cengIndex});
					continue;
				}
				Article article = ArticleManager.getInstance().getArticle(entity.getArticleName());
				if (article == null) {
					removeCell.add(c);
					logger.error("[千层塔] [奖励对象不存在] [错误] [{}] [{}] [{}] [{}]", new String[]{player.getLogString(), dao.getDaoIndex()+"", ""+cengIndex, entity.getArticleName()});
					continue;
				}
				if (article instanceof QiLingArticle) {
					//器灵放器灵
					if (!player.getKnapsacks_QiLing().isFull()) {
						removeCell.add(c);
						if(logger.isWarnEnabled())
							logger.warn("[千层塔][获取奖励][成功] [p={}] [daoIndex={}] [cengIndex={}] [eid={}] [eName={}] [num={}] [size={}]", new Object[]{player.getId(), dao.getDaoIndex(), cengIndex, entity.getId(), entity.getArticleName(), c.getCount(), rewards.size()});
						player.getKnapsacks_QiLing().putToEmptyCell(entity.getId(), c.getCount(), "千层塔器灵获取");
					}
				}else {
					if (!player.getKnapsack_common().isFull()) {
						removeCell.add(c);
						if(logger.isWarnEnabled())
						logger.warn("[千层塔][获取奖励][成功] [p={}] [daoIndex={}] [cengIndex={}] [eid={}] [eName={}] [num={}] [size={}]", new Object[]{player.getId(), dao.getDaoIndex(), cengIndex, entity.getId(), entity.getArticleName(), c.getCount(), rewards.size()});
						player.putToKnapsacks(entity, c.getCount(), "千层塔奖励");
					}
				}
				
				if (player.getKnapsack_common().isFull() && player.getKnapsacks_QiLing().isFull()) {
					break;
				}
			}catch(Exception e) {
				logger.error("千层塔获取奖励错误", e);
			}
		}
		
		for (int i = 0; i <removeCell.size(); i++) {
			rewards.remove(removeCell.get(i));
		}
		
		if(rewards.size()>0){
			player.sendError(Translate.text_qiancengta_背包或器灵背包已经满了请清理背包);
			if(logger.isWarnEnabled())
				logger.warn("[千层塔][获取奖励][背包已满，奖励未全部放入背包]"+"cengIndex="+cengIndex+"rewards size="+rewards.size()+player.getLogString());
			rewardStatus = STATUS_奖励未领取;
		}else{
			if(logger.isWarnEnabled())
				logger.warn("[千层塔][获取奖励][奖励全部放入背包]"+"cengIndex="+cengIndex+"rewards size="+rewards.size()+player.getLogString());
			rewardStatus = STATUS_奖励已领取;
		}
	}
	
	public void clear(){
		for (int i = 0; i < getRewards().size(); i++) {
			ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(getRewards().get(i).getEntityId());
			if (entity == null) {
				continue;
			}
			Article article = ArticleManager.getInstance().getArticle(entity.getArticleName());
			if (article == null) {
				continue;
			}
			if (!article.isOverlap()) {
				entity.setAbandoned(true);
			}
		}
	}
	
	
//	public void giveUpReward(){
//		if(rewardStatus==STATUS_无奖励){
//			if(logger.isWarnEnabled())
//				logger.warn("[千层塔][放弃奖励][无奖励]"+"cengIndex="+cengIndex+"status="+rewardStatus);
//			return ;
//		}
//		if(rewardStatus ==STATUS_奖励已领取){
//			if(logger.isWarnEnabled())
//				logger.warn("[千层塔][放弃奖励][奖励已经领取]"+"cengIndex="+cengIndex+"status="+rewardStatus);
//			return ;
//		}
//		if(rewardStatus ==STATUS_奖励已放弃){
//			if(logger.isWarnEnabled())
//				logger.warn("[千层塔][放弃奖励][奖励已放弃]"+"cengIndex="+cengIndex+"status="+rewardStatus);
//			return ;
//		}
//		rewards.clear();
//		rewardStatus = STATUS_奖励已放弃;
//	}
	
	public RewardMsg getRewardMsg(){
		RewardMsg reM = new RewardMsg();
		reM.setCengIndex(cengIndex);
		long[] ids = new long[rewards.size()];
		int[] nums = new int[rewards.size()];
		for(int i=0;i<rewards.size();i++){
			ids[i] = rewards.get(i).getEntityId();
			nums[i] = rewards.get(i).getCount();
		}
		reM.setRewardid(ids);
		reM.setNums(nums);
		return reM;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setRewardExp(int rewardExp) {
		this.rewardExp = rewardExp;
	}
	public int getRewardExp() {
		return rewardExp;
	}
	public void setNandu(int nandu) {
		this.nandu = nandu;
	}
	public int getNandu() {
		return nandu;
	}
	
}
