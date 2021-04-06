package com.fy.engineserver.util;

import static com.fy.engineserver.util.CommonSystem.*;

public enum CommonSubSystem {
	总在线(在线控制),
	新手村在线(在线控制),
	预创建角色开始时间(系统),
	预创建角色结束时间(系统),
	预创建角色国家人数设定(系统), ;

	private CommonSystem commonSystem;

	private CommonSubSystem(CommonSystem commonSystem) {
		this.commonSystem = commonSystem;
	}

	public CommonSystem getCommonSystem() {
		return commonSystem;
	}

	public void setCommonSystem(CommonSystem commonSystem) {
		this.commonSystem = commonSystem;
	}

	@Override
	public String toString() {
		return "[" + commonSystem.toString() + "-" + super.toString() + "]";
	}

}
