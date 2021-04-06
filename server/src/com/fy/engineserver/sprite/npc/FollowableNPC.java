package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Road;
import com.fy.engineserver.core.g2d.SignPost;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_FOLLOWNPC_FARAWAY_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_FOLLOWNPC_NEAR_REQ;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

/**
 * 可跟随玩家的NPC<BR/>
 * 1.护送任务需要用<BR/>
 * 2.镖车需要用<BR/>
 * 3.在角色中记录。且唯一<BR/>
 * 4.一定要设置当前地图<BR/>
 * 
 */
@SuppressWarnings("serial")
public abstract class FollowableNPC extends NPC {

	static int short_radius = 150;// 最小半径.在这个半径内就不寻路了.减少计算
	static int target_radius = 150;// 到达目的地的响应半径
	static int call_radius = 1000;// 求救距离(同地图)
	static double moveChangeRate = 0.6;// 重新计算路径比例(当前路径走过了多少后开始计算新的路径)

	/** 主人ID */
	private long ownerId;
	/** 存活 */
	private long life;
	/** 出生时间 */
	private long bornTime;
	/** 死亡时间 */
	private long deadTime;
	/** 跟随半径,主人在半径外则停止 */
	private int radius;
	/** 当前地图 */
	private Game currentGame;
	/** 目标地图 */
	private String targetMap;
	/** 目标NPC名字 */
	private String targetNPCName;

	/** 是否要跟随主人传送 */
	private boolean transferWithOwner;
	/** 级别-与形象相关 */
	private int grade;
	/** 是否已经通知主人距离太远了 */
	// TODO
	private boolean hasNoticeOwnerFaraway;

	/** 目的地所在X */
	private int targetX;
	/** 目的地所在Y */
	private int targetY;

	@Override
	public byte getNpcType() {
		return NPC_TYPE_CONVOY;
	}

