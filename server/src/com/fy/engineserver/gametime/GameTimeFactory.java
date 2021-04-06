package com.fy.engineserver.gametime;

import java.nio.channels.IllegalSelectorException;

public class GameTimeFactory {

	public enum SourceType {
		SYSTEM, SELF
	}

	static AbstractGameTime createCurrentTime(SourceType type) {
		switch (type) {
		case SYSTEM:
			return UseSystemTime.getInstance();
		case SELF:
			return UseSelfTime.getInstance();
		default:
			break;
		}

		throw new IllegalSelectorException();
	}
}
