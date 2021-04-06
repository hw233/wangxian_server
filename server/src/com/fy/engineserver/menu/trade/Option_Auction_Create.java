package com.fy.engineserver.menu.trade;

import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;

//拍卖行
public class Option_Auction_Create extends Option {

	public Option_Auction_Create(){
		super();
	}
	
	public byte getOType() {
		return Option.OPTION_TYPR_CLIENT_FUNCTION;
	}
	
	public int getOId() {
		return OptionConstants.CLIENT_FUNCTION_AUCTION;
	}
}
