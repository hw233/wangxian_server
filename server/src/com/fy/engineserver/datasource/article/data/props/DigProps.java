package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.dig.DigManager;
import com.fy.engineserver.activity.dig.DigPropsEntity;
import com.fy.engineserver.activity.dig.DigTemplate;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.Option_Diliver;
import com.fy.engineserver.menu.activity.Option_FindWay;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tool.GlobalTool;

public class DigProps extends Props {

	@Override
	public String canUse(Player p) {

		return super.canUse(p);
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {

		try {
			try {
				String vS = GlobalTool.verifyTransByOther(player.getId());
				if (vS != null) {
					player.sendError(Translate.限制地图不允许使用此道具);
					return false;
				}
			} catch (Exception e) {}
			if (ae instanceof DigPropsEntity) {
				Map<Long, DigTemplate> digInfo = player.getDigInfo();
				if (digInfo == null) {
					player.setDigInfo(new HashMap<Long, DigTemplate>());
				}
				digInfo = player.getDigInfo();

				if (!digInfo.containsKey(ae.getId())) {
					if (digInfo.size() >= DigManager.MAXNUM) {
						player.sendError("已使用藏宝图过多，请寻宝后再使用新的藏宝图!");
						return false;
					}
					DigTemplate dTemplateRandom = DigManager.getInstance().randomGetDigTemplate();
					DigTemplate dTemplate;
					try {
						dTemplate = (DigTemplate) dTemplateRandom.clone();
						if (dTemplate.getInCountry() == 0) {
							dTemplate.setCountry((byte) 0);
						} else {
							dTemplate.setCountry(player.getCountry());
						}
						digInfo.put(ae.getId(), dTemplate);
						player.setDigInfo(digInfo);
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
				DigTemplate dt = digInfo.get(ae.getId());
				if (dt != null) {
					int x = player.getX();
					int y = player.getY();
					if (dt.getPoints().x == x && dt.getPoints().y == y && player.getCurrentGame().gi.name.equals(dt.getMapName()) && player.getCurrentGameCountry() == dt.getCountry()) {
						ArticleEntity aee=player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "挖宝使用删除", true);
						if(aee==null){
							String description = Translate.删除物品不成功;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							if (ActivitySubSystem.logger.isWarnEnabled()) ActivitySubSystem.logger.warn("[使用挖宝道具] ["+description+"] [id:"+ae.getId()+"]");
							return false;
						}
						TaskSubSystem.logger.error(player.getLogString() + "[找到挖宝位置,删除挖宝道具]");
						player.getDigInfo().remove(ae.getId());
						player.setDigInfo(player.getDigInfo());
						// TODO 生成事件
						{
							DigManager.getInstance().noticeFindRightposition(player, game, ae.getArticleName());
							ActivenessManager.getInstance().record(player, ActivenessType.挖宝);
						}
						try {
							ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
							strongMaterialEntitys.add(ae);
							ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
						} catch (Exception e) {
							ActivitySubSystem.logger.error("[使用赠送活动] [使用挖宝道具] [" + player.getLogString() + "]", e);
						}
					} else {
						WindowManager windowManager = WindowManager.getInstance();
						MenuWindow mw = windowManager.createTempMenuWindow(600);
						mw.setDescriptionInUUB(Translate.translateString(Translate.挖宝前往, new String[][] { { Translate.STRING_1, GameManager.getInstance().getDisplayName(dt.getMapName(), dt.getCountry()) } }));
						Option_Diliver option_diliver = new Option_Diliver((DigPropsEntity) ae);
						option_diliver.setText(Translate.传送);
						Option_FindWay option_findWay = new Option_FindWay((DigPropsEntity) ae);
						option_findWay.setText(Translate.寻路);
						mw.setOptions(new Option[] { option_diliver, option_findWay });
						QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
						player.addMessageToRightBag(req);
						return true;
					}
				} else {
					TaskSubSystem.logger.error("[使用挖宝图失败] [" + player.getLogString() + "] [物品不存在]");
				}
			} else {
				TaskSubSystem.logger.error("[使用挖宝图失败] [" + player.getLogString() + "] [道具名:" + ae.getArticleName() + "] [类型:" + ae.getClass().toString() + "]");
			}
		} catch (Exception e) {
			TaskSubSystem.logger.error("[使用挖宝图失败] [" + player.getLogString() + "]", e);
		}

		return false;
	}

}
