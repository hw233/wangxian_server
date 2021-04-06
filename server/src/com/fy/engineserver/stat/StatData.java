package com.fy.engineserver.stat;

import com.fy.engineserver.datasource.language.Translate;
import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.cache.Cacheable;

public class StatData implements Cacheable {
	
	public static final int STAT_KILLING_NUM=0;
	
	public static final int STAT_BATTLE_FIELD_KILLING_NUM=1;
	
	public static final int STAT_BATTLE_FIELD_KILLING_NUM_WEEKLY=2;
	
	public static final int STAT_BATTLE_FIELD_GLORY=3;
	
	public static final int STAT_BATTLE_FIELD_GLORY_WEEKLY=4;
	
	public static final int STAT_INCOME_FORM_AU=5;
	
	public static final int STAT_INCOME_FORM_AU_WEEKLY=6;
	
	public static final int STAT_ONLINE_TIME=7;
	
	public static final int STAT_ONLINE_DAYS=8;
	
	public static final int STAT_CURRENT_LEVEL_ONLINE_TIME=9;
	
	public static final int STAT_DUEL_TIMES=10;
	
	public static final int STAT_DUEL_WIN_TIMES=11;
	
	public static final int STAT_DUEL_LOSE_TIMES=12;
	
	public static final int STAT_CURRENT_DAY_ONLINE_TIME=13;
	
	public static final int STAT_CURRENT_WEEK_ONLINE_DAYS=14;
	
	public static final int STAT_LAST_WEEK_ONLINE_DAYS=15;
	
	public static final int STAT_HONOR_KILLING_NUM=16;
	
	public static final int STAT_ARATHI_WIN_TIMES=17;
	
	public static final int STAT_COMBAT_5V5_WIN_TIMES=18;
	
	public static final int STAT_COMBAT_10V10_WIN_TIMES=19;
	
	public static final int STAT_QUIT_GANG_TIME=20;
	
	public static final int STAT_PLAYER_CREATE_TIME=21;
	
	public static final int STAT_PRENTICE_NUM=22;
	
	public static final int STAT_LOGINACTIVITY_NUW=23;
	
	public static final String[] STAT_NAMES={Translate.text_207,Translate.text_5957,Translate.text_5958,Translate.text_5959,Translate.text_5960,Translate.text_2355,
		Translate.text_5961,Translate.text_5962,Translate.text_5963,Translate.text_5964,Translate.text_5965,Translate.text_5966,Translate.text_5967,Translate.text_5360,
		Translate.text_5968,Translate.text_5969,Translate.text_5970,Translate.text_5971,Translate.text_5972,Translate.text_5973,Translate.text_5974,Translate.text_5975,Translate.text_5976,Translate.text_6381};
		

	private long playerId;
	

	private int statId;
	
	private long value;
	
	/**
	 * 上次更新时间，约定如果此值为0，cache服务器认为是新增，进行插入数据库操作
	 */
	private long lastUpdateTime;
	
	private boolean newData;
	
	private boolean dirty;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
		dirty = true;
	}

	public int getStatId() {
		return statId;
	}

	public void setStatId(int statId) {
		this.statId = statId;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
		dirty = true;
	}
	
	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isNewData() {
		return newData;
	}

	public void setNewData(boolean newData) {
		this.newData = newData;
	}

	public int getSize(){
		int size=0;
		size+=CacheSizes.sizeOfBoolean();
		size+=CacheSizes.sizeOfLong()*2;
		size+=CacheSizes.sizeOfInt();
		return size;
	}
	
}
