package com.fy.engineserver.activity.base;

import java.util.ArrayList;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_FireRate;
import com.fy.engineserver.sprite.Player;

/**
 * 只有蓝色以上的酒
 *
 */
public class ExpAddHeJiu extends ExpAddAbstract {

	private int jiuColorType;
	
	public ExpAddHeJiu(long id, long startTime, long endTime, int addType, int addParameter, int jiuColorType) {
		super(id, startTime, endTime, ExperienceManager.ADDEXP_REASON_FIREACTIVITY, addType, addParameter);
		this.jiuColorType = jiuColorType;
	}

	@Override
	public boolean canAdd(Player player, int addReason, Object...is) {
		if (addReason == this.addReason){
			ArrayList<Buff> buffs = player.getBuffs();
			for (int i = 0; i <buffs.size(); i++) {
				if (buffs.get(i) instanceof Buff_FireRate) {
					if (buffs.get(i).getLevel()%5 >= jiuColorType) {
						return true;
					}
					return false;
				}
			}
		}
		return false;
	}
	public void setJiuColorType(int jiuColorType) {
		this.jiuColorType = jiuColorType;
	}
	public int getJiuColorType() {
		return jiuColorType;
	}

	@Override
	public String getParmeter() {
		return "喝酒" + "color=" + jiuColorType;
	}

}
