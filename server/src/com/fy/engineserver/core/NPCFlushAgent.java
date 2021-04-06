package com.fy.engineserver.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 刷怪代理器
 * 
 * 此代理器管理者某个游戏中的怪物，
 * 每个怪物有一个出身地点，
 * 当这个怪物消失后，一段时间，系统自动刷出这个怪。
 * 
 * 
 * 
 */
public class NPCFlushAgent {

	public static class BornPoint {
		int x, y;

		boolean isAlive = false;
		long lastDisappearTime = 0;
		long flushFrequency = 0;
		NPC npc;

		public BornPoint(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

	}

	// 所有出生点和怪的对应表
	protected HashMap<BornPoint, Integer> templateMap = new HashMap<BornPoint, Integer>();

	Game game;
	Element ele;

	protected NPCFlushAgent(Game game, Element ele) {
		this.game = game;
		this.ele = ele;
	}

	public int[] getNpcCategoryIds() {
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(templateMap.values());
		Integer[] iids = set.toArray(new Integer[0]);

		int[] ids = new int[iids.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = iids[i].intValue();
		}

		return ids;
	}

	public BornPoint[] getBornPoints4NpcCategoryId(int npcCategoryId) {
		List<BornPoint> l = new ArrayList<BornPoint>();

		for (BornPoint bp : templateMap.keySet()) {
			int categoryId = templateMap.get(bp);
//			System.out.println("categoryId:"+categoryId+"--"+npcCategoryId);
			if (categoryId == npcCategoryId) {
				l.add(bp);
			}
		}

		return l.toArray(new BornPoint[0]);
	}

	/**
	 * 文件格式：
	 * <game game=''>
	 * <npc categoryId=''>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * </npc>
	 * <npc categoryId=''>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * </npc>
	 * 
	 * </game>
	 * @param file
	 * @throws Exception
	 */
	protected void load() throws Exception {
		try {
			if (ele == null) return;

			HashMap<BornPoint, Integer> map = new HashMap<BornPoint, Integer>();

			Element root = ele;
			Element spriteEles[] = XmlUtil.getChildrenByName(root, "npc");
			for (int i = 0; i < spriteEles.length; i++) {
				Element e = spriteEles[i];
				int categoryId = XmlUtil.getAttributeAsInteger(e, "categoryId");
				Element eles[] = XmlUtil.getChildrenByName(e, "bornpoint");
				for (int j = 0; j < eles.length; j++) {
					int x = XmlUtil.getAttributeAsInteger(eles[j], "x");
					int y = XmlUtil.getAttributeAsInteger(eles[j], "y");
					BornPoint bp = new BornPoint(x, y);
					map.put(bp, categoryId);
					Game.logger.warn("[加载NPC] [NPC:" + categoryId + "] [(" + x + "," + y + ")] [地图  " + game.gi.displayName + " 上NPC个数:" + spriteEles.length + "]");
				}
			}

			templateMap = map;
		} catch (Exception e) {
			Game.logger.error("[加载NPC出错]", e);
		}
	}

	/**
	 * 检查所有刷出来的怪，是否消失
	 */
	protected void heartbeat() {

		long currentTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		NPCManager sm = game.gm.getNpcManager();
		Iterator<BornPoint> it = templateMap.keySet().iterator();
		while (it.hasNext()) {

			BornPoint b = it.next();
			if (b.flushFrequency < 10000) b.flushFrequency = 10000;
			Integer categoryId = templateMap.get(b);
			if (categoryId != null && b.isAlive == false && (currentTime - b.lastDisappearTime) > b.flushFrequency) {
				if (sm != null) {
					b.npc = sm.createNPC(categoryId.intValue());
					if (b.npc != null) {
						b.npc.setGameNames(game.gi);
					} else {
						b.lastDisappearTime = currentTime;
						// Game.logger.warn("["+game.gi.name+"] [刷新NPC] [失败] [模板编号为"+categoryId+"的NPC不存在]");
						if (Game.logger.isWarnEnabled()) Game.logger.warn("[{}] [刷新NPC] [失败] [模板编号为{}的NPC不存在]", new Object[] { game.gi.name, categoryId });
					}
				} else {
					b.npc = null;
				}
				if (b.npc != null) {

					b.npc.setX(b.x);
					b.npc.setY(b.y);
					b.npc.setBornPoint(new Point(b.x, b.y));
					b.isAlive = true;
					b.flushFrequency = b.npc.getFlushFrequency();
					if (b.npc.getCountry() != 0) {
						b.npc.setCountry((byte) game.country);
					}
					game.addSprite(b.npc);
				}
			} else if (categoryId != null && b.isAlive == true) {
				if (b.npc == null) {
					b.isAlive = false;
					b.lastDisappearTime = currentTime;
				} else {
					if (!game.contains(b.npc)) {
						b.isAlive = false;
						b.lastDisappearTime = currentTime;
						if (sm != null) {
							sm.removeNPC(b.npc);
							b.npc = null;
						}
					}
				}
			}
		}
	}

	public void clear() {
		NPCManager sm = game.gm.getNpcManager();
		Iterator<BornPoint> it = templateMap.keySet().iterator();
		while (it.hasNext()) {
			BornPoint b = it.next();
			if (b.npc != null) {
				b.npc.setAlive(false);
				if (sm != null) {
					sm.removeNPC(b.npc);
					b.npc = null;
				}
			}
		}
	}

}
