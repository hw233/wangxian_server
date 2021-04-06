package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_Jiazu_salary_ceremony_confirm extends Option {
	private int x;
	private int y;
	private int mapid;
	
	public Option_Jiazu_salary_ceremony_confirm()
	{
		
	}
	@Override
	public int getOId() {
		// TODO Auto-generated method stub
		return OptionConstants.SERVER_FUNCTION_CONFIRM_SALARY_CEREMONY_JIAZU;
	}

	@Override
	public String getOptionId() {
		// TODO Auto-generated method stub
		return super.getOptionId();
	}

	@Override
	public byte getOType() {
		// TODO Auto-generated method stub
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public void doSelect(Game game, Player player) {
		//TODO 调转到指定的位置
	}
}
