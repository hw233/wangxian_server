package com.fy.engineserver.menu.jiazu;

import static com.fy.engineserver.datasource.language.Translate.COUNT_1;
import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.STRING_2;
import static com.fy.engineserver.datasource.language.Translate.STRING_3;
import static com.fy.engineserver.datasource.language.Translate.text_feed_silkworm_004;
import static com.fy.engineserver.datasource.language.Translate.text_jiazu_031;
import static com.fy.engineserver.datasource.language.Translate.text_jiazu_047;
import static com.fy.engineserver.datasource.language.Translate.text_jiazu_107;
import static com.fy.engineserver.datasource.language.Translate.text_jiazu_177;
import static com.fy.engineserver.datasource.language.Translate.text_jiazu_178;
import static com.fy.engineserver.datasource.language.Translate.translateString;

import java.util.ArrayList;

import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.septbuilding.entity.SeptBuildingEntity;
import com.fy.engineserver.septbuilding.templet.BiaoZhiXiangBH;
import com.fy.engineserver.septbuilding.templet.BiaoZhiXiangQL;
import com.fy.engineserver.septbuilding.templet.BiaoZhiXiangXW;
import com.fy.engineserver.septbuilding.templet.BiaoZhiXiangZQ;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.TimeTool;

/**
 * 发放家族BUFF
 * 
 * 
 */
public class Option_Jiazu_Buff extends Option implements NeedCheckPurview, NeedRecordNPC, Cloneable {

	private NPC npc;

