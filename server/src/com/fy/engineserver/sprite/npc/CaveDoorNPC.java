package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.MAP_POLYGON_MODIFY_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;

public class CaveDoorNPC extends CaveNPC implements com.xuanzhi.tools.cache.Cacheable {

	/** 形象设置 */
	private String[] avatas1;

	public CaveDoorNPC() {

	}

	@Override
	public int getSize() {
		return super.getSize();
	}

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

	@Override
	public byte getNpcType() {
		return Sprite.NPC_TYPE_CAVE_DOOR;
	}

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
	}

	public String[] getAvatas1() {
		return avatas1;
	}

	public void setAvatas1(String[] avatas1) {
		this.avatas1 = avatas1;
	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		CaveDoorNPC p = new CaveDoorNPC();
		p.cloneAllInitNumericalProperty(this);
		p.setAvatas(this.getAvatas());
		p.setAvatas1(this.getAvatas1());
		p.isClosed = isClosed;
		p.polygonX = polygonX;
		p.polygonY = polygonY;
		p.country = country;

		p.setnPCCategoryId(this.getnPCCategoryId());
		p.setWindowId(this.getWindowId());
		return p;
	}
}
