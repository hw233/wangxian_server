package com.fy.engineserver.menu;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.MailSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 提取付费邮件确认操作
 * 
 * 
 *
 */
public class Option_GetOutPriceArticleFromMail extends Option{

	Player owner;
	
	long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		MailSubSystem mss = MailSubSystem.getInstance();
		if(mss != null){
			mss.getPriceMail(player, id);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_提取付费邮件;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5260;
	}
}
