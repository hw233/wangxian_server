package com.fy.engineserver.menu.zhaoHuiMiMa;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.Option_InputProp;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.text.WordFilter;

public class Option_InputXiuGai extends Option implements NeedCheckPurview {

	private int xiugai_Type; // 修改 类型 0 为家族name 1为宗派name

	private String serverNames;

	public Option_InputXiuGai() {
		super();
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	// public void doInput(Game game, Player player, String input) {
	// try{
	// if (xiugai_Type == 0) {
	// Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
	// if (jiazu == null) {
	// player.sendError(Translate.你还没有家族);
	// return;
	// }
	// if (jiazu.getName().indexOf(UnitedServerManager.GaiMing_BiaoJi) < 0) {
	// player.sendError(Translate.text_jiazu_183);
	// return;
	// }
	// if (jiazu.getJiazuMaster() != player.getId()) {
	// player.sendError(Translate.text_jiazu_182);
	// return;
	// }
	// if (input.equals("")) {
	// player.sendError(Translate.text_jiazu_184);
	// return;
	// }
	// WordFilter filter = WordFilter.getInstance();
	// boolean validName = input.getBytes().length <= JiazuSubSystem.NAME_MAX && filter.cvalid(input, 0) && filter.evalid(input, 1) && JiazuSubSystem.getInstance().tagValid(input);
	// if (!validName) {
	// player.sendError(Translate.text_jiazu_129);
	// return;
	// }
	// if (JiazuManager.getInstance().getJiazu(input) != null) {
	// player.sendError(Translate.text_jiazu_016);
	// return;
	// }
	// jiazu.setName(input);
	// Player[] players = PlayerManager.getInstance().getOnlinePlayers();
	// for (Player p : players) {
	// if (p.getJiazuId() == jiazu.getJiazuID()) {
	// p.initJiazuTitleAndIcon();
	// }
	// }
	// }else if (xiugai_Type == 1) {
	// ZongPai pai = ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId());
	// if(pai == null) {
	// player.sendError(Translate.您的家族没有宗派);
	// return;
	// }
	// if (pai.getZpname().indexOf(UnitedServerManager.GaiMing_BiaoJi) < 0) {
	// player.sendError(Translate.你的宗派不能修改名字);
	// return;
	// }
	// if(pai.getMasterId() != player.getId()){
	// player.sendError(Translate.你不是宗主不能修改宗派名字);
	// return;
	// }
	// if (input.equals("")) {
	// player.sendError(Translate.请输入你要修改的宗派名字);
	// return;
	// }
	// String re = ZongPaiManager.getInstance().zongPaiNameValidate(input, pai.getDeclaration());
	// if (re != null && !"".equals(re)) {
	// player.sendError(Translate.您输入的宗派名字含有禁用字符或敏感字符请重新输入);
	// return;
	// }
	// pai.setZpname(input);
	// Player[] players = PlayerManager.getInstance().getOnlinePlayers();
	// for (Player p : players) {
	// if (ZongPaiManager.getInstance().getZongPaiByPlayerId(p.getId()).getId() == pai.getId()) {
	// p.initZongPaiName();
	// }
	// }
	// }
	// }catch(Exception e) {
	// UnitedServerManager.logger.error("修改名称出错 [" + player.getLogString() + "] [" + xiugai_Type + "]", e);
	// }
	// }

	@Override
	public void doSelect(Game game, Player player) {
		
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(120);
		Option_InputXiuGai_Real option_InputXiuGai = new Option_InputXiuGai_Real(xiugai_Type, serverNames);
		option_InputXiuGai.setText(Translate.确定);
		String str = "请输入内容";
		if (xiugai_Type < Translate.请输入新的名字.length) {
			str = Translate.请输入新的名字[xiugai_Type];
		}
		mw.setDescriptionInUUB(str);
		//
		// Option_Cancel cancle = new Option_Cancel();
		// cancle.setText(Translate.取消);
		mw.setOptions(new Option[] {  option_InputXiuGai });
		

		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 50, " ", Translate.确定, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);
	}

	public void setXiugai_Type(int xiugai_Type) {
		this.xiugai_Type = xiugai_Type;
	}

	public int getXiugai_Type() {
		return xiugai_Type;
	}

	public String getServerNames() {
		return serverNames;
	}

	public void setServerNames(String serverNames) {
		this.serverNames = serverNames;
	}

	@Override
	public boolean canSee(Player player) {
		if (serverNames.contains(GameConstants.getInstance().getServerName())) {
			return true;
		}
		if (UnitedServerManager.getInstance().getServer4Rename().contains(GameConstants.getInstance().getServerName())) {
			return true;
		}
		return false;
	}

}
