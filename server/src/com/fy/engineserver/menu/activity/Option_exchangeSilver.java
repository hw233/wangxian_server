package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.YinPiaoEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 将shopSilver转换为silver并删除玩家身上的银票
 * 
 * @date on create 2016年6月2日 下午3:33:19
 */

public class Option_exchangeSilver extends Option implements NeedCheckPurview{
	
	public static Platform[] platForms = new Platform[]{Platform.官方};
	public static String[] serverNames = new String[]{"国内本地测试", "天下无双", "群龙聚首", "刀剑如梦", "跃马弯弓"};
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void doSelect(Game game, Player player) {
		if (!canSee(player)) {
			return ;
		}
		synchronized (player) {
			ArticleEntity entity = player.removeArticle(Translate.银票, "兑换删除银票道具", "");
			if (entity == null) {
				player.sendError("您身上沒有银票，不用继续兑换！");
				return ;
			}
			long shopSilver = ((YinPiaoEntity)entity).getHaveMoney();
			if (shopSilver > 0) {
				long oldSilver = player.getSilver();
				long oldShopSilver = player.getShopSilver();
				player.setShopSilver(0);
				player.setSilver(player.getSilver() + shopSilver);
				Game.logger.warn("[玩家兑换银票] [成功] [" + player.getLogString() + "] [oldShopSilver:" + shopSilver + "] [silver:" + oldSilver + "->" + player.getSilver() + "] [oldShopSilver:" + oldShopSilver + "]");
				player.sendError("已经将您身上" + BillingCenter.得到带单位的银两(shopSilver) + "银票转换为银子！");
			} else {
				if (TransitRobberyManager.logger.isInfoEnabled()) {
					TransitRobberyManager.logger.info("[银票没有银子] [" + player.getLogString() + "] [shops:" + player.getShopSilver() + "]");
				}
			}
		}
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		if (PlatformManager.getInstance().isPlatformOf(platForms)) {
			String svname = GameConstants.getInstance().getServerName();
			for (int i=0; i<serverNames.length; i++) {
				if (serverNames[i].equals(svname)) {
					return true;
				}
			}
		}
		return false;
	}

}
