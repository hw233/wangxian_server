package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.MAP_POLYGON_MODIFY_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 门 NPC
 * 
 * 所有的门NPC都必须是此类的子类
 * 
 * 
 * 
 * 
 */
public class DoorNpc extends NPC implements Cloneable {

	private static final long serialVersionUID = -4393238389156456469L;

	// 是否关闭，关闭情况下，碰撞区域其作用。
	boolean isClosed = true;

	// 门对应的碰撞区域，必须是顺时针的
	short[] polygonX;
	short[] polygonY;

	public boolean isClosed() {
		return isClosed;
	}

	public void setIsClosed(boolean b) {
		this.isClosed = b;
	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}

	public short[] getPolygonX() {
		return polygonX;
	}

	public void setPolygonX(short[] polygonX) {
		this.polygonX = polygonX;
	}

	public short[] getPolygonY() {
		return polygonY;
	}

	public void setPolygonY(short[] polygonY) {
		this.polygonY = polygonY;
	}

	public void openDoor(Game game) {

		short[] xs = getPolygonX();
		short[] ys = getPolygonY();
		MAP_POLYGON_MODIFY_REQ req = new MAP_POLYGON_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, xs, ys);

		LivingObject los[] = game.getLivingObjects();
		for (int i = 0; i < los.length; i++) {
			if (los[i] instanceof Player) {
				Player p = (Player) los[i];
				p.addMessageToRightBag(req);
			}
		}

		this.setAlive(false);
	}

	public void closeDoor(Game game) {

		short[] xs = getPolygonX();
		short[] ys = getPolygonY();
		MAP_POLYGON_MODIFY_REQ req = new MAP_POLYGON_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, xs, ys);

		LivingObject los[] = game.getLivingObjects();
		for (int i = 0; i < los.length; i++) {
			if (los[i] instanceof Player) {
				Player p = (Player) los[i];
				p.addMessageToRightBag(req);
			}
		}
	}

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		DoorNpc p = new DoorNpc();
		p.cloneAllInitNumericalProperty(this);

		p.isClosed = isClosed;
		p.polygonX = polygonX;
		p.polygonY = polygonY;
		p.country = country;

		p.setnPCCategoryId(this.getnPCCategoryId());

		return p;
	}
}
