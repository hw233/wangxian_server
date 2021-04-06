package com.fy.engineserver.activity.base;

import java.util.ArrayList;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_FireRate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tengxun.TengXunDataManager;

/**
 * 只有蓝色以上的酒的腾讯魔钻活动
 *
 */
public class ExpAddTengXunHeJiu extends ExpAddAbstract {

	private int jiuColorType;
	
	public ExpAddTengXunHeJiu(long id, long startTime, long endTime, int addType, int addParameter, int jiuColorType) {
		super(id, startTime, endTime, ExperienceManager.ADDEXP_REASON_FIREACTIVITY, addType, addParameter);
		this.jiuColorType = jiuColorType;
	}

	//魔钻用户经验活动
	@Override
	public boolean canAdd(Player player, int addReason, Object...is) {
		if (TengXunDataManager.instance.getGameLevel(player.getId()) > 0 && addReason == this.addReason){
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
		return "魔钻喝酒" + "color=" + jiuColorType;
	}

}
