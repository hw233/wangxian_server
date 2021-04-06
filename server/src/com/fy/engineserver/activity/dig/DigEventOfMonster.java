package com.fy.engineserver.activity.dig;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;

public class DigEventOfMonster extends DigEvent {

	/** 角色等级 */
	private int playerLevel;
	/** 怪物ID */
	private int monsterId;
	/** 刷新方式；0-脚下，1-录入地图 */
	private int refType;
	/** 地图 */
	private String mapName;
	/** 刷新位置 */
	private Point point;
	/** 是否广播：0-不广播，1-广播 */
	private int sendNotice;

	public DigEventOfMonster(String useArticleName, String useArticleNameStat, int eventType) {
		super(useArticleName, useArticleNameStat, eventType);
	}

	public DigEventOfMonster(String useArticleName, String useArticleNameStat, int playerLevel, int monsterId, int refType, int sendNotice) {
		this(useArticleName, useArticleNameStat, DigManager.EVENT_MONSTER);
		this.playerLevel = playerLevel;
		this.monsterId = monsterId;
		this.refType = refType;
		this.sendNotice = sendNotice;
	}

	public DigEventOfMonster(String useArticleName, String useArticleNameStat, int playerLevel, int monsterId, int refType, String mapName, Point point, int sendNotice) {
		this(useArticleName, useArticleNameStat, DigManager.EVENT_MONSTER);
		this.playerLevel = playerLevel;
		this.monsterId = monsterId;
		this.refType = refType;
		this.mapName = mapName;
		this.point = point;
		this.sendNotice = sendNotice;
	}

	@Override
	public void execute(Player player, Game game) {

		Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(monsterId);
		if (monster == null) {
			TaskSubSystem.logger.warn(player.getLogString() + "[挖宝刷怪获取怪物失败] [怪物id:" + monsterId + "]");
			// TODO LOG
			return;
		}
		Random random = new Random();
		List<DigRefMapInfo> digList = DigManager.getInstance().getDigRefInfoList();
		Game target = null;
		int x = 0;
		int y = 0;
		refType = random.nextInt(2);
		boolean needNotice = false;
		switch (refType) { 
		case 0:
			target = player.getCurrentGame();
			x = player.getX();
			y = player.getY();
			break;
		case 1:
			DigRefMapInfo digRefMapInfo = digList.get(random.nextInt(digList.size()));
			Point randomPoint = digRefMapInfo.getPoint();
			setMapName(digRefMapInfo.getMapName());
			setPoint(randomPoint);
			x = point.getX();
			y = point.getY();
			target = GameManager.getInstance().getGameByName(digRefMapInfo.getMapName(), 0);
			needNotice = true;
			break;

		default:
			break;
		}

		if (target == null) {
			TaskSubSystem.logger.warn(player.getLogString() + "[挖宝刷怪获取地图信息失败] [地图名称:" + mapName + "]");
			return;
		}
		monster.setBornPoint(new Point(x, y));
		monster.setX(x);
		monster.setY(y);
		target.addSprite(monster);

		try {
			if (needNotice) {
				ChatMessage msg=new ChatMessage();
				msg.setMessageText(Translate.translateString(Translate.中立刷怪,new String[][]{{Translate.STRING_1,player.getName()},{Translate.STRING_2,target.gi.displayName}}));
				ChatMessageService.getInstance().sendHintMessageToSystem(msg);
//				String noticeContent = Translate.translateString(Translate.中立刷怪,new String[][]{{Translate.STRING_1,player.getName()},{Translate.STRING_2,target.gi.displayName}}); 
//				player.sendError("[刷怪地图:"+mapName+"] [怪物名:"+monster.getName()+"] [坐标("+point.x+","+point.y+")]");
//				TaskSubSystem.logger.error(player.getLogString()+"[刷怪地图:"+mapName+"] [怪物名:"+monster.getName()+"] [坐标("+point.x+","+point.y+")]");
//				ChatMessageService.getInstance().sendMessageToSystem(noticeContent);
			}
		} catch (Exception e) {
			TaskSubSystem.logger.warn(player.getLogString() + "[挖宝刷怪发送广播异常]" + e);
			e.printStackTrace();
		}
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}

	public int getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	public int getRefType() {
		return refType;
	}

	public void setRefType(int refType) {
		this.refType = refType;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getSendNotice() {
		return sendNotice;
	}

	public void setSendNotice(int sendNotice) {
		this.sendNotice = sendNotice;
	}

	@Override
	public String toString() {
		return "DigEventOfMonster [mapName=" + mapName + ", monsterId=" + monsterId + ", playerLevel=" + playerLevel + ", point=" + point + ", refType=" + refType + ", sendNotice=" + sendNotice + "]";
	}

}
