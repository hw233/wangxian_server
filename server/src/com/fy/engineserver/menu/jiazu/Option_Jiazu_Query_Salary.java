package com.fy.engineserver.menu.jiazu;

import java.util.List;
import java.util.Set;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.JIAZU_LIST_SALARY_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * 查询家族成员工资
 * 
 * 
 */
public class Option_Jiazu_Query_Salary extends Option {

	public static interface PlayerName {
		public long getId();

		public String getName();
	}

	@Override
	public void doSelect(Game game, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return;
		}
		JiazuManager jiazuManager = JiazuManager.getInstance();
		if (player.getJiazuId() <= 0) {
			if (JiazuSubSystem.logger.isWarnEnabled()) {
				JiazuSubSystem.logger.warn(player.getLogString() + "[取出家族工资列表] [失败] [玩家尚未加入家族] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		if (jiazu == null) {
			if (JiazuSubSystem.logger.isWarnEnabled()) {
				JiazuSubSystem.logger.warn(player.getLogString() + "[取出家族工资列表] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return;
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (JiazuSubSystem.logger.isWarnEnabled()) {
				JiazuSubSystem.logger.warn(player.getLogString() + "[取出家族工资列表] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6129);
			player.addMessageToRightBag(nreq);
			return;
		} else {
			long salarySum = jiazu.getSalarySum();
			boolean hasPopedom = member.getTitle() == JiazuTitle.master;
			String des = Translate.text_jiazu_028;
			String batchDes = Translate.text_jiazu_029;
			long salaryLeft = jiazu.getSalaryLeft();
			Set<JiazuMember> memberList = jiazuManager.getJiazuMember(jiazu.getJiazuID());

			String[] playerName = new String[memberList.size()];
			for (int i = 0; i < playerName.length; i++) {
				playerName[i] = "";
			}
			long[] playerId = new long[memberList.size()];
			long[] salary = new long[memberList.size()];
			long[] lastWeekSalary = new long[memberList.size()];
			boolean[] hasPaid = new boolean[memberList.size()];
			long[] paidSalary = new long[memberList.size()];

			int i = 0;
			for (JiazuMember tempMem : memberList) {
				playerId[i] = tempMem.getPlayerID();
				salary[i] = jiazu.getSinglePlayerSalary(tempMem.getPlayerID());
				lastWeekSalary[i] = tempMem.getLastSalary();
				hasPaid[i] = (tempMem.getCurrentSalary() >= jiazu.getSinglePlayerSalary(tempMem.getPlayerID()));// tempMem.isPaidthisweek();
				paidSalary[i] = hasPaid[i] ? tempMem.getCurrentSalary() : 0;
				i++;
			}

			try {
				SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);

				List<PlayerName> list = em.queryFields(PlayerName.class, playerId);

				for (i = 0; i < playerId.length; i++) {
					for (PlayerName pn : list) {
						if (pn.getId() == playerId[i]) {
							playerName[i] = pn.getName();
						}
					}
				}

			} catch (Exception e) {
				JiazuSubSystem.logger.error(player.getLogString() + "[查询工资异常]", e);
			}

			if (JiazuSubSystem.logger.isInfoEnabled()) {
				JiazuSubSystem.logger.info(player.getLogString() + "[取出家族工资列表] [成功] [{}] [jiazuName:{}] [jiazuID:{}] [salarySum:{}] [salaryLeft:{}] [memberSize:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), salarySum, salaryLeft, memberList.size(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_LIST_SALARY_RES res = new JIAZU_LIST_SALARY_RES(GameMessageFactory.nextSequnceNum(), salarySum, salaryLeft, hasPopedom, des, batchDes, playerName, playerId, salary, paidSalary, lastWeekSalary, hasPaid);
			player.addMessageToRightBag(res);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
