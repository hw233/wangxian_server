package com.fy.engineserver.tournament.data;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;

/**
 * 武圣雕像
 * 
 * 
 */
public class StatueForTournament {
	/** 雕像位置 */
	private Point point;
	/** 地图名字 */
	private String mapName;
	/** NPCID */
	private int categoryId;
	/** 称号 */
	private String title;
	/** 名字颜色 */
	private int nameColor;
	/** NPC粒子 */
	private String particleName;
	/** 名次 */
	private int index;

	private int windowId;

	public StatueForTournament(Point point, String mapName, int categoryId, String title, int nameColor, String particleName, int index, int windowId) {
		this.point = point;
		this.mapName = mapName;
		this.categoryId = categoryId;
		this.title = title;
		this.nameColor = nameColor;
		this.particleName = particleName;
		this.index = index;
		this.windowId = windowId;
	}

	public void putNpcLike(Player player) {
		for (int i = 0; i < 4; i++) {
			NPC newNpc = MemoryNPCManager.getNPCManager().createNPC(categoryId);
			if (newNpc == null) {
				throw new IllegalStateException("雕像配置错误,NPC不存在:" + categoryId);
			}
			Game game = GameManager.getInstance().getGameByName(mapName, i);
			if (game != null) {
				newNpc.setAvata(player.getAvata());
				newNpc.setAvataRace(player.getAvataRace());
				newNpc.setAvataSex(player.getAvataSex());
				newNpc.setAvataType(player.getAvataType());

				newNpc.setLevel(player.getLevel());

				newNpc.setX(point.getX());
				newNpc.setY(point.getY());
				String[] avatas = player.getAvata();

				StringBuffer sbf = new StringBuffer();
				for (int ik = (avatas.length <= 2 ? 0 : 1); ik < avatas.length; ik++) {
					String avata = avatas[ik];
					String value = avata.split("\\/")[2].split("\\_")[0];
					if (!"gx".equals(value)) {
						sbf.append(avata.split("\\/")[2].split("\\_")[0]).append("；");
					}
				}
				newNpc.setPutOnEquipmentAvata(sbf.toString());
				ResourceManager.getInstance().getAvata(newNpc);
				newNpc.setObjectScale((short) 1500);
				if (particleName != null && !"".equals(particleName)) {
					newNpc.setParticleName(particleName);
					newNpc.setParticleX((short)0);
					newNpc.setParticleY((short)0);
				}
				ResourceManager rm = ResourceManager.getInstance();
				rm.getAvata(newNpc);
				newNpc.setTitle(CountryManager.得到国家名(player.getCountry()) + "●" + player.getName());
				newNpc.setNameColor(nameColor);
				newNpc.setName(title);
				newNpc.setWindowId(windowId);

				game.addSprite(newNpc);
				
			}
		}
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNameColor() {
		return nameColor;
	}

	public void setNameColor(int nameColor) {
		this.nameColor = nameColor;
	}

	public String getParticleName() {
		return particleName;
	}

	public void setParticleName(String particleName) {
		this.particleName = particleName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getWindowId() {
		return windowId;
	}

	public void setWindowId(int windowId) {
		this.windowId = windowId;
	}

	@Override
	public String toString() {
		return "StatueForTournament [point=" + point + ", mapName=" + mapName + ", categoryId=" + categoryId + ", title=" + title + ", nameColor=" + nameColor + ", particleName=" + particleName + ", index=" + index + ", windowId=" + windowId + "]";
	}
}
