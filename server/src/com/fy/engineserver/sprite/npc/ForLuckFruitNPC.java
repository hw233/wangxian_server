package com.fy.engineserver.sprite.npc;

import java.util.Hashtable;
import java.util.Map;

import com.fy.engineserver.activity.luckfruit.ForLuckFruitManager;
import com.fy.engineserver.activity.luckfruit.ForLuckFruitTimeBar;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.RandomTool.RandomType;

/**
 * 祝福果树npc
 * 
 * 
 */
@SuppressWarnings("serial")
public class ForLuckFruitNPC extends NPC implements Collectionable, NeedRecordByOption {
	private long jiazuId;

	/** 产生果子总数 */
	private int totalNum;
	/** 剩余果子总数 */
	private volatile int leftNum;

	private Game game;

	private int articleColor;

	private String articleName;

	private Hashtable<Long, Long> reapers = new Hashtable<Long, Long>();
	/** 是否提供服务,成熟了为true */
	private volatile boolean inService;

	private long collectionBarTime = 1000L;

	private long bournTime;
	/** 成熟时间 */
	private long growupTime;
	/** 死亡时间 */
	private long deadTime;

	@Override
	public byte getNpcType() {
		return NPC_TYPE_NORMAL;
	}

	@Override
	public boolean canCollection(Player player) {
		return canCollection(player.getId());
	}

	public boolean canCollection(long playerId) {
		return !getReapers().containsKey(playerId);
	}

	@Override
	public int getOncePickupNum() {
		return 1;
	}

	@Override
	public int getOncePickupColor() {
		int index = RandomTool.getResultIndexs(RandomType.groupRandom, ForLuckFruitManager.getInstance().getTreeOutputArticleColor()[articleColor], 1).get(0);
		return index;
	}

	@Override
	public int getTotalNum() {
		return totalNum;
	}

	@Override
	public synchronized void collection(int num) {
		leftNum -= num;
		leftNum = leftNum < 0 ? 0 : leftNum;
		setTitle(getCurrentTitle());
	}

	@Override
	public int getLeftNum() {
		return leftNum;
	}

	@Override
	public Game getBelongs() {
		return game;
	}

