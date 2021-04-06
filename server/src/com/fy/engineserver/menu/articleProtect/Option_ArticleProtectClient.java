package com.fy.engineserver.menu.articleProtect;

import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;

public class Option_ArticleProtectClient extends Option {

	public Option_ArticleProtectClient() {};
	
	public byte getOType() {
		return Option.OPTION_TYPR_CLIENT_FUNCTION;
	}
	
	@Override
	public int getOId() {
		return OptionConstants.CLIENT_FUNCTION_ARTICLEPROTECT;
	}
}
