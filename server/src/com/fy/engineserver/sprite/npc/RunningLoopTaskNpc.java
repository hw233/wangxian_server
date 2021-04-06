package com.fy.engineserver.sprite.npc;

import java.util.Random;

/**
 * 跑环NPC
 * 
 *
 */
public class RunningLoopTaskNpc extends NPC implements Cloneable{

	private static Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	/**
	 * 
	 */
	private static final long serialVersionUID = 8140356567487195176L;

	protected String runningLoopTaskName;

	
	public String getRunningLoopTaskName() {
		return runningLoopTaskName;
	}


	public void setRunningLoopTaskName(String runningLoopTaskName) {
		this.runningLoopTaskName = runningLoopTaskName;
	}


	/**
	 * 为某个玩家创建一个跑环NPC的窗口
	 * 
	 * @param player
	 * @return
	 */
//	public MenuWindow generateMenuWindow(Game game,Player player){
//		
//		
//		WindowManager wm = WindowManager.getInstance();
//		TaskManager tm = TaskManager.getInstance();
//		RuningLoopTask rlt = tm.getRuningLoopTaskByName(runningLoopTaskName);
//		MenuWindow mw = wm.createTempMenuWindow(600);
//		mw.setTitle("" + runningLoopTaskName);
//		if(rlt == null){
//			mw.setDescriptionInUUB(Translate.translate.text_5762+runningLoopTaskName+Translate.translate.text_1852);
//			
//			Option_Cancel op = new Option_Cancel();
//			op.setColor(0xffffff);
//			op.setText(Translate.translate.text_5373);
//			op.setIconId("172");
//			mw.setOptions(new Option[]{op});
//		}else{
//			mw.setDescriptionInUUB(rlt.getDescription());
//			
//			int currentLn = rlt.getCurrentLoopNum(player);
//			if(currentLn != -1){
//				
//				Option_Cancel op = new Option_Cancel();
//				op.setColor(0xffffff);
//				op.setText(Translate.translate.text_5763+currentLn+Translate.translate.text_5764);
//				op.setIconId("172");
//				mw.setOptions(new Option[]{op});
//			}else {
//				int k = rlt.getPrepareLoopNum(player);
//				if(k == -1){
//					Option_Cancel op = new Option_Cancel();
//					op.setColor(0xffffff);
//					op.setText(Translate.translate.text_5765);
//					op.setIconId("172");
//					mw.setOptions(new Option[]{op});
//				}else if(k == -2){
//					Option_Cancel op = new Option_Cancel();
//					op.setColor(0xffffff);
//					op.setText(Translate.translate.text_5766);
//					op.setIconId("172");
//					mw.setOptions(new Option[]{op});
//				}else if(k == -3){
//					Option_Cancel op = new Option_Cancel();
//					op.setColor(0xffffff);
//					op.setText(Translate.translate.text_5767);
//					op.setIconId("172");
//					mw.setOptions(new Option[]{op});
//				}else if(k == -4){
//					Option_Cancel op = new Option_Cancel();
//					op.setColor(0xffffff);
//					op.setText(Translate.translate.text_5768);
//					op.setIconId("172");
//					mw.setOptions(new Option[]{op});
//				}else{
//					Task task = rlt.randomTaskForGivenLoopNum(player,k);
//				
//					if(task != null){
//						Option_RunningLoopTaskForTake op2 = new Option_RunningLoopTaskForTake();
//						op2.setTask(task);
//						op2.setColor(0xffffff);
//						op2.setText(Translate.translate.text_5769+k+Translate.translate.text_5770+task.getName());
//						op2.setIconId("74");
//						
//						mw.setOptions(new Option[]{op2});
//						
//					}else{
//						Option_Cancel op = new Option_Cancel();
//						op.setColor(0xffffff);
//						op.setText(Translate.translate.text_5771);
//						op.setIconId("172");
//						mw.setOptions(new Option[]{op});
//					}
//				}
//			}
//		}
//
//		
//		return mw;
//	}
	
	/**
	 * clone出一个对象
	 */
	public Object clone() {
		RunningLoopTaskNpc p = new RunningLoopTaskNpc();
		p.cloneAllInitNumericalProperty(this);
		
		p.country = country;
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		
		p.runningLoopTaskName = runningLoopTaskName;
		p.windowId = windowId;
		p.id = nextId();
		return p;
	}
}
