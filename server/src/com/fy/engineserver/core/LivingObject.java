package com.fy.engineserver.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.fy.engineserver.activity.pickFlower.MagicWeaponNpc;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.language.Translate;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 描述游戏系统中一个有状态的生物
 * 
 * LivingObject有3个接口： 1）获得变量更新的标记，是一个Boolean数组，数组的下标就是对应的变量的编号
 * 2）针对某个变量的编号，获取其值，返回值为Object 3）清空所有的变量改变标志
 * 
 * 
 */

public abstract class LivingObject implements Comparable<LivingObject>, Cloneable, Constants {

	// 此变量用户Game的AroundChange搜集，提高效率
	public transient boolean lastLiveingSetContainsFlag;

	public LivingObject() {
		//
	}

	@Override
	protected void finalize() throws Throwable {
		//
	}

	public transient Random random = new Random();

	public static String getStateStr(int state) {
		switch (state) {
		case STATE_STAND:
			return Translate.text_3097;
		case STATE_MOVE:
			return Translate.text_2570;
		case STATE_CAST_FIRE:
			return Translate.text_3098;
		case STATE_DEAD:
			return Translate.text_3099;
		case STATE_CAST_PREPAIR:
			return Translate.text_3100;
		}
		return Translate.text_1211;
	}

	// 状态 定义在Constants接口
	// public static final byte STATE_STAND = 0;
	// public static final byte STATE_MOVE = 1;
	// public static final byte STATE_CAST_PREPAIR = 2;
	// public static final byte STATE_CAST_FIRE = 3;
	// public static final byte STATE_USE_ITEM = 4;
	// public static final byte STATE_DEAD = 5;

	// public static final byte UP = 0;
	// public static final byte DOWN = 1;
	// public static final byte LEFT = 2;
	// public static final byte RIGHT = 3;
	// public static final byte LEFT_UP = 4;
	// public static final byte LEFT_DOWN = 5;
	// public static final byte RIGHT_UP = 6;
	// public static final byte RIGHT_DOWN = 7;
	// public static final byte NONE = 8;

	@SimpleId
	protected long id = 1;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@SimpleVersion
	protected int version;

	@SimpleColumn(saveInterval = 600)
	protected int x;

	@SimpleColumn(saveInterval = 600)
	protected int y;

	/**
	 * 仅供avata用，看不懂问美术或单涛
	 */
	protected String avataRace = "";
	protected String avataSex = "";

	public String getAvataRace() {
		return avataRace;
	}

	public void setAvataRace(String avataRace) {
		this.avataRace = avataRace;
	}

	public String getAvataSex() {
		return avataSex;
	}

	public void setAvataSex(String avataSex) {
		this.avataSex = avataSex;
	}

	// 上一次心跳时的位置
	protected transient int lastHeartBeatX;
	protected transient int lastHeartBeatY;

	protected int viewWidth = 640;
	protected int viewHeight = 640;

	protected transient MoveTrace path = null;

	// 本次心跳开始的时间
	protected transient long heartBeatStartTime;
	// 两次心跳的时间间隔
	protected transient long interval;

	// 临时采用的变量，为了在游戏心跳函数中收集属性变化更快，此变量请不要作为其他任何用途 -- add by  2009-4-26
	protected transient ArrayList<FieldChangeEvent> filedChangeEventList = new ArrayList<FieldChangeEvent>();

	/**
	 * 获得变量更新的标记，是一个Boolean数组，数组的下标就是对应的变量的编号
	 * 
	 * @return
	 */
	public abstract boolean[] getAroundMarks();

	/**
	 * 针对某个变量的编号，获取其值，返回值为Object, 如果变量的编号不存在，抛出IllegalArgumentException
	 * 
	 * @param fieldId
	 * @return
	 */
	public abstract Object getValue(int fieldId);

	/**
	 * 清空所有的变量改变标志
	 */
	public abstract void clear();

	private transient boolean alive = true;

	/**
	 * 坐骑套装技能的一系列数值
	 */
	public transient Map<Integer, Long> suitSkillCDTime = new HashMap<Integer, Long>();
	public transient Map<Integer, Long> suitSkillLastingTime = new HashMap<Integer, Long>();
	/**
	 * 状态标记：不能回血
	 */
	private transient boolean canNotIncHp;

	public boolean isCanNotIncHp() {
		return canNotIncHp;
	}

	public void setCanNotIncHp(boolean canNotIncHp) {
		this.canNotIncHp = canNotIncHp;
	}
	// 坐骑魂石套装传染状态，被封魔时同时给对方封魔
	protected transient boolean infect1;
	
