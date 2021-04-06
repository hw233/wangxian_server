package com.fy.engineserver.menu.cave;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.resource.IncreaseScheduleCfg;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.TimeTool;

public class Option_Cave_Check_IncreaseSchedule extends CaveOption implements NeedRecordNPC,
		NeedCheckPurview {

	public Option_Cave_Check_IncreaseSchedule() {

	}

	@Override
	public void doSelect(Game game, Player player) {
		FaeryManager faeryManager = FaeryManager.getInstance();
		List<IncreaseScheduleCfg> list = faeryManager.getIncreaseScheduleCfgs();

		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60000);
		mw.setNpcId(getNpc() == null ? 0 : getNpc().getId());
		List<Option> optionList = new ArrayList<Option>();

		StringBuffer sbf = new StringBuffer();
		for (IncreaseScheduleCfg cfg : list) {
			sbf.append(Translate.translateString(Translate.text_cave_077, new String[][] { { Translate.STRING_1, cfg.getArticleName() }, { Translate.COUNT_1, String.valueOf(cfg.getIncreaseNum()) }, { Translate.COUNT_2, TimeTool.instance.getShowTime(cfg.getIncreaseTime()) } }));
			sbf.append("\n");
			Option_Cave_IncreaseSchedule schedule = new Option_Cave_IncreaseSchedule(cfg);
			schedule.setNpc(npc);
			schedule.setText(Translate.translateString(Translate.text_cave_078, new String[][] { { Translate.STRING_1, cfg.getArticleName() } }));
			optionList.add(schedule);
		}
		Option[] options = optionList.toArray(new Option[0]);

		mw.setDescriptionInUUB(sbf.toString());
		mw.setOptions(options);
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, options);
		player.addMessageToRightBag(res);
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = (CaveNPC) npc;
	}

	@Override
	public boolean canSee(Player player) {
		return FaeryManager.isSelfCave(player, getNpc().getId());
	}
}