	private boolean confirm = false;
	/** 标志像索引 青龙11  白虎12 朱雀13 玄武14*/
	private int buildIndex;
	
	
	@Override
	public void doSelect(Game game, Player player) {

		try {
			if (!player.inSelfSeptStation()) {
				player.sendError(text_feed_silkworm_004);
				return;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
			if (jiazu == null || jiazuMember == null) {
				player.sendError(text_jiazu_031);
				return;
			}
			SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(player.getJiazuId());
			if (septStation == null) {
				JiazuSubSystem.logger.error(player.getLogString() + "[发布家族BUFF] [驻地不存在]");
				return;
			}
			SeptBuildingEntity biaozhixiang = septStation.getCurrentBiaozhixiang();
			if (biaozhixiang == null) {
				JiazuSubSystem.logger.error(player.getLogString() + "[发布家族BUFF] [标志像不存在]");
				return;
			}
			// 有没有权限
			boolean hasPermission = JiazuTitle.hasPermission(jiazuMember.getTitle(), JiazuFunction.add_buff);
			if (!hasPermission) {
				player.sendError(text_jiazu_047);
				return;
			}
			if (jiazu.getJiazuStatus() == JiazuManager2.封印家族功能) {
				player.sendError(Translate.家族资金不足封印);
				return ;
			}

			int bzxLevel = biaozhixiang.getBuildingState().getLevel();
			BuildingType buildingType = biaozhixiang.getBuildingState().getTempletType().getBuildingType();
			long cost = 0;
			String buffName = null;
			long delay = 0L;

			switch (buildingType) {
			case 标志像朱雀:
				BiaoZhiXiangZQ zq = (BiaoZhiXiangZQ) biaozhixiang.getBuildingState().getTempletType();
				cost = zq.getCosts()[bzxLevel - 1];
				buffName = zq.getBufferName();
				delay = zq.getDurations()[bzxLevel - 1];
				break;
			case 标志像玄武:
				BiaoZhiXiangXW xw = (BiaoZhiXiangXW) biaozhixiang.getBuildingState().getTempletType();
				cost = xw.getCosts()[bzxLevel - 1];
				buffName = xw.getBufferName();
				delay = xw.getDurations()[bzxLevel - 1];
				break;
			case 标志像白虎:
				BiaoZhiXiangBH bh = (BiaoZhiXiangBH) biaozhixiang.getBuildingState().getTempletType();
				cost = bh.getCosts()[bzxLevel - 1];
				buffName = bh.getBufferName();
				delay = bh.getDurations()[bzxLevel - 1];
				break;
			case 标志像青龙:
				BiaoZhiXiangQL ql = (BiaoZhiXiangQL) biaozhixiang.getBuildingState().getTempletType();
				cost = ql.getCosts()[bzxLevel - 1];
				buffName = ql.getBufferName();
				delay = ql.getDurations()[bzxLevel - 1];
				break;
			default:
				break;
			}
			delay *= 1000;
			BuffTemplateManager bm = BuffTemplateManager.getInstance();
			BuffTemplate bt = bm.getBuffTemplateByName(buffName);
			if (bt == null || cost <= 0 || delay <= 0) {
				player.sendError(translateString(text_jiazu_177, new String[][] { { STRING_1, buffName } }));
				JiazuSubSystem.logger.error(player.getLogString() + "[发布BUFF] [BUFF不存在:{}] [BUFF家族资金cost:{}] [BUFF持续时间delay:{}ms]", new Object[] { buffName, cost, delay });
				return;
			}
			if (!confirm) {
				JiazuManager2.popConfirmTakeBuff(player, npc, cost);
				return ;
			}

			synchronized (jiazu) {
				if (jiazu.getJiazuMoney() < cost) {
					player.sendError(text_jiazu_107);
					return;
				}
				jiazu.setJiazuMoney(jiazu.getJiazuMoney() - cost);
				try {
					if (JiazuManager2.instance.noticeNum.get(jiazu.getLevel()) != null) {
						int num = JiazuManager2.instance.noticeNum.get(jiazu.getLevel());
						if (jiazu.getJiazuMoney() < num) {
							JiazuManager2.instance.noticeJizuMondeyNotEnough(jiazu);
						}
					}
				} catch (Exception e) {
					JiazuManager2.logger.error("[新家族] [发送家族资金不足提醒] [异常] [" + jiazu.getLogString() + "]", e);
				}
				// player.sendError("发布" + bzxLevel + "级BUFF:" + buffName + "成功,消耗银子:" + BillingCenter.得到带单位的银两(cost) + ",持续:" + TimeTool.instance.getShowTime(delay));
				player.sendError(translateString(text_jiazu_178, new String[][] { { COUNT_1, String.valueOf(bzxLevel) }, { STRING_1, buffName }, { STRING_2, BillingCenter.得到带单位的银两(cost) }, { STRING_3, TimeTool.instance.getShowTime(delay) } }));

				String s = Translate.translateString(Translate.发BUFF, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.COUNT_1, String.valueOf(bzxLevel) }, { Translate.STRING_2, buffName }, { Translate.STRING_3, TimeTool.instance.getShowTime(delay) } });
				ChatMessageService.getInstance().sendMessageToJiazu(jiazu, s, "");

				if (JiazuSubSystem.logger.isWarnEnabled()) {
					JiazuSubSystem.logger.warn(player.getLogString() + "[发布家族BUFF:{}] [消耗家族资金:{}] [家族资金剩余:{}]", new Object[] { buffName, cost, jiazu.getJiazuMoney() });
				}
			}

			ArrayList<Player> olPlayers = jiazu.getOnLineMembers();
			Player[] playerArr = olPlayers.toArray(new Player[0]);
			for (Player p : playerArr) {
				Buff buff = p.getBuffByName(buffName);
				if (buff != null) {
					buff.setInvalidTime(0);
				}
				buff = bt.createBuff(bzxLevel);
				buff.setStartTime(SystemTime.currentTimeMillis());
				buff.setInvalidTime(buff.getStartTime() + delay);
				buff.setCauser(p);
				p.placeBuff(buff);
				if (JiazuSubSystem.logger.isWarnEnabled()) {
					JiazuSubSystem.logger.warn(player.getLogString() + "[发布家族buff:{}] [受益者:{}] [持续:{}MS] [BUFF等级:{}]", new Object[] { buffName, p.getLogString(), delay, bzxLevel });
				}
			}
		} catch (Exception e) {
			JiazuSubSystem.logger.error(player.getLogString() + "[发布家族BUFF] [异常]", e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}

	@Override
	public boolean canSee(Player player) {
		if (!player.inSelfSeptStation()) {
			return false;
		}
		SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(player.getJiazuId());
		if (septStation == null) {
			if (JiazuSubSystem.logger.isInfoEnabled()) {
				JiazuSubSystem.logger.info(player.getLogString() + "[驻地没拿到]");
			}
			return false;
		}
		SeptBuildingEntity biaozhixiang = septStation.getCurrentBiaozhixiang();
		if (JiazuSubSystem.logger.isInfoEnabled()) {
			JiazuSubSystem.logger.info(player.getLogString() + "[biaozhixiang:" + biaozhixiang + "] [biaozhixiang.getNpc():" + (biaozhixiang == null ? "---" : biaozhixiang.getNpc()) + "] [this:" + this + "] [ this.getNPC() :" + this.getNPC() + "]");
		}
		if (biaozhixiang != null) {// && biaozhixiang.getNpc().getId() == this.getNPC().getId()) {
			BuildingType buildingType = biaozhixiang.getBuildingState().getTempletType().getBuildingType();
			if (buildingType.getIndex() == this.buildIndex) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	public int getBuildIndex() {
		return buildIndex;
	}

	public void setBuildIndex(int buildIndex) {
		this.buildIndex = buildIndex;
	}
}
