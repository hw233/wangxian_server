package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivity;
import com.fy.engineserver.activity.chongzhiActivity.XiaoFeiServerConfig;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.TradeManager;

public class Option_XiaoFeiChaXun extends Option implements NeedCheckPurview{

	public Option_XiaoFeiChaXun() {
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		//已经废弃
//		try{
//			XiaoFeiServerConfig config = ChongZhiActivity.getInstance().getXiaoFeiConfig4(player);
//			if (config == null) {
//				return;
//			}
//			Long money = ChongZhiActivity.getInstance().getXiaoFeiMoneys().get(player.getId());
//			if (money == null) {
//				money = 0L;
//			}
//			String type = "";
//			for (int i = 0; i < config.getXiaoFeiTongDao().length; i++) {
//				if (config.getXiaoFeiTongDao()[i] == 0) {
//					type += "商城购物";
//				}else if (config.getXiaoFeiTongDao()[i] == 1) {
//					type += "交易扣税";
//				}
//				if (i != config.getXiaoFeiTongDao().length - 1) {
//					type += "及";
//				}
//			}
//			String des = "NPC菜单：查询消费活动\n";
//			des += "您当前的消费积分：\n";
//			des += "<f color='#ffff00'>【银子】</f>";
//			des += TradeManager.putMoneyToMyText(money);
//			des += "\n";
//			des += "活动期间，您在<f color='#FFFF00' >"+type+"</f>每消费满<f color='#FFFF00' >"+(config.getMoney()/1000/1000)+"锭</f>，即可通过邮件获得<f color='#FFFF00' >"+config.getPropName()+"</f>1个，可随机开出元气丹、蓝色育兽丹、飞行坐骑碎片等珍贵物品。\n";
//			des += "<f color='#FFFF00' >注：以上消费积分满"+(config.getMoney()/1000/1000)+"锭后清零重新累计，活动期间消费积分每满"+(config.getMoney()/1000/1000)+"锭可获得1次奖励。</f>\n";
//			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
//			mw.setDescriptionInUUB(des);
//			mw.setOptions(new Option[]{});
//			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
//			player.addMessageToRightBag(req);
//		}catch(Exception e) {
//			ChongZhiActivity.logger.error("查询消费积分出错:" + player.getLogString(), e);
//		}
	}

	@Override
	public boolean canSee(Player player) {
		//已经废弃
//		XiaoFeiServerConfig config = ChongZhiActivity.getInstance().getXiaoFeiConfig4(player);
//		if (config != null) {
//			if (config.isStart()) {
//				return true;
//			}
//		}
		return false;
	}
}
