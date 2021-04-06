package com.fy.engineserver.menu;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.question.Question;
import com.fy.engineserver.sprite.Player;

/**
 * 中秋活动答题
 * 
 * 
 *
 */
public class Option_MidAutumnQuestion extends Option{

	Player owner;

	long npcId;
	
	Question question;
	
	int index = -1;
	
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

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
//
//	public int getIndex() {
//		return index;
//	}
//
//	public void setIndex(int index) {
//		this.index = index;
//	}

	public void doInput(Game game, Player player, String inputContent) {}

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
