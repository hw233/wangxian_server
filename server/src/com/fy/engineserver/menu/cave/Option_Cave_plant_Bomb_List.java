package com.fy.engineserver.menu.cave;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.cave.CaveFieldBombConfig;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.smith.ArticleMaker;
import com.fy.engineserver.sprite.Player;

/**
 * 放置炸弹选择列表
 * @author Administrator
 *         2014-5-14
 * 
 */
public class Option_Cave_plant_Bomb_List extends CaveOption implements NeedCheckPurview {

	private CaveFieldBombConfig bombConfig;

	public static int CAVE_LEVEL_LIMIT = 5;

	public Option_Cave_plant_Bomb_List(CaveFieldBombConfig bombConfig) {
		super();
		this.bombConfig = bombConfig;
	}

	public CaveFieldBombConfig getBombConfig() {
		return bombConfig;
	}

	public void setBombConfig(CaveFieldBombConfig bombConfig) {
		this.bombConfig = bombConfig;
	}

	public Option_Cave_plant_Bomb_List() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSelect(Game game, Player player) {
		// 点击弹出多个列表
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(60);
		mw.setNpcId(getNpc().getId());
		List<Option> optionList = new ArrayList<Option>();
		for (CaveFieldBombConfig config : FaeryManager.getInstance().getBombConfigs()) {
			Option_Cave_plant_Bomb plant_Bomb = new Option_Cave_plant_Bomb(config);
			int hasNum = player.getArticleNum(config.getArticleName(), config.getArticleColor(), BindType.BOTH);

			plant_Bomb.setText("<f color='" + (hasNum > 0 ? 0xff00ff : 0xffffff) + "'>" + Translate.translateString(Translate.放置炸弹 + " (" + hasNum + ")</f>", new String[][] { { Translate.STRING_1, config.getArticleName() } }));
			plant_Bomb.setNpc(getNpc());
			optionList.add(plant_Bomb);
		}
		Option[] options = optionList.toArray(new Option[optionList.size()]);
		mw.setOptions(options);
		player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
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
		if (cave.getMainBuilding().getGrade() < CAVE_LEVEL_LIMIT) {
			return false;
		}
		CaveField caveField = (CaveField) caveBuilding;
		if (caveField.getPlantStatus() == null) {
			return false;
		}
		if (caveField.getPlantStatus().getOutShowStatus() == FaeryConfig.PLANT_AVATA_STATUS_GROWNUP) {
			return false;
		}

		if (caveField.getCaveFieldBombData() != null) {
			return false;
		}
		return true;
	}
}
