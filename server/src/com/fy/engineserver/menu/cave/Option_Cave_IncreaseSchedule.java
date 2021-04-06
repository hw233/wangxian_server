package com.fy.engineserver.menu.cave;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.resource.IncreaseScheduleCfg;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;

/**
 * 增加队列
 * 
 * 
 */
public class Option_Cave_IncreaseSchedule extends CaveOption implements NeedRecordNPC,
		NeedCheckPurview {

	private IncreaseScheduleCfg scheduleCfg;
	private NPC npc;

	public Option_Cave_IncreaseSchedule(IncreaseScheduleCfg scheduleCfg) {
		this.scheduleCfg = scheduleCfg;
	}

	@Override
	public void doSelect(Game game, Player player) {
		boolean isSelf = FaeryManager.isSelfCave(player, getNpc().getId());
		if (isSelf) {
			synchronized (player.tradeKey) {
				Cave cave = FaeryManager.getInstance().getCave(player);
				long leftIncreaseTime = cave.getIncreaseScheduleLast() + cave.getIncreaseScheduleStart() - SystemTime.currentTimeMillis();// 仙府加队列剩余时间如果<=0表示加速已经结束
				if (leftIncreaseTime <= 0) {
					leftIncreaseTime = 0;
				}

				String articleName = scheduleCfg.getArticleName();
				ArticleEntity ae = player.getArticleEntity(articleName);
				if (ae == null) {
					player.sendError(Translate.translateString(Translate.text_cave_080, new String[][] { { Translate.STRING_1, articleName } }));
					return;
				}
				if (leftIncreaseTime + scheduleCfg.getIncreaseTime() > FaeryConfig.INCREASESCHEDULE_TIME_LIMIT) {
					player.sendError(Translate.增加队列时间超出最大限制);
					return;
				}
				ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "洞府", true);
				if(aee==null){
					String description = Translate.删除物品不成功;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (FaeryManager.logger.isWarnEnabled()) FaeryManager.logger.warn("[增加队列] ["+description+"] [id:"+ae.getId()+"]");
					return;
				}
				ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "庄园增加执行队列", "");
				cave.setIncreaseScheduleNum(scheduleCfg.getIncreaseNum());
				cave.setIncreaseScheduleStart(SystemTime.currentTimeMillis());
				cave.setIncreaseScheduleLast(leftIncreaseTime + scheduleCfg.getIncreaseTime());//更新时间.用剩余时间+本次道具增加的时间 2014-5-14 15:14:23
				player.addMessageToRightBag(CaveSubSystem.getInstance().getCave_DETAILED_INFO_RES(player, null));
				{
					// 统计
					if (Translate.高级鲁班令.equals(articleName)) {
						AchievementManager.getInstance().record(player, RecordAction.使用高级鲁班令次数);
						if (FaeryManager.logger.isInfoEnabled()) {
							FaeryManager.logger.info(player.getLogString() + "[使用高级鲁班令]");
						}
					}
				}

				player.sendError(Translate.translateString(Translate.text_cave_1081, new String[][] { { Translate.STRING_1, TimeTool.instance.getShowTime(cave.getIncreaseScheduleLast(),TimeDistance.DAY) } }));
				if (FaeryManager.logger.isInfoEnabled()) {
					FaeryManager.logger.info(player.getLogString() + " [增加洞府建筑队列] " + scheduleCfg.toString());
				}
			}
		} else {
			player.sendError(Translate.text_cave_016);
			return;
		}
	}

	public IncreaseScheduleCfg getScheduleCfg() {
		return scheduleCfg;
	}

	public void setScheduleCfg(IncreaseScheduleCfg scheduleCfg) {
		this.scheduleCfg = scheduleCfg;
	}

	@Override
	public boolean canSee(Player player) {
		boolean isSelf = FaeryManager.isSelfCave(player, getNpc().getId());
		return isSelf;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;

	}
}
