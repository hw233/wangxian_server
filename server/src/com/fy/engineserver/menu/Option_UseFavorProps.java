package com.fy.engineserver.menu;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 使用好感度道具
 * 
 * 
 *
 */
public class Option_UseFavorProps extends Option{

	Player owner;
	
	int indexOfKnapsack;

	long favorPlayerId;
	
	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getIndexOfKnapsack() {
		return indexOfKnapsack;
	}

	public void setIndexOfKnapsack(int indexOfKnapsack) {
		this.indexOfKnapsack = indexOfKnapsack;
	}

	public long getFavorPlayerId() {
		return favorPlayerId;
	}

	public void setFavorPlayerId(long favorPlayerId) {
		this.favorPlayerId = favorPlayerId;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
//		UseFavorPropsEvent event = new UseFavorPropsEvent();
//		event.setFavorPlayerId(favorPlayerId);
//		event.setGame(game);
//		event.setIndexOfKnapsack(indexOfKnapsack);
//		event.setOwner(owner);
//		game.getSubThread().addEvent(event);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_增加友好度;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_3694;
	}
}
