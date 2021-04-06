package com.fy.engineserver.activity.fairyBuddha;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;

/**
 * 仙尊雕像
 * 
 */
public class StatueForFairyBuddha implements Serializable {
	/** 雕像位置 */
	private Point point;
	/** 地图名字 */
	private String mapName;
	/** 雕像名字 */
	private String npcName;
	/** NPCID */
	private int categoryId;
	/** 称号 */
	private String title;
	/** 名字颜色 */
	private int nameColor;
	/** NPC粒子 */
	private String particleName;
	/** 职业 */
	private byte career;
	/** 血 */
	private int maxHP;
	/** 外功 */
	private int phyAttack;
	/** 法攻 */
	private int magicAttack;
	/** Avata */
	private String[] avatas;
	private String avataRace;
	private String avataSex;
	private byte[] avataType;

	/** 默认npc */
	private NPC defaultNpc;

	public StatueForFairyBuddha() {
	}

	public StatueForFairyBuddha(Point point, String mapName, String npcName, int categoryId, String title, int nameColor, String particleName, byte career, String[] avatas, String avataRace, String avataSex, byte[] avataType) {
		this.point = point;
		this.mapName = mapName;
		this.npcName = npcName;
		this.categoryId = categoryId;
		this.title = title;
		this.nameColor = nameColor;
		this.particleName = particleName;
		this.career = career;
		this.avatas = avatas;
		this.avataRace = avataRace;
		this.avataSex = avataSex;
		this.avataType = avataType;
	}

	public StatueForFairyBuddha(String npcName, byte career, int maxHP, int phyAttack, int magicAttack, String[] avatas, String avataRace, String avataSex, byte[] avataType) {
		this.npcName = npcName;
		this.career = career;
		this.maxHP = maxHP;
		this.phyAttack = phyAttack;
		this.magicAttack = magicAttack;
		this.avatas = avatas;
		this.avataRace = avataRace;
		this.avataSex = avataSex;
		this.avataType = avataType;
	}

