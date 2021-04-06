package com.fy.engineserver.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager.MonsterTempalte;
import com.fy.engineserver.worldmap.MapSingleMonsterInfo;
import com.fy.engineserver.worldmap.WorldMapManager;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 刷怪代理器
 * 
 * 出生点 对应多个怪，但是之多只有一个怪为活着的怪，可以有多个死怪
 * 
 * 
 */
public class MonsterFlushAgent {

	public static class BornPoint {
		int x, y;

		boolean isAlive = false;
		long lastDisappearTime = 0;
		long flushFrequency = 30000;
		Monster sprite;

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

	private LinkedList<Monster> deadList = new LinkedList<Monster>();

	private boolean dirty = false;

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean b) {
		dirty = b;
	}

	Game game;
	Element ele;

	protected MonsterFlushAgent(Game game, Element ele) {
		this.game = game;
		this.ele = ele;
	}

	/**
	 * 刷新怪物的最后死亡时间
	 * 
	 * 此方法是用于副本进度，但系统重启后，需要副本调用此方法
	 * 已保证已经死亡的怪，不立即刷新出来
	 */
	public void updateLastDisappearTime(HashMap<String, Long> map) {
		BornPoint bps[] = templateMap.keySet().toArray(new BornPoint[0]);
		for (int i = 0; i < bps.length; i++) {
			BornPoint p = bps[i];
			int categoryIds = templateMap.get(p);
			String key = p.x + "," + p.y + "," + categoryIds;
			Long l = map.get(key);
			if (l != null) {
				p.lastDisappearTime = l.longValue();
				// System.out.println("[设置副本进度，怪刷新时间] [成功] ["+key+"] ["+DateUtil.formatDate(new Date(l.longValue()), "yyyy-MM-dd HH:mm:ss")+"] ["+(p.lastDisappearTime)+"]");
			} else {
				// System.out.println("[设置副本进度，怪刷新时间] [未设置] ["+key+"] [-]");
			}
		}
	}

	/**
	 * 保存怪物死亡事件
	 * 
	 * 此方法是用于副本进度，但系统重启后，需要副本调用此方法
	 * 已保证已经死亡的怪，不立即刷新出来
	 * @return
	 */
	public HashMap<String, Long> saveLastDisappearTime() {
		HashMap<String, Long> map = new HashMap<String, Long>();
		BornPoint bps[] = templateMap.keySet().toArray(new BornPoint[0]);
		for (int i = 0; i < bps.length; i++) {
			BornPoint p = bps[i];
			int categoryIds = templateMap.get(p);
			String key = p.x + "," + p.y + "," + categoryIds;
			if (p.isAlive == false) {
				map.put(key, p.lastDisappearTime);
			}
		}
		return map;
	}

	public int[] getSpriteCategoryIds() {
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(templateMap.values());
		Integer[] iids = set.toArray(new Integer[0]);

		int[] ids = new int[iids.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = iids[i].intValue();
		}

		return ids;
	}

	public BornPoint[] getBornPoints4SpriteCategoryId(int spriteCategoryId) {
		List<BornPoint> l = new ArrayList<BornPoint>();

		for (BornPoint bp : templateMap.keySet()) {
			int categoryId = templateMap.get(bp);
			if (categoryId == spriteCategoryId) {
				l.add(bp);
			}
		}

		return l.toArray(new BornPoint[0]);
	}

	/**
	 * 文件格式：
	 * <sprite-flush-agent>
	 * <game game=''>
	 * <sprite categoryId=''>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * </sprite>
	 * <sprite categoryId=''>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * </sprite>
	 * </game>
	 * </sprite-flush-agent>
	 * @param file
	 * @throws Exception
	 */
	protected void load() throws Exception {
		if (ele == null) return;

		HashMap<BornPoint, Integer> map = new HashMap<BornPoint, Integer>();

		Element root = ele;
		Element spriteEles[] = XmlUtil.getChildrenByName(root, "sprite");
		MemoryMonsterManager sm = (MemoryMonsterManager) game.gm.getMonsterManager();
		MapSingleMonsterInfo msmi = new MapSingleMonsterInfo();
		for (int i = 0; i < spriteEles.length; i++) {
			Element e = spriteEles[i];
			int categoryId = XmlUtil.getAttributeAsInteger(e, "categoryId");
			int country = XmlUtil.getAttributeAsInteger(e, "country");
			Element eles[] = XmlUtil.getChildrenByName(e, "bornpoint");
			for (int j = 0; j < eles.length; j++) {
				int x = XmlUtil.getAttributeAsInteger(eles[j], "x");
				int y = XmlUtil.getAttributeAsInteger(eles[j], "y");

				BornPoint bp = new BornPoint(x, y);
				map.put(bp, categoryId);
				try {
					MonsterTempalte mt = sm.getMonsterTempalteById(categoryId);
					if (mt != null) {
						Position position = new Position((short) x, (short) y, game.gi.name, game.gi.displayName);
						HashMap<Integer, Position> ps = WorldMapManager.monsterPositions2.get(mt.monster.getName());
						if(ps == null){
							ps = new HashMap<Integer, Position>();
						}
						ps.put(country, position);
						WorldMapManager.monsterPositions2.put(mt.monster.getName(), ps);
						
						if (!msmi.getMonsterNamesList().contains(mt.monster.getName())) {
							msmi.getMonsterNamesList().add(mt.monster.getName());
							msmi.getMonsterLvList().add(mt.monster.getLevel());
							msmi.getMonsterxList().add((short) x);
							msmi.getMonsteryList().add((short) y);
							msmi.setMonsterNames();
							msmi.setMonsterLv();
							msmi.setMonsterx();
							msmi.setMonstery();
						}
					}

				} catch (Exception ex) {

				}
			}
		}
		WorldMapManager.mapEnglishSingleMonsterInfos.put(game.gi.name, msmi);
		WorldMapManager.mapChinaSingleMonsterInfos.put(game.gi.displayName, msmi);
		templateMap = map;

		// 加载地图中的怪物坐标
		for (int i = 0; i < msmi.getMonsterNames().length; i++) {
			String monsterName = msmi.getMonsterNames()[i];
			Position position = new Position(msmi.getMonsterx()[i], msmi.getMonstery()[i], game.gi.name, game.gi.displayName);
			WorldMapManager.monsterPositions.put(monsterName, position);
		}
	}

