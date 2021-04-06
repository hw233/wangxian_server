package com.fy.engineserver.menu.zhaoHuiMiMa;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

public class Option_InputMiBao extends Option {
	
	private int find_Type;				//查找的密码类型，0仓库密码   1家族密码    2宗派密码	  3战队密码
	
	private int battleType;

	public int getBattleType() {
		return this.battleType;
	}

	public void setBattleType(int battleType) {
		this.battleType = battleType;
	}

	public Option_InputMiBao(){
		super();
	}
	
	public Option_InputMiBao(int type){
		super();
		this.find_Type = type;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doInput(Game game, Player player, String input) {
		String secretAnswer = player.getPassport().getSecretAnswer();
		if ("".equals(input)) {
			player.sendError(Translate.请输入密保答案);
			return;
		}
		if (secretAnswer == null || "".equals(secretAnswer)) {
			player.sendError(Translate.您还未设置密保);
			return;
		} else {
			if (input.equals(secretAnswer)) {
				if (find_Type == 0) {
					if ("".equals(player.getCangkuPassword())) {
						player.sendError(Translate.您没有仓库密码);
					}else {
						player.sendError(Translate.您的仓库密码是 + player.getCangkuPassword());
					}
				}else if (find_Type == 1) {
					Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
					if (jiazu == null) {
						player.sendError(Translate.很抱歉您没有家族无法为您找回密码);
					}else {
						if (jiazu.getJiazuMaster() == player.getId()) {
							player.send_HINT_REQ(Translate.您的家族密码是 + jiazu.getJiazuPassword());
						}else {
							player.sendError(Translate.很抱歉您非此家族族长无法为您找回密码);
						}
					}
					
				}else if (find_Type == 2) {
					ZongPai zongpai = ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId());
					if (zongpai == null) {
						player.sendError(Translate.很抱歉您没有宗派无法为您找回密码);
					}else {
						if (zongpai.getMasterId() == player.getId()) {
							player.send_HINT_REQ(Translate.您的宗派密码是 + zongpai.getPassword());
						}else {
							player.sendError(Translate.很抱歉您非此宗派宗主无法为您找回密码);
						}
					}
				}
			}else {
				player.sendError(Translate.很抱歉您的密保答案错误请您重新填写);
			}
		}
	}

	public int getFind_Type() {
		return find_Type;
	}

	public void setFind_Type(int find_Type) {
		this.find_Type = find_Type;
	}
	
}
