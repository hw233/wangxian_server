package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 开垦田地
 * 
 * 
 */
public class Option_Cave_AssartField extends CaveOption {

	public Option_Cave_AssartField() {

	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			Cave cave = getNpc().getCave();
			if (!FaeryManager.isSelfCave(player, getNpc().getId())) {// 不是自己的庄园不能点击升级
				player.sendError(Translate.text_cave_010 + ":" + getNpc().getId());
				return;
			}
			CaveBuilding cBuilding = cave.getCaveBuildingByNPCId(getNpc().getId());

			CompoundReturn cr = cave.assartField(cBuilding.getType(), player);
			String failreason = Translate.text_cave_001;
			if (!cr.getBooleanValue()) {
				switch (cr.getIntValue()) {
				case 1:
					failreason = Translate.text_cave_017;
					break;
				case 2:
					failreason = Translate.text_cave_018;
					break;
				case 3:
//					failreason = Translate.text_cave_019;
					int nextGrade = 0;
					int currentMainGrade = cave.getMainBuilding().getGrade();// 当前主建筑等级
					int fieldLimit = FaeryManager.getInstance().getMainCfgs()[currentMainGrade - 1].getFieldNumLimit();
					for (int i = currentMainGrade - 1; i < FaeryManager.getInstance().getMainCfgs().length; i++) {
						if (fieldLimit < FaeryManager.getInstance().getMainCfgs()[i].getFieldNumLimit()) {
							nextGrade = i + 1;
							break;
						}
					}
					if (nextGrade == 0) {
						failreason = Translate.text_cave_062;
						JiazuSubSystem.logger.error(player.getLogString() + "[开垦田地][异常][拿不到更高级别的建筑开放开垦][现在主建筑等级]", new Object[] { currentMainGrade });
					} else {
						failreason = Translate.translateString(Translate.text_cave_019, new String[][] { { Translate.STRING_1, String.valueOf(nextGrade) } });
					}
					break;
				case 4:
					// failreason = Translate.translateString(Translate.text_cave_020, new String[][] { { Translate.STRING_1, cr.getStringValues()[0] }, { Translate.STRING_2,
					// cr.getStringValues()[1] }, { Translate.STRING_3, cr.getStringValues()[2] } });
					// 当不能开垦是因为田地令不足的时候,直接弹出菜单提示玩家是否直接扣除银子来开垦土地
					String propName = cr.getStringValues()[0];
					int need = Integer.valueOf(cr.getStringValues()[1]);
					int has = Integer.valueOf(cr.getStringValues()[2]);
					long needSilver = (need - has) * FaeryManager.assartFieldArticlePrize;
					Option_Cave_AssartField_Confirm confirm = new Option_Cave_AssartField_Confirm(getNpc(), need, has, needSilver);
					MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
					// 你的@STRING_1@不足@COUNT_1@个，是否使用@COUNT_2@个@STRING_2@并消耗@STRING_3@银子补齐不足的@STRING_4@数量开启田地
					// mw.setDescriptionInUUB("你的" + propName + "不足" + cr.getStringValues()[1] + "个，是否使用" + cr.getStringValues()[1] + "个" + propName + "并消耗" +
					// BillingCenter.得到带单位的银两(needSilver) + "银子补齐不足的" + propName + "数量开启田地");
					mw.setDescriptionInUUB(Translate.translateString(Translate.text_cave_105, new String[][] { { Translate.STRING_1, cr.getStringValues()[0] }, { Translate.COUNT_1, cr.getStringValues()[1] }, { Translate.COUNT_2, cr.getStringValues()[2] }, { Translate.STRING_2, cr.getStringValues()[2] }, { Translate.STRING_3, BillingCenter.得到带单位的银两(needSilver) }, { Translate.STRING_4,propName } }));
					mw.setTitle(Translate.text_cave_104);
					Option_UseCancel oc = new Option_UseCancel();
					oc.setText(Translate.取消);
					oc.setColor(0xffffff);

					confirm.setText(Translate.确定);
					oc.setColor(0xffffff);
					mw.setOptions(new Option[] { confirm, oc });
					QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					player.addMessageToRightBag(res);
					return;
				default:
					break;
				}
			}
			player.sendNotice(failreason);
		} catch (Exception e) {
			CaveSubSystem.logger.error(this.getClass().getName(), e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (!FaeryManager.isSelfCave(player, getNpc().getId())) {
			return false;
		}
		Cave cave = FaeryManager.getInstance().getCave(player);
		CaveBuilding caveBuilding = cave.getCaveBuildingByNPCId(getNpc().getId());
		if (caveBuilding == null || !(caveBuilding instanceof CaveField)) {
			return false;
		}

		CaveField caveField = (CaveField) caveBuilding;
		if (caveField.getAssartStatus() != FaeryConfig.FIELD_STATUS_DESOLATION) {
			return false;
		}
		return true;
	}
}