	public static boolean checkError = true;

	/**
	 * 检查所有刷出来的怪，是否消失
	 */
	protected void heartbeat() {

		long currentTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		MemoryMonsterManager sm = (MemoryMonsterManager) game.gm.getMonsterManager();
		Iterator<BornPoint> it = templateMap.keySet().iterator();
		while (it.hasNext()) {

			BornPoint b = it.next();
			Integer categoryId = templateMap.get(b);
			if (categoryId != null) {
				MonsterTempalte mt = sm.getMonsterTempalteById(categoryId.intValue());
				if (mt != null) {
					b.flushFrequency = mt.monster.getFlushFrequency();
				}
			}
			if (b.flushFrequency < 1000) b.flushFrequency = 1000;
			if (checkError) {
				if (b.sprite != null) {
					if (b.sprite.getX() < 0 || b.sprite.getY() < 0) {
						b.isAlive = false;
						b.lastDisappearTime = 0;
					}
				}
			}
			if (categoryId != null && b.isAlive == false && (currentTime - b.lastDisappearTime) > b.flushFrequency) {
				if (sm != null) {
					b.sprite = sm.createMonster(categoryId.intValue());
					if (b.sprite != null) {
						b.sprite.setGameNames(game.gi);
					} else {
						b.lastDisappearTime = currentTime;
						// Game.logger.warn("[" + game.gi.name + "] [刷新怪物] [失败] [模板编号为" + categoryId + "的怪物不存在]");
						if (Game.logger.isWarnEnabled()) Game.logger.warn("[{}] [刷新怪物] [失败] [模板编号为{}的怪物不存在]", new Object[] { game.gi.name, categoryId });
					}
				} else {
					b.sprite = null;
				}
				if (b.sprite != null) {

					// /////////////临时添加/////////////////
					GameInfo gi = game.gi;
					// MapArea ma = gi.getMapAreaByName("出生点");
					// if(ma != null) {
					// int x = ma.x + (int)(Math.random() * ma.width);
					// int y = ma.y + (int)(Math.random() * ma.height);
					// b.sprite.setX(x);
					// b.sprite.setY(y);
					// b.sprite.setBornPoint(new Point(x,y));
					// System.out.println("===================临时刷新怪===========[" + gi.getName() + "] ["+ma.name+"] {"+x+"}{"+y+"}");
					// }

					b.sprite.setX(b.x);
					b.sprite.setY(b.y);
					b.sprite.setBornPoint(new Point(b.x, b.y));
					b.sprite.setHp(b.sprite.getMaxHP());
					b.isAlive = true;
					b.flushFrequency = b.sprite.getFlushFrequency();

					game.addSprite(b.sprite);

					// System.out.println("===================刷新怪===========[" + gi.getName() + "] {" + b.x + "}{" + b.y + "}");

				} else {

				}
			} else if (categoryId != null && b.isAlive == true) {

				if (b.sprite == null) {
					b.isAlive = false;
					b.lastDisappearTime = currentTime;

					dirty = true;
				} else {

					// if(!game.contains(b.sprite)){
					// b.isAlive = false;
					// b.lastDisappearTime = currentTime;
					// if(sm != null){
					// sm.removeMonster(b.sprite);
					// b.sprite = null;
					// }
					// }

					if (b.sprite.getState() == Sprite.STATE_DEAD || b.sprite.getHp() <= 0) {
						b.isAlive = false;
						b.lastDisappearTime = currentTime;

						dirty = true;

						deadList.add(b.sprite);

						b.sprite = null;
					}
				}
			}
		}
		Iterator<Monster> it2 = deadList.iterator();
		while (it2.hasNext()) {
			Monster m = it2.next();
			if (game.contains(m) == false) {
				it2.remove();
				if (sm != null) {
					sm.removeMonster(m);
				}

			}
		}
	}

	public void clear() {
		MemoryMonsterManager sm = (MemoryMonsterManager) game.gm.getMonsterManager();
		Iterator<BornPoint> it = templateMap.keySet().iterator();
		while (it.hasNext()) {
			BornPoint b = it.next();
			if (b.sprite != null) {
				b.sprite.setAlive(false);
				if (sm != null) {
					sm.removeMonster(b.sprite);
				}
			}
		}
	}

}
