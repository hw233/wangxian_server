package com.fy.engineserver.sprite.npc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.COLLECTION_NPC_MODIFY_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.TimeTool;

public class TaskCollectionNPC extends NPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// /** 采集过这个NPC的玩家列表 在接取任务时候要做判断，当玩家进入地图的时候清除再判断 */
	// private List<Long> playerIds = new ArrayList<Long>();
	/** 角色最后采集时间 */
	private Hashtable<Long, Long> playerCollectionMap = new Hashtable<Long, Long>();
	/** 对应的任务名字 */
	private List<String> taskNames = new ArrayList<String>();
	/** 物品颜色 */
	private int articleColor;
	/** 物品最少数量 */
	private int articleMinNum;
	/** 物品最 大数量 */
	private int articleMaxNum;
	/** 物品名字 */
	private String articleName;
	/** 读条时间 */
	private long collectionBarTime;

	/** 可采集间隔 */
	public static long reCollectionDistance = TimeTool.oneSecond * 1;

	@Override
	public byte getNpcType() {
		return Sprite.NPC_TYPE_COLLECTION;
	}

	private static Random random = new Random();

	public int getOnceDorpNum() {
		int num = 0;
		if (articleMinNum >= articleMaxNum) {
			num = 1;
		} else {
			num = random.nextInt(articleMaxNum - articleMinNum) + articleMinNum;
		}
		return num;
	}

	/**
	 * 能不能收集 <BR/>
	 * 先判断是否有符合的任务,再判断NPC身上是否记录了玩家的采集信息，判断是否可以再采集了
	 * @param player
	 * @return
	 */
	public boolean canCollection(Player player) {
//		TaskManager taskManager = TaskManager.getInstance();
		boolean hasNotCompleteTask = false;
		for (String taskName : getTaskNames()) {
//			Task task = taskManager.getTask(taskName);
			List<Task> groupTask = TaskManager.getInstance().getTaskGroupByGroupName(taskName);
			if (groupTask != null) {
				for(Task t : groupTask){
					if(t != null && TaskManager.countryFit(player, t)){
						int taskStatus = player.getTaskStatus(t);
						if (TaskSubSystem.logger.isWarnEnabled()) TaskSubSystem.logger.warn(player.getLogString() + "[采集物品] [id:{}] [任务名字:{}][状态:{}]", new Object[] {t.getId(), taskName, taskStatus });
						if (taskStatus == 1) {
							hasNotCompleteTask = true;
							break;
						}
					}
				}
			} else {
				if (TaskSubSystem.logger.isWarnEnabled()) TaskSubSystem.logger.warn(player.getLogString() + "[采集物品][任务不存在, 名字:{}]", new Object[] { taskName });
			}
		}
		return hasNotCompleteTask ? canCollection(player.getId()) : hasNotCompleteTask;
	}

	private boolean canCollection(long playerId) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (TaskSubSystem.logger.isWarnEnabled()) TaskSubSystem.logger.warn("[采集物品][NPC:{}是否包含此人的采集信息:{}][map长度{}]", new Object[] { getId(), getPlayerCollectionMap().containsKey(playerId), getPlayerCollectionMap().size() });
		if (!getPlayerCollectionMap().containsKey(playerId)) {
			return true;
		}
		return (getPlayerCollectionMap().get(playerId) + reCollectionDistance) <= now;
	}

	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PlayerManager playerManager = GamePlayerManager.getInstance();
		// 将不在线的角色移除，检测玩家采集时间
		List<Long> removeIds = new ArrayList<Long>();
		for (Iterator<Long> itor = getPlayerCollectionMap().keySet().iterator(); itor.hasNext();) {
			try {
				long playerId = itor.next();
				Player player = playerManager.getPlayer(playerId);
				long collectionTime = getPlayerCollectionMap().get(playerId);
				if (collectionTime + reCollectionDistance <= now) {// 时间满足采集 并且玩家任务未完成
					// 通知玩家刷新了
					removeIds.add(playerId);// 无用数据 移除 减少下次心跳运算
					COLLECTION_NPC_MODIFY_REQ req = new COLLECTION_NPC_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, new long[] { getId() });
					player.addMessageToRightBag(req);
					// }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 移除
		for (Long removeId : removeIds) {
			getPlayerCollectionMap().remove(removeId);
		}
	}

	public List<String> getTaskNames() {
		return taskNames;
	}

	public void setTaskNames(List<String> taskNames) {
		this.taskNames = taskNames;
	}

	public int getArticleColor() {
		return articleColor;
	}

	public void setArticleColor(int articleColor) {
		this.articleColor = articleColor;
	}

	public int getArticleMinNum() {
		return articleMinNum;
	}

	public void setArticleMinNum(int articleMinNum) {
		this.articleMinNum = articleMinNum;
	}

	public int getArticleMaxNum() {
		return articleMaxNum;
	}

	public void setArticleMaxNum(int articleMaxNum) {
		this.articleMaxNum = articleMaxNum;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public Hashtable<Long, Long> getPlayerCollectionMap() {
		return playerCollectionMap;
	}

	public void setPlayerCollectionMap(Hashtable<Long, Long> playerCollectionMap) {
		this.playerCollectionMap = playerCollectionMap;
	}

	public long getCollectionBarTime() {
		return collectionBarTime;
	}

	public void setCollectionBarTime(long collectionBarTime) {
		this.collectionBarTime = collectionBarTime;
	}

	@Override
	public Object clone() {
		TaskCollectionNPC collectionNPC = new TaskCollectionNPC();
		collectionNPC.cloneAllInitNumericalProperty(this);
		collectionNPC.setTaskNames(taskNames);
		collectionNPC.setArticleColor(articleColor);
		collectionNPC.setArticleMaxNum(articleMaxNum);
		collectionNPC.setArticleMinNum(articleMinNum);
		collectionNPC.setArticleName(articleName);
		collectionNPC.setCollectionBarTime(collectionBarTime);

		collectionNPC.setnPCCategoryId(this.getnPCCategoryId());
		collectionNPC.setWindowId(this.getWindowId());
		return collectionNPC;
	}
}
