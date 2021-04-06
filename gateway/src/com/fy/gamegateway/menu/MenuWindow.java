package com.fy.gamegateway.menu;


public class MenuWindow {

	private int winId;
	
	// 窗口的标题
	private String title;

	// 窗口的描述，UUB格式的
	private String descriptionInUUB;
	
	// 窗口中的选项
	private Option options[] = new Option[0];
	
	// png图片数据
	private byte[] png = new byte[0];
	
	//销毁时间
	private long destoryTime;

	public void setWinId(int winId) {
		this.winId = winId;
	}

	public int getWinId() {
		return winId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescriptionInUUB(String descriptionInUUB) {
		this.descriptionInUUB = descriptionInUUB;
	}

	public String getDescriptionInUUB() {
		return descriptionInUUB;
	}

	public void setOptions(Option options[]) {
		this.options = options;
	}

	public Option[] getOptions() {
		return options;
	}

	public void setPng(byte[] png) {
		this.png = png;
	}

	public byte[] getPng() {
		return png;
	}

	public void setDestoryTime(long destoryTime) {
		this.destoryTime = destoryTime;
	}

	public long getDestoryTime() {
		return destoryTime;
	}
}
