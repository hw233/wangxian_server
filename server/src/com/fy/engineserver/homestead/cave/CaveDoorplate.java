package com.fy.engineserver.homestead.cave;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 门牌
 * 
 * 
 */

@SimpleEmbeddable
public class CaveDoorplate extends CaveBuilding {

	public CaveDoorplate() {
		setType(CAVE_BUILDING_TYPE_DOORPLATE);
	}

	@Override
	public void modifyName() {
		super.modifyName();
	}
}
