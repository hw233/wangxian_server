package com.fy.engineserver.country.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.activity.peekandbrick.PeekAndBrickManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class CountryManagerData {

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@SimpleId
	private long id = 1;

	@SimpleVersion
	protected int version2;

	/**
	 * 此表每天凌晨清空
	 */
	@SimpleColumn(name = "tuoyun")
	public Hashtable<Long, Integer> 已托运次数Map = new Hashtable<Long, Integer>();

	/**
	 * 此表每天凌晨清空，key为国家官职编号，此值只管职位，不管是否换人
	 */
	@SimpleColumn(name = "qiujin")
	public Hashtable<String, Integer> 已囚禁次数Map = new Hashtable<String, Integer>();

	/**
	 * 此表每天凌晨清空，key为国家编号，此值只管职位，不管是否换人
	 */
	@SimpleColumn(name = "jinyan")
	public Hashtable<String, Integer> 已禁言次数Map = new Hashtable<String, Integer>();

	/**
	 * 此表每天凌晨清空
	 */
	@SimpleColumn(name = "npcTouPiao",
		length = 200000)
	public Hashtable<Long, Integer> 已在NPC处投票次数Map = new Hashtable<Long, Integer>();

	/**
	 * 此表每天凌晨清空
	 */
	@SimpleColumn(name = "playerTouPiao",
		length = 200000)
	public Hashtable<Long, Integer> 已在玩家发起中投票次数Map = new Hashtable<Long, Integer>();

	/**
	 * 此表每天凌晨清空，key为国家编号，此值只管职位，不管是否换人
	 */
	@SimpleColumn(name = "fenglu")
	public Hashtable<Byte, Integer> 已发放俸禄的国王Map = new Hashtable<Byte, Integer>();

	/**
	 * 此表每天凌晨清空，key为国家编号，value为使用次数，此值只管职位，不管是否换人
	 */
	@SimpleColumn(name = "guowangzhaojiling")
	public Hashtable<Byte, Integer> 国王使用召集令的次数Map = new Hashtable<Byte, Integer>();

	/**
	 * 此表每天凌晨清空，key为国家编号，value为使用次数，此值只管职位，不管是否换人
	 */
	@SimpleColumn(name = "dasimazhaojiling")
	public Hashtable<Byte, Integer> 大司马使用召集令的次数Map = new Hashtable<Byte, Integer>();

	/**
	 * 此表每天凌晨清空，key为国家编号，value为使用次数，此值只管职位，不管是否换人
	 */
	@SimpleColumn(name = "dajiangjunzhaojiling")
	public Hashtable<Byte, Integer> 大将军使用召集令的次数Map = new Hashtable<Byte, Integer>();

	/**
	 * 此表每天凌晨清空，key为国家编号，value为使用次数，此值只管职位，不管是否换人
	 */
	@SimpleColumn(name = "dajiangjunNpc")
	public Hashtable<Byte, Integer> 大将军使用召集NPC的次数Map = new Hashtable<Byte, Integer>();

	/**
	 * 此表每天凌晨清空，key为国家编号，value为使用次数，此值只管职位，不管是否换人
	 */
	@SimpleColumn(name = "yuanshuaiNpc")
	public Hashtable<Byte, Integer> 元帅使用召集NPC的次数Map = new Hashtable<Byte, Integer>();

	/**
	 * 此表每天凌晨清空，key为时间，每次召集时间不一样，这样可以根据时间记录次数，value为次数，此值为了防止传送太多人导致机器卡死
	 */
	@SimpleColumn(name = "zhaojilingrenshu")
	public Hashtable<Long, Integer> 使用一次召集令召集来的人的数量Map = new Hashtable<Long, Integer>();

	/**
	 * 此表每天凌晨清空，key为playerId，value为次数，根据次数计算money
	 */
	@SimpleColumn(name = "wangzhezhiyin")
	public Hashtable<Long, Integer> 使用王者之印的次数Map = new Hashtable<Long, Integer>();

	/**
	 * 这个值不清
	 */
	@SimpleColumn(name = "wangzhezhiyinzong",
		length = 100000)
	public Hashtable<Long, Long> 使用王者之印的总次数Map = new Hashtable<Long, Long>();

	/**
	 * 此表每天凌晨清空，key为playerId，value为次数
	 */
	@SimpleColumn(name = "yuweishu")
	public Hashtable<Long, Integer> 国王使用御卫术拉御林军的次数Map = new Hashtable<Long, Integer>();

	/** 每个国家发布的刺探任务 */
	public List<String> peekTaskNames = new ArrayList<String>();
	/** 最后一次发布刺探任务的时间 */
	public long lastReleasePeekTask;
	/** 每个国家发布的偷砖任务 */
	public List<String> brickTaskNames = new ArrayList<String>();
	/** 最后一次发布偷砖任务的时间 */
	@SimpleColumn(name = "lrbt")
	public long lastReleaseBrickTask;

	transient boolean dirty;

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public String info() {
		String description = "";
		return description;
	}

	/**
	 * 重新发布刺探任务
	 */
	public void reReleasePeektask() {
		try {
			if (PeekAndBrickManager.logger.isInfoEnabled()) PeekAndBrickManager.logger.info("[开始重新发布刺探任务]");
			Random random = new Random();
			PeekAndBrickManager peekAndBrickManager = PeekAndBrickManager.getInstance();
			String gameName = peekAndBrickManager.getPeekTaskGameName();
			String npcName = peekAndBrickManager.getPeekTaskNpcName();
			GameManager gameManager = GameManager.getInstance();
			boolean isSameDay = TimeTool.instance.isSame(lastReleasePeekTask, SystemTime.currentTimeMillis(), TimeTool.TimeDistance.DAY, 1);
			if (!isSameDay || peekTaskNames.size() == 0) {
				if (peekTaskNames.size() > 0) { // 原来有，则先取消原来的任务
					if (PeekAndBrickManager.logger.isInfoEnabled()) PeekAndBrickManager.logger.info("[开始重新发布刺探任务] [原来有发布了的任务]");
					for (int i = 0; i < peekTaskNames.size(); i++) {
						byte country = (byte) (i + 1);// 取得国家编号
						String currTaskName = peekTaskNames.get(country - 1);
						Game game = gameManager.getGameByDisplayName(gameName, country);
						if (game != null) {
							TaskManager.getInstance().cancelReleaseTask(game, npcName, currTaskName);
						}
					}
				}
				peekTaskNames.clear();
				// 严格按照顺序加入任务 下标为(国家索引-1)
				for (int i = 1; i <= 3; i++) {
					String[] tasks = peekAndBrickManager.getCountryPeekTasks().get((byte) i);
					String taskName = tasks[random.nextInt(tasks.length)];
					peekTaskNames.add(taskName);
				}
				lastReleasePeekTask = SystemTime.currentTimeMillis();
			}
			for (int i = 0; i < 3; i++) {
				String taskName = peekTaskNames.get(i);
				Game game = gameManager.getGameByDisplayName(gameName, i + 1);
				if (game != null) {
					TaskManager.getInstance().releaseTask(game, npcName, taskName);
				} else {
					if (PeekAndBrickManager.logger.isInfoEnabled()) PeekAndBrickManager.logger.info("[开始重新发布刺探任务] [给国家:{}新增刺探任务:{}] [失败,地图不存在]", new Object[] { i, taskName });
				}
				if (PeekAndBrickManager.logger.isInfoEnabled()) PeekAndBrickManager.logger.info("[开始重新发布刺探任务] [给国家:{}新增刺探任务:{}] [地图:{}] [NPC:{}]", new Object[] { i, taskName, gameName, npcName });
			}
		} catch (Exception e) {
			PeekAndBrickManager.logger.error("[刺探任务失败]", e);
		}
	}

	/**
	 * 重新发偷砖探任务
	 */
	public void reReleaseBricktask() {
		try {
			if (PeekAndBrickManager.logger.isWarnEnabled()) PeekAndBrickManager.logger.warn("[开始重新发布偷砖任务]");
			Random random = new Random();
			PeekAndBrickManager peekAndBrickManager = PeekAndBrickManager.getInstance();
			String gameName = peekAndBrickManager.getBrickTaskGameName();
			String npcName = peekAndBrickManager.getBrickTaskNpcName();

			GameManager gameManager = GameManager.getInstance();

			boolean isSameDay = TimeTool.instance.isSame(lastReleaseBrickTask, SystemTime.currentTimeMillis(), TimeTool.TimeDistance.DAY, 1);
			if (!isSameDay || brickTaskNames.size() == 0) {
				if (brickTaskNames.size() > 0) { // 原来有，则先取消原来的任务
					if (PeekAndBrickManager.logger.isWarnEnabled()) PeekAndBrickManager.logger.warn("[开始重新发布偷砖任务] [原来有发布了的任务:{}]", new Object[] { Arrays.toString(brickTaskNames.toArray(new String[0])) });
					for (int i = 0; i < brickTaskNames.size(); i++) {
						String currTaskName = brickTaskNames.get(i);
						byte country = (byte) (i + 1);// 取得国家编号
						Game game = gameManager.getGameByDisplayName(gameName, country);
						if (game != null) {
							TaskManager.getInstance().cancelReleaseTask(game, npcName, currTaskName);
							if (PeekAndBrickManager.logger.isWarnEnabled()) PeekAndBrickManager.logger.warn("[开始重新发布偷砖任务] [原来有发布了的任务] [地图名字:{}] [npc名字:{}] [任务名字:{}]", new Object[] { game.gi.displayName, npcName, currTaskName });
						} else {
							if (PeekAndBrickManager.logger.isWarnEnabled()) PeekAndBrickManager.logger.info("[开始重新发布偷砖任务] [原来有发布了的任务] [------地图不存在------] [地图名字:{}] [npc名字:{}] [任务名字:{}]", new Object[] { "", npcName, currTaskName });
						}
					}
				}
				brickTaskNames.clear();
				// 严格按照顺序加入任务 下标为(国家索引-1)
				for (int i = 1; i <= 3; i++) {
					String[] tasks = peekAndBrickManager.getCountryBrickTasks().get((byte) i);
					if (PeekAndBrickManager.logger.isWarnEnabled()) PeekAndBrickManager.logger.warn("[国家:{}] [可发布的任务:{}]", new Object[] { i, Arrays.toString(tasks) });
					String taskName = tasks[random.nextInt(tasks.length)];
					if (PeekAndBrickManager.logger.isWarnEnabled()) PeekAndBrickManager.logger.warn("[国家:{}] [地图:{}] [可发布的任务:{}] [结果:{}]", new Object[] { i, gameName, Arrays.toString(tasks), taskName });
					brickTaskNames.add(taskName);
				}
				lastReleaseBrickTask = SystemTime.currentTimeMillis();
			}

			for (int i = 0; i < 3; i++) {
				String taskName = brickTaskNames.get(i);
				Game game = gameManager.getGameByDisplayName(gameName, i + 1);
				if (game != null) {
					TaskManager.getInstance().releaseTask(game, npcName, taskName);
				} else {
					if (PeekAndBrickManager.logger.isWarnEnabled()) PeekAndBrickManager.logger.warn("[开始重新发布刺探任务] [给国家:{}新增偷砖任务:{}] [失败,地图不存在]", new Object[] { i, taskName });
				}
				if (PeekAndBrickManager.logger.isWarnEnabled()) PeekAndBrickManager.logger.warn("[开始重新发布刺探任务] [给国家:{}新增偷砖任务:{}]", new Object[] { i, taskName });
			}

		} catch (Exception e) {
			PeekAndBrickManager.logger.error("发布任务异常", e);
		}
	}
}
