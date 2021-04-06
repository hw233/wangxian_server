package com.fy.engineserver.menu.articleProtect;

import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_ArticleProtectBlock extends Option {
	
	private long entityID;
	private int entityIndex;
	private long propID;
	private int propIndex;

	public Option_ArticleProtectBlock() {}
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		ArticleProtectManager.instance.option_block(player, entityID, entityIndex, propID, propIndex);
	}

	public void setEntityID(long entityID) {
		this.entityID = entityID;
	}

	public long getEntityID() {
		return entityID;
	}

	public void setEntityIndex(int entityIndex) {
		this.entityIndex = entityIndex;
	}

	public int getEntityIndex() {
		return entityIndex;
	}

	public void setPropID(long propID) {
		this.propID = propID;
	}

	public long getPropID() {
		return propID;
	}

	public void setPropIndex(int propIndex) {
		this.propIndex = propIndex;
	}

	public int getPropIndex() {
		return propIndex;
	}
}
