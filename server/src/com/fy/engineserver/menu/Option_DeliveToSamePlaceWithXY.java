package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.newtask.NewDeliverTaskManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

/**
 * 传送到某地
 * 
 * 
 *
 */
public class Option_DeliveToSamePlaceWithXY extends Option implements NeedCheckPurview{

	//目地地
	String destinationMapName = "";
	private int targetX;
	private int targetY;
	/** -1为不需要完成任务就可以传送 */
	private long taskId = -1;
	
	String destinationAreaName = Translate.出生点;
	
	
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if (!checkTask(player)) {
			return ;
		}
		Game currentGame = player.getCurrentGame();
		if (currentGame != null) {
			if (targetX != 0 && targetY != 0) {
				TransportData td = new TransportData(0,0,0,0,destinationMapName.trim(),targetX,targetY);
				currentGame.transferGame(player, td);
			} else {
				GameManager gm = GameManager.getInstance();
				GameInfo gi = null;
				if(destinationMapName != null){
					gi = gm.getGameInfo(destinationMapName.trim());
				}

				MapArea area = null;

				if(gi != null && destinationAreaName != null){
					area = gi.getMapAreaByName(destinationAreaName.trim());
				}
				try{
					TransportData td = new TransportData(0,0,0,0,destinationMapName.trim(),(int)(area.getX() + Math.random()*area.getWidth()),
							(int)(area.getY() + Math.random()*area.getHeight()));
					game.transferGame(player, td);
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	/** 检测任务是否完成 */
	public boolean checkTask(Player player) {
		if (getTaskId() <= 0) {
			return true;
		}
		try {
			Task task = TaskManager.getInstance().getTask(getTaskId());
			boolean isDeliver = player.inDeliver(task);
			TaskSubSystem.logger.warn("[测试任务是否完成] ["+player.getLogString()+"] [是否交付:"+isDeliver+"] [taskName:"+task.getName()+"]");
			return isDeliver;
		} catch (Exception e) {
			return true;
		}
	}
	

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_传送到某地;
	}

	public void setOId(int oid) {
	}
	
	
	public String getDestinationMapName() {
		return destinationMapName;
	}

	public void setDestinationMapName(String destinationMapName) {
		this.destinationMapName = destinationMapName;
	}


	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		return checkTask(player);
	}
	
	public int getTargetY() {
		return targetY;
	}
	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}
	public int getTargetX() {
		return targetX;
	}
	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}




}
