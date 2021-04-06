package com.fy.engineserver.menu.society.chatGroup;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.society.SocietySubSystem;
import com.fy.engineserver.sprite.Player;

/**
 * 群主不同意玩家加入群组
 * @author Administrator
 *
 */
public class Option_GroupMaster_DisAgree extends Option {
	
	private Player p;
	private long groupId;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_加入聊天分组申请;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if(player.getId() != groupId){
			if(p.isOnline()){
				String description = Translate.translateString(Translate.text_拒绝了你的入群申请,new String[][]{{Translate.PLAYER_NAME_1,player.getName()}});
				p.send_HINT_REQ(description);
			}
			
		}else{
//			SocietySubSystem.logger.error("[群主拒绝加入聊天分组失败] [不是群主]  ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [分组id"+groupId+"]");
			SocietySubSystem.logger.error("[群主拒绝加入聊天分组失败] [不是群主]  [{}] [{}] [{}] [分组id{}]", new Object[]{player.getId(),player.getName(),player.getUsername(),groupId});
		}
		
		
	}

	public Player getP() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
}
