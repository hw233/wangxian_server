package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.FIND_WAY_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 寻路窗口,都可用这个 其他数据可自行创建构造器
 * @author Administrator
 * 2014-5-21
 *
 */
public class Option_Find_Way extends Option {

	private int x;
	private int y;
	private String gameName;

	public Option_Find_Way(int x, int y, String gameName) {
		this.x = x;
		this.y = y;
		this.gameName = gameName;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		FIND_WAY_RES res = new FIND_WAY_RES();
		res.setIntx(this.x);
		res.setInty(this.y);
		res.setMapName(this.gameName);
		player.addMessageToRightBag(res);
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

}