	public boolean needFindPath() {
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(ownerId);
			if (!player.isOnline()) {// 不在线，直接返回false
				return false;
			}
			if (!getGameName().equals(player.getGame())) {
				return false;
			}
			return !isNear(this.getX(), this.getY(), player.getX(), player.getY(), short_radius);
		} catch (Exception e) {
			TaskSubSystem.logger.error("[判断护送NPC距离异常]", e);
			return true;
		}
	}

	/**
	 * 是否到达目标了,做不同的事儿
	 * @return
	 */
	public boolean nearTarget() {
		if (!getCurrentGame().gi.displayName.equals(targetMap)) {
			// if (TaskSubSystem.logger.isDebugEnabled()) {
			// TaskSubSystem.logger.debug("[NPC和目标所在地图不一样][当前:{}][目标:{}]", new Object[] { getCurrentGame().gi.displayName, targetMap });
			// }
			return false;
		} else {
			// if (TaskSubSystem.logger.isDebugEnabled()) {
			// TaskSubSystem.logger.debug("[NPC和目标所在地图相同 ][当前:{}][目标:{}]", new Object[] { getCurrentGame().gi.displayName, targetMap });
			// }
		}
		return isNear(this.getX(), this.getY(), targetX, targetY, target_radius);
	}

	/**
	 * 当接近目标的时候要做的事儿.任务,要设置任务目标完成等等
	 */
	public abstract void onNearTarget();

	public boolean ownerIsAround() {
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(ownerId);
			if (!player.isOnline()) {// 不在线，直接返回false
				return false;
			}
			if (!getGameName().equals(player.getGame())) {
				if (TaskManager.logger.isInfoEnabled()) TaskManager.logger.info(player.getLogString() + "[人和NPC分离][在地图:{}][护送的NPC:{}][NPC在地图:{}]", new Object[] { player.getGame(), getName(), getGameName() });
				return false;
			}
			// 判断距离;用半径为radius的圆的外切正方形作为区域判断,先取出来必要属性
			int playerX = player.getX();
			int playerY = player.getY();
			return isNear(this.getX(), this.getY(), playerX, playerY, radius);
		} catch (Exception e) {
			TaskManager.logger.error("[NPC:{}寻路][异常]", new Object[] { getName() }, e);
		}
		return false;
	}

	protected void pathFinding() {
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(ownerId);
		} catch (Exception e) {
			getCurrentGame().removeSprite(this);
			TaskSubSystem.logger.error("[NPC:{}][寻路][主人不见了,主人{}][从地图中移除]", new Object[] { getName(), ownerId }, e);
			return;
		}
		Game de = this.currentGame;
		if (de == null) {
			TaskSubSystem.logger.error("[NPC:{}][寻路][当前地图不存在]", new Object[] { getName() });
			return;
		}
		int dx = player.getX();
		int dy = player.getY();
		int distance = 20;
		Point s = new Point(this.getX(), this.getY());
		Point e = new Point(dx, dy);
		int L = Graphics2DUtil.distance(s, e);
		int speed = 1;
		if (this.getSpeed() > 0) {
			speed = this.getSpeed();
		}
		if (L > distance && de.getGameInfo().navigator.isVisiable(s.x, s.y, e.x, e.y)) {
			int L1 = L - distance;
			int L2 = distance;

			Point ps[] = new Point[2];
			short roadLen[] = new short[1];
			ps[0] = new Point(s.x, s.y);
			ps[1] = new Point((L1 * e.x + L2 * s.x) / L, (L1 * e.y + L2 * s.y) / L);
			roadLen[0] = (short) L1;
			MoveTrace path = new MoveTrace(roadLen, ps, (long) (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (long) L1 * 1000 / speed));
			this.setMoveTrace(path);
		} else if (L > distance) {
			SignPost sps[] = de.getGameInfo().navigator.findPath(s, e);
			if (sps == null) {
				
				return;
			}
			Point ps[] = new Point[sps.length + 1];
			short roadLen[] = new short[sps.length];
			for (int i = 0; i < ps.length; i++) {
				if (i == 0) {
					ps[i] = new Point(s.x, s.y);
				} else {
					ps[i] = new Point(sps[i - 1].x, sps[i - 1].y);
				}
			}
			int totalLen = 0;
			for (int i = 0; i < roadLen.length; i++) {
				if (i == 0) {
					roadLen[i] = (short) Graphics2DUtil.distance(ps[0], ps[1]);
				} else {
					Road r = de.getGameInfo().navigator.getRoadBySignPost(sps[i - 1].id, sps[i].id);
					if (r != null) {
						roadLen[i] = r.len;
					} else {
						roadLen[i] = (short) Graphics2DUtil.distance(ps[i], ps[i + 1]);
					}
				}
				totalLen += roadLen[i];
			}
			MoveTrace path = new MoveTrace(roadLen, ps, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + totalLen * 1000 / speed);
			this.setMoveTrace(path);
		}
	}

	/**
	 * 是否在某个位置的周围.
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isNear(int thisX, int thisY, int x, int y, int radius) {
		// int thisX = this.getX();
		// int thisY = this.getY();
		if ((thisX + radius >= x) && (thisX - radius <= x) && (thisY + radius >= y) && (thisY - radius <= y)) {
			return true;
		}
		return false;
	}

	long lasthaertbeat = 0L;

	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
		if ((heartBeatStartTime - lasthaertbeat) > 2000L) {
			lasthaertbeat = heartBeatStartTime;
		}
		if (nearTarget()) {
			onNearTarget();
			return;
		}
		Player owner = null;
		try {
			owner = GamePlayerManager.getInstance().getPlayer(ownerId);
		} catch (Exception e1) {
			getCurrentGame().removeSprite(this);
			TaskSubSystem.logger.error("[护送NPC:{" + getName() + "}][心跳][主人不在了主人ID:{" + ownerId + "}][从当前地图移除]", e1);
			return;
		}
		if ((heartBeatStartTime - bornTime) >= life) {// 不用实时判断
			getCurrentGame().removeSprite(this);
			owner.setFollowableNPC(null);
			// 通知取消箭头
			owner.addMessageToRightBag(new NOTICE_CLIENT_FOLLOWNPC_NEAR_REQ(GameMessageFactory.nextSequnceNum()));
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn(owner.getLogString() + "[护送NPC:{}颜色:{}时间到了自动消失]", new Object[] { getName(), getGrade() });
			}
			return;
		}
		// 处理是否停止
		if (getMoveTrace() == null || (getMoveTrace().alreadyMoved() > moveChangeRate)) {// 原来的路径没走完，则继续走，不做处理
			if (ownerIsAround()) {
				if (hasNoticeOwnerFaraway) {
					hasNoticeOwnerFaraway = false;
					if (owner.isOnline()) {
						if (TaskSubSystem.logger.isWarnEnabled()) {
							TaskSubSystem.logger.warn(owner.getLogString() + "[护送NPC:{}][回到视野][通知客户端取消]");
						}
						owner.addMessageToRightBag(new NOTICE_CLIENT_FOLLOWNPC_NEAR_REQ(GameMessageFactory.nextSequnceNum()));
					}
				}
				if (needFindPath()) {
					pathFinding();
				} else {
				}
			} else {
				if (hasNoticeOwnerFaraway) {// 如果已经通知了主人远离但是主人不在线了,为了保证主人再次上线能够收到消息,把发送状态设置为false
					if (!owner.isOnline()) {
						hasNoticeOwnerFaraway = false;
						if (TaskSubSystem.logger.isWarnEnabled()) {
							TaskSubSystem.logger.warn(owner.getLogString() + "[护送NPC:{}][主人不在线][取消发送状态]");
						}
					}
				} else if (!hasNoticeOwnerFaraway) {
					if (owner.isOnline()) {
						hasNoticeOwnerFaraway = true;
						if (TaskSubSystem.logger.isWarnEnabled()) {
							TaskSubSystem.logger.warn(owner.getLogString() + "[护送NPC:{}][离开视野][通知主人远离镖车]", new Object[] { getName() });
						}
						owner.addMessageToRightBag(new NOTICE_CLIENT_FOLLOWNPC_FARAWAY_REQ(GameMessageFactory.nextSequnceNum(), getGameName(), getX(), getY()));
					}
				}
			}
		}
		// print = false;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public long getLife() {
		return life;
	}

	public void setLife(long life) {
		this.life = life;
	}

	public long getBornTime() {
		return bornTime;
	}

	public void setBornTime(long bornTime) {
		this.bornTime = bornTime;
	}

	public long getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Game getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}

	public boolean isTransferWithOwner() {
		return transferWithOwner;
	}

	public void setTransferWithOwner(boolean transferWithOwner) {
		this.transferWithOwner = transferWithOwner;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getTargetMap() {
		return targetMap;
	}

	public void setTargetMap(String targetMap) {
		this.targetMap = targetMap;
	}

	public String getTargetNPCName() {
		return targetNPCName;
	}

	public void setTargetNPCName(String targetNPCName) {
		this.targetNPCName = targetNPCName;
	}

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	@Override
	public String toString() {
		return "FollowableNPC [ownerId=" + ownerId + ", life=" + life + ", bornTime=" + bornTime + ", deadTime=" + deadTime + ", radius=" + radius + ", currentGame=" + currentGame + ", targetMap=" + targetMap + ", targetNPCName=" + targetNPCName + ", transferWithOwner=" + transferWithOwner + ", grade=" + grade + ", hasNoticeOwnerFaraway=" + hasNoticeOwnerFaraway + ", targetX=" + targetX + ", targetY=" + targetY + ", lasthaertbeat=" + lasthaertbeat + "]";
	}

}
