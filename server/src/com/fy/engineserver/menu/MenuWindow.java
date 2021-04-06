package com.fy.engineserver.menu;

import com.fy.engineserver.menu.cave.Option_Cave_CloseDoor;
import com.fy.engineserver.menu.cave.Option_Cave_OptionDoor;


/**
 * 
 * 定义客户端显示的一个窗口
 * 
 * 
 * 
 */
public class MenuWindow {

	// 窗口的Id
	int id = -1;

	// 窗口的标题
	String title;

	// 窗口的描述，UUB格式的
	String descriptionInUUB;

	// 窗口对应的NPCId，可以为-1
	long npcId;

	// 是否要显示NPC身上的任务
	boolean showTask = true;

	// 窗口中的选项
	Option options[] = new Option[0];

	// 指定高度 0为默认 超过屏幕最大高度为屏幕最大高度
	int height = 0;

	// 指定宽度 0为默认 超过屏幕最大宽度为屏幕最大宽度
	int width = 0;

	// 窗口执行指令时 是否关闭掉自己
	boolean destroy = true;

	// 内部变量，不会传送给客户端，转为临时窗口设置
	long invalidTime = 0;
	// png图片数据
	byte[] png = new byte[0];

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescriptionInUUB() {
		return descriptionInUUB;
	}

	public void setDescriptionInUUB(String descriptionInUUB) {
		if(!descriptionInUUB.toLowerCase().contains(LoadWindows.compareStr.toLowerCase())){
			descriptionInUUB = LoadWindows.startContentStr + descriptionInUUB + LoadWindows.endStr;
		}
		this.descriptionInUUB = descriptionInUUB;
	}

	public long getNpcId() {
		return npcId;
	}

	public void setNpcId(long npcId) {
		this.npcId = npcId;
	}

	public boolean isShowTask() {
		return showTask;
	}

	public void setShowTask(boolean showTask) {
		this.showTask = showTask;
	}

	public Option[] getOptions() {
		return options;
	}

	public void setOptions(Option[] options) {
		if (options == null) {
			this.options = options;
			return;
		}
		if(getNpcId()>0){
			for (Option option : options) {
				if (option.getText() != null ) {
					if (!(option instanceof Option_Cave_OptionDoor || option instanceof Option_Cave_CloseDoor)) {
						if (!option.getText().toLowerCase().contains(LoadWindows.compareStr.toLowerCase())) {
							option.setText(LoadWindows.startStr + option.getText() + LoadWindows.endStr);
						}
					}
				}
			}
		}
		this.options = options;
	}

	public void insertFirst(Option option) {
		Option[] temp = new Option[options.length + 1];
		temp[0] = option;
		System.arraycopy(options, 0, temp, 1, options.length);
		options = temp;
	}

	// public void add(Option option) {
	// Option[] temp = new Option[options.length + 1];
	// temp[temp.length - 1] = option;
	// System.arraycopy(options, 0, temp, 0, options.length - 1);
	// options = temp;
	// }

	/**
	 * 为某个玩家copy一个窗口出来
	 * 
	 * @param p
	 * @return
	 */
	// public MenuWindow copy(Player p){
	// MenuWindow mw = new MenuWindow();
	// mw.id = id;
	// mw.showTask = showTask;
	// mw.npcId = npcId;
	// mw.title = title;
	// mw.descriptionInUUB = descriptionInUUB;
	// mw.options = new Option[options.length];
	// for(int i = 0 ; i < mw.options.length ; i++){
	// mw.options[i] = options[i].copy(p);
	//
	// }
	// return mw;
	// }

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isDestroy() {
		return destroy;
	}

	public void setDestroy(boolean destroy) {
		this.destroy = destroy;
	}

	public byte[] getPng() {
		return png;
	}

	public void setPng(byte[] png) {
		this.png = png;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		MenuWindow menuWindow = new MenuWindow();
		menuWindow.setDescriptionInUUB(descriptionInUUB);
		menuWindow.setDestroy(destroy);
		menuWindow.setHeight(height);
		menuWindow.setId(id);
		menuWindow.setNpcId(npcId);
		menuWindow.setPng(png);
		menuWindow.setShowTask(showTask);
		menuWindow.setTitle(title);
		menuWindow.setWidth(width);
		Option[] newOptions = new Option[options.length];
		for (int i = 0; i < options.length; i++) {
			newOptions[i] = (Option) options[i].clone();
		}
		menuWindow.setOptions(newOptions);
		return menuWindow;
	}
}
