package com.fy.engineserver.platform;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.platform.PlatformManager.Platform;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 关闭功能管理
 * 
 * 
 */
public class CloseFunctionManager {

	public static Set<String> closeRegisterPlayerServers = new HashSet<String>();

	static {
//		closeRegisterPlayerServers.add("華山之巔");
//		closeRegisterPlayerServers.add("昆侖仙境");
//		closeRegisterPlayerServers.add("飄渺王城");
//		closeRegisterPlayerServers.add("昆华古城");
//		closeRegisterPlayerServers.add("仙侶奇緣");
//		closeRegisterPlayerServers.add("斬龍屠魔");
//		closeRegisterPlayerServers.add("天降神兵");
//		closeRegisterPlayerServers.add("雪域冰城");
//		closeRegisterPlayerServers.add("伏虎沖天");
//		closeRegisterPlayerServers.add("無相如來");
//		closeRegisterPlayerServers.add("人間仙境");
//		closeRegisterPlayerServers.add("皇圖霸業");
//		closeRegisterPlayerServers.add("步雲登仙");
//		closeRegisterPlayerServers.add("仙姿玉貌");
//		closeRegisterPlayerServers.add("瓊漿玉露");
//		closeRegisterPlayerServers.add("仙人指路");
	}

	/**
	 * 是否是关闭创建角色的服务器
	 * @return
	 */
	public static boolean isCloseRegisterPlayer() {
		if (!PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
			return false;
		}

		GameConstants constants = GameConstants.getInstance();
		String serverName = constants.getServerName();
		return closeRegisterPlayerServers.contains(serverName);
	}
}
