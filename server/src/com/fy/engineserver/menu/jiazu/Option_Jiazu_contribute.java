package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;

public class Option_Jiazu_contribute extends Option {

	private long money;

	public Option_Jiazu_contribute(long money) {
		this.money = money;
	}

	@Override
	public void doSelect(Game game, Player player) {
		JiazuSubSystem.logger.warn(player.getLogString() + "[点击捐钱菜单 ]");
		synchronized (player) {
			if (player.getJiazuId() <= 0) {
				player.sendError(Translate.text_jiazu_031);
				return;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				player.sendError(Translate.text_jiazu_031);
				return;
			}
			JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
			if (member == null) {
				player.sendError(Translate.text_jiazu_031);
				return;
			}

			if (player.getSilver() + player.getShopSilver() < money || money <= 0) {
				player.sendError(Translate.text_jiazu_103);
				return;
			}
			SeptStation ss = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
			if (ss == null) {
				player.sendError(Translate.家族没有驻地);
				return ;
			}
			if ((money + jiazu.getJiazuMoney()) > ss.getInfo().getMaxCoin()) {
				player.sendError(Translate.家族资金超上限);
				return;
			}

			try {
				BillingCenter.getInstance().playerExpense(player, money, CurrencyType.SHOPYINZI, ExpenseReasonType.JIAZU_CONTRIBUTE, "家族捐献", -1);
				int contribute = (int) (money / JiazuManager.moneyExchangeContribution);
				jiazu.addJiazuMoney(money);
//				synchronized (jiazu) {
//					jiazu.setJiazuMoney(jiazu.getJiazuMoney() + money);
//				}
				
				if (contribute > 0) {
					member.setCurrentWeekContribution(member.getCurrentWeekContribution() + contribute);
					if (JiazuSubSystem.logger.isWarnEnabled()) {
						JiazuSubSystem.logger.warn(player.getLogString() + "[获得贡献度] [捐献银子] [{}] [增加贡献度:{}] [增加后贡献度:{}]", new Object[] { money, contribute, member.getCurrentWeekContribution() });
					}
				}
				member.setContributeMoney(member.getContributeMoney() + money);
				jiazu.initMember4Client();

				ChatMessageService.getInstance().sendMessageToJiazu(jiazu, Translate.translateString(Translate.text_jiazu_108, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, BillingCenter.得到带单位的银两(money) } }), "");
			} catch (Exception e) {
				JiazuSubSystem.logger.error(player.getLogString() + "[家族捐献异常][要捐献钱:" + money, e);
			}

		}

	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
