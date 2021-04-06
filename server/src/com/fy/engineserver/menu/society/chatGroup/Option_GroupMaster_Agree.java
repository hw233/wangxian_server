package com.fy.engineserver.menu.society.chatGroup;

import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 群主同意玩家加入群组
 * @author Administrator
 *
 */
public class Option_GroupMaster_Agree extends Option {
	
	private Player applyor; // 申请者
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
//		
//		if(player.getId() == groupId){
//			
//			ChatGroup cg = ChatGroupManager.getInstance().getChatGroup(groupId);
//			if(cg.getMembers().contains(applyor.getId())){
//				player.send_HINT_REQ(applyor.getName()+"已经在"+cg.getName()+"中");
//				return ;
//			}
//			if(cg.getMembers().size() < SocialManager.GROUP_MAX_NUM){
//				
//				chatMessageService.sendMessageToGroup(applyor.getName()+"加入了"+player.getName()+"组", groupId);
//				cg.add(applyor);
//				Relation relation = SocialManager.getInstance().getRelationById(applyor.getId());
////				relation.addChatGroup(groupId);
//				if(applyor.isOnline()){
//					applyor.send_HINT_REQ("你加入了"+player.getName()+"组");
//				}
//				if(SocialManager.logger.isDebugEnabled()){
//					SocialManager.logger.debug("[群主同意玩家加入群组] ["+player.getLogString()+"] ["+applyor.getLogString()+"] ");
//				}
//				
//			}else{
//				player.send_HINT_REQ("群组人数已达上限");
//			}
//		}else{
//			SocialManager.logger.error("[群主同意某人加入组错误] [他不是群主] ["+player.getLogString()+"] [群id:"+groupId+"]");
//		}
	}

	public Player getApplyor() {
		return applyor;
	}
	public void setApplyor(Player applyor) {
		this.applyor = applyor;
	}
	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
}
