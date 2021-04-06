package com.fy.engineserver.menu.society.chatGroup;

import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 玩家同意加入群组
 * @author Administrator
 *
 */
public class Option_Player_Agree extends Option {
	
	private Player host; // 群主
	private long groupId;
	private ChatMessageService chatMessageService = ChatMessageService.getInstance();
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_加入聊天分组申请;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
////		SocietySubSystem.logger.info("[成员同意添加聊天分组] ["+player.getName()+"] []");
//		if(SocialManager.logger.isInfoEnabled())
//			SocialManager.logger.info("[成员同意添加聊天分组] [{}] []", new Object[]{player.getName()});
//		try {
//			Relation relation = SocialManager.getInstance().getRelationById(player.getId());
//			List<Long> list = relation.getChatGrouplist();
//			if(!list.contains(groupId)){
//				
//				ChatGroup cg = ChatGroupManager.getInstance().getChatGroup(groupId);
//				chatMessageService.sendMessageToGroup(player.getName()+"加入了"+host.getName()+"组", groupId);
//				relation.addChatGroup(groupId);
//				cg.add(player);
//				player.send_HINT_REQ("你加入了"+cg.getName()+"组");
//				
//				if(SocialManager.logger.isDebugEnabled()){
//					SocialManager.logger.debug("[玩家同意加入群组] ["+player.getLogString()+"] ["+groupId+"] ");
//				}
//				
//			}else{
//				player.send_HINT_REQ("你已经加入其中");
//			}
//		} catch (Exception e) {
//			if(SocietySubSystem.logger.isInfoEnabled())
//				SocietySubSystem.logger.info("[成员同意添加聊天分组异常] ["+player.getName()+"] []" ,e);
//		}
	}

	public Player getHost() {
		return host;
	}
	public void setHost(Player host) {
		this.host = host;
	}
	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
}
