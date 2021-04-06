package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskConfig.TargetType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 任务目标：获得BUFF<BR/>
 * 此类目标只在乎buff的名字。<BR/>
 * 对颜色不关注<BR/>
 * 
 * 
 */
public class TaskTargetOfBuff extends TaskTarget {

	public TaskTargetOfBuff(String buffName) {
		setTargetType(TargetType.BUFF);
		setTargetByteType(getTargetType().getIndex());
		setTargetName(new String[] { buffName });
		setTargetColor(-1);
		setTargetNum(1);
	}

	@Override
	public CompoundReturn dealOnGet(Player player, Task task) {
		Buff buff = player.getBuffByName(getTargetName()[0]);
		return CompoundReturn.createCompoundReturn().setBooleanValue(buff != null).setIntValue(1);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		return super.dealAction(action);
	}
}
