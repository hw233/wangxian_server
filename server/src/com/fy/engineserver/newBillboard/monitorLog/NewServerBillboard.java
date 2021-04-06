package com.fy.engineserver.newBillboard.monitorLog;

import java.io.Serializable;

public class NewServerBillboard implements Serializable{
	private String menu;
	private String subMenu;

	/** 角色名,如果是家族排行,这里就是家族名称 */
	private String name;

	/** 角色账号,如果是家族,这里不写,用中划线代替 */
	private String userName = "-";

	/** 角色id,如果是家族排行,就是家族id */
	private Long id;

	public NewServerBillboard(){
	}
	public NewServerBillboard(String menu, String subMenu, String name, String userName, Long id) {
		this.menu = menu;
		this.subMenu = subMenu;
		this.name = name;
		this.userName = userName;
		this.id = id;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(String subMenu) {
		this.subMenu = subMenu;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
