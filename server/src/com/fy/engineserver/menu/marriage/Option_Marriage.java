package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;

/**
 * 点击NPC结婚菜单
 * 
 *
 */
public class Option_Marriage extends Option{

	public Option_Marriage(){
		super();
	}
	
	public byte getOType() {
		return Option.OPTION_TYPR_CLIENT_FUNCTION;
	}
	
	public int getOId() {
		return OptionConstants.CLIENT_FUNCTION_MARRIAGE;
	}
}