	public void setInfect1(boolean value){
		this.infect1 = value;
	}
	
	public boolean isInfect1(){
		return this.infect1;
	}
	// 坐骑魂石套装传染状态，被封魔时同时给对方封魔
	protected transient boolean infect2;
	
	public void setInfect2(boolean value){
		this.infect2 = value;
	}
	
	public boolean isInfect2(){
		return this.infect2;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * 设置活着的标记，如果此标记设置为true 在下一次心跳后，此生物将从game中清除。
	 * 
	 * @param b
	 */
	public void setAlive(boolean b) {
		alive = b;
	}

	/*
	 * 心跳函数，在这个函数中可以做一些逻辑运算
	 */
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		this.heartBeatStartTime = heartBeatStartTime;
		this.interval = interval;
		if (path != null) {
			path.moveFollowPath(heartBeatStartTime);
		}
	}

	public MoveTrace getMoveTrace() {
		return path;
	}

	public void setMoveTrace(MoveTrace trace) {
		path = trace;
		path.setLivingObject(this);
		path.setStartTimestamp(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		x = trace.getStartPointX();
		y = trace.getStartPointY();
		setState(STATE_MOVE);

		// if( this instanceof Monster){
		// Monster m = (Monster)this;
		// if(m.getName().equals("测试怪物移动")){
		// System.out.println("[lastpath="+(las!=null?las.toString():"null")+"]测试怪物移动1setMoveTrace:[time:"+(trace.getDestineTimestamp()-trace.getStartTimestamp())+"][st:"+trace.getStartTimestamp()+"][dt:"+trace.getDestineTimestamp()+"][totallen:"+trace.getTotalLength()+"][speed:"+m.getMoveSpeed()+"]"+trace.toString());
		// }
		// }
	}

	public void face(int x1, int y1, int x2, int y2) {
		int dx = (x1 > x2 ? x1 - x2 : x2 - x1);
		int dy = (y1 > y2 ? y1 - y2 : y2 - y1);
		if (y2 <= y1 && dy >= dx) {
			setDirection(UP);
		} else if (y2 >= y1 && dy >= dx) {
			setDirection(DOWN);
		} else if (x2 <= x1 && dx >= dy) {
			setDirection(LEFT);
		} else {
			setDirection(RIGHT);
		}
	}

	public void removeMoveTrace() {
		// if( this instanceof Monster){
		// Monster m = (Monster)this;
		// if(m.getName().equals("测试怪物移动")){
		// System.out.println("测试怪物移动removeMoveTrace "+com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		// }
		// }
		if (this instanceof MagicWeaponNpc) { // 法宝路径调试
			if (MagicWeaponManager.logger.isDebugEnabled()) {
				MagicWeaponManager.logger.debug("[法宝npc停止] [" + this.getId() + "]");
			}
		}
		path = null;
		setState(STATE_STAND);
	}

	// public void removeFromPath(LivingObject living) {
	// if (path != null) {
	// path.removeNotifyMark(living);
	// }
	// }

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		Class<? extends LivingObject> cl = getClass();
		try {
			return cl.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isViewWindowEnabled() {
		return viewWidth > 0 && viewHeight > 0;
	}

	public int compareTo(LivingObject o) {
		if (o == this) return 0;
		if (x < o.x) return -1;
		if (x > o.x) return 1;
		if (y < o.y) return -1;
		if (y > o.y) return 1;
		return 0;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getViewWidth() {
		return viewWidth;
	}

	public void setViewWidth(int viewWidth) {
		this.viewWidth = viewWidth;
	}

	public int getViewHeight() {
		return viewHeight;
	}

	public void setViewHeight(int viewHeight) {
		this.viewHeight = viewHeight;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof LivingObject) {
			LivingObject lo = (LivingObject) o;
			if (lo.getId() == getId() && lo.getClassType() == getClassType()) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return getClassType() << 24 + getId();
	}

	public long getInterval() {
		return interval;
	}

	public long getHeartBeatStartTime() {
		return heartBeatStartTime;
	}

	/**
	 * 将方向调整为面朝指定的点
	 * @param x
	 * @param y
	 */
	public void face(int x, int y) {
		face(this.x, this.y, x, y);
	}

	public abstract byte getState();

	public abstract void setState(byte state);

	public abstract void setDirection(byte direction);

	public abstract byte getDirection();

	/**
	 * 得到这个对象的类型，比如是Player还是NPC，或者动态事件等等
	 * 
	 * @return
	 */
	public abstract byte getClassType();

}
