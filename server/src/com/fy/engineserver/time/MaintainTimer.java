package com.fy.engineserver.time;


import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 维护型计时器
 * 剔除服务器的维护时间
 * 
 *
 */
@SimpleEmbeddable
public class MaintainTimer extends Timer{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offline() {
		// TODO Auto-generated method stub
		setOfflineTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void online() {
		// TODO Auto-generated method stub
		SystemMaintainManager smm = SystemMaintainManager.getInstance();
		if(nowState == open && offlineTime < smm.getSystemStartTime()){
			open();
			long 维护时间 = 0;
			for(long i = smm.maxId; i >= 0; i--){
				SystemMaintain sm = smm.systemMaintainMap.get(i);
				if(sm != null){
					//这里为了防止玩家在系统维护后下线，即系统的停机时间比玩家下线时间小，采用
					if(sm.systemStopTime + 120000 >= offlineTime){
						维护时间 += sm.systemStartTime - sm.systemStopTime;
					}else{
						break;
					}
				}
			}
			setOfflineTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			setEndTime(endTime + 维护时间);
		}
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
