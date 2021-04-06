package com.fy.engineserver.menu;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
/**
 * 喊话确认功能
 * 
 * 
 *
 */
public class Option_Confirm_Chat_Need_Article extends Option{

	ChatMessage chatMessage;
	
	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		ChatMessageService cms = ChatMessageService.getInstance();
		if(cms != null){
			cms.confirmSendWorldChatMessage(player, chatMessage);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_TUOYUN;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
