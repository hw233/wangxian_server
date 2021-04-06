package com.fy.engineserver.homestead.cave;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 主建筑
 * 
 * 
 */
@SimpleEmbeddable
public class CaveMainBuilding extends CaveBuilding {

	public CaveMainBuilding() {
		setType(CAVE_BUILDING_TYPE_MAIN);
	}

	@Override
	public void modifyName() {
		super.modifyName();
	}
}