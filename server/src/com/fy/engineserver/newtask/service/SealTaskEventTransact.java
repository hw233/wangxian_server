package com.fy.engineserver.newtask.service;

import java.util.Random;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.sprite.npc.SealTaskFlushNPC;

/**
 * 封印任务
 * 
 *
 */
public class SealTaskEventTransact extends AbsTaskEventTransact {

	public static String[] maps = new String[]{"bianjing"};
	public static Random random = new Random();
	public static int 封印npcID = 30000000;
	public static int[][] 坐标 = new int[][]{{272,2729},{1532,3698},{2059,1420}};
	
	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case accept:
			return new String[] { Translate.封印任务};
		}
		return null;
	}

	public void init() {

	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
		String[] taskNames = getWannaDealWithTaskNames(Taskoperation.accept);
		if (taskNames != null) {
			for (String taskName : taskNames) {
				if (task.getName().equals(taskName)) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.translateString(Translate.您接受了任务,new String[][]{{Translate.STRING_1,taskName}}));
					player.addMessageToRightBag(hreq);
					//地图刷新封印任务奸细
					GameManager gm = GameManager.getInstance();
					if(gm != null){
						String mapName = maps[0];
						Game flushGame = gm.getGameByName(mapName,player.getCountry());
						if(flushGame != null){
							try{
								NPCManager npcm = MemoryNPCManager.getNPCManager();
								NPC newNpc = npcm.createNPC(封印npcID);
								if(newNpc instanceof SealTaskFlushNPC){
									SealTaskFlushNPC sealNPC = (SealTaskFlushNPC)newNpc;
									int[] xy = 坐标[random.nextInt(坐标.length)];
									sealNPC.setX(xy[0]);
									sealNPC.setY(xy[1]);
									sealNPC.flushPlayerId = player.getId();
									sealNPC.sealLevel = SealManager.getInstance().getSealLevel();
									sealNPC.setCountry(player.getCountry());
									sealNPC.setTitle("("+player.getName()+")");
									flushGame.addSprite(sealNPC);
									String description = Translate.translateString(Translate.在地图刷出奸细1, new String[][]{{Translate.PLAYER_NAME_1,player.getName()},{Translate.STRING_1,flushGame.gi.displayName}});
									ChatMessageService cms = ChatMessageService.getInstance();
									ChatMessage msg = new ChatMessage();
									msg.setMessageText(description);
									cms.sendMessageToCountry(player.getCountry(), msg);
									String description2 = Translate.translateString(Translate.在地图刷出奸细2, new String[][]{{Translate.PLAYER_NAME_1,player.getName()},{Translate.STRING_1,flushGame.gi.displayName}});
									ChatMessageService cms2 = ChatMessageService.getInstance();
									ChatMessage msg2 = new ChatMessage();
									msg2.setMessageText(description2);
									cms2.sendMessageToCountry2(player.getCountry(), msg2);
									if(Game.logger.isInfoEnabled()){
										Game.logger.info("[刷新奸细] [在{}的({},{})位置上产生一个id为{}的npc]",new Object[]{mapName,xy[0],xy[1],newNpc.getId()});
									}
								}else{
									if(Game.logger.isWarnEnabled())
										Game.logger.warn("[刷新奸细] [错误] [!newNpc instanceof SealTaskFlushNPC]");
								}

							}catch(Exception ex){
								if(Game.logger.isWarnEnabled())
									Game.logger.warn("[刷新奸细] [异常]",ex);
							}
						}else{
							if(Game.logger.isWarnEnabled())
								Game.logger.warn("[刷新奸细] [错误] [刷新地图不存在]");
						}
					}else{
						if(Game.logger.isWarnEnabled())
							Game.logger.warn("[刷新奸细] [错误] [gm == null]");
					}
					break;
				}
			}
		}
	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		String[] taskNames = getWannaDealWithTaskNames(Taskoperation.accept);
		if (taskNames != null) {
			for (String taskName : taskNames) {
				if (task.getName().equals(taskName)) {
					player.sendError(Translate.translateString(Translate.你完成了任务快去找交付任务, new String[][]{{Translate.STRING_1,taskName},{Translate.STRING_2,task.getEndNpc()}}));
				}
			}
		}

	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {

	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}
}