	@Override
	public synchronized void onBeCollection(Player player) {
		if (!inService) { // 还不能采集
			if (ForLuckFruitManager.logger.isInfoEnabled()) ForLuckFruitManager.logger.info(player.getLogString() + "[摘取祝福果，结果不能摘取]");
			return;
		}
		if (ForLuckFruitManager.logger.isInfoEnabled()) ForLuckFruitManager.logger.info(player.getLogString() + "[是否可以摘取祝福果{}]", new Object[] { this.canCollection(player) });
		if (this.canCollection(player)) {
			if (this.isInService() && player.getCurrentGame().contains(this)) {
				ForLuckFruitTimeBar bar = new ForLuckFruitTimeBar(player, this);
				player.getTimerTaskAgent().createTimerTask(bar, getCollectionBarTime(), com.fy.engineserver.sprite.TimerTask.type_采集);
				player.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), getCollectionBarTime(), Translate.text_490));
			} else {
				player.sendError(Translate.text_forluck_006);
				return;
			}
		} else {
			player.sendError(Translate.text_forluck_007);
			return;
		}
	}

	public synchronized void onBourn() {
		if (bournTime > 0) {
			ForLuckFruitManager.logger.error("[祝福果树] [出生] [发现已经出生了] [{},{}]", new Object[] { getX(), getY() });
			return;
		}
		getGame().addSprite(this);
		bournTime = SystemTime.currentTimeMillis();
		growupTime = bournTime + ForLuckFruitManager.getInstance().getGrowupTime();
		deadTime = growupTime + ForLuckFruitManager.getInstance().getDeadTime();
		setInService(false);
		setNameColor(ArticleManager.color_article[articleColor]);
		setTitle(getCurrentTitle());
		if (ForLuckFruitManager.logger.isInfoEnabled()) {
			ForLuckFruitManager.logger.info("[祝福果树已经种下][需要时间{}][title{}]", new Object[] { ForLuckFruitManager.getInstance().getGrowupTime(), getCurrentTitle() });
		}
	}

	public synchronized void onDead() {
		// 在地图上移除，家族记录的当前位置的NPC移除
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
		if (jiazu != null) {
			if (ForLuckFruitManager.logger.isInfoEnabled()) {
				ForLuckFruitManager.logger.info("[祝福果树死了][x{},y{}][家族祝福果:{}] [家族id:{}]", new Object[] { getX(), getY(), jiazu.getFruitNPCs().length, jiazuId });
			}
			for (int i = 0; i < jiazu.getFruitNPCs().length; i++) {
				ForLuckFruitNPC npc = jiazu.getFruitNPCs()[i];
				if (npc != null) {
					if (npc.getId() == this.getId()) {
						jiazu.getFruitNPCs()[i] = null;
						autoHarvest();
						break;
					}
				}
			}
		} else {
			ForLuckFruitManager.logger.error("[自动回收的时候家族不存在ID:{}]", new Object[] { jiazuId });
		}
		getGame().removeSprite(this);
	}

	public synchronized void autoHarvest() {
		setInService(false);
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
		if (jiazu != null) {
			if (ForLuckFruitManager.logger.isInfoEnabled()) ForLuckFruitManager.logger.info("[家族:{}id:{}的祝福果树自动收获了][数量:{}]", new Object[] { jiazu.getName(), jiazuId, leftNum });
			while (leftNum > 0) {
				int num = getOncePickupNum();
				int color = getOncePickupColor();
				jiazu.getForLuckFriutNums()[color] += num;
				jiazu.notifyFieldChange("forLuckFriutNums");
				leftNum--;
			}
		}
	}

	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
		setTitle(getCurrentTitle(growupTime - heartBeatStartTime));
	}

	@Override
	public Map<Long, Long> getReapers() {
		return reapers;
	}

	@Override
	public String getArticleName() {
		return articleName;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getArticleColor() {
		return articleColor;
	}

	public void setArticleColor(int articleColor) {
		this.articleColor = articleColor;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public void setReapers(Hashtable<Long, Long> reapers) {
		this.reapers = reapers;
	}

	@Override
	public long getReadTimebarTime() {
		return 3 * 1000L;
	}

	public long getJiazuId() {
		return jiazuId;
	}

	public void setJiazuId(long jiazuId) {
		this.jiazuId = jiazuId;
	}

	@Override
	public boolean isInService() {
		return inService;
	}

	public void setInService(boolean inService) {
		this.inService = inService;
	}

	@Override
	public long getCollectionBarTime() {
		return collectionBarTime;
	}

	@Override
	public void setCollectionBarTime(long collectionBarTime) {
		this.collectionBarTime = collectionBarTime;
	}

	/**
	 * 得到NPC当前的称号，显示在头顶
	 * @return
	 */
	public String getCurrentTitle() {
		return getCurrentTitle(growupTime - SystemTime.currentTimeMillis());
	}

	public String getCurrentTitle(long time) {
		StringBuffer sbf = new StringBuffer();

		sbf.append("<f color='").append(ArticleManager.color_article[articleColor]).append("'>");
		if (inService) {
			sbf.append("(").append(getLeftNum()).append("/").append(getTotalNum()).append(")");
		} else {
			sbf.append(TimeTool.instance.getShowTime(time));
		}
		sbf.append("</f>");
		return sbf.toString();
	}

	public long getBournTime() {
		return bournTime;
	}

	public void setBournTime(long bournTime) {
		this.bournTime = bournTime;
	}

	public long getGrowupTime() {
		return growupTime;
	}

	public void setGrowupTime(long growupTime) {
		this.growupTime = growupTime;
	}

	public long getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

	@Override
	public Object clone() {
		ForLuckFruitNPC p = new ForLuckFruitNPC();
		p.cloneAllInitNumericalProperty(this);
		p.country = country;
		p.windowId = this.windowId;
		p.setnPCCategoryId(this.getnPCCategoryId());
//		p.setCollectionBarTime(FeedSilkwormManager.getInstance().getCollectionBarTime());
		return p;
	}
}
