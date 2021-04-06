package com.fy.engineserver.datasource.article.data.props;

/**
 * 地图限制，包括:地图名字，地图上的横纵坐标点，范围半径
 * @author Administrator
 *
 */
public class GameMapLimit {

	private String hint;
	
	/**
	 * 地图的名字
	 */
	private String gameMapName;

	/**
	 * 地图的横坐标
	 */
	private int x;

	/**
	 * 地图的纵坐标
	 */
	private int y;

	/**
	 * 区域半径值
	 */
	private int redius;

	
	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getGameMapName() {
		return gameMapName;
	}

	public void setGameMapName(String gameMapName) {
		this.gameMapName = gameMapName;
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

	public int getRedius() {
		return redius;
	}

	public void setRedius(int redius) {
		this.redius = redius;
	}

}
