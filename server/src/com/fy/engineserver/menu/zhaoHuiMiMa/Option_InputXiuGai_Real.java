package com.fy.engineserver.menu.zhaoHuiMiMa;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.text.WordFilter;

public class Option_InputXiuGai_Real extends Option implements NeedCheckPurview {
	
	private int xiugai_Type;				//修改 类型    0 为家族name  1为宗派name
	
	private String serverNames;

	public Option_InputXiuGai_Real(){
		super();
	}
	
	
	public Option_InputXiuGai_Real(int xiugai_Type, String serverNames) {
		this.xiugai_Type = xiugai_Type;
		this.serverNames = serverNames;
	}


	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doInput(Game game, Player player, String input) {
		try{
		if (xiugai_Type == 0) {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				player.sendError(Translate.你还没有家族);
				return;
			}
			if (jiazu.getName().indexOf(UnitedServerManager.GaiMing_BiaoJi) < 0) {
				player.sendError(Translate.text_jiazu_183);
				return;
			}
			if (jiazu.getJiazuMaster() != player.getId()) {
				player.sendError(Translate.text_jiazu_182);
				return;
			}
			if (input.equals("")) {
				player.sendError(Translate.text_jiazu_184);
				return;
			}
			WordFilter filter = WordFilter.getInstance();
			boolean validName = input.getBytes().length <= JiazuSubSystem.NAME_MAX && filter.cvalid(input, 0) && filter.evalid(input, 1) && JiazuSubSystem.getInstance().tagValid(input);
			if (!validName) {
				player.sendError(Translate.text_jiazu_129);
				return;
			}
			if (JiazuManager.getInstance().getJiazu(input) != null) {
				player.sendError(Translate.text_jiazu_016);
				return;
			}
			jiazu.setName(input);
			JiazuManager.getInstance().getJiazuLruCacheByName().put(input, jiazu);
			Player[] players = PlayerManager.getInstance().getCachedPlayers();
			for (Player p : players) {
				if (p.getJiazuId() == jiazu.getJiazuID()) {
					p.initJiazuTitleAndIcon();
				}
			}
		}else if (xiugai_Type == 1) {
			ZongPai pai = ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId());
			if(pai == null) {
				player.sendError(Translate.您的家族没有宗派);
				return;
			}
			if (pai.getZpname().indexOf(UnitedServerManager.GaiMing_BiaoJi) < 0) {
				player.sendError(Translate.你的宗派不能修改名字);
				return;
			}
			if(pai.getMasterId() != player.getId()){
				player.sendError(Translate.你不是宗主不能修改宗派名字);
				return;
			}
			if (input.equals("")) {
				player.sendError(Translate.请输入你要修改的宗派名字);
				return;
			}
			String re = ZongPaiManager.getInstance().zongPaiNameValidate(input.trim(), pai.getDeclaration());
			if (re != null && !"".equals(re)) {
				player.sendError(Translate.您输入的宗派名字含有禁用字符或敏感字符请重新输入);
				return;
			}
			pai.setZpname(input);
			ZongPaiManager.em.flush(pai);
			Player[] players = PlayerManager.getInstance().getCachedPlayers();
			for (Player p : players) {
				ZongPai zp = ZongPaiManager.getInstance().getZongPaiByPlayerId(p.getId());
				if (zp != null && zp.getId() == pai.getId()) {
					p.initZongPaiName();
				}
			}
		}
		}catch(Exception e) {
			UnitedServerManager.logger.error("修改名称出错 [" + player.getLogString() + "] [" + xiugai_Type + "]", e);
		}
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
		return false;
	}
	
}
