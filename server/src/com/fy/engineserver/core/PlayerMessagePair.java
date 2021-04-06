package com.fy.engineserver.core;

import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Message;
public class PlayerMessagePair {
	public final Player player;
	public final Message message;
	public final Object attachment;

	public PlayerMessagePair(Player player, Message message, Object attachment) {
		this.message = message;
		this.player = player;
		this.attachment = attachment;
	}
}
