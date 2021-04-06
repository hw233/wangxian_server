package com.fy.engineserver.menu.society.zongpai;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

public class Option_ZongPai_zhaoji_Agree extends Option {

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	private long inviteId;

	private int x;
	private int y;
	private String gameName;
	private int country;

	public Option_ZongPai_zhaoji_Agree(long inviteId, int x, int y, String gameName, int country) {
		super();
		this.inviteId = inviteId;
		this.x = x;
		this.y = y;
		this.gameName = gameName;
		this.country = country;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	@Override
	public void doSelect(Game game, Player player) {

		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu != null) {
			ZongPai zongPai = ZongPaiManager.getInstance().getZongPaiById(jiazu.getZongPaiId());
			if (zongPai != null) {
				long masterId = zongPai.getMasterId();
				if (masterId == inviteId) {
					try {
						Player invite = PlayerManager.getInstance().getPlayer(inviteId);
						if (!invite.isOnline()) {
							// player.sendError(invite.getName()+"不在线，不能传送");
							player.sendError(Translate.translateString(Translate.xx不在线不能传送, new String[][] { { Translate.STRING_1, invite.getName() } }));
							if (ZongPaiManager.logger.isWarnEnabled()) {
								ZongPaiManager.logger.warn("[宗主令传送不能完成] [不在线不能传送] [" + player.getLogString() + "] [" + invite.getLogString() + "]");
							}
							return;
						}
						int country = this.country;
						player.setTransferGameCountry(country);

						Game game1 = GameManager.getInstance().getGameByName(this.gameName, this.country);// invite.getCurrentGame();
						if (game1 == null) {
							return;
						}
						if (game1.getGameInfo().getName().contains(SeptStationManager.jiazuMapName)) {

							if (invite.getJiazuId() > 0) {
								if (invite.getJiazuId() != player.getJiazuId()) {
									player.sendError(Translate.translateString(Translate.xx在家族地图中你俩不是一个家族不能到达, new String[][] { { Translate.STRING_1, invite.getName() } }));
									invite.sendError(Translate.translateString(Translate.你在家族地图中xx跟你不是一个家族不能到达, new String[][] { { Translate.STRING_1, player.getName() } }));
									if (ZongPaiManager.logger.isWarnEnabled()) {
										ZongPaiManager.logger.warn("[宗主令传送不能完成] [不是一个家族] [" + player.getLogString() + "] [" + invite.getLogString() + "]");
									}
									return;
								}
							} else {
								if (ZongPaiManager.logger.isWarnEnabled()) {
									ZongPaiManager.logger.warn("[宗主令传送异常] [没有家族但在家族地图] [" + player.getLogString() + "] [" + invite.getLogString() + "]");
								}
							}
						}
						game.transferGame(player, new TransportData(0, 0, 0, 0, game1.gi.name, this.x, this.y));

						if (ZongPaiManager.logger.isWarnEnabled()) {
							ZongPaiManager.logger.warn("[宗主令传送完成] [" + player.getLogString() + "] []");
						}
					} catch (Exception e) {
						ZongPaiManager.logger.warn("[宗主令传送异常] [" + player.getLogString() + "]", e);
					}
				} else {
					player.sendError(Translate.宗主换了);
					ZongPaiManager.logger.warn("[同意宗主令传送失败] [不是宗主了] [" + player.getLogString() + "]");
				}
			} else {
				ZongPaiManager.logger.warn("[同意宗主令传送失败] [没有宗派] [" + player.getLogString() + "]");
			}
		} else {
			ZongPaiManager.logger.warn("[同意宗主令传送失败] [没有家族] [" + player.getLogString() + "]");
		}
	}

	public long getInviteId() {
		return inviteId;
	}

	public void setInviteId(long inviteId) {
		this.inviteId = inviteId;
	}

}
