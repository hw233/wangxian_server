package com.fy.engineserver.menu;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 摘花
 * 
 * 
 *
 */
public class Option_SeedRipeAndPick extends Option{

	Player owner;

	long npcId;
	
	public long getNpcId() {
		return npcId;
	}

	public void setNpcId(long npcId) {
		this.npcId = npcId;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_采摘;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_3086;
	}
}
