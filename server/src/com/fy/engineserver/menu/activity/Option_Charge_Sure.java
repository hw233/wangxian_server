package com.fy.engineserver.menu.activity;

import java.util.List;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.chongzhiActivity.ChargePackageActivity;
import com.fy.engineserver.activity.chongzhiActivity.ChargeRecord;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CHARGE_AGRS_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;

public class Option_Charge_Sure extends Option {


	public Option_Charge_Sure() {
	}

	@Override
	public void doSelect(Game game, Player player) {

		Passport passport = player.getPassport();
		if(passport != null){
			String channelName = passport.getLastLoginChannel();
			if(channelName != null && !channelName.isEmpty()){
				List<ChargeMode> list = ChargeManager.getInstance().getChannelChargeModes(channelName);
				if (list == null || list.size() == 0) {
					player.sendError(Translate.无匹的充值信息请联系GM);
					ActivitySubSystem.logger.error(player.getLogString() + "[查询充值列表] [异常] [无匹配的充值列表] [渠道:{}] [玩家:{}]",new Object[]{channelName,player.getLogString()});
					return;
				}
				
				ChargeRecord record = ActivityManager.getInstance().getChargeRecord(player);
				int nextDays = record.getDays() + 1;
				ChargePackageActivity activity = null;
				int maxDays = 0;
				for(ChargePackageActivity a : ActivityManager.getInstance().chargePackageActivity){
					if(a.getDays() > maxDays){
						maxDays = a.getDays();
					}
				}
				
				if(nextDays > maxDays){
					nextDays = maxDays;
				}
				
				for(ChargePackageActivity a : ActivityManager.getInstance().chargePackageActivity){
					if(nextDays == a.getDays()){
						activity = a;
					}
				}
				
				if(activity == null){
					ActivitySubSystem.logger.warn("[处理玩家购买充值送礼礼包] [出错:活动配置错误] [nextDays:{}] [maxDays:{}] [玩家:{}]",new Object[]{nextDays,maxDays,player.getLogString()});
					return;
				}
				
				long money = activity.getMoney();
				
				long chargeMoney = money * 100;
				ChargeMode chargemode = null;
				String chargeId = "";
				String specConfig = "";
				end:for(ChargeMode mode : list){
					if(mode != null && mode.getModeName().equals("支付宝")){
						if(mode.getMoneyConfigures() != null){
							for(ChargeMoneyConfigure config : mode.getMoneyConfigures()){
								if(config.getDenomination() == chargeMoney){
									chargemode = mode;
									chargeId = config.getId();
									specConfig = config.getSpecialConf();
									break end;
								}
							}
						}
					}
				}
				
				if(chargemode == null){
					end:for(ChargeMode mode : list){
						if(mode != null && mode.getMoneyConfigures() != null){
							for(ChargeMoneyConfigure config : mode.getMoneyConfigures()){
								if(config.getDenomination() == chargeMoney){
									chargemode = mode;
									chargeId = config.getId();
									specConfig = config.getSpecialConf();
									break end;
								}
							}
						}
					}
				}
				
				if(chargemode == null){
					player.sendError(Translate.暂时不能充值联系GM);
					return;
				}
				
				CHARGE_AGRS_RES res = new CHARGE_AGRS_RES(GameMessageFactory.nextSequnceNum(), chargemode.getModeName(), chargeId, specConfig, nextDays+"", SavingReasonType.充值获取礼包, chargeMoney, new String[0]);
				player.addMessageToRightBag(res);	
			}else{
				if(ActivitySubSystem.logger.isInfoEnabled()){
					ActivitySubSystem.logger.info("[充值购买礼包确认] [失败] [channelName==null] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "]");
				}
			}
		}else{
			if(ActivitySubSystem.logger.isInfoEnabled()){
				ActivitySubSystem.logger.info("[充值购买礼包确认] [失败] [passport==null] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "]");
			}
		}
		
	
	}

	@Override
	public byte getOType() {
		// TODO Auto-generated method stub
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
