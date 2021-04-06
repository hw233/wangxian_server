package com.fy.engineserver.shortcut;

/**
 * 施放武功的快捷键。 
 * 
 */
public class GongFukillShortcut extends Shortcut {
	public short skillIds[] = new short[0];
	public GongFukillShortcut(short skillIds[]) {
		if( skillIds!= null)
		this.skillIds = skillIds;		
	}
}
