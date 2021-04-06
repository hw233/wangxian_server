package com.fy.engineserver.activity.TransitRobbery.model;

import java.util.Arrays;

public class TempGuanliAvata {
	public long playerId;
	
	public String feishengjilu;
	
	public String[] avatar;
	
	public byte[] avatarType;
	
	@Override
	public String toString() {
		return "TempGuanliAvata [playerId=" + playerId + ", feishengjilu=" + feishengjilu + ", avatar=" + Arrays.toString(avatar) + ", avatarType=" + Arrays.toString(avatarType) + "]";
	}



	public TempGuanliAvata(long playerId, String feishengjilu, String[] avatar, byte[] avatarType) {
		super();
		this.playerId = playerId;
		this.feishengjilu = feishengjilu;
		this.avatar = avatar;
		this.avatarType = avatarType;
	}
	
}
