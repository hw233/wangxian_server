package com.fy.engineserver.core;

import java.util.ArrayList;
import java.util.List;

public class WorldMapInfo {	
	String mapName;
	List<WorldMapLocation> ls = new ArrayList<WorldMapLocation>();

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	public int getVersion(){
		if( mapName!=null){			
			GameInfo gi = GameManager.getInstance().getGameInfo(mapName);
			if( gi!=null) return gi.getVersion();
		}
		return 0;
	}
	
	public WorldMapLocation[] getWorldMapLocations() {
		return ls.toArray(new WorldMapLocation[0]);
	}

	public WorldMapLocation getWorldMapLocation(String mapName) {
		WorldMapLocation[] wls = getWorldMapLocations();
		for (int i = 0; i < wls.length; i++) {
			WorldMapLocation wl = wls[i];
			if (wl.getMapName().equals(mapName)) {
				return wl;
			}
		}
		return null;
	}

	public void removeWorldMapLocation(String mapName) {
		for (int i = ls.size() - 1; i >= 0; i--) {
			if (ls.get(i).getMapName().equals(mapName)) {
				ls.remove(i);
			}
		}
	}
}