	public void putNpcLike(int categoryId, FairyBuddhaInfo fbi) {
		for (int i = 0; i < 4; i++) {
			MemoryNPCManager ma = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			NPCTempalte nt = ma.getNPCTempalteByCategoryId(categoryId);
			if (nt == null) {
				FairyBuddhaManager.logger.error("[仙尊替换] [仙尊默认npc不存在] [categoryId:" + categoryId + "]");
			} else {
				NPC newNpc = null;
				for (StatueForFairyBuddha sb : FairyBuddhaManager.getInstance().statueList) {
					if (this.career == sb.getCareer()) {
						newNpc = sb.getDefaultNpc();
					}
				}

				// NPC newNpc = MemoryNPCManager.getNPCManager().createNPC(categoryId);
				if (newNpc == null) {
					throw new IllegalStateException("雕像配置错误,NPC不存在:" + categoryId);
				}
				FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
				List<StatueForFairyBuddha> statueList = fbm.getStatueList();
				for (StatueForFairyBuddha sffb : statueList) {
					if (sffb.getCategoryId() == categoryId) {
						FairyBuddhaManager.logger.error("[仙尊替换] [地图:" + sffb.getMapName() + "]");
						Game game = GameManager.getInstance().getGameByName(sffb.getMapName(), i);
						if (game != null) {
							byte[] tempAvataType = new byte[0];
							String[] tempAvatas = new String[0];
							for (int kk = 0; kk < avataType.length; kk++) {
								if (avataType[kk] != 13) { // 13为共享物品-加了会出问题
									tempAvataType = Arrays.copyOf(tempAvataType, tempAvataType.length + 1);
									tempAvataType[tempAvataType.length - 1] = avataType[kk];
									tempAvatas = Arrays.copyOf(tempAvatas, tempAvatas.length + 1);
									tempAvatas[tempAvatas.length - 1] = avatas[kk];
								}
							}
							// FairyBuddhaManager.logger.error("[仙尊替换测试log] ["+Arrays.toString(tempAvataType)+"=======" + Arrays.toString(tempAvatas) + "]");
							newNpc.setAvata(tempAvatas);
							newNpc.setAvataRace(avataRace);
							newNpc.setAvataSex(avataSex);
							newNpc.setAvataType(tempAvataType);

							// newNpc.setLevel(new.getLevel());

							// newNpc.setX(point.getX());
							// newNpc.setY(point.getY());

							StringBuffer sbf = new StringBuffer();
							for (int ik = (tempAvatas.length <= 2 ? 0 : 1); ik < tempAvatas.length; ik++) {
								String avata = tempAvatas[ik];
								String value = avata.split("\\/")[2].split("\\_")[0];
								if (!"gx".equals(value)) {
									sbf.append(avata.split("\\/")[2].split("\\_")[0]).append("；");
								}
								if (avata.matches(".*@")) {
									tempAvatas[ik] = avata.split("@")[0];
								}
							}
							newNpc.setPutOnEquipmentAvata(sbf.toString());
							ResourceManager.getInstance().getAvata(newNpc);
							// FairyBuddhaManager.logger.error("[仙尊替换测试log] ["+Arrays.toString(newNpc.getAvataType())+"=======" + Arrays.toString(newNpc.getAvata()) + "]");
							newNpc.setObjectScale((short) 1200);
							// if (npc.getParticleName() != null && !"".equals(npc.getParticleName())) {
							// newNpc.setParticleName(npc.getParticleName());
							// newNpc.setParticleX((short) 0);
							// newNpc.setParticleY((short) 0);
							// }
							// ResourceManager rm = ResourceManager.getInstance();
							// rm.getAvata(newNpc);
							// newNpc.setName(npcName);
							try {
								// Player player = PlayerManager.getInstance().getPlayer(npcName);
								// if (player != null) {
								// newNpc.setName(player.getName() + "●" + CountryManager.得到国家名(player.getCountry()));
								// }
								List<IBillboardPlayerInfo> list = TransitRobberyEntityManager.getBillboardPlayerInfo(fbi.getId());
								if (list != null && list.size() > 0) {
									IBillboardPlayerInfo playerInfo = list.get(0);
									newNpc.setName(playerInfo.getName() + "●" + CountryManager.得到国家名(playerInfo.getCountry()));
								} else {
									FairyBuddhaManager.logger.error("[仙尊] [替换npc名字异常] [未查询到玩家信息]");
								}
								String windowDes = "<f color='0xFFFF00' size='28'>" + Translate.当前仙尊宣言 + ":</f>\n<f size='28'>" + fbi.getDeclaration() + "</f>\n<f color='0xFFFF00' size='28'>" + Translate.被膜拜次数 + ":</f><f size='28'>" + fbi.getBeWorshipped() + "</f>";
								WindowManager.getInstance().getWindowMap().get(newNpc.getWindowId()).setDescriptionInUUB(windowDes);
							} catch (Exception e) {
								FairyBuddhaManager.logger.error("[仙尊] [替换npc名字异常]" + e);
								e.printStackTrace();
							}

							// game.addSprite(newNpc);
							FairyBuddhaManager.logger.warn("[仙尊摆放新npc] [npcName:" + newNpc.getName() + "]");
						}
					}
				}
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

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getPhyAttack() {
		return phyAttack;
	}

	public void setPhyAttack(int phyAttack) {
		this.phyAttack = phyAttack;
	}

	public int getMagicAttack() {
		return magicAttack;
	}

	public void setMagicAttack(int magicAttack) {
		this.magicAttack = magicAttack;
	}

	public String[] getAvatas() {
		return avatas;
	}

	public void setAvatas(String[] avatas) {
		this.avatas = avatas;
	}

	public String getAvataRace() {
		return avataRace;
	}

	public void setAvataRace(String avataRace) {
		this.avataRace = avataRace;
	}

	public String getAvataSex() {
		return avataSex;
	}

	public void setAvataSex(String avataSex) {
		this.avataSex = avataSex;
	}

	public byte[] getAvataType() {
		return avataType;
	}

	public void setAvataType(byte[] avataType) {
		this.avataType = avataType;
	}

	public String getNpcName() {
		return npcName;
	}

	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}

	public NPC getDefaultNpc() {
		return defaultNpc;
	}

	public void setDefaultNpc(NPC defaultNpc) {
		this.defaultNpc = defaultNpc;
	}

	@Override
	public String toString() {
		return "StatueForFairyBuddha [avataRace=" + avataRace + ", avataSex=" + avataSex + ", avataType=" + Arrays.toString(avataType) + ", avatas=" + Arrays.toString(avatas) + ", career=" + career + ", categoryId=" + categoryId + ", defaultNpc=" + defaultNpc + ", magicAttack=" + magicAttack + ", mapName=" + mapName + ", maxHP=" + maxHP + ", nameColor=" + nameColor + ", npcName=" + npcName + ", particleName=" + particleName + ", phyAttack=" + phyAttack + ", point=" + point + ", title=" + title + "]";
	}

}
