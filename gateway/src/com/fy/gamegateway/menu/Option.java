package com.fy.gamegateway.menu;

import com.xuanzhi.tools.transport.Connection;

public class Option {

	/**
	 * 选项对应为服务器的某种功能，当玩家选择此选项的时候，
	 * 需要客户端发送指令给服务器，并且关闭窗口
	 */
	public static final byte OPTION_TYPE_SERVER_FUNCTION = 1;

	/**
	 * 选项对应的是服务器的响应，响应是显示下一个窗口
	 * 当玩家选择此选项的时候，需要客户端发送指令给服务器，
	 * 并且等待服务器的显示下一个窗口的响应
	 */
	public static final byte OPTION_TYPR_WAITTING_NEXT_WINDOW = 2;

	/**
	 * 选项对应的是客户端的某个功能,当玩家选择此选项的时候，
	 * 需要客户端关闭窗口，并且启动对应的功能。
	 * 
	 * 比如：单独修理装备，比如强化某件装备
	 */
	public static final byte OPTION_TYPR_CLIENT_FUNCTION = 3;

	/**
	 * 选项唯一标识，内部使用，有工具自己随机产生
	 */
	protected String optionId;

	/**
	 * 空 标识没有图片
	 */
	private String iconId = "";

	/**
	 * 显示的文字
	 */
	private String text = "";

	/**
	 * 显示文字的颜色
	 */
	private int color;

	/**
	 * 选项的Type
	 * 
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时：
	 * oId标识的服务器功能的Id
	 * 当oType = OPTION_TYPR_WAITTING_NEXT_WINDOW时：
	 * oId标识的是要打开的窗口Id
	 * 当oType = OPTION_TYPR_CLIENT_FUNCTION时
	 * oId标识的客户端功能的Id
	 */
	private byte oType = OPTION_TYPE_SERVER_FUNCTION;

	/**
	 * oType 为客户端功能呢的时候
	 * 		0 确定，关闭窗口
	 * */
	private int oId;

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Connection conn, String[] input) {
	}

	protected void copy(Option op) {
		op.color = color;
		op.iconId = iconId;
		op.oId = getOId();
		op.oType = getOType();
		op.text = getText();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public byte getOType() {
		return oType;
	}

	public void setOType(byte type) {
		oType = type;
	}

	public int getOId() {
		return oId;
	}

	public void setOId(int id) {
		oId = id;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public void setIconId(String iconId) {
		this.iconId = iconId;
	}

	public String getIconId() {
		return iconId;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
